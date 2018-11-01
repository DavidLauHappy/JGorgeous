package business.admin.view;

import java.util.List;

import model.APPSYSTEM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import bean.App;

import resource.Constants;
import resource.Context;
import resource.DbNodeView.ShellCloseAction;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

/**
 * @author Administrator
 *  应用系统的配置
 */
public class AppMngView {
	private Composite parent;
	public Composite content;
	public Table appTable=null;
	private  String[] header=new String[]{ Constants.getStringVaule("header_userApps"),
			Constants.getStringVaule("header_userAppsys") ,
			Constants.getStringVaule("header_appSysname"),
			Constants.getStringVaule("header_enable"),
			Constants.getStringVaule("header_timespan"),
			Constants.getStringVaule("header_operuser"),
			Constants.getStringVaule("header_opertime")
			};
	
	public AppMngView(Composite com){
		this.parent=com;
		this.createAndShow();
	}
	
	private void createAndShow(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 10));
		
		Composite toolPanel= new Composite(this.content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(3, 10));
		Button btnUpt=new  Button(toolPanel,SWT.PUSH);
		btnUpt.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
		btnUpt.setText("   "+Constants.getStringVaule("btn_tips_setting")+"    ");
		btnUpt.addSelectionListener(new SetAppAction());
		toolPanel.pack();
		
		Composite dataPanel= new Composite(this.content,SWT.NONE);
		dataPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		dataPanel.setLayout(LayoutUtils.getComGridLayout(1, 10));
		appTable=new Table(dataPanel,SWT.BORDER|SWT.SINGLE|SWT.FULL_SELECTION);
		appTable.setHeaderVisible(true);
		appTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		appTable.setLinesVisible(true);
		appTable.addMouseListener(new ShowAppAction());
		appTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		  for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(appTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
			
		  List<App> apps=APPSYSTEM.getAllSystem();
		  if(apps!=null&&apps.size()>0){
			  for(App app:apps){
				  TableItem tableItem=new TableItem(appTable,SWT.BORDER);
				  tableItem.setText(new String[]{app.getApp(),app.getSystem(),app.getName(),app.getFlag(),app.getTimespan(),app.getMdfuser(),app.getMdfTime()});
				  tableItem.setData(app);
			  }
		  }
			for(int j=0;j<header.length;j++){		
				appTable.getColumn(j).pack();
			}	
			
		dataPanel.pack();
		content.pack();
	}
	
	public void refreshApps(){
		appTable.removeAll();
		  List<App> apps=APPSYSTEM.getAllSystem();
		  if(apps!=null&&apps.size()>0){
			  for(App app:apps){
				  TableItem tableItem=new TableItem(appTable,SWT.BORDER);
				  tableItem.setText(new String[]{app.getApp(),app.getSystem(),app.getName(),app.getFlag(),app.getTimespan(),app.getMdfuser(),app.getMdfTime()});
				  tableItem.setData(app);
			  }
		  }
	}
	public  Shell shell;
	public Button btnCheck,btnNoCheck;
	public Text txtSpan;
	public class SetAppAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
				 TableItem[] selections=appTable.getSelection();
				 if(selections!=null&&selections.length>0){
					 TableItem item=selections[0];
					 App app=(App)item.getData();
					 shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.RESIZE|SWT.SYSTEM_MODAL);
					 shell.setText(Constants.getStringVaule("window_appinfo"));
					 shell.setLocation(AppView.getInstance().getCentreScreenPoint());
					 shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
					 Composite pannel=new Composite(shell,SWT.NONE);
					 pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 350, 180));
					 pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
					 
					 Label labApp=new Label(pannel,SWT.NONE);
					 labApp.setText(Constants.getStringVaule("menu_group"));
					 labApp.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
					 Text txtApp=new Text(pannel,SWT.BORDER);
					 txtApp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
					 if(!StringUtil.isNullOrEmpty(app.getName())){
						 txtApp.setText(app.getName());
						 txtApp.setEditable(false);
					 }
					 Label labFlag=new Label(pannel,SWT.NONE);
					 labFlag.setText(Constants.getStringVaule("header_enable"));
					 labFlag.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
					 Group group1=new Group(pannel,SWT.SHADOW_ETCHED_OUT);
				    group1.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
				    group1.setLayout(new FillLayout(SWT.HORIZONTAL));
				    btnCheck=new Button(group1,SWT.RADIO|SWT.FLAT);
				    btnCheck.setText("启用");
				    btnNoCheck=new Button(group1,SWT.RADIO|SWT.FLAT);
				    btnNoCheck.setText("禁用");
				    if("0".equals(app.getFlag())){
				    	btnNoCheck.setSelection(true);
				    }else{
				    	btnCheck.setSelection(true);
				    }
				    group1.pack();
				    
				     Label labSpan=new Label(pannel,SWT.NONE);
				     labSpan.setText(Constants.getStringVaule("header_timespan"));
				     labSpan.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
				     txtSpan=new Text(pannel,SWT.BORDER);
				     txtSpan.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
				     txtSpan.setToolTipText(Constants.getStringVaule("text_tips_timespan"));
				     if(!StringUtil.isNullOrEmpty(app.getTimespan())){
				    	 txtSpan.setText(app.getTimespan());
				     }
				     Button btnCancel=new Button(pannel,SWT.PUSH);
					 btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
					 btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
					 btnCancel.addSelectionListener(new SelectionListener(){
						 public void widgetSelected(SelectionEvent arg0) {
							 txtSpan.setText("");
						}
						public void widgetDefaultSelected(SelectionEvent arg0) {
							
						}
					});
					 
					    Button btnSure=new Button(pannel,SWT.PUSH);
					    btnSure.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
					    btnSure.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
					    btnSure.setData(item);
					    btnSure.addSelectionListener(new SelectionListener(){
							 public void widgetSelected(SelectionEvent arg0) {
								 Button btn=(Button)arg0.getSource();
								 TableItem control=(TableItem)btn.getData();
								 App bean=(App)control.getData();
								   String span=txtSpan.getText();
								   String flag="0";
								   if(btnCheck.getSelection()){
									   flag="1";
								   }
								   if(!StringUtil.isNullOrEmpty(span)){
									   String timeSpanRegx="^(20|21|22|23|[0-1]\\d):[0-5]\\d-(20|21|22|23|[0-1]\\d):[0-5]\\d$";
									   if(StringUtil.checkRegex(span, timeSpanRegx)){
										   bean.update(span, flag, Context.session.userID);
										   control.setData(bean);
										   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
										    box.setText(Constants.getStringVaule("messagebox_alert"));
											box.setMessage(Constants.getStringVaule("alert_successoperate"));
											box.open();	
											refreshApps();
											shell.dispose();
									   }else{
										    MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
										    box.setText(Constants.getStringVaule("messagebox_alert"));
										    box.setMessage(Constants.getStringVaule("alert_errorformat"));
										    box.open();	
									   }
								   }else{
									   bean.update(span, flag, Context.session.userID);
									   control.setData(bean);
									   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									    box.setText(Constants.getStringVaule("messagebox_alert"));
									box.setMessage(Constants.getStringVaule("alert_successoperate"));
									box.open();	
									refreshApps();
									shell.dispose();
								   }
								   
							}
							public void widgetDefaultSelected(SelectionEvent arg0) {
								
							}
						});
						
					 pannel.pack();
					 shell.pack();
					 shell.open();
					 shell.addShellListener(new ShellAdapter() {
						public void shellClosed(ShellEvent e){	
				 			shell.dispose();
				 		}	
					 });
				 }
		 	}
	}
	
	 public class ShowAppAction extends MouseAdapter{
			public void mouseDown(MouseEvent e){
			}
			public void mouseDoubleClick(MouseEvent e){
				TableItem currentItem=appTable.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null){
	 				 App app=(App)currentItem.getData();
					 shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.RESIZE|SWT.SYSTEM_MODAL);
					 shell.setText(Constants.getStringVaule("window_appinfo"));
					 shell.setLocation(AppView.getInstance().getCentreScreenPoint());
					 shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
					 Composite pannel=new Composite(shell,SWT.NONE);
					 pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 350, 180));
					 pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
					 
					 Label labApp=new Label(pannel,SWT.NONE);
					 labApp.setText(Constants.getStringVaule("menu_group"));
					 labApp.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
					 Text txtApp=new Text(pannel,SWT.BORDER);
					 txtApp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
					 if(!StringUtil.isNullOrEmpty(app.getName())){
						 txtApp.setText(app.getName());
						 txtApp.setEditable(false);
					 }
					 Label labFlag=new Label(pannel,SWT.NONE);
					 labFlag.setText(Constants.getStringVaule("header_enable"));
					 labFlag.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
					 Group group1=new Group(pannel,SWT.SHADOW_ETCHED_OUT);
				    group1.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
				    group1.setLayout(new FillLayout(SWT.HORIZONTAL));
				    btnCheck=new Button(group1,SWT.RADIO|SWT.FLAT);
				    btnCheck.setText("启用");
				    btnNoCheck=new Button(group1,SWT.RADIO|SWT.FLAT);
				    btnNoCheck.setText("禁用");
				    if("0".equals(app.getFlag())){
				    	btnNoCheck.setSelection(true);
				    }else{
				    	btnCheck.setSelection(true);
				    }
				    group1.pack();
				    
				     Label labSpan=new Label(pannel,SWT.NONE);
				     labSpan.setText(Constants.getStringVaule("header_timespan"));
				     labSpan.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
				     txtSpan=new Text(pannel,SWT.BORDER);
				     txtSpan.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true,false, 4, 1, 0, 0));
				     txtSpan.setToolTipText(Constants.getStringVaule("text_tips_timespan"));
				     if(!StringUtil.isNullOrEmpty(app.getTimespan())){
				    	 txtSpan.setText(app.getTimespan());
				     }
				     Button btnCancel=new Button(pannel,SWT.PUSH);
					 btnCancel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
					 btnCancel.setText("     "+Constants.getStringVaule("btn_cancel")+"     ");
					 btnCancel.addSelectionListener(new SelectionListener(){
						 public void widgetSelected(SelectionEvent arg0) {
							 txtSpan.setText("");
						}
						public void widgetDefaultSelected(SelectionEvent arg0) {
							
						}
					});
					 
					    Button btnSure=new Button(pannel,SWT.PUSH);
					    btnSure.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1, 0, 0));
					    btnSure.setText("     "+Constants.getStringVaule("btn_ok")+"     ");
					    btnSure.setData(currentItem);
					    btnSure.addSelectionListener(new SelectionListener(){
							 public void widgetSelected(SelectionEvent arg0) {
								 Button btn=(Button)arg0.getSource();
								 TableItem control=(TableItem)btn.getData();
								 App bean=(App)control.getData();
								  String span=txtSpan.getText();
								   String flag="0";
								   if(btnCheck.getSelection()){
									   flag="1";
								   }
								   if(!StringUtil.isNullOrEmpty(span)){
									   String timeSpanRegx="^(20|21|22|23|[0-1]\\d):[0-5]\\d-(20|21|22|23|[0-1]\\d):[0-5]\\d$";
									   if(StringUtil.checkRegex(span, timeSpanRegx)){
										   bean.update(span, flag, Context.session.userID);
										   control.setData(bean);
										   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
										    box.setText(Constants.getStringVaule("messagebox_alert"));
										    box.setMessage(Constants.getStringVaule("alert_successoperate"));
										    box.open();	
										    refreshApps();
										    shell.dispose();
									   }else{
										   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
										    box.setText(Constants.getStringVaule("messagebox_alert"));
										    box.setMessage(Constants.getStringVaule("alert_errorformat"));
										    box.open();	
									   }
								   }else{
									   bean.update(span, flag, Context.session.userID);
									   control.setData(bean);
									   MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									    box.setText(Constants.getStringVaule("messagebox_alert"));
									    box.setMessage(Constants.getStringVaule("alert_successoperate"));
									    box.open();	
									    refreshApps();
									    shell.dispose();
								   }
							
							}
							public void widgetDefaultSelected(SelectionEvent arg0) {
								
							}
						});
						
					 pannel.pack();
					 shell.pack();
					 shell.open();
					 shell.addShellListener(new ShellAdapter() {
						public void shellClosed(ShellEvent e){	
				 			shell.dispose();
				 		}	
					 });
	 			}
			}
	 }
	 
	
}
