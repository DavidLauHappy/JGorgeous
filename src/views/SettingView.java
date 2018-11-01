package views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import common.localdb.LocalDataHelper;
import bean.Triple;
import resource.AppInit;
import resource.Constants;
import resource.UserChoose;
import utils.LayoutUtils;
import utils.StringUtil;

/**
 * @author DavidLau
 * @date 2016-8-10
 * @version 1.0
 * 类说明
 */
public class SettingView {

	public static SettingView getInstance(){
		if(unique_instance==null){
			unique_instance=new SettingView();
		}
		return unique_instance;
	}
	
	public Text textUser=null;
	public void show(){
		Setting=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		Setting.setText(Constants.getStringVaule("window_sysparam"));
		Point point = AppView.getInstance().getDisplay().getCurrent().getCursorLocation();
		Setting.setLocation(point);
		Setting.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
		
		
		Data=new HashMap<String,Control>();
		//根据数据来动态构建页面
		 List<Triple> parameters=LocalDataHelper.getParameters();
		    int height=100+parameters.size()*30;
		 	Composite pannel=new Composite(Setting,SWT.V_SCROLL|SWT.BORDER);
			pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, height));
			pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
			Label labUser=new Label(pannel,SWT.NONE);
			labUser.setText(Constants.getStringVaule("label_nextUser"));
			labUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 2, 1, 0, 0));
			textUser=new Text(pannel,SWT.BORDER);
			textUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
			Button btnChoose=new  Button(pannel,SWT.PUSH);
			btnChoose.setText("   "+Constants.getStringVaule("btn_choose")+"   ");
			btnChoose.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
			btnChoose.addSelectionListener(new ChooseUserAction());
			
		 for(Triple data:parameters){
			    String type=data.getType();
			    if((Triple.ControlType.Check.ordinal()+"").equals(type)){
			    	Button btn=new Button(pannel,SWT.CHECK|SWT.LEFT);
			    	btn.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, true, 6, 1, 0, 0));
			    	btn.setText(data.getDesc());
			    	btn.setData(data);
			    	if(data.getValue().equals("1")){
			    		btn.setSelection(true);
			    	}else{
			    		btn.setSelection(false);
			    	}
			    	if(!StringUtil.isNullOrEmpty(data.getTip())){
			    		btn.setToolTipText(data.getTip());
			    	}
			    	Data.put(data.getName(), btn);
			    }
			    else if((Triple.ControlType.Text.ordinal()+"").equals(type)){
					Label labName=new Label(pannel,SWT.NONE);
					labName.setText(data.getDesc());
					labName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 2, 1, 0, 0));
					
					Text text= new Text(pannel,SWT.BORDER);
					text.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, true, 4, 1, 0, 0));
					text.setText(data.getValue());
					text.setData(data);
					if(!StringUtil.isNullOrEmpty(data.getTip())){
						text.setToolTipText(data.getTip());
			    	}
					Data.put(data.getName(), text);
			    }
			    else if((Triple.ControlType.Radio.ordinal()+"").equals(type)){
			    	Group group=new Group(pannel,SWT.SHADOW_ETCHED_OUT);
			    	group.setLayout(new FillLayout(SWT.HORIZONTAL));
			    	group.setText(data.getDesc());
			    	group.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, true, 6, 1, 0, 0));
			    	group.setData(data);
			    	Button btn1=new Button(group,SWT.RADIO|SWT.LEFT);
			    	btn1.setText(data.getMax());
			    	btn1.setSelection(false);
			    	Button btn2=new Button(group,SWT.RADIO|SWT.LEFT);
			    	btn2.setText(data.getMin());
			    	btn2.setSelection(false);
			    	if(data.getValue().equals("1")){
			    		btn1.setSelection(true);
			    	}
			    	if(data.getValue().equals("2")){
			    		btn2.setSelection(true);
			    	}
			    	Data.put(data.getName(), group);
			    } else if((Triple.ControlType.Special.ordinal()+"").equals(type)){
			    	textUser.setText("  "+data.getValue());
			    }
		 }
		
		    Button btnApply=new Button(pannel,SWT.PUSH);
			btnApply.setText("   "+Constants.getStringVaule("btn_apply")+"   ");
			btnApply.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 3, 1, 0, 0));
			btnApply.addSelectionListener(new ApplyNodePropertyAction());
			
			Button btnCancle=new Button(pannel,SWT.PUSH);
			btnCancle.setText("   "+Constants.getStringVaule("btn_cancel")+"   ");
			btnCancle.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 3, 1, 0, 0));
			btnCancle.addSelectionListener(new WindowCloseAction());
			
			pannel.pack();
			Setting.pack();
			Setting.open();
			Setting.addShellListener(new ShellCloseAction());
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			Setting.dispose();
		}	
	}

	public class WindowCloseAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Setting.dispose();
		}
	}
	
	public class ChooseUserAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			UserChoose control=new UserChoose(textUser);
   		}
	}
	
	public class ApplyNodePropertyAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String nextUser=textUser.getText();
			if(!StringUtil.isNullOrEmpty(nextUser)){
				String userID=nextUser.substring(nextUser.indexOf(" "));
				userID=userID.trim();
				LocalDataHelper.updateParameters("NextUser", userID);
			}
			Set setNode=Data.keySet();
			for (Iterator it = setNode.iterator(); it.hasNext();) {
				String key = (String)it.next();
				Control control=Data.get(key);
				Triple bean=(Triple)control.getData();
				String type=bean.getType();
				 if((Triple.ControlType.Check.ordinal()+"").equals(type)){
					 Button btn=(Button)control;
					 String value=btn.getSelection()?"1":"0";
					 LocalDataHelper.updateParameters(bean.getName(), value);
				 }
				 else if((Triple.ControlType.Text.ordinal()+"").equals(type)){
					 Text text=(Text)control;
					 String value=text.getText().trim();
					 String max=bean.getMax();
					 String min=bean.getMin();
					 int curVal=0;
					 if(!StringUtil.isNullOrEmpty(max)&&!StringUtil.isNullOrEmpty(min)){
						 int maxVal=Integer.parseInt(max);
						 int minVal=Integer.parseInt(min);
						 try{
							  curVal=Integer.parseInt(value);
						 }
						 catch(Exception exp){
							 curVal=0;
						 }
						 if(curVal>=minVal&&curVal<=maxVal){
							 LocalDataHelper.updateParameters(bean.getName(), value);
						 }
						 else{
								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(Constants.getStringVaule("alert_errorformat")+bean.getDesc()+" 需要介于["+minVal+","+maxVal+"]之间");
								box.open();	
								return;
						 }
					 }else{
						 LocalDataHelper.updateParameters(bean.getName(), value);
					 }
				 }
				 else if((Triple.ControlType.Radio.ordinal()+"").equals(type)){
					 Group group=(Group)control;
					 Control[] sons=group.getChildren();
					 int checkValue=0;
					 for(int i=0;i<sons.length;i++){
						 Button btn=(Button)sons[i];
						 if(btn.getSelection()){
							 checkValue=i+1;
						 }
					 }
					 LocalDataHelper.updateParameters(bean.getName(), Integer.toString(checkValue));
				 }
			}
			//更新完参数以后，为保证参数实时生效，需要重新加载参数到内存
			AppInit.getStartInstance().refreshParameters();
			Setting.dispose();
		}
	}
	
	
	private SettingView(){}
	private static SettingView unique_instance;
	private  Shell Setting=null;
	private Map<String,Control>  Data;
}
