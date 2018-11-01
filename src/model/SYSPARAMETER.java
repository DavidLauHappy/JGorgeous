package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.SystemParamBean;
import sql.SYSPARAMETERSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class SYSPARAMETER {

	public static int updateParameter(String key,String value,String mdfUser){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=SYSPARAMETERSql.getUpdate(key, value, mdfUser);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static List<SystemParamBean> getParameters(){
		Connection conn = null;
		List<SystemParamBean>  data=new ArrayList<SystemParamBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=SYSPARAMETERSql.getAllParameter();
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					SystemParamBean bean=new SystemParamBean();
					bean.setName(dataLine.get("NAME"));
					bean.setValue(dataLine.get("VALUE"));
					bean.setDesc(dataLine.get("DESC"));
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
}
