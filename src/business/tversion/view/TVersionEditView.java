package business.tversion.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

import resource.Constants;
import resource.TDiagramView;

import utils.LayoutUtils;
import views.AppView;
import views.EditView;
import views.WorkAreaView;


public class TVersionEditView extends EditView{
	private static TVersionEditView unique_instance;
	public static  TVersionEditView getInstance(Composite parent){
		if (unique_instance==null){
			unique_instance=new TVersionEditView(parent,SWT.NONE);//SWT.V_SCROLL|SWT.H_SCROLL|SWT.BORDER)
		}
		return unique_instance;
	}
	
	private Composite comSelf=null;
	public Composite getEditView(){
		return comSelf;
	}
	private TVersionEditView(Composite com,int style){
		//super(com,style);
		comSelf=new Composite(com,style);
		comSelf.setLayoutData(LayoutUtils.getEidtViewLayoutData());
		comSelf.setLayout(LayoutUtils.getEditViewLayout());
	}
	
	public void showEditView(){
		tabFloder=new CTabFolder(comSelf,SWT.TOP|SWT.BORDER|SWT.CLOSE);//||
		tabFloder.setLayoutData(LayoutUtils.getCommomGridData());
		tabFloder.setMaximizeVisible(true);
		tabFloder.setMinimizeVisible(true);  
		tabFloder.setSimple(false);
		tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		tabFloder.setTabHeight(20);
		tabFloder.addMouseListener(new CTabFullScreenAction());
		tabFloder.addCTabFolder2Listener(new CTabFolder2Adapter(){
			public void close(CTabFolderEvent  e){
				CTabItem closingItem =(CTabItem)e.item;
				Composite com=(Composite)closingItem.getControl();
				TDiagramView page=(TDiagramView)com.getData("page");
				if(page!=null){
					//�жϰ汾�Ƿ�װ���
					if(page.canPageRemove){
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage("�汾�Ѿ���ȷ��װ��ɣ�ȷ�ϲ�����װ����ֱ���˳���");
						int choice=box.open();		
						if(SWT.NO==choice){
							e.doit=false;
						}
					 }else{
						e.doit=false;
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage("�汾���ڰ�װ�����޷�ֱ���˳������ĵȴ���");
						box.open();		
					}
				}
			 }
			});
		//tabFloder.addCTabFolderListener(new CloseTabAction());
		//tabFloder.addSelectionListener(new TabSelectAction());
		
		//���˲���tabҳ��Ĭ�ϵ�
/*		 CTabItem itemVersionMake=new CTabItem(tabFloder,SWT.NONE);
		 itemVersionMake.setText(Constants.getStringVaule("tabItem_vermake"));
		 makeView=MakeView.getInstance(tabFloder);
		 makeView.setLayout(LayoutUtils.getDeployLayout());
		 makeView.pack();
		 itemVersionMake.setControl(makeView);
		 tabFloder.setSelection(itemVersionMake);*/
		  
		tabFloder.pack();
		comSelf.pack();
	}
	
	public CTabFolder getTabFloder(){
		return tabFloder;
	}
	private   CTabFolder tabFloder=null;
	//private  DeployView deployView=null;
	
	public  void  setTabItems(Composite com,String tabName)
	{				
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
	
	
	 //�ж�һ��TAB�Ƿ��Ѿ��رջ����Ѿ���ʾ
		public  boolean getTabState(String item){
			CTabItem[] items=tabFloder.getItems();
			for(int k=0;k<items.length;k++){
				if(item.equals(items[k].getText())){
					//������Ϊѡ��
				  tabFloder.setSelection(items[k]);    
				  return true;
				}
			}
			return false;
		}
		
	//���˫�����ÿؼ�������Ļ�ͻ�ԭ
		public class CTabFullScreenAction extends MouseAdapter{
			public void mouseDoubleClick(MouseEvent e){
			  if(!isFolderMaxed){
				  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(comSelf);
				  TVersionEditView.getInstance(null).setFolderMaxed(true);
				  WorkAreaView.getInstance(null).getSachForm().layout(true);
			  }else{
				  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
				  TVersionEditView.getInstance(null).setFolderMaxed(false);
				  WorkAreaView.getInstance(null).getSachForm().layout(true);
			  }		
			}
			public void mouseUp(MouseEvent e){
				CTabItem[] items=tabFloder.getItems();
	            int tabCount=items.length;
	            if(tabCount==0) {
	  			  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
				  TVersionEditView.getInstance(null).setFolderMaxed(false);
				  WorkAreaView.getInstance(null).getSachForm().layout(true);
	            }
			}
	   }

}
