package utils;

import java.io.InputStream;

import business.deploy.action.DBRunAction;
import business.deploy.action.LocalAction;


import resource.Logger;
import resource.SubTask;


public class ShellUtil {
	
	public static boolean command(String cmdline) {		
		return callCmd(cmdline);
	}

	private static boolean callCmd(String cmdline) {
		Runtime app = Runtime.getRuntime();
		Process proc = null;
		try {
			proc = app.exec(cmdline);
			new Thread(new SubTask(proc.getInputStream())).start();
			new Thread(new SubTask(proc.getErrorStream())).start();			
			proc.waitFor();
			InputStream errStream = proc.getErrorStream();
			int size = errStream.available();
			if (size == 0) {
				app.runFinalization();
				return true;
			} else {
				StringBuffer errMsg = new StringBuffer();
				byte[] bytes = new byte[size];
				while ((errStream.read(bytes, 0, bytes.length)) != -1) {
					errMsg = errMsg.append(new String(bytes));
    			}
				System.out.print("执行命令["+cmdline+"]时捕获到错误流信息："+errMsg);
				Logger.getInstance().error("执行命令["+cmdline+"]时捕获到错误流信息："+errMsg);
			    return false;
			} 
		} catch (Exception e) {
			System.out.print("执行命令["+cmdline+"]时发生错误："+e.getMessage());
			Logger.getInstance().error("执行命令["+cmdline+"]时发生错误："+e.getMessage());
			return false;			
		}
	}
	
	public  static void execute(String command,DBRunAction action) {
		// 脚本文件为NULL或空值
		int iretCode = 0;
		if (null == command || "".equals(command)) {
			action.setResult(LocalAction.Status.ReturnFailed.ordinal()+"");
			action.setResultInfo("执行脚本错误：command is null");
		}
		Thread taskErr=null;
		Thread taskStt=null;
		SubTask stErr=null;
		SubTask stStt=null;
		Process process=null;
		try {
			//注意要点  1必须使用线程来同时处理标准流和错误流 因为无法预知谁先开始或结束，
		                  //2读取线程无法知道进程是否已经关闭管道，会一直读，需要让主线程等待两个读取线程的结束
			  process = Runtime.getRuntime().exec(command);
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
		    	 action.setResult(LocalAction.Status.ReturnFailed.ordinal()+"");
				 action.setResultInfo("执行脚本错误："+"execute shell " + command + " failed! errorMsg:"+ retErrMsg+" iretCode:"+iretCode);
		    }
		    if (iretCode != 0) {
		    	 action.setResult(LocalAction.Status.ReturnFailed.ordinal()+"");
		    	 action.setResultInfo("执行脚本错误："+"execute shell " + command + " failed for return code: "+ iretCode);
		     }
			
		} catch (Exception e) {
			 action.setResult(LocalAction.Status.ReturnFailed.ordinal()+"");
			 action.setResultInfo("执行脚本错误："+"执行 " + command + " 系统I/O异常：" + e.toString());
		}
		finally {
			try {
			    taskErr.join();
				taskStt.join();
				stErr.getInputStream().close();
				stStt.getInputStream().close();
				action.setDoneFlag(true);
			} catch (Exception e) {
				if(StringUtil.isNullOrEmpty(action.getResult())){
					action.setResult(LocalAction.Status.ReturnFailed.ordinal()+"");
					action.setResultInfo("执行脚本错误："+"执行 " + command + " 回收系统资源异常：" + e.toString());
				}
				action.setDoneFlag(true);
			} 
		}
	}
}
