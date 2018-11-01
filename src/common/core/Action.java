package common.core;

public abstract class Action {
	public abstract String getResultInfo();
	public abstract boolean execute();
	public abstract String getInstallFileMd5();
	
	public enum CMD_TYPE{
		WinFileUpload, //Windows�ļ��ϴ�����>VService����
		LnxFileUpload,//Linux�ļ��ϴ�����> sftp����
		WinFileCopy, //WindowsԶ���ļ���������>VService����
		LnxFileCopy,//LinuxԶ���ļ���������>sftp����
		WinSvcCtrl, //WindowsԶ�̷�����ͣ����>VService����
		LnxSvcCtrl,//LinuxԶ�̷�����ͣ����> sftp����
	   DbExecute,//Զ�����ݿ�ű�����ִ�С���>sqlplus/sqlcmd
	   DbBackup;//���ݿⱸ�ݡ���>�������ݿ�����
}
}
