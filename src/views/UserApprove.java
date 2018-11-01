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

import resource.CommonCallBack;
import resource.Constants;
import resource.Context;
import resource.SecurityCenter;
import resource.UserLogin;
import utils.LayoutUtils;
import utils.StringUtil;

public class UserApprove {
	private static UserApprove unique_instance;
	private Shell Approve;
	private Text textName,textPasswd;
	public CommonCallBack callBackOK;
	public CommonCallBack callBackCancel;
	
	public  static UserApprove getInstance(){
		if(unique_instance==null)
			unique_instance=new UserApprove();
		return unique_instance;
	} 
	
	public void setOKCommonCallBack(CommonCallBack callBack){
		this.callBackOK=callBack;
	}
	
	public void setCancelCommonCallBack(CommonCallBack callBack){
		this.callBackCancel=callBack;
	}
	
    private UserApprove(){}
	
	public void show(String functionName){
		Approve=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		Approve.setText(functionName);
		Point point = AppView.getInstance().getDisplay().getCurrent().getCursorLocation();
		Approve.setLocation(point);
		Approve.setLayout(LayoutUtils.getComGridLayout(1, 0));
		Composite pannel=new Composite(Approve,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 170));
		pannel.setLayout(LayoutUtils.getComGridLayout(8, 0));
		
		Label labName=new Label(pannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_user"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, true, 3, 1, 0, 0));
		textName= new Text(pannel,SWT.BORDER);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 5, 1, 0, 0));
		
		Label labPasswd=new Label(pannel,SWT.NONE);
		labPasswd.setText(Constants.getStringVaule("label_passwd"));
		labPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, true, 3, 1, 0, 0));
		textPasswd= new Text(pannel,SWT.BORDER|SWT.PASSWORD);
		textPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 5, 1, 0, 0));
		
		Button btnApply=new Button(pannel,SWT.PUSH);
		btnApply.setText("   "+Constants.getStringVaule("btn_approveOK")+"   ");
		btnApply.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 4, 1, 0, 0));
		btnApply.addSelectionListener(new LoginAction());
		 
		 Button btnCancle=new Button(pannel,SWT.PUSH);
		 btnCancle.setText("   "+Constants.getStringVaule("btn_approveNot")+"   ");
		 btnCancle.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 4, 1, 0, 0));
		 btnCancle.addSelectionListener(new WindowCloseAction());
		 pannel.pack();
		 Approve.pack();
		 Approve.open();
		 Approve.addShellListener(new ShellCloseAction());
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			Approve.dispose();
		}	
	}
	
	public boolean login(){
		
		return false;
	}
	
	public class LoginAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String username=textName.getText();
			if(Context.session.userID.equals(username)){
				String msg="不允许本人复核！";
				MessageBox box=new MessageBox(Approve,SWT.ICON_WARNING|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(msg);
				box.open();
			}else{
				String userpasswd=textPasswd.getText();
				String passwd=SecurityCenter.getInstance().encrypt(userpasswd, Context.EncryptKey);
				//复核用户请输入用户编号
				if(StringUtil.isNullOrEmpty(username)||StringUtil.isNullOrEmpty(userpasswd)){
					String msg="请先登录才能进行现场复核！";
					MessageBox box=new MessageBox(Approve,SWT.ICON_WARNING|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					box.open();
				}
				else{
					UserLogin login=new UserLogin(username);
					String ret=login.login();
					if(StringUtil.isNullOrEmpty(ret)){
						//判断密码
						String passwdRmt=login.getPasswd();
						//密码为空，判定为首次登录，需要修改密码
						if(StringUtil.isNullOrEmpty(passwdRmt)){
							LoginPasswdView Passwd=new LoginPasswdView(Approve,login);
							Passwd.show();
						}else{
							if(!passwd.equals(passwdRmt)){
								int errorCnt=Integer.parseInt(login.getErrorTimes());
								errorCnt=errorCnt+1;
								String msg="";
								if(errorCnt>=UserLogin.maxPasswdErrorTimes){
									 msg="密码输入错误！密码错误次数超过最大允许次数。账户已被锁定！请联系管理员解锁。";
									 login.lockUser(username);
								}else{
									 msg="密码输入错误！";
								}
								MessageBox box=new MessageBox(Approve,SWT.ICON_WARNING|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(msg);
								box.open();
							}else{
								Approve.dispose();
								callBackOK.action();
							}
						}
						
					}else{
						MessageBox box=new MessageBox(Approve,SWT.ICON_WARNING|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(ret);
						box.open();
						
					}
				}
			}
			
		}
	}
	
	public class WindowCloseAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String username=textName.getText();
			if(Context.session.userID.equals(username)){
				String msg="不允许本人复核！";
				MessageBox box=new MessageBox(Approve,SWT.ICON_WARNING|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(msg);
				box.open();
			}else{
				String userpasswd=textPasswd.getText();
				String passwd=SecurityCenter.getInstance().encrypt(userpasswd, Context.EncryptKey);
				//复核用户请输入用户编号
				if(StringUtil.isNullOrEmpty(username)||StringUtil.isNullOrEmpty(userpasswd)){
					String msg="请先登录才能进行现场复核！";
					MessageBox box=new MessageBox(Approve,SWT.ICON_WARNING|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					box.open();
				}
				else{
					UserLogin login=new UserLogin(username);
					String ret=login.login();
					if(StringUtil.isNullOrEmpty(ret)){
						//判断密码
						String passwdRmt=login.getPasswd();
						//密码为空，判定为首次登录，需要修改密码
						if(StringUtil.isNullOrEmpty(passwdRmt)){
							LoginPasswdView Passwd=new LoginPasswdView(Approve,login);
							Passwd.show();
						}else{
							if(!passwd.equals(passwdRmt)){
								int errorCnt=Integer.parseInt(login.getErrorTimes());
								errorCnt=errorCnt+1;
								String msg="";
								if(errorCnt>=UserLogin.maxPasswdErrorTimes){
									 msg="密码输入错误！密码错误次数超过最大允许次数。账户已被锁定！请联系管理员解锁。";
									 login.lockUser(username);
								}else{
									 msg="密码输入错误！";
								}
								MessageBox box=new MessageBox(Approve,SWT.ICON_WARNING|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(msg);
								box.open();
							}else{
								Approve.dispose();
								callBackCancel.action();
							}
						}
						
					}else{
						MessageBox box=new MessageBox(Approve,SWT.ICON_WARNING|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(ret);
						box.open();
						
					}
				}
			}
		}
	}

	
	
	
}
