package business.dmanager.view;

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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import bean.TaskBean;

import resource.Constants;
import resource.Context;
import resource.Icons;
import resource.ReqDetailView;

import utils.LayoutUtils;
import views.AppView;

public class MyRequirementView  extends Composite{
	 private static  MyRequirementView unique_instance;
	  private Composite content=null;
	  private Tree tree;
	  
	  public static MyRequirementView getInstance(Composite com){
		  if(unique_instance==null)
			  unique_instance=new MyRequirementView(com);
		  return unique_instance;
	  }
	  
	 private MyRequirementView (Composite parent){
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
	    	toolPanel.pack();
	    	
		  tree=new Tree(content,SWT.SINGLE);
		  tree.addMouseListener(new RequirePropertyAction());
		  tree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		  //get all the self-define requirements
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
	 				if(!DManagerEditView.getInstance(null).getTabState(item)){
	 					ReqDetailView rdv=new ReqDetailView(DManagerEditView.getInstance(null).getTabFloder(),req);
	 					DManagerEditView.getInstance(null).setTabItems(rdv.content, item);
	 				}
	 			}
	 		 }
	  }
	  
}
