package business.deploy.core;

import java.util.List;

import common.core.LnxShell;
import common.core.WinShell;

import resource.Context;
import resource.Logger;
import utils.StringUtil;
import bean.COMMANDBean;
import bean.NODEBean;
import bean.NODEFILEBean;
import business.deploy.action.LocalAction;
import business.deploy.action.LocalActionFacotry;


/**
 * @author DavidLau
 * 第二层调度，真正的指令调度
 */
public class PkgNodeScheduler extends  Thread{
		public static  PkgNodeScheduler getInstance(){
			 if(unique_instance==null)
				 unique_instance=new PkgNodeScheduler();
			 return unique_instance;
		}
		
		public synchronized boolean setTask(NODEBean node,String versionID){
			if(this.busyFlag)
				return false;
			else{
				this.node=node;
				this.versionID=versionID;
				this.busyFlag=true;
				this.canWorkAble=true;
				return true;
			}
		}
		
		public synchronized void resumeWork(){
			this.canWorkAble=true;
		}
		
		
		public void run(){
			while(this.runFlag){
		          if(!this.canWorkAble){
		        	  try{
		        		  this.sleep(1*1000L);
		        	   }catch(Exception e){
		        		   Logger.getInstance().serviceLog(Context.ServiceSchedulerNode,"调度睡眠过程发生异常："+e.toString());
							Logger.getInstance().error("调度服务["+"]Scheduler.run()调度睡眠过程发生异常："+e.toString());
		        	   }
		          }else{
		        	     this.node.SetStatus(NODEBean.Status.Running.ordinal()+"");//更新节点的调度状态
		        	     Logger.getInstance().serviceLog(Context.ServiceSchedulerNode,"正在调度节点："+node.getName()+"("+node.getIp()+")");
		        	   //按指令顺序来调度，取出所有步骤来循环调度
						 List<COMMANDBean> commands=node.getScheCommands(this.versionID);
						 if(commands!=null&&commands.size()>0){
								int count=commands.size();
								int current=0;
								for(COMMANDBean cmd:commands){
									  cmd.setStatus(COMMANDBean.Status.Scheduling.ordinal()+"");//更新指令调度状态
									  if(!this.exitFlag){
										     Logger.getInstance().serviceLog(Context.ServiceSchedulerNode,"节点："+node.getName()+"("+node.getIp()+")指令执行:"+cmd.getName());
										     LocalAction action=LocalActionFacotry.createAction(cmd,node);
										     boolean ret=action.execute();
											 String msgID=cmd.getId();
											 String outcode=action.getResult();
											 String outmsg=action.getResultInfo();
											 String fileID=action.getFileID();
											 String md5=action.getInstallFileMd5();
											 if(!StringUtil.isNullOrEmpty(fileID)){
												 NODEFILEBean nodeFile=new NODEFILEBean();
												 nodeFile.setFileID(fileID);
												 nodeFile.setMd5(md5);
												 nodeFile.setPkgID(cmd.getPkgID());
												 nodeFile.setNodeID(cmd.getNodeID());
												 nodeFile.setMdfUser(Context.session.userID);
												 nodeFile.inroll();
								    		 }
											 cmd.setRemind(COMMANDBean.Remind.Ok.ordinal()+"");
											 if(!ret){   
												 node.SetStatus(NODEBean.Status.Error.ordinal()+"");
												 this.canWorkAble=false;
												 PkgSystemScheduler.getInstance().blockPkgSystem();
												 Logger.getInstance().serviceLog(Context.ServiceSchedulerNode,"节点："+node.getName()+"("+node.getIp()+")指令执行异常，调度服务暂停……");
												 break;
											 }else{
												 current++;
												continue; 
											 }
									  }
								}
								if(current>=count){
									 node.setSchFlag(NODEBean.Flag.Mute.ordinal()+"");
									 PkgSystemScheduler.getInstance().releaseNode(node.getId());
									node.setStatus(NODEBean.Status.Done.ordinal()+"");
									Logger.getInstance().serviceLog(Context.ServiceSchedulerNode,"节点："+node.getName()+"("+node.getIp()+")完成指令调度");
									 //释放WinShell/LinuxShell
									if((NODEBean.OS.Linux.ordinal()+"").equals(node.getOs())){
										LnxShell xshell=LnxShell.getShell(node);
										if(xshell!=null){
											xshell.closeConnection(node.getId());
										}
									}else{
										WinShell wShell=WinShell.getShell(node.getIp());
										if(wShell!=null){
											wShell.exit();
										}
									}
									this.busyFlag=false;
									 this.canWorkAble=false;
								}
						 }else{
							 //节点部署完成
							 node.SetStatus(NODEBean.Status.Done.ordinal()+"");
							 PkgSystemScheduler.getInstance().releaseNode(node.getId());
							 //释放WinShell/LinuxShell
								if((NODEBean.OS.Linux.ordinal()+"").equals(node.getOs())){
									LnxShell xshell=LnxShell.getShell(node);
									if(xshell!=null){
										xshell.closeConnection(node.getId());
									}
								}else{
									WinShell wShell=WinShell.getShell(node.getIp());
									if(wShell!=null){
										wShell.exit();
									}
								}
								this.busyFlag=false;
								this.canWorkAble=false;
						 }
		          }
			}
		}
		
		
		 public synchronized void exit(){
				try{
					this.exitFlag=true;
					this.runFlag=false;
					Logger.getInstance().serviceLog(Context.ServiceSchedulerNode,"服务结束并退出……");
				}catch(Exception e){
					Logger.getInstance().serviceLog(Context.ServiceSchedulerNode,"服务退出异常:"+e.toString());
				}
		    }
		 
		private PkgNodeScheduler(){
			runFlag=true;
			this.start();
		}
		
		
		private static PkgNodeScheduler unique_instance;
		private boolean runFlag=false;
		 private boolean busyFlag=false;
		 private boolean canWorkAble=false;
		 private boolean exitFlag=false;
		private String versionID;
		private NODEBean node;
}
