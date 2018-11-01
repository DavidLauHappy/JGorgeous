package business.developer.view;


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
import views.FileSourceView;
import views.SourceView;
import views.WorkAreaView;

/**
 * @author DavidLaur
 * @date 2016-8-11
 * @version 1.0
 * 类说明
 */
public class DeveloperSourceView extends SourceView{
	  
			public static DeveloperSourceView getInstance(Composite parent){			
				if(unique_instance==null){
					unique_instance=new DeveloperSourceView(parent,SWT.NONE);
				}
				return unique_instance;
			}
			

		private RequirementView reqView=null;
		private FileSourceView fileView=null;
		private DeveloperSourceView(Composite com,int style){
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
			////////////////////////////////////////////////////////////////////////////////////
			 CTabItem requirementTab=new CTabItem(tabFloder,SWT.NONE);
			 requirementTab.setText(Constants.getStringVaule("tabItem_task"));
			 reqView=RequirementView.getInstance(tabFloder);
			 reqView.setLayout(LayoutUtils.getComGridLayout(1, 0));
			 reqView.pack();
			 requirementTab.setControl(reqView);
			 tabFloder.setSelection(requirementTab);
			 /////////////////////////////////////////////////////////////////////
			 CTabItem fileTab=new CTabItem(tabFloder,SWT.NONE);
			 fileTab.setText(Constants.getStringVaule("tabItem_files"));
			 fileView=FileSourceView.getInstance(tabFloder);
			 fileView.setLayout(LayoutUtils.getComGridLayout(1, 0));
			 fileView.pack();
			 fileTab.setControl(fileView);
			 
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
						  DeveloperSourceView.getInstance(null).setFolderMaxed(true);
						  WorkAreaView.getInstance(null).getSachForm().layout(true);
					  }else{
						  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
						  DeveloperSourceView.getInstance(null).setFolderMaxed(false);
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
			private static DeveloperSourceView unique_instance;
		    private  ScrolledComposite comSelf=null;
			private   CTabFolder tabFloder=null;
}

