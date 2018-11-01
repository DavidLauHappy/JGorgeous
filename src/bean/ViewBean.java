package bean;

import java.util.List;

import resource.Context;
import utils.StringUtil;
import model.TTASK;
import model.USERS;
import model.VIEW;


/**
 * @author Administrator
 * 发布或者视图对象，与项目需求是一对多的概念，
 * 是进行版本管理的基础单位
 */
public class ViewBean {
	public enum Status{Normal,Lock,Close;} //视图的状态
	public enum ReleaseStatus{Init,				//初始状态
											Apply,				//可申请新版本
											Organize,			//待组织版本
											Organized;		//版本组织状态
											}//发布状态
	private String viewID;
	private String viewName;
	private String veiwDesc;
	private String crtUser;
	private String crtTime;
	private String uptUser;//可以理解为上一步骤处理人
	private String uptTime;
	private String version;
	private String streamID;
	private String showVersion;
	private String status=Status.Normal.ordinal()+"";
	private String app;
	private String rdate;//预计上线时间
	private String deadDate;//开发完成时间
	private String owner;//开发责任人
	private String towner;//测试责任人
	private String ownerName;
	private String townerName;
	private String progress;
	private String currentUserID;
	private String lastProgress;
	private String uptFlag="0";
	private String verDesc="";
	
	//视图对应的测试任务
	public List<TTaskBean> ttasks;
	public ViewBean(){}

	public void sync(){
		VIEW.add(this);
	}
	
	public void clearReqs(){
		VIEW.removeReq(this.viewID);
	}
	
	public void addReq(String sno){
		VIEW.addReq(this.viewID, sno, Context.session.userID);
	}
	
	public void createVersion(){
		//String versionID=VIEW.getVersion(this.getViewID());
		//this.version=versionID;
		VIEW.addVersion(this.viewID, this.veiwDesc, this.version, Context.session.userID);
	}
	
	public void submit(){
		VIEW.submit(this.viewID, this.progress, this.currentUserID, Context.session.userID,this.lastProgress);
	}
	
	public void chgStream(){
		VIEW.chgStream(viewID, streamID);
	}
	public String getViewID() {
		return viewID;
	}

	
	public void setViewID(String viewID) {
		this.viewID = viewID;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getVeiwDesc() {
		return veiwDesc;
	}

	public void setVeiwDesc(String veiwDesc) {
		this.veiwDesc = veiwDesc;
	}

	public String getCrtUser() {
		return crtUser;
	}

	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}

	public String getCrtTime() {
		return crtTime;
	}

	public void setCrtTime(String crtTime) {
		this.crtTime = crtTime;
	}

	public String getUptUser() {
		return uptUser;
	}

	public void setUptUser(String uptUser) {
		this.uptUser = uptUser;
	}

	public String getUptTime() {
		return uptTime;
	}

	public void setUptTime(String uptTime) {
		this.uptTime = uptTime;
	}

	public String getVersion() {
		if(StringUtil.isNullOrEmpty(this.version)){
			String versionID=VIEW.getVersion(this.getViewID());
			this.version=versionID;
		}
		return version;
	}

	public String getNewVersion(){
		String versionID=VIEW.getNextVersion(this.getViewID());
		this.version=versionID;
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public String getStreamID() {
		return streamID;
	}

	public void setStreamID(String streamID) {
		this.streamID = streamID;
	}

	public String getShowVersion() {
		return showVersion;
	}

	public void setShowVersion(String showVersion) {
		this.showVersion = showVersion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void lockStatus(String status){
		this.status=status;
		VIEW.setStatus(this.viewID, status);
	}
	
	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}



	public String getDeadDate() {
		return deadDate;
	}

	public void setDeadDate(String deadDate) {
		this.deadDate = deadDate;
	}

	

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getCurrentUserID() {
		return currentUserID;
	}

	public void setCurrentUserID(String currentUserID) {
		this.currentUserID = currentUserID;
	}

	public String getLastProgress() {
		return lastProgress;
	}

	public void setLastProgress(String lastProgress) {
		this.lastProgress = lastProgress;
	}

	public String getUptFlag() {
		return uptFlag;
	}

	public void setUptFlag(String uptFlag) {
		this.uptFlag = uptFlag;
	}
	
	public void reSetUptFlag(String uptFlag) {
		this.uptFlag = uptFlag;
		VIEW.setFlag(this.viewID,this.uptFlag);
	}

	public String getVerDesc() {
		return verDesc;
	}

	public void setVerDesc(String verDesc) {
		this.verDesc = verDesc;
	}

	public String getRdate() {
		return rdate;
	}

	public void setRdate(String rdate) {
		this.rdate = rdate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTowner() {
		return towner;
	}

	public void setTowner(String towner) {
		this.towner = towner;
	}

	public void SetTowner(String towner){
		this.towner=towner;
		VIEW.setTowner(this.viewID, this.towner);
	}
	public String getOwnerName() {
		if(StringUtil.isNullOrEmpty(this.ownerName)){
			UserBean user=USERS.getUser(this.owner);
			this.ownerName=user.getUserName()+"("+user.getUserID()+")";
		}
		return ownerName;
	}

	public String getTownerName() {
		if(StringUtil.isNullOrEmpty(this.townerName)){
			UserBean user=USERS.getUser(this.towner);
			this.townerName=user.getUserName()+"("+user.getUserID()+")";
		}
		return townerName;
	}

	//实时获取视图对应的测试任务
	public List<TTaskBean> getTestTasks(){
		if(ttasks==null){
			ttasks=TTASK.getTasksByView(this.viewID);
		}
		return ttasks;
	}
	
}
