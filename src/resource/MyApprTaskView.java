package resource;

import java.util.List;

import model.DELAYDETAIL;
import model.GROUPUSER;

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

import bean.DelayDate;
import bean.GroupUserBean;

import utils.LayoutUtils;
import utils.StringUtil;


/**
 * @author Administrator
 *  我的审批-待我处理的审批
 */
public class MyApprTaskView  extends Composite{

	public MyApprTaskView(Composite com){
		super(com,SWT.NONE);
	   	 content=this;
	   	 this.createAndShow();
	}
	
	public Combo nexter=null;
	public Text keyword=null;
	public void createAndShow(){
		Composite pannelAction=new Composite(content,SWT.NONE);
		pannelAction.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannelAction.setLayout(LayoutUtils.getComGridLayout(8, 0));
		//提交人
		 Label labNexter=new Label(pannelAction,SWT.NONE);
		 labNexter.setText(Constants.getStringVaule("header_submituser"));
		 labNexter.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 nexter=new Combo(pannelAction,SWT.DROP_DOWN);
		 nexter.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 String groupID="Flow5";
		 List<GroupUserBean> users=GROUPUSER.getUsers(groupID);
		 if(users!=null&&users.size()>0){
			  String[] nextUsers=new String[users.size()];
			  int index=0;
			  for(GroupUserBean user:users){
				  nextUsers[index]=user.getUserFullName();
				  index++;
			  }
			  nexter.setItems(nextUsers);
			  nexter.select(0);
		 }
		 //搜索关键字
		 keyword=new Text(pannelAction,SWT.BORDER);
		 keyword.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		//搜索按钮
		 Button btnSearch=new Button(pannelAction,SWT.PUSH);
		 btnSearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
		 btnSearch.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 btnSearch.addSelectionListener(new SearchAction());
		 pannelAction.pack();
		//
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
											    		 Constants.getStringVaule("label_reason")
											    		 };
		   for(int i=0;i<header.length;i++){
					TableColumn tablecolumn=new TableColumn(dataTable,SWT.BORDER);
					tablecolumn.setText(header[i]);
					tablecolumn.setMoveable(true);
				}
		   
		   List<DelayDate> data=DELAYDETAIL.getApprList("", Context.session.userID, DelayDate.Status.Apply.ordinal()+"", "");
		   if(data!=null&&data.size()>0){
			   for(DelayDate apply:data){
				   TableItem tableItem=new TableItem(dataTable,SWT.BORDER);
				   tableItem.setText(new String[]{apply.getApplyID(),apply.getApplyUserFullName(),apply.getApplyTime(),apply.getName(),apply.getReason()});
				   tableItem.setData(apply);
			   }
		   }
		   for(int j=0;j<header.length;j++){		
					dataTable.getColumn(j).pack();
			}	
		   pannelData.pack();
	}
	
	public class SearchAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String nextUser=nexter.getText();
			String nextUserID=StringUtil.getUserIdFromName(nextUser);
			String key=keyword.getText();
			dataTable.removeAll();
			 List<DelayDate> data=DELAYDETAIL.getApprList(nextUserID, Context.session.userID, DelayDate.Status.Apply.ordinal()+"", key);
			   if(data!=null&&data.size()>0){
				   for(DelayDate apply:data){
					   TableItem tableItem=new TableItem(dataTable,SWT.BORDER);
					   tableItem.setText(new String[]{apply.getApplyID(),apply.getApplyUserFullName(),apply.getApplyTime(),apply.getName(),apply.getReason()});
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
		 			MyApproveDetailView view=new MyApproveDetailView(data,1);
		 			view.show();
	 			 }
	 		 }
	 }
	
	public Composite content=null;
	public Table dataTable=null;
}
