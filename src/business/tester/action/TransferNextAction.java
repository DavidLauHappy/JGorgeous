package business.tester.action;

import java.util.List;
import java.util.UUID;



import model.TASK;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;

import resource.Constants;
import resource.Context;
import resource.IMessage;
import resource.User;
import resource.UserChooseCallBack;
import utils.DateUtil;
import views.AppView;
import bean.TaskBean;
import bean.ViewBean;
import business.tversion.view.TVersionReleaseView;

public class TransferNextAction implements UserChooseCallBack{

	private List<User> users;
	private ViewBean bean;
	public Button btn1,btn2,btn3=null;
	public TransferNextAction(ViewBean bean,Button btn1,Button btn2,Button btn3){
		this.bean=bean;
		this.btn1=btn1;
		this.btn2=btn2;
		this.btn3=btn3;
	}
	
	
	public boolean action(){
		String name=bean.getViewName();
		if(this.users!=null&&this.users.size()>0){
			User nextUser=this.users.get(0);
			bean.setCurrentUserID(nextUser.getUserID());
			bean.submit();
			//发布关联的需求同步滚动
			List<TaskBean> reqs=TASK.getViewReqs(bean.getViewID());
			if(reqs!=null&&reqs.size()>0){
				for(TaskBean req:reqs){
					String seq=UUID.randomUUID().toString();
					seq=seq.replace("-", "");
					req.logStep(seq, Context.session.userID, "测试转处理", "6");
					req.progress("6", nextUser.getUserID(), Context.session.userID);
				}
			}
			//通知下一处理人
			String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"转处理发布【"+bean.getViewName()+"】给您。请查阅。";
			IMessage message=new IMessage(nextUser.getUserID(),msg);
			message.addMsg();
			String alterMsg="发布【"+name+"】转处理成功!";
			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
			box.setText(Constants.getStringVaule("messagebox_alert"));
			box.setMessage(alterMsg);
			box.open();
			TVersionReleaseView.getInstance(null).refreshTree();
			if(this.btn1!=null)
				btn1.setEnabled(false);
			if(this.btn2!=null)
				btn2.setEnabled(false);
			if(this.btn3!=null)
				btn3.setEnabled(false);
		}
       return true;
	}

	public void setUserList(List<User> users) {
		this.users = users;
	}
}
