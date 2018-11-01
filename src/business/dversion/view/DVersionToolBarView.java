package business.dversion.view;

import java.io.File;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import business.dmanager.view.DManagerToolBarView.AutoUpdateAction;


import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Paths;
import resource.QuestionManageView;


import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppAboutView;
import views.ConfigMngView;
import views.SettingView;
import views.ToolBarView;

public class DVersionToolBarView extends ToolBarView {
	public static DVersionToolBarView unique_instance;
	private Composite mainComposite=null;
	//private Button btnLoad;
	public void createToolBar() {
	

		/*Button btnAddStream=new Button(mainComposite,SWT.PUSH);
		btnAddStream.setImage(Icons.getAddStreamIcon());
		btnAddStream.setLayoutData(this.getToolsLayoutData());
		btnAddStream.setToolTipText(Constants.getStringVaule("btn_tips_addstream"));
		btnAddStream.addSelectionListener(new AddStreamAction());*/
		
		/*Button btnStream=new Button(mainComposite,SWT.PUSH);
		btnStream.setImage(Icons.getStreamIcon());
		btnStream.setLayoutData(this.getToolsLayoutData());
		btnStream.setToolTipText(Constants.getStringVaule("btn_tips_refreshstream"));
		btnStream.addSelectionListener(new RefreshStreamAction());*/
		
		Button btnAddRelease=new Button(mainComposite,SWT.PUSH);
		btnAddRelease.setImage(Icons.getAddReleaseIcon());
		btnAddRelease.setLayoutData(this.getToolsLayoutData());
		btnAddRelease.setToolTipText(Constants.getStringVaule("btn_tips_addrelease"));
		btnAddRelease.addSelectionListener(new AddReleaseAction());
		
		/*Button btnRelease=new Button(mainComposite,SWT.PUSH);
		btnRelease.setImage(Icons.getReleaseIcon());
		btnRelease.setLayoutData(this.getToolsLayoutData());
		btnRelease.setToolTipText(Constants.getStringVaule("btn_tips_refreshrelease"));
		btnRelease.addSelectionListener(new RefreshReleaseAction());*/
		
		Button btnFind=new Button(mainComposite,SWT.PUSH);
		btnFind.setToolTipText(Constants.getStringVaule("btn_tips_findReq"));
		btnFind.setImage(Icons.getFindIcon());
		btnFind.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnFind.addSelectionListener(new SearchReqAction());
		

		Button btnQuestion=new Button(mainComposite,SWT.PUSH);
		btnQuestion.setToolTipText(Constants.getStringVaule("btn_tips_question"));
		btnQuestion.setImage(Icons.getQustionIcon());
		btnQuestion.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnQuestion.addSelectionListener(new FindQuestionAction());

		
		Button btnSetting=new Button(mainComposite,SWT.PUSH);
		btnSetting.setImage(Icons.getSetIcon());
		btnSetting.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnSetting.setToolTipText(Constants.getStringVaule("btn_tips_setting"));
		btnSetting.addSelectionListener(new SettingAction());
		
		/*Button btnConfig=new Button(mainComposite,SWT.PUSH);
		btnConfig.setToolTipText(Constants.getStringVaule("btn_tips_cfgmng"));
		btnConfig.setImage(Icons.getCfgIcon());
		btnConfig.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnConfig.addSelectionListener(new ConfigMngAction());*/
		
		/* btnLoad=new Button(mainComposite,SWT.PUSH);
		 btnLoad.setToolTipText(Constants.getStringVaule("btn_tips_loading"));
		 btnLoad.setImage(Icons.getLoadingIcon());
		 btnLoad.setLayoutData(this.getToolsLayoutData());*/
		 
		    Button btnHelp=new Button(mainComposite,SWT.PUSH);
			btnHelp.setImage(Icons.getHelpIcon());
			btnHelp.setLayoutData(this.getToolsLayoutData());
			btnHelp.setToolTipText(Constants.getStringVaule("btn_tips_helpversion"));
			btnHelp.addSelectionListener(new AutoUpdateAction());
		 /*Button btnHelp=new Button(mainComposite,SWT.PUSH);
		 btnHelp.setImage(Icons.getHelpIcon());
		 btnHelp.setLayoutData(this.getToolsLayoutData());
		 btnHelp.setToolTipText(Constants.getStringVaule("btn_tips_manual"));
		 btnHelp.addSelectionListener(new ManualAction());
		 
		 Button btnAbout=new Button(mainComposite,SWT.PUSH);
		 btnAbout.setImage(Icons.getAboutIcon());
		 btnAbout.setLayoutData(this.getToolsLayoutData());
		 btnAbout.setToolTipText(Constants.getStringVaule("btn_tips_about"));
		 btnAbout.addSelectionListener(new AboutAction());*/
		 
		    Button btnUser=new Button(mainComposite,SWT.NONE);
			String tips=Context.session.userID+"("+Context.session.userName+")";
			String line="";
			Map<String, String> Apps=Dictionary.getDictionaryMap("APP");
			if(!StringUtil.isNullOrEmpty(Context.Apps)){
				String[] apps=Context.Apps.split("\\|");
				for(int w=0;w<apps.length;w++){
					String key=apps[w];
					String value=Apps.get(key);
					line+=key+"["+value+"]";
				}
				if(!StringUtil.isNullOrEmpty(line)){
					tips+="\r\n"+line;
				}
			}
			btnUser.setToolTipText(tips);
			btnUser.setImage(Icons.getMeIcon());
			btnUser.setLayoutData(LayoutUtils.getToolsLayoutData());
		unique_instance=this;
	}

	public DVersionToolBarView(Composite main,int style){
		mainComposite=new Composite(main,SWT.NONE);
		mainComposite.setLayout(this.getLayout());
		mainComposite.setLayoutData(this.getToolsLayoutData());
	}
	
	private  GridLayout getLayout(){
		GridLayout grid_row=new GridLayout();
		//grid_row.makeColumnsEqualWidth=true;
		grid_row.numColumns=15;
		grid_row.horizontalSpacing=10;
		grid_row.verticalSpacing=10;
		return grid_row;
	}
	
	public  GridData getToolsLayoutData(){
		GridData griddata_row=new GridData();
		griddata_row.horizontalAlignment=GridData.FILL;
		griddata_row.verticalAlignment=GridData.FILL;
		//griddata_row.grabExcessVerticalSpace=true;
		griddata_row.widthHint=64;
		griddata_row.heightHint=64;
		//垂直方向上布局的误差
		griddata_row.verticalIndent=-5;
		return griddata_row;
	}
	
/*	public void updateLoadStatus(){
		this.btnLoad.setImage(Icons.getLoadedIcon());
	}*/
	
	  public class AddStreamAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String item=Constants.getStringVaule("item_add_stream");
	   			if(!DVersionEditView.getInstance(null).getTabState(item)){
	   				NewStreamView streamView=new NewStreamView(DVersionEditView.getInstance(null).getTabFloder());
	   				DVersionEditView.getInstance(null).setTabItems(streamView.self, item);
	   			}
	   		}
	  }
	  
	  public class AddReleaseAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String item=Constants.getStringVaule("item_add_release");
	   			//先不管权限
	   			if(!DVersionEditView.getInstance(null).getTabState(item)){
	   				NewReleaseView releaseView=new NewReleaseView(DVersionEditView.getInstance(null).getTabFloder(),NewReleaseView.Type.New);
	   				DVersionEditView.getInstance(null).setTabItems(releaseView.self, item);
	   			}
	   		}
	  }
	  
	  public class SearchReqAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				String item=Constants.getStringVaule("btn_tips_findReq");
				if(!DVersionEditView.getInstance(null).getTabState(item)){
					AllReqSearchView newReqView=new AllReqSearchView(DVersionEditView.getInstance(null).getTabFloder());
					DVersionEditView.getInstance(null).setTabItems(newReqView.content, item);
				}
			}
	  }
	  
	  
	  public class  FindQuestionAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				String item=Constants.getStringVaule("btn_tips_question");
				if(!DVersionEditView.getInstance(null).getTabState(item)){
					QuestionManageView qdetailTest=new QuestionManageView(DVersionEditView.getInstance(null).getTabFloder());
					DVersionEditView.getInstance(null).setTabItems(qdetailTest.content, item);
				}
			}
	  }
	  
	  public class ConfigMngAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				String item=Constants.getStringVaule("btn_tips_cfgmng");
				if(!DVersionEditView.getInstance(null).getTabState(item)){
					ConfigMngView cfgmngView=new ConfigMngView(DVersionEditView.getInstance(null).getTabFloder());
					DVersionEditView.getInstance(null).setTabItems(cfgmngView.content, item);
				}
			}
		}
	  
	  
	  
	  public class SettingAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			SettingView.getInstance().show();
	   		}
	  }
	  

	  public class AutoUpdateAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			AppAboutView.getInstance().show();
	   		}
	  }
	
	  public class ManualAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String fileName=FileUtils.formatPath(Paths.getInstance().getBasePath())+File.separator+"Manual.pdf";
			  	int dot = fileName.lastIndexOf('.');
			 	   if (dot != -1) {
			 	     String extension = fileName.substring(dot);
			 	     Program program = Program.findProgram(extension);
			 	     if (program != null){
			 	    	program.launch(fileName);
			 	      }
			 	   }
	   		}
	  }
	  
	  public class AboutAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			AppAboutView.getInstance().show();
	   		}
	  }
}
