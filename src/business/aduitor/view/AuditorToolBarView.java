package business.aduitor.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;



import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;


import utils.LayoutUtils;
import utils.StringUtil;
import views.ToolBarView;

public class AuditorToolBarView extends ToolBarView{
	public static  AuditorToolBarView unique_instance;
	private Composite mainComposite=null;
	public AuditorToolBarView(Composite main,int style){
		mainComposite=new Composite(main,SWT.NONE);
		mainComposite.setLayout(this.getLayout());
		mainComposite.setLayoutData(this.getToolsLayoutData());
	}
	
	public void createToolBar() {
		
		
		Button btnAudit=new Button(mainComposite,SWT.NONE);
		btnAudit.setToolTipText(Constants.getStringVaule("btn_tips_audit"));
		btnAudit.setImage(Icons.getAuditIcon());
		btnAudit.setLayoutData(LayoutUtils.getToolsLayoutData());
		
		Button btnSelect=new Button(mainComposite,SWT.NONE);
		btnSelect.setToolTipText(Constants.getStringVaule("btn_tips_auditcheck"));
		btnSelect.setImage(Icons.getSelectIcon());
		btnSelect.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnSelect.addSelectionListener(new SelectAuditAction());
		
		/*Button btnApprove=new Button(mainComposite,SWT.PUSH);
		btnApprove.setToolTipText(Constants.getStringVaule("btn_tips_approve"));
		btnApprove.setImage(Icons.getApproveIcon());
		btnApprove.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnApprove.addSelectionListener(new MyApproveAction());*/
		
		Button btnUser=new Button(mainComposite,SWT.NONE);
		String tips= Context.session.userID+"("+ Context.session.userName+")";
		String line="";
		Map<String, String> Apps=Dictionary.getDictionaryMap("APP");
		if(!StringUtil.isNullOrEmpty(Context.Apps)){
			String[] apps=Context.Apps.split("\\|");
			for(int w=0;w<apps.length;w++){
				String key=apps[w];
				String value=Apps.get(key);
				line+=key+"["+value+"]";
			}
			if(!StringUtil.isNullOrEmpty(line)){
				tips+="\r\n"+line;
			}
		}
		
		btnUser.setToolTipText(tips);
		btnUser.setImage(Icons.getMeIcon());
		btnUser.setLayoutData(LayoutUtils.getToolsLayoutData());
		unique_instance=this;
	}
	
	
	public  GridData getToolsLayoutData(){
		GridData griddata_row=new GridData();
		griddata_row.horizontalAlignment=GridData.FILL;
		griddata_row.verticalAlignment=GridData.FILL;
		//griddata_row.grabExcessVerticalSpace=true;
		griddata_row.widthHint=64;
		griddata_row.heightHint=64;
		//垂直方向上布局的误差
		griddata_row.verticalIndent=-5;
		return griddata_row;
	}
	
	private  GridLayout getLayout(){
		GridLayout grid_row=new GridLayout();
		//grid_row.makeColumnsEqualWidth=true;
		grid_row.numColumns=15;
		grid_row.horizontalSpacing=10;
		grid_row.verticalSpacing=10;
		return grid_row;
	}
	
	
	public class SelectAuditAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_auditcheck");
			if(!AuditorEditView.getInstance(null).getTabState(item)){
				AuditSelectView selectView=new  AuditSelectView(AuditorEditView.getInstance(null).getTabFloder());
				AuditorEditView.getInstance(null).setTabItems(selectView.content, item);
			}
		}
	}
}
