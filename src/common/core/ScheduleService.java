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
 * ��״̬���� ȡ������ָ�����尴��ִ�У�ִ���е�
 * ���Ұ汾���+�ڵ㣬������͵��ţ��Ҿ���������һ�ĵڶ��������
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
    		    				 Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"����˯�߹��̷����쳣��"+e.toString());
    		    			 }
    		          }else{  
    		           Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"��������,���Ȱ汾:"+this.versionID);
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
    		        		   			Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"��������,���Ȱ汾:"+this.versionID+"ָ��["+cmd.getCmdID()+"]ִ�г���"+action.getResultInfo());
    		        		   			break;
    		        		   		}else{
    		        		   		   cmd.setRetunInfo(LOCALCOMMANDBean.Status.ReturnOK.ordinal()+"", outMsg, LOCALCOMMANDBean.Remind.Ok.ordinal()+"");
    		        		   		   current++;
									   continue; 
    		        		   		}
    		        	   }
    		        	   if(current>=count){
    		        		    //�ͷű��ؽڵ���Դ�޸Ľڵ�״̬
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
    		        		   Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"��������,���Ȱ汾:"+this.versionID+"ȫ���������");
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
			Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"����������˳�����");
		}catch(Exception e){
			Logger.getInstance().serviceLog(Context.ServiceSchedulerQuick,"�����˳��쳣:"+e.toString());
		}
    }
    
}
