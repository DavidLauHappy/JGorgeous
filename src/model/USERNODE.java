package model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import sql.USERNODESql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class USERNODE {

	public static boolean nodeSelected(String nodeId,String userID){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERNODESql.getExistByID(nodeId,userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return true;
				}
			}
		}catch(Exception e){
			return false;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
	
	public static int  addUserNode(String userID,String nodeID,String status,String seq,String type,String schFlag,String os){
		Connection conn = null;
		int num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERNODESql.getAddData(userID, nodeID, status, seq, type, schFlag,os);
			num=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){

		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
	
	public static int updateBaisc(String userID,String nodeID,String seq,String os,String sPort){
		Connection conn = null;
		int num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERNODESql.getUptBasicInfo(userID, nodeID, seq, os,sPort);
			num=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){

		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
	
	public static int delete(String userID,String nodeID){
		Connection conn = null;
		int num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERNODESql.getDelete(userID, nodeID);
			num=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){

		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
	
	
	public static int updateDBInfo(String userID,String nodeID, String dbtype,String dbuser, String dbpasswd, String dbname, String backdbname, String port){
		Connection conn = null;
		int num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERNODESql.getUptDBInfo(userID, nodeID, dbtype,dbuser, dbpasswd, dbname, backdbname, port);
			num=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){

		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
	
	public static int updateSftpInfo(String userID,String nodeID,String  sftpuser,String sftppasswd, String port, String dir){
		Connection conn = null;
		int num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERNODESql.getUptSftpInfo(userID, nodeID, sftpuser, sftppasswd, port, dir);
			num=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){

		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
	
	
}
