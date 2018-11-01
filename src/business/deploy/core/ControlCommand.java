package business.deploy.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import common.core.Protocol;
import common.core.WinShell;
import common.db.DataHelper;

import model.COMMAND;
import model.NODE;
import model.PFILE;
import model.STEP;

import resource.ComOrder;
import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Paths;
import utils.DateUtil;
import utils.FileUtils;
import utils.StringUtil;
import bean.COMMANDBean;
import bean.NODEBean;
import bean.NODEDIRBean;
import bean.NODESERVICEBean;
import bean.PFILEBean;
import bean.STEPBean;
import business.deploy.bean.StrategyData;

/**
 * @author ���� E-mail:
 * @date 2016-8-8
 * @version 1.0
 * ��˵��
 */
public class ControlCommand {

	public static synchronized  String getStart(String ip,String versionID,String nodeID){ 
		String result="";
		String cmdCount=COMMAND.getCount(versionID, nodeID, COMMANDBean.Status.Runnable.ordinal()+"");
		String cmd=Protocol.START_CONTROL_CMD;
		cmd=cmd.replace("@VERSION_ID", versionID);
		cmd=cmd.replace("@NODE_ID", nodeID);
		cmd=cmd.replace("@CMD_SUM", cmdCount);
		cmd=cmd.replace("@TIME", DateUtil.getCurrentTime());
		 result=Protocol.Start+"|"+ cmd;
		return result;
	}
	
	public static synchronized  String getBarrier(String ip,String versionID,String nodeID){ 
		String result="";
		String cmdCount=COMMAND.getCount(versionID, nodeID, COMMANDBean.Status.Running.ordinal()+"");
		String cmd=Protocol.BARRIER_CONTROL_CMD;
		cmd=cmd.replace("@VERSION_ID", versionID);
		cmd=cmd.replace("@NODE_ID", nodeID);
		cmd=cmd.replace("@CMD_SUM", cmdCount);
		cmd=cmd.replace("@TIME", DateUtil.getCurrentTime());
		 result=Protocol.Barrier+"|"+ cmd;
		return result;
	}
	
	public static synchronized  String getEnd(String ip,String versionID,String nodeID){ 
		String result="";
		String cmd=Protocol.END_CONTROL_CMD;
		cmd=cmd.replace("@VERSION_ID", versionID);
		cmd=cmd.replace("@NODE_ID", nodeID);
		cmd=cmd.replace("@TIME", DateUtil.getCurrentTime());
		 result=Protocol.End+"|"+ cmd;
		return result;
	}
	
	public static synchronized  String getBreakStep(String msgID,String mode,String versionID,String nodeID){ 
		String result="";
		String cmd=Protocol.RESEND_CONTROL_CMD;
		cmd=cmd.replace("@MSGID", msgID);
		cmd=cmd.replace("@NODE_ID", nodeID);
		cmd=cmd.replace("@MODE", mode);
		 result=Protocol.Resend+"|"+ cmd;
		return result;
	}
	
	public static synchronized  String getQuit(){ 
		String result="";
		String cmd=Protocol.QUIT_CONTROL_CMD;
		 cmd=cmd.replace("@TIME", DateUtil.getCurrentTime());
		 result=Protocol.Quit+"|"+ cmd;
		return result;
	}
	
	public static String getNodeBasePath(NODEBean node){
		 String path="";
		 String os=node.getOs();
		 if((NODEBean.OS.Linux.ordinal()+"").equals(os)){
			 path=node.getSftpDir();
			 if(StringUtil.isNullOrEmpty(path)){
				  path=Constants.defaultSftpDir;
			 }
		 }
		 else {
			 path=WinShell.DEFAULT_PATH;
		 }
		 return path;
	}
	
	public static synchronized void  command2Db(String versionID,String systemID,String approved,COMMANDBean.Flag Switch){
		ComOrder Order=new ComOrder();
		COMMAND.setFlag(versionID, systemID, COMMANDBean.Flag.Off.ordinal()+"",  Context.session.userID);
		List<StrategyData> datas=DataHelper.getStrategeCmdData(versionID,systemID,"1");
		if(datas!=null&&datas.size()>0){
			List<String> cmdNodes=new ArrayList<String>();
			for(StrategyData data:datas){
				String nodeID=data.getNodeID();
				if(!cmdNodes.contains(nodeID)){
					cmdNodes.add(nodeID);
				}
			}
			//ɾ��ָ��-����ָ��
			if(cmdNodes!=null&&cmdNodes.size()>0){
				for(String node:cmdNodes){
					COMMAND.delete(versionID, node);
				}
			}
		}
		if(datas!=null&&datas.size()>0){
			for(StrategyData data:datas){
				String pkgID=data.getVersion();
				String nodeID=data.getNodeID();
				String stepID=data.getStepID();
				STEPBean step=STEP.getPkgSteps(pkgID,stepID);
				String action=step.getAction();
				NODEBean node=NODE.getNodeByID( Context.session.userID, nodeID, Context.session.currentFlag);
				if(node!=null){
				     //�汾�ϴ�
					if(action.equals(STEPBean.ActionType.UploadPkg.ordinal()+"")){
						pkgUploadCommand(node,step,approved,pkgID,Order,Switch);
					}//Ŀ¼����
					else if(action.equals(STEPBean.ActionType.BackDir.ordinal()+"")){
						backDirCommand(node,step,approved,Order,Switch);
					}//ȡ�����µ��ļ���װ
					else	if(action.equals(STEPBean.ActionType.FileCopy.ordinal()+"")){
						fileCopyCommand(node,step,approved,Order,Switch);
					}//���пⱸ��
					else if(action.equals(STEPBean.ActionType.BackRun.ordinal()+"")){
						backDbCommand(node,step,approved,Order,Switch);
					}//ȡ�����µĽű���װ
					else if(action.equals(STEPBean.ActionType.ScriptInstall.ordinal()+"")){
						scriptExeCommand(node,step,approved,Order,Switch);
					}//��������ָ��
					else if(action.equals(STEPBean.ActionType.ServiceStart.ordinal()+"")){
						serviceControlCommand(node,step,COMMANDBean.ServiceStatus.Start,approved,Order,Switch);
					}//������ָֹ��
					else if(action.equals(STEPBean.ActionType.ServiceStop.ordinal()+"")){
						serviceControlCommand(node,step,COMMANDBean.ServiceStatus.Stop,approved,Order,Switch);
					}
				}
			}
		}
	}
	
	private  static synchronized void pkgUploadCommand(NODEBean node,STEPBean step,String approved,String versionID,ComOrder Order,COMMANDBean.Flag Switch){
		 if(node!=null&&step!=null){
			 String seqNum=Order.getOrder();
			 String id=UUID.randomUUID().toString();
			 String name="�ڽڵ�["+node.getName()+"]��ִ��["+step.getDesc()+"]";
			 String parameters=Protocol.PKG_UPLOAD_CMD;//�汾�ϴ�ָ��
		    		parameters=parameters.replace("@IP", node.getIp());
		    		parameters=parameters.replace("@VERSION_ID", versionID);
		    		parameters=parameters.replace("@NODE_ID", node.getId());
		    		parameters=parameters.replace("@STEP_ID", step.getId());
		    		parameters=parameters.replace("@MSGID", id);
		    		parameters=parameters.replace("@CRT_TIME", DateUtil.getCurrentTime());
		    		parameters=parameters.replace("@SEQ", seqNum);
		    		//�ϴ��汾�ı���Ŀ¼��windows�Ĺ��߹���Ŀ¼
		    		String srcPath=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+step.getPkgID();
		    		parameters=parameters.replace("@SRC_PATH", srcPath); 
		    		//�汾�ϴ���Ŀ��Ŀ¼�޷�ȷ���������ϴ�����������ȷ��
	   		    	String uploadPath=ControlCommand.getNodeBasePath(node);
		    		parameters=parameters.replace("@TARGET_PATH", uploadPath); 
		    	    parameters=Protocol.UploadPKg+"|"+ parameters;
		    	    COMMANDBean cmd=new COMMANDBean();
		    	    cmd.setPkgID(versionID);
		    	    cmd.setStepID(step.getId());
		    	    cmd.setNodeID(node.getId());
		    	    cmd.setFileID("");
		    	    cmd.setId(id);
		    	    cmd.setName(name);
		    	    cmd.setParameter(parameters);
		    	    cmd.setSeq(seqNum);
		    	    cmd.setStatus(COMMANDBean.Status.Initial.ordinal()+"");
		    	    cmd.setFlag(Switch.ordinal()+"");
		    	    cmd.setRemote(COMMANDBean.Remote.No.ordinal()+"");
		    	    cmd.setRemind(COMMANDBean.Remind.No.ordinal()+"");
		    	    cmd.setApprID(approved);
		    	    cmd.setUserID( Context.session.userID);
   		            cmd.inroll();
   		            Order.inc();
		 }
	}
	
	private  static synchronized void backDirCommand(NODEBean node,STEPBean step,String approved,ComOrder Order,COMMANDBean.Flag Switch){
		 if(node!=null&&step!=null){
				 String seqNum=Order.getOrder();
				 String id=UUID.randomUUID().toString();
				 String name="�ڽڵ�["+node.getName()+"]��ִ��["+step.getDesc()+"]";
				    String parameters=Protocol.FILE_BACKUP_CMD;	 //�ļ������ϴ�ָ��
		    		parameters=parameters.replace("@IP", node.getIp());
		    		parameters=parameters.replace("@VERSION_ID", step.getPkgID());
		    		parameters=parameters.replace("@NODE_ID", node.getId());
		    		parameters=parameters.replace("@STEP_ID", step.getId());
		    		parameters=parameters.replace("@MSGID", id);
		    		parameters=parameters.replace("@CRT_TIME", DateUtil.getCurrentTime());
		    		parameters=parameters.replace("@SEQ", seqNum);
		    		
		    		//����ԴĿ¼��ַ
		    		String dirName=step.getName();//�������ƻ���·����
		    		dirName=StringUtil.ltrim(dirName, "/");
		    		String componentName=step.getComponentName();
		    		NODEDIRBean nodeDir=node.getDirs().get(componentName);
		    		String componentPaths=nodeDir.getDirValue();
		    		parameters=parameters.replace("@FILTER", nodeDir.getDirFilter());
		    		String sonDir=dirName.replace(componentName, "");
		    		sonDir=StringUtil.ltrim(sonDir, "/");
		    		sonDir=sonDir.replace("/", File.separator);
		    		sonDir=FileUtils.formatPath(sonDir);
		    		//����ʵ�ʵĸ�Ŀ¼+��װ��������
		    		String srcPath=componentPaths+File.separator+sonDir;
		    		parameters=parameters.replace("@SRC_PATH", srcPath);
		    		//����Ŀ���ַ
		    		String basePath=ControlCommand.getNodeBasePath(node);
		    		String targetPath=basePath+File.separator+"backup"+File.separator+node.getId()+File.separator+
		    						  step.getPkgID()+File.separator+FileUtils.formatPath(dirName);

	   		    	parameters=parameters.replace("@TARGET_PATH", targetPath);
		    		
		    	   String osVersion=node.getOs();
		    	   COMMANDBean.Remote remote=COMMANDBean.Remote.Yes;
		    	   if( (NODEBean.OS.Linux.ordinal()+"").equals(osVersion)){
		    		   remote=COMMANDBean.Remote.No;
		    		   parameters=Protocol.DeleteFile+"|"+ parameters;
		    	   }
		    	   else{
		    		   parameters=Protocol.FileCopy+"|"+ parameters;
		    	   }
	   		        COMMANDBean cmd=new COMMANDBean();
		    	    cmd.setPkgID(step.getPkgID());
		    	    cmd.setStepID(step.getId());
		    	    cmd.setNodeID(node.getId());
		    	    cmd.setFileID("");
		    	    cmd.setId(id);
		    	    cmd.setName(name);
		    	    cmd.setParameter(parameters);
		    	    cmd.setSeq(seqNum);
		    	    cmd.setStatus(COMMANDBean.Status.Initial.ordinal()+"");
		    	    cmd.setFlag(Switch.ordinal()+"");
		    	    cmd.setRemote(remote.ordinal()+"");
		    	    cmd.setRemind(COMMANDBean.Remind.No.ordinal()+"");
		    	    cmd.setApprID(approved);
		    	    cmd.setUserID( Context.session.userID);
			         cmd.inroll();
	   		         Order.inc();
		 }
	}
	

	//�нڵ�Ŀ¼δ����
	private  static synchronized void fileCopyCommand(NODEBean node,STEPBean step,String approved,ComOrder Order,COMMANDBean.Flag Switch){
       if(node!=null&&step!=null){
    	   List<PFILEBean> files=PFILE.getStepFiles(step.getPkgID(), step.getId());
			if(files!=null&&files.size()>0){
				for(PFILEBean file:files){
					String seqNum=Order.getOrder();
					 String id=UUID.randomUUID().toString();
					 String name="�ڽڵ�["+node.getName()+"]�ϰ�װ�ļ�["+file.getName()+"]";
					 String parameters= Protocol.FILE_COPY_CMD;
					 parameters=parameters.replace("@IP", node.getIp());
	       		  	 parameters=parameters.replace("@VERSION_ID", step.getPkgID());
			    	 parameters=parameters.replace("@NODE_ID", node.getId());
			    	 parameters=parameters.replace("@STEP_ID", step.getId());
	   		    	 parameters=parameters.replace("@MSGID", id);
	   		    	 parameters=parameters.replace("@CRT_TIME", DateUtil.getCurrentTime());
	   		    	 parameters=parameters.replace("@SEQ", seqNum);
	   		    	 parameters=parameters.replace("@FILE_ID", file.getId());
	   		    	 String relateDir=file.getDir();
	   		    	 relateDir=relateDir.replace('/', File.separatorChar);
	   		    	String dirName=step.getName();//�������ƻ���·����
	   		    	dirName=StringUtil.ltrim(dirName, "/");
		    		String componentName=step.getComponentName();
		    		NODEDIRBean nodeDir=node.getDirs().get(componentName);
		    		String componentPaths=nodeDir.getDirValue();
		    		String sonDir=dirName.replace(componentName, "");
		    		sonDir=StringUtil.ltrim(sonDir, "/");
		    		sonDir=sonDir.replace('/', File.separatorChar);
		    		sonDir=FileUtils.formatPath(sonDir);
		    		//����ʵ�ʵĸ�Ŀ¼+��װ��������
		    		String targetPath=componentPaths+File.separator+sonDir+relateDir;
		    		parameters=parameters.replace("@TARGET_PATH", targetPath);
	   		    	//��װ��Դ�ļ�
		    		String basePath=ControlCommand.getNodeBasePath(node);
		    		String srcPath=FileUtils.formatPath(basePath)+File.separator+step.getPkgID()+File.separator+FileUtils.formatPath(dirName)+relateDir+File.separator+file.getName();
		    		parameters=parameters.replace("@SRC_PATH", srcPath);
	   		    	//�ļ��������ǵ�Ч�ʣ�ͳһʹ��agent���ؿ����ķ�ʽ
	   		    	//����samba�ļ����ļ���Ļ�Ч�ʺ���
	   		    	//�˴��ļ�������·��ʹ�ýڵ��ϵ��ļ�����·��
	   		    	 parameters=Protocol.FileCopy+"|"+ parameters;
	   		    	 String osVersion=node.getOs();
	   		    	 COMMANDBean.Remote remote=COMMANDBean.Remote.Yes;
			    	   if((NODEBean.OS.Linux.ordinal()+"").equals(osVersion))
			    		   remote=COMMANDBean.Remote.No;
			     	    COMMANDBean cmd=new COMMANDBean();
			    	    cmd.setPkgID(step.getPkgID());
			    	    cmd.setStepID(step.getId());
			    	    cmd.setNodeID(node.getId());
			    	    cmd.setFileID(file.getId());
			    	    cmd.setId(id);
			    	    cmd.setName(name);
			    	    cmd.setParameter(parameters);
			    	    cmd.setSeq(seqNum);
			    	    cmd.setStatus(COMMANDBean.Status.Initial.ordinal()+"");
			    	    cmd.setFlag(Switch.ordinal()+"");
			    	    cmd.setRemote(remote.ordinal()+"");
			    	    cmd.setRemind(COMMANDBean.Remind.No.ordinal()+"");
			    	    cmd.setApprID(approved);
			    	    cmd.setUserID( Context.session.userID);
	   		            cmd.inroll();
	   		            Order.inc(); 
				}
			}
       }
	}
	
	private  static synchronized void backDbCommand(NODEBean node,STEPBean step,String approved,ComOrder Order,COMMANDBean.Flag Switch){
		 if(node!=null&&step!=null){
			 String seqNum=Order.getOrder();
					String id=UUID.randomUUID().toString();
					String name="�ڽڵ�["+node.getName()+"]��ִ��["+step.getDesc()+"]";
					String parameters=Protocol.DB_BACKUP_CMD;	 //���ݿⱸ��ָ��
		    		parameters=parameters.replace("@IP", node.getIp());
		    		parameters=parameters.replace("@VERSION_ID", step.getPkgID());
		    		parameters=parameters.replace("@NODE_ID", node.getId());
		    		parameters=parameters.replace("@STEP_ID", step.getId());
		    		parameters=parameters.replace("@MSGID", id);
		    		parameters=parameters.replace("@CRT_TIME", DateUtil.getCurrentTime());
		    		parameters=parameters.replace("@SEQ", seqNum);
		    		parameters=parameters.replace("@DB_NAME",  node.getDbname());
		    		parameters=parameters.replace("@DB_USER", node.getDbUser());
		    		parameters=parameters.replace("@DB_PASSWD", node.getDbPasswd());
		    		parameters=parameters.replace("@BACK_DB", node.getBackDBname());
		    		parameters=Protocol.SqlserverBackup+"|"+ parameters;
		   		  	   COMMANDBean cmd=new COMMANDBean();
			    	    cmd.setPkgID(step.getPkgID());
			    	    cmd.setStepID(step.getId());
			    	    cmd.setNodeID(node.getId());
			    	    cmd.setFileID("");
			    	    cmd.setId(id);
			    	    cmd.setName(name);
			    	    cmd.setParameter(parameters);
			    	    cmd.setSeq(seqNum);
			    	    cmd.setStatus(COMMANDBean.Status.Initial.ordinal()+"");
			    	    cmd.setFlag(Switch.ordinal()+"");
			    	    cmd.setRemote(COMMANDBean.Remote.No.ordinal()+"");
			    	    cmd.setRemind(COMMANDBean.Remind.No.ordinal()+"");
			    	    cmd.setApprID(approved);
			    	    cmd.setUserID( Context.session.userID);
				        cmd.inroll();
				        Order.inc(); 
		 }
	}

	private  static synchronized void scriptExeCommand(NODEBean node,STEPBean step,String approved,ComOrder Order,COMMANDBean.Flag Switch){
		   if(node!=null&&step!=null){
			   boolean hasBoot=false;
				List<PFILEBean>  files=PFILE.getStepFiles(step.getPkgID(), step.getId());
				if(files!=null&&files.size()>0){
					//���ݿ�ű��У�����������ļ�����ֻ�����������ű���ָ��
					for(PFILEBean file:files){
						if("1".equals(file.getBootfalg()))
							hasBoot=true;
					}
					for(PFILEBean file:files){
						if(!hasBoot||("1".equals(file.getBootfalg()))){
							String seqNum=Order.getOrder();
							 String id=UUID.randomUUID().toString();
							 String name="�ڽڵ�["+node.getName()+"]��ִ���ļ�["+file.getName()+"]";
							 String parameters= Protocol.SCRIPT_RUN_CMD;
							 parameters=parameters.replace("@IP", node.getIp());
		 	       		  	 parameters=parameters.replace("@VERSION_ID", step.getPkgID());
		 	   		    	 parameters=parameters.replace("@NODE_ID", node.getId());
		 	   		    	 parameters=parameters.replace("@STEP_ID", step.getId());
		       		    	 parameters=parameters.replace("@MSGID", id);
		       		    	 parameters=parameters.replace("@CRT_TIME", DateUtil.getCurrentTime());
		       		    	 parameters=parameters.replace("@SEQ", seqNum);
		       		    	 parameters=parameters.replace("@FILE_ID", file.getId());
		       		    	 parameters=parameters.replace("@FILE_NAME", file.getPath());
		       		    	 parameters=parameters.replace("@DB_NAME", file.getDbOwner());
		       		    	 parameters=parameters.replace("@DB_USER", node.getDbUser());
		       		    	 parameters=parameters.replace("@DB_PASSWD", node.getDbPasswd());
		       		    	 parameters=Protocol.ScriptExecute+"|"+ parameters;
		       		    	COMMANDBean cmd=new COMMANDBean();
				    	    cmd.setPkgID(step.getPkgID());
				    	    cmd.setStepID(step.getId());
				    	    cmd.setNodeID(node.getId());
				    	    cmd.setFileID(file.getId());
				    	    cmd.setId(id);
				    	    cmd.setName(name);
				    	    cmd.setParameter(parameters);
				    	    cmd.setSeq(seqNum);
				    	    cmd.setStatus(COMMANDBean.Status.Initial.ordinal()+"");
				    	    cmd.setFlag(Switch.ordinal()+"");
				    	    cmd.setRemote(COMMANDBean.Remote.No.ordinal()+"");
				    	    cmd.setRemind(COMMANDBean.Remind.No.ordinal()+"");
				    	    cmd.setApprID(approved);
				    	    cmd.setUserID( Context.session.userID);
		   		            cmd.inroll();
		   		            Order.inc(); 
						}
					}
				}
		   }
	}
	
	private  static synchronized void serviceControlCommand(NODEBean node,STEPBean step,COMMANDBean.ServiceStatus status,String approved,ComOrder Order,COMMANDBean.Flag Switch){
		 if(node!=null&&step!=null){
			 	 String id=UUID.randomUUID().toString();
			     String serverID=step.getName();//�����š�Ŀ¼����ڲ��������д洢
			     String serverName=Dictionary.getDictionaryMap("NODE_SERVICE.TYPE").get(serverID);
			     String parameters= Protocol.SERVICE_CONTROL_CMD;
			     String name="";
			     String mode="";
			     String path="";
			     String runPath="";
			     String seqNum=Order.getOrder();
			     NODESERVICEBean service=node.getServices().get(serverID);
			     if(status.equals(COMMANDBean.ServiceStatus.Stop)){
			    	 name="�ڽڵ�["+node.getName()+"]��ֹͣ["+serverName+"]";
			    	 mode="1";
			    	 if(service!=null){
			    		 path=service.getStop();
			    	 }
			     }else{
			    	 name="�ڽڵ�["+node.getName()+"]������["+serverName+"]";
			    	 mode="0";
			    	 if(service!=null){
			    		 path=service.getStart();
			    	 }
			     }
			     if(!StringUtil.isNullOrEmpty(path)){
				     int index=path.lastIndexOf(File.separatorChar);
				     if(index<0)
				    	 index=path.lastIndexOf("/");
				     if(index<0)
				    	 index=path.lastIndexOf("\\");
				     if(index>=0){
				         runPath=path.substring(0,index);
				     }else{
				    	 runPath=path;
				     }
			     }
   		  	 	parameters=parameters.replace("@ROUTINE_NAME",serverName);
   		  	 	parameters=parameters.replace("@MODE", mode);
   		  	 	parameters=parameters.replace("@PATH", path);
   		  	 	parameters=parameters.replace("@RUN_PATH", runPath);
		    	 parameters=parameters.replace("@TIME", DateUtil.getCurrentTime());
		    	 parameters=parameters.replace("@SEQ", seqNum);
		    	 parameters=parameters.replace("@IP", node.getIp());
	       		 parameters=parameters.replace("@VERSION_ID", step.getPkgID());
	   		     parameters=parameters.replace("@NODE_ID", node.getId());
	   		     parameters=parameters.replace("@STEP_ID", step.getId());
	   		     parameters=parameters.replace("@MSGID", id);
		    	 parameters=Protocol.ServiceControl+"|"+ parameters;
		    	 String osVersion=node.getOs();
		    	 COMMANDBean.Remote remote=COMMANDBean.Remote.Yes;
		    	   if((NODEBean.OS.Linux.ordinal()+"").equals(osVersion))
		    		   remote=COMMANDBean.Remote.No;
	   		    COMMANDBean cmd=new COMMANDBean();
	    	    cmd.setPkgID(step.getPkgID());
	    	    cmd.setStepID(step.getId());
	    	    cmd.setNodeID(node.getId());
	    	    cmd.setFileID("");
	    	    cmd.setId(id);
	    	    cmd.setName(name);
	    	    cmd.setParameter(parameters);
	    	    cmd.setSeq(seqNum);
	    	    cmd.setStatus(COMMANDBean.Status.Initial.ordinal()+"");
	    	    cmd.setFlag(Switch.ordinal()+"");
	    	    cmd.setRemote(remote.ordinal()+"");
	    	    cmd.setRemind(COMMANDBean.Remind.No.ordinal()+"");
	    	    cmd.setApprID(approved);
	    	    cmd.setUserID( Context.session.userID);
		        cmd.inroll();
		         Order.inc(); 
		 }
	}
}
