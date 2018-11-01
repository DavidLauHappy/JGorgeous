package business.tester.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import model.TASK;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Item;
import resource.ReqDetailView;
import resource.UserChoose;
import utils.LayoutUtils;
import utils.StringUtil;
import bean.TaskBean;
import business.tester.action.ChooseUserAction;


public class TesterReqSearchView {
	private Composite parent;
	public Composite content;
	public Text text=null;
	public Button btnMore=null;
	public Table resultTable=null;
	public Map<String,String> Apps=Dictionary.getDictionaryMap("APP");
	public Map<String,String> Process=Dictionary.getDictionaryMap("REQUIREMENT.PROGRESS");
	public String[] header=new String[]{Constants.getStringVaule("header_crtdate"),
															Constants.getStringVaule("label_reqno"),
															Constants.getStringVaule("label_taskdowner"),
															Constants.getStringVaule("label_reqtitle"),
															Constants.getStringVaule("header_reqsno"),
															Constants.getStringVaule("header_reqstatus"),
															Constants.getStringVaule("header_reqsystem"),
															Constants.getStringVaule("header_reqprocessor")
															};
	//·­Ò³²éÑ¯ÒªËØ
	public static  int PageCount=100;
	public int allCount=0;
	public int currentNum=0;
	
	public TesterReqSearchView(Composite com){
		this.parent=com;
		content=new  Composite(com,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		this.allCount=TASK.getAllReqCount();
		this.createAndShow();
		content.pack();
	}
	
	private void createAndShow(){
		Composite toolPanel=new Composite(content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(10, 5)); 
		
		text=new Text(toolPanel,SWT.BORDER);
		text.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 8, 1, 0, 0));
		Button btnSearch=new Button(toolPanel,SWT.PUSH);
		btnSearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
		btnSearch.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnSearch.addSelectionListener(new SearchAction());
		
		btnMore=new Button(toolPanel,SWT.PUSH);
		btnMore.setText("   "+Constants.getStringVaule("btn_more")+"   ");
		btnMore.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnMore.addSelectionListener(new QueryMoreAction());
		btnMore.setVisible(false);
		if(allCount>PageCount){
			btnMore.setVisible(true);
		}
		toolPanel.pack();
		
		resultTable=new Table(content,SWT.BORDER|SWT.FULL_SELECTION);
		resultTable.setHeaderVisible(true);
		resultTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		resultTable.setLinesVisible(true);
		resultTable.addMouseListener(new ShowReqDetailAction());
		resultTable.addListener(SWT.MeasureItem, new Listener(){
			public void handleEvent(Event e){
				e.height=20;
			}
		});
		for(int i=0;i<header.length;i++){
			TableColumn tablecolumn=new TableColumn(resultTable,SWT.BORDER);
			tablecolumn.setText(header[i]);
			tablecolumn.setMoveable(true);
		}
		
		List<TaskBean> reqs=TASK.getAllTopTasks(currentNum+"",currentNum+PageCount+"");
		 if(reqs!=null&&reqs.size()>0){
			 for(TaskBean req:reqs){
				 String appName=Apps.get(req.getApp());
				 String step=Process.get(req.getStatus());
				 String currUser=req.getCurrentUserName()+"("+req.getCurrentUser()+")";
				 TableItem tableItem=new TableItem(resultTable,SWT.BORDER);
     	          tableItem.setText(new String[]{req.getCrtTime(),req.getReqID(),req.getOwnerShowName(),req.getTname(),req.getId(),step, appName, currUser}); 
     	          tableItem.setData(req);
			 }
		 }
		for(int j=0;j<header.length;j++){		
			resultTable.getColumn(j).pack();
		}	
		resultTable.pack();
		
	}
	
	
	public class QueryMoreAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			currentNum=currentNum+PageCount;
			List<TaskBean> reqs=TASK.getAllTopTasks(currentNum+"",currentNum+PageCount+"");
			 if(reqs!=null&&reqs.size()>0){
				 for(TaskBean req:reqs){
					 TableItem tableItem=new TableItem(resultTable,SWT.BORDER);
					 String appName=Apps.get(req.getApp());
					 String step=Process.get(req.getStatus());
					 String currUser=req.getCurrentUserName()+"("+req.getCurrentUser()+")";
	     	          tableItem.setText(new String[]{req.getCrtTime(),req.getReqID(),req.getOwnerShowName(),req.getTname(),req.getId(),step, appName,currUser}); 
	     	          tableItem.setData(req);
				 }
			 }
			 if(currentNum+PageCount>=allCount){
					btnMore.setVisible(false);
			} 
		}
	}
	
	
	public class SearchAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String keyword=text.getText();
			resultTable.removeAll();
			if(!StringUtil.isNullOrEmpty(keyword)){
				 List<TaskBean> reqs=TASK.getTaskSearch(keyword);
				 if(reqs!=null&&reqs.size()>0){
					 for(TaskBean req:reqs){
						 String appName=Apps.get(req.getApp());
						 String step=Process.get(req.getStatus());
						 String currUser=req.getCurrentUserName()+"("+req.getCurrentUser()+")";
						 TableItem tableItem=new TableItem(resultTable,SWT.BORDER);
		     	          tableItem.setText(new String[]{req.getCrtTime(),req.getReqID(),req.getOwnerShowName(),req.getTname(),req.getId(),step, appName, currUser}); 
		     	          tableItem.setData(req);
					 }
				 }
			}else{
				currentNum=0;
				List<TaskBean> reqs=TASK.getAllTopTasks(currentNum+"",currentNum+PageCount+"");
				 if(reqs!=null&&reqs.size()>0){
					 for(TaskBean req:reqs){
						 TableItem tableItem=new TableItem(resultTable,SWT.BORDER);
						 String appName=Apps.get(req.getApp());
						 String step=Process.get(req.getStatus());
						 String currUser=req.getCurrentUserName()+"("+req.getCurrentUser()+")";
		     	         tableItem.setText(new String[]{req.getCrtTime(),req.getReqID(),req.getOwnerShowName(),req.getTname(),req.getId(),step, appName, currUser}); 
		     	         tableItem.setData(req);
					 }
				 }
				 allCount=TASK.getAllReqCount();
				 if(allCount>PageCount){
						btnMore.setVisible(true);
					}
			}
		}
	}
	
	  public class ShowReqDetailAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){}
	 		 public void mouseDoubleClick(MouseEvent e){
	 			 TableItem currentItem=resultTable.getItem(new Point(e.x,e.y));
	 			 if(currentItem!=null){
	 				TaskBean bean=(TaskBean)currentItem.getData();
	 				String tab=bean.getTname();
	 				if(!TesterEditView.getInstance(null).getTabState(tab)){
	 					ReqDetailView rdv=new ReqDetailView(TesterEditView.getInstance(null).getTabFloder(),bean);
	 					TesterEditView.getInstance(null).setTabItems(rdv.content, tab);
	 				}
	 			 }
	 		 }
	  }
}
