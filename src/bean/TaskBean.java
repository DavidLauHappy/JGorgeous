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
		private String overDate;//ʵ����ɵ�ʱ��
		private String rdate;//Ԥ������ʱ��
		private String owner;//����������
		private String ownerName;
		private String scheID;//��������
		private String isDelay;
		private String delayCnt;//���ڴ���
		private String rlsCnt;//�������
		private String rstCnt;//�˻ش���
		private String crtUser;//���������
		private String crtUserName;
		private String crtTime;//����ʱ��
		private String mdfUser;//�޸���
		private String mdfTime;//�޸�ʱ��
		private String rstApprise;//�Ƿ���Ҫ���ŷ�������
		private String codeApprise;//�Ƿ���Ҫ��������
		private String app;
		
		///////////////////���̰汾�ύ����Ϣ////////////////////////
		private String revDesc;//�����������
		private String scopeDesc;//�޸ķ�Χ����
		private String submitJson;//�ύ˵��
		private String testDesc;//����˵��
		private String selfTestJson;//�Բ�˵��
		private String caseJson;//���԰���˵��
		private String currentUser;//��ǰ������
		private String currentUserName;
		private String currentVersion;//��ǰ�汾
		private String releaseFlag;//�Ƿ�ɷ����ʶ
		private String versionDesc;//���汾˵��(�޸�˵��)
		private String fileList;//�ļ��嵥����
		private String cmpFileList;//�����ļ��嵥����	
		
		//�汾���±�ʶ
		public enum ReleaseStatus{Init,			//��ʼ״̬
												Apply,			//�������°汾
												Organize,		//����֯�汾
												Organized,	//�汾��֯״̬
												CheckReturn;//����˻�
												}
		//����ĸ�������
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
		//��չ����
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
		
		//��������ĵ�����
		private BackLogBean req;
		public BackLogBean getReq() {
			if(this.req==null)
				this.req=BACKLOG.getReq(this.reqID);
			return req;
		}
		public void setReq(BackLogBean req) {
			this.req = req;
		}
		//ҳ���������
		public void logStep(String seq,String userID,String comment,String stepID){
			TASK.stepLog(this.id, userID, stepID, comment, seq);
		}
		
		//����������ת����һ��
		public void progress(String stepID,String nextUserID,String mdfUser){
			TASK.stepSwtich(this.id, stepID, nextUserID, mdfUser);
		}
		//����ÿ�������ύ���̰汾����������°汾
		public void makeNewVersion(){
			this.currentVersion=TASK.getNewVersion(this.id);
		}
		//�����ύ�����ύ��Ϣ
		public int setSubmitInfo(){
			this.mdfUser=Context.session.userID;
			return TASK.updateSubmit(this);
		}
		
		//�����ύ�����ĸ����ϴ�
		public void addAttach(String fileID,String md5,String location,String fileName,String fileTime,String mdfUser,boolean upload,String type){
			TASK.addAttachs(this.id, fileID, md5, location, fileName, fileTime, mdfUser, upload, this.currentVersion, type);
		}
		//ͬ�����÷�����ʶ
		public void ResetReleaseFlag(String releaseFlag){
			this.setReleaseFlag(releaseFlag);
			TASK.updateFlag(this.id, releaseFlag);
		}
}
