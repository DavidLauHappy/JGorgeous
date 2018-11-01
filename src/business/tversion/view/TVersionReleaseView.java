package business.tversion.view;

import java.util.ArrayList;
import java.util.List;

import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import bean.ViewBean;


import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;


public class TVersionReleaseView  extends Composite{
	  private static TVersionReleaseView unique_instance;
	  private Composite content=null;
	  private Tree tree;
	  public Button btnShow;
	  public static TVersionReleaseView getInstance(Composite parent){
		  if(unique_instance==null)
			  unique_instance=new TVersionReleaseView(parent);
		  return unique_instance;
	  }
	  
	  private TVersionReleaseView(Composite parent){
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
		    btnShow=new Button(toolPanel,SWT.PUSH);
			btnShow.setImage(Icons.getVisableIcon());
			btnShow.setLayoutData(new RowData(18,18));
			btnShow.setToolTipText(Constants.getStringVaule("btn_tips_showClosed"));
			btnShow.addSelectionListener(new FilterShowAction());
			Button btnRefrsh= new Button(toolPanel,SWT.PUSH);
		    btnRefrsh.setLayoutData(new RowData(18,18));
		    btnRefrsh.setImage(Icons.getRefreshIcon());
		    btnRefrsh.addSelectionListener(new RefreshViewAction());
		    btnRefrsh.setToolTipText(Constants.getStringVaule("btn_tips_refreshReq"));
		    Button btnFilter=new Button(toolPanel,SWT.PUSH);
		    btnFilter.setLayoutData(new RowData(18,18));
		    btnFilter.setImage(Icons.getFliterIcon());
		    btnFilter.setToolTipText(Constants.getStringVaule("btn_tips_filterReq"));
		    btnFilter.addSelectionListener(new FliterViewAction());
		    toolPanel.pack();
		   tree=new Tree(content,SWT.SINGLE);//
		 // tree.addSelectionListener(new ShowReleaseAction());
		   tree.addMouseListener(new PopMenuAction());
		   tree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		  //取所有的发布
		  List<ViewBean>  releases=VIEW.getViews(); 
		  for(ViewBean bean:releases){
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getViewName());
	        	 if(bean.getStatus().equals(ViewBean.Status.Close.ordinal()+"")){
	        		 treeRoot.setImage(Icons.getFloderDelIcon());
	        	 }else{
	        		 if(Context.session.userID.equals(bean.getCurrentUserID())){
	        			  treeRoot.setImage(Icons.getMyViewIcon());
	        		 }
	        		 else{
	        			 treeRoot.setImage(Icons.getViewIcon());
	        		 }
	        	 }
	        	 treeRoot.setData(bean);
		  }
		  tree.pack();
	  }
	  
	  
	  public  GridData getToolsLayoutData(){
			GridData griddata_row=new GridData();
			griddata_row.horizontalAlignment=SWT.RIGHT;
			griddata_row.verticalAlignment=SWT.FILL;
			griddata_row.horizontalSpan=1;//水平占据单元格数目
			griddata_row.verticalSpan=1;//垂直占据单元格数目
			griddata_row.widthHint=16;
			griddata_row.heightHint=16;
			//垂直方向上布局的误差
			griddata_row.verticalIndent=-5;
			griddata_row.grabExcessHorizontalSpace=true;//水平方向是否放大
			griddata_row.grabExcessVerticalSpace=false;//垂直方向是否放大
			return griddata_row;
		}
	  
	  public void refreshTree(){
		  tree.removeAll();
		  List<ViewBean>  releases=null;
		  if(!showClosed){
			  releases=VIEW.getViews(); 
		  }else{
			  releases=VIEW.getAllViews();
		  }
		  for(ViewBean bean:releases){
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getViewName());
	        	 if(bean.getStatus().equals(ViewBean.Status.Close.ordinal()+"")){
	        		 treeRoot.setImage(Icons.getFloderDelIcon());
	        	 }else{
	        		 if(Context.session.userID.equals(bean.getCurrentUserID())){
	        			  treeRoot.setImage(Icons.getMyViewIcon());
	        		 }else{
	        			 treeRoot.setImage(Icons.getViewIcon());
	        		 }
	        	 }
	        	 treeRoot.setData(bean);
		  }
	  }
	  
	  public  void fliterTree(String status){
		  tree.removeAll();
		  List<ViewBean>  releases=null;
		  if(!showClosed){
			  releases=VIEW.getViews(); 
		  }else{
			  releases=VIEW.getAllViews();
		  }
		  
		  for(ViewBean bean:releases){
			  if(!StringUtil.isNullOrEmpty(bean.getProgress())){
				   if("0".equals(status)||bean.getProgress().equals(status)){
					     TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
			        	 treeRoot.setText(bean.getViewName());
			        	 if(bean.getStatus().equals(ViewBean.Status.Close.ordinal()+"")){
			        		 treeRoot.setImage(Icons.getFloderDelIcon());
			        	 }else{
			        		 if(Context.session.userID.equals(bean.getCurrentUserID())){
			        			  treeRoot.setImage(Icons.getMyViewIcon());
			        		 }else{
			        			 treeRoot.setImage(Icons.getViewIcon());
			        		 }
			        	 }
			        	 treeRoot.setData(bean);
				   	}
			   }
		  }
	  }
	  
	  
	  public class ShowReleaseAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			//根据权限，来编辑或查看发布的详情
	   			TreeItem currentItem=(TreeItem)e.item;
	   			ViewBean data=(ViewBean)currentItem.getData();
	   			String item=data.getViewName();
	   			if(!TVersionEditView.getInstance(null).getTabState(item)){
	   				TVersionReleaseInfoView releaseView=new TVersionReleaseInfoView(TVersionEditView.getInstance(null).getTabFloder(),data);
	   				TVersionEditView.getInstance(null).setTabItems(releaseView.self, item);
	   			}
	   			
	   		}
	  }
	  
	  public boolean showClosed=false;
	  public class FilterShowAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   		 List<ViewBean>  releases=null;
	   			if(!showClosed){
	   				btnShow.setToolTipText(Constants.getStringVaule("btn_tips_hideClosed"));
	   				btnShow.setImage(Icons.getInvisableIcon());
	   				releases=VIEW.getAllViews(); 
	   				showClosed=true;
	   			}else{
	   				btnShow.setToolTipText(Constants.getStringVaule("btn_tips_showClosed"));
	   				btnShow.setImage(Icons.getVisableIcon());
	   				releases=VIEW.getViews();
	   				showClosed=false;
	   			}
	   		 tree.removeAll();
			  for(ViewBean bean:releases){
				    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
		        	 treeRoot.setText(bean.getViewName());
		        	 if(bean.getStatus().equals(ViewBean.Status.Close.ordinal()+"")){
		        		 treeRoot.setImage(Icons.getFloderDelIcon());
		        	 }else{
		        		 treeRoot.setImage(Icons.getFloderIcon());
		        	 }
		        	 treeRoot.setData(bean);
			  }
	   		}
	  }
	  
	  public class PopMenuAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){
	 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null&&e.button==3){
	 				ViewBean data=(ViewBean)currentItem.getData();
	 				tree.setMenu(TVersionReleaseView.getInstance(null).getVersionMenu(data));
	 			}else{
	 				tree.setMenu(null);
	 			}
	 		}
	 		 public void mouseDoubleClick(MouseEvent e){
		 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
		 			if(currentItem!=null){
		 			 	ViewBean data=(ViewBean)currentItem.getData();
	      			 	String status=data.getStatus();
	      			 	if(status.equals(ViewBean.Status.Close.ordinal()+"")){
	      			 		 String msg="发布【"+data.getViewName()+"】已被关闭！无法使用！";
	   						 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
	   						 box.setText(Constants.getStringVaule("messagebox_alert"));
	   						 box.setMessage(msg);
	   						 box.open();
	      			 	}else{
	          			 	String itemName=data.getViewName();
	          	   			if(!TVersionEditView.getInstance(null).getTabState(itemName)){
	          	   				TVersionReleaseInfoView releaseView=new TVersionReleaseInfoView(TVersionEditView.getInstance(null).getTabFloder(),data);
	          	   				TVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
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
      			 	ViewBean data=(ViewBean)item.getData();
      			 	String status=data.getStatus();
      			 	if(status.equals(ViewBean.Status.Close.ordinal()+"")){
      			 		 String msg="发布【"+data.getViewName()+"】已被关闭！无法使用！";
   						 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
   						 box.setText(Constants.getStringVaule("messagebox_alert"));
   						 box.setMessage(msg);
   						 box.open();
      			 	}else{
          			 	String itemName=data.getViewName();
          	   			if(!TVersionEditView.getInstance(null).getTabState(itemName)){
          	   				TVersionReleaseInfoView releaseView=new TVersionReleaseInfoView(TVersionEditView.getInstance(null).getTabFloder(),data);
          	   				TVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
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
      			 	ViewBean data=(ViewBean)item.getData();
      			 	String status=data.getStatus();
      			 	if(status.equals(ViewBean.Status.Close.ordinal()+"")){
      			 		 String msg="发布【"+data.getViewName()+"】已被关闭！无法使用！";
   						 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
   						 box.setText(Constants.getStringVaule("messagebox_alert"));
   						 box.setMessage(msg);
   						 box.open();
      			 	}else{
	        			 	String itemName=data.getViewName()+"版本树";
	        			 	if(!TVersionEditView.getInstance(null).getTabState(itemName)){
	        			 		TReleaseVersionView rvview=new TReleaseVersionView(TVersionEditView.getInstance(null).getTabFloder(),data);
	        			 		TVersionEditView.getInstance(null).setTabItems(rvview.content, itemName);
	        			 	}
      			 	}
      		 }
	    	 });
	    	 
	    	 MenuItem itemViewUnlock=new MenuItem(versionMenu,SWT.PUSH);
	    	 itemViewUnlock.setText("解锁");
	    	 itemViewUnlock.setData(obj);
	    	 itemViewUnlock.setImage(Icons.getActiveIcon());
	    	 itemViewUnlock.addSelectionListener(new SelectionAdapter(){ 	
      		 public void widgetSelected(SelectionEvent e){
      			 	MenuItem item=(MenuItem)e.getSource();
      			 	ViewBean data=(ViewBean)item.getData();
      			 	data.lockStatus(ViewBean.Status.Normal.ordinal()+"");
      			 	MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					 box.setText(Constants.getStringVaule("alert_successoperate"));
					 box.setMessage(Constants.getStringVaule("alert_successoperate"));
					 box.open();
      		 }
	    	 });
	    	 
	    	 MenuItem itemViewLock=new MenuItem(versionMenu,SWT.PUSH);
	    	 itemViewLock.setText("锁定");
	    	 itemViewLock.setData(obj);
	    	 itemViewLock.setImage(Icons.getLockIcon());
	    	 itemViewLock.addSelectionListener(new SelectionAdapter(){ 	
      		 public void widgetSelected(SelectionEvent e){
      			 	MenuItem item=(MenuItem)e.getSource();
      			 	ViewBean data=(ViewBean)item.getData();
      			 	data.lockStatus(ViewBean.Status.Lock.ordinal()+"");
      			 	MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					 box.setText(Constants.getStringVaule("alert_successoperate"));
					 box.setMessage(Constants.getStringVaule("alert_successoperate"));
					 box.open();
      		 }
	    	 });
	    	 
	    	 MenuItem itemViewClose=new MenuItem(versionMenu,SWT.PUSH);
		  	 itemViewClose.setText("关闭发布");
		  	 itemViewClose.setData(obj);
		   	 itemViewClose.setImage(Icons.getDeleteIcon());
		  	 itemViewClose.addSelectionListener(new SelectionAdapter(){ 	
		  		 public void widgetSelected(SelectionEvent e){
		  			MenuItem item=(MenuItem)e.getSource();
		  			ViewBean data=(ViewBean)item.getData();
      			 	data.lockStatus(ViewBean.Status.Close.ordinal()+"");
      			 	refreshTree();
      			 	MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					 box.setText(Constants.getStringVaule("alert_successoperate"));
					 box.setMessage(Constants.getStringVaule("alert_successoperate"));
					 box.open();
		  		 }
			 });
	    	 return versionMenu;
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
				List<Item> status=Dictionary.getDictionaryList("REQUIREMENT.PROGRESS");
				List<Item> AllStatus=new ArrayList<Item>();
				for(Item item:status){
					AllStatus.add(item);
				}
				AllStatus.add(new Item("REQUIREMENT.PROGRESS","0","全部"));
				Menu menu=getFilterMenu(AllStatus);
				btn.setMenu(menu);
				menu.setVisible(true);
			}
	  }
	  
	  public String currentViewStatus="0";
	  public  Menu getFilterMenu(List<Item> status){
		    	Menu fliterMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
		    	for(Item item:status ){
		    		if(!item.getKey().equals("1")&&!item.getKey().equals("2")&&!item.getKey().equals("3")){
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
		    	}
	     return fliterMenu;
	  }

}
