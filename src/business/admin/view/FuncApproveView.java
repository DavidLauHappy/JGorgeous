package business.admin.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.FLOWDEF;
import model.FLOWSTEP;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import resource.Constants;
import resource.Context;
import resource.Dictionary;

import bean.FLOWDEFBean;
import bean.FlowStepBean;
import business.admin.view.UserGroupView.RemoveUserRole;

import utils.LayoutUtils;

public class FuncApproveView {
	private Composite parent;
	public Composite content;
	public Button btnSwitch;
	
	private  String[] header=new String[]{ Constants.getStringVaule("header_flowid"),
			Constants.getStringVaule("header_funcid") ,
			Constants.getStringVaule("header_flowname"),
			Constants.getStringVaule("header_flowstatus")};
	
	private  String[] sheader=new String[]{ Constants.getStringVaule("header_flowname"),
			Constants.getStringVaule("header_stepname") ,
			Constants.getStringVaule("header_approverType"),
			Constants.getStringVaule("header_approverobj"),
			Constants.getStringVaule("header_nextstepID")};
	
	public Table flowTable,stepTable=null;
	Map<String,String> Status=Dictionary.getDictionaryMap("IS_ENABLE");
	Map<String,String> ApproverTypes=Dictionary.getDictionaryMap("FLOW_STEP.APPRER_TYPE");
	Map<String,String> Flows=new HashMap<String, String>();
	public FuncApproveView(Composite com){
		this.parent=com;
		this.createAndShow();
	}
	
	private void createAndShow(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 10));
		Composite pannelAction=new Composite(content,SWT.NONE);
		pannelAction.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannelAction.setLayout(LayoutUtils.getComGridLayout(5, 0));
		btnSwitch=new Button(pannelAction,SWT.PUSH);
		btnSwitch.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 4, 1, 0, 0));
		btnSwitch.setText("   "+"开关"+"    ");
		btnSwitch.addSelectionListener(new SwitchFlowAction());
		pannelAction.pack();
		
		    Composite pannelData=new Composite(content,SWT.NONE);
		    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		    flowTable=new Table(pannelData,SWT.BORDER|SWT.SINGLE|SWT.FULL_SELECTION);
		    flowTable.setHeaderVisible(true);
		    flowTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    flowTable.setLinesVisible(true);
		    flowTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		    flowTable.addSelectionListener(new SwitchFlowButtonAction());
		    for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(flowTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
		    
			List<FLOWDEFBean> flows=FLOWDEF.getFlows();
			if(flows!=null&&flows.size()>0){
				for(FLOWDEFBean flow:flows){
					 TableItem tableItem=new TableItem(flowTable,SWT.BORDER);
					 String statusName=Status.get(flow.getEnable());
					  tableItem.setText(new String[]{flow.getId(),flow.getFuncID(),flow.getName(),statusName});
					  tableItem.setData(flow);
					  Flows.put(flow.getId(), flow.getName());
				}
			}
		
			for(int j=0;j<header.length;j++){		
				flowTable.getColumn(j).pack();
			}	
			
			stepTable=new Table(pannelData,SWT.BORDER|SWT.SINGLE|SWT.FULL_SELECTION);
			stepTable.setHeaderVisible(true);
			stepTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 100));
			stepTable.setLinesVisible(true);
			stepTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
			 for(int i=0;i<sheader.length;i++){
					TableColumn tablecolumn=new TableColumn(stepTable,SWT.BORDER);
					tablecolumn.setText(sheader[i]);
					tablecolumn.setMoveable(true);
				}
			 for(int j=0;j<sheader.length;j++){		
				 stepTable.getColumn(j).pack();
				}	
		    pannelData.pack();
		content.pack();
	}
	
	public class SwitchFlowAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			TableItem[] items=flowTable.getSelection();
			if(items!=null&&items.length>0){
				TableItem flowItem=items[0];
				FLOWDEFBean flow=(FLOWDEFBean)flowItem.getData();
				if(flow.getEnable().equals("0")){
					 flow.setEnable("1");
					 String statusName=Status.get(flow.getEnable());
					 flowItem.setText(new String[]{flow.getId(),flow.getFuncID(),flow.getName(),statusName});
					 flowItem.setData(flow);
					 //修改数据库
					 FLOWDEF.setFlowStatus(flow.getId(), flow.getEnable(), flow.getFuncID(), Context.session.userID);
				}else{
					flow.setEnable("0");
					 String statusName=Status.get(flow.getEnable());
					 flowItem.setText(new String[]{flow.getId(),flow.getFuncID(),flow.getName(),statusName});
					 flowItem.setData(flow);
					 //修改数据库
					 FLOWDEF.setFlowStatus(flow.getId(), flow.getEnable(), flow.getFuncID(), Context.session.userID);
				}
			}
		}
	}
	
	public class SwitchFlowButtonAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			TableItem[] items=flowTable.getSelection();
			if(items!=null&&items.length>0){
				TableItem flowItem=items[0];
				FLOWDEFBean flow=(FLOWDEFBean)flowItem.getData();
				if(flow.getEnable().equals("0")){
					btnSwitch.setText("   "+ "启用"+"   ");
				}
				else{
					btnSwitch.setText("   "+ "禁用"+"   ");
				}
				stepTable.removeAll();
				List<FlowStepBean>  steps=FLOWSTEP.getSteps(flow.getId());
				if(steps!=null&&steps.size()>0){
					for(FlowStepBean step:steps){
						 TableItem tableItem=new TableItem(stepTable,SWT.BORDER);
						  String flowName=Flows.get(step.getFlowID());
						  String approverType=ApproverTypes.get(step.getAppUserType());
						  tableItem.setText(new String[]{flowName,step.getName(),approverType,step.getAppUserID(),step.getNextID()});
						  tableItem.setData(flow);
					}
				}
			}
		}
	}

}
