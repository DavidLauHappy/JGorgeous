package resource;

import java.util.List;
import java.util.Map;

import model.DELAYDETAIL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bean.DelayDate;
import utils.LayoutUtils;

public class MyApprView {
	private Composite parent=null;
	public Composite content=null;
	
	public MyApprView(Composite parent){
		this.parent=parent;
	    this.createAndShow();
	}
	
	public Text keyword=null;
	public Table dataTable=null;
	public void createAndShow(){
		content=new Composite(parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		Composite pannelAction=new Composite(content,SWT.NONE);
		pannelAction.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannelAction.setLayout(LayoutUtils.getComGridLayout(5, 0));
		 keyword=new Text(pannelAction,SWT.BORDER);
		 keyword.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		//ËÑË÷°´Å¥
		 Button btnSearch=new Button(pannelAction,SWT.PUSH);
		 btnSearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
		 btnSearch.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 btnSearch.addSelectionListener(new SearchAction());
		 pannelAction.pack();
		 
		 //////////////////////////////////////////////////////////////////
		    Composite pannelData=new Composite(content,SWT.NONE);
			pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
			dataTable=new Table(pannelData,SWT.BORDER|SWT.SINGLE|SWT.FULL_SELECTION);
			dataTable.setHeaderVisible(true);
			dataTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			dataTable.setLinesVisible(true);
			dataTable.addMouseListener(new ShowDetailAction());
			dataTable.addListener(SWT.MeasureItem, new Listener() {
			@Override
				public void handleEvent(Event e) {
					e.height=20;
				}
			   });
			   
			   String[] header=new String[]{Constants.getStringVaule("header_approveID"),
												    		 Constants.getStringVaule("header_applyUser"),
												    		 Constants.getStringVaule("header_applytime"),
												    		 Constants.getStringVaule("header_flowdetail"),
												    		 Constants.getStringVaule("label_reason"),
												    		 Constants.getStringVaule("header_userStatus"),
												    		 Constants.getStringVaule("header_processcmt"),
												    		 };
			   for(int i=0;i<header.length;i++){
						TableColumn tablecolumn=new TableColumn(dataTable,SWT.BORDER);
						tablecolumn.setText(header[i]);
						tablecolumn.setMoveable(true);
					}
			   
			   List<DelayDate> data=DELAYDETAIL.getApplyList(Context.session.userID, "");
			   if(data!=null&&data.size()>0){
				   Map<String,String> Status=Dictionary.getDictionaryMap("DELAY_DETAIL.DSTATUS");
				   for(DelayDate apply:data){
					   TableItem tableItem=new TableItem(dataTable,SWT.BORDER);
					   String statusName=Status.get(apply.getStatus());
					   tableItem.setText(new String[]{apply.getApplyID(),apply.getApplyUserFullName(),apply.getApplyTime(),apply.getName(),apply.getReason(),statusName,apply.getComment()});
					   tableItem.setData(apply);
				   }
			   }
			   for(int j=0;j<header.length;j++){		
						dataTable.getColumn(j).pack();
				}	
			   pannelData.pack();
		     content.pack();
	}
	
	public class SearchAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			   String key=keyword.getText();
			   dataTable.removeAll();
			   List<DelayDate> data=DELAYDETAIL.getApplyList(Context.session.userID, key);
			   if(data!=null&&data.size()>0){
				   Map<String,String> Status=Dictionary.getDictionaryMap("DELAY_DETAIL.DSTATUS");
				   for(DelayDate apply:data){
					   TableItem tableItem=new TableItem(dataTable,SWT.BORDER);
					   String statusName=Status.get(apply.getStatus());
					   tableItem.setText(new String[]{apply.getApplyID(),apply.getApplyUserFullName(),apply.getApplyTime(),apply.getName(),apply.getReason(),statusName,apply.getComment()});
					   tableItem.setData(apply);
				   }
			   }
		}
	}
	
	 public class ShowDetailAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){}
	 		 public void mouseDoubleClick(MouseEvent e){
	 			 TableItem currentItem=dataTable.getItem(new Point(e.x,e.y));
	 			 if(currentItem!=null){
	 				 DelayDate data=(DelayDate)currentItem.getData();
	 				MyApproveDetailView view=new MyApproveDetailView(data,0);
	 				view.show();
	 			 }
	 		 }
	 }
	
}
