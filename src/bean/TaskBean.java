package bean;

import resource.Context;
import utils.StringUtil;
import model.BACKLOG;
import model.TASK;

public class TaskBean {
		private String id;
		private String tname;
		private String reqID;
		private String status;
		private String arrangeDate;
		private String overDate;//实际完成的时间
		private String rdate;//预计上线时间
		private String owner;//任务责任人
		private String ownerName;
		private String scheID;//需求批次
		private String isDelay;
		private String delayCnt;//延期次数
		private String rlsCnt;//发版次数
		private String rstCnt;//退回次数
		private String crtUser;//任务分配人
		private String crtUserName;
		private String crtTime;//分派时间
		private String mdfUser;//修改人
		private String mdfTime;//修改时间
		private String rstApprise;//是否需要安排方案评审
		private String codeApprise;//是否需要代码评审
		private String app;
		
		///////////////////过程版本提交的信息////////////////////////
		private String revDesc;//解决方案描述
		private String scopeDesc;//修改范围描述
		private String submitJson;//提交说明
		private String testDesc;//测试说明
		private String selfTestJson;//自测说明
		private String caseJson;//测试案例说明
		private String currentUser;//当前处理人
		private String currentUserName;
		private String currentVersion;//当前版本
		private String releaseFlag;//是否可发版标识
		private String versionDesc;//发版本说明(修改说明)
		private String fileList;//文件清单描述
		private String cmpFileList;//编译文件清单描述	
		
		//版本更新标识
		public enum ReleaseStatus{Init,			//初始状态
												Apply,			//可申请新版本
												Organize,		//待组织版本
												Organized,	//版本组织状态
												CheckReturn;//审核退回
												}
		//任务的附件类型
		public enum AttachType{Normal,Solution,Code,Requiremnt;}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTname() {
			return tname;
		}
		public void setTname(String tname) {
			this.tname = tname;
		}
		public String getReqID() {
			return reqID;
		}
		public void setReqID(String reqID) {
			this.reqID = reqID;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getArrangeDate() {
			return arrangeDate;
		}
		public void setArrangeDate(String arrangeDate) {
			this.arrangeDate = arrangeDate;
		}
		public String getOverDate() {
			return overDate;
		}
		public void setOverDate(String overDate) {
			this.overDate = overDate;
		}
		public String getRdate() {
			return rdate;
		}
		public void setRdate(String rdate) {
			this.rdate = rdate;
		}
		public String getOwner() {
			this.owner=this.owner.trim();
			return owner;
		}
		public String getOwnerShowName(){
			return this.ownerName+"("+this.owner+")";
		}
		
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getScheID() {
			return scheID;
		}
		public void setScheID(String scheID) {
			this.scheID = scheID;
		}
		public String getIsDelay() {
			return isDelay;
		}
		public void setIsDelay(String isDelay) {
			this.isDelay = isDelay;
		}
		public String getDelayCnt() {
			return delayCnt;
		}
		public void setDelayCnt(String delayCnt) {
			this.delayCnt = delayCnt;
		}
		public String getRlsCnt() {
			return rlsCnt;
		}
		public void setRlsCnt(String rlsCnt) {
			this.rlsCnt = rlsCnt;
		}
		public String getRstCnt() {
			return rstCnt;
		}
		public void setRstCnt(String rstCnt) {
			this.rstCnt = rstCnt;
		}
		public String getCrtUser() {
			return crtUser;
		}
		
		public String getCrtUserShowName(){
			return this.crtUserName+"("+this.crtUser+")";
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
		public String getMdfUser() {
			return mdfUser;
		}
		public void setMdfUser(String mdfUser) {
			this.mdfUser = mdfUser;
		}
		public String getMdfTIme() {
			return mdfTime;
		}
		public void setMdfTIme(String mdfTIme) {
			this.mdfTime = mdfTIme;
		}
		public String getRstApprise() {
			return rstApprise;
		}
		public void setRstApprise(String rstApprise) {
			this.rstApprise = rstApprise;
		}
		public String getCodeApprise() {
			return codeApprise;
		}
		public void setCodeApprise(String codeApprise) {
			this.codeApprise = codeApprise;
		}
		public String getApp() {
			return app;
		}
		public void setApp(String app) {
			this.app = app;
		}
		public String getRevDesc() {
			return revDesc;
		}
		public void setRevDesc(String revDesc) {
			this.revDesc = revDesc;
		}
		public String getScopeDesc() {
			return scopeDesc;
		}
		public void setScopeDesc(String scopeDesc) {
			this.scopeDesc = scopeDesc;
		}
		public String getSubmitJson() {
			return submitJson;
		}
		public void setSubmitJson(String submitJson) {
			this.submitJson = submitJson;
		}
		public String getTestDesc() {
			return testDesc;
		}
		public void setTestDesc(String testDesc) {
			this.testDesc = testDesc;
		}
		public String getSelfTestJson() {
			return selfTestJson;
		}
		public void setSelfTestJson(String selfTestJson) {
			this.selfTestJson = selfTestJson;
		}
		public String getCaseJson() {
			return caseJson;
		}
		public void setCaseJson(String caseJson) {
			this.caseJson = caseJson;
		}
		public String getCurrentUser() {
			this.currentUser=this.currentUser.trim();
			return currentUser;
		}
		public void setCurrentUser(String currentUser) {
			this.currentUser = currentUser;
		}
		public String getCurrentVersion() {
			return currentVersion;
		}
		public void setCurrentVersion(String currentVersion) {
			this.currentVersion = currentVersion;
		}
		public String getReleaseFlag() {
			return releaseFlag;
		}
		public void setReleaseFlag(String releaseFlag) {
			this.releaseFlag = releaseFlag;
		}
		public String getVersionDesc() {
			return versionDesc;
		}
		public void setVersionDesc(String versionDesc) {
			this.versionDesc = versionDesc;
		}
		public String getFileList() {
			return fileList;
		}
		public void setFileList(String fileList) {
			this.fileList = fileList;
		}
		public String getCmpFileList() {
			return cmpFileList;
		}
		public void setCmpFileList(String cmpFileList) {
			this.cmpFileList = cmpFileList;
		}
		public String getOwnerName() {
			return ownerName;
		}
		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}
		public String getCrtUserName() {
			return crtUserName;
		}
		public void setCrtUserName(String crtUserName) {
			this.crtUserName = crtUserName;
		}
		public String getCurrentUserName() {
			return currentUserName;
		}
		public void setCurrentUserName(String currentUserName) {
			this.currentUserName = currentUserName;
		}
		//扩展属性
		private String  viewID;
		private ViewBean viewBean;
		
		public String getViewID() {
			if(StringUtil.isNullOrEmpty(this.viewID))
				this.viewID=TASK.getViewByTask(this.id);
			return viewID;
		}
		
		
		public void setViewID(String viewID) {
			this.viewID = viewID;
		}
		public ViewBean getViewBean() {
			return viewBean;
		}
		public void setViewBean(ViewBean viewBean) {
			this.viewBean = viewBean;
		}
		
		public String getShowName(){
			return this.id+"_"+this.tname;
		}
		
		//任务关联的的需求
		private BackLogBean req;
		public BackLogBean getReq() {
			if(this.req==null)
				this.req=BACKLOG.getReq(this.reqID);
			return req;
		}
		public void setReq(BackLogBean req) {
			this.req = req;
		}
		//页面操作方法
		public void logStep(String seq,String userID,String comment,String stepID){
			TASK.stepLog(this.id, userID, stepID, comment, seq);
		}
		
		//任务流程跳转到下一人
		public void progress(String stepID,String nextUserID,String mdfUser){
			TASK.stepSwtich(this.id, stepID, nextUserID, mdfUser);
		}
		//开发每次重新提交过程版本产生任务的新版本
		public void makeNewVersion(){
			this.currentVersion=TASK.getNewVersion(this.id);
		}
		//任务提交设置提交信息
		public int setSubmitInfo(){
			this.mdfUser=Context.session.userID;
			return TASK.updateSubmit(this);
		}
		
		//任务提交关联的附近上传
		public void addAttach(String fileID,String md5,String location,String fileName,String fileTime,String mdfUser,boolean upload,String type){
			TASK.addAttachs(this.id, fileID, md5, location, fileName, fileTime, mdfUser, upload, this.currentVersion, type);
		}
		//同步设置发布标识
		public void ResetReleaseFlag(String releaseFlag){
			this.setReleaseFlag(releaseFlag);
			TASK.updateFlag(this.id, releaseFlag);
		}
}
