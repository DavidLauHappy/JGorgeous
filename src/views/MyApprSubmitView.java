package views;

import java.util.List;
import java.util.Map;

import model.FLOWDETAIL;
import model.USERS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import bean.FlowDetailBean;
import bean.UserBean;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import utils.LayoutUtils;

public class MyApprSubmitView extends Composite{

	public MyApprSubmitView(Composite com){
		 super(com,SWT.NONE);
    	 content=this;
    	 this.createAndShow();
	}
	
	private void createAndShow(){
		//this.createActionArea();
		this.createDataArea();
	}
	
	private void createActionArea(){
		Composite pannelAction=new Composite(content,SWT.NONE);
		pannelAction.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannelAction.setLayout(LayoutUtils.getComGridLayout(4, 0));
	    Button btnEnable=new Button(pannelAction,SWT.PUSH);
	    btnEnable.setText("   "+Constants.getStringVaule("btn_enable")+"    ");
	    btnEnable.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1, 0, 0));
	    pannelAction.pack();
	}
	
	private void createDataArea(){
		  Composite pannelData=new Composite(content,SWT.NONE);
		   pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		   pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		   dataTable=new Table(pannelData,SWT.BORDER|SWT.SINGLE|SWT.FULL_SELECTION|SWT.CHECK);
		   dataTable.setHeaderVisible(true);
		   dataTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		   dataTable.setLinesVisible(true);
		   dataTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		     String[] header=new String[]{ Constants.getStringVaule("header_approveID"),
												    		 Constants.getStringVaule("header_flowname"),
												    		 Constants.getStringVaule("header_stepname"),
												    		 Constants.getStringVaule("header_applytime"),
												    		 Constants.getStringVaule("header_approveStatus"),
										   					Constants.getStringVaule("header_currentUser")};
			for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(dataTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
		    
			List<FlowDetailBean> data=FLOWDETAIL.getMyApply(Context.session.userID);
			Map<String, String> applyStatus=Dictionary.getDictionaryMap("FLOW_DETAIL.REC_STATUS");
			if(data!=null&&data.size()>0){
				for(FlowDetailBean apply:data){
					UserBean curUser=USERS.getUser(apply.getCurUserID());
					apply.setApplyUserName(Context.session.userName);
					apply.setCurUserName(curUser.getUserName());
					String status=applyStatus.get(apply.getStatus());
					 TableItem tableItem=new TableItem(dataTable,SWT.BORDER);
		    		 tableItem.setText(new String[]{apply.getId(),apply.getFlowName(),apply.getStepName(),apply.getApplyTime(),status,apply.getCurUserName()});
		    		 tableItem.setData(apply);
				}
			}
			for(int j=0;j<header.length;j++){		
				dataTable.getColumn(j).pack();
			}	
		   pannelData.pack();
	}
	public Composite content=null;
	public Table dataTable=null;
}
