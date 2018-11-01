package bean;

public class USERNODEBean {

	private String userID;
	private String nodeID;
	private String status;
	private String dbUser;
	private String dbPasswd;
	private String dbname;
	private String backDBname;
	private String dbport;
	private String sftpUser;
	private String sftpPasswd;
	private String sftpDir;
	private String sftpPort;
	private String seq;
	private String type;
	private String schFlag;
	private String os;
	private String mdfTime;
	
	public USERNODEBean(){}

	
	
	public USERNODEBean(String userID, String nodeID, String status,
			String dbUser, String dbPasswd, String dbname, String backDBname,
			String dbport, String sftpUser, String sftpPasswd, String sftpDir,
			String sftpPort, String seq, String type, String schFlag,
			String os, String mdfTime) {
		super();
		this.userID = userID;
		this.nodeID = nodeID;
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
		this.mdfTime = mdfTime;
	}



	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getMdfTime() {
		return mdfTime;
	}

	public void setMdfTime(String mdfTime) {
		this.mdfTime = mdfTime;
	}
	
	
}
