package business.deploy.action;

public abstract class LocalAction {
	public abstract String getResultInfo();
	public abstract String getResult();
	public abstract boolean execute();
	public abstract String getFileID();
	public abstract String getInstallFileMd5();
	public enum Status{Initial,//初始化状态
         Runnable,//可运行状态
         Scheduling,//调度中(受理完毕)
         Running,//执行中(发送完毕)
         TimeOut,//超时（发送指令时）
         ReturnOK,//执行成功
         ReturnNull,//执行无返回
         ReturnFailed;//执行失败
         }
}
