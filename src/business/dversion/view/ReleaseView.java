package business.dversion.view;

import java.util.ArrayList;
import java.util.List;

import model.STREAM;
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

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;

import bean.StreamBean;
import bean.TaskBean;
import bean.VStep;
import bean.ViewBean;
import business.developer.view.RequirementView.FliterReqAction;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;



public class ReleaseView extends Composite{
	  private static ReleaseView unique_instance;
	  private Composite content=null;
	  private Tree tree;
	  public static ReleaseView getInstance(Composite parent){
		  if(unique_instance==null)
			  unique_instance=new ReleaseView(parent);
		  return unique_instance;
	  }
	  
	  private ReleaseView(Composite parent){
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
			  if(!StringUtil.isNullOrEmpty(bean.getProgress())){
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getViewName());
	        	 if(Context.session.userID.equals(bean.getCurrentUserID())||
	        			 (ViewBean.ReleaseStatus.Organize.ordinal()+"").equals(bean.getUptFlag())){
	       			  	treeRoot.setImage(Icons.getMyViewIcon());
			       }else{
			       		treeRoot.setImage(Icons.getViewIcon());
			        }
	        	 treeRoot.setData(bean);
			  }
		  }
		  tree.pack();
	  }
	  
	  public void refreshTree(){
		  tree.removeAll();
		  List<ViewBean>  releases=VIEW.getViews();
		  for(ViewBean bean:releases){
			  if(!StringUtil.isNullOrEmpty(bean.getProgress())){
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getViewName());
	        	 if(Context.session.userID.equals(bean.getCurrentUserID())||
	        		(ViewBean.ReleaseStatus.Organize.ordinal()+"").equals(bean.getUptFlag())){
       			  	treeRoot.setImage(Icons.getMyViewIcon());
		       		 }else{
		       			 treeRoot.setImage(Icons.getViewIcon());
		       		 }
	        	 treeRoot.setData(bean);
			  }
		  }
		  //tree.pack();
	  }
	  
	  public void fliterTree(String status){
		  tree.removeAll();
		  List<ViewBean>  releases=VIEW.getViews();
		  for(ViewBean bean:releases){
			  if(!StringUtil.isNullOrEmpty(bean.getProgress())){
				  if("-1".equals(status)||bean.getProgress().equals(status)){
					 TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
		        	 treeRoot.setText(bean.getViewName());
		        	 if(Context.session.userID.equals(bean.getCurrentUserID())||
		        		(ViewBean.ReleaseStatus.Organize.ordinal()+"").equals(bean.getUptFlag())){
	        			  treeRoot.setImage(Icons.getMyViewIcon());
	        		 }else{
	        			 treeRoot.setImage(Icons.getViewIcon());
	        		 }
		        	 treeRoot.setData(bean);
				  }
			  }
		  }
		  //tree.pack();
	  }
	  
	  
	  public class ShowReleaseAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			//根据权限，来编辑或查看发布的详情
	   			TreeItem currentItem=(TreeItem)e.item;
	   			ViewBean data=(ViewBean)currentItem.getData();
	   			String item=data.getViewName();
	   			if(!DVersionEditView.getInstance(null).getTabState(item)){
	   				ReleaseInfoView releaseView=new ReleaseInfoView(DVersionEditView.getInstance(null).getTabFloder(),ReleaseInfoView.Type.View,data);
	   				DVersionEditView.getInstance(null).setTabItems(releaseView.self, item);
	   			}
	   			
	   		}
	  }
	  public class PopMenuAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){
	 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null&&e.button==3){
	 				ViewBean data=(ViewBean)currentItem.getData();
	 				tree.setMenu(ReleaseView.getInstance(null).getVersionMenu(data));
	 			}else{
	 				tree.setMenu(null);
	 			}
	 		}
	 		 public void mouseDoubleClick(MouseEvent e){
		 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
		 			if(currentItem!=null){
		 				ViewBean data=(ViewBean)currentItem.getData();
        			 	String streamID=data.getStreamID();
        			 	StreamBean stream=STREAM.getStreamByID(streamID);
        			 	String steamStatus=stream.getStatus();
        			 	String status=data.getStatus();
        			 	String progress=data.getProgress();
        			 	//只有当流未锁定/视图未锁定/当前版本为'待版本整合'才能进行编辑
        			 	if(status.equals(ViewBean.Status.Normal.ordinal()+"")&&
        			 	   steamStatus.equals("doing")&&
        			 	  ((progress.equals(VStep.VersionMake)&&Context.session.userID.equals(data.getCurrentUserID()))||
               			       (ViewBean.ReleaseStatus.Organize.ordinal()+"").equals(data.getUptFlag()))
        			 			){
        			 		String itemName=data.getViewName();
            	   			if(!DVersionEditView.getInstance(null).getTabState(itemName)){
            	   				ReleaseInfoView releaseView=new ReleaseInfoView(DVersionEditView.getInstance(null).getTabFloder(),ReleaseInfoView.Type.Edit,data);
            	   				DVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
            	   			}
        			 	}else{
        			 		String itemName=data.getViewName();
            	   			if(!DVersionEditView.getInstance(null).getTabState(itemName)){
            	   				ReleaseInfoView releaseView=new ReleaseInfoView(DVersionEditView.getInstance(null).getTabFloder(),ReleaseInfoView.Type.View,data);
            	   				DVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
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
        			 	String streamID=data.getStreamID();
        			 	StreamBean stream=STREAM.getStreamByID(streamID);
        			 	String steamStatus=stream.getStatus();
        			 	String status=data.getStatus();
        			 	String progress=data.getProgress();
        			 	//只有当流未锁定/视图未锁定/当前版本为'待版本整合'才能进行编辑
        			 	if(status.equals(ViewBean.Status.Normal.ordinal()+"")&&
        			 	   steamStatus.equals("doing")&&
        			 	  ((progress.equals(VStep.VersionMake)&&Context.session.userID.equals(data.getCurrentUserID()))||
        			       (ViewBean.ReleaseStatus.Organize.ordinal()+"").equals(data.getUptFlag()))
        			 			){
        			 		String itemName=data.getViewName();
            	   			if(!DVersionEditView.getInstance(null).getTabState(itemName)){
            	   				ReleaseInfoView releaseView=new ReleaseInfoView(DVersionEditView.getInstance(null).getTabFloder(),ReleaseInfoView.Type.Edit,data);
            	   				DVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
            	   			}
        			 	}else{
        			 		String itemName=data.getViewName();
            	   			if(!DVersionEditView.getInstance(null).getTabState(itemName)){
            	   				ReleaseInfoView releaseView=new ReleaseInfoView(DVersionEditView.getInstance(null).getTabFloder(),ReleaseInfoView.Type.View,data);
            	   				DVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
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
        			 	String itemName=data.getViewName()+"版本树";
        			 	if(!DVersionEditView.getInstance(null).getTabState(itemName)){
        			 		ReleaseVersionView rvview=new ReleaseVersionView(DVersionEditView.getInstance(null).getTabFloder(),data);
        			 		DVersionEditView.getInstance(null).setTabItems(rvview.content, itemName);
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
	    	 return versionMenu;
	  }
	  
	  public class FliterViewAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				Button btn=(Button)e.getSource();
				btn.setMenu(null);
				List<Item> status=Dictionary.getDictionaryList("TASK_DEF.UPT_FLAG");
				List<Item> AllStatus=new ArrayList<Item>();
				for(Item item:status){
					AllStatus.add(item);
				}
				AllStatus.add(new Item("TASK_DEF.UPT_FLAG","-1","全部"));
				Menu menu=getFilterMenu(AllStatus);
				btn.setMenu(menu);
				menu.setVisible(true);
			}
	  }
	  
	  public String currentViewStatus="2";
	  public  Menu getFilterMenu(List<Item> status){
	    	Menu fliterMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
	    	for(Item item:status ){
	    		if(!item.getKey().equals("1")&&!item.getKey().equals("2")&&!item.getKey().equals("3")&&!item.getKey().equals("5")){
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
