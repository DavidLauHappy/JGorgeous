package business.deploy.figures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.STEP;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;
import bean.STEPBean;

public class ServiceCfgView {
	private  Shell shell=null;
	private Point position=null;
	private String versionID;
	public Combo startDir;
	public Combo stopDir;
	public Tree svcTree;
	public Map<String, STEPBean> DirMap;
	public ServiceCfgView(String versionID,Point position){
		this.versionID=versionID;
		this.position=position;
		DirMap=new HashMap<String, STEPBean>();
	}
	
	public void showPanel(){
		shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		shell.setText(Constants.getStringVaule("window_servicecfg"));
		shell.setLocation(this.position);
		shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
		   Composite content=new Composite(shell,SWT.NONE);
		   content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 600, 300));
		   content.setLayout(LayoutUtils.getComGridLayout(2, 0));
		  this.createServicePanel(content);
		  this.createDirPanel(content);
		   content.pack();
		shell.pack();
		shell.open();
		shell.addShellListener(new ShellCloseAction());
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			//对版本数据进行排序
			//刷新视图侧的树
			StepView.getInstance(null).refreshVersionTree(versionID);
			shell.dispose();
		}
	}
	
	private void createDirPanel(Composite parent){
		Group dirPanel=new Group(parent,SWT.NONE);
		dirPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		dirPanel.setLayout(LayoutUtils.getComGridLayout(1, 0));
		dirPanel.setText(Constants.getStringVaule("group_servicess"));
		
		Label labStartDir=new Label(dirPanel,SWT.NONE);
		labStartDir.setText(Constants.getStringVaule("label_svcstart"));
		labStartDir.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		startDir=new Combo(dirPanel,SWT.DROP_DOWN);
		startDir.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		List<STEPBean>  steps=STEP.getPkgSteps(this.versionID);
		if(steps!=null&&steps.size()>0){
			String[] items=new String[steps.size()];
			int i=0;
			for(STEPBean step:steps){
				items[i]=step.getDesc();
				i++;
				DirMap.put(step.getDesc(), step);
			}
			startDir.setItems(items);
		}
		
		Label labStopDir=new Label(dirPanel,SWT.NONE);
		labStopDir.setText(Constants.getStringVaule("label_svcstop"));
		labStopDir.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		stopDir=new Combo(dirPanel,SWT.DROP_DOWN);
		stopDir.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		if(steps!=null&&steps.size()>0){
			String[] items=new String[steps.size()];
			int i=0;
			for(STEPBean step:steps){
				items[i]=step.getDesc();
				i++;
			}
			stopDir.setItems(items);
		}
		
		Button btnSure=new Button(dirPanel,SWT.PUSH);
		btnSure.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.BEGINNING, true, false, 1, 1, 0, 0));
		btnSure.setText("   "+Constants.getStringVaule("btn_apply")+"   ");
		btnSure.addSelectionListener(new CfgServiceAction());
		dirPanel.pack();
	}
	
	private void createServicePanel(Composite parent){
		Group svcPanel=new Group(parent,SWT.NONE);
		svcPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		svcPanel.setLayout(LayoutUtils.getComGridLayout(1, 0));
		svcPanel.setText(Constants.getStringVaule("window_servicecfg"));
		 svcTree=new Tree(svcPanel,SWT.SINGLE|SWT.CHECK);//;
		 svcTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 svcTree.addSelectionListener(new PackDirAction());
		List<Item> Services=Dictionary.getDictionaryList("NODE_SERVICE.TYPE");
		if(Services!=null&&Services.size()>0){
			for(Item item:Services){
				TreeItem  treeRoot=new TreeItem(svcTree,SWT.CHECK|SWT.SINGLE);
			   	 treeRoot.setText(item.getValue());
			   	 treeRoot.setImage(Icons.getServiceIcon());
			   	 treeRoot.setData(item);
			}
		}
		svcPanel.pack();
	}
	
	 public class PackDirAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			TreeItem currentItem=(TreeItem)e.item;
	 			Item service=(Item)currentItem.getData();
	 			if(currentItem.getChecked()){
	 				List<STEPBean> steps=STEP.getMyServiceSteps(versionID, service.getKey(), Context.session.userID);
		 			if(steps!=null&&steps.size()>0){
		 				for(STEPBean step:steps){
		 					String action=step.getAction();
		 					if(action.equals(STEPBean.ActionType.ServiceStart.ordinal()+"")){
		 						String parentID=step.getParentid();
		 						STEPBean startStep=STEP.getPkgSteps(versionID, parentID);  
		 						if(startStep!=null){
			 						String[] items=startDir.getItems();
			 						for(int w=0;w<items.length;w++){
			 							if(items[w].equals(startStep.getDesc())){
			 								startDir.select(w);
			 							}
			 						}
		 						}
		 					}
		 					else if(action.equals(STEPBean.ActionType.ServiceStop.ordinal()+"")){
		 						String parentID=step.getParentid();
		 						STEPBean stopStep=STEP.getPkgSteps(versionID, parentID);  
		 						if(stopStep!=null){
		 						String[] items=stopDir.getItems();
			 						for(int w=0;w<items.length;w++){
			 							if(items[w].equals(stopStep.getDesc())){
			 								stopDir.select(w);
			 							}
			 						}
		 						}
		 					}	
		 				}
		 			}
	 			}else{
	 				startDir.setText("");
	 				stopDir.setText("");
	 			}
	 		}
	 }
	
	 public class CfgServiceAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			TreeItem[] items=svcTree.getItems();
	 			if(items!=null&&items.length>0){
	 			    for(TreeItem item:items){
	 			    	if(item.getChecked()){
	 			    		Item service=(Item)item.getData();
	 			    		String startName=startDir.getText();
	 			    		if(!StringUtil.isNullOrEmpty(startName)){
	 			    			STEPBean startDir=DirMap.get(startName);
	 			    			String desc="启动["+service.getValue()+"]";
	 			    			String existID=STEP.eixst(versionID, desc);
	 			    			if(!StringUtil.isNullOrEmpty(existID)){
	 			    				STEPBean  startSvc=STEP.getPkgSteps(versionID, existID);
	 			    				startSvc.setParentid(startDir.getId());
	 			    			}else{
	 			    				String newID=STEP.getID(versionID);
		 			    			STEPBean startSvc=new STEPBean(versionID,newID,service.getKey(),desc,STEPBean.ActionType.ServiceStart.ordinal()+"",startDir.getId(),"0","0",Context.session.userID);
		 			    			startSvc.inroll();
	 			    			}
	 			    		}
	 			    		String stopName=stopDir.getText();
	 			    		if(!StringUtil.isNullOrEmpty(stopName)){
	 			    			STEPBean stopDir=DirMap.get(stopName);
	 			    			String desc="停止["+service.getValue()+"]";
	 			    			String existID=STEP.eixst(versionID, desc);
	 			    			if(!StringUtil.isNullOrEmpty(existID)){
	 			    				STEPBean  stopSvc=STEP.getPkgSteps(versionID, existID);
	 			    				stopSvc.setParentid(stopDir.getId());
	 			    			}else{
		 			    			String newID=STEP.getID(versionID);
		 			    			STEPBean stopSvc=new STEPBean(versionID,newID,service.getKey(),desc,STEPBean.ActionType.ServiceStop.ordinal()+"",stopDir.getId(),"0","0",Context.session.userID);
		 			    			stopSvc.inroll();
	 			    			}
	 			    		}
	 			    		MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
	 						box.setText(Constants.getStringVaule("alert_successoperate"));
	 						box.setMessage("服务配置成功");
	 						box.open();		
	 			    	}
	 			    }
	 			   StepView.getInstance(null).refreshVersionTree(versionID);
	 			   shell.dispose(); 
	 			}else{
	 				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("请选择服务");
					box.open();	
	 			}
	 		}
	 }
	 
}
