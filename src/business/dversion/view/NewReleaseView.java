package business.dversion.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import model.BACKLOG;
import model.GROUPUSER;
import model.STREAM;
import model.TASK;
import model.USERS;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import common.controls.DavidCombo;
import common.localdb.LocalDataHelper;


import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.FtpFileService;
import resource.IMessage;
import resource.Icons;
import resource.Item;
import resource.User;
import resource.UserChoose;
import bean.BackLogBean;
import bean.GroupUserBean;
import bean.RFile;
import bean.StreamBean;
import bean.TaskBean;
import bean.UserBean;
import bean.VStep;
import bean.ViewBean;
import bean.ViewFileBean;
import business.dversion.action.DropTargetAction;
import business.dversion.action.ViewSubmitAction;


import utils.DateUtil;
import utils.LayoutUtils;
import utils.StringUtil;
import utils.ZenDaoApi;
import views.AppView;

/**
 * @author David
 *  同一个界面按钮，处理新增和发布(上传)
 */
public class NewReleaseView {
	public Composite self=null;
	public SashForm content=null;
	public Composite parent=null;
	private Text textName,textDesc,textComment,textCompileFiles,textFileList,textInput;
	private  Combo comboStream=null;
	public Table fileTable=null;
	private  String[] header=null;
	private Type opType;
	public enum Type{New,Relase;}
	public Button btnRelease,btnChoose;
	private  Combo cmbApp=null;
	public Map<String, String> Stream;
	public Map<String,String> System=new HashMap<String, String>();
	public Map<String,String> SystemApp=Dictionary.getDictionaryMap("APP");
	public Shell reqShell;
	public NewReleaseView(Composite com,Type optype){
		Stream=new HashMap<String, String>();
		 this.parent=com;
		 this.opType=optype;
		 self=new Composite(com,SWT.NONE);
		 self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 self.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 
		 content=new SashForm(self,SWT.VERTICAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 this.createToolsView();
		 this.createListView();
		 content.setWeights(new int[]{50,50});
		 self.pack();
	}
	

	public Text txtTaskOwner,dueDate,txtTaskDDate=null;
	public Combo nexter=null;
	private void createToolsView(){
		
		Composite propertyPanel=new Composite(content,SWT.NONE);
		propertyPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		propertyPanel.setLayout(LayoutUtils.getComGridLayout(1, 5));
		
		Composite actionPanel=new Composite(propertyPanel,SWT.BORDER);
		actionPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		actionPanel.setLayout(LayoutUtils.getComGridLayout(5, 5));
		
		btnChoose=new Button(actionPanel,SWT.PUSH);
		btnChoose.setText("   "+Constants.getStringVaule("btn_addreq")+"   ");
		btnChoose.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnChoose.addSelectionListener(new ChooseReqAction());
		

		 //处理人
		 Label labNexter=new Label(actionPanel,SWT.NONE);
		 labNexter.setText(Constants.getStringVaule("label_nextUser"));
		 labNexter.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 nexter=new Combo(actionPanel,SWT.DROP_DOWN);
		 nexter.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 String groupID="Flow"+"2";
		 List<GroupUserBean> users=GROUPUSER.getUsers(groupID);
		 if(users!=null&&users.size()>0){
			  String[] nextUsers=new String[users.size()];
			  int index=0;
			  for(GroupUserBean user:users){
				  nextUsers[index]=user.getUserFullName();
				  index++;
			  }
			  nexter.setItems(nextUsers);
			  nexter.select(0);
		 }
		
		btnRelease=new  Button(actionPanel,SWT.PUSH);
		btnRelease.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnRelease.addSelectionListener(new AddReleaseAction());
		btnRelease.setImage(Icons.getOkIcon());
		btnRelease.setText("   "+Constants.getStringVaule("btn_submit")+"   ");
		actionPanel.pack();
		
		
	  	Composite toolPanel=new Composite(propertyPanel,SWT.BORDER);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(8, 5));
		//标题
		Label labName=new Label(toolPanel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_name"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textName=new Text(toolPanel,SWT.BORDER);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		//归属流
		Label labStreamName=new Label(toolPanel,SWT.NONE);
		labStreamName.setText(Constants.getStringVaule("label_streamname"));
		labStreamName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		comboStream=new Combo(toolPanel,SWT.DROP_DOWN);
		comboStream.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		List<StreamBean> rawdata=STREAM.getStream("doing");
		List<StreamBean> data=new ArrayList<StreamBean>();
		for(StreamBean bean:rawdata){
			if(bean.getStatus().equals("doing")){
				data.add(bean);
			}
		}
		if(data!=null&&data.size()>0){
	    	String[] items=new String[data.size()];
	    	int i=0;
	    	for(StreamBean bean:data){
	    		items[i]=bean.getStreamName();
	    		i++;
	    		Stream.put(bean.getStreamName(), bean.getStreamID());
	    	}
	    	comboStream.setItems(items);
	    	comboStream.select(0);
	    }
		//系统分类
		Label labApp=new Label(toolPanel,SWT.NONE);
		labApp.setText(Constants.getStringVaule("label_appclass"));
		labApp.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		cmbApp=new Combo(toolPanel,SWT.DROP_DOWN);
		cmbApp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		List<Item> systems=Dictionary.getDictionaryList("APP");
		if(systems!=null&&systems.size()>0){
			String[] items=new String[systems.size()];
			for(int w=0;w<systems.size();w++){
				items[w]=systems.get(w).getValue();
				System.put(systems.get(w).getValue(), systems.get(w).getKey());
			}
			cmbApp.setItems(items);
		}
		//开发责任人
		 Label labTaskOwner=new Label(toolPanel,SWT.NONE);
		 labTaskOwner.setText(Constants.getStringVaule("label_taskowner"));
		 labTaskOwner.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 txtTaskOwner=new Text(toolPanel,SWT.BORDER);
		 txtTaskOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		
		
		//预期上线日期
		Label labDate=new Label(toolPanel,SWT.NONE);
		labDate.setText(Constants.getStringVaule("label_duedate"));
		labDate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		dueDate=new Text(toolPanel,SWT.BORDER);
		dueDate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, false, false, 3, 1, 0, 0));
		//开发完成时间
		 Label labTaskDDate=new Label(toolPanel,SWT.NONE);
		 labTaskDDate.setText(Constants.getStringVaule("label_taskddate"));
		 labTaskDDate.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 txtTaskDDate=new Text(toolPanel,SWT.BORDER);
		 txtTaskDDate.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		
		//内容描述
		Label labDesc=new Label(toolPanel,SWT.NONE);
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textDesc=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 7, 1, 0, 40));
		
		//发版说明
		Label labComment=new Label(toolPanel,SWT.NONE);
		labComment.setText(Constants.getStringVaule("label_editDesc"));
		labComment.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textComment=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textComment.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 7, 1, 0, 40));
		textComment.setForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_RED));//前景色，即文字颜色
		//修改文件
		Label labFileList=new Label(toolPanel,SWT.NONE);
		labFileList.setText(Constants.getStringVaule("label_fileList"));
		labFileList.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textFileList=new Text(toolPanel,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP|SWT.BORDER);
		textFileList.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
		
		//编译文件
		Label labCompileFiles=new Label(toolPanel,SWT.NONE);
		labCompileFiles.setText(Constants.getStringVaule("label_compileFiles"));
		labCompileFiles.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textCompileFiles=new Text(toolPanel,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP|SWT.BORDER);//
		textCompileFiles.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
		
	
		
		//搜索文件
		textInput=new Text(toolPanel,SWT.BORDER);
		textInput.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 6, 1, 0, 0));
		Button btnSearch=new  Button(toolPanel,SWT.PUSH);
		btnSearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
		btnSearch.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		
		Button btnDelete=new  Button(toolPanel,SWT.PUSH);
		btnDelete.setText("   "+Constants.getStringVaule("btn_delete")+"   ");
		btnDelete.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnDelete.addSelectionListener(new DeleteFileAtion());
		
		toolPanel.pack();
		propertyPanel.pack();
}
	
	public  void setComboCheck(Combo source,String text){
		String[] items=source.getItems();
		String desc=SystemApp.get(text);
		for(int w=0;w<items.length;w++){
			if(items[w].equals(desc)){
				source.select(w);
			}
		}
	}
	
	
	
	public void enableEditable(){
		this.opType=Type.Relase;
		btnRelease.setText("   "+Constants.getStringVaule("btn_release")+"   ");
		fileTable.setEnabled(true);
		   DropTarget targetTable=new DropTarget(fileTable,DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
		   targetTable.setTransfer(new Transfer[]{FileTransfer.getInstance()});
		   targetTable.addDropListener(new DropTargetAction(fileTable));
		   textName.setEditable(false);
	   
	}

	private void createListView(){
		    Composite pannelData=new Composite(content,SWT.NONE);
		    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		    fileTable=new Table(pannelData,SWT.FULL_SELECTION|SWT.MULTI);
		    fileTable.setHeaderVisible(true);
		    fileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    fileTable.setLinesVisible(true);
		    fileTable.addSelectionListener(new SetSelectionAction());
		    fileTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
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
			}
			
			for(int j=0;j<header.length;j++){		
				fileTable.getColumn(j).pack();
			}	
			
			DropTarget targetTable=new DropTarget(fileTable,DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
			targetTable.setTransfer(new Transfer[]{FileTransfer.getInstance()});
			targetTable.addDropListener(new DropTargetAction(fileTable));
			
	    pannelData.pack();
	}
	
	 public class SetSelectionAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				int total=fileTable.getItemCount();
				for(int w=0;w<total;w++){
					TableItem item=fileTable.getItem(w);
					if(item.getChecked()){
						item.setForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
						item.setBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
					}else{
						item.setForeground(null);
						item.setBackground(null);
					}
				}
			}
	 }
	 
	
	 //新增时，把维护的属性存储，原始的文件维护进去
	 public class AddReleaseAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				if(opType.equals(Type.New)){
					String name=textName.getText();
					String streamName=comboStream.getText();
					String desc=textDesc.getText();
					String app=cmbApp.getText();
					String verDesc=textComment.getText();
					String date=dueDate.getText();
					String streamID=Stream.get(streamName);
					String owner=txtTaskOwner.getText();
					String doneDate=txtTaskDDate.getText();
					if(StringUtil.isNullOrEmpty(name,60)||
					   StringUtil.isNullOrEmpty(streamName,60)||	
					   StringUtil.isNullOrEmpty(app,10)||
					   StringUtil.isNullOrEmpty(date,10)||		
					   StringUtil.isNullOrEmpty(desc,4000)||
					   StringUtil.isNullOrEmpty(owner,10)||
					   StringUtil.isNullOrEmpty(doneDate,10)||
					   StringUtil.isNullOrEmpty(verDesc,4000)
							){
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
						box.open();
						return;
					}
					else{
						int len=name.length();
						if(len>60){
							String msg="发布名称超长，不能超过60个字符！";
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(msg);
							box.open();
							return;
						}else{
							ViewBean view=new ViewBean();
							boolean exist=VIEW.isNameExist(name);
							if(exist){
								String msg="发布名称【"+name+"】已存在，不能重复新增！";
								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(msg);
								box.open();
								return;
							}else{
								///////////////////////////////////基本信息提交
								String viewID=VIEW.getNewNo();
								view.setViewID(viewID);
								view.setViewName(name);
								view.setVeiwDesc(desc);
								view.setStreamID(streamID);
								view.setVerDesc(verDesc);
								view.setCrtUser(Context.session.userID);
								view.setUptUser(Context.session.userID);
								view.setStatus(ViewBean.Status.Normal.ordinal()+"");
								String appName=System.get(app);
								view.setApp(appName);
								view.setRdate(date);
								owner=StringUtil.getUserIdFromName(owner);
								view.setOwner(owner);
								view.setDeadDate(doneDate);
								/////////////////////////////////////////////文件提交
								List<ViewFileBean> files=new ArrayList<ViewFileBean>();
								int fileCnt=fileTable.getItemCount();
								if(fileCnt>0){
									for(int w=0;w<fileCnt;w++){
										TableItem item=fileTable.getItem(w);
										ViewFileBean bean=(ViewFileBean)item.getData();
										String fileID=UUID.randomUUID().toString();
										bean.setFileID(fileID);
										bean.setViewID(view.getViewID());
										bean.setStreamID(view.getStreamID());
										bean.setMdfUser(Context.session.userID);
										bean.setCrtUser(Context.session.userID);
										if((ViewFileBean.Mode.Local.ordinal()+"").equals(bean.getFileMode())){
											boolean upRet=FtpFileService.getService().upLoad(bean.getLocation(), bean.getRemotePath());
											if(upRet){
												bean.setStatus(ViewFileBean.Status.New.ordinal()+"");
											}else{
												String msg="发布文件失败！";
												MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
												box.setText(Constants.getStringVaule("messagebox_alert"));
												box.setMessage(msg);
												box.open();
											}
										}else{
											bean.setStatus(ViewFileBean.Status.Normal.ordinal()+"");
										}
										files.add(bean);
									}
								}
								//记录发布与需求的关系数据
								 List<TaskBean> reqs=new ArrayList<TaskBean>();
								if(loadReqs!=null&&loadReqs.size()>0){
									view.clearReqs();
									for(String reqID:loadReqs.keySet()){
										TaskBean req=loadReqs.get(reqID);
										reqs.add(req);
									}
								}
								
								String nextUser=nexter.getText();
								String nextUserID=StringUtil.getUserIdFromName(nextUser);
								//是否使用本地化参数提交发布
									view.sync();
									if(files!=null&&files.size()>0){
										view.createVersion();
										for(ViewFileBean file:files){
											file.setVersionID(view.getVersion());
											if((ViewFileBean.Status.New.ordinal()+"").equals(file.getStatus())){
												file.save();
												file.saveFileObj();
											}else{
												file.save();
											}
										}
									}
									view.setCurrentUserID(nextUserID);
									view.setProgress(VStep.VersionDispatch);//测试安排
									view.setLastProgress(VStep.VersionMake);
									view.reSetUptFlag(ViewBean.ReleaseStatus.Organized.ordinal()+"");
									view.submit();
									//推进需求的步骤，同时记录
									if(reqs!=null&&reqs.size()>0){
										for(TaskBean req:reqs){
											view.addReq(req.getId());
											String seq=UUID.randomUUID().toString();
											seq=seq.replace("-", "");
											req.logStep(seq, Context.session.userID, "版本整合", VStep.VersionMake);
											req.progress(VStep.VersionDispatch, nextUserID, Context.session.userID);
											req.ResetReleaseFlag(TaskBean.ReleaseStatus.Organized.ordinal()+"");
											//开发任务对应的需求记录需要更新状态和当前处理人
											BACKLOG.updateBackLog(req.getReqID(), nextUserID, BackLogBean.Status.TestDispath.ordinal()+"", "2");
											//更新禅道中需求的状态
											ZenDaoApi.getInstance().setReqStatus(req.getReqID(), "testing");
										}
									}
									//通知下一处理人
									String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"提交发布【"+view.getViewName()+"】请查阅。";
									IMessage message=new IMessage(nextUserID,msg);
									message.addMsg();
									String alterMsg="新增发布成功！";
									MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage(alterMsg);
									box.open();
									//刷新发布浏览器
									ReleaseView.getInstance(null).refreshTree();
									DVersionRequirementView.getInstance(null).refreshTree();
									btnRelease.setEnabled(false);
									btnChoose.setEnabled(false);
									
								opType=Type.Relase;//不能重复去提交
							}
						}
					}
				}
			}
	 }
	 

	 public class DeleteFileAtion extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			TableItem[] deleteItems=fileTable.getSelection();
	   			if(deleteItems!=null&&deleteItems.length>0){
	   				String msg="确认要删除已选择的文件吗？";
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
									break;
								}
							}
						}
					}
	   			}else{
	   				String msg="请选择需要删除的文件记录(可使用Ctrl/Shift多选)。";
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					box.open();
	   			}
	   		}
		 }
	 
	 
	 //当前已经整合了的需求对象
	 Map<String,TaskBean> loadReqs=new HashMap<String, TaskBean>();
	 public class ChooseReqAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				 Point point=AppView.getInstance().getDisplay().getCurrent().getCursorLocation();
				 reqShell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
				 reqShell.setLocation(point);
				 reqShell.setText("需求选择");
				 reqShell.setLayout(LayoutUtils.getComGridLayout(1, 0));
				 Composite pannel=new Composite(reqShell,SWT.V_SCROLL);
				 pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 140));
				 pannel.setLayout(LayoutUtils.getComGridLayout(1, 0));
				 //获取需求列表 我的待审批
				 List<TaskBean> reqs=TASK.getMyTasks(Context.session.userID);
				 if(reqs!=null&&reqs.size()>0){
					 for(TaskBean req:reqs){
						 Button btn=new  Button(pannel,SWT.RADIO);
						 btn.setText(req.getTname());
						 btn.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
						 btn.addSelectionListener(new LoadReqAction());
						 btn.setData(req);
						 if(loadReqs.containsKey(req.getId())){
							 btn.setSelection(true);
						 }else{
							 btn.setSelection(false);
						 }
					 }
				 }
				pannel.pack();
				reqShell.pack();
				reqShell.open();
				reqShell.addShellListener(new ShellAdapter() {
					public void shellClosed(ShellEvent e){	
						reqShell.dispose();
					}	
				});
			}
	 }
	 
	 public TaskBean currentReq=null;
	 public class LoadReqAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				Button btn=(Button)e.getSource();
				if(btn!=null){
					TaskBean data=(TaskBean)btn.getData();
					if(btn.getSelection()){
						if(!loadReqs.containsKey(data.getId())){
							loadReqs.put(data.getId(), data);
							currentReq=data;
							textName.setText(data.getTname());//视图名称
							//开发责任人
							if(!StringUtil.isNullOrEmpty(data.getOwnerShowName())){
								txtTaskOwner.setEditable(true);
								txtTaskOwner.setText(data.getOwnerShowName());
								txtTaskOwner.setEditable(false);
							}
							//开发完成时间
							if(!StringUtil.isNullOrEmpty(data.getOverDate())){
								txtTaskDDate.setEditable(true);
								txtTaskDDate.setText(data.getOverDate());
								txtTaskDDate.setEditable(false);
							}
							setComboCheck(cmbApp,data.getApp());
							//预期上线时间
							if(!StringUtil.isNullOrEmpty(data.getRdate())){
								dueDate.setEditable(true);
								dueDate.setText(data.getRdate());
								dueDate.setEditable(false);
							}
							//修改内容描述
							if(!StringUtil.isNullOrEmpty(data.getRevDesc())){
								textDesc.setEditable(true);
								textDesc.setText(data.getRevDesc());
								textDesc.setEditable(false);
							}
							//修改文件
							if(!StringUtil.isNullOrEmpty(data.getFileList())){
								textFileList.setEditable(true);
								textFileList.setText(data.getFileList());
								textFileList.setEditable(false);
							}
							//编译文件
							if(!StringUtil.isNullOrEmpty(data.getCmpFileList())){
								textCompileFiles.setEditable(true);
								textCompileFiles.setText(data.getCmpFileList());
								textCompileFiles.setEditable(false);
							}
							//发版说明
							if(!StringUtil.isNullOrEmpty(data.getVersionDesc())){
								textComment.setEditable(true);
								textComment.setText(data.getVersionDesc());
								textComment.setEditable(false);
							}
							//附件处理
							 List<RFile> files=TASK.getAttach(data.getId(),data.getCurrentVersion(),TaskBean.AttachType.Normal.ordinal()+"");
							 if(files!=null&&files.size()>0){
								 int w=0;
								 for(RFile file:files){
									   ViewFileBean bean=new ViewFileBean();
									   bean.setFileName(file.getFileName());
									   bean.setRemotePath(file.getRpath());
									   bean.setFileTime(file.getFileTime());
									   bean.setCrtTime(data.getCrtTime());
									   bean.setLocation("");//为下载前没有本地路径
									   bean.setMd5(file.getMd5());
										bean.setFileMode(ViewFileBean.Mode.Remote.ordinal()+"");
										bean.setOrignalOrder(w);
										Image icon=Icons.getFileImage(file.getFileName());
										w++;
										TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
										tableItem.setText(new String[]{bean.getFileName(),bean.getCrtTime(),bean.getFileTime(),bean.getMd5()});
										tableItem.setImage(icon);
										tableItem.setData(bean);
								 }
							 }
						}
					}else{
						if(loadReqs.containsKey(data.getId())){
							if(currentReq.getId().equals(data.getId())){
								textName.setEditable(true);
								textName.setText("");
								txtTaskOwner.setEditable(true);
								txtTaskOwner.setText("");
								txtTaskDDate.setEditable(true);
								txtTaskDDate.setText("");
								dueDate.setEditable(true);
								dueDate.setText("");
								textDesc.setEditable(true);
								textDesc.setText("");
								textCompileFiles.setEditable(true);
								textCompileFiles.setText("");
								textFileList.setEditable(true);
								textFileList.setText("");
								textComment.setEditable(true);
								textComment.setText("");
							}
							//移除对应的附件
							String sno=data.getId();
							 List<RFile> files=TASK.getAttach(data.getId(),data.getCurrentVersion(),TaskBean.AttachType.Normal.ordinal()+"");
							 if(files!=null&&files.size()>0){
								 for(RFile file:files){
									 removeFileItem(file.getMd5());
								 }
							 }
							loadReqs.remove(data.getId());
						}
					}
				}
			}
	 }
	 
	 
	 public void removeFileItem(String md5){
		 TableItem[] items=	this.fileTable.getItems();
			if(items!=null&&items.length>0){
				for(int i=0;i<items.length;i++){
					ViewFileBean data=(ViewFileBean)items[i].getData();
					if(data.getMd5().equals(md5)){
						this.fileTable.remove(i);
						return;
					}
				}
			}
	 }
}
