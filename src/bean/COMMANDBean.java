package bean;

import java.util.Map;

import utils.StringUtil;

import model.COMMAND;

public class COMMANDBean {
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
	 public enum Flag{Off,On;}//步骤打开或者屏蔽的开关
	 public enum Remote{No,Yes;} 
	 public enum Remind{No,Ok,Yes;}
	 public enum ServiceStatus{Start,Stop;}
	private String pkgID;
	private String stepID;
	private String fileID;
	private String nodeID;
	private String id;
	private String name;
	private String parameter;
	private String seq;
	private String status;
	private String flag;
	private String remote;
	private String crtTime;
	private String logInfo;
	private String retTime;
	private String remind;
	private String apprID;
	private String userID;
	private String mdfTime;
	
	public COMMANDBean(){}
	
	public COMMANDBean(String pkgID, String stepID, String fileID,
			String nodeID, String id, String name, String parameter,
			String seq, String status, String flag, String remote,
			String crtTime, String logInfo, String retTime, String remind,
			String apprID, String userID, String mdfTime) {
		super();
		this.pkgID = pkgID;
		this.stepID = stepID;
		this.fileID = fileID;
		this.nodeID = nodeID;
		this.id = id;
		this.name = name;
		this.parameter = parameter;
		this.seq = seq;
		this.status = status;
		this.flag = flag;
		this.remote = remote;
		this.crtTime = crtTime;
		this.logInfo = logInfo;
		this.retTime = retTime;
		this.remind = remind;
		this.apprID = apprID;
		this.userID = userID;
		this.mdfTime = mdfTime;
	}
	
	public void inroll(){
		COMMAND.add(this);
	}

	public String getPkgID() {
		return pkgID;
	}

	public void setPkgID(String pkgID) {
		this.pkgID = pkgID;
	}

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		COMMAND.updateStatus(this.id, this.status);
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getCrtTime() {
		return crtTime;
	}

	public void setCrtTime(String crtTime) {
		this.crtTime = crtTime;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public String getRetTime() {
		return retTime;
	}

	public void setRetTime(String retTime) {
		this.retTime = retTime;
	}

	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
		COMMAND.updateRemind(this.id, this.remind);
	}

	public String getApprID() {
		return apprID;
	}

	public void setApprID(String apprID) {
		this.apprID = apprID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getMdfTime() {
		return mdfTime;
	}

	public void setMdfTime(String mdfTime) {
		this.mdfTime = mdfTime;
	}
	//指令的协议类型
	 public String getCmdType(){
		 String cmdType=this.parameter.substring(0, this.parameter.indexOf("|"));
		 return cmdType;
    }
	
	 //将指令报文解析成key-value对
	 public Map<String,String> getParameters(){
    	 int index=this.parameter.indexOf("|");
		 String xmlStr=this.parameter.substring(index+1);
		 return StringUtil.parseXML(xmlStr);
    }
	//指令在构件执行完成后的集合更新，避免多次数据库操作
	 public void setExecuteInfo(String result,String resultInfo,String remind){
		 this.status=result;
		 this.setLogInfo(resultInfo);
		 this.remind=remind;
		 COMMAND.updateExecuteInfo(this.id,this.status, this.logInfo, this.remind);
	 }

}
