package views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CONFIG;

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
import resource.Icons;
import resource.Item;
import bean.ConfigBean;

import utils.LayoutUtils;

public class ConfigMngView {

	private Composite parent=null;
	public Composite content=null;
	private  Combo comboApp=null;
	private Text textFileName=null;
	public Table fileTable=null;
	
	public Map<String,String> Apps=new HashMap<String, String>();
	public Map<String,String> App=Dictionary.getDictionaryMap("APP");
	public Map<String,String> Env=Dictionary.getDictionaryMap("CONFIG.ENV_TYPE");
	 String[]   header=new String[]{ Constants.getStringVaule("header_appname"),
				Constants.getStringVaule("header_environment") ,
				Constants.getStringVaule("header_configfile"),
				Constants.getStringVaule("header_variable"),
				Constants.getStringVaule("header_value")};
	 
	public ConfigMngView(Composite parent){
		this.parent=parent;
	    this.creatAndShow();
	}
	
	public void creatAndShow(){
		content=new Composite(parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
		Composite pannelAction=new Composite(content,SWT.NONE);
		pannelAction.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannelAction.setLayout(LayoutUtils.getComGridLayout(8, 0));
		comboApp=new Combo(pannelAction,SWT.DROP_DOWN);
		comboApp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
		List<Item> apps=Dictionary.getDictionaryList("APP");
	    if(apps!=null&&apps.size()>0){
	    	String[] items=new String[apps.size()];
	    	int i=0;
	    	for(Item app:apps){
	    		items[i]=app.getValue();
	    		i++;
	    		Apps.put(app.getValue(), app.getKey());
	    	}
	    	comboApp.setItems(items);
	    }
		
	    Label labName=new Label(pannelAction,SWT.NONE);
		labName.setText(Constants.getStringVaule("header_name")+"(*)");
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textFileName=new Text(pannelAction,SWT.BORDER);
		textFileName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		textFileName.setToolTipText(Constants.getStringVaule("text_tips_configname"));
		
		  Button btnQuery=new Button(pannelAction,SWT.PUSH);
		  btnQuery.setText("   "+Constants.getStringVaule("btn_query")+"    ");
		  btnQuery.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1, 0, 0));
		  btnQuery.addSelectionListener(new QueryCfgAction());
		  
		  	Button btnAdd=new Button(pannelAction,SWT.PUSH);	
	        btnAdd.setToolTipText(Constants.getStringVaule("btn_tips_addcfg"));
	        btnAdd.setImage(Icons.getAddIcon());
	        btnAdd.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
	        btnAdd.addSelectionListener(new AddCfgAction());
	        
		  pannelAction.pack();
		
		   Composite pannelData=new Composite(content,SWT.BORDER);
		    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		    fileTable=new Table(pannelData,SWT.BORDER|SWT.SINGLE|SWT.FULL_SELECTION);
		    fileTable.setHeaderVisible(true);
		    fileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    fileTable.setLinesVisible(true);
		    fileTable.addMouseListener(new ShowCfgPropertyAction());
		    fileTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});

		   
				for(int i=0;i<header.length;i++){
					TableColumn tablecolumn=new TableColumn(fileTable,SWT.BORDER);
					tablecolumn.setText(header[i]);
					tablecolumn.setMoveable(true);
				}
				
				for(int j=0;j<header.length;j++){		
					fileTable.getColumn(j).pack();
				}	
		    pannelData.pack();
		  content.pack();
	}
	
	  public class QueryCfgAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			String  appName=comboApp.getText();
	 			String app=Apps.get(appName);
	 			String fileName=textFileName.getText();
	 			fileTable.removeAll();    	 
	 			List<ConfigBean> configs=CONFIG.getConfigs(app, fileName, Context.session.userID);
	 			if(configs!=null&&configs.size()>0){
	 				for(ConfigBean cfg:configs){
	 					String AppName=App.get(cfg.getApp());
	 					String env=Env.get(cfg.getEnvType());
	 				    TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
		    		    tableItem.setText(new String[]{AppName,env,cfg.getFileName(),cfg.getParamName(),cfg.getParamVal()});
		    		    tableItem.setData(cfg);		
	 				}
	 				 for(int j=0;j<fileTable.getColumnCount();j++){		
			    		 fileTable.getColumn(j).pack();
			 		}	 
	 			}
	 			fileTable.layout(true);
	 		}
	  }

	  public class ShowCfgPropertyAction extends MouseAdapter{
			public void mouseDown(MouseEvent e){
			}
			public void mouseDoubleClick(MouseEvent e){
				TableItem currentItem=fileTable.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null){
	 				ConfigBean cfg=(ConfigBean)currentItem.getData();
	 				CfgPropertyView cfgView=new CfgPropertyView();
	 				cfgView.setData(cfg);
	 				cfgView.show();
	 			}
			}
	 }
	  
		public class AddCfgAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				CfgPropertyView cfgView=new CfgPropertyView();
 				cfgView.show();
			}
		}
}
