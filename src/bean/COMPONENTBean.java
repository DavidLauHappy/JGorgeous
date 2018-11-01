package bean;

public class COMPONENTBean {
	private String id;
	private String name;
	private String abbr;
	private String type;
	private String systemID;
	private String status;
	private String mdfUser;
	private String mdfTime;
	private String flag;
	public COMPONENTBean(){}

	public COMPONENTBean(String id, String name, String abbr, String type,
			String systemID, String status, String mdfUser, String flag) {
		super();
		this.id = id;
		this.name = name;
		this.abbr = abbr;
		this.type = type;
		this.systemID = systemID;
		this.status = status;
		this.mdfUser = mdfUser;
		this.flag = flag;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	
}
