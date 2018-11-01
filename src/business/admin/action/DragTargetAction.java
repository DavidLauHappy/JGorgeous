package business.admin.action;

import java.util.Map;

import model.USERS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import resource.Context;
import resource.Dictionary;
import utils.StringUtil;

import bean.UserBean;

public class DragTargetAction  implements DropTargetListener{
	public Table targetTable=null;
	private  Combo rolesControl=null;
	public Map<String,String> Roles;
	Map<String,String> Status=Dictionary.getDictionaryMap("USERS.STATUS");
	
	public DragTargetAction(Table table,Combo combo,Map roles){
		this.targetTable=table;
		this.rolesControl=combo;
		this.Roles=roles;
	}
	public void dropAccept(DropTargetEvent e) {
		if(e.detail==DND.DROP_DEFAULT)
			  e.detail=DND.DROP_COPY;
	}
	public void drop(DropTargetEvent event) {
		if(FileTransfer.getInstance().isSupportedType(event.currentDataType)){
			String[] users=(String[])event.data;
			if(users!=null&&users.length>0){
				for(String userID:users){
					  if(!this.checkExist(userID)){
						  UserBean user=USERS.getUser(userID);
						  TableItem tableItem=new TableItem(targetTable,SWT.BORDER);
						  String statusName=Status.get(user.getStatus());
						  tableItem.setText(new String[]{user.getUserID(),user.getUserName(),user.getEmail(),user.getPhone(),statusName,user.getApps()});
						  tableItem.setData(user);
						  //更新用户角色
						  String roleName=this.rolesControl.getText();
						  String roleID=Roles.get(roleName);
						  if(!StringUtil.isNullOrEmpty(roleID)){
							  USERS.setRole(userID, roleID,Context.session.userID);
						  }
					  }
				}
			}
		}
	}
	
	public boolean checkExist(String userID){
		TableItem[] items=targetTable.getItems();
		if(items!=null&&items.length>0){
			for(TableItem item:items){
				UserBean data=(UserBean)item.getData();
				if(data.getUserID().equals(userID))
					return true;
			}
		}
		return false;
	}
	public void dragOver(DropTargetEvent event) {
		event.feedback=DND.FEEDBACK_EXPAND|DND.FEEDBACK_SELECT;
	}
	public void dragOperationChanged(DropTargetEvent e) {
		if(e.detail==DND.DROP_DEFAULT)
			  e.detail=DND.DROP_COPY;
	}
	public void dragLeave(DropTargetEvent arg0) {
	}
	public void dragEnter(DropTargetEvent arg0) {
	}
	
}
