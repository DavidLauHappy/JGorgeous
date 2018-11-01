package business.deploy.bean;

public class ReviewData {
	private String seq="";
	private String versionID="";
	private String opName="";
	private String groupName="";
	private String nodeName="";
	private String ip="";
	
	public ReviewData(String seq, String versionID, String opName,
			String groupName, String nodeName, String ip) {
		super();
		this.seq = seq;
		this.versionID = versionID;
		this.opName = opName;
		this.groupName = groupName;
		this.nodeName = nodeName;
		this.ip = ip;
	}

	public String getSeq() {
		return seq;
	}

	public String getVersionID() {
		return versionID;
	}

	public String getOpName() {
		return opName;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public String getIp() {
		return ip;
	}

	private String status="";
	private String result="";
    private String resultTime="";
	public String getStatus() {
		return status;
	}

	public String getResult() {
		return result;
	}

	public String getResultTime() {
		return resultTime;
	}

	//用于安装报告，增加了返回信息和返回状态
	public ReviewData(String seq, String versionID, String opName,
			String groupName, String nodeName, String ip, String status,
			String result, String resultTime) {
		super();
		this.seq = seq;
		this.versionID = versionID;
		this.opName = opName;
		this.groupName = groupName;
		this.nodeName = nodeName;
		this.ip = ip;
		this.status = status;
		this.result = result;
		this.resultTime = resultTime;
	}

}
