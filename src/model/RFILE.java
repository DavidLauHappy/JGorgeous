package model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import sql.RFILESql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class RFILE {
	
	public static boolean fileExist(String md5){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=RFILESql.getFileExist(md5);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map line=(Map)result.get(0);
				String exist=line.get("COUNT")+"";
				count=Integer.parseInt(exist);
				if(count>0)
					return true;
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
}
