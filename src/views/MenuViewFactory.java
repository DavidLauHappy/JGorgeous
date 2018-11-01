package views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import business.admin.view.AdminMenuView;
import business.aduitor.view.AuditorMenuView;
import business.deploy.figures.DeployMenuView;
import business.developer.view.DeveloperMenuView;
import business.dmanager.view.DManagerMenuView;
import business.dversion.view.DVersionMenuView;
import business.tester.view.TesterMenuView;
import business.tversion.view.TVersionMenuView;
import resource.Constants;

/**
 * @author DavidLau
 * @date 2016-8-11
 * @version 1.0
 * ÀàËµÃ÷
 */
public class MenuViewFactory {
	 public static MenuView getMenuView(Shell shell, Composite com,int style,String roleType){
		 MenuView menuview=null;
		if(roleType.equals( Constants.RoleType.Deploy.ordinal()+"")){
			menuview=DeployMenuView.getInstance(shell);
		}
		else if(roleType.equals( Constants.RoleType.Auditor.ordinal()+"")){
			menuview=AuditorMenuView.getInstance(shell);
		}
		else if(roleType.equals( Constants.RoleType.TesterVersion.ordinal()+"")){
			menuview= TVersionMenuView.getInstance(shell);
		}
		else if(roleType.equals( Constants.RoleType.Tester.ordinal()+"")){
			menuview= TesterMenuView.getInstance(shell);
		}
		else if(roleType.equals( Constants.RoleType.DeveloperVersion.ordinal()+"")){
			menuview= DVersionMenuView.getInstance(shell);
		}
		else if(roleType.equals( Constants.RoleType.Developer.ordinal()+"")){
			menuview= DeveloperMenuView.getInstance(shell);
		}
		else if(roleType.equals( Constants.RoleType.DeveloperManager.ordinal()+"")){
			menuview= DManagerMenuView.getInstance(shell);
		}
		else if(roleType.equals( Constants.RoleType.Admin.ordinal()+"")){
			menuview= AdminMenuView.getInstance(shell);
		}
		return menuview;
	 }
	 
	 
	
}
