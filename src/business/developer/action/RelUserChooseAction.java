package business.developer.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Text;

import resource.User;
import resource.UserChooseCallBack;
import utils.StringUtil;

public class RelUserChooseAction implements UserChooseCallBack{
	
	private Text textRelUsers;
	public RelUserChooseAction(Text text){
		this.textRelUsers=text;
	}
	
	
	private List<User> users;
	public void setUserList(List<User> users) {
		this.users = users;
	}
	
	public boolean action(){
		if(this.users!=null&&this.users.size()>0){
			 String userID="";
			 List<User> chooseUsers=new ArrayList<User>();
			 for(User user:this.users){
				 userID+=user.getShowName()+",";
				 chooseUsers.add(user);
			 }
			 userID=StringUtil.rtrim(userID, ",");
			 textRelUsers.setText(userID); 
			 textRelUsers.setData(chooseUsers);
		}
		return true;
	}
}
