package bean;

import resource.Context;
import model.SYSTEM;

public class SYSTEMBean {
	private String id;
	private String name;
	private String abbr;
	private String app;
	private String cate;
	private String bussID;
	private String status;
	private String mdfUser;
	private String mdfTime;
	private String falg;
	
	public SYSTEMBean(){}
	public SYSTEMBean(String id, String name, String abbr, String app,
			String cate, String bussID, String status, String mdfUser,
			 String falg) {
		super();
		this.id = id;
		this.name = name;
		this.abbr = abbr;
		this.app = app;
		this.cate = cate;
		this.bussID = bussID;
		this.status = status;
		this.mdfUser = mdfUser;
		this.falg = falg;
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

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
	}

	public String getBussID() {
		return bussID;
	}

	public void setBussID(String bussID) {
		this.bussID = bussID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getFalg() {
		return falg;
	}

	public void setFalg(String falg) {
		this.falg = falg;
	}
	
	public void setNode(String scheStatus){
		SYSTEM.setNodeSchedule( Context.session.userID, this.bussID, this.falg, scheStatus);
	}
}
