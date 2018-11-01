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
				//���Է���
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
				//�����ǰ���������Լ������ܽ��з����ķ����ύ
				if(!Context.session.userID.equals(data.getCurrentUserID())){
					cmbTester.setVisible(false);
					btnDispatch.setVisible(false);
				}
			}else{
				//����������
				btnSpan=new  Button(toolPanel,SWT.PUSH);
				btnSpan.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
				btnSpan.setVisible(false);
			}
		}
		
	    btnReject=new  Button(toolPanel,SWT.PUSH);
		btnReject.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnReject.setText("   "+Constants.getStringVaule("btn_approveNot")+"   ");
		btnReject.addSelectionListener(new RetunViewAction());
		//�����ǰ���������Լ������ܽ��з����ķ����ύ
		if(!Context.session.userID.equals(data.getCurrentUserID())||data.getProgress().equals("8")){
			btnReject.setVisible(false);
		}
		//����������
		 Label labOwner=new Label(toolPanel,SWT.NONE);
		 labOwner.setText("����"+Constants.getStringVaule("label_taskowner"));
		 labOwner.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtOwner=new Text(toolPanel,SWT.BORDER);
		 txtOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(data.getOwner())){
			 txtOwner.setText(data.getOwnerName());
		 }
		 txtOwner.setEditable(false);
		 
		//����������
		 Label labTOwner=new Label(toolPanel,SWT.NONE);
		 labTOwner.setText("����"+Constants.getStringVaule("label_taskowner"));
		 labTOwner.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTOwner=new Text(toolPanel,SWT.BORDER);
		 txtTOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(data.getTowner())){
			 txtTOwner.setText(data.getTownerName());
		 }
		 txtTOwner.setEditable(false);
		 
		//����˵��
		Label labDesc=new Label(toolPanel,SWT.NONE);
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textDesc=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 30));
		textDesc.setText(data.getVeiwDesc());
		textDesc.setEditable(false);
		//����˵��
		Label labComment=new Label(toolPanel,SWT.NONE);
		labComment.setText(Constants.getStringVaule("label_editDesc"));
		labComment.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textVersion=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textVersion.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 30));
		textVersion.setForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_RED));//ǰ��ɫ����������ɫ
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
		String[] header=new String[]{"����"};
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
		            if(downResult){//���سɹ����ļ������Ե��ñ��س����
		            	FileUtils.openFileByLocal(file.getLocation());
		   			}else{
		   				String msg="�ļ���"+file.getFileName()+"������ʧ�ܡ����Ժ����ԣ�";
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
					 box.setMessage("���ٲ�����ͬʱѡ��������͵��ļ�(����һ��ѡ��һ�����͵��ļ����п��ٲ���)");
					 box.open();
   				}else{
   					List<ViewFileBean> files=new ArrayList<ViewFileBean>();
   					//�ű���ʱ����
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
				 box.setMessage("��ѡ����Ҫ������ļ�(һ��ֻ�ܲ���һ�����)");
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
						    String msg="�ļ���"+fileName+"������ʧ�ܡ����Ժ����ԣ�";
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(msg);
							box.open();
					}else{
							String msg="�ļ�������ɡ����Ե����ȷ������Ŀ¼��"+localDir+"���鿴��";
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
	   					//��������֪ͨ�Ͳ�������
	   					String userID=StringUtil.getUserIdFromName(user);
	   					if(StringUtil.isNullOrEmpty(firstUserID))
	   						firstUserID=userID;
	   					 //���ɲ�������
	   					String uuid=UUID.randomUUID().toString();
	   					uuid=uuid.replace("-", "");
	   					TTaskBean task=new TTaskBean();
	   					task.setViewID(data.getViewID());
	   					task.setId(uuid);
	   					task.setUserID(userID);
	   					task.setStatus(TTaskBean.Status.Todo.ordinal()+"");
	   					task.setCrtUser(Context.session.userID);
	   					task.add();
	   					//֪ͨ��һ������
	   					String msg=Context.session.userName+"("+Context.session.userID+")��"+DateUtil.getCurrentTime()+"���䷢����"+data.getViewName()+"������������ġ�";
	   					IMessage message=new IMessage(userID,msg);
	   					message.addMsg();
	   					String alterMsg="������"+data.getViewName()+"�����ɳɹ�!";
	   				}
	   				data.SetTowner(firstUserID);//���ò���������
	   				data.setCurrentUserID(firstUserID);
	   				data.setProgress("6");//��ϸ����
	   				data.setLastProgress("5");
	   				//data.reSetUptFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
	   				data.submit();
	   			  //��������������ͬ������
	   				List<TaskBean> reqs=TASK.getViewReqs(data.getViewID());
	   				if(reqs!=null&&reqs.size()>0){
	   					for(TaskBean req:reqs){
	   						String seq=UUID.randomUUID().toString();
	   						seq=seq.replace("-", "");
	   						req.logStep(seq, Context.session.userID, "���԰���", "5");
	   						req.progress("6", firstUserID, Context.session.userID);
	   						//req.ResetReleaseFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
		   					//���������Ӧ�������¼��Ҫ����״̬�͵�ǰ������
						   BACKLOG.updateBackLog(req.getReqID(), Context.session.userID, BackLogBean.Status.Test.ordinal()+"", "2");
	   					}
	   				}
	   				
	   				btnDispatch.setEnabled(false);
	   				btnReject.setEnabled(false);
	   				String alterMsg="������"+data.getViewName()+"�����ɳɹ�!";
	   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
	   				box.setText(Constants.getStringVaule("messagebox_alert"));
	   				box.setMessage(alterMsg);
	   				box.open();
	   				TVersionReleaseView.getInstance(null).refreshTree();
	   			}else{
	   				String alterMsg="���ɲ���������Ҫ��ѡ�������Ա";
	   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
	   				box.setText(Constants.getStringVaule("messagebox_alert"));
	   				box.setMessage(alterMsg);
	   				box.open();
	   			}
	   		}
		}
	 
		//�������ͨ��
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
						req.logStep(seq, Context.session.userID, "�������ͨ��(������)", VStep.VersionCheck);
						//���������״̬����
						req.progress(VStep.VersionPackage, Context.session.userID, Context.session.userID);
						//���������Ӧ�������¼��Ҫ����״̬�͵�ǰ������
						BACKLOG.updateBackLog(req.getReqID(), Context.session.userID, "A", "2");
						//ͬ������������״̬
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
					//�������������Σ�ͬʱ�����������������������
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
					box.setMessage("���ṩ�޸����");
					box.open();	
					return;
	   			}else{
		   			//�ܾ�Ҫ�ṩ�޸����
		   			List<TaskBean> reqs=TASK.getViewReqs(data.getViewID());
		   			if(reqs!=null){
		   				String  lastProcess=data.getLastProgress();
		   				if("6".equals(lastProcess)){//return to tester 
		   					for(TaskBean req:reqs){
		   						String seq=UUID.randomUUID().toString();
		   						seq=seq.replace("-", "");
		   						req.logStep(seq, Context.session.userID, cmt, "7");
		   						req.progress("6", data.getUptUser(), Context.session.userID);
		   						String msg=Context.session.userName+"("+Context.session.userID+")��"+DateUtil.getCurrentTime()+"�˻ط�����"+req.getTname()+"������������ġ�";
		   						IMessage message=new IMessage(data.getUptUser(),msg);
		   						message.addMsg();
		   					}
		   					data.setCurrentUserID(data.getUptUser());
		   					data.setProgress("6");//��ϸ����
		   					data.setLastProgress("7");
		   					data.submit();
		   					//������Ӧ�Ĳ����������¼���
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
		   						String msg=Context.session.userName+"("+Context.session.userID+")��"+DateUtil.getCurrentTime()+"�˻�����"+req.getTname()+"������������ġ�";
		   						IMessage message=new IMessage(req.getOwner(),msg);
		   						message.addMsg();
		   					}
		   					//��������ͣ���ڴ��汾����
		   					data.setCurrentUserID(data.getCrtUser());
		   					data.setProgress("4");//��ϸ����
		   					data.setLastProgress("5");
		   					data.submit();
		   					String msg=Context.session.userName+"("+Context.session.userID+")��"+DateUtil.getCurrentTime()+"�˻ط�����"+data.getViewName()+"������������ġ�";
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
