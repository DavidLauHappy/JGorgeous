package bean;

import java.io.File;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.MessageBox;

import resource.Constants;
import resource.Context;
import resource.FtpFileService;
import resource.Paths;

import model.QUESTIONFILE;
import model.QUESTIONS;
import utils.FileUtils;
import utils.StringUtil;
import views.AppView;

public class QUESTIONFILEBean {
	private String qid="";
	private String fileId="";
	private String fileName="";
	private String fileTime="";
	private String md5="";
	private String crtUser="";
	private String crtTime="";
	public enum Type{Test,
								  Develper;
	}
	private String qfileType="";
	//控制属性
	public enum Status{Local,//upload before save
								  Remote,//download before open
								  downlaoded,
								  uploaded;
	}
	private String status="";
	private String localPath="";
	//下载的远程属性 userID/qid/
	private String remotePath="";
	public String getQid() {
		if(StringUtil.isNullOrEmpty(qid))
			qid=QUESTIONS.getNo();
		return qid;
	}
	public void setQid(String qid) {
		this.qid = qid;
	}
	public String getFileId() {
		if(StringUtil.isNullOrEmpty(this.fileId))
			this.fileId=UUID.randomUUID().toString();
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileTime() {
		return fileTime;
	}
	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getCrtUser() {
		return crtUser;
	}
	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}
	public String getCrtTime() {
		return crtTime;
	}
	public void setCrtTime(String crtTime) {
		this.crtTime = crtTime;
	}
	public String getQfileType() {
		return qfileType;
	}
	public void setQfileType(String qfileType) {
		this.qfileType = qfileType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	
	public String getRemotePath() {
		return remotePath;
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	
	public void open(){
		if(StringUtil.isNullOrEmpty(this.localPath)){
			String path=Paths.getInstance().getWorkDir();
			String localDir=FileUtils.formatPath(path)+File.separator+this.qid;
			File dir=new File(localDir);
	        if(!dir.exists()){
	      	  dir.mkdirs();
	       }
	       boolean dresult= FtpFileService.getService().dowload(this.fileName, this.remotePath, localDir, this.fileTime);
	       if(dresult){
	    	   this.localPath=localDir+File.separator+this.fileName;
	    	   this.setStatus(Status.downlaoded.ordinal()+"");
	       }else{
	    	    String alterMsg="文件["+this.fileName+"]下载失败!请稍后重试。";
				MessageBox msgBox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
				msgBox.setText(Constants.getStringVaule("messagebox_alert"));
				msgBox.setMessage(alterMsg);
				msgBox.open();
				return;
	       }
		}
		int dot = this.localPath.lastIndexOf('.');
 	    if (dot != -1) {
 	     String extension = this.localPath.substring(dot);
 	     Program program = Program.findProgram(extension);
 	     if (program != null){
 	    	 boolean ret=program.launch(this.localPath);
 	      }
 	   }
	}
	
	public void save(){
		//上传文件
		if(StringUtil.isNullOrEmpty(this.remotePath)){
			this.remotePath=Context.session.userID+File.separator+this.qid;
			boolean ret=FtpFileService.getService().upLoad(localPath, this.remotePath);
			if(ret){ //保存记录
				this.setStatus(Status.uploaded.ordinal()+"");
				QUESTIONFILE.getAdd(this);
			}else{
				String alterMsg="文件["+this.fileName+"]提交失败!请稍后重试。";
				MessageBox msgBox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
				msgBox.setText(Constants.getStringVaule("messagebox_alert"));
				msgBox.setMessage(alterMsg);
				msgBox.open();
			}
		}
	}
	
	public int delete(){
	  return QUESTIONFILE.getDelete(this);
	}
}
