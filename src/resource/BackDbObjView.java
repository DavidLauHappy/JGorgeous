package resource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import common.db.DataHelper;

import bean.DbObj;
import bean.LOCALNODEBean;
import bean.NODEBean;
import bean.PFILEBean;

import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.SqlServer;
import utils.StringUtil;
import views.AppView;

public class BackDbObjView {
	private Composite parent;
	public Composite content;
	private LOCALNODEBean node;
	public BackDbObjView(Composite com,LOCALNODEBean data){
		this.parent=com;
		this.node=data;
		content=new  Composite(com,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		this.createAndShow();
		content.pack();
	}
	
	public Tree leftTree,rightTree=null;
	public Text  keyword=null;
	public List<DbObj> objects;
	public TreeItem  itemTable,itemProc=null;
	public Button btnAction=null;
	public  void createAndShow(){
		SashForm nodeCom=new SashForm(this.content,SWT.HORIZONTAL); 
		nodeCom.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		nodeCom.setLayout(LayoutUtils.getComGridLayout(2, 10));
		////////////////////////////////////////////////////////////////////
		Composite comLeft=new  Composite(nodeCom,SWT.NONE);
		comLeft.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		comLeft.setLayout(LayoutUtils.getComGridLayout(7, 1));
		keyword=new Text(comLeft,SWT.BORDER);
		keyword.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 6, 1, 0, 0));
		keyword.setToolTipText(Constants.getStringVaule("text_tips_dbobj"));
	    Button btnFind=new Button(comLeft,SWT.PUSH);
	    btnFind.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false,1, 1, 0, 0));
	    btnFind.setImage(Icons.getSearchIcon());
	    btnFind.addSelectionListener(new FindObjAction());
	    
	    leftTree=new Tree(comLeft,SWT.BORDER|SWT.MULTI|SWT.CHECK);
	    leftTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 7, 1, 0, 0));
	    leftTree.addSelectionListener(new SetCheckAction());
	    objects=SqlServer.getInstance().getDbObjects(this.node.getDbType(), this.node.getIp(), this.node.getDbPort(), this.node.getDbUser(), this.node.getDbPasswd(), this.node.getDbName());
	    if(objects!=null&&objects.size()>0){
	    	List<DbObj> tables=new ArrayList<DbObj>();
	    	List<DbObj> others=new ArrayList<DbObj>();
	    	for(DbObj obj:objects){
	    		 if((DbObj.Type.Table.ordinal()+"").equals(obj.getType())){
	    			 tables.add(obj);
	    		 }else{
	    			 others.add(obj);
	    		 }
	    	}
	    	itemTable=new TreeItem(leftTree,SWT.NONE);
	    	itemTable.setText("±í");
	    	itemTable.setImage(Icons.getFloderIcon());
	   	 	itemTable.setData("$Type", "root"); 
	   	 	for(DbObj obj:tables){
		   	 	TreeItem  tableItem=new TreeItem(itemTable,SWT.NONE);
		   	 	tableItem.setText(obj.getName());
		   	 	tableItem.setImage(Icons.getDetailIcon());
		   		tableItem.setData("$Type", "node"); 
		   	 	tableItem.setData(obj);
	   	 	}
	   	 itemTable.setExpanded(true);	
	   	 ///////////////////////////////
	   	 itemProc=new TreeItem(leftTree,SWT.NONE);
	   	 itemProc.setText("³ÌÐò");
	   	 itemProc.setImage(Icons.getFloderIcon());
	   	 itemProc.setData("$Type", "root"); 
	   	 	for(DbObj obj:others){
		   	 	TreeItem  tableItem=new TreeItem(itemProc,SWT.NONE);
		   	 	tableItem.setText(obj.getName());
		   	 	tableItem.setImage(Icons.getDetailIcon());
		   	 	tableItem.setData(obj);
		   	 	tableItem.setData("$Type", "node"); 
	   	 	}
	     }
	    itemProc.setExpanded(true);	
	    leftTree.pack();
		comLeft.pack();
		//////////////////////////////////////////////////////////////////////////////////////////////
		Composite comRight=new  Composite(nodeCom,SWT.NONE);
		comRight.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		comRight.setLayout(LayoutUtils.getComGridLayout(1, 1));
		btnAction=new Button(comRight,SWT.PUSH);
		btnAction.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false,1, 1, 0, 0));
		btnAction.setText("     "+Constants.getStringVaule("btn_backup")+"     ");
		btnAction.addSelectionListener(new BackupAction());
		rightTree=new Tree(comRight,SWT.BORDER|SWT.MULTI);
		rightTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		rightTree.pack();
		comRight.pack();
		nodeCom.setWeights(new int[]{60,40});
	}
	
	  public class FindObjAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			 String key=keyword.getText();
	   			 int limit=5;
	   			 if(!StringUtil.isNullOrEmpty(key)){
	   				TreeItem[] items=itemTable.getItems();
	   				for(TreeItem item:items){
	   					if((item.getText().equalsIgnoreCase(key)||item.getText().startsWith(key))&&limit>0){
	   						limit--;
	   						item.setChecked(true);
	   						leftTree.select(item);
	   					}else{
	   						item.setChecked(false);
	   					}
	   				}
	   				TreeItem[] allItems=itemProc.getItems();
	   				for(TreeItem item:allItems){
	   					if((item.getText().equalsIgnoreCase(key)||item.getText().startsWith(key))&&limit>0){
	   						limit--;
	   						item.setChecked(true);
	   						leftTree.select(item);
	   					}else{
	   						item.setChecked(false);
	   					}
	   				}
	   			 }
	   		}
	  }
	  
	  public class BackupAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			btnAction.setEnabled(false);
	   			List<TreeItem> items=getSelections();
	   			if(items.size()<=0){
	   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_necessarychoose"));
					box.open();	
	   			}else{
	   			   Connection con=null;
	   			   List<String> backs=new ArrayList<String>();
	   				try{
	   				     con =SqlServer.getInstance().getConnection(node.getIp(), node.getDbPort(), node.getDbUser(), node.getDbPasswd(), node.getBackdbname(),node.getDbType());
		   				for(TreeItem item:items){
		   					  DbObj obj=(DbObj)item.getData();
		   					  String ddl=backUPDbObject(con,obj.getType(),obj.getName(),node.getDbType());
		   					  if((DbObj.Type.Table.ordinal()+"").equals(obj.getType())){
		   						  String backName=obj.getName()+"_"+"v"+DateUtil.getCurrentDate("yyyyMMddHHmmss");
		   						  boolean ret= backUPDbObjectData(con,node.getDbName(),obj.getName(),backName,node.getDbType());
		   						  if(ret){
		   							  backs.add(backName);
		   						  }
		   					  }
		   					 String backPath=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+  "dbbackup"; 
		   					File dir=new File(backPath);
		   					if(!dir.exists())
		   						dir.mkdirs();
		   					 FileUtils.writeFile(dir.getAbsolutePath()+File.separator+obj.getName()+".sql", ddl);
		   					 backs.add(dir.getAbsolutePath()+File.separator+obj.getName()+".sql");
		   				}
	   				}catch(Exception exp){
	   					
	   				}finally{
	   					if(con!=null){
							 try {
								con.close();
							} catch (SQLException exp) {
								
							}
						 }
	   				}
	   				if(backs.size()>0){
	   					rightTree.removeAll();
	   					for(String line:backs){
	   						TreeItem  tableItem=new TreeItem(rightTree,SWT.NONE);
	   				   	 	tableItem.setText(line);
	   				   	 	if(line.indexOf(File.separator)!=-1){
	   				   	 	   tableItem.setImage(Icons.getFileIcon());
	   				   	 	}
	   				   	 	else{
	   				   	 		tableItem.setImage(Icons.getDetailIcon());
	   				   	 	}
	   					}
	   				}
	   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_successoperate"));
					box.open();
					btnAction.setEnabled(true);
	   			}
	   		}
	  }
	  
	  private String backUPDbObject(Connection con,String objType,String objName,String dbType){
			String objText="";
			if((DbObj.Type.Proc.ordinal()+"").equals(objType)){
				 objText=DataHelper.getDbProcText(con, objName,dbType);
			 }
			else{
				objText=DataHelper.getDbTableText(con, objName,dbType);
			}
			return objText;
		}
	  
	  
	  private boolean  backUPDbObjectData(Connection con,String dbOwner,String objName,String backObjName,String dbtype){
			String sql="";//
			if((NODEBean.DB.SqlServer.ordinal()+"").equals(dbtype)){
				sql="select * into @backTableName from @dbOwner.dbo.@objName";
			}else{
				sql="create table  @backTableName as  select *  from @dbOwner.@objName";
			}
			sql=sql.replace("@backTableName", backObjName);
			sql=sql.replace("@objName", objName);
			sql=sql.replace("@dbOwner", dbOwner);
			return DataHelper.backDbTableData(con, sql);
		}
	  
	  public List<TreeItem> getSelections(){
		  List<TreeItem> selections=new ArrayList<TreeItem>();
		  TreeItem[] items=itemTable.getItems();
			for(TreeItem item:items){
				if(item.getChecked()){
					selections.add(item);
				}
			}
			  TreeItem[] allitems=itemProc.getItems();
				for(TreeItem item:allitems){
					if(item.getChecked()){
						selections.add(item);
					}
				}
			return selections;
	  }
	  public class SetCheckAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			TreeItem item=(TreeItem)e.item;
	   			if(item!=null){
	   				String type=(String)item.getData("$Type");
	   				if("root".equals(type))
	   					item.setChecked(false);
	   			}
	   		}
	  }
	  
	  
	  
}
