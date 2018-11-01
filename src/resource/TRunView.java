package resource;

import java.util.ArrayList;
import java.util.List;

import model.LOCALCOMMAND;
import model.LOCALNODE;

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

import common.core.ScheduleService;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;
import business.deploy.figures.RunView.CheckAction;

public class TRunView {
	private String versionID;
	private LOCALNODEBean data=null;
	private  Shell Guide=null;
	private Point position=null;
	private Tree stepTree;
	
	public void setData(LOCALNODEBean data){
		this.data=data;
	}
	public void setPosition(Point position){
		this.position=position;
	}
	
	public void setVersion(String ver){
		this.versionID=ver;
	}
	public  void show(){
		Guide=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL|SWT.RESIZE);
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
		
		 stepTree=new Tree(pannel,SWT.BORDER|SWT.SINGLE|SWT.CHECK);//
		 stepTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 6, 1, 0, 0));
		 stepTree.addSelectionListener(new CheckAction());
		 List<LOCALCOMMANDBean> cmds=LOCALCOMMAND.getCommand(versionID, data.getId());
		 if(cmds!=null&&cmds.size()>0){
			 for(LOCALCOMMANDBean cmd:cmds){
				 TreeItem  treeitem=new TreeItem(stepTree,SWT.CHECK);
    			 treeitem.setText(cmd.getCmdName());
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
			}else{
				for(TreeItem item:selected){
					LOCALCOMMANDBean cmd=(LOCALCOMMANDBean)item.getData();
					cmd.resetStatus(LOCALCOMMANDBean.Status.Runnable.ordinal()+"");
					ScheduleService.getInstance().rework(cmd.getVersionID(), cmd.getNodeID());
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
				box.setMessage(Constants.getStringVaule("alert_select_zh"));
				box.open();	
				return;
			}else{
				for(TreeItem item:selected){
					LOCALCOMMANDBean cmd=(LOCALCOMMANDBean)item.getData();
					cmd.resetStatus(LOCALCOMMANDBean.Status.ReturnOK.ordinal()+"");
					ScheduleService.getInstance().rework(cmd.getVersionID(), cmd.getNodeID());
					 String statistic=LOCALCOMMAND.getInstallProgress(cmd.getVersionID(), cmd.getNodeID());
					  if(!StringUtil.isNullOrEmpty(statistic)){
						  int max=Integer.parseInt(statistic.split("\\|")[0]);
							int cur=Integer.parseInt(statistic.split("\\|")[1]);
							if(cur>0){
								if(max==cur){
									 LOCALNODEBean node =LOCALNODE.getNode(cmd.getNodeID());
									 node.reSetStatus(LOCALNODEBean.Status.Done.ordinal()+"");
								}
							}
					  }
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
			LOCALCOMMANDBean cmd=(LOCALCOMMANDBean)currentItem.getData();
			if(!(LOCALCOMMANDBean.Status.ReturnFailed.ordinal()+"").equals(cmd.getStatus())&&
			  !(LOCALCOMMANDBean.Status.TimeOut.ordinal()+"").equals(cmd.getStatus())){
				currentItem.setChecked(false);
			}
		}
	}
	
}
