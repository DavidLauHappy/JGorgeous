package business.deploy.bean;

public class ApproveData {
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public enum Status{Init,Approving,ApproveOK;}
	private String id;//审批编号
	private String status;//审批状态
	private String statusDesc;//审批状态描述
	private String comment;//审批意见
	private String approveTime;//审批时间
	
	public ApproveData(String id, String status,String time) {
		super();
		this.id = id;
		this.status = status;
		this.approveTime=time;
	}
	public String getId() {
		return id;
	}
	public String getStatus() {
		return status;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public String getComment() {
		return comment;
	}
	public String getApproveTime() {
		return approveTime;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}
	
	

}
