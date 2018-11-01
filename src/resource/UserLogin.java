package resource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.RoleBean;

import common.db.DBConnectionManager;
import common.localdb.LocalDataHelper;
import utils.DateUtil;
import utils.SqlServerUtil;
import utils.StringUtil;


/**
 * @author David
 * 
 */
public class UserLogin {
  private String userName;
  private String passwd;
  private String status;
  private String errorTimes;
  private String roleID;
  private String userID;
  private String apps;
  public enum Status{Init,Logging,Logout,Lock;}
  
  public static final int maxPasswdErrorTimes=5;
  public UserLogin(String userID){
	  this.userID=userID;
  }
  
  public UserLogin(){
	 
  }
  
  
  public List<RoleBean> getUserRoles(){
	  Connection conn = null;
	  List<RoleBean> data=new ArrayList<RoleBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String query="select ROLES.ID ID,ROLES.NAME NAME from USER_ROLE,ROLES WHERE USER_ROLE.USER_ID='@USER_ID' and USER_ROLE.ROLE_ID=ROLES.ID";
			 query=query.replace("@USER_ID", userID);
			  List sqlResult=SqlServerUtil.executeQuery(query, conn);
				if(sqlResult!=null&&sqlResult.size()>0){
					for(int w=0;w<sqlResult.size();w++){
						Map<String,String> dataLine=(Map)sqlResult.get(w);
						String id=dataLine.get("ID");
						String name=dataLine.get("NAME");
						RoleBean bean=new RoleBean(id,name);
						data.add(bean);
					}
					Context.session.Roles=data;
				}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
  }
  
  public void setDefaultRole(String roleID){
	  Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			 String updater="UPDATE USERS SET ROLE_ID='@ROLE_ID',LOG_TIME=@LOG_TIME where USER_ID='@USERID'";
	    	 updater=updater.replace("@USERID", userID);
	    	 updater=updater.replace("@ROLE_ID", roleID);
	    	 updater=updater.replace("@LOG_TIME", "CONVERT(varchar(100),GETDATE(),120)");
	    	 SqlServerUtil.executeUpdate(updater, conn);
	    	 LocalDataHelper.updateParameters("DefaultRole", roleID);
		}
		catch(Exception e){
			
 		}finally{
 			DBConnectionManager.getInstance().freeConnction(conn);
 		}
  }
  public String login(){
	  Connection conn = null;
	  String result="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			  String query="SELECT USER_NAME ,PASSWD,STATUS ,ERROR_TIMES,ROLE_ID,APPS FROM USERS where USER_ID='@USERID'";
			  query=query.replace("@USERID", userID);
			  List sqlResult=SqlServerUtil.executeQuery(query, conn);
				if(sqlResult!=null&&sqlResult.size()>0){
					  Map dataLine=(Map)sqlResult.get(0);
					  this.userName=(String)dataLine.get("USER_NAME");
					  this.passwd=((String)dataLine.get("PASSWD")).trim();
					  this.status=(String)dataLine.get("STATUS");
					  this.errorTimes=dataLine.get("ERROR_TIMES")+"";
					  this.roleID=(String)dataLine.get("ROLE_ID");
					  this.apps=(String)dataLine.get("APPS");
					  if(StringUtil.isNullOrEmpty(this.roleID)){
						  result="用户未初始化默认角色，无法使用系统！请联系管理员。";
					  }
				}else{
					result="用户不存在！请联系管理员。";
				}
				if((UserLogin.Status.Lock.ordinal()+"").equals(this.status)){
					 result="用户已锁定，无法登录！请联系管理员解锁。";
				}
				if((UserLogin.Status.Logging.ordinal()+"").equals(status)){
					 result="";//"用户已登录，可允许重复登录！"; 用户经常异常推出导致用户登录中
				}
		}
		catch(Exception e){
			 result="用户已=登录，未知异常："+e.toString();
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return result;
  }
  
  private void init(String userID){
	 
  }
  
      public void lockUser(String userID){
    	  Connection conn = null;
    		try{
    			conn=DBConnectionManager.getInstance().getConnection();
    			 String updater="UPDATE USERS SET STATUS='3' where USER_ID='@USERID'";
    	    	 updater=updater.replace("@USERID", userID);
    	    	 SqlServerUtil.executeUpdate(updater, conn);
    		}
    		catch(Exception e){
    			
	   		}finally{
	   			DBConnectionManager.getInstance().freeConnction(conn);
	   		}
      }

      public void loginSuccessed(String userID){
    	  Connection conn = null;
	  		try{
	  			conn=DBConnectionManager.getInstance().getConnection();
	  			 String updater="UPDATE USERS SET STATUS='1',LOG_TIME='@TIME',ERROR_TIMES=0 where USER_ID='@USERID'";
	  	    	  updater=updater.replace("@USERID", userID);
	  	    	  updater=updater.replace("@TIME", DateUtil.getCurrentTime());	
	  	    	  SqlServerUtil.executeUpdate(updater, conn);
	  		}
  		 catch(Exception e){
  			
	   		}finally{
	   			DBConnectionManager.getInstance().freeConnction(conn);
	   		}
      }
      
      public void loginOut(String userID){
    	  Connection conn = null;
	  		try{
	  			conn=DBConnectionManager.getInstance().getConnection();
	  			 String updater="UPDATE USERS SET STATUS='2',LOG_TIME='@TIME' where USER_ID='@USERID'";
	  	    	  updater=updater.replace("@USERID", userID);
	  	    	  updater=updater.replace("@TIME", DateUtil.getCurrentTime());
	  	    	  SqlServerUtil.executeUpdate(updater, conn);
	  		}
  		  catch(Exception e){
  			
	   		}finally{
	   			DBConnectionManager.getInstance().freeConnction(conn);
	   		}

      }
      
      public void setPasswd(String passwd){
    	  Connection conn = null;
	  		try{
	  			conn=DBConnectionManager.getInstance().getConnection();
		  		  String updater="UPDATE USERS SET PASSWD='@PASSWD',LOG_TIME='@TIME' where USER_ID='@USERID'";
		    	  updater=updater.replace("@PASSWD", passwd);
		    	  updater=updater.replace("@USERID", userID);
		    	  updater=updater.replace("@TIME", DateUtil.getCurrentTime());
		    	  SqlServerUtil.executeUpdate(updater, conn);
	  		}
	  		catch(Exception e){
  			
	   		}finally{
	   			DBConnectionManager.getInstance().freeConnction(conn);
	   		}
      }
      
      public void refreshRole(){
    	  Context.Apps=this.apps;
    	  Context.session.userID=this.userID;
    	  Context.session.roleID=this.roleID;
    	  Context.session.userName=this.userName;
    	  Context.appName=AppInit.getStartInstance().getAppName();
    	  
      }
		public String getUserName() {
			return userName;
		}
		
		public String getPasswd() {
			return passwd;
		}
		
		public String getStatus() {
			return status;
		}
		
		public String getErrorTimes() {
			return errorTimes;
		}
		
		public String getRoleID() {
			return roleID;
		}
  
  
}
