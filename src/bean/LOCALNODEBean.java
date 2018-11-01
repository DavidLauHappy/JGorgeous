package bean;

import java.util.List;

import model.LOCALNODE;
import utils.StringUtil;

public class LOCALNODEBean {
	private String userID;
	private String app="";
	private String id="";
	private String ip="";
	private String name="";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String os="";//1-Linux 2-Windoss
	private String dir1="";
	private String dir2="";
	private String dir3="";
	private String dir4="";
	private String dir5="";
	private String autoBackup="";
	private String start="";
	private String stop="";
	private String autoStart="";
	private String dbType="";
	private String dbUser="";
	private String dbPasswd="";
	private String dbName="";
	private String dbPort="";
	private String dbAutoBackup="";
	private String sftpUser="";
	private String  sftpPasswd="";
	private String sftpPort="";
	private String sftpDir="";
	private String status="";
	private String time="";
	private String backdbname="";
	public enum Status{Null,
		Connected,
		Init,
		Running,
		Error,
		Done;}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
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
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getDir1() {
		return dir1;
	}
	public void setDir1(String dir1) {
		this.dir1 = dir1;
	}
	public String getDir2() {
		return dir2;
	}
	public void setDir2(String dir2) {
		this.dir2 = dir2;
	}
	public String getDir3() {
		return dir3;
	}
	public void setDir3(String dir3) {
		this.dir3 = dir3;
	}
	public String getDir4() {
		return dir4;
	}
	public void setDir4(String dir4) {
		this.dir4 = dir4;
	}
	public String getDir5() {
		return dir5;
	}
	public void setDir5(String dir5) {
		this.dir5 = dir5;
	}
	public String getAutoBackup() {
		return autoBackup;
	}
	public void setAutoBackup(String autoBackup) {
		this.autoBackup = autoBackup;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getStop() {
		return stop;
	}
	public void setStop(String stop) {
		this.stop = stop;
	}
	public String getAutoStart() {
		return autoStart;
	}
	public void setAutoStart(String autoStart) {
		this.autoStart = autoStart;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
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
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbPort() {
		return dbPort;
	}
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	public String getDbAutoBackup() {
		return dbAutoBackup;
	}
	public void setDbAutoBackup(String dbAutoBackup) {
		this.dbAutoBackup = dbAutoBackup;
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
	public String getSftpPort() {
		return sftpPort;
	}
	public void setSftpPort(String sftpPort) {
		this.sftpPort = sftpPort;
	}
	public String getSftpDir() {
		return sftpDir;
	}
	public void setSftpDir(String sftpDir) {
		this.sftpDir = sftpDir;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void reSetStatus(String status){
	    this.status=status;
	    LOCALNODE.setStatus(this.id, this.status);
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	public String getBackdbname() {
		if(StringUtil.isNullOrEmpty(this.backdbname))
				this.backdbname=this.dbName;
		return backdbname;
	}
	public void setBackdbname(String backdbname) {
		this.backdbname = backdbname;
	}
	private String showName;
	public String getShowName(){
		if(StringUtil.isNullOrEmpty(this.showName))
			this.showName=this.name+"("+this.ip+")";
		return this.showName;
	}
	
	public List<String>cmdDirs;
	
}
