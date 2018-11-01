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
 *�������¼��ҳ�棬�������������˻�
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
	
	//�Զ��˻ش���
	public class SubmitAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String reason=comment.getText();
			if(!StringUtil.isNullOrEmpty(reason, 600)){
				 String user= data.getOwner();//�˻ص�����������
				 String seq=UUID.randomUUID().toString();
				 String step=data.getStatus();
				 String nextStep=(Integer.parseInt(step)-1)+"";//�˻�״̬Ϊ��һ����
				 seq=seq.replace("-", "");
				data.logStep(seq, Context.session.userID, reason, step);
				data.progress(nextStep, user, Context.session.userID);
				//�������̰汾������ͨ�������񷢰�״̬��Ȼ�ǳ�ʼ��״̬����֤�����������
				data.ResetReleaseFlag(TaskBean.ReleaseStatus.CheckReturn.ordinal()+"");
				String msg=Context.session.userName+"("+Context.session.userID+")��"+DateUtil.getCurrentTime()+"������ˡ�"+data.getTname()+"��δͨ��("+reason+")������ġ�";
				IMessage message=new IMessage(user,msg);
				message.addMsg();
				//ˢ�������������������ظ�
				MyRequirementView.getInstance(null).refreshTree();
			   btn1.setEnabled(false);
			   btn2.setEnabled(false);
			   shell.dispose();
			}else{
				 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				  box.setText(Constants.getStringVaule("messagebox_alert"));
				 box.setMessage("�������У�鲻ͨ��");
				 box.open();	
			}
			return;
		}
	}
	
}
