package sql;

import resource.Context;

public class USERSql {

	public static String getUser(String userID){
		 String sql="select  USER_ID,USER_NAME,PASSWD,EMAIL,PHONE,STATUS,ERROR_TIMES,LOG_TIME,ROLE_ID,GROUP_ID,APPS from USERS where USER_ID='@USER_ID'";
		sql=sql.replace("@USER_ID", userID);
		return sql;
	}
	
	
	public static String getAllUser(String userID){
		 String sql="select  USER_ID,USER_NAME,PASSWD,EMAIL,PHONE,STATUS,ERROR_TIMES,LOG_TIME,ROLE_ID,GROUP_ID,APPS from USERS where USER_ID<>'@USER_ID' order by USER_ID";
		sql=sql.replace("@USER_ID", userID);
		return sql;
	}
	
	public static String getRoleUsers(String roleID){
		 String sql="select  USER_ID,USER_NAME,PASSWD,EMAIL,PHONE,STATUS,ERROR_TIMES,LOG_TIME,ROLE_ID,GROUP_ID,APPS from USERS where exists(select 1 from USER_ROLE where ROLE_ID='@ROLE_ID'  and USER_ID=USERS.USER_ID) order by USER_ID";
		sql=sql.replace("@ROLE_ID", roleID);
		return sql;
	}
	
	public static String getGroupUsers(String groupID){
		 String sql="select  USERS.USER_ID USER_ID,USERS.USER_NAME USER_NAME,USERS.PASSWD PASSWD,USERS.EMAIL EMAIL,USERS.PHONE PHONE,USERS.STATUS STATUS,USERS.ERROR_TIMES ERROR_TIMES,USERS.LOG_TIME LOG_TIME,USERS.ROLE_ID ROLE_ID,USERS.GROUP_ID GROUP_ID,USERS.APPS  APPS "+
				 		"from USERS,GROUP_USER where GROUP_USER.USER_ID=USERS.USER_ID and GROUP_USER.GROUP_ID='@GROUP_ID' order by USERS.USER_ID";
		sql=sql.replace("@GROUP_ID", groupID);
		return sql;
	}
	
	public static String getAddUserRole(String userID,String roleID,String mdfUser){
		 String sql="insert into  USER_ROLE(ROLE_ID,USER_ID,MDF_USER,MDF_TIME) values('@ROLE_ID','@USER_ID','@MDF_USER',@MDF_TIME)";
		sql=sql.replace("@ROLE_ID", roleID);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@MDF_USER", mdfUser);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getSetUserRole(String userID,String roleID){
		 String sql="update   USERS set ROLE_ID='@ROLE_ID',LOG_TIME=@LOG_TIME where  USER_ID ='@USER_ID'";
		sql=sql.replace("@ROLE_ID", roleID);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@LOG_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getRemoveUserRole(String userID,String roleID){
		 String sql="delete from USER_ROLE where ROLE_ID='@ROLE_ID' and USER_ID='@USER_ID'";
		sql=sql.replace("@ROLE_ID", roleID);
		sql=sql.replace("@USER_ID", userID);
		return sql;
	}
	
	
	public static String getUptGroup(String userID,String group){
		 String sql="select  USER_ID,USER_NAME,PASSWD,EMAIL,PHONE,STATUS,ERROR_TIMES,LOG_TIME,ROLE_ID,GROUP_ID,APPS from USERS where USER_ID<>'@USER_ID' order by USER_ID";
		sql=sql.replace("@USER_ID", userID);
		return sql;
	}
	
	public static String getAddGroup(String userID,String groupID){
		 String sql="insert into GROUP_USER(GROUP_ID,USER_ID,UPT_USER,UPT_TIME) values('@GROUP_ID','@USER_ID','@UPT_USER',@UPT_TIME)" ;
		sql=sql.replace("@GROUP_ID", groupID);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@UPT_USER", Context.session.userID);
		sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getDeleteGroup(String userID,String groupID){
		 String sql="delete from GROUP_USER where GROUP_ID='@GROUP_ID' and USER_ID='@USER_ID'" ;
		sql=sql.replace("@GROUP_ID", groupID);
		sql=sql.replace("@USER_ID", userID);
		return sql;
	}
	
	public static String getDeleteByGroup(String groupID){
		 String sql="delete from GROUP_USER where GROUP_ID='@GROUP_ID'" ;
		sql=sql.replace("@GROUP_ID", groupID);
		return sql;
	}
	
	public static String getAdd(String id,String name,String email,String phone,String status,String error,String roleID,String groupID,String apps){
		 String sql="insert into USERS(USER_ID,USER_NAME,EMAIL,PHONE,STATUS,ERROR_TIMES,LOG_TIME,ROLE_ID,GROUP_ID,APPS) "+
				 		   "values('@USER_ID','@USER_NAME','@EMAIL','@PHONE','@STATUS','@ERROR_TIMES',@LOG_TIME,'@ROLE_ID','@GROUP_ID','@APPS')" ;
			sql=sql.replace("@USER_ID", id);
			sql=sql.replace("@USER_NAME", name);
			sql=sql.replace("@EMAIL", email);
			sql=sql.replace("@PHONE", phone);
			sql=sql.replace("@STATUS", status);
			sql=sql.replace("@ERROR_TIMES", error);
			sql=sql.replace("@ROLE_ID", roleID);
			sql=sql.replace("@GROUP_ID", groupID);
			sql=sql.replace("@APPS", apps);
			sql=sql.replace("@LOG_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
	}
	
	public static String getUpt(String id,String name,String email,String phone,String status,String error,String roleID,String groupID,String apps){
		 String sql="update  USERS set USER_NAME='@USER_NAME',EMAIL='@EMAIL',PHONE='@PHONE',STATUS='@STATUS',ERROR_TIMES='@ERROR_TIMES',"+
				 		"LOG_TIME=@LOG_TIME,ROLE_ID='@ROLE_ID',GROUP_ID='@GROUP_ID',APPS='@APPS' where  USER_ID='@USER_ID'" ;
			sql=sql.replace("@USER_ID", id);
			sql=sql.replace("@USER_NAME", name);
			sql=sql.replace("@EMAIL", email);
			sql=sql.replace("@PHONE", phone);
			sql=sql.replace("@STATUS", status);
			sql=sql.replace("@ERROR_TIMES", error);
			sql=sql.replace("@ROLE_ID", roleID);
			sql=sql.replace("@GROUP_ID", groupID);
			sql=sql.replace("@APPS", apps);
			sql=sql.replace("@LOG_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
	}
	
	public static String getUserExist(String userID){
		 String sql="select  count(*) as COUNT from USERS where USER_ID='@USER_ID' ";
		sql=sql.replace("@USER_ID", userID);
		return sql;
	}
}
