package business.developer.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.GROUPUSER;
import model.TASK;
import model.VIEW;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import common.controls.DavidTable;
import common.controls.Segment;
import bean.GroupUserBean;
import bean.RFile;
import bean.RequirementLog;
import bean.TaskBean;
import bean.VStep;
import bean.ViewFileBean;
import business.developer.action.DropAttatAction;


import resource.Constants;
import resource.Context;
import resource.FtpFileService;
import resource.IMessage;
import resource.Icons;
import resource.Paths;
import resource.ReqQuestionView;
import resource.TextViewer;

import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

//���������ύ���̰汾��ϸҳ��
//¼����ϸ���ύ��Ϣ(�����������/���渽��/�������󸽼�/�������󸽼�)
//���̰汾������������

public class DevSubmitView {
	
	public ScrolledComposite self=null;
	private Composite parent;
	public Composite content;
	public TaskBean data;
	public boolean editable=false;
	private List<ViewFileBean> DeleteFiles;//��¼ɾ�����ļ�
	
	public DevSubmitView(Composite com,TaskBean data,boolean editable){
		this.parent=com;
		this.data=data;
		this.editable=editable;
		DeleteFiles=new ArrayList<ViewFileBean>();
		self=new ScrolledComposite(com,SWT.V_SCROLL|SWT.H_SCROLL);
		self.addControlListener(new ScrollCompositeResizeAction());
	    self.setAlwaysShowScrollBars(true);
		self.setExpandHorizontal(true);
		self.setExpandVertical(true);
		
		content=new Composite(self,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		if(this.editable){
			this.createToolsView();
		}
		this.createDataView();
		 content.pack();
		self.layout(true);
		self.setContent(content);
		self.pack();
	}
	

	public Button  btnSubmit,btnSave,btnRelated=null;
	public Combo actions,nexter=null;
	private void createToolsView(){
		Composite toolPanel=new Composite(content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(10, 5)); 
		//�������
		btnRelated=new Button(toolPanel,SWT.PUSH);
		btnRelated.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
		btnRelated.setText("   "+Constants.getStringVaule("btn_relatedQuestion")+"   ");
		if(!(TaskBean.ReleaseStatus.Apply.ordinal()+"").equals(this.data.getReleaseFlag())){
			btnRelated.setVisible(false);
		}
		btnRelated.addSelectionListener(new RelateQuestionAction());
		//����
		 Label labAction=new Label(toolPanel,SWT.NONE);
		 labAction.setText(Constants.getStringVaule("header_action"));
		 labAction.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 actions=new Combo(toolPanel,SWT.DROP_DOWN);
		 actions.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 actions.addSelectionListener(new ChooseAction());
		 String[] actionItems=new String[]{"�ύ�������","�ύ�汾����"};
		 //��һ��ǿ���ύ�鳤������ˣ�����ѡ�񣬺����û��Լ�ѡ�������Ĭ��
		 if((TaskBean.ReleaseStatus.Init.ordinal()+"").equals(this.data.getReleaseFlag())||
				 (TaskBean.ReleaseStatus.CheckReturn.ordinal()+"").equals(this.data.getReleaseFlag())){
			 actions.setItems(actionItems);
			 actions.select(0);
			 actions.setEnabled(false);
		 }else{
			 actions.setItems(actionItems);
			 actions.select(1);
		 }
		 //������
		 Label labNexter=new Label(toolPanel,SWT.NONE);
		 labNexter.setText(Constants.getStringVaule("label_nextUser"));
		 labNexter.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		 nexter=new Combo(toolPanel,SWT.DROP_DOWN);
		 nexter.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 String groupID="Flow"+actions.getSelectionIndex();
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
		 
		 btnSubmit=new Button(toolPanel,SWT.PUSH);
		 btnSubmit.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		 btnSubmit.setToolTipText("   "+Constants.getStringVaule("btn_submit")+"   ");
		 btnSubmit.setImage(Icons.getOkIcon());
		 btnSubmit.addSelectionListener(new SubmitNewReqAction());
		 
		btnSave=new Button(toolPanel,SWT.PUSH);
		btnSave.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnSave.setToolTipText("   "+Constants.getStringVaule("btn_save")+"   ");
		btnSave.setImage(Icons.getImportIcon16());
		btnSave.addSelectionListener(new SaveInfoAction());
		
		toolPanel.pack();
	}
	
	
	
	public Text textResolveDesc,textFileList,textCompileFiles,textEditDesc=null;
	public Segment comSubmitDesc=null;
	//public DavidTable selfTestTable,textCaseTable=null;
	public Table attachTable,ccAttachTable,scAttachTable=null;
	public String[] header=new String[]{ StringUtil.rightPad(Constants.getStringVaule("header_filename"), 100, " "),
															StringUtil.rightPad("",8," ")};
   public Button btnCCOK,btnSLTCOK=null;
	private void createDataView(){
		Composite Panel=new Composite(content,SWT.BORDER);
		Panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		Panel.setLayout(LayoutUtils.getComGridLayout(20, 5)); 
		
		//////////�������˵��//////////////
		Label labResolveDesc=new Label(Panel,SWT.NONE);
		labResolveDesc.setText(Constants.getStringVaule("label_resolveDesc"));
		labResolveDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 2, 1, 0, 0));
		Button btnViewerResolve=new Button(Panel,SWT.PUSH);
		btnViewerResolve.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.TOP, false, false, 1, 1, 0, 0));
		btnViewerResolve.setImage(Icons.getBackupIcon());
		btnViewerResolve.setToolTipText(Constants.getStringVaule("btn_tips_viewdetail"));
		btnViewerResolve.addSelectionListener(new ShowResolveAction());
		textResolveDesc=new Text(Panel,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP|SWT.BORDER);//SWT.V_SCROLL|
		textResolveDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 17, 1, 10, 120));
		//���ı�������ţ��޷����ó��ı�Ҫ����widthHint
		if(!StringUtil.isNullOrEmpty(this.data.getRevDesc())){
			textResolveDesc.setText(this.data.getRevDesc());
		}
		
		
		
		/////////////////////////�޸�Դ�ļ�(svc���ļ���·��)+��Ҫ�����ļ�///////////////
		Label labFileList=new Label(Panel,SWT.NONE);
		labFileList.setText(Constants.getStringVaule("label_fileList"));
		labFileList.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 2, 1, 0, 0));
		Button btnViewerFileList=new Button(Panel,SWT.PUSH);
		btnViewerFileList.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.TOP, false, false, 1, 1, 0, 0));
		btnViewerFileList.setImage(Icons.getBackupIcon());
		btnViewerFileList.setToolTipText(Constants.getStringVaule("btn_tips_viewdetail"));
		btnViewerFileList.addSelectionListener(new ShowFileListAction());
		textFileList=new Text(Panel,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP|SWT.BORDER);//SWT.V_SCROLL|
		textFileList.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 7, 1, 10, 160));
		if(!StringUtil.isNullOrEmpty(this.data.getFileList())){
			textFileList.setText(this.data.getFileList());
		}
		
		
		Label labCompileFiles=new Label(Panel,SWT.NONE);
		labCompileFiles.setText(Constants.getStringVaule("label_compileFiles"));
		labCompileFiles.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 2, 1, 0, 0));
		Button btnViewerCompileFiles=new Button(Panel,SWT.PUSH);
		btnViewerCompileFiles.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.TOP, false, false, 1, 1, 0, 0));
		btnViewerCompileFiles.setImage(Icons.getBackupIcon());
		btnViewerCompileFiles.setToolTipText(Constants.getStringVaule("btn_tips_viewdetail"));
		btnViewerCompileFiles.addSelectionListener(new ShowCompileFilesAction());
		textCompileFiles=new Text(Panel,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP|SWT.BORDER);//
		textCompileFiles.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 7, 1, 10, 160));
		if(!StringUtil.isNullOrEmpty(this.data.getCmpFileList())){
			textCompileFiles.setText(this.data.getCmpFileList());
		}
		
		//////////////////////////�����б�(��SVN�Ϲ�����ļ���һ�����ļ�)////////////////////////////
		Label labSource=new Label(Panel,SWT.NONE);
		labSource.setText(Constants.getStringVaule("label_source"));
		labSource.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.TOP, true, false, 3, 1, 0, 0));
		labSource.setToolTipText(Constants.getStringVaule("table_tips_candragfile"));
		attachTable=new Table(Panel,SWT.V_SCROLL|SWT.MULTI|SWT.BORDER);
		attachTable.setHeaderVisible(true);
		attachTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 17, 1, 0, 120));
		attachTable.setLinesVisible(true);
		attachTable.addMouseListener(new FileItemAction(attachTable));
		attachTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
		  for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(attachTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
		  //Դ�븽���嵥
		  List<RFile> files=TASK.getAttach(this.data.getId(),this.data.getCurrentVersion(),TaskBean.AttachType.Normal.ordinal()+"");
		  if(files!=null&&files.size()>0){
			  for(RFile rfile:files){
				    TableItem tableItem=new TableItem(attachTable,SWT.BORDER);
					tableItem.setText(new String[]{rfile.getFileName()});
					Image icon=Icons.getFileImage(rfile.getFileName());
					tableItem.setImage(icon);
					//ֻ���������п��Ļ�������ɾ��Դ��ĸ���
					if(this.editable){
						final TableEditor editorDel=new TableEditor(attachTable);
						Button btnDel=new Button(attachTable,SWT.PUSH);
						btnDel.setImage(Icons.getDeleteIcon());
						btnDel.addSelectionListener(new SelectionAdapter(){
							public void widgetSelected(SelectionEvent e){
								   RFile bean=(RFile)editorDel.getItem().getData();
								  deleteFileItem(attachTable,bean);
								  ((Button)e.getSource()).dispose();
								  attachTable.pack();
							}
						});
						editorDel.grabHorizontal=true;
						editorDel.setEditor(btnDel, tableItem, 1);
					}
					tableItem.setData(rfile);
			  }
		  }
			for(int j=0;j<header.length;j++){		
				attachTable.getColumn(j).pack();
			}
			if(this.editable){
				DropTarget targetTable=new DropTarget(attachTable,DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
				targetTable.setTransfer(new Transfer[]{FileTransfer.getInstance()});
				targetTable.addDropListener(new DropAttatAction(attachTable));
		   }
		 attachTable.pack();
		 
		//////////////����˵��(��汾����Ա��ͨ���ֶΣ�////////////////////////////////////////////////////
		Label labEditDesc=new Label(Panel,SWT.NONE);
		labEditDesc.setText(Constants.getStringVaule("label_editDesc"));
		labEditDesc.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.TOP, true, false, 3, 1, 0, 0));
		textEditDesc=new Text(Panel,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP|SWT.BORDER);//
		textEditDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 17, 1, 10, 80));
		if(!StringUtil.isNullOrEmpty(this.data.getVersionDesc())){
			textEditDesc.setText(this.data.getVersionDesc());
		}
		
		//////////////////////�Ƿ���������Ƿ񷽰����� 2�������б�///////////////////////////////////////
		Label labSLTCheck=new Label(Panel,SWT.NONE);
		labSLTCheck.setText(Constants.getStringVaule("label_solutionCheck"));
		labSLTCheck.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.TOP, true, false, 3, 1, 0, 0));
		labSLTCheck.setToolTipText(Constants.getStringVaule("table_tips_candragfile"));
		Group groupSLTCheck=new Group(Panel,SWT.SHADOW_ETCHED_OUT);
		groupSLTCheck.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 7, 1, 0, 0));
		groupSLTCheck.setLayout(new FillLayout(SWT.HORIZONTAL));
		btnSLTCOK=new Button(groupSLTCheck,SWT.RADIO|SWT.FLAT);
		btnSLTCOK.setText("��Ҫ");
		if("1".equals(this.data.getRstApprise())){
			btnSLTCOK.setSelection(true);
		 }
	    Button  btnSLTCNot=new Button(groupSLTCheck,SWT.RADIO|SWT.FLAT);
	    btnSLTCNot.setText("����Ҫ");
	    groupSLTCheck.pack();
	    
	    
	    Label labCodeCheck=new Label(Panel,SWT.NONE);
		labCodeCheck.setText(Constants.getStringVaule("label_codeCheck"));
		labCodeCheck.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 3, 1, 0, 0));
		labCodeCheck.setToolTipText(Constants.getStringVaule("table_tips_candragfile"));
		Group groupCodeCheck=new Group(Panel,SWT.SHADOW_ETCHED_OUT);
		groupCodeCheck.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 7, 1, 0, 0));
		groupCodeCheck.setLayout(new FillLayout(SWT.HORIZONTAL));
	    btnCCOK=new Button(groupCodeCheck,SWT.RADIO|SWT.FLAT);
		btnCCOK.setText("��Ҫ");
		if("1".equals(this.data.getCodeApprise())){
			btnCCOK.setSelection(true);
		 }
	    Button  btnCCNot=new Button(groupCodeCheck,SWT.RADIO|SWT.FLAT);
	    btnCCNot.setText("����Ҫ");
	    groupCodeCheck.pack();
	    
	    
		 
		scAttachTable=new Table(Panel,SWT.V_SCROLL|SWT.MULTI|SWT.BORDER);
		scAttachTable.setHeaderVisible(true);
		scAttachTable.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, true, 10, 1, 0, 60));
		scAttachTable.setLinesVisible(true);
		scAttachTable.addMouseListener(new FileItemAction(scAttachTable));
		scAttachTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
			  for(int i=0;i<header.length;i++){
					TableColumn tablecolumn=new TableColumn(scAttachTable,SWT.BORDER);
					tablecolumn.setText(header[i]);
					tablecolumn.setMoveable(true);
				}
			  List<RFile> scfiles=TASK.getAttach(this.data.getId(),this.data.getCurrentVersion(),TaskBean.AttachType.Solution.ordinal()+"");
			  if(scfiles!=null&&scfiles.size()>0){
				  for(RFile rfile:scfiles){
					    TableItem tableItem=new TableItem(scAttachTable,SWT.BORDER);
						tableItem.setText(new String[]{rfile.getFileName()});
						Image icon=Icons.getFileImage(rfile.getFileName());
						tableItem.setImage(icon);
						//ֻ���������п��Ļ�������ɾ��Դ��ĸ���
						if(this.editable){
							final TableEditor editorDel=new TableEditor(scAttachTable);
							Button btnDel=new Button(scAttachTable,SWT.PUSH);
							btnDel.setImage(Icons.getDeleteIcon());
							btnDel.addSelectionListener(new SelectionAdapter(){
								public void widgetSelected(SelectionEvent e){
									   RFile bean=(RFile)editorDel.getItem().getData();
									  deleteFileItem(scAttachTable,bean);
									  ((Button)e.getSource()).dispose();
									  scAttachTable.pack();
								}
							});
							editorDel.grabHorizontal=true;
							editorDel.setEditor(btnDel, tableItem, 1);
						}
						tableItem.setData(rfile);
				  }
			  }
				for(int j=0;j<header.length;j++){		
					scAttachTable.getColumn(j).pack();
				}
				if(this.editable){
					DropTarget targetTable=new DropTarget(scAttachTable,DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
					targetTable.setTransfer(new Transfer[]{FileTransfer.getInstance()});
					targetTable.addDropListener(new DropAttatAction(scAttachTable));
			   }
		scAttachTable.pack();
		
		ccAttachTable=new Table(Panel,SWT.V_SCROLL|SWT.MULTI|SWT.BORDER);
	    ccAttachTable.setHeaderVisible(true);
	    ccAttachTable.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, true, 10, 1, 0, 60));
	    ccAttachTable.setLinesVisible(true);
	    ccAttachTable.addMouseListener(new FileItemAction(ccAttachTable));
	    ccAttachTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
		  for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(ccAttachTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
		  List<RFile> ccfiles=TASK.getAttach(this.data.getId(),this.data.getCurrentVersion(),TaskBean.AttachType.Code.ordinal()+"");
		  if(ccfiles!=null&&ccfiles.size()>0){
			  for(RFile rfile:ccfiles){
				    TableItem tableItem=new TableItem(ccAttachTable,SWT.BORDER);
					tableItem.setText(new String[]{rfile.getFileName()});
					Image icon=Icons.getFileImage(rfile.getFileName());
					tableItem.setImage(icon);
					//ֻ���������п��Ļ�������ɾ��Դ��ĸ���
					if(this.editable){
						final TableEditor editorDel=new TableEditor(ccAttachTable);
						Button btnDel=new Button(ccAttachTable,SWT.PUSH);
						btnDel.setImage(Icons.getDeleteIcon());
						btnDel.addSelectionListener(new SelectionAdapter(){
							public void widgetSelected(SelectionEvent e){
								   RFile bean=(RFile)editorDel.getItem().getData();
								  deleteFileItem(ccAttachTable,bean);
								  ((Button)e.getSource()).dispose();
								  ccAttachTable.pack();
							}
						});
						editorDel.grabHorizontal=true;
						editorDel.setEditor(btnDel, tableItem, 1);
					}
					tableItem.setData(rfile);
			  }
		  }
			for(int j=0;j<header.length;j++){		
				ccAttachTable.getColumn(j).pack();
			}
			if(this.editable){
				DropTarget targetTable=new DropTarget(ccAttachTable,DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
				targetTable.setTransfer(new Transfer[]{FileTransfer.getInstance()});
				targetTable.addDropListener(new DropAttatAction(ccAttachTable));
		   }
		ccAttachTable.pack();
		
		///////////////Ӱ�췶Χ˵��(��ȥ�����ֶ�)///////////////////////////
		/*
		Label labImpactDesc=new Label(Panel,SWT.NONE);
		labImpactDesc.setText(Constants.getStringVaule("label_impactDesc"));
		labImpactDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textImpactDesc=new Text(Panel,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP|SWT.BORDER);//SWT.V_SCROLL|
		textImpactDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 10, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getScopeDesc())){
			textImpactDesc.setText(this.data.getScopeDesc());
		}
		
		///////////////�ύ˵��(��ȥ�����ֶΣ��ύ�汾�ķ�������)///////////////////////////
		Label labSubmitDesc=new Label(Panel,SWT.NONE);
		labSubmitDesc.setText(Constants.getStringVaule("label_submitDesc"));
		labSubmitDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		comSubmitDesc=new Segment(Panel,this.editable,1,3);
		comSubmitDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 200));
		 List<JSONObject> jsonObjects =null;
		if(!StringUtil.isNullOrEmpty(this.data.getSubmitJson())&&!"[]".equals(this.data.getSubmitJson())){
			  jsonObjects=JSONArray.fromObject(this.data.getSubmitJson());
		}else{
			 jsonObjects = new ArrayList<JSONObject>();
			 JSONObject json1=new JSONObject();
			 json1.put("name", "client");
			 json1.put("value", "");
			 jsonObjects.add(json1);
			 JSONObject json2=new JSONObject();
			 json2.put("name", "lbm");
			 json2.put("value", "");
			 jsonObjects.add(json2);
			 JSONObject json3=new JSONObject();
			 json3.put("name", "script");
			 json3.put("value", "");
			 jsonObjects.add(json3);
			 JSONObject json4=new JSONObject();
			 json4.put("name", "relatedFunc");
			 json4.put("value", "");
			 jsonObjects.add(json4);
		}
		comSubmitDesc.setDatas(new String[]{"�ͻ���","LBM","�ű�","��������"}, jsonObjects.toString());
		comSubmitDesc.show();

		///////////////���Խ���(��ȥ�����ֶ�)///////////////////////////
		Label labTestSuggestDesc=new Label(Panel,SWT.NONE);
		labTestSuggestDesc.setText(Constants.getStringVaule("label_testSuggestDesc"));
		labTestSuggestDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textTestSuggestDesc=new Text(Panel,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP|SWT.BORDER);//SWT.V_SCROLL|
		textTestSuggestDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 10, 0));
		if(!StringUtil.isNullOrEmpty(this.data.getTestDesc())){
			textTestSuggestDesc.setText(this.data.getTestDesc());
		}
		
		//////////////�Բ�˵��(��ȥ�����ֶ�)///////////////////////////
		Label labSelfTestDesc=new Label(Panel,SWT.NONE);
		labSelfTestDesc.setText(Constants.getStringVaule("label_selfTestDesc"));
		labSelfTestDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		selfTestTable=new DavidTable(Panel,this.editable,4);
		if(!StringUtil.isNullOrEmpty(this.data.getSelfTestJson())&&!"[]".equals(this.data.getSelfTestJson())){
			selfTestTable.setHeader(new String[]{"��������(������Ҫ�㣬���԰���)","���Խ��","��ע"}, this.data.getSelfTestJson());
		}else{
			selfTestTable.setHeader(new String[]{"��������(������Ҫ�㣬���԰���)","���Խ��","��ע"}, new String[]{"content","result","comment"});
		}
		selfTestTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 0));
		selfTestTable.show();
		
		/////////////��������(��ȥ�����ֶ�)///////////////////////////
		Label labTestCaseDesc=new Label(Panel,SWT.NONE);
		labTestCaseDesc.setText(Constants.getStringVaule("label_testcaseDesc"));
		labTestCaseDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textCaseTable=new DavidTable(Panel,this.editable,4);
		if(!StringUtil.isNullOrEmpty(this.data.getCaseJson())&&!"[]".equals(this.data.getCaseJson())){
			textCaseTable.setHeader(new String[]{"���","���ܵ�","��ʼ����","��ϸ���Լ�鲽��","�����������"}, this.data.getCaseJson());
		}else{
			textCaseTable.setHeader(new String[]{"���","���ܵ�","��ʼ����","��ϸ���Լ�鲽��","�����������"}, new String[]{"sno","function","condition","detail","result"});
		}
		textCaseTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 0));
		textCaseTable.show();
		*/
		///////////////////////////////////////////////////////////////////////////////////////
		if(!this.editable){
			textResolveDesc.setEditable(false);
			textCompileFiles.setEditable(false);
			textFileList.setEditable(false);
			textEditDesc.setEditable(false);
		}
/*		if(StringUtil.isNullOrEmpty(this.data.getCurrentVersion())){
			textResolveDesc.setEditable(false);
			textTestSuggestDesc.setEditable(false);
			textImpactDesc.setEditable(false);
			textCompileFiles.setEditable(false);
			textFileList.setEditable(false);
			textImpactDesc.setEditable(false);
			textEditDesc.setEditable(false);
		}*/
		Panel.pack();
	}
	
	public void deleteFileItem(Table table, RFile bean){
		TableItem[] items=	table.getItems();
		if(items!=null&&items.length>0){
			for(int i=0;i<items.length;i++){
				RFile data=(RFile)items[i].getData();
				if(bean.getFileName().equals(data.getFileName())){
					table.remove(i);
					return;
				}
			}
		}
	}
	

	
	public class ScrollCompositeResizeAction extends ControlAdapter{  
		public void controlResized(ControlEvent e) {
			content.setSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			self.setMinSize(content.getSize());
		}
	}
	
	public class FileItemAction extends MouseAdapter{
		public Table table;
		public FileItemAction(Table table){
			this.table=table;
		}
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem tableItem=table.getItem(new  Point(e.x,e.y));
			 if(tableItem!=null){
				 RFile rfile=(RFile)tableItem.getData();
				 String localPath=rfile.getLocalPath();
				 String  remotePath=rfile.getRpath();
				 if(StringUtil.isNullOrEmpty(localPath)){
					 String path=Paths.getInstance().getWorkDir();
			         String localDir=FileUtils.formatPath(path)+File.separator+data.getId()+File.separator;
			         File dir=new File(localDir);
		            if(!dir.exists()){
		            	dir.mkdirs();
		            }
		        	boolean downResult=FtpFileService.getService().dowload(rfile.getFileName(), remotePath, localDir,rfile.getFileTime());    
		        	if(downResult){//���سɹ����ļ������Ե��ñ��س����
		   				 localPath=localDir+File.separator+rfile.getFileName();
		   				  rfile.setLocalPath(localPath);
		   				  tableItem.setData(rfile);
		   				 FileUtils.openFileByLocal(localPath);
		        	}else{
		        		String msg="�ļ���"+rfile.getFileName()+"������ʧ�ܡ����Ժ����ԣ�";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
		        	}
				 }else{
					 String fileName=rfile.getFileName();
					 FileUtils.openFileByLocal(localPath);
				 }
			 }
		 }
	}
	
	
	
	public boolean saveInfo(){
		String resolveDesc=textResolveDesc.getText();
		String fileList=textFileList.getText();
		String desc=textEditDesc.getText();
		if(StringUtil.isNullOrEmpty(resolveDesc,4000)||StringUtil.isNullOrEmpty(fileList,4000)||StringUtil.isNullOrEmpty(desc,600)){
			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
			box.setText(Constants.getStringVaule("messagebox_alert"));
			box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
			box.open();
			return false;
		}else{
			String compileFiles=textCompileFiles.getText();
			String impaceDesc="";
			String testSuggestDesc="";
			String submitDesc=" ";//comSubmitDesc.getData();
			String selfTest="";
			String textCase="";
			String codeApprise="0";
			if(btnCCOK.getSelection()){
				codeApprise="1";
				if(ccAttachTable.getItemCount()<=0){
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("�������������Ҫ�ύ(ֱ����ק)�����ĵ�������Ҫ�ȸ���!");
					box.open();
					return false;
				}
			}
			String rstApprise="0";
			if(btnSLTCOK.getSelection()){
				rstApprise="1";
				if(scAttachTable.getItemCount()<=0){
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("�������������Ҫ�ύ(ֱ����ק)�����ĵ�������Ҫ�ȸ���!");
					box.open();
					return false;
				}
			}
			
			data.setRevDesc(resolveDesc);
			data.setFileList(fileList);
			data.setCmpFileList(compileFiles);
			data.setScopeDesc(impaceDesc);
			data.setVersionDesc(desc);
			data.setTestDesc(testSuggestDesc);
			data.setSubmitJson(submitDesc);
			data.setSelfTestJson(selfTest);
			data.setCaseJson(textCase);
			data.setCodeApprise(codeApprise);
			data.setRstApprise(rstApprise);
			data.makeNewVersion();
			String error="";
			int result=data.setSubmitInfo();
			if(result>0){
				//attachment and upload
				//data.removeAttachment();
				//���渽��
				error=this.uploadAttach(attachTable, TaskBean.AttachType.Normal.ordinal()+"");
				//�������󸽼��Ĵ���
				if(StringUtil.isNullOrEmpty(error)){
					error=this.uploadAttach(ccAttachTable, TaskBean.AttachType.Code.ordinal()+"");
					//�������󸽼��Ĵ���
					if(StringUtil.isNullOrEmpty(error)){
						error=this.uploadAttach(scAttachTable, TaskBean.AttachType.Solution.ordinal()+"");
					}
				}
				
				if(StringUtil.isNullOrEmpty(error)){
					return true;
				}else{
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(error);
					box.open();
					return false;
				}
			}else{
				return true;
			}
		}
		
	}
	
	public  String uploadAttach(Table table,String type){
		String error="";
		TableItem[] fileItems=table.getItems();
		if(fileItems!=null&&fileItems.length>0){
			for(TableItem fileItem:fileItems){
				RFile file=(RFile)fileItem.getData();
				String time="";
				boolean upRet=true;
				boolean needUpload=false;
				if(StringUtil.isNullOrEmpty(file.getLocalPath())){
					upRet=true;
					time=file.getFileTime();
				}else{
					 File fileObj=new File(file.getLocalPath());
					 time=DateUtil.getTimeFormLong(fileObj.lastModified());
					String location=Context.session.userID+"\\"+DateUtil.getCurrentDate("yyyy-MM-dd")+"\\"+data.getId();
					 file.setRpath(location);
					 //�ļ�ʵ�御�������ظ����ϴ�
					 if(!file.existOnServer()){
						 upRet=FtpFileService.getService().upLoad(fileObj.getAbsolutePath(), location);
						 needUpload=true;
					 }else{
						 upRet=true;
						 needUpload=false;
					 }
				}
				if(upRet){
					String fileID=UUID.randomUUID().toString();
					file.setFileID(fileID);
					data.addAttach(fileID, file.getMd5(), file.getRpath(), file.getFileName(), time, Context.session.userID,needUpload,type);
				}else{
					String msg="�ļ���"+file.getFileName()+"���ϴ�ʧ�ܡ�";
					error+=msg;
				}
			}
		}
		return error;
	}
	
	public class ChooseAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			 String groupID="Flow"+actions.getSelectionIndex();
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
		}
	}
	
	public class ShowFileListAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Point pos=((Control)e.widget).toDisplay(e.x,e.y);
			TextViewer viewer=new TextViewer(textFileList,pos);
			viewer.show();
		}
	}
	
	public class ShowResolveAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Point pos=((Control)e.widget).toDisplay(e.x,e.y);
			TextViewer viewer=new TextViewer(textResolveDesc,pos);
			viewer.show();
		}
	}
	
	
	public class ShowCompileFilesAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Point pos=((Control)e.widget).toDisplay(e.x,e.y);
			TextViewer viewer=new TextViewer(textCompileFiles,pos);
			viewer.show();
		}
	}
	
	
	public class SaveInfoAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			if(saveInfo()){
				String msg="����ɹ���";
				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				box.setText(Constants.getStringVaule("alert_successoperate"));
				box.setMessage(msg);
				box.open();
				textResolveDesc.setEditable(false);
				textCompileFiles.setEditable(false);
				textFileList.setEditable(false);
				textEditDesc.setEditable(false);
				attachTable.setEnabled(false);
				ccAttachTable.setEnabled(false);
				scAttachTable.setEnabled(false);
			}
		}
	}
	
	
	//�����û�ѡ�������ߵ���ͬ������
	public class SubmitNewReqAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			//save information
			//flow process
			if(saveInfo()){
				//״̬δ��������������¼״̬�ٴ���
				String nextUser=nexter.getText();
				String nextUserID=StringUtil.getUserIdFromName(nextUser);
				String actionStepName=actions.getText();
				String nextStep="";
				String comment="�����ύ";
				if("�ύ�������".equals(actionStepName)){
					nextStep=VStep.CodeCheck;
				}else{
					nextStep=VStep.VersionMake;
				}
				String seq=UUID.randomUUID().toString();
				seq=seq.replace("-", ""); 
				data.logStep(seq, Context.session.userID, comment, data.getStatus());
				data.progress(nextStep, nextUserID, Context.session.userID);
	            data.ResetReleaseFlag(TaskBean.ReleaseStatus.Organize.ordinal()+"");//����ʵ�����ʱ��ͷ���״̬
	            //����һ�����ȥ����ͬ������״̬����״̬ ����˵��
				VIEW.setFlagByReq(data.getId(), TaskBean.ReleaseStatus.Organize.ordinal()+"",data.getVersionDesc());
	            
				String msg=Context.session.userName+"("+Context.session.userID+")��"+DateUtil.getCurrentTime()+"�����ύ��"+data.getTname()+"������ġ�";
				IMessage message=new IMessage(nextUserID,msg);
				message.addMsg();
				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(Constants.getStringVaule("alert_successoperate"));
				box.open();
				btnSave.setEnabled(false);
				btnSubmit.setEnabled(false);
				//���������б�ȿؼ�Ҳ����Ϊ�����޸�
				textResolveDesc.setEditable(false);
				textCompileFiles.setEditable(false);
				textFileList.setEditable(false);
				textEditDesc.setEditable(false);
				attachTable.setEnabled(false);
				ccAttachTable.setEnabled(false);
				scAttachTable.setEnabled(false);
				RequirementView.getInstance(null).refreshTree();
			}
		}
	}
	
	public class RelateQuestionAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			List<String> questions=new ArrayList<String>();
 			ReqQuestionView view=new ReqQuestionView(data,questions);
 			view.show();
		}
	}
	
	/*public class UpdateReqAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			if(saveInfo()){
				//����״̬
				data.ResetReleaseFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
				//����״̬ ����˵��
				VIEW.setFlagByReq(data.getId(), TaskBean.ReleaseStatus.Organized.ordinal()+"",data.getVersionDesc());
				//֪ͨ�汾����Ա���汾
				 RequirementLog log=TASK.getStepLog(data.getId(), VStep.VersionMake);
				 if(log!=null){
					 String nexter=log.getUserID();
					 String msg=Context.session.userName+"("+Context.session.userID+")��"+DateUtil.getCurrentTime()+"��������"+data.getTname()+"���İ汾�������뼰ʱ����";
					  IMessage message=new IMessage(nexter,msg);
					  message.addMsg();
				 }
				 btnRelated.setEnabled(false);
				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(Constants.getStringVaule("alert_successoperate"));
				box.open();	
			}
		}
	}*/
	
}
