package model;

import java.sql.Connection;
import bean.QUESTIONFILEBean;
import sql.QUESTIONFILESql;
import utils.SqlServerUtil;
import common.db.DBConnectionManager;

public class QUESTIONFILE {
	
	public static int getAdd(QUESTIONFILEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONFILESql.getAdd(bean.getQid(), bean.getFileId(), bean.getFileName(), bean.getFileTime(), bean.getMd5(), bean.getCrtUser(), bean.getQfileType());
			count=SqlServerUtil.executeUpdate(sql, conn);
			if(count>0){
				sql=QUESTIONFILESql.getInsertFile(bean.getMd5(), bean.getRemotePath(), bean.getCrtUser());
				count=SqlServerUtil.executeUpdate(sql, conn);
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int getDelete(QUESTIONFILEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONFILESql.getDelete(bean.getQid(), bean.getFileId());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
}
