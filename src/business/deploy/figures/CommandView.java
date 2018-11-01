package business.deploy.figures;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import bean.NODEBean;
import bean.SYSTEMBean;

import utils.LayoutUtils;
import views.AppView;

public class CommandView{

	
private  Composite content=null;
private   CTabFolder tabFloder=null;
public  CTabFolder getTabFloder(){
	return tabFloder;
}

public CommandView(Composite com,SYSTEMBean group){
	this.group=group;
	content=new Composite(com,SWT.NONE);
	content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
	tabFloder=new CTabFolder(content,SWT.TOP|SWT.BORDER);//|SWT.CLOSE|
	tabFloder.setLayoutData(LayoutUtils.getCommomGridData());
	tabFloder.setMaximizeVisible(true);
	tabFloder.setMinimizeVisible(true);  
	tabFloder.setSimple(false);
	tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
	tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
	tabFloder.setTabHeight(20);
	tabFloder.pack();
	content.pack();
	
}

public  void  setTabItems(Composite com,String name,Object data,Object control){	    
    	 CTabItem new_item=new CTabItem(tabFloder,SWT.NONE);
    	 new_item.setText(name);
    	 new_item.setControl(com);
    	 new_item.setData(data);
    	 new_item.setData("Composite", control);
    	 com.pack();
    	 com.layout(true);
    	 tabFloder.setSelection(new_item);
    	 tabFloder.layout(true);
}

public  void  setTabItems(Composite com,String name,String tips,Object data,Object control){	    
	 CTabItem new_item=new CTabItem(tabFloder,SWT.NONE);
	 new_item.setText(name);
	 new_item.setControl(com);
	 new_item.setToolTipText(tips);
	 new_item.setData(data);
	 new_item.setData("Composite", control);
	 com.pack();
	 com.layout(true);
	 tabFloder.setSelection(new_item);
	 tabFloder.layout(true);
}

/*public boolean existTab(String tabName){
	CTabItem[] items=tabFloder.getItems();
	for(int w=0;w<items.length;w++){
		CTabItem item=items[w];
		if(item.getText().equals(tabName))
			return true;
	}
	return false;
}*/

public  boolean getTabState(String item){
	CTabItem[] items=tabFloder.getItems();
	for(int k=0;k<items.length;k++){
		if(item.equals(items[k].getText())){ //先设置为选中
		  tabFloder.setSelection(items[k]);    
		  return true;
		}
	}
	return false;
}

public void updateTabContent(String id,String text){
	CTabItem[] items=tabFloder.getItems();
	for(int k=0;k<items.length;k++){
		NODEBean node=(NODEBean)items[k].getData();
		if(node.getId().equals(id)){
			ConsoleView console=(ConsoleView)items[k].getData("Composite");
			console.setConsoleText(text);
		}
		
	}
}

private SYSTEMBean group;

}
