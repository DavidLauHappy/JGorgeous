package bean;

public class FlowStepBean {
	
	public enum ApprType{User,Role,Group;}
	private String flowID;
	private String stepID;
	private String name;
	private String appUserID;
	private String appUserType;
	private String nextID;
	
	//使用过程中绑定的审批流程对象
	private String apprID;
	private String nextStepName;
	private String apprStatus;
	private String locked;
	private String submitUser;
	private String submitTime;
	private String bussObj;
	private String Url;
	private String actionName;
	private String notUrl;
	private String notActionName;
	////步骤对象
	
	
	public FlowStepBean(){
		
	}

	public String getFlowID() {
		return flowID;
	}

	public void setFlowID(String flowID) {
		this.flowID = flowID;
	}

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppUserID() {
		return appUserID;
	}

	public void setAppUserID(String appUserID) {
		this.appUserID = appUserID;
	}

	public String getAppUserType() {
		return appUserType;
	}

	public void setAppUserType(String appUserType) {
		this.appUserType = appUserType;
	}

	public String getNextID() {
		return nextID;
	}

	public void setNextID(String nextID) {
		this.nextID = nextID;
	}

	public String getApprID() {
		return apprID;
	}

	public void setApprID(String apprID) {
		this.apprID = apprID;
	}

	public String getNextStepName() {
		return nextStepName;
	}

	public void setNextStepName(String nextStepName) {
		this.nextStepName = nextStepName;
	}

	public String getApprStatus() {
		return apprStatus;
	}

	public void setApprStatus(String apprStatus) {
		this.apprStatus = apprStatus;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getBussObj() {
		return bussObj;
	}

	public void setBussObj(String bussObj) {
		this.bussObj = bussObj;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
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
	
	
	
}
