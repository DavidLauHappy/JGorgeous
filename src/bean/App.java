package bean;

import model.APPSYSTEM;

public class App {
	private String app;
	private String system;
	private String name;
	private String flag;
	private String timespan;
	private String mdfuser;
	private String mdfTime;
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getTimespan() {
		return timespan;
	}
	public void setTimespan(String timespan) {
		this.timespan = timespan;
	}
	public String getMdfuser() {
		return mdfuser;
	}
	public void setMdfuser(String mdfuser) {
		this.mdfuser = mdfuser;
	}
	public String getMdfTime() {
		return mdfTime;
	}
	public void setMdfTime(String mdfTime) {
		this.mdfTime = mdfTime;
	}
	
	public void update(String timeSpan,String flag,String userID){
		this.timespan=timeSpan;
		this.flag=flag;
		APPSYSTEM.update(this.app, this.system, this.timespan, this.flag, userID);
	}
}
