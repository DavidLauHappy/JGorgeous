package bean;

public class DATAFLAGBean {
    private String id;
    private String flag;
    private String mdfTime;
    
    public DATAFLAGBean(){}

	public DATAFLAGBean(String id, String flag, String mdfTime) {
		super();
		this.id = id;
		this.flag = flag;
		this.mdfTime = mdfTime;
	}
	
	public DATAFLAGBean(String id, String flag) {
		super();
		this.id = id;
		this.flag = flag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMdfTime() {
		return mdfTime;
	}

	public void setMdfTime(String mdfTime) {
		this.mdfTime = mdfTime;
	}
    
    
    
}
