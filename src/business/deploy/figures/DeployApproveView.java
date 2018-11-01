package business.deploy.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.COMMAND;
import model.PKGSYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import common.db.DataHelper;

import bean.COMMANDBean;
import bean.PKGSYSTEMBean;
import business.deploy.bean.ApproveData;

import resource.Constants;
import resource.Context;
import resource.Dictionary;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

public class DeployApproveView {
	
	public DeployApproveView(Composite com){
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
	    Button btnEnable=new Button(pannelAction,SWT.PUSH);
	    btnEnable.setText("   "+Constants.getStringVaule("btn_enable")+"    ");
	    btnEnable.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1, 0, 0));
	    btnEnable.addSelectionListener(new SetEnableAction());
	    pannelAction.pack();
	}
	
	private void createDataArea(){
		  Composite pannelData=new Composite(content,SWT.BORDER);
		   pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		   pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		   dataTable=new Table(pannelData,SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION|SWT.CHECK);
		   dataTable.setHeaderVisible(true);
		   dataTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		   dataTable.setLinesVisible(true);
		   dataTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		     String[] header=new String[]{ Constants.getStringVaule("header_approveID"),
										   				  Constants.getStringVaule("header_approveStatus"),
										   				 Constants.getStringVaule("header_applytime"),
										   				 };
			for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(dataTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
		     List<ApproveData> approveList=DataHelper.getMyDeployTask( Context.session.userID);
		     if(approveList!=null&&approveList.size()>0){
		    	 Map<String, String> applyStatus=Dictionary.getDictionaryMap("FLOW_DETAIL.REC_STATUS");
		    	 for(int w=0;w<approveList.size();w++){
					 ApproveData bean=approveList.get(w);
					 String status=applyStatus.get(bean.getStatus());
					 bean.setStatusDesc(status);
					 TableItem tableItem=new TableItem(dataTable,SWT.BORDER);
		    		 tableItem.setText(new String[]{bean.getId(),bean.getStatusDesc(),bean.getApproveTime()});
		    		 tableItem.setData(bean);
				 }
		     }
			
			for(int j=0;j<header.length;j++){		
				dataTable.getColumn(j).pack();
			}	
		   pannelData.pack();
	}
	
	 public class SetEnableAction extends SelectionAdapter{
	 	public void widgetSelected(SelectionEvent e){
	 		List<TableItem> items=new ArrayList<TableItem>();
	 		for(TableItem curitem:dataTable.getItems()){
	 			if(curitem.getChecked())
	 				items.add(curitem);
	 		}
	 		List<ApproveData> datas=new ArrayList<ApproveData>();
	 		if(items!=null&&items.size()>0){
	 			for(TableItem item:items){
	 				ApproveData bean=(ApproveData)item.getData();
	 				if(bean.getStatus().equals(ApproveData.Status.ApproveOK.ordinal()+"")){
	 					datas.add(bean);
	 				}
	 			}
	 			if(datas!=null&&datas.size()>0){
		 			for(ApproveData data:datas){
		 				//本指令状态设置，提示可以正式部署安装
		 				COMMAND.setFlag(data.getId(),  Context.session.userID, COMMANDBean.Flag.On.ordinal()+"");
		 				String pkg=DataHelper.getPkgByApprID(data.getId(),  Context.session.userID);
		 				String systemID=DataHelper.getSystemByApprID(data.getId(), Context.session.userID);
		 				if(!StringUtil.isNullOrEmpty(pkg)&&!StringUtil.isNullOrEmpty(systemID)){
		 					//判断版本包在系统是否已经存在，存在则不能重复去部署
		 					if(PKGSYSTEM.isPkgSysExist(pkg, systemID, Context.session.userID)){
		 						MessageBox alertbox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK|SWT.CANCEL);
		 						alertbox.setText(Constants.getStringVaule("messagebox_alert"));
		 						alertbox.setMessage("版本["+pkg+"]在系统["+systemID+"]已经安排部署，是否需要重新部署？");
		 						int choice=alertbox.open();	
		 						if(SWT.YES==choice){
		 							PKGSYSTEM.deletePkgSys(pkg, systemID, Context.session.userID);
		 							PKGSYSTEMBean pkgGroup=new PKGSYSTEMBean(pkg,systemID,PKGSYSTEMBean.Status.Ready.ordinal()+"", Context.session.userID);
				 					pkgGroup.inroll();
		 						}
		 					}else{
			 					PKGSYSTEMBean pkgGroup=new PKGSYSTEMBean(pkg,systemID,PKGSYSTEMBean.Status.Ready.ordinal()+"", Context.session.userID);
			 					pkgGroup.inroll();
		 					}
		 				}
		 			}
		 			MessageBox alertbox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					alertbox.setText(Constants.getStringVaule("messagebox_alert"));
					alertbox.setMessage("配置策略完成！可点击图标开始安装版本包。");
					alertbox.open();	
					DeployEditView.getInstance(null).closeTab(Constants.getStringVaule("btn_tips_approve"));
					
		 		}else{
		 			MessageBox alertbox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					alertbox.setText(Constants.getStringVaule("messagebox_alert"));
					alertbox.setMessage("未通过审批的部署安装任务无法启用");
					alertbox.open();	
		 		}
	 			
	 		}else{
	 			MessageBox alertbox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				alertbox.setText(Constants.getStringVaule("messagebox_alert"));
				alertbox.setMessage("请选择记录");
				alertbox.open();	
	 		}
	 	}
	 }
	 
	
	private Composite parent=null;
	public Composite content=null;
	public Table dataTable=null;
}
