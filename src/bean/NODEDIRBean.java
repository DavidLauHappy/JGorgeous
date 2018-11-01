package bean;

import model.NODE;

public class NODEDIRBean {
	  private String nodeID;
	  private String dirName;
	  private String dirValue;
	  private String dirFilter;
	  private String mdfUser;
	  private String mdfTime;
	  
	   public NODEDIRBean(){}

	public NODEDIRBean(String nodeID, String dirName, String dirValue,
			String dirFilter, String mdfUser, String mdfTime) {
		super();
		this.nodeID = nodeID;
		this.dirName = dirName;
		this.dirValue = dirValue;
		this.dirFilter = dirFilter;
		this.mdfUser = mdfUser;
		this.mdfTime = mdfTime;
	}

	public NODEDIRBean(String nodeID, String dirName, String dirValue,
			String dirFilter, String mdfUser) {
		super();
		this.nodeID = nodeID;
		this.dirName = dirName;
		this.dirValue = dirValue;
		this.dirFilter = dirFilter;
		this.mdfUser = mdfUser;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public String getDirValue() {
		return dirValue;
	}

	public void setDirValue(String dirValue) {
		this.dirValue = dirValue;
	}

	public String getDirFilter() {
		return dirFilter;
	}

	public void setDirFilter(String dirFilter) {
		this.dirFilter = dirFilter;
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
	   
   public void inroll(){		
	   NODE.setNodeDir(this);
   }
	  
	    
}
