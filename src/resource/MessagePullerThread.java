package resource;

import java.util.List;



import utils.DateUtil;
import utils.StringUtil;
import views.AppView;
import views.PopMsgView;

/**
 * @author David
 *
 */
public class MessagePullerThread  extends Thread{
	public static MessagePullerThread getInstance(){
		if(unique_instance==null){
			unique_instance=new MessagePullerThread();
		}
		return unique_instance;
	}
	
	public void setRunFlag(boolean runable){
 	   this.runFlag=runable;
    }
	
	public  List<IMessage> msgs;
	 public void run(){
		 try{
			 Logger.getInstance().log("消息拉取服务：成功启动……");
			   IMessage data=new IMessage();
			   while(this.runFlag){
			   		if(PopMsgView.getPopView().visiable){
			   			try {
							Thread.sleep(pollSencod);//每30秒钟刷新下界面
						} catch(InterruptedException e) {
							Logger.getInstance().error("MessagePullerThread.run()消息通知线程睡眠异常："+e.toString());
						}
			   		}else{
					   	msgs=data.getMessage(Context.session.userID);
					   	if(msgs!=null&&msgs.size()>0){
					   		Logger.getInstance().log("消息拉取服务：工作中……");
					   		AppView.getInstance().getDisplay().asyncExec(new Runnable() {
								public void run() {
									PopMsgView.getPopView().show(msgs);
								}
					   		});
					   	}
					   	try {
							Thread.sleep(pollSencod);//每30秒钟刷新下界面
						} catch(InterruptedException e) {
							Logger.getInstance().error("MessagePullerThread.run()消息通知线程睡眠异常："+e.toString());
						}
					   	//生成提醒 提醒类型：开发任务预期提醒
					   	this.makeMyAlert();
			   		}
			   }
		 }
		 catch(Exception exp){
			 Logger.getInstance().error("MessagePullerThread.run()消息通知线程运行异常："+exp.toString());
		 }
		 finally{
			 Logger.getInstance().log("消息拉取服务：服务结束退出……");
		 }
	  }
	 
	 //每天生成自己的提醒
	 public boolean runable=true;
	 public String workDate="";
	 public void makeMyAlert(){
		 String nowDate=DateUtil.getCurrentDate("yyyyMMdd");
		 if(StringUtil.isNullOrEmpty(workDate)){
			 workDate=nowDate;
			 runable=true;
		 }
		 //日期发生跳转，重新生成当日的本人提醒
		if(nowDate.compareTo(workDate)>0){
			 workDate=nowDate;
			 runable=true;
		}
		if(runable){
			runable=false;
			IMessage.makeAlert(Context.session.userID, Context.TaskAlertAheadDays);
			
		}
	 }
	 public void exit(){
		 try{
	    		this.runFlag=false;
	    		unique_instance.interrupt();
	    		 Logger.getInstance().log("消息拉取服务：服务结束退出……");
	    	}
	    	catch(Exception e){
	    		Logger.getInstance().error("MessagePullerThread.exit()消息通知退出异常："+e.toString());
	    	}
	 }
	private static MessagePullerThread unique_instance;
	private boolean runFlag=false;
	private static int pollSencod=30000;   
	   
	private MessagePullerThread(){}

}
