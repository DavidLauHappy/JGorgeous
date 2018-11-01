package business.developer.view;

import java.util.ArrayList;
import java.util.List;


import model.TASK;

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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import bean.TaskBean;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import resource.ReqDetailView;
import utils.LayoutUtils;
import views.AppView;


/** 任务浏览器
 * @author Administrator
 *
 */
public class RequirementView  extends Composite{
	  private static  RequirementView unique_instance;
	  private Composite content=null;
	  private Tree tree;
	  
	  public static RequirementView getInstance(Composite com){
		  if(unique_instance==null)
			  unique_instance=new RequirementView(com);
		  return unique_instance;
	  }
	  
	  private RequirementView(Composite parent){
		  super(parent,SWT.NONE);
		  content=this;
		  this.createView();
	  }
	  
	  private void createView(){
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
		    btnRefrsh.setToolTipText(Constants.getStringVaule("btn_tips_refreshReq"));
	    	btnRefrsh.addSelectionListener(new RefreshReqAction());
	    	 Button btnFilter=new Button(toolPanel,SWT.PUSH);
			 btnFilter.setLayoutData(new RowData(18,18));
			 btnFilter.setImage(Icons.getFliterIcon());
			 btnFilter.setToolTipText(Constants.getStringVaule("btn_tips_filterReq"));
			 btnFilter.addSelectionListener(new FliterReqAction());
	    	toolPanel.pack();
	    	
		  tree=new Tree(content,SWT.SINGLE);
		  tree.addMouseListener(new RequirePropertyAction());
		  tree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		  //get all the  requirements
		  List<TaskBean> reqs=TASK.getMyTasks(Context.session.userID);
		  if(reqs!=null&&reqs.size()>0){
			  for(TaskBean req:reqs){
				    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
		        	 treeRoot.setText(req.getShowName());
		        	 treeRoot.setImage(Icons.getFloderIcon());
		        	 treeRoot.setData(req);
			  }
		  }
		  tree.pack();
	  }
	  
	  public void refreshTree(){
		  tree.removeAll();
		  List<TaskBean> reqs=TASK.getMyTasks(Context.session.userID);
		  if(reqs!=null&&reqs.size()>0){
			  for(TaskBean req:reqs){
				    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
		        	 treeRoot.setText(req.getShowName());
		        	 treeRoot.setImage(Icons.getFloderIcon());
		        	 treeRoot.setData(req);
			  }
		  }
		 // tree.pack();
	  }
	  
	  public void fliterTree(String status){
		  tree.removeAll();
		  List<TaskBean> reqs=TASK.getMyTasks(Context.session.userID);
		  if(reqs!=null&&reqs.size()>0){
			  for(TaskBean req:reqs){
				  if("0".equals(status)||req.getStatus().equals(status)){
				    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
		        	 treeRoot.setText(req.getShowName());
		        	 treeRoot.setImage(Icons.getFloderIcon());
		        	 treeRoot.setData(req);
				  }
			  }
		  }
	  }
	  
	  public class RefreshReqAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				refreshTree();
			}
	  }
	  
	  public class RequirePropertyAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){}
	 		 public void mouseDoubleClick(MouseEvent e){
	 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null){
	 				TaskBean req=(TaskBean)currentItem.getData();
	 				String item=req.getTname();
	 				if(!DeveloperEditView.getInstance(null).getTabState(item)){
	 					//判断需求是否在流程中，流程是否开始
	 					ReqDetailView rdv=new ReqDetailView(DeveloperEditView.getInstance(null).getTabFloder(),req);
	 					DeveloperEditView.getInstance(null).setTabItems(rdv.content, item);
	 				}
	 			}
	 		 }
	  }
	  
	  public class FliterReqAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				Button btn=(Button)e.getSource();
				btn.setMenu(null);
				List<Item> status=Dictionary.getDictionaryList("TASK_DEF.STATUS");
				List<Item> AllStatus=new ArrayList<Item>();
				for(Item item:status){
					AllStatus.add(item);
				}
				AllStatus.add(new Item("TASK_DEF.STATUS","0","全部"));
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
}
