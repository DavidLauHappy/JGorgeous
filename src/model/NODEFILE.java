package model;

import java.sql.Connection;

import bean.NODEFILEBean;

import sql.NODEFILESql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class NODEFILE {

	public static int addData(NODEFILEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODEFILESql.getAdd(bean.getPkgID(), bean.getNodeID(), bean.getFileID(), bean.getMd5(), bean.getMdfUser());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int deleteData(NODEFILEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODEFILESql.getDelete(bean.getPkgID(), bean.getNodeID(), bean.getFileID());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
}
