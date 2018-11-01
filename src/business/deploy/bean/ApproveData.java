package business.deploy.bean;

public class ApproveData {
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public enum Status{Init,Approving,ApproveOK;}
	private String id;//�������
	private String status;//����״̬
	private String statusDesc;//����״̬����
	private String comment;//�������
	private String approveTime;//����ʱ��
	
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
