package business.deploy.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.core.LnxShell;
import common.core.WinShell;

import model.SYSTEM;
import resource.Context;
import resource.Logger;
import utils.StringUtil;
import bean.COMMANDBean;
import bean.NODEBean;
import bean.SYSTEMBean;
import business.deploy.bean.TransData;

public class ServiceControlAction extends LocalAction {

	public enum ServiceMode{Start,Kill;}
	private String resultInfo="";
	private String result="";
	public  String getResultInfo(){
		return resultInfo;
	}
	public  String getResult(){
		return result;
	}
	private String fileID;
	public  String getFileID(){
		return fileID;
	}
	public String odlMD5="";
	public String getInstallFileMd5(){
		return this.odlMD5;
	}


	private COMMANDBean cmd;
	private NODEBean node;
	public ServiceControlAction(COMMANDBean data,NODEBean bean){
		this.cmd=data;
		this.node=bean;
	}
	
	//Linux�ϵķ�����ֻͣ�õ�path���ԣ��þ���·��������sh
	public boolean execute(){
		COMMANDBean.Status status=null;
		TransData tansLoger=new TransData();
		SYSTEMBean system= SYSTEM.getSystemsByID(this.node.getSystemID(), Context.session.currentFlag);
		tansLoger.setId(this.cmd.getId());
		tansLoger.setApp(system.getApp());
		tansLoger.setSystemID(system.getBussID());
		tansLoger.setNodeID(this.node.getId());
		tansLoger.setFuncID("F-999-00005");
		 boolean result=false;
		String fileOperation="";
        try{
			 Map<String,String> parameters=this.cmd.getParameters();
			 String exeName=parameters.get("ROUTINE_NAME");
			 String batPath=parameters.get("PATH");
			 String mode=parameters.get("MODE");
			 String time=parameters.get("TIME");
			 String runPath=parameters.get("RUN_PATH");
			  String ip=this.node.getIp();//parameters.get("IP");
			  String detail="��["+ip+"]��ִ�з�����ͣ[������:"+exeName+"]";
			  if((ServiceControlAction.ServiceMode.Start.ordinal()+"").equals(mode)){
				   detail+="����";  
			  }else{
				  detail+="ֹͣ";  
			  }
			  tansLoger.setDetail(detail);
			  tansLoger.inroll();
			  String os=node.getOs();
			  if((NODEBean.OS.Linux.ordinal()+"").equals(os)){
				  //����Ŀ¼ִ��sh
				  String cmd="@sh";
				  cmd=cmd.replace("@sh", batPath);
				  LnxShell shell= LnxShell.getShell(this.node);
				   List<String> cmdLines=new ArrayList<String>();
				   cmdLines.add(cmd);
				   result=shell.executeCommand(cmdLines);
				   if(result){
						this.result=LocalAction.Status.ReturnOK.ordinal()+"";
						this.resultInfo="��ͣ����["+batPath+"]�ɹ�ִ��";
						status=COMMANDBean.Status.ReturnOK;
						String info=shell.getResponse();
						Logger.getInstance().debug(info);
						 result=true;
				   } else{
						 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
						 String  error=shell.getResponse();
				    	 this.resultInfo="��ͣ����["+batPath+"]ִ��]ʧ�ܡ�ʧ��ԭ��"+error;
				    	 Logger.getInstance().error(error);
					 }
			  }else{
				    WinShell winShell=WinShell.getShell(ip);
				    if(winShell!=null){
				    	   boolean ret=winShell.executeCommand(this.cmd.getParameter(),true);
				    	  if(ret){
					    		String response=winShell.getResponse();
					    		int index=response.indexOf("|");
					   		 	String xmlStr=response.substring(index+1);
					   		 	Map<String,String> retMap=StringUtil.parseXML(xmlStr);
					   		 	String outMsg=retMap.get("OUT_MSG");
					   		 	String outCode=retMap.get("OUT_CODE");
						   		 if("0".equals(outCode)){
						   			this.result=LocalAction.Status.ReturnOK.ordinal()+"";
									this.resultInfo="��ͣ����["+batPath+"]�ɹ�ִ��";
									status=COMMANDBean.Status.ReturnOK;
									String info=outCode;
									Logger.getInstance().debug(info);
									 result=true;
						   		 	}
						   		 else{
						   		 	 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
									 String  error=outMsg;
							    	 this.resultInfo="��ͣ����["+batPath+"]ִ��]ʧ�ܡ�ʧ��ԭ��"+error;
							    	 Logger.getInstance().error(error);
						   		 }
				    	}
				   else{
				    		 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
							 String  error=winShell.getResponse();
					    	 this.resultInfo="��ͣ����["+batPath+"]ִ��]ʧ�ܡ�ʧ��ԭ��"+error;
					    	 Logger.getInstance().error(error);
				    	}
				     }
				 else{
				    	 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
						 String  error="�������["+ip+"]��VService�����Ƿ������������������";
				    	 this.resultInfo="��ͣ����["+batPath+"]ִ��]ʧ�ܡ�ʧ��ԭ��"+error;
				    	 Logger.getInstance().error(error);
				    }
			  }
		}
        catch(Exception exp){
			this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
			this.resultInfo=exp.toString();
			status=COMMANDBean.Status.ReturnFailed;
			result=false;
		}
        tansLoger.setEndTime();
		 this.cmd.setExecuteInfo(this.result, this.resultInfo, COMMANDBean.Remind.No.ordinal()+"");
		 return result;
	}
}
