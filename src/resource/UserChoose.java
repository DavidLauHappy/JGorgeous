package resource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import common.db.DBConnectionManager;





import utils.DateUtil;
import utils.LayoutUtils;
import utils.SqlServerUtil;
import utils.StringUtil;
import views.AppView;

public class UserChoose {
	private  Shell shell=null;
	public  Shell  GroupShell=null;
	public Text  callbackText=null;
	private Text textUser;
	public Tree userTree=null;
	public Tree chooseUserTree=null;
	private Text textGroup;
	public UserChooseCallBack action=null;
	public UserChoose(UserChooseCallBack action){
		this.action=action;
		this.show();
	}
	public UserChoose(Text text){
		this.callbackText=text;
       this.show();
	}
	
	private void show(){
		shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		shell.setText(Constants.getStringVaule("window_userchoose"));
		shell.setLocation(AppView.getInstance().getCentreScreenPoint());
		shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
	 	Composite topPannel=new Composite(shell,SWT.NONE);
	 	topPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
	 	topPannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
	 	textUser=new   Text(topPannel,SWT.BORDER);
	 	textUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, true, 4, 1, 0, 0));
	 	Button btnQuery= new Button(topPannel,SWT.PUSH);
	 	btnQuery.setText("   "+Constants.getStringVaule("btn_search")+"   ");
	 	btnQuery.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1, 0, 0));	
	 	btnQuery.addSelectionListener(new FuzzyQueryAction());
	 	Button btnDefine= new Button(topPannel,SWT.PUSH);
	 	btnDefine.setText("   "+Constants.getStringVaule("btn_define")+"   ");
	 	btnDefine.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1, 0, 0));
	 	btnDefine.addSelectionListener(new AddGroupAction());
	 	topPannel.pack();
		
	 	Composite pannel=new Composite(shell,SWT.BORDER);
	 	pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 300));
	 	pannel.setLayout(LayoutUtils.getComGridLayout(5, 0));
	 	this.createUserTree(pannel);
	 	this.createButtonPanel(pannel);
	 	this.createResultPanel(pannel);
	 	pannel.pack();
	 	
		shell.pack();
		shell.open();
		shell.addShellListener(new ShellCloseAction());
	}
	private void createUserTree(Composite parent){
	 	Group pannel=new Group(parent,SWT.NONE);
	 	pannel.setText("待选择用户:");
	 	pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 2, 1, 0, 0));
	 	pannel.setLayout(LayoutUtils.getComGridLayout(1, 0));
	 	userTree=new Tree(pannel,SWT.BORDER|SWT.MULTI);//
	 	userTree.addSelectionListener(new ChooseAction());
	 	userTree.addMouseListener(new MoveUserAction());
	 	userTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	 	this.loadUserGroup();
	 	userTree.pack();
	 	pannel.pack();
	}
	
	private void loadUserGroup(){
		List<UserGroup> Groups=new ArrayList<UserGroup>();
		Connection conn = null;
		try{
			String sql="SELECT GROUP_ID,GROUP_NAME,TYPE FROM GROUPS where TYPE=0 "+
							  "UNION "+
							  "SELECT GROUP_ID,GROUP_NAME,TYPE FROM GROUPS where TYPE=1 AND UPT_USER='@UPT_USER'";
			sql=sql.replace("@UPT_USER",  Context.session.userID); 
			conn=DBConnectionManager.getInstance().getConnection();
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
		   		  for(int w=0;w<sqlResult.size();w++){
		   			Map<String,String> dataLine=(Map)sqlResult.get(w);
		   			UserGroup data=new UserGroup(dataLine.get("GROUP_ID"),dataLine.get("GROUP_NAME"),dataLine.get("TYPE"));
		   			Groups.add(data);
		   		  }
		   	  }
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
	   	  if(Groups.size()>0){
	   		  userTree.removeAll();
	   		  for(UserGroup group:Groups){
		   			 TreeItem  treeRoot=new TreeItem(userTree,SWT.SINGLE);
		        	 treeRoot.setText(group.getGroupName());
		        	 treeRoot.setImage(Icons.getFloderIcon());
		        	 treeRoot.setData(group);
		        	 treeRoot.setData("$type", "1");//标识群组
		        	 List<User> users=getGroupUser(group.getGroupID(),group.getType());
	   					if(users.size()>0){
	   						for(User user:users){
		   						 TreeItem  treeItem=new TreeItem(treeRoot,SWT.SINGLE);
			   						treeItem.setText(user.getShowName());
			   						treeItem.setImage(Icons.getUserIcon());
			   						treeItem.setData(user);
			   						treeItem.setData("$type", "2");
	   						}
	   				}
	   		  	}
	   	  	}
	 }
	
	
	private void createButtonPanel(Composite parent){
		Composite pannel=new Composite(parent,SWT.NONE);
	 	pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	 	pannel.setLayout(LayoutUtils.getComGridLayout(1, 0));
	 	Button btnLeft=new  Button(pannel,SWT.PUSH);
	 	btnLeft.setText("   "+"选择>>"+"   ");
	 	btnLeft.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1, 0, 0));
	 	btnLeft.addSelectionListener(new AddUserAction());
	 	
	 	
	 	Button btnRight=new  Button(pannel,SWT.PUSH);
	 	btnRight.setText("   "+"取消选择<<"+"   ");
	 	btnRight.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1, 0, 0));
	 	btnRight.addSelectionListener(new RemoveUserAction());
	 	pannel.pack();
	}
	
	public Group pannel;
	private void createResultPanel(Composite parent){
		pannel=new Group(parent,SWT.NONE);
	 	pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 2, 1, 0, 0));
	 	pannel.setLayout(LayoutUtils.getComGridLayout(1, 0));
	 	pannel.setText("已选择用户：");
	 	chooseUserTree=new Tree(pannel,SWT.BORDER|SWT.MULTI);//
	 	//chooseUserTree.addSelectionListener(new ChooseAction());
	 	chooseUserTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	 	chooseUserTree.pack();
	 	pannel.pack();
	}
	
	
	public boolean groupEdit=false;
	public String groupID="";
	 public class MoveUserAction extends MouseAdapter{
		 public void mouseDoubleClick(MouseEvent e){
			 TreeItem currentItem=userTree.getItem(new Point(e.x,e.y));
			  if(currentItem!=null){
				  String type=(String)currentItem.getData("$type");
					if("2".equals(type)){
						  User user=(User)currentItem.getData();
		 					if(!checkChoooseUser(user)){
			   					TreeItem  treeitem=new TreeItem(chooseUserTree,SWT.SINGLE);
			   					treeitem.setText(user.getShowName());
			   					treeitem.setImage(Icons.getUserIcon());
			   					treeitem.setData(user);
			   					if(groupEdit){
			   						addGroupUser(groupID,user);
			   					}
		 					}
					}else{
						UserGroup group=(UserGroup)currentItem.getData();
						String groupType=group.getType();
						if("1".equals(groupType)){//自定义群组进入编辑模式
							groupEdit=true;
							groupID=group.getGroupID();
							chooseUserTree.removeAll();
							pannel.setText("编辑自定义群组用户：");
							List<User> users=getGroupUser(group.getGroupID(),group.getType());
		   					if(users.size()>0){
		   						for(User user:users){
			   						 TreeItem  treeitem=new TreeItem(chooseUserTree,SWT.SINGLE);
			   						treeitem.setText(user.getShowName());
			   						treeitem.setImage(Icons.getUserIcon());
			   						treeitem.setData(user);
		   						}
						}
					}else{
						pannel.setText("已选择用户：");
						groupEdit=false;
						groupID="";
					}
			   }
	 		}
		 }
	 }
	 public class ChooseAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			TreeItem current=(TreeItem)e.item;
	   			if(current!=null){
	   				String type=(String)current.getData("$type");
	   				if("1".equals(type)){
	   					current.removeAll();
	   					UserGroup group=(UserGroup)current.getData();
	   					List<User> users=getGroupUser(group.getGroupID(),group.getType());
	   					if("1".equals(group.getType())){
	   						pannel.setText("已选择用户：");
	   						groupEdit=false;
							groupID="";
	   					}
	   					if(users.size()>0){
	   						for(User user:users){
		   						 TreeItem  treeRoot=new TreeItem(current,SWT.SINGLE);
		   			        	 treeRoot.setText(user.getShowName());
		   			        	 treeRoot.setImage(Icons.getUserIcon());
		   			        	 treeRoot.setData(user);
		   			        	 treeRoot.setData("$type", "2");
	   						}
	   						current.setExpanded(true);	
	   					}
	   				}
	   			}
	   		}
	 }
	 
	 public class AddGroupAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			GroupShell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
	   			GroupShell.setText(Constants.getStringVaule("window_groupdefine"));
	   			GroupShell.setLocation(AppView.getInstance().getCentreScreenPoint());
	   			GroupShell.setLayout(LayoutUtils.getComGridLayout(1, 0));
	   			
	   			Composite pannel=new Composite(GroupShell,SWT.NONE);
	   			pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 300, 100));
	   			pannel.setLayout(LayoutUtils.getComGridLayout(6, 0));
	   			Label labDesc=new Label(pannel,SWT.NONE);
	   			labDesc.setText(Constants.getStringVaule("label_node_group"));
	   			labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, true, 2, 1, 0, 0));
	   			textGroup=new Text(pannel,SWT.BORDER|SWT.MULTI);
	   			textGroup.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.BEGINNING, true, true, 4, 1, 0, 0));
	   			
	   			Button btnApply=new Button(pannel,SWT.PUSH);
	   			btnApply.setText("   "+Constants.getStringVaule("btn_apply")+"   ");
	   			btnApply.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 3, 1, 0, 0));
	   			btnApply.addSelectionListener(new AddGroupBtnAction());
	   			Button btnCancle=new Button(pannel,SWT.PUSH);
	   			btnCancle.setText("   "+Constants.getStringVaule("btn_cancel")+"   ");
	   			btnCancle.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, true, 3, 1, 0, 0));
	   			btnCancle.addSelectionListener(new WindowCloseAction());
	   			pannel.pack();
	   			GroupShell.pack();
	   			GroupShell.open();
	   			GroupShell.addShellListener(new GroupShellCloseAction());
	   		}
	 }
	 
	 public class WindowCloseAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				GroupShell.dispose();
			}
	}
	 
	 public class AddGroupBtnAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				String name=textGroup.getText();
				if(StringUtil.isNullOrEmpty(name)){
					String msg="群组名称不允许为空";
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					box.open();	
				}else{
					 int len=name.getBytes().length;
					 if(len>=120){
						 String msg="群组名称不允许超过120个字符";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();	
					 }else{
						 boolean ret=inrollUserGroup(name);
						 if(ret){
							 GroupShell.dispose();
							 loadUserGroup();
						 }else{
							    String msg="群组新增失败，请稍后重试";
								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(msg);
								box.open();	
						 }
					 }
				}
			}
	 }
	 
	 public boolean inrollUserGroup(String name){
			Connection conn = null;
			try{
			 String sql="SELECT  ISNULL(max(cast(GROUP_ID as int),0)+1 GROUP_ID FROM GROUPS";
			 conn=DBConnectionManager.getInstance().getConnection();
			 List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			 String groupID="";
			 if(sqlResult!=null&&sqlResult.size()>0){
				 Map dataLine=(Map)sqlResult.get(0);
				  groupID=(String)dataLine.get("GROUP_ID");
			 }
		   String insert="INSERT INTO GROUPS(GROUP_ID,GROUP_NAME,TYPE,UPT_USER,UPT_TIME)  "+
		   						"VALUES('@GROUP_ID','@GROUP_NAME','@TYPE','@UPT_USER','@UPT_TIME')";
			   insert=insert.replace("@GROUP_ID", groupID);
			   insert=insert.replace("@GROUP_NAME", name);
			   insert=insert.replace("@TYPE", "1");
			   insert=insert.replace("@UPT_USER",  Context.session.userID);
			   insert=insert.replace("@UPT_TIME", DateUtil.getCurrentTime());
			   int count=SqlServerUtil.executeUpdate(insert, conn);
			   if(count>0)
				   return true;
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return false;
	 }
	 
	 
	 public void  addGroupUser(String groupID,User user){
			Connection conn = null;
			try{
		 	String insert="INSERT INTO GROUP_USER(GROUP_ID,USER_ID,UPT_USER,UPT_TIME)  "+
					"VALUES('@GROUP_ID','@USER_ID','@UPT_USER','@UPT_TIME')";
						insert=insert.replace("@GROUP_ID", groupID);
						insert=insert.replace("@USER_ID", user.getUserID());
						insert=insert.replace("@UPT_USER",  Context.session.userID);
						insert=insert.replace("@UPT_TIME", DateUtil.getCurrentTime());
						conn=DBConnectionManager.getInstance().getConnection();
						SqlServerUtil.executeUpdate(insert, conn);
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
	 }
	 
	 public void  removeGroupUser(String groupID,String userID){
			 Connection conn = null;
				try{
					String insert="DELETE FROM GROUP_USER WHERE GROUP_ID='@GROUP_ID'  AND USER_ID='@USER_ID' ";
					insert=insert.replace("@GROUP_ID", groupID);
					insert=insert.replace("@USER_ID", userID);
					conn=DBConnectionManager.getInstance().getConnection();
					SqlServerUtil.executeUpdate(insert, conn);
			}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
	 }
	 
	 public class FuzzyQueryAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			String key=textUser.getText();
	   			if(!StringUtil.isNullOrEmpty(key)){
	   				TreeItem[] groups=userTree.getItems();
	   				for(int w=0;w<groups.length;w++){
	   					TreeItem[] items=groups[w].getItems();
	   				    for(TreeItem item:items){
		   			    	String type=(String)item.getData("$type");
		   			    	if(!"1".equals(type)){
		   			    		User user=(User)item.getData();
			   			    	String userID=user.getUserID();
			   			    	String userName=user.getUserName();
			   			    	if(userID.indexOf(key)!=-1||userName.indexOf(key)!=-1){//命中
			   			    		TreeItem parentItem=item.getParentItem();
			   			    		parentItem.setExpanded(true);
			   			    		//userTree.select(item);
			   			    		userTree.setSelection(item);
			   			    		if(userID.equals(key)||userName.equals(key)){
			   			    			if(!checkChoooseUser(user)){
			   			    				TreeItem  treeitem=new TreeItem(chooseUserTree,SWT.SINGLE);
			   			   					treeitem.setText(user.getShowName());
			   			   					treeitem.setImage(Icons.getUserIcon());
			   			   					treeitem.setData(user);
			   			    			}
			   			    		}
			   			    	}
		   			    	}
		   			    }
	   				}
	   			}
	   		}
	 }
	 
	 public boolean checkChoooseUser(User user){
		 TreeItem[] items=chooseUserTree.getItems();
		 if(items!=null&&items.length>0){
			 for(TreeItem item:items){
				 User chooseUser=(User)item.getData();
				 if(chooseUser.getUserID().equals(user.getUserID())){
					 chooseUserTree.select(item);
					 return true;
				 }
			 }
		 }
		 return false;
	 }
	 
	 
	 public List<User> getGroupUser(String groupID,String type){
		 //自定义群组就不能这么取了
		    List<User> Result=new ArrayList<User>();
			Connection conn = null;
		    try{
			    String sql="";
			    if(type.equals("0")){
			    	sql="SELECT USER_ID,USER_NAME,PASSWD,EMAIL,PHONE,STATUS,ERROR_TIMES,LOG_TIME,ROLE_ID,GROUP_ID FROM USERS WHERE GROUP_ID='@GROUP_ID'";
			    }else{
			    	sql="SELECT USERS.USER_ID USER_ID,USERS.USER_NAME USER_NAME,USERS.PASSWD PASSWD,USERS.EMAIL EMAIL,USERS.PHONE PHONE,USERS.STATUS STATUS,USERS.ERROR_TIMES ERROR_TIMES,USERS.LOG_TIME LOG_TIME,USERS.ROLE_ID ROLE_ID,GROUP_USER.GROUP_ID GROUP_ID FROM USERS,GROUP_USER WHERE GROUP_USER.GROUP_ID='@GROUP_ID' AND GROUP_USER.USER_ID=USERS.USER_ID";
			    }
				
				sql=sql.replace("@GROUP_ID", groupID);
				conn=DBConnectionManager.getInstance().getConnection();
				List sqlResult=SqlServerUtil.executeQuery(sql, conn);
				if(sqlResult!=null&&sqlResult.size()>0){
			   		  for(int w=0;w<sqlResult.size();w++){
			   			Map dataLine=(Map)sqlResult.get(w);
			   			User data=new User((String)dataLine.get("USER_ID"),(String)dataLine.get("USER_NAME"),(String)dataLine.get("PASSWD"),(String)dataLine.get("EMAIL"),(String)dataLine.get("PHONE"),(String)dataLine.get("STATUS"),dataLine.get("ERROR_TIMES")+"",(String)dataLine.get("LOG_TIME"),(String)dataLine.get("ROLE_ID"),(String)dataLine.get("GROUP_ID"));
			   			Result.add(data);
			   		  }
			   	  }
		    }catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
		   	return Result;
	 }
	 
	 public class RemoveUserAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			TreeItem[] allItems=chooseUserTree.getItems();
	   			List<User> Users=new ArrayList<User>();
	   			if(allItems!=null&&allItems.length>0){
	   				for(TreeItem item:allItems){
	   					if(!isUserSelected(item)){
	   						Users.add((User)item.getData());
	   					}else{
	   						if(groupEdit){//从群组移除用户
	   							removeGroupUser(groupID,((User)item.getData()).getUserID());
		   					}
	   					}
	   				}
	   			}
	   			chooseUserTree.removeAll();
	   		 if(Users!=null&&Users.size()>0){
				 for(User user:Users){
					    TreeItem  treeitem=new TreeItem(chooseUserTree,SWT.SINGLE);
	   					treeitem.setText(user.getShowName());
	   					treeitem.setImage(Icons.getUserIcon());
	   					treeitem.setData(user);
				 }
	   		 }
	   	 }
	 }
	 
	 public class AddUserAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			TreeItem[] selectedItems=userTree.getSelection();
	   			for(TreeItem item:selectedItems){
	   			 User user=(User)item.getData();
	   				if(!checkChoooseUser(user)){
	   					TreeItem  treeitem=new TreeItem(chooseUserTree,SWT.SINGLE);
	   					treeitem.setText(user.getShowName());
	   					treeitem.setImage(Icons.getUserIcon());
	   					treeitem.setData(user);
	   					if(groupEdit){
	   						addGroupUser(groupID,user);
	   					}
	   				}
	   			}
	   		}
	 }
	 
	 
	 public boolean isUserSelected(TreeItem item){
		 TreeItem[] selectedItems=chooseUserTree.getSelection();
		 if(selectedItems!=null&&selectedItems.length>0){
			for(TreeItem selecteditem:selectedItems){
					if(selecteditem.equals(item)){
						return true;
					}
				}
		 }
		return false;
	 }
	 
	 public class GroupShellCloseAction extends ShellAdapter{
			public void shellClosed(ShellEvent e){	
				GroupShell.dispose();
			}
	 }
	 
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			TreeItem[] items=chooseUserTree.getItems();
			if(items!=null&&items.length>0){
				if(callbackText!=null){
					User user=(User)items[0].getData();
					callbackText.setText(user.getShowName());
					callbackText.setData(user);
				}
				if(action!=null){
					List<User> users=new ArrayList<User>();
					for(TreeItem item:items){
						User data=(User)item.getData();
						users.add(data);
					}
					action.setUserList(users);
					action.action();
				}
			}
			shell.dispose();
		}
	}
}
