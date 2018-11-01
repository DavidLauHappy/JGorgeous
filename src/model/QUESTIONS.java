package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sql.QUESTIONFILESql;
import sql.QUESTIONSql;
import utils.DateUtil;
import utils.SqlServerUtil;
import utils.StringUtil;
import bean.QUESTIONFILEBean;
import bean.QUESTIONSBean;
import common.db.DBConnectionManager;


public class QUESTIONS {
	public static int add(QUESTIONSBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getAdd(bean.getId(),bean.getQdesc(), bean.getQtype(), bean.getQstatus(), bean.getApp(), bean.getReqid(), bean.getCrtUser(), bean.getCurUser(),bean.getDeveloper());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int update(QUESTIONSBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getUpdate(bean.getId(),bean.getQdesc(), bean.getQtype(), bean.getQstatus(), bean.getApp(), bean.getReqid(), bean.getCurUser(),bean.getDeveloper());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int updateStatus(String id,String status){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getStatusSet(id, status);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int  developerEdit(QUESTIONSBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getDEdit(bean.getId(), bean.getMdesc(),bean.getQstatus());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int testOver(QUESTIONSBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getTEdit(bean.getId(),bean.getQstatus(),  bean.getCurUser(),bean.getQdesc());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static String getNo(){
		Connection conn = null;
		String ret="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getNewID();
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map dataLine=(Map)result.get(0);
					String id=dataLine.get("ID")+"";
					id=StringUtil.leftpad(id, 5, "0");
					ret="T"+DateUtil.getCurrentDate("yyyyMMdd")+id;
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return ret;
	}
	
	public static List<QUESTIONFILEBean> getFiles(String id){
		Connection conn = null;
		List<QUESTIONFILEBean>  data=new ArrayList<QUESTIONFILEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONFILESql.getFiles(id);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					QUESTIONFILEBean bean=new QUESTIONFILEBean();
					bean.setQid(dataLine.get("QID"));
					bean.setFileId(dataLine.get("FILE_ID"));
					bean.setFileName(dataLine.get("FILE_NAME"));
					bean.setFileTime(dataLine.get("FILE_TIME"));
					bean.setMd5(dataLine.get("MD5"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setQfileType(dataLine.get("QFILE_TYPE"));
					bean.setRemotePath(dataLine.get("LOCATION"));
					bean.setStatus(QUESTIONFILEBean.Status.Remote.ordinal()+"");
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
	
	public static int getQuestionSearchCount(String id,String userCnd){
		Connection conn = null;
		int ret=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getSearchCount(id, userCnd);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String retCount=dataLine.get("COUNT")+"";	
				ret=Integer.parseInt(retCount);
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return ret;
	}
	
	public static List<QUESTIONSBean> getQuestionSearch(String id,String userCnd,int startIndex,int endIndex){
		Connection conn = null;
		List<QUESTIONSBean>  data=new ArrayList<QUESTIONSBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getSearch(id,userCnd,startIndex+"",endIndex+"");
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					QUESTIONSBean bean=new QUESTIONSBean();
					bean.setId(dataLine.get("ID"));
				    bean.setQdesc(dataLine.get("QDESC"));
				    bean.setMdesc(dataLine.get("MDESC"));
				    bean.setQtype(dataLine.get("QTYPE"));
				    bean.setQstatus(dataLine.get("QSTATUS"));
				    bean.setApp(dataLine.get("APP"));
				    bean.setReqid(dataLine.get("REQID"));
				    bean.setReqNo(dataLine.get("REQ_ID"));
				    bean.setReqName(dataLine.get("REQ_NAME"));
				    bean.setDeveloper(dataLine.get("DEVELOPER"));
				    bean.setReqFullName(dataLine.get("REQ_ID")+" "+dataLine.get("REQ_NAME"));
				    bean.setCrtUser(dataLine.get("CRT_USER"));
				    bean.setCrtUserFull(dataLine.get("CRT_USERNAME")+"("+dataLine.get("CRT_USER")+")");
				    bean.setCurUser(dataLine.get("CUR_USER"));
				    bean.setCurUserFull(dataLine.get("CUR_USERNAME")+"("+dataLine.get("CUR_USER")+")");
				    bean.setCrtTime(dataLine.get("CRT_TIME"));
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
	
	public static List<QUESTIONSBean> getQuestionByReq(String reqID){
		Connection conn = null;
		List<QUESTIONSBean>  data=new ArrayList<QUESTIONSBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getReqQustions(reqID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					QUESTIONSBean bean=new QUESTIONSBean();
					bean.setId(dataLine.get("ID"));
				    bean.setQdesc(dataLine.get("QDESC"));
				    bean.setQstatus(dataLine.get("QSTATUS"));
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
	
	public static List<QUESTIONSBean> getaAllOtherQuestion(String reqID,String qid){
		Connection conn = null;
		List<QUESTIONSBean>  data=new ArrayList<QUESTIONSBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=QUESTIONSql.getAllReqQustions(reqID,qid);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					QUESTIONSBean bean=new QUESTIONSBean();
					bean.setId(dataLine.get("ID"));
				    bean.setQdesc(dataLine.get("QDESC"));
				    bean.setQstatus(dataLine.get("QSTATUS"));
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
