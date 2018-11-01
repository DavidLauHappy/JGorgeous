package business.deploy.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.core.LnxShell;
import common.core.WinShell;

import model.SYSTEM;
import resource.Context;
import resource.Logger;
import utils.FileUtils;
import utils.StringUtil;
import bean.COMMANDBean;
import bean.NODEBean;
import bean.SYSTEMBean;
import business.deploy.bean.TransData;

public class FileBackupAction extends LocalAction {
	private String resultInfo="";
	private String result="";
	public  String getResultInfo(){
		return resultInfo;
	}
	public  String getResult(){
		return result;
	}
	private String fileID;
	public  String getFileID(){
		return fileID;
	}
	public String installedMD5="";
	public String getInstallFileMd5(){
		return this.installedMD5;
	}
	
	public FileBackupAction(COMMANDBean data,NODEBean bean){
		this.cmd=data;
		this.node=bean;
	}
	
	private COMMANDBean cmd;
	private NODEBean node;
	public boolean execute(){
		   boolean result=false;
		   COMMANDBean.Status status;
		   TransData tansLoger=new TransData();
			SYSTEMBean system= SYSTEM.getSystemsByID(this.node.getSystemID(), Context.session.currentFlag);
			tansLoger.setId(this.cmd.getId());
			tansLoger.setApp(system.getApp());
			tansLoger.setSystemID(system.getBussID());
			tansLoger.setNodeID(this.node.getId());
			tansLoger.setFuncID("F-999-00003");
			LnxShell shell=null;
		   try{
			     if((NODEBean.OS.Linux.ordinal()+"").equals(node.getOs())){
				   	 Map<String,String> parameters=cmd.getParameters();
					 String versionID=parameters.get("VERSION_ID");
					 String source=parameters.get("SRC_PATH");//���Ŀ¼Ӧ���Ǿ���Ŀ¼��(�汾�ϴ�Ŀ¼�����ļ������Ŀ¼��
					 source=FileUtils.formatPath(source);
					 source=source.replace(File.separatorChar, '/');
					 String sourceDir=source.substring(0,source.lastIndexOf("/"));
					 String dirName=source.substring(source.lastIndexOf("/")+1);
					 String zipFile=dirName+".tgz";
					 String target=parameters.get("TARGET_PATH");
					 target=FileUtils.formatPath(target);
					 target=target.replace(File.separatorChar, '/');
					 String detail="��["+this.node.getIp()+"]��ִ���ļ�����[���ļ�:"+source+"��װ��:"+target+"]";
					 tansLoger.setDetail(detail);
					 tansLoger.inroll();
					 //����targetĿ¼��һ������ ��Ҫ����
					 String  cmdCtdir="mkdir -p"+" @dst";
					 cmdCtdir=cmdCtdir.replace("@dst", target);
					 
					 String cmdStr="tar -pzcf @zipFile -C @dir @component#filter;mv @zipFile @dst";//"cp -afpr "+"@src"+" "+"@dst";
					 if(parameters.containsKey("FILTER")&&!StringUtil.isNullOrEmpty(parameters.get("FILTER"))){
						 String filter=parameters.get("FILTER");
						 cmdStr=cmdStr.replace("#filter", " --exclude "+filter);
					 }else{
						 cmdStr=cmdStr.replace("#filter", " ");
					 }
					 cmdStr=cmdStr.replace("@src", source);
					 cmdStr=cmdStr.replace("@zipFile", zipFile);
					 cmdStr=cmdStr.replace("@dir", sourceDir);
					 cmdStr=cmdStr.replace("@component", dirName);
					 cmdStr=cmdStr.replace("@dst", target);
					 List<String> cmdLines=new ArrayList<String>();
					 cmdLines.add(cmdCtdir);//����Ŀ¼��ָ��
					 cmdLines.add(cmdStr);//������װ��ָ��
					 shell= LnxShell.getShell(this.node);
					 result=shell.executeCommand(cmdLines);
					 String pushStr=shell.getResponse();
					 if(result){
						    this.result=LocalAction.Status.ReturnOK.ordinal()+"";
							this.resultInfo="Ŀ¼["+source+"]�ɹ����ݵ�["+target+"]";
							status=COMMANDBean.Status.ReturnOK;
							result=true;
					 }else{
						 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
						 String  error=pushStr;
				    	 this.resultInfo="Ŀ¼["+source+"]���ݵ�["+target+"]ʧ�ܡ�ʧ��ԭ��"+error;
				    	 Logger.getInstance().error(error);
					 }
			     }else{
			    	 WinShell winShell=WinShell.getShell(node.getIp());
					    if(winShell!=null){
					    	boolean ret=winShell.executeCommand(cmd.getParameter(),true);
					    	if(ret){
					    		String response=winShell.getResponse();
					    		int index=response.indexOf("|");
					   		 	String xmlStr=response.substring(index+1);
					   		 	Map<String,String> retMap=StringUtil.parseXML(xmlStr);
					   		 	String outMsg=retMap.get("OUT_MSG");
					   		 	String outCode=retMap.get("OUT_CODE");
						   		 if("0".equals(outCode)){
						   			   this.result=LocalAction.Status.ReturnOK.ordinal()+"";
										this.resultInfo="Ŀ¼�ɹ����ݣ�"+outMsg;
										status=COMMANDBean.Status.ReturnOK;
										result=true;
						   		 	}else{
							   		 	 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
								    	 this.resultInfo="Ŀ¼����ʧ�ܡ�ʧ��ԭ��"+outMsg;
								    	 Logger.getInstance().error(outMsg);
								    	 status=COMMANDBean.Status.ReturnFailed;
								    	 result=false;
						   		 	}
					    	}else{
					    		 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
					    		 this.resultInfo="Ŀ¼����ʧ�ܡ�ʧ��ԭ��"+winShell.getResponse();
					    		 status=COMMANDBean.Status.ReturnFailed;
					    		 result=false;
					    	}
					    }else{
					    	 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
					    	 this.resultInfo="�������["+node.getIp()+"]��VService�����Ƿ������������������";
					    	 status=COMMANDBean.Status.ReturnFailed;
					    	 result=false;
					    }
			     }	 
		   }catch(Exception e){
			   this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
			   this.resultInfo=e.toString();
			   status=COMMANDBean.Status.ReturnFailed;
			   result=false;
		   }finally{
			  /* if(shell!=null){
				   shell.closeConnection(this.node.getId());
				   LnxShell.clearShell(this.node);
			   }*/
			   tansLoger.setEndTime();
			   this.cmd.setExecuteInfo(this.result, this.resultInfo, COMMANDBean.Remind.No.ordinal()+"");
		   }
		   return result;
	}
	
}
