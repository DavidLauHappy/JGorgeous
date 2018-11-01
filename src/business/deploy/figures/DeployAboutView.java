package business.deploy.figures;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import resource.Constants;
import resource.Context;
import resource.Icons;
import utils.LayoutUtils;
import views.AppView;

public class DeployAboutView {
	public static DeployAboutView getInstance(){
		if(unique_instance==null){
			unique_instance=new DeployAboutView();
		}
		return unique_instance;
	}
	
	public void show(){
		About=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		About.setText("¹ØÓÚ"+Context.appName);
		Point point = AppView.getInstance().getCentreScreenPoint();
		About.setLocation(point);
		About.setLayout(LayoutUtils.getComGridLayout(1, 0));
		Composite pannel=new Composite(About,SWT.BORDER);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 200));
		pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
		Button image=new Button(pannel,SWT.IMAGE_ICO);
		image.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
		image.setImage(Icons.getAppIcon());
		Label labName=new Label(pannel,SWT.NONE);
		labName.setText(Context.appName);
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		Label labNameCopy=new Label(pannel,SWT.NONE);
		labNameCopy.setText(Constants.getStringVaule("label_namecopyright"));
		labNameCopy.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		
		Label labValueCopy=new Label(pannel,SWT.NONE);
		labValueCopy.setText(Constants.getStringVaule("label_valuecopyright"));
		labValueCopy.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		Label labNameContact=new Label(pannel,SWT.NONE);
		labNameContact.setText(Constants.getStringVaule("label_namecontact"));
		labNameContact.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		
		Label labValueContact=new Label(pannel,SWT.NONE);
		labValueContact.setText(Constants.getStringVaule("label_valuecontact"));
		labValueContact.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		Label labNameVersion=new Label(pannel,SWT.NONE);
		labNameVersion.setText(Constants.getStringVaule("label_nameversion"));
		labNameVersion.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		
		Label labValueVersion=new Label(pannel,SWT.NONE);
		labValueVersion.setText(Constants.getStringVaule("label_valueversion"));
		labValueVersion.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		Label labNameScope=new Label(pannel,SWT.NONE);
		labNameScope.setText(Constants.getStringVaule("label_namescope"));
		labNameScope.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
		
		Label labValueScope=new Label(pannel,SWT.NONE);
		labValueScope.setText(Constants.getStringVaule("label_valuescope"));
		labValueScope.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, true, false, 4, 1, 0, 0));
		
		pannel.pack();
		About.pack();
		About.open();
		About.addShellListener(new ShellCloseAction());
}

public class ShellCloseAction extends ShellAdapter{
	public void shellClosed(ShellEvent e){	
		About.dispose();
	}	
}
	private DeployAboutView(){}
	private static DeployAboutView unique_instance;
	private  Shell About=null;
}
