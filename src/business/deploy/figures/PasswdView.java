package business.deploy.figures;

import java.util.ArrayList;
import java.util.List;

import model.COMPONENT;
import model.NODE;
import model.SYSTEM;
import model.USERNODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import bean.COMPONENTBean;
import bean.Cluster;
import bean.NODEBean;
import bean.SYSTEMBean;


import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import resource.SecurityCenter;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

public class PasswdView {

	private static PasswdView unique_instance;
	public static PasswdView getInstance(Composite parent){
		if(unique_instance==null){
			if(parent!=null){
				unique_instance=new PasswdView(parent);
			}
		}
		return unique_instance;
	}
	
	private PasswdView(Composite com){
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
		this.createPasswdPanel();
		sashForm.setWeights(new int[]{50,50});
		content.pack();
	
	}
	
	public Combo comboApp;
	public Tree tagTree;
	public TreeItem  treeRoot;
	public String appName;
	private void createNodeTreePanel(){
		
		
		self=new ScrolledComposite(sashForm,SWT.V_SCROLL|SWT.H_SCROLL|SWT.BORDER);
		self.addControlListener(new ScrollCompositeResizeAction());
	    self.setAlwaysShowScrollBars(true);
		self.setExpandHorizontal(true);
		self.setExpandVertical(true);
		
	    pannel=new Composite(self,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, false, false, 1, 1, 300, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(3, 10));
		
		Label labName=new Label(pannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_appname"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		comboApp=new Combo(pannel,SWT.DROP_DOWN);
		comboApp.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1, 0, 0));
		comboApp.addSelectionListener(new LoadAppTags());
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
		
		tagTree=new Tree(pannel,SWT.BORDER|SWT.MULTI|SWT.CHECK);
		tagTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
		tagTree.addSelectionListener(new SelectNodeAction());
	    treeRoot=new TreeItem(tagTree,SWT.NONE);
   	 	treeRoot.setText("根节点");
   	 	treeRoot.setImage(Icons.getTagFloderIcon());
   	 	tagTree.pack();
   	    loadDirTree();
   	   
   	   pannel.pack();
	   	self.layout(true);
		self.setContent(pannel);
		self.pack();
	}
	
	private void createPasswdPanel(){
		Composite rightComposite=new Composite(sashForm,SWT.NONE);
		rightComposite.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 200, 0));
		rightComposite.setLayout(LayoutUtils.getComGridLayout(1, 1));
		

		Group groupFtp=new Group(rightComposite,SWT.NONE);
		groupFtp.setText(Constants.getStringVaule("group_ftp"));
		groupFtp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		groupFtp.setLayout(LayoutUtils.getComGridLayout(3, 1));
		Label labFtpUser=new Label(groupFtp,SWT.NONE);
		labFtpUser.setText(Constants.getStringVaule("label_ftpuser")+"(*)");
		labFtpUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		textFtpUser=new Text(groupFtp,SWT.BORDER);
		textFtpUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		textFtpUser.setToolTipText(Constants.getStringVaule("text_tips_ftpuser"));
		
		Label labFtpPasswd=new Label(groupFtp,SWT.NONE);
		labFtpPasswd.setText(Constants.getStringVaule("label_ftppasswd")+"(*)");
		labFtpPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		texFtpPasswd=new Text(groupFtp,SWT.BORDER|SWT.PASSWORD);
		texFtpPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		texFtpPasswd.setToolTipText(Constants.getStringVaule("text_tips_ftppasswd"));
		
		Label labFtpDir=new Label(groupFtp,SWT.NONE);
		labFtpDir.setText(Constants.getStringVaule("label_ftpdir"));
		labFtpDir.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		textFtpDir=new Text(groupFtp,SWT.BORDER);
		textFtpDir.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		textFtpDir.setToolTipText(Constants.getStringVaule("text_tips_ftpdir"));
		
		Label labFtpPort=new Label(groupFtp,SWT.NONE);
		labFtpPort.setText(Constants.getStringVaule("label_ftpport"));
		labFtpPort.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		ftpPort=new Text(groupFtp,SWT.BORDER);
		ftpPort.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		ftpPort.setToolTipText(Constants.getStringVaule("text_tips_ftpdir"));
		
		Button btnApply=new Button(groupFtp,SWT.PUSH);
		btnApply.setText("   "+Constants.getStringVaule("btn_apply")+"   ");
		btnApply.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1, 0, 0));
		btnApply.addSelectionListener(new SetSFtpPasswodAction());
		groupFtp.pack();
		
		Group groupDb=new Group(rightComposite,SWT.NONE);
		groupDb.setText(Constants.getStringVaule("group_db"));
		groupDb.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		groupDb.setLayout(LayoutUtils.getComGridLayout(3, 1));
		
		Label labDbtype=new Label(groupDb,SWT.NONE);
	 	labDbtype.setText(Constants.getStringVaule("label_dbtype")+"(*)");
	 	labDbtype.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		comboDbType=new Combo(groupDb,SWT.DROP_DOWN);
		comboDbType.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		comboDbType.setItems(new String[]{"SqlServer","Oracle"});
		
		Label labDbuser=new Label(groupDb,SWT.NONE);
		labDbuser.setText(Constants.getStringVaule("label_dbuser")+"(*)");
		labDbuser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		textDbuser=new Text(groupDb,SWT.BORDER);
		textDbuser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		textDbuser.setToolTipText(Constants.getStringVaule("text_tips_dbuser"));
		
		
		Label labDbpasswd=new Label(groupDb,SWT.PASSWORD);
		labDbpasswd.setText(Constants.getStringVaule("label_dbpasswd")+"(*)");
		labDbpasswd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		textPasswd=new Text(groupDb,SWT.BORDER|SWT.PASSWORD);
		textPasswd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		textPasswd.setToolTipText(Constants.getStringVaule("text_tips_dbpasswd"));
		
		Label labDbport=new Label(groupDb,SWT.PASSWORD);
		labDbport.setText(Constants.getStringVaule("label_dbport"));
		labDbport.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		textPort=new Text(groupDb,SWT.BORDER);
		textPort.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		textPort.setToolTipText(Constants.getStringVaule("text_tips_dbport"));
		
		Label labDbName=new Label(groupDb,SWT.NONE);
		labDbName.setText(Constants.getStringVaule("label_dbname")+"(*)");
		labDbName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textDbName=new Text(groupDb,SWT.BORDER);
		textDbName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		textDbName.setToolTipText(Constants.getStringVaule("text_tips_dbname"));
		
		 Label labBackDbName=new Label(groupDb,SWT.NONE);
		 labBackDbName.setText(Constants.getStringVaule("label_backdbname")+"(*)");
		 labBackDbName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		 textBackDbName=new Text(groupDb,SWT.BORDER);
		 textBackDbName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		 textBackDbName.setToolTipText(Constants.getStringVaule("text_tips_backdbname"));
		
		
		
		Button btnApplyDb=new Button(groupDb,SWT.PUSH);
		btnApplyDb.setText("   "+Constants.getStringVaule("btn_apply")+"   ");
		btnApplyDb.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1, 0, 0));
		btnApplyDb.addSelectionListener(new SetDBPasswodAction());
		groupDb.pack();
		rightComposite.pack();
	}
	
	private void loadDirTree(){
		treeRoot.removeAll();
		String rawAppName=comboApp.getText();
		appName=rawAppName.substring(0, rawAppName.indexOf(" "));
		
		 List<SYSTEMBean> systems=SYSTEM.getSystems(appName, Context.session.currentFlag);
		for(SYSTEMBean system:systems){
			TreeItem  treeItemSystem=new TreeItem(treeRoot,SWT.NONE);
			treeItemSystem.setText(system.getName());
			treeItemSystem.setImage(Icons.getTagFloderIcon());
			treeItemSystem.setData(system);
			treeItemSystem.setData("$Type", "system");
			List<COMPONENTBean>   components=COMPONENT.getUserComponents(Context.session.userID, system.getBussID(), Context.session.currentFlag);
			for(COMPONENTBean component:components){
					TreeItem  treeItemComponent=new TreeItem(treeItemSystem,SWT.NONE);
					treeItemComponent.setText(component.getName());
					treeItemComponent.setImage(Icons.getTagFloderIcon());
					treeItemComponent.setData(component);
					treeItemComponent.setData("$Type", "component");
					List<Cluster> clusters=COMPONENT.getCuster(component.getId(), Context.session.currentFlag);
					if(clusters!=null&&clusters.size()>0){
						for(Cluster cluster:clusters){
							cluster.setComponentType(component.getAbbr());
						
							List<NODEBean>  clusterNodes=NODE.getMyClusterNodes(Context.session.userID, cluster.getComponentID(),cluster.getName(), Context.session.currentFlag);
							if(clusterNodes!=null&&clusterNodes.size()>0){
								TreeItem  treeItemCluster=new TreeItem(treeItemComponent,SWT.NONE);
								treeItemCluster.setText(cluster.getName());
								treeItemCluster.setImage(Icons.getTagFloderIcon());
								treeItemCluster.setData(cluster);
								treeItemCluster.setData("$Type", "cluster");
								for(NODEBean node:clusterNodes){
									node.setComponentType(component.getAbbr());//节点的组件类型
									TreeItem  treeItemNode=new TreeItem(treeItemCluster,SWT.NONE);
									treeItemNode.setText(node.getIp()+"("+node.getName()+")");
									treeItemNode.setImage(Icons.getMidNodeIcon());
									treeItemNode.setData(node);
									treeItemNode.setData("$Type", "node");
								}
								treeItemCluster.setExpanded(true);
							}
						}
					}
					//treeItemComponent.setExpanded(true);
				}
			}
			//treeItemSystem.setExpanded(true);
		treeRoot.setExpanded(true);
	}
		
	
	
	
	public class LoadAppTags extends SelectionAdapter{
 		public void widgetSelected(SelectionEvent e){
 			loadDirTree();
 		}
	}
	
	//选择后，自动选择下级节点
	 public class SelectNodeAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			TreeItem currentItem=(TreeItem)e.item;
	 			boolean checked=currentItem.getChecked();
	 			setChildrenCheck(currentItem,checked);
	 			currentItem.setExpanded(true);
	 		}
	 	}
	 
	 public void setChildrenCheck(TreeItem parent,boolean chekced){
		 TreeItem[] items=parent.getItems();
			if(items!=null&&items.length>0){
				boolean checked=parent.getChecked();
				for(TreeItem item:items){
 					item.setChecked(checked);
 					setChildrenCheck(item,checked);
 				}
			}
	 }

	 
	 public  List<NODEBean> getTreeNodes(){
		 TreeItem[] items=treeRoot.getItems();
		 List<NODEBean> Nodes=new ArrayList<NODEBean>();
		 if(items!=null&&items.length>0){
			 for(TreeItem item:items){
				String type=(String)item.getData("$type");
				if("node".equals(type)){
					if(item.getChecked()){
						NODEBean node=(NODEBean)item.getData();
						Nodes.add(node);
					}
				}else{
					this.getChildrenNode(Nodes, item);
				}
			 }
		 }
		 return Nodes;
	 }
	 
	 private void getChildrenNode(List<NODEBean> list,TreeItem currentItem){
		 TreeItem[] items=currentItem.getItems();
		 if(items!=null&&items.length>0){
			 for(TreeItem item:items){
					String type=(String)item.getData("$type");
					if("node".equals(type)){
						if(item.getChecked()){
							NODEBean node=(NODEBean)item.getData();
							list.add(node);
						}
					}else{
						this.getChildrenNode(list, item);
					}
			 }
		 }
	 }
	 
	 public class SetDBPasswodAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			String user=textDbuser.getText();
	 			String passwd=textPasswd.getText();
	 			String portNo=textPort.getText();
	 			String dbName=textDbName.getText();
	 			String backup=textBackDbName.getText();
	 			String dbType=comboDbType.getText();
	 			 if((NODEBean.DB.SqlServer.name()).equals(dbType)){
					 dbType=NODEBean.DB.SqlServer.ordinal()+"";
				 }
				 else if((NODEBean.DB.Oracle.name()).equals(dbType)){
					 dbType=NODEBean.DB.Oracle.ordinal()+"";
				 }else{
					 dbType=" ";
				 }
	 			try{
	 				int portno=Integer.parseInt(portNo);
	 			}
	 			catch(Exception exp){
	 				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_errorformat"));
					box.open();	
					return;
	 			}
	 			passwd=SecurityCenter.getInstance().encrypt(passwd, Context.EncryptKey);
	 			if(StringUtil.isNullOrEmpty(user)||StringUtil.isNullOrEmpty(passwd)||StringUtil.isNullOrEmpty(portNo)){
	 				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
					box.open();	
	 			}else{
	 				List<NODEBean> nodes=getTreeNodes();
	 				if(nodes!=null&&nodes.size()>0){
	 					for(NODEBean bean:nodes){
	 						bean.setDbUser(user);
	 						bean.setDbport(portNo);
	 						bean.setDbPasswd(passwd);
	 						bean.setDbname(dbName);
	 						bean.setDbType(dbType);
	 						USERNODE.updateDBInfo(Context.session.userID, bean.getId(), dbType,user, passwd, dbName, backup, portNo);
	 					}
		 					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WORKING|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(Constants.getStringVaule("alert_successoperate"));
							box.open();	
		 				}
		 				else{
		 					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(Constants.getStringVaule("alert_necessarychoose"));
							box.open();	
		 				}
		 			}
	 			}
	 }
	 
	 public class SetSFtpPasswodAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			String user=textFtpUser.getText();
	 			String passwd=texFtpPasswd.getText();
	 			String dir=textFtpDir.getText();
	 			String port=ftpPort.getText();
	 			passwd=SecurityCenter.getInstance().encrypt(passwd, Context.EncryptKey);
	 			if(StringUtil.isNullOrEmpty(user)||StringUtil.isNullOrEmpty(passwd)){
	 				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
					box.open();	
	 			}else{
	 				List<NODEBean> nodes=getTreeNodes();
	 				if(nodes!=null&&nodes.size()>0){
	 					for(NODEBean bean:nodes){
	 						bean.setSftpUser(user);
	 						bean.setSftpPasswd(passwd);
	 						bean.setSftpDir(dir);
	 						bean.setSftpPort(port);
	 						USERNODE.updateSftpInfo(Context.session.userID, bean.getId(), user, passwd, port, dir);
	 					}
	 					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WORKING|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(Constants.getStringVaule("alert_successoperate"));
						box.open();	
	 				}
	 				else{
	 					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(Constants.getStringVaule("alert_necessarychoose"));
						box.open();	
	 				}
	 			}
	 		}
	 }
	 
		public class ScrollCompositeResizeAction extends ControlAdapter{
			public void controlResized(ControlEvent e) {
				pannel.setSize(pannel.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				self.setMinSize(pannel.getSize());	
			}
		}
		
	public ScrolledComposite self=null;
	public Composite pannel=null;
	public Composite content=null;
	public Composite parent=null;
	private SashForm sashForm=null;
	private Text textFtpUser=null;
	private Text texFtpPasswd=null;
	private Text textFtpDir=null;
	private Text ftpPort=null;
	private Text textDbuser=null;
	private  Combo comboDbType=null;
	private Text textPasswd=null;
	private Text textPort=null;
	private Text textDbName;
	private Text textBackDbName;
	private Text shareDirDbuser=null;
	private Text shareDirPasswd=null;
	private Text textShareDir=null;
	private Tree nodeTree=null;
	private Tree DbNodeTree=null;
	
}
