package resource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import model.QUESTIONS;
import model.TASK;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

import utils.DataUtil;
import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

import bean.QUESTIONFILEBean;
import bean.QUESTIONSBean;
import bean.TaskBean;
import bean.ViewBean;
import business.developer.view.RequirementView;

public class QuestionDetailView {
	public QUESTIONSBean data;
	private Composite parent;
	public Composite content;
	public CTabFolder propertyFloder;
	public String opType;
	public List<TaskBean> Reqs;
	public QuestionDetailView(QUESTIONSBean bean,String op,Composite com){
		this.data=bean;
		this.parent=com;
		this.opType=op;
		content=new  Composite(com,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		this.createAndShow();
		content.pack();
	}
	
	private void createAndShow(){
		propertyFloder=new CTabFolder(content,SWT.TOP|SWT.BORDER);
		propertyFloder.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		propertyFloder.setMaximizeVisible(true);
		propertyFloder.setMinimizeVisible(true);  
		propertyFloder.setTabHeight(20);
		propertyFloder.setSimple(false);
	    this.createBasicTab();
	    this.createDevelopeTab();
		propertyFloder.pack();
	}
	
	public Text textQid,textCrtUser,textClass,textStatus,textReq,textOwner,textQDesc,textDDesc=null;
	public Combo cboClass,cboReq=null;
	public Table tFilesTable,dFilesTable=null;
	public Button btnSave,btnSubmit,btnClose,btnLeft,btnReSubmit=null;
	 private void createBasicTab(){
		 CTabItem itemBasic=new CTabItem(propertyFloder,SWT.NONE);
		 itemBasic.setText(Constants.getStringVaule("tabItem_basic"));
		  Composite pannel=new Composite(propertyFloder,SWT.NONE);
		  pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		  pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		  
		 Group basicPannel=new Group(pannel,SWT.MULTI|SWT.H_SCROLL);
		 basicPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 6, 1, 0, 0));
		 basicPannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		 basicPannel.setText(Constants.getStringVaule("group_basic"));
			
		  Label labID=new Label(basicPannel,SWT.NONE);
		  labID.setText(Constants.getStringVaule("label_qid"));
		  labID.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		  textQid=new Text(basicPannel,SWT.BORDER);
		  textQid.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		  textQid.setText(data.getId());
		  textQid.setEditable(false);
		  Label labCrtUser=new Label(basicPannel,SWT.NONE);
		  labCrtUser.setText(Constants.getStringVaule("label_suser"));
		  labCrtUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		  textCrtUser=new Text(basicPannel,SWT.BORDER);
		  textCrtUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		  if("0".equals(this.opType)){	
			  textCrtUser.setText(Context.session.userName+"("+Context.session.userID+")");
			  textCrtUser.setEditable(false);
		  }else{
			  textCrtUser.setText(this.data.getCurUserFull());
			  textCrtUser.setEditable(false);
		  }
		  
		  Label labClass=new Label(basicPannel,SWT.NONE);
		  labClass.setText(Constants.getStringVaule("label_qclass"));
		  labClass.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		  if("0".equals(this.opType)){	
			  cboClass=new Combo(basicPannel,SWT.DROP_DOWN);
			  cboClass.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
			  this.initCombo(cboClass, "QUESTIONS.QTYPE");
		  }else{
			  textClass=new Text(basicPannel,SWT.BORDER);
			  textClass.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
			  String qtype=Dictionary.getDictionaryMap("QUESTIONS.QTYPE").get(this.data.getQtype());
			  textClass.setText(qtype);
			  textClass.setEditable(false);
		  }
		  Label labStatus=new Label(basicPannel,SWT.NONE);
		  labStatus.setText(Constants.getStringVaule("label_qstatus"));
		  labStatus.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		  if("0".equals(this.opType)){	

			  textStatus=new Text(basicPannel,SWT.BORDER);
			  textStatus.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
			  String qtype=Dictionary.getDictionaryMap("QUESTIONS.QSTATUS").get("0");
			  textStatus.setText(qtype);
			  textStatus.setEditable(false);
		  }else{
			  textStatus=new Text(basicPannel,SWT.BORDER);
			  textStatus.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
			  String qtype=Dictionary.getDictionaryMap("QUESTIONS.QSTATUS").get(this.data.getQstatus());
			  textStatus.setText(qtype);
			  textStatus.setEditable(false);
		  }
		  
		  Label labReq=new Label(basicPannel,SWT.NONE);
		  labReq.setText(Constants.getStringVaule("label_qreq"));
		  labReq.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		  if("0".equals(this.opType)){	
			  cboReq=new Combo(basicPannel,SWT.DROP_DOWN);
			  cboReq.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
			  cboReq.setToolTipText(Constants.getStringVaule("cbo_tips_searchreq"));
			  //模糊匹配需求，选中加载需求所属开发人员
			  Reqs=TASK.getAllTasks();
			  if(Reqs!=null&&Reqs.size()>0){
				  String[] items=new String[Reqs.size()];
				  int index=0;
				  for(TaskBean req:Reqs){
					  items[index]=req.getReqID()+" "+req.getTname();
					  index++;
				  }
				  cboReq.setItems(items);
			  }
			  cboReq.addKeyListener(new AutoMatchReqAction());
			  cboReq.addSelectionListener(new SetReqUserAction());
			  
		  }else{
			  textReq=new Text(basicPannel,SWT.BORDER);
			  textReq.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
			  textReq.setText(this.data.getReqFullName());
			  textReq.setEditable(false);
		  }
		  Label labOwner=new Label(basicPannel,SWT.NONE);
		  labOwner.setText(Constants.getStringVaule("label_qown"));
		  labOwner.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		  if("0".equals(this.opType)){	 
			  textOwner=new Text(basicPannel,SWT.BORDER);
			  textOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 1, 1, 0, 0));
			  Button btnChoose=new  Button(basicPannel,SWT.PUSH);
			  btnChoose.setText("   "+Constants.getStringVaule("btn_choose")+"   ");
			  btnChoose.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
			  btnChoose.addSelectionListener(new ChooseUserAction());
		  }else{
			  textOwner=new Text(basicPannel,SWT.BORDER);
			  textOwner.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
			  textOwner.setText(this.data.getCurUserFull());
			  textOwner.setEditable(false);
		  }
		  basicPannel.pack();
		  
		  Group descPannel=new Group(pannel,SWT.MULTI|SWT.H_SCROLL);
		  descPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 6, 1, 0, 0));
		  descPannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		  descPannel.setText(Constants.getStringVaule("group_qinfo"));
		  Label labDesc=new Label(descPannel,SWT.NONE);
		  labDesc.setText(Constants.getStringVaule("label_qdesc"));
		  labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		  textQDesc=new Text(descPannel,SWT.BORDER|SWT.MULTI);
		  textQDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 5, 1, 0, 100));
		  if(!StringUtil.isNullOrEmpty(this.data.getQdesc()))
			  textQDesc.setText(this.data.getQdesc());
		  textQDesc.setEditable(false);
		  
		  Label labDAtta=new Label(descPannel,SWT.NONE);
		  labDAtta.setText(Constants.getStringVaule("label_qatta"));
		  labDAtta.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		  Composite comTable=new Composite(descPannel,SWT.BORDER);
		  comTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 5, 1, 0, 80));
		  comTable.setLayout(LayoutUtils.getComGridLayout(10, 0));
		  
		  Composite actionPanel=new Composite(comTable,SWT.NONE);
		  actionPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 5, 1, 0, 28));
		  actionPanel.setLayout(LayoutUtils.getComGridLayout(10, 0));
		  Button btnUplaod=new  Button(actionPanel,SWT.PUSH);
		  btnUplaod.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.BEGINNING, false, false, 1, 1, 0, 0));
		  btnUplaod.setImage(Icons.getAddIcon());
		  btnUplaod.setEnabled(false);
		  Label labAdd=new Label(actionPanel,SWT.NONE);
		  labAdd.setText(Constants.getStringVaule("btn_add"));
		  labAdd.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		  
		  Button btnDelete=new  Button(actionPanel,SWT.PUSH);
		  btnDelete.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.BEGINNING, false, false, 1, 1, 0, 0));
		  btnDelete.setImage(Icons.getDeleteIcon());
		  btnDelete.setEnabled(false);
		  Label labDelete=new Label(actionPanel,SWT.NONE);
		  labDelete.setText(Constants.getStringVaule("btn_delete"));
		  labDelete.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		  actionPanel.pack();
		  tFilesTable=new Table(comTable,SWT.SINGLE |SWT.FULL_SELECTION);
		  tFilesTable.setHeaderVisible(true);
		  tFilesTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 10, 1, 0, 100));
		  tFilesTable.setLinesVisible(true);
		  tFilesTable.addMouseListener(new FileItemAction(tFilesTable));
		  tFilesTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
			 String[] header=new String[]{"附件名","上传时间"};
			 for(int i=0;i<header.length;i++){
					TableColumn tablecolumn=new TableColumn(tFilesTable,SWT.BORDER);
					String lenheader=StringUtil.rightPad(StringUtil.leftpad(header[i], 30, " "),60," ");
					tablecolumn.setText(lenheader);
				}
			 List<QUESTIONFILEBean> qfiles=this.data.getFiles();
			 if(qfiles!=null&&qfiles.size()>0){
				 List<QUESTIONFILEBean>tfiles=this.data.getTFiles();
				 if(tfiles!=null&&tfiles.size()>0){
					 for(QUESTIONFILEBean file:tfiles){
							TableItem tableItem=new TableItem(tFilesTable,SWT.BORDER);
			   				String time=file.getCrtTime();
							tableItem.setText(new String[]{file.getFileName(),time});
							Image icon=Icons.getFileImage(file.getFileName());
							tableItem.setImage(icon);
							tableItem.setData(file);
					 }
				 }
			 }
			 for(int j=0;j<header.length;j++){		
				 tFilesTable.getColumn(j).pack();
				}
			 tFilesTable.pack();	   
			 btnUplaod.addSelectionListener(new AddFilesAction(tFilesTable,QUESTIONFILEBean.Type.Test.ordinal()+""));
			 btnDelete.addSelectionListener(new DeleteFilesAction(tFilesTable));
		  comTable.pack();
		  descPannel.pack();
		  if((Constants.RoleType.Tester.ordinal()+"").equals(Context.session.roleID)){
			  if("0".equals(this.opType)||"0".equals(this.data.getQstatus())){	
				    btnUplaod.setEnabled(true);
				    btnDelete.setEnabled(true);
				    textQDesc.setEditable(true);
				    btnSave= new  Button(pannel,SWT.PUSH);
				    btnSave.setText("   "+Constants.getStringVaule("btn_save")+"   ");
				    btnSave.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
				    btnSave.addSelectionListener(new SaveQueInfoAction());
				    
				    btnSubmit= new  Button(pannel,SWT.PUSH);
				    btnSubmit.setText("   "+Constants.getStringVaule("btn_submit")+"   ");
				    btnSubmit.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
				    btnSubmit.addSelectionListener(new AddQueAction());
			  }else{
				  if("3".equals(this.data.getQstatus())){
					  btnUplaod.setEnabled(true);
					  btnDelete.setEnabled(true);
					  textQDesc.setEditable(true);
					  btnClose= new  Button(pannel,SWT.PUSH);
					  btnClose.setText("   "+Constants.getStringVaule("btn_close")+"   ");
					  btnClose.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
					  btnClose.addSelectionListener(new CloseQuestAction());
					  
					  btnLeft= new  Button(pannel,SWT.PUSH);
					  btnLeft.setText(Constants.getStringVaule("btn_left"));
					  btnLeft.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
					  btnLeft.addSelectionListener(new LeftQuestAction());
					  
					  btnReSubmit= new  Button(pannel,SWT.PUSH);
					  btnReSubmit.setText(Constants.getStringVaule("btn_resubmit"));
					  btnReSubmit.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
					  btnReSubmit.addSelectionListener(new ResubmitQuestAction());
				  }
			  }
		  }
		  itemBasic.setControl(pannel);
		  propertyFloder.setSelection(itemBasic);
	 }
	 
	 public Button btnReply,btnReturn=null;
	 private void createDevelopeTab(){
		 CTabItem itemDevelop=new CTabItem(propertyFloder,SWT.NONE);
		 itemDevelop.setText(Constants.getStringVaule("tabItem_developer"));
		  Composite pannel=new Composite(propertyFloder,SWT.NONE);
		  pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		  pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
	
		  Label labDDesc=new Label(pannel,SWT.NONE);
		  labDDesc.setText(Constants.getStringVaule("label_qddesc"));
		  labDDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		  textDDesc=new Text(pannel,SWT.BORDER|SWT.MULTI);
		  textDDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 5, 1, 0, 100));
		  if(!StringUtil.isNullOrEmpty(this.data.getMdesc()))
			  textDDesc.setText(this.data.getMdesc());
		  textDDesc.setEditable(false);
		  
		  Label labDAtta=new Label(pannel,SWT.NONE);
		  labDAtta.setText(Constants.getStringVaule("label_qatta"));
		  labDAtta.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		  Composite comTable=new Composite(pannel,SWT.BORDER);
		  comTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 5, 1, 0, 80));
		  comTable.setLayout(LayoutUtils.getComGridLayout(10, 0));
		  
		  Composite actionPanel=new Composite(comTable,SWT.NONE);
		  actionPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 10, 1, 0, 28));
		  actionPanel.setLayout(LayoutUtils.getComGridLayout(10, 0));
		  Button btnUplaod=new  Button(actionPanel,SWT.PUSH);
		  btnUplaod.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.BEGINNING, false, false, 1, 1, 0, 0));
		  btnUplaod.setImage(Icons.getAddIcon());
		  btnUplaod.setEnabled(false);
		  Label labAdd=new Label(actionPanel,SWT.NONE);
		  labAdd.setText(Constants.getStringVaule("btn_add"));
		  labAdd.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		  
		  Button btnDelete=new  Button(actionPanel,SWT.PUSH);
		  btnDelete.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.BEGINNING, false, false, 1, 1, 0, 0));
		  btnDelete.setImage(Icons.getDeleteIcon());
		  btnDelete.setEnabled(false);
		  Label labDelete=new Label(actionPanel,SWT.NONE);
		  labDelete.setText(Constants.getStringVaule("btn_delete"));
		  labDelete.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		  actionPanel.pack();
		  
		  dFilesTable=new Table(comTable,SWT.SINGLE|SWT.FULL_SELECTION);
		  dFilesTable.setHeaderVisible(true);
		  dFilesTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 10, 1, 0, 100));
		  dFilesTable.setLinesVisible(true);
		  dFilesTable.addMouseListener(new FileItemAction(dFilesTable));
		  dFilesTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
			 String[] header=new String[]{"附件名","上传时间"};
			 for(int i=0;i<header.length;i++){
					TableColumn tablecolumn=new TableColumn(dFilesTable,SWT.BORDER);
					String lenheader=StringUtil.rightPad(StringUtil.leftpad(header[i], 30, " "),60," ");
					tablecolumn.setText(lenheader);
				}
			 List<QUESTIONFILEBean> qfiles=this.data.getFiles();
			 if(qfiles!=null&&qfiles.size()>0){
				 List<QUESTIONFILEBean>tfiles=this.data.getDFiles();
				 if(tfiles!=null&&tfiles.size()>0){
					 for(QUESTIONFILEBean file:tfiles){
							TableItem tableItem=new TableItem(dFilesTable,SWT.BORDER);
			   				String time=file.getCrtTime();
							tableItem.setText(new String[]{file.getFileName(),time});
							Image icon=Icons.getFileImage(file.getFileName());
							tableItem.setImage(icon);
							tableItem.setData(file);
					 }
				 }
			 }
			 for(int j=0;j<header.length;j++){		
				 dFilesTable.getColumn(j).pack();
				}
			 dFilesTable.pack();
			 btnUplaod.addSelectionListener(new AddFilesAction(dFilesTable,QUESTIONFILEBean.Type.Develper.ordinal()+""));
			 btnDelete.addSelectionListener(new DeleteFilesAction(dFilesTable));
		     comTable.pack();
	     if((Constants.RoleType.Developer.ordinal()+"").equals(Context.session.roleID)){
	    	 if("1".equals(this.data.getQstatus())){
		    	 btnUplaod.setEnabled(true);
		    	 btnDelete.setEnabled(true);
		    	 textDDesc.setEditable(true);
		    	 btnReply= new  Button(pannel,SWT.PUSH);
		    	 btnReply.setText(Constants.getStringVaule("btn_reply"));
		    	 btnReply.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
		    	 btnReply.addSelectionListener(new ReplyQuestAction());
		    	 
		    	 btnReturn=new  Button(pannel,SWT.PUSH);
		    	 btnReturn.setText(Constants.getStringVaule("btn_return"));
		    	 btnReturn.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
		    	 btnReturn.addSelectionListener(new ReturnQuestAction());
	    	 }
	     }
	     
		  pannel.pack();
		  itemDevelop.setControl(pannel);
	 }
	 
	 public void initCombo(Combo combo,String dicType){
		  List<Item> classes=Dictionary.getDictionaryList(dicType);
			  if(classes!=null&&classes.size()>0){
				  String[] items=new String[classes.size()];
				  Map<String,String> classMap=new HashMap<String, String>();
				  int index=0;
				  for(Item item:classes){
					  items[index]=item.getValue();
					  classMap.put(item.getValue(), item.getKey());
					  index++;
				  }
				  combo.setItems(items);
				  combo.select(0);
				  combo.setData(classMap);
		 }
	 }
	 
	 public String lastAttatchPath="";
	 public class AddFilesAction extends SelectionAdapter{
		    public Table table=null;
		    public String fileType="";
		    public AddFilesAction( Table table,String type){
		    	this.table=table;
		    	this.fileType=type;
		    }
	   		public void widgetSelected(SelectionEvent e){
	   			  FileDialog dialog=new FileDialog(AppView.getInstance().getShell(),SWT.OPEN);
	   			  if(StringUtil.isNullOrEmpty(lastAttatchPath)){
	   				dialog.setFilterPath(Paths.getInstance().getWorkDir());
	   			  }else{
	   				dialog.setFilterPath(lastAttatchPath); 
	   			  }
	   			dialog.setFilterNames(new String[]{"All Files(*.*)"});
	   			String path=dialog.open();
	   			if(!StringUtil.isNullOrEmpty(path)){
	   				File file=new File(path);
	   				lastAttatchPath=file.getParent();
	   				TableItem tableItem=new TableItem(table,SWT.BORDER);
	   				String time=DateUtil.getTimeFormLong(file.lastModified());
					tableItem.setText(new String[]{file.getName(),time});
					Image icon=Icons.getFileImage(file.getName());
					tableItem.setImage(icon);
					 QUESTIONFILEBean fileBean=new QUESTIONFILEBean();
					  fileBean.setQid(data.getId());
					  fileBean.setFileName(file.getName());
					  fileBean.setFileId(UUID.randomUUID().toString());
					  fileBean.setFileTime(DateUtil.getTimeFormLong(file.lastModified()));
					  fileBean.setCrtUser(Context.session.userID);
					  fileBean.setLocalPath(file.getAbsolutePath());
					  fileBean.setMd5(FileUtils.getMd5ByFile(file));
					  fileBean.setQfileType(fileType);
					  fileBean.save();
					  tableItem.setData(fileBean);
	   			}
	   		}
	 }
	 
	 public class DeleteFilesAction extends SelectionAdapter{
		    public Table table=null;
		    public DeleteFilesAction( Table table){
		    	this.table=table;
		    }
	   		public void widgetSelected(SelectionEvent e){
	   			int index=table.getSelectionIndex();
	   			if(index>=0){
	   				TableItem item=table.getItem(index);
	   				QUESTIONFILEBean file=(QUESTIONFILEBean)item.getData();
	   				if(file!=null)
	   					file.delete();
	   				table.remove(index);
	   			}
	   		}
	 }
	 
	 public class FileItemAction extends MouseAdapter{
		 public Table table=null;
		    public FileItemAction( Table table){
		    	this.table=table;
		    }
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem tableItem=table.getItem(new  Point(e.x,e.y));
			 if(tableItem!=null){
				 QUESTIONFILEBean fileBean=(QUESTIONFILEBean)tableItem.getData();
				 if(fileBean!=null)
					 fileBean.open();
			 }
		 }
	 }
	 public class ChooseUserAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			UserChoose control=new UserChoose(textOwner);
	   		}
		}
	 
	 public class AutoMatchReqAction extends KeyAdapter{
		 public void keyPressed(KeyEvent e){
			  String name=cboReq.getText();
			  if(!StringUtil.isNullOrEmpty(name)){
				   String[] src=cboReq.getItems();
				   for(int w=0;w<src.length;w++){
					   String text=src[w];
					   if(text.indexOf(name)!=-1){
						   cboReq.setItem(0, src[w]);
						   cboReq.setItem(w, src[0]);
						   cboReq.setListVisible(true);
						   break;
					   }
				   }
			  }
		 }
	 }
	
	 public class SetReqUserAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){ 
	   			 String reqName=cboReq.getText();
	   			 if(Reqs!=null&&Reqs.size()>0){
					  for(TaskBean req:Reqs){
						  if((req.getReqID()+" "+req.getTname()).equals(reqName)){
							  textOwner.setEditable(true);
							  textOwner.setText(req.getOwnerShowName());
							  break;
						  }
					  }
	   			}
	   		}
	 }
	 
	 public class SaveQueInfoAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){ 
	   			   String  reqClass=cboClass.getText();
	   			   String reqStatus=textStatus.getText();
	   			   String reqName=cboReq.getText();
	   			   String reqOwner=textOwner.getText();
	   			   String reqDesc=textQDesc.getText();
	   			   if(StringUtil.isNullOrEmpty(reqClass)||
	   					StringUtil.isNullOrEmpty(reqStatus)||
	   					StringUtil.isNullOrEmpty(reqName)||
	   					StringUtil.isNullOrEmpty(reqOwner)||
	   					StringUtil.isNullOrEmpty(reqDesc,1000) ){
	   				   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
					   box.setText(Constants.getStringVaule("messagebox_alert"));
					   box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
					   box.open();
	   			   }else{
	   				   reqOwner=StringUtil.getUserIdFromName(reqOwner);
  					   reqOwner=reqOwner.trim();
  					   data.setCurUser(reqOwner);
  					   data.setDeveloper(reqOwner);
  					   data.setQtype(cboClass.getSelectionIndex()+"");
  					   data.setCrtUser(Context.session.userID);
  					 TaskBean  req=getReqByName(reqName);
  					   if(req!=null){
  						   data.setReqid(req.getId());
  						   data.setApp(req.getApp());
  					   }
  					   data.setQdesc(reqDesc);
	   				   if(StringUtil.isNullOrEmpty(data.getQstatus())){
	   					   data.setQstatus("0");
	   					   data.save();
	   				   }else{
	   					  data.setQstatus("0");
	   					 data.update();
	   				   }
   					  MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					  box.setText(Constants.getStringVaule("messagebox_alert"));
					  box.setMessage(Constants.getStringVaule("alert_successoperate"));
					  box.open();
					  btnSave.setEnabled(false);
					  btnSubmit.setEnabled(false);
	   			   }
	   		}
	 }
	 
	 public TaskBean getReqByName(String name){
		 if(Reqs!=null&&Reqs.size()>0){
			  for(TaskBean req:Reqs){
				  if((req.getReqID()+" "+req.getTname()).equals(name)){
					  return req;
				  }
			  }
			}
		 return null;
	 }
	 
	 //测试人员提交测试问题，开发任务和视图的发版状态激活为初始化
	 public class AddQueAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){ 
	   			String  reqClass="";
	   			String reqName="";
	   			String reqStatus=textStatus.getText();
		   		  if("0".equals(opType)){	
		   			  reqClass=cboClass.getText();
		   			  reqClass=cboClass.getSelectionIndex()+"";
		   			  reqName=cboReq.getText();
		   		  }else{
		   			reqClass=textClass.getText();
		   			Map<String,String> QTypes=Dictionary.getDictionaryMap("QUESTIONS.QTYPE");
		   			reqClass=(String)DataUtil.getMapFirstKey(QTypes, reqClass);
		   			reqName=textReq.getText();
		   		  }
	   			   String reqOwner=textOwner.getText();
	   			   String reqDesc=textQDesc.getText();
	   			   if(StringUtil.isNullOrEmpty(reqClass)||
	   					StringUtil.isNullOrEmpty(reqStatus)||
	   					StringUtil.isNullOrEmpty(reqName)||
	   					StringUtil.isNullOrEmpty(reqOwner)||
	   					StringUtil.isNullOrEmpty(reqDesc,1000) ){
	   				   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
					   box.setText(Constants.getStringVaule("messagebox_alert"));
					   box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
					   box.open();
	   			   }else{
	   				   if(reqOwner.indexOf(" ")!=-1){
	   					   reqOwner=reqOwner.substring(reqOwner.indexOf(" "));
	   				   }else{
	   					  reqOwner=reqOwner.substring(reqOwner.indexOf("(")+1);
	   					  reqOwner=reqOwner.replace(")", "");
	   				   }
					   reqOwner=reqOwner.trim();
					   data.setCurUser(reqOwner);
					   data.setDeveloper(reqOwner);
					   data.setQtype(reqClass);
					   data.setCrtUser(Context.session.userID);
					   TaskBean  req=getReqByName(reqName);
					   if(req!=null){
						   data.setReqid(req.getId());
						   data.setApp(req.getApp());
					   }
					   data.setQdesc(reqDesc);
	   				   if(StringUtil.isNullOrEmpty(data.getQstatus())){
	   					  data.setQstatus("1");
	   					   data.save();
	   				   }else{
	   					  data.setQstatus("1");
	   					 data.update();
	   				   } 
	   				 String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"提交测试问题【"+data.getId()+"】给您。请及时处理。";
	   				 IMessage message=new IMessage(reqOwner,msg);
					 message.addMsg();
	   				 //开发任务状态跳转
					 req.ResetReleaseFlag(TaskBean.ReleaseStatus.Init.ordinal()+"");
					 //视图状态的跳转
					 String viewID=req.getViewID();
					 if(!StringUtil.isNullOrEmpty(viewID)){
						 ViewBean view=VIEW.getViewById(viewID);
						 view.reSetUptFlag(ViewBean.ReleaseStatus.Init.ordinal()+"");
					 }
			   		  MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					  box.setText(Constants.getStringVaule("messagebox_alert"));
					  box.setMessage(Constants.getStringVaule("alert_successoperate"));
					  box.open();
					  btnSave.setEnabled(false);
					  btnSubmit.setEnabled(false);
	   			   }
	   		}
	 }
	 
	 //关闭的问题不影响视图的流程
	 public class CloseQuestAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){ 
	   			data.setQstatus("4");
	   			data.close();
	   			//关闭问题算一个有效问题
	   			String reqID=data.getReqid();
	   			String userID=data.getDeveloper();
	   			TASK.questionAdd(reqID, userID);
	   			//最后一个问题关闭，对应的视图流程重新回到测试人员手中
	   		/*	String reqID=data.getReqid();
	   			TaskBean task=TASK.getReq(reqID);*/
	   			
	   			btnClose.setEnabled(false);
	   			btnLeft.setEnabled(false);
	   			btnReSubmit.setEnabled(false);
	   		}
	   	}
	 
	 //遗留的问题 不影响视图的流程
	 public class LeftQuestAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){ 
	   			data.setQstatus("5");
	   			data.close();
	   			btnClose.setEnabled(false);
	   			btnLeft.setEnabled(false);
	   			btnReSubmit.setEnabled(false);
	   		}
	   	}
	 
	 public class ResubmitQuestAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){ 
	   		 String reqDesc=textQDesc.getText();
	   		 if(StringUtil.isNullOrEmpty(reqDesc,1000)){
		   			   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
					   box.setText(Constants.getStringVaule("messagebox_alert"));
					   box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
					   box.open();
	   		 }else{
			   		 data.setQdesc(reqDesc);
			   		 data.setQstatus("1");
			   		 data.close();
			   		 //开发任务的发榜状态设置为初始化
			   		 String taskID=data.getReqid();
	   		   		 TaskBean task= TASK.getReq(taskID);
	   		   		 task.ResetReleaseFlag(TaskBean.ReleaseStatus.Init.ordinal()+"");
					 //视图状态的跳转
		   		   	 String viewID=task.getViewID();
					 if(!StringUtil.isNullOrEmpty(viewID)){
						 ViewBean view=VIEW.getViewById(viewID);
						 view.reSetUptFlag(ViewBean.ReleaseStatus.Init.ordinal()+"");
					 }
			   		 String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"重新提交测试问题【"+data.getId()+"】给您。请及时处理。";
					 IMessage message=new IMessage(data.getCurUser(),msg);
					 message.addMsg();
		   			 btnClose.setEnabled(false);
		   			 btnLeft.setEnabled(false);
		   			 btnReSubmit.setEnabled(false);
	   		 	}
	   		}
	   	}
	 
	 //开发人员答复问题，开发任务和视图激活为申请状态
	 public class ReplyQuestAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){ 
	   			 String ddesc=textDDesc.getText();
	   			 if(StringUtil.isNullOrEmpty(ddesc,1000)){
	   			   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
				   box.setText(Constants.getStringVaule("messagebox_alert"));
				   box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
				   box.open();
	   			 }else{
	   				 data.setMdesc(ddesc);
	   		   		 data.setQstatus("2");
	   		   		 data.sumbit();
	   		   		 //开发任务的状态
	   		   		 String taskID=data.getReqid();
	   		   		 TaskBean task= TASK.getReq(taskID);
	   		   		 task.ResetReleaseFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
	   		   		 //视图的发版本状态
		   		   	 String viewID=task.getViewID();
					 if(!StringUtil.isNullOrEmpty(viewID)){
					 	ViewBean view=VIEW.getViewById(viewID);
	   		   			 view.reSetUptFlag(ViewBean.ReleaseStatus.Apply.ordinal()+"");
	   		   		 }
	   		   		 String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"答复了测试问题【"+data.getId()+"】。请及时处理。";
	   				 IMessage message=new IMessage(data.getCrtUser(),msg);
	   				 message.addMsg();
	   				btnReply.setEnabled(false);
	   				btnReturn.setEnabled(false);
	   				//开发者任务视图需要同步刷新
	   				RequirementView.getInstance(null).refreshTree();
	   			 }
	   		}
	 }
	 
	 //问题退回(不答复)，(如果只有当前一个问题)对应的视图发版状态要修改已组织发版
	 public class ReturnQuestAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){ 
	   		 String ddesc=textDDesc.getText();
   			 if(StringUtil.isNullOrEmpty(ddesc,1000)){
   			   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
			   box.setText(Constants.getStringVaule("messagebox_alert"));
			   box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
			   box.open();
   			 }else{
   				 data.setMdesc(ddesc);
   		   		 data.setQstatus("3");
   		   		 data.sumbit();
	   		   	 //开发任务的状态
   		   		 String taskID=data.getReqid();
   		   		 List<QUESTIONSBean> questions=QUESTIONS.getaAllOtherQuestion(taskID, data.getId());
	   		   	 if(questions==null||questions.size()<=0){	 
		   		   	TaskBean task= TASK.getReq(taskID);
	  		   		 task.ResetReleaseFlag(TaskBean.ReleaseStatus.Organized.ordinal()+"");
	  		   		 //视图的发版本状态
		   		   	 String viewID=task.getViewID();
					 if(!StringUtil.isNullOrEmpty(viewID)){
					 	ViewBean view=VIEW.getViewById(viewID);
	  		   			view.reSetUptFlag(ViewBean.ReleaseStatus.Organized.ordinal()+"");
	  		   		 }
	   		   	 }
   		   	      String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"答复了测试问题【"+data.getId()+"】。请及时处理。";
				  IMessage message=new IMessage(data.getCrtUser(),msg);
				  message.addMsg();
	   			btnReply.setEnabled(false);
	   			btnReturn.setEnabled(false);
   			  }
	   	}
	 }
	 
	 
}
