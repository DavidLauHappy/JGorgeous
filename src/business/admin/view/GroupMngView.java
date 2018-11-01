package business.admin.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.GROUPS;
import model.USERS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import resource.Constants;
import bean.GroupBean;
import business.admin.view.UserGroupView.RemoveUserRole;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

public class GroupMngView {
	private Composite parent;
	public Composite content;
	public Text textName;
	private org.eclipse.swt.widgets.List listControl=null;
	public Map<String,String> Groups=new HashMap<String, String>();
	
	public GroupMngView(Composite com){
		this.parent=com;
		this.createAndShow();
	}
	
	private void createAndShow(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 10));
		Composite pannelAction=new Composite(content,SWT.NONE);
		pannelAction.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannelAction.setLayout(LayoutUtils.getComGridLayout(5, 0));
		textName=new Text(pannelAction,SWT.NONE);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 3, 1, 0, 0));

		Button btnAdd=new Button(pannelAction,SWT.PUSH);
		btnAdd.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
		btnAdd.setText("   "+Constants.getStringVaule("btn_add")+"    ");
		btnAdd.addSelectionListener(new AddGroupAction());
		
		Button btnRemove=new Button(pannelAction,SWT.PUSH);
		btnRemove.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
		btnRemove.setText("   "+Constants.getStringVaule("btn_delete")+"    ");
		btnRemove.addSelectionListener(new RemoveGroupAction());
		pannelAction.pack();

			Group pannelData=new Group(content,SWT.NONE);
			    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
			    pannelData.setText("用户群组");
			    listControl=new org.eclipse.swt.widgets.List(pannelData,SWT.SINGLE|SWT.V_SCROLL);
				listControl.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
				List<GroupBean> roles=GROUPS.getGroups();
				if(roles!=null&&roles.size()>0){
					String[] items=new String[roles.size()];
					int index=0;
						for(GroupBean role:roles){
							items[index]=role.getName();
							Groups.put(role.getName(), role.getId());
							index++;
						}
					listControl.setItems(items);
				}
			    pannelData.pack();
			    content.pack();
	}
	
	public void  refresh(){
		List<GroupBean> roles=GROUPS.getGroups();
		if(roles!=null&&roles.size()>0){
			String[] items=new String[roles.size()];
			int index=0;
				for(GroupBean role:roles){
					items[index]=role.getName();
					Groups.put(role.getName(), role.getId());
					index++;
				}
			listControl.setItems(items);
		}
	}
	public class RemoveGroupAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String[] items=listControl.getSelection();
			if(items!=null&&items.length>0){
				for(String item:items){
					String groupID=Groups.get(item);
					GROUPS.deleteGroup(groupID);
					//群组里面的人也需要删除掉
					USERS.deleteByGroup(groupID);
				}
			}
			refresh();
		}
	}
	
	public class AddGroupAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			 String groupName=textName.getText();
			 if(!StringUtil.isNullOrEmpty(groupName)){
				 if(!GROUPS.groupExist(groupName)){
					  String  groupID=GROUPS.getID();
					  GroupBean bean=new GroupBean(groupID,groupName.trim());
					  bean.inroll();
					  refresh();  
				 }else{
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(Constants.getStringVaule("alert_duplicateval"));
						box.open();	
				 }
			 }
		}
	}
	
}
