package business.admin.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import views.MenuView;

public class AdminMenuView extends MenuView {
	private static AdminMenuView unique_instance;
	private Shell  parentShell=null;
	private Composite mainComposite=null;
	
	public static AdminMenuView getInstance(Shell shell){
		if(unique_instance==null)
			unique_instance=new AdminMenuView(shell);
			return unique_instance;
	}
	
	private AdminMenuView(Shell shell){
		parentShell=shell;
	}
	
	public void createMenuView() {
		// TODO Auto-generated method stub
	}
}
