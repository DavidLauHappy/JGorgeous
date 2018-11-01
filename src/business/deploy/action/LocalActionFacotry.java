package business.deploy.action;

import common.core.Protocol;

import resource.Logger;
import bean.COMMANDBean;
import bean.NODEBean;



public class LocalActionFacotry {
	  public static LocalAction createAction(COMMANDBean cmd,NODEBean node){
		  LocalAction action=null;
    	 String cmdType=cmd.getCmdType();
    	 //String remote=cmd.getRemote();
    	 if(Protocol.ScriptExecute.equals(cmdType)){
    		 action=new DBRunAction(cmd,node);
    	 }
    	 else if(Protocol.SqlserverBackup.equals(cmdType)){
      		  action=new DBBackupAction(cmd,node);
    	 }
    	 else if(Protocol.UploadPKg.equals(cmdType)){
    		 action=new UploadPKGAction(cmd,node);
    	 } 
    	 else if(Protocol.FileCopy.equals(cmdType)){
    		 action=new FileCopyAction(cmd,node);
    	 }
    	 else if(Protocol.ServiceControl.equals(cmdType)){
    		 action=new ServiceControlAction(cmd,node);
    	 }
    	 else if(Protocol.DeleteFile.equals(cmdType)){
    		 action=new FileBackupAction(cmd,node);
    	 }
    	 else{
    		 Logger.getInstance().error("本地不支持的命令:"+cmd.getId());
    	 }
    	 return action;
	  }
}
