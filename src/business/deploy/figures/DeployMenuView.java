package business.deploy.figures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.DATAFLAG;
import model.SYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import common.db.DataHelper;

import resource.Constants;
import resource.Context;
import resource.Icons;
import resource.Paths;
import utils.FileUtils;
import utils.StringUtil;
import views.AppView;
import views.EditView;
import views.MenuView;
import views.NewFileView;
import bean.DATAFLAGBean;
import bean.SYSTEMBean;
import business.deploy.core.PkgNodeScheduler;
import business.deploy.core.PkgSystemScheduler;
import business.deploy.core.ViewRefresher;

/**
 * @author 作者 E-mail:
 * @date 2016-8-11
 * @version 1.0
 * 类说明
 */
public class DeployMenuView extends MenuView{
	
	private Shell  parentShell=null;
	private Composite mainComposite=null;
	private Menu mainMenu=null;
	private static DeployMenuView unique_instance;
	public static  DeployMenuView getInstance(Shell shell){
		if(unique_instance==null){
			unique_instance=new DeployMenuView(shell);
		}
		return unique_instance;
	}
	public DeployMenuView(Shell shell)
	{
		parentShell=shell;
	}
	
	public void createMenuView(){
		mainMenu=new Menu(parentShell,SWT.BAR);

		/*createManageMenu(m_parent,main_menu);
		createToolsMenu(m_parent,main_menu);
		createWindowMenu(m_parent,main_menu);
		createConfigMenu(m_parent,main_menu);
		createDocumentMenu(m_parent,main_menu);
		createModularMenu(m_parent,main_menu);*/
		this.createFileMenu(parentShell,mainMenu);
		this.createNodeMenu(parentShell, mainMenu);
		this.createHelpMenu(parentShell, mainMenu);
		parentShell.setMenuBar(mainMenu);
		
	}
	
	
	private void createFileMenu(Shell shell,Menu menu)
	{
		//系统菜单的第一个菜单项
		MenuItem file=new MenuItem(menu,SWT.CASCADE);
		file.setText(Constants.getStringVaule("menu_file"));
		///////////////文件菜单
		Menu file_menu=new Menu(shell,SWT.DROP_DOWN);
		//打开文件
		MenuItem  openItem=new MenuItem(file_menu,SWT.PUSH);
		openItem.setText(Constants.getStringVaule("menuitem_file_openvzip"));
		openItem.setImage(Icons.getImportIcon16());
		openItem.addSelectionListener(new OpenFileAction());
	/*	////另存为
		MenuItem  saveAsItem=new MenuItem(file_menu,SWT.PUSH);
		saveAsItem.setText(Constants.getStringVaule("menuitem_file_saveAs"));
		saveAsItem.addSelectionListener(new FileSaveAsAction());
		///关闭
		MenuItem  closeItem=new MenuItem(file_menu,SWT.PUSH);
		closeItem.setText(Constants.getStringVaule("menuitem_file_close"));
		closeItem.addSelectionListener(new CloseTabAction());
		///关闭所有
		MenuItem  closeAllItem=new MenuItem(file_menu,SWT.PUSH);
		closeAllItem.setText(Constants.getStringVaule("menuitem_file_closeAll"));
		closeAllItem.addSelectionListener(new CloseAllTabAction());*/
		//退出
		MenuItem  exitItem=new MenuItem(file_menu,SWT.PUSH);
		exitItem.setText(Constants.getStringVaule("menuitem_file_exit"));
		exitItem.setImage(Icons.getDeleteIcon());
		exitItem.addSelectionListener(new CloseTabAction());
		file.setMenu(file_menu);
		
	}
	
	private void createNodeMenu(Shell shell,Menu menu){
		group=new MenuItem(menu,SWT.CASCADE);
		group.setText(Constants.getStringVaule("menu_group"));
		///////////////文件菜单
		 group_menu=new Menu(shell,SWT.DROP_DOWN);
		//从数据库加载应用系统
		 DATAFLAGBean dataflag=DATAFLAG.getByID(Constants.dataFlagCmdb);
		 Context.session.currentFlag=dataflag.getFlag();
		if(!StringUtil.isNullOrEmpty(Context.Apps)){
			String[] apps=Context.Apps.split("\\|");
			List<SYSTEMBean> systems=new ArrayList<SYSTEMBean>();
			for(int w=0;w<apps.length;w++){
				List<SYSTEMBean> appSys=SYSTEM.getSystems(apps[w], dataflag.getFlag());
				systems.addAll(appSys);
			}
						
			if(systems!=null&&systems.size()>0){
				for(SYSTEMBean bean:systems){
					MenuItem  openItem=new MenuItem(group_menu,SWT.PUSH);
					openItem.setText(bean.getName());
					openItem.setData(bean);
					openItem.setImage(Icons.getTagFloderIcon());
					openItem.addSelectionListener(new OpenDeployAction());
				}
			}
		}
		group.setMenu(group_menu);
	}
	
/*	public void refreshNodeMenu(String app){
		   List<business.deploy.bean.System> appSys=DataHelper.getSystem(app);
		   if(appSys!=null&&appSys.size()>0){
			   for(business.deploy.bean.System bean:appSys){
					MenuItem  openItem=new MenuItem(group_menu,SWT.PUSH);
					openItem.setText(bean.getName());
					openItem.setData(bean);
					openItem.setImage(Icons.getTagFloderIcon());
					openItem.addSelectionListener(new OpenDeployAction());
				}
			   group.setMenu(group_menu);
		   }
	}*/
	
	private void createHelpMenu(Shell shell,Menu menu){
		MenuItem help=new MenuItem(menu,SWT.CASCADE);
		help.setText(Constants.getStringVaule("menu_help"));
		///////////////文件菜单
		Menu help_menu=new Menu(shell,SWT.DROP_DOWN);
		//打开文件
		MenuItem  manualItem=new MenuItem(help_menu,SWT.PUSH);
		manualItem.setText(Constants.getStringVaule("menuitem_manual"));
		manualItem.addSelectionListener(new OpenManualAction());
		MenuItem  aboutItem=new MenuItem(help_menu,SWT.PUSH);
		aboutItem.setText(Constants.getStringVaule("menuitem_about"));
		aboutItem.addSelectionListener(new ShowAboutAction());
		help.setMenu(help_menu);
	}
	private  Menu group_menu=null;
	private  MenuItem group=null;
	public  void refreshNodeMenu(){
		group_menu.dispose();
		group_menu=new Menu(AppView.getInstance().getShell(),SWT.DROP_DOWN);
		//从数据库加载群组信息
		group.setMenu(group_menu);
		List<SYSTEMBean> appSys=SYSTEM.getSystems(Context.pkgApp, Context.session.currentFlag);
		if(appSys!=null&&appSys.size()>0){
			for(SYSTEMBean bean:appSys){
				MenuItem  openItem=new MenuItem(group_menu,SWT.PUSH);
				openItem.setText(bean.getName());
				openItem.setData(bean);
				openItem.setImage(Icons.getTagFloderIcon());
				openItem.addSelectionListener(new OpenDeployAction());
			}
		}
		group.setMenu(group_menu);
	}
	
	public class OpenDeployAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			MenuItem control=(MenuItem)e.getSource();
			SYSTEMBean bean=(SYSTEMBean)control.getData();
			DeployEditView.getInstance(null).setDeployView(bean,"");
		}
	}
	
	
	public class ShowAboutAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			DeployAboutView.getInstance().show();
		}
	}
	
	public class OpenManualAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String fileName=FileUtils.formatPath(Paths.getInstance().getBasePath())+File.separator+"Manual.pdf";
		  	int dot = fileName.lastIndexOf('.');
		 	   if (dot != -1) {
		 	     String extension = fileName.substring(dot);
		 	     Program program = Program.findProgram(extension);
		 	     if (program != null){
		 	    	program.launch(fileName);
		 	      }
		 	   }
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//内部类，菜单响应函数
	public class OpenFileAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			FileDialog dialog=new FileDialog(parentShell,SWT.OPEN);
			dialog.setFilterPath(Paths.getInstance().getBasePath());
			dialog.setFilterExtensions(new String[]{"*.*"});
			dialog.setFilterNames(new String[]{"All Files(*.*)"});
			String filePath=dialog.open();
			if(filePath!=null)
			{
				String content=FileUtils.getFileContent(filePath);
				List<String> lines=FileUtils.getFileLineList(filePath);
				File file=new File(filePath);
				String name=file.getName();
				String linecontent="";
				if(lines!=null&&lines.size()>0){
					for(int w=0;w<lines.size();w++){
						linecontent=linecontent+(w+1)+"\r\n";
					}
				}
			  if(!DeployEditView.getInstance(null).getTabState(name)){
				  NewFileView nfv=new NewFileView(DeployEditView.getInstance(null).getTabFloder(),content,linecontent,filePath);
				  DeployEditView.getInstance(null).setTabItems(nfv.self, name);
			  }
			}
		}
	}
	
	
	public class FileSaveAsAction extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			FileDialog dialog = new FileDialog(parentShell, SWT.SAVE);
			dialog.setFilterPath(Paths.getInstance().getBasePath());
			dialog.setFilterExtensions(new String[] { "*.*" });
			dialog.setFilterNames(new String[] { "All Files(*.*)" });
			String filePath = dialog.open();
			if (filePath != null) {
	/*			CTabItem[] tabs = EditArea.tabFloder.getItems();
				if (EditArea.tabFloder.getSelection() != null) {
					String activeTabText = EditArea.tabFloder.getSelection()
							.getText();
					for (int k = 0; k < tabs.length; k++) {
						if (tabs[k].getText().equals(activeTabText)) {
							Composite com = (Composite) tabs[k].getData();
							Control[] children = com.getChildren();
							Composite content = (Composite) children[0];
							if (content != null
									&& content.getChildren().length >= 2) {
								String controlType = content.getChildren()[1]
										.toString();
								if (controlType.startsWith("Text")) {
									Text text = (Text) content.getChildren()[1];
									if (text != null) {
										try {
											File file = new File(filePath);
											FileWriter fw = new FileWriter(file);
											BufferedWriter bw = new BufferedWriter(fw);
											bw.write(text.getText());
											bw.close();
											fw.close();
										} catch (Exception e1) {
											LogUtil.Log(e1.getMessage());
										}
									}
								}
							}
						}
					}
				}*/
			}
		}
	}
	
	public class CloseTabAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			AppView.getInstance().getShell().dispose();
			PkgNodeScheduler.getInstance().exit();//调度管理退出
			PkgSystemScheduler.getInstance().exit();////调度管理退出
			ViewRefresher.getInstance().exit();//监管线程退出
		}
	}
	
	public class CloseAllTabAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			
		}
	}
}
