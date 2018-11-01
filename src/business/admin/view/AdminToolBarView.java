package business.admin.view;

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

public class AdminToolBarView extends ToolBarView{
	public static  AdminToolBarView unique_instance;
	private Composite mainComposite=null;
	public void createToolBar() {
		
		
		Button btnUserRole=new Button(mainComposite,SWT.NONE);
		btnUserRole.setToolTipText(Constants.getStringVaule("btn_tips_userrole"));
		btnUserRole.setImage(Icons.getManageUserIcon());
		btnUserRole.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnUserRole.addSelectionListener(new UserRoleAction());
		
		Button btnUserGroup=new Button(mainComposite,SWT.NONE);
		btnUserGroup.setToolTipText(Constants.getStringVaule("btn_tips_usergroup"));
		btnUserGroup.setImage(Icons.getGroupUserIcon());
		btnUserGroup.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnUserGroup.addSelectionListener(new UserGroupAction());
		
		Button btnGroup=new Button(mainComposite,SWT.NONE);
		btnGroup.setToolTipText(Constants.getStringVaule("btn_tips_groupmng"));
		btnGroup.setImage(Icons.getGroupIcon());
		btnGroup.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnGroup.addSelectionListener(new GroupMngAction());
		
		
		Button btnTag=new Button(mainComposite,SWT.NONE);
		btnTag.setToolTipText(Constants.getStringVaule("btn_tips_groupmanager"));
		btnTag.setImage(Icons.getTagIcon());
		btnTag.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnTag.addSelectionListener(new NodeManageAction());
		
		
		Button btnFlow=new Button(mainComposite,SWT.NONE);
		btnFlow.setToolTipText(Constants.getStringVaule("btn_tips_appmng"));
		btnFlow.setImage(Icons.getFlowIcon());
		btnFlow.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnFlow.addSelectionListener(new AppMngAction());
		
		/*Button btnApprove=new Button(mainComposite,SWT.PUSH);
		btnApprove.setToolTipText(Constants.getStringVaule("btn_tips_approve"));
		btnApprove.setImage(Icons.getApproveIcon());
		btnApprove.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnApprove.addSelectionListener(new MyApproveAction());*/
		
		Button btnSetting=new Button(mainComposite,SWT.PUSH);
		btnSetting.setToolTipText(Constants.getStringVaule("btn_tips_settingmng"));
		btnSetting.setImage(Icons.getSettingIcon());
		btnSetting.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnSetting.addSelectionListener(new SettingMngAction());
		
		
		Button btnUser=new Button(mainComposite,SWT.NONE);
		String tips=Context.session.userID+"("+Context.session.userName+")";
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
	
	
	public AdminToolBarView(Composite main,int style){
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
	
	public class NodeManageAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_groupmanager");
			if(!AdminEditView.getInstance(null).getTabState(item)){
				NodeManagerView  nodeView=new NodeManagerView(AdminEditView.getInstance(null).getTabFloder());
				AdminEditView.getInstance(null).setTabItems(nodeView.content, item);
			}
		}
	}
	
	public class UserRoleAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_userrole");
			if(!AdminEditView.getInstance(null).getTabState(item)){
				UserRoleView  nodeView=new UserRoleView(AdminEditView.getInstance(null).getTabFloder());
				AdminEditView.getInstance(null).setTabItems(nodeView.content, item);
			}
		}
	}
	
	public class UserGroupAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_usergroup");
			if(!AdminEditView.getInstance(null).getTabState(item)){
				UserGroupView  nodeView=new UserGroupView(AdminEditView.getInstance(null).getTabFloder());
				AdminEditView.getInstance(null).setTabItems(nodeView.content, item);
			}
		}
	}
	
	public class GroupMngAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_groupmng");
			if(!AdminEditView.getInstance(null).getTabState(item)){
				GroupMngView  nodeView=new GroupMngView(AdminEditView.getInstance(null).getTabFloder());
				AdminEditView.getInstance(null).setTabItems(nodeView.content, item);
			}
		}
	}
	
	public class AppMngAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_appmng");
			if(!AdminEditView.getInstance(null).getTabState(item)){
				AppMngView  nodeView=new AppMngView(AdminEditView.getInstance(null).getTabFloder());
				AdminEditView.getInstance(null).setTabItems(nodeView.content, item);
			}
		}
	}
	
	
	public class SettingMngAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String item=Constants.getStringVaule("btn_tips_settingmng");
			if(!AdminEditView.getInstance(null).getTabState(item)){
				SettingMngView settingView=new SettingMngView(AdminEditView.getInstance(null).getTabFloder());
				AdminEditView.getInstance(null).setTabItems(settingView.content, item);
			}
		}
	}
}
