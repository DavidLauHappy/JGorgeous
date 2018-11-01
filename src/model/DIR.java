package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.DIRBean;

import sql.DIRSql;
import utils.DateUtil;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class DIR {
	
	public  static List<DIRBean>  getMyActionDirs(String userID,String app){
		Connection conn = null;
		List<DIRBean> data=new ArrayList<DIRBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DIRSql.getMyActionDirs(userID,app);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					DIRBean bean=new DIRBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setType(dataLine.get("TYPE"));
					bean.setParentID(dataLine.get("PARENTID"));
					bean.setApp(dataLine.get("APP"));
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

	public  static List<DIRBean>  getMyChildrenDirs(String userID,String parentID,String app){
		Connection conn = null;
		List<DIRBean> data=new ArrayList<DIRBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DIRSql.getMyChildrenDirs(userID,parentID,app);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					DIRBean bean=new DIRBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setType(dataLine.get("TYPE"));
					bean.setParentID(dataLine.get("PARENTID"));
					bean.setApp(dataLine.get("APP"));
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

	public static String getID(String userID){
		Connection conn = null;
		String id="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DIRSql.getID(userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map dataLine=(Map)result.get(0);
					id=dataLine.get("ID")+"";
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return id;
	}

	public static int addData(DIRBean data){
		Connection conn = null;
		int  cout=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DIRSql.addData(data.getId(),data.getName(),data.getType(),data.getParentID(),data.getApp(),data.getMdfUser(),data.getMdfTime());
			cout=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return cout;
	}
	
	public static int delete(String id,String userID){
		Connection conn = null;
		int  count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DIRSql.delete(id,userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static boolean updateName(String app,String id,String parentID,String userID,String name){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DIRSql.nameExist(app,parentID,name,userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return false;
				}else{
					String updater=DIRSql.updateName(id,userID,name,DateUtil.getCurrentTime());
					int num=SqlServerUtil.executeUpdate(updater, conn);
					if(num<0)
						return false;
				}
			}
		}catch(Exception e){
			return false;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return true;
	}
	
	public static int updateType(String id,String userID,String type){
		Connection conn = null;
		int  count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DIRSql.updateType(id,userID,type,DateUtil.getCurrentTime());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static String getDirFullPath(String userID,String app,String parentID,String name){
		Connection conn = null;
		String fullpath="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=DIRSql.getDirFullPath(userID, parentID, app);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map<String,String> dataLine=(Map)result.get(0);
				String dirName =dataLine.get("NAME");
				String dirPrentID=dataLine.get("PARENTID");
				fullpath= dirName+"/"+name;
				return getDirFullPath(userID,app,dirPrentID,fullpath);
			}else{
				 return "/"+name;
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return "";
	}
}
