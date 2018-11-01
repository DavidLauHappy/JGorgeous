package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.UserBean;
import sql.USERSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class USERS {

	public static UserBean getUser(String userID){
		Connection conn = null;
		UserBean  bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getUser(userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map dataLine=(Map)result.get(0);
				    bean=new UserBean();
					bean.setUserID((String)dataLine.get("USER_ID"));
					bean.setUserName((String)dataLine.get("USER_NAME"));
					bean.setPasswd((String)dataLine.get("PASSWD"));
					bean.setEmail((String)dataLine.get("EMAIL"));
					bean.setPhone((String)dataLine.get("PHONE"));
					bean.setStatus((String)dataLine.get("STATUS"));
					bean.setErrorTimes(dataLine.get("ERROR_TIMES")+"");
					bean.setLogTime((String)dataLine.get("LOG_TIME"));
					bean.setRoleID((String)dataLine.get("ROLE_ID"));
					bean.setGroupID((String)dataLine.get("GROUP_ID"));
					bean.setApps((String)dataLine.get("APPS"));
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	public static List<UserBean> getAllUser(String userID){
		Connection conn = null;
		List<UserBean>  data=new ArrayList<UserBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getAllUser(userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map dataLine=(Map)result.get(w);
					UserBean bean=new UserBean();
					bean.setUserID((String)dataLine.get("USER_ID"));
					bean.setUserName((String)dataLine.get("USER_NAME"));
					bean.setPasswd((String)dataLine.get("PASSWD"));
					bean.setEmail((String)dataLine.get("EMAIL"));
					bean.setPhone((String)dataLine.get("PHONE"));
					bean.setStatus((String)dataLine.get("STATUS"));
					bean.setErrorTimes(dataLine.get("ERROR_TIMES")+"");
					bean.setLogTime((String)dataLine.get("LOG_TIME"));
					bean.setRoleID((String)dataLine.get("ROLE_ID"));
					bean.setGroupID((String)dataLine.get("GROUP_ID"));
					bean.setApps((String)dataLine.get("APPS"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static List<UserBean> getRoleUsers(String roleID){
		Connection conn = null;
		List<UserBean>  data=new ArrayList<UserBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getRoleUsers(roleID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map dataLine=(Map)result.get(w);
					UserBean bean=new UserBean();
					bean.setUserID((String)dataLine.get("USER_ID"));
					bean.setUserName((String)dataLine.get("USER_NAME"));
					bean.setPasswd((String)dataLine.get("PASSWD"));
					bean.setEmail((String)dataLine.get("EMAIL"));
					bean.setPhone((String)dataLine.get("PHONE"));
					bean.setStatus((String)dataLine.get("STATUS"));
					bean.setErrorTimes(dataLine.get("ERROR_TIMES")+"");
					bean.setLogTime((String)dataLine.get("LOG_TIME"));
					bean.setRoleID((String)dataLine.get("ROLE_ID"));
					bean.setGroupID((String)dataLine.get("GROUP_ID"));
					bean.setApps((String)dataLine.get("APPS"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static List<UserBean> getGroupUsers(String groupID){
		Connection conn = null;
		List<UserBean>  data=new ArrayList<UserBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getGroupUsers(groupID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map dataLine=(Map)result.get(w);
					UserBean bean=new UserBean();
					bean.setUserID((String)dataLine.get("USER_ID"));
					bean.setUserName((String)dataLine.get("USER_NAME"));
					bean.setPasswd((String)dataLine.get("PASSWD"));
					bean.setEmail((String)dataLine.get("EMAIL"));
					bean.setPhone((String)dataLine.get("PHONE"));
					bean.setStatus((String)dataLine.get("STATUS"));
					bean.setErrorTimes(dataLine.get("ERROR_TIMES")+"");
					bean.setLogTime((String)dataLine.get("LOG_TIME"));
					bean.setRoleID((String)dataLine.get("ROLE_ID"));
					bean.setGroupID((String)dataLine.get("GROUP_ID"));
					bean.setApps((String)dataLine.get("APPS"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static int addGroup(String userID,String groupID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getAddGroup(userID, groupID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int deleteGroup(String userID,String groupID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getDeleteGroup(userID, groupID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int deleteByGroup(String groupID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getDeleteByGroup(groupID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setRole(String userID,String roleID,String mdfUser){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getAddUserRole(userID, roleID,mdfUser);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setUserRole(String userID,String roleID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getSetUserRole(userID, roleID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int removeRole(String userID,String roleID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getRemoveUserRole(userID, roleID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int addUser(UserBean user){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getAdd(user.getUserID(), user.getUserName(), user.getEmail(), user.getPhone(), user.getStatus(), user.getErrorTimes(), user.getRoleID(), user.getGroupID(), user.getApps());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int updateUser(UserBean user){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getUpt(user.getUserID(), user.getUserName(), user.getEmail(), user.getPhone(), user.getStatus(), user.getErrorTimes(), user.getRoleID(), user.getGroupID(), user.getApps());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static boolean userExists(String userID){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERSql.getUserExist(userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return true;
				}
			}
		}catch(Exception e){
		
		}
		finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
		return false;
	}
	
}
