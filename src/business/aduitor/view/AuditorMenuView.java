package business.aduitor.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import views.MenuView;

public class AuditorMenuView extends MenuView {
		private static AuditorMenuView unique_instance;
		private Shell  parentShell=null;
		private Composite mainComposite=null;
		
		public static AuditorMenuView getInstance(Shell shell){
			if(unique_instance==null)
				unique_instance=new AuditorMenuView(shell);
				return unique_instance;
		}
		
		private AuditorMenuView(Shell shell){
			parentShell=shell;
		}
		
		public void createMenuView() {
			// TODO Auto-generated method stub
		}

}
