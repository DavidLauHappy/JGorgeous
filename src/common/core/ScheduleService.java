package common.core;

import java.util.List;

import model.LOCALCOMMAND;
import model.LOCALNODE;

import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;

import resource.Context;
import resource.Logger;
import utils.StringUtil;

/**
 * @author Administrator
 * 无状态服务 取出所有指令整体按序执行，执行中的
 * 给我版本编号+节点，我做完就等着，我就是万中无一的第二层调度者
 */
public class ScheduleService extends  Thread{
	
	private static ScheduleService unique_instance;
	private String versionID;
    private boolean runFlag=true;
    private boolean busyFlag=false;
    private boolean workable=true;
    private String nodeID;
	public static ScheduleService getInstance(){
		if(unique_instance==null)
			unique_instance=new ScheduleService();
		return unique_instance;
	}
	
	private ScheduleService(){
		runFlag=true;
		this.start();
	}
    
	public synchronized boolean setVersion(String versionID,String nodeID){
		if(this.busyFlag)
			return false;
		this.rework(versionID,nodeID);
		return true;
	}
	
	public synchronized void rework(String versionID,String nodeID){
		this.versionID=versionID;
		this.nodeID=nodeID;
		this.busyFlag=true;
		this.workable=true;
	}
	
	public synchronized void clearWork(String versionID,String nodeID){
		this.versionID="";
		this.nodeID="";
		this.busyFlag=false;
		this.workable=false;
	}
	
	
    
    public void run(){
    	while(this.runFlag){
    		          if(!this.workable){
    		        	  try{
    		        		  this.sleep(1000);
    		        	   }
    		        	  catch(Exception e){
    		    				 Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"调度睡眠过程发生异常："+e.toString());
    		    			 }
    		          }else{  
    		           Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"服务启动,调度版本:"+this.versionID);
    		           List<LOCALCOMMANDBean> cmds=LOCALCOMMAND.getScheduleComs(this.versionID);
    		           if(cmds!=null&&cmds.size()>0){
    		        	   int count=cmds.size();
    		        	   int current=0;
    		        	   for(LOCALCOMMANDBean cmd:cmds){
    		        		   		cmd.setStatus(LOCALCOMMANDBean.Status.Scheduling.ordinal()+"");
    		        		   		 Action action=ActionFacotry.getAction(cmd);
    		        		   		 boolean result=action.execute();
    		        		   		 String outMsg=action.getResultInfo();
    		        		   		 String md5=action.getInstallFileMd5();
    		        		   		 if(!StringUtil.isNullOrEmpty(md5)){
    		        		   			cmd.reSetInstalled(md5);
    		        		   		 }
    		        		   		if(!result){
    		        		   			cmd.setRetunInfo(LOCALCOMMANDBean.Status.ReturnFailed.ordinal()+"", outMsg, LOCALCOMMANDBean.Remind.Ok.ordinal()+"");
    		        		   			this.workable=false;
    		        		   			Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"服务启动,调度版本:"+this.versionID+"指令["+cmd.getCmdID()+"]执行出错："+action.getResultInfo());
    		        		   			break;
    		        		   		}else{
    		        		   		   cmd.setRetunInfo(LOCALCOMMANDBean.Status.ReturnOK.ordinal()+"", outMsg, LOCALCOMMANDBean.Remind.Ok.ordinal()+"");
    		        		   		   current++;
									   continue; 
    		        		   		}
    		        	   }
    		        	   if(current>=count){
    		        		    //释放本地节点资源修改节点状态
    		        		    if(!StringUtil.isNullOrEmpty(this.nodeID)){
    		        		    	 LOCALNODEBean node=LOCALNODE.getNode(nodeID);
    		        		    	 if("1".equals(node.getOs())){
    		        		    		  LnxShell  shell=LnxShell.getShell(node);
    		        		    		  if(shell!=null){
    		        		    			  shell.closeConnection(this.nodeID);
    		        		    		  }
    		        		    	 }else{
    		        		    		 WinShell shell=WinShell.getShell(node.getIp());
    		        		    		 if(shell!=null){
    		        		    			 shell.exit();
    		        		    		 }
    		        		    	 }
    		        		    }
    		        		   Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"服务启动,调度版本:"+this.versionID+"全部调度完成");
    		        	       this.clearWork(this.versionID,this.nodeID);
    		        	   }
    		           }else{
    		        	   this.workable=false;
    		           }
    			 }
    	}
    }

    public synchronized void exit(){
		try{
			this.runFlag=false;
			this.interrupt();
			Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"服务结束并退出……");
		}catch(Exception e){
			Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"服务退出异常:"+e.toString());
		}
    }
    
}
