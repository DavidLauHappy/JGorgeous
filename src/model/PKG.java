package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.PKGBean;

import sql.PKGSql;
import utils.SqlServerUtil;
import common.db.DBConnectionManager;

public class PKG {

	public static int inrollData(PKGBean data){
		Connection conn = null;
		int  num=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String exitsSql=PKGSql.exist(data.getId());
			List result=SqlServerUtil.executeQuery(exitsSql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					String updater=PKGSql.updateInroll(data.getStatus(), data.getMdfTime(), data.getCfgfile(), data.getId(),data.getMdfUser());
					num=SqlServerUtil.executeUpdate(updater, conn);
				}else{
					num=addData(data);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return num;
	}
	
	public static int addData(PKGBean data){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSql.addData(data.getId(),data.getAppname(),data.getRelapps(),data.getDesc(),data.getStatus(),data.getCfgfile(),data.getEnalbe(),data.getMdfUser(),data.getMdfTime());
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int archivePkg(String pkgID){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String[] tables=new String[]{"HIS_PKG","HIS_STEP","HIS_PFILE","HIS_NODE_FILE","HIS_NODE_STEP","HIS_COMMAND"};
			for(String tableName:tables){
				String sql=PKGSql.getDeleteArchive(pkgID, tableName);
				cout=SqlServerUtil.executeUpdate(sql, conn);
			}
			for(String tableName:tables){
				String srcTableName=tableName.replace("HIS_", "");
				String sql=PKGSql.getArchive(pkgID, tableName, srcTableName);
				cout=SqlServerUtil.executeUpdate(sql, conn);
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int disablePkg(String pkgID,String enable,String time){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String[] tables=new String[]{"PKG","STEP","PFILE","NODE_FILE","NODE_STEP","COMMAND"};
			for(String tableName:tables){
				String sql=PKGSql.getDelete(pkgID, tableName);
				cout=SqlServerUtil.executeUpdate(sql, conn);
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int setStatus(String pkgID,String status,String userID){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSql.setStatus(pkgID,status,userID);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	
	
	public static int deletePkg(String pkgID){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSql.deletePkg(pkgID);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int deletePkgViews(String pkgID){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSql.deletePkgViews(pkgID);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int addPkgView(String pkgID,String viewID,String crtUser){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSql.getAddPkgView(pkgID, viewID, crtUser);
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static List<PKGBean> getMyPkgs(String userID,String enable){
		Connection conn = null;
		List<PKGBean> pkgs=new ArrayList<PKGBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSql.getMyPkgs(userID,enable);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map dataLine=(Map)result.get(w);
					PKGBean bean=new PKGBean();
					bean.setId((String)dataLine.get("ID"));
					bean.setAppname((String)dataLine.get("APP"));
					bean.setRelapps((String)dataLine.get("RELAPPS"));
					bean.setDesc((String)dataLine.get("DESC"));
					bean.setStatus((String)dataLine.get("STATUS"));
					bean.setCfgfile((String)dataLine.get("CFGFILE"));
					bean.setEnalbe((String)dataLine.get("ENABLE"));
					bean.setMdfUser((String)dataLine.get("MDF_USER"));
					bean.setMdfTime((String)dataLine.get("MDF_TIME"));
					pkgs.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return pkgs;
	}
	

	public static PKGBean getPkg(String pkgID){
		Connection conn = null;
		PKGBean bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=PKGSql.getPkgByID(pkgID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				bean=new PKGBean();
				bean.setId((String)dataLine.get("ID"));
				bean.setAppname((String)dataLine.get("APP"));
				bean.setRelapps((String)dataLine.get("RELAPPS"));
				bean.setDesc((String)dataLine.get("DESC"));
				bean.setStatus((String)dataLine.get("STATUS"));
				bean.setCfgfile((String)dataLine.get("CFGFILE"));
				bean.setEnalbe((String)dataLine.get("ENABLE"));
				bean.setMdfUser((String)dataLine.get("MDF_USER"));
				bean.setMdfTime((String)dataLine.get("MDF_TIME"));
			}
		}catch(Exception e){
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	
	
}
