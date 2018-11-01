package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.NODEBean;
import bean.PKGSYSTEMBean;
import sql.PKGSYSTEMSql;
import utils.SqlServerUtil;
import utils.StringUtil;
import common.db.DBConnectionManager;

public class PKGSYSTEM {
	
	
	//根据节点序获取又版本在某个系统的用户指定调度顺序的所有节点
	public static List<String>  getNodes(String userID,String pkgID,String systemID,String flag){
		Connection conn = null;
		List<String>  data=new ArrayList<String>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSYSTEMSql.getNode(pkgID, systemID, flag, userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					data.add(dataLine.get("NODE_ID"));
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static List<PKGSYSTEMBean> getPkgSystems(String userID,String status){
		Connection conn = null;
		List<PKGSYSTEMBean>  data=new ArrayList<PKGSYSTEMBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSYSTEMSql.getByStatus(userID, status);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					PKGSYSTEMBean bean=new PKGSYSTEMBean();
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setPkgID(dataLine.get("PKG_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setStatus(dataLine.get("STATUS"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static List<PKGSYSTEMBean> getMyPkgSystems(String pkgID,String userID){
		Connection conn = null;
		List<PKGSYSTEMBean>  data=new ArrayList<PKGSYSTEMBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSYSTEMSql.getMyPkgSys(userID,pkgID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					PKGSYSTEMBean bean=new PKGSYSTEMBean();
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setPkgID(dataLine.get("PKG_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setStatus(dataLine.get("STATUS"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}

	
	public static int getAdd(PKGSYSTEMBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSYSTEMSql.getAdd(bean.getPkgID(), bean.getSystemID(),bean.getStatus(), bean.getMdfUser());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int updateStatus(String userID,String pkgID,String systemID,String status){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSYSTEMSql.getUptStatus(userID, pkgID, systemID, status);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	//寻找系统中的可调度节点
	//优先找正在调度 发生中断的那个节点
	//再找一切准备好 只等着被调度的节点
	public static NODEBean electNode(String userID,String pkgID,String systemID,String flag){
		Connection conn = null;
		NODEBean  bean=null;
		try{
			String nodeID="";
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSYSTEMSql.getScheduledNode(pkgID, systemID, flag, userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					nodeID=dataLine.get("NODE_ID");
			}
			else{
				sql=PKGSYSTEMSql.getScheduleAbleNode(pkgID, systemID, flag, userID);
				result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					nodeID=dataLine.get("NODE_ID");
				}
			}
			if(!StringUtil.isNullOrEmpty(nodeID)){
				bean=NODE.getNodeByID(userID, nodeID, flag);
			}else{
				bean=null;
			}
			
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	public static boolean  isPkgSysExist(String pkgID,String systemID,String userID){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSYSTEMSql.getPkgSysExists(pkgID, systemID, userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return true;
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
	
	public static  int deletePkgSys(String pkgID,String systemID,String userID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSYSTEMSql.getDeletePkgSys(pkgID, systemID, userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
			
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	
	public static boolean  hasCommand(String userID,String pkgID,String systemID,String status,String dataFlag){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSYSTEMSql.hasCommand(userID, pkgID, systemID, status, dataFlag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return true;
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
}
