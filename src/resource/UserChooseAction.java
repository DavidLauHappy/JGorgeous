package resource;

import java.util.List;

import org.eclipse.swt.widgets.Text;

public class UserChooseAction  implements UserChooseCallBack{
			private List<User> users;
			public void setUserList(List<User> users) {
				this.users = users;
			}
			
			
			public boolean action(){
				if(this.users!=null&&this.users.size()>0){
					User singleUser=this.users.get(0);
					this.source.setText(singleUser.getShowName());
				}
				return true;
			}
			
			//只能单选一个人
			private Text source;
			public UserChooseAction(Text source){
				this.source=source;
			}
}
