package business.dversion.view;



import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;


import utils.LayoutUtils;
import views.AppView;
import views.EditView;
import views.WorkAreaView;

public class DVersionEditView extends EditView {

	private static DVersionEditView unique_instance=null;
	public static  DVersionEditView getInstance(Composite parent){
		if (unique_instance==null){
			unique_instance=new DVersionEditView(parent,SWT.NONE);//SWT.V_SCROLL|SWT.H_SCROLL|SWT.BORDER)
		}
		return unique_instance;
		
	}
	
	private DVersionEditView(Composite com,int style)
	{
		//super(com,style);
		comSelf=new Composite(com,style);
		comSelf.setLayoutData(LayoutUtils.getEidtViewLayoutData());
		comSelf.setLayout(LayoutUtils.getEditViewLayout());
	}
	
	private Composite comSelf=null;
	private CTabFolder tabFloder=null;
	public Composite getEditView(){
		return comSelf;
	}
	
	public void showEditView() {
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

		 tabFloder.pack();
			//comSelf.setContent(tabFloder);
		 comSelf.pack();
	}

	@Override
	public boolean getTabState(String name) {
		CTabItem[] items=tabFloder.getItems();
		for(int k=0;k<items.length;k++){
			if(name.equals(items[k].getText())){
			  tabFloder.setSelection(items[k]);   //先设置为选中 
			  return true;
			}
		}
		return false;
	}

	@Override
	public CTabFolder getTabFloder() {
		return tabFloder;
	}

	@Override
	public void setTabItems(Composite com, String tabName) {
		 CTabItem new_item=new CTabItem(tabFloder,SWT.NONE);
		 new_item.setText(tabName);
		 new_item.setControl(com);
		 new_item.setData(com);
		 com.pack();
		 com.layout(true);
		tabFloder.setSelection(new_item);
		tabFloder.layout(true);

	}
	private  boolean isFolderMaxed=false;
	
	public boolean isFolderMaxed() {
		return isFolderMaxed;
	}
	public void setFolderMaxed(boolean isFolderMaxed) {
		this.isFolderMaxed = isFolderMaxed;
	}
	
	//鼠标双击设置控件充满屏幕和还原
			public class CTabFullScreenAction extends MouseAdapter{
				public void mouseDoubleClick(MouseEvent e){
				  if(!isFolderMaxed){
					  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(comSelf);
					  DVersionEditView.getInstance(null).setFolderMaxed(true);
					  WorkAreaView.getInstance(null).getSachForm().layout(true);
				  }else{
					  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
					  DVersionEditView.getInstance(null).setFolderMaxed(false);
					  WorkAreaView.getInstance(null).getSachForm().layout(true);
				  }		
				}
				public void mouseUp(MouseEvent e){
					CTabItem[] items=tabFloder.getItems();
		            int tabCount=items.length;
		            if(tabCount==0) {
		            	  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
						  DVersionEditView.getInstance(null).setFolderMaxed(false);
						  WorkAreaView.getInstance(null).getSachForm().layout(true);
		            }
				}
		   }
}
