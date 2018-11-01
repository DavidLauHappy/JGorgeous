package business.deploy.core;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import common.db.DataHelper;

import model.NODE;

import resource.Context;
import resource.Logger;
import bean.NODEBean;
import bean.PKGSYSTEMBean;

/**
 * @author DavidLau
 * 第一层调度
 */
public class PkgSystemScheduler  extends  Thread{
	
	public static PkgSystemScheduler getInstance(){
		if(unqueInstance==null)
			unqueInstance=new PkgSystemScheduler();
		return unqueInstance;
	}
	
	public synchronized boolean setTask(PKGSYSTEMBean pkgSystem){
		if(this.busyFlag)
			return false;
		else{
			this.nodeMsg.clear();
			this.groupData=pkgSystem;
			this.groupData.setStatus(PKGSYSTEMBean.Status.Busy.ordinal()+"");
			this.canWorkFlag=true;
			this.busyFlag=true;
			return true;
		}
	}
	
	public synchronized void releaseNode(String nodeID){
		this.nodeMsg.put(nodeID, true);
	}
	
	public synchronized void blockPkgSystem(){
		 this.groupData.setStatus(PKGSYSTEMBean.Status.Halt.ordinal()+"");
	}
	
	public synchronized String getCurrVersionID(){
		return this.groupData.getPkgID();
	}
	
	public void run(){
		try{
			 while(this.runFlag){
				   if(!this.canWorkFlag){
					   try{
							this.sleep(1*1000L);
					   }catch(Exception e){
							Logger.getInstance().serviceLog(Context.ServiceScheduler,"调度睡眠过程发生异常："+e.toString());
							Logger.getInstance().error("调度服务["+"]PkgSystemScheduler.run()调度睡眠过程发生异常："+e.toString());
						}
				   }else{
					   Logger.getInstance().serviceLog(Context.ServiceScheduler,"服务对象：系统["+this.groupData.getSystemID()+"]版本["+this.groupData.getPkgID()+"]工作中……");
				        List<String> nodeIDs=this.groupData.getNodes();
				        if(nodeIDs!=null&&nodeIDs.size()>0){
				        	for(String nodeID:nodeIDs){
				        		 this.nodeMsg.put(nodeID, false);
				        		 if(!this.exitFlag){
				        			 	boolean loop=true;
						        		while(loop){
							        		 NODEBean node=NODE.getNodeByID(Context.session.userID, nodeID,  Context.session.currentFlag);
							        			 boolean schResult=PkgNodeScheduler.getInstance().setTask(node, this.groupData.getPkgID());
							        			 if(schResult){
							        				  Logger.getInstance().serviceLog(Context.ServiceScheduler,"调度线程分派：版本"+this.groupData.getPkgID()+"在节点["+node.getName()+"("+node.getIp()+")]开始部署");
							        				  //守护节点的调度状态直到状态完成
							        				  while(!this.nodeMsg.get(nodeID)){
							        					  this.sleep(1*1000L);
							        				  }
							        				  loop=false;
							        			 }else{
							        			    this.sleep(1*1000L);
							        			    loop=true;
							        			 }
						        		}
				        		 }
				        		//循环调度下一个节点
				        	}
				        }
				        if(!this.exitFlag){
				        		this.groupData.setStatus(PKGSYSTEMBean.Status.Done.ordinal()+"");
						        Logger.getInstance().serviceLog(Context.ServiceScheduler,this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+"调度完成……");
						        //循环等待服务下一个任务,本系统要进行数据统计资源释放
						        DataHelper.statisticMyPkgFiles( groupData.getPkgID(),groupData.getSystemID(),Context.session.userID);
				        }
				        this.canWorkFlag=false;
				        this.busyFlag=false;
				   }
			 }
		}catch(Exception e){
			 Logger.getInstance().serviceLog(Context.ServiceScheduler,this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+"服务异常:"+e.toString());
			 Logger.getInstance().error("调度服务["+"]"+this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+"服务异常:"+e.toString());
		}
	}
	
	
	public  void exit(){
		try{
			for(String key:nodeMsg.keySet()){
				this.releaseNode(key);
			}
			this.exitFlag=true;
			this.runFlag=false;
			 Logger.getInstance().serviceLog(Context.ServiceScheduler,this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+")服务结束并退出……");
		}catch(Exception e){
			try{
				 Logger.getInstance().serviceLog(Context.ServiceScheduler,this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+")服务退出时发生异常："+e.toString());
				 Logger.getInstance().error("调度服务["+"]"+this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+")服务退出时发生异常："+e.toString());
			}catch(Exception exp){
				Logger.getInstance().error("调度服务["+"]"+"服务退出时发生异常："+e.toString());
			}
		}
	}
	
	private static PkgSystemScheduler unqueInstance;
	private ConcurrentMap<String,Boolean> nodeMsg;
	private PkgSystemScheduler(){
		this.runFlag=true;
		nodeMsg=new ConcurrentHashMap<String, Boolean>();
		this.start();
	}
	private boolean runFlag=false;
	private boolean canWorkFlag=false;
	private boolean busyFlag=false;
	private boolean exitFlag=false;
	private PKGSYSTEMBean groupData;
	
}
