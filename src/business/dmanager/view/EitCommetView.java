package business.dmanager.view;

import java.util.UUID;

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

import resource.Constants;
import resource.Context;
import resource.IMessage;
import utils.DateUtil;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;
import bean.TaskBean;
import bean.VStep;


/**
 * @author Administrator
 *审批意见录入页面，常用来做审批退回
 */
public class EitCommetView {
	private  Shell shell=null;
	private Text comment=null;
	private TaskBean data=null;
	public Button btn1,btn2=null;
	public EitCommetView(TaskBean req,Button btn1,Button btn2){
		this.data=req;
		this.btn1=btn1;
		this.btn2=btn2;
	}
	
	public void show(){
		shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		shell.setText(Constants.getStringVaule("window_flowprocess"));
		shell.setLocation(AppView.getInstance().getCentreScreenPoint());
		shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
		Composite Panel=new Composite(shell,SWT.BORDER);
		Panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 220));
		Panel.setLayout(LayoutUtils.getComGridLayout(4, 5)); 
		
		Label label=new Label(Panel,SWT.NONE);
		label.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1, 0, 0));
		label.setText(Constants.getStringVaule("group_appcmt"));
		comment=new Text(Panel,SWT.MULTI|SWT.V_SCROLL);
		comment.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
		
		Button btnOk=new Button(Panel,SWT.PUSH);
		btnOk.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1, 0, 0));
		btnOk.setText("    "+Constants.getStringVaule("btn_submit")+"    ");
		btnOk.addSelectionListener(new SubmitAction());
		Panel.pack();
		shell.pack();
		shell.open();
		shell.addShellListener(new ShellCloseAction());
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			shell.dispose();
		}
	}
	
	//自动退回处理
	public class SubmitAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String reason=comment.getText();
			if(!StringUtil.isNullOrEmpty(reason, 600)){
				 String user= data.getOwner();//退回到任务责任人
				 String seq=UUID.randomUUID().toString();
				 String step=data.getStatus();
				 String nextStep=(Integer.parseInt(step)-1)+"";//退回状态为上一步骤
				 seq=seq.replace("-", "");
				data.logStep(seq, Context.session.userID, reason, step);
				data.progress(nextStep, user, Context.session.userID);
				//开发过程版本审批不通过，任务发版状态仍然是初始化状态，保证不能跳过审核
				data.ResetReleaseFlag(TaskBean.ReleaseStatus.CheckReturn.ordinal()+"");
				String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"发版审核【"+data.getTname()+"】未通过("+reason+")！请查阅。";
				IMessage message=new IMessage(user,msg);
				message.addMsg();
				//刷新任务浏览器，否则会重复
				MyRequirementView.getInstance(null).refreshTree();
			   btn1.setEnabled(false);
			   btn2.setEnabled(false);
			   shell.dispose();
			}else{
				 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				  box.setText(Constants.getStringVaule("messagebox_alert"));
				 box.setMessage("审批意见校验不通过");
				 box.open();	
			}
			return;
		}
	}
	
}
