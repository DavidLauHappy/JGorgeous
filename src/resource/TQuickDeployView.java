package resource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import model.LOCALNODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import common.core.Action;
import common.core.Protocol;

import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;
import bean.NODEBean;
import bean.ViewFileBean;
import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import resource.TDiagramView;
import utils.DataUtil;
import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;
import views.EditView;
import views.EditViewFactory;

public class TQuickDeployView {
	
	
	
	public static TQuickDeployView getInstance(){
		if(unique_instance==null)
			unique_instance=new TQuickDeployView();
		return unique_instance;
	}
	
	private static  TQuickDeployView unique_instance; 
	private TQuickDeployView(){
		
	}
	
	private EditView editView;
	private Shell view=null;
	public Table fileTable=null;
	public Table nodeTable=null;
	public Table dirTable=null;
	public List<ViewFileBean> files;
	public void show(List<ViewFileBean> srcFiles){
		this.files=srcFiles;
		view=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL|SWT.RESIZE);
		view.setText(Constants.getStringVaule("window_quickdeploy"));
		view.setLocation(AppView.getInstance().getCentreScreenPoint());
		view.setLayout(LayoutUtils.getComGridLayout(1, 0));
		Composite pannel=new Composite(view,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 750, 400));
		pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		this.createFileTable(pannel);
		this.createNodeTable(pannel);
		this.createDirTable(pannel);
		Button btnOK=new Button(pannel,SWT.PUSH);
		btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
		btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
		btnOK.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			   if(isTableChecked(fileTable)&&isTableChecked(nodeTable)&&isTableChecked(dirTable)){
				     //生成指令入库并启动调度，同时打开新的安装界面
				      if(stepFileDownlaod()){
				    	  String versionID="ver"+DateUtil.getCurrentDate("yyyyMMddHHmmssSSS"); 
				    	  LOCALNODEBean uniqueNode=null;
				    	  Map<String,String> BackDirs=new HashMap<String, String>();
				    	  boolean backDb=false;
				    	  Map<String,LOCALNODEBean> nodes=new HashMap<String,LOCALNODEBean>();
				    	  int seq=0;
				    	  TableItem[] fileItems=fileTable.getItems(); 
				    	  for(TableItem item:fileItems){
				    		   if(item.getChecked()){
				    			   ViewFileBean data=(ViewFileBean)item.getData();
				    			   TableItem[] nodeItems=nodeTable.getItems();
				    			   for(TableItem nodeItem:nodeItems){
				    				    if(nodeItem.getChecked()){
				    				    	LOCALNODEBean node=(LOCALNODEBean)nodeItem.getData();
				    				    	uniqueNode=node;
				    				    	if(!nodes.containsKey(node.getId())){
				    				    		nodes.put(node.getId(), node);
				    				    	}
				    				    	 TableItem[] dirItems =dirTable.getItems();
				    				    	 for(TableItem dirItem:dirItems){
				    				    		  if(dirItem.getChecked()){
				    				    			  String dir=(String)dirItem.getData();
				    				    			  String fileName=data.getFileName();
				    				    			  String opName="在节点["+node.getShowName()+"]"+"@type"+"文件["+data.getFileName()+"]到["+dir+"]";
				    				    			  if(!"sql".equalsIgnoreCase(FileUtils.getFileSuffix(fileName))){
				    				    				  //文件上传指令
				    				    				  seq++;
				    				    				 createFileUplaodCmd(data,node,versionID,seq+"");
				    				    			  }
				    				    		  }
				    				    	 }
				    				    }
				    			   }
				    		   }
				    	  }
				    	  //服务停止指令
				    	  for(String ip:nodes.keySet()){
				    		  LOCALNODEBean node=nodes.get(ip);
				    		  if("1".equals(node.getAutoStart())&&!StringUtil.isNullOrEmpty(node.getStop())){
				    			  seq++;
				    			  createSvcCtrlCmd(node,versionID,seq+"","0");
				    		  }
				    	  }
				     	  for(TableItem item:fileItems){
				    		   if(item.getChecked()){
				    			   ViewFileBean data=(ViewFileBean)item.getData();
				    			   TableItem[] nodeItems=nodeTable.getItems();
				    			   for(TableItem nodeItem:nodeItems){
				    				    if(nodeItem.getChecked()){
				    				    	LOCALNODEBean node=(LOCALNODEBean)nodeItem.getData();
				    				    	 TableItem[] dirItems =dirTable.getItems();
				    				    	 for(TableItem dirItem:dirItems){
				    				    		  if(dirItem.getChecked()){
				    				    			  String dir=(String)dirItem.getData();
				    				    			  String fileName=data.getFileName();
				    				    			  String opName="在节点["+node.getShowName()+"]"+"@type"+"文件["+data.getFileName()+"]到["+dir+"]";
				    				    			  if("sql".equalsIgnoreCase(FileUtils.getFileSuffix(fileName))){
				    				    				  if(!backDb&&"1".equals(node.getDbAutoBackup())){ //备份数据库
				    				    					  seq++;
				    				    					  String bakOpName="在节点["+node.getShowName()+"]备份数据库["+node.getDbName()+"]";
				    				    					  createDbBackupCmd(node,versionID,seq+"",bakOpName);
				    				    					  backDb=true;
				    				    				  }
				    				    				  seq++;
				    				    				  opName=opName.replace("@type", "执行");
				    				    				  createDbRunCmd(data,node,versionID,seq+"",opName);
				    				    			  }else{
				    				    				  //文件夹备份
				    				    				  if(!BackDirs.containsKey(dir)){
				    				    					  seq++;
				    				    					  createFloderBakCmd(node,versionID,dir,seq+"");
				    				    					  BackDirs.put(dir, "0");
				    				    				  }
				    				    				  //文件拷贝安装
				    				    				  seq++;
				    				    				  opName=opName.replace("@type", "安装");
				    				    				  createFileCopyCmd(data,node,versionID,seq+"",dir,opName);
				    				    			  }
				    				    		  }
				    				    	 }
				    				    }
				    			   }
				    		   }
				     	  }
				     	  //服务启动指令生成
				    	  for(String ip:nodes.keySet()){
				    		  LOCALNODEBean node=nodes.get(ip);
				    		  if("1".equals(node.getAutoStart())&&!StringUtil.isNullOrEmpty(node.getStart())){
				    			  seq++;
				    			  createSvcCtrlCmd(node,versionID,seq+"","1");
				    		  }
				    	  }
				    	  //选择了数据库脚本，提示先进行数据库备份
				    	  EditView editView=EditViewFactory.getEditView(null, Context.session.roleID);
				    	  if(!editView.getTabState(versionID)){
				    		  TDiagramView tdv=new  TDiagramView(editView.getTabFloder(),versionID,uniqueNode.getId());
				    		  editView.setTabItems(tdv.content,versionID);
				    	  }
				    	  view.dispose();
				      }else{
				    	    MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage("文件下载到本地失败，请稍后重新下载……");
							box.open();	
				      }
			   }else{
				   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("请选择需要部署的文件、部署目标机器、机器目录(数据库)");
					box.open();	
			   }
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		Button btnCancel=new Button(pannel,SWT.PUSH);
		btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
		btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
		btnCancel.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				view.dispose();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		pannel.pack();
		 view.pack();
		 view.open();
		 view.addShellListener(new ShellCloseAction());
 	}
 	
	
	public void createFileUplaodCmd(ViewFileBean data, LOCALNODEBean node,String versionID,String seq){
		 String upFileOpName="上传文件["+data.getFileName()+"]到机器["+node.getShowName()+"]的GAgent工作目录";
		  String msgID=UUID.randomUUID().toString().replace("-", "");
		  String cmdUpload=Protocol.UploadPKg+"|"+Protocol.PKG_UPLOAD_CMD;
		  cmdUpload=cmdUpload.replace("@IP", node.getIp());
		  cmdUpload=cmdUpload.replace("@VERSION_ID", versionID);
		  cmdUpload=cmdUpload.replace("@NODE_ID", node.getId());
		  cmdUpload=cmdUpload.replace("@STEP_ID", "0");
		  cmdUpload=cmdUpload.replace("@MSGID", msgID);
		  cmdUpload=cmdUpload.replace("@CRT_TIME", DateUtil.getCurrentTime());
		  cmdUpload=cmdUpload.replace("@SEQ", seq);
		  cmdUpload=cmdUpload.replace("@SRC_PATH", data.getLocation());
		  cmdUpload=cmdUpload.replace("@TARGET_PATH", "$WORKDIR/"+versionID);
		  
		  LOCALCOMMANDBean uploadFile=new LOCALCOMMANDBean();
		  uploadFile.setVersionID(versionID);
		  uploadFile.setFileID(data.getFileID());
		  uploadFile.setNodeID(node.getId());
		  uploadFile.setCmdID(msgID);
		  uploadFile.setCmdName(upFileOpName);
		  uploadFile.setCmdText(cmdUpload);
		  uploadFile.setSeq(seq);
		  uploadFile.setLpath(data.getLocation());
		  uploadFile.setRemote("0");
		  uploadFile.setMd5(data.getMd5());
		  if((NODEBean.OS.Linux.ordinal()+"").equals(node.getOs()))
			  uploadFile.setCmdType(Action.CMD_TYPE.LnxFileUpload.ordinal()+"");
		  else
			  uploadFile.setCmdType(Action.CMD_TYPE.WinFileUpload.ordinal()+""); 
		  uploadFile.setStatus("0");
		  uploadFile.setRemind("0");
		  uploadFile.setUserID(Context.session.userID);
		  uploadFile.add();
	}
	
	public void createFloderBakCmd(LOCALNODEBean node,String versionID,String dir,String seq){
		  String msgID=UUID.randomUUID().toString().replace("-", "");
		  String msg=Protocol.FileCopy+"|"+Protocol.FILE_COPY_CMD;
		  msg=msg.replace("@IP", node.getIp());
		  msg=msg.replace("@VERSION_ID", versionID);
		  msg=msg.replace("@NODE_ID", node.getId());
		  msg=msg.replace("@STEP_ID", "0");
		  msg=msg.replace("@MSGID", msgID);
		  msg=msg.replace("@CRT_TIME", DateUtil.getCurrentTime());
		  msg=msg.replace("@SEQ", seq+"");
		  msg=msg.replace("@FILE_ID", dir);
		  msg=msg.replace("@SRC_PATH", dir);
		  msg=msg.replace("@TARGET_PATH", "$WORKDIR/"+versionID+"/backup");
		  String opName="在节点["+node.getShowName()+"]上备份目录["+dir+"]";
		  LOCALCOMMANDBean backDir=new LOCALCOMMANDBean();
		  backDir.setVersionID(versionID);
		  backDir.setFileID(dir);
		  backDir.setNodeID(node.getId());
		  backDir.setCmdID(msgID);
		  backDir.setCmdName(opName);
		  backDir.setCmdText(msg);
		  backDir.setSeq(seq);
		  if((NODEBean.OS.Linux.ordinal()+"").equals(node.getOs()))
			  backDir.setCmdType(Action.CMD_TYPE.LnxFileCopy.ordinal()+"");
		  else
			  backDir.setCmdType(Action.CMD_TYPE.WinFileCopy.ordinal()+""); 
		  backDir.setStatus("0");
		  backDir.setRemind("0");
		  backDir.setRemote("1");
		  backDir.setUserID(Context.session.userID);
		  backDir.add();
	}

	public void createFileCopyCmd(ViewFileBean data, LOCALNODEBean node,String versionID,String seq,String dir,String opName){
		String msgID=UUID.randomUUID().toString().replace("-", "");
		  String msg=Protocol.FileCopy+"|"+Protocol.FILE_COPY_CMD;
		  msg=msg.replace("@IP", node.getIp());
		  msg=msg.replace("@VERSION_ID",versionID);
		  msg=msg.replace("@NODE_ID",node.getId() );
		  msg=msg.replace("@STEP_ID", "0");
		  msg=msg.replace("@MSGID", msgID);
		  msg=msg.replace("@CRT_TIME", DateUtil.getCurrentTime());
		  msg=msg.replace("@SEQ", seq+"");
		  msg=msg.replace("@FILE_ID", data.getFileID());
		  msg=msg.replace("@SRC_PATH", "$WORKDIR/"+versionID+"/"+data.getFileName());
		  msg=msg.replace("@TARGET_PATH", dir);
		  LOCALCOMMANDBean uploadFile=new LOCALCOMMANDBean();
		  uploadFile.setVersionID(versionID);
		  uploadFile.setFileID(data.getFileID());
		  uploadFile.setNodeID(node.getId());
		  uploadFile.setCmdID(msgID);
		  uploadFile.setCmdName(opName);
		  uploadFile.setCmdText(msg);
		  uploadFile.setSeq(seq);
		  uploadFile.setMd5(data.getMd5());
		  if((NODEBean.OS.Linux.ordinal()+"").equals(node.getOs()))
			  uploadFile.setCmdType(Action.CMD_TYPE.LnxFileCopy.ordinal()+"");
		  else
			  uploadFile.setCmdType(Action.CMD_TYPE.WinFileCopy.ordinal()+""); 
		  uploadFile.setStatus("0");
		  uploadFile.setRemind("0");
		  uploadFile.setRemote("1");
		  uploadFile.setUserID(Context.session.userID);
		  uploadFile.add();
	}
	

	public void createDbBackupCmd(LOCALNODEBean node,String versionID,String seq,String opName){
		  String msgID=UUID.randomUUID().toString().replace("-", "");
		  String  msg=Protocol.SqlserverBackup+"|"+Protocol.DB_BACKUP_CMD;
		  msg=msg.replace("@IP", node.getIp());
		  msg=msg.replace("@VERSION_ID",versionID);
		  msg=msg.replace("@NODE_ID", node.getId());
		  msg=msg.replace("@STEP_ID", "0");
		  msg=msg.replace("@MSGID", msgID);
		  msg=msg.replace("@CRT_TIME", DateUtil.getCurrentTime());
		  msg=msg.replace("@SEQ", seq);
		  msg=msg.replace("@DB_NAME", node.getDbName());
		  msg=msg.replace("@DB_USER", node.getDbUser());
		  msg=msg.replace("@DB_PASSWD",node.getDbPasswd() );
		  msg=msg.replace("@BACK_DB", "");
		  LOCALCOMMANDBean backDb=new LOCALCOMMANDBean();
		  backDb.setVersionID(versionID);
		  backDb.setFileID(node.getDbName());
		  backDb.setNodeID(node.getId());
		  backDb.setCmdID(msgID);
		  backDb.setCmdName(opName);
		  backDb.setCmdText(msg);
		  backDb.setSeq(seq);
		  backDb.setCmdType(Action.CMD_TYPE.DbBackup.ordinal()+"");
		  backDb.setStatus("0");
		  backDb.setRemind("0");
		  backDb.setRemote("0");
		  backDb.setUserID(Context.session.userID);
		  backDb.add();
	}
	
	public void createDbRunCmd(ViewFileBean data, LOCALNODEBean node,String versionID,String seq,String opName){
		  String msgID=UUID.randomUUID().toString().replace("-", "");
		  String  msg=Protocol.ScriptExecute+"|"+Protocol.SCRIPT_RUN_CMD;
		  msg=msg.replace("@IP", node.getIp());
		  msg=msg.replace("@VERSION_ID",versionID);
		  msg=msg.replace("@NODE_ID", node.getId());
		  msg=msg.replace("@STEP_ID", "0");
		  msg=msg.replace("@MSGID", msgID);
		  msg=msg.replace("@CRT_TIME", DateUtil.getCurrentTime());
		  msg=msg.replace("@SEQ", seq);
		  msg=msg.replace("@FILE_ID", data.getFileID());
		  msg=msg.replace("@FILE_NAME", data.getLocation());
		  msg=msg.replace("@DB_NAME", node.getDbName());
		  msg=msg.replace("@DB_USER", node.getDbUser());
		  msg=msg.replace("@DB_PASSWD",node.getDbPasswd() );
		  msg=msg.replace("@@RETURN_MODE", "");
		  LOCALCOMMANDBean runDb=new LOCALCOMMANDBean();
		  runDb.setVersionID(versionID);
		  runDb.setFileID(node.getDbName());
		  runDb.setNodeID(node.getId());
		  runDb.setCmdID(msgID);
		  runDb.setCmdName(opName);
		  runDb.setCmdText(msg);
		  runDb.setSeq(seq);
		  runDb.setLpath(data.getLocation());
		  runDb.setMd5(data.getMd5());
		  runDb.setCmdType(Action.CMD_TYPE.DbExecute.ordinal()+"");
		  runDb.setStatus("0");
		  runDb.setRemind("0");
		  runDb.setRemote("0");
		  runDb.setUserID(Context.session.userID);
		  runDb.add();
	}
	
	public void createSvcCtrlCmd( LOCALNODEBean node,String versionID,String seq,String type){
		  String opName="";
		  String runPath="";
		  String path="";
		  String msgID=UUID.randomUUID().toString().replace("-", "");
		  String  msg=Protocol.ServiceControl+"|"+Protocol.SERVICE_CONTROL_CMD;
		  msg=msg.replace("@IP", node.getIp());
		  msg=msg.replace("@VERSION_ID",versionID);
		  msg=msg.replace("@NODE_ID", node.getId());
		  msg=msg.replace("@STEP_ID", "0");
		  msg=msg.replace("@MSGID", msgID);
		  msg=msg.replace("@TIME", DateUtil.getCurrentTime());
		  msg=msg.replace("@SEQ", seq);
		 // msg=msg.replace("@FILE_ID", data.getFileID());
		 // msg=msg.replace("@FILE_NAME", data.getLocation());
		  msg=msg.replace("@MODE", type);
		  if("0".equals(type)){
			  msg=msg.replace("@PATH",node.getStop() );
			  opName="在节点["+node.getShowName()+"]执行服务停止脚本["+node.getStop()+"]";
			   path=node.getStop();
			  int index=path.lastIndexOf(File.separatorChar);
			     if(index<0)
			    	 index=path.lastIndexOf("/");
			     if(index<0)
			    	 index=path.lastIndexOf("\\");
			     if(index>=0){
			         runPath=path.substring(0,index);
			     }else{
			    	 runPath=path;
			     }
		  }
		  else{
			  msg=msg.replace("@PATH",node.getStart() );
			  opName="在节点["+node.getShowName()+"]执行服务启动脚本["+node.getStart()+"]";
			   path=node.getStop();
			  int index=path.lastIndexOf(File.separatorChar);
			     if(index<0)
			    	 index=path.lastIndexOf("/");
			     if(index<0)
			    	 index=path.lastIndexOf("\\");
			     if(index>=0){
			         runPath=path.substring(0,index);
			     }else{
			    	 runPath=path;
			     }
		  }
		  msg=msg.replace("@RUN_PATH",runPath);
		  msg=msg.replace("@ROUTINE_NAME","应用服务");
		  
		  LOCALCOMMANDBean runSvc=new LOCALCOMMANDBean();
		  runSvc.setVersionID(versionID);
		  runSvc.setFileID(path);
		  runSvc.setNodeID(node.getId());
		  runSvc.setCmdID(msgID);
		  runSvc.setCmdName(opName);
		  runSvc.setCmdText(msg);
		  runSvc.setSeq(seq);
		  if((NODEBean.OS.Linux.ordinal()+"").equals(node.getOs()))
			  runSvc.setCmdType(Action.CMD_TYPE.LnxSvcCtrl.ordinal()+"");
		  else
			  runSvc.setCmdType(Action.CMD_TYPE.WinSvcCtrl.ordinal()+""); 
		  runSvc.setStatus("0");
		  runSvc.setRemind("0");
		  runSvc.setRemote("0");
		  runSvc.setUserID(Context.session.userID);
		  runSvc.add();
	}
	
	public boolean stepFileDownlaod(){
		boolean result=false;
		TableItem[] items=fileTable.getItems(); 
		for(TableItem item:items){
			ViewFileBean data=(ViewFileBean)item.getData();
			if(StringUtil.isNullOrEmpty(data.getLocation())){
				result=data.download();
			}
			if(!result){
				return false;
			}
		}
		return result;
	}
	
	public boolean isTableChecked(Table table){
		TableItem[] items=table.getItems();
		if(items!=null&&items.length>0){
			for(TableItem item:items){
				if(item.getChecked())
					return true;
			}
		}else{
			return false;
		}
		return false;
	}
	
	private void createFileTable(Composite parent){
		Composite pannel=new Composite(parent,SWT.BORDER);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 2, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(3, 0));
		
		Button btnAll=new Button(pannel,SWT.PUSH);
		btnAll.setToolTipText(Constants.getStringVaule("btn_chooseAll"));
		btnAll.setText(Constants.getStringVaule("btn_chooseAll"));
		btnAll.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
		btnAll.addSelectionListener(new SelectAllAction());
		
		 Button btnUp=new Button(pannel,SWT.PUSH);
		 btnUp.setToolTipText(Constants.getStringVaule("btn_up"));
		 btnUp.setImage(Icons.getUpIcon());
		 btnUp.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
		 btnUp.addSelectionListener(new UpFileAction());
		 
		Button btnDown=new Button(pannel,SWT.PUSH);
		btnDown.setToolTipText(Constants.getStringVaule("btn_down"));
		btnDown.setImage(Icons.getDownIcon());
		btnDown.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
		btnDown.addSelectionListener(new DownFileAction());
		 
		fileTable=new Table(pannel,SWT.FULL_SELECTION|SWT.MULTI|SWT.CHECK);
	    fileTable.setHeaderVisible(true);
	    fileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
	    fileTable.setLinesVisible(true);
	    fileTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
	    String[] headerFile=new String[]{ StringUtil.rightPad(Constants.getStringVaule("header_filename"), 60, " ")};
	    for(int i=0;i<headerFile.length;i++){
			TableColumn tablecolumn=new TableColumn(fileTable,SWT.BORDER);
			tablecolumn.setText(headerFile[i]);
	    }
	    if(this.files!=null&&this.files.size()>0){
		    for(ViewFileBean file:files){
		    	String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name});
				tableItem.setImage(icon);
				tableItem.setData(file);
				tableItem.setChecked(true);
		    }
	    }
	    for(int j=0;j<headerFile.length;j++){		
			fileTable.getColumn(j).pack();
		}	
	    fileTable.pack();
	    pannel.pack();
	}
	
	boolean checkFlag=true;
	public class  SelectAllAction extends SelectionAdapter{
	    public void widgetSelected(SelectionEvent e){
	    	TableItem[] items=fileTable.getItems();
     		if(items!=null&&items.length>0){
     			for(TableItem item:items){
     				if(checkFlag){
     					item.setChecked(false);
     		    	}else{
     		    		item.setChecked(true);
     		    	}
     			}
     		}	
	      checkFlag=!checkFlag;
	    }
	}
	
	
	
	 public class UpFileAction extends SelectionAdapter{
	    public void widgetSelected(SelectionEvent e){
	    	int checkCnt=getCheckCount(fileTable);
	    	if(checkCnt<=0){
	    		MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage("请选择文件后才能操作");
				box.open();	
	    	}else{
	    		if(checkCnt>1){
	    			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("每次调整顺序只能选择一个文件，请重新选择");
					box.open();	
	    		}else{
	    			TableItem[] items=fileTable.getItems();
	    			int index=-1;
	    			for(int w=0;w<items.length;w++){
	    				if(items[w].getChecked()){
	    					index=w;
	    				}
	    			}
	    			if(index>0){
	    				DataUtil.swap(files, index, index-1);
	    				int newIndex=0;
	    				fileTable.removeAll();
	    				  for(ViewFileBean file:files){
	    				    	String name=file.getFileName();
	    				    	Image icon=Icons.getFileImage(name);
	    				    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
	    						tableItem.setText(new String[]{name});
	    						tableItem.setImage(icon);
	    						tableItem.setData(file);
	    						if(newIndex== index-1)
	    						    tableItem.setChecked(true);
	    						newIndex++;
	    				    }
	    				  fileTable.pack();
	    			}
	    		}
	    	}
	    }
	 }
	 
	 
	
	 
	 
	 public class DownFileAction extends SelectionAdapter{
		    public void widgetSelected(SelectionEvent e){
		    	int checkCnt=getCheckCount(fileTable);
		    	if(checkCnt<=0){
		    		MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("请选择文件后才能操作");
					box.open();	
		    	}else{
		    		if(checkCnt>1){
		    			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage("每次调整顺序只能选择一个文件，请重新选择");
						box.open();	
		    		}else{
		    			TableItem[] items=fileTable.getItems();
		    			int index=-1;
		    			for(int w=0;w<items.length;w++){
		    				if(items[w].getChecked()){
		    					index=w;
		    				}
		    			}
		    			if(index>=0){
		    				DataUtil.swap(files, index, index+1);
		    				int newIndex=0;
		    				fileTable.removeAll();
		    				  for(ViewFileBean file:files){
		    				    	String name=file.getFileName();
		    				    	Image icon=Icons.getFileImage(name);
		    				    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
		    						tableItem.setText(new String[]{name});
		    						tableItem.setImage(icon);
		    						tableItem.setData(file);
		    						if(newIndex==index+1)
		    							tableItem.setChecked(true);
		    						newIndex++;
		    				    }
		    				  fileTable.pack();
		    			}
		    		}
		    	}
		    }
		 }
	 
	 
	public Combo comboApp;
 	private void createNodeTable(Composite parent){
 		Composite pannel=new Composite(parent,SWT.BORDER);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 2, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(3, 0));
		Label labName=new Label(pannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_appname"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		comboApp=new Combo(pannel,SWT.DROP_DOWN);
		comboApp.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1, 0, 0));
		comboApp.addSelectionListener(new LoadAppNodes());
		List<Item> Apps=Dictionary.getDictionaryList("APP");
		if(Apps!=null&&Apps.size()>0){
	    	String[] items=new String[Apps.size()];
	    	int i=0;
	    	for(Item bean:Apps){
	    		items[i]=bean.getKey()+" "+bean.getValue();
	    		i++;
	    	}
	    	comboApp.setItems(items);
	    	comboApp.select(0);
		}
		nodeTable=new Table(pannel,SWT.FULL_SELECTION|SWT.SINGLE|SWT.CHECK);
		nodeTable.setHeaderVisible(true);
		nodeTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
		nodeTable.setLinesVisible(true);
		nodeTable.addSelectionListener(new GetNodeDirsAction());
		nodeTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
		  String[] header=new String[]{ StringUtil.rightPad(Constants.getStringVaule("label_node"), 60, " ")};
		  for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(nodeTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
		    }
		  if(!StringUtil.isNullOrEmpty(Context.Apps)){
			  String app=Context.Apps.split("\\|")[0];
			  List<LOCALNODEBean>  nodes=LOCALNODE.getMyAppNodes(app, Context.session.userID);
			  if(nodes!=null&&nodes.size()>0){
				  for(LOCALNODEBean node:nodes){
					  TableItem tableItem=new TableItem(nodeTable,SWT.BORDER);
					  tableItem.setText(new String[]{node.getShowName()});
					  tableItem.setImage(Icons.getMidNodeIcon());
					  tableItem.setData(node);
				  }
			  }
		  }
		  for(int j=0;j<header.length;j++){		
			  nodeTable.getColumn(j).pack();
			}	
		nodeTable.pack();
		pannel.pack();
 	}
 	
 	public void createDirTable(Composite parent){
 		Composite pannel=new Composite(parent,SWT.BORDER);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 2, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(1, 0));
		dirTable=new Table(pannel,SWT.FULL_SELECTION|SWT.MULTI|SWT.CHECK);
		dirTable.setHeaderVisible(true);
		dirTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		dirTable.setLinesVisible(true);
		dirTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
		  String[] header=new String[]{ StringUtil.rightPad("目录/数据库", 60, " ")};
		  for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(dirTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
		    }
		  for(int j=0;j<header.length;j++){		
			  dirTable.getColumn(j).pack();
			}	
		  dirTable.pack();
		pannel.pack();
 	}
	
	public class LoadAppNodes extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			nodeTable.removeAll();
			String rawAppName=comboApp.getText();
			String app=rawAppName.substring(0, rawAppName.indexOf(" "));
			List<LOCALNODEBean>  nodes=LOCALNODE.getMyAppNodes(app, Context.session.userID);
			  if(nodes!=null&&nodes.size()>0){
				  for(LOCALNODEBean node:nodes){
					  TableItem tableItem=new TableItem(nodeTable,SWT.BORDER);
					  tableItem.setText(new String[]{node.getShowName()});
					  tableItem.setImage(Icons.getMidNodeIcon());
					  tableItem.setData(node);
				  }
			  }
		}
	}
	public class GetNodeDirsAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			TableItem item=(TableItem)e.item;
			LOCALNODEBean data=(LOCALNODEBean)item.getData();
				dirTable.removeAll();
				String dir1=data.getDir1();
				if(!StringUtil.isNullOrEmpty(dir1)){
					  TableItem tableItem=new TableItem(dirTable,SWT.BORDER);
					  tableItem.setText(new String[]{dir1});
					  tableItem.setData(dir1);
				}
				String dir2=data.getDir2();
				if(!StringUtil.isNullOrEmpty(dir2)){
					  TableItem tableItem=new TableItem(dirTable,SWT.BORDER);
					  tableItem.setText(new String[]{dir2});
					  tableItem.setData(dir2);
				}
				String dir3=data.getDir3();
				if(!StringUtil.isNullOrEmpty(dir3)){
					  TableItem tableItem=new TableItem(dirTable,SWT.BORDER);
					  tableItem.setText(new String[]{dir3});
					  tableItem.setData(dir3);
				}
				String dir4=data.getDir4();
				if(!StringUtil.isNullOrEmpty(dir4)){
					  TableItem tableItem=new TableItem(dirTable,SWT.BORDER);
					  tableItem.setText(new String[]{dir4});
					  tableItem.setData(dir4);
				}
				String dir5=data.getDir5();
				if(!StringUtil.isNullOrEmpty(dir5)){
					  TableItem tableItem=new TableItem(dirTable,SWT.BORDER);
					  tableItem.setText(new String[]{dir5});
					  tableItem.setData(dir5);
				}
				String dbname=data.getDbName();
				if(!StringUtil.isNullOrEmpty(dbname)){
					  TableItem tableItem=new TableItem(dirTable,SWT.BORDER);
					  tableItem.setText(new String[]{dbname});
					  tableItem.setData(dbname);
				}
				dirTable.pack();
		}
	}
	
	
	public class ShellCloseAction extends ShellAdapter{
 		public void shellClosed(ShellEvent e){	
 			view.dispose();
 		}
 	}
 	
 	public int getCheckCount(Table table){
 		int count=0;
 		TableItem[] items=table.getItems();
 		if(items!=null&&items.length>0){
 			for(TableItem item:items){
 				 if(item.getChecked()){
 					count++;
 				 }
 			}
 		}
 		return count;
 	}
 	
}
