package bean;

import java.io.File;

import model.VIEWFILE;

import resource.Context;
import resource.FileNameOrder;
import resource.FtpFileService;
import resource.Paths;
import utils.DateUtil;
import utils.FileUtils;
import utils.StringUtil;

/**
 * @author Administrator
 * 发布中的版本文件实体对象
 */
public class ViewFileBean {
	public enum Mode{Remote,Local;}
	public enum Status{Normal,Delete,Update,New,Rename;}
	private String fileID;
	private String fileName;
	private String fileTime;//文件自带的最后修改日期，该日期在未发生覆盖额情况下该日期属性会一直保持不变
	private String md5;
	private String viewID;
	private String streamID;
	private String crtUser;
	private String crtTime;//文件的上传时间和记录生成的时间
	private String mdfUser;
	private String mdfTime;//文件记录的修改时间
	private String versionID;
	//////////////////////////////
	private Integer orignalOrder;
	private Integer nameOrder;
	private String fileMode;//
	private String location;//文件的本地路径
	private String remotePath;//文件的远程路径
	private String status;//文件记录的状态标识
	private String type="0";//文件类型 0-版本文件 1-沟通纪要附件
	private String streamName;
	private String viewName;
	public void save(){
		VIEWFILE.add(this);
	}
	
	public void saveFileObj(){
		VIEWFILE.addObj(this.md5, this.remotePath, Context.session.userID);
	}
	
	public String getFileID() {
		return fileID;
	}
	public void setFileID(String fileID) {
		this.fileID = fileID;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
		this.setNameOrder();
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
	public String getViewID() {
		return viewID;
	}
	public void setViewID(String viewID) {
		this.viewID = viewID;
	}
	public String getStreamID() {
		return streamID;
	}
	public void setStreamID(String streamID) {
		this.streamID = streamID;
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
	public String getMdfUser() {
		return mdfUser;
	}
	public void setMdfUser(String mdfUser) {
		this.mdfUser = mdfUser;
	}
	public String getMdfTime() {
		return mdfTime;
	}
	public void setMdfTime(String mdfTime) {
		this.mdfTime = mdfTime;
	}
	public String getVersionID() {
		return versionID;
	}
	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}
	public Integer getOrignalOrder() {
		return orignalOrder;
	}
	public void setOrignalOrder(Integer orignalOrder) {
		this.orignalOrder = orignalOrder;
	}
	public Integer getNameOrder() {
		return nameOrder;
	}
	public void setNameOrder() {
		this.nameOrder =  FileNameOrder.getOrder(fileName);
	}
	public String getFileMode() {
		return fileMode;
	}
	public void setFileMode(String fileMode) {
		this.fileMode = fileMode;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRemotePath() {
		if(!StringUtil.isNullOrEmpty(this.remotePath))
			return remotePath;
		else{
			String remoteDir=Context.session.userID+File.separator+DateUtil.getCurrentDate()+File.separator+this.viewID+File.separator+this.versionID;
			 this.remotePath=remoteDir;
			 return remotePath;
		}
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean md5Exist(String md5){
		return VIEWFILE.md5Exists(md5);
	}
	
	public void remove(){
		VIEWFILE.remove(this.fileID, this.viewID, this.versionID);
	}
	
   //文件下载到本地目录(中文目录)
	public boolean download(){
		String path=Paths.getInstance().getWorkDir();
		//String localDir=FileUtils.formatPath(path)+File.separator+this.getStreamID()+File.separator+this.getViewID()+File.separator+this.getVersionID();
		String localDir=FileUtils.formatPath(path)+File.separator+this.getStreamName()+File.separator+this.getViewName()+File.separator+this.getVersionID();
		File dir=new File(localDir);
        if(!dir.exists()){
      	  dir.mkdirs();
       }
        boolean downResult=FtpFileService.getService().dowload(this.getFileName(), remotePath, localDir,this.getFileTime()); 
        if(downResult){
	    	this.location=localDir+File.separator+this.getFileName();
	    }
	    return downResult;
	}
	
	public boolean downloadStream(){
		String path=Paths.getInstance().getWorkDir();
		//String localDir=FileUtils.formatPath(path)+File.separator+this.getStreamID()+File.separator+this.getViewID()+File.separator+this.getVersionID();
		String localDir=FileUtils.formatPath(path)+File.separator+this.getStreamName();
		File dir=new File(localDir);
        if(!dir.exists()){
      	  dir.mkdirs();
       }
        boolean downResult=FtpFileService.getService().dowload(this.getFileName(), remotePath, localDir,this.getFileTime()); 
        if(downResult){
	    	this.location=localDir+File.separator+this.getFileName();
	    }
	    return downResult;
	}
	
	 //文件下载到本地目录(中文目录)
		public boolean downloadAtttach(){
			String path=Paths.getInstance().getWorkDir();
			//String localDir=FileUtils.formatPath(path)+File.separator+this.getStreamID()+File.separator+this.getViewID()+File.separator+this.getVersionID();
			String localDir=FileUtils.formatPath(path)+File.separator+this.getStreamName()+File.separator+this.getViewName();
			File dir=new File(localDir);
	        if(!dir.exists()){
	      	  dir.mkdirs();
	       }
	        boolean downResult=FtpFileService.getService().dowload(this.getFileName(), remotePath, localDir,this.getFileTime()); 
	        if(downResult){
		    	this.location=localDir+File.separator+this.getFileName();
		    }
		    return downResult;
		}
	
	private String fileShowName;//文件内容有变化时，对文件名进行标星
	public String getShowFileName() {
		if(StringUtil.isNullOrEmpty(fileShowName)){
			return this.fileName;
		}else{
			return fileShowName;
		}
	}
	public void setShowFileName(String showFileName) {
		this.fileShowName = showFileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	
}
