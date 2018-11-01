package business.deploy.figures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.COMPONENT;
import model.DATAFLAG;
import model.NODE;
import model.USERNODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import common.core.LnxSftp;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import resource.SecurityCenter;
import bean.COMPONENTBean;
import bean.Cluster;
import bean.DATAFLAGBean;
import bean.NODEBean;
import bean.NODEDIRBean;
import bean.NODESERVICEBean;
import bean.SYSTEMBean;
import bean.Triple;
import business.deploy.action.NodeDragAction;

import utils.DataUtil;
import utils.LayoutUtils;
import utils.SqlServer;
import utils.StringUtil;
import views.AppView;

public class DiagramView {
	
	
	private boolean editable=true;
	public  DiagramView(SashForm com,SYSTEMBean data,boolean edit){
		content=new Composite(com,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		this.group=data;
		nodeData=new ArrayList<NODEBean>();
		this.editable=edit;
		//机房即使是部署员角色，也只能新增节点不能修改节点的ip地址
		if(!Context.NodeEditable){
			if(this.editable){
				this.editable=false;
			}
		}
		createToolsView();
		createNodeView();
		content.pack();
	}
	
	
	private void createToolsView(){
		 
		Composite toolPanel=new Composite(content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(20, 1));
		
		GridData gridData=LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, false, false, 1, 1, 28, 28);
		gridData.verticalIndent=-5;
		
		Button bthAddNodeBp=new Button(toolPanel,SWT.PUSH);
		bthAddNodeBp.setToolTipText(Constants.getStringVaule("btn_tips_addnodebp"));
		bthAddNodeBp.setImage(Icons.getKcbpNodeIcon24());
		bthAddNodeBp.setLayoutData(gridData);
    

		textProgress=new Text(toolPanel,SWT.RIGHT);
		textProgress.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
		textProgress.setVisible(false);
		progress=new ProgressBar(toolPanel,SWT.SMOOTH);// SWT.HORIZONTAL
		progress.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 15, 1, 0, 0));
		progress.setMinimum(0);
		progress.setVisible(false);
		toolPanel.pack();
	}
	
	private void createNodeView(){
		SashForm nodeCom=new SashForm(this.content,SWT.HORIZONTAL); 
		nodeCom.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		nodeCom.setLayout(LayoutUtils.getComGridLayout(2, 10));
		
		
		nodeTree=new Tree(nodeCom,SWT.BORDER|SWT.MULTI);
		nodeTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		//修改节点需要增加角色权限判断
		nodeTree.addMouseListener(new EditNodeAction());
		nodeTree.addSelectionListener(new NodePropertyAction());
		treeRoot=new TreeItem(nodeTree,SWT.NONE);
   	 	treeRoot.setText(this.group.getName());
   	 	treeRoot.setData(this.group);
   	 	treeRoot.setData("$Type", "root");
   	 	treeRoot.setData("$Status", "0");
   	 	treeRoot.setImage(Icons.getTagFloderIcon());
   		List<COMPONENTBean>   components=COMPONENT.getUserComponents( Context.session.userID, this.group.getBussID(), Context.session.currentFlag);
		for(COMPONENTBean component:components){
				TreeItem  treeItemComponent=new TreeItem(treeRoot,SWT.NONE);
				treeItemComponent.setText(component.getName());
				treeItemComponent.setImage(Icons.getTagFloderIcon());
				treeItemComponent.setData(component);
				treeItemComponent.setData("$Type", "component");
				List<Cluster> clusters=COMPONENT.getCuster(component.getId(), Context.session.currentFlag);
				if(clusters!=null&&clusters.size()>0){
					for(Cluster cluster:clusters){
						cluster.setComponentType(component.getAbbr());
						List<NODEBean>  clusterNodes=NODE.getMyClusterNodes( Context.session.userID, cluster.getComponentID(),cluster.getName(), Context.session.currentFlag);
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
								nodeData.add(node);
							}
							treeItemCluster.setExpanded(true);
						}
					}
				}
			
				treeItemComponent.setExpanded(true);
			
		}
   	 	treeRoot.setExpanded(true);
		nodeTree.pack();
		editor=new TreeEditor(nodeTree);
		editor.horizontalAlignment=SWT.LEFT;
		editor.grabHorizontal=true;
		editor.minimumWidth=30;
		
		//属性
		tabFloder=new CTabFolder(nodeCom,SWT.TOP|SWT.BORDER);
		tabFloder.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		tabFloder.setMaximizeVisible(true);
		tabFloder.setMinimizeVisible(true);  
		tabFloder.setTabHeight(20);
		tabFloder.setSimple(false);
		this.createBasicTab();
		this.createDirectoryTab();
		this.createDbTab();
		//this.createFtpTab();
		this.createSFtpTab();
		//this.createSambaTab();
		this.createServiceTab();
		tabFloder.pack();
		nodeCom.setWeights(new int[]{40,60});
	}
	
	private void refreshNodeTree(){
		treeRoot.removeAll();
		/*treeRoot=new TreeItem(nodeTree,SWT.NONE);
   	 	treeRoot.setText(this.group.getName());
   	 	treeRoot.setData(this.group);
   	 	treeRoot.setData("$Type", "root");
   	 	treeRoot.setImage(Icons.getTagFloderIcon());*/
		treeRoot.setData("$Status", "1");
		DATAFLAGBean dataflag=DATAFLAG.getByID(Constants.dataFlagCmdb);
		if(StringUtil.isNullOrEmpty(Context.session.currentFlag))
			Context.session.currentFlag=dataflag.getFlag();
		List<COMPONENTBean>   components=COMPONENT.getUserComponents( Context.session.userID, this.group.getBussID(), Context.session.currentFlag);
		for(COMPONENTBean component:components){
				TreeItem  treeItemComponent=new TreeItem(treeRoot,SWT.NONE);
				treeItemComponent.setText(component.getName());
				treeItemComponent.setImage(Icons.getTagFloderIcon());
				treeItemComponent.setData(component);
				treeItemComponent.setData("$Type", "component");
				List<Cluster> clusters=COMPONENT.getCuster(component.getId(), dataflag.getFlag());
				if(clusters!=null&&clusters.size()>0){
					for(Cluster cluster:clusters){
						cluster.setComponentType(component.getAbbr());
						TreeItem  treeItemCluster=new TreeItem(treeItemComponent,SWT.NONE);
						treeItemCluster.setText(cluster.getName());
						treeItemCluster.setImage(Icons.getTagFloderIcon());
						treeItemCluster.setData(cluster);
						treeItemCluster.setData("$Type", "cluster");
						List<NODEBean>  clusterNodes=NODE.getMyClusterNodes( Context.session.userID, cluster.getComponentID(),cluster.getName(), dataflag.getFlag());
						for(NODEBean node:clusterNodes){
							node.setComponentType(component.getAbbr());//节点的组件类型
							TreeItem  treeItemNode=new TreeItem(treeItemCluster,SWT.NONE);
							treeItemNode.setText(node.getIp()+"("+node.getName()+")");
							treeItemNode.setImage(Icons.getNodeIcon(node.getStatus()));
							treeItemNode.setData(node);
							treeItemNode.setData("$Type", "node");
							RunView guide=new RunView();
				        	guide.setData(node);
				        	treeItemNode.setData("$View", guide);
							nodeData.add(node);
						}
						treeItemCluster.setExpanded(true);
					}
				}
				treeItemComponent.setExpanded(true);
		}
   	 	treeRoot.setExpanded(true);
	}
	
	
	private Text textIp,textName,textSPort=null;
	private Combo comboSeq,comboOS=null;
	private void createBasicTab(){
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
			//节点的编号顺序
			Label labNodeSeq=new Label(basicPannel,SWT.NONE);
		    labNodeSeq.setText(Constants.getStringVaule("label_nodeseq"));
		    labNodeSeq.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		    comboSeq=new Combo(basicPannel,SWT.DROP_DOWN);
		    comboSeq.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		    comboSeq.setItems(DataUtil.getSeqArray(Context.MaxOrder));
		    comboSeq.setToolTipText(Constants.getStringVaule("text_tips_nodeseq"));
		    comboSeq.addSelectionListener(new CheckSeqAction());

		    //节点服务端口号
			Label labDesc=new Label(basicPannel,SWT.NONE);
			labDesc.setText(Constants.getStringVaule("label_node_desc"));
			labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			textSPort=new Text(basicPannel,SWT.BORDER|SWT.MULTI);
			textSPort.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textSPort.setToolTipText(Constants.getStringVaule("text_tips_nodedesc"));
			textSPort.setText(Context.ServerPort+"");
			
			Button btnCancel=new Button(basicPannel,SWT.PUSH);
			btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
			btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
			btnCancel.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					textSPort.setText("");
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}
			});
			Button btnOK=new Button(basicPannel,SWT.PUSH);
			btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
			btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
			btnOK.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					String seq=comboSeq.getText();
					String sport=textSPort.getText();
					String os=comboOS.getText();
					if(StringUtil.isNullOrEmpty(seq)){
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
						box.open();	
					}else{
						//再次检查节点序号
						 boolean ret=NODE.nodeSeqExistSystem(group.getBussID(), seq, Context.session.userID,Context.session.currentFlag);
						 if(ret){
							    MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(Constants.getStringVaule("alert_duplicateval"));
								box.open();	
								comboSeq.setText(" ");
						 }
						 else{
							 if((NODEBean.OS.Linux.name()).equals(os)){
								  os=NODEBean.OS.Linux.ordinal()+"";
							 }
							 else if((NODEBean.OS.Windows.name()).equals(os)){
								 os=NODEBean.OS.Windows.ordinal()+"";
							 }else{
								 os="0";
							 }
							  if(StringUtil.isPort(sport)){
									USERNODE.updateBaisc( Context.session.userID, currentNode.getId(), seq, os,sport);
									currentNode.setOs(os);
									currentNode.setSeq(seq);
									currentNode.setsPort(sport+"");
									MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage(Constants.getStringVaule("alert_successoperate"));
									box.open();	
							 }else{
								    MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage("端口输入"+Constants.getStringVaule("alert_errorformat"));
									box.open();	
							 }
						 }
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
	
	public class CheckSeqAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			 String seq=comboSeq.getText();
			 String gourpID=group.getBussID();
			 boolean ret=NODE.nodeSeqExistSystem(gourpID, seq, Context.session.currentFlag,Context.session.userID);
			 if(ret){
				    MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_duplicateval"));
					box.open();	
					comboSeq.setText(" ");
			 }
		}
    }
	
	public Composite directoryPannel;
	public ScrolledComposite scComosite;

	private void createDirectoryTab(){
		 CTabItem itemDirectory=new CTabItem(tabFloder,SWT.NONE);
		 itemDirectory.setText(Constants.getStringVaule("tabItem_directory"));
		 
		 Composite pannel=new Composite(tabFloder,SWT.NONE);
		 pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 pannel.setLayout(LayoutUtils.getComGridLayout(7, 0));
		 
		   //固定标签
		    Label labDirName=new Label(pannel,SWT.NONE);
			labDirName.setText(Constants.getStringVaule("label_dir_name")+"(*)");
			labDirName.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
			
			Label labDirKey=new Label(pannel,SWT.NONE);
			labDirKey.setText(Constants.getStringVaule("label_dir_value")+"(*)");
			labDirKey.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 3, 1, 0, 0));
			
			Label labDirValue=new Label(pannel,SWT.NONE);
			labDirValue.setText(Constants.getStringVaule("label_dir_filter"));
			labDirValue.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 3, 1, 0, 0));
		
			
			 scComosite=new ScrolledComposite(pannel,SWT.V_SCROLL|SWT.H_SCROLL|SWT.BORDER);
			 scComosite.setAlwaysShowScrollBars(true);
			 scComosite.setExpandHorizontal(true);
			 scComosite.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 7, 1, 0, 0));
			 scComosite.setLayout(LayoutUtils.getComGridLayout(1, 1));
			//动态目录
			 directoryPannel=new Composite(scComosite,SWT.NONE);
			 directoryPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			 directoryPannel.setLayout(LayoutUtils.getComGridLayout(7, 0));
			 for(int w=0;w<5;w++){
				   Text textName=new Text(directoryPannel,SWT.BORDER);
				   textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 1, 1, 0, 0));
				   textName.setData("$Type", "name");
				   
				   Text textPath=new Text(directoryPannel,SWT.BORDER);
				   textPath.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 3, 1, 0, 0));
				   textPath.setData("$Type", "value");
				   
				   Text textFilter=new Text(directoryPannel,SWT.BORDER);
				   textFilter.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 3, 1, 0, 0));
				   textFilter.setData("$Type", "filter");
			 }

			 directoryPannel.pack();
		
		
		 Composite actionPannel=new Composite(pannel,SWT.NONE);
		 actionPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 7, 1, 0, 0));
		 actionPannel.setLayout(LayoutUtils.getComGridLayout(8, 0));
		  
		    Button btnCancel=new Button(actionPannel,SWT.PUSH);
			btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 4, 1, 0, 0));
			btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
			btnCancel.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					Control[] controls=directoryPannel.getChildren();
					for(Control control:controls){
							Text text=(Text)control;
							if(text!=null){
								text.setText("");
							}
					}
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}
			});
			Button btnOK=new Button(actionPannel,SWT.PUSH);
			btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 4, 1, 0, 0));
			btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
			btnOK.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {

					Control[] controls=directoryPannel.getChildren();
					String dirs="";
					for(Control control:controls){
							Text text=(Text)control;
							if(text!=null){
								String type=(String)text.getData("$Type");
								String dirVal=text.getText();
								if("name".equals(type)){
									dirs=dirs+dirVal;
								}
								else{
									dirs=dirs+"|"+dirVal;
								}
								if("filter".equals(type)){
									dirs+="&";
								}
							}
					}
					dirs=StringUtil.ltrim(dirs, "|");
					dirs=StringUtil.rtrim(dirs, "&");
					String[] columns=dirs.split("&");
					String alertMsg="";
					if(columns.length>0){
						Map<String,Triple> Dir=new HashMap<String, Triple>();
						for(int w=0;w<columns.length;w++){
							String line=columns[w];
							String[] cols=line.split("\\|");
							if(cols!=null&&cols.length>0){
								String name=cols[0];
								String  value="";
								String filter="";
								if(cols.length>=2)
									value=cols[1];
								if(cols.length>=3)
									filter=cols[2];
								Triple triple=new Triple();
								triple.setName(name);
								triple.setValue(value);
								triple.setType(filter);
								if(StringUtil.isNullOrEmpty(value)){
									alertMsg+="组件:"+name+"目录为空，将忽略该设置;";
								}
								else{
									if(Dir.containsKey(name)){
										alertMsg+="组件:"+name+"已经存在，将忽略该设置;";
									}
									else{
										Dir.put(name, triple);
									}
								}
							}
						}
						if(Dir.size()>0){
							//先删除目录信息
							NODE.removeNodeDirs(currentNode.getId());
							Map<String,NODEDIRBean> nodeDirs=new HashMap<String, NODEDIRBean>();
							for(String name:Dir.keySet()){
								NODEDIRBean dir=new NODEDIRBean();
								dir.setNodeID(currentNode.getId());
								dir.setDirName(name);
								dir.setDirValue(Dir.get(name).getValue());
								dir.setDirFilter(Dir.get(name).getType());
								dir.setMdfUser(Context.session.userID);
								dir.inroll();
								nodeDirs.put(name, dir);
							}
							currentNode.setDirs(nodeDirs);
						}
					}

					
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_successoperate")+alertMsg);
					box.open();	
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}
			});	 
			
			 
		 actionPannel.pack();
		 scComosite.layout(true);
		 scComosite.setContent(directoryPannel);
		 scComosite.pack();
		 pannel.pack();
		 itemDirectory.setControl(pannel);
	}
	
	private Text textDbName,textDbuser,textPasswd,textPasswdDouble,textPort,textBackDbName=null;
	private Combo comboDbType=null;
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
		    //btnShow.addSelectionListener(new ShowDbPasswodAction());
		    
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
		 
			 Label labBackDbName=new Label(dbPannel,SWT.NONE);
			 labBackDbName.setText(Constants.getStringVaule("label_backdbname")+"(*)");
			 labBackDbName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			 textBackDbName=new Text(dbPannel,SWT.BORDER);
			 textBackDbName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			 textBackDbName.setToolTipText(Constants.getStringVaule("text_tips_backdbname"));
			
			
			Label labDbPort=new Label(dbPannel,SWT.NONE);
			labDbPort.setText(Constants.getStringVaule("label_dbport")+"(*)");
			labDbPort.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			textPort=new Text(dbPannel,SWT.BORDER);
			textPort.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
			textPort.setToolTipText(Constants.getStringVaule("text_tips_dbport"));

			Button btnCancel=new Button(dbPannel,SWT.PUSH);
			btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
			btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
			btnCancel.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					textDbuser.setText("");
					textPasswd.setText("");
					textDbName.setText("");
					textPort.setText("");
					textBackDbName.setText("");
					comboDbType.setText("");
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					
				}
			});
			
			Button btnOK=new Button(dbPannel,SWT.PUSH);
			btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
			btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
			btnOK.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					String dbType=comboDbType.getText();
					String user=textDbuser.getText();
					String passwd=textPasswd.getText();
					String dbname=textDbName.getText();
					String dbport=textPort.getText();
					String backup=textBackDbName.getText();
					if(StringUtil.isNullOrEmpty(dbType)||
							StringUtil.isNullOrEmpty(user,8)||
							StringUtil.isNullOrEmpty(passwd,32)||	
							StringUtil.isNullOrEmpty(dbname,32)||
							StringUtil.isPort(dbport)){
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
							box.open();	
						}else{
							passwd=SecurityCenter.getInstance().encrypt(passwd, Context.EncryptKey);
							 if((NODEBean.DB.SqlServer.name()).equals(dbType)){
								 dbType=NODEBean.DB.SqlServer.ordinal()+"";
							 }
							 else if((NODEBean.DB.Oracle.name()).equals(dbType)){
								 dbType=NODEBean.DB.Oracle.ordinal()+"";
							 }else{
								 dbType=" ";
							 }
							String nodeServer=currentNode.getIp();
							boolean testResult=SqlServer.getInstance().testConnection(dbType, nodeServer, dbport, user, passwd, dbname);
							if(testResult){
									currentNode.setDbType(dbType);
									currentNode.setDbUser(user);
									currentNode.setDbPasswd(passwd);
									currentNode.setDbname(dbname);
									currentNode.setBackDBname(backup);
									currentNode.setDbport(dbport);
									USERNODE.updateDBInfo( Context.session.userID, currentNode.getId(), dbType,user, passwd, dbname, backup, dbport);
									MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage(Constants.getStringVaule("alert_successoperate"));
									box.open();	
							 }else{
								    MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage("测试链接失败，请确认数据属性配置正确！");
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

	private Text texSFtpUser,texSFtpPasswd,texSFtpPasswdDouble,texSFtpDir,ftpSPort=null;
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
		ftpSPort=new Text(ftpPannel,SWT.BORDER);
		ftpSPort.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		ftpSPort.setToolTipText(Constants.getStringVaule("text_tips_ftpdir"));	
		ftpSPort.setText(Context.SFtpPortNo+"");
		 
		Label labFtpDir=new Label(ftpPannel,SWT.NONE);
		labFtpDir.setText(Constants.getStringVaule("label_ftpdir")+"");
		labFtpDir.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.BEGINNING, true, false, 2, 1, 0, 0));
		texSFtpDir=new Text(ftpPannel,SWT.BORDER);
		texSFtpDir.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		texSFtpDir.setToolTipText(Constants.getStringVaule("text_tips_ftpdir"));
		
		Button btnCancel=new Button(ftpPannel,SWT.PUSH);
		btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
		btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
		btnCancel.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				texSFtpUser.setText("");
				texSFtpPasswd.setText("");
				texSFtpPasswdDouble.setText("");
				texSFtpDir.setText("");
				ftpSPort.setText("");
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
		
		
		Button btnOK=new Button(ftpPannel,SWT.PUSH);
		btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
		btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
		btnOK.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				String user=texSFtpUser.getText();
				String passwd=texSFtpPasswd.getText();
				String dir=texSFtpDir.getText();
				String port=ftpSPort.getText();
				if(StringUtil.isNullOrEmpty(user,30)||
					StringUtil.isNullOrEmpty(passwd,32)||
					StringUtil.isNullOrEmpty(dir,60)){
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
					String ip=currentNode.getIp();
					boolean testResult=LnxSftp.testSsh(ip, user, passwd, portNo, dir);
					if(testResult){
						passwd=SecurityCenter.getInstance().encrypt(passwd, Context.EncryptKey);
						currentNode.setSftpUser(user);
						currentNode.setSftpPasswd(passwd);
						currentNode.setSftpDir(dir);
						currentNode.setSftpPort(port);
						USERNODE.updateSftpInfo( Context.session.userID, currentNode.getId(), user, passwd, port, dir);
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(Constants.getStringVaule("alert_successoperate"));
						box.open();	
					}else{
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage("测试连接失败，请确认ssh的用户名密码信息和目录权限信息");
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
	
	
	public  ScrolledComposite scSVCComosite;
	private Composite dynamicSVCPanel;
	public Map<String,String> Services=null;
	private void createServiceTab(){
		 CTabItem itemService=new CTabItem(tabFloder,SWT.NONE);
		 itemService.setText(Constants.getStringVaule("tabItem_service"));
		 
		 Composite svcPannel=new Composite(tabFloder,SWT.NONE);
		 svcPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 svcPannel.setLayout(LayoutUtils.getComGridLayout(8, 0));
		   
		 	Label labSvcName=new Label(svcPannel,SWT.NONE);
		    labSvcName.setText(Constants.getStringVaule("label_svc_name"));
		    labSvcName.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 2, 1, 0, 0));
			
			Label labSvcStart=new Label(svcPannel,SWT.NONE);
			labSvcStart.setText(Constants.getStringVaule("label_svc_start"));
			labSvcStart.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 3, 1, 0, 0));
			
			Label labSvcStop=new Label(svcPannel,SWT.NONE);
			labSvcStop.setText(Constants.getStringVaule("label_svc_stop"));
			labSvcStop.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 3, 1, 0, 0));

			scSVCComosite=new ScrolledComposite(svcPannel,SWT.V_SCROLL|SWT.H_SCROLL|SWT.BORDER);
			scSVCComosite.setAlwaysShowScrollBars(true);
			scSVCComosite.setExpandHorizontal(true);
			scSVCComosite.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 8, 1, 0, 0));
			scSVCComosite.setLayout(LayoutUtils.getComGridLayout(1, 1));
			 
			dynamicSVCPanel=new Composite(scSVCComosite,SWT.NONE);
			dynamicSVCPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			dynamicSVCPanel.setLayout(LayoutUtils.getComGridLayout(8, 0));
			
			//Services=Dictionary.getDictionaryMap("NODE_SERVICE.TYPE");
			Services=new HashMap<String, String>();
			List<Item> services=Dictionary.getDictionaryList("NODE_SERVICE.TYPE");
			if(services!=null&&services.size()>0){
				String[] items=new String[services.size()];
				int index=0;
				for(Item item:services){
					items[index]=item.getValue();
					index++;
					Services.put(item.getValue(), item.getKey());
				}
				 for(int w=0;w<services.size();w++){
					   Combo svcName= new Combo(dynamicSVCPanel,SWT.DROP_DOWN);
					   svcName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 2, 1, 0, 0));
					   svcName.setItems(items);
					   svcName.setData("$Type", "name");
					   
					   Text textPath=new Text(dynamicSVCPanel,SWT.BORDER);
					   textPath.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 3, 1, 0, 0));
					   textPath.setData("$Type", "start");
					   
					   Text textFilter=new Text(dynamicSVCPanel,SWT.BORDER);
					   textFilter.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 3, 1, 0, 0));
					   textFilter.setData("$Type", "stop");
				 }
			}
			dynamicSVCPanel.pack();
			 Composite actionPannel=new Composite(svcPannel,SWT.NONE);
			 actionPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 8, 1, 0, 0));
			 actionPannel.setLayout(LayoutUtils.getComGridLayout(8, 0));
			  
			    Button btnCancel=new Button(actionPannel,SWT.PUSH);
				btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 4, 1, 0, 0));
				btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
				btnCancel.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						Control[] controls=dynamicSVCPanel.getChildren();
						if(controls!=null&&controls.length>0){
							for(Control control:controls){
								    String type=(String)control.getData("$Type");
								    if("name".equals(type)){
								    	Combo combo=(Combo)control;
								    	combo.setText("");
								    }else{
								    	Text text=(Text)control;
										if(text!=null){
											text.setText("");
										}
								    }
							}
						}
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						
					}
				});
				
				Button btnOK=new Button(actionPannel,SWT.PUSH);
				btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 4, 1, 0, 0));
				btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
				btnOK.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent arg) {
						Control[] controls=dynamicSVCPanel.getChildren();
						String services="";
						if(controls!=null&&controls.length>0){
							for(Control control:controls){
								 String type=(String)control.getData("$Type");
								 if("name".equals(type)){
									 Combo combo=(Combo)control;
									 String name=combo.getText();
									 String svName=Services.get(name);
									 services=services+"|"+svName;
								 }
								 else if("start".equals(type)){
									 Text text=(Text)control;
									 String start=text.getText();
									 services=services+";"+start;
								 }else{
									 Text text=(Text)control; 
									 String stop=text.getText();
									 services=services+";"+stop;
								 }
							}
						}
						services=StringUtil.ltrim(services, "|");
						services=StringUtil.rtrim(services, ";");
						String[] columns=services.split("\\|");
						String alertMsg="";
						if(columns.length>0){
							Map<String,Triple> Svc=new HashMap<String, Triple>();
							for(int w=0;w<columns.length;w++){
								String line=columns[w];
								String[] cols=line.split(";");
								if(cols!=null&&cols.length>0){
									String name=cols[0];
									String  start="";
									String stop="";
									if(cols.length>=2)
										start=cols[1];
									if(cols.length>=3)
										stop=cols[2];
									Triple triple=new Triple();
									triple.setName(name);
									triple.setValue(start);
									triple.setType(stop);
									if(StringUtil.isNullOrEmpty(start)||StringUtil.isNullOrEmpty(stop)){
										alertMsg+="服务:"+name+"启停脚本路径为空，将忽略该服务的设置;";
									}
									else{
										if(Svc.containsKey(name)){
											alertMsg+="服务:"+name+"已经存在，将忽略该设置;";
										}
										else{
											Svc.put(name, triple);
										}
									}
								}
							}
							if(Svc.size()>0){
								NODE.removeNodeServices(currentNode.getId());
								Map<String,NODESERVICEBean> nodeSvcs=new HashMap<String, NODESERVICEBean>();
								for(String name:Svc.keySet()){
									NODESERVICEBean svc=new NODESERVICEBean();
									svc.setNodeID(currentNode.getId());
									svc.setType(name);
									svc.setStart(Svc.get(name).getValue());
									svc.setStop(Svc.get(name).getType());
									svc.setMdfUser(Context.session.userID);
									svc.inroll();
									nodeSvcs.put(name, svc);
								}
								currentNode.setServices(nodeSvcs);
							}
						
						}
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(Constants.getStringVaule("alert_successoperate")+alertMsg);
						box.open();	
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent arg) {
						
					}
				});
			scSVCComosite.layout(true);
			scSVCComosite.setContent(dynamicSVCPanel);
			scSVCComosite.pack();
		 
		 svcPannel.pack();
		 itemService.setControl(svcPannel);
	}
	
	private  Composite content=null;
	private SYSTEMBean group;
	private Text textProgress=null;
	private ProgressBar progress=null;
	private Tree nodeTree=null;
	public TreeItem  treeRoot=null;
	private CTabFolder tabFloder=null;
	private CommandView commandView=null;
	private List<NODEBean> nodeData=null;
	
	public void setCommandView(CommandView instance){
		this.commandView=instance;
	}
	
	public void initConsoles(){
		  if(!this.nodeData.isEmpty()){	
			  for(int i=0;i<this.nodeData.size();i++){
				  NODEBean bean=this.nodeData.get(i);
	        		String name=bean.getName();
	        			  if(!commandView.getTabState(name)){
	      					ConsoleView console=new ConsoleView(commandView.getTabFloder(),bean);
	      					commandView.setTabItems(console.getContent(), name,bean.getIp(),bean,console);
	      				}
	        		}
			  }
	}
	
   public SYSTEMBean  getGroup(){
	   return this.group;
   }
   
   public void setProgress(String text,int max,int curr){
		if(!textProgress.getVisible())
			textProgress.setVisible(true);
		if(!progress.getVisible())
			progress.setVisible(true);
		textProgress.setEditable(true);
		textProgress.setText(text);
		textProgress.setEditable(false);
		progress.setMaximum(max);
		progress.setSelection(curr);//进度条前进一格
	}
   
   public void hideProgress(){
		textProgress.setText("");
		textProgress.setVisible(false);
		progress.setSelection(0);
		progress.setMaximum(0);
		progress.setVisible(false);
	}
   
   //更新节点的图标
   
   public void redrawDiagramView(){
	  this.refreshNodeTree();
   }
   
   
   public  TreeEditor editor;
   public class EditNodeAction extends MouseAdapter{
		public void mouseDown(MouseEvent e){
			TreeItem currentItem=nodeTree.getItem(new Point(e.x,e.y));
		    //让之前的节点失去焦点先
			 Control oldControl=editor.getEditor();
				if(oldControl!=null){
					oldControl.dispose();
				}
 			if(currentItem!=null&&e.button==3){
 				//手动添加节点
 				String type=(String)currentItem.getData("$Type");
 				if("cluster".equals(type)){
 					Cluster data= (Cluster)currentItem.getData();
 					  Menu actionMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
	 		    	 MenuItem itemAdd=new MenuItem(actionMenu,SWT.PUSH);
	 		    	 itemAdd.setText("新增节点");
	 		    	 itemAdd.setData(data);
	 		    	 itemAdd.setData("TreeItem", currentItem);
	 		    	 itemAdd.setImage(Icons.getAddIcon());
	 		    	 itemAdd.addSelectionListener(new SelectionAdapter(){ 	
	 	        		 public void widgetSelected(SelectionEvent e){
	 	        			MenuItem item=(MenuItem)e.getSource();
	 	        			Cluster data=(Cluster)item.getData();
	 	        			TreeItem parentItem=(TreeItem)item.getData("TreeItem");
	 	        			TreeItem newItem=new TreeItem(parentItem,SWT.NONE);
	 	        			newItem.setText("新增节点");
	 	        			newItem.setImage(Icons.getMidNodeIcon());
	 	        			newItem.setData("$Type", "node");
	 	        			parentItem.setExpanded(true);
	 	        			COMPONENTBean component=COMPONENT.getComponentByID(data.getComponentID(), Context.session.currentFlag);
	 	        			String id=NODE.getNewNodeID();
	 	        			//节点定义
	 	        			NODEBean node=new NODEBean(id," ","新增节点",NODEBean.OS.Null.ordinal()+"",data.getName(),data.getComponentID(),component.getSystemID(), Context.session.userID,"C");
	 	        			node.setComponentType(data.getComponentType());
	 	        			node.setStatus(NODEBean.Status.Null.ordinal()+"");
	 	        			node.setOs(NODEBean.OS.Null.ordinal()+"");
	 	        			node.setSeq("1");
	 	        			node.setType(NODEBean.Type.Node.ordinal()+"");
	 	        			node.setSchFlag(NODEBean.Flag.Mute.ordinal()+"");
	 	        			newItem.setData(node);
	 	        			nodeData.add(node);
	 	        			NODE.addData(node);
	 	        			//用户节点
	 	        			USERNODE.addUserNode( Context.session.userID, id, node.getStatus(), node.getSeq(), node.getType(),node.getSchFlag(),node.getOs());
	 	        			Text newEditor=new Text(nodeTree,SWT.NONE);
	 	        			newEditor.setText("新增节点");
	 	        			newEditor.addModifyListener(new ModifyListener(){
								public void modifyText(ModifyEvent e) {
									Text text=(Text)editor.getEditor();
									String newName=text.getText();
									if(newName.equals("新增节点")){
										editor.getItem().setText("");
										text.setText("");
									}else{
									   editor.getItem().setText(newName);
									}
								}
	 	        			});
	 	        			newEditor.setFocus();
	 	        			newEditor.addFocusListener(new FocusListener() {
								public void focusLost(FocusEvent e) {
									Text text=(Text)e.getSource();
									NODEBean data=((NODEBean)editor.getItem().getData());
									String temp=text.getText();
									if(temp.indexOf("(")>0&&temp.indexOf(")")!=-1){
		 								String ip=temp.substring(0,temp.indexOf("("));
		 								String name=temp.substring(temp.indexOf("(")+1);
		 								name=name.replace(")", "");
		 								data.setIp(ip);
		 								data.setName(name);
		 								NODE.setNodeTag(data.getId(), ip, name, data.getFlag(),  Context.session.userID);
		 								editor.getItem().setData(data);
		 							}else{
		 								String msg="机器节点名称"+temp+"不符合格式：ip(name),请重新命名！";
		 								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
		 								box.setText(Constants.getStringVaule("messagebox_alert"));
		 								box.setMessage(msg);
		 								box.open();
		 							}
								}
								public void focusGained(FocusEvent e) {
								}
							});
	 	        			newEditor.setFocus();
	 	        			editor.setEditor(newEditor, newItem);
	 	        		 }
	 		    	});	 
	 		      nodeTree.setMenu(actionMenu);
 				}else{
 					nodeTree.setMenu(null);
 				}
 			}
 		
		}
		
		
 		public void mouseDoubleClick(MouseEvent e){
 			TreeItem currentItem=nodeTree.getItem(new Point(e.x,e.y));
 			if(currentItem!=null){
 				String type=(String)currentItem.getData("$Type");
 				String status=(String)treeRoot.getData("$Status");
 				if(editable){
 					NODEBean data=(NODEBean)currentItem.getData();
 					 if(data!=null){
 	 					Control oldControl=editor.getEditor();
 	 					if(oldControl!=null){
 	 						oldControl.dispose();
 	 					}
	 					Text newEditor=new Text(nodeTree,SWT.NONE);
	 					newEditor.setText(currentItem.getText());
	      			    newEditor.addModifyListener(new ModifyListener(){
	 						public void modifyText(ModifyEvent e) {
	 							Text text=(Text)editor.getEditor();
	 							String newName=text.getText();
	 							editor.getItem().setText(newName);
	 						}
	      			    	});
		      			newEditor.addFocusListener(new FocusListener() {
		 						public void focusLost(FocusEvent e) {
		 							Text text=(Text)e.getSource();
		 							NODEBean data=((NODEBean)editor.getItem().getData());
		 							String temp=text.getText();
		 							if(temp.indexOf("(")>0&&temp.indexOf(")")!=-1){
		 								String ip=temp.substring(0,temp.indexOf("("));
		 								String name=temp.substring(temp.indexOf("(")+1);
		 								name=name.replace(")", "");
		 								data.setIp(ip);
		 								data.setName(name);
		 								NODE.setNodeTag(data.getId(), ip, name, data.getFlag(),  Context.session.userID);
		 								editor.getItem().setData(data);
		 							}else{
		 								String msg="机器节点名称"+temp+"不符合格式：ip(name),请重新命名！";
		 								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
		 								box.setText(Constants.getStringVaule("messagebox_alert"));
		 								box.setMessage(msg);
		 								box.open();
		 							}
		 							
		 						}
		 						public void focusGained(FocusEvent e) {
		 							
		 						}
		 					});
		      			
		      				newEditor.setFocus();
		      				editor.setEditor(newEditor, currentItem);
		 	 			}
		 		  } else{
		 				RunView guide=(RunView)currentItem.getData("$View");
		 				if(guide!=null){
		 					guide.setPosition(new Point(e.x,e.y));
		 					guide.show();
		 				}
		 		  }
 			}
 		}
   }
   
   public NODEBean currentNode;
   public class NodePropertyAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			TreeItem item=(TreeItem)e.item;
			String type=(String)item.getData("$Type");
			 if("node".equals(type)){
				 NODEBean data=(NODEBean)item.getData();
				 currentNode=data;
				 
				 textIp.setText(data.getIp());
				 textIp.setEditable(false);
				 textName.setText(data.getName());
				 textName.setEditable(false);
				 textSPort.setText(data.getsPort());
				 int index=0;
				 if(!StringUtil.isNullOrEmpty(data.getSeq())){
					index=Integer.parseInt(data.getSeq())-1;
				 }
			     comboSeq.select(index);
			     if((NODEBean.OS.Windows.ordinal()+"").equals(data.getOs())){
			    	 comboOS.select(0);
			     }
			     if((NODEBean.OS.Linux.ordinal()+"").equals(data.getOs())){
			    	 comboOS.select(1);
			     }
			     
			     if(data!=null&&!StringUtil.isNullOrEmpty(data.getDbType())){
				     if((NODEBean.DB.SqlServer.ordinal()+"").equals(data.getDbType())){
				    	 comboDbType.select(0);
				     }
				     if((NODEBean.DB.Oracle.ordinal()+"").equals(data.getDbType())){
				    	 comboDbType.select(1);
				     }
			     }
			     else{
			    	 comboDbType.setText("");
			     }
			     if(data!=null&&!StringUtil.isNullOrEmpty(data.getDbUser()))
			     		textDbuser.setText(data.getDbUser());
			     else
			    	 textDbuser.setText("");
					String passwd=data.getDbPasswd();
					if(!StringUtil.isNullOrEmpty(passwd)){
							passwd=SecurityCenter.getInstance().decrypt(passwd, Context.EncryptKey);
							textPasswd.setText(passwd);
					}else{
						textPasswd.setText("");
					}
				if(data!=null&&!StringUtil.isNullOrEmpty(data.getDbname()))
					textDbName.setText(data.getDbname());
				else
					textDbName.setText("");
				if(data!=null&&!StringUtil.isNullOrEmpty(data.getBackDBname()))
					textBackDbName.setText(data.getBackDBname());
				else
					textBackDbName.setText("");
				if(data!=null&&!StringUtil.isNullOrEmpty(data.getDbport()))
					textPort.setText(data.getDbport());
				else
					textPort.setText("");
					
				Map<String, NODEDIRBean> dirs=data.getDirs();
				if(dirs.size()>0){
					int w=0;
					Control[] controls=directoryPannel.getChildren();
					for(String key:dirs.keySet()){
						NODEDIRBean dir=dirs.get(key);
						String name=dir.getDirName();
						String value=dir.getDirValue();
						String filter=dir.getDirFilter();
						((Text)controls[w]).setText(name);
						w++;
						((Text)controls[w]).setText(value);
						w++;
						((Text)controls[w]).setText(filter);
						w++;
					}
					for(int n=w;n<controls.length;n++){
						((Text)controls[n]).setText("");
					}
				}else{
					Control[] controls=directoryPannel.getChildren();
					for(int w=0;w<controls.length;w++){
						((Text)controls[w]).setText("");
					}
				}
		    	directoryPannel.layout();  
		    	Map<String,String> AllServices=Dictionary.getDictionaryMap("NODE_SERVICE.TYPE");
		    	Map<String, NODESERVICEBean> svcs=data.getServices();
				if(svcs.size()>0){
					int w=0;
					Control[] controls=dynamicSVCPanel.getChildren();
					for(String key:svcs.keySet()){
						NODESERVICEBean svc=svcs.get(key);
						String name=svc.getType();
						String svcKey=AllServices.get(name);
						String start=svc.getStart();
						String  stop=svc.getStop();
						((Combo)controls[w]).setText(svcKey);
						w++;
						((Text)controls[w]).setText(start);
						w++;
						((Text)controls[w]).setText(stop);
						w++;
					}
					for(int n=w;n<controls.length;n++){
						String  comType=(String)controls[n].getData("$Type");
						if("name".equals(comType)){
							((Combo)controls[n]).setText("");
						}else{
							((Text)controls[n]).setText("");
						}
					}
				}else{
					Control[] controls=dynamicSVCPanel.getChildren();
					for(int w=0;w<controls.length;w++){
						String  comType=(String)controls[w].getData("$Type");
						if("name".equals(comType)){
							((Combo)controls[w]).setText("");
						}else{
							((Text)controls[w]).setText("");
						}
					}
				}
				dynamicSVCPanel.layout();
				
		  if(data!=null&&!StringUtil.isNullOrEmpty(data.getSftpUser()))
			texSFtpUser.setText(data.getSftpUser());
		  else
			  texSFtpUser.setText("");
		  if(data!=null&&!StringUtil.isNullOrEmpty(data.getSftpDir()))
			texSFtpDir.setText(data.getSftpDir());
		  else
			  texSFtpDir.setText("");
		  if(data!=null&&!StringUtil.isNullOrEmpty(data.getSftpPort()))
			ftpSPort.setText(data.getSftpPort());
		  else
			  ftpSPort.setText("");
			String sftppasswd=data.getSftpPasswd();
			if(!StringUtil.isNullOrEmpty(sftppasswd)){
				sftppasswd=SecurityCenter.getInstance().decrypt(sftppasswd, Context.EncryptKey);
				texSFtpPasswd.setText(sftppasswd);
			}else{
				texSFtpPasswd.setText("");
			}
		   }
		}
   }
   
   
   
   public class AddDirAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
		    Label labDirName=new Label(directoryPannel,SWT.NONE);
			labDirName.setText(Constants.getStringVaule("label_rootdir"));
			labDirName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 3, 1, 0, 0));
			labDirName.setData("$Type", "Label");
			
			Text textRootDir=new Text(directoryPannel,SWT.BORDER);
			textRootDir.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 5, 1, 0, 0));
			textRootDir.setToolTipText(Constants.getStringVaule("text_tips_rootdir"));
			textRootDir.setData("$Type", "Text");
			directoryPannel.layout();
		}
   }
   
   public class RemoveDirAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Control[] controls=directoryPannel.getChildren();
			if(controls!=null&&controls.length>2){
				for(int w=controls.length-1;w>=controls.length-2;w--){
					controls[w].dispose();
				}
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
