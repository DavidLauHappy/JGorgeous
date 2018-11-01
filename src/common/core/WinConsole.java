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
				outMsg="执行脚本错误：command is null";
				return false;
			}
			Thread taskErr=null;
			Thread taskStt=null;
			SubTask stErr=null;
			SubTask stStt=null;
			Process process=null;
			try {
				//注意要点  1必须使用线程来同时处理标准流和错误流 因为无法预知谁先开始或结束，
			                  //2读取线程无法知道进程是否已经关闭管道，会一直读，需要让主线程等待两个读取线程的结束
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
			    	 outMsg="执行脚本错误："+"execute shell " + cmdLine + " failed! errorMsg:"+ retErrMsg+" iretCode:"+iretCode;
			    	 return false;
			     }
			    if (iretCode != 0) {
			    	 outMsg="执行脚本错误："+"execute shell " + cmdLine + " failed for return code: "+ iretCode;
			    	 return false;
			     }
				
			} catch (Exception e) {
				 outMsg="执行脚本错误："+"执行 " + cmdLine + " 系统I/O异常：" + e.toString();
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
						outMsg="执行脚本错误："+"执行 " + cmdLine + " 回收系统资源异常：" + e.toString();
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
