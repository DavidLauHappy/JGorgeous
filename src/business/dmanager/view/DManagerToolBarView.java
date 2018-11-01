package business.dmanager.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import business.developer.view.DeveloperToolBarView.AutoUpdateAction;





import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.MyApproveView;
import resource.QuestionManageView;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppAboutView;
import views.SettingView;
import views.ToolBarView;

public class DManagerToolBarView extends ToolBarView{
	public static  DManagerToolBarView unique_instance;
	private Composite mainComposite=null;
	public void createToolBar() {
		
		
		Button btnFind=new Button(mainComposite,SWT.PUSH);
		btnFind.setToolTipText(Constants.getStringVaule("btn_tips_findReq"));
		btnFind.setImage(Icons.getFindIcon());
		btnFind.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnFind.addSelectionListener(new SearchReqAction());
		
		Button btnQuestion=new Button(mainComposite,SWT.PUSH);
		btnQuestion.setToolTipText(Constants.getStringVaule("btn_tips_question"));
		btnQuestion.setImage(Icons.getQustionIcon());
		btnQuestion.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnQuestion.addSelectionListener(new FindQuestionAction());
		
		Button btnSetting=new Button(mainComposite,SWT.PUSH);
		btnSetting.setImage(Icons.getSetIcon());
		btnSetting.setLayoutData(this.getToolsLayoutData());
		btnSetting.setToolTipText(Constants.getStringVaule("btn_tips_setting"));
		btnSetting.addSelectionListener(new SettingAction());
		
		Button btnApprove=new Button(mainComposite,SWT.PUSH);
		btnApprove.setImage(Icons.getApproveIcon());
		btnApprove.setLayoutData(this.getToolsLayoutData());
		btnApprove.setToolTipText(Constants.getStringVaule("btn_tips_approve"));
		btnApprove.addSelectionListener(new ApproveAction());
		
			
		Button btnHelp=new Button(mainComposite,SWT.PUSH);
		btnHelp.setImage(Icons.getHelpIcon());
		btnHelp.setLayoutData(this.getToolsLayoutData());
		btnHelp.setToolTipText(Constants.getStringVaule("btn_tips_helpversion"));
		btnHelp.addSelectionListener(new AutoUpdateAction());
		
		Button btnUser=new Button(mainComposite,SWT.NONE);
		String tips=Context.session.userID+"("+ Context.session.userName+")";
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
		btnUser.setImage(Icons.getOperatorIcon());
		btnUser.setLayoutData(LayoutUtils.getToolsLayoutData());
	}
	
	
	public DManagerToolBarView(Composite main,int style){
		mainComposite=new Composite(main,SWT.NONE);
		mainComposite.setLayout(this.getLayout());
		mainComposite.setLayoutData(this.getToolsLayoutData());
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
	
	
	public class SearchReqAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_findReq");
			if(!DManagerEditView.getInstance(null).getTabState(item)){
				ReqSearchView newReqView=new ReqSearchView(DManagerEditView.getInstance(null).getTabFloder());
				DManagerEditView.getInstance(null).setTabItems(newReqView.content, item);
			}
		}
	}

	public class  FindQuestionAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_question");
			if(!DManagerEditView.getInstance(null).getTabState(item)){
				QuestionManageView qdetailTest=new QuestionManageView(DManagerEditView.getInstance(null).getTabFloder());
				DManagerEditView.getInstance(null).setTabItems(qdetailTest.content, item);
			}
		}
  }
	
	  public class SettingAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			SettingView.getInstance().show();
	   		}
	  }
	
	  public class AutoUpdateAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			AppAboutView.getInstance().show();
	   		}
	  }
	  
	  public class ApproveAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String item=Constants.getStringVaule("btn_tips_approve");
	   			if(!DManagerEditView.getInstance(null).getTabState(item)){
	   				MyApproveView apprView=new MyApproveView(DManagerEditView.getInstance(null).getTabFloder());
	   				DManagerEditView.getInstance(null).setTabItems(apprView.content, item);
	   			}
	   		}
	  }
}

