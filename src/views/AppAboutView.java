package views;

import java.io.File;

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

import resource.Constants;
import resource.Context;
import resource.Icons;
import resource.Logger;
import resource.Paths;
import utils.FileUtils;
import utils.LayoutUtils;

public class AppAboutView {
	public static AppAboutView getInstance(){
		if(unique_instance==null){
			unique_instance=new AppAboutView();
		}
		return unique_instance;
	}
	
	public void show(){
		About=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		About.setText("关于"+Context.appName);
		Point point = AppView.getInstance().getCentreScreenPoint();
		About.setLocation(point);
		About.setLayout(LayoutUtils.getComGridLayout(1, 0));
		Composite pannel=new Composite(About,SWT.BORDER);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 200));
		pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		Button image=new Button(pannel,SWT.IMAGE_ICO);
		image.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
		image.setImage(Icons.getAppIcon());
		Label labName=new Label(pannel,SWT.NONE);
		labName.setText(Context.appID);
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		Label labNameCopy=new Label(pannel,SWT.NONE);
		labNameCopy.setText(Constants.getStringVaule("label_namecopyright"));
		labNameCopy.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		
		Label labValueCopy=new Label(pannel,SWT.NONE);
		labValueCopy.setText(Constants.getStringVaule("label_valuecopyright"));
		labValueCopy.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		Label labNameVersion=new Label(pannel,SWT.NONE);
		labNameVersion.setText(Constants.getStringVaule("label_nameversion"));
		labNameVersion.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		
		Label labValueVersion=new Label(pannel,SWT.NONE);
		labValueVersion.setText(Context.CurrentVersion);
		labValueVersion.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		Label labNewVersion=new Label(pannel,SWT.NONE);
		labNewVersion.setText(Constants.getStringVaule("label_newversion"));
		labNewVersion.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		Label labNewValueVersion=new Label(pannel,SWT.NONE);
		labNewValueVersion.setText(Context.NewVersion);
		labNewValueVersion.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		Label labNameScope=new Label(pannel,SWT.NONE);
		labNameScope.setText(Constants.getStringVaule("label_versiondesc"));
		labNameScope.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		
		Label labValueScope=new Label(pannel,SWT.NONE);
		labValueScope.setText(Context.NewVersionDesc);
		labValueScope.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		Button btnUpdate=new Button(pannel,SWT.PUSH);
		btnUpdate.setImage(Icons.getRefreshIcon());
		btnUpdate.setToolTipText(Constants.getStringVaule("btn_tips_installversion"));
		btnUpdate.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, false, false, 6, 1, 0, 0));
		btnUpdate.addSelectionListener(new UpdateAction());
		
		String curVersion=Context.CurrentVersion;
		curVersion=curVersion.toLowerCase();
		curVersion=curVersion.replace("v", "");
		curVersion=curVersion.replace(".", "");
		int intCurVersion=Integer.parseInt(curVersion);
		
		String newVersion=Context.NewVersion;
		newVersion=newVersion.toLowerCase();
		newVersion=newVersion.replace("v", "");
		newVersion=newVersion.replace(".", "");
		int intNewVersion=Integer.parseInt(newVersion);
		if(intNewVersion>intCurVersion){
			btnUpdate.setVisible(true);
		}else{
			btnUpdate.setVisible(false);
		}
		pannel.pack();
		
		About.pack();
		About.open();
		About.addShellListener(new ShellCloseAction());
}

	  public class UpdateAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			try{
		   			//调用AutoUpdate.exe程序
		   			String path=Paths.getInstance().getBasePath()+File.separator+"AutoUpdate.exe";
		   			path=FileUtils.formatPath(path);
		   			File updater=new File(path);
		   			if(updater.exists()){
				   			String[] cmd=new String[]{path,Context.VersionServerIp,Context.VersionPort,Context.appID,Context.NewVersion};
				   			Logger.getInstance().log("开始调用自动检测安装更新程序："+cmd.toString());
				   			Runtime.getRuntime().exec(cmd);
				   			//本身退出主程序
				   			Logger.getInstance().log("主程序准备自动退出");
				   			AppView.getInstance().exit();
			   			  }
			   			else{
			   			   String msg="更新程序："+path+"未下载，请从https://pan.guosen.com.cn/l/iJFKXs中获取";
			   			   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
						   box.setText(Constants.getStringVaule("messagebox_alert"));
						   box.setMessage(msg);
						   box.open();
			   			}
	   			}
	   			catch(Exception exp){
	   				Logger.getInstance().error("自动检测安装更新程序启动发生未知异常："+exp.toString());
	   			}
	   		}
	  }
	
	
public class ShellCloseAction extends ShellAdapter{
	public void shellClosed(ShellEvent e){	
		About.dispose();
	}	
}
	private AppAboutView(){}
	private static AppAboutView unique_instance;
	private  Shell About=null;
}
