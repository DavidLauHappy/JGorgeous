package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.RoleBean;

import sql.ROLESql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class ROLES {

	public static List<RoleBean> getRoles(){
		Connection conn = null;
		List<RoleBean>  data=new ArrayList<RoleBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=ROLESql.getRoles();
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					RoleBean bean=new RoleBean(dataLine.get("ID"),dataLine.get("NAME"));
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
