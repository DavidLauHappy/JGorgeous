package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.LOCALCOMMANDBean;
import sql.LOCALCOMMANDSql;
import utils.SqlServerUtil;



import common.db.DBConnectionManager;

public class LOCALCOMMAND {
	public static int add(LOCALCOMMANDBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getAdd(bean.getVersionID(), bean.getFileID(), bean.getNodeID(), bean.getCmdID(), bean.getCmdName(), bean.getCmdText(),  bean.getSeq(), bean.getCmdType(),bean.getStatus(), bean.getRemind(), bean.getUserID(),bean.getLpath(),bean.getMd5(),bean.getRemote());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	
	public static int delete(String versionID,String nodeID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getDelete(versionID, nodeID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static List<LOCALCOMMANDBean> getCommand(String versionID,String nodeID){
		Connection conn = null;
		List<LOCALCOMMANDBean>  data=new ArrayList<LOCALCOMMANDBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getCommand(versionID, nodeID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					LOCALCOMMANDBean bean=new LOCALCOMMANDBean();
					bean.setVersionID(dataLine.get("VER_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setCmdID(dataLine.get("CMD_ID"));
					bean.setCmdName(dataLine.get("CMD_NAME"));
					bean.setCmdText(dataLine.get("CMD_TEXT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setCmdType(dataLine.get("CMD_TYPE"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setLoginfo(dataLine.get("LOGINFO"));
					bean.setRemind(dataLine.get("REMIND"));
					bean.setTime(dataLine.get("TIME"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setLpath(dataLine.get("LPATH"));
					bean.setInstalled(dataLine.get("INSTALLED"));
					bean.setMd5(dataLine.get("MD5"));
					bean.setRemote(dataLine.get("REMOTE"));
					data.add(bean);
				}
			}
		}
		catch(Exception e){
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	//
	public static List<LOCALCOMMANDBean> getScheduleComs(String version){
		Connection conn = null;
		List<LOCALCOMMANDBean>  data=new ArrayList<LOCALCOMMANDBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getScheduleCmd(version);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					LOCALCOMMANDBean bean=new LOCALCOMMANDBean();
					bean.setVersionID(dataLine.get("VER_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setCmdID(dataLine.get("CMD_ID"));
					bean.setCmdName(dataLine.get("CMD_NAME"));
					bean.setCmdText(dataLine.get("CMD_TEXT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setCmdType(dataLine.get("CMD_TYPE"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setLoginfo(dataLine.get("LOGINFO"));
					bean.setRemind(dataLine.get("REMIND"));
					bean.setTime(dataLine.get("TIME"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setLpath(dataLine.get("LPATH"));
					bean.setInstalled(dataLine.get("INSTALLED"));
					bean.setMd5(dataLine.get("MD5"));
					bean.setRemote(dataLine.get("REMOTE"));
					data.add(bean);
				}
			}
		}
		catch(Exception e){
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static List<LOCALCOMMANDBean> getRemindComs(String versionID,String nodeID,int fetchNum){
		Connection conn = null;
		List<LOCALCOMMANDBean>  data=new ArrayList<LOCALCOMMANDBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getReminds(versionID, nodeID, fetchNum);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					LOCALCOMMANDBean bean=new LOCALCOMMANDBean();
					bean.setVersionID(dataLine.get("VER_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setCmdID(dataLine.get("CMD_ID"));
					bean.setCmdName(dataLine.get("CMD_NAME"));
					bean.setCmdText(dataLine.get("CMD_TEXT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setCmdType(dataLine.get("CMD_TYPE"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setLoginfo(dataLine.get("LOGINFO"));
					bean.setRemind(dataLine.get("REMIND"));
					bean.setTime(dataLine.get("TIME"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setLpath(dataLine.get("LPATH"));
					bean.setInstalled(dataLine.get("INSTALLED"));
					bean.setMd5(dataLine.get("MD5"));
					bean.setRemote(dataLine.get("REMOTE"));
					data.add(bean);
				}
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static LOCALCOMMANDBean getCommand(String cmdID){
		Connection conn = null;
		LOCALCOMMANDBean  bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getCmd(cmdID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					 bean=new LOCALCOMMANDBean();
					bean.setVersionID(dataLine.get("VER_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setCmdID(dataLine.get("CMD_ID"));
					bean.setCmdName(dataLine.get("CMD_NAME"));
					bean.setCmdText(dataLine.get("CMD_TEXT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setCmdType(dataLine.get("CMD_TYPE"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setLoginfo(dataLine.get("LOGINFO"));
					bean.setRemind(dataLine.get("REMIND"));
					bean.setTime(dataLine.get("TIME"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setLpath(dataLine.get("LPATH"));
					bean.setInstalled(dataLine.get("INSTALLED"));
					bean.setMd5(dataLine.get("MD5"));
					bean.setRemote(dataLine.get("REMOTE"));
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	
	public static int setStatus(String id,String status){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getStatusSet(id, status);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setRemind(String id,String remind){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getRemindSet(id, remind);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setInstalled(String id,String md5){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getInstallSet(id, md5);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setRemote(String id,String status,String loginfo,String remind){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getCmdRemote(id, status, loginfo, remind);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int getCmdCount(String versionID,String nodeID,String status){
		Connection conn = null;
	    int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALCOMMANDSql.getCmdCount(versionID, nodeID, status);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map<String,String> dataLine=(Map)result.get(0);
				 String ret=(String)dataLine.get("COUNT");
				 count=Integer.parseInt(ret);
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	  public static String getInstallProgress(String versionID,String nodeID){
		   Connection conn = null;
			String progress="";
			try{
				conn=DBConnectionManager.getInstance().getConnection();
			    String sql=LOCALCOMMANDSql.getStatistic(versionID, nodeID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
						Map dataLine=(Map)result.get(0);
						String total=dataLine.get("TOTAL")+"";
						String done=dataLine.get("DONE")+"";
						progress=total+"|"+done;
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return progress;
	  }
	
}
