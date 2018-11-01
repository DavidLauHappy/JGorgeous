package business.aduitor.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;



import resource.Constants;
import utils.LayoutUtils;
import views.AppView;
import views.SourceView;
import views.WorkAreaView;

/**
 * @author DavidLaur
 * @date 2016-8-11
 * @version 1.0
 * 类说明
 */
public class AuditorSourceView extends SourceView{
	  
			public static AuditorSourceView getInstance(Composite parent){			
				if(unique_instance==null){
					unique_instance=new AuditorSourceView(parent,SWT.NONE);
				}
				return unique_instance;
			}
			

		private AuditorSourceView(Composite com,int style){
			comSelf=new ScrolledComposite(com,SWT.NONE);
			comSelf.setLayoutData(LayoutUtils.getSourceViewLayoutData());
			comSelf.setLayout(LayoutUtils.getSourceViewLayout(comSelf));
		}
		

		public void createSourceView(){
			tabFloder=new CTabFolder(comSelf,SWT.TOP|SWT.BORDER);
			tabFloder.setLayout(LayoutUtils.getTabFloderLayout(tabFloder));
			tabFloder.setMaximizeVisible(true);
			tabFloder.setMinimizeVisible(true);  
			tabFloder.setSimple(false);
			tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			tabFloder.addMouseListener(new CTabFullScreenAction());
			
			CTabItem itemApps=new CTabItem(tabFloder,SWT.NONE);
			itemApps.setText(Constants.getStringVaule("tabItem_appsystem"));
			AppSystemView appView=AppSystemView.getInstance(tabFloder);
			appView.setLayout(LayoutUtils.getComGridLayout(1, 0));
			appView.pack();
			 //自动浏览并识别投产步骤
			itemApps.setControl(appView);
			 tabFloder.setSelection(itemApps);
			tabFloder.pack();
			comSelf.setContent(tabFloder);
		}
		
		  //让当前选择的Tab页在最前面
			public  void  setTabItems(Composite com,String tabName){
				 CTabItem new_item=new CTabItem(tabFloder,SWT.NONE);
				 new_item.setText(tabName);
				 new_item.setControl(com);
				 tabFloder.setSelection(new_item);
				 tabFloder.pack();
				 
				 WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
				 WorkAreaView.getInstance(null).getSachForm().layout(true);
				 
			}
			
			//让tab页充满整个屏幕
			public class CTabFullScreenAction extends MouseAdapter{
				public void mouseDoubleClick(MouseEvent e){
					  if(!isFolderMaxed){
						  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(comSelf);
						  AuditorSourceView.getInstance(null).setFolderMaxed(true);
						  WorkAreaView.getInstance(null).getSachForm().layout(true);
					  }else{
						  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
						  AuditorSourceView.getInstance(null).setFolderMaxed(false);
						  WorkAreaView.getInstance(null).getSachForm().layout(true);
					  }		
				}
			}
			
			private  boolean isFolderMaxed=false;
			public boolean isFolderMaxed() {
				return isFolderMaxed;
			}
			public void setFolderMaxed(boolean isFolderMaxed) {
				this.isFolderMaxed = isFolderMaxed;
			}
			private static AuditorSourceView unique_instance;
		    private  ScrolledComposite comSelf=null;
			private   CTabFolder tabFloder=null;
}

