package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.PFILEBean;
import sql.PFILESql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class PFILE {
	public static int addData(PFILEBean data){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PFILESql.addData(data.getId(),data.getName(),data.getPath(),data.getBootfalg(),data.getSeq(),data.getPkgID(),data.getStepID(),data.getMd5(),data.getType(),data.getDbOwner(),data.getUser(),data.getDbType(),data.getObjName(),data.getDir(),data.getIsDir(),data.getMdfUser(),data.getMdfTime());
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int deleteByPkg(String pkgID){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PFILESql.deleteByPkg(pkgID);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static PFILEBean geFile(String pkgID,String stepID,String id){
		Connection conn = null;
		PFILEBean bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PFILESql.getFileByID(pkgID, stepID, id);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					 bean=new PFILEBean();
					 bean.setId(dataLine.get("ID"));
					 bean.setName(dataLine.get("NAME"));
				     bean.setPath(dataLine.get("PATH"));
				     bean.setBootfalg(dataLine.get("BOOTFLAG"));
				     bean.setSeq(dataLine.get("SEQ"));
				     bean.setPkgID(dataLine.get("PKG_ID"));
				     bean.setStepID(dataLine.get("STEP_ID"));
				     bean.setMd5(dataLine.get("MD5"));
				     bean.setType(dataLine.get("TYPE"));
				     bean.setDbOwner(dataLine.get("OWNER"));
				     bean.setUser(dataLine.get("USER"));
				     bean.setDbType(dataLine.get("DBTYPE"));
				     bean.setObjName(dataLine.get("OBJNAME"));
				     bean.setDir(dataLine.get("DIR"));
				     bean.setIsDir(dataLine.get("ISDIR"));
				     bean.setMdfUser(dataLine.get("MDF_USER"));
				     bean.setMdfTime(dataLine.get("MDF_TIME"));
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	public static List<PFILEBean> gePkgFiles(String pkgID,String owner){
		Connection conn = null;
		List<PFILEBean> data=new ArrayList<PFILEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PFILESql.getFilesByPkg(pkgID, owner);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					PFILEBean bean=new PFILEBean();
					 bean.setId(dataLine.get("ID"));
					 bean.setName(dataLine.get("NAME"));
				     bean.setPath(dataLine.get("PATH"));
				     bean.setBootfalg(dataLine.get("BOOTFLAG"));
				     bean.setSeq(dataLine.get("SEQ"));
				     bean.setPkgID(dataLine.get("PKG_ID"));
				     bean.setStepID(dataLine.get("STEP_ID"));
				     bean.setMd5(dataLine.get("MD5"));
				     bean.setType(dataLine.get("TYPE"));
				     bean.setDbOwner(dataLine.get("OWNER"));
				     bean.setUser(dataLine.get("USER"));
				     bean.setDbType(dataLine.get("DBTYPE"));
				     bean.setObjName(dataLine.get("OBJNAME"));
				     bean.setDir(dataLine.get("DIR"));
				     bean.setIsDir(dataLine.get("ISDIR"));
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
	
	public static List<PFILEBean> getStepFiles(String pkgID,String stepID){
		Connection conn = null;
		List<PFILEBean> data=new ArrayList<PFILEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PFILESql.getStepFiles(pkgID, stepID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					PFILEBean bean=new PFILEBean();
					 bean.setId(dataLine.get("ID"));
					 bean.setName(dataLine.get("NAME"));
				     bean.setPath(dataLine.get("PATH"));
				     bean.setBootfalg(dataLine.get("BOOTFLAG"));
				     bean.setSeq(dataLine.get("SEQ"));
				     bean.setPkgID(dataLine.get("PKG_ID"));
				     bean.setStepID(dataLine.get("STEP_ID"));
				     bean.setMd5(dataLine.get("MD5"));
				     bean.setType(dataLine.get("TYPE"));
				     bean.setDbOwner(dataLine.get("OWNER"));
				     bean.setUser(dataLine.get("USER"));
				     bean.setDbType(dataLine.get("DBTYPE"));
				     bean.setObjName(dataLine.get("OBJNAME"));
				     bean.setDir(dataLine.get("DIR"));
				     bean.setIsDir(dataLine.get("ISDIR"));
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
