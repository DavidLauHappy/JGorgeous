package business.aduitor.view;

import java.util.List;

import model.DATAFLAG;
import model.SYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;

import utils.LayoutUtils;
import bean.DATAFLAGBean;
import bean.SYSTEMBean;

public class AppSystemView  extends Composite {

	public static AppSystemView getInstance(Composite parent){
		if(unique_instance==null){
			unique_instance=new AppSystemView(parent);
		}
		return unique_instance;
	}
	
	private AppSystemView(Composite parent){
		super(parent,SWT.NONE);
		 content=this;
		 this.createTree();
	}
	 private void createTree(){
		 DATAFLAGBean dataflag=DATAFLAG.getByID(Constants.dataFlagCmdb);
		 Context.session.currentFlag=dataflag.getFlag();
		 appTree=new Tree(content,SWT.MULTI);//
		 //appTree.addMouseListener(new ShowUserAction());
		 appTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 
		 List<Item> apps=Dictionary.getDictionaryList("APP");
		 if(apps!=null&&apps.size()>0){
			 for(Item app:apps){
				 	TreeItem  treeApp=new TreeItem(appTree,SWT.SINGLE);
				 	treeApp.setText(app.getValue());
				 	treeApp.setImage(Icons.getFloderIcon());
				 	treeApp.setData(app);
				 	treeApp.setData("$Type", "app");
				 	List<SYSTEMBean> appSys=SYSTEM.getSystems(app.getKey(), Context.session.currentFlag);
				 	if(appSys!=null&&appSys.size()>0){
				 		for(SYSTEMBean sys:appSys){
				 			TreeItem  treeSys=new TreeItem(treeApp,SWT.SINGLE);
				 			treeSys.setText(sys.getName());
				 			treeSys.setImage(Icons.getTagFloderIcon());
				 			treeSys.setData(sys);
				 			treeSys.setData("$Type", "system");
				 		}
				 	}
			 }
		 }
		 
	 }
	 
	 private Tree appTree;
	 private Composite content=null;
	 private static AppSystemView unique_instance;
}
