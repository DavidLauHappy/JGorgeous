package model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import bean.ViewFileBean;
import sql.VIEWFILESql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class VIEWFILE {

	public static int add(ViewFileBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWFILESql.getAdd(bean.getFileID(), bean.getFileName(), bean.getFileTime(), bean.getMd5(), bean.getViewID(), bean.getStreamID(), bean.getCrtUser(), bean.getMdfUser(), bean.getVersionID(),bean.getType());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int addObj(String md5,String remoteLocation,String userID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWFILESql.getAddFileObj(md5, remoteLocation, userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static boolean md5Exists(String md5){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWFILESql.checkExist(md5);
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				Map dataLine=(Map)sqlResult.get(0);
				String result=dataLine.get("COUNT")+"";
				count=Integer.parseInt(result);
				if(count>0)
					return true;
				return false;
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
	
	public static int remove(String fileID,String viewID,String version){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWFILESql.getRemove(viewID, version, fileID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
}
