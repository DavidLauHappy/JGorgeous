package model;

import java.sql.Connection;
import java.util.UUID;

import sql.FLOWAPPROVERSql;
import sql.MSGSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class FLOWAPPROVER {
	
	    public static int  add(String apprID,String userID,String flowID,String stepID,String mdfUser,String msg){
	    	Connection conn = null;
			int count=0;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=FLOWAPPROVERSql.getAdd(apprID, userID, flowID, stepID, mdfUser);
				count=SqlServerUtil.executeUpdate(sql, conn);
				String msgID=UUID.randomUUID().toString();
				msgID=msgID.replace("-", "");
				sql=MSGSql.getAdd(userID, msgID, msg, mdfUser);
				count=SqlServerUtil.executeUpdate(sql, conn);
				
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return count;
	    }
}
