package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.GroupBean;

import resource.Context;
import sql.GROUPSql;

import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class GROUPS {
	public static List<GroupBean> getGroups(){
		Connection conn = null;
		List<GroupBean>  data=new ArrayList<GroupBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=GROUPSql.getGroups();
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					GroupBean bean=new GroupBean(dataLine.get("GROUP_ID"),dataLine.get("GROUP_NAME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	
	public static String getID(){
		Connection conn = null;
		String id="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=GROUPSql.getMaxID();
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map dataLine=(Map)result.get(0);
					id=dataLine.get("num")+"";
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return id;
	}
	
	public static int addGroup(GroupBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=GROUPSql.getAdd(bean.getId(), bean.getName(), "0", Context.session.userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int deleteGroup(String groupID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=GROUPSql.getDelete(groupID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static boolean groupExist(String groupName){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=GROUPSql.getGroupExist(groupName);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return true;
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
}
