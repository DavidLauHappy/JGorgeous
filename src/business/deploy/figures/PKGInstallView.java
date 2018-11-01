package business.deploy.figures;

import java.util.List;
import java.util.Map;

import model.APPSYSTEM;
import model.PKG;
import model.PKGSYSTEM;
import model.SYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import common.db.DataHelper;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import bean.PKGBean;
import bean.PKGSYSTEMBean;
import bean.SYSTEMBean;
import business.deploy.bean.InstallData;
import business.deploy.core.PkgSystemScheduler;
import business.deploy.core.ViewRefresher;

import utils.DateUtil;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

/**
 * @author Administrator
 * 显示版本安装的情况，包括已经完成的安装
 * 我的版本部署
 */
public class PKGInstallView {

	public PKGInstallView(Composite com){
		this.parent=com;
		this.createAndShow();
	}
	
	private void createAndShow(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 0));
		this.createActionArea();
		this.createDataArea();
		content.pack();
	}
	
	private void createActionArea(){
		Composite pannelAction=new Composite(content,SWT.NONE);
		pannelAction.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannelAction.setLayout(LayoutUtils.getComGridLayout(4, 0));
		
	    btnAction=new Button(pannelAction,SWT.PUSH);
	    btnAction.setText("   "+Constants.getStringVaule("btn_enable")+"    ");
	    btnAction.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1, 0, 0));
	    btnAction.addSelectionListener(new SetEnableAction());
	    btnAction.setEnabled(false);
	    pannelAction.pack();
	}
	
	private void createDataArea(){
		  Composite pannelData=new Composite(content,SWT.BORDER);
		   pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		   pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		   dataTable=new Table(pannelData,SWT.BORDER|SWT.SINGLE|SWT.FULL_SELECTION);//SWT.CHECK
		   dataTable.setHeaderVisible(true);
		   dataTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		   dataTable.setLinesVisible(true);
		   dataTable.addSelectionListener(new SetButtonAction());
		   dataTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		     String[] header=new String[]{ Constants.getStringVaule("header_version"),
										   Constants.getStringVaule("header_group"),
										   Constants.getStringVaule("header_installstatus"),
										   Constants.getStringVaule("header_mdfdate")
										   };
			for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(dataTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
		  List<InstallData> pkgSystems=DataHelper.getMyInstallData(Context.session.userID, Context.session.currentFlag);
		  Map<String,String> status=Dictionary.getDictionaryMap("PKG_SYSTEM.STATUS");
		  if(pkgSystems!=null&&pkgSystems.size()>0){
			  for(InstallData pkgSys:pkgSystems){
				  TableItem tableItem=new TableItem(dataTable,SWT.BORDER);
				  String statusName=status.get(pkgSys.getStatus());
				  pkgSys.setStatusName(statusName);
				  tableItem.setText(new String[]{pkgSys.getPkgID(),pkgSys.getSystemName(),pkgSys.getStatusName(),pkgSys.getMdfTime()});
				  tableItem.setData(pkgSys);
			  }
		  }
			for(int j=0;j<header.length;j++){		
				dataTable.getColumn(j).pack();
			}	
		   pannelData.pack();
	}
	
	public class SetButtonAction extends SelectionAdapter{
	 	public void widgetSelected(SelectionEvent e){
	 		currentItem=dataTable.getSelection();
	 		if(currentItem!=null){
	 				InstallData data=(InstallData)currentItem[0].getData();
	 				if((PKGSYSTEMBean.Status.Ready.ordinal()+"").equals(data.getStatus())){
	 					 btnAction.setText("   "+Constants.getStringVaule("btn_install")+"    ");
	 					btnAction.setEnabled(true);
	 				}
	 				else if((PKGSYSTEMBean.Status.Halt.ordinal()+"").equals(data.getStatus())){
	 					 btnAction.setText("   "+Constants.getStringVaule("btn_goonInstall")+"    ");
		 				 btnAction.setEnabled(true);
	 				}
	 				else{
	 					 btnAction.setEnabled(false);
	 				}
	 		}
	 	}
	}
	
	public class SetEnableAction extends SelectionAdapter{
	 	public void widgetSelected(SelectionEvent e){	
	 		if(currentItem!=null){
 				InstallData oldData=(InstallData)currentItem[0].getData();
 				InstallData data=DataHelper.getMyInstallData(Context.session.userID, Context.session.currentFlag, oldData.getPkgID(), oldData.getSystemID());
 				if(!StringUtil.isNullOrEmpty(data.getPkgID())&&data.getPkgID().indexOf("_")!=-1){
 					if(!Context.NodeEditable) {
 						 String app=data.getPkgID().substring(0, data.getPkgID().indexOf("_"));
 	 					 String timeSpan=APPSYSTEM.getAppSpan(app, data.getSystemID());
 	 					 if(!StringUtil.isNullOrEmpty(timeSpan)){
 	 						    String time1=timeSpan.split("-")[0];
 	 						    time1=time1.replace(":", "");
 	 						    String time2=timeSpan.split("-")[1];
 	 						     time2=time2.replace(":", "");
 	 						     String curr=DateUtil.getCurrentDate("HHmm");
 	 						     if(!StringUtil.numMid(time1, time2, curr)){
 	 						    	 String msg="当前时间不满足应用["+app+"]部署版本时间区间["+timeSpan+"]，请稍后再试";
 	 	 	 						 MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
 	 	 	 						 messagebox.setText(Constants.getStringVaule("messagebox_alert"));
 	 	 	 						 messagebox.setMessage( msg);
 	 	 	 						 messagebox.open();
 	 						     }else{
 	 						    	pkgInstall(data);
 	 						     }
 	 					 }else{
 	 						 String msg="应用["+app+"]未配置部署版本时间区间，无法自动部署版本，请通知管理员配置";
 	 						 MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
 	 						 messagebox.setText(Constants.getStringVaule("messagebox_alert"));
 	 						 messagebox.setMessage( msg);
 	 						 messagebox.open();
 	 					 }
 					}else{
 						pkgInstall(data);
 					}
 				}else{
 					//不是规范的自动化部署版本包，不执行部署安装
 					 String msg=data.getPkgID()+"不是标准的自动化版本包，无法支持部署。";
					 MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
					 messagebox.setText(Constants.getStringVaule("messagebox_alert"));
					 messagebox.setMessage( msg);
					 messagebox.open();
 				}
	 		}
	 	}
	}
	
	public void pkgInstall(InstallData data){
		if((PKGSYSTEMBean.Status.Ready.ordinal()+"").equals(data.getStatus())){
			 Context.CurrentVersionID=data.getPkgID();
			   SYSTEMBean tag=SYSTEM.getSystemsByID(data.getSystemID(), Context.session.currentFlag);
			   String memo="确认版本["+ Context.CurrentVersionID+"]在["+tag.getName()+"]上安装？";
			  MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
			  messagebox.setText(Constants.getStringVaule("messagebox_alert"));
			  messagebox.setMessage(memo);
			 int choose=messagebox.open();
			 if(choose==SWT.YES){
 					//启动调度者线程
 				    PKGSYSTEMBean  group=new PKGSYSTEMBean();
 				    group.setPkgID(data.getPkgID());
	 				group.setSystemID(data.getSystemID());
	 				group.setStatus(data.getStatus());
	 				group.setMdfTime(data.getMdfTime());
	 				group.setMdfUser(Context.session.userID);
	 				boolean result=PkgSystemScheduler.getInstance().setTask(group);
					if(!result){
						MessageBox msgBox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
						msgBox.setText(Constants.getStringVaule("messagebox_alert"));
						msgBox.setMessage("当前调度器繁忙！请稍后再试");
						msgBox.open();
					}else{
						DeployEditView.getInstance(null).setDeployView(tag,data.getPkgID());
						//版本状态同步更新
	 					PKGBean version=PKG.getPkg(data.getPkgID());
	 					PKG.setStatus(data.getPkgID(), PKGBean.Status.Run.ordinal()+"", Context.session.userID);
	 					//启动监视器
	 					ViewRefresher.getInstance().setRunable(true);
					}
			 }
		}
		else if((PKGSYSTEMBean.Status.Halt.ordinal()+"").equals(data.getStatus())){
			 Context.CurrentVersionID=data.getPkgID();
			     SYSTEMBean tag=SYSTEM.getSystemsByID(data.getSystemID(), Context.session.currentFlag);
			     String memo="版本["+ Context.CurrentVersionID+"]在["+tag.getName()+"]上安装被中止！是否继续安装？";
			 MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
			 messagebox.setText(Constants.getStringVaule("messagebox_alert"));
			 messagebox.setMessage(memo);
			 int choose=messagebox.open();
			 if(choose==SWT.YES){
 					//启动调度者线程
 				    PKGSYSTEMBean  group=new PKGSYSTEMBean();
 				    group.setPkgID(data.getPkgID());
	 				group.setSystemID(data.getSystemID());
	 				group.setStatus(data.getStatus());
	 				group.setMdfTime(data.getMdfTime());
	 				group.setMdfUser(Context.session.userID);
	 				boolean result=PkgSystemScheduler.getInstance().setTask(group);
	 				if(!result){
						MessageBox msgBox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
						msgBox.setText(Constants.getStringVaule("messagebox_alert"));
						msgBox.setMessage("当前调度器繁忙！请稍后再试");
						msgBox.open();
					}else{
						 DeployEditView.getInstance(null).setDeployView(tag,data.getPkgID());
	 					//版本状态同步更新
	 					PKGBean version=PKG.getPkg(data.getPkgID());
	 					PKG.setStatus(data.getPkgID(), PKGBean.Status.Run.ordinal()+"", Context.session.userID);
	 					//启动监视器
	 					ViewRefresher.getInstance().setRunable(true);
					}
			 }
		}else{
			 Context.CurrentVersionID=data.getPkgID();
			     SYSTEMBean tag=SYSTEM.getSystemsByID(data.getSystemID(), Context.session.currentFlag);
			     String memo="版本["+ Context.CurrentVersionID+"]在["+tag.getName()+"]上安装过，不允许重复安装";
				 MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.NO);
				 messagebox.setText(Constants.getStringVaule("messagebox_alert"));
				 messagebox.setMessage(memo);
				messagebox.open();
		}
	}
	
	private Composite parent=null;
	public Composite content=null;
	public Table dataTable=null;
	public  Button btnAction=null;
	public TableItem[] currentItem=null;
}
