package business.deploy.figures;

import java.io.File;
import java.util.Map;

import model.PKG;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Logger;
import resource.Paths;
import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;
import views.ConfigMngView;
import views.SettingView;
import views.ToolBarView;
import bean.PKGBean;
import business.deploy.core.PKGOperator;


/**
 * @author DavidLau
 * @date 2016-8-11
 * @version 1.0
 * 类说明
 */
public class DeployToolBarView extends ToolBarView {

	public Button btnImport=null;
	public DeployToolBarView(Composite main,int style){   
		mainComposite=new Composite(main,SWT.NONE);
		mainComposite.setLayout(LayoutUtils.getToolBarLayout());
		mainComposite.setLayoutData(LayoutUtils.getToolsLayoutData());
	}
	private Composite mainComposite=null;
	public void createToolBar(){
		btnImport=new Button(mainComposite,SWT.PUSH);
		btnImport.setToolTipText(Constants.getStringVaule("btn_tips_importpkg"));
		btnImport.setImage(Icons.getImportIcon());
		btnImport.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnImport.addSelectionListener(new ImportPkgAction());
		
		Button btnAction= new Button(mainComposite,SWT.PUSH);
		btnAction.setToolTipText(Constants.getStringVaule("btn_tips_versionation"));
		btnAction.setImage(Icons.getActionIcon());
		btnAction.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnAction.addSelectionListener(new IntallVersionAction());
		
		Button btnTag=new Button(mainComposite,SWT.NONE);
		btnTag.setToolTipText(Constants.getStringVaule("btn_tips_groupmanager"));
		btnTag.setImage(Icons.getTagIcon());
		btnTag.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnTag.addSelectionListener(new TagManageAction());
		
		Button btnCompare=new Button(mainComposite,SWT.NONE);
		btnCompare.setToolTipText(Constants.getStringVaule("btn_tips_compare"));
		btnCompare.setImage(Icons.getCompareIcon());
		btnCompare.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnCompare.addSelectionListener(new ShowCompareAction());
		
		Button btnReport=new Button(mainComposite,SWT.NONE);
		btnReport.setToolTipText(Constants.getStringVaule("btn_tips_report"));
		btnReport.setImage(Icons.getReportIcon());
		btnReport.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnReport.addSelectionListener(new ReportViewAction());
		
		Button btnSetting=new Button(mainComposite,SWT.NONE);
		btnSetting.setToolTipText(Constants.getStringVaule("btn_tips_setting"));
		btnSetting.setImage(Icons.getSettingIcon());
		btnSetting.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnSetting.addSelectionListener(new ShowSettingAction());
		
		Button btnPasswod=new Button(mainComposite,SWT.PUSH);
		btnPasswod.setToolTipText(Constants.getStringVaule("btn_tips_passwd"));
		btnPasswod.setImage(Icons.getPasswdIcon());
		btnPasswod.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnPasswod.addSelectionListener(new SetPasswdAction());
		
		/*Button btnApprove=new Button(mainComposite,SWT.PUSH);
		btnApprove.setToolTipText(Constants.getStringVaule("btn_tips_approve"));
		btnApprove.setImage(Icons.getApproveIcon());
		btnApprove.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnApprove.addSelectionListener(new MyApproveAction());*/
		
		Button btnToDoList=new Button(mainComposite,SWT.PUSH);
		btnToDoList.setToolTipText(Constants.getStringVaule("btn_tips_todoList"));
		btnToDoList.setImage(Icons.getTaskIcon());
		btnToDoList.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnToDoList.addSelectionListener(new MyTaskAction());
		
		Button btnConfig=new Button(mainComposite,SWT.PUSH);
		btnConfig.setToolTipText(Constants.getStringVaule("btn_tips_cfgmng"));
		btnConfig.setImage(Icons.getCfgIcon());
		btnConfig.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnConfig.addSelectionListener(new ConfigMngAction());
		
		/*Button btnFTP=new Button(mainComposite,SWT.NONE);
		btnFTP.setToolTipText(Constants.getStringVaule("btn_tips_ftp"));
		btnFTP.setImage(Icons.getFtpIcon());
		btnFTP.setLayoutData(LayoutUtils.getToolsLayoutData());
		
		Button btnSamba=new Button(mainComposite,SWT.NONE);
		btnSamba.setToolTipText(Constants.getStringVaule("btn_tips_share"));
		btnSamba.setImage(Icons.getSambaIcon());
		btnSamba.setLayoutData(LayoutUtils.getToolsLayoutData());*/
		
		
		Button btnUser=new Button(mainComposite,SWT.NONE);
		String tips= Context.session.userID+"("+Context.session.userName+")";
		String line="";
		Map<String, String> Apps=Dictionary.getDictionaryMap("APP");
		if(!StringUtil.isNullOrEmpty(Context.Apps)){
			String[] apps=Context.Apps.split("\\|");
			for(int w=0;w<apps.length;w++){
				String key=apps[w];
				String value=Apps.get(key);
				line+=key+"["+value+"]";
			}
			if(!StringUtil.isNullOrEmpty(line)){
				tips+="\r\n"+line;
			}
		}
		
		btnUser.setToolTipText(tips);
		btnUser.setImage(Icons.getOperatorIcon());
		btnUser.setLayoutData(LayoutUtils.getToolsLayoutData());
	}
	
	public class ImportPkgAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			//注册会话功能
			Context.session.inroll(e.getClass().toString(), "F-001-00001");
			FileDialog dialog=new FileDialog(AppView.getInstance().getShell(),SWT.OPEN);
			dialog.setFilterPath(Paths.getInstance().getBasePath());
			
			dialog.setFileName("");
			dialog.setFilterExtensions(new String[] { "*.zip","*.rar" });
			dialog.setFilterNames(new String[]{"All Files(*.*)"});
			String filePath=dialog.open();
			//分析压缩包
			if(filePath!=null){
				 File pkgFile=new File(filePath);
				 String pkgID=FileUtils.getFileNameNoSuffix(pkgFile.getName());
				 //判断当前版本是否正在安装或者已经导入配置完成了
				 PKGBean  version=PKG.getPkg(pkgID);
				 
				 if(version!=null&&
				    (version.getStatus().equals(PKGBean.Status.Run.ordinal()+"")||
				    version.getStatus().equals(PKGBean.Status.Config.ordinal()+""))){
					    MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(Constants.getStringVaule("alert_busy"));
						box.open();	
				 }else{
					 try{
						 btnImport.setEnabled(false);
						 PKGOperator pkgOperator=new PKGOperator(filePath,pkgFile.getName());
						 pkgOperator.start();
						 while(!pkgOperator.isDone){
							 Thread.sleep(1000L);
						 }
						 btnImport.setEnabled(true);
					 }catch(Exception exp){
						 
					 }finally{
						 btnImport.setEnabled(true);
					 }
					 //记录日志详细
					 Logger.getInstance().logServer("导入版本包:"+pkgFile.getName());
				 }
			}
		 }
	}
	
	public class IntallVersionAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
				if(checkTime()){
					String item=Constants.getStringVaule("tabItem_myinstall");
					if(!DeployEditView.getInstance(null).getTabState(item)){
						PKGInstallView installview=new PKGInstallView(DeployEditView.getInstance(null).getTabFloder());
						DeployEditView.getInstance(null).setTabItems(installview.content, item);
					}
				}else{
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_installalert"));
					box.setMessage(Constants.getStringVaule("alert_timezone"));
					box.open();
				}
		}
	}
	
	  public  boolean checkTime(){
		  if(!StringUtil.isNullOrEmpty(Context.startTime)&&!StringUtil.isNullOrEmpty(Context.endTime)){
				String currTime=DateUtil.getCurrentDate("HH:mm");
				currTime=currTime.replace(":", "");
				try{
					int current=Integer.parseInt(currTime);
					int max=Integer.parseInt(Context.endTime.replace(":", ""));
					int min=Integer.parseInt(Context.startTime.replace(":", ""));
					if(current>max||current<min){
						return false;
					}else{
						return true;
					}
				}
				catch(Exception e){
					return false;
				}
			}
		 return true;
	  }
	  

	
	public class ShowSettingAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			SettingView.getInstance().show();
		}
	}
	
	public class ShowCompareAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_compare");
			if(!DeployEditView.getInstance(null).getTabState(item)){
				CompareFileView compareFileView=CompareFileView.getInstance(DeployEditView.getInstance(null).getTabFloder());
				 DeployEditView.getInstance(null).setTabItems(compareFileView.content, item);
			}
		}
	}
	
	
	public class TagManageAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_groupmanager");
			if(!DeployEditView.getInstance(null).getTabState(item)){
				TagManageView groupView=new TagManageView(DeployEditView.getInstance(null).getTabFloder());
				  DeployEditView.getInstance(null).setTabItems(groupView.content, item);
			}
		}
	}
		
	
	public class SetPasswdAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_passwd");
			if(!DeployEditView.getInstance(null).getTabState(item)){
				  PasswdView passwdView=PasswdView.getInstance(DeployEditView.getInstance(null).getTabFloder());
				  DeployEditView.getInstance(null).setTabItems(passwdView.content, item);
			}
		}
	}

	public class MyTaskAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_todoList");
			if(!DeployEditView.getInstance(null).getTabState(item)){
				DeployApproveView approveView=new DeployApproveView(DeployEditView.getInstance(null).getTabFloder());//DeployApproveView.getIntance(DeployEditView.getInstance(null).getTabFloder());
				DeployEditView.getInstance(null).setTabItems(approveView.content, item);
			}
		}
	}
	
	public class ConfigMngAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_cfgmng");
			if(!DeployEditView.getInstance(null).getTabState(item)){
				ConfigMngView cfgmngView=new ConfigMngView(DeployEditView.getInstance(null).getTabFloder());//DeployApproveView.getIntance(DeployEditView.getInstance(null).getTabFloder());
				DeployEditView.getInstance(null).setTabItems(cfgmngView.content, item);
			}
		}
	}
	
	public class ReportViewAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_report");
			if(!DeployEditView.getInstance(null).getTabState(item)){
				ReportView reportView=ReportView.getIntance(DeployEditView.getInstance(null).getTabFloder());
				 DeployEditView.getInstance(null).setTabItems(reportView.content, item);
			}
		}
	}
	
}
