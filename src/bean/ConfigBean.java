package bean;

public class ConfigBean {
	private String app;
	private String envType;
	private String fileName;
	private String paramName;
	private String paramVal;
	private String crtUser;
	private String mdfUser;
	private String mdfTime;
	
	public ConfigBean(){
		
	}
	
	
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getEnvType() {
		return envType;
	}
	public void setEnvType(String envType) {
		this.envType = envType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamVal() {
		return paramVal;
	}
	public void setParamVal(String paramVal) {
		this.paramVal = paramVal;
	}
	public String getCrtUser() {
		return crtUser;
	}
	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
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
	
	
}
