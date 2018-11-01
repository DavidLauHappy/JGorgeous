package business.deploy.figures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.COMPONENT;
import model.DATAFLAG;
import model.NODE;
import model.SYSTEM;
import model.USERNODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import resource.Logger;
import bean.COMPONENTBean;
import bean.DATAFLAGBean;
import bean.NODEBean;
import bean.SYSTEMBean;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

public class TagManageView {
	private Composite parent;
	public Composite content;
	public Tree tagTree;
	public TreeItem  treeRoot;
	public  TreeEditor editor;
	public TreeItem currentItem;
	public Combo comboApp;
	public String appName;
	public String appID;
	public Map<String,NODEBean> OriginalNode;
	public TagManageView(Composite com){
		this.parent=com;
		OriginalNode=new HashMap<String, NODEBean>();
		this.createAndShow();
	}
	
	private void createAndShow(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 10));
		
		Composite pannel=new Composite(content,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(6, 10));
		
		Label labName=new Label(pannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_appname"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		comboApp=new Combo(pannel,SWT.DROP_DOWN);
		comboApp.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1, 0, 0));
		comboApp.addSelectionListener(new LoadAppTags());
		if(StringUtil.isNullOrEmpty(Context.Apps)){
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
			
		}
		else{
			Map<String, String> Apps=Dictionary.getDictionaryMap("APP");
			String[] apps=Context.Apps.split("\\|");
			String[] items=new String[apps.length];
			for(int w=0;w<apps.length;w++){
				String key=apps[w];
				String value=Apps.get(key);
				items[w]=key+" "+value;
	    		
			}
			comboApp.setItems(items);
	    	comboApp.select(0);
		}
		
		Button btnOK=new Button(pannel,SWT.PUSH);
		btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1, 0, 0));
		btnOK.setText("    "+Constants.getStringVaule("btn_ok")+"    ");
		btnOK.addSelectionListener(new ChooseComponentAction());
		pannel.pack();
		
		tagTree=new Tree(content,SWT.BORDER|SWT.MULTI|SWT.CHECK);
		tagTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		tagTree.addSelectionListener(new CheckAction());
		//tagTree.addMouseListener(new PopMenuAction());
	    treeRoot=new TreeItem(tagTree,SWT.NONE);
   	 	treeRoot.setText("根节点");
   	 	treeRoot.setData("$Type", "root");
   	 	treeRoot.setImage(Icons.getTagFloderIcon());
   	 	tagTree.pack();
   	    loadDirTree();
   	    editor=new TreeEditor(tagTree);
		editor.horizontalAlignment=SWT.LEFT;
		editor.grabHorizontal=true;
		editor.minimumWidth=30;
		content.pack();
	}
	
	private void loadDirTree(){
		treeRoot.removeAll();
		
		AppView.getInstance().getDisplay().asyncExec(new Runnable() {
			public void run() {
				OriginalNode.clear();
				String rawAppName=comboApp.getText();
				 appID=rawAppName.substring(0, rawAppName.indexOf(" "));
				 appName=rawAppName.substring( rawAppName.indexOf(" ")+1);
				//每次调用接口会覆盖筛选的数据
				//调接口时间太长，需要改成异步处理
				TreeItem  treeItemApp=new TreeItem(treeRoot,SWT.NONE);
				treeItemApp.setText(appName+"("+appID+")");
				treeItemApp.setImage(Icons.getTagFloderIcon());
				treeItemApp.setData("$Type", "app");
				DATAFLAGBean dataflag=DATAFLAG.getByID(Constants.dataFlagCmdb);
				Context.session.currentFlag=dataflag.getFlag();
			    List<SYSTEMBean> systems=SYSTEM.getSystems(appID, dataflag.getFlag());
				for(SYSTEMBean system:systems){
					TreeItem  treeItemSystem=new TreeItem(treeItemApp,SWT.NONE);
					treeItemSystem.setText(system.getName());
					treeItemSystem.setImage(Icons.getTagFloderIcon());
					treeItemSystem.setData(system);
					treeItemSystem.setData("$Type", "system");
					List<COMPONENTBean>   components=COMPONENT.getComponents(system.getBussID(), dataflag.getFlag());
					for(COMPONENTBean component:components){
						TreeItem  treeItemComponent=new TreeItem(treeItemSystem,SWT.NONE);
						treeItemComponent.setText(component.getName());
						treeItemComponent.setImage(Icons.getTagFloderIcon());
						treeItemComponent.setData(component);
						treeItemComponent.setData("$Type", "component");
						List<NODEBean> nodes=NODE.getComponentNodes(component.getId(), dataflag.getFlag());
						for(NODEBean node:nodes){
							TreeItem  treeItemNode=new TreeItem(treeItemComponent,SWT.NONE);
							treeItemNode.setText(node.getCluster()+"_"+node.getIp()+"("+node.getName()+")");
							treeItemNode.setImage(Icons.getMidNodeIcon());
							treeItemNode.setData(node);
							treeItemNode.setData("$Type", "node");
							boolean selected=USERNODE.nodeSelected(node.getId(),Context.session.userID);
							treeItemNode.setChecked(selected);
							if(selected){
								OriginalNode.put(node.getId(), node);
							}
						}
						//treeItemComponent.setExpanded(true);
					}
					//treeItemSystem.setExpanded(true);
				}
				treeItemApp.setExpanded(true);
				treeRoot.setExpanded(true);
			}
		});
		
		
		
	}
	
	
	public class LoadAppTags extends SelectionAdapter{
 		public void widgetSelected(SelectionEvent e){
 			loadDirTree();
 		}
	}
	
	
	public class ChooseComponentAction extends SelectionAdapter{
 		public void widgetSelected(SelectionEvent e){
 			Context.session.inroll(this.getClass().toString(), "F-001-00003");
 			List<TreeItem> checkItems=new ArrayList<TreeItem>();
 			Map<String,String> selectedMap=new HashMap<String, String>();
 			TreeItem[] items=treeRoot.getItems();
 			if(items!=null&&items.length>0){
 				TreeItem itemApp=items[0];
 				TreeItem[] itemSystems=itemApp.getItems();
 				if(itemSystems!=null){
	 				for(TreeItem treeItem:itemSystems){
	 					TreeItem[] itemComponents=treeItem.getItems();
	 					if(itemComponents!=null){
	 						for(TreeItem itemComponent:itemComponents){
	 							TreeItem[] itemNodes=itemComponent.getItems();
	 							if(itemNodes!=null){
	 								for(TreeItem itemNode:itemNodes){
	 									if(itemNode.getChecked()){
	 										checkItems.add(itemNode);
	 									    NODEBean data=(NODEBean)itemNode.getData();
	 									    if(data!=null)
	 									    	selectedMap.put(data.getId(), data.getId());
	 									}
	 								}
	 							}
	 						}
	 					}
	 				}
 				}
 			}
 			
 			List<NODEBean> deleteNodes=new ArrayList<NODEBean>();
 			//有方向取消的
				for(String nodeID:OriginalNode.keySet()){
					if(!selectedMap.containsKey(nodeID)){
						USERNODE.delete(Context.session.userID, nodeID);
						deleteNodes.add(OriginalNode.get(nodeID));
					}
				}
				if(deleteNodes!=null&&deleteNodes.size()>0){
					for(NODEBean node:deleteNodes){
						OriginalNode.remove(node);
					}
				}
				
 			if(checkItems.size()>0){
 				//本地个人的所有节点设置为不选择
 				for(TreeItem nodeItem:checkItems){
 					NODEBean data=(NODEBean)nodeItem.getData();
 					/*String itemText=nodeItem.getText();
 					String cluster=itemText.substring(0, itemText.indexOf("_"));
 					String temp=itemText.substring(itemText.indexOf("_")+1);
 					String ip=temp.substring(0,temp.indexOf("("));
 					data.selected="1";
 					data.setIpAddress(ip);//ip地址可能发生改变
 					data.checkRealNode2DB();*/
 					boolean exist=USERNODE.nodeSelected(data.getId(), Context.session.userID);
 					if(!exist){
 						USERNODE.addUserNode(Context.session.userID, data.getId(), data.getStatus(), data.getSeq(), data.getType(),data.getSchFlag(),data.getOs());
 						OriginalNode.put(data.getId(), data);
 					}
 				}
 				
 				Logger.getInstance().logServer("应用["+comboApp.getText()+"]部署组件筛选确认");
 				String msg="筛选实际的部署节点成功！";
				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(msg);
				box.open();
 				
 			}else{
 				String msg="请选择需要实际部署的机器！";
				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
				box.setText(Constants.getStringVaule("messagebox_alert"));
				box.setMessage(msg);
				box.open();
 			}
 			
 		}
	}
	
	public class CheckAction extends SelectionAdapter{
 		public void widgetSelected(SelectionEvent e){
 			TreeItem item=(TreeItem)e.item;
 			String type=(String)item.getData("$Type");
			 if("component".equals(type)){		
				boolean checked=item.getChecked();
				TreeItem[] items=item.getItems();
				if(items!=null){
					for(TreeItem node:items){
						node.setChecked(checked);
					}
				}
				item.setChecked(false);
			}
			 else{
				 if(!"node".equals(type)){
					item.setChecked(false);
			    }
			 }
 		}
	}
	
	
	 public class PopMenuAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){}
	 		public void mouseDoubleClick(MouseEvent e){
	 			TreeItem currentItem=tagTree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null){
	 				String type=(String)currentItem.getData("$Type");
	 				if("node".equals(type)){
	 					NODEBean data=(NODEBean)currentItem.getData();
	 					 if(data!=null){
	 	 					Control oldControl=editor.getEditor();
	 	 					if(oldControl!=null){
	 	 						oldControl.dispose();
	 	 					}
		 					Text newEditor=new Text(tagTree,SWT.NONE);
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
			 						/*	boolean result=data.updateName(data.getParentID(),data.getId(), text.getText());
			 							if(result){
			 								Control oldControl=editor.getEditor();
			 			 					if(oldControl!=null){
			 			 						oldControl.dispose();
			 			 					}
			 							}else{
			 								String msg="目录["+text.getText()+"]已存在！请重新命名！";
			 								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
			 								box.setText(Constants.getStringVaule("messagebox_alert"));
			 								box.setMessage(msg);
			 								box.open();
			 							}*/
			 						}
			 						public void focusGained(FocusEvent e) {}
			 					});
			      			
			      			newEditor.setFocus();
			      			editor.setEditor(newEditor, currentItem);
			 	 				}
			 				} 
	 			}
	 		}
	  }

}
