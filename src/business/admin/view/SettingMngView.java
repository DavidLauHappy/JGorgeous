package business.admin.view;

import java.util.List;

import model.SYSPARAMETER;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import resource.Constants;
import resource.Context;

import bean.SystemParamBean;

import utils.LayoutUtils;
import views.AppView;

public class SettingMngView {
	private Composite parent;
	public Composite content;
	public Composite basicPannel;
	public SettingMngView(Composite com){
		this.parent=com;
		this.createAndShow();
	}
	
	private void createAndShow(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(2, 10));
	    
		basicPannel=new Composite(content,SWT.NONE);
	    basicPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 2, 1, 0, 0));
	    basicPannel.setLayout(LayoutUtils.getComGridLayout(4, 0));
		   
		List<SystemParamBean> parameters=SYSPARAMETER.getParameters();
		if(parameters!=null&&parameters.size()>0){
			for(SystemParamBean bean:parameters){
				Label labName=new Label(basicPannel,SWT.NONE);
				labName.setText(bean.getDesc());
				labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
				labName.setData("$Type", "Label");
				Text textName=new Text(basicPannel,SWT.BORDER);
				textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1, 0, 0));
				textName.setText(bean.getValue());
				textName.setData(bean);
				textName.setData("$Type", "Text");
			}
		}
		basicPannel.pack();
		
		Button btnOK=new Button(content,SWT.PUSH);
		btnOK.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnOK.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
		btnOK.addSelectionListener(new SettingOKAction());
		
		Button btnCancel=new Button(content,SWT.PUSH);
		btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
		btnCancel.addSelectionListener(new SettingCancelAction());
		content.pack();
	}
	
	public class SettingOKAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Control[] controls=basicPannel.getChildren();
			for(Control control:controls){
				String type=(String)control.getData("$Type");
				if("Text".equals(type)){
					Text text=(Text)control;
					  SystemParamBean bean=(SystemParamBean)text.getData();
					  bean.setValue(text.getText());
					  SYSPARAMETER.updateParameter(bean.getName(), text.getText(), Context.session.userID);
				}
			}
			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
			box.setText(Constants.getStringVaule("messagebox_alert"));
			box.setMessage(Constants.getStringVaule("alert_successoperate"));
			box.open();	
		}
	}
	
	public class SettingCancelAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Control[] controls=basicPannel.getChildren();
			for(Control control:controls){
				String type=(String)control.getData("$Type");
				if("Text".equals(type)){
					Text text=(Text)control;
					text.setText("");
				}
			}
		}
	}
}
