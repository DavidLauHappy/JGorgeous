package business.admin.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.GROUPS;
import model.ROLES;
import model.USERS;

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

import common.controls.DavidCombo;

import bean.GroupBean;
import bean.RoleBean;
import bean.UserBean;

import resource.Constants;
import resource.Dictionary;
import resource.Item;

import utils.DataUtil;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

public class UserPropertyView {
	
		private  Shell property=null;
		private Point point=null;
		private UserBean data;
		private boolean newAble=false;
		public Text textID,textName,textEmail,textPhone=null;
		public Combo cmbStatus,cmbRole,cmbGroup=null;
		private DavidCombo cmbApp=null;
		public Map<String,String> Status=new HashMap<String, String>();
		public Map<String,String> Groups=new HashMap<String, String>();
		public Map<String,String> Roles=new HashMap<String, String>();
		public Map<String,String> Apps=new HashMap<String, String>();
		
		
		public void setData(UserBean data) {
			this.data = data;
		}

		public UserPropertyView(){
			 this.point=AppView.getInstance().getDisplay().getCurrent().getCursorLocation();
		 }
		 
		 public void show(){
			 property=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
			 property.setText(Constants.getStringVaule("window_userproperty"));
			 property.setLocation(point);
			 property.setLayout(LayoutUtils.getComGridLayout(1, 0));
			    Composite pannel=new Composite(property,SWT.V_SCROLL|SWT.BORDER);
				pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 7*40+60));
				pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
					Label labID=new Label(pannel,SWT.NONE);
					labID.setText(Constants.getStringVaule("header_userID"));
					labID.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
					textID=new Text(pannel,SWT.BORDER);
					textID.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
					
					Label labName=new Label(pannel,SWT.NONE);
					labName.setText(Constants.getStringVaule("header_userName"));
					labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
					textName=new Text(pannel,SWT.BORDER);
					textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
					
					Label labEmail=new Label(pannel,SWT.NONE);
					labEmail.setText(Constants.getStringVaule("header_userEmail"));
					labEmail.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
					textEmail=new Text(pannel,SWT.BORDER);
					textEmail.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
					
					Label labPhone=new Label(pannel,SWT.NONE);
					labPhone.setText(Constants.getStringVaule("header_userPhone"));
					labPhone.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
					textPhone=new Text(pannel,SWT.BORDER);
					textPhone.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
					
					Label labStatus=new Label(pannel,SWT.NONE);
					labStatus.setText(Constants.getStringVaule("header_userStatus"));
					labStatus.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
					cmbStatus=new Combo(pannel,SWT.DROP_DOWN);
					cmbStatus.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 4, 1, 0, 0));
					List<Item> status=Dictionary.getDictionaryList("USERS.STATUS");
					if(status!=null&&status.size()>0){
						String[] items=new String[status.size()];
						int index=0;
						for(Item item:status){
							items[index]=item.getValue();
							Status.put(item.getValue(), item.getKey());
							index++;
						}
						cmbStatus.setItems(items);
					}
					
					Label labRole=new Label(pannel,SWT.NONE);
					labRole.setText(Constants.getStringVaule("Label_dftRole"));
					labRole.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
					cmbRole=new Combo(pannel,SWT.DROP_DOWN);
					cmbRole.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 4, 1, 0, 0));
					List<RoleBean> roles=ROLES.getRoles();
					if(roles!=null&&roles.size()>0){
						String[] items=new String[roles.size()];
						int index=0;
						for(RoleBean role:roles){
							items[index]=role.getName();
							Roles.put(role.getName(), role.getId());
							index++;
						}
						cmbRole.setItems(items);
					}
					
					Label labGroup=new Label(pannel,SWT.NONE);
					labGroup.setText(Constants.getStringVaule("Label_dftGroup"));
					labGroup.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
					cmbGroup=new Combo(pannel,SWT.DROP_DOWN);
					cmbGroup.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 4, 1, 0, 0));
					List<GroupBean> groups=GROUPS.getGroups();
					if(groups!=null&&groups.size()>0){
						String[] items=new String[groups.size()];
						int index=0;
						for(GroupBean role:groups){
							items[index]=role.getName();
							Groups.put(role.getName(), role.getId());
							index++;
						}
						cmbGroup.setItems(items);
					}
					
					Label labApps=new Label(pannel,SWT.NONE);
					labApps.setText(Constants.getStringVaule("header_userApps"));
					labApps.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
					cmbApp=new DavidCombo(pannel,SWT.DROP_DOWN);
					cmbApp.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 4, 1, 0, 0));
					List<Item> apps=Dictionary.getDictionaryList("APP");
					String[] items=null;
					if(apps!=null&&apps.size()>0){
						 items=new String[apps.size()];
						int index=0;
						for(Item item:apps){
							items[index]=item.getValue();
							Apps.put(item.getValue(), item.getKey());
							index++;
						}
						cmbApp.setItems(items);
					}
					//赋予初始值
					if(this.data!=null){
						textID.setText(this.data.getUserID());
						textID.setEditable(false);
						textName.setText(this.data.getUserName());
						textEmail.setText(this.data.getEmail());
						textPhone.setText(this.data.getPhone());
						int indexStatus=Integer.parseInt(this.data.getStatus());
						cmbStatus.select(indexStatus);
						cmbRole.select(getIndex(cmbRole,this.data.getRoleID(),Roles));
						cmbGroup.select(getIndex(cmbGroup,this.data.getGroupID(),Groups));
						//多选列表的赋值
						if(!StringUtil.isNullOrEmpty(this.data.getApps())){
							String[]  userApps=this.data.getApps().split("\\|");
							String appDesc="";
							if(userApps!=null&&userApps.length>0){
								for(String app:userApps){
									String appName=(String)DataUtil.getMapFirstKey(Apps,app);
									appDesc+=appName+",";
								}
								String[] selections=appDesc.split(",");
								cmbApp.setSelectionItems(selections);
							}
						}
					}
					
					Button btnOk=new Button(pannel,SWT.PUSH);
					btnOk.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1, 0, 0));
					btnOk.setText("   "+Constants.getStringVaule("btn_ok")+"    ");
					btnOk.addSelectionListener(new UserModifyAction());
					
					Button btnCancle=new Button(pannel,SWT.PUSH);
					btnCancle.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1, 0, 0));
					btnCancle.setText("   "+Constants.getStringVaule("btn_cancel")+"    ");
					btnCancle.addSelectionListener(new UserCancelAction());
					
					pannel.pack();
			 property.pack();
			 property.open();
			 property.addShellListener(new ShellCloseAction());
		 }
		 
		 
		 public int getIndex(Combo combo,String value,Map<String,String> map){
			 String resultKey="";
			 for(String key:map.keySet()){
				 if(map.get(key).equals(value)){
					 resultKey=key;
					 break;
				 }
			 }
			 String[] items=combo.getItems();
			 for(int w=0;w<items.length;w++){
				 if(items[w].equals(resultKey))
					 return w;
			 }
			 return 0;
		 }
		 public class ShellCloseAction extends ShellAdapter{
				public void shellClosed(ShellEvent e){	
					property.dispose();
				}	
			}
		 
		 public class UserCancelAction  extends SelectionAdapter{
				public void widgetSelected(SelectionEvent e){
					property.dispose();
				}
		 }

		 public class UserModifyAction  extends SelectionAdapter{
				public void widgetSelected(SelectionEvent e){
						String id=textID.getText();
						String name=textName.getText();
						String email=textEmail.getText();
						String  phone=textPhone.getText();
						String status=cmbStatus.getText();
						String role=cmbRole.getText();
						String group=cmbGroup.getText();
						String appDesc=cmbApp.getText();
						if(StringUtil.isNullOrEmpty(id)||
								StringUtil.isNullOrEmpty(name)||	
								StringUtil.isNullOrEmpty(email)||
								StringUtil.isNullOrEmpty(phone)||
								StringUtil.isNullOrEmpty(status)||
								StringUtil.isNullOrEmpty(role)||
								StringUtil.isNullOrEmpty(group)||
								StringUtil.isNullOrEmpty(appDesc)
								){
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(Constants.getStringVaule("alert_necessaryinput"));
							box.open();	
						}else{
							String statusVal=Status.get(status);
							String groupID=Groups.get(group);
							String roleID=Roles.get(role);
								String[] apps=appDesc.split(",");
								String appStr="";
								for(String app:apps){
									appStr+=Apps.get(app)+"|";
								}
								appStr=StringUtil.rtrim(appStr, "|");
								UserBean user=new UserBean();
								user.setUserID(id.trim());
								user.setUserName(name.trim());
								user.setEmail(email.trim());
								user.setPhone(phone.trim());
								user.setStatus(statusVal);
								user.setRoleID(roleID.trim());
								user.setGroupID(groupID.trim());
								user.setApps(appStr.trim());
								user.setErrorTimes("0");
								if(newAble){
									//判断ID
									if(USERS.userExists(id)){
										MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
										box.setText(Constants.getStringVaule("messagebox_alert"));
										box.setMessage(Constants.getStringVaule("alert_duplicateval"));
										box.open();	
									}else{
											USERS.addUser(user);
											//刷新用户树
											UserView.getUserView(null).refreshUserTree();
											property.dispose();
									}
								}else{
									USERS.updateUser(user);
									//刷新用户树
									UserView.getUserView(null).refreshUserTree();
									property.dispose();
							    }
							
					}
					
				}
		 }
		 
		public void setNewAble(boolean newAble) {
			this.newAble = newAble;
		}
		 
		 
}
