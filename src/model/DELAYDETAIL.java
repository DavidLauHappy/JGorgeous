package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sql.DELAYDETAILSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

import bean.DelayDate;

public class DELAYDETAIL {
	
	public static int add(DelayDate bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DELAYDETAILSql.getInsert(bean.getReqID(), bean.getStaskID(), bean.getApplyID(), bean.applyUserID, bean.getApprUserID(), bean.getStatus(), bean.getReason(),bean.getOldDate(), bean.getNewDate());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	
	public static int approved(DelayDate bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DELAYDETAILSql.getApproveSet(bean.getApplyID(), bean.getStatus(), bean.getComment());
			count=SqlServerUtil.executeUpdate(sql, conn);
			//申请延期审批通过后，需要标识和统计
			if((DelayDate.Status.Approved.ordinal()+"").equals(bean.getStatus())){
				 TASK.DelayDate(bean.getStaskID(), bean.getNewDate(),bean.getReqID());
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static List<DelayDate>  getApprList(String applyUserID,String userID,String status,String keyword){
		Connection conn = null;
		List<DelayDate> data=new ArrayList<DelayDate>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DELAYDETAILSql.getApprs(applyUserID, userID, status, keyword);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					 Map<String,String> dataLine=(Map)result.get(w);
					 DelayDate bean=new DelayDate();
					 bean.setApplyID(dataLine.get("LOG_ID"));
					 bean.setApplyUserID(dataLine.get("APPLY_USER"));
					 bean.setApplyTime(dataLine.get("APPLY_TIME"));
					 bean.setApprUserID(dataLine.get("APPR_USER"));
					 bean.setName(dataLine.get("NAME"));
					 bean.setReqID(dataLine.get("REQ_ID"));
					 bean.setStaskID(dataLine.get("TASK_ID"));
					 bean.setStatus(dataLine.get("DSTATUS"));
					 bean.setReason(dataLine.get("REASON"));
					 bean.setComment(dataLine.get("DCOMMENT"));
					 bean.setOldDate(dataLine.get("PDATE"));
					 bean.setNewDate(dataLine.get("DDATE"));
					 bean.setMtime(dataLine.get("MTIME"));
					 bean.setDtime(dataLine.get("DTIME"));
					 data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
		
	}
	
	public static List<DelayDate>  getApprovedList(String applyUserID,String userID,String status,String keyword){
		Connection conn = null;
		List<DelayDate> data=new ArrayList<DelayDate>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DELAYDETAILSql.getApproves(applyUserID, userID, status, keyword);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					 Map<String,String> dataLine=(Map)result.get(w);
					 DelayDate bean=new DelayDate();
					 bean.setApplyID(dataLine.get("LOG_ID"));
					 bean.setApplyUserID(dataLine.get("APPLY_USER"));
					 bean.setApplyTime(dataLine.get("APPLY_TIME"));
					 bean.setApprUserID(dataLine.get("APPR_USER"));
					 bean.setName(dataLine.get("NAME"));
					 bean.setReqID(dataLine.get("REQ_ID"));
					 bean.setStaskID(dataLine.get("TASK_ID"));
					 bean.setStatus(dataLine.get("DSTATUS"));
					 bean.setReason(dataLine.get("REASON"));
					 bean.setComment(dataLine.get("DCOMMENT"));
					 bean.setOldDate(dataLine.get("PDATE"));
					 bean.setNewDate(dataLine.get("DDATE"));
					 bean.setMtime(dataLine.get("MTIME"));
					 bean.setDtime(dataLine.get("DTIME"));
					 data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
		
	}
	
	public static List<DelayDate>  getApplyList(String applyUserID,String keyword){
		Connection conn = null;
		List<DelayDate> data=new ArrayList<DelayDate>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DELAYDETAILSql.getApplys(applyUserID, keyword);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					 Map<String,String> dataLine=(Map)result.get(w);
					 DelayDate bean=new DelayDate();
					 bean.setApplyID(dataLine.get("LOG_ID"));
					 bean.setApplyUserID(dataLine.get("APPLY_USER"));
					 bean.setApplyTime(dataLine.get("APPLY_TIME"));
					 bean.setApprUserID(dataLine.get("APPR_USER"));
					 bean.setName(dataLine.get("NAME"));
					 bean.setReqID(dataLine.get("REQ_ID"));
					 bean.setStaskID(dataLine.get("TASK_ID"));
					 bean.setStatus(dataLine.get("DSTATUS"));
					 bean.setReason(dataLine.get("REASON"));
					 bean.setComment(dataLine.get("DCOMMENT"));
					 bean.setOldDate(dataLine.get("PDATE"));
					 bean.setNewDate(dataLine.get("DDATE"));
					 bean.setMtime(dataLine.get("MTIME"));
					 bean.setDtime(dataLine.get("DTIME"));
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
