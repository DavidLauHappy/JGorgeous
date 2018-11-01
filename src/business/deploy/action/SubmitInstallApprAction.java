package business.deploy.action;

import java.util.List;

import model.FLOWDETAIL;
import model.PKGSYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import bean.COMMANDBean;
import business.deploy.core.ControlCommand;
import resource.Constants;
import resource.Context;
import resource.User;
import resource.UserChooseCallBack;
import utils.DateUtil;
import views.AppView;

public class SubmitInstallApprAction implements UserChooseCallBack{
	private List<User> users;
	private String userID;
	private String functionID;
	private String json;
	private String versionID;
	private String systemID;
	
	
	public SubmitInstallApprAction(String userID, String functionID, String json,String versionID,String systemID) {
		super();
		this.userID = userID;
		this.functionID = functionID;
		this.json = json;
		this.versionID=versionID;
		this.systemID=systemID;
	}
	
	public boolean action(){
		if(this.users!=null&&this.users.size()>0){
			if(PKGSYSTEM.isPkgSysExist(versionID, systemID, Context.session.userID)){
				    MessageBox alertbox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK|SWT.CANCEL);
					alertbox.setText(Constants.getStringVaule("messagebox_alert"));
					alertbox.setMessage("版本["+versionID+"]在系统["+systemID+"]已经安排部署，是否需要重新部署？");
					int choice=alertbox.open();	
					if(SWT.OK==choice){
						String apprID="";
						for(User user:this.users){
							String currentTime=DateUtil.getCurrentTime();
							String userID=user.getUserID();
							//提交审批使用通用接口
							apprID=FLOWDETAIL.approveSubmit(this.functionID,userID,this.json," ");
						}
						ControlCommand.command2Db(versionID,systemID,apprID,COMMANDBean.Flag.Off);
						//审批记录记录到本地
						MessageBox msgBox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						msgBox.setText(Constants.getStringVaule("messagebox_alert"));
						msgBox.setMessage("审批["+apprID+"]提交成功，请耐心等待！");
						msgBox.open();	
					}
			}
			else{
				String apprID="";
				for(User user:this.users){
					String currentTime=DateUtil.getCurrentTime();
					String userID=user.getUserID();
					//提交审批使用通用接口
					apprID=FLOWDETAIL.approveSubmit(this.functionID,userID,this.json," ");
				}
				ControlCommand.command2Db(versionID,systemID,apprID,COMMANDBean.Flag.Off);
				//审批记录记录到本地
				MessageBox alertbox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				alertbox.setText(Constants.getStringVaule("messagebox_alert"));
				alertbox.setMessage("审批["+apprID+"]提交成功，请耐心等待！");
				alertbox.open();	
			}
			
		}
		return true;
	}
	public void setUserList(List<User> users) {
		this.users = users;
	}
}
