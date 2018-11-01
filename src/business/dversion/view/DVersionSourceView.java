package business.dversion.view;

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

public class DVersionSourceView extends SourceView {

	private static DVersionSourceView unique_instance;
    private  ScrolledComposite comSelf=null;
	private   CTabFolder tabFloder=null;
	private DVersionFileView fileView=null;
	private ReleaseView releaseView=null;
	private StreamView streamView=null;
	private DVersionRequirementView requireView=null;
	public static DVersionSourceView getInstance(Composite parent){			
		if(unique_instance==null){
			unique_instance=new DVersionSourceView(parent,SWT.NONE);
		}
		return unique_instance;
	}
	
	private DVersionSourceView(Composite com,int style){
		comSelf=new ScrolledComposite(com,SWT.NONE);//SWT.V_SCROLL|SWT.H_SCROLL|
		comSelf.setLayoutData(LayoutUtils.getSourceViewLayoutData());
		comSelf.setLayout(LayoutUtils.getSourceViewLayout(comSelf));
	}
	
	public void createSourceView() {
		tabFloder=new CTabFolder(comSelf,SWT.TOP|SWT.BORDER);//SWT.CLOSE|
		tabFloder.setLayout(LayoutUtils.getTabFloderLayout(tabFloder));
		tabFloder.setMaximizeVisible(true);
		tabFloder.setMinimizeVisible(true);  
		tabFloder.setSimple(false);
		tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		tabFloder.addMouseListener(new CTabFullScreenAction());
		//文件浏览器 
		CTabItem itemFiles=new CTabItem(tabFloder,SWT.NONE);
		 itemFiles.setText(Constants.getStringVaule("tabItem_files"));
		 fileView=DVersionFileView.getInstance(tabFloder);
		 fileView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 fileView.pack();
		 itemFiles.setControl(fileView);
		 tabFloder.setSelection(itemFiles);
		 //待处理需求浏览器
		 CTabItem itemReqs=new CTabItem(tabFloder,SWT.NONE);
		 itemReqs.setText(Constants.getStringVaule("tabItem_requirement"));
		 requireView=DVersionRequirementView.getInstance(tabFloder);
		 requireView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 requireView.pack();
		 itemReqs.setControl(requireView);
		//发布浏览器
		 CTabItem itemRelease=new CTabItem(tabFloder,SWT.NONE);
		 itemRelease.setText(Constants.getStringVaule("tabItem_release"));
		 releaseView=ReleaseView.getInstance(tabFloder);
		 releaseView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 releaseView.pack();
		 itemRelease.setControl(releaseView);
		 //流管理器
		 CTabItem itemStream=new CTabItem(tabFloder,SWT.NONE);
		 itemStream.setText(Constants.getStringVaule("tabItem_stream"));
		 streamView=StreamView.getInstance(tabFloder);
		 streamView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 streamView.pack();
		 itemStream.setControl(streamView);
		 
		 tabFloder.pack();
		comSelf.setContent(tabFloder);
		 
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
	
	public class CTabFullScreenAction extends MouseAdapter{
		public void mouseDoubleClick(MouseEvent e){
			 if(!isFolderMaxed){
				 WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(comSelf);
            	 WorkAreaView.getInstance(null).getSachForm().layout(true);
            	 DVersionSourceView.getInstance(null).setFolderMaxed(true);
			 }else{
				 WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
	            	WorkAreaView.getInstance(null).getSachForm().layout(true);
	            	DVersionSourceView.getInstance(null).setFolderMaxed(false); 
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

}
