package business.tester.view;

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

public class TesterSourceView extends SourceView {

	private TesterReleaseView releaseView=null;
	private TesterStreamView streamView=null;
	private TesterFileView fileView=null;
	public static TesterSourceView getInstance(Composite parent){			
		if(unique_instance==null){
			unique_instance=new TesterSourceView(parent,SWT.NONE);
		}
		return unique_instance;
	}
	
	private TesterSourceView(Composite com,int style){
		comSelf=new ScrolledComposite(com,SWT.V_SCROLL|SWT.H_SCROLL|SWT.NONE);
		comSelf.setLayoutData(LayoutUtils.getSourceViewLayoutData());
		comSelf.setLayout(LayoutUtils.getSourceViewLayout(comSelf));
	}

	public void createSourceView() {
		tabFloder=new CTabFolder(comSelf,SWT.TOP|SWT.BORDER);//SWT.CLOSE
		tabFloder.setLayout(LayoutUtils.getTabFloderLayout(tabFloder));
		tabFloder.setMaximizeVisible(true);
		tabFloder.setMinimizeVisible(true);  
		tabFloder.setSimple(false);
		tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		tabFloder.addMouseListener(new CTabFullScreenAction());
		 
			//发布浏览器
		 CTabItem itemRelease=new CTabItem(tabFloder,SWT.NONE);
		 itemRelease.setText(Constants.getStringVaule("tabItem_task"));
		 releaseView=TesterReleaseView.getInstance(tabFloder);
		 releaseView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 releaseView.pack();
		 itemRelease.setControl(releaseView);
		 tabFloder.setSelection(itemRelease);
		 
		 //流管理器
		 CTabItem itemStream=new CTabItem(tabFloder,SWT.NONE);
		 itemStream.setText(Constants.getStringVaule("tabItem_stream"));
		 streamView=TesterStreamView.getInstance(tabFloder);
		 streamView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 streamView.pack();
		 itemStream.setControl(streamView);
		 //文件浏览器
		 CTabItem itemFiles=new CTabItem(tabFloder,SWT.NONE);
		 itemFiles.setText(Constants.getStringVaule("tabItem_files"));
		 fileView=TesterFileView.getInstance(tabFloder);
		 fileView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 fileView.pack();
		 itemFiles.setControl(fileView);
		 
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
	
	public void setReleaseTabTop(String name) {
		CTabItem[] items=tabFloder.getItems();
		for(int k=0;k<items.length;k++){
			if(name.equals(items[k].getText())){
			  tabFloder.setSelection(items[k]);   //先设置为选中 
			  releaseView.refreshTree();
			}
		}
	}
	
	public void setStreamTabTop(String name) {
		CTabItem[] items=tabFloder.getItems();
		for(int k=0;k<items.length;k++){
			if(name.equals(items[k].getText())){
			  tabFloder.setSelection(items[k]);   //先设置为选中 
			  streamView.refreshTree();
			}
		}
	}
	
	//让tab页充满整个屏幕
	public class CTabFullScreenAction extends MouseAdapter{
		public void mouseDoubleClick(MouseEvent e){
			 if(!isFolderMaxed){
				 WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(comSelf);
            	 WorkAreaView.getInstance(null).getSachForm().layout(true);
            	 TesterSourceView.getInstance(null).setFolderMaxed(true);
			 }else{
				 WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
	            	WorkAreaView.getInstance(null).getSachForm().layout(true);
	            	TesterSourceView.getInstance(null).setFolderMaxed(false); 
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
	private static TesterSourceView unique_instance;
    private  ScrolledComposite comSelf=null;
	private   CTabFolder tabFloder=null;
}
