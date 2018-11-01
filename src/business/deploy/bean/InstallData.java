package business.deploy.bean;

public class InstallData {
	private String pkgID;
	private String systemID;
	private String systemName;
	private String mdfTime;
	private String status;
	private String statusName;
	
    public InstallData(){}
    
	public InstallData(String pkgID, String systemID, String systemName,
			String mdfTime, String status) {
		super();
		this.pkgID = pkgID;
		this.systemID = systemID;
		this.systemName = systemName;
		this.mdfTime = mdfTime;
		this.status = status;
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

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getMdfTime() {
		return mdfTime;
	}

	public void setMdfTime(String mdfTime) {
		this.mdfTime = mdfTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
	
}
