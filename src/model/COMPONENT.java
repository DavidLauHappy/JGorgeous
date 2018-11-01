package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.COMPONENTBean;
import bean.Cluster;

import sql.COMPONENTSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class COMPONENT {

	public static int addDataFlag(COMPONENTBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMPONENTSql.getInsert(bean.getId(), bean.getName(), bean.getAbbr(), bean.getType(),  bean.getSystemID(), bean.getStatus(), bean.getMdfUser(), bean.getFlag());
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
			String sql=COMPONENTSql.getDeleteByFlag(flag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static List<COMPONENTBean> getComponents(String bussID,String flag){
		Connection conn = null;
		List<COMPONENTBean>  data=new ArrayList<COMPONENTBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMPONENTSql.getQueryBySystem(bussID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					COMPONENTBean bean=new COMPONENTBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setAbbr(dataLine.get("ABBR"));
					bean.setType(dataLine.get("TYPE"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFlag(dataLine.get("FLAG"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static COMPONENTBean getComponentByID(String componentID,String flag){
		Connection conn = null;
		COMPONENTBean  bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMPONENTSql.getComponentByID(componentID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					 bean=new COMPONENTBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setAbbr(dataLine.get("ABBR"));
					bean.setType(dataLine.get("TYPE"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFlag(dataLine.get("FLAG"));
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	public static List<COMPONENTBean> getUserComponents(String userID,String bussID,String flag){
		Connection conn = null;
		List<COMPONENTBean>  data=new ArrayList<COMPONENTBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMPONENTSql.getQueryByUserSystem(userID, bussID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					COMPONENTBean bean=new COMPONENTBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setAbbr(dataLine.get("ABBR"));
					bean.setType(dataLine.get("TYPE"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFlag(dataLine.get("FLAG"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static List<Cluster> getCuster(String componentID,String flag){
		Connection conn = null;
		List<Cluster>  data=new ArrayList<Cluster>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMPONENTSql.geCluster(componentID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					Cluster bean=new Cluster(dataLine.get("COMPONET_ID"),dataLine.get("CLUSTER"));
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
