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
				System.out.print("ִ������["+cmdline+"]ʱ���񵽴�������Ϣ��"+errMsg);
				Logger.getInstance().error("ִ������["+cmdline+"]ʱ���񵽴�������Ϣ��"+errMsg);
			    return false;
			} 
		} catch (Exception e) {
			System.out.print("ִ������["+cmdline+"]ʱ��������"+e.getMessage());
			Logger.getInstance().error("ִ������["+cmdline+"]ʱ��������"+e.getMessage());
			return false;			
		}
	}
	
	public  static void execute(String command,DBRunAction action) {
		// �ű��ļ�ΪNULL���ֵ
		int iretCode = 0;
		if (null == command || "".equals(command)) {
			action.setResult(LocalAction.Status.ReturnFailed.ordinal()+"");
			action.setResultInfo("ִ�нű�����command is null");
		}
		Thread taskErr=null;
		Thread taskStt=null;
		SubTask stErr=null;
		SubTask stStt=null;
		Process process=null;
		try {
			//ע��Ҫ��  1����ʹ���߳���ͬʱ�����׼���ʹ����� ��Ϊ�޷�Ԥ֪˭�ȿ�ʼ�������
		                  //2��ȡ�߳��޷�֪�������Ƿ��Ѿ��رչܵ�����һֱ������Ҫ�����̵߳ȴ�������ȡ�̵߳Ľ���
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
				 action.setResultInfo("ִ�нű�����"+"execute shell " + command + " failed! errorMsg:"+ retErrMsg+" iretCode:"+iretCode);
		    }
		    if (iretCode != 0) {
		    	 action.setResult(LocalAction.Status.ReturnFailed.ordinal()+"");
		    	 action.setResultInfo("ִ�нű�����"+"execute shell " + command + " failed for return code: "+ iretCode);
		     }
			
		} catch (Exception e) {
			 action.setResult(LocalAction.Status.ReturnFailed.ordinal()+"");
			 action.setResultInfo("ִ�нű�����"+"ִ�� " + command + " ϵͳI/O�쳣��" + e.toString());
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
					action.setResultInfo("ִ�нű�����"+"ִ�� " + command + " ����ϵͳ��Դ�쳣��" + e.toString());
				}
				action.setDoneFlag(true);
			} 
		}
	}
}
