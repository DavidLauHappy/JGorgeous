package views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import resource.Context;
import resource.Logger;

import utils.LayoutUtils;


public class WorkAreaView   extends SashForm{
	
	private static WorkAreaView unique_instance;
	public static WorkAreaView getInstance(Composite parent){
		if(unique_instance==null){
			if(parent!=null)
			 unique_instance=new WorkAreaView(parent,SWT.HORIZONTAL);
		}
		return unique_instance;
	}
	
	private  SashForm self=null;
	public SashForm getSachForm(){
		return self;
	}
	private SourceView sourceView=null;
	private EditView editView=null;
	private  WorkAreaView(Composite c,int style){
		super(c,style);
		self=this;
		 createSashForms();
	}
	
	private void createSashForms()
	{	
		//左右结构，左边是资源视图，右边是详情和编辑视图
		sourceView=SourceViewFactory.getSourceView(self,  Context.session.roleID);//SourceView.getInstance(self);
		sourceView.createSourceView();

		editView=EditViewFactory.getEditView(self,  Context.session.roleID);
		editView.showEditView();
		self.setWeights(new int[]{30,70});
	}
}
