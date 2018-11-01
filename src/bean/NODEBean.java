package bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.deploy.figures.DeployEditView;

import resource.Context;
import views.AppView;
import model.NODE;
import model.USERS;

public class NODEBean {
	public enum Status{Null,
									Connected,
									Init,
									Running,
									Error,
									Done;}
	public enum Type{Null,
								Node;
								}
	public enum Flag{Mute,
								ScheduleAble,
								Scheduled;
								}
	public enum OS{Null,
							  Linux,
							  Windows;
							  }
	public enum DB{SqlServer,
			Oracle;
		  }
	private String id;
	private String ip;
	private String name;
	private String status;
	private String dbUser="";
	private String dbPasswd="";
	private String dbname="";
	private String backDBname="";
	private String dbport="";
	private String sftpUser="";
	private String sftpPasswd="";
	private String sftpDir="";
	private String sftpPort="";
	private String seq;
	private String type;
	private String schFlag;//调度的状态开关
	private String os;
	private String cluster;
	private String componentID;
	private String systemID;
	private String mdfUser;
	private String mdfTime;
	private String flag;//数据开关
	private String componentType;
	private String dbType;//节点的数据库类型sqlServer/Oracle
	private String sPort;
	////////////////////////扩展属性//////////
	private  Map<String,NODEDIRBean> dirs=new HashMap<String,NODEDIRBean>();
	private  Map<String,NODESERVICEBean> services=new HashMap<String,NODESERVICEBean>();
	
	public NODEBean(){}
	
	public NODEBean(String id, String ip, String name, String os,
			String cluster, String componentID, String systemID,
			String mdfUser, String flag) {
		super();
		this.id = id;
		this.ip = ip;
		this.name = name;
		this.os = os;
		this.cluster = cluster;
		this.componentID = componentID;
		this.systemID = systemID;
		this.mdfUser = mdfUser;
		this.flag = flag;
	}

	
	
	public NODEBean(String id, String ip, String name, String status,
			String dbUser, String dbPasswd, String dbname, String backDBname,
			String dbport, String sftpUser, String sftpPasswd, String sftpDir,
			String sftpPort, String seq, String type, String schFlag,
			String os, String cluster, String componentID, String systemID,
			String mdfUser, String mdfTime, String flag) {
		super();
		this.id = id;
		this.ip = ip;
		this.name = name;
		this.status = status;
		this.dbUser = dbUser;
		this.dbPasswd = dbPasswd;
		this.dbname = dbname;
		this.backDBname = backDBname;
		this.dbport = dbport;
		this.sftpUser = sftpUser;
		this.sftpPasswd = sftpPasswd;
		this.sftpDir = sftpDir;
		this.sftpPort = sftpPort;
		this.seq = seq;
		this.type = type;
		this.schFlag = schFlag;
		this.os = os;
		this.cluster = cluster;
		this.componentID = componentID;
		this.systemID = systemID;
		this.mdfUser = mdfUser;
		this.mdfTime = mdfTime;
		this.flag = flag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	//实时获取节点状态
	public String getStatusSync(){
		return NODE.getInstanceStatus(this.id, Context.session.userID, Context.session.currentFlag);
	}
	
	public void SetStatus(String status){
		this.status = status;
		NODE.setNodeStatus(this.id,  Context.session.userID, this.status);
	}
	public void setStatus(String status) {
		this.status = status;
		NODE.setNodeStatus(this.id,  Context.session.userID, this.status);
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPasswd() {
		return dbPasswd;
	}

	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getBackDBname() {
		return backDBname;
	}

	public void setBackDBname(String backDBname) {
		this.backDBname = backDBname;
	}

	public String getDbport() {
		return dbport;
	}

	public void setDbport(String dbport) {
		this.dbport = dbport;
	}

	public String getSftpUser() {
		return sftpUser;
	}

	public void setSftpUser(String sftpUser) {
		this.sftpUser = sftpUser;
	}

	public String getSftpPasswd() {
		return sftpPasswd;
	}

	public void setSftpPasswd(String sftpPasswd) {
		this.sftpPasswd = sftpPasswd;
	}

	public String getSftpDir() {
		return sftpDir;
	}

	public void setSftpDir(String sftpDir) {
		this.sftpDir = sftpDir;
	}

	public String getSftpPort() {
		return sftpPort;
	}

	public void setSftpPort(String sftpPort) {
		this.sftpPort = sftpPort;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSchFlag() {
		return schFlag;
	}

	public void setSchFlag(String schFlag) {
		this.schFlag = schFlag;
		NODE.setNodeScheduleStatus(this.id,  Context.session.userID, this.schFlag);
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getComponentID() {
		return componentID;
	}

	public void setComponentID(String componentID) {
		this.componentID = componentID;
	}

	
	public String getsPort() {
		return sPort;
	}

	public void setsPort(String sPort) {
		this.sPort = sPort;
	}

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
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

	public String getFlag() {
		return flag;
	}

  public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	
	public boolean hasCommand(String pkgID,String cmdStatus){
		String cmdFlag=COMMANDBean.Flag.On.ordinal()+"";
		return NODE.hasCommand(pkgID, this.id, cmdStatus, cmdFlag);
	}
	
	public boolean hasNoCommand(String pkgID,String cmdStatus){
		String cmdFlag=COMMANDBean.Flag.On.ordinal()+"";
		return NODE.hasNoCommand(pkgID, this.id, cmdStatus, cmdFlag);
	}
	
	public void setCommandStatus(String pkgID,String status){
		NODE.setCommandStatus(pkgID, this.id, status);
	}
	
	public void resetCommandStatus(String pkgID,String status){
		NODE.resetCommandStatus(pkgID, this.id, status);
	}
	
	public List<COMMANDBean> getScheCommands(String pkgID){
		return NODE.getCommandLists(pkgID, this.id);
	}

	public Map<String, NODEDIRBean> getDirs() {
		if(dirs!=null&&dirs.size()>0)
		return dirs;
		this.dirs=NODE.getDirs(this.id);
		return dirs;
		
	}
	
	public void setDirs(Map<String, NODEDIRBean> dirs) {
		this.dirs = dirs;
	}

	public Map<String, NODESERVICEBean> getServices() {
		if(services!=null&&services.size()>0)
			return services;
		this.services=NODE.getServices(this.id);
		return services;
	}

	public void setServices(Map<String, NODESERVICEBean> services) {
		this.services = services;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	
}
