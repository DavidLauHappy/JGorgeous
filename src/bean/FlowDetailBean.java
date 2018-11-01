package bean;

import model.FLOWDETAIL;

public class FlowDetailBean {
	 private String id;
	 private String flowID;
	 private String flowName;
	 private String stepID;
	 private String stepName;
	 private String applyUserID;
	 private String applyUserName;
	 private String applyTime;
	 private String status;
	 private String curUserID;
	 private String curUserName;
	 private String apprData;
	 private String processTime;
	 private String appComment;
	 private String locked="0";
	 /////////////////////////////////////////////////////
	 private String bussiObj="";//业务对象
	 private String url="";//显示业务对象
	 private String actionName="";
	 private String notUrl="";
	 private String notActionName="";
	 private String pageUrl="";
	 ////////////////////////////////////////
	 public enum Locked{No,Yes;}
	 public enum Status{Init,Progress,Sucessed,Failed;}
	public FlowDetailBean(){
		
	}
	 
	public FlowDetailBean(String id, String flowID, String flowName,
			String stepID, String stepName, String applyTime, String status,
			String curUserID, String curUserName) {
		super();
		this.id = id;
		this.flowID = flowID;
		this.flowName = flowName;
		this.stepID = stepID;
		this.stepName = stepName;
		this.applyTime = applyTime;
		this.status = status;
		this.curUserID = curUserID;
		this.curUserName = curUserName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlowID() {
		return flowID;
	}

	public void setFlowID(String flowID) {
		this.flowID = flowID;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurUserID() {
		return curUserID;
	}

	public void setCurUserID(String curUserID) {
		this.curUserID = curUserID;
	}

	public String getCurUserName() {
		return curUserName;
	}

	public void setCurUserName(String curUserName) {
		this.curUserName = curUserName;
	}

	public String getApplyUserID() {
		return applyUserID;
	}

	public void setApplyUserID(String applyUserID) {
		this.applyUserID = applyUserID;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getApprData() {
		return apprData;
	}

	public void setApprData(String apprData) {
		this.apprData = apprData;
	}

	public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

	public String getAppComment() {
		return appComment;
	}

	public void setAppComment(String appComment) {
		this.appComment = appComment;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String userID,String locked) {
		this.locked = locked;
		FLOWDETAIL.setLocked(userID, this.id, this.locked);
	}
	 
	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getBussiObj() {
		return bussiObj;
	}

	public void setBussiObj(String bussiObj) {
		this.bussiObj = bussiObj;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getNotUrl() {
		return notUrl;
	}

	public void setNotUrl(String notUrl) {
		this.notUrl = notUrl;
	}

	public String getNotActionName() {
		return notActionName;
	}

	public void setNotActionName(String notActionName) {
		this.notActionName = notActionName;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	 
	
}
