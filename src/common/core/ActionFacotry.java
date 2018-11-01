package common.core;

import bean.LOCALCOMMANDBean;

public class ActionFacotry {
	 public static Action getAction(LOCALCOMMANDBean cmd){
		 Action action=null;
		 String type=cmd.getCmdType();
		 if((Action.CMD_TYPE.WinFileUpload.ordinal()+"").equals(type)){
			 action=new WinFileUploadAction(cmd);
		 }else if((Action.CMD_TYPE.WinFileCopy.ordinal()+"").equals(type)){
			 action=new WinFileCopyAction(cmd);
		 }else if((Action.CMD_TYPE.WinSvcCtrl.ordinal()+"").equals(type)){
			 action=new WinServiceCtrlAction(cmd);
		 }else if((Action.CMD_TYPE.LnxFileUpload.ordinal()+"").equals(type)){
			 action=new LnxFileUploadAction(cmd);
		 }else if((Action.CMD_TYPE.LnxFileCopy.ordinal()+"").equals(type)){
			 action=new LnxFileCopyAction(cmd);
		 }else if((Action.CMD_TYPE.LnxSvcCtrl.ordinal()+"").equals(type)){
			 action=new LnxServiceCtrlAction(cmd);
		 }else if((Action.CMD_TYPE.DbBackup.ordinal()+"").equals(type)){
			 action=new DBBackupAction(cmd);
		 }else if((Action.CMD_TYPE.DbExecute.ordinal()+"").equals(type)){
			 action=new DBRunScriptAction(cmd);
		 }
		 return action;
	 }
}
