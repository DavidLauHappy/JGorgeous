package bean;

public class DIRBean {
	public enum Type{Hierarchy,InstallDir,ExecuteDir,Document;}
	private String id;
	private String name;
	private String type;
	private String parentID;
	private String app;
	private String mdfUser;
	private String mdfTime;
	private String fullPath;
	
	public DIRBean(){}
	public DIRBean(String id, String name, String type, String parentID,String app,
			String mdfUser, String mdfTime) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.parentID = parentID;
		this.app=app;
		this.mdfUser = mdfUser;
		this.mdfTime = mdfTime;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getParentID() {
		return parentID;
	}

	public String getMdfUser() {
		return mdfUser;
	}

	public String getMdfTime() {
		return mdfTime;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public void setMdfUser(String mdfUser) {
		this.mdfUser = mdfUser;
	}

	public void setMdfTime(String mdfTime) {
		this.mdfTime = mdfTime;
	}

	public void setFullPath(String fullpath) {
		this.fullPath = fullpath;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	
	
}
