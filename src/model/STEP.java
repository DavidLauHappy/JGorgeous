package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sql.PFILESql;
import sql.STEPSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

import bean.STEPBean;

public class STEP {

	public static int addData(STEPBean data){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.addData(data.getPkgID(),data.getId(),data.getName(),data.getDesc(),data.getAction(),data.getParentid(),data.getOrder(),data.getBackupFlag(),data.getMdfUser());
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
			String sql=STEPSql.deleteByPkg(pkgID);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int deleteByID(String pkgID,String id){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.deleteByID(pkgID,id);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	
	public static int setStepOrder(String pkgID,String id,String order,String time){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.setStepOrder(pkgID,id,order);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int setBackFlag(String pkgID,String id,String backFlag){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.setBackFlag(pkgID,id,backFlag);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int setParent(String pkgID,String id,String parentID){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.setParent(pkgID,id,parentID);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static String getID(String pkgID){
		Connection conn = null;
		String id="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.getMaxID(pkgID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map dataLine=(Map)result.get(0);
					id=dataLine.get("num")+"";
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return id;
	}
	
	public static List<STEPBean> getPkgSteps(String pkgID){
		Connection conn = null;
		List<STEPBean> steps=new ArrayList<STEPBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.getPkgSteps(pkgID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map dataLine=(Map)result.get(w);
					STEPBean bean=new STEPBean();
					bean.setPkgID((String)dataLine.get("PKG_ID"));
					bean.setId((String)dataLine.get("ID"));
					bean.setName((String)dataLine.get("NAME"));
					bean.setDesc((String)dataLine.get("DESC"));
					bean.setAction((String)dataLine.get("ACTION"));
					bean.setParentid((String)dataLine.get("PARENT_ID"));
					bean.setOrder((String)dataLine.get("SEQ"));
					bean.setBackupFlag((String)dataLine.get("BACKUP_FLAG"));
					bean.setMdfUser((String)dataLine.get("MDF_USER"));
					bean.setMdfTime((String)dataLine.get("MDF_TIME"));
					steps.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return steps;
	}
	
	public static STEPBean getPkgSteps(String pkgID,String stepID){
		Connection conn = null;
		STEPBean bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.getPkgSteps(pkgID,stepID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
			    bean=new STEPBean();
				bean.setPkgID((String)dataLine.get("PKG_ID"));
				bean.setId((String)dataLine.get("ID"));
				bean.setName((String)dataLine.get("NAME"));
				bean.setDesc((String)dataLine.get("DESC"));
				bean.setAction((String)dataLine.get("ACTION"));
				bean.setParentid((String)dataLine.get("PARENT_ID"));
				bean.setOrder((String)dataLine.get("SEQ"));
				bean.setBackupFlag((String)dataLine.get("BACKUP_FLAG"));
				bean.setMdfUser((String)dataLine.get("MDF_USER"));
				bean.setMdfTime((String)dataLine.get("MDF_TIME"));
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	public static List<STEPBean> geFileSteps(String pkgID,String stepID){
		Connection conn = null;
		List<STEPBean> steps=new ArrayList<STEPBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PFILESql.getStepFiles(pkgID,stepID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map dataLine=(Map)result.get(w);
					STEPBean bean=new STEPBean();
					bean.setPkgID((String)dataLine.get("PKG_ID"));
					bean.setId((String)dataLine.get("ID"));
					bean.setName((String)dataLine.get("NAME"));
					String desc="安装["+(String)dataLine.get("NAME")+"]";
					bean.setDesc(desc);
					bean.setAction("");
					bean.setParentid((String)dataLine.get("STEP_ID"));
					bean.setOrder((String)dataLine.get("SEQ"));
					bean.setBackupFlag("");
					bean.setMdfUser((String)dataLine.get("MDF_USER"));
					bean.setMdfTime((String)dataLine.get("MDF_TIME"));
					steps.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return steps;
	}
	
	public static List<STEPBean> getStepsByAction(String pkgID,String actionType){
		Connection conn = null;
		List<STEPBean> steps=new ArrayList<STEPBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PFILESql.getStepByAction(pkgID,actionType);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map dataLine=(Map)result.get(w);
					STEPBean bean=new STEPBean();
					bean.setPkgID((String)dataLine.get("PKG_ID"));
					bean.setId((String)dataLine.get("ID"));
					bean.setName((String)dataLine.get("NAME"));
					bean.setDesc((String)dataLine.get("DESC"));
					bean.setAction((String)dataLine.get("ACTION"));
					bean.setParentid((String)dataLine.get("PARENT_ID"));
					bean.setOrder((String)dataLine.get("SEQ"));
					bean.setBackupFlag((String)dataLine.get("BACKUP_FLAG"));
					bean.setMdfUser((String)dataLine.get("MDF_USER"));
					bean.setMdfTime((String)dataLine.get("MDF_TIME"));
					steps.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return steps;
	}
	
	public static List<STEPBean> getMyServiceSteps(String pkgID,String serviceID,String userID){
		Connection conn = null;
		List<STEPBean> steps=new ArrayList<STEPBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.getStepByName(pkgID, serviceID, userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map dataLine=(Map)result.get(w);
					STEPBean bean=new STEPBean();
					bean.setPkgID((String)dataLine.get("PKG_ID"));
					bean.setId((String)dataLine.get("ID"));
					bean.setName((String)dataLine.get("NAME"));
					bean.setDesc((String)dataLine.get("DESC"));
					bean.setAction((String)dataLine.get("ACTION"));
					bean.setParentid((String)dataLine.get("PARENT_ID"));
					bean.setOrder((String)dataLine.get("SEQ"));
					bean.setBackupFlag((String)dataLine.get("BACKUP_FLAG"));
					bean.setMdfUser((String)dataLine.get("MDF_USER"));
					bean.setMdfTime((String)dataLine.get("MDF_TIME"));
					steps.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return steps;
	}
	
	//版本安装中，步骤不允许重复
	public static String eixst(String pkgID,String stepDesc){
		Connection conn = null;
		String id="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STEPSql.getStepExist(pkgID,stepDesc);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					id=dataLine.get("ID");
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return id;
	}
}
