package business.aduitor.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.SYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bean.SYSTEMBean;
import business.aduitor.bean.TransLogBean;
import business.aduitor.model.DataHelper;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Item;

import utils.LayoutUtils;
import utils.StringUtil;

public class AuditSelectView {
	private Composite parent;
	public Composite content;
	public Text textUser,textIp=null;
	public DateTime dataDate=null;
	public Combo cmbApp,cmbSystem=null;
	public Table recTable=null;
	
	public Map<String,String> Apps=new HashMap<String, String>();
	public Map<String,String> Systems=new HashMap<String, String>();
	private  String[] header=new String[]{ Constants.getStringVaule("header_seq"),
																Constants.getStringVaule("header_userApps") ,
																Constants.getStringVaule("header_group"),
																Constants.getStringVaule("header_operuser"),
																Constants.getStringVaule("header_operterm"),	
																Constants.getStringVaule("header_tgtnode"),
																Constants.getStringVaule("header_action"),
																Constants.getStringVaule("header_flowdetail"),	
																Constants.getStringVaule("header_opertime")};
	
	
	
	public AuditSelectView(Composite com){
		this.parent=com;
		this.createAndShow();
	}
	
	private void createAndShow(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 10));
		this.createQueryArea();
		this.createDataArea();
	    content.pack();
	}
	
	private void createQueryArea(){
		Composite pannel=new Composite(content,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		
		Label labApp=new Label(pannel,SWT.NONE);
		labApp.setText(Constants.getStringVaule("label_appname"));
		labApp.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		cmbApp=new Combo(pannel,SWT.DROP_DOWN);
		cmbApp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
		cmbApp.addSelectionListener(new QueryAppSystemAction());
		 List<Item> apps=Dictionary.getDictionaryList("APP");
			if(apps!=null&&apps.size()>0){
				String[] items=new String[apps.size()];
				int index=0;
				for(Item item:apps){
					items[index]=item.getValue();
					Apps.put(item.getValue(), item.getKey());
					index++;
				}
				cmbApp.setItems(items);
			}
			
		Label labSys=new Label(pannel,SWT.NONE);
		labSys.setText(Constants.getStringVaule("header_group"));
		labSys.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		cmbSystem=new Combo(pannel,SWT.DROP_DOWN);
		cmbSystem.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
		
		
		Label labUser=new Label(pannel,SWT.NONE);
		labUser.setText(Constants.getStringVaule("label_operator"));
		labUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textUser=new Text(pannel,SWT.BORDER);
		textUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		
		Label labIp=new Label(pannel,SWT.NONE);
		labIp.setText(Constants.getStringVaule("label_tgtIp"));
		labIp.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textIp=new Text(pannel,SWT.BORDER);
		textIp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		
		Label labDate=new Label(pannel,SWT.NONE);
		labDate.setText(Constants.getStringVaule("label_datadate"));
		labDate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		dataDate=new DateTime(pannel, SWT.DATE );
		dataDate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, false, false, 5, 1, 0, 0));
		dataDate.setYear(2016);
		Button btnSelect=new Button(pannel,SWT.PUSH);
		btnSelect.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, false, false, 6, 1, 0, 0));
		btnSelect.setText("   "+Constants.getStringVaule("btn_query")+"    ");
		btnSelect.addSelectionListener(new QueryRecAction());
		pannel.pack();
	}
	
	private void createDataArea(){
	    Composite pannelData=new Composite(content,SWT.NONE);
	    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
	    recTable=new Table(pannelData,SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION);
	    recTable.setHeaderVisible(true);
	    recTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	    recTable.setLinesVisible(true);
	    recTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
	    for(int i=0;i<header.length;i++){
			TableColumn tablecolumn=new TableColumn(recTable,SWT.BORDER);
			tablecolumn.setText(header[i]);
			tablecolumn.setMoveable(true);
		}
		
		for(int j=0;j<header.length;j++){		
			recTable.getColumn(j).pack();
		}	
	    pannelData.pack();
	}
	
	 public class QueryAppSystemAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				String appName=cmbApp.getText();
				String appID=Apps.get(appName);
				if(!StringUtil.isNullOrEmpty(appID)){
					List<SYSTEMBean> appSys=SYSTEM.getSystems(appID, Context.session.currentFlag);
						if(appSys!=null&&appSys.size()>0){
							Systems.clear();
							String[] items=new String[appSys.size()];
							int index=0;
							for(SYSTEMBean item:appSys){
								items[index]=item.getName();
								Systems.put(item.getName(), item.getBussID());
								index++;
							}
							cmbSystem.setItems(items);
						}
				}
			}
	 }
	 
	 public class QueryRecAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				recTable.removeAll();
				String appName=cmbApp.getText();
				String appID=Apps.get(appName);
				String systemName=cmbSystem.getText();
				String systemID=Systems.get(systemName);
				String userID=textUser.getText();
				String ip=textIp.getText();
				String date="";
				if(dataDate.getYear()!=2016){
					String monStr=(dataDate.getMonth()+1)+"";
					monStr=StringUtil.leftpad(monStr, 2, "0");
					String dayStr=dataDate.getDay()+"";
					dayStr=StringUtil.leftpad(dayStr, 2, "0");
					date=dataDate.getYear()+"-"+monStr+"-"+dayStr;
				}
				if(!StringUtil.isNullOrEmpty(appID)&&!StringUtil.isNullOrEmpty(systemID)){
					List<TransLogBean> recs=DataHelper.getTransLog(appID, systemID, userID, ip, date);
					if(recs!=null&&recs.size()>0){
						for(TransLogBean bean:recs){
							 TableItem tableItem=new TableItem(recTable,SWT.BORDER);
							  tableItem.setText(new String[]{bean.getId(),bean.getAppName(),bean.getSystemName(),bean.getOperDesc(),bean.getUserTerminal(),bean.getTargetNodeDesc(),bean.getFuncName(),bean.getDetail(),bean.getDuration()});
							  tableItem.setData(bean);
						}
						 for(int j=0;j<recTable.getColumnCount();j++){		
							 recTable.getColumn(j).pack();
				 		}	
						 recTable.layout(true);
					}
				}
			}
		}
}
