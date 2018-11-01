package business.deploy.bean;

import model.NODE;
import model.STEP;
import resource.Constants;
import resource.Context;
import bean.NODEBean;
import bean.NODEDIRBean;
import bean.NODESERVICEBean;
import bean.STEPBean;
import business.deploy.core.ControlCommand;
import utils.LocalDBUtil;
import utils.StringUtil;

public class StrategyData {
	private String version;
	private String stepID;
	private String stepName;
	private String stepComp;
	private String nodeID;
	private String nodeName;
	private String nodeIp;
	private String groupID;
	private String flag;
	////////////////////////////////////
	private String targetPath;
	//////////////////////////////////////
	private String showNode;
	
	public StrategyData(){}
	public StrategyData(String version, String stepID, String stepName,
			String nodeID, String nodeName, String nodeIp, String groupID,
			String flag,String stepComp) {
		super();
		this.version = version;
		this.stepID = stepID;
		this.stepName = stepName;
		this.nodeID = nodeID;
		this.nodeName = nodeName;
		this.nodeIp = nodeIp;
		this.groupID = groupID;
		this.flag = flag;
		this.showNode=this.nodeName+"("+this.nodeIp+")";
		this.stepComp=stepComp;
		this.targetPath=getStepDstPath(this.nodeID,this.stepComp);
	}
	
	private String getStepDstPath(String nodeID,String stepName){
		String result="";
		try{
			NODEBean node=NODE.getNodeByID( Context.session.userID, nodeID, Context.session.currentFlag);
			if(Constants.StepUpload.equals(stepName)){
				result=ControlCommand.getNodeBasePath(node);
				return result;
			}
			STEPBean step=STEP.getPkgSteps(this.version, this.stepID);
			String comName=StringUtil.ltrim(stepName, "/");
			if((STEPBean.ActionType.ServiceStart.ordinal()+"").equals(step.getAction())){
				result=((NODESERVICEBean)node.getServices().get(comName)).getStart();
			}else if((STEPBean.ActionType.ServiceStop.ordinal()+"").equals(step.getAction())){
				result=((NODESERVICEBean)node.getServices().get(comName)).getStop();
			}
			else if("0".equals(step.getBackupFlag())){
				String base=ControlCommand.getNodeBasePath(node);
				String bakPath=Constants.backVersionDir;
				bakPath=bakPath.replace("@base", base);
				bakPath=bakPath.replace("@nodeid", nodeID);
				bakPath=bakPath.replace("@pkgid", this.version);
				bakPath=bakPath.replace("@stepName", stepName);
				result=bakPath;
				return result;
			}else{
					result=((NODEDIRBean)node.getDirs().get(comName)).getDirValue();
			}
		}catch(Exception e){
			
		}
		return result;
	}
	public String getVersion() {
		return version;
	}
	public String getStepID() {
		return stepID;
	}
	public String getStepName() {
		return stepName;
	}
	public String getNodeID() {
		return nodeID;
	}
	public String getNodeName() {
		return nodeName;
	}
	public String getNodeIp() {
		return nodeIp;
	}
	public String getGroupID() {
		return groupID;
	}
	public String getFlag() {
		return flag;
	}
	public String getShowNode() {
		return showNode;
	}
	
	
	public String getTargetPath() {
		return targetPath;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setStepID(String stepID) {
		this.stepID = stepID;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public void setStepComp(String stepComp) {
		this.stepComp = stepComp;
	}

}
