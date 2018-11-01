package common.core;

import resource.SubTask;
public class WinConsole {

		public WinConsole(){
			this.isDone=false;
		}
		
		private boolean isDone=false;
		private String outMsg;
		public boolean callCmd(String cmdLine){
			int iretCode = 0;
			if (null == cmdLine || "".equals(cmdLine)) {
				outMsg="ִ�нű�����command is null";
				return false;
			}
			Thread taskErr=null;
			Thread taskStt=null;
			SubTask stErr=null;
			SubTask stStt=null;
			Process process=null;
			try {
				//ע��Ҫ��  1����ʹ���߳���ͬʱ�����׼���ʹ����� ��Ϊ�޷�Ԥ֪˭�ȿ�ʼ�������
			                  //2��ȡ�߳��޷�֪�������Ƿ��Ѿ��رչܵ�����һֱ������Ҫ�����̵߳ȴ�������ȡ�̵߳Ľ���
				  process = Runtime.getRuntime().exec(cmdLine);
				  stErr=new SubTask(process.getErrorStream());
				  stStt=new SubTask(process.getInputStream());
				  taskErr=new Thread(stErr);
				  taskStt=new Thread(stStt);
				  taskErr.start();
				  taskStt.start();
			     iretCode = process.waitFor();
			     String retErrMsg= stErr.getInput();
			     String retSttMsg=stStt.getInput();
			     if(retErrMsg!=null){
			    	 outMsg="ִ�нű�����"+"execute shell " + cmdLine + " failed! errorMsg:"+ retErrMsg+" iretCode:"+iretCode;
			    	 return false;
			     }
			    if (iretCode != 0) {
			    	 outMsg="ִ�нű�����"+"execute shell " + cmdLine + " failed for return code: "+ iretCode;
			    	 return false;
			     }
				
			} catch (Exception e) {
				 outMsg="ִ�нű�����"+"ִ�� " + cmdLine + " ϵͳI/O�쳣��" + e.toString();
				 return false;
			}
			finally {
				try {
				    taskErr.join();
					taskStt.join();
					stErr.getInputStream().close();
					stStt.getInputStream().close();
					this.isDone=true;
				 } 
				catch (Exception e) {
						outMsg="ִ�нű�����"+"ִ�� " + cmdLine + " ����ϵͳ��Դ�쳣��" + e.toString();
						this.isDone=true;
						return false;
					}
				} 
			return true;
		}
		
		public String getOutMsg(){
			return this.outMsg;
		}
		
		public boolean isDone(){
			return this.isDone;
		}
}
