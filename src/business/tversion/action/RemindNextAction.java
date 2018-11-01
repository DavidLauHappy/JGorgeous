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
			//������������ǰ����
			User nextUser=this.users.get(0);
			bean.setCurrentUserID(nextUser.getUserID());
			bean.setProgress("6");//��ϸ����
			bean.setLastProgress("5");
			bean.reSetUptFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
			bean.submit();
			//��������������ͬ������
			List<TaskBean> reqs=TASK.getViewReqs(bean.getViewID());
			if(reqs!=null&&reqs.size()>0){
				for(TaskBean req:reqs){
					String seq=UUID.randomUUID().toString();
					seq=seq.replace("-", "");
					req.logStep(seq, Context.session.userID, "���԰���", "5");
					req.progress("6", nextUser.getUserID(), Context.session.userID);
					req.ResetReleaseFlag(TaskBean.ReleaseStatus.Apply.ordinal()+"");
				}
			}
			//֪ͨ��һ������
			String msg=Context.session.userName+"("+Context.session.userID+")��"+DateUtil.getCurrentTime()+"���䷢����"+bean.getViewName()+"������������ġ�";
			IMessage message=new IMessage(nextUser.getUserID(),msg);
			message.addMsg();
			String alterMsg="������"+name+"�����ɳɹ�!";
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
