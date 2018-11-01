package business.tversion.action;

import java.util.List;
import java.util.UUID;

import model.TASK;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;

import bean.TaskBean;
import bean.ViewBean;
import business.tversion.view.TVersionReleaseView;
import resource.Constants;
import resource.Context;
import resource.IMessage;
import resource.User;
import resource.UserChooseCallBack;
import utils.DateUtil;
import views.AppView;

public class RemindNextAction implements UserChooseCallBack{

	private List<User> users;
	private ViewBean bean;
	private Button btn1,btn2,btn3;
	public RemindNextAction(ViewBean bean,Button btn1,Button btn2,Button btn3){
		this.bean=bean;
		this.btn1=btn1;
		this.btn2=btn2;
		this.btn3=btn3;
	}
	public boolean action(){
		String name=bean.getViewName();
		if(this.users!=null&&this.users.size()>0){
			//发布的流程向前滚动
			User nextUser=this.users.get(0);
			bean.setCurrentUserID(nextUser.getUserID());
			bean.setProgress("6");//详细测试
			bean.setLastProgress("5");
			bean.reSetUptFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
			bean.submit();
			//发布关联的需求同步滚动
			List<TaskBean> reqs=TASK.getViewReqs(bean.getViewID());
			if(reqs!=null&&reqs.size()>0){
				for(TaskBean req:reqs){
					String seq=UUID.randomUUID().toString();
					seq=seq.replace("-", "");
					req.logStep(seq, Context.session.userID, "测试安排", "5");
					req.progress("6", nextUser.getUserID(), Context.session.userID);
					req.ResetReleaseFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
				}
			}
			//通知下一处理人
			String msg=Context.session.userName+"("+Context.session.userID+")于"+DateUtil.getCurrentTime()+"分配发布【"+bean.getViewName()+"】给您。请查阅。";
			IMessage message=new IMessage(nextUser.getUserID(),msg);
			message.addMsg();
			String alterMsg="发布【"+name+"】分派成功!";
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
