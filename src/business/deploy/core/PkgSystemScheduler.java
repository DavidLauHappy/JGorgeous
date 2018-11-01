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
 * ��һ�����
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
							Logger.getInstance().serviceLog(Context.ServiceScheduler,"����˯�߹��̷����쳣��"+e.toString());
							Logger.getInstance().error("���ȷ���["+"]PkgSystemScheduler.run()����˯�߹��̷����쳣��"+e.toString());
						}
				   }else{
					   Logger.getInstance().serviceLog(Context.ServiceScheduler,"�������ϵͳ["+this.groupData.getSystemID()+"]�汾["+this.groupData.getPkgID()+"]�����С���");
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
							        				  Logger.getInstance().serviceLog(Context.ServiceScheduler,"�����̷߳��ɣ��汾"+this.groupData.getPkgID()+"�ڽڵ�["+node.getName()+"("+node.getIp()+")]��ʼ����");
							        				  //�ػ��ڵ�ĵ���״ֱ̬��״̬���
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
				        		//ѭ��������һ���ڵ�
				        	}
				        }
				        if(!this.exitFlag){
				        		this.groupData.setStatus(PKGSYSTEMBean.Status.Done.ordinal()+"");
						        Logger.getInstance().serviceLog(Context.ServiceScheduler,this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+"������ɡ���");
						        //ѭ���ȴ�������һ������,��ϵͳҪ��������ͳ����Դ�ͷ�
						        DataHelper.statisticMyPkgFiles( groupData.getPkgID(),groupData.getSystemID(),Context.session.userID);
				        }
				        this.canWorkFlag=false;
				        this.busyFlag=false;
				   }
			 }
		}catch(Exception e){
			 Logger.getInstance().serviceLog(Context.ServiceScheduler,this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+"�����쳣:"+e.toString());
			 Logger.getInstance().error("���ȷ���["+"]"+this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+"�����쳣:"+e.toString());
		}
	}
	
	
	public  void exit(){
		try{
			for(String key:nodeMsg.keySet()){
				this.releaseNode(key);
			}
			this.exitFlag=true;
			this.runFlag=false;
			 Logger.getInstance().serviceLog(Context.ServiceScheduler,this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+")����������˳�����");
		}catch(Exception e){
			try{
				 Logger.getInstance().serviceLog(Context.ServiceScheduler,this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+")�����˳�ʱ�����쳣��"+e.toString());
				 Logger.getInstance().error("���ȷ���["+"]"+this.groupData.getPkgID()+"->"+this.groupData.getSystemID()+")�����˳�ʱ�����쳣��"+e.toString());
			}catch(Exception exp){
				Logger.getInstance().error("���ȷ���["+"]"+"�����˳�ʱ�����쳣��"+e.toString());
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
