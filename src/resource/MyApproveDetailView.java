package resource;

import model.TASK;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

import bean.DelayDate;
import bean.TaskBean;

public class MyApproveDetailView {
	private  Shell shell=null;
	public DelayDate data=null;
	public  int opFlag=0;
	
	public MyApproveDetailView(DelayDate data,int opType){
		this.data=data;
		this.opFlag=opType;
	}
	
	public Text textComment;
	public void show(){
		shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL|SWT.MAX);
		shell.setText(Constants.getStringVaule("window_flowprocess"));
		shell.setLocation(AppView.getInstance().getCentreScreenPoint());
		shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
		Composite Panel=new Composite(shell,SWT.BORDER);
		Panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 600, 300));
		Panel.setLayout(LayoutUtils.getComGridLayout(4, 5)); 
		
		Label labelApprID=new Label(Panel,SWT.NONE);
		labelApprID.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
		labelApprID.setText(Constants.getStringVaule("header_approveID"));
		Text textApprID=new Text(Panel,SWT.BORDER);
		textApprID.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 3, 1, 0, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getApplyID())){
			textApprID.setText(this.data.getApplyID());
			textApprID.setEditable(false);
		}
		
		Label applyUser=new Label(Panel,SWT.NONE);
		applyUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
		applyUser.setText(Constants.getStringVaule("header_applyUser"));
		Text textApprUserID=new Text(Panel,SWT.BORDER);
		textApprUserID.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 3, 1, 0, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getApplyUserFullName())){
			textApprUserID.setText(this.data.getApplyUserFullName());
			textApprUserID.setEditable(false);
		}
		
		Label applyTime=new Label(Panel,SWT.NONE);
		applyTime.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
		applyTime.setText(Constants.getStringVaule("header_applyUser"));
		Text textApprTime=new Text(Panel,SWT.BORDER);
		textApprTime.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 3, 1, 0, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getApplyTime())){
			textApprTime.setText(this.data.getApplyTime());
			textApprTime.setEditable(false);
		}
		
		Label applyName=new Label(Panel,SWT.NONE);
		applyName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
		applyName.setText(Constants.getStringVaule("header_applyUser"));
		Text textAppName=new Text(Panel,SWT.BORDER);
		textAppName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 3, 1, 0, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getName())){
			textAppName.setText(this.data.getName());
			textAppName.setEditable(false);
		}
		
		Label labTaskRDate=new Label(Panel,SWT.NONE);
		labTaskRDate.setText(Constants.getStringVaule("label_taskddate"));
		labTaskRDate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1, 0, 0));
		Text textApplyODate=new Text(Panel,SWT.BORDER);
		textApplyODate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getOldDate())){
			textApplyODate.setText(this.data.getOldDate());
			textApplyODate.setEditable(false);
		}
		
		Label labNewDate=new Label(Panel,SWT.NONE);
		labNewDate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
		labNewDate.setText(Constants.getStringVaule("label_delaydate"));
		Text textApplyNDate=new Text(Panel,SWT.BORDER);
		textApplyNDate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getNewDate())){
			textApplyNDate.setText(this.data.getNewDate());
			textApplyNDate.setEditable(false);
		}
		
		Label labReason=new Label(Panel,SWT.NONE);
		labReason.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
		labReason.setText(Constants.getStringVaule("label_reason"));
		Text textReason=new Text(Panel,SWT.MULTI|SWT.V_SCROLL|SWT.BORDER);
		textReason.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getReason())){
			textReason.setText(this.data.getReason());
			textReason.setEditable(false);
		}
		
		Label labApprUser=new Label(Panel,SWT.NONE);
		labApprUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
		labApprUser.setText(Constants.getStringVaule("header_currentUser"));
		Text textApprName=new Text(Panel,SWT.BORDER);
		textApprName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 3, 1, 0, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getApprUserFullName())){
			textApprName.setText(this.data.getApprUserFullName());
			textApprName.setEditable(false);
		}
		
		Label labComment=new Label(Panel,SWT.NONE);
		labComment.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
		labComment.setText(Constants.getStringVaule("header_processcmt"));
		textComment=new Text(Panel,SWT.MULTI|SWT.V_SCROLL|SWT.BORDER);
		textComment.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getComment())){
			textComment.setText(this.data.getComment());
		}
		textComment.setEditable(false);
		
		 Button btnSubmit=new Button(Panel,SWT.PUSH);
		 btnSubmit.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
		 btnSubmit.setToolTipText("   "+Constants.getStringVaule("btn_submit")+"   ");
		 btnSubmit.setImage(Icons.getOkIcon());
		 btnSubmit.addSelectionListener(new ApproveDelayAction());
		 
		 Button btnCancel=new Button(Panel,SWT.PUSH);
		 btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
		 btnCancel.setToolTipText("   "+Constants.getStringVaule("btn_cancel")+"   ");
		 btnCancel.setImage(Icons.getReturnIcon());
		 btnCancel.addSelectionListener(new RejectDelayAction());
		
		if(1==opFlag){
			btnSubmit.setVisible(true);
			btnCancel.setVisible(true);
			textComment.setEditable(true);
		}else{
			btnSubmit.setVisible(false);
			btnCancel.setVisible(false);
		}
		Panel.pack();
		
		Composite taskPanel=new Composite(shell,SWT.BORDER);
		taskPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 600, 500));
		taskPanel.setLayout(LayoutUtils.getComGridLayout(1, 5)); 
		
		TaskBean taskData=TASK.getReq(this.data.getStaskID());
		ReqDetailView rdv=new ReqDetailView(taskPanel,taskData);
		taskPanel.pack();
		
		shell.pack();
		shell.open();
		shell.addShellListener(new ShellCloseAction());
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			shell.dispose();
		}
	}
	
	public class ApproveDelayAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			//申请记录变化
			String cmt=textComment.getText();
			if(StringUtil.isNullOrEmpty(cmt)){
				cmt="延期审批通过";
			}else{
				if(cmt.length()>200){
					cmt=cmt.substring(0, 200);
				}
			}
			
			//任务记录更新|需求记录更新
			data.SetAppr(DelayDate.Status.Approved.ordinal()+"", cmt);
			//操作提示
			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
			 box.setText(Constants.getStringVaule("messagebox_alert"));
			 box.setMessage(Constants.getStringVaule("alert_successoperate"));
			 box.open();	
			//窗口关闭
			 shell.dispose();
		}
	}
	
	public class RejectDelayAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			//申请记录变化
			String cmt=textComment.getText();
			if(!StringUtil.isNullOrEmpty(cmt)){
					cmt=cmt.substring(0, 200);
					data.SetAppr(DelayDate.Status.Rejected.ordinal()+"", cmt);
					//操作提示
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					 box.setText(Constants.getStringVaule("messagebox_alert"));
					 box.setMessage(Constants.getStringVaule("alert_successoperate"));
					 box.open();	
					//窗口关闭
				   shell.dispose();
			}else{
				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 box.setText(Constants.getStringVaule("messagebox_alert"));
				 box.setMessage("请输入驳回理由！");
				 box.open();	
			}
		}
	}
	
}
