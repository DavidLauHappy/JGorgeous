package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.ViewBean;
import bean.ViewFileBean;
import bean.ViewVersionBean;

import resource.ComOrder;
import resource.Context;
import sql.VIEWSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class VIEW {
	
	public static String getNewNo(){
		Connection conn = null;
		String no="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getNewID();
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				no=dataLine.get("ID")+"";
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return no;
	}
	
	
	public static boolean isNameExist(String name){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getNameExist(name);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String no=dataLine.get("COUNT")+"";
				count=Integer.parseInt(no);
				if(count>0)
					return true;
				return false;
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
	
	public static int add(ViewBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getAdd(bean.getViewID(), bean.getViewName(), bean.getVeiwDesc(), bean.getStatus(), bean.getCrtUser(), bean.getUptUser(), bean.getStreamID(), bean.getApp(), bean.getRdate(), bean.getDeadDate(), bean.getOwner(),bean.getVerDesc(),bean.getUptFlag());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static String getVersion(String viewID){
		Connection conn = null;
		String versionID="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getViewVersion(viewID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				versionID=dataLine.get("ID")+"";
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return versionID;
	}
	
	public static String getNextVersion(String viewID){
		Connection conn = null;
		String versionID="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getViewNextVersion(viewID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				versionID=dataLine.get("ID")+"";
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return versionID;
	}
	
	
	public static int addVersion(String viewID,String viewDesc,String versionID,String userID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getAddVersion(viewID, viewDesc, versionID, userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int removeReq(String viewID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getReqRemove(viewID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int addReq(String viewID,String sno,String userID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getAddReq(viewID, sno, userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setReqDateByView(String viewID,String date){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getReqDateSet(viewID, date);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int submit(String viewID,String progress,String userID,String mdfUser,String lastProgress ){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getNextProgress(viewID, progress, userID, mdfUser,lastProgress);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int chgStream(String viewID,String streamID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getSetStream(viewID,streamID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setFlag(String viewID,String flag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getSetFlag(viewID,flag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int setTowner(String viewID,String userID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getSetTowner(viewID,userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
   public static List<ViewBean> getViews(){
	   Connection conn = null;
	   List<ViewBean> data=new ArrayList<ViewBean>();
	   try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getAllViews();
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				for(int w=0;w<sqlResult.size();w++){
					Map<String,String> dataLine=(Map)sqlResult.get(w);
					ViewBean bean=new ViewBean();
					bean.setViewID(dataLine.get("VIEW_ID"));
					bean.setViewName(dataLine.get("VIEW_NAME"));
					bean.setVeiwDesc(dataLine.get("VIEW_DESC"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setUptUser(dataLine.get("UPT_USER"));
					bean.setUptTime(dataLine.get("UPT_TIME"));
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setApp(dataLine.get("APP"));
					bean.setRdate(dataLine.get("RDATE"));
					bean.setDeadDate(dataLine.get("DEAD_DATE"));
					bean.setOwner(dataLine.get("OWNER"));
					bean.setTowner(dataLine.get("TOWNER"));
					bean.setProgress(dataLine.get("PROGRESS"));
					bean.setCurrentUserID(dataLine.get("CUR_USERID"));
					bean.setLastProgress(dataLine.get("LST_PROGRESS"));
					bean.setUptFlag(dataLine.get("UPT_FLAG"));
					bean.setVerDesc(dataLine.get("VER_DESC"));
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

   public static List<ViewBean> getAllViews(){
	   Connection conn = null;
	   List<ViewBean> data=new ArrayList<ViewBean>();
	   try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getViews();
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				for(int w=0;w<sqlResult.size();w++){
					Map<String,String> dataLine=(Map)sqlResult.get(w);
					ViewBean bean=new ViewBean();
					bean.setViewID(dataLine.get("VIEW_ID"));
					bean.setViewName(dataLine.get("VIEW_NAME"));
					bean.setVeiwDesc(dataLine.get("VIEW_DESC"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setUptUser(dataLine.get("UPT_USER"));
					bean.setUptTime(dataLine.get("UPT_TIME"));
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setApp(dataLine.get("APP"));
					bean.setRdate(dataLine.get("RDATE"));
					bean.setDeadDate(dataLine.get("DEAD_DATE"));
					bean.setOwner(dataLine.get("OWNER"));
					bean.setTowner(dataLine.get("TOWNER"));
					bean.setProgress(dataLine.get("PROGRESS"));
					bean.setCurrentUserID(dataLine.get("CUR_USERID"));
					bean.setLastProgress(dataLine.get("LST_PROGRESS"));
					bean.setUptFlag(dataLine.get("UPT_FLAG"));
					bean.setVerDesc(dataLine.get("VER_DESC"));
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
   
   public static ViewBean getViewById(String viewID){
	   Connection conn = null;
	   ViewBean bean=null;
	   try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getViewByID(viewID);
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				Map<String,String> dataLine=(Map)sqlResult.get(0);
				bean=new ViewBean();
				bean.setViewID(dataLine.get("VIEW_ID"));
				bean.setViewName(dataLine.get("VIEW_NAME"));
				bean.setVeiwDesc(dataLine.get("VIEW_DESC"));
				bean.setStatus(dataLine.get("STATUS"));
				bean.setCrtUser(dataLine.get("CRT_USER"));
				bean.setCrtTime(dataLine.get("CRT_TIME"));
				bean.setUptUser(dataLine.get("UPT_USER"));
				bean.setUptTime(dataLine.get("UPT_TIME"));
				bean.setStreamID(dataLine.get("STREAM_ID"));
				bean.setApp(dataLine.get("APP"));
				bean.setRdate(dataLine.get("RDATE"));
				bean.setDeadDate(dataLine.get("DEAD_DATE"));
				bean.setOwner(dataLine.get("OWNER"));
				bean.setTowner(dataLine.get("TOWNER"));
				bean.setProgress(dataLine.get("PROGRESS"));
				bean.setCurrentUserID(dataLine.get("CUR_USERID"));
				bean.setLastProgress(dataLine.get("LST_PROGRESS"));
				bean.setUptFlag(dataLine.get("UPT_FLAG"));
				bean.setVerDesc(dataLine.get("VER_DESC"));
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
			String sql=VIEWSql.getStatusSet(id, status, Context.session.userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
   }
   
   public static int setFlagByReq(String reqNo,String status,String desc){
	   Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getFlagSet(reqNo, status,desc);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
   }

   
   public static List<ViewFileBean> getVersionFiles(String viewID,String versionID){
	   Connection conn = null;
	   List<ViewFileBean> data=new ArrayList<ViewFileBean>();
	   try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getVersionFiles(viewID, versionID);
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				for(int w=0;w<sqlResult.size();w++){
					Map<String,String> dataLine=(Map)sqlResult.get(w);
					ViewFileBean bean=new ViewFileBean();
					bean.setFileName(dataLine.get("FILE_NAME"));
					bean.setRemotePath(dataLine.get("LOCATION"));
					bean.setMd5(dataLine.get("MD5"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setViewID(dataLine.get("VIEW_ID"));
					bean.setVersionID(versionID);
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setFileTime(dataLine.get("FILE_TIME"));
					bean.setViewName(dataLine.get("VIEW_NAME"));
					bean.setStreamName(dataLine.get("STREAM_NAME"));
					bean.setOrignalOrder(w);
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
   
   public static List<ViewFileBean> getVersionAttachs(String viewID,String versionID){
	   Connection conn = null;
	   List<ViewFileBean> data=new ArrayList<ViewFileBean>();
	   try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getVersionAttachs(viewID, versionID);
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				for(int w=0;w<sqlResult.size();w++){
					Map<String,String> dataLine=(Map)sqlResult.get(w);
					ViewFileBean bean=new ViewFileBean();
					bean.setFileName(dataLine.get("FILE_NAME"));
					bean.setRemotePath(dataLine.get("LOCATION"));
					bean.setMd5(dataLine.get("MD5"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setViewID(dataLine.get("VIEW_ID"));
					bean.setVersionID(versionID);
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setFileTime(dataLine.get("FILE_TIME"));
					bean.setViewName(dataLine.get("VIEW_NAME"));
					bean.setStreamName(dataLine.get("STREAM_NAME"));
					bean.setOrignalOrder(w);
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
   
   public static List<ViewFileBean> getVersionFiles(String viewID,String versionID,ComOrder order){
	   Connection conn = null;
	   List<ViewFileBean> data=new ArrayList<ViewFileBean>();
	   try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getVersionFiles(viewID, versionID);
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				for(int w=0;w<sqlResult.size();w++){
					Map<String,String> dataLine=(Map)sqlResult.get(w);
					int seqNo=order.getOrderInt();
					ViewFileBean bean=new ViewFileBean();
					bean.setFileName(dataLine.get("FILE_NAME"));
					bean.setRemotePath(dataLine.get("LOCATION"));
					bean.setMd5(dataLine.get("MD5"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setViewID(dataLine.get("VIEW_ID"));
					bean.setVersionID(versionID);
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setFileTime(dataLine.get("FILE_TIME"));
					bean.setOrignalOrder(seqNo);
					bean.setViewName(dataLine.get("VIEW_NAME"));
					bean.setStreamName(dataLine.get("STREAM_NAME"));
					data.add(bean);
					order.inc();
				}
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
	   return data;
   }
   
   public static List<ViewVersionBean> getVersions(String viewID){
	   Connection conn = null;
	   List<ViewVersionBean> data=new ArrayList<ViewVersionBean>();
	   try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=VIEWSql.getVersions(viewID);
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				for(int w=0;w<sqlResult.size();w++){
					Map<String,String> dataLine=(Map)sqlResult.get(w);
					ViewVersionBean bean=new ViewVersionBean();
					bean.setViewID(dataLine.get("VIEW_ID"));
					bean.setViewName(dataLine.get("VIEW_NAME"));
					bean.setViewDesc(dataLine.get("VIEW_DESC"));
					bean.setVersion(dataLine.get("VERSION"));
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setUptUser(dataLine.get("UPT_USER"));
					bean.setUptTime(dataLine.get("UPT_TIME"));
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
}
