package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sql.FLOWDEFSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

import bean.FLOWDEFBean;

public class FLOWDEF {
	
	 public static FLOWDEFBean getByFunc(String funcID){
		 Connection conn = null;
		 FLOWDEFBean  bean=null;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=FLOWDEFSql.getByFunc(funcID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
						Map dataLine=(Map)result.get(0);
					    bean=new FLOWDEFBean();
						bean.setId((String)dataLine.get("ID"));
						bean.setFuncID((String)dataLine.get("FUNC_ID"));
						bean.setName((String)dataLine.get("NAME"));
						bean.setEnable((String)dataLine.get("IS_ENABLE"));
						bean.setMdfUser((String)dataLine.get("MDF_USER"));
						bean.setMdfTime((String)dataLine.get("MDF_TIME"));
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return bean;
	 }
	 
	 public static FLOWDEFBean getByID(String ID){
		 Connection conn = null;
		 FLOWDEFBean  bean=null;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=FLOWDEFSql.getByID(ID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
						Map dataLine=(Map)result.get(0);
					    bean=new FLOWDEFBean();
						bean.setId((String)dataLine.get("ID"));
						bean.setFuncID((String)dataLine.get("FUNC_ID"));
						bean.setName((String)dataLine.get("NAME"));
						bean.setEnable((String)dataLine.get("IS_ENABLE"));
						bean.setMdfUser((String)dataLine.get("MDF_USER"));
						bean.setMdfTime((String)dataLine.get("MDF_TIME"));
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return bean;
	 }
	 
	 public static List<FLOWDEFBean> getFlows(){
		 Connection conn = null;
		 List<FLOWDEFBean> result=new ArrayList<FLOWDEFBean>();
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=FLOWDEFSql.getFlows();
				List sqlResult=SqlServerUtil.executeQuery(sql, conn);
				if(sqlResult!=null&&sqlResult.size()>0){
					for(int w=0;w<sqlResult.size();w++){
						Map dataLine=(Map)sqlResult.get(w);
						FLOWDEFBean bean=new FLOWDEFBean();
						bean.setId((String)dataLine.get("ID"));
						bean.setFuncID((String)dataLine.get("FUNC_ID"));
						bean.setName((String)dataLine.get("NAME"));
						bean.setEnable((String)dataLine.get("IS_ENABLE"));
						bean.setMdfUser((String)dataLine.get("MDF_USER"));
						bean.setMdfTime((String)dataLine.get("MDF_TIME"));
						result.add(bean);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return result;
	 }
	 
	 public static int setFlowStatus(String flowID,String flowStatus,String funcID,String mdfUser){
			Connection conn = null;
			int count=0;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=FLOWDEFSql.getFlowEnable(flowID, flowStatus, mdfUser);
				count=SqlServerUtil.executeUpdate(sql, conn);
				String funcStatus="";
				if("0".equals(flowStatus)){
					funcStatus="0";
				}else{
					funcStatus="2";
				}
				sql=FLOWDEFSql.getFuncEnable(funcID, funcStatus);
				count=SqlServerUtil.executeUpdate(sql, conn);
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return count;
		}
}
