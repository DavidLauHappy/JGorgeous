package resource;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import utils.LayoutUtils;
import views.AppView;

/**
 * @author Administrator
 *  我的审批页面
 *  目前只有开发任务的延期审批
 */
public class MyApproveView {
	
	private Composite parent=null;
	public Composite content=null;
	private  CTabFolder tabFloder=null;
	
	public MyApproveView(Composite parent){
		this.parent=parent;
	    this.createAndShow();
	}
	
	public void createAndShow(){
		content=new Composite(parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
		tabFloder=new CTabFolder(content,SWT.TOP);//|SWT.CLOSE|
		tabFloder.setLayoutData(LayoutUtils.getCommomGridData());
		tabFloder.setLayout(LayoutUtils.getComGridLayout(1, 1));
		tabFloder.setMaximizeVisible(true);
		tabFloder.setMinimizeVisible(true);  
		tabFloder.setSimple(false);
		tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		tabFloder.setTabHeight(20);
		
		CTabItem itemMyTask=new CTabItem(tabFloder,SWT.NONE);
		itemMyTask.setText(Constants.getStringVaule("tabItem_mytask"));
		MyApprTaskView taskView=new MyApprTaskView(tabFloder);
		taskView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		taskView.pack();
		itemMyTask.setControl(taskView);
		
		CTabItem itemMyDone=new CTabItem(tabFloder,SWT.NONE);
		itemMyDone.setText(Constants.getStringVaule("tabItem_mydone"));
		MyApprDoneView doneView=new MyApprDoneView(tabFloder);
		doneView.setLayout(LayoutUtils.getComGridLayout(1, 0));
		doneView.pack();
		itemMyDone.setControl(doneView);
		
		tabFloder.setSelection(itemMyTask);
		tabFloder.pack();
		content.pack();
	}
	
	
}
