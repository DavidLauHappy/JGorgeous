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
 * @author 作者 E-mail:
 * @date 2016-8-8
 * @version 1.0
 * 类说明
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
			//删除指令-生成指令
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
				     //版本上传
					if(action.equals(STEPBean.ActionType.UploadPkg.ordinal()+"")){
						pkgUploadCommand(node,step,approved,pkgID,Order,Switch);
					}//目录备份
					else if(action.equals(STEPBean.ActionType.BackDir.ordinal()+"")){
						backDirCommand(node,step,approved,Order,Switch);
					}//取步骤下的文件安装
					else	if(action.equals(STEPBean.ActionType.FileCopy.ordinal()+"")){
						fileCopyCommand(node,step,approved,Order,Switch);
					}//运行库备份
					else if(action.equals(STEPBean.ActionType.BackRun.ordinal()+"")){
						backDbCommand(node,step,approved,Order,Switch);
					}//取步骤下的脚本安装
					else if(action.equals(STEPBean.ActionType.ScriptInstall.ordinal()+"")){
						scriptExeCommand(node,step,approved,Order,Switch);
					}//服务启动指令
					else if(action.equals(STEPBean.ActionType.ServiceStart.ordinal()+"")){
						serviceControlCommand(node,step,COMMANDBean.ServiceStatus.Start,approved,Order,Switch);
					}//服务终止指令
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
			 String name="在节点["+node.getName()+"]上执行["+step.getDesc()+"]";
			 String parameters=Protocol.PKG_UPLOAD_CMD;//版本上传指令
		    		parameters=parameters.replace("@IP", node.getIp());
		    		parameters=parameters.replace("@VERSION_ID", versionID);
		    		parameters=parameters.replace("@NODE_ID", node.getId());
		    		parameters=parameters.replace("@STEP_ID", step.getId());
		    		parameters=parameters.replace("@MSGID", id);
		    		parameters=parameters.replace("@CRT_TIME", DateUtil.getCurrentTime());
		    		parameters=parameters.replace("@SEQ", seqNum);
		    		//上传版本的本地目录是windows的工具工作目录
		    		String srcPath=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+step.getPkgID();
		    		parameters=parameters.replace("@SRC_PATH", srcPath); 
		    		//版本上传的目标目录无法确定，交给上传构件来具体确定
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
				 String name="在节点["+node.getName()+"]上执行["+step.getDesc()+"]";
				    String parameters=Protocol.FILE_BACKUP_CMD;	 //文件备份上传指令
		    		parameters=parameters.replace("@IP", node.getIp());
		    		parameters=parameters.replace("@VERSION_ID", step.getPkgID());
		    		parameters=parameters.replace("@NODE_ID", node.getId());
		    		parameters=parameters.replace("@STEP_ID", step.getId());
		    		parameters=parameters.replace("@MSGID", id);
		    		parameters=parameters.replace("@CRT_TIME", DateUtil.getCurrentTime());
		    		parameters=parameters.replace("@SEQ", seqNum);
		    		
		    		//备份源目录地址
		    		String dirName=step.getName();//步骤名称或者路径端
		    		dirName=StringUtil.ltrim(dirName, "/");
		    		String componentName=step.getComponentName();
		    		NODEDIRBean nodeDir=node.getDirs().get(componentName);
		    		String componentPaths=nodeDir.getDirValue();
		    		parameters=parameters.replace("@FILTER", nodeDir.getDirFilter());
		    		String sonDir=dirName.replace(componentName, "");
		    		sonDir=StringUtil.ltrim(sonDir, "/");
		    		sonDir=sonDir.replace("/", File.separator);
		    		sonDir=FileUtils.formatPath(sonDir);
		    		//构件实际的根目录+安装构件名称
		    		String srcPath=componentPaths+File.separator+sonDir;
		    		parameters=parameters.replace("@SRC_PATH", srcPath);
		    		//备份目标地址
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
	

	//有节点目录未处理
	private  static synchronized void fileCopyCommand(NODEBean node,STEPBean step,String approved,ComOrder Order,COMMANDBean.Flag Switch){
       if(node!=null&&step!=null){
    	   List<PFILEBean> files=PFILE.getStepFiles(step.getPkgID(), step.getId());
			if(files!=null&&files.size()>0){
				for(PFILEBean file:files){
					String seqNum=Order.getOrder();
					 String id=UUID.randomUUID().toString();
					 String name="在节点["+node.getName()+"]上安装文件["+file.getName()+"]";
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
	   		    	String dirName=step.getName();//步骤名称或者路径端
	   		    	dirName=StringUtil.ltrim(dirName, "/");
		    		String componentName=step.getComponentName();
		    		NODEDIRBean nodeDir=node.getDirs().get(componentName);
		    		String componentPaths=nodeDir.getDirValue();
		    		String sonDir=dirName.replace(componentName, "");
		    		sonDir=StringUtil.ltrim(sonDir, "/");
		    		sonDir=sonDir.replace('/', File.separatorChar);
		    		sonDir=FileUtils.formatPath(sonDir);
		    		//构件实际的根目录+安装构件名称
		    		String targetPath=componentPaths+File.separator+sonDir+relateDir;
		    		parameters=parameters.replace("@TARGET_PATH", targetPath);
	   		    	//安装的源文件
		    		String basePath=ControlCommand.getNodeBasePath(node);
		    		String srcPath=FileUtils.formatPath(basePath)+File.separator+step.getPkgID()+File.separator+FileUtils.formatPath(dirName)+relateDir+File.separator+file.getName();
		    		parameters=parameters.replace("@SRC_PATH", srcPath);
	   		    	//文件拷贝考虑到效率，统一使用agent本地拷贝的方式
	   		    	//拷贝samba文件，文件多的话效率很慢
	   		    	//此处文件拷贝的路径使用节点上的文件绝对路径
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
					String name="在节点["+node.getName()+"]上执行["+step.getDesc()+"]";
					String parameters=Protocol.DB_BACKUP_CMD;	 //数据库备份指令
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
					//数据库脚本中，如果有引导文件，则只用生成引导脚本的指令
					for(PFILEBean file:files){
						if("1".equals(file.getBootfalg()))
							hasBoot=true;
					}
					for(PFILEBean file:files){
						if(!hasBoot||("1".equals(file.getBootfalg()))){
							String seqNum=Order.getOrder();
							 String id=UUID.randomUUID().toString();
							 String name="在节点["+node.getName()+"]上执行文件["+file.getName()+"]";
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
			     String serverID=step.getName();//服务编号、目录编号在步骤名称中存储
			     String serverName=Dictionary.getDictionaryMap("NODE_SERVICE.TYPE").get(serverID);
			     String parameters= Protocol.SERVICE_CONTROL_CMD;
			     String name="";
			     String mode="";
			     String path="";
			     String runPath="";
			     String seqNum=Order.getOrder();
			     NODESERVICEBean service=node.getServices().get(serverID);
			     if(status.equals(COMMANDBean.ServiceStatus.Stop)){
			    	 name="在节点["+node.getName()+"]上停止["+serverName+"]";
			    	 mode="1";
			    	 if(service!=null){
			    		 path=service.getStop();
			    	 }
			     }else{
			    	 name="在节点["+node.getName()+"]上启动["+serverName+"]";
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
