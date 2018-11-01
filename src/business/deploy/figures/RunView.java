package business.deploy.figures;

import java.util.ArrayList;
import java.util.List;

import model.NODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import resource.Constants;
import resource.Context;
import resource.Icons;
import utils.LayoutUtils;
import views.AppView;

import bean.COMMANDBean;
import bean.NODEBean;
import business.deploy.core.PkgNodeScheduler;
/**
 * @author DavidLau
 * @date 2016-8-11
 * @version 1.0
 * 类说明
 */
public class RunView {

	public void setData(NODEBean data){
		this.data=data;
	}
	public void setPosition(Point position){
		this.position=position;
	}
	
	
	private  Shell Guide=null;
	private Point position=null;
	private NODEBean data=null;
	private Tree stepTree;
	public  void show()
	{
		Guide=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		Guide.setText(Constants.getStringVaule("window_schedule"));
		Guide.setLocation(this.position);
		Guide.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
		Composite pannel=new Composite(Guide,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 450, 400));
		pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		
			Composite toolPanel=new Composite(pannel,SWT.NONE);
			toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 6, 1, 0, 0));
			toolPanel.setLayout(LayoutUtils.getComGridLayout(2, 1));
				Button btnRedo=new Button(toolPanel,SWT.PUSH);
				btnRedo.setText(Constants.getStringVaule("btn_redo"));
				btnRedo.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
				btnRedo.addSelectionListener(new RedoAction());
				Button btnSkip=new Button(toolPanel,SWT.PUSH);
				btnSkip.setText(Constants.getStringVaule("btn_skip"));
				btnSkip.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
				btnSkip.addSelectionListener(new SkipAction());
			toolPanel.pack();
			
		/*	ScrolledComposite	scoll=new ScrolledComposite(pannel,SWT.V_SCROLL|SWT.BORDER);
			scoll.setAlwaysShowScrollBars(true);
			scoll.setExpandVertical(true);
			scoll.setMinSize(300,Constants.sizeCanvasHeigth);
			scoll.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			scoll.setLayout(LayoutUtils.getComGridLayout(1, 1));*/
			 stepTree=new Tree(pannel,SWT.BORDER|SWT.SINGLE|SWT.CHECK);//
			 stepTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 6, 1, 0, 0));
			 stepTree.addSelectionListener(new CheckAction());
	    	 List<COMMANDBean> cmds= NODE.getNodeCommand(Context.CurrentVersionID, data.getId());
	    	 if(cmds!=null&&cmds.size()>0){
	    		 for(COMMANDBean cmd:cmds){
	    			 TreeItem  treeitem=new TreeItem(stepTree,SWT.CHECK);
	    			 treeitem.setText(cmd.getName());
	    			 treeitem.setImage(Icons.getStepIcon(cmd.getStatus()));
	    	    	 treeitem.setData(cmd);
	    		 }
	    	 }
	    	 stepTree.pack();
	    	 pannel.pack();
	    	 Guide.pack();
	    	 Guide.open();
	    	 Guide.addShellListener(new ShellCloseAction());
	 	}
	 	
	 	public class ShellCloseAction extends ShellAdapter{
	 		public void shellClosed(ShellEvent e){	
	 			Guide.dispose();
	 		}	
	 	}
	 	
		public class RedoAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				TreeItem[] items=stepTree.getItems();
				List<TreeItem> selected=new ArrayList<TreeItem>();
				for(int w=0;w<items.length;w++){
					if(items[w].getChecked()){
						selected.add(items[w]);
					}
				}
				if(selected.size()<=0){
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_select_zh"));
					box.open();	
					return;
				}
				else{
					for(TreeItem treeitem:selected){
						COMMANDBean cmd=(COMMANDBean)treeitem.getData();
						//要唤醒本地工作线程重做，需要该本地命令状态
						cmd.setStatus(COMMANDBean.Status.Runnable.ordinal()+"");
						PkgNodeScheduler.getInstance().resumeWork();
					}
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_successoperate"));
					int choose=box.open();	
					if(choose==SWT.OK){
						Guide.dispose();
					}
				}
			}
		}
		public class SkipAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				TreeItem[] items=stepTree.getItems();
				List<TreeItem> selected=new ArrayList<TreeItem>();
				for(int w=0;w<items.length;w++){
					if(items[w].getChecked()){
						selected.add(items[w]);
					}
				}
				if(selected.size()<=0){
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_select"));
					box.open();	
					return;
				}
				else{
					for(TreeItem treeitem:selected){
						COMMANDBean cmd=(COMMANDBean)treeitem.getData();
							 //要唤醒本地工作线程续作
							cmd.setStatus(COMMANDBean.Status.ReturnOK.ordinal()+"");
							//data.setSchFlag(NODEBean.Flag.ScheduleAble.ordinal()+"");
							PkgNodeScheduler.getInstance().resumeWork();
						}
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(Constants.getStringVaule("alert_successoperate"));
					int choose=box.open();	
					if(choose==SWT.OK){
						Guide.dispose();
					}
				}
			}
		}
		public class CheckAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				TreeItem currentItem=(TreeItem)e.item;
				COMMANDBean cmd=(COMMANDBean)currentItem.getData();
				//执行错误的可以重做/续作
				//代理端退出后，发送超时的指令可以重做
				if(!cmd.getStatus().equals(COMMANDBean.Status.ReturnFailed.ordinal()+"")&&
				   !cmd.getStatus().equals(COMMANDBean.Status.TimeOut.ordinal()+"")	){
					currentItem.setChecked(false);
				}
			}
		}
}
