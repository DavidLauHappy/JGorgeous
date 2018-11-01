package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.NODEFILESTTBean;

import sql.NODEFILESTTSql;
import utils.SqlServerUtil;


import common.db.DBConnectionManager;

public class NODEFILESTT {

	public static int addData(NODEFILESTTBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODEFILESTTSql.getInsert(bean.getPkgID(), bean.getSystemID(), bean.getNodeID(), bean.getFileDir(), bean.getFileCount(), bean.getMdfUser());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int deleteSystemData(String pkgID,String systemID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODEFILESTTSql.getDelete(pkgID, systemID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static List<NODEFILESTTBean> getNodeFileStatistic(String pkgID,String systemID,String flag){
		Connection conn = null;
		List<NODEFILESTTBean>  data=new ArrayList<NODEFILESTTBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODEFILESTTSql.getBySystem(pkgID, systemID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					NODEFILESTTBean bean=new NODEFILESTTBean();
					bean.setPkgID(dataLine.get("PKG_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setFileDir(dataLine.get("FILE_DIR"));
					bean.setFileCount(dataLine.get("FILE_CNT"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setNodeName(dataLine.get("NODE_NAME"));
					bean.setSystemName(dataLine.get("SYSTEM_NAME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	
	
	
	
}
