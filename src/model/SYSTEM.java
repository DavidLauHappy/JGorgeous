package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.SYSTEMBean;
import bean.Triple;
import sql.SYSTEMSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class SYSTEM {
	
	public static int addDataFlag(SYSTEMBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=SYSTEMSql.getInsert(bean.getId(), bean.getName(), bean.getAbbr(), bean.getApp(), bean.getCate(), bean.getBussID(), bean.getStatus(), bean.getMdfUser(), bean.getFalg());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int deleteByFlag(String flag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=SYSTEMSql.getDeleteByFlag(flag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}

	public static List<String> getAppSystem(String appID){
		Connection conn = null;
		List<String>  data=new ArrayList<String>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=SYSTEMSql.getQueryByApp(appID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					data.add(dataLine.get("SYS"));
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}

	public static List<SYSTEMBean> getSystems(String appID,String flag){
		Connection conn = null;
		List<SYSTEMBean>  data=new ArrayList<SYSTEMBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=SYSTEMSql.getQueryByApp(appID,flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					SYSTEMBean bean=new SYSTEMBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setAbbr(dataLine.get("ABBR"));
					bean.setApp(dataLine.get("APP"));
					bean.setCate(dataLine.get("CATE"));
					bean.setBussID(dataLine.get("BUSSID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFalg(dataLine.get("FLAG"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	
	public static List<SYSTEMBean> getSystems(String userID,String pkgID,String flag){
		Connection conn = null;
		List<SYSTEMBean>  data=new ArrayList<SYSTEMBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=SYSTEMSql.getQueryByPkg(userID, pkgID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					SYSTEMBean bean=new SYSTEMBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setAbbr(dataLine.get("ABBR"));
					bean.setApp(dataLine.get("APP"));
					bean.setCate(dataLine.get("CATE"));
					bean.setBussID(dataLine.get("BUSSID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFalg(dataLine.get("FLAG"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static SYSTEMBean getSystemsByID(String bussID,String flag){
		Connection conn = null;
		SYSTEMBean  bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=SYSTEMSql.getQueryByID(bussID,flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					bean=new SYSTEMBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setAbbr(dataLine.get("ABBR"));
					bean.setApp(dataLine.get("APP"));
					bean.setCate(dataLine.get("CATE"));
					bean.setBussID(dataLine.get("BUSSID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFalg(dataLine.get("FLAG"));
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	//为了支持中断后能继续调度上次的节点，调度过的节点状态不能重新设置
	public static int setNodeSchedule(String userID,String bussID,String flag,String nodeFlag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=SYSTEMSql.getSetNodesFlag(userID, bussID, flag, nodeFlag);
			count=SqlServerUtil.executeUpdate(sql, conn);
			///
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
}
