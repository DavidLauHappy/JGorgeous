package bean;

import model.NODEFILE;

public class NODEFILEBean {
	 private String pkgID;
	 private String nodeID;
	 private String fileID;
	 private String md5;
	 private String mdfUser;
	 private String mdfTime;
	 
	public NODEFILEBean(){}
	public NODEFILEBean(String pkgID, String nodeID, String fileID, String md5,
			String mdfUser) {
		super();
		this.pkgID = pkgID;
		this.nodeID = nodeID;
		this.fileID = fileID;
		this.md5 = md5;
		this.mdfUser = mdfUser;
	}

	public void inroll(){
		NODEFILE.deleteData(this);
		NODEFILE.addData(this);
	}
	public String getPkgID() {
		return pkgID;
	}

	public void setPkgID(String pkgID) {
		this.pkgID = pkgID;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
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
