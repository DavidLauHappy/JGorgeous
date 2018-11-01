package bean;

import utils.StringUtil;

public class ViewVersionBean {
	private String viewID;
	private String viewName;
	private String viewDesc;
	private String version;
	private String showVersion;
	private String streamID;
	private String uptUser;
	private String uptTime;
	
	public String getViewID() {
		return viewID;
	}
	public void setViewID(String viewID) {
		this.viewID = viewID;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getViewDesc() {
		return viewDesc;
	}
	public void setViewDesc(String viewDesc) {
		this.viewDesc = viewDesc;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
		this.showVersion="v"+StringUtil.leftpad(this.version, 4, "0");
	}
	public String getShowVersion() {
		return showVersion;
	}
	
	public String getStreamID() {
		return streamID;
	}
	public void setStreamID(String streamID) {
		this.streamID = streamID;
	}
	public String getUptUser() {
		return uptUser;
	}
	public void setUptUser(String uptUser) {
		this.uptUser = uptUser;
	}
	public String getUptTime() {
		return uptTime;
	}
	public void setUptTime(String uptTime) {
		this.uptTime = uptTime;
	}
	
	
}
