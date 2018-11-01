package business.deploy.figures;


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

import bean.SYSTEMBean;
import utils.LayoutUtils;
import views.AppView;
import views.EditView;
import views.WorkAreaView;


/**
 * @author DavidLau
 * @date 2016-8-12
 * @version 1.0
 * ��˵��
 */
public class DeployEditView extends EditView{
	private static DeployEditView unique_instance=null;
	public static  DeployEditView getInstance(Composite parent){
		if (unique_instance==null){
			unique_instance=new DeployEditView(parent,SWT.NONE);//SWT.V_SCROLL|SWT.H_SCROLL|SWT.BORDER)
		}
		return unique_instance;
		
	}
	
	private Composite comSelf=null;
	public Composite getEditView(){
		return comSelf;
	}
	private DeployEditView(Composite com,int style){
		//super(com,style);
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
		tabFloder.addCTabFolder2Listener(new CTabFolder2Adapter(){
			public void close(CTabFolderEvent  e){
				CTabItem closingItem =(CTabItem)e.item;
				if(closingItem.getData()!=null){
					DeployView page=(DeployView)closingItem.getData();
					if(!page.canClose){
						e.doit=false;
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage("�汾���ڰ�װ�����޷�ֱ���˳������ĵȴ���");
						box.open();		
					}else{
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage("ȷ��Ҫ�ر�ҳ����");
						int choice=box.open();		
						if(SWT.NO==choice){
							e.doit=false;
						}
					}
				}
			}
		});
	
		 tabFloder.pack();
		 comSelf.pack();
      
	}
	
	public CTabFolder getTabFloder(){
		return tabFloder;
	}

	
	public  void  setTabItems(Composite com,String tabName){				
		 CTabItem new_item=new CTabItem(tabFloder,SWT.NONE);
		 new_item.setText(tabName);
		 new_item.setControl(com);
		 //new_item.setData(com);
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
	
	
	 //�ж�һ��TAB�Ƿ��Ѿ��رջ����Ѿ���ʾ
		public  boolean getTabState(String item){
			CTabItem[] items=tabFloder.getItems();
			for(int k=0;k<items.length;k++){
				if(item.equals(items[k].getText())){
				  tabFloder.setSelection(items[k]);   //������Ϊѡ�� 
				  return true;
				}
			}
			return false;
		}
		
		//�������ʹر�ĳ��tablItem
		public void closeTab(String itemName){
			CTabItem[] items=tabFloder.getItems();
			for(int k=0;k<items.length;k++){
				if(itemName.equals(items[k].getText())){
					items[k].dispose();
					//ɾ��tabҳ�������������������������һ������Ҫ�����滹ԭ������
		            int tabCount=items.length;
		            if(tabCount==0) {
		            	WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
						  DeployEditView.getInstance(null).setFolderMaxed(false);
						  WorkAreaView.getInstance(null).getSachForm().layout(true);
		            }
				}
			}
		}
		
		public void setDeployView(SYSTEMBean data,String versionID){
					String tabName=data.getName();
					boolean checked=false;
					CTabItem[] items=tabFloder.getItems();
					for(int k=0;k<items.length;k++){
						if(tabName.equals(items[k].getText())){
						  tabFloder.setSelection(items[k]);  //������Ϊѡ��
						  checked=true;
						}
					}
					if(!checked){
						 CTabItem itemDeployView=new CTabItem(tabFloder,SWT.NONE);
						 itemDeployView.setText(tabName);
						 DeployView deployView=new DeployView(tabFloder,data,versionID);
						 itemDeployView.setControl(deployView.getSachForm());
						 itemDeployView.setData(deployView);
						 tabFloder.setSelection(itemDeployView);
					}
		}
	//���˫�����ÿؼ�������Ļ�ͻ�ԭ
		public class CTabFullScreenAction extends MouseAdapter{
			public void mouseDoubleClick(MouseEvent e){
			  if(!isFolderMaxed){
				  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(comSelf);
				  DeployEditView.getInstance(null).setFolderMaxed(true);
				  WorkAreaView.getInstance(null).getSachForm().layout(true);
			  }else{
				  WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
				  DeployEditView.getInstance(null).setFolderMaxed(false);
				  WorkAreaView.getInstance(null).getSachForm().layout(true);
			  }		
			}
			public void mouseUp(MouseEvent e){
				CTabItem[] items=tabFloder.getItems();
	            int tabCount=items.length;
	            if(tabCount==0) {
	            	WorkAreaView.getInstance(null).getSachForm().setMaximizedControl(null);
					  DeployEditView.getInstance(null).setFolderMaxed(false);
					  WorkAreaView.getInstance(null).getSachForm().layout(true);
	            }
			}
	   }
		//�ر����Tabҳ
/*		public class CloseTabAction extends CTabFolderAdapter{
			public void itemClosed(CTabFolderEvent e){
				CTabItem currItem=(CTabItem)e.item; 
				String type=(String)currItem.getData("TYPE");
				if(!StringUtil.isNullOrEmpty(type)&&"Group".equals(type)){
					SYSTEMBean data=(SYSTEMBean)currItem.getData();
					Deploys.remove(data.getId());
				}
			}
		}*/
/*		public DeployView getDeployView(String groupID){
			if(this.Deploys.containsKey(groupID)){
				return Deploys.get(groupID);
			}
			return null;
		}*/
		
	/*	public boolean existDeployView(String groupid){
			return this.Deploys.containsKey(groupid);
		}
		
		public boolean checkIp(String ip){
			 for (	String groupID : Deploys.keySet()) {
				 	DeployView currentDeployView= Deploys.get(groupID);
				 	boolean ret=currentDeployView.getDiagramView().checkNodeExist(ip);
				 	if(ret){
				 		return true;
				 	}
				}
			 return false;
		}*/
		
		private   CTabFolder tabFloder=null;
		private  DeployView deployView=null;
		private  boolean isFolderMaxed=false;
		
		
}
