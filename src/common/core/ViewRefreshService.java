package common.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;

import model.LOCALCOMMAND;
import model.LOCALNODE;

import resource.Constants;
import resource.Context;
import resource.Logger;
import resource.TDiagramView;
import utils.StringUtil;
import views.AppView;

/**
 * @author Administrator
 * 根据绑定的界面和版本定时更新界面
 */
public class ViewRefreshService extends Thread{
	
	public  static ViewRefreshService getIntance(){
		if(unique_instance==null)
			unique_instance=new ViewRefreshService();
		return unique_instance;
	}
	
	public synchronized void addPage(TDiagramView page){
		this.pageList.put(page.versionID, page);
	}
	
	public synchronized void removePage(String versionID){
		try{
			this.pageList.remove(versionID);	
			  Logger.getInstance().serviceLog(Context.ServicePeekerQuick,"服务刷新版本安装界面完成，注销页面对象:"+versionID);
		}catch(Exception e){
			Logger.getInstance().serviceLog(Context.ServicePeekerQuick,"快速部署后台界面更新ViewRefreshService.removePage()异常："+e.toString());
		}
	}
	
	public synchronized void setRunable(boolean flag){
		this.runable=flag;
	}
	
	public void run(){
		try{
			  Logger.getInstance().serviceLog(Context.ServicePeekerQuick,"服务启动");
			  while(this.runable){
				  Thread.sleep(5*1000);//每5秒钟刷新下界面
				  if(this.pageList.size()>0){
					  for(String versionID:this.pageList.keySet()){
						  currentPage=this.pageList.get(versionID);
						  Logger.getInstance().serviceLog(Context.ServicePeekerQuick,"服务运行ViewRefreshService.run()刷新版本:"+versionID);
						  this.progress();
						  this.console();
						  Thread.sleep(1*1000);//每1秒钟刷新下界面 
					  }
				  }
			  }
		}
		catch(Exception e){
			 Logger.getInstance().serviceLog(Context.ServicePeekerQuick,"服务运行ViewRefreshService.run()过程发生异常："+e.toString());
		}
	}
	
	public void exit(){
		try{
			   this.setRunable(false);
			   this.interrupt();
			   Logger.getInstance().serviceLog(Context.ServicePeekerQuick,"服务结束并退出……");
		}catch(Exception e){
			Logger.getInstance().serviceLog(Context.ServicePeekerQuick,"服务退出异常:"+e.toString());
		}
	}
	
	public TDiagramView currentPage;
	private void progress(){
		AppView.getInstance().getDisplay().asyncExec(new Runnable() {
			public void run() {
				 String versionID=currentPage.versionID;
				 String nodeID=currentPage.nodeID;
				 String statistic=LOCALCOMMAND.getInstallProgress(versionID, nodeID);
				  if(!StringUtil.isNullOrEmpty(statistic)){
					   int max=Integer.parseInt(statistic.split("\\|")[0]);
						int cur=Integer.parseInt(statistic.split("\\|")[1]);
						if(cur>0){
							float percent=( (float)cur/max)*100;  
							DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
							String strPercent = df.format(percent)+"%";//返回的是String类型 
							currentPage.setProgress(strPercent, max, cur);
							if(max==cur){
								currentPage.canPageRemove=true;
								if(!currentPage.promptAble){
									currentPage.promptAble=true;
									MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.INDETERMINATE|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage("版本"+versionID+"在节点["+nodeID+"]全部部署完成。");
									box.open();
								}
							}
						}
				  }
			}
		});
	}
	
	private void console(){
		AppView.getInstance().getDisplay().asyncExec(new Runnable() {
			public void run() {
				 String versionID=currentPage.versionID;
				 String nodeID=currentPage.nodeID;
				 List<LOCALCOMMANDBean> cmds=LOCALCOMMAND.getRemindComs(versionID, nodeID, 5);
				 if(cmds!=null&&cmds.size()>0){
					 for(LOCALCOMMANDBean cmd:cmds){
						 String  retMsg=cmd.getLoginfo();
						 retMsg=retMsg.replaceAll("&", "\r\n");
						 String status=cmd.getStatus();
						 LOCALNODEBean node =LOCALNODE.getNode(nodeID);
						 if((LOCALCOMMANDBean.Status.ReturnFailed.ordinal()+"").equals(status)||
								 (LOCALCOMMANDBean.Status.TimeOut.ordinal()+"").equals(status)||
								 (LOCALCOMMANDBean.Status.ReturnNull.ordinal()+"").equals(status)){
							 node.reSetStatus(LOCALNODEBean.Status.Error.ordinal()+"");
						 }else{
							 node.reSetStatus(LOCALNODEBean.Status.Running.ordinal()+"");
						 }
						 currentPage.redrawNode();
						 currentPage.updateTabContent(cmd.getNodeID(), retMsg);
						 cmd.reSetRemind(LOCALCOMMANDBean.Remind.Yes.ordinal()+"");
					 }
				 }else{
					 if(currentPage.canPageRemove){
						 removePage(currentPage.versionID);
					 }
				 }
			}
		});
	}
	
	private boolean runable=false;
	private ConcurrentMap<String,TDiagramView> pageList=new ConcurrentHashMap<String,TDiagramView>();
	private static ViewRefreshService unique_instance;
	private ViewRefreshService(){}
	
	
}
