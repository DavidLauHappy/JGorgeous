package business.tester.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import views.MenuView;

public class TesterMenuView  extends MenuView {

	private static TesterMenuView unique_instance;
	private Shell  parentShell=null;
	private Composite mainComposite=null;
	
	public static  TesterMenuView getInstance(Shell shell){
		if(unique_instance==null){
			unique_instance=new TesterMenuView(shell);
		}
		return unique_instance;
	}
	private TesterMenuView(Shell shell)
	{
		parentShell=shell;
	}
	@Override
	public void createMenuView() {
		// TODO Auto-generated method stub
	}

}
