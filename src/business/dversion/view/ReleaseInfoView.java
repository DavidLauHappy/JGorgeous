package business.dversion.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import model.BACKLOG;
import model.QUESTIONS;
import model.STREAM;
import model.TASK;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;


import bean.BackLogBean;
import bean.QUESTIONSBean;
import bean.RequirementLog;
import bean.StreamBean;
import bean.TaskBean;
import bean.VStep;
import bean.ViewBean;
import bean.ViewFileBean;
import business.dversion.action.DropTargetAction;




import resource.Constants;
import resource.Context;
import resource.FtpFileService;
import resource.IMessage;
import resource.Icons;
import resource.Paths;

import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

public class ReleaseInfoView {
	public Composite self=null;
	public SashForm content=null;
	public Composite parent=null;
	private Text textName,textDesc,textEditDesc,textInput,comboStream;
	public Button btnRelease;
	public enum Type{View,Edit;}
	private Type opType;
	private ViewBean data;
	public Table fileTable=null;
	private  String[] header=null;
	private Map<String,ViewFileBean> Records;//��¼��ʷ��¼�����ڱȽϱ仯��
	private List<ViewFileBean> DeleteFiles;//��¼ɾ�����ļ�
	public Table attachTable=null;//�޸�˵��
	
	public ReleaseInfoView(Composite com,Type type,ViewBean bean){
		this.parent=com;
		this.opType=type;
		this.data=bean;
		Records=new HashMap<String, ViewFileBean>();
		DeleteFiles=new ArrayList<ViewFileBean>();
		 self=new Composite(com,SWT.NONE);
		 self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 self.setLayout(LayoutUtils.getComGridLayout(1, 1));;
		 
		 content=new SashForm(self,SWT.VERTICAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 this.createToolsView();
		 this.createListView();
		 content.setWeights(new int[]{40,60});
		 self.pack();
	}
	
	private void createToolsView(){
		Composite toolPanel=new Composite(content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(10, 5));
		
		Label labName=new Label(toolPanel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_name"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textName=new Text(toolPanel,SWT.BORDER);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		textName.setText(data.getViewName());
		
		Label labStreamName=new Label(toolPanel,SWT.NONE);
		labStreamName.setText(Constants.getStringVaule("label_streamname"));
		labStreamName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
	
		comboStream=new Text(toolPanel,SWT.BORDER);
		comboStream.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		String streamID=data.getStreamID();
	 	StreamBean stream=STREAM.getStreamByID(streamID);
		String streamName=stream.getStreamName();
		comboStream.setText(streamName);
	
		btnRelease=new  Button(toolPanel,SWT.PUSH);
		btnRelease.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
		btnRelease.setText("   "+Constants.getStringVaule("btn_release")+"   ");
		btnRelease.addSelectionListener(new RefreshReleaseAction());
		
		Label labDesc=new Label(toolPanel,SWT.NONE);
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textDesc=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 0));
		textDesc.setText(data.getVeiwDesc());
		
		Label labEditDesc=new Label(toolPanel,SWT.NONE);
		labEditDesc.setText(Constants.getStringVaule("label_editDesc"));
		labEditDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textEditDesc=new Text(toolPanel,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP|SWT.BORDER);//
		textEditDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 10, 100));
		textEditDesc.setForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_RED));//ǰ��ɫ����������ɫ
		if(!StringUtil.isNullOrEmpty(data.getVerDesc())){
			textEditDesc.setText(data.getVerDesc());
		}
		textEditDesc.setEditable(false);
		
		
		textInput=new Text(toolPanel,SWT.BORDER);
		textInput.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 7, 1, 0, 0));
		Button btnSearch=new  Button(toolPanel,SWT.PUSH);
		btnSearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
		btnSearch.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnSearch.addSelectionListener(new SearchAction());
		
		Button btnDown=new  Button(toolPanel,SWT.PUSH);
		btnDown.setText("   "+Constants.getStringVaule("btn_download")+"   ");
		btnDown.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnDown.addSelectionListener(new DownloadAction());
		
		Button btnDelete=new  Button(toolPanel,SWT.PUSH);
		btnDelete.setText("   "+Constants.getStringVaule("btn_delete")+"   ");
		btnDelete.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnDelete.addSelectionListener(new DeleteFileAtion());
		
		comboStream.setEditable(false);
		textName.setEditable(false);
		if(this.opType.equals(Type.Edit)){
			textDesc.setEditable(true);
			//��ѡ����
			btnRelease.setVisible(true);
			btnDelete.setVisible(true);
		}else{
			textDesc.setEditable(false);
			btnRelease.setVisible(false);
			btnDelete.setVisible(false);
		}
		toolPanel.pack();
	}
	
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
	    header=new String[]{ 
	    								 StringUtil.rightPad(Constants.getStringVaule("header_filename"), 60, " "),
	    								 StringUtil.rightPad(Constants.getStringVaule("header_mdfdate"),25," ") ,
	    								 StringUtil.rightPad(Constants.getStringVaule("header_crtdate"),25," ") ,
	    								 StringUtil.rightPad(Constants.getStringVaule("header_md5"),50," ")
	    								 };
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
	    for(ViewFileBean file:Files){
	    	String name=file.getFileName();
	    	Image icon=Icons.getFileImage(name);
	    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
			tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
			tableItem.setImage(icon);
			tableItem.setData(file);
			String key=name+"|"+file.getFileTime()+"|"+file.getMd5();
			Records.put(key, file);
	    }
	    
		for(int j=0;j<header.length;j++){		
			fileTable.getColumn(j).pack();
			
		}	
		if(opType.equals(Type.Edit)){
			   DropTarget targetTable=new DropTarget(fileTable,DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
			   targetTable.setTransfer(new Transfer[]{FileTransfer.getInstance()});
			   targetTable.addDropListener(new DropTargetAction(fileTable));
		}
		pannelData.pack();
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
   			}
   		}
	}
	
	public class AttachItemAction extends MouseAdapter{
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem tableItem=attachTable.getItem(new  Point(e.x,e.y));
			 if(tableItem!=null){
				  ViewFileBean file=(ViewFileBean)tableItem.getData();
				    if((ViewFileBean.Mode.Local.ordinal()+"").equals(file.getFileMode())){
				    	FileUtils.openFileByLocal(file.getLocation());
				    }else{
			            boolean downResult=file.downloadAtttach();
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
	}
	
	public class FileItemAction extends MouseAdapter{
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem tableItem=fileTable.getItem(new  Point(e.x,e.y));
			 if(tableItem!=null){
				 ViewFileBean file=(ViewFileBean)tableItem.getData();
				   if(file.getFileMode().equals(ViewFileBean.Mode.Remote.ordinal()+"")){
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
				   }else{
						FileUtils.openFileByLocal(file.getLocation());
				   }
			 }
		 }
	}


	public boolean orderOrignal=true;
	public class OrderFilesNameAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
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
	   		 for(ViewFileBean file:datas){
	 	    	String name=file.getFileName();
	 	    	Image icon=Icons.getFileImage(name);
	 	    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
	 			tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
	 			tableItem.setImage(icon);
	 			tableItem.setData(file);
	   		 }
	   		for(int j=0;j<header.length;j++){		
				fileTable.getColumn(j).pack();
			}
   		}
	}
	
	public class OrderFilesMTimeAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
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
    		 for(ViewFileBean file:datas){
 	 	    	String name=file.getFileName();
 	 	    	Image icon=Icons.getFileImage(name);
 	 	    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
 	 			tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
 	 			tableItem.setImage(icon);
 	 			tableItem.setData(file);
 	   		 }
 	   		for(int j=0;j<header.length;j++){		
 				fileTable.getColumn(j).pack();
 			}
   		}
	}
	
	public class OrderFilesCTimeAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
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
    		 for(ViewFileBean file:datas){
 	 	    	String name=file.getFileName();
 	 	    	Image icon=Icons.getFileImage(name);
 	 	    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
 	 			tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
 	 			tableItem.setImage(icon);
 	 			tableItem.setData(file);
 	   		 }
 	   		for(int j=0;j<header.length;j++){		
 				fileTable.getColumn(j).pack();
 			}
   		}
	}
	
	/*�����°汾��Ҫ��
						�ļ��������
						�����ļ���¼
						ɾ���ļ���¼*/
	 public class RefreshReleaseAction extends SelectionAdapter{
		 public void widgetSelected(SelectionEvent e){
			 boolean recordChg=false;
			 //�ж��ļ��б����ޱ仯��û�仯�����Բ��ϴ��ļ�ֻ���¼�¼����
			 List<ViewFileBean> AddFiles=new ArrayList<ViewFileBean>();
			 List<ViewFileBean> SameFiles=new ArrayList<ViewFileBean>();
			 TableItem[] items=	fileTable.getItems();
				if(items!=null&&items.length>0){
					for(int i=0;i<items.length;i++){
						ViewFileBean data=(ViewFileBean)items[i].getData();
						 String key=data.getFileName()+"|"+data.getFileTime()+"|"+data.getMd5();
						 if(!Records.containsKey(key)){
							 recordChg=true;
							 AddFiles.add(data);
						 }else{
							 SameFiles.add(data); //�洢�汾��¼����
						 }
					}
				}
				//�°汾���ܷ�����ɾ����¼
				if(items.length<Records.size()){
					recordChg=true;
				}
				if(!recordChg){
					String msg="�������ļ�δ�����仯���޷��������·�����";
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					box.open();
					return;
				}else{//�汾��ͼ���ڱ仯
					String version=data.getNewVersion();
					String viewDesc=textDesc.getText();
					data.setVeiwDesc(viewDesc);
					if(viewFilesProcess(AddFiles,SameFiles)){
						 skipToNext();
						 btnRelease.setEnabled(false);
						 //���·��������㷢����Ӧ����Ĺ��̰汾��
						 List<TaskBean> tasks=TASK.getViewReqs(data.getViewID());
						 if(tasks!=null&&tasks.size()>0){
							 for(TaskBean task:tasks){
								 TASK.releaseAdd(task.getId());
							 }
						 }
					}else{
						String msg="�������ļ�δ�����仯���޷��������·�����";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
						return;
					}
				}				
		 }
	 }
	 
	 public boolean viewFilesProcess(List<ViewFileBean> addFiles ,List<ViewFileBean> sameFiles){
		 boolean result=true;
		//�����ļ�
			if(addFiles!=null&&addFiles.size()>0){
				for(ViewFileBean File:addFiles){
					File.setVersionID(data.getVersion());
					 String fileID=File.getFileID();
					 if(StringUtil.isNullOrEmpty(fileID)){
						 fileID=UUID.randomUUID().toString();
						 File.setFileID(fileID);
						 File.setViewID(data.getViewID());
						 File.setVersionID(data.getVersion());
						 File.setStreamID(data.getStreamID());
						 File.setMdfUser(Context.session.userID);
						 File.setMdfTime(DateUtil.getCurrentTime());
					 }
					String md5=File.getMd5();
					boolean exist=File.md5Exist(md5);
					if(!exist){
						boolean upRet=FtpFileService.getService().upLoad(File.getLocation(),File.getRemotePath());
						if(upRet){
							File.saveFileObj();
							File.setFileMode(ViewFileBean.Mode.Remote.ordinal()+"");
							File.setStatus(ViewFileBean.Status.New.ordinal()+"");
						}
						else{
							result=false;
							 //errorMsg+="�ļ���"+File.getLocation()+"���ϴ�ʧ�ܣ�";
						}
					}else{
						File.setStatus(ViewFileBean.Status.Update.ordinal()+"");	
					}
					File.save();
				}
			}
			//δ�����仯���ļ�
			if(sameFiles!=null&&sameFiles.size()>0){
				for(ViewFileBean File:sameFiles){
					 File.setViewID(data.getViewID());
					 File.setVersionID(data.getVersion());
					 File.setMdfUser(Context.session.userID);
					 File.setMdfTime(DateUtil.getCurrentTime());
					 File.setStreamID(data.getStreamID());
					 File.save();
				}
			}
			//ɾ���ļ�
			if(DeleteFiles!=null&&DeleteFiles.size()>0){
				for(ViewFileBean File:DeleteFiles){
					File.setViewID(data.getVersion());
					File.setStatus(ViewFileBean.Status.Delete.ordinal()+"");
					File.setMdfUser(Context.session.userID);
					File.setMdfTime(DateUtil.getCurrentTime());
					File.setViewID(data.getViewID());
					File.remove();
				}
			}
		 return result;
	 }
	 
	 
	 //��ͼ�����°汾
	 //��ȡ�����еĵ�ǰ������
	 public void skipToNext(){
		 List<String> testers=new ArrayList();
		 List<TaskBean> reqs=TASK.getViewReqs(data.getViewID());
		 if(reqs!=null&&reqs.size()>0){
			 for(TaskBean req:reqs){
				 //�����Ӧ���������Ϊ����������֤��
				 List<QUESTIONSBean> questions=QUESTIONS.getQuestionByReq(req.getId());
				 if(questions!=null&&questions.size()>0){
					 for(QUESTIONSBean que:questions){
						 if("6".equals(que.getQstatus())){
							 que.reSetStatus("3");
							 testers.add(que.getCrtUser());
						 }
					 }
				 }	 
			
			 }
			//��ͼ�����°汾
			data.createVersion();
			//����֪ͨ��Ϣ
			String currentTime=DateUtil.getCurrentTime();
			String name=data.getViewName();
			//�����ͼ��ǰ�Ĵ����� �������Ѽ���
			if(testers!=null&&testers.size()>0){
				for(String userID:testers){
					String msg=Context.session.userName+"("+Context.session.userID+")��"+currentTime+"���¡�"+name+"���汾����ġ�";
					IMessage message=new IMessage(userID,msg);
					message.addMsg();
				}
			}else{
				String currUserID=data.getCurrentUserID();
				String msg=Context.session.userName+"("+Context.session.userID+")��"+currentTime+"���¡�"+name+"���汾����ġ�";
				IMessage message=new IMessage(currUserID,msg);
				message.addMsg();
			}
			//�����󣬸�����ͼ������ķ���״̬
			data.reSetUptFlag(ViewBean.ReleaseStatus.Organized.ordinal()+"");
			 if(reqs!=null&&reqs.size()>0){
				 for(TaskBean req:reqs){
					 req.ResetReleaseFlag(TaskBean.ReleaseStatus.Organized.ordinal()+"");
						//��¼��־
					    String seq=UUID.randomUUID().toString();
						seq=seq.replace("-", "");
						req.logStep(seq, Context.session.userID, "�汾����", VStep.VersionMake);
				 }
			 }
			
			String successMsg="���·����ɹ���";
			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
			box.setText(Constants.getStringVaule("messagebox_alert"));
			box.setMessage(successMsg);
			box.open();
			ReleaseView.getInstance(null).refreshTree();
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
	 
	 public class DeleteFileAtion extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			TableItem[] deleteItems=fileTable.getSelection();
	   			if(deleteItems!=null&&deleteItems.length>0){
	   				String msg="ȷ��Ҫɾ����ѡ����ļ���";
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					int choices=box.open();
					if(choices==SWT.YES){
						for(TableItem del:deleteItems){
							ViewFileBean dalData=(ViewFileBean)del.getData();
							TableItem[] items=fileTable.getItems();
							for(int i=0;i<items.length;i++){
								ViewFileBean data=(ViewFileBean)items[i].getData();
								if(dalData.getFileName().equals(data.getFileName())){
									fileTable.remove(i);
									DeleteFiles.add(data);//��Ӧ�����ݿ��¼Ҳ��Ҫͬ��ɾ��
									break;
								}
							}
						}
					}
	   			}else{
	   				String msg="��ѡ����Ҫɾ�����ļ���¼(��ʹ��Ctrl/Shift��ѡ)��";
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					box.open();
	   			}
	   		}
		 }
}
