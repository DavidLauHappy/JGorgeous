package business.developer.view;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import views.MenuView;
public class DeveloperMenuView extends MenuView {

	private static DeveloperMenuView unique_instance;
	private Shell  parentShell=null;
	private Composite mainComposite=null;
	
	public static  DeveloperMenuView getInstance(Shell shell){
		if(unique_instance==null){
			unique_instance=new DeveloperMenuView(shell);
		}
		return unique_instance;
	}
	private DeveloperMenuView(Shell shell)
	{
		parentShell=shell;
	}
	@Override
	public void createMenuView() {
		// TODO Auto-generated method stub
	}
}