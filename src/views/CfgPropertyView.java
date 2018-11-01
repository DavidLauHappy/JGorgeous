package views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CONFIG;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Item;
import utils.LayoutUtils;
import utils.StringUtil;

import bean.ConfigBean;

public class CfgPropertyView {
	private  Shell property=null;
	private  ConfigBean data;
	private Point point=null;
	public Text textFile,textName,textValue=null;
	public Combo comboApp,comboEnv=null; 
	public Map<String,String> Apps=new HashMap<String, String>();
	public Map<String,String> App=Dictionary.getDictionaryMap("APP");
	public Map<String,String> Envs=new HashMap<String, String>();
	public void setData(ConfigBean data) {
		this.data = data;
	}
	
	public CfgPropertyView(){
		 this.point=AppView.getInstance().getDisplay().getCurrent().getCursorLocation();
	 }
	 
	 public void show(){
		 property=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		 property.setText(Constants.getStringVaule("window_cfgproperty"));
		 property.setLocation(point);
		 property.setLayout(LayoutUtils.getComGridLayout(1, 0));
		 Composite pannel=new Composite(property,SWT.V_SCROLL|SWT.BORDER);
			pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 360));
			pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Label labID=new Label(pannel,SWT.NONE);
			labID.setText(Constants.getStringVaule("header_appname"));
			labID.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
			comboApp=new Combo(pannel,SWT.DROP_DOWN);
			comboApp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
			List<Item> apps=Dictionary.getDictionaryList("APP");
		    if(apps!=null&&apps.size()>0){
		    	String[] items=new String[apps.size()];
		    	int i=0;
		    	for(Item app:apps){
		    		items[i]=app.getValue();
		    		i++;
		    		Apps.put(app.getValue(), app.getKey());
		    	}
		    	comboApp.setItems(items);
		    }
           ///////////////////////////////////////////////////////////////////////////////////////////////////////////
		    Label labEnv=new Label(pannel,SWT.NONE);
		    labEnv.setText(Constants.getStringVaule("header_environment"));
		    labEnv.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
		    comboEnv=new Combo(pannel,SWT.DROP_DOWN);
		    comboEnv.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
			List<Item> envs=Dictionary.getDictionaryList("CONFIG.ENV_TYPE");
		    if(envs!=null&&envs.size()>0){
		    	String[] items=new String[envs.size()];
		    	int i=0;
		    	for(Item env:envs){
		    		items[i]=env.getValue();
		    		i++;
		    		Envs.put(env.getValue(), env.getKey());
		    	}
		    	comboEnv.setItems(items);
		    }
			////////////////////////////////////////////////////////////////////////////
		    Label labFile=new Label(pannel,SWT.NONE);
		    labFile.setText(Constants.getStringVaule("header_configfile"));
		    labFile.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
		    textFile=new Text(pannel,SWT.BORDER);
		    textFile.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
			/////////////////////////////////////////////////////////////////////////////////////////
		    Label labName=new Label(pannel,SWT.NONE);
		    labName.setText(Constants.getStringVaule("header_variable"));
		    labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
		    textName=new Text(pannel,SWT.BORDER);
		    textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		    ////////////////////////////////////////////////////////////////////////////////////////////////
		    Label labValue=new Label(pannel,SWT.NONE);
		    labValue.setText(Constants.getStringVaule("header_value"));
		    labValue.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
		    textValue=new Text(pannel,SWT.BORDER|SWT.MULTI);
		    textValue.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 4, 1, 0, 0));
		    
		    
			Button btnOk=new Button(pannel,SWT.PUSH);
			btnOk.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1, 0, 0));
			btnOk.setText("   "+Constants.getStringVaule("btn_ok")+"    ");
			btnOk.addSelectionListener(new CfgModifyAction());
			
			Button btnCancle=new Button(pannel,SWT.PUSH);
			btnCancle.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1, 0, 0));
			btnCancle.setText("   "+Constants.getStringVaule("btn_cancel")+"    ");
			btnCancle.addSelectionListener(new CfgCancelAction());
			
			if(this.data!=null){
				int indexStatus=Integer.parseInt(this.data.getEnvType());
				comboEnv.select(indexStatus);
				comboEnv.setEnabled(false);
				String appName=App.get(this.data.getApp());
				String[] items=comboApp.getItems();
				if(items!=null&&items.length>0){
					for(int w=0;w<items.length;w++){
						if(appName.equals(items[w]))
							comboApp.select(w);
					}
				}
				comboApp.setEnabled(false);
				textFile.setText(this.data.getFileName());
				textFile.setEditable(false);
				textName.setText(this.data.getParamName());
				textName.setEditable(false);
				textValue.setText(this.data.getParamVal());
			}
			pannel.pack();
			 property.pack();
			 property.open();
			 property.addShellListener(new ShellCloseAction());
		 }
	 
	 public class ShellCloseAction extends ShellAdapter{
			public void shellClosed(ShellEvent e){	
				property.dispose();
			}	
		}
	 
	 public class CfgModifyAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				if(data!=null){
					String paramValue=textValue.getText();
					if(!StringUtil.isNullOrEmpty(paramValue)){
						data.setParamVal(paramValue);
						CONFIG.update(data);
					}
				}else{
					 String app=comboApp.getText();
					 String env=comboEnv.getText();
					 String file=textFile.getText();
					 String name=textName.getText();
					 String value=textValue.getText();
						if(StringUtil.isNullOrEmpty(app)||
								StringUtil.isNullOrEmpty(env)||	
								StringUtil.isNullOrEmpty(file)||
								StringUtil.isNullOrEmpty(name)||
								StringUtil.isNullOrEmpty(value)){
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
							box.open();	
						}
						else{
							if(name.startsWith("${")&&name.endsWith("}")){
								String appid=Apps.get(app);
								String envId=Envs.get(env);
								ConfigBean bean=new ConfigBean();
								bean.setApp(appid);
								bean.setCrtUser(Context.session.userID);
								bean.setEnvType(envId);
								bean.setFileName(file);
								bean.setMdfUser(Context.session.userID);
								bean.setParamName(name);
								bean.setParamVal(value);
								int count=CONFIG.add(bean);
								if(count>0){
									MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage(Constants.getStringVaule("alert_successoperate"));
									box.open();	
									property.dispose();
								}else{
									MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage("无法新增配置，请稍后尝试！");
									box.open();	
								}
							}else{
								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(Constants.getStringVaule("alert_errorformat")+"必须符合：${name}格式");
								box.open();	
							}
						}
				}
			}
	 }
	 
	 public class CfgCancelAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				if(data!=null){
					textValue.setText("");
				}else{
					 comboApp.setText("");
					 comboEnv.setText("");
					 textFile.setText("");
					 textName.setText("");
					 textValue.setText("");
				}
				property.dispose();
			}
	 }
}
