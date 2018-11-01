package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sql.GROUPUSERSql;
import utils.SqlServerUtil;
import bean.GroupUserBean;

import common.db.DBConnectionManager;

public class GROUPUSER {
	
	//根据群组编号获取群组用户
    public static List<GroupUserBean> getUsers(String groupID){
		Connection conn = null;
		List<GroupUserBean> data=new ArrayList<GroupUserBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=GROUPUSERSql.getGroupUser(groupID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					GroupUserBean bean=new GroupUserBean();
					 bean.setGroupID(dataLine.get("GROUP_ID"));
					 bean.setUserID(dataLine.get("USER_ID"));
					 bean.setUserFullName(dataLine.get("USER_NAME")+"("+dataLine.get("USER_ID").trim()+")");
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
