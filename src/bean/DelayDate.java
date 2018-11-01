package bean;

import utils.StringUtil;
import model.DELAYDETAIL;
import model.USERS;

public class DelayDate {
	public enum Status{Apply,Approved,Rejected;}
	public String applyID;
	public String reqID;
	public String staskID;
	public String applyUserID;
	public String applyUserFullName;
	public String applyTime;
	public String apprUserID;
	public String apprUserFullName;
	public String reason;
	public String comment;
	public String oldDate;
	public String newDate;
	public String status;
	public String name;
	public String mtime;
	public String dtime;
	public String getApplyID() {
		return applyID;
	}
	public void setApplyID(String applyID) {
		this.applyID = applyID;
	}
	public String getReqID() {
		return reqID;
	}
	public void setReqID(String reqID) {
		this.reqID = reqID;
	}
	public String getStaskID() {
		return staskID;
	}
	public void setStaskID(String staskID) {
		this.staskID = staskID;
	}
	public String getApplyUserID() {
		return applyUserID;
	}
	public void setApplyUserID(String applyUserID) {
		this.applyUserID = applyUserID;
		if(StringUtil.isNullOrEmpty(this.applyUserFullName)){
			UserBean user= USERS.getUser(this.applyUserID);
			this.applyUserFullName=user.getUserName()+"("+this.applyUserID +")";
		}
	}
	public String getApprUserID() {
		return apprUserID;
	}
	public void setApprUserID(String apprUserID) {
		this.apprUserID = apprUserID;
		if(StringUtil.isNullOrEmpty(this.apprUserFullName)){
			UserBean user= USERS.getUser(this.apprUserID);
			this.apprUserFullName=user.getUserName()+"("+this.apprUserID +")";
		}
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOldDate() {
		return oldDate;
	}
	public void setOldDate(String oldDate) {
		this.oldDate = oldDate;
	}
	public String getNewDate() {
		return newDate;
	}
	public void setNewDate(String newDate) {
		this.newDate = newDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getApplyUserFullName() {
		return applyUserFullName;
	}
	public String getApprUserFullName() {
		return apprUserFullName;
	}
	
	
	public String getMtime() {
		return mtime;
	}
	public void setMtime(String mtime) {
		this.mtime = mtime;
	}
	public String getDtime() {
		return dtime;
	}
	public void setDtime(String dtime) {
		this.dtime = dtime;
	}
	
	
	
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public void add(){
		DELAYDETAIL.add(this);
	}
	
	public void SetAppr(String status,String comment){
		this.status=status;
		this.comment=comment;
		DELAYDETAIL.approved(this);
	}
}
