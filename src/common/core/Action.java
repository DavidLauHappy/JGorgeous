package common.core;

public abstract class Action {
	public abstract String getResultInfo();
	public abstract boolean execute();
	public abstract String getInstallFileMd5();
	
	public enum CMD_TYPE{
		WinFileUpload, //Windows文件上传——>VService服务
		LnxFileUpload,//Linux文件上传——> sftp服务
		WinFileCopy, //Windows远程文件操作——>VService服务
		LnxFileCopy,//Linux远程文件操作——>sftp服务
		WinSvcCtrl, //Windows远程服务启停——>VService服务
		LnxSvcCtrl,//Linux远程服务启停——> sftp服务
	   DbExecute,//远程数据库脚本本地执行——>sqlplus/sqlcmd
	   DbBackup;//数据库备份——>本地数据库连接
}
}
