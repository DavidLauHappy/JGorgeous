package business.admin.view;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.COMPONENT;
import model.DATAFLAG;
import model.NODE;
import model.SYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import bean.COMPONENTBean;
import bean.DATAFLAGBean;
import bean.NODEBean;
import bean.SYSTEMBean;
import business.admin.core.HttpLoader;
import business.deploy.figures.TagManageView.CheckAction;
import business.deploy.figures.TagManageView.LoadAppTags;
import business.deploy.figures.TagManageView.PopMenuAction;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;

import utils.LayoutUtils;
import views.AppView;

public class NodeManagerView {
	private Composite parent;
	public Composite content;
	public Tree tagTree;
	public TreeItem  treeRoot;
	public  TreeEditor editor;
	public TreeItem currentItem;
	public Combo comboApp;
	public ExecutorService threadPool;
	public NodeManagerView(Composite com){
		this.parent=com;
		this.createAndShow();
		threadPool=Executors.newFixedThreadPool(1);
		HttpLoader loader=new HttpLoader();
		threadPool.execute(loader);
		threadPool.shutdown();
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
		comboApp.addSelectionListener(new LoadAppNodes());
		List<Item> Apps=Dictionary.getDictionaryList("APP");
		String[] items=new String[Apps.size()];
		for(int w=0;w<Apps.size();w++){
			Item item=Apps.get(w);
			String key=item.getKey();
			String value=item.getValue();
			items[w]=key+" "+value;
		}
		comboApp.setItems(items);
    	comboApp.select(0);
    	
    	tagTree=new Tree(content,SWT.BORDER|SWT.MULTI|SWT.CHECK);
		tagTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	    treeRoot=new TreeItem(tagTree,SWT.NONE);
   	 	treeRoot.setText("¸ù½Úµã");
   	 	treeRoot.setData("$Type", "root");
   	 	treeRoot.setImage(Icons.getTagFloderIcon());
   	 	tagTree.pack();
   	 	content.pack();
	}
	
	public class LoadAppNodes extends SelectionAdapter{
 		public void widgetSelected(SelectionEvent e){
 			treeRoot.removeAll();
 			AppView.getInstance().getDisplay().asyncExec(new Runnable() {
 				public void run() {
					 String rawAppName=comboApp.getText();
					 String appID=rawAppName.substring(0, rawAppName.indexOf(" "));
					 String appName=rawAppName.substring( rawAppName.indexOf(" ")+1);
 					TreeItem  treeItemApp=new TreeItem(treeRoot,SWT.NONE);
 					treeItemApp.setText(appName+"("+appID+")");
 					treeItemApp.setImage(Icons.getTagFloderIcon());
 					treeItemApp.setData("$Type", "app");
 					DATAFLAGBean dataflag=DATAFLAG.getByID(Constants.dataFlagCmdb);
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
 							}
 							treeItemComponent.setExpanded(true);
 						}
 						treeItemSystem.setExpanded(true);
 					}
 					treeItemApp.setExpanded(true);
 					treeRoot.setExpanded(true);
 				}
 			});
 		}
	}
}
