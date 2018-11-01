package bean;

import java.util.ArrayList;
import java.util.List;

import model.QUESTIONS;

public class QUESTIONSBean {
	private String id="";
	private String qdesc="";
	private String mdesc="";
	private String qtype="";
	private String qstatus="";
	private String app="";
	private String reqid="";
	private String crtUser="";
	private String crtTime="";
	private String curUser="";
	private String mdfTime="";
	private String developer="";
	private List<QUESTIONFILEBean> files;
	
	//
	private String crtUserFull="";
	private String curUserFull="";
	//
	private String reqNo="";
	private String reqName="";
	private String reqFullName="";
	
	public QUESTIONSBean(){
		this.id=QUESTIONS.getNo();
	}
	
	public String getReqNo() {
		return reqNo;
	}
	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}
	public String getReqName() {
		return reqName;
	}
	public void setReqName(String reqName) {
		this.reqName = reqName;
	}
	public String getReqFullName() {
		return reqFullName;
	}
	public void setReqFullName(String reqFullName) {
		this.reqFullName = reqFullName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQdesc() {
		return qdesc;
	}
	public void setQdesc(String qdesc) {
		this.qdesc = qdesc;
	}
	public String getMdesc() {
		return mdesc;
	}
	public void setMdesc(String mdesc) {
		this.mdesc = mdesc;
	}
	public String getQtype() {
		return qtype;
	}
	public void setQtype(String qtype) {
		this.qtype = qtype;
	}
	public String getQstatus() {
		return qstatus;
	}
	public void setQstatus(String qstatus) {
		this.qstatus = qstatus;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getReqid() {
		return reqid;
	}
	public void setReqid(String reqid) {
		this.reqid = reqid;
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
	public String getCurUser() {
		return curUser;
	}
	public void setCurUser(String curUser) {
		this.curUser = curUser;
	}
	public String getMdfTime() {
		return mdfTime;
	}
	public void setMdfTime(String mdfTime) {
		this.mdfTime = mdfTime;
	}
	
	
	
	
	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public List<QUESTIONFILEBean> getFiles() {
		if(this.files==null||this.files.size()>0){
			this.files=QUESTIONS.getFiles(this.id);
		}
		return files;
	}
	public void setFiles(List<QUESTIONFILEBean> files) {
		this.files = files;
	}
	
	
	
	public String getCrtUserFull() {
		return crtUserFull;
	}
	public void setCrtUserFull(String crtUserFull) {
		this.crtUserFull = crtUserFull;
	}
	public String getCurUserFull() {
		return curUserFull;
	}
	public void setCurUserFull(String curUserFull) {
		this.curUserFull = curUserFull;
	}
	public List<QUESTIONFILEBean> getTFiles(){
		List<QUESTIONFILEBean> result=new ArrayList<QUESTIONFILEBean>();
		if(this.files!=null&&this.files.size()>0){
			for(QUESTIONFILEBean file:files){
				if((QUESTIONFILEBean.Type.Test.ordinal()+"").equals(file.getQfileType())){
					result.add(file);
				}
			}
		}
		return result;
	}
	
	public List<QUESTIONFILEBean> getDFiles(){
		List<QUESTIONFILEBean> result=new ArrayList<QUESTIONFILEBean>();
		if(this.files!=null&&this.files.size()>0){
			for(QUESTIONFILEBean file:files){
				if((QUESTIONFILEBean.Type.Develper.ordinal()+"").equals(file.getQfileType())){
					result.add(file);
				}
			}
		}
		return result;
	}
	
	public void save(){
		QUESTIONS.add(this);
	}
	
	public void update(){
		QUESTIONS.update(this);
	}
	
	public void close(){
		QUESTIONS.testOver(this);
	}
	
	public void sumbit(){
		QUESTIONS.developerEdit(this);
	}
	
	public void reSetStatus(String status){
		this.qstatus=status;
		QUESTIONS.updateStatus(this.id, this.qstatus);
		
	}
}
