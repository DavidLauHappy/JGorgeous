package business.deploy.figures;

import java.util.ArrayList;
import java.util.List;

import model.COMMAND;
import model.FUNC;
import model.NODESTEP;
import model.PKG;
import model.PKGSYSTEM;
import net.sf.json.JSONObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import common.core.Protocol;
import common.db.DataHelper;

import resource.CommonCallBack;
import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Paths;
import resource.UserChoose;
import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;
import views.UserApprove;
import bean.COMMANDBean;
import bean.FUNCBean;
import bean.PKGBean;
import bean.PKGSYSTEMBean;
import bean.SYSTEMBean;
import business.deploy.action.SubmitInstallApprAction;
import business.deploy.bean.StrategyData;
import business.deploy.core.ControlCommand;

/**
 * @author David
 *  版本针对节点的安装策略的复核
 */
public class StrategyView {
	private Composite parent;
	public Composite content=null;
	public Table reviewTable=null;
	public Button btnReview=null;
	private String versionID="";
	private SYSTEMBean group=null;
	private boolean choose=false;
	private int seqCmd=1;
	private String tabName;
	private String apprData;
	public StrategyView(Composite com,String version,SYSTEMBean data,String tabName){
		this.parent=com;
		this.versionID=version;
		this.group=data;
		this.tabName=tabName;
		this.show();
	}
	
	private void show(){
		content=new Composite(parent,SWT.BORDER);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
		Composite pannelAction=new Composite(content,SWT.NONE);
		pannelAction.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		pannelAction.setLayout(LayoutUtils.getComGridLayout(5, 0));
		   btnReview=new Button(pannelAction,SWT.PUSH);
		   btnReview.setText("       "+Constants.getStringVaule("btn_finalok")+"        ");
		   btnReview.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 4, 1, 0, 0));
		   btnReview.addSelectionListener(new InstallApplyAction());
		   pannelAction.pack();	
		 
		    Composite pannelData=new Composite(content,SWT.BORDER);
		    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		    reviewTable=new Table(pannelData,SWT.FULL_SELECTION);
		    reviewTable.setHeaderVisible(true);
		    reviewTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    reviewTable.setLinesVisible(true);
		    reviewTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		    String[] header=new String[]{ 
		    		Constants.getStringVaule("header_version"), 
		    		Constants.getStringVaule("header_action"),
					Constants.getStringVaule("header_node"),
					Constants.getStringVaule("header_path"),
					"      "+Constants.getStringVaule("header_action")+"      "
					};
			for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(reviewTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
					
			for(int j=0;j<header.length;j++){		
				reviewTable.getColumn(j).pack();
			}	
			List<StrategyData> datas=DataHelper.getStrategeCmdData(this.versionID,this.group.getBussID(),null);
			if(datas!=null&&datas.size()>0){
				List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
				for(StrategyData bean:datas){
					 TableItem tableItem=new TableItem(reviewTable,SWT.BORDER);
					 tableItem.setText(new String[]{bean.getVersion(),bean.getStepName(),bean.getShowNode(),bean.getTargetPath()});
					 tableItem.setData(bean);

					 JSONObject jsonObj=new JSONObject();
					 jsonObj.put("version", bean.getVersion());
					 jsonObj.put("step", bean.getStepName());
					 jsonObj.put("node", bean.getShowNode());
					 jsonObj.put("path", bean.getTargetPath());
					 
					 final TableEditor editorCheck=new TableEditor(reviewTable);
					 Button btnCheck=new Button(reviewTable,SWT.CHECK);
					 btnCheck.setImage(Icons.getOkIcon());
					 btnCheck.setText(Constants.getStringVaule("btn_ok"));
					 if("1".equals(bean.getFlag()))
						 btnCheck.setSelection(true);
					 else
						 btnCheck.setSelection(false);
					 btnCheck.addSelectionListener(new SelectionAdapter(){
							public void widgetSelected(SelectionEvent e){
								StrategyData data=(StrategyData)editorCheck.getItem().getData();
								Button btn=((Button)e.getSource());
								String flag="";
								if(btn.getSelection())
									flag="1";
								else
									flag="0";
								data.setFlag(flag);
								NODESTEP.setFlag(data.getVersion(), data.getGroupID(), data.getStepID(), data.getNodeID(), flag);
							}
					 });
					 editorCheck.grabHorizontal=true;
					 editorCheck.setEditor(btnCheck, tableItem, 4);
					 jsonObjects.add(jsonObj);
				}
				List<JSONObject> jsonHeaders= new ArrayList<JSONObject>();
				for(int j=0;j<header.length-1;j++){		
					 JSONObject jsonHeader=new JSONObject();
					 String name="";
					switch(j){
					case 0:name="version";break;
					case 1:name="step";break;
					case 2:name="node";break;
					case 3:name="path";break;
					default:break;
					}
					 jsonHeader.put("name", name);
					 jsonHeader.put("value", header[j]);
					 jsonHeaders.add(jsonHeader);
				}
				///////////////////////////////////////////////////
				JSONObject  json=new JSONObject();
				json.put("type", "Table");
				json.put("header", jsonHeaders);
				json.put("data", jsonObjects);
				apprData=json.toString();
			}
			for(int j=0;j<reviewTable.getColumnCount();j++){		
				reviewTable.getColumn(j).pack();
	 		}	
			pannelData.pack();
		    content.pack();
	}
	
	//复核之后的步骤
	public class InstallApplyAction extends SelectionAdapter{
 		public void widgetSelected(SelectionEvent e){
 			    Context.session.inroll(e.getClass().toString(), "F-001-00008");
 			   FUNCBean func=FUNC.getByID(Context.session.currentFunctionID);
 			    if((FUNCBean.Type.NoCheck.ordinal()+"").equals(func.getIsCheck())){
 			    	//直接生成治理
 			    	makeCommand();
 			    }
 			    else if((FUNCBean.Type.LocalCheck.ordinal()+"").equals(func.getIsCheck())){
 			    	//本地验证复合
 			    	UserApprove.getInstance().show(func.getName()+"复核");
 			    	UserApprove.getInstance().setOKCommonCallBack(new CommonCallBack() {
 			    		public boolean action(){
 			    			makeCommand();
 			    			return true;
 			    		}
 			    	});
 			    	UserApprove.getInstance().setCancelCommonCallBack(new CommonCallBack() {
 			    		public boolean action(){
 			    			MessageBox alertbox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
 							alertbox.setText(Constants.getStringVaule("messagebox_alert"));
 							alertbox.setMessage("本地授权未通过，请重新配置");
 							alertbox.open();	
 			    			return true;
 			    		}
 			    	});
 			    }else{
 			    	//远程审批，选择审批人提交审批
 			    	SubmitInstallApprAction action=new SubmitInstallApprAction(Context.session.userID,"F-001-00008",apprData,versionID,group.getBussID());
 		   			UserChoose control=new UserChoose(action);
 			    }
 			   DeployEditView.getInstance(null).closeTab(tabName);
 		}
	}
	
	
	public void makeCommand(){
			String approved="";
			MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.YES|SWT.NO);
			box.setText(Constants.getStringVaule("messagebox_alert"));
			box.setMessage(Constants.getStringVaule("alert_successoperate")+Constants.getStringVaule("alert_config"));
			int choice=	box.open();	
			if(choice==SWT.YES){
			    ControlCommand.command2Db(versionID,group.getBussID(),approved,COMMANDBean.Flag.On);
			    PKG.setStatus(versionID, PKGBean.Status.Config.ordinal()+"", Context.session.userID);
			    PKGSYSTEMBean pkgSystem=new PKGSYSTEMBean();
			    pkgSystem.setPkgID(versionID);
			    pkgSystem.setSystemID(group.getBussID());
			    pkgSystem.setStatus(PKGSYSTEMBean.Status.Ready.ordinal()+"");
			    pkgSystem.setMdfUser(Context.session.userID);
			    PKGSYSTEM.getAdd(pkgSystem);
			    //根据策略中节点+步骤，重新对指令开关进行控制
			    List<StrategyData> datas=DataHelper.getStrategeCmdData(this.versionID,this.group.getBussID(),null);
			    if(datas!=null&&datas.size()>0){
			    	for(StrategyData bean:datas){
			    		if("0".equals(bean.getFlag())){
			    			COMMAND.reSetFlag(bean.getVersion(), bean.getNodeID(), bean.getStepID(), bean.getFlag());
			    		}
			    	}
			    }
				DeployEditView.getInstance(null).closeTab(tabName);
				MessageBox alertbox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				alertbox.setText(Constants.getStringVaule("messagebox_alert"));
				alertbox.setMessage("配置策略完成！可点击图标开始安装版本包。");
			}
	}
}
