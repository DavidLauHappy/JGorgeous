package bean;

import model.STEP;
import utils.StringUtil;

public class STEPBean {
	 public enum ActionType{Null,FileCopy,ScriptInstall,BackDir,BackRun,ServiceStart,ServiceStop,UploadPkg;}
	 public enum Type{Null,Pkg,Step,fileStep;}
	 private String pkgID;
	   private String id;
	   private String name;
	   private String desc;
	   private String action;
	   private String parentid;
	   private String order;
	   private String backupFlag;
	   private String mdfUser;
	   private String mdfTime;
	   
	   
	   
	public STEPBean(String pkgID, String id, String name, String desc,
			String action, String parentid, String order, String backupFlag,
			String mdfUser) {
		super();
		this.pkgID = pkgID;
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.action = action;
		this.parentid = parentid;
		this.order = order;
		this.backupFlag = backupFlag;
		this.mdfUser = mdfUser;
	}
	public STEPBean(){}
	public STEPBean(String pkgID, String id, String name, String desc,
			String action, String parentid, String order, String backupFlag,
			String mdfUser, String mdfTime) {
		super();
		this.pkgID = pkgID;
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.action = action;
		this.parentid = parentid;
		this.order = order;
		this.backupFlag = backupFlag;
		this.mdfUser = mdfUser;
		this.mdfTime = mdfTime;
	}

	public String getPkgID() {
		return pkgID;
	}

	public void setPkgID(String pkgID) {
		this.pkgID = pkgID;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getParentid() {
		return parentid;
	}



	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getBackupFlag() {
		return backupFlag;
	}

	public void setBackupFlag(String backupFlag) {
		this.backupFlag = backupFlag;
		STEP.setBackFlag(this.getPkgID(),this.getId(),this.backupFlag);
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
		STEP.setParent(this.getPkgID(), this.getId(), this.parentid);
	}
	
	public void inroll(){
		STEP.addData(this);
	}
	
	public void delete(){
		STEP.deleteByID(this.getPkgID(), this.getId());
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
	 
	//根据步骤名称取步骤对于的组件名称，就是/A/B/C 最前面的一段
	 public String getComponentName(){
	    	if(this.name.indexOf("/")!=-1){
	    		String[] dirs=this.name.split("/");
	    		if(dirs!=null&&dirs.length>0){
	    			for(int w=0;w<dirs.length;w++){
	    				if(!StringUtil.isNullOrEmpty(dirs[w]))
	    					return dirs[w];
	    			}
	    		}
	    	}
	    	return this.name;
	    }
	   
}
