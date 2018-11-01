package business.admin.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.GROUPS;
import model.ROLES;
import model.USERS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import bean.GroupBean;
import bean.UserBean;
import business.admin.action.DragTargetGroupAction;


import resource.Constants;
import resource.Dictionary;

import utils.LayoutUtils;
import utils.StringUtil;

public class UserGroupView {
	private Composite parent;
	public Composite content;
	private  Combo comboGroups=null;
	public Table userTable=null;
	
	private  String[] header=new String[]{ Constants.getStringVaule("header_userID"),
				Constants.getStringVaule("header_userName") ,
				Constants.getStringVaule("header_userEmail"),
				Constants.getStringVaule("header_userPhone"),
				Constants.getStringVaule("header_userStatus"),
				Constants.getStringVaule("header_userApps")};
	
	
	public Map<String,String> Groups=new HashMap<String, String>();
	
	public UserGroupView(Composite com){
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
		comboGroups=new Combo(pannelAction,SWT.DROP_DOWN);
		comboGroups.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 3, 1, 0, 0));
		
		List<GroupBean> roles=GROUPS.getGroups();
		if(roles!=null&&roles.size()>0){
			String[] items=new String[roles.size()];
			int index=0;
			for(GroupBean role:roles){
				items[index]=role.getName();
				Groups.put(role.getName(), role.getId());
				index++;
			}
			comboGroups.setItems(items);
		}
		comboGroups.addSelectionListener(new LoadRoleUser());
		
		Button btnRemove=new Button(pannelAction,SWT.PUSH);
		btnRemove.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1, 0, 0));
		btnRemove.setText("   "+Constants.getStringVaule("btn_deletegroup")+"    ");
		btnRemove.addSelectionListener(new RemoveUserRole());
		pannelAction.pack();
	
	    Composite pannelData=new Composite(content,SWT.NONE);
	    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
	    userTable=new Table(pannelData,SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION);
	    userTable.setHeaderVisible(true);
	    userTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	    userTable.setLinesVisible(true);
	    userTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
	    DropTarget drawableTarget=new DropTarget(userTable,DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
	    drawableTarget.setTransfer(new Transfer[]{FileTransfer.getInstance()});
	    drawableTarget.addDropListener(new DragTargetGroupAction(userTable,comboGroups,Groups));
	    
	    for(int i=0;i<header.length;i++){
			TableColumn tablecolumn=new TableColumn(userTable,SWT.BORDER);
			tablecolumn.setText(header[i]);
			tablecolumn.setMoveable(true);
		}
		
		for(int j=0;j<header.length;j++){		
			userTable.getColumn(j).pack();
		}	
	    pannelData.pack();
		content.pack();
	}
	
	Map<String,String> Status=Dictionary.getDictionaryMap("USERS.STATUS");
	public class LoadRoleUser  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			refreshTable();
		}
	}
	
	public void refreshTable(){
		userTable.removeAll();
		String roleName=comboGroups.getText();
		String roleID=Groups.get(roleName);
		if(!StringUtil.isNullOrEmpty(roleID)){	
			List<UserBean> users=USERS.getGroupUsers(roleID);
			if(users!=null&&users.size()>0){
				for(UserBean user:users){
					  TableItem tableItem=new TableItem(userTable,SWT.BORDER);
					  String statusName=Status.get(user.getStatus());
					  tableItem.setText(new String[]{user.getUserID(),user.getUserName(),user.getEmail(),user.getPhone(),statusName,user.getApps()});
					  tableItem.setData(user);
				}
				 for(int j=0;j<userTable.getColumnCount();j++){		
					 userTable.getColumn(j).pack();
		 		}	
				userTable.layout(true);
			}
		}
	}
	
	public class RemoveUserRole  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			TableItem[] items=userTable.getSelection();
			if(items!=null&&items.length>0){
				for(TableItem item:items){
					UserBean user=(UserBean)item.getData();
					 USERS.deleteGroup(user.getUserID(), user.getGroupID());
				}
			}
			refreshTable();
		}
	}
}
