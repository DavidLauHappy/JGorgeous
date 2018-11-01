package resource;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import model.GROUPUSER;
import model.TASK;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import bean.GroupUserBean;
import bean.RequirementLog;
import bean.TaskBean;
import bean.VStep;
import business.developer.view.DevSubmitView;
import business.developer.view.NewReqView;
import business.dmanager.view.EitCommetView;
import business.dmanager.view.MyRequirementView;

import resource.Constants;
import resource.Context;

import utils.DateUtil;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;


/**
 * @author Administrator
 * 任务的详情页面 包括：
 * 1任务需求基本信息
 * 2任务发版提交信息
 * 3任务过往记录信息
 * 3个tab页
 */
public class ReqDetailView {
	
	private Composite parent;
	public Composite content;
	private  CTabFolder tabFloder=null;
	private TaskBean data;
	
	public String[] fowHeader=new String[]{Constants.getStringVaule("header_action"),
															Constants.getStringVaule("header_currentUser"),
															Constants.getStringVaule("header_processTime"),
															Constants.getStringVaule("header_processcmt")
	};
	
	public ReqDetailView(Composite com,TaskBean data){
		this.parent=com;
		this.data=data;
		content=new  Composite(parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		this.createAndShow();
		content.pack();
	}
	
	public Button btnAgree,btnReturn=null;
	public Combo nexter=null;
	public void createAndShow(){
		
		 boolean editable=false;
		 if(this.data.getCurrentUser().equals(Context.session.userID)){
			 String  step=this.data.getStatus();
			 //可以做审批处理 还是可以做编辑修改
			 if(VStep.CodeSubmit.equals(step)){
				 editable=true;
			 }
			 else{
				 if(VStep.CodeCheck.equals(step)){//代码审核
					 Composite toolPanel=new Composite(content,SWT.NONE);
					 toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
					 toolPanel.setLayout(LayoutUtils.getComGridLayout(10, 5)); 

					 Label labNexter=new Label(toolPanel,SWT.NONE);
					 labNexter.setText(Constants.getStringVaule("label_nextUser")+":");
					 labNexter.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
					 nexter=new Combo(toolPanel,SWT.DROP_DOWN);
					 nexter.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
					 String groupID="Flow"+"1";
					 List<GroupUserBean> users=GROUPUSER.getUsers(groupID);
					 if(users!=null&&users.size()>0){
						  String[] nextUsers=new String[users.size()];
						  int index=0;
						  for(GroupUserBean user:users){
							  nextUsers[index]=user.getUserFullName();
							  index++;
						  }
						  nexter.setItems(nextUsers);
						  nexter.select(0);
					 }
					 
					 Label labAction=new Label(toolPanel,SWT.NONE);
					 labAction.setText(Constants.getStringVaule("btn_agreePkg"));
					 labAction.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
				    btnAgree=new Button(toolPanel,SWT.PUSH);
				    btnAgree.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1, 0, 0));
				    btnAgree.setToolTipText("   "+Constants.getStringVaule("btn_agreePkg")+"   ");
				    btnAgree.setImage(Icons.getPkgOKIcon());
				    btnAgree.addSelectionListener(new AgreePkgAction());
				    
					 
				    btnReturn=new Button(toolPanel,SWT.PUSH);
				    btnReturn.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
				    btnReturn.setToolTipText("   "+Constants.getStringVaule("btn_return")+"   ");
				    btnReturn.setImage(Icons.getReturnIcon());
				    btnReturn.addSelectionListener(new ReturnPkgAction());
					    
					toolPanel.pack();
				 }
			 }
		 }

		 if(this.data.getOwner().equals(Context.session.userID)){
			
			 if((TaskBean.ReleaseStatus.Apply.ordinal()+"").equals(data.getReleaseFlag())){
				 editable=true;
			 }
		 }
		tabFloder=new CTabFolder(content,SWT.TOP|SWT.BORDER);
		tabFloder.setLayout(LayoutUtils.getTabFloderLayout(tabFloder));
		tabFloder.setMaximizeVisible(true);
		tabFloder.setMinimizeVisible(true);  
		tabFloder.setSimple(false);
		tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		tabFloder.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		//基本信息（任务+需求）
		CTabItem basicTab=new CTabItem(tabFloder,SWT.NONE);
		 basicTab.setText(Constants.getStringVaule("tabItem_basicinfo"));
		 NewReqView comBasic=new NewReqView(tabFloder,data);
		 basicTab.setControl(comBasic.content);
		 tabFloder.setSelection(basicTab);
		 //开发提交的详细信息
		 CTabItem detailTab=new CTabItem(tabFloder,SWT.NONE);
		 detailTab.setText(Constants.getStringVaule("tabItem_detailinfo"));
		 DevSubmitView newReqView=new DevSubmitView(tabFloder,this.data,editable) ;
		 detailTab.setControl(newReqView.self);
		 newReqView.self.pack();
		 newReqView.self.layout(true);
		 //流程过往记录
		 CTabItem flowTab=new CTabItem(tabFloder,SWT.NONE);
		 flowTab.setText(Constants.getStringVaule("tabItem_flowrecord"));
		 Composite comFlow=new Composite(tabFloder,SWT.NONE);
		 comFlow.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 comFlow.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 
		 Table flowTable=new Table(comFlow,SWT.BORDER|SWT.FULL_SELECTION);
		 flowTable.setHeaderVisible(true);
		 flowTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 100));
		 flowTable.setLinesVisible(true);
		 flowTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		 
		 for(int i=0;i<fowHeader.length;i++){
				TableColumn tablecolumn=new TableColumn(flowTable,SWT.BORDER);
				tablecolumn.setText(fowHeader[i]);
				tablecolumn.setMoveable(true);
			}
		 
		 List<RequirementLog> steps=TASK.getLogs(this.data.getId());
		 if(steps!=null&&steps.size()>0){
			 Map<String,String> Process=Dictionary.getDictionaryMap("REQUIREMENT.PROGRESS");
			 for(RequirementLog step:steps){
				 String stepName=Process.get(step.getStepID());
				 TableItem tableItem=new TableItem(flowTable,SWT.BORDER);
    	         tableItem.setText(new String[]{stepName,step.getUserName(),step.getTime(),step.getComment()});
    	         tableItem.setData(step);
			 }
		 }
		 for(int j=0;j<fowHeader.length;j++){		
			flowTable.getColumn(j).pack();
			}	
			flowTable.pack();
		 comFlow.pack();
		 flowTab.setControl(comFlow);
		 tabFloder.layout(true);
	}
	
	//提交版本管理员做版本整合
	public   class AgreePkgAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String nextUser=nexter.getText();
			String nextUserID=StringUtil.getUserIdFromName(nextUser);
			String seq=UUID.randomUUID().toString();
			seq=seq.replace("-", "");			
			data.logStep(seq, Context.session.userID, "发版审核通过", data.getStatus());
			data.progress(VStep.VersionMake, nextUserID, Context.session.userID);
			//发版状态为可组织版本
			data.ResetReleaseFlag(TaskBean.ReleaseStatus.Organize.ordinal()+"");
			String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"发版审核【"+data.getTname()+"】通过,可以组织发版。请查阅。";
			IMessage message=new IMessage(nextUserID,msg);
			message.addMsg();
			MyRequirementView.getInstance(null).refreshTree();
			btnAgree.setEnabled(false);
			btnReturn.setEnabled(false);
			if(nexter!=null)
				nexter.setEnabled(false);
		}
	}
	
	//退回开发
	public class ReturnPkgAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			EitCommetView view=new EitCommetView(data,btnAgree,btnReturn);
			view.show();
		}
	}
	

	
}
