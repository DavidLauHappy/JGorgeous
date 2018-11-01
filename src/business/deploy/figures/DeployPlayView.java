package business.deploy.figures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.COMPONENT;
import model.NODE;
import model.NODESTEP;
import model.PKG;
import model.STEP;
import model.SYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Logger;

import bean.COMPONENTBean;
import bean.Cluster;
import bean.NODEBean;
import bean.NODEDIRBean;
import bean.NODESTEPBean;
import bean.PKGBean;
import bean.STEPBean;
import bean.SYSTEMBean;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

public class DeployPlayView {
	
	  private Composite parent;
	  public  Composite selfContent=null;
	  private  SashForm content=null;
	  public  Group group=null;
	  public  Combo comboGroup;
	  private Tree stepTree;
	  private Tree nodeTree;
	  private PKGBean version;
	  private Map<String,SYSTEMBean> Systems;
	  private List<TreeItem> Nodes;//每个步骤对应的被选中的节点对象
	   public DeployPlayView(Composite com,String versionID){
		     parent=com;
		     this.version=PKG.getPkg(versionID);
			 selfContent=new Composite(parent,SWT.NONE);
			 selfContent.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			 selfContent.setLayout(LayoutUtils.getComGridLayout(1, 10));
	
			 content=new SashForm(selfContent,SWT.VERTICAL);
			 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
			 this.createToolView();
			 this.createTreesPanel();
			 content.setWeights(new int[]{30,70});
			 selfContent.pack();
	   }
	   
	 
	   private void createToolView(){
		    Composite toolPanel=new Composite(content,SWT.NONE);
			toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			toolPanel.setLayout(LayoutUtils.getComGridLayout(10, 1));
			
			Label labName=new Label(toolPanel,SWT.NONE);
			labName.setText(Constants.getStringVaule("label_pkgname"));
			labName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1, 0, 0));
			Text textPkg=new Text(toolPanel,SWT.NONE);
			textPkg.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1, 0, 0));
			textPkg.setText(version.getId());
			textPkg.setEditable(false);
			
			Label labApp=new Label(toolPanel,SWT.NONE);
			labApp.setText(Constants.getStringVaule("label_appname"));
			labApp.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1, 0, 0));
			Text textName=new Text(toolPanel,SWT.NONE);
			textName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1, 0, 0));
			Map<String, String> apps=Dictionary.getDictionaryMap("APP");
			String appKey=version.getAppname();
			String appDesc=apps.get(appKey);
			textName.setText(appDesc);
			textName.setEditable(false);
			 group=new Group(toolPanel,SWT.NONE|SWT.V_SCROLL);
			 group.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
			 group.setLayout(LayoutUtils.getComGridLayout(1, 10));
			 group.setText("关联应用");
				if(apps!=null&&apps.size()>0){
					 for(String app:apps.keySet()){
						   String appName=apps.get(app);
						    Button btn=new Button(group,SWT.CHECK|SWT.LEFT);
							btn.setData(appName);
							btn.setText(appName);
							btn.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
							if(!StringUtil.isNullOrEmpty(version.getRelapps())){
								if(version.getRelapps().indexOf(app)!=-1){
									btn.setSelection(true);
								}
							}
				    	}
				}
			group.pack();

			Label labDesribe=new Label(toolPanel,SWT.NONE);
			labDesribe.setText(Constants.getStringVaule("label_versionDesc"));
			labDesribe.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, true, 1, 1, 0, 0));
			
			Text describe=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
			describe.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 6, 1, 0, 0));
			if(!StringUtil.isNullOrEmpty(version.getDesc())){
				describe.setText(version.getDesc());
			}
			describe.setEditable(false);
			toolPanel.pack();
	   }
   
	   private void createTreesPanel(){
		       SashForm Panel=new SashForm(content,SWT.HORIZONTAL|SWT.BORDER);
			   Panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			   Panel.setLayout(LayoutUtils.getComGridLayout(2, 1));
			   this.createStepTreePanel(Panel);
			   this.createNodeTreePanel(Panel);
			   Panel.setWeights(new int[]{50,50});
			   Panel.pack();
		  }
	   
	   private void createStepTreePanel(SashForm parent){
		   Composite Panel=new Composite(parent,SWT.NONE);
		   Panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		   Panel.setLayout(LayoutUtils.getComGridLayout(1, 1));
		   Button btnReview=new Button(Panel,SWT.PUSH);
		   btnReview.setText("       "+Constants.getStringVaule("btn_review")+"        ");
		   btnReview.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		   btnReview.addSelectionListener(new MakeReviewAction());
		   
		     stepTree=new Tree(Panel,SWT.SINGLE);//
		     stepTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	    	 stepTree.addSelectionListener(new ChooseNodeAction());
	    	 stepTree.addMouseListener(new ExpandFloderAction());
	    	 TreeItem  treeRoot=new TreeItem(stepTree,SWT.CHECK|SWT.MULTI);
	    	 treeRoot.setText(version.getId());
	    	 treeRoot.setImage(Icons.getFloderIcon());
	    	 treeRoot.setData(version);
	    	 treeRoot.setData("$Type", STEPBean.Type.Pkg);
	    	 List<STEPBean>  steps=STEP.getPkgSteps(version.getId());
	    	  for(int w=0;w<steps.size();w++){
	    		  STEPBean step=steps.get(w);
	    		  boolean expand=false;
	    		  if(step.getAction().equals(STEPBean.ActionType.FileCopy.ordinal()+"")||
	          			step.getAction().equals(STEPBean.ActionType.ScriptInstall.ordinal()+"") ){
	          			  expand=true;
	    		  }
	    		  TreeItem  workflowStep=new TreeItem(treeRoot,SWT.CHECK|SWT.MULTI);
        		  workflowStep.setText(step.getDesc());
        		  workflowStep.setImage(Icons.getFloderIcon());
        		  workflowStep.setData(step);
        		  if(expand){
        			  workflowStep.setData("$Type", STEPBean.Type.Step);
        		  }else{
        			  workflowStep.setData("$Type", STEPBean.Type.Null);
        		  }
	    	  }
	    	  treeRoot.setExpanded(true);
		   stepTree.pack();
		   Panel.pack();
	   }
	   
	   private void createNodeTreePanel(SashForm parent){
		    Composite pannel=new Composite(parent,SWT.NONE);
			pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			pannel.setLayout(LayoutUtils.getComGridLayout(3, 10));
			Label labName=new Label(pannel,SWT.NONE);
			labName.setText(Constants.getStringVaule("label_node_group"));
			labName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
			comboGroup=new Combo(pannel,SWT.DROP_DOWN);
			comboGroup.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
			comboGroup.addSelectionListener(new SwitchGroupAction());
			
		   Button btnApply=new Button(pannel,SWT.PUSH);
		   btnApply.setText("       "+Constants.getStringVaule("btn_apply")+"        ");
		   btnApply.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
		   btnApply.addSelectionListener(new SetNodeStepAction());
		   
		   List<SYSTEMBean> appSys=SYSTEM.getSystems(version.getAppname(), Context.session.currentFlag);
		   if(appSys!=null&&appSys.size()>0){
			   Systems=new HashMap<String, SYSTEMBean>();
			   String[] items=new String[appSys.size()];
			   int index=0;
			   for(SYSTEMBean system:appSys){
				   items[index]=system.getName();
				   index++;
				   Systems.put(system.getName(), system);
			   }
			   comboGroup.setItems(items);
			   comboGroup.select(0);
		   }
		   
		   
			nodeTree=new Tree(pannel,SWT.CHECK);
			nodeTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
			nodeTree.addSelectionListener(new CheckNodeAction());
			this.loadTagTree();
			nodeTree.pack();
			pannel.pack();
	   }
	   
	   public class ChooseNodeAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			TreeItem currentItem=(TreeItem)e.item;
	 			STEPBean.Type type=(STEPBean.Type)currentItem.getData("$Type");
	 			if( STEPBean.Type.Step.equals(type)){
	 				if(Nodes==null)
	 					Nodes=new ArrayList<TreeItem>();
	 				Nodes.clear();
	 				STEPBean data=(STEPBean)currentItem.getData();
	 				  //显示这个步骤的节点定制情况
	 				  String groupName=comboGroup.getText();
	 				  String systemID=Systems.get(groupName).getBussID();
	 				  loadTagTree();
	 				  List<NODESTEPBean> stepNodes=NODESTEP.getStepNode(data.getPkgID(), systemID, data.getId());
	 				   if(stepNodes!=null&&stepNodes.size()>0){
	 					   for(NODESTEPBean step:stepNodes)
	 						  setNodeCheck(step.getNodeID(),data);
	 				   }else{
	 					  setNodeCheck(null,data);
	 				   }
	 			}
	 			
	 			if(STEPBean.Type.Null.equals(type)){
	 				STEPBean data=(STEPBean)currentItem.getData();
	 				 String sysName=comboGroup.getText();
	 				  String systemID=Systems.get(sysName).getBussID();
	 				  loadTagTree();
	 				 List<NODESTEPBean> stepNodes=NODESTEP.getStepNode(data.getPkgID(), systemID, data.getId());
	 				   if(stepNodes!=null&&stepNodes.size()>0){
	 					   for(NODESTEPBean step:stepNodes)
	 						  setNodeCheck(step.getNodeID(),data);
	 				   }else{
	 					  setNodeCheck(null,data);
	 				   }
	 			}
	 		}
	   }
	   
	    //策略配置时展开版本步骤
	    public class ExpandFloderAction extends MouseAdapter{
	    	public void mouseDoubleClick(MouseEvent e){
	    		TreeItem currentItem=stepTree.getItem(new Point(e.x,e.y));
	    		if(currentItem!=null){
	    			STEPBean.Type type=(STEPBean.Type)currentItem.getData("$Type");
		 			if( STEPBean.Type.Step.equals(type)){
		 				STEPBean data=(STEPBean)currentItem.getData();
		 				List<STEPBean>  childrenStep=STEP.geFileSteps(data.getPkgID(), data.getId());
		 				currentItem.removeAll();
		 				 for(int w=0;w<childrenStep.size();w++){
		 		    		  TreeItem  step=new TreeItem(currentItem,SWT.NONE);
		 		    		  step.setText(childrenStep.get(w).getDesc());
		 		    		  step.setImage(Icons.getFileIcon());
		 		    		  step.setData(childrenStep.get(w));
		 		    		  step.setData("$Type", STEPBean.Type.fileStep);
		 				 }
		 				currentItem.setExpanded(true);
		 			}
	    		}
	 		}
	    }
	   
	   private void loadTagTree(){
		   nodeTree.removeAll();
		   if(Nodes!=null)
			   Nodes.clear();
		   String group=comboGroup.getText();
		   if(!StringUtil.isNullOrEmpty(group)){
			   String groupName=group;
			   SYSTEMBean system=Systems.get(groupName);
		   		List<COMPONENTBean>   components=COMPONENT.getUserComponents(Context.session.userID, system.getBussID(), Context.session.currentFlag);
			   for(COMPONENTBean component:components){
						TreeItem  treeItemComponent=new TreeItem(nodeTree,SWT.NONE);
						treeItemComponent.setText(component.getName());
						treeItemComponent.setImage(Icons.getTagFloderIcon());
						treeItemComponent.setData(component);
						treeItemComponent.setData("$Type", "component");
						List<Cluster> clusters=COMPONENT.getCuster(component.getId(),Context.session.currentFlag);
						if(clusters!=null&&clusters.size()>0){
							for(Cluster cluster:clusters){
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
						treeItemComponent.setExpanded(true);
				}
		   }
	   }
	   
		
		public class SwitchGroupAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			loadTagTree();
	 		}
		}
		public class CheckNodeAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			TreeItem item=(TreeItem)e.item;
	 			if(item!=null){
	 				if(Nodes==null)
	 					Nodes=new ArrayList<TreeItem>();
	 				 String type=(String)item.getData("$Type");
	 				 if("node".equals(type)){
	 					 if(item.getChecked()){
	 						 Nodes.add(item);
	 					 }else{//反选或取消节点时，默认数据要同步删除	 
	 						NODEBean node=(NODEBean)item.getData();
	 		 				 String groupName=comboGroup.getText();
	 		 				 String groupID=Systems.get(groupName).getBussID();
	 						 //步骤树是一个单选树
	 						TreeItem[] stepItems=stepTree.getSelection();
	 						if(stepItems!=null&&stepItems.length>0){
	 							STEPBean step=(STEPBean)stepItems[0].getData();
	 							NODESTEP.delete(step.getPkgID(),groupID, step.getId(), node.getId());
	 							boolean ret=Nodes.remove(item);
	 						}
	 					 }
	 				 }else{
	 					  boolean checked=item.getChecked();
	 					 if("cluster".equals(type)){
	 						TreeItem[] items=item.getItems();
	 						if(items!=null){
	 							for(TreeItem node:items){
	 								node.setChecked(checked);
	 								if(item.getChecked()){
	 									 Nodes.add(node);
	 								}else{
	 									NODEBean nodeObj=(NODEBean)node.getData();
	 			 		 				 String groupName=comboGroup.getText();
	 			 		 				 String groupID=Systems.get(groupName).getBussID();
	 			 						 //步骤树是一个单选树
	 			 						TreeItem[] stepItems=stepTree.getSelection();
	 			 						if(stepItems!=null&&stepItems.length>0){
	 			 							STEPBean step=(STEPBean)stepItems[0].getData();
	 			 							NODESTEP.delete(step.getPkgID(),groupID, step.getId(), nodeObj.getId());
	 			 							boolean ret=Nodes.remove(node);
	 			 						}
	 								}
	 							}
	 						}
	 					 }
	 					item.setChecked(false); 
	 				 }
	 			}
	 		}
		}
		
		public class MakeReviewAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
				String group=comboGroup.getText(); 
	 			String itemName=version.getId()+"配置"+group+"安装策略";
	 			SYSTEMBean system=Systems.get(group);
				if(!DeployEditView.getInstance(null).getTabState(itemName)){
					StrategyView strategyView=new StrategyView(DeployEditView.getInstance(null).getTabFloder(),version.getId(),system,itemName);
					DeployEditView.getInstance(null).setTabItems(strategyView.content, itemName);
				}
				DeployEditView.getInstance(null).closeTab(version.getId());
	 		}
		}
		
		
		public class SetNodeStepAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			Context.session.inroll(e.getClass().toString(), "F-001-00007");
	 			//步骤树每次只能选择一个步骤
	 			TreeItem[] stepItems=stepTree.getSelection();
	 			if(stepItems!=null&&stepItems.length>0){
	 				if(Nodes!=null&& Nodes.size()>0){
	 					 String pkg="";
	 					 String groupName=comboGroup.getText();
						 String groupID=Systems.get(groupName).getBussID();
	 					 for(TreeItem item:stepItems){
	 						STEPBean step=(STEPBean)item.getData();
	 						//数据库已经定义的步骤节点策略数据要先删除
	 						NODESTEP.delete(step.getPkgID(), groupID, step.getId());
	 						for(TreeItem nodeItem:Nodes){
	 							NODEBean node=(NODEBean)nodeItem.getData();
	 							 pkg=step.getPkgID();
	 							NODESTEPBean ns=new NODESTEPBean();
	 							ns.setPkgID(step.getPkgID());
	 							ns.setSystemID(groupID);
	 							ns.setNodeID(node.getId());
	 							ns.setStepID(step.getId());
	 							ns.setFlag(NODESTEPBean.Flag.On.ordinal()+"");
	 							ns.setMdfUser(Context.session.userID);
	 							NODESTEP.add(ns);
	 						}
	 					 }
	 					Logger.getInstance().logServer(pkg+"步骤配置节点安装策略");
	 					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(Constants.getStringVaule("alert_successoperate"));
						box.open();	
	 					
	 				}else{
	 					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage("请选择需要部署的节点");
						box.open();	
	 				}
	 			}else{
	 				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("请选择安装步骤");
					box.open();	
	 			}
	 		}
		}
		
		public void setNodeCheck(String nodeID,STEPBean step){
			TreeItem[] items=nodeTree.getItems();
			if(items!=null&&items.length>0){
				for(TreeItem item:items){
					COMPONENTBean component=(COMPONENTBean)item.getData();
					TreeItem[] clusterItems=item.getItems();
					if(clusterItems!=null&&clusterItems.length>0){
						for(TreeItem itemCluster:clusterItems){
							Cluster cluster=(Cluster)itemCluster.getData();
							TreeItem[] nodeItems=itemCluster.getItems();
							if(nodeItems!=null&&nodeItems.length>0){
								for(TreeItem nodeItem:nodeItems){
									NODEBean node=(NODEBean)nodeItem.getData();
									if(nodeID!=null){
										//使用已经配置的数据来匹配初始化
										if(nodeID.equals(node.getId())){
											nodeItem.setChecked(true);
											if(Nodes==null)
							 					Nodes=new ArrayList<TreeItem>();
											if(!Nodes.contains(nodeItem))
						    			      Nodes.add(nodeItem);
						    			      
										}										 
									}else{//根据组件简称来自动匹配
										String abbr=component.getAbbr();
										String stepName=step.getName();
										if(stepName.indexOf(abbr)>0){
											nodeItem.setChecked(true);
											if(Nodes==null)
							 					Nodes=new ArrayList<TreeItem>();
											if(!Nodes.contains(nodeItem))
						    			      Nodes.add(nodeItem);
										}
									}
									//根据节点目录对象匹配步骤名称
									 Map<String, NODEDIRBean> dirs=node.getDirs();
									 String stepName=step.getName();
									 stepName=StringUtil.ltrim(stepName, "/");
									 if(dirs!=null&&dirs.containsKey(stepName)){
											nodeItem.setChecked(true);
											if(Nodes==null)
							 					Nodes=new ArrayList<TreeItem>();
											if(!Nodes.contains(nodeItem))
												Nodes.add(nodeItem);
									 }
								}
							}
						}
					}
				}
			}
		}
		
}
