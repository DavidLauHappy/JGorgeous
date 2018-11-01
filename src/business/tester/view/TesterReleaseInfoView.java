package business.tester.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import model.BACKLOG;
import model.GROUPUSER;
import model.STREAM;
import model.TASK;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bean.BackLogBean;
import bean.GroupUserBean;
import bean.RequirementLog;
import bean.StreamBean;
import bean.TTaskBean;
import bean.TaskBean;
import bean.VStep;
import bean.ViewBean;
import bean.ViewFileBean;
import business.tester.action.AttachDropTargetAction;
import business.tester.action.ReportDropTargetAction;
import business.tester.action.TransferNextAction;
import business.tversion.view.TVersionReleaseView;
import resource.Constants;
import resource.Context;
import resource.FtpFileService;
import resource.IMessage;
import resource.Icons;
import resource.Paths;
import resource.TQuickDeployView;
import resource.UserChoose;
import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;


public class TesterReleaseInfoView {
	public Composite self=null;
	public SashForm content=null;
	public Composite parent=null;
	private Text textName,textDesc,textVersion,textInput,comboStream;
	private TTaskBean bean;
	private ViewBean data;
	public Table fileTable=null;
	private  String[] header=null;
	private List<ViewFileBean> DeleteFiles;//记录删除的附件文件
	public List<String> NFiles=new ArrayList<String>();
	public List<String> ViewFiles=new ArrayList<String>();
	public List<ViewFileBean> ReportsFiles=new ArrayList<ViewFileBean>();
	
	public TesterReleaseInfoView(Composite com,TTaskBean bean){
		this.parent=com;
		this.bean=bean;
		this.data=bean.getView();
		 self=new Composite(com,SWT.NONE);
		 self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 self.setLayout(LayoutUtils.getComGridLayout(1, 1));;
		 DeleteFiles=new ArrayList<ViewFileBean>();
		 content=new SashForm(self,SWT.VERTICAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 this.createToolsView();
		 this.createListView();
		 content.setWeights(new int[]{40,60});
		 self.pack();
	}
	
	
	public Button btnSubmit,btnReject,btnTransfer=null;
	public Combo cboAuditors,cboTester=null;
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
		StreamBean stream=STREAM.getStreamByID(data.getStreamID());
		String streamName=stream.getStreamName();
		comboStream.setText(streamName);
	
		btnSubmit=new  Button(toolPanel,SWT.PUSH);
		btnSubmit.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnSubmit.setText("   "+Constants.getStringVaule("btn_submitchek")+"   ");
		btnSubmit.addSelectionListener(new  SubmitCheckAction());
		
		
		cboAuditors=new Combo(toolPanel,SWT.DROP_DOWN);
		cboAuditors.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		 String groupID="Flow"+"4";
		 List<GroupUserBean> users=GROUPUSER.getUsers(groupID);
		 if(users!=null&&users.size()>0){
			  String[] tUsers=new String[users.size()];
			  int index=0;
			  for(GroupUserBean user:users){
				  tUsers[index]=user.getUserFullName();
				  index++;
			  }
			  cboAuditors.setItems(tUsers);
		 }
		/*btnReject=new  Button(toolPanel,SWT.PUSH);
		btnReject.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnReject.setText("   "+Constants.getStringVaule("btn_approveNot")+"   ");
		btnReject.addSelectionListener(new ReturnViewAction());
		if(!Context.session.userID.equals(data.getCurrentUserID())){
			btnReject.setVisible(false);
		}*/
		
		
		//开发责任人
		 Label labOwner=new Label(toolPanel,SWT.NONE);
		 labOwner.setText("开发"+Constants.getStringVaule("label_taskowner"));
		 labOwner.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtOwner=new Text(toolPanel,SWT.BORDER);
		 txtOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(data.getOwner())){
			 txtOwner.setText(data.getOwnerName());
		 }
		 txtOwner.setEditable(false);
		 
		//测试责任人
		 Label labTOwner=new Label(toolPanel,SWT.NONE);
		 labTOwner.setText("测试"+Constants.getStringVaule("label_taskowner"));
		 labTOwner.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 Text txtTOwner=new Text(toolPanel,SWT.BORDER);
		 txtTOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		 if(!StringUtil.isNullOrEmpty(data.getTowner())){
			 txtTOwner.setText(data.getTownerName());
		 }
		 txtTOwner.setEditable(false);
		 
		btnTransfer=new Button(toolPanel,SWT.PUSH);
		btnTransfer.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnTransfer.setText("   "+Constants.getStringVaule("btn_transfer")+"   ");
		btnTransfer.addSelectionListener(new  TransferViewAction());
		
		cboTester=new Combo(toolPanel,SWT.DROP_DOWN);
		cboTester.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		 String groupid="Flow"+"3";
		 List<GroupUserBean> nextUsers=GROUPUSER.getUsers(groupid);
		 if(nextUsers!=null&&nextUsers.size()>0){
			  String[] tUsers=new String[nextUsers.size()];
			  int index=0;
			  for(GroupUserBean user:nextUsers){
				  tUsers[index]=user.getUserFullName();
				  index++;
			  }
			  cboTester.setItems(tUsers);
		 }	
			
		 if(!(ViewBean.ReleaseStatus.Organized.ordinal()+"").equals(data.getUptFlag())||
			(TTaskBean.Status.Done.ordinal()+"").equals(bean.getStatus())){
			     btnSubmit.setVisible(false);
				cboAuditors.setVisible(false);
			    btnTransfer.setVisible(false);
				cboTester.setVisible(false);
		 }
		 
		//方案说明
		Label labDesc=new Label(toolPanel,SWT.NONE);
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textDesc=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 30));
		textDesc.setText(data.getVeiwDesc());
		//发版说明
		Label labComment=new Label(toolPanel,SWT.NONE);
		labComment.setText(Constants.getStringVaule("label_editDesc"));
		labComment.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textVersion=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textVersion.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 30));
		textVersion.setForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_RED));//前景色，即文字颜色
		textVersion.setText(data.getVerDesc());
		textVersion.setEditable(false);
		
	
		 
		textInput=new Text(toolPanel,SWT.BORDER);
		textInput.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 6, 1, 0, 0));
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
		
		Button btnDelete= new  Button(toolPanel,SWT.PUSH);
		btnDelete.setText("   "+Constants.getStringVaule("btn_delete")+"   ");
		btnDelete.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnDelete.addSelectionListener(new DeleteAction());
		
		comboStream.setEditable(false);
		textName.setEditable(false);
		textDesc.setEditable(false);
		toolPanel.pack();
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
			tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
			tableItem.setImage(icon);
			tableItem.setData(file);
			ViewFiles.add(name);
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
		//
	   DropTarget targetTable=new DropTarget(fileTable,DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
	   targetTable.setTransfer(new Transfer[]{FileTransfer.getInstance()});
	   targetTable.addDropListener(new ReportDropTargetAction(fileTable,ReportsFiles));
	   pannelData.pack();
	   
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
   					TQuickDeployView.getInstance().show(files);
   				}
   			}else{
	   			 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 box.setText(Constants.getStringVaule("messagebox_alert"));
				 box.setMessage("请选择需要部署的文件(一次只能部署一个组件)");
				 box.open();
   			}
   		}
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
	//测试报告上传
	public boolean submitTestReport(){
		boolean result=true;
		if(ReportsFiles.size()>0){
			for(ViewFileBean file:ReportsFiles){
				String fileID=UUID.randomUUID().toString();
				file.setFileID(fileID);
				file.setViewID(data.getViewID());
				file.setStreamID(data.getStreamID());
				file.setMdfUser(Context.session.userID);
				file.setCrtUser(Context.session.userID);
				file.setVersionID(data.getVersion());
				boolean upRet=FtpFileService.getService().upLoad(file.getLocation(), file.getRemotePath());
				if(upRet){
					file.setStatus(ViewFileBean.Status.New.ordinal()+"");
					file.save();
					file.saveFileObj();
				}else{
					result=false;
					String alterMsg="测试报告提交失败!请稍后重试。";
					MessageBox msgBox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
					msgBox.setText(Constants.getStringVaule("messagebox_alert"));
					msgBox.setMessage(alterMsg);
					msgBox.open();
				}
			}
		}else{
			result=false;//
		}
		return result;
	}
	

	
	public class SubmitCheckAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			    if(submitTestReport()){
   			    	//测试审核人
   			    	if(!StringUtil.isNullOrEmpty(cboAuditors.getText())){
   			    		String nextUserID=StringUtil.getUserIdFromName(cboAuditors.getText());
		   			    List<TaskBean> reqs=TASK.getViewReqs(data.getViewID());
						 if(reqs!=null){
							    //测试任务设置为完成
							    bean.SetStatus(TTaskBean.Status.Done.ordinal()+"");
				   				//视图提交审核(多个测试任务可重复视图提交测试审核)
				   				data.setCurrentUserID(nextUserID);//提交到测试管理员审核
			   					data.setProgress(VStep.VersionCheck);//测试审核
			   					data.setLastProgress(VStep.VersionTest);
			   					data.submit();
			   					if(reqs.size()>1){
					   				//如果发布对应到多个需求(发生了需求整合)
					   					for(TaskBean req:reqs){
					   						String seq=UUID.randomUUID().toString();
					   						seq=seq.replace("-", "");
					   						req.logStep(seq, Context.session.userID, "详细测试完成", VStep.VersionTest);	
					   						//视图对应的测试任务
					   						List<TTaskBean> tasks= data.getTestTasks();
					   						boolean allDone=true;
					   						for(TTaskBean task:tasks){
					   							 if(!(TTaskBean.Status.Done.ordinal()+"").equals(task.getStatus())){
					   								 allDone=false;
					   							 }
					   						}
					   						if(allDone){
					   						   //对应的需求 不是最后一个提交人不能设置为测试完成
						   					   req.progress(VStep.VersionCheck, nextUserID, Context.session.userID);
						   					   //开发任务对应的需求记录需要更新状态和当前处理人
												BACKLOG.updateBackLog(req.getReqID(), Context.session.userID, BackLogBean.Status.TestAudit.ordinal()+"", "2");
					   						}
					   					}
					   				}else{
					   					TaskBean req=reqs.get(0);
					   					String seq=UUID.randomUUID().toString();
										seq=seq.replace("-", "");
										req.logStep(seq, Context.session.userID,"详细测试完成", VStep.VersionTest);
										//视图对应的测试任务
				   						List<TTaskBean> tasks= data.getTestTasks();
				   						boolean allDone=true;
				   						for(TTaskBean task:tasks){
				   							 if(!(TTaskBean.Status.Done.ordinal()+"").equals(task.getStatus())){
				   								 allDone=false;
				   							 }
				   						}
				   						if(allDone){
				   						   //对应的需求 不是最后一个提交人不能设置为测试完成
					   					   req.progress(VStep.VersionCheck, nextUserID, Context.session.userID);
					   					  //开发任务对应的需求记录需要更新状态和当前处理人
										  BACKLOG.updateBackLog(req.getReqID(), Context.session.userID, BackLogBean.Status.TestAudit.ordinal()+"", "2");
				   						}
					   				}
				   				String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"提交发布【"+data.getViewName()+"】测试完成。请您审核。";
								IMessage message=new IMessage(data.getUptUser(),msg);
								message.addMsg();
								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(Constants.getStringVaule("alert_successoperate"));
								box.open();	
								if(btnSubmit!=null)
									btnSubmit.setEnabled(false);
								if(btnReject!=null)
									btnReject.setEnabled(false);
								if(btnTransfer!=null)
									btnTransfer.setEnabled(false);
								TesterReleaseView.getInstance(null).refreshTree();
								return;
						 }
					 }else{
						 	String alterMsg="请选择审核人";
							MessageBox msgBox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
							msgBox.setText(Constants.getStringVaule("messagebox_alert"));
							msgBox.setMessage(alterMsg);
							msgBox.open();
					 }
   			    }else{
   			    	String alterMsg="测试报告未提交";
					MessageBox msgBox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
					msgBox.setText(Constants.getStringVaule("messagebox_alert"));
					msgBox.setMessage(alterMsg);
					msgBox.open();
   			    }
   		}
	}
	
	public class ReturnViewAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			createPrompt();
   		}
	}
	
	public class TransferViewAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			String nextUser=cboTester.getText();
   			if(!StringUtil.isNullOrEmpty(nextUser)){
   				String userID=StringUtil.getUserIdFromName(nextUser);
   				data.setCurrentUserID(userID);
   				data.submit();
   				//测试负责人要同步修改
   				data.SetTowner(userID);
   				//任务也需要同步转移
   				bean.setUserID(userID);
   				bean.SetStatus(TTaskBean.Status.Todo.ordinal()+"");
   				List<TaskBean> reqs=TASK.getViewReqs(bean.getViewID());
   				if(reqs!=null&&reqs.size()>0){
   					for(TaskBean req:reqs){
   						String seq=UUID.randomUUID().toString();
   						seq=seq.replace("-", "");
   						req.logStep(seq, Context.session.userID, "测试转处理", "6");
   						req.progress("6", userID, Context.session.userID);
   					}
   				}
   				//通知下一处理人
   				String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"转处理发布【"+bean.getViewName()+"】给您。请查阅。";
   				IMessage message=new IMessage(userID,msg);
   				message.addMsg();
   				String alterMsg="发布【"+data.getViewName()+"】转处理成功!";
   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
   				box.setText(Constants.getStringVaule("messagebox_alert"));
   				box.setMessage(alterMsg);
   				box.open();
   				TesterReleaseView.getInstance(null).refreshTree();
   				if(btnSubmit!=null)
   					btnSubmit.setEnabled(false);
   				if(btnReject!=null)
   					btnReject.setEnabled(false);
   				if(btnTransfer!=null)
   					btnTransfer.setEnabled(false);
   				
   			}else{
   				String alterMsg="转处理请选择下一处理人!";
   				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
   				box.setText(Constants.getStringVaule("messagebox_alert"));
   				box.setMessage(alterMsg);
   				box.open();
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
		   				if(reqs.size()>1){
		   				   //如果发布对应到多个需求(发生了需求整合)，需求退回到开发提交阶段
		   					for(TaskBean req:reqs){
		   						String seq=UUID.randomUUID().toString();
		   						seq=seq.replace("-", "");
		   						req.logStep(seq, Context.session.userID, cmt,  VStep.VersionTest);
		   						req.progress(VStep.CodeSubmit, req.getOwner(), Context.session.userID);
		   						String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"退回需求【"+req.getTname()+"】给您。请查阅。";
		   						IMessage message=new IMessage(req.getOwner(),msg);
		   						message.addMsg();
		   					}
		   					data.setCurrentUserID(data.getCrtUser());
		   					data.setProgress(VStep.VersionMake);//待版本整合
		   					data.setLastProgress(VStep.VersionTest);
		   					data.submit();
		   				}else{
		   				 //如果发布对应到一个需求，退回直接退到开发提交阶段
		   					TaskBean req=reqs.get(0);
		   					String seq=UUID.randomUUID().toString();
	   						seq=seq.replace("-", "");
	   						req.logStep(seq, Context.session.userID,cmt, VStep.VersionTest);
	   						//取代码提交人
	   						req.progress(VStep.CodeSubmit, req.getOwner(), Context.session.userID);
	   						String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"退回需求【"+req.getTname()+"】给您。请查阅。";
	   						IMessage message=new IMessage(req.getOwner(),msg);
	   						message.addMsg();
	   						//发布本身停留在待版本整合
	   						data.setCurrentUserID(data.getCrtUser());
		   					data.setProgress(VStep.VersionMake);//待版本整合
		   					data.setLastProgress(VStep.VersionTest);
		   					data.submit();
		   				}
		   			}
		   			String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"退回发布【"+data.getViewName()+"】给您。请查阅。";
					IMessage message=new IMessage(data.getCrtUser(),msg);
					message.addMsg();
		   			promptShell.dispose();
		   			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_successoperate"));
					box.open();	
					TesterReleaseView.getInstance(null).refreshTree();
					if(btnSubmit!=null)
						btnSubmit.setEnabled(false);
					if(btnReject!=null)
						btnReject.setEnabled(false);
					if(btnTransfer!=null)
						btnTransfer.setEnabled(false);
					return;
	   			}
   			}
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
	   	                  return arg0.getMdfTime().compareTo(arg1.getMdfTime());
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
   			 TableItem[] items=fileTable.getItems();
   			List<String> selectFiles=getSelected();
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
	 
	 
	 public class DeleteAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			List selections=Arrays.asList(fileTable.getSelection());
	   			TableItem[] items=fileTable.getItems();
	   			if(selections!=null&&selections.size()>0){
	   				List<Integer> deletes=new ArrayList<Integer>();
		   			for(int w=0;w< items.length;w++){
						 ViewFileBean data=(ViewFileBean)items[w].getData();
						 if(selections.contains(items[w])){
							 if(!ViewFiles.contains(data.getFileName())){
								 deletes.add(w);
							 }
						 }
		   			}
		   			for(int index:deletes){
		   				fileTable.remove(index);
		   			}
	   			}
	   		}
	 }
	 
	 
}
