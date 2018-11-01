package model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import bean.BackLogBean;

import sql.BACKLOGSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class BACKLOG {
	
	//根据任务编号取任务对应的需求条目
    public static BackLogBean getReq(String id){
		Connection conn = null;
		BackLogBean  bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=BACKLOGSql.getReqById(id);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					 bean=new BackLogBean();
					bean.setId(dataLine.get("ID"));
					bean.setSubmitUser(dataLine.get("SUSER"));
					bean.setSubmitDept(dataLine.get("DEPT"));
					bean.setSubmitDate(dataLine.get("SDATE"));
					bean.setInrollUser(dataLine.get("IUSER"));
					bean.setName(dataLine.get("NAME"));
					bean.setBackground(dataLine.get("BACKGROUND"));
					bean.setExpectDesc(dataLine.get("RDESC"));
					bean.setExpectDate(dataLine.get("RDATE"));
					bean.setDateReason(dataLine.get("REASON"));
					bean.setCheckUser(dataLine.get("CUSER"));
					bean.setAuditUser(dataLine.get("AUSER"));
					bean.setComment(dataLine.get("COMMENT"));
					bean.setSource(dataLine.get("RSRC"));
					bean.setType(dataLine.get("RTYPE"));
					bean.setRclass(dataLine.get("RCLASS"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setLink(dataLine.get("LINK"));
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
    
    public static int updateBackLog(String id,String curUserID,String status,String syncflag){
    	Connection conn = null;
    	int count=0;
    	try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=BACKLOGSql.getReqUpt(id, status, curUserID, syncflag);
			count=SqlServerUtil.executeUpdate(sql, conn);			
		}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
    	return count;
    }
    
    public static int delay(String reqID){
    	Connection conn = null;
    	int count=0;
    	try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=BACKLOGSql.getDelay(reqID);
			count=SqlServerUtil.executeUpdate(sql, conn);			
		   }catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
    	return count;
    }
}
