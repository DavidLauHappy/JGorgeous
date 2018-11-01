package common.core;

import java.util.Map;

import utils.StringUtil;
import bean.LOCALCOMMANDBean;

public class WinServiceCtrlAction extends  Action{
	private LOCALCOMMANDBean cmd;
	public WinServiceCtrlAction(LOCALCOMMANDBean cmd){
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
			   String ip=parameters.get("IP");
			   WinShell winShell=WinShell.getShell(ip);
			    if(winShell!=null){
			    	boolean result=winShell.executeCommand(cmd.getCmdText(),true);
			    	if(result){
			    		String response=winShell.getResponse();
			    		int index=response.indexOf("|");
			   		 	String xmlStr=response.substring(index+1);
			   		 	Map<String,String> retMap=StringUtil.parseXML(xmlStr);
			   		 	String outMsg=retMap.get("OUT_MSG");
			   		 	this.resultInfo=outMsg;
			   		 	String outCode=retMap.get("OUT_CODE");
				   		 if("0".equals(outCode)){
				   		 		return true;
				   		 	}else{
				   		 		return false;
				   		 	}
			    	}else{
			    		this.resultInfo=winShell.getResponse();
			    		return false;
			    	}
			    }else{
			    	this.resultInfo="请检查机器["+ip+"]上VService服务是否正常部署或正常启动";
			    	return false;
			    }
		   }catch(Exception e){
			   this.resultInfo=e.toString();
			   return false;
		   }
	}

}
