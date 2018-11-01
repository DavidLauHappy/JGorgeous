package resource;

import java.util.List;

import model.LOCALNODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import bean.LOCALNODEBean;

import utils.LayoutUtils;
import views.AppView;

public class BackDbView {
	private Composite parent;
	public Composite content;
	
	
	private static BackDbView unique_instance;
	public static BackDbView getInstance(Composite com){
		if(unique_instance==null)
			unique_instance=new BackDbView(com);
		return unique_instance;
	}
	
	
	private  BackDbView(Composite com){
		this.parent=com;
		content=new  Composite(com,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		this.createAndShow();
		content.pack();
	}
	
	private void createAndShow(){
			this.createToolsView();
			this.createTabFloder();
	}
	
	private void createToolsView(){
		Composite toolPanel=new Composite(content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(15, 1)); 
		Button btnNew =new  Button(toolPanel,SWT.PUSH);
		btnNew.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.TOP, true, false, 1, 1, 0, 0));
		btnNew.setToolTipText(Constants.getStringVaule("btn_tips_add"));
		btnNew.setImage(Icons.getAddDbIcon());
		btnNew.addSelectionListener(new OpenDbAction());
		
		
		Button btnOpen =new  Button(toolPanel,SWT.PUSH);
		btnOpen.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.TOP, true, false, 1, 1, 0, 0));
		btnOpen.setToolTipText(Constants.getStringVaule("btn_tips_open"));
		btnOpen.setImage(Icons.getOpenDbIcon());
		btnOpen.addSelectionListener(new OpenDbMenuAction());
		toolPanel.pack();
	}
	
	private   CTabFolder tabFloder=null;
	private void createTabFloder(){
		Composite com=new Composite(content,SWT.NONE);
		com.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		com.setLayout(LayoutUtils.getComGridLayout(1, 1));
		tabFloder=new CTabFolder(com,SWT.TOP|SWT.BORDER);//|SWT.CLOSE|
		tabFloder.setLayoutData(LayoutUtils.getCommomGridData());
		tabFloder.setMaximizeVisible(true);
		tabFloder.setMinimizeVisible(true);  
		tabFloder.setSimple(false);
		tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		tabFloder.setTabHeight(20);
		tabFloder.pack();
		com.pack();
	}
	
	public  void  setTabItems(LOCALNODEBean node,Composite com){
		 CTabItem new_item=new CTabItem(tabFloder,SWT.NONE);
		 new_item.setText(node.getName());
		 new_item.setControl(com);
		 new_item.setToolTipText(node.getIp());
		 new_item.setData(node);
		 com.pack();
		 com.layout(true);
		 tabFloder.setSelection(new_item);
		 tabFloder.layout(true);
	}
	
	  public String currentDbID="";
	  public class OpenDbMenuAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			 List<LOCALNODEBean> dbs=LOCALNODE.getMyDbNodes(Context.session.userID);
	   			Button btn=(Button)e.getSource();
				btn.setMenu(null);
				if(dbs!=null&&dbs.size()>0){
					Menu nodeMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
					for(LOCALNODEBean db:dbs){
						MenuItem itemDb=new MenuItem(nodeMenu,SWT.RADIO);
						itemDb.setText(db.getIp()+"("+db.getName()+")");
						itemDb.setData(db);
						if(db.getId().equals(currentDbID)){
							itemDb.setSelection(true);
						}
						itemDb.addSelectionListener(new SelectionAdapter(){ 	
					  		 public void widgetSelected(SelectionEvent e){
							  			 MenuItem item=(MenuItem)e.getSource();
							  			 LOCALNODEBean data=(LOCALNODEBean)item.getData();
							  			 currentDbID=data.getId();
							  			String name=data.getName();
							  			if(!getTabState(name)){
								  			 BackDbObjView bdov=new BackDbObjView(tabFloder,data);
								  			 setTabItems(data,bdov.content);
							  			}
					  		}
						 });
					}
					btn.setMenu(nodeMenu);
					nodeMenu.setVisible(true);
				}
	   		}
	  }
	  
	  
	  public class OpenDbAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			DbNodeView dnv=new DbNodeView(AppView.getInstance().getCentreScreenPoint());
	   			dnv.show();
	   		}
	  }
	  
	  
		public  boolean getTabState(String item){
			CTabItem[] items=tabFloder.getItems();
			for(int k=0;k<items.length;k++){
				if(item.equals(items[k].getText())){
				  tabFloder.setSelection(items[k]);    
				  return true;
				}
			}
			return false;
		}
	  

}
