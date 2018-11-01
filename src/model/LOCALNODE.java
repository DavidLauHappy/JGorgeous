package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sql.LOCALNODESql;
import utils.SqlServerUtil;

import bean.LOCALNODEBean;

import common.db.DBConnectionManager;

public class LOCALNODE {

	public static int add(LOCALNODEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getAdd(bean.getUserID(), bean.getApp(), bean.getId(), bean.getName(),bean.getIp(), bean.getOs());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setDir(LOCALNODEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getDirSet(bean.getId(), bean.getDir1(), bean.getDir2(), bean.getDir3(), bean.getDir4(), bean.getDir5(), bean.getAutoBackup(), bean.getStart(), bean.getStop(), bean.getAutoStart());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setDb(LOCALNODEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getDbSet(bean.getId(), bean.getDbType(), bean.getDbUser(), bean.getDbPasswd(), bean.getDbName(), bean.getBackdbname(), bean.getDbPort(), bean.getDbAutoBackup());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setSftp(LOCALNODEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getSftpSet(bean.getId(), bean.getSftpUser(), bean.getSftpPasswd(), bean.getSftpPort(), bean.getSftpDir());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static  List<LOCALNODEBean> getMyAppNodes(String app,String userID){
		Connection conn = null;
		List<LOCALNODEBean> data=new ArrayList<LOCALNODEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getNodes(userID, app);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					LOCALNODEBean bean=new LOCALNODEBean();
					bean.setApp(dataLine.get("APP"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setOs(dataLine.get("OS"));
					bean.setDir1(dataLine.get("DIR1") );
					bean.setDir2(dataLine.get("DIR2"));
					bean.setDir3(dataLine.get("DIR3"));
					bean.setDir4(dataLine.get("DIR4"));
					bean.setDir5(dataLine.get("DIR5"));
					bean.setAutoBackup(dataLine.get("AUTOBACKUP"));
					bean.setStart(dataLine.get("START"));
					bean.setStop(dataLine.get("STOP"));
					bean.setAutoStart(dataLine.get("AUTOSTART"));
					bean.setDbType(dataLine.get("DBTYPE"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbName(dataLine.get("DBNAME"));
					bean.setDbPort(dataLine.get("DBPORT"));
					bean.setDbAutoBackup(dataLine.get("DBAUTOBACKUP"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setStatus(dataLine.get("STATUS"));	
					bean.setBackdbname(dataLine.get("BACKDBNAME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static  List<LOCALNODEBean> getVersionNode(String versionID){
		Connection conn = null;
		List<LOCALNODEBean> data=new ArrayList<LOCALNODEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getVersionNodes(versionID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					LOCALNODEBean bean=new LOCALNODEBean();
					bean.setApp(dataLine.get("APP"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setOs(dataLine.get("OS"));
					bean.setDir1(dataLine.get("DIR1") );
					bean.setDir2(dataLine.get("DIR2"));
					bean.setDir3(dataLine.get("DIR3"));
					bean.setDir4(dataLine.get("DIR4"));
					bean.setDir5(dataLine.get("DIR5"));
					bean.setAutoBackup(dataLine.get("AUTOBACKUP"));
					bean.setStart(dataLine.get("START"));
					bean.setStop(dataLine.get("STOP"));
					bean.setAutoStart(dataLine.get("AUTOSTART"));
					bean.setDbType(dataLine.get("DBTYPE"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbName(dataLine.get("DBNAME"));
					bean.setDbPort(dataLine.get("DBPORT"));
					bean.setDbAutoBackup(dataLine.get("DBAUTOBACKUP"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setStatus(dataLine.get("STATUS"));			
					bean.setBackdbname(dataLine.get("BACKDBNAME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	
	public static  List<LOCALNODEBean> getMyDbNodes(String userID){
		Connection conn = null;
		List<LOCALNODEBean> data=new ArrayList<LOCALNODEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getDbNodes(userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					LOCALNODEBean bean=new LOCALNODEBean();
					bean.setApp(dataLine.get("APP"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setOs(dataLine.get("OS"));
					bean.setDir1(dataLine.get("DIR1") );
					bean.setDir2(dataLine.get("DIR2"));
					bean.setDir3(dataLine.get("DIR3"));
					bean.setDir4(dataLine.get("DIR4"));
					bean.setDir5(dataLine.get("DIR5"));
					bean.setAutoBackup(dataLine.get("AUTOBACKUP"));
					bean.setStart(dataLine.get("START"));
					bean.setStop(dataLine.get("STOP"));
					bean.setAutoStart(dataLine.get("AUTOSTART"));
					bean.setDbType(dataLine.get("DBTYPE"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbName(dataLine.get("DBNAME"));
					bean.setDbPort(dataLine.get("DBPORT"));
					bean.setDbAutoBackup(dataLine.get("DBAUTOBACKUP"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setStatus(dataLine.get("STATUS"));	
					bean.setBackdbname(dataLine.get("BACKDBNAME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	
	public static  List<LOCALNODEBean> getNodes(String versionID,String ip){
		Connection conn = null;
		List<LOCALNODEBean> data=new ArrayList<LOCALNODEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getNodesByIp(versionID,ip);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					LOCALNODEBean bean=new LOCALNODEBean();
					bean.setApp(dataLine.get("APP"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setOs(dataLine.get("OS"));
					bean.setDir1(dataLine.get("DIR1") );
					bean.setDir2(dataLine.get("DIR2"));
					bean.setDir3(dataLine.get("DIR3"));
					bean.setDir4(dataLine.get("DIR4"));
					bean.setDir5(dataLine.get("DIR5"));
					bean.setAutoBackup(dataLine.get("AUTOBACKUP"));
					bean.setStart(dataLine.get("START"));
					bean.setStop(dataLine.get("STOP"));
					bean.setAutoStart(dataLine.get("AUTOSTART"));
					bean.setDbType(dataLine.get("DBTYPE"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbName(dataLine.get("DBNAME"));
					bean.setDbPort(dataLine.get("DBPORT"));
					bean.setDbAutoBackup(dataLine.get("DBAUTOBACKUP"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setStatus(dataLine.get("STATUS"));	
					bean.setBackdbname(dataLine.get("BACKDBNAME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static LOCALNODEBean getNode(String id){
		Connection conn = null;
		LOCALNODEBean bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getNode(id);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map<String,String> dataLine=(Map)result.get(0);
				 bean=new LOCALNODEBean();
				bean.setApp(dataLine.get("APP"));
				bean.setUserID(dataLine.get("USER_ID"));
				bean.setId(dataLine.get("ID"));
				bean.setName(dataLine.get("NAME"));
				bean.setIp(dataLine.get("IP"));
				bean.setOs(dataLine.get("OS"));
				bean.setDir1(dataLine.get("DIR1") );
				bean.setDir2(dataLine.get("DIR2"));
				bean.setDir3(dataLine.get("DIR3"));
				bean.setDir4(dataLine.get("DIR4"));
				bean.setDir5(dataLine.get("DIR5"));
				bean.setAutoBackup(dataLine.get("AUTOBACKUP"));
				bean.setStart(dataLine.get("START"));
				bean.setStop(dataLine.get("STOP"));
				bean.setAutoStart(dataLine.get("AUTOSTART"));
				bean.setDbType(dataLine.get("DBTYPE"));
				bean.setDbUser(dataLine.get("DBUSER"));
				bean.setDbPasswd(dataLine.get("DBPASSWD"));
				bean.setDbName(dataLine.get("DBNAME"));
				bean.setDbPort(dataLine.get("DBPORT"));
				bean.setDbAutoBackup(dataLine.get("DBAUTOBACKUP"));
				bean.setSftpUser(dataLine.get("SFTPUSER"));
				bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
				bean.setSftpPort(dataLine.get("SFTPPORT"));
				bean.setSftpDir(dataLine.get("SFTPDIR"));
				bean.setStatus(dataLine.get("STATUS"));		
				bean.setBackdbname(dataLine.get("BACKDBNAME"));
			}
			
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	
	public static boolean nodeExists(String userID,String ipAddress){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getNodeExists(userID, ipAddress);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return true;
				}
			}
		}catch(Exception e){
			return false;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
	
	public static boolean delete(LOCALNODEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getNodeDelete(bean.getId());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			count=-1;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		if(count>=0){
			return true;
		}
		return false;
	}
	
	public static int setStatus(String id,String status){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=LOCALNODESql.getStatusSet(id,status);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			count=-1;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	
}
