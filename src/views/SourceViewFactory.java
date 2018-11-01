package views;

import org.eclipse.swt.widgets.Composite;

import resource.Constants;
import business.admin.view.AdminSourceView;
import business.aduitor.view.AuditorSourceView;
import business.deploy.figures.DeploySourceView;
import business.developer.view.DeveloperSourceView;
import business.dmanager.view.DManagerSourceView;
import business.dversion.view.DVersionSourceView;
import business.tester.view.TesterSourceView;
import business.tversion.view.TVersionSourceView;

/**
 * @author DavidLau
 * @date 2016-8-11
 * @version 1.0
 * ÀàËµÃ÷
 */
public class SourceViewFactory {
  public static SourceView getSourceView(Composite parent,String roleType){
	  SourceView sourceView=null;
	  if(roleType.equals( Constants.RoleType.Deploy.ordinal()+"")){
		  sourceView=DeploySourceView.getInstance(parent);
		}
	  else if(roleType.equals( Constants.RoleType.Auditor.ordinal()+"")){
			sourceView=AuditorSourceView.getInstance(parent);
		}
		else if(roleType.equals( Constants.RoleType.TesterVersion.ordinal()+"")){
			sourceView=TVersionSourceView.getInstance(parent);
		}
		else if(roleType.equals( Constants.RoleType.Tester.ordinal()+"")){
			sourceView=TesterSourceView.getInstance(parent);
		}
		else if(roleType.equals( Constants.RoleType.DeveloperVersion.ordinal()+"")){
			sourceView=DVersionSourceView.getInstance(parent);
		}
		else if(roleType.equals( Constants.RoleType.Developer.ordinal()+"")){
			sourceView=DeveloperSourceView.getInstance(parent);
		}
		else if(roleType.equals( Constants.RoleType.DeveloperManager.ordinal()+"")){
			sourceView=DManagerSourceView.getInstance(parent);
		}	
		else if(roleType.equals( Constants.RoleType.Admin.ordinal()+"")){
			sourceView=AdminSourceView.getInstance(parent);
		}	
	  return sourceView;
  }
}
