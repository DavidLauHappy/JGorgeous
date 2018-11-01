package business.tester.view;



import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import business.developer.view.DeveloperToolBarView.AutoUpdateAction;
import business.tversion.view.MakeView;
import business.tversion.view.TVersionDirMapView;

import resource.BackDbView;
import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.QuestionManageView;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppAboutView;
import views.ConfigMngView;
import views.SettingView;
import views.ToolBarView;

public class TesterToolBarView extends ToolBarView {

	public static TesterToolBarView unique_instance;
	//private Button btnLoad;
	public TesterToolBarView(Composite main,int style){   
		mainComposite=new Composite(main,SWT.NONE);
		mainComposite.setLayout(this.getLayout());
		mainComposite.setLayoutData(this.getToolsLayoutData());
	}
	public void createToolBar() {

		Button btnStream=new Button(mainComposite,SWT.PUSH);
		btnStream.setImage(Icons.getStreamIcon());
		btnStream.setLayoutData(this.getToolsLayoutData());
		btnStream.setToolTipText(Constants.getStringVaule("btn_tips_refreshstream"));
		btnStream.addSelectionListener(new RefreshStreamAction());
		
		Button btnRelease=new Button(mainComposite,SWT.PUSH);
		btnRelease.setImage(Icons.getReleaseIcon());
		btnRelease.setLayoutData(this.getToolsLayoutData());
		btnRelease.setToolTipText(Constants.getStringVaule("btn_tips_refreshrelease"));
		btnRelease.addSelectionListener(new RefreshReleaseAction());
		
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
		
		Button btnLocalNode=new Button(mainComposite,SWT.PUSH);
		btnLocalNode.setToolTipText(Constants.getStringVaule("btn_tips_localnode"));
		btnLocalNode.setImage(Icons.getLocalNodeIcon());
		btnLocalNode.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnLocalNode.addSelectionListener(new LocalNodeAction());
		
		
		/*Button btnConfig=new Button(mainComposite,SWT.PUSH);
		btnConfig.setToolTipText(Constants.getStringVaule("btn_tips_cfgmng"));
		btnConfig.setImage(Icons.getCfgIcon());
		btnConfig.setLayoutData(LayoutUtils.getToolsLayoutData());
		btnConfig.addSelectionListener(new ConfigMngAction());*/
		
		Button btnSetting=new Button(mainComposite,SWT.PUSH);
		btnSetting.setImage(Icons.getSetIcon());
		btnSetting.setLayoutData(this.getToolsLayoutData());
		btnSetting.setToolTipText(Constants.getStringVaule("btn_tips_setting"));
		btnSetting.addSelectionListener(new SettingAction());
		
		
		
		/*btnLoad=new Button(mainComposite,SWT.PUSH);
		btnLoad.setToolTipText(Constants.getStringVaule("btn_tips_loading"));
		btnLoad.setImage(Icons.getLoadingIcon());
		btnLoad.setLayoutData(this.getToolsLayoutData());*/
		
		Button btnMake=new Button(mainComposite,SWT.PUSH);
		btnMake.setImage(Icons.getMakeIcon());
		btnMake.setLayoutData(this.getToolsLayoutData());
		btnMake.setToolTipText(Constants.getStringVaule("btn_make"));
		btnMake.addSelectionListener(new MakeVersionAction());	
		
		Button btnDir=new Button(mainComposite,SWT.PUSH);
		btnDir.setImage(Icons.getDirMapIcon());
		btnDir.setLayoutData(this.getToolsLayoutData());
		btnDir.setToolTipText(Constants.getStringVaule("btn_tips_versiondir"));
		btnDir.addSelectionListener(new DirMapSettingAction());
		
		
		Button btnBackDb=new Button(mainComposite,SWT.PUSH);
		btnBackDb.setImage(Icons.getDbbackupIcon());
		btnBackDb.setLayoutData(this.getToolsLayoutData());
		btnBackDb.setToolTipText(Constants.getStringVaule("btn_tips_dbbackup"));
		btnBackDb.addSelectionListener(new BackDbAction());
		/* Button btnHelp=new Button(mainComposite,SWT.PUSH);
		 btnHelp.setImage(Icons.getHelpIcon());
		 btnHelp.setLayoutData(this.getToolsLayoutData());
		 btnHelp.setToolTipText(Constants.getStringVaule("btn_tips_manual"));
		 btnHelp.addSelectionListener(new ManualAction());*/
		 
		/* Button btnAbout=new Button(mainComposite,SWT.PUSH);
		 btnAbout.setImage(Icons.getAboutIcon());
		 btnAbout.setLayoutData(this.getToolsLayoutData());
		 btnAbout.setToolTipText(Constants.getStringVaule("btn_tips_about"));
		 btnAbout.addSelectionListener(new AboutAction());
		 */
		
		Button btnHelp=new Button(mainComposite,SWT.PUSH);
		btnHelp.setImage(Icons.getHelpIcon());
		btnHelp.setLayoutData(this.getToolsLayoutData());
		btnHelp.setToolTipText(Constants.getStringVaule("btn_tips_helpversion"));
		btnHelp.addSelectionListener(new AutoUpdateAction());
		
		
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
	
	 public class SettingAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			SettingView.getInstance().show();
	   		}
	  }
	
	  public class RefreshReleaseAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String name=Constants.getStringVaule("tabItem_release");
	   			TesterSourceView.getInstance(null).setReleaseTabTop(name);
	   		}
	  }
	  
		
		 public class SearchReqAction extends SelectionAdapter{
		   		public void widgetSelected(SelectionEvent e){
		   			String name=Constants.getStringVaule("btn_tips_findReq");
		   			if(!TesterEditView.getInstance(null).getTabState(name)){
		   				TesterReqSearchView reqView=new TesterReqSearchView(TesterEditView.getInstance(null).getTabFloder());
						TesterEditView.getInstance(null).setTabItems(reqView.content, name);
					}
		   		}
		 }
		 
		 public class LocalNodeAction extends SelectionAdapter{
		   		public void widgetSelected(SelectionEvent e){
		   			String name=Constants.getStringVaule("btn_tips_localnode");
		   			if(!TesterEditView.getInstance(null).getTabState(name)){
		   				TLocalNodeView tlnv=TLocalNodeView.getInstance(TesterEditView.getInstance(null).getTabFloder());
		   				TesterEditView.getInstance(null).setTabItems(tlnv.content,name);
		   			}
		   		}
		 }
		 
		 
		public class ConfigMngAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				String item=Constants.getStringVaule("btn_tips_cfgmng");
				if(!TesterEditView.getInstance(null).getTabState(item)){
					ConfigMngView cfgmngView=new ConfigMngView(TesterEditView.getInstance(null).getTabFloder());
					TesterEditView.getInstance(null).setTabItems(cfgmngView.content, item);
				}
			}
		}
		
		  public class MakeVersionAction extends SelectionAdapter{
		   		public void widgetSelected(SelectionEvent e){
		   			String item=Constants.getStringVaule("tabItem_vermake");
		   			if(!TesterEditView.getInstance(null).getTabState(item)){
		   				MakeView makeView=new MakeView(TesterEditView.getInstance(null).getTabFloder());
		   				TesterEditView.getInstance(null).setTabItems(makeView.selfContent, item);
		   			}
		   		}
		  }
		
		  public class DirMapSettingAction extends SelectionAdapter{
		   		public void widgetSelected(SelectionEvent e){
		   			String item=Constants.getStringVaule("btn_tips_versiondir");
		   			if(!TesterEditView.getInstance(null).getTabState(item)){
		   				TVersionDirMapView dirMapView=new TVersionDirMapView(TesterEditView.getInstance(null).getTabFloder());
		   				TesterEditView.getInstance(null).setTabItems(dirMapView.content, item);
		   			}
		   		}
		  }
		  
		  public class BackDbAction extends SelectionAdapter{
		   		public void widgetSelected(SelectionEvent e){
		   			String item=Constants.getStringVaule("btn_tips_dbbackup");
		   			if(!TesterEditView.getInstance(null).getTabState(item)){
		   				BackDbView bdv=BackDbView.getInstance(TesterEditView.getInstance(null).getTabFloder());
		   				TesterEditView.getInstance(null).setTabItems(bdv.content, item);
		   			}
		   		}
		  }
		  
		  
		public class  FindQuestionAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				String item=Constants.getStringVaule("btn_tips_question");
				if(!TesterEditView.getInstance(null).getTabState(item)){
					QuestionManageView qdetailTest=new QuestionManageView(TesterEditView.getInstance(null).getTabFloder());
					TesterEditView.getInstance(null).setTabItems(qdetailTest.content, item);
				}
			}
	  }
		
	  public class RefreshStreamAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String name=Constants.getStringVaule("tabItem_stream");
	   			TesterSourceView.getInstance(null).setStreamTabTop(name);
	   		}
	  }
	  
	  public class AutoUpdateAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			AppAboutView.getInstance().show();
	   		}
	  }
	  
	  public class ManualAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			/*String fileName=FileUtils.formatPath(Paths.getInstance().getBasePath())+File.separator+"Manual.pdf";
			  	int dot = fileName.lastIndexOf('.');
			 	   if (dot != -1) {
			 	     String extension = fileName.substring(dot);
			 	     Program program = Program.findProgram(extension);
			 	     if (program != null){
			 	    	program.launch(fileName);
			 	      }
			 	   }*/
	   			/*String item="ver20171122084923335";
				if(!TesterEditView.getInstance(null).getTabState(item)){
					TDiagramView diagramView=new TDiagramView(TesterEditView.getInstance(null).getTabFloder(),item);
					TesterEditView.getInstance(null).setTabItems(diagramView.content, item);
				}*/
	   		}
	  }
	  
	  public class AboutAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			AppAboutView.getInstance().show();
	   		}
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
	private Composite mainComposite=null;
	
	/*public void updateLoadStatus(){
		this.btnLoad.setImage(Icons.getLoadedIcon());
	}*/
}
