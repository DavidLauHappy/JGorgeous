package common.core;

import java.io.File;
import java.util.Map;

import resource.Context;
import resource.SecurityCenter;

import utils.FileUtils;
import utils.StringUtil;

import model.LOCALNODE;
import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;

public class LnxFileUploadAction  extends  Action{
	private LOCALCOMMANDBean cmd;
	public LnxFileUploadAction(LOCALCOMMANDBean cmd){
		this.cmd=cmd;
	}
	
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
	
	public boolean execute(){
		  try{
			   	 Map<String,String> parameters=cmd.getParameters();
			   	 String sourcePath=parameters.get("SRC_PATH");
				 String targetPath=parameters.get("TARGET_PATH");
				  			targetPath=FileUtils.formatPath(targetPath);
				  			targetPath=targetPath.replace(File.separatorChar, '/');
				 String nodeID=parameters.get("NODE_ID");
				 LOCALNODEBean node=LOCALNODE.getNode(nodeID);
				 String ip=node.getIp();//parameters.get("IP");
				 String detail="文件"+sourcePath+"上传到节点["+ip+"]"+targetPath;
				 int portNo=22;
				  if(!StringUtil.isNullOrEmpty(node.getSftpPort())){
					  portNo=Integer.parseInt(node.getSftpPort());
				  }
				  String sftpuser=node.getSftpUser();
				  String passwd=node.getSftpPasswd();
				  passwd=SecurityCenter.getInstance().decrypt(passwd, Context.EncryptKey);
				  LnxSftp sftp=new  LnxSftp(ip,sftpuser,passwd,portNo);
				  boolean result=sftp.upLoadFile(sourcePath, targetPath);
				  if(result){
					  this.resultInfo=detail+"成功完成";
					  return true;
				  }else{
					  this.resultInfo=detail+"执行异常："+sftp.getOutMsg();
					  return false;
				  }
		  }catch(Exception e){
			   this.resultInfo=e.toString();
			   return  false;
		   }
	}

}
