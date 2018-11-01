package bean;

import model.NODEFILESTT;

public class NODEFILESTTBean {
	private String pkgID;
	private String systemID;
	private String nodeID;
	private String fileDir;
	private String fileCount;
	private String mdfUser;
	private String mdfTime;
	private String systemName;
	private String nodeName;
	
	public NODEFILESTTBean(){}
	public NODEFILESTTBean(String pkgID, String systemID, String nodeID,
			String fileDir, String fileCount, String mdfUser) {
		super();
		this.pkgID = pkgID;
		this.systemID = systemID;
		this.nodeID = nodeID;
		this.fileDir = fileDir;
		this.fileCount = fileCount;
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

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public String getFileCount() {
		return fileCount;
	}

	public void setFileCount(String fileCount) {
		this.fileCount = fileCount;
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

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public void inroll(){
		NODEFILESTT.addData(this);
	}
	
	
}
