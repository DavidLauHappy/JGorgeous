package bean;

import java.util.List;

import resource.Context;
import model.PKGSYSTEM;

public class PKGSYSTEMBean {
	private String pkgID;
	private String systemID;
	private String status;
	private String mdfUser;
	private String mdfTime;
	public enum Status{Ready,
		Busy,//调度中(本地模式下是调度执行，代理模式下指的是网络发送中)
		Done,//调度完成(本地模式下是执行完成，代理模式下指的是网络发送完成)
		Halt;}
	
	public PKGSYSTEMBean(){}
	public PKGSYSTEMBean(String pkgID, String systemID, String status,
			String mdfUser) {
		super();
		this.pkgID = pkgID;
		this.systemID = systemID;
		this.status = status;
		this.mdfUser = mdfUser;
	}
	public String getPkgID() {
		return pkgID;
	}
	public void setPkgID(String pkgID) {
		this.pkgID = pkgID;
	}
	public String getSystemID() {
		return systemID;
	}
	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
		PKGSYSTEM.updateStatus( Context.session.userID, this.pkgID, this.systemID , this.status);
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
	public PKGSYSTEMBean(String pkgID, String systemID, String status,
			String mdfUser, String mdfTime) {
		super();
		this.pkgID = pkgID;
		this.systemID = systemID;
		this.status = status;
		this.mdfUser = mdfUser;
		this.mdfTime = mdfTime;
	}
	
	public List<String> getNodes(){
		 return PKGSYSTEM.getNodes(Context.session.userID, pkgID, systemID, Context.session.currentFlag);
	}
	
	public NODEBean  electNode(){
		NODEBean node=PKGSYSTEM.electNode( Context.session.userID, pkgID, systemID, Context.session.currentFlag);
		return node;
	}
	
	public void inroll(){
		PKGSYSTEM.getAdd(this);
	}
}
