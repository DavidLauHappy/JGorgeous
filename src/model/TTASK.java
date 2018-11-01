package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.TTaskBean;
import bean.TaskBean;

import sql.TTASKSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class TTASK {
	
		public static int add(TTaskBean bean){
			Connection conn = null;
			int count=0;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=TTASKSql.getAdd(bean.getViewID(), bean.getId(), bean.getUserID(), bean.getStatus(), bean.getCrtUser());
				count=SqlServerUtil.executeUpdate(sql, conn);
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return count;
		}
		
		
		public static List<TTaskBean> getTasksByView(String viewID){
			Connection conn = null;
			List<TTaskBean>  data=new ArrayList<TTaskBean>();
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=TTASKSql.getTasks(viewID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					for(int w=0;w<result.size();w++){
						Map<String,String> dataLine=(Map)result.get(w);
						TTaskBean bean=new TTaskBean();
						bean.setViewID(dataLine.get("VIEW_ID"));
						bean.setId(dataLine.get("ID"));
						bean.setUserID(dataLine.get("USER_ID"));
						bean.setCrtUser(dataLine.get("CRT_USER"));
						bean.setStatus(dataLine.get("STATUS"));
						bean.setCrtTime(dataLine.get("CRT_TIME"));
						data.add(bean);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return data;		
		}
		
		public static List<TTaskBean> getMyTasks(String userID){
			Connection conn = null;
			List<TTaskBean>  data=new ArrayList<TTaskBean>();
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=TTASKSql.getMyTasks(userID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					for(int w=0;w<result.size();w++){
						Map<String,String> dataLine=(Map)result.get(w);
						TTaskBean bean=new TTaskBean();
						bean.setViewID(dataLine.get("VIEW_ID"));
						bean.setId(dataLine.get("ID"));
						bean.setUserID(dataLine.get("USER_ID"));
						bean.setCrtUser(dataLine.get("CRT_USER"));
						bean.setStatus(dataLine.get("STATUS"));
						bean.setCrtTime(dataLine.get("CRT_TIME"));
						bean.setViewName(dataLine.get("VIEW_NAME"));
						data.add(bean);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return data;		
		}
		
		//设置测试任务的状态
		public static int setStatus(String taskID,String status,String userID){
			Connection conn = null;
			int count=0;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=TTASKSql.getStatusSet(taskID, status, userID);
				count=SqlServerUtil.executeUpdate(sql, conn);
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return count;
		}
		
}
