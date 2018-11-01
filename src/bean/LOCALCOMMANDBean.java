package bean;

import java.util.Map;

import utils.StringUtil;
import model.LOCALCOMMAND;

public class LOCALCOMMANDBean {
	
	public enum Status{Initial,//初始化状态
        Runnable,//可运行状态
        Scheduling,//调度中(受理完毕)
        Running,//执行中(发送完毕)
        TimeOut,//超时（发送指令时）
        ReturnOK,//执行成功
        ReturnNull,//执行无返回
        ReturnFailed,//执行失败
		Scheduled;//调度完成到队列到发送完成这段时间不能重复调度，需要一个中间状态
                    }
	 public enum Remind{No,Ok,Yes;}
	private String versionID;
	private String fileID;
	private String nodeID;
	private String cmdID;
	private String cmdName;
	private String cmdText;
	private String seq;
	private String cmdType;
	private String status;
	private String loginfo;
	private String remind;
	private String time;
	private String userID;
	////////////////////////
	private String lpath=" ";
	private String installed=" ";
	private String md5=" ";
	private String remote=" ";
	
	public String getVersionID() {
		return versionID;
	}
	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}
	public String getFileID() {
		return fileID;
	}
	public void setFileID(String fileID) {
		this.fileID = fileID;
	}
	public String getNodeID() {
		return nodeID;
	}
	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}
	public String getCmdID() {
		return cmdID;
	}
	public void setCmdID(String cmdID) {
		this.cmdID = cmdID;
	}
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	public String getCmdText() {
		return cmdText;
	}
	public void setCmdText(String cmdText) {
		this.cmdText = cmdText;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getCmdType() {
		return cmdType;
	}
	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void resetStatus(String status){
		this.status=status;
		LOCALCOMMAND.setStatus(this.cmdID, this.status);
	}
	public String getLoginfo() {
		return loginfo;
	}
	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}
	public String getRemind() {
		return remind;
	}
	public void setRemind(String remind) {
		this.remind = remind;
	}
	public void reSetRemind(String remind) {
		this.remind = remind;
		LOCALCOMMAND.setRemind(cmdID, this.remind);
	}
	
	public void setRetunInfo(String status,String log,String remind){
		this.status=status;
		this.loginfo=log;
		this.remind=remind;
		LOCALCOMMAND.setRemote(this.cmdID, this.status, this.loginfo, this.remind);
	}
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public void add(){
		LOCALCOMMAND.add(this);
	}
	public String getLpath() {
		return lpath;
	}
	public void setLpath(String lpath) {
		this.lpath = lpath;
	}
	public String getInstalled() {
		if(StringUtil.isNullOrEmpty(this.installed))
			this.installed="NULL";
		return installed;
	}
	public void setInstalled(String installed) {
		this.installed = installed;
	}
	
	public void reSetInstalled(String installed){
		this.installed=installed;
		LOCALCOMMAND.setInstalled(this.cmdID, this.installed);
	}
	public String getMd5() {
		if(StringUtil.isNullOrEmpty(this.md5))
			this.md5="NULL";
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getRemote() {
		return remote;
	}
	public void setRemote(String remote) {
		this.remote = remote;
	}
	
	 public Map<String,String> getParameters(){
    	 int index=this.cmdText.indexOf("|");
		 String xmlStr=this.cmdText.substring(index+1);
		 return StringUtil.parseXML(xmlStr);
    }
	
}
