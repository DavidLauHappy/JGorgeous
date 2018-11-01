package bean;

public class FUNCBean {
	public enum Type{NoCheck,LocalCheck,Approve;}
	private String id;
	private String name;
	private String isLog;
	private String isCheck;
	
	public FUNCBean(){}
	public FUNCBean(String id, String name, String isLog, String isCheck) {
		super();
		this.id = id;
		this.name = name;
		this.isLog = isLog;
		this.isCheck = isCheck;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIsLog() {
		return isLog;
	}

	public String getIsCheck() {
		return isCheck;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIsLog(String isLog) {
		this.isLog = isLog;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	
	
}
