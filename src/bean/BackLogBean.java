package bean;

//ԭʼ����
public class BackLogBean {
		public enum Status{Submit,//�����
									DeptAudit,//���������
									Apprise,//����������
									Review,//����������
									Dispatch,//������
									Develop,//����ʵʩ��
									TestDispath,//���Է�����
									Test,//����ʵʩ��
									TestAudit,//���������
									Package,//�汾������
									Online,//������
									Vertify,//����
									Over;//����
									}
		private String id;
		private String submitUser;//����� ������û����Ǳ�ż��û���
		private String submitDept;
		private String submitDate;
		private String inrollUser;
		private String name;
		private String background;
		private String expectDesc;
		private String expectDate;
		private String dateReason;
		private String checkUser;//�����
		private String auditUser;//������
		private String comment;
		private String source;//������Դ
		private String type;
		private String rclass;
		private String status;
		private String Link;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSubmitUser() {
			return submitUser;
		}
		public void setSubmitUser(String submitUser) {
			this.submitUser = submitUser;
		}
		public String getSubmitDept() {
			return submitDept;
		}
		public void setSubmitDept(String submitDept) {
			this.submitDept = submitDept;
		}
		public String getSubmitDate() {
			return submitDate;
		}
		public void setSubmitDate(String submitDate) {
			this.submitDate = submitDate;
		}
		public String getInrollUser() {
			return inrollUser;
		}
		public void setInrollUser(String inrollUser) {
			this.inrollUser = inrollUser;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getBackground() {
			return background;
		}
		public void setBackground(String background) {
			this.background = background;
		}
		public String getExpectDesc() {
			expectDesc=expectDesc.replace("?", "");
			return expectDesc;
		}
		public void setExpectDesc(String expectDesc) {
			this.expectDesc = expectDesc;
		}
		public String getExpectDate() {
			return expectDate;
		}
		public void setExpectDate(String expectDate) {
			this.expectDate = expectDate;
		}
		public String getDateReason() {
			return dateReason;
		}
		public void setDateReason(String dateReason) {
			this.dateReason = dateReason;
		}
		public String getCheckUser() {
			return checkUser;
		}
		public void setCheckUser(String checkUser) {
			this.checkUser = checkUser;
		}
		public String getAuditUser() {
			return auditUser;
		}
		public void setAuditUser(String auditUser) {
			this.auditUser = auditUser;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getRclass() {
			return rclass;
		}
		public void setRclass(String rclass) {
			this.rclass = rclass;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getLink() {
			return Link;
		}
		public void setLink(String link) {
			Link = link;
		}
		
		
		
}
