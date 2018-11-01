package bean;

public class PKGBean {
	 public enum Status{Initial,//导入完成，可配置
			 Config,//版本配置完成(对某一个群组)
			 Run,//版本正部署状态相当于锁定，不允许重复部署
			 Done//完成版本安装
			 }
	 public enum Enable{No,Yes;}
	  private String id;
	  private String appname;
	  private String relapps;
	  private String desc;
	  private String status;
	  private String cfgfile;
	  private String enalbe;
	  private String mdfUser;
	  private String mdfTime;
	  
	  public PKGBean(){}
	  
	public PKGBean(String id, String appname, String relapps, String desc,
			String status, String cfgfile, String enalbe,
			String mdfUser, String mdfTime) {
		super();
		this.id = id;
		this.appname = appname;
		this.relapps = relapps;
		this.desc = desc;
		this.status = status;
		this.cfgfile = cfgfile;
		this.enalbe = enalbe;
		this.mdfUser = mdfUser;
		this.mdfTime = mdfTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getRelapps() {
		return relapps;
	}
	public void setRelapps(String relapps) {
		this.relapps = relapps;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCfgfile() {
		return cfgfile;
	}
	public void setCfgfile(String cfgfile) {
		this.cfgfile = cfgfile;
	}
	public String getEnalbe() {
		return enalbe;
	}
	public void setEnalbe(String enalbe) {
		this.enalbe = enalbe;
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
