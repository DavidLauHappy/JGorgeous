package bean;

import model.STREAM;

public class StreamBean {
	//public enum Status{Normal,Lock,Close;}
	private String streamID;
	private String streamName;
	private String streamDesc;
	private String status="";
	private String updUser;
	private String updTime;
	private String startTime;
	private String endTime;
	private String state;
	private String code;
	
	public StreamBean(){}
	
	public String getStreamID() {
		return streamID;
	}
	public void setStreamID(String streamID) {
		this.streamID = streamID;
	}
	public String getStreamName() {
		return streamName;
	}
	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	public String getStreamDesc() {
		return streamDesc;
	}
	public void setStreamDesc(String streamDesc) {
		this.streamDesc = streamDesc;
	}
	public String getStatus() {
		return state;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpdUser() {
		return updUser;
	}
	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}
	public String getUpdTime() {
		return updTime;
	}
	public void setUpdTime(String updTime) {
		this.updTime = updTime;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean nameExist(String name){
		boolean result=STREAM.nameExist(name);
		if(!result)
			this.streamName=name;
		return result;
	}
	
	public void create(){
		String id=STREAM.getStreamID();
		this.streamID=id;
		STREAM.add(this);
	}
	
	public void SetStatus(String status){
		this.status=status;
		STREAM.setStatus(this.streamID, this.status, this.updUser);
	}
}
