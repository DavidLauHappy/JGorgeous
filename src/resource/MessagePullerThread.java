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
			 Logger.getInstance().log("��Ϣ��ȡ���񣺳ɹ���������");
			   IMessage data=new IMessage();
			   while(this.runFlag){
			   		if(PopMsgView.getPopView().visiable){
			   			try {
							Thread.sleep(pollSencod);//ÿ30����ˢ���½���
						} catch(InterruptedException e) {
							Logger.getInstance().error("MessagePullerThread.run()��Ϣ֪ͨ�߳�˯���쳣��"+e.toString());
						}
			   		}else{
					   	msgs=data.getMessage(Context.session.userID);
					   	if(msgs!=null&&msgs.size()>0){
					   		Logger.getInstance().log("��Ϣ��ȡ���񣺹����С���");
					   		AppView.getInstance().getDisplay().asyncExec(new Runnable() {
								public void run() {
									PopMsgView.getPopView().show(msgs);
								}
					   		});
					   	}
					   	try {
							Thread.sleep(pollSencod);//ÿ30����ˢ���½���
						} catch(InterruptedException e) {
							Logger.getInstance().error("MessagePullerThread.run()��Ϣ֪ͨ�߳�˯���쳣��"+e.toString());
						}
					   	//�������� �������ͣ���������Ԥ������
					   	this.makeMyAlert();
			   		}
			   }
		 }
		 catch(Exception exp){
			 Logger.getInstance().error("MessagePullerThread.run()��Ϣ֪ͨ�߳������쳣��"+exp.toString());
		 }
		 finally{
			 Logger.getInstance().log("��Ϣ��ȡ���񣺷�������˳�����");
		 }
	  }
	 
	 //ÿ�������Լ�������
	 public boolean runable=true;
	 public String workDate="";
	 public void makeMyAlert(){
		 String nowDate=DateUtil.getCurrentDate("yyyyMMdd");
		 if(StringUtil.isNullOrEmpty(workDate)){
			 workDate=nowDate;
			 runable=true;
		 }
		 //���ڷ�����ת���������ɵ��յı�������
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
	    		 Logger.getInstance().log("��Ϣ��ȡ���񣺷�������˳�����");
	    	}
	    	catch(Exception e){
	    		Logger.getInstance().error("MessagePullerThread.exit()��Ϣ֪ͨ�˳��쳣��"+e.toString());
	    	}
	 }
	private static MessagePullerThread unique_instance;
	private boolean runFlag=false;
	private static int pollSencod=30000;   
	   
	private MessagePullerThread(){}

}
