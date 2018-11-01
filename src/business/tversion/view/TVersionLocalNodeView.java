package business.tversion.view;

import java.util.List;
import java.util.UUID;

import model.LOCALNODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import common.core.LnxSftp;

import bean.LOCALNODEBean;


import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import resource.SecurityCenter;
import utils.LayoutUtils;
import utils.SqlServer;
import utils.StringUtil;
import views.AppView;
public class TVersionLocalNodeView {
	private static TVersionLocalNodeView unique_instance;
	public Composite parent=null;
	public Composite content=null;
	private SashForm sashForm=null;
	
	  public static TVersionLocalNodeView getInstance(Composite parent){
	    if(unique_instance==null){
	      if(parent!=null){
	        unique_instance=new TVersionLocalNodeView(parent);
	      }
	    }
	    return unique_instance;
	  }
	  
	  private TVersionLocalNodeView(Composite com){
	    this.parent=com;
	    this.createAndShow();
	  }
	  
	  private void createAndShow(){
			content=new Composite(parent,SWT.BORDER);
			content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			content.setLayout(LayoutUtils.getComGridLayout(1, 1));
			sashForm=new SashForm(content,SWT.HORIZONTAL);
			sashForm.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			sashForm.setLayout(LayoutUtils.getDeployLayout());
			
			this.createNodeTreePanel();
			this.createNodePropertyPanel();
			sashForm.setWeights(new int[]{40,60});
			content.pack();
	  }
	  
	  
	  public Composite pannel=null;
	  public Combo comboApp;
	  public Tree nodeTree;
	  public TreeItem  treeRoot;
	  private void createNodeTreePanel(){
		    pannel=new Composite(sashForm,SWT.NONE);
			pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			pannel.setLayout(LayoutUtils.getComGridLayout(3, 10));
			
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
			pannel.pack();
			
			nodeTree=new Tree(pannel,SWT.BORDER);
			nodeTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
			nodeTree.addSelectionListener(new SelectNodeAction());
			nodeTree.addMouseListener(new PopMenuAction());
		    treeRoot=new TreeItem(nodeTree,SWT.NONE);
	   	 	treeRoot.setText("根节点");
	   	 	treeRoot.setData("$Type", "Dir");
	   	 	treeRoot.setImage(Icons.getTagFloderIcon());
	   	 	this.loadNodes();
	   	 	nodeTree.pack();
	   	 	pannel.pack();
	  }
	  
	  public String app="";
	  public void loadNodes(){
			treeRoot.removeAll();
			String rawAppName=comboApp.getText();
			 app=rawAppName.substring(0, rawAppName.indexOf(" "));
			List<LOCALNODEBean>  nodes=LOCALNODE.getMyAppNodes(app, Context.session.userID);
			if(nodes!=null&&nodes.size()>0){
				for(LOCALNODEBean node:nodes){
					TreeItem newItem=new TreeItem(treeRoot,SWT.NONE);
	        		newItem.setText(node.getShowName());
	        		newItem.setData(node);
	        		newItem.setData("$Type", "Node");
	        		newItem.setImage(Icons.getMidNodeIcon());
				}
			}
			treeRoot.setExpanded(true);
	  }
	  
	  public Composite rightComposite=null;
	  private CTabFolder tabFloder=null;
	  private void createNodePropertyPanel(){
		    rightComposite=new Composite(sashForm,SWT.NONE);
			rightComposite.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			rightComposite.setLayout(LayoutUtils.getComGridLayout(1, 1));
			
			tabFloder=new CTabFolder(rightComposite,SWT.TOP|SWT.BORDER);
			tabFloder.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			tabFloder.setMaximizeVisible(true);
			tabFloder.setMinimizeVisible(true);  
			tabFloder.setTabHeight(20);
			tabFloder.setSimple(false);
			this.createBaiscTab();
			this.createNormalTab();
			this.createDbTab();
		    this.createSFtpTab();
			rightComposite.pack();
	  }
	  
	 private Text textIp,textName=null;
	 private Combo comboSeq,comboOS=null;
	 private void createBaiscTab(){
		  CTabItem itemBasic=new CTabItem(tabFloder,SWT.NONE);
		   itemBasic.setText(Constants.getStringVaule("tabItem_basic"));
		   Composite basicPannel=new Composite(tabFloder,SWT.NONE);
		   basicPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		   basicPannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		 //节点ip地址
		    Label labIp=new Label(basicPannel,SWT.NONE);
			labIp.setText(Constants.getStringVaule("label_ip")+"(*)");
			labIp.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			textIp=new Text(basicPannel,SWT.BORDER);
			textIp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textIp.setToolTipText(Constants.getStringVaule("text_tips_ip"));	
			//节点标识名称
			Label labName=new Label(basicPannel,SWT.NONE);
			labName.setText(Constants.getStringVaule("label_node")+"(*)");
			labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		    textName=new Text(basicPannel,SWT.BORDER);
			textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textName.setToolTipText(Constants.getStringVaule("text_tips_nodename"));	
			//操作系统
			Label labOS=new Label(basicPannel,SWT.NONE);
			labOS.setText(Constants.getStringVaule("label_os")+"(*)");
			labOS.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			comboOS=new Combo(basicPannel,SWT.DROP_DOWN);
			comboOS.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			comboOS.setItems(new String[]{"Windows","Linux"});
			comboOS.select(0);
			comboOS.setToolTipText(Constants.getStringVaule("alert_agentIntall"));
			
			Button btnCancel=new Button(basicPannel,SWT.PUSH);
			btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
			btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
			btnCancel.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					textIp.setText("");
					textName.setText("");
					comboOS.select(0);
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}
			});
			Button btnOK=new Button(basicPannel,SWT.PUSH);
			btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
			btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
			btnOK.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					 String ip=textIp.getText();
					 String name=textName.getText();
					 if(StringUtil.isNullOrEmpty(ip)||
						StringUtil.isNullOrEmpty(name)||!StringUtil.isIp(ip)){
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
							box.open();	
					 }else{
						/*    if(LOCALNODE.nodeExists(Context.session.userID, ip)){
						    	MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(ip+"已存在，无法新增!");
								box.open();	
						    }else{*/
						    	 String id="Node"+UUID.randomUUID().toString().replace("-", ""); 
						    	 LOCALNODEBean bean=new LOCALNODEBean();
						    	 bean.setUserID(Context.session.userID);
						    	String rawAppName=comboApp.getText();
								 app=rawAppName.substring(0, rawAppName.indexOf(" "));
						    	 bean.setApp(app);
						    	 bean.setIp(ip);
						    	 bean.setId(id);
						    	 bean.setName(name);
						    	 String os=comboOS.getSelectionIndex()+"";
						    	 bean.setOs(os);
						    	 LOCALNODE.add(bean);
						    	 currTreeItem.setText(bean.getShowName());
						    	 currTreeItem.setData(bean);
						    	 
						    	 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								 box.setText(Constants.getStringVaule("messagebox_alert"));
								 box.setMessage(Constants.getStringVaule("alert_successoperate"));
								 box.open();	
						   // }
					 }
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
			});
			basicPannel.pack();
			itemBasic.setControl(basicPannel);
			tabFloder.setSelection(itemBasic);
	  }

	 private Text textDir1,textDir2,textDir3,textDir4,textDir5,textStart1,textStop1=null;
	 public Button btnDirBackup,btnServiceSS=null;
	 private void createNormalTab(){
		 CTabItem itemDirectory=new CTabItem(tabFloder,SWT.NONE);
		 itemDirectory.setText(Constants.getStringVaule("tabItem_directory"));
		 Composite pannel=new Composite(tabFloder,SWT.NONE);
		 pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 pannel.setLayout(LayoutUtils.getComGridLayout(5, 0));
		 //固定标签
		    Label labDirName1=new Label(pannel,SWT.NONE);
		    labDirName1.setText(Constants.getStringVaule("label_dir_value")+"(*)");
		    labDirName1.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
			textDir1=new Text(pannel,SWT.BORDER);
			textDir1.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
			
			Label labDirName2=new Label(pannel,SWT.NONE);
		    labDirName2.setText(Constants.getStringVaule("label_dir_value")+"(*)");
		    labDirName2.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
		    textDir2=new Text(pannel,SWT.BORDER);
		    textDir2.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
			
		    Label labDirName3=new Label(pannel,SWT.NONE);
		    labDirName3.setText(Constants.getStringVaule("label_dir_value")+"(*)");
		    labDirName3.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
		    textDir3=new Text(pannel,SWT.BORDER);
		    textDir3.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
		    
		    Label labDirName4=new Label(pannel,SWT.NONE);
		    labDirName4.setText(Constants.getStringVaule("label_dir_value")+"(*)");
		    labDirName4.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
		    textDir4=new Text(pannel,SWT.BORDER);
		    textDir4.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
			
		    Label labDirName5=new Label(pannel,SWT.NONE);
		    labDirName5.setText(Constants.getStringVaule("label_dir_value")+"(*)");
		    labDirName5.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
		    textDir5=new Text(pannel,SWT.BORDER);
		    textDir5.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
		    
		    Label labDirBackup=new Label(pannel,SWT.NONE);
		    labDirBackup.setText(Constants.getStringVaule("label_dir_autobackup"));
		    labDirBackup.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
		    Group group1=new Group(pannel,SWT.SHADOW_ETCHED_OUT);
		    group1.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
		    group1.setLayout(new FillLayout(SWT.HORIZONTAL));
		    btnDirBackup=new Button(group1,SWT.RADIO|SWT.FLAT);
		    btnDirBackup.setText("启用");
		    btnDirBackup.setSelection(true);
		    Button  btnDirBackupNot=new Button(group1,SWT.RADIO|SWT.FLAT);
		    btnDirBackupNot.setText("禁用");
		    group1.pack();
		    
		    Label labSvcStart=new Label(pannel,SWT.NONE);
			labSvcStart.setText(Constants.getStringVaule("label_svc_start"));
			labSvcStart.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
			textStart1=new Text(pannel,SWT.BORDER);
			textStart1.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
			
			Label labSvcStop=new Label(pannel,SWT.NONE);
			labSvcStop.setText(Constants.getStringVaule("label_svc_stop"));
			labSvcStop.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
			textStop1=new Text(pannel,SWT.BORDER);
			textStop1.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
			
			Label labServceSS=new Label(pannel,SWT.NONE);
			labServceSS.setText(Constants.getStringVaule("label_dir_autoservice"));
			labServceSS.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
			Group group2=new Group(pannel,SWT.SHADOW_ETCHED_OUT);
			group2.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
			group2.setLayout(new FillLayout(SWT.HORIZONTAL));
			btnServiceSS=new Button(group2,SWT.RADIO|SWT.FLAT);
			btnServiceSS.setText("启用");
			btnServiceSS.setSelection(true);
		    Button  btnServiceSSNot=new Button(group2,SWT.RADIO|SWT.FLAT);
		    btnServiceSSNot.setText("禁用");
		    group2.pack();
			
			Button btnCancel=new Button(pannel,SWT.PUSH);
			btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
			btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
			btnCancel.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					textDir1.setText("");
					textDir2.setText("");
					textDir3.setText("");
					textDir4.setText("");
					textDir5.setText("");
					textDir1.setText("");
					btnDirBackup.setSelection(true);
					textStart1.setText("");
					textStop1.setText("");
					btnServiceSS.setSelection(true);
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}
			});
			Button btnOK=new Button(pannel,SWT.PUSH);
			btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
			btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
			btnOK.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					 String dir1=textDir1.getText();
					 String dir2=textDir2.getText();
					 String dir3=textDir3.getText();
					 String dir4=textDir4.getText();
					 String dir5=textDir5.getText();
					 String autoBackup="0";
					 if(btnDirBackup.getSelection())
						 autoBackup="1";
					 String start=textStart1.getText();
					 String stop=textStop1.getText();
					 String autostart="0";
					 if(btnServiceSS.getSelection())
						 autostart="1";
					 if(StringUtil.isNullOrEmpty(start)&&StringUtil.isNullOrEmpty(stop))
						 autostart="0";
					 if(StringUtil.isNullOrEmpty(dir1)&&
						 StringUtil.isNullOrEmpty(dir2)&&
						 StringUtil.isNullOrEmpty(dir3)&&
						 StringUtil.isNullOrEmpty(dir4)&&
						 StringUtil.isNullOrEmpty(dir5)){
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
							box.open();	
					 }else{
						 LOCALNODEBean bean=(LOCALNODEBean)currTreeItem.getData();
						 bean.setDir1(dir1);
						 bean.setDir2(dir2);
						 bean.setDir3(dir3);
						 bean.setDir4(dir4);
						 bean.setDir5(dir5);
						 bean.setAutoBackup(autoBackup);
						 bean.setStart(start);
						 bean.setStop(stop);
						 bean.setAutoStart(autostart);
						 LOCALNODE.setDir(bean);
						 currTreeItem.setData(bean);
						 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						 box.setText(Constants.getStringVaule("messagebox_alert"));
						 box.setMessage(Constants.getStringVaule("alert_successoperate"));
						 box.open();	
					 }
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}
			});
			
		    pannel.pack();
			itemDirectory.setControl(pannel);
	 }
	 
	 private Text textDbName,textDbuser,textPasswd,textPasswdDouble,textPort=null;
	 private Combo comboDbType=null;
	 public Button btnDbBackup=null;
	 private void createDbTab(){
		 CTabItem itemDatabase=new CTabItem(tabFloder,SWT.NONE);
		 itemDatabase.setText(Constants.getStringVaule("tabItem_database"));
		 
		 Composite dbPannel=new Composite(tabFloder,SWT.NONE);
		 dbPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 dbPannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		 Label labDbtype=new Label(dbPannel,SWT.NONE);
		 	labDbtype.setText(Constants.getStringVaule("label_dbtype")+"(*)");
		 	labDbtype.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
			comboDbType=new Combo(dbPannel,SWT.DROP_DOWN);
			comboDbType.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			comboDbType.setItems(new String[]{"SqlServer","Oracle"});
			comboDbType.select(0);
			
		    Label labDbuser=new Label(dbPannel,SWT.NONE);
			labDbuser.setText(Constants.getStringVaule("label_dbuser")+"(*)");
			labDbuser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
			textDbuser=new Text(dbPannel,SWT.BORDER);
			textDbuser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textDbuser.setToolTipText(Constants.getStringVaule("text_tips_dbuser"));
			
			
			Label labDbpasswd=new Label(dbPannel,SWT.NONE);
			labDbpasswd.setText(Constants.getStringVaule("label_dbpasswd")+"(*)");
			labDbpasswd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
			textPasswd=new Text(dbPannel,SWT.BORDER|SWT.PASSWORD);
			textPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1, 0, 0));
			textPasswd.setToolTipText(Constants.getStringVaule("text_tips_dbpasswd"));
		    Button btnShow=new Button(dbPannel,SWT.CHECK);
		    btnShow.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		    btnShow.setText(Constants.getStringVaule("btn_showpasswd"));
		    btnShow.addSelectionListener(new ShowDbPasswodAction());
		    
		    Label labDouble=new Label(dbPannel,SWT.NONE);
			labDouble.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
			labDouble.setVisible(false);
			textPasswdDouble=new Text(dbPannel,SWT.BORDER);
			textPasswdDouble.setToolTipText(Constants.getStringVaule("text_tips_dbpasswd"));
			textPasswdDouble.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
			textPasswdDouble.setVisible(false);
			
		    Label labDbName=new Label(dbPannel,SWT.NONE);
			labDbName.setText(Constants.getStringVaule("label_dbname")+"(*)");
			labDbName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			textDbName=new Text(dbPannel,SWT.BORDER);
			textDbName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textDbName.setToolTipText(Constants.getStringVaule("text_tips_dbname"));
			Label labDbPort=new Label(dbPannel,SWT.NONE);
			labDbPort.setText(Constants.getStringVaule("label_dbport")+"(*)");
			labDbPort.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			textPort=new Text(dbPannel,SWT.BORDER);
			textPort.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textPort.setToolTipText(Constants.getStringVaule("text_tips_dbport"));
			textPort.setText("1433");
			
			    Label labDbBackup=new Label(dbPannel,SWT.NONE);
			    labDbBackup.setText(Constants.getStringVaule("label_db_autobackup"));
			    labDbBackup.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			    Group group1=new Group(dbPannel,SWT.SHADOW_ETCHED_OUT);
			    group1.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
			    group1.setLayout(new FillLayout(SWT.HORIZONTAL));
			    btnDbBackup=new Button(group1,SWT.RADIO|SWT.FLAT);
			    btnDbBackup.setText("启用");
			    btnDbBackup.setSelection(true);
			    Button  btnDbBackupNot=new Button(group1,SWT.RADIO|SWT.FLAT);
			    btnDbBackupNot.setText("禁用");
			    group1.pack();
			    
			Button btnCancel=new Button(dbPannel,SWT.PUSH);
			btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
			btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
			btnCancel.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					textDbuser.setText("");
					textPasswd.setText("");
					textDbName.setText("");
					textPort.setText("");
					comboDbType.setText("");
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					 
				}
			});
			
			Button btnOK=new Button(dbPannel,SWT.PUSH);
			btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
			btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
			btnOK.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					String dbType=comboDbType.getSelectionIndex()+"";
					String user=textDbuser.getText();
					String passwd=textPasswd.getText();
					String dbname=textDbName.getText();
					String dbport=textPort.getText();
					String autoBack="0";
					if(btnDbBackup.getSelection()){
						autoBack="1";
					}
					  if(StringUtil.isNullOrEmpty(user)&&
								 StringUtil.isNullOrEmpty(passwd)&&
								 StringUtil.isNullOrEmpty(dbname)&&
								 StringUtil.isNullOrEmpty(dbport)){
									MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
									box.open();	
					  }else{
						  LOCALNODEBean bean=(LOCALNODEBean)currTreeItem.getData();
						  passwd=SecurityCenter.getInstance().encrypt(passwd, Context.EncryptKey);
						  String nodeServer=bean.getIp();
						  boolean testResult=SqlServer.getInstance().testConnection(dbType, nodeServer, dbport, user, passwd, dbname);
						  if(testResult){
							  bean.setDbUser(user);
							  bean.setDbPasswd(passwd);
							  bean.setDbName(dbname);
							  bean.setDbPort(dbport);
							  bean.setDbType(dbType);
							  bean.setDbAutoBackup(autoBack);
							  LOCALNODE.setDb(bean);
							  currTreeItem.setData(bean);
								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(Constants.getStringVaule("alert_successoperate"));
								box.open();	
						  }else{
							  MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage("测试链接失败，请确认数据库属性配置正确！");
								box.open();	
						  }
					  }
				}
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}
			});
			
		 dbPannel.pack();
		 itemDatabase.setControl(dbPannel);
	 }
	  
	 private Text texSFtpUser,texSFtpPasswd,texSFtpPasswdDouble,texSFtpDir,sftpPort=null;
	 private void createSFtpTab(){
		 CTabItem itemFTP=new CTabItem(tabFloder,SWT.NONE);
		 itemFTP.setText(Constants.getStringVaule("tabItem_sftp"));
		 Composite ftpPannel=new Composite(tabFloder,SWT.NONE);
		 ftpPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 ftpPannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		 
		 Label labFtpUser=new Label(ftpPannel,SWT.NONE);
		 labFtpUser.setText(Constants.getStringVaule("label_ftpuser")+"(*)");
		 labFtpUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		 texSFtpUser=new Text(ftpPannel,SWT.BORDER);
		 texSFtpUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		 texSFtpUser.setToolTipText(Constants.getStringVaule("text_tips_ftpuser"));
		
		 
		Label labFtpPasswd=new Label(ftpPannel,SWT.NONE);
		labFtpPasswd.setText(Constants.getStringVaule("label_ftppasswd")+"(*)");
		labFtpPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		texSFtpPasswd=new Text(ftpPannel,SWT.BORDER|SWT.PASSWORD);
		texSFtpPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1, 0, 0));
		texSFtpPasswd.setToolTipText(Constants.getStringVaule("text_tips_ftppasswd"));
		Button btnShow=new Button(ftpPannel,SWT.CHECK);
	    btnShow.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
	    btnShow.setText(Constants.getStringVaule("btn_showpasswd"));
	    btnShow.addSelectionListener(new ShowSFtpPasswodAction());
		    
	    Label labDouble=new Label(ftpPannel,SWT.NONE);
		labDouble.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		labDouble.setVisible(false);
		texSFtpPasswdDouble=new Text(ftpPannel,SWT.BORDER);
		texSFtpPasswdDouble.setToolTipText(Constants.getStringVaule("text_tips_dbpasswd"));
		texSFtpPasswdDouble.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		texSFtpPasswdDouble.setVisible(false);
		
		
	 
		Label labFtpPort=new Label(ftpPannel,SWT.NONE);
		labFtpPort.setText(Constants.getStringVaule("label_ftpport")+"(*)");
		labFtpPort.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		sftpPort=new Text(ftpPannel,SWT.BORDER);
		sftpPort.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		sftpPort.setToolTipText(Constants.getStringVaule("text_tips_ftpdir"));	
		sftpPort.setText("22");
		
		Label labFtpDir=new Label(ftpPannel,SWT.NONE);
		labFtpDir.setText(Constants.getStringVaule("label_ftpdir")+"");
		labFtpDir.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		texSFtpDir=new Text(ftpPannel,SWT.BORDER);
		texSFtpDir.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		texSFtpDir.setToolTipText(Constants.getStringVaule("text_tips_ftpdir"));
		
		Button btnCancel=new Button(ftpPannel,SWT.PUSH);
		btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
		btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
		btnCancel.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				texSFtpUser.setText("");
				texSFtpPasswd.setText("");
				texSFtpPasswdDouble.setText("");
				texSFtpDir.setText("");
				sftpPort.setText("");
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
		Button btnOK=new Button(ftpPannel,SWT.PUSH);
		btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 3, 1, 0, 0));
		btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
		btnOK.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				String user=texSFtpUser.getText();
				String passwd=texSFtpPasswd.getText();
				String dir=texSFtpDir.getText();
				String port=sftpPort.getText();
				  if(StringUtil.isNullOrEmpty(user)&&
							 StringUtil.isNullOrEmpty(passwd)&&
							 StringUtil.isNullOrEmpty(dir)&&
							 StringUtil.isNullOrEmpty(port)){
								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
								box.open();	
				  }else{
					  int portNo=22;
						if(!StringUtil.isNullOrEmpty(port)){
							try{
								portNo=Integer.parseInt(port);
							}catch(Exception e){
								
							}
						}
						 LOCALNODEBean bean=(LOCALNODEBean)currTreeItem.getData();
						 String ip=bean.getIp();
						 boolean testResult=LnxSftp.testSsh(ip, user, passwd, portNo, dir);
						 if(testResult){
							 passwd=SecurityCenter.getInstance().encrypt(passwd, Context.EncryptKey);
							 bean.setSftpUser(user);
							 bean.setSftpPasswd(passwd);
							 bean.setSftpDir(dir);
							 bean.setSftpPort(portNo+"");
							  LOCALNODE.setSftp(bean);
							  currTreeItem.setData(bean);
							  MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(Constants.getStringVaule("alert_successoperate"));
								box.open();	
						 }else{
							 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage("测试链接失败，请确认ssh属性配置正确！");
								box.open();	
						 }
				  }
				
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
		ftpPannel.pack(); 
		itemFTP.setControl(ftpPannel);
	}
	 
	 public class LoadAppNodes extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			loadNodes();
	 		}
	  }
	  
	  public class SelectNodeAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			clearSets();
	 			TreeItem item=(TreeItem)e.item;
	 			currTreeItem=item;
	 			String type=(String)item.getData("$Type");
				 if("Node".equals(type)){
					 currTreeItem=item;
					 LOCALNODEBean bean=(LOCALNODEBean)item.getData();
					 if(bean!=null){
					 textIp.setText(bean.getIp());
					 textName.setText(bean.getName());
					 comboOS.select(Integer.parseInt(bean.getOs()));
					 ////////////////////////////////
					 if(!StringUtil.isNullOrEmpty(bean.getDir1()))
						 textDir1.setText(bean.getDir1());
					 if(!StringUtil.isNullOrEmpty(bean.getDir2()))
						 textDir2.setText(bean.getDir2());
					 if(!StringUtil.isNullOrEmpty(bean.getDir3()))
						 textDir3.setText(bean.getDir3());
					 if(!StringUtil.isNullOrEmpty(bean.getDir4()))
						 textDir4.setText(bean.getDir4());
					 if(!StringUtil.isNullOrEmpty(bean.getDir5()))
						 textDir5.setText(bean.getDir5());
					 if(!StringUtil.isNullOrEmpty(bean.getStart()))
						 textStart1.setText(bean.getStart());
					 if(!StringUtil.isNullOrEmpty(bean.getStop()))
						 textStop1.setText(bean.getStop());
						if("1".equals(bean.getAutoBackup())){
							btnDirBackup.setSelection(true);
						}else{
							btnDirBackup.setSelection(false);
						}
						if("1".equals(bean.getAutoStart())){
							btnServiceSS.setSelection(true);
						}else{
							btnServiceSS.setSelection(false);
						}
					 /////////////////////////
					if(!StringUtil.isNullOrEmpty(bean.getDbUser()))
						textDbuser.setText(bean.getDbUser());
					 String dbpasswd=bean.getDbPasswd();
						if(!StringUtil.isNullOrEmpty(dbpasswd)){
							dbpasswd=SecurityCenter.getInstance().decrypt(dbpasswd, Context.EncryptKey);
							textPasswd.setText(dbpasswd);
						}else{
							textPasswd.setText("");
						}
						if(!StringUtil.isNullOrEmpty(bean.getDbName()))
							textDbName.setText(bean.getDbName());
						if(!StringUtil.isNullOrEmpty(bean.getDbPort()))
							textPort.setText(bean.getDbPort());
						if(!StringUtil.isNullOrEmpty(bean.getDbType()))
							comboDbType.select(Integer.parseInt(bean.getDbType()));
						if("1".equals(bean.getDbAutoBackup())){
							btnDbBackup.setSelection(true);
						}else{
							btnDbBackup.setSelection(false);
						}
					 /////////////////////
					if(!StringUtil.isNullOrEmpty(bean.getSftpUser()))	
						texSFtpUser.setText(bean.getSftpUser());
					 String passwd=bean.getSftpPasswd();
						if(!StringUtil.isNullOrEmpty(passwd)){
								passwd=SecurityCenter.getInstance().decrypt(passwd, Context.EncryptKey);
								texSFtpPasswd.setText(passwd);
						}else{
							texSFtpPasswd.setText("");
						}
						if(!StringUtil.isNullOrEmpty(bean.getSftpDir()))	
							texSFtpDir.setText(bean.getSftpDir());
						if(!StringUtil.isNullOrEmpty(bean.getSftpPort()))	
							sftpPort.setText(bean.getSftpPort());
					 }else{
						clearSets();
					 }
				 }
	 		}
	  }
	  
	  public void clearSets(){
		  	textIp.setText("");
			textName.setText("");
			comboOS.select(0);
				
			 textDir1.setText("");
			textDir2.setText("");
			textDir3.setText("");
			textDir4.setText("");
			textDir5.setText("");
			textDir1.setText("");
			btnDirBackup.setSelection(true);
			textStart1.setText("");
			textStop1.setText("");
			btnServiceSS.setSelection(true);
				
			textDbuser.setText("");
			textPasswd.setText("");
			textDbName.setText("");
			textPort.setText("");
			comboDbType.setText("");
			 
			 texSFtpUser.setText("");
			texSFtpPasswd.setText("");
			texSFtpPasswdDouble.setText("");
			texSFtpDir.setText("");
			sftpPort.setText("");
	  }
	  
	  public TreeItem currTreeItem=null;
	  public class PopMenuAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){
	 			TreeItem currentItem=nodeTree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null&&e.button==3){
	 				String type=(String)currentItem.getData("$Type");
	 				Menu actionMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
	 				if("Dir".equals(type)){
			 		      MenuItem itemAdd=new MenuItem(actionMenu,SWT.PUSH);
			 		      itemAdd.setText("新增节点");
			 		     itemAdd.setData("TreeItem", currentItem);
			 		     itemAdd.setImage(Icons.getAddIcon());
		 		    	 itemAdd.addSelectionListener(new SelectionAdapter(){ 	
		 	        		 public void widgetSelected(SelectionEvent e){
		 	        			MenuItem item=(MenuItem)e.getSource();
		 	        			TreeItem parentItem=(TreeItem)item.getData("TreeItem");
		 	        			TreeItem newItem=new TreeItem(parentItem,SWT.NONE);
		 	        			newItem.setText("新节点");
		 	        			newItem.setData("$Type", "Node");
		 	        			newItem.setImage(Icons.getMidNodeIcon());
		 	        			currTreeItem=newItem;
		 	        			parentItem.setExpanded(true);
		 	        		 }
		 		    	 });
	 				}else{
	 					 MenuItem itemDelete=new MenuItem(actionMenu,SWT.PUSH);
		 		    	 itemDelete.setText("删除节点");
		 		    	 itemDelete.setData("TreeItem", currentItem);
		 		    	 itemDelete.setImage(Icons.getDeleteIcon());
		 		    	 itemDelete.addSelectionListener(new SelectionAdapter(){ 	
		 	        		 public void widgetSelected(SelectionEvent e){
		 	        			MenuItem item=(MenuItem)e.getSource();
		 	        			TreeItem treeItem=(TreeItem)item.getData("TreeItem");
		 	        			 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
								 box.setText(Constants.getStringVaule("messagebox_alert"));
								 box.setMessage("确认要删除机器节点吗？");
								 int chooice=box.open();	
								 if(chooice==SWT.YES){
			 	        			//删除数据
			 	        			LOCALNODEBean data=(LOCALNODEBean)treeItem.getData();
			 	        			LOCALNODE.delete(data);
			 	        			//刷新节点树
			 	        			loadNodes();
								 }
		 	        		 }
		 		    	 });
	 				}
	 				nodeTree.setMenu(actionMenu);
	 			}else{
	 				nodeTree.setMenu(null);
	 			}
	 		}
	  }

	  public class ShowDbPasswodAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				Button btn=(Button)e.getSource();
				boolean checked=btn.getSelection();
				if(checked){
					String text=textPasswd.getText();
					if(!StringUtil.isNullOrEmpty(text)){
						textPasswdDouble.setText(text);
						textPasswdDouble.setVisible(true);
					}
				}else{
					textPasswdDouble.setVisible(false);
				}
			}
	  }
	  
	  public class ShowSFtpPasswodAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				Button btn=(Button)e.getSource();
				boolean checked=btn.getSelection();
				if(checked){
					String text=texSFtpPasswd.getText();
					if(!StringUtil.isNullOrEmpty(text)){
						texSFtpPasswdDouble.setText(text);
						texSFtpPasswdDouble.setVisible(true);
					}
				}else{
					texSFtpPasswdDouble.setVisible(false);
				}
			}
	  }
}
