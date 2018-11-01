package business.deploy.figures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.NODE;
import model.PKG;
import model.SYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import common.db.DataHelper;

import bean.NODEBean;
import bean.PKGBean;
import bean.SYSTEMBean;
import business.deploy.bean.CompareData;
import business.deploy.core.ExcelWriter;

import resource.Constants;
import resource.Context;
import resource.Paths;
import utils.DataUtil;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;
/**
 * @author DavidLau
 * @date 2016-8-12
 * @version 1.0
 * ÀàËµÃ÷
 */
public class CompareFileView {
	public static CompareFileView getInstance(Composite com){
		if(unique_instance==null){
			unique_instance=new CompareFileView(com);
		}
		return unique_instance;
	}
	
	private CompareFileView(Composite com){
		this.parent=com;
		Systems=new HashMap<String, String>();
		Nodes=new HashMap<String, String>();
		this.show();
	}
	
	private void show(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
			Composite pannelAction=new Composite(content,SWT.NONE);
			pannelAction.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
			pannelAction.setLayout(LayoutUtils.getComGridLayout(8, 0));
			comboVersion=new Combo(pannelAction,SWT.DROP_DOWN);
			comboVersion.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
			List<PKGBean> versions=PKG.getMyPkgs(Context.session.userID, PKGBean.Enable.Yes.ordinal()+"");
		    if(versions!=null&&versions.size()>0){
		    	String[] items=new String[versions.size()];
		    	int i=0;
		    	for(PKGBean version:versions){
		    		items[i]=version.getId();
		    		i++;
		    	}
		    	comboVersion.setItems(items);
		    }
		    comboVersion.addSelectionListener(new SetSystemAction());
		    
		    comboGroup=new Combo(pannelAction,SWT.DROP_DOWN);
		    comboGroup.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
		    List<SYSTEMBean> systems=SYSTEM.getSystems(Context.pkgApp, Context.session.currentFlag);
		    if(systems!=null&&systems.size()>0){
		    	String[] item=new String[systems.size()];
		    	int j=0;
		    	for(SYSTEMBean system:systems){
		    		item[j]=system.getName();
		    		j++;
		    		Systems.put(system.getBussID(), system.getName());
				}
		    	comboGroup.setItems(item);
		    }
			comboGroup.addSelectionListener(new SetNodesAction());
			
			comboNode=new Combo(pannelAction,SWT.DROP_DOWN);
			comboNode.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));

		    btnQuery=new Button(pannelAction,SWT.PUSH);
		    btnQuery.setText("   "+Constants.getStringVaule("btn_query")+"    ");
		    btnQuery.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
		    btnQuery.addSelectionListener(new QueryFileAction());
		    
		   Button btnExport=new Button(pannelAction,SWT.PUSH);
		   btnExport.setText("   "+Constants.getStringVaule("btn_export")+"    ");
		   btnExport.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
		   btnExport.addSelectionListener(new ExportFileAction());
		    
		    pannelAction.pack();
		    Composite pannelData=new Composite(content,SWT.BORDER);
		    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		    fileTable=new Table(pannelData,SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION);
		    fileTable.setHeaderVisible(true);
		    fileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    fileTable.setLinesVisible(true);
		    fileTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		     header=new String[]{ Constants.getStringVaule("header_version"),
 										Constants.getStringVaule("header_name") ,
 										Constants.getStringVaule("header_group"),
 										Constants.getStringVaule("header_node"),
 										Constants.getStringVaule("header_oldmd5"),
 										Constants.getStringVaule("header_md5"),
 										Constants.getStringVaule("header_result"),
 										Constants.getStringVaule("header_ip")};
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

	  public class ExportFileAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			FileDialog dialog = new FileDialog(AppView.getInstance().getShell(), SWT.SAVE);
				dialog.setFilterPath(Paths.getInstance().getWorkDir());
				dialog.setFilterNames(new String[] { "All Files(*.*)" });
				dialog.setFilterExtensions(new String[] { "*.xls" });
				dialog.setFileName ("filelist.xls");
				String filePath = dialog.open();
				if (filePath != null) {
					ExcelWriter.makeFileXls(filePath);
				}
	 		}
	  }
	
	  public class QueryFileAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			String versionID=comboVersion.getText();
	 			String groupName=comboGroup.getText();
	 			String groupID=(String)DataUtil.getMapFirstKey(Systems, groupName);
	 			String nodeName=comboNode.getText();
	 			String nodeID=(String)DataUtil.getMapFirstKey(Nodes, nodeName);
	 			FileList=DataHelper.getCompareData(Context.session.userID, Context.session.currentFlag, versionID,groupID,nodeID);
	 			fileTable.removeAll();    	 
		    	 for(int w=0;w<FileList.size();w++){
		    		 CompareData bean=(CompareData)FileList.get(w);
		    		   TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
		    		   tableItem.setText(new String[]{bean.getVersionID(),bean.getFileName(),bean.getGroupName(),bean.getNodeName(),bean.getMD5(),bean.getInstallMd5(),bean.getResult(),bean.getIp()});
		    	
		    		   if(bean.type.equals(CompareData.Type.NonEqual.ordinal()+"")){
		    			   tableItem.setForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_RED));
		    		   }
		    		   if(bean.type.equals(CompareData.Type.NewFile.ordinal()+"")){
		    			   tableItem.setForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_BLUE));
		    		   }
		    	 }
		    	 for(int j=0;j<fileTable.getColumnCount();j++){		
		    		 fileTable.getColumn(j).pack();
		 		}	
		    	 fileTable.layout(true);
	 		}
	 	}
	  
	  
	  public class SetNodesAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			String chooseGroup=comboGroup.getText();
	 			String groupID=(String)DataUtil.getMapFirstKey(Systems, chooseGroup);
	 			if(!StringUtil.isNullOrEmpty(groupID)){
	 				comboNode.removeAll();
	 				 List<NODEBean> loadNodes=NODE.getSystemNodes(Context.session.userID, groupID, Context.session.currentFlag);
	 				if(loadNodes!=null&&loadNodes.size()>0){
	 					Nodes.clear();
	 					String[] items=new String[loadNodes.size()];
	 			    	int i=0;
	 			    	 for(NODEBean node:loadNodes){
	 			    		items[i]=node.getName();
	 			    		i++;
	 			    		Nodes.put(node.getId(), node.getName());
	 			    	}
	 			    	comboNode.setItems(items);
	 				}
	 			}
	 		}
	  }
	  
	  public class SetSystemAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			String pkgID=comboVersion.getText();
	 			if(!StringUtil.isNullOrEmpty(pkgID)){
	 				comboGroup.removeAll();
	 				  List<SYSTEMBean> systems=SYSTEM.getSystems(Context.session.userID,pkgID, Context.session.currentFlag);
	 				    if(systems!=null&&systems.size()>0){
	 				    	String[] item=new String[systems.size()];
	 				    	int j=0;
	 				    	for(SYSTEMBean system:systems){
	 				    		item[j]=system.getName();
	 				    		j++;
	 				    		Systems.put(system.getBussID(), system.getName());
	 						}
	 				    	comboGroup.setItems(item);
	 				    }
	 			}
	 		}
	  }
	  
	  public  List<CompareData> getFileList(){
		  return this.FileList;
	  }
	  
	  public String[] getHeader(){
		  return this.header;
	  }

	
	private static CompareFileView unique_instance;
	public  Composite content=null;
	private  Composite parent=null;
	private  Combo comboVersion=null;
	private  Combo comboGroup=null;
	private  Combo comboNode=null;
	private Button  btnQuery;
	public Table fileTable=null;
	private List<CompareData> FileList=null;
	private  String[] header=null;
	private Map<String, String> Systems=null;
	private Map<String, String> Nodes=null;
}
