package resource;

import utils.StringUtil;

public class User {
	private String userID;
	private String userName;
	private String passwd;
	private String email;
	private String phone;
	private String status;
	private String errorTimes;
	private String logTime;
	private String roleID;
	private String groupID;
	private String showName;
	public String getUserID() {
		if(!StringUtil.isNullOrEmpty(this.userID))
			this.userID=this.userID.trim();
		return userID;
	}
	public User(String userID, String userName, String passwd, String email,
			String phone, String status, String errorTimes, String logTime,
			String roleID, String groupID) {
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
		this.showName=this.userName+" "+this.userID;
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
	public String getShowName() {
		return showName;
	}
	
	
	
	
	
}
