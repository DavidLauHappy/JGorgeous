package business.admin.view;


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
 * ��˵��
 */
public class AdminSourceView extends SourceView{
	  
			public static AdminSourceView getInstance(Composite parent){			
				if(unique_instance==null){
					unique_instance=new AdminSourceView(parent,SWT.NONE);
				}
				return unique_instance;
			}
			

		private AdminSourceView(Composite com,int style){
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
			CTabItem itemUsers=new CTabItem(tabFloder,SWT.NONE);
			itemUsers.setText(Constants.getStringVaule("tabItem_users"));
			UserView userView=UserView.getUserView(tabFloder);
			userView.setLayout(LayoutUtils.getComGridLayout(1, 0));
			userView.pack();
			 //�Զ������ʶ��Ͷ������
			itemUsers.setControl(userView);
			 tabFloder.setSelection(itemUsers);
			tabFloder.pack();
			comSelf.setContent(tabFloder);
		}
		
		  //�õ�ǰѡ���Tabҳ����ǰ��
			public  void  setTabItems(Composite com,String tabName){
				 CTabItem new_item=new CTabItem(tabFloder,SWT.NONE);
				 new_item.setText(tabName);
				 new_item.setControl(com);
				 tabFloder.setSelection(new_item);
				 tabFloder.pack();
				 
				 WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
				 WorkAreaView.getInstance(null).getSachForm().layout(true);
				 
			}
			
			//��tabҳ����������Ļ
			public class CTabFullScreenAction extends MouseAdapter{
				public void mouseDoubleClick(MouseEvent e){
					  if(!isFolderMaxed){
						  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(comSelf);
						  AdminSourceView.getInstance(null).setFolderMaxed(true);
						  WorkAreaView.getInstance(null).getSachForm().layout(true);
					  }else{
						  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
						  AdminSourceView.getInstance(null).setFolderMaxed(false);
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
			private static AdminSourceView unique_instance;
		    private  ScrolledComposite comSelf=null;
			private   CTabFolder tabFloder=null;
}

