package model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import bean.DATAFLAGBean;

import sql.DATAFLAGSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class DATAFLAG {

	public static DATAFLAGBean getByID(String id){
		Connection conn = null;
		DATAFLAGBean data=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DATAFLAGSql.getByID(id);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map<String,String> dataLine=(Map)result.get(0);
				data=new DATAFLAGBean();
				data.setId(id);
				data.setFlag((String)dataLine.get("FLAG"));
				data.setMdfTime((String)dataLine.get("MDF_TIME"));
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static int addDataFlag(String id,String flag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DATAFLAGSql.getInsertSql(id, flag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setFlag(String id,String flag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DATAFLAGSql.getUpdateSql(id, flag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
}
