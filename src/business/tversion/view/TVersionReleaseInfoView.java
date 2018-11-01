package business.tversion.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import model.BACKLOG;
import model.GROUPUSER;
import model.STREAM;
import model.TASK;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Shell;

import common.controls.DavidCombo;

import bean.BackLogBean;
import bean.GroupUserBean;
import bean.StreamBean;
import bean.TTaskBean;
import bean.TaskBean;
import bean.VStep;
import bean.ViewBean;
import bean.ViewFileBean;
import business.tversion.action.RemindNextAction;
import resource.Constants;
import resource.Context;
import resource.IMessage;
import resource.Icons;
import resource.Paths;
import resource.UserChoose;
import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import utils.ZenDaoApi;
import views.AppView;

public class TVersionReleaseInfoView {
	public Composite self=null;
	public SashForm content=null;
	public Composite parent=null;
	private Text textName,textDesc,textVersion,textInput;
	private  Combo comboStream=null;
	private ViewBean data;
	public Table fileTable=null;
	private  String[] header=null;
	public List<String> NFiles=new ArrayList<String>();
	public Map<String, String> Stream=new HashMap<String, String>();
	
	public TVersionReleaseInfoView(Composite com,ViewBean bean){
		this.parent=com;
		this.data=bean;
		 self=new Composite(com,SWT.BORDER);
		 self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 self.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 
		 content=new SashForm(self,SWT.VERTICAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 this.createToolsView();
		 this.createListView();
		 content.setWeights(new int[]{35,65});
		 self.pack();
	}
	
	public Button btnDispatch,btnConfirm,btnReject,btnSpan=null;
	public DavidCombo cmbTester=null;
	private void createToolsView(){
		Composite toolPanel=new Composite(content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(10, 5));
		
		Label labName=new Label(toolPanel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_name"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textName=new Text(toolPanel,SWT.BORDER);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		textName.setText(data.getViewName());
		
		Label labStreamName=new Label(toolPanel,SWT.NONE);
		labStreamName.setText(Constants.getStringVaule("label_streamname"));
		labStreamName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
	
		comboStream=new Combo(toolPanel,SWT.DROP_DOWN);
		comboStream.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		if(Context.session.userID.equals(data.getCurrentUserID())&&
				data.getProgress().equals("8")){
			setStreamCombo(comboStream,true,data.getStreamID());
			
			Button btnSetting=new  Button(toolPanel,SWT.PUSH);
			btnSetting.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
			btnSetting.setText("   "+Constants.getStringVaule("btn_chgstream")+"   ");
			btnSetting.addSelectionListener(new ChgViewStreamAction());
		}else{
			setStreamCombo(comboStream,false,data.getStreamID());
		}
		
		if(Context.session.userID.equals(data.getCurrentUserID())&&
				data.getProgress().equals("7")){
			btnConfirm=new  Button(toolPanel,SWT.PUSH);
			btnConfirm.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
			btnConfirm.setText("   "+Constants.getStringVaule("btn_confirm")+"   ");
			btnConfirm.addSelectionListener(new ConfirmViewAction());
		}else{
			if(!data.getProgress().equals("8")){
				//测试分派
				cmbTester=new DavidCombo(toolPanel,SWT.DROP_DOWN);
				cmbTester.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
				 String groupID="Flow"+"3";
				 List<GroupUserBean> users=GROUPUSER.getUsers(groupID);
				 if(users!=null&&users.size()>0){
					  String[] tUsers=new String[users.size()];
					  int index=0;
					  for(GroupUserBean user:users){
						  tUsers[index]=user.getUserFullName();
						  index++;
					  }
					  cmbTester.setItems(tUsers);
				 }
			    btnDispatch=new  Button(toolPanel,SWT.PUSH);
				btnDispatch.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
				btnDispatch.setText("   "+Constants.getStringVaule("btn_dispatch")+"   ");
				btnDispatch.addSelectionListener(new DispatchViewAction());
				//如果当前处理人是自己，才能进行发布的分派提交
				if(!Context.session.userID.equals(data.getCurrentUserID())){
					cmbTester.setVisible(false);
					btnDispatch.setVisible(false);
				}
			}else{
				//布局做调整
				btnSpan=new  Button(toolPanel,SWT.PUSH);
				btnSpan.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
				btnSpan.setVisible(false);
			}
		}
		
	    btnReject=new  Button(toolPanel,SWT.PUSH);
		btnReject.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnReject.setText("   "+Constants.getStringVaule("btn_approveNot")+"   ");
		btnReject.addSelectionListener(new RetunViewAction());
		//如果当前处理人是自己，才能进行发布的分派提交
		if(!Context.session.userID.equals(data.getCurrentUserID())||data.getProgress().equals("8")){
			btnReject.setVisible(false);
		}
		//开发责任人
		 Label labOwner=new Label(toolPanel,SWT.NONE);
		 labOwner.setText("开发"+Constants.getStringVaule("label_taskowner"));
		 labOwner.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtOwner=new Text(toolPanel,SWT.BORDER);
		 txtOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(data.getOwner())){
			 txtOwner.setText(data.getOwnerName());
		 }
		 txtOwner.setEditable(false);
		 
		//测试责任人
		 Label labTOwner=new Label(toolPanel,SWT.NONE);
		 labTOwner.setText("测试"+Constants.getStringVaule("label_taskowner"));
		 labTOwner.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTOwner=new Text(toolPanel,SWT.BORDER);
		 txtTOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(data.getTowner())){
			 txtTOwner.setText(data.getTownerName());
		 }
		 txtTOwner.setEditable(false);
		 
		//方案说明
		Label labDesc=new Label(toolPanel,SWT.NONE);
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textDesc=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 30));
		textDesc.setText(data.getVeiwDesc());
		textDesc.setEditable(false);
		//发版说明
		Label labComment=new Label(toolPanel,SWT.NONE);
		labComment.setText(Constants.getStringVaule("label_editDesc"));
		labComment.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textVersion=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textVersion.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 30));
		textVersion.setForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_RED));//前景色，即文字颜色
		textVersion.setText(data.getVerDesc());
		textVersion.setEditable(false);
		/*Label labAttach=new Label(toolPanel,SWT.NONE);
		labAttach.setText(Constants.getStringVaule("label_descattach"));
		labAttach.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		attachTable=new Table(toolPanel,SWT.BORDER);
		attachTable.setHeaderVisible(true);
		attachTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 40));
		attachTable.setLinesVisible(true);
		attachTable.addMouseListener(new AttachItemAction());
		attachTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
		String[] header=new String[]{"附件"};
		 for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(attachTable,SWT.BORDER);
				String lenheader=StringUtil.rightPad(StringUtil.leftpad(header[i], 100, " "),200," ");
				tablecolumn.setText(lenheader);
			}
		 //there is only one file 
		  List<ViewFileBean> Files=VIEW.getVersionAttachs(data.getViewID(),data.getVersion());
			if(Files!=null&&Files.size()>0){
				  for(ViewFileBean file:Files){
				    	String name=file.getFileName();
				       TableItem tableItem=new TableItem(attachTable,SWT.BORDER);
						tableItem.setText(new String[]{name});
						Image icon=Icons.getFileImage(name);
						tableItem.setImage(icon);
						tableItem.setData(file);
				  }
			}
			for(int j=0;j<header.length;j++){		
				attachTable.getColumn(j).pack();
			}
		 attachTable.pack();	   */
			 
		textInput=new Text(toolPanel,SWT.BORDER);
		textInput.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 7, 1, 0, 0));
		Button btnSearch=new  Button(toolPanel,SWT.PUSH);
		btnSearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
		btnSearch.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnSearch.addSelectionListener(new SearchAction());
		
		Button btnDeploy=new  Button(toolPanel,SWT.PUSH);
		btnDeploy.setText("   "+Constants.getStringVaule("btn_deploy")+"   ");
		btnDeploy.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnDeploy.addSelectionListener(new QuickDeployAction());
		
		Button btnDown=new  Button(toolPanel,SWT.PUSH);
		btnDown.setText("   "+Constants.getStringVaule("btn_download")+"   ");
		btnDown.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnDown.addSelectionListener(new DownloadAction());
		
		
		
		
		textName.setEditable(false);
		textDesc.setEditable(false);
		toolPanel.pack();
	}
	
	public void  setStreamCombo(Combo com,boolean editable,String streamID){
		StreamBean stream=STREAM.getStreamByID(data.getStreamID());
		String streamName=stream.getStreamName();
		if(editable){
			int index=0;
			List<StreamBean> rawdata=STREAM.getStream("doing");
			List<StreamBean> streams=new ArrayList<StreamBean>();
			for(StreamBean bean:rawdata){
				if(bean.getStatus().equals("doing")){
					streams.add(bean);
				}
			}
			if(streams!=null&&streams.size()>0){
		    	String[] items=new String[streams.size()];
		    	int i=0;
		    	for(StreamBean bean:streams){
		    		items[i]=bean.getStreamName();
		    		Stream.put(bean.getStreamName(), bean.getStreamID());
		    		if(bean.getStreamName().equals(streamName)){
		    			index=i;
		    		}
		    		i++;
		    	}
		    	com.setItems(items);
		    	com.select(index);
		    }	
		}else{
			Stream.put(stream.getStreamName(), streamName);
			String[] items=new String[]{streamName};
			com.setItems(items);
			com.select(0);
		}
	}
	
	public static  Color green = AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_GREEN);  
	public static Color gray = AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_GRAY);  
	private void createListView(){
	    Composite pannelData=new Composite(content,SWT.BORDER);
	    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
	    fileTable=new Table(pannelData,SWT.FULL_SELECTION|SWT.MULTI);
	    fileTable.setHeaderVisible(true);
	    fileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	    fileTable.setLinesVisible(true);
	    fileTable.addMouseListener(new FileItemAction());
	    fileTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
	    fileTable.addListener(SWT.EraseItem, new Listener(){
			public void handleEvent(Event event){
				event.detail &= ~SWT.HOT;  
	            if ((event.detail & SWT.SELECTED) == 0) return; /* item not selected */  
	            int clientWidth = fileTable.getClientArea().width;  
	            GC gc = event.gc;  
	            Color oldForeground = gc.getForeground();  
	            Color oldBackground = gc.getBackground();  
	            gc.setForeground(green);  
	            gc.setBackground(gray);  
	            gc.fillGradientRectangle(0, event.y, clientWidth, event.height, false);  
	            gc.setForeground(oldForeground);  
	            gc.setBackground(oldBackground);  
	            event.detail &= ~SWT.SELECTED;  
			}
		});
	    header=new String[]{ StringUtil.rightPad(Constants.getStringVaule("header_filename"), 60, " "),
	    								 StringUtil.rightPad(Constants.getStringVaule("header_mdfdate"),25," ") ,
	    								 StringUtil.rightPad(Constants.getStringVaule("header_crtdate"),25," ") ,
	    								 StringUtil.rightPad(Constants.getStringVaule("header_md5"),50," ")};
	    for(int i=0;i<header.length;i++){
			TableColumn tablecolumn=new TableColumn(fileTable,SWT.BORDER);
			tablecolumn.setText(header[i]);
			tablecolumn.setMoveable(true);
			if(i==0){
				tablecolumn.addSelectionListener(new OrderFilesNameAction());
			}
			if(i==1){
				tablecolumn.addSelectionListener(new OrderFilesMTimeAction());
			}
			if(i==2){
				tablecolumn.addSelectionListener(new OrderFilesCTimeAction());
			}
		}
		
	    List<ViewFileBean> Files=VIEW.getVersionFiles(data.getViewID(),data.getVersion());
	    List<String> LFiles=this.getPrevVersionFiles();
	    List<TableItem> selected=new ArrayList<TableItem>();
	    for(ViewFileBean file:Files){
	    	String name=file.getFileName();
	    	Image icon=Icons.getFileImage(name);
	    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
			tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(), file.getMd5()});
			tableItem.setImage(icon);
			tableItem.setData(file);
			if(!LFiles.contains(name)){
				selected.add(tableItem);
				NFiles.add(name);
			}
	    }
	    if(selected.size()>0){
	    	TableItem[] items=new TableItem[selected.size()];
	    	fileTable.setSelection(selected.toArray(items));
	    }
		for(int j=0;j<header.length;j++){		
			fileTable.getColumn(j).pack();
		}	
		pannelData.pack();
	}
	
	public List<String> getPrevVersionFiles(){
		List<String> Files=new ArrayList<String>();
		try{
			String currV=data.getVersion();
			int  iCurrV=Integer.parseInt(currV);
			if(iCurrV>0){
				iCurrV=iCurrV-1;
				 List<ViewFileBean> files=VIEW.getVersionFiles(data.getViewID(),iCurrV+"");
				 if(files!=null&&files.size()>0){
					 for(ViewFileBean file:files){
						 Files.add(file.getFileName());
					 }
				 }
			}
		}catch(Exception e){
			
		}
		return Files;
	}
	
	
	
	public class SearchAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			String keyword=textInput.getText();
   			if(!StringUtil.isNullOrEmpty(keyword)){
   				TableItem[] items=fileTable.getItems();
   				for(int w=0;w<items.length;w++){
   					ViewFileBean data=(ViewFileBean)items[w].getData();
   					if(data.getFileName().startsWith(keyword)){
   						fileTable.setSelection(items[w]);
   					}
   				}
   			}else{
   				List<TableItem> selections=new ArrayList<TableItem>();
   				TableItem[] items=fileTable.getItems();
   				for(int w=0;w<items.length;w++){
   					ViewFileBean data=(ViewFileBean)items[w].getData();
   					if(NFiles.contains(data.getFileName()))
   						selections.add(items[w]);
   				}
   				if(selections.size()>0){
   		   			TableItem[] sItems=new TableItem[selections.size()];
   		   			fileTable.setSelection(selections.toArray(sItems));
   		   		 } 
   			}
   		}
	}
	
	public class FileItemAction  extends MouseAdapter{
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem tableItem=fileTable.getItem(new  Point(e.x,e.y));
			 if(tableItem!=null){
				 ViewFileBean file=(ViewFileBean)tableItem.getData();
		            boolean downResult=file.download();
		            if(downResult){//下载成功的文件，尝试调用本地程序打开
		            	FileUtils.openFileByLocal(file.getLocation());
		   			}else{
		   				String msg="文件【"+file.getFileName()+"】下载失败。请稍后重试！";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
		   			}
			 }
		 }
	}
	
	public List<String> getSelected(){
		List<String> result=new ArrayList<String>();
		TableItem[] items=fileTable.getSelection();
		if(items!=null&&items.length>0){
			for(TableItem item:items){
				ViewFileBean data=(ViewFileBean)item.getData();
				result.add(data.getFileName());
			}
		}
		return result;
	}
	
	public boolean orderOrignal=true;
	public class OrderFilesNameAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			List<String> selectFiles=getSelected();
   			 TableItem[] items=fileTable.getItems();
   			 List<ViewFileBean> datas=new ArrayList<ViewFileBean>();
   			for(int w=0;w<items.length;w++){
   				ViewFileBean data=(ViewFileBean)items[w].getData();
					datas.add(data);
   			}
   			if(orderOrignal){
	   			  Collections.sort(datas, new Comparator<ViewFileBean>() {
	   	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
	   	                  return arg0.getNameOrder().compareTo(arg1.getNameOrder());
	   	              }
	   			   });
	   			orderOrignal=false;
   			}else{
   			 Collections.sort(datas, new Comparator<ViewFileBean>() {
  	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
  	                  return arg0.getOrignalOrder().compareTo(arg1.getOrignalOrder());
  	              }
  			   });
   			orderOrignal=true;
   			}
   		  fileTable.removeAll();
   		 List<TableItem>  selectItems=new ArrayList<TableItem>();
	   		 for(ViewFileBean file:datas){
	   			String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
				tableItem.setImage(icon);
				tableItem.setData(file);
				if(selectFiles.contains(name))
					selectItems.add(tableItem);
	   		 }
	   		if(selectItems.size()>0){
	   			TableItem[] selections=new TableItem[selectItems.size()];
	   			fileTable.setSelection(selectItems.toArray(selections));
	   		 } 
   		}
	}
	
	public class OrderFilesMTimeAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			 List<String> selectFiles=getSelected();
   			 TableItem[] items=fileTable.getItems();
   			 List<ViewFileBean> datas=new ArrayList<ViewFileBean>();
   			for(int w=0;w<items.length;w++){
   				ViewFileBean data=(ViewFileBean)items[w].getData();
					datas.add(data);
   			}
   			if(orderOrignal){
	   			  Collections.sort(datas, new Comparator<ViewFileBean>() {
	   	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
	   	                  return arg0.getFileTime().compareTo(arg1.getFileTime());
	   	              }
	   			   });
	   			orderOrignal=false;
   			}else{
   			 Collections.sort(datas, new Comparator<ViewFileBean>() {
  	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
  	                  return arg0.getOrignalOrder().compareTo(arg1.getOrignalOrder());
  	              }
  			   });
   			orderOrignal=true;
   			}
   		  fileTable.removeAll();
   		  List<TableItem>  selectItems=new ArrayList<TableItem>();
	   		 for(ViewFileBean file:datas){
	   			String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
				tableItem.setImage(icon);
				tableItem.setData(file);
				if(selectFiles.contains(name))
					selectItems.add(tableItem);
	   		 }
	   		if(selectItems.size()>0){
	   			TableItem[] selections=new TableItem[selectItems.size()];
	   			fileTable.setSelection(selectItems.toArray(selections));
	   		 } 
   		}
	}
	
	public class OrderFilesCTimeAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			 List<String> selectFiles=getSelected();
   			 TableItem[] items=fileTable.getItems();
   			 List<ViewFileBean> datas=new ArrayList<ViewFileBean>();
   			for(int w=0;w<items.length;w++){
   				ViewFileBean data=(ViewFileBean)items[w].getData();
					datas.add(data);
   			}
   			if(orderOrignal){
	   			  Collections.sort(datas, new Comparator<ViewFileBean>() {
	   	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
	   	                  return arg0.getCrtTime().compareTo(arg1.getCrtTime());
	   	              }
	   			   });
	   			orderOrignal=false;
   			}else{
   			 Collections.sort(datas, new Comparator<ViewFileBean>() {
  	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
  	                  return arg0.getOrignalOrder().compareTo(arg1.getOrignalOrder());
  	              }
  			   });
   			orderOrignal=true;
   			}
   		  fileTable.removeAll();
   		List<TableItem>  selectItems=new ArrayList<TableItem>();
	   		 for(ViewFileBean file:datas){
	   			String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
				tableItem.setImage(icon);
				tableItem.setData(file);
				if(selectFiles.contains(name))
					selectItems.add(tableItem);
	   		 }
	   		if(selectItems.size()>0){
	   			TableItem[] selections=new TableItem[selectItems.size()];
	   			fileTable.setSelection(selectItems.toArray(selections));
	   		 }  
   		}
	}
	
	public class QuickDeployAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			TableItem[] selections= fileTable.getSelection();
   			if(selections!=null&&selections.length>0){
   				List<ViewFileBean> tabList=new ArrayList<ViewFileBean>();
				List<ViewFileBean> initList=new ArrayList<ViewFileBean>();
				List<ViewFileBean> otherList=new ArrayList<ViewFileBean>();
   				boolean hasDbFiles=false;
   				boolean hasOFiles=false;
   				for(TableItem item:selections){
   					ViewFileBean data=(ViewFileBean)item.getData();
   					String suffix=FileUtils.getFileSuffix(data.getFileName());
   					if("SQL".equalsIgnoreCase(suffix)){
   						hasDbFiles=true;
   					}
   					if(!"SQL".equalsIgnoreCase(suffix)){
   						hasOFiles=true;
   					}
   					if(((data.getFileName()).toLowerCase()).indexOf("table")!=-1){
   						tabList.add(data);
   					}else if(((data.getFileName()).toLowerCase()).indexOf("init")!=-1){
   						initList.add(data);
   					}else{
   						otherList.add(data);
   					}
   				}
   				if(hasDbFiles&&hasOFiles){
	   				 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					 box.setText(Constants.getStringVaule("messagebox_alert"));
					 box.setMessage("快速部署不能同时选择多种类型的文件(建议一次选择一种类型的文件进行快速部署)");
					 box.open();
   				}else{
   					List<ViewFileBean> files=new ArrayList<ViewFileBean>();
   					//脚本临时分类
   					if(tabList.size()>0){
   						for(ViewFileBean data:tabList)
   							files.add(data);
   					}
   					if(initList.size()>0){
   						for(ViewFileBean data:initList)
   							files.add(data);
   					}
   					if(otherList.size()>0){
   						for(ViewFileBean data:otherList)
   							files.add(data);
   					}
   					TVersionQuickDeployView.getInstance().show(files);
   				}
   			}else{
	   			 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 box.setText(Constants.getStringVaule("messagebox_alert"));
				 box.setMessage("请选择需要部署的文件(一次只能部署一个组件)");
				 box.open();
   			}
   		}
	}
	 
	 public class DownloadAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			TableItem[] items=fileTable.getSelection();
	   			 if(items==null||items.length<=0){		
	   				 items=fileTable.getItems();
	   			 }
	   			String localDir="";
	   			 List<String> errorList=new ArrayList<String>();
					for(int w=0;w<items.length;w++){
						ViewFileBean file=(ViewFileBean)items[w].getData();
						if(StringUtil.isNullOrEmpty(localDir)){
							localDir=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+file.getStreamName()+File.separator+file.getViewName()+File.separator+file.getVersionID();
						}
			            boolean downResult=file.download();
					   if(!downResult){
						   errorList.add(file.getFileName());
					   }
					}
					if(errorList!=null&&errorList.size()>0){
						   String fileName="";
						    for(String line:errorList){
						    	fileName=fileName+line+"\r\n";
						    }
						    String msg="文件【"+fileName+"】下载失败。请稍后重试！";
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(msg);
							box.open();
					}else{
							String msg="文件下载完成。可以点击【确定】在目录【"+localDir+"】查看。";
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(msg);
							int choice=box.open();
							if(SWT.OK==choice){
								FileDialog dialog=new FileDialog(AppView.getInstance().getShell(),SWT.OPEN);
								dialog.setFilterPath(localDir);
								dialog.setFilterNames(new String[]{"All Files(*.*)"});
								dialog.open();
							}
					}
	   		}
	 }
	 
		public class DispatchViewAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String testers=cmbTester.getText();
	   			if(!StringUtil.isNullOrEmpty(testers)){
	   				String  firstUserID="";
	   				String[] users=testers.split(",");
	   				for(String user:users){
	   					//遍历生成通知和测试任务
	   					String userID=StringUtil.getUserIdFromName(user);
	   					if(StringUtil.isNullOrEmpty(firstUserID))
	   						firstUserID=userID;
	   					 //生成测试任务
	   					String uuid=UUID.randomUUID().toString();
	   					uuid=uuid.replace("-", "");
	   					TTaskBean task=new TTaskBean();
	   					task.setViewID(data.getViewID());
	   					task.setId(uuid);
	   					task.setUserID(userID);
	   					task.setStatus(TTaskBean.Status.Todo.ordinal()+"");
	   					task.setCrtUser(Context.session.userID);
	   					task.add();
	   					//通知下一处理人
	   					String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"分配发布【"+data.getViewName()+"】给您。请查阅。";
	   					IMessage message=new IMessage(userID,msg);
	   					message.addMsg();
	   					String alterMsg="发布【"+data.getViewName()+"】分派成功!";
	   				}
	   				data.SetTowner(firstUserID);//设置测试责任人
	   				data.setCurrentUserID(firstUserID);
	   				data.setProgress("6");//详细测试
	   				data.setLastProgress("5");
	   				//data.reSetUptFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
	   				data.submit();
	   			  //发布关联的需求同步滚动
	   				List<TaskBean> reqs=TASK.getViewReqs(data.getViewID());
	   				if(reqs!=null&&reqs.size()>0){
	   					for(TaskBean req:reqs){
	   						String seq=UUID.randomUUID().toString();
	   						seq=seq.replace("-", "");
	   						req.logStep(seq, Context.session.userID, "测试安排", "5");
	   						req.progress("6", firstUserID, Context.session.userID);
	   						//req.ResetReleaseFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
		   					//开发任务对应的需求记录需要更新状态和当前处理人
						   BACKLOG.updateBackLog(req.getReqID(), Context.session.userID, BackLogBean.Status.Test.ordinal()+"", "2");
	   					}
	   				}
	   				
	   				btnDispatch.setEnabled(false);
	   				btnReject.setEnabled(false);
	   				String alterMsg="发布【"+data.getViewName()+"】分派成功!";
	   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
	   				box.setText(Constants.getStringVaule("messagebox_alert"));
	   				box.setMessage(alterMsg);
	   				box.open();
	   				TVersionReleaseView.getInstance(null).refreshTree();
	   			}else{
	   				String alterMsg="分派测试任务需要先选择测试人员";
	   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
	   				box.setText(Constants.getStringVaule("messagebox_alert"));
	   				box.setMessage(alterMsg);
	   				box.open();
	   			}
	   		}
		}
	 
		//测试审核通过
	   public class ConfirmViewAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			data.setCurrentUserID(Context.session.userID);
	   			data.setLastProgress(VStep.VersionCheck);
	   			data.setProgress(VStep.VersionPackage);
	   			data.submit();
	   			List<TaskBean> reqs=TASK.getViewReqs(data.getViewID());
	   			if(reqs!=null&&reqs.size()>0){
					for(TaskBean req:reqs){
						String seq=UUID.randomUUID().toString();
						seq=seq.replace("-", "");
						req.logStep(seq, Context.session.userID, "测试审核通过(待上线)", VStep.VersionCheck);
						//开发任务的状态更新
						req.progress(VStep.VersionPackage, Context.session.userID, Context.session.userID);
						//开发任务对应的需求记录需要更新状态和当前处理人
						BACKLOG.updateBackLog(req.getReqID(), Context.session.userID, "A", "2");
						//同步禅到中需求状态
						ZenDaoApi.getInstance().setReqStatus(req.getReqID(), "tested");
					}
				}
	   			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(Constants.getStringVaule("alert_successoperate"));
				box.open();	
	   			TVersionReleaseView.getInstance(null).refreshTree();
	   		}
	   }
	  
	   public class ChgViewStreamAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String streamName=comboStream.getText();
	   			if(!StringUtil.isNullOrEmpty(streamName)){
	   				String streamID=Stream.get(streamName);
	   				data.setStreamID(streamID);
	   				data.chgStream();
	   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_successoperate"));
					box.open();	
					//调整发布的批次，同时调整发布中任务的所属批次
					List<TaskBean> reqs=TASK.getViewReqs(data.getViewID());
		   			if(reqs!=null&&reqs.size()>0){
						for(TaskBean req:reqs){
							ZenDaoApi.getInstance().setReqPlan(req.getReqID(), streamID);
							ZenDaoApi.getInstance().setTaskPlan(req.getId(), streamID);
						}
		   			}
	   			}
	   		}
	   }
	   
	   
		public Shell promptShell;
		public Text textComment;
		public void createPrompt(){
			promptShell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
			Point curPoint=AppView.getInstance().getDisplay().getCurrent().getCursorLocation();
			promptShell.setLocation(new Point(curPoint.x-300,curPoint.y-20));
			promptShell.setLayout(LayoutUtils.getComGridLayout(1, 0));
			Button btnOK=new  Button(promptShell,SWT.PUSH);
			btnOK.setText("   "+Constants.getStringVaule("btn_ok")+"   ");
			btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
			btnOK.addSelectionListener(new ApplyRetunAction());
			
			Group group=new Group(promptShell,SWT.SINGLE|SWT.V_SCROLL);
			group.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 300, 80));
			group.setLayout(LayoutUtils.getComGridLayout(1, 0));
			group.setText(Constants.getStringVaule("group_comment"));
			textComment=new Text(group,SWT.MULTI|SWT.V_SCROLL);
			textComment.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			group.pack();
			promptShell.pack();
			promptShell.open();
			promptShell.addShellListener(new ShellCloseAction());
		}
		
		public class ShellCloseAction extends ShellAdapter{
			public void shellClosed(ShellEvent e){	
				promptShell.dispose();
			}
		}
		
		public class ApplyRetunAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String cmt=textComment.getText();
	   			if(StringUtil.isNullOrEmpty(cmt)){
	   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("请提供修改意见");
					box.open();	
					return;
	   			}else{
		   			//拒绝要提供修改意见
		   			List<TaskBean> reqs=TASK.getViewReqs(data.getViewID());
		   			if(reqs!=null){
		   				String  lastProcess=data.getLastProgress();
		   				if("6".equals(lastProcess)){//return to tester 
		   					for(TaskBean req:reqs){
		   						String seq=UUID.randomUUID().toString();
		   						seq=seq.replace("-", "");
		   						req.logStep(seq, Context.session.userID, cmt, "7");
		   						req.progress("6", data.getUptUser(), Context.session.userID);
		   						String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"退回发布【"+req.getTname()+"】给您。请查阅。";
		   						IMessage message=new IMessage(data.getUptUser(),msg);
		   						message.addMsg();
		   					}
		   					data.setCurrentUserID(data.getUptUser());
		   					data.setProgress("6");//详细测试
		   					data.setLastProgress("7");
		   					data.submit();
		   					//发布对应的测试任务重新激活
		   					List<TTaskBean> tasks= data.getTestTasks();
		   					for(TTaskBean task:tasks){
		   						task.SetStatus(TTaskBean.Status.Todo.ordinal()+"");
		   					}
		   				}
		   				else{ //return to developer
		   					for(TaskBean req:reqs){
		   						String seq=UUID.randomUUID().toString();
		   						seq=seq.replace("-", "");
		   						req.logStep(seq, Context.session.userID, cmt, "5");
		   						req.progress("2", req.getOwner(), Context.session.userID);
		   						String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"退回需求【"+req.getTname()+"】给您。请查阅。";
		   						IMessage message=new IMessage(req.getOwner(),msg);
		   						message.addMsg();
		   					}
		   					//发布本身停留在待版本整合
		   					data.setCurrentUserID(data.getCrtUser());
		   					data.setProgress("4");//详细测试
		   					data.setLastProgress("5");
		   					data.submit();
		   					String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"退回发布【"+data.getViewName()+"】给您。请查阅。";
							IMessage message=new IMessage(data.getCrtUser(),msg);
							message.addMsg();
		   				}
		   			}
	   				promptShell.dispose();
		   			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_successoperate"));
					box.open();	
					TVersionReleaseView.getInstance(null).refreshTree();
					if(btnDispatch!=null){
						btnDispatch.setEnabled(false);
					}if(btnConfirm!=null){
						btnConfirm.setEnabled(false);
					}if(btnReject!=null){
						btnReject.setEnabled(false);
					}
					return;
		   		}
	   		 }
		}
		
		
		public class RetunViewAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			createPrompt();
	   		}
		}
		

		
}
