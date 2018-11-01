package business.deploy.action;

public abstract class LocalAction {
	public abstract String getResultInfo();
	public abstract String getResult();
	public abstract boolean execute();
	public abstract String getFileID();
	public abstract String getInstallFileMd5();
	public enum Status{Initial,//��ʼ��״̬
         Runnable,//������״̬
         Scheduling,//������(�������)
         Running,//ִ����(�������)
         TimeOut,//��ʱ������ָ��ʱ��
         ReturnOK,//ִ�гɹ�
         ReturnNull,//ִ���޷���
         ReturnFailed;//ִ��ʧ��
         }
}
