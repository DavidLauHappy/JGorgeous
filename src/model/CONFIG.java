package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sql.CONFIGSql;
import utils.SqlServerUtil;
import bean.ConfigBean;

import common.db.DBConnectionManager;

public class CONFIG {
	public static  List<ConfigBean> getConfigs(String app,String filename,String userID){
		Connection conn = null;
		List<ConfigBean>  data=new ArrayList<ConfigBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=CONFIGSql.getConfig(app, filename, userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					ConfigBean bean=new ConfigBean();
					bean.setApp(dataLine.get("APP"));
					bean.setEnvType(dataLine.get("ENV_TYPE"));
					bean.setFileName(dataLine.get("CFG_FILE"));
					bean.setParamName(dataLine.get("PARAM_NAME"));
					bean.setParamVal(dataLine.get("PARAM_VAL"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
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
	
	public static  List<ConfigBean> getConfigs(String app,String userID){
		Connection conn = null;
		List<ConfigBean>  data=new ArrayList<ConfigBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=CONFIGSql.getConfig(app, userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					ConfigBean bean=new ConfigBean();
					bean.setApp(dataLine.get("APP"));
					bean.setEnvType(dataLine.get("ENV_TYPE"));
					bean.setFileName(dataLine.get("CFG_FILE"));
					bean.setParamName(dataLine.get("PARAM_NAME"));
					bean.setParamVal(dataLine.get("PARAM_VAL"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
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
	
	public static int add(ConfigBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=CONFIGSql.getInsert(bean.getApp(), bean.getEnvType(), bean.getFileName(), bean.getParamName(), bean.getParamVal(), bean.getCrtUser());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int update(ConfigBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=CONFIGSql.getUpdate(bean.getApp(), bean.getEnvType(), bean.getFileName(), bean.getParamName(), bean.getParamVal(), bean.getCrtUser());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
}
