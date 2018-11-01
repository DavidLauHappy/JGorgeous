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

import bean.UserBean;

import resource.Dictionary;
import utils.StringUtil;

public class DragTargetGroupAction implements DropTargetListener{
	public Table targetTable=null;
	private  Combo groupsControl=null;
	public Map<String,String> Groups;
	
	Map<String,String> Status=Dictionary.getDictionaryMap("USERS.STATUS");
	
	public DragTargetGroupAction(Table table,Combo combo,Map groups){
		this.targetTable=table;
		this.groupsControl=combo;
		this.Groups=groups;
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
						  //更新用户角色
						  String roleName=this.groupsControl.getText();
						  String groupID=Groups.get(roleName);
						  if(!StringUtil.isNullOrEmpty(groupID)){
							  USERS.addGroup(userID, groupID);
						  }
						  user.setGroupID(groupID);
						  tableItem.setData(user);
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
