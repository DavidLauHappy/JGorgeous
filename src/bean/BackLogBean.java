package bean;

//原始需求
public class BackLogBean {
		public enum Status{Submit,//提出中
									DeptAudit,//部门审核中
									Apprise,//需求评估中
									Review,//需求评审中
									Dispatch,//待分派
									Develop,//开发实施中
									TestDispath,//测试分派中
									Test,//测试实施中
									TestAudit,//测试审核中
									Package,//版本制作中
									Online,//已上线
									Vertify,//验收
									Over;//结束
									}
		private String id;
		private String submitUser;//提出人 这里的用户都是编号加用户名
		private String submitDept;
		private String submitDate;
		private String inrollUser;
		private String name;
		private String background;
		private String expectDesc;
		private String expectDate;
		private String dateReason;
		private String checkUser;//审核人
		private String auditUser;//验收人
		private String comment;
		private String source;//需求来源
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
