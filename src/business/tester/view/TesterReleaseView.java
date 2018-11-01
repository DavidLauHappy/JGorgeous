package business.tester.view;

import java.util.ArrayList;
import java.util.List;

import model.TTASK;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import bean.TTaskBean;
import bean.ViewBean;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import utils.LayoutUtils;
import views.AppView;


public class TesterReleaseView  extends Composite{
	  private static TesterReleaseView unique_instance;
	  private Composite content=null;
	  private Tree tree;
	  public static TesterReleaseView getInstance(Composite parent){
		  if(unique_instance==null)
			  unique_instance=new TesterReleaseView(parent);
		  return unique_instance;
	  }
	  
	  private TesterReleaseView(Composite parent){
		  super(parent,SWT.NONE);
		  content=this;
		  this.createTree();
	  }
	  
	  private void createTree(){
		  Composite toolPanel=new Composite(content,SWT.NONE);
		  toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 20));
		  RowLayout layout=new RowLayout();
			layout.type=SWT.HORIZONTAL;
			layout.marginLeft=1;
			layout.marginTop=1;
			layout.marginRight=1;
			layout.marginBottom=1;
			layout.spacing=5;
			toolPanel.setLayout(layout);
			toolPanel.setBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			Button btnRefrsh= new Button(toolPanel,SWT.PUSH);
		    btnRefrsh.setLayoutData(new RowData(18,18));
		    btnRefrsh.setImage(Icons.getRefreshIcon());
		    btnRefrsh.addSelectionListener(new RefreshViewAction());
		    btnRefrsh.setToolTipText(Constants.getStringVaule("btn_tips_refreshrelease"));
		    Button btnFilter=new Button(toolPanel,SWT.PUSH);
		    btnFilter.setLayoutData(new RowData(18,18));
		    btnFilter.setImage(Icons.getFliterIcon());
		    btnFilter.setToolTipText(Constants.getStringVaule("btn_tips_filterReq"));
		    btnFilter.addSelectionListener(new FliterViewAction());
			toolPanel.pack();
			
		  tree=new Tree(content,SWT.NONE|SWT.SINGLE);//
		  tree.addMouseListener(new PopMenuAction());
		  tree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		  //取所有的测试任务
		  List<TTaskBean>  releases=TTASK.getMyTasks(Context.session.userID);
		  for(TTaskBean bean:releases){
			  	ViewBean view=bean.getView();
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getViewName());
	        	 if(bean.getStatus().equals(TTaskBean.Status.Done.ordinal()+"")){
	        		 treeRoot.setImage(Icons.getViewIcon());//全部都是本人的测试任务
	        	 }else{
	        		 if(bean.getStatus().equals(TTaskBean.Status.Todo.ordinal()+"")&&
	        			(ViewBean.ReleaseStatus.Organized.ordinal()+"").equals(view.getUptFlag()) ){
	        			  	treeRoot.setImage(Icons.getMyViewIcon());
	        		 }else{
	        			 treeRoot.setImage(Icons.getViewIcon());
	        		 }
	        	 }
	        	 treeRoot.setData(bean);
		  }
		  tree.pack();
	  }
	  
	  public void refreshTree(){
		  tree.removeAll();
		  List<TTaskBean>  releases=TTASK.getMyTasks(Context.session.userID);
		  for(TTaskBean bean:releases){
				ViewBean view=bean.getView();
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getViewName());
	        	 if(bean.getStatus().equals(TTaskBean.Status.Done.ordinal()+"")){
	        		 treeRoot.setImage(Icons.getViewIcon());//全部都是本人的测试任务
	        	 }
	        	 else{
	        		 if(bean.getStatus().equals(TTaskBean.Status.Todo.ordinal()+"")&&
	 	        			(ViewBean.ReleaseStatus.Organized.ordinal()+"").equals(view.getUptFlag()) ){
	        			  treeRoot.setImage(Icons.getMyViewIcon());
	        		 }else{
	        			 treeRoot.setImage(Icons.getViewIcon());
	        		 }
	        	 }
	        	 treeRoot.setData(bean);
		  }
		  //tree.pack();
	  }
	  
	  
	  public  void fliterTree(String status){
		  tree.removeAll();
		  List<TTaskBean>  releases=TTASK.getMyTasks(Context.session.userID);
		  for(TTaskBean bean:releases){
			  ViewBean view=bean.getView();
			    if(status.equals(bean.getStatus())||"0".equals(status)){
					     TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
			        	 treeRoot.setText(bean.getViewName());
			        	 if(bean.getStatus().equals(TTaskBean.Status.Done.ordinal()+"")){
			        		 treeRoot.setImage(Icons.getFloderDelIcon());
			        	 }else{
			        		 if(bean.getStatus().equals(TTaskBean.Status.Todo.ordinal()+"")&&
				 	        	(ViewBean.ReleaseStatus.Organized.ordinal()+"").equals(view.getUptFlag()) ){
			        			  treeRoot.setImage(Icons.getMyViewIcon());
			        		 }else{
			        			 treeRoot.setImage(Icons.getViewIcon());
			        		 }
			        	 }
			        treeRoot.setData(bean);
			    }
		  }
	  }
	  public class RefreshViewAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				refreshTree();
			}
	  }
	  
	  public class FliterViewAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				Button btn=(Button)e.getSource();
				btn.setMenu(null);
				List<Item> status=Dictionary.getDictionaryList("TTASK_DEF.STATUS");
				List<Item> AllStatus=new ArrayList<Item>();
				for(Item item:status){
					AllStatus.add(item);
				}
				AllStatus.add(new Item("TTASK_DEF.STATUS","0","全部"));
				Menu menu=getFilterMenu(AllStatus);
				btn.setMenu(menu);
				menu.setVisible(true);
			}
	  }
	  
	  public String currentViewStatus="0";
	  public  Menu getFilterMenu(List<Item> status){
	    	Menu fliterMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
	    	for(Item item:status ){
		    		MenuItem itemStatus=new MenuItem(fliterMenu,SWT.RADIO);
		    		itemStatus.setText(item.getValue());
		    		itemStatus.setData(item.getKey());
		    		if(item.getKey().equals(currentViewStatus)){
		    			itemStatus.setSelection(true);
		    		}
		    		itemStatus.addSelectionListener(new SelectionAdapter(){ 	
				  		 public void widgetSelected(SelectionEvent e){
				  			MenuItem item=(MenuItem)e.getSource();
				  			String status=(String)item.getData();
				  			currentViewStatus=status;
				  			fliterTree(status);
				  		 }
					 });
	    	}
       return fliterMenu;
  }

	  public class PopMenuAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){
	 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null&&e.button==3){
	 				ViewBean data=(ViewBean)currentItem.getData();
	 				tree.setMenu(TesterReleaseView.getInstance(null).getVersionMenu(data));
	 			}else{
	 				tree.setMenu(null);
	 			}
	 		}
	 		
	 		 public void mouseDoubleClick(MouseEvent e){
		 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
		 			if(currentItem!=null){
		 				TTaskBean data=(TTaskBean)currentItem.getData();
		 				ViewBean view=data.getView();
	    			 	String status=view.getStatus();
	    			 	if(status.equals(ViewBean.Status.Lock.ordinal()+"")){
	    			 		 String msg="发布【"+data.getViewName()+"】已被锁定！请解锁后重试！";
	 						 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
	 						 box.setText(Constants.getStringVaule("messagebox_alert"));
	 						 box.setMessage(msg);
	 						 box.open();
	    			 	}else{
	        			 	String itemName=data.getViewName();
	        	   			if(!TesterEditView.getInstance(null).getTabState(itemName)){
	        	   				TesterReleaseInfoView releaseView=new TesterReleaseInfoView(TesterEditView.getInstance(null).getTabFloder(),data);
	        	   				TesterEditView.getInstance(null).setTabItems(releaseView.self, itemName);
	        	   			}
	    			 	}
		 			}
	 		 }
	  
	  }
	  
	  public  Menu getVersionMenu(ViewBean obj){
	    	 Menu versionMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
	    	 MenuItem itemView=new MenuItem(versionMenu,SWT.PUSH);
	    	 itemView.setText("详情");
	    	 itemView.setData(obj);
	    	 itemView.setImage(Icons.getDetailIcon());
	    	 itemView.addSelectionListener(new SelectionAdapter(){ 	
    		 public void widgetSelected(SelectionEvent e){
    			 	MenuItem item=(MenuItem)e.getSource();
    			 	TTaskBean data=(TTaskBean)item.getData();
    			 	ViewBean view=data.getView();
    			 	String status=view.getStatus();
    			 	if(status.equals(ViewBean.Status.Lock.ordinal()+"")){
    			 		 String msg="发布【"+data.getViewName()+"】已被锁定！请解锁后重试！";
 						 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
 						 box.setText(Constants.getStringVaule("messagebox_alert"));
 						 box.setMessage(msg);
 						 box.open();
    			 	}else{
        			 	String itemName=data.getViewName();
        	   			if(!TesterEditView.getInstance(null).getTabState(itemName)){
        	   				TesterReleaseInfoView releaseView=new TesterReleaseInfoView(TesterEditView.getInstance(null).getTabFloder(),data);
        	   				TesterEditView.getInstance(null).setTabItems(releaseView.self, itemName);
        	   			}
    			 	}
    		 }
	    	 });
	    	 
	    	 MenuItem itemViewHis=new MenuItem(versionMenu,SWT.PUSH);
	    	 itemViewHis.setText("历史版本");
	    	 itemViewHis.setData(obj);
	    	 itemViewHis.setImage(Icons.getHistoryIcon());
	    	 itemViewHis.addSelectionListener(new SelectionAdapter(){ 	
    		 public void widgetSelected(SelectionEvent e){
    			 	MenuItem item=(MenuItem)e.getSource();
    			 	TTaskBean bean=(TTaskBean)item.getData();
    			 	ViewBean data=bean.getView();
    			 	String status=data.getStatus();
    			 	if(status.equals(ViewBean.Status.Lock.ordinal()+"")){
    			 		 String msg="发布【"+data.getViewName()+"】已被锁定！请解锁后重试！";
 						 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
 						 box.setText(Constants.getStringVaule("messagebox_alert"));
 						 box.setMessage(msg);
 						 box.open();
    			 	}else{
	        			 	String itemName=data.getViewName()+"版本树";
	        			 	if(!TesterEditView.getInstance(null).getTabState(itemName)){
	        			 		TesterReleaseVersionView rvview=new TesterReleaseVersionView(TesterEditView.getInstance(null).getTabFloder(),data);
	        			 		TesterEditView.getInstance(null).setTabItems(rvview.content, itemName);
	        			 	}
    			 	}
    		 }
	    	 });
	    	 
	    	 return versionMenu;
	  }


}
