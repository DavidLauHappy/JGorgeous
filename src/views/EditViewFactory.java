package views;

import org.eclipse.swt.widgets.Composite;

import business.admin.view.AdminEditView;
import business.aduitor.view.AuditorEditView;
import business.deploy.figures.DeployEditView;
import business.developer.view.DeveloperEditView;
import business.dmanager.view.DManagerEditView;
import business.dversion.view.DVersionEditView;
import business.tester.view.TesterEditView;
import business.tversion.view.TVersionEditView;

import resource.Constants;



/**
 * @author 作者 E-mail:
 * @date 2016-8-12
 * @version 1.0
 * 类说明
 */
public class EditViewFactory {
     public static EditView getEditView(Composite parent,String roleType){
    	 EditView editView=null;
    	 if(roleType.equals( Constants.RoleType.Deploy.ordinal()+"")){
    		 editView=DeployEditView.getInstance(parent);
   		}
    	 else if(roleType.equals( Constants.RoleType.Auditor.ordinal()+"")){
    			editView=AuditorEditView.getInstance(parent);
    		}
   		else if(roleType.equals( Constants.RoleType.TesterVersion.ordinal()+"")){
   			editView=TVersionEditView.getInstance(parent);
   		}
   		else if(roleType.equals( Constants.RoleType.Tester.ordinal()+"")){
   			editView=TesterEditView.getInstance(parent);
   		}
   		else if(roleType.equals( Constants.RoleType.DeveloperVersion.ordinal()+"")){
   			editView=DVersionEditView.getInstance(parent);
   		}
   		else if(roleType.equals( Constants.RoleType.Developer.ordinal()+"")){
   			editView=DeveloperEditView.getInstance(parent);
   		}
   		else if(roleType.equals( Constants.RoleType.DeveloperManager.ordinal()+"")){
   			editView=DManagerEditView.getInstance(parent);
   		}
   		else  if(roleType.equals( Constants.RoleType.Admin.ordinal()+"")){
   			editView=AdminEditView.getInstance(parent);
   		}
    	 return editView;
     }
}
