package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sql.APPSYSTEMSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;
import bean.App;


public class APPSYSTEM {
		
	public static List<App> getAllSystem(){
		Connection conn = null;
		List<App>  data=new ArrayList<App>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=APPSYSTEMSql.getSelect();
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					App app=new App();
					app.setApp(dataLine.get("APP"));
					app.setFlag(dataLine.get("IS_ENABLE"));
					app.setMdfTime(dataLine.get("MDF_TIME"));
					app.setMdfuser(dataLine.get("USER_NAME")+"("+dataLine.get("USER_ID")+")");
					app.setName(dataLine.get("SYSNAME"));
					app.setSystem(dataLine.get("SYS"));
					app.setTimespan(dataLine.get("TIMESAPN"));
					data.add(app);
				}
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static  int update(String app,String sys,String timespan,String flag,String userID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=APPSYSTEMSql.getUpt(app, sys, timespan, flag, userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static String getAppSpan(String app,String systemID){
		Connection conn = null;
		String ret="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=APPSYSTEMSql.getTimeSpan(app, systemID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map<String,String> dataLine=(Map)result.get(0);
				ret=dataLine.get("TIMESPAN");
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return ret;
	}
	
}
