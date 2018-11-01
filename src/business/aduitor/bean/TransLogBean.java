package business.aduitor.bean;

public class TransLogBean {
	private String id;//
	private String app;
	private String appName;//
	private String systemName;//
	private String ip;
	private String targetNodeDesc;//
	private String operID;
	private String operDesc;//
	private String userTerminal;//
	private String funcID;
	private String funcName;//
	private String detail;//
	private String duration;//
	
	public TransLogBean(){
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getTargetNodeDesc() {
		return targetNodeDesc;
	}
	public void setTargetNodeDesc(String targetNodeDesc) {
		this.targetNodeDesc = targetNodeDesc;
	}
	public String getOperID() {
		return operID;
	}
	public void setOperID(String operID) {
		this.operID = operID;
	}
	public String getOperDesc() {
		return operDesc;
	}
	public void setOperDesc(String operDesc) {
		String shortDesc=operDesc.replace(" ", "");
		this.operDesc = shortDesc;
	}
	public String getUserTerminal() {
		return userTerminal;
	}
	public void setUserTerminal(String userTerminal) {
		this.userTerminal = userTerminal;
	}
	public String getFuncID() {
		return funcID;
	}
	public void setFuncID(String funcID) {
		this.funcID = funcID;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	
	
	
}
