package business.deploy.core;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import model.COMMAND;
import model.NODE;

import common.db.DataHelper;

import bean.COMMANDBean;
import bean.NODEBean;
import business.deploy.figures.DeployView;
import resource.Constants;
import resource.Context;
import resource.Logger;
import utils.StringUtil;
import views.AppView;

public class ViewRefresher extends Thread{
	
	
	 public static ViewRefresher getInstance(){
		 if(unique_isntance==null)
			 unique_isntance=new ViewRefresher();
		 return unique_isntance;
	 }
	 
    public synchronized void addPage(DeployView page){
    	this.pageList.put(page.getSystemID(), page);
    }
    
    public synchronized void removePage(String systemID){
    	try{
			this.pageList.remove(systemID);	
			  Logger.getInstance().serviceLog(Context.ServicePeeker,"服务刷新版本安装界面完成，注销页面对象:"+systemID);
		}catch(Exception e){
			Logger.getInstance().serviceLog(Context.ServicePeeker,"removePage()监管线程运行异常："+e.toString());
			Logger.getInstance().error("ViewRefresher.removePage()监管线程运行异常："+e.toString());
		}
    }
	 public void run(){
		 try{
				Logger.getInstance().serviceLog(Context.ServicePeeker,"界面更新ViewRefresher服务成功启动……");
				while(runable){
					try {
						Thread.sleep(5*1000);//每5秒钟刷新下界面
					} catch(InterruptedException e) {
						Logger.getInstance().serviceLog(Context.ServicePeeker,"PeekThread.run()监管线程睡眠异常："+e.toString());
						Logger.getInstance().error("ViewRefresher.run()监管线程睡眠异常："+e.toString());
					}
					 if(this.pageList.size()>0){
						  for(String systemID:this.pageList.keySet()){
							  currDeployView=this.pageList.get(systemID);
							  if(currDeployView!=null&&!StringUtil.isNullOrEmpty(currDeployView.getVersion())){
								  Logger.getInstance().serviceLog(Context.ServicePeeker,"界面更新Peeker服务："+systemID+"->"+currDeployView.getVersion()+"服务中……");
								  this.progress();
								  Thread.sleep(1*1000);//每1秒钟刷新下界面 
							  }
						  }
					 }
				}
		 }catch(Exception e){
				Logger.getInstance().serviceLog(Context.ServicePeeker,"ViewRefresher.run()监管线程运行异常："+e.toString());
				Logger.getInstance().error("ViewRefresher.run()监管线程运行异常："+e.toString());
			}
	 }
	 
	 public DeployView currDeployView;
	 public void progress() {
			AppView.getInstance().getDisplay().asyncExec(new Runnable() {
				public void run() {
					try{
						 String versionID=currDeployView.getVersion();
						 String systemID=currDeployView.getSystemID();
						 String sysName=currDeployView.getSystemName();
						 boolean doneFlag=false;
						 String statistic=DataHelper.getInstallProgress(versionID, systemID, Context.session.currentFlag,Context.session.userID);
						  if(!StringUtil.isNullOrEmpty(statistic)){
							    Logger.getInstance().serviceLog(Context.ServicePeeker,"更新界面进度条:"+versionID+"->"+systemID+":"+statistic);
								int max=Integer.parseInt(statistic.split("\\|")[0]);
								int cur=Integer.parseInt(statistic.split("\\|")[1]);
								if(cur>0){
									float percent=( (float)cur/max)*100;  
									DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
									String strPercent = df.format(percent)+"%";//返回的是String类型 
									currDeployView.getDiagramView().setProgress(strPercent, max, cur);
									if(max==cur){
										doneFlag=true;
										if(!currDeployView.promptAble){
											currDeployView.promptAble=true;
											MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.INDETERMINATE|SWT.OK);
											box.setText(Constants.getStringVaule("messagebox_alert"));
											box.setMessage("版本"+versionID+"在系统["+sysName+"]全部部署完成。");
											box.open();
										}
									}
								}
						  }
						  //取5条指令的结果更新界面
						  List<COMMANDBean> cmds=COMMAND.getRemindCommand(versionID, Context.session.userID, Constants.PeekFetchRows);
						  if(cmds!=null&&cmds.size()>0){
							  for(COMMANDBean cmd:cmds){
									   String status=cmd.getStatus();
										String  retMsg=cmd.getLogInfo();
										retMsg=retMsg.replaceAll("&", "\r\n");
										String  nodeID=cmd.getNodeID();
										NODEBean node=NODE.getNodeByID(Context.session.userID, nodeID, Context.session.currentFlag);
										String groupID=node.getSystemID();
										String name=cmd.getName();
										String text="";
										if((COMMANDBean.Status.TimeOut.ordinal()+"").equals(status)||
												(COMMANDBean.Status.ReturnNull.ordinal()+"").equals(status)||
												(COMMANDBean.Status.ReturnFailed.ordinal()+"").equals(status)){
											text=name+"异常，返回信息:"+retMsg;
											//更新数据库的节点状态
											node.SetStatus(NODEBean.Status.Error.ordinal()+"");
											//更新数据库的节点状态
										}else{
											text=name+"成功。返回信息:"+retMsg;
											node.setStatus(NODEBean.Status.Running.ordinal()+"");
										}
										currDeployView.getDiagramView().redrawDiagramView();
										Logger.getInstance().serviceLog(Context.ServicePeeker,"界面更新Peeker服务更新命令行文本:"+text);
										currDeployView.getCommandView().updateTabContent(nodeID, text);
										//更新指令的通知状态
										cmd.setRemind(COMMANDBean.Remind.Yes.ordinal()+"");
								  }
							 }else{
								 if(doneFlag){
									 currDeployView.getDiagramView().redrawDiagramView();
									 currDeployView.canClose=true;
									 removePage(currDeployView.getSystemID());
								 }
							 }
					}catch(Exception exp){
						Logger.getInstance().serviceLog(Context.ServicePeeker,"ViewRefresher.progress()监管线程更新进度异常："+exp.toString());
			    		Logger.getInstance().error("ViewRefresher.progress()监管线程更新进度异常："+exp.toString());
					}
				}
			});
	 }
	 

	 public void exit(){
			try{
				   this.setRunable(false);
				   this.interrupt();
				   Logger.getInstance().serviceLog(Context.ServicePeeker,"界面更新Peeker服务退出……");
			}catch(Exception e){
				Logger.getInstance().serviceLog(Context.ServicePeeker,"ViewRefresher.exit()监管线程退出异常："+e.toString());
	    		Logger.getInstance().error("ViewRefresher.exit()监管线程退出异常："+e.toString());
			}
		}
	 
	 public synchronized void setRunable(boolean flag){
			this.runable=flag;
		}
	 
	 private ConcurrentMap<String,DeployView> pageList=new ConcurrentHashMap<String, DeployView>();
	 private boolean runable=false;
	 private static ViewRefresher  unique_isntance;
	 private ViewRefresher(){}
}
