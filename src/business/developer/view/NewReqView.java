package business.developer.view;



import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import model.GROUPUSER;
import model.TASK;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bean.DelayDate;
import bean.GroupUserBean;
import bean.RFile;
import bean.TaskBean;
import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.FtpFileService;
import resource.Icons;
import resource.Paths;
import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;


//任务对应的需求信息和分配信息
public class NewReqView {
	
	private Composite parent;
	public Composite content;
	public TaskBean bean=null;
	private  Combo cmbApp=null;

	public NewReqView(Composite com,TaskBean bean){
		 this.parent=com;
		 this.bean=bean;
		 content=new  Composite(com,SWT.NONE);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 SashForm view=new SashForm(content,SWT.VERTICAL);
		view.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		view.setLayout(LayoutUtils.getComGridLayout(1, 1));
		this.taskView(view);
		this.reqView(view);
		 view.setWeights(new int[]{40,60});
		 content.pack();
	}
	
	private void taskView(Composite parent){
		 Group taskGroup=new Group(parent,SWT.SINGLE|SWT.V_SCROLL);
		 taskGroup.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 taskGroup.setLayout(LayoutUtils.getComGridLayout(6, 0));
		 taskGroup.setText(Constants.getStringVaule("group_basic"));
		 
		 Label labTaskNo=new Label(taskGroup,SWT.NONE);
		 labTaskNo.setText(Constants.getStringVaule("label_taskno"));
		 labTaskNo.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskNo=new Text(taskGroup,SWT.BORDER);
		 txtTaskNo.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getId())){
			 txtTaskNo.setText(this.bean.getId());
			 txtTaskNo.setEditable(false);
		 }
		 
		 Label labTaskName=new Label(taskGroup,SWT.NONE);
		 labTaskName.setText(Constants.getStringVaule("label_taskname"));
		 labTaskName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskName=new Text(taskGroup,SWT.BORDER);
		 txtTaskName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getTname())){
			 txtTaskName.setText(this.bean.getTname());
			 txtTaskName.setEditable(false);
		 }
		 
		 Label labTaskApp=new Label(taskGroup,SWT.NONE);
		 labTaskApp.setText(Constants.getStringVaule("label_taskapp"));
		 labTaskApp.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskApp=new Text(taskGroup,SWT.BORDER);
		 txtTaskApp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getApp())){
			Map<String,String> apps=  Dictionary.getDictionaryMap("APP");
			String Apps=this.bean.getApp();
			String[] moreApps=Apps.split("\\|");
			String appName=apps.get(moreApps[0]);
			 txtTaskApp.setText(appName);
			 txtTaskApp.setEditable(false);
		 }
		 
		 
		 Label labTaskRDate=new Label(taskGroup,SWT.NONE);
		 labTaskRDate.setText(Constants.getStringVaule("label_taskrdate"));
		 labTaskRDate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskRDate=new Text(taskGroup,SWT.BORDER);
		 txtTaskRDate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getRdate())){
			 txtTaskRDate.setText(this.bean.getRdate());
			 txtTaskRDate.setEditable(false);
		 }
		 
		 
		 Label labTaskCrtor=new Label(taskGroup,SWT.NONE);
		 labTaskCrtor.setText(Constants.getStringVaule("label_taskcrt"));
		 labTaskCrtor.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskCrtor=new Text(taskGroup,SWT.BORDER);
		 txtTaskCrtor.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getCrtUser())){
			 txtTaskCrtor.setText(this.bean.getCrtUserShowName());
			 txtTaskCrtor.setEditable(false);
		 }
		 
		 //开发进行延期 开发完成时间
		 Label labTaskADate=new Label(taskGroup,SWT.NONE);
		 labTaskADate.setText(Constants.getStringVaule("label_taskadate"));
		 labTaskADate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskADate=new Text(taskGroup,SWT.BORDER);
		 txtTaskADate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 1, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getArrangeDate())){
			 txtTaskADate.setText(this.bean.getArrangeDate());
			 txtTaskADate.setEditable(false);
		 }
		 Button btnDelay=new Button(taskGroup,SWT.PUSH);
		 btnDelay.setText(Constants.getStringVaule("btn_delay"));
		 btnDelay.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 btnDelay.addSelectionListener(new DelayTaskAction());
		 btnDelay.setVisible(false);
		 if(Context.session.userID.equals(this.bean.getOwner())){
			 btnDelay.setVisible(true);
		 }
		 
		 Label labTaskOwner=new Label(taskGroup,SWT.NONE);
		 labTaskOwner.setText(Constants.getStringVaule("label_taskowner"));
		 labTaskOwner.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskOwner=new Text(taskGroup,SWT.BORDER);
		 txtTaskOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getOwner())){
			 txtTaskOwner.setText(this.bean.getOwnerShowName());
			 txtTaskOwner.setEditable(false);
		 }
		 
		 Label labTaskDDate=new Label(taskGroup,SWT.NONE);
		 labTaskDDate.setText(Constants.getStringVaule("label_taskddate"));
		 labTaskDDate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskDDate=new Text(taskGroup,SWT.BORDER);
		 txtTaskDDate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getOverDate())){
			 txtTaskDDate.setText(this.bean.getOverDate());
		 }
		 txtTaskDDate.setEditable(false);
		
		 
		 
		 Label labTaskBatch=new Label(taskGroup,SWT.NONE);
		 labTaskBatch.setText(Constants.getStringVaule("label_taskbatch"));
		 labTaskBatch.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskBatch=new Text(taskGroup,SWT.BORDER);
		 txtTaskBatch.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getScheID())){
			 txtTaskBatch.setText(this.bean.getScheID());
			 txtTaskBatch.setEditable(false);
		 }
		 

		 Label labTaskStatus=new Label(taskGroup,SWT.NONE);
		 labTaskStatus.setText(Constants.getStringVaule("label_taskstatus"));
		 labTaskStatus.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTaskStatus=new Text(taskGroup,SWT.BORDER);
		 txtTaskStatus.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getStatus())){
			 Map<String,String> status=Dictionary.getDictionaryMap("TASK_DEF.STATUS");
			 String statusVal=status.get(this.bean.getStatus());
			 txtTaskStatus.setText(statusVal);
			 txtTaskStatus.setEditable(false);
		 }
		 taskGroup.pack();
	}
	
	public String[] header=new String[]{StringUtil.rightPad(Constants.getStringVaule("header_filename"), 100, " ")};
	public Text txtReqLink=null;
	private void reqView(Composite parent){
		 Group reqGroup=new Group(parent,SWT.SINGLE|SWT.V_SCROLL);
		 reqGroup.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 reqGroup.setLayout(LayoutUtils.getComGridLayout(6, 0));
		 reqGroup.setText(Constants.getStringVaule("group_rinfo"));
		 
		 Label labReqNo=new Label(reqGroup,SWT.NONE);
		 labReqNo.setText(Constants.getStringVaule("label_reqno"));
		 labReqNo.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqNo=new Text(reqGroup,SWT.BORDER);
		 txtReqNo.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getReqID())){
			 txtReqNo.setText(this.bean.getReqID());
			 txtReqNo.setEditable(false);
		 }
		 
		 Label labReqTitle=new Label(reqGroup,SWT.NONE);
		 labReqTitle.setText(Constants.getStringVaule("label_reqtitle"));
		 labReqTitle.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqTitle=new Text(reqGroup,SWT.BORDER);
		 txtReqTitle.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(this.bean.getTname())){
			 txtReqTitle.setText(this.bean.getTname());
			 txtReqTitle.setEditable(false);
		 }

		 Label labReqSUser=new Label(reqGroup,SWT.NONE);
		 labReqSUser.setText(Constants.getStringVaule("label_reqsuer"));
		 labReqSUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqSUser=new Text(reqGroup,SWT.BORDER);
		 txtReqSUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
	
		 
		 Label labReqDept=new Label(reqGroup,SWT.NONE);
		 labReqDept.setText(Constants.getStringVaule("label_reqdept"));
		 labReqDept.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqDept=new Text(reqGroup,SWT.BORDER);
		 txtReqDept.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		

		 Label labReqSDate=new Label(reqGroup,SWT.NONE);
		 labReqSDate.setText(Constants.getStringVaule("label_reqsdate"));
		 labReqSDate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqSDate=new Text(reqGroup,SWT.BORDER);
		 txtReqSDate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		
		 
		 Label labReqRDate=new Label(reqGroup,SWT.NONE);
		 labReqRDate.setText(Constants.getStringVaule("label_reqrdate"));
		 labReqRDate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqRDate=new Text(reqGroup,SWT.BORDER);
		 txtReqRDate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		
		 
		 Label labReqSource=new Label(reqGroup,SWT.NONE);
		 labReqSource.setText(Constants.getStringVaule("label_reqsrouce"));
		 labReqSource.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqSource=new Text(reqGroup,SWT.BORDER);
		 txtReqSource.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		
		 
		 Label labReqType=new Label(reqGroup,SWT.NONE);
		 labReqType.setText(Constants.getStringVaule("label_reqtype"));
		 labReqType.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqType=new Text(reqGroup,SWT.BORDER);
		 txtReqType.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		

		 Label labReqLevel=new Label(reqGroup,SWT.NONE);
		 labReqLevel.setText(Constants.getStringVaule("label_reqLevel"));
		 labReqLevel.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqLevel=new Text(reqGroup,SWT.BORDER);
		 txtReqLevel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		

		 Label labReqStatus=new Label(reqGroup,SWT.NONE);
		 labReqStatus.setText(Constants.getStringVaule("label_reqStatus"));
		 labReqStatus.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqStatus=new Text(reqGroup,SWT.BORDER);
		 txtReqStatus.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		

		 Label labReqBK=new Label(reqGroup,SWT.NONE);
		 labReqBK.setText(Constants.getStringVaule("label_reqbk"));
		 labReqBK.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqBK=new Text(reqGroup,SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		 txtReqBK.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 5, 1, 0, 0));
		

		 Label labReqRDesc=new Label(reqGroup,SWT.NONE);
		 labReqRDesc.setText(Constants.getStringVaule("label_reqrdesc"));
		 labReqRDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtReqRDesc=new Text(reqGroup,SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		 txtReqRDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 5, 1, 0, 0));
		
        
		 Label labReqLink=new Label(reqGroup,SWT.NONE);
		 labReqLink.setText(Constants.getStringVaule("label_reqlink"));
		 labReqLink.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 txtReqLink=new Text(reqGroup,SWT.BORDER);
		 txtReqLink.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		
		 
		 if(this.bean.getReq()!=null){

			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getSubmitUser())){
				 txtReqSUser.setText(this.bean.getReq().getSubmitUser());
				 txtReqSUser.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getSubmitDept())){
				 txtReqDept.setText(this.bean.getReq().getSubmitDept());
				 txtReqDept.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getSubmitDate())){
				 txtReqSDate.setText(this.bean.getReq().getSubmitDate());
				 txtReqSDate.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getExpectDate())){
				 txtReqRDate.setText(this.bean.getReq().getExpectDate());
				 txtReqRDate.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getSource())){
				 //需求来源
				 Map<String,String> rsrc=Dictionary.getDictionaryMap("BACKLOG.RSRC");
				 String rsrcName=rsrc.get(this.bean.getReq().getSource());
				 txtReqSource.setText(rsrcName);
				 txtReqSource.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getType())){
				 //需求类型
				 Map<String,String> rtype=Dictionary.getDictionaryMap("BACKLOG.RTYPE");
				 String rtypeName=rtype.get(this.bean.getReq().getType());
				 txtReqType.setText(rtypeName);
				 txtReqType.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getRclass())){
				 //需求紧急程度
				 Map<String,String> rclass=Dictionary.getDictionaryMap("BACKLOG.RCLASS");
				 String rclassName=rclass.get(this.bean.getReq().getRclass());
				 txtReqLevel.setText(rclassName);
				 txtReqLevel.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getStatus())){
				 //需求状态
				 Map<String,String> status=Dictionary.getDictionaryMap("BACKLOG.STATUS");
				 String reqStatus=status.get(this.bean.getReq().getStatus());
				 txtReqStatus.setText(reqStatus);
				 txtReqStatus.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getBackground())){
				 txtReqBK.setText(this.bean.getReq().getBackground());
				 txtReqBK.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getExpectDesc())){
				 txtReqRDesc.setText(this.bean.getReq().getExpectDesc());
				 txtReqRDesc.setEditable(false);
			 }
			 
			 if(!StringUtil.isNullOrEmpty(this.bean.getReq().getLink())){
				 txtReqLink.setText(this.bean.getReq().getLink());
				 txtReqLink.setEditable(false);
			 }
		 }
		 
		 Button btnBrower=new  Button(reqGroup,SWT.PUSH);
		 btnBrower.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		 btnBrower.setImage(Icons.getBrowserIcon());
		 btnBrower.setToolTipText(Constants.getStringVaule("btn_tips_open"));
		 btnBrower.addSelectionListener(new OpenReqLinkAction());
		 
			Label labAttach=new Label(reqGroup,SWT.NONE);
			labAttach.setText(Constants.getStringVaule("label_reqAttach"));
			labAttach.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		 
			 Table attachTable=new Table(reqGroup,SWT.V_SCROLL|SWT.MULTI|SWT.BORDER);
			attachTable.setHeaderVisible(true);
			attachTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 5, 1, 0, 100));
			attachTable.setLinesVisible(true);
			attachTable.addMouseListener(new FileItemAction(attachTable));//打开附件的方法
			attachTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
			  for(int i=0;i<header.length;i++){
					TableColumn tablecolumn=new TableColumn(attachTable,SWT.BORDER);
					tablecolumn.setText(header[i]);
					tablecolumn.setMoveable(true);
				}
			  //源码附件清单
			  List<RFile> files=TASK.getAttach(this.bean.getId(),this.bean.getCurrentVersion(),TaskBean.AttachType.Requiremnt.ordinal()+"");
			  if(files!=null&&files.size()>0){
				  for(RFile rfile:files){
					    TableItem tableItem=new TableItem(attachTable,SWT.BORDER);
						tableItem.setText(new String[]{rfile.getFileName()});
						Image icon=Icons.getFileImage(rfile.getFileName());
						tableItem.setImage(icon);
				  }
			  }
				for(int j=0;j<header.length;j++){		
					attachTable.getColumn(j).pack();
				}
		   attachTable.pack();
		   reqGroup.pack();
	}
	
	//提交开发任务延期申请
	public Shell shell;
	public DateTime dataDate;
	public Combo nexter;
	public Text comment;
	public class DelayTaskAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			  shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL|SWT.RESIZE);
			  shell.setText(Constants.getStringVaule("btn_delay"));
			  shell.setLocation(AppView.getInstance().getCentreScreenPoint());
			  shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
			    Composite Panel=new Composite(shell,SWT.BORDER);
				Panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 760, 200));
				Panel.setLayout(LayoutUtils.getComGridLayout(8, 5)); 
				
				Label label=new Label(Panel,SWT.NONE);
				label.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
				label.setText(Constants.getStringVaule("label_delaydate"));
				dataDate=new DateTime(Panel, SWT.DATE );
				dataDate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, false, false, 2, 1, 0, 0));
				//dataDate.setYear(2016);
				
				 Label labNexter=new Label(Panel,SWT.NONE);
				 labNexter.setText(Constants.getStringVaule("label_nextUser"));
				 labNexter.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
				 nexter=new Combo(Panel,SWT.DROP_DOWN);
				 nexter.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
				 String groupID="Flow0";
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
				 
				 Button btnSubmit=new Button(Panel,SWT.PUSH);
				 btnSubmit.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
				 btnSubmit.setToolTipText("   "+Constants.getStringVaule("btn_submit")+"   ");
				 btnSubmit.setImage(Icons.getOkIcon());
				 btnSubmit.addSelectionListener(new AddDelayApplyAction());
				 
				 Button btnCancel=new Button(Panel,SWT.PUSH);
				 btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
				 btnCancel.setToolTipText("   "+Constants.getStringVaule("btn_cancel")+"   ");
				 btnCancel.setImage(Icons.getReturnIcon());
				 btnCancel.addSelectionListener(new CancelDelayAction());
				 
				 Label labelCmt=new Label(Panel,SWT.NONE);
				 labelCmt.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
				 labelCmt.setText(Constants.getStringVaule("label_reason"));
				 comment=new Text(Panel,SWT.MULTI|SWT.V_SCROLL);
				 comment.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 7, 1, 0, 0));
				
				Panel.pack();
			  shell.pack();
			  shell.open();
			  shell.addShellListener(new PopShellCloseAction());
		}
	}
	
	public class PopShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			shell.dispose();
		}
	}
	
	public class CancelDelayAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			shell.dispose();
		}
	}
	
	public class AddDelayApplyAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String monStr=(dataDate.getMonth()+1)+"";
			monStr=StringUtil.leftpad(monStr, 2, "0");
			String dayStr=dataDate.getDay()+"";
			dayStr=StringUtil.leftpad(dayStr, 2, "0");
			String newDate=dataDate.getYear()+"-"+monStr+"-"+dayStr;
			String newDateStr=dataDate.getYear()+monStr+dayStr;
			String reason=comment.getText();
			String sysDate=DateUtil.getCurrentDate("yyyyMMdd");
			String nextUser=nexter.getText();
			String nextUserID=StringUtil.getUserIdFromName(nextUser);
			if(!StringUtil.isNullOrEmpty(reason, 600)&&newDateStr.compareTo(sysDate)>0){
				DelayDate data=new DelayDate();
				String seq=UUID.randomUUID().toString();
				seq=seq.replace("-", ""); 
				data.setApplyID(seq);
				data.setApplyUserID(Context.session.userID);
				data.setApprUserID(nextUserID);
				data.setReason(reason);
				data.setNewDate(newDate);
				data.setOldDate(bean.getRdate());
				data.setReqID(bean.getReqID());
				data.setStaskID(bean.getId());
				data.setStatus(DelayDate.Status.Apply.ordinal()+"");
				data.add();
				 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 box.setText(Constants.getStringVaule("messagebox_alert"));
				 box.setMessage(Constants.getStringVaule("alert_successoperate"));
				 box.open();	
				shell.dispose();
			}else{
				 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				  box.setText(Constants.getStringVaule("messagebox_alert"));
				 box.setMessage("延期日期或延期理由校验不通过");
				 box.open();	
			}
		}
	}
	
	
	public class FileItemAction extends MouseAdapter{
		public Table table;
		public FileItemAction(Table table){
			this.table=table;
		}
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem tableItem=table.getItem(new  Point(e.x,e.y));
			 if(tableItem!=null){
				 RFile rfile=(RFile)tableItem.getData();
				 String localPath=rfile.getLocalPath();
				 String  remotePath=rfile.getRpath();
				 if(StringUtil.isNullOrEmpty(localPath)){
					 String path=Paths.getInstance().getWorkDir();
			         String localDir=FileUtils.formatPath(path)+File.separator+bean.getId()+File.separator;
			         File dir=new File(localDir);
		            if(!dir.exists()){
		            	dir.mkdirs();
		            }
		        	boolean downResult=FtpFileService.getService().dowload(rfile.getFileName(), remotePath, localDir,rfile.getFileTime());    
		        	if(downResult){//下载成功的文件，尝试调用本地程序打开
		   				 localPath=localDir+File.separator+rfile.getFileName();
		   				  rfile.setLocalPath(localPath);
		   				  tableItem.setData(rfile);
		   				 FileUtils.openFileByLocal(localPath);
		        	}else{
		        		String msg="文件【"+rfile.getFileName()+"】下载失败。请稍后重试！";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
		        	}
				 }else{
					 String fileName=rfile.getFileName();
					 FileUtils.openFileByLocal(localPath);
				 }
			 }
		 }
	}
	
	public Shell popShell;
	 public class OpenReqLinkAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				try{
					String url=txtReqLink.getText();
					if(!StringUtil.isNullOrEmpty(url)){
						popShell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL|SWT.MAX);
						popShell.setText(Constants.getStringVaule("window_detail"));
						popShell.setLocation(AppView.getInstance().getCentreScreenPoint());
						popShell.setLayout(LayoutUtils.getComGridLayout(1, 0));
						Browser browser=new   Browser(popShell, SWT.EMBEDDED);
						browser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 800, 600));
						browser.setUrl(url);
						popShell.pack();
						popShell.open();
						popShell.addShellListener(new ShellCloseAction());
					}
				}catch(Exception exp){
					exp.printStackTrace();
				}
			}
	 }
	
		public class ShellCloseAction extends ShellAdapter{
			public void shellClosed(ShellEvent e){	
				popShell.dispose();
			}
		}
}
