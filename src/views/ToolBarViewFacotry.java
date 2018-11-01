package views;

import org.eclipse.swt.widgets.Composite;

import business.admin.view.AdminToolBarView;
import business.aduitor.view.AuditorToolBarView;
import business.deploy.figures.DeployToolBarView;
import business.developer.view.DeveloperToolBarView;
import business.dmanager.view.DManagerToolBarView;
import business.dversion.view.DVersionToolBarView;
import business.tester.view.TesterToolBarView;
import business.tversion.view.TVersionToolBarView;

import resource.Constants;


/**
 * @author DavidLau
 * @date 2016-8-11
 * @version 1.0
 * ÀàËµÃ÷
 */
public class ToolBarViewFacotry {

	public static ToolBarView getToolBarView(Composite main,int style,String roleType){
		ToolBarView toolBar=null;
		if(roleType.equals( Constants.RoleType.Deploy.ordinal()+"")){
			toolBar=new DeployToolBarView(main,style);
		}
		else if(roleType.equals( Constants.RoleType.Auditor.ordinal()+"")){
			toolBar=new AuditorToolBarView(main,style);
		}
		else if(roleType.equals( Constants.RoleType.TesterVersion.ordinal()+"")){
			toolBar=new TVersionToolBarView(main,style);
		}
		else if(roleType.equals( Constants.RoleType.Tester.ordinal()+"")){
			toolBar=new TesterToolBarView(main,style);
		}
		else if(roleType.equals( Constants.RoleType.Developer.ordinal()+"")){
			toolBar=new DeveloperToolBarView(main,style);
		}
		else if(roleType.equals( Constants.RoleType.DeveloperManager.ordinal()+"")){
			toolBar=new DManagerToolBarView(main,style);
		}
		else if(roleType.equals( Constants.RoleType.DeveloperVersion.ordinal()+"")){
			toolBar=new DVersionToolBarView(main,style);
		}else if(roleType.equals( Constants.RoleType.Admin.ordinal()+"")){
			toolBar=new AdminToolBarView(main,style);
		}
		return toolBar;
	}
	
}
