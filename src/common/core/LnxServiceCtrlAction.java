package common.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.LOCALNODE;
import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;

public class LnxServiceCtrlAction extends  Action{
	private LOCALCOMMANDBean cmd;
	public LnxServiceCtrlAction(LOCALCOMMANDBean cmd){
		this.cmd=cmd;
	}
	
	private String resultInfo="";
	public  String getResultInfo(){
		return resultInfo;
	}

	public String installedMD5="";
	public String getInstallFileMd5(){
		return this.installedMD5;
	}
	
	public boolean execute(){
		  try{
			   	 Map<String,String> parameters=cmd.getParameters();
				 String exeName=parameters.get("ROUTINE_NAME");
				 String batPath=parameters.get("PATH");
				 String mode=parameters.get("MODE");
				 String time=parameters.get("TIME");
				 String runPath=parameters.get("RUN_PATH");
				 String nodeID=parameters.get("NODE_ID");
				 LOCALNODEBean node=LOCALNODE.getNode(nodeID);
				 String detail="在["+node.getIp()+"]上执行服务("+exeName+")";
				  if("0".equals(mode)){
					   detail+="启动";  
				  }else{
					  detail+="终止";  
				  }
				  String cmd="@sh";
				  cmd=cmd.replace("@sh", batPath);
				  LnxShell shell=LnxShell.getShell(node);
				 if(shell!=null){
					  List<String> cmdLines=new ArrayList<String>();
					   cmdLines.add(cmd);
					   boolean result=shell.executeCommand(cmdLines);
					   String info=shell.getResponse();
					   if(result){
						  this.resultInfo=detail+"成功完成";
						  return true;
					   }else{
						   this.resultInfo=detail+"异常："+info;
						   return false;
					   }
				 }
		  }catch(Exception e){
			   this.resultInfo=e.toString();
			   return  false;
		   }
		   return true;
	}

}
