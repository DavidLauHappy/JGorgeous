package business.dmanager.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import views.MenuView;

public class DManagerMenuView  extends MenuView {

	private static DManagerMenuView unique_instance;
	private Shell  parentShell=null;
	private Composite mainComposite=null;
	
	public static  DManagerMenuView getInstance(Shell shell){
		if(unique_instance==null){
			unique_instance=new DManagerMenuView(shell);
		}
		return unique_instance;
	}
	private DManagerMenuView(Shell shell)
	{
		parentShell=shell;
	}
	@Override
	public void createMenuView() {
		// TODO Auto-generated method stub
	}


}
