package business.dmanager.action;

import java.util.List;

import org.eclipse.swt.widgets.Text;

import resource.User;
import resource.UserChooseCallBack;

public class DMChooseUserAction implements UserChooseCallBack{
  private List<User> users;
  public void setUserList(List<User> users) {
    this.users = users;
  }
  
  private Text text;
  public DMChooseUserAction(Text text){
    this.text=text;
  }
  
  public boolean action(){
    if(this.users!=null&&this.users.size()>0){
      User  curUser=this.users.get(0);
      String userStr=curUser.getUserName()+" "+curUser.getUserID();
      text.setText(userStr);
    }
    return true;
  }
  
}
