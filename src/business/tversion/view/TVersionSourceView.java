package business.tversion.view;

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


public class TVersionSourceView extends SourceView {
	private TVersionReleaseView releaseView=null;
	private TVersionStreamView streamView=null;
	
	public static TVersionSourceView getInstance(Composite parent){			
		if(unique_instance==null){
			unique_instance=new TVersionSourceView(parent,SWT.NONE);
		}
		return unique_instance;
	}
	

private TVersionSourceView(Composite com,int style){
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
		 //�Զ������ʶ��Ͷ������
		 CTabItem itemFiles=new CTabItem(tabFloder,SWT.NONE);
		 itemFiles.setText(Constants.getStringVaule("tabItem_files"));
		 fileView=FileView.getInstance(tabFloder);
		 fileView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 fileView.pack();
		 itemFiles.setControl(fileView);
		 tabFloder.setSelection(itemFiles);
		 
			//���������
		 CTabItem itemRelease=new CTabItem(tabFloder,SWT.NONE);
		 itemRelease.setText(Constants.getStringVaule("tabItem_release"));
		 releaseView=TVersionReleaseView.getInstance(tabFloder);
		 releaseView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 releaseView.pack();
		 itemRelease.setControl(releaseView);
		 //��������
		 CTabItem itemStream=new CTabItem(tabFloder,SWT.NONE);
		 itemStream.setText(Constants.getStringVaule("tabItem_stream"));
		 streamView=TVersionStreamView.getInstance(tabFloder);
		 streamView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 streamView.pack();
		 itemStream.setControl(streamView);
		 
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
	
	public void setReleaseTabTop(String name) {
		CTabItem[] items=tabFloder.getItems();
		for(int k=0;k<items.length;k++){
			if(name.equals(items[k].getText())){
			  tabFloder.setSelection(items[k]);   //������Ϊѡ�� 
			  releaseView.refreshTree();
			}
		}
	}
	
	public void setStreamTabTop(String name) {
		CTabItem[] items=tabFloder.getItems();
		for(int k=0;k<items.length;k++){
			if(name.equals(items[k].getText())){
			  tabFloder.setSelection(items[k]);   //������Ϊѡ�� 
			  streamView.refreshTree();
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
	
	//��tabҳ����������Ļ
	public class CTabFullScreenAction extends MouseAdapter{
		public void mouseDoubleClick(MouseEvent e){
			  if(!isFolderMaxed){
            	 WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(comSelf);
            	 TVersionSourceView.getInstance(null).setFolderMaxed(true);
            	 WorkAreaView.getInstance(null).getSachForm().layout(true);
            }
            else{
            	WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
            	WorkAreaView.getInstance(null).getSachForm().layout(true);
            	 TVersionSourceView.getInstance(null).setFolderMaxed(false);
            }
	    }
	}
	private static TVersionSourceView unique_instance;
    private  ScrolledComposite comSelf=null;
	private   CTabFolder tabFloder=null;
	private FileView fileView=null;

}
