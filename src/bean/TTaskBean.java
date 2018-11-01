package bean;

import model.TTASK;
import model.VIEW;

public class TTaskBean {
	public enum Status{Null,Todo,Doing,Done;}
	private String viewID;
	private String id;
	private String userID;
	private String status;
	private String crtUser;
	private String crtTime;
	private String viewName;
	private ViewBean view;
	public String getViewID() {
		return viewID;
	}
	public void setViewID(String viewID) {
		this.viewID = viewID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	//测试任务提交
	public void SetStatus(String status){
		this.status = status;
		TTASK.setStatus(this.id, this.status, this.userID);
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
	
	public void add(){
		TTASK.add(this);
	}
	public ViewBean getView() {
		if(view==null)
			view=VIEW.getViewById(this.viewID);
		return view;
	}
	public void setView(ViewBean view) {
		this.view = view;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	
	
	
	
}
