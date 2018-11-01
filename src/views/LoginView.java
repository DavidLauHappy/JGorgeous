package views;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import bean.RoleBean;

import common.localdb.LocalDataHelper;



import resource.Constants;
import resource.Context;
import resource.Icons;
import resource.SecurityCenter;
import resource.UserLogin;
import utils.LayoutUtils;
import utils.StringUtil;

public class LoginView {

	private static LoginView unique_instance;
	private Button remeberPasswd, swtichRole;
	public  static LoginView getInstance(){
		if(unique_instance==null)
			unique_instance=new LoginView();
		return unique_instance;
	}
	private boolean loginResult=false;
	private LoginView(){}
	
	public void show(){
		Login=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		Login.setText(Constants.getStringVaule("window_userlogin"));
		Point point = AppView.getInstance().getDisplay().getCurrent().getCursorLocation();
		Login.setLocation(point);
		Login.setLayout(LayoutUtils.getComGridLayout(1, 0));
		Composite pannel=new Composite(Login,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 170));
		pannel.setLayout(LayoutUtils.getComGridLayout(10, 0));
		
		Label labName=new Label(pannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_user"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, true, 3, 1, 0, 0));
		textName= new Text(pannel,SWT.BORDER);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 5, 1, 0, 0));
		textName.setText(Context.session.userID);
	    swtichRole=new Button(pannel,SWT.PUSH);
		swtichRole.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, true, 2, 1, 0, 0));
		swtichRole.setToolTipText(Constants.getStringVaule("text_tips_switchuser"));
		swtichRole.setImage(Icons.getAppIcon());
		swtichRole.addSelectionListener(new ShowRolesAction());
		
		Label labPasswd=new Label(pannel,SWT.NONE);
		labPasswd.setText(Constants.getStringVaule("label_passwd"));
		labPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, true, 3, 1, 0, 0));
		textPasswd= new Text(pannel,SWT.BORDER|SWT.PASSWORD);
		textPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 5, 1, 0, 0));
		
	    remeberPasswd=new Button(pannel,SWT.CHECK);
		remeberPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, true, 2, 1, 0, 0));
		remeberPasswd.setToolTipText(Constants.getStringVaule("text_tips_remeber"));
		remeberPasswd.setText(Constants.getStringVaule("btn_remeberpasswd"));
		remeberPasswd.addSelectionListener(new RemeberPasswdAction());
		if(!StringUtil.isNullOrEmpty(Context.UserPasswd)){
			String passwd=SecurityCenter.getInstance().decrypt(Context.UserPasswd, Context.EncryptKey);
			if(Context.RemeberPasswd){
				remeberPasswd.setSelection(true);
				textPasswd.setText(passwd);
			}
		}
		if(!StringUtil.isNullOrEmpty(Context.session.userID)){
			if(Context.RemeberPasswd){
				remeberPasswd.setSelection(true);
				textName.setText(Context.session.userID);
			}
		}
		
		Link link = new Link(pannel, SWT.NONE); 
		link.setText("<A>"+Constants.getStringVaule("link_forgetpasswd")+"</A>");  
		link.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 10, 1, 0, 0));
		
		 Button btnApply=new Button(pannel,SWT.PUSH);
		 btnApply.setText("   "+Constants.getStringVaule("btn_login")+"   ");
		 btnApply.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 5, 1, 0, 0));
		 btnApply.addSelectionListener(new LoginAction());
		 
		 Button btnCancle=new Button(pannel,SWT.PUSH);
		 btnCancle.setText("   "+Constants.getStringVaule("btn_cancel")+"   ");
		 btnCancle.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 5, 1, 0, 0));
		 btnCancle.addSelectionListener(new WindowCloseAction());
		pannel.pack();
		Login.pack();
		Login.open();
		Login.addShellListener(new ShellCloseAction());
	}

	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			Login.dispose();
			AppView.getInstance().getShell().dispose();
		}	
	}
	
	
	public class RemeberPasswdAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			if(remeberPasswd.getSelection()){//记住密码
				LocalDataHelper.updateParameters("Remember_Flag","1");
			}else{
				LocalDataHelper.updateParameters("Remember_Flag","0");
			}
		}	
	}
	
	public class ShowRolesAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
				String username=textName.getText();
			     UserLogin login=new UserLogin(username);
			     List<RoleBean> roles=login.getUserRoles();
			     if(roles!=null&&roles.size()>0){
			    	 Menu actionMenu=new Menu(Login,SWT.POP_UP);
			    	 for(RoleBean bean:roles){
				    	 MenuItem itemRole=new MenuItem(actionMenu,SWT.RADIO);
				    	 itemRole.setText(bean.getName());
				    	 itemRole.setData(bean);
				    	 itemRole.setData("UserLogin", login);
				    	 if(bean.getId().equals(Context.session.roleID)){
				    		 itemRole.setSelection(true);
				    	 }
				    	 itemRole.addSelectionListener(new SelectionAdapter(){ 	
		 	        		 public void widgetSelected(SelectionEvent e){
		 	        			 MenuItem item=(MenuItem) e.getSource();
		 	        			 item.setSelection(true);
		 	        			 RoleBean data=(RoleBean)item.getData();
		 	        			//更新用户默认角色
		 	        			UserLogin login=(UserLogin)item.getData("UserLogin");
		 	        			Context.session.roleID=data.getId();
		 	        			login.setDefaultRole(data.getId());
		 	        		 }});
			    	 }
			    	 actionMenu.setVisible(true);
			    	 swtichRole.setMenu(actionMenu);
			     }else{
			    	 swtichRole.setMenu(null);
			     }
		}
	}
	
	public class LoginAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String username=textName.getText();
			String userpasswd=textPasswd.getText();
			String passwd=SecurityCenter.getInstance().encrypt(userpasswd, Context.EncryptKey);
			
			UserLogin login=new UserLogin(username);
			String result=login.login();
			if(StringUtil.isNullOrEmpty(result)){
				//判断密码
				String passwdRmt=login.getPasswd();
				//密码为空，判定为首次登录，需要修改密码
				if(StringUtil.isNullOrEmpty(passwdRmt)){
					LoginPasswdView Passwd=new LoginPasswdView(Login,login);
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
						MessageBox box=new MessageBox(Login,SWT.ICON_WARNING|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
					}else{
						login.loginSuccessed(username);
						login.refreshRole();
						if(remeberPasswd.getSelection()){//记住密码
							LocalDataHelper.updateParameters("User_Passwd",passwd);
							LocalDataHelper.updateParameters("User_Id",username);
						}else{
							LocalDataHelper.updateParameters("User_Passwd","");
							LocalDataHelper.updateParameters("User_Id","");
						}
						AppView.getInstance().createFrameView();
						loginResult=true;
						Login.dispose();
					}
				}
				
			}else{
				MessageBox box=new MessageBox(Login,SWT.ICON_WARNING|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(result);
				box.open();
				return;
			}
		}
	}
	
	public class WindowCloseAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Login.dispose();
			AppView.getInstance().getShell().dispose();
		}
	}
	private Shell Login;
	private Text textName,textPasswd;
	
	public boolean isLoginResult() {
		return loginResult;
	}
	
}
