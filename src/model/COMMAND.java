package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.COMMANDBean;

import common.db.DBConnectionManager;

import sql.COMMANDSql;
import utils.SqlServerUtil;

public class COMMAND {

	public static int add(COMMANDBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getAdd(bean.getPkgID(), bean.getStepID(), bean.getFileID(), bean.getNodeID(), bean.getId(), bean.getName(), bean.getParameter(), bean.getSeq(), bean.getStatus(), bean.getFlag(), bean.getRemote(), bean.getRemind(), bean.getApprID(), bean.getUserID());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static  int updateStatus(String id,String status){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getUptStatusByID(id, status);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static  int updateRemind(String id,String remind){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getUptRemindByID(id, remind);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static  String getCount(String pkgID,String nodeID,String status){
		Connection conn = null;
		String count="0";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getCount(pkgID, nodeID, status);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map<String,String> dataLine=(Map)result.get(0);
				count=dataLine.get("COUNT");
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}

	
	public static int updateExecuteInfo(String id,String status,String logInfo,String remind){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getUptExecuteInfoByID(id, status, logInfo, remind);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int delete(String pkgID,String nodeID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getDelete(pkgID,nodeID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setFlag(String pkgID,String systemID,String flag,String dataFlag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getUptFlag(pkgID, systemID, flag, dataFlag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setFlag(String approveID,String userID,String flag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getUptFlag(approveID, userID, flag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int reSetFlag(String pkgID,String nodeID,String stepID,String flag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getFlagSet(pkgID, nodeID, stepID, flag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static COMMANDBean getCommand(String cmdID){
		Connection conn = null;
		COMMANDBean  bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getCmdByID(cmdID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					bean=new COMMANDBean();
					bean.setPkgID(dataLine.get("PKG_ID"));
					bean.setStepID(dataLine.get("STEP_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setParameter(dataLine.get("PARAMETER"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setRemote(dataLine.get("REMOTE"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setLogInfo(dataLine.get("LOGINFO"));
					bean.setRetTime(dataLine.get("RET_TIME"));
					bean.setRemind(dataLine.get("REMIND"));
					bean.setApprID(dataLine.get("APPRID"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	public static List<COMMANDBean> getRemindCommand(String pkgID,String userID,int fetchNum){
		Connection conn = null;
		List<COMMANDBean>  data=new ArrayList<COMMANDBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getReminds(pkgID, userID, fetchNum);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					COMMANDBean bean=new COMMANDBean();
					bean.setPkgID(dataLine.get("PKG_ID"));
					bean.setStepID(dataLine.get("STEP_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setParameter(dataLine.get("PARAMETER"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setRemote(dataLine.get("REMOTE"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setLogInfo(dataLine.get("LOGINFO"));
					bean.setRetTime(dataLine.get("RET_TIME"));
					bean.setRemind(dataLine.get("REMIND"));
					bean.setApprID(dataLine.get("APPRID"));
					bean.setUserID(dataLine.get("USER_ID"));
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
