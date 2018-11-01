package business.deploy.figures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.NODE;
import model.NODEFILESTT;
import model.PKG;
import model.SYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import common.db.DataHelper;

import bean.NODEBean;
import bean.NODEFILESTTBean;
import bean.PKGBean;
import bean.SYSTEMBean;
import business.deploy.bean.ReviewData;

import resource.Constants;
import resource.Context;
import resource.Dictionary;

import utils.DataUtil;
import utils.LayoutUtils;
import utils.StringUtil;

/**
 * @author David
 * 
 */
public class ReportView {

	private static ReportView unique_instance;
	public static ReportView getIntance(Composite com){
		if(unique_instance==null){
			unique_instance=new ReportView(com);
		}
		return unique_instance;
	}
	
	private ReportView(Composite com){
		Systems=new HashMap<String, String>();
		Nodes=new HashMap<String, String>();
		Status=Dictionary.getDictionaryMap("COMMAND.STATUS");
		
		this.parent=com;
		this.createAndShow();
	}
	
	private void createAndShow(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 0));
		SashForm sashForm=new SashForm(content,SWT.VERTICAL);
		sashForm.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		sashForm.setLayout(LayoutUtils.getDeployLayout());
		this.createSumPanel(sashForm);
		this.createCmdPanel(sashForm);
		sashForm.setWeights(new int[]{40,60});
		content.pack();
	}
	
	private void createSumPanel(Composite com){
		Composite pannel=new Composite(com,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
	
		 	fileTable=new Table(pannel,SWT.BORDER);
		    fileTable.setHeaderVisible(true);
		    fileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 8, 1, 0, 0));
		    fileTable.setLinesVisible(true);
		    fileTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		  String[]  header=new String[]{ Constants.getStringVaule("header_version"),
				  										Constants.getStringVaule("header_group") ,
														Constants.getStringVaule("header_node") ,
														Constants.getStringVaule("header_dir"),
														Constants.getStringVaule("header_file")
														};
				for(int i=0;i<header.length;i++){
					TableColumn tablecolumn=new TableColumn(fileTable,SWT.BORDER);
					tablecolumn.setText(header[i]);
					tablecolumn.setMoveable(true);
				}
				for(int j=0;j<header.length;j++){		
					fileTable.getColumn(j).pack();
				}	
				pannel.pack();
	}
	
	private void createCmdPanel(Composite com){
		Composite pannel=new Composite(com,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(8, 0));
		comboVersion=new Combo(pannel,SWT.DROP_DOWN);
		comboVersion.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
		List<PKGBean> versions=PKG.getMyPkgs( Context.session.userID, PKGBean.Enable.Yes.ordinal()+"");
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
	    
	    comboGroup=new Combo(pannel,SWT.DROP_DOWN);
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
	    
		comboNode=new Combo(pannel,SWT.DROP_DOWN);
		comboNode.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));

	    btnQuery=new Button(pannel,SWT.PUSH);
	    btnQuery.setText("   "+Constants.getStringVaule("btn_query")+"    ");
	    btnQuery.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
	    btnQuery.addSelectionListener(new QueryFileAction());
		//使用扩展了的复核的数据结构
		cmdTable=new Table(pannel,SWT.BORDER);
		cmdTable.setHeaderVisible(true);
		cmdTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 8, 1, 0, 0));
		cmdTable.setLinesVisible(true);
		cmdTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		  String[]  header=new String[]{  Constants.getStringVaule("header_seq"),
				  										Constants.getStringVaule("header_version"),
														Constants.getStringVaule("header_group") ,
														Constants.getStringVaule("header_node"),
														Constants.getStringVaule("header_action"),
														Constants.getStringVaule("header_status"),
														Constants.getStringVaule("header_retresult"),
														Constants.getStringVaule("header_time"),
														Constants.getStringVaule("label_ip")};
				for(int i=0;i<header.length;i++){
					TableColumn tablecolumn=new TableColumn(cmdTable,SWT.BORDER);
					tablecolumn.setText(header[i]);
					tablecolumn.setMoveable(true);
					
				}				
				for(int j=0;j<header.length;j++){		
					cmdTable.getColumn(j).pack();
					switch(j){
					case 1:cmdTable.getColumn(j).setWidth(150);break;
					case 3:cmdTable.getColumn(j).setWidth(100);break;
					case 4:cmdTable.getColumn(j).setWidth(180);break;
					case 6:cmdTable.getColumn(j).setWidth(150);break;
					case 8:cmdTable.getColumn(j).setWidth(100);break;
					default:break;
				}
				}	
				pannel.pack();
	}
	
	  public class QueryFileAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			String versionID=comboVersion.getText();
	 			String groupName=comboGroup.getText();
	 			String groupID=(String)DataUtil.getMapFirstKey(Systems, groupName);
	 			String nodeName=comboNode.getText();
	 			String nodeID=(String)DataUtil.getMapFirstKey(Nodes, nodeName);
	 			List<ReviewData> datas=DataHelper.getReportCmdData(versionID,groupID,nodeID,Context.session.currentFlag);
	 			cmdTable.removeAll();    	
				if(datas!=null&&datas.size()>0){
					for(ReviewData bean:datas){
						  TableItem tableItem=new TableItem(cmdTable,SWT.BORDER);
						  String state=Status.get(bean.getStatus());
						  String retMsg=bean.getResult();
						  retMsg=retMsg.replaceAll("&", "");
			    		   tableItem.setText(new String[]{bean.getSeq(),bean.getVersionID(),bean.getGroupName(),bean.getNodeName(),bean.getOpName(),state,retMsg,bean.getResultTime(),bean.getIp()});
					}
				}
				//同步刷新节点文件统计信息
				// DataHelper.statisticVersionFiles(groupID, versionID);
				 List<NODEFILESTTBean> data=NODEFILESTT.getNodeFileStatistic(versionID,groupID, Context.session.currentFlag);
				 fileTable.removeAll();
					if(data!=null&&data.size()>0){
						for(NODEFILESTTBean bean:data){
							  TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				    		   tableItem.setText(new String[]{bean.getPkgID(),groupName, bean.getNodeName(),bean.getFileDir(),bean.getFileCount()});
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
	 				 List<NODEBean> loadNodes=NODE.getSystemNodes( Context.session.userID, groupID, Context.session.currentFlag);
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
	  
	private Composite parent=null;
	public Composite content=null;
	public Table fileTable,cmdTable=null;
	private  Combo comboVersion=null;
	private  Combo comboGroup=null;
	private  Combo comboNode=null;
	private Button  btnQuery=null;
	private Map<String, String> Systems=null;
	private Map<String, String> Nodes=null;
	private Map<String,String> Status=null;
}
