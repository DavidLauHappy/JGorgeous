package business.deploy.bean;

import utils.StringUtil;

public class CompareData {
	public enum Type{NewFile,NonEqual,Equal,Wired;}
	private String versionID="";
	private String groupName="";
	private String nodeName="";
	private String ip="";
	private String fileName="";
	private String MD5="";
	private String installMd5="";
	private String result="";
	public String type="";
	
	
	
	
	public CompareData(String versionID, String groupName, String nodeName,
			String ip, String fileName, String mD5, String installMd5) {
		super();
		this.versionID = versionID;
		this.groupName = groupName;
		this.nodeName = nodeName;
		this.ip = ip;
		this.fileName = fileName;
		this.MD5 = mD5;
		this.installMd5 = installMd5;
		if(!StringUtil.isNullOrEmpty(this.installMd5)&&!StringUtil.isNullOrEmpty(this.MD5)){
			if(!this.MD5.equals(this.installMd5)){
				this.result="不一致";
				this.type=Type.NonEqual.ordinal()+"";
			}
			else{
				this.result="一致";
				this.type=Type.Equal.ordinal()+"";
			}
		}
		else{
			if(StringUtil.isNullOrEmpty(this.MD5)&&!StringUtil.isNullOrEmpty(this.installMd5)){
				this.result="不一致";
				this.type=Type.NewFile.ordinal()+"";
			}
			if(StringUtil.isNullOrEmpty(this.installMd5)&&!StringUtil.isNullOrEmpty(this.MD5)){
				this.result="不一致";
				this.type=Type.Wired.ordinal()+"";
			}
		}
	}

	public String getVersionID() {
		return versionID;
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

	public String getFileName() {
		return fileName;
	}

	public String getMD5() {
		return MD5;
	}

	public String getInstallMd5() {
		return installMd5;
	}

	public String getResult() {
		return result;
	}


	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public void setMD5(String mD5) {
		MD5 = mD5;
	}


	public void setInstallMd5(String installMd5) {
		this.installMd5 = installMd5;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
