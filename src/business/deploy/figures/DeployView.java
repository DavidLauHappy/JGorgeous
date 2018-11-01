package business.deploy.figures;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import bean.SYSTEMBean;
import business.deploy.core.PkgSystemScheduler;
import business.deploy.core.ViewRefresher;

import resource.Logger;

import utils.LayoutUtils;
import utils.StringUtil;


public class DeployView {

 private  SashForm selfContent=null;
	public SashForm getSachForm(){
		return selfContent;
	}
 public DeployView(Composite com,SYSTEMBean data,String versionID){
	 selfContent=new SashForm(com,SWT.VERTICAL);
	 selfContent.setLayout(LayoutUtils.getDeployLayout());
	 this.data=data;
	 this.versionID=versionID;
	 createSashForms();
	 selfContent.pack();
	  ViewRefresher.getInstance().addPage(this);
	  if(StringUtil.isNullOrEmpty(this.versionID)){
		  this.canClose=true;
	  }else{
		  this.canClose=false;
	  }
 }
 
 private DiagramView diagramView=null;
 private CommandView commandView=null;
 private void createSashForms(){
	 //上下结构，上面为拓扑部署逻辑结构，下部分是命令行阅读窗口
	 //diagramView=new DrawableView(selfContent,this.data)
	 boolean editable=true;
	  if(!StringUtil.isNullOrEmpty(this.versionID)){
		  editable=false;
	  }
	 diagramView=new DiagramView(selfContent,this.data,editable);
	 commandView=new CommandView(selfContent,this.data);
	 diagramView.setCommandView(commandView);
	
	 diagramView.initConsoles();
	 selfContent.setWeights(new int[]{60,40});
}
 
public DiagramView getDiagramView() {
	return diagramView;
}
public CommandView getCommandView() {
	return commandView;
}
 
private SYSTEMBean data;
private String versionID;

public String getSystemName(){
	return this.data.getName();
}

	public String getSystemID(){
		return this.data.getBussID();
	}

	public String getVersion(){
		if(StringUtil.isNullOrEmpty(this.versionID)){
			this.versionID=PkgSystemScheduler.getInstance().getCurrVersionID();
			this.canClose=false;
		}
		return this.versionID;
	}
	
	public boolean canClose=true;
	public boolean promptAble=false;
 
}
