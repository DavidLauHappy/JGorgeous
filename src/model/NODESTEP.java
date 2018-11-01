package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.NODESTEPBean;
import sql.NODESTEPSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class NODESTEP {

	public static int add(NODESTEPBean data){
		Connection conn = null;
		int  num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESTEPSql.addData(data.getPkgID(), data.getSystemID(), data.getNodeID(), data.getStepID(), data.getFlag(), data.getMdfUser());
			num=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
	
	public static List<NODESTEPBean> getStepNode(String pkgID,String systemID,String stepID){
		List<NODESTEPBean> data=new ArrayList<NODESTEPBean>();
		Connection conn = null;
		int  num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESTEPSql.getStepNode(pkgID, systemID, stepID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					NODESTEPBean bean=new NODESTEPBean();
					bean.setPkgID(dataLine.get("PKG_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setStepID(dataLine.get("STEP_ID"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static int delete(String pkgID,String systemID,String stepID){
		Connection conn = null;
		int  num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESTEPSql.getDelete(pkgID, systemID, stepID);
			num=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
	
	public static int delete(String pkgID,String systemID,String stepID,String nodeID){
		Connection conn = null;
		int  num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESTEPSql.getDelete(pkgID, systemID, nodeID, stepID);
			num=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
	
	public static int setFlag(String pkgID,String systemID,String stepID,String nodeID,String flag){
		Connection conn = null;
		int  num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESTEPSql.getSetFlag(pkgID, systemID, nodeID, stepID, flag);
			num=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
}
