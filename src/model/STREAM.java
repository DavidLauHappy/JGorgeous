package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import resource.ComOrder;
import sql.STREAMSql;
import utils.SqlServerUtil;
import bean.StreamBean;
import bean.ViewBean;
import bean.ViewFileBean;

import common.db.DBConnectionManager;

public class STREAM {

	public static Map<String,StreamBean> getAllStreams(){
		 Connection conn = null;
		 Map<String,StreamBean> result=new HashMap<String, StreamBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STREAMSql.getStreams();
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				for(int w=0;w<sqlResult.size();w++){
					Map<String,String> dataLine=(Map)sqlResult.get(w);
					StreamBean bean=new StreamBean();
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setStreamName(dataLine.get("STREAM_NAME"));
					bean.setStreamDesc(dataLine.get("STREAM_DESC"));
					bean.setUpdUser(dataLine.get("UPT_USER"));
					bean.setUpdTime(dataLine.get("UPT_TIME"));
					bean.setState(dataLine.get("STATUS"));
					bean.setStartTime(dataLine.get("DATE_START"));
					bean.setEndTime(dataLine.get("DATE_END"));
					bean.setCode(dataLine.get("STREAM_CODE"));
					result.put(dataLine.get("STREAM_ID"), bean);
			  }
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}	
		return result;
	}
	
	public static List<StreamBean> getStreams(){
		 Connection conn = null;
		 List<StreamBean> result=new ArrayList<StreamBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STREAMSql.getStreams();
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				for(int w=0;w<sqlResult.size();w++){
					Map<String,String> dataLine=(Map)sqlResult.get(w);
					StreamBean bean=new StreamBean();
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setStreamName(dataLine.get("STREAM_NAME"));
					bean.setStreamDesc(dataLine.get("STREAM_DESC"));
					bean.setUpdUser(dataLine.get("UPT_USER"));
					bean.setUpdTime(dataLine.get("UPT_TIME"));
					bean.setState(dataLine.get("STATUS"));
					bean.setStartTime(dataLine.get("DATE_START"));
					bean.setEndTime(dataLine.get("DATE_END"));
					bean.setCode(dataLine.get("STREAM_CODE"));
					result.add(bean);
			  }
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}	
		return result;
	}
	 public static List<StreamBean> getStream(String status){
		 Connection conn = null;
	     List<StreamBean> result=new ArrayList<StreamBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STREAMSql.getStreamByStatus(status);
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				for(int w=0;w<sqlResult.size();w++){
					Map<String,String> dataLine=(Map)sqlResult.get(w);
					StreamBean bean=new StreamBean();
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setStreamName(dataLine.get("STREAM_NAME"));
					bean.setStreamDesc(dataLine.get("STREAM_DESC"));
					bean.setUpdUser(dataLine.get("UPT_USER"));
					bean.setUpdTime(dataLine.get("UPT_TIME"));
					bean.setState(dataLine.get("STATUS"));
					bean.setStartTime(dataLine.get("DATE_START"));
					bean.setEndTime(dataLine.get("DATE_END"));
					bean.setCode(dataLine.get("STREAM_CODE"));
					result.add(bean);
				}
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}	
		return result;
	 }
	 
	 public static StreamBean getStreamByID(String id){
		 Connection conn = null;
	     StreamBean bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STREAMSql.getStream(id);
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
					Map<String,String> dataLine=(Map)sqlResult.get(0);
					bean=new StreamBean();
					bean.setStreamID(dataLine.get("STREAM_ID"));
					bean.setStreamName(dataLine.get("STREAM_NAME"));
					bean.setStreamDesc(dataLine.get("STREAM_DESC"));
					bean.setUpdUser(dataLine.get("UPT_USER"));
					bean.setUpdTime(dataLine.get("UPT_TIME"));
					bean.setState(dataLine.get("STATUS"));
					bean.setStartTime(dataLine.get("DATE_START"));
					bean.setEndTime(dataLine.get("DATE_END"));
					bean.setCode(dataLine.get("STREAM_CODE"));
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}	
		return bean;
	 }
	 
	 public static boolean nameExist(String name){
		 Connection conn = null;
		 int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STREAMSql.getNameExist(name);
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
					Map dataLine=(Map)sqlResult.get(0);
					String result=dataLine.get("COUNT")+"";
					count=Integer.parseInt(result);
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
	 
	 public static String getStreamID(){
		 Connection conn = null;
		 String result="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STREAMSql.getNewID();
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
					Map dataLine=(Map)sqlResult.get(0);
					 result=dataLine.get("ID")+"";
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}	
		return result;
	 }
	 
	 public static int add(StreamBean bean){
		 Connection conn = null;
		 int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STREAMSql.getAdd(bean.getStreamID(), bean.getStreamName(), bean.getStreamDesc(), bean.getStatus(), bean.getUpdUser());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}	
		return count;
	 }
	 
	 public static int setStatus(String id,String status,String mdfUserID){
		 Connection conn = null;
		 int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=STREAMSql.getStatusSet(id, status, mdfUserID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}	
		return count;
	 }
	 
	 //获取流下面的文件
	 //流中有多个视图
	 //每个视图的最新版本下都有若干个文件
	 public static List<ViewFileBean> getFiles(String streamID){
		 Connection conn = null;
		  List<ViewFileBean> data=new ArrayList<ViewFileBean>();
		  try{
				conn=DBConnectionManager.getInstance().getConnection();
				List<ViewBean> views=STREAM.getViews(streamID);
				if(views!=null&&views.size()>0){
					ComOrder order=new ComOrder();
					for(ViewBean view:views){
						 String versionID=view.getVersion();
						 List<ViewFileBean> files=VIEW.getVersionFiles(view.getViewID(), versionID, order);
						 data.addAll(files);
					}
				}
		  }
		  catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}	
		  return data;
	 }
	 
	 public static List<ViewBean>  getViews(String streamID){
		  List<ViewBean> data=new ArrayList<ViewBean>();
		  Connection conn = null;
		   try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=STREAMSql.getViews(streamID);
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
						bean.setProgress(dataLine.get("PROGRESS"));
						bean.setCurrentUserID(dataLine.get("CUR_USERID"));
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
