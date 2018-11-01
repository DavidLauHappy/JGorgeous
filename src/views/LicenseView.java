package views;

import java.util.Properties;


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

import common.db.DataHelper;
import common.localdb.LocalDataHelper;




import resource.Constants;
import resource.Logger;
import resource.SecurityCenter;
import utils.DateUtil;
import utils.LayoutUtils;
import utils.NetworkUtil;

public class LicenseView {
	private  Shell License=null;
	private Point position=null;
	private  Text inputArea=null;
	private static LicenseView unique_instance;
	public static LicenseView getView(){
		if(unique_instance==null){
			unique_instance=new LicenseView();
		}
		return unique_instance;
	}
	
	private LicenseView(){}
	public void setPosition(Point position){
		this.position=position;
	}
	
	public void show(){
		License=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		License.setText(Constants.getStringVaule("window_valid"));
		License.setLocation(this.position);
		License.setLayout(LayoutUtils.getComGridLayout(1, 0));

		Composite pannel=new Composite(License,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 160));
		pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		
		Label msgLabel=new Label(pannel,SWT.NONE);
		String msg="产品序列号已失效，请激活或重新认证";
		msgLabel.setText(msg);
		msgLabel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 6, 1, 0, 0));
		
		Label labUploadDir=new Label(pannel,SWT.NONE);
		labUploadDir.setText(Constants.getStringVaule("label_sn"));
		labUploadDir.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 2, 1, 0, 0));
		inputArea=new Text(pannel,SWT.BORDER);
		inputArea.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));

		Button btnApply=new Button(pannel,SWT.PUSH);
		btnApply.setText("   "+Constants.getStringVaule("btn_active")+"   ");
		btnApply.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 3, 1, 0, 0));
		btnApply.addSelectionListener(new AppActivateAction());
		
		Button btnCancle=new Button(pannel,SWT.PUSH);
		btnCancle.setText("   "+Constants.getStringVaule("btn_cancel")+"   ");
		btnCancle.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 3, 1, 0, 0));
		btnCancle.addSelectionListener(new WindowCloseAction());
		
		pannel.pack();
		License.pack();
		License.open();
		License.addShellListener(new ShellCloseAction());
		while(!License.isDisposed()){
			if(!AppView.getInstance().getDisplay().readAndDispatch())
				AppView.getInstance().getDisplay().sleep();
		}
		AppView.getInstance().getDisplay().dispose();
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			License.dispose();
		}
	}
	
	public class WindowCloseAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			License.dispose();
			//AppView.getInstance().getDisplay().dispose();
		}
	}
	
	public class AppActivateAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String inputKey=inputArea.getText().trim();
			String key=DateUtil.getCurrentDate("yyyyMMdd").hashCode()+"";
			String escape=SecurityCenter.getInstance().decrypt(inputKey, key);
			String standard="Gorgeous"+DateUtil.getCurrentDate("yyyyMMdd");
			if(escape==null||!escape.equals(standard)){
				String msg="无效产品序列号，请联系开发者获取合法产品序列号";
				MessageBox box=new MessageBox(License,SWT.ICON_WARNING|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(msg);
				box.open();
			}else{
				License.dispose();
				//记录过期日期
				String licenseID=NetworkUtil.getLocalIp()+"@"+getSystemUser();
				LocalDataHelper.updateParameters("LICENSE", licenseID);
				String expired=DateUtil.getExpiredDate();
				LocalDataHelper.updateParameters("Expired_Date", expired);
				AppView.getInstance().setJumpOk();
				AppView.getInstance().createAndShowView();
			}
		}
	}
	private String getSystemUser(){
		String localUser="";
		try{
			Properties prop = System.getProperties();
			localUser=prop.getProperty("user.name");
		}catch(Exception e){
			Logger.getInstance().error("LicenseView.getSystemUser()获取本地操作系统域用户发生异常:"+e.toString());
		}
		return localUser;
	}
}
