package business.deploy.action;

import java.io.File;
import java.util.Map;

import common.core.LnxSftp;
import common.core.WinShell;

import model.SYSTEM;

import resource.Context;
import resource.SecurityCenter;
import utils.FileUtils;
import utils.StringUtil;

import bean.COMMANDBean;
import bean.NODEBean;
import bean.NODEDIRBean;
import bean.SYSTEMBean;
import business.deploy.bean.TransData;

public class UploadPKGAction extends LocalAction {

	private String resultInfo="";
	private String result="";
	public String getResultInfo() {
		return resultInfo;
	}

	public String getResult() {
		return result;
	}

	public String callbackMsg="";
	
	public boolean execute() {
		COMMANDBean.Status status=null;
		TransData tansLoger=new TransData();
		SYSTEMBean system= SYSTEM.getSystemsByID(this.node.getSystemID(), Context.session.currentFlag);
		tansLoger.setId(this.cmd.getId());
		tansLoger.setApp(system.getApp());
		tansLoger.setSystemID(system.getBussID());
		tansLoger.setNodeID(this.node.getId());
		tansLoger.setFuncID("F-999-00006");
		boolean ret=false;
        try{
			 Map<String,String> parameters=this.cmd.getParameters();
			  String sourcePath=parameters.get("SRC_PATH");
			  String targetPath=parameters.get("TARGET_PATH");
			  targetPath=FileUtils.formatPath(targetPath);
			  //ip地址封装在报文中，节点调整ip后 无法同步更新报文，ip取节点的最准确
			  String ip=this.node.getIp();//parameters.get("IP");
			  String detail="在["+ip+"]上执行版本包上传[将版本包:"+sourcePath+"上传到:"+targetPath+"]";
			  tansLoger.setDetail(detail);
			  tansLoger.inroll();
			  String upDir="";
			  //直接根据平台走不同的渠道
			  String os=node.getOs();
			  if((NODEBean.OS.Linux.ordinal()+"").equals(os)){
				  targetPath=targetPath.replace(File.separatorChar, '/');
				  int portNo=22;
				  if(!StringUtil.isNullOrEmpty(node.getSftpPort())){
					  portNo=Integer.parseInt(node.getSftpPort());
				  }
				  String sftpuser=node.getSftpUser();
				  String passwd=node.getSftpPasswd();
				  passwd=SecurityCenter.getInstance().decrypt(passwd, Context.EncryptKey);
				  
				  LnxSftp sftp=new  LnxSftp(ip,sftpuser,passwd,portNo);
				  Map<String,NODEDIRBean> dirs=node.getDirs();
				   if(dirs!=null){
					    for(String dirName:dirs.keySet())
					    	sftp.addComDir(dirName);
				   }
				   ret=sftp.upLoadFile(sourcePath, targetPath);
				   upDir=targetPath;
					  if(ret){
						  this.result=LocalAction.Status.ReturnOK.ordinal()+"";
						  this.resultInfo="成功上传版本包到["+node.getName()+"]的"+upDir+"]";
						  status=COMMANDBean.Status.ReturnOK;
						  ret=true;
					  }else{
						  this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
						  this.resultInfo="上传版本包到["+node.getName()+"]的"+upDir+"发生异常，"+this.callbackMsg;
						  status=COMMANDBean.Status.ReturnFailed;
					  }
			  }
			  else{
				  WinShell winShell=WinShell.getShell(ip);
				  if(winShell!=null){
					   upDir=WinShell.DEFAULT_PATH;
					   Map<String,NODEDIRBean> dirs=node.getDirs();
					   if(dirs!=null){
						    for(String dirName:dirs.keySet())
						    	winShell.addComDir(dirName);
					   }
					  ret= winShell.sendDir(sourcePath, upDir); 
					  String response=winShell.getResponse();
			    		int index=response.indexOf("|");
			   		 	String xmlStr=response.substring(index+1);
			   		 	Map<String,String> retMap=StringUtil.parseXML(xmlStr);
			   		 	String outMsg=retMap.get("OUT_MSG");
			   		 	String outCode=retMap.get("OUT_CODE");
					  if(!ret){
						  this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
						  this.resultInfo="上传版本包到["+node.getName()+"]的"+upDir+"发生异常，"+outMsg;
						  status=COMMANDBean.Status.ReturnFailed;
					   }else{
						   if("0".equals(outCode)){
							   this.result=LocalAction.Status.ReturnOK.ordinal()+"";
								this.resultInfo="成功上传版本包到["+node.getName()+"]"+outMsg;
								status=COMMANDBean.Status.ReturnOK;
								ret=true;
						   }else{
							   this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
							   this.resultInfo="上传版本包到["+node.getName()+"]的"+upDir+"发生异常，"+outMsg;
							   status=COMMANDBean.Status.ReturnFailed;
						   }
					   }
				  }else{
					  this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
					  this.resultInfo="上传版本包到["+node.getName()+"]的"+upDir+"发生异常:"+"请检查机器["+ip+"]上VService服务是否正常部署或正常启动";
					  status=COMMANDBean.Status.ReturnFailed;
				  }
			  }
        }
        catch(Exception exp){
        	this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
			this.resultInfo=exp.toString();
			status=COMMANDBean.Status.ReturnFailed;
			ret=false;
        }
        tansLoger.setEndTime();
        this.cmd.setExecuteInfo(this.result, this.resultInfo, COMMANDBean.Remind.No.ordinal()+"");
 		return ret;
	}

	private String fileID;
	public String getFileID() {
		return fileID;
	}

	public String odlMD5="";
	public String getInstallFileMd5() {
		return odlMD5;
	}
	private COMMANDBean cmd;
	private NODEBean node;
	public UploadPKGAction(COMMANDBean data,NODEBean bean){
		this.cmd=data;
		this.node=bean;
	}

	public String getCallbackMsg() {
		return callbackMsg;
	}

	public void setCallbackMsg(String callbackMsg) {
		this.callbackMsg = callbackMsg;
	}
	
	
}
