package business.deploy.figures;

import java.util.ArrayList;
import java.util.List;

import model.PKG;
import model.STEP;

import org.eclipse.swt.SWT;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import bean.PKGBean;
import bean.STEPBean;


import resource.Constants;
import resource.Context;
import resource.Icons;
import resource.Logger;
import utils.DateUtil;
import utils.LayoutUtils;
import views.AppView;


public class StepView extends Composite{
     private static StepView unique_instance;
     public static StepView getInstance(Composite parent){
    	 if(unique_instance==null)
    		 if(parent!=null){
    			 unique_instance=new StepView(parent);
    		 }
    	 return unique_instance;
     }
     
     
     private Composite content=null;
     private StepView(Composite parent){
    	 super(parent,SWT.NONE);
    	 content=this;
    	 this.createToolPanel();
    	 this.createStepTree();
     }
     
     private void createToolPanel(){
    	    Composite toolPanel=new Composite(content,SWT.NONE);
			toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
			toolPanel.setLayout(LayoutUtils.getComGridLayout(7, 1));
			
			   Button btnBackup=new Button(toolPanel,SWT.PUSH);
			   btnBackup.setToolTipText(Constants.getStringVaule("btn_backup"));
			   btnBackup.setImage(Icons.getBackupIcon());
			   btnBackup.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
			   btnBackup.addSelectionListener(new BackupAction());
			   
			   
			   Button btnService=new Button(toolPanel,SWT.PUSH);
			   btnService.setToolTipText(Constants.getStringVaule("btn_service"));
			   btnService.setImage(Icons.getServiceIcon());
			   btnService.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
			   btnService.addSelectionListener(new ServiceCfgAction());
			   
			    Button btnUp=new Button(toolPanel,SWT.PUSH);
				btnUp.setToolTipText(Constants.getStringVaule("btn_up"));
				btnUp.setImage(Icons.getUpIcon());
				btnUp.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
				btnUp.addSelectionListener(new UpStepAction());
				
				Button btnDown=new Button(toolPanel,SWT.PUSH);
				btnDown.setToolTipText(Constants.getStringVaule("btn_down"));
				btnDown.setImage(Icons.getDownIcon());
				btnDown.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
				btnDown.addSelectionListener(new DownStepAction());
				
			    Button btnDel=new Button(toolPanel,SWT.PUSH);
				btnDel.setToolTipText(Constants.getStringVaule("btn_delete"));
				btnDel.setImage(Icons.getDeleteIcon());
				btnDel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
				btnDel.addSelectionListener(new DeleteStepAction());
				toolPanel.pack();
     }
     
     private Tree stepTree;
     private void createStepTree(){
    	 stepTree=new Tree(content,SWT.SINGLE|SWT.CHECK);//
    	 List<PKGBean> versions=PKG.getMyPkgs( Context.session.userID, PKGBean.Enable.Yes.ordinal()+"");
    	 if(versions!=null){
    		 for(int w=0;w<versions.size();w++){
    			 PKGBean version=versions.get(w);
    			 String versionID=version.getId();
    			 this.addTree(version,true);
    		 }
    	 }
    	 stepTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
    	 stepTree.addSelectionListener(new ExpandAction());
    	 stepTree.addMouseListener(new ShowPkgAction());
    	 stepTree.pack();
     }
     
     private void addTree(PKGBean version,boolean seqAble){
    	 TreeItem  treeRoot=new TreeItem(stepTree,SWT.CHECK|SWT.MULTI);
    	 treeRoot.setText(version.getId());
    	 treeRoot.setImage(Icons.getFloderIcon());
    	 treeRoot.setData(version);
    	 treeRoot.setData("$Type", STEPBean.Type.Pkg);
    	 if(currentRootItem==null)
    		 currentRootItem=treeRoot;
    	 //取得版本下的步骤信息
    	  List<STEPBean>  steps=STEP.getPkgSteps(version.getId());
    	  for(int w=0;w<steps.size();w++){
    		  STEPBean step=steps.get(w);
    		  boolean expand=false;
    		  if(!step.getAction().equals(STEPBean.ActionType.BackDir.ordinal()+"")&&
    			 !step.getAction().equals(STEPBean.ActionType.BackRun.ordinal()+"")){
        		  if(step.getAction().equals(STEPBean.ActionType.FileCopy.ordinal()+"")||
        			step.getAction().equals(STEPBean.ActionType.ScriptInstall.ordinal()+"") ){
        			  expand=true;
        			  String parentID=step.getParentid();
        			  if(!"0".equals(parentID)){
        				  STEPBean PPStep=STEP.getPkgSteps(version.getId(), parentID);
        				  if(PPStep!=null&&(PPStep.getAction().equals(STEPBean.ActionType.BackDir.ordinal()+"")
        						                       ||PPStep.getAction().equals(STEPBean.ActionType.BackRun.ordinal()+"")
        						  )){
        					  TreeItem  backupStep=new TreeItem(treeRoot,SWT.CHECK|SWT.MULTI);
        					  backupStep.setText(PPStep.getDesc());
        					  backupStep.setImage(Icons.getFloderIcon());
        					  backupStep.setData(PPStep);
        					  backupStep.setData("$Type", STEPBean.Type.Null);
        				  }
        			  }
        		  }
        		  TreeItem  workflowStep=new TreeItem(treeRoot,SWT.CHECK|SWT.MULTI);
        		  workflowStep.setText(step.getDesc());
        		  workflowStep.setImage(Icons.getFloderIcon());
        		  workflowStep.setData(step);
        		  if(expand){
        			  workflowStep.setData("$Type", STEPBean.Type.Step);
        		  }else{
        			  workflowStep.setData("$Type", STEPBean.Type.Null);
        		  }
    		  }
    	  }
    	  //把步骤顺序初始化好
    	  if(!seqAble){
	    	  TreeItem[] stepItems= treeRoot.getItems();
	    	  if(stepItems!=null&&stepItems.length>0){
	    		  int w=1;
	    		  for(TreeItem treeItem:stepItems){
	    			  STEPBean step=(STEPBean)treeItem.getData();
	    			  if(step!=null){
	    				  step.setOrder(w+"");
	    				  STEP.setStepOrder(step.getPkgID(), step.getId(), step.getOrder(), DateUtil.getCurrentTime());
	    				  w++;
	    			  }
	    		  }
	    	  }
    	  }
    	  treeRoot.setExpanded(true);//刷新版本树后，将树展开
     }
     
     public void importTree(PKGBean versionID){
    	 //判断是否有该版本树，有的话需要先删除
    	 TreeItem[] trees=stepTree.getItems();
    	 for(int k=0;k<trees.length;k++){
    		 String curTreeData=trees[k].getText();
    		 if(curTreeData.equals(versionID)){
    			 trees[k].removeAll();//只能删除本节点
    			 trees[k].dispose();//删除本身
    		 }
    	 }
    	 this.addTree(versionID,false);
     }
     
     public void removeVersionTree(String versionID){
    	 TreeItem[] versions=stepTree.getItems();
    	 for(TreeItem item:versions){
    		  String version=(String)item.getData();
    		  if(versionID.equals(version)){
    			  item.removeAll();
    			  item.dispose();
    			  
    		  }
    	 }
     }
     
     public void refreshVersionTree(String versionID){
    	 TreeItem[] versions=stepTree.getItems();
    	 PKGBean pkg=null;
    	 if(versions!=null&&versions.length>0){
	    	 for(TreeItem item:versions){
	    		 PKGBean pkgData=(PKGBean)item.getData();
	    		  String version=pkgData.getId();
	    		  if(versionID.equals(version)){
	    			  item.removeAll();
	    			  item.dispose();
	    			  currentRootItem=null;
	    			  pkg=pkgData;
	    		  }
	    	 }
    	 }
    	 if(pkg==null){
    		 pkg=new  PKGBean();
    		 pkg.setId(versionID);
    		 pkg.setStatus(PKGBean.Status.Config.ordinal()+"");
    	 }
    	 this.addTree(pkg,false);
     }
     
     public class ExpandAction extends SelectionAdapter{
 		public void widgetSelected(SelectionEvent e){
 			TreeItem currentItem=(TreeItem)e.item;
 			STEPBean.Type type=(STEPBean.Type)currentItem.getData("$Type");
 			if( STEPBean.Type.Step.equals(type)){
 				//当前版本也要同步跟着切换
 				currentRootItem=currentItem.getParentItem();
 				STEPBean data=(STEPBean)currentItem.getData();
 				List<STEPBean>  childrenStep=STEP.geFileSteps(data.getPkgID(), data.getId());
 				currentItem.removeAll();
 				 for(int w=0;w<childrenStep.size();w++){
 		    		  TreeItem  step=new TreeItem(currentItem,SWT.NONE);
 		    		  step.setText(childrenStep.get(w).getDesc());
 		    		  step.setImage(Icons.getFileIcon());
 		    		  step.setData(childrenStep.get(w));
 		    		  step.setData("$Type", STEPBean.Type.fileStep);
 				 }
 				currentItem.setExpanded(true);
 			}
 			else if(STEPBean.Type.Pkg.equals(type)){//展现版本详情
 				currentRootItem=currentItem;
 				/*PKG version=(PKG)currentRootItem.getData();
 				String itemName=version.getId();
				if(!DeployEditView.getInstance(null).getTabState(itemName)){
					DeployPlayView strategyView=new DeployPlayView(DeployEditView.getInstance(null).getTabFloder(),itemName);
					DeployEditView.getInstance(null).setTabItems(strategyView.selfContent, itemName);
				}*/
 			}
 			else{
 				currentRootItem=currentItem.getParentItem();
 				//currentItem.setChecked(false);
 			}
 		}
 	}
     
     public TreeItem currentRootItem;
     public class UpStepAction extends SelectionAdapter{
		    public void widgetSelected(SelectionEvent e){
		    	if(currentRootItem!=null){
		    		Context.session.inroll(e.getClass().toString(), "F-001-00004");
		    		TreeItem[] items=currentRootItem.getItems();
		    		STEPBean[] steps=new STEPBean[items.length];
		    		//改变顺序并向上滚动
		 			List<Integer> orderItems=new ArrayList<Integer>();
		 			List<Integer> checkedSeq=new ArrayList<Integer>();
		 			if(items!=null&&items.length>0){
		 				for(int w=0;w<items.length;w++){
		 					if(items[w].getChecked()){
		 						orderItems.add(w);
		 					}
		 					steps[w]=(STEPBean)items[w].getData();
		 				}
		 			}
		 			boolean reentrant=true;
		 			if(orderItems!=null&&orderItems.size()>0){
		 				for(int index:orderItems){
		 					int switchIndex=index-1;
			 				if(switchIndex>=0&&reentrant){
			 					STEPBean swichiObj=steps[switchIndex];
			 					steps[switchIndex]=steps[index];
			 					steps[index]=swichiObj;
			 					checkedSeq.add(switchIndex);
			 				}else{
			 					checkedSeq.add(index);
			 					reentrant=false;
			 				}
		 				}
		 				currentRootItem.removeAll();
			 			for(int i=0;i<steps.length;i++){
			 				  STEP.setStepOrder(steps[i].getPkgID(), steps[i].getId(), (i+1)+"", DateUtil.getCurrentTime());
			 				  TreeItem  workflowStep=new TreeItem(currentRootItem,SWT.CHECK|SWT.MULTI);
			 	    		  workflowStep.setText(steps[i].getDesc());
			 	    		  workflowStep.setImage(Icons.getFloderIcon());
			 	    		  workflowStep.setData(steps[i]);
			 	    		  workflowStep.setData("$Type", STEPBean.Type.Step);
			 	    		 if(checkedSeq.contains(i)){
			 	    			workflowStep.setChecked(true);
							 }
			 			}
			 			currentRootItem.setExpanded(true);
			 			Logger.getInstance().logServer("调整版本包步骤完成");
		 			}
		    	}
		    }
     }
     
     public class DownStepAction extends SelectionAdapter{
		    public void widgetSelected(SelectionEvent e){
		    	if(currentRootItem!=null){
		    		    Context.session.inroll(e.getClass().toString(), "F-001-00004");
			    		TreeItem[] items=currentRootItem.getItems();
			    		STEPBean[] steps=new STEPBean[items.length];
			    		//改变顺序并向上滚动
			 			List<Integer> orderItems=new ArrayList<Integer>();
			 			List<Integer> checkedSeq=new ArrayList<Integer>();
			 			if(items!=null&&items.length>0){
			 				for(int w=0;w<items.length;w++){
			 					if(items[w].getChecked()){
			 						orderItems.add(w);
			 					}
			 					steps[w]=(STEPBean)items[w].getData();
			 				}
			 			}
			 			boolean reentrant=true;
			 			if(orderItems!=null&&orderItems.size()>0){
			 				for(int w=orderItems.size()-1;w>=0;w--){
				 				int index=orderItems.get(w);
				 				int switchIndex=index+1;
				 				if(switchIndex<items.length&&reentrant){
				 					STEPBean swichiObj=steps[switchIndex];
				 					steps[switchIndex]=steps[index];
				 					steps[index]=swichiObj;
				 					checkedSeq.add(switchIndex);
				 				}else{
				 					checkedSeq.add(index);
				 					reentrant=false;
				 				}
			 				currentRootItem.removeAll();
				 			for(int i=0;i<steps.length;i++){
				 				 STEP.setStepOrder(steps[i].getPkgID(), steps[i].getId(), (i+1)+"", DateUtil.getCurrentTime());
				 				  TreeItem  workflowStep=new TreeItem(currentRootItem,SWT.CHECK|SWT.MULTI);
				 	    		  workflowStep.setText(steps[i].getDesc());
				 	    		  workflowStep.setImage(Icons.getFloderIcon());
				 	    		  workflowStep.setData(steps[i]);
				 	    		  workflowStep.setData("$Type", STEPBean.Type.Step);
				 	    		 if(checkedSeq.contains(i)){
				 	    			workflowStep.setChecked(true);
								 }
				 			}
				 			Logger.getInstance().logServer("调整版本包步骤完成");
				 			currentRootItem.setExpanded(true);
			 			}
			    	}
		    	}
		    }
     }
     
     
     public class DeleteStepAction extends SelectionAdapter{
		    public void widgetSelected(SelectionEvent e){
		    	if(currentRootItem!=null){
		    		//注册会话功能
					Context.session.inroll(e.getClass().toString(), "F-001-00002");
			    	TreeItem[] items=currentRootItem.getItems();
			    	List<STEPBean> steps=new ArrayList<STEPBean>();
			    	if(items!=null&&items.length>0){
		 				for(int w=0;w<items.length;w++){
		 					if(items[w].getChecked()){
		 						STEPBean data=(STEPBean)items[w].getData();
		 						steps.add(data);
		 					}
		 				}
			    	}
			    	if(steps!=null&&steps.size()>0){
			    		for(STEPBean step:steps){
			    			STEP.deleteByID(step.getPkgID(),step.getId());
			    		}
			    		currentRootItem.removeAll();
			    		 PKGBean version=(PKGBean)currentRootItem.getData();
			    		 List<STEPBean>  Steps=STEP.getPkgSteps(version.getId());
				       	  for(int w=0;w<Steps.size();w++){
				       		  TreeItem  workflowStep=new TreeItem(currentRootItem,SWT.CHECK|SWT.MULTI);
				       		  workflowStep.setText(Steps.get(w).getDesc());
				       		  workflowStep.setImage(Icons.getFloderIcon());
				       		  workflowStep.setData(Steps.get(w));
				       		  workflowStep.setData("$Type", STEPBean.Type.Step);
				       	  }
				       	currentRootItem.setExpanded(true);//刷新版本树后，将树展开
			    	}
			    	else{//判断是否要删除版本包
			    		if(currentRootItem.getChecked()){
				    		 PKGBean version=(PKGBean)currentRootItem.getData();
				    		 if(version.getStatus().equals(PKGBean.Status.Run.ordinal()+"")){
				    			    MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage("当前版本正在安装，不允许删除！");
									box.open();	
				    		 }else{
					    		 currentRootItem.removeAll();
					    		 Logger.getInstance().logServer("删除版本包:"+version.getId());
					    		 PKG.archivePkg(version.getId());
					    		 PKG.disablePkg(version.getId(), PKGBean.Enable.No.ordinal()+"", DateUtil.getCurrentTime());
					    		 currentRootItem.dispose();
					    		 currentRootItem=null;
					    		  MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									box.setText(Constants.getStringVaule("alert_successoperate"));
									box.setMessage("版本数据已成功删除并归档！");
									box.open();	
				    		 }
			    		}
			    	}
		    	}
		    }
     }
     
     public class BackupAction extends SelectionAdapter{
		    public void widgetSelected(SelectionEvent e){
		    	if(currentRootItem!=null){
		    		PKGBean version=(PKGBean)currentRootItem.getData();
		    		Point point=AppView.getInstance().getCentreScreenPoint();//((Button)e.getSource()).getLocation();
		    		BackupCfgView cfv=new BackupCfgView(version.getId(),point);
		    		cfv.showPanel();
		    	}
		    }
     }
     
     public class ServiceCfgAction extends SelectionAdapter{
		    public void widgetSelected(SelectionEvent e){
		    	if(currentRootItem!=null){
		    		PKGBean version=(PKGBean)currentRootItem.getData();
		    		Point point=AppView.getInstance().getCentreScreenPoint();
		    		ServiceCfgView sfv=new ServiceCfgView(version.getId(),point);
		    		sfv.showPanel();
		    	}
		    }
     }
     
     public class ShowPkgAction extends MouseAdapter{
 		public void mouseDoubleClick(MouseEvent e){
 			TreeItem currentItem=stepTree.getItem(new Point(e.x,e.y));
 			if(currentItem!=null){
 				STEPBean.Type type=(STEPBean.Type)currentItem.getData("$Type");
 				 if(STEPBean.Type.Pkg.equals(type)){//展现版本详情
 	 				currentRootItem=currentItem;
 	 				PKGBean version=(PKGBean)currentRootItem.getData();
 	 				String itemName=version.getId();
 					if(!DeployEditView.getInstance(null).getTabState(itemName)){
 						DeployPlayView strategyView=new DeployPlayView(DeployEditView.getInstance(null).getTabFloder(),itemName);
 						DeployEditView.getInstance(null).setTabItems(strategyView.selfContent, itemName);
 					}
 	 			}
 			}
 		}
 		 public void mouseDown(MouseEvent e){
 			TreeItem currentItem=stepTree.getItem(new Point(e.x,e.y));
 			if(currentItem!=null){
 				STEPBean.Type type=(STEPBean.Type)currentItem.getData("$Type");
 				 if(!STEPBean.Type.Pkg.equals(type)){//展现版本详情
 					//当前版本也要同步跟着切换
 	 				currentRootItem=currentItem.getParentItem();
 				 }
 			}
 		 }
 	} 

}
