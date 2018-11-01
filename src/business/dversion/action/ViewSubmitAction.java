package business.dversion.action;

import java.util.List;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;

import bean.TaskBean;
import bean.VStep;
import bean.ViewBean;
import bean.ViewFileBean;
import business.dversion.view.DVersionRequirementView;
import business.dversion.view.ReleaseView;
import resource.Constants;
import resource.Context;
import resource.IMessage;
import resource.User;
import resource.UserChooseCallBack;
import utils.DateUtil;
import views.AppView;

public class ViewSubmitAction implements UserChooseCallBack{
	
	
	private List<User> users;
	public ViewBean view;
	private List<TaskBean> reqs;
	List<ViewFileBean> files;
	public Button btn1,btn2;
	public ViewSubmitAction(ViewBean view, List<TaskBean> reqs,List<ViewFileBean> files,Button btn1,Button btn2){
		this.view=view;
		this.reqs=reqs;
		this.files=files;
		this.btn1=btn1;
		this.btn2=btn2;
	}
	
	public boolean action(){
		if(this.users!=null&&this.users.size()>0){
			String  nextUserID=this.users.get(0).getUserID();
			view.sync();//ѡ����һ�����˺�Ĵ���������ص�����.�ƽ�����
			if(this.files!=null&&this.files.size()>0){
				view.createVersion();
				for(ViewFileBean file:files){
					file.setVersionID(view.getVersion());
					if((ViewFileBean.Status.New.ordinal()+"").equals(file.getStatus())){
						file.save();
						file.saveFileObj();
					}else{
						file.save();
					}
				}
			}
			view.setCurrentUserID(nextUserID);
			view.setProgress(VStep.VersionDispatch);//���԰���
			view.setLastProgress(VStep.VersionMake);
			view.submit();
			//�ƽ�����Ĳ��裬ͬʱ��¼
			if(this.reqs!=null&&this.reqs.size()>0){
				for(TaskBean req:reqs){
					view.addReq(req.getId());
					String seq=UUID.randomUUID().toString();
					seq=seq.replace("-", "");
					req.logStep(seq, Context.session.userID, "�汾����", VStep.VersionMake);
					req.progress(VStep.VersionDispatch, nextUserID, Context.session.userID);
				}
			}
			this.btn1.setEnabled(false);
			this.btn2.setEnabled(false);
			//֪ͨ��һ������
			String msg=Context.session.userName+"("+Context.session.userID+")��"+DateUtil.getCurrentTime()+"�ύ������"+view.getViewName()+"������ġ�";
			IMessage message=new IMessage(nextUserID,msg);
			message.addMsg();
			String alterMsg="���������ɹ���";
			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
			box.setText(Constants.getStringVaule("messagebox_alert"));
			box.setMessage(alterMsg);
			box.open();
			//ˢ�·��������
			DVersionRequirementView.getInstance(null).refreshTree();
			ReleaseView.getInstance(null).refreshTree();
		}
		return true;
	}
	
	public void setUserList(List<User> users) {
		this.users = users;
	}

}
