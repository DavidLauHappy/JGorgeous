package business.dversion.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import bean.ViewVersionBean;
import resource.Constants;
import utils.LayoutUtils;
import views.AppView;

public class VersionPropertyView {

	private ViewVersionBean data;
	private Point position=null;
	private  Shell property=null;
	
	public VersionPropertyView(ViewVersionBean data){
		this.data=data;
	}
	
	public  void showPropertyView(){
		property=new Shell(AppView.getInstance().getShell(),SWT.RESIZE|SWT.CLOSE|SWT.SYSTEM_MODAL);
		property.setText(Constants.getStringVaule("window_detail"));
		property.setLocation(this.position);
		property.setLayout(LayoutUtils.getComGridLayout(1, 0));
		Composite pannel=new Composite(property,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 300));
		pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		Label labName=new Label(pannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_name"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		Text textName=new Text(pannel,SWT.BORDER);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		textName.setText(data.getViewName());
		textName.setEditable(false);
		
		Label labDesc=new Label(pannel,SWT.NONE);
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, true, 2, 1, 0, 0));
		Text textDesc=new Text(pannel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, true, 4, 1, 0, 0));
		textDesc.setText(data.getViewDesc());
		textDesc.setEditable(false);
		
		Label labUser=new Label(pannel,SWT.NONE);
		labUser.setText(Constants.getStringVaule("label_mdfuser"));
		labUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		Text textUser=new Text(pannel,SWT.BORDER);
		textUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		textUser.setText(data.getUptUser());
		textUser.setEditable(false);
		
		Label labTime=new Label(pannel,SWT.NONE);
		labTime.setText(Constants.getStringVaule("label_mdftime"));
		labTime.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		Text textTime=new Text(pannel,SWT.BORDER);
		textTime.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, false, 4, 1, 0, 0));
		textTime.setText(data.getUptTime());
		textTime.setEditable(false);
		pannel.pack();
		property.pack();
		property.open();
		property.addShellListener(new ShellCloseAction());
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			property.dispose();
		}
	}

	public void setData(ViewVersionBean data) {
		this.data = data;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
	
	
}
