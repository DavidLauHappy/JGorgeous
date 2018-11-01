package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.FUNCBean;

import sql.FUNCSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class FUNC {

	public  static FUNCBean  getByID(String id){
		Connection conn = null;
		FUNCBean bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=FUNCSql.getByID(id);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					 bean=new FUNCBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIsLog(dataLine.get("IS_LOG"));
					bean.setIsCheck(dataLine.get("IS_CHECK"));
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
}
