package business.tversion.view;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import resource.Constants;
import resource.Icons;
import resource.Paths;

import utils.FileUtils;
import utils.StringUtil;
import utils.Zipper;
import views.AppView;
import views.MenuView;


public class TVersionMenuView  extends MenuView {

	private static TVersionMenuView unique_instance;
	private Shell  parentShell=null;
	private Menu mainMenu=null;
	public static  TVersionMenuView getInstance(Shell shell){
		if(unique_instance==null){
			unique_instance=new TVersionMenuView(shell);
		}
		return unique_instance;
	}
	private TVersionMenuView(Shell shell)
	{
		parentShell=shell;
	}
	@Override
	public void createMenuView() {
		mainMenu=new Menu(parentShell,SWT.BAR);
		this.createFileMenu(parentShell,mainMenu);
		parentShell.setMenuBar(mainMenu);
	}
	
	private void createFileMenu(Shell shell,Menu menu){
		//系统菜单的第一个菜单项
		MenuItem file=new MenuItem(menu,SWT.CASCADE);
		file.setText(Constants.getStringVaule("menu_file"));
		///////////////文件菜单
		Menu file_menu=new Menu(shell,SWT.DROP_DOWN);
		//打开文件
		MenuItem  openItem=new MenuItem(file_menu,SWT.PUSH);
		openItem.setText(Constants.getStringVaule("menuitem_file_openvzip"));
		openItem.setImage(Icons.getCfgNodeIcon());
		openItem.addSelectionListener(new OpenFileAction());
		file.setMenu(file_menu);
	}
	
	public class OpenFileAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			FileDialog dialog=new FileDialog(parentShell,SWT.OPEN);
			dialog.setFilterPath(Paths.getInstance().getBasePath());
			dialog.setFilterExtensions(new String[]{"*.zip"});
			dialog.setFilterNames(new String[]{"All Files(*.*)"});
			String filePath=dialog.open();
			if(filePath!=null){
				File Dir=new File(filePath);
				boolean result=Zipper.getInstance().unPackage(filePath, Paths.getInstance().getWorkDir());
				if(!result){
					MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
 					messagebox.setText(Constants.getStringVaule("messagebox_alert"));
 					messagebox.setMessage("版本包["+filePath+"]处理错误！详见日志。");
 					messagebox.open();
				}else{
					String versionID=FileUtils.getFileNameNoSuffix(Dir.getName());
					String direcotry=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+versionID;
					
					File startDir=new File(direcotry);
					if(startDir.exists()&&startDir.isDirectory()){
						String playFile="";
						File[] files=startDir.listFiles();
						if(files!=null&&files.length>0){
							for(File file:files){
								if(file.getName().endsWith("_play.xml")){
									playFile=file.getAbsolutePath();
									break;
								}
							}
						}
						if(!StringUtil.isNullOrEmpty(playFile)){
				   			if(!TVersionEditView.getInstance(null).getTabState(versionID)){
				   				MakeView makeView=new MakeView(TVersionEditView.getInstance(null).getTabFloder(),versionID,playFile);
				   				makeView.disableApp();
				   				TVersionEditView.getInstance(null).setTabItems(makeView.selfContent, versionID);
				   			}
						}
					}
				}
			}
		}
	}

}
