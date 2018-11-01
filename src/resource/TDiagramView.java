package resource;

import java.io.File;
import java.util.List;

import model.LOCALCOMMAND;
import model.LOCALNODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import common.core.ScheduleService;
import common.core.ViewRefreshService;

import resource.Icons;
import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

public class TDiagramView {
	
	 public String versionID;
	 public String nodeID;
	 public  Composite content=null;
	 private  Composite parent=null;
	 public TDiagramView self=null;
	 public boolean canPageRemove=false;
	 public boolean promptAble=false;
	 public TDiagramView(Composite com,String versionID,String nodeID){
		 this.parent=com;
		 this.versionID=versionID;
		 this.nodeID=nodeID;
		 content=new Composite(com,SWT.NONE);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		  this.createToolsView();
		  this.createNodeView();
		  content.pack();
		  self=this;
		  content.setData("page", self);
		  boolean result=ScheduleService.getInstance().setVersion(versionID, nodeID);
		  if(result){
			  	ViewRefreshService.getIntance().addPage(self);
		  }else{
			  btnAction.setEnabled(true);
		  }
		
	 }
	 
	 private Text textProgress=null;
	 private ProgressBar progress=null;
	 private Button btnReport=null;
	 private Button btnAction=null;
	 private void createToolsView(){
		 Composite toolPanel=new Composite(content,SWT.NONE);
		  toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		  toolPanel.setLayout(LayoutUtils.getComGridLayout(22, 1));
		  btnAction=new Button(toolPanel,SWT.PUSH);
		  btnAction.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
		  btnAction.setText(Constants.getStringVaule("btn_tips_start"));
		  btnAction.addSelectionListener(new StartPushAction());
		  btnAction.setEnabled(false);
		  
		  textProgress=new Text(toolPanel,SWT.RIGHT);
		  textProgress.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 2, 1, 0, 0));
		  textProgress.setVisible(false);
		  progress=new ProgressBar(toolPanel,SWT.SMOOTH);// SWT.HORIZONTAL
		  progress.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 15, 1, 0, 0));
		  progress.setMinimum(0);
		  progress.setVisible(false);
		  btnReport=new Button(toolPanel,SWT.PUSH);
		  btnReport.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
		  btnReport.setText(Constants.getStringVaule("btn_tips_report"));
		  btnReport.addSelectionListener(new InstallReportAction());
		  btnReport.setVisible(false);
		  toolPanel.pack();
	 }
	 
	 public void setProgress(String text,int max,int curr){
			if(!textProgress.getVisible())
				textProgress.setVisible(true);
			if(!progress.getVisible())
				progress.setVisible(true);
			textProgress.setEditable(true);
			textProgress.setText(text);
			textProgress.setEditable(false);
			progress.setMaximum(max);
			progress.setSelection(curr);//进度条前进一格
			if(curr>0&&max==curr){
				btnReport.setVisible(true);
			}
		}
	 
	 private  SashForm sashForm=null;
	 private Tree nodeTree=null;
	 private   CTabFolder tabFloder=null;
	 private List<LOCALNODEBean> nodes;
	 private void createNodeView(){
		 sashForm=new SashForm(content,SWT.VERTICAL);
		 sashForm.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 
		 nodeTree=new Tree(sashForm,SWT.BORDER|SWT.MULTI);
		 nodeTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 nodeTree.addMouseListener(new ShowNodeStepsAction());
		 
		 nodes=LOCALNODE.getVersionNode(this.versionID);
		 if(nodes!=null&&nodes.size()>0){
			 for(LOCALNODEBean node:nodes){
				 TreeItem  treeItemNode=new TreeItem(nodeTree,SWT.NONE);
				 treeItemNode.setText(node.getShowName());
				 treeItemNode.setImage(Icons.getNodeIcon(node.getStatus()));
				 treeItemNode.setData(node);
				 TRunView guide=new TRunView();
				 guide.setVersion(this.versionID);
		         guide.setData(node);
		         
		         treeItemNode.setData("$View", guide);
			 }
		 }
		 nodeTree.pack();
		 
		 tabFloder=new CTabFolder(sashForm,SWT.TOP|SWT.BORDER);//|SWT.CLOSE|
		 tabFloder.setLayoutData(LayoutUtils.getCommomGridData());
		 tabFloder.setMaximizeVisible(true);
		 tabFloder.setMinimizeVisible(true);  
		 tabFloder.setSimple(false);
		 tabFloder.setSelectionForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		 tabFloder.setSelectionBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		 tabFloder.setTabHeight(20);
		 this.initConsoles();
		 tabFloder.pack();
		sashForm.setWeights(new int[]{40,60});
			
	 }
	 
	 
	 public void redrawNode(){
		 this.refreshNodeTree();
	 }
	 
	 private void refreshNodeTree(){
		 nodeTree.removeAll();
		 nodes=LOCALNODE.getVersionNode(this.versionID);
		 if(nodes!=null&&nodes.size()>0){
			 for(LOCALNODEBean node:nodes){
				 TreeItem  treeItemNode=new TreeItem(nodeTree,SWT.NONE);
				 treeItemNode.setText(node.getShowName());
				 treeItemNode.setImage(Icons.getNodeIcon(node.getStatus()));
				 treeItemNode.setData(node);
				 TRunView guide=new TRunView();
				 guide.setVersion(this.versionID);
		         guide.setData(node);
		         treeItemNode.setData("$View", guide);
			 }
		 }
	 }
	 
	 public class StartPushAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				 boolean result=ScheduleService.getInstance().setVersion(versionID, nodeID);
				  if(result){
					  	 btnAction.setEnabled(false);
					 	ViewRefreshService.getIntance().addPage(self);
				  }else{
					  	MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage("当前有版本仍在按照，调度繁忙请稍后重试");
						box.open();	
						btnAction.setEnabled(true);
				  }
			}
	 }
	 
	 public class InstallReportAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
					 String rptPath=Paths.getInstance().getWorkDir()+File.separator+versionID+nodeID+".txt";
					 File rptFile=new File(rptPath);
					 if(!rptFile.exists()){
						 List<LOCALCOMMANDBean> cmds=LOCALCOMMAND.getCommand(versionID, nodeID);
						 String fileContent="";
						 for(LOCALCOMMANDBean cmd:cmds){
							 String line=StringUtil.rightPadBytes(cmd.getCmdName(),100," ")+"\t"+StringUtil.rightPadBytes(cmd.getMd5(),32," ")+"\t"+StringUtil.rightPadBytes(cmd.getInstalled(),32," ")+"\t"+cmd.getLoginfo();
							 fileContent=fileContent+line+"\r\n";
						 }
						 FileUtils.writeFile(rptPath, fileContent);
						 //删除数据
						 LOCALCOMMAND.delete(versionID, nodeID);
					 }
					 FileUtils.openFileByLocal(rptPath); 
					 //ViewRefreshService.getIntance().removePage(self);
			}
	 }
	 
	 
	 public class ShowNodeStepsAction extends MouseAdapter{
			public void mouseDown(MouseEvent e){}
			public void mouseDoubleClick(MouseEvent e){
	 			TreeItem currentItem=nodeTree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null){
	 				TRunView guide=(TRunView)currentItem.getData("$View");
	 				if(guide!=null){
	 					guide.setPosition(new Point(e.x,e.y));
	 					guide.show();
	 				}
	 			}
			}
	 }
	 //////////////////consoleView///////////////////////////////////////////////////////////////////////////////////////////
	 public void initConsoles(){
		 if(nodes!=null&&nodes.size()>0){
			 for(LOCALNODEBean node:nodes){
					String name=node.getName();
					  if(!getTabStatus(name)){
						  TConsoleView console=new TConsoleView(tabFloder,node);
						  setTabItems(console.getContent(), name,node.getIp(),node,console);
					  }
			 }
		 }
	 }
	 
	 public  boolean getTabStatus(String item){
		 CTabItem[] items=tabFloder.getItems();
			for(int k=0;k<items.length;k++){
				if(item.equals(items[k].getText())){ //先设置为选中
				  tabFloder.setSelection(items[k]);    
				  return true;
				}
			}
			return false;
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
	 
	 public void updateTabContent(String id,String text){
			CTabItem[] items=tabFloder.getItems();
			for(int k=0;k<items.length;k++){
				LOCALNODEBean node=(LOCALNODEBean)items[k].getData();
				if(node.getId().equals(id)){
					TConsoleView console=(TConsoleView)items[k].getData("Composite");
					console.setConsoleText(text);
				}
			}
		}
	 //////////////////end consoleView///////////////////////////////////////////////////////////////////////////////////////////
}
