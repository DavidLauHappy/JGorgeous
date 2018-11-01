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
			  Logger.getInstance().serviceLog(Context.ServicePeeker,"����ˢ�°汾��װ������ɣ�ע��ҳ�����:"+systemID);
		}catch(Exception e){
			Logger.getInstance().serviceLog(Context.ServicePeeker,"removePage()����߳������쳣��"+e.toString());
			Logger.getInstance().error("ViewRefresher.removePage()����߳������쳣��"+e.toString());
		}
    }
	 public void run(){
		 try{
				Logger.getInstance().serviceLog(Context.ServicePeeker,"�������ViewRefresher����ɹ���������");
				while(runable){
					try {
						Thread.sleep(5*1000);//ÿ5����ˢ���½���
					} catch(InterruptedException e) {
						Logger.getInstance().serviceLog(Context.ServicePeeker,"PeekThread.run()����߳�˯���쳣��"+e.toString());
						Logger.getInstance().error("ViewRefresher.run()����߳�˯���쳣��"+e.toString());
					}
					 if(this.pageList.size()>0){
						  for(String systemID:this.pageList.keySet()){
							  currDeployView=this.pageList.get(systemID);
							  if(currDeployView!=null&&!StringUtil.isNullOrEmpty(currDeployView.getVersion())){
								  Logger.getInstance().serviceLog(Context.ServicePeeker,"�������Peeker����"+systemID+"->"+currDeployView.getVersion()+"�����С���");
								  this.progress();
								  Thread.sleep(1*1000);//ÿ1����ˢ���½��� 
							  }
						  }
					 }
				}
		 }catch(Exception e){
				Logger.getInstance().serviceLog(Context.ServicePeeker,"ViewRefresher.run()����߳������쳣��"+e.toString());
				Logger.getInstance().error("ViewRefresher.run()����߳������쳣��"+e.toString());
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
							    Logger.getInstance().serviceLog(Context.ServicePeeker,"���½��������:"+versionID+"->"+systemID+":"+statistic);
								int max=Integer.parseInt(statistic.split("\\|")[0]);
								int cur=Integer.parseInt(statistic.split("\\|")[1]);
								if(cur>0){
									float percent=( (float)cur/max)*100;  
									DecimalFormat df = new DecimalFormat("0.00");//��ʽ��С��   
									String strPercent = df.format(percent)+"%";//���ص���String���� 
									currDeployView.getDiagramView().setProgress(strPercent, max, cur);
									if(max==cur){
										doneFlag=true;
										if(!currDeployView.promptAble){
											currDeployView.promptAble=true;
											MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.INDETERMINATE|SWT.OK);
											box.setText(Constants.getStringVaule("messagebox_alert"));
											box.setMessage("�汾"+versionID+"��ϵͳ["+sysName+"]ȫ��������ɡ�");
											box.open();
										}
									}
								}
						  }
						  //ȡ5��ָ��Ľ�����½���
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
											text=name+"�쳣��������Ϣ:"+retMsg;
											//�������ݿ�Ľڵ�״̬
											node.SetStatus(NODEBean.Status.Error.ordinal()+"");
											//�������ݿ�Ľڵ�״̬
										}else{
											text=name+"�ɹ���������Ϣ:"+retMsg;
											node.setStatus(NODEBean.Status.Running.ordinal()+"");
										}
										currDeployView.getDiagramView().redrawDiagramView();
										Logger.getInstance().serviceLog(Context.ServicePeeker,"�������Peeker��������������ı�:"+text);
										currDeployView.getCommandView().updateTabContent(nodeID, text);
										//����ָ���֪ͨ״̬
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
						Logger.getInstance().serviceLog(Context.ServicePeeker,"ViewRefresher.progress()����̸߳��½����쳣��"+exp.toString());
			    		Logger.getInstance().error("ViewRefresher.progress()����̸߳��½����쳣��"+exp.toString());
					}
				}
			});
	 }
	 

	 public void exit(){
			try{
				   this.setRunable(false);
				   this.interrupt();
				   Logger.getInstance().serviceLog(Context.ServicePeeker,"�������Peeker�����˳�����");
			}catch(Exception e){
				Logger.getInstance().serviceLog(Context.ServicePeeker,"ViewRefresher.exit()����߳��˳��쳣��"+e.toString());
	    		Logger.getInstance().error("ViewRefresher.exit()����߳��˳��쳣��"+e.toString());
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
