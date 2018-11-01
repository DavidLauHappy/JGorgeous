package business.deploy.figures;

import java.util.List;

import model.PKG;
import model.STEP;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import bean.PKGBean;
import bean.STEPBean;

import resource.Constants;
import resource.Context;
import resource.Icons;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

/**
 * @author David
 * 参数来实现自动配置
 * 项目显示和修改配置结果
 */
public class BackupCfgView {
	private  Shell shell=null;
	private Point position=null;
	private String versionID;
	public BackupCfgView(String versionID,Point position){
		this.versionID=versionID;
		this.position=position;
	}
	

	
	public void showPanel(){
		shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		shell.setText(Constants.getStringVaule("window_backupcfg"));
		shell.setLocation(this.position);
		shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
		   Composite content=new Composite(shell,SWT.NONE);
		   content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 600, 300));
		   content.setLayout(LayoutUtils.getComGridLayout(2, 0));
		  this.createDirPanel(content);
		  this.createScriptPanel(content);
		   content.pack();
		shell.pack();
		shell.open();
		shell.addShellListener(new ShellCloseAction());
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			//刷新视图侧的树
			StepView.getInstance(null).refreshVersionTree(versionID);
			shell.dispose();
		}
	}
	
	private void createDirPanel(Composite parent){
		Group dirPanel=new Group(parent,SWT.NONE);
		dirPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		dirPanel.setLayout(LayoutUtils.getComGridLayout(1, 0));
		dirPanel.setText(Constants.getStringVaule("group_backupdir"));
		Tree stepTree=new Tree(dirPanel,SWT.SINGLE|SWT.CHECK);//;
		stepTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		stepTree.addSelectionListener(new BackupAction());
		PKGBean version=PKG.getPkg(versionID);
	   	 TreeItem  treeRoot=new TreeItem(stepTree,SWT.CHECK|SWT.MULTI);
	   	 treeRoot.setText(version.getId());
	   	 treeRoot.setImage(Icons.getFloderIcon());
	   	 treeRoot.setData(version);
	   	 treeRoot.setData("$Type", STEPBean.Type.Pkg);

	   	 List<STEPBean> steps=STEP.getStepsByAction(this.versionID, STEPBean.ActionType.FileCopy.ordinal()+"");
		   	for(int w=0;w<steps.size();w++){
	  		  TreeItem  workflowStep=new TreeItem(treeRoot,SWT.NONE);
	  		  workflowStep.setText(steps.get(w).getDesc());
	  		  workflowStep.setImage(Icons.getFloderIcon());
	  		  workflowStep.setData("$Type", STEPBean.Type.Step);
	  		  workflowStep.setData(steps.get(w));
		  		  /*if(steps.get(w).getBackupFlag().equals("1")){
		  			workflowStep.setChecked(true);
		  			//treeRoot.setChecked(true);
		  		  }else{
		  			workflowStep.setChecked(false);
		  		  }*/
	  	    }
  	        treeRoot.setExpanded(true);
		stepTree.pack();
	
		dirPanel.pack();
	}
	
	
	private void createScriptPanel(Composite parent){
		Group dbPanel=new Group(parent,SWT.NONE);
		dbPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		dbPanel.setLayout(LayoutUtils.getComGridLayout(1, 0));
		dbPanel.setText(Constants.getStringVaule("group_backupdb"));
		Tree stepTree=new Tree(dbPanel,SWT.SINGLE|SWT.CHECK);//;
		stepTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		stepTree.addSelectionListener(new BackupDbAction());
		
		 PKGBean version=PKG.getPkg(versionID);
	   	 TreeItem  treeRoot=new TreeItem(stepTree,SWT.CHECK|SWT.MULTI);
	   	 treeRoot.setText(version.getId());
	   	 treeRoot.setImage(Icons.getFloderIcon());
	   	 treeRoot.setData(version);
	   	 treeRoot.setData("$Type", STEPBean.Type.Pkg);
	 	 List<STEPBean> steps=STEP.getStepsByAction(this.versionID, STEPBean.ActionType.ScriptInstall.ordinal()+"");
		   	for(int w=0;w<steps.size();w++){
	  		  TreeItem  workflowStep=new TreeItem(treeRoot,SWT.NONE);
	  		  workflowStep.setText(steps.get(w).getDesc());
	  		  workflowStep.setImage(Icons.getFloderIcon());
	  		  workflowStep.setData(steps.get(w));
	  		  workflowStep.setData("$Type", STEPBean.Type.Step);
		  		  if(steps.get(w).getBackupFlag().equals("1")){
		  			workflowStep.setChecked(true);
		  			//treeRoot.setChecked(true);
		  		  }else{
		  			workflowStep.setChecked(false);
		  		  }
	  	    }
 	        treeRoot.setExpanded(true);
 	      
		stepTree.pack();
		dbPanel.pack();
	}
	
	
	 public class BackupDbAction extends SelectionAdapter{
	    public void widgetSelected(SelectionEvent e){
	    	   TreeItem currentItem=(TreeItem)e.item;
	    	   STEPBean.Type type=(STEPBean.Type)currentItem.getData("$Type");
	 			if( STEPBean.Type.Step.equals(type)){
	 				STEPBean step=(STEPBean)currentItem.getData();
	 				if(currentItem.getChecked()){
	 					step.setBackupFlag("1");
	 					String name=step.getName();
	 					String desc="备份["+name+"]";
	 					STEPBean exist=STEP.getPkgSteps(versionID, step.getParentid());
	 					if(exist==null){
	 						String existID=STEP.eixst(versionID, desc);
	 						if(StringUtil.isNullOrEmpty(existID)){
	 							existID=STEP.getID(versionID);
	 							String action="";
			 					if(step.getAction().equals(STEPBean.ActionType.FileCopy.ordinal()+"")){
			 						action=STEPBean.ActionType.BackDir.ordinal()+"";
			 					}
			 					if(step.getAction().equals(STEPBean.ActionType.ScriptInstall.ordinal()+"")){
			 						action=STEPBean.ActionType.BackRun.ordinal()+"";
			 					}
			 					STEPBean backupStep=new STEPBean(versionID,existID,name,desc,action,"0","0","0",Context.session.userID);
			 					backupStep.inroll();
	 						}
		 					step.setParentid(existID);
	 					}else{
	 						//根据备份步骤名称判断有备份，有备份修改
	 						step.setParentid(exist.getId());
	 					}
	 				}else{
		 					STEPBean exist=STEP.getPkgSteps(versionID, step.getParentid());
		 					if(exist!=null){
		 						exist.delete();
		 					}
		 					String id=step.getParentid();
		 					STEP.deleteByID(versionID, id);
		 					step.setBackupFlag("0");
		 					step.setParentid("0");
	 				}
	 			}else{
	 				TreeItem[] items=currentItem.getItems();
	 				if(items!=null&&items.length>0){
	 					for(TreeItem item:items){
	 						STEPBean step=(STEPBean)item.getData();
	 						if(currentItem.getChecked()){
	 							step.setBackupFlag("1");
	 							String name=step.getName();
			 					String desc="备份["+name+"]";
	 							STEPBean exist=STEP.getPkgSteps(versionID, step.getParentid());
			 					if(exist==null){
		 							String existID=STEP.eixst(versionID, desc);
			 						if(StringUtil.isNullOrEmpty(existID)){
			 							existID=STEP.getID(versionID);
					 					String action="";
					 					if(step.getAction().equals(STEPBean.ActionType.FileCopy.ordinal()+"")){
					 						action=STEPBean.ActionType.BackDir.ordinal()+"";
					 					}
					 					if(step.getAction().equals(STEPBean.ActionType.ScriptInstall.ordinal()+"")){
					 						action=STEPBean.ActionType.BackRun.ordinal()+"";
					 					}
					 					STEPBean backupStep=new STEPBean(versionID,existID,name,desc,action,"0","0","0",Context.session.userID);
					 					backupStep.inroll();
			 						}
				 					step.setParentid(existID);
				 					item.setData(step);
			 					}
			 					item.setChecked(true);
	 						}else{
	 							item.setChecked(false);
	 							STEPBean exist=STEP.getPkgSteps(versionID, step.getParentid());
	 							if(exist!=null){
			 						exist.delete();
			 					}
	 							String id=step.getParentid();
			 					STEP.deleteByID(versionID, id);
	 							step.setBackupFlag("0");
			 					step.setParentid("0");
			 					item.setData(step);
	 						}
	 					}
	 				}
	 			}
	    }
 }
	
	 public class BackupAction extends SelectionAdapter{
		    public void widgetSelected(SelectionEvent e){
		    	   TreeItem currentItem=(TreeItem)e.item;
		    	   STEPBean.Type type=(STEPBean.Type)currentItem.getData("$Type");
		 			if( STEPBean.Type.Step.equals(type)){
		 				STEPBean step=(STEPBean)currentItem.getData();
				 				if(currentItem.getChecked()){
				 					step.setBackupFlag("1");
				 					String name=step.getName();
				 					String desc="备份["+name+"]";
				 					STEPBean exist=STEP.getPkgSteps(versionID, step.getParentid());
				 					if(exist==null){
				 						String existID=STEP.eixst(versionID, desc);
				 						if(StringUtil.isNullOrEmpty(existID)){
				 							existID=STEP.getID(versionID);
				 							String action="";
						 					if(step.getAction().equals(STEPBean.ActionType.FileCopy.ordinal()+"")){
						 						action=STEPBean.ActionType.BackDir.ordinal()+"";
						 					}
						 					if(step.getAction().equals(STEPBean.ActionType.ScriptInstall.ordinal()+"")){
						 						action=STEPBean.ActionType.BackRun.ordinal()+"";
						 					}
						 					STEPBean backupStep=new STEPBean(versionID,existID,name,desc,action,"0","0","0",Context.session.userID);
						 					backupStep.inroll();
				 						}
					 					step.setParentid(existID);
				 					}else{
				 						//根据备份步骤名称判断有备份，有备份修改
				 						step.setParentid(exist.getId());
				 					}
				 				}else{
			 						  STEPBean exist=STEP.getPkgSteps(versionID, step.getParentid());
					 					if(exist!=null){
					 						exist.delete();
					 					}
					 					String id=step.getParentid();
					 					STEP.deleteByID(versionID, id);
					 					step.setBackupFlag("0");
					 					step.setParentid("0");
		 				}
		 			}else{
		 				TreeItem[] items=currentItem.getItems();
		 				if(items!=null&&items.length>0){
		 					for(TreeItem item:items){
		 						STEPBean step=(STEPBean)item.getData();
		 						if(currentItem.getChecked()){
		 							step.setBackupFlag("1");
		 							String name=step.getName();
				 					String desc="备份["+name+"]";
		 							STEPBean exist=STEP.getPkgSteps(versionID, step.getParentid());
				 					if(exist==null){
			 							String existID=STEP.eixst(versionID, desc);
				 						if(StringUtil.isNullOrEmpty(existID)){
				 							existID=STEP.getID(versionID);
						 					String action="";
						 					if(step.getAction().equals(STEPBean.ActionType.FileCopy.ordinal()+"")){
						 						action=STEPBean.ActionType.BackDir.ordinal()+"";
						 					}
						 					if(step.getAction().equals(STEPBean.ActionType.ScriptInstall.ordinal()+"")){
						 						action=STEPBean.ActionType.BackRun.ordinal()+"";
						 					}
						 					STEPBean backupStep=new STEPBean(versionID,existID,name,desc,action,"0","0","0",Context.session.userID);
						 					backupStep.inroll();
				 						}
					 					step.setParentid(existID);
					 					item.setData(step);
				 					}
				 					item.setChecked(true);
		 						}else{
		 							   if("1".equals(step.getBackupFlag())){
		 								  item.setChecked(true);
		 							   }else{
			 							   item.setChecked(false);
				 							STEPBean exist=STEP.getPkgSteps(versionID, step.getParentid());
				 							if(exist!=null){
						 						exist.delete();
						 					}
				 							String id=step.getParentid();
						 					STEP.deleteByID(versionID, id);
				 							step.setBackupFlag("0");
						 					step.setParentid("0");
						 					item.setData(step);
			 							}
		 							}
		 					   }
		 					}
		 			}
		    }
	 }
}
