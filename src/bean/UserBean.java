package bean;

public class UserBean {
	private String userID;
	private String userName;
	private String passwd;
	private String email;
	private String phone;
	private String status;
	private String errorTimes;
	private String logTime;
	private String roleID;//用户的默认角色
	private String groupID;
	private String apps;
	private String showName;
	
    public UserBean(){}
    
	public UserBean(String userID, String userName, String passwd,
			String email, String phone, String status, String errorTimes,
			String logTime, String roleID, String groupID, String apps) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.passwd = passwd;
		this.email = email;
		this.phone = phone;
		this.status = status;
		this.errorTimes = errorTimes;
		this.logTime = logTime;
		this.roleID = roleID;
		this.groupID = groupID;
		this.apps = apps;
	}

	public String getUserID() {
		return userID;
	}

	public String getUserName() {
		return userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getStatus() {
		return status;
	}

	public String getErrorTimes() {
		return errorTimes;
	}

	public String getLogTime() {
		return logTime;
	}

	public String getRoleID() {
		return roleID;
	}

	public String getGroupID() {
		return groupID;
	}

	public String getApps() {
		return apps;
	}

	public String getShowName() {
		return showName;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		this.showName=this.userName+"("+this.userID.trim()+")";
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setErrorTimes(String errorTimes) {
		this.errorTimes = errorTimes;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public void setApps(String apps) {
		this.apps = apps;
	}
	
	
	
}
