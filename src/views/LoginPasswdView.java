package views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import resource.Constants;
import resource.Context;
import resource.SecurityCenter;
import resource.UserLogin;
import utils.CheckUtil;
import utils.LayoutUtils;


/**
 * @author David
 *
 */
public class LoginPasswdView {

	private Shell parent;
	private Shell Passwd;
	private Text textPasswd,surePasswd;
	private UserLogin action;
	public LoginPasswdView(Shell com,UserLogin action){
		this.parent=com;
		this.action=action;
	}
	
	public void show(){
		Passwd=new Shell(parent,SWT.CLOSE|SWT.SYSTEM_MODAL);
		Passwd.setText(Constants.getStringVaule("window_passwdset"));
		Point point = AppView.getInstance().getDisplay().getCurrent().getCursorLocation();
		Passwd.setLocation(point);
		Passwd.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
		Composite pannel=new Composite(Passwd,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 140));
		pannel.setLayout(LayoutUtils.getComGridLayout(10, 0));
		Label labPasswd=new Label(pannel,SWT.NONE);
		labPasswd.setText(Constants.getStringVaule("label_passwd"));
		labPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, true, 2, 1, 0, 0));
		textPasswd= new Text(pannel,SWT.BORDER|SWT.PASSWORD);
		textPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 4, 1, 0, 0));
		Label labPasswdTip=new Label(pannel,SWT.NONE);
		labPasswdTip.setText("(6~18位字母和数字组成)");
		labPasswdTip.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, true, 4, 1, 0, 0));
		
		Label labSurePasswd=new Label(pannel,SWT.NONE);
		labSurePasswd.setText(Constants.getStringVaule("label_surepasswd"));
		labSurePasswd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, true, 2, 1, 0, 0));
		surePasswd= new Text(pannel,SWT.BORDER|SWT.PASSWORD);
		surePasswd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 4, 1, 0, 0));
		
		 Button btnApply=new Button(pannel,SWT.PUSH);
		 btnApply.setText("   "+Constants.getStringVaule("btn_apply")+"   ");
		 btnApply.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 5, 1, 0, 0));
		 btnApply.addSelectionListener(new SetPasswdAction());
		 
		 Button btnCancle=new Button(pannel,SWT.PUSH);
		 btnCancle.setText("   "+Constants.getStringVaule("btn_cancel")+"   ");
		 btnCancle.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 5, 1, 0, 0));
		 btnCancle.addSelectionListener(new WindowCloseAction());
		 
		pannel.pack();
		Passwd.pack();
		Passwd.open();
		Passwd.addShellListener(new ShellCloseAction());
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			Passwd.dispose();
		}	
	}
	
	public class WindowCloseAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Passwd.dispose();
		}
	}
	
	public class SetPasswdAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String passwd=textPasswd.getText();
			if(!CheckUtil.IsPasswLength(passwd)){
				String msg="密码长度必须在【6-18】位范围内！";
				MessageBox box=new MessageBox(Passwd,SWT.ICON_WARNING|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(msg);
				box.open();
				return;
			}
			if(!CheckUtil.IsPassword(passwd)){
				String msg="密码必须由字母和数字组成！";
				MessageBox box=new MessageBox(Passwd,SWT.ICON_WARNING|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(msg);
				box.open();
				return;
			}
			String passwdAgain=surePasswd.getText();
			if(!passwdAgain.equals(passwd)){
				String msg="两次输入的密码不一致，请重新输入！";
				MessageBox box=new MessageBox(Passwd,SWT.ICON_WARNING|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(msg);
				box.open();
				return;
			}else{//存储密码
				String enPasswd=SecurityCenter.getInstance().encrypt(passwd, Context.EncryptKey);
				action.setPasswd(enPasswd);
				Passwd.dispose();
			}
		}
	}
}
