package resource;

import java.util.UUID;

import model.LOCALNODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import bean.LOCALNODEBean;


import utils.LayoutUtils;
import utils.SqlServer;
import utils.StringUtil;
import views.AppView;

public class DbNodeView {
	private  Shell shell=null;
	private Point position=null;
	
	public DbNodeView(Point position){
		this.position=position;
	}
	
	 private Text textDbName,textBackDbName,textDbuser,textPasswd,textPasswdDouble,textPort=null;
	 private Combo comboDbType=null;
	 private Text textIp,textName=null;
	 private Combo comboOS=null;
	public  void show(){
		
		shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.RESIZE|SWT.SYSTEM_MODAL);
		shell.setText(Constants.getStringVaule("window_dbnode"));
		shell.setLocation(this.position);
		shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
		Composite dbPannel=new Composite(shell,SWT.NONE);
		dbPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 500, 350));
		dbPannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		
		Label labIp=new Label(dbPannel,SWT.NONE);
		labIp.setText(Constants.getStringVaule("label_ip")+"(*)");
		labIp.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		textIp=new Text(dbPannel,SWT.BORDER);
		textIp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		textIp.setToolTipText(Constants.getStringVaule("text_tips_ip"));	
		//节点标识名称
		Label labName=new Label(dbPannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_node")+"(*)");
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
	    textName=new Text(dbPannel,SWT.BORDER);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		textName.setToolTipText(Constants.getStringVaule("text_tips_nodename"));	
		//操作系统
		Label labOS=new Label(dbPannel,SWT.NONE);
		labOS.setText(Constants.getStringVaule("label_os")+"(*)");
		labOS.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		comboOS=new Combo(dbPannel,SWT.DROP_DOWN);
		comboOS.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		comboOS.setItems(new String[]{"Windows","Linux"});
		comboOS.select(0);
		comboOS.setToolTipText(Constants.getStringVaule("alert_agentIntall"));
		
		   Label labDbtype=new Label(dbPannel,SWT.NONE);
		 	labDbtype.setText(Constants.getStringVaule("label_dbtype")+"(*)");
		 	labDbtype.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
			comboDbType=new Combo(dbPannel,SWT.DROP_DOWN);
			comboDbType.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			comboDbType.setItems(new String[]{"SqlServer","Oracle"});
			comboDbType.select(0);
			
		    Label labDbuser=new Label(dbPannel,SWT.NONE);
			labDbuser.setText(Constants.getStringVaule("label_dbuser")+"(*)");
			labDbuser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
			textDbuser=new Text(dbPannel,SWT.BORDER);
			textDbuser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textDbuser.setToolTipText(Constants.getStringVaule("text_tips_dbuser"));
			
			
			Label labDbpasswd=new Label(dbPannel,SWT.NONE);
			labDbpasswd.setText(Constants.getStringVaule("label_dbpasswd")+"(*)");
			labDbpasswd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
			textPasswd=new Text(dbPannel,SWT.BORDER|SWT.PASSWORD);
			textPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1, 0, 0));
			textPasswd.setToolTipText(Constants.getStringVaule("text_tips_dbpasswd"));
		    Button btnShow=new Button(dbPannel,SWT.CHECK);
		    btnShow.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		    btnShow.setText(Constants.getStringVaule("btn_showpasswd"));
		    btnShow.addSelectionListener(new ShowDbPasswodAction());
		    
		    Label labDouble=new Label(dbPannel,SWT.NONE);
			labDouble.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
			labDouble.setVisible(false);
			textPasswdDouble=new Text(dbPannel,SWT.BORDER);
			textPasswdDouble.setToolTipText(Constants.getStringVaule("text_tips_dbpasswd"));
			textPasswdDouble.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
			textPasswdDouble.setVisible(false);
			
		    Label labDbName=new Label(dbPannel,SWT.NONE);
			labDbName.setText(Constants.getStringVaule("label_dbname")+"(*)");
			labDbName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			textDbName=new Text(dbPannel,SWT.BORDER);
			textDbName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textDbName.setToolTipText(Constants.getStringVaule("text_tips_dbname"));
			
			 Label labBackDbName=new Label(dbPannel,SWT.NONE);
			 labBackDbName.setText(Constants.getStringVaule("label_backdbname"));
			 labBackDbName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			
			textBackDbName=new Text(dbPannel,SWT.BORDER);
			textBackDbName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textBackDbName.setToolTipText(Constants.getStringVaule("text_tips_backdbname"));
			
			Label labDbPort=new Label(dbPannel,SWT.NONE);
			labDbPort.setText(Constants.getStringVaule("label_dbport")+"(*)");
			labDbPort.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			textPort=new Text(dbPannel,SWT.BORDER);
			textPort.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textPort.setToolTipText(Constants.getStringVaule("text_tips_dbport"));
			textPort.setText("1433");
		
			Button btnCancel=new Button(dbPannel,SWT.PUSH);
			btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
			btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
			btnCancel.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					textIp.setText("");
					textName.setText("");
					comboOS.select(0);
					textDbuser.setText("");
					textPasswd.setText("");
					textDbName.setText("");
					textBackDbName.setText("");
					textPort.setText("");
					comboDbType.setText("");
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}
			});
			Button btnOK=new Button(dbPannel,SWT.PUSH);
			btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
			btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
			btnOK.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					 String ip=textIp.getText();
					 String name=textName.getText();
					 String dbType=comboDbType.getSelectionIndex()+"";
					 String user=textDbuser.getText();
					 String passwd=textPasswd.getText();
					 String dbname=textDbName.getText();
					 String  backdbname=textBackDbName.getText();
					 String dbport=textPort.getText();
					  if(StringUtil.isNullOrEmpty(ip)||
								StringUtil.isNullOrEmpty(name,30)||
								!StringUtil.isIp(ip)||
								StringUtil.isNullOrEmpty(user,8)||
								 StringUtil.isNullOrEmpty(passwd,32)||
								 StringUtil.isNullOrEmpty(dbname,32)||
								 StringUtil.isNullOrEmpty(backdbname,32)||
								 !StringUtil.isPort(dbport)){
									MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
									box.open();	
					  }else{
						  passwd=SecurityCenter.getInstance().encrypt(passwd, Context.EncryptKey);
						  boolean testResult=SqlServer.getInstance().testConnection(dbType, ip, dbport, user, passwd, dbname);
						  if(testResult){
							  LOCALNODEBean bean=new LOCALNODEBean();
							  bean.setUserID(Context.session.userID);
							  String id="Node"+UUID.randomUUID().toString().replace("-", ""); 
							  bean.setIp(ip);
						      bean.setId(id);
						      bean.setName(name);
						     String os=comboOS.getSelectionIndex()+"";
						      bean.setOs(os);
						      String app=Context.Apps;
						      if(!StringUtil.isNullOrEmpty(app)){
						    	  if(app.indexOf("|")!=-1){
						    	  		app=app.substring(0,app.indexOf("|"));
						    	  }
						      }
						      bean.setApp(app);
							  bean.setDbUser(user);
							  bean.setDbPasswd(passwd);
							  bean.setDbName(dbname);
							  bean.setDbPort(dbport);
							  bean.setDbType(dbType);
							  bean.setBackdbname(backdbname);
							  if(LOCALNODE.add(bean)>0){
								  LOCALNODE.setDb(bean);
							  }
							  shell.dispose(); 
						  }else{
							    MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage("测试链接失败，请确认数据库属性配置正确！");
								box.open();	
						  }
					  }
				}
				public void widgetDefaultSelected(SelectionEvent arg0) {	
					
				}
			});
			
			dbPannel.pack();
		 shell.pack();
		 shell.open();
		 shell.addShellListener(new ShellCloseAction());
 	}
 	
 	public class ShellCloseAction extends ShellAdapter{
 		public void shellClosed(ShellEvent e){	
 			shell.dispose();
 		}	
 	}
 	
 	 public class ShowDbPasswodAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				Button btn=(Button)e.getSource();
				boolean checked=btn.getSelection();
				if(checked){
					String text=textPasswd.getText();
					if(!StringUtil.isNullOrEmpty(text)){
						textPasswdDouble.setText(text);
						textPasswdDouble.setVisible(true);
					}
				}else{
					textPasswdDouble.setVisible(false);
				}
			}
	  }
}
