package business.developer.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

import resource.Constants;
import utils.LayoutUtils;
import views.AppView;
import views.EditView;
import views.WorkAreaView;

public class DeveloperEditView extends EditView{
	private static DeveloperEditView unique_instance=null;
	public static  DeveloperEditView getInstance(Composite parent){
		if (unique_instance==null){
			unique_instance=new DeveloperEditView(parent,SWT.NONE);
		}
		return unique_instance;
		
	}
	
	private Composite comSelf=null;
	public Composite getEditView(){
		return comSelf;
	}
	private DeveloperEditView(Composite com,int style){
		comSelf=new Composite(com,style);
		comSelf.setLayoutData(LayoutUtils.getEidtViewLayoutData());
		comSelf.setLayout(LayoutUtils.getEditViewLayout());
	}
	
	public void showEditView(){
		tabFloder=new CTabFolder(comSelf,SWT.TOP|SWT.BORDER|SWT.CLOSE);//|SWT.CLOSE|
		tabFloder.setLayoutData(LayoutUtils.getCommomGridData());
		tabFloder.setLayout(LayoutUtils.getComGridLayout(1, 1));
		tabFloder.setMaximizeVisible(true);
		tabFloder.setMinimizeVisible(true);  
		tabFloder.setSimple(false);
		tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		tabFloder.setTabHeight(20);
		tabFloder.addMouseListener(new CTabFullScreenAction());
		//tabFloder.addCTabFolder2Listener(new CloseTabAction());
		tabFloder.addCTabFolderListener(new CloseTabAction());
		//tabFloder.addSelectionListener(new TabSelectAction());
	  	
		//拓扑部署tab页是默认的
		/* */
		 tabFloder.pack();
		 comSelf.pack();
      
	}
	
	public CTabFolder getTabFloder(){
		return tabFloder;
	}

	
	public  void  setTabItems(Composite com,String tabName)
	{				
		 CTabItem new_item=new CTabItem(tabFloder,SWT.NONE);
		 new_item.setText(tabName);
		 new_item.setControl(com);
		 new_item.setData(com);
		 new_item.setData("TYPE","Other");
		 com.pack();
		 com.layout(true);
		tabFloder.setSelection(new_item);
		tabFloder.layout(true);
	}
	
   
	
	public boolean isFolderMaxed() {
		return isFolderMaxed;
	}
	public void setFolderMaxed(boolean isFolderMaxed) {
		this.isFolderMaxed = isFolderMaxed;
	}
	
	
	 //判断一个TAB是否已经关闭还是已经显示
		public  boolean getTabState(String item){
			CTabItem[] items=tabFloder.getItems();
			for(int k=0;k<items.length;k++){
				if(item.equals(items[k].getText())){
				  tabFloder.setSelection(items[k]);   //先设置为选中 
				  return true;
				}
			}
			return false;
		}
		
		//根据名词关闭某个tablItem
		public void closeTab(String itemName){
			CTabItem[] items=tabFloder.getItems();
			for(int k=0;k<items.length;k++){
				if(itemName.equals(items[k].getText())){
					items[k].dispose();
					//删除tab页，考虑最大化情况下如果是最会最后一个，需要主界面还原的问题
		            int tabCount=items.length;
		            if(tabCount==0) {
		            	WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
						  DeveloperEditView.getInstance(null).setFolderMaxed(false);
						  WorkAreaView.getInstance(null).getSachForm().layout(true);
		            }
				}
			}
		}
		
		
	//鼠标双击设置控件充满屏幕和还原
		public class CTabFullScreenAction extends MouseAdapter{
			public void mouseDoubleClick(MouseEvent e){
			  if(!isFolderMaxed){
				  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(comSelf);
				  DeveloperEditView.getInstance(null).setFolderMaxed(true);
				  WorkAreaView.getInstance(null).getSachForm().layout(true);
			  }else{
				  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
				  DeveloperEditView.getInstance(null).setFolderMaxed(false);
				  WorkAreaView.getInstance(null).getSachForm().layout(true);
			  }		
			}
			public void mouseUp(MouseEvent e){
				CTabItem[] items=tabFloder.getItems();
	            int tabCount=items.length;
	            if(tabCount==0) {
	            	WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
					  DeveloperEditView.getInstance(null).setFolderMaxed(false);
					  WorkAreaView.getInstance(null).getSachForm().layout(true);
	            }
			}
	   }
		//关闭这个Tab页
		public class CloseTabAction extends CTabFolderAdapter{
			public void itemClosed(CTabFolderEvent e){
				CTabItem currItem=(CTabItem)e.item; 
				
			}
		}
		
		
		private   CTabFolder tabFloder=null;
		private  boolean isFolderMaxed=false;
		
		
}
