package bean;


/**
 * @author Administrator
 * 所谓的策略主要就是这个表数据的扩展
 */
public class NODESTEPBean {

	public enum Flag{Off,On;}
	private String pkgID;
	private String systemID;
	private String nodeID;
	private String stepID;
	private String flag;
	private String mdfUser;
	private String mdfTime;
	
	public NODESTEPBean(){}

	public NODESTEPBean(String pkgID, String systemID, String nodeID,
			String stepID, String flag, String mdfUser) {
		super();
		this.pkgID = pkgID;
		this.systemID = systemID;
		this.nodeID = nodeID;
		this.stepID = stepID;
		this.flag = flag;
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

	public String getStepID() {
		return stepID;
	}

	public void setStepID(String stepID) {
		this.stepID = stepID;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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
