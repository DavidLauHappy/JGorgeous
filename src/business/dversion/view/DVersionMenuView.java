package business.dversion.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import views.MenuView;

public class DVersionMenuView extends MenuView {

	private static DVersionMenuView unique_instance;
	private Shell  parentShell=null;
	private Composite mainComposite=null;
	
	public static  DVersionMenuView getInstance(Shell shell){
		if(unique_instance==null){
			unique_instance=new DVersionMenuView(shell);
		}
		return unique_instance;
	}
	private DVersionMenuView(Shell shell)
	{
		parentShell=shell;
	}
	@Override
	public void createMenuView() {
		// TODO Auto-generated method stub
	}

}
