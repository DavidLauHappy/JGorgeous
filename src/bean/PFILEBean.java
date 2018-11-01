package bean;

public class PFILEBean {
	public enum Type{Text,Binary;}
	public enum DbType{DDL,DATA,PROC;}
	private String id;
	private String name;
	private String path;
	private String bootfalg;
	private String seq;
	private String pkgID;
	private String stepID;
	private String md5;
	private String type;
	private String dbOwner;
	private String user;
	private String dbType;
	private String objName;
	private String dir;
	private String isDir;
	private String mdfUser;
	private String mdfTime;
	
	public PFILEBean(){}
	public PFILEBean(String id, String name, String path, String bootfalg,
			String seq, String pkgID, String stepID, String md5, String type,
			String dbOwner, String user, String dbType, String objName,
			String dir, String isDir, String mdfUser, String mdfTime) {
		super();
		this.id = id;
		this.name = name;
		this.path = path;
		this.bootfalg = bootfalg;
		this.seq = seq;
		this.pkgID = pkgID;
		this.stepID = stepID;
		this.md5 = md5;
		this.type = type;
		this.dbOwner = dbOwner;
		this.user = user;
		this.dbType = dbType;
		this.objName = objName;
		this.dir = dir;
		this.isDir = isDir;
		this.mdfUser = mdfUser;
		this.mdfTime = mdfTime;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBootfalg() {
		return bootfalg;
	}

	public void setBootfalg(String bootfalg) {
		this.bootfalg = bootfalg;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getPkgID() {
		return pkgID;
	}

	public void setPkgID(String pkgID) {
		this.pkgID = pkgID;
	}

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDbOwner() {
		return dbOwner;
	}

	public void setDbOwner(String dbOwner) {
		this.dbOwner = dbOwner;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getIsDir() {
		return isDir;
	}

	public void setIsDir(String isDir) {
		this.isDir = isDir;
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

	
	 
	
	
}
