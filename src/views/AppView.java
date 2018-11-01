package views;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.PKGSYSTEM;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import common.core.LnxShell;
import common.core.ScheduleService;
import common.core.ViewRefreshService;
import common.core.WinShell;
import common.db.DBConnectionManager;

import bean.PKGSYSTEMBean;
import business.deploy.core.PkgNodeScheduler;
import business.deploy.core.PkgSystemScheduler;
import business.deploy.core.ViewRefresher;
import business.dversion.core.DVFileLoader;
import business.tester.core.TFileLoader;
import business.tversion.core.FileLoader;

import resource.AppInit;
import resource.Constants;
import resource.Context;
import resource.Icons;
import resource.Logger;
import resource.MessagePullerThread;
import resource.Paths;
import resource.UserLogin;
import utils.LayoutUtils;
import utils.StringUtil;

public class AppView {

	
	public static AppView getInstance(){
		if(unique_instance==null){
			unique_instance=new AppView();
		}
		return unique_instance;
	}

	private AppView(){
		display=new Display();
		shell=new Shell(display,SWT.SHELL_TRIM);
	}
	
	public void createAndShowView(){
		if(this.init()){
			//����Ƿ��Ѿ��н�����ʵ��
			if(this.isInstanceActive(Context.appID)){
				String msg="Ӧ���������������ϵͳ����ͼ�������";
				MessageBox box=new MessageBox(shell,SWT.ICON_INFORMATION|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(msg);
				box.open();
			}else{
				createMainView();
			}
		}
	}
	
	
	private boolean isInstanceActive(String appID){
		try{
			 int count=0;
			 Process process = Runtime.getRuntime().exec("taskList");
	          Scanner in = new Scanner(process.getInputStream());
	            while (in.hasNextLine()) {
	            	  String temp = in.nextLine();
	                  if (temp.contains(appID)) {
	                	  Logger.getInstance().log("�ж�"+appID+"ʵ���Ƿ�����:"+temp);
	                	  String[] t = temp.split(" ");
	                	  for(String column:t){
	                		  if(appID.equalsIgnoreCase(column)){
	                			  count++;
	                		  }
	                	  }
	                  }
	            }
                  if(count>1){
                	  return true;
                  }
		}catch (Exception e) {
			Logger.getInstance().error("�ж�"+appID+"ʵ���Ƿ����������쳣:"+e.toString());
		}
		return false;
	}
	
	
	
	public Shell getShell(){
		return shell;
	}
	
	public Display getDisplay(){
		return display;
	}
	
	/*ȡ��Ӧ����������Ļ���м�λ��*/
	public Point getCentreScreenPoint(){
		Point start=this.shell.getLocation();
		int width=this.shell.getBounds().width;
		int height=this.shell.getBounds().height;
		Point point=new Point(start.x+width/2,start.y+height/2);
		return point;
	}
	
	private void createMainView(){
		shell.setImage(Icons.getAppIcon());
		boolean needLogin=true;
		//��չ�ֵ�½����
		this.createTrayMenu();
        if(needLogin){
        	LoginView.getInstance().show();
        }else{
        	this.createFrameView();
        }
		////////////////////////////////////////
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())
				display.sleep();
			}
			display.dispose();
	}
	
	public void createFrameView(){
		shell.setText(Context.appName);
		
		mainComposite=	new Composite(shell,SWT.NONE);
		mainComposite.setLayoutData(LayoutUtils.getMainLayoutData());
		mainComposite.setLayout(LayoutUtils.getShellLayout());
		
		  //�˵���
		   menu=MenuViewFactory.getMenuView(shell,mainComposite,SWT.NONE,Context.session.roleID);
		   menu.createMenuView();
          //////////////////////////////////////
		   //������
		   toolComposite=ToolBarViewFacotry.getToolBarView(mainComposite,SWT.NONE,Context.session.roleID);
		   toolComposite.createToolBar();
		  //������
		   workComposite=WorkAreaView.getInstance(mainComposite);
		   workComposite.setLayout(LayoutUtils.getWorkAreaLayout());
		   workComposite.setLayoutData(LayoutUtils.getWorkAreaLayoutData());
		   workComposite.pack();

		   //״̬�� //////////////////////////
		  // statusBarView=StatusBarView.getInstance(mainComposite);
		   mainComposite.pack();
		shell.setLayout(new GridLayout());
		shell.pack();
		shell.open();
		shell.addShellListener(new ShellCloseAction());
		//////��������ͨ�ŷ���,���̻߳�һֱ����ֱ��app�˳�///////
		if(Context.session.roleID.equals(Constants.RoleType.Deploy.ordinal()+"")){
			//û�е�����ɵķ������жϣ��Զ�����
			ViewRefresher.getInstance().setRunable(true);
			ViewRefresher.getInstance().start();
		}
		if(Context.session.roleID.equals(Constants.RoleType.TesterVersion.ordinal()+"")){
			//FileLoader.getFileLoader().start();
			if(Context.alertEnable){
				MessagePullerThread.getInstance().setRunFlag(true);
				MessagePullerThread.getInstance().start();
			}
			ViewRefreshService.getIntance().setRunable(true);
			ViewRefreshService.getIntance().start();
		}
		if(Context.session.roleID.equals(Constants.RoleType.DeveloperVersion.ordinal()+"")){
			if(Context.alertEnable){
				MessagePullerThread.getInstance().setRunFlag(true);
				MessagePullerThread.getInstance().start();
			}
		}
		if(Context.session.roleID.equals(Constants.RoleType.Tester.ordinal()+"")){
			//TFileLoader.getFileLoader().start();
			if(Context.alertEnable){
				MessagePullerThread.getInstance().setRunFlag(true);
				MessagePullerThread.getInstance().start();
			}
			ViewRefreshService.getIntance().setRunable(true);
			ViewRefreshService.getIntance().start();
		}
		if(Context.session.roleID.equals(Constants.RoleType.Developer.ordinal()+"")){
			if(Context.alertEnable){
				MessagePullerThread.getInstance().setRunFlag(true);
				MessagePullerThread.getInstance().start();
			}
		}
		if(Context.session.roleID.equals(Constants.RoleType.DeveloperManager.ordinal()+"")){
			if(Context.alertEnable){
				MessagePullerThread.getInstance().setRunFlag(true);
				MessagePullerThread.getInstance().start();
			}
		}
	}
	
	private void createTrayMenu(){
		 //����ϵͳ���ؼ�
		tray = display.getSystemTray();
	    trayItem = new TrayItem(tray, SWT.NONE);
	    trayItem.setVisible(false);
        trayItem.setToolTipText(shell.getText());
        trayItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                toggleDisplay(shell, tray);
            }
        });
        final Menu trayMenu = new Menu(shell, SWT.POP_UP);
        MenuItem showMenuItem = new MenuItem(trayMenu, SWT.PUSH);
        showMenuItem.setText("��ʾ����(&s)");
        //��ʾ���ڣ�������ϵͳ���е�ͼ��
        showMenuItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                toggleDisplay(shell, tray);
            }
        });
        trayMenu.setDefaultItem(showMenuItem);
        new MenuItem(trayMenu, SWT.SEPARATOR);
        //ϵͳ���е��˳��˵�������ֻ��ͨ������˵��˳�
        MenuItem exitMenuItem = new MenuItem(trayMenu, SWT.PUSH);
        exitMenuItem.setText("�˳�����(&x)");
        exitMenuItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
            	AppView.getInstance().exit();
            }
        });
        //��ϵͳ��ͼ��������Ҽ�ʱ���¼�������ϵͳ���˵�
        trayItem.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent arg0) {
				 trayMenu.setVisible(true);
			}
		});
        trayItem.setImage(shell.getImage());
	}
	

	public void exit(){
		UserLogin login=new UserLogin();
    	login.loginOut(Context.session.userID);
    	//������Ҫ���̵�Ӧ�õ��˳������ﴦ��
    	if(Context.session.roleID.equals(Constants.RoleType.Developer.ordinal()+"")){
			MessagePullerThread.getInstance().exit();
		}
    	if(Context.session.roleID.equals(Constants.RoleType.DeveloperVersion.ordinal()+"")){
    		MessagePullerThread.getInstance().exit();
		}
    	if(Context.session.roleID.equals(Constants.RoleType.TesterVersion.ordinal()+"")){
    		MessagePullerThread.getInstance().exit();
    		ViewRefreshService.getIntance().exit();
    		ScheduleService.getInstance().exit();
    		LnxShell.release();
    		WinShell.release();
		}
    	if(Context.session.roleID.equals(Constants.RoleType.Tester.ordinal()+"")){
    		MessagePullerThread.getInstance().exit();
    		ScheduleService.getInstance().exit();
    		ViewRefreshService.getIntance().exit();
    		LnxShell.release();
    		WinShell.release();
    		
		}
    	if(Context.session.roleID.equals(Constants.RoleType.Developer.ordinal()+"")){
    		MessagePullerThread.getInstance().exit();
    		
		}
    	if(Context.session.roleID.equals(Constants.RoleType.DeveloperManager.ordinal()+"")){
    		MessagePullerThread.getInstance().exit();
		}
    	DBConnectionManager.getInstance().release();//�ͷ����ӳ�
        shell.dispose();
	}
	
	
	 private boolean init(){
		 if(!this.jumpFlag){
			  String rowPath= getStartPath();
			  Paths.getInstance().init(rowPath);
			  boolean startRet= AppInit.getStartInstance().startUp();
			  if(!startRet){
				  Logger.startInfo("���������쳣�������־��");
			  }
			  else{
				  boolean checkRet=AppInit.getStartInstance().checkLicence();
				  if(!checkRet){
					  this.alertStartMsg();
				  }
				  else{
					  return true;
				  }
			  }
			  return false;
		 }else {
			 return true;
		 }
	   }
	 
	

	 public void alertStartMsg(){
			LicenseView pop=LicenseView.getView();
			int x=AppView.getInstance().getCentreScreenPoint().x;
			int y=AppView.getInstance().getCentreScreenPoint().y;
			pop.setPosition(new Point(x,y));
			pop.show();
	 }
	 private String getStartPath(){
			String path="";
			try {
				path= System.getProperty("user.dir");//���ֲ�֧������·����
				String pathClass= URLDecoder.decode(this.getClass().getClassLoader().getResource("views/AppView.class").getPath(),"UTF-8");
				Logger.startInfo("AppView.getStartPath()[System.getProperty(\"user.dir\")]"+path);
				Logger.startInfo("AppView.getStartPath()[getClassLoader]"+pathClass);
			} catch (Exception e) {
				Logger.startInfo("AppView.getStartPath()"+e.toString());
				e.printStackTrace();
			}
			return  path;
		}
		
		public class ShellCloseAction extends ShellAdapter{
			public void shellClosed(ShellEvent e){	
				if(Context.session.roleID.equals(Constants.RoleType.Deploy.ordinal()+"")){
					//���ڰ�װ�İ汾��Ҫ��ֹ
					List<PKGSYSTEMBean> pkgSys=PKGSYSTEM.getMyPkgSystems(Context.CurrentVersionID, Context.session.userID);
					if(pkgSys!=null&&pkgSys.size()>0){
						for(PKGSYSTEMBean onePkgSys:pkgSys){
							if((PKGSYSTEMBean.Status.Busy.ordinal()+"").equals(onePkgSys.getStatus())){
								onePkgSys.setStatus(PKGSYSTEMBean.Status.Halt.ordinal()+"");
							}
						}
					}
					interrupt=true;
					UserLogin login=new UserLogin();
	            	login.loginOut(Context.session.userID);
	            	ViewRefresher.getInstance().exit();//����߳��˳�
					PkgNodeScheduler.getInstance().exit();//���ȹ����˳�
					PkgSystemScheduler.getInstance().exit();////���ȹ����˳�
					DBConnectionManager.getInstance().release();//�ͷ����ӳ�
					LnxShell.release();
					shell.dispose();
					display.dispose();
					WinShell.release();//������Դ�ͷ�
				}
				else{
					//������ڹرհ�ťʱ��������ֹ���򣬶�ʱ���ش��ڣ�ͬʱϵͳ����ʾͼ��
					 e.doit = false; //���ĵ�ԭ��ϵͳ��������¼�
		             toggleDisplay(shell, tray);
				}
				//�û��ǳ�
			}
			 public void shellIconified(ShellEvent e) {
					 if(!Context.session.roleID.equals(Constants.RoleType.Deploy.ordinal()+"")){
		                toggleDisplay(shell, tray);
					 }
	            }
		}
		
		private static void toggleDisplay(Shell shell, Tray tray) {
	        try {
	            shell.setVisible(!shell.isVisible());
	            tray.getItem(0).setVisible(!shell.isVisible());
	            if (shell.getVisible()) {
	                shell.setMinimized(false);
	                shell.setActive();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		
		public boolean isInterrupt(){
			return this.interrupt;
		}
		
		public void setJumpOk(){
			this.jumpFlag=true;
		}
		public Composite getTopComposite(){
			return this.mainComposite;
		}
		private boolean interrupt=false;
		private static AppView unique_instance=null;
		private Display display=null;
		private  Shell shell=null;
		private Composite mainComposite=null;
		private MenuView  menu=null;
		private ToolBarView toolComposite=null;
		private StatusBarView statusBarView=null;
		private WorkAreaView workComposite=null;
		private boolean jumpFlag=false;
		//����ϵͳ���ؼ�
        public Tray tray;
        public TrayItem trayItem;
}
