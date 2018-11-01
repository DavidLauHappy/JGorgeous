package views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;


import utils.LayoutUtils;

public class StatusBarView {
	 private static StatusBarView uniqueInstance=null;
	 public static StatusBarView getInstance(Composite com){
		 if(uniqueInstance==null)
			 uniqueInstance=new StatusBarView(com);
		 return uniqueInstance;
	 }
	 
	 private Composite parent;
	 private Composite selfContent;
	 private ProgressBar progressBar;
	 private Text status;
	 
	 
	 private StatusBarView(Composite com){
		 this.parent=com;
		 this.create();
	 }
	 
	 private void create(){
		 selfContent=new Composite(parent,SWT.NONE);
		 selfContent.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 18));
		 selfContent.setLayout(LayoutUtils.getComGridLayout(3, 3));
		  status=new Text(selfContent,SWT.NONE|SWT.RIGHT);
		  status.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
		  progressBar = new ProgressBar(selfContent, SWT.SMOOTH);
		  progressBar.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		  progressBar.setMinimum(0); // 最小值
          progressBar.setMaximum(100);// 最大值
		 selfContent.pack();
		 selfContent.setVisible(false);
	 }
	 

	public Composite getSelfContent() {
		return selfContent;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public Text getStatus() {
		return status;
	}
	 
	 
}
