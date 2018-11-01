package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sql.REQUIREMENTLOGSql;
import sql.REQUIREMENTVERSIONSql;
import sql.TASKSql;
import utils.SqlServerUtil;

import bean.RFile;
import bean.RequirementLog;
import bean.TaskBean;


import common.db.DBConnectionManager;

public class TASK {
	
	//发版提交
	public static int updateSubmit(TaskBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getSubmit(bean.getId(), bean.getRevDesc(), bean.getFileList(), bean.getCmpFileList(), bean.getScopeDesc(), bean.getSubmitJson(), bean.getTestDesc(), bean.getSelfTestJson(), bean.getCaseJson(), bean.getMdfUser(), bean.getCurrentVersion(), bean.getVersionDesc(),bean.getCodeApprise(),bean.getRstApprise());
			count=SqlServerUtil.executeUpdate(sql, conn);
			sql=REQUIREMENTVERSIONSql.getInser(bean.getId(), bean.getFileList(), bean.getCmpFileList(), bean.getVersionDesc(), bean.getCurrentVersion());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	 //带问题出去的发榜通知更新
	public static int updateFlag(String id,String uptFlag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getUptFlag(id, uptFlag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
		
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	//任务附件添加
	public static int addAttachs(String sno,String fileID,String md5,String location,String fileName,String fileTime,String mdfUser,boolean uplaod,String version,String type){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getAddAttachs(sno, fileID, fileName, fileTime, md5, mdfUser,version,type);
			count=SqlServerUtil.executeUpdate(sql, conn);
			if(count>0&&uplaod){
				sql=TASKSql.getAddAttachHD(md5, location,mdfUser);
				count=SqlServerUtil.executeUpdate(sql, conn);
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	//按类型取需求的附件信息
	public static List<RFile> getAttach(String sno,String version,String type){
		Connection conn = null;
		List<RFile>  data=new ArrayList<RFile>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getAttach(sno,version,type);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					RFile bean=new RFile();
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setFileName(dataLine.get("FILE_NAME"));
					bean.setMd5(dataLine.get("MD5"));
					bean.setRpath(dataLine.get("LOCATION"));
					bean.setFileTime(dataLine.get("FILE_TIME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	//获取个人的任务列表
	public static List<TaskBean> getMyTasks(String userID){
		Connection conn = null;
		List<TaskBean>  data=new ArrayList<TaskBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getMyReqs(userID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					TaskBean bean=new TaskBean();
					bean.setId(dataLine.get("ID"));
					bean.setTname(dataLine.get("NAME"));
					bean.setReqID(dataLine.get("REQ_ID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setArrangeDate(dataLine.get("DEAD_DATE"));
					bean.setOverDate(dataLine.get("DONE_DATE"));
					bean.setRdate(dataLine.get("RDATE")); //预期上线时间
					bean.setOwner(dataLine.get("OWNER"));
					bean.setOwnerName(dataLine.get("OWNERNAME"));
					bean.setScheID(dataLine.get("SCHE_ID"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setCrtUserName(dataLine.get("CRT_USERNAME"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setRstApprise(dataLine.get("RST_APPRISE"));
					bean.setCodeApprise(dataLine.get("CODE_APPRISE"));
					///////////////////////////////////////////////////
					bean.setApp(dataLine.get("APP"));
					bean.setRevDesc(dataLine.get("REVDESC"));
					bean.setScopeDesc(dataLine.get("SCOPEDESC"));
					bean.setSubmitJson(dataLine.get("SUBMITJSON"));
					bean.setTestDesc(dataLine.get("TESTDESC"));
					bean.setSelfTestJson(dataLine.get("SELFTESTJSON"));
					bean.setCaseJson(dataLine.get("CASEJSON"));
					bean.setVersionDesc(dataLine.get("VER_DESC"));
					bean.setCurrentUser(dataLine.get("CUR_USERID"));
					bean.setCurrentUserName(dataLine.get("CUR_USERNAME"));
					bean.setCurrentVersion(dataLine.get("CUR_VERSION"));
					bean.setReleaseFlag(dataLine.get("UPT_FLAG"));
					///////////////////////////////////////////
					bean.setFileList(dataLine.get("FILELIST"));
					bean.setCmpFileList(dataLine.get("CMPFILELIST"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	//任务搜索仅仅支持 按任务编号/用户编号/需求名称的关键词搜查
	public static List<TaskBean> getTaskSearch(String keywords){
		Connection conn = null;
		List<TaskBean>  data=new ArrayList<TaskBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getTaskSearch(keywords);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					TaskBean bean=new TaskBean();
					bean.setId(dataLine.get("ID"));
					bean.setTname(dataLine.get("NAME"));
					bean.setReqID(dataLine.get("REQ_ID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setArrangeDate(dataLine.get("DEAD_DATE"));
					bean.setOverDate(dataLine.get("DONE_DATE"));
					bean.setOwner(dataLine.get("OWNER"));
					bean.setOwnerName(dataLine.get("OWNERNAME"));
					bean.setScheID(dataLine.get("SCHE_ID"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setCrtUserName(dataLine.get("CRT_USERNAME"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setRstApprise(dataLine.get("RST_APPRISE"));
					bean.setCodeApprise(dataLine.get("CODE_APPRISE"));
					///////////////////////////////////////////////////
					bean.setApp(dataLine.get("APP"));
					bean.setRevDesc(dataLine.get("REVDESC"));
					bean.setScopeDesc(dataLine.get("SCOPEDESC"));
					bean.setSubmitJson(dataLine.get("SUBMITJSON"));
					bean.setTestDesc(dataLine.get("TESTDESC"));
					bean.setSelfTestJson(dataLine.get("SELFTESTJSON"));
					bean.setCaseJson(dataLine.get("CASEJSON"));
					bean.setVersionDesc(dataLine.get("VER_DESC"));
					bean.setCurrentUser(dataLine.get("CUR_USERID"));
					bean.setCurrentUserName(dataLine.get("CUR_USERNAME"));
					bean.setCurrentVersion(dataLine.get("CUR_VERSION"));
					bean.setReleaseFlag(dataLine.get("UPT_FLAG"));
					///////////////////////////////////////////
					bean.setFileList(dataLine.get("FILELIST"));
					bean.setCmpFileList(dataLine.get("CMPFILELIST"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	//根据任务编号取任务
    public static TaskBean getReq(String id){
		Connection conn = null;
		TaskBean  bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getTaskById(id);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					 bean=new TaskBean();
					bean.setId(dataLine.get("ID"));
					bean.setTname(dataLine.get("NAME"));
					bean.setReqID(dataLine.get("REQ_ID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setArrangeDate(dataLine.get("DEAD_DATE"));
					bean.setOverDate(dataLine.get("DONE_DATE"));
					bean.setOwner(dataLine.get("OWNER"));
					bean.setOwnerName(dataLine.get("OWNERNAME"));
					bean.setScheID(dataLine.get("SCHE_ID"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setCrtUserName(dataLine.get("CRT_USERNAME"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setRstApprise(dataLine.get("RST_APPRISE"));
					bean.setCodeApprise(dataLine.get("CODE_APPRISE"));
					///////////////////////////////////////////////////
					bean.setApp(dataLine.get("APP"));
					bean.setRevDesc(dataLine.get("REVDESC"));
					bean.setScopeDesc(dataLine.get("SCOPEDESC"));
					bean.setSubmitJson(dataLine.get("SUBMITJSON"));
					bean.setTestDesc(dataLine.get("TESTDESC"));
					bean.setSelfTestJson(dataLine.get("SELFTESTJSON"));
					bean.setCaseJson(dataLine.get("CASEJSON"));
					bean.setVersionDesc(dataLine.get("VER_DESC"));
					bean.setCurrentUser(dataLine.get("CUR_USERID"));
					bean.setCurrentUserName(dataLine.get("CUR_USERNAME"));
					bean.setCurrentVersion(dataLine.get("CUR_VERSION"));
					bean.setReleaseFlag(dataLine.get("UPT_FLAG"));
					///////////////////////////////////////////
					bean.setFileList(dataLine.get("FILELIST"));
					bean.setCmpFileList(dataLine.get("CMPFILELIST"));
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	//任务搜索获取总数量
	public static int getAllReqCount(){
		Connection conn = null;
		int result=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getCount();
			List sqlResult=SqlServerUtil.executeQuery(sql, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				Map dataLine=(Map)sqlResult.get(0);
				String count=dataLine.get("COUNT")+"";
				result=Integer.parseInt(count);
			}
		}
		catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return result;
	}
	
	//翻页查询任务列表
	public static List<TaskBean> getAllTopTasks(String startIndex,String endIndex){
		Connection conn = null;
		List<TaskBean>  data=new ArrayList<TaskBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql= TASKSql.getTopsReqs(startIndex, endIndex);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					TaskBean bean=new TaskBean();
					bean.setId(dataLine.get("ID"));
					bean.setTname(dataLine.get("NAME"));
					bean.setReqID(dataLine.get("REQ_ID"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setArrangeDate(dataLine.get("DEAD_DATE"));
					bean.setOverDate(dataLine.get("DONE_DATE"));
					bean.setOwner(dataLine.get("OWNER"));
					bean.setOwnerName(dataLine.get("OWNERNAME"));
					bean.setScheID(dataLine.get("SCHE_ID"));
					bean.setCrtUser(dataLine.get("CRT_USER"));
					bean.setCrtUserName(dataLine.get("CRT_USERNAME"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setRstApprise(dataLine.get("RST_APPRISE"));
					bean.setCodeApprise(dataLine.get("CODE_APPRISE"));
					///////////////////////////////////////////////////
					bean.setApp(dataLine.get("APP"));
					bean.setRevDesc(dataLine.get("REVDESC"));
					bean.setScopeDesc(dataLine.get("SCOPEDESC"));
					bean.setSubmitJson(dataLine.get("SUBMITJSON"));
					bean.setTestDesc(dataLine.get("TESTDESC"));
					bean.setSelfTestJson(dataLine.get("SELFTESTJSON"));
					bean.setCaseJson(dataLine.get("CASEJSON"));
					bean.setVersionDesc(dataLine.get("VER_DESC"));
					bean.setCurrentUser(dataLine.get("CUR_USERID"));
					bean.setCurrentUserName(dataLine.get("CUR_USERNAME"));
					bean.setCurrentVersion(dataLine.get("CUR_VERSION"));
					bean.setReleaseFlag(dataLine.get("UPT_FLAG"));
					///////////////////////////////////////////
					bean.setFileList(dataLine.get("FILELIST"));
					bean.setCmpFileList(dataLine.get("CMPFILELIST"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	//记录需求的每个处理步骤信息
	public static  int stepLog(String reqID,String userID,String stepID,String comment,String seq){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getLog(reqID, userID, stepID, comment, seq);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	//根据不同角色进行任务的跳转
	public static int stepSwtich(String reqID,String status,String nextUserID,String mdfUser){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getProgress(reqID, status, nextUserID, mdfUser);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	//取任务过程版本的新版本
	public static String getNewVersion(String sno){
		Connection conn = null;
		String version="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=TASKSql.getNewVersion(sno);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map line=(Map)result.get(0);
				version=line.get("ID")+"";
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return version;
	}
	
		//获得每个任务的过往记录
		public static List<RequirementLog> getLogs(String id){
			Connection conn = null;
			List<RequirementLog>  data=new ArrayList<RequirementLog>();
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=REQUIREMENTLOGSql.getAllLog(id);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					for(int w=0;w<result.size();w++){
						Map<String,String> dataLine=(Map)result.get(w);
						RequirementLog log=new RequirementLog();
						log.setReqID(dataLine.get("ID"));
						log.setUserID(dataLine.get("USER_ID"));
						log.setStepID(dataLine.get("STEP_ID"));
						log.setComment(dataLine.get("COMMENT"));
						log.setTime(dataLine.get("MDF_TIME"));
						log.setSeq(dataLine.get("SEQ"));
						log.setUserName(dataLine.get("USER_NAME"));
						data.add(log);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return data;
		}
			
			//获取任务某个步骤的处理信息(同一个处理步骤可能处理多次）
			public static RequirementLog getStepLog(String id,String stepID){
				Connection conn = null;
				RequirementLog  log=null;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=REQUIREMENTLOGSql.getLog(id,stepID);
					List result=SqlServerUtil.executeQuery(sql, conn);
					if(result!=null&&result.size()>0){
							Map<String,String> dataLine=(Map)result.get(0);
							 log=new RequirementLog();
							log.setReqID(dataLine.get("ID"));
							log.setUserID(dataLine.get("USER_ID"));
							log.setStepID(dataLine.get("STEP_ID"));
							log.setComment(dataLine.get("COMMENT"));
							log.setTime(dataLine.get("MDF_TIME"));
							log.setSeq(dataLine.get("SEQ"));
							log.setUserName(dataLine.get("USER_NAME"));
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return log;
			}	
			
			//获取视图的任务列表，默认是一个视图对应一个任务
			public static List<TaskBean> getViewReqs(String viewID){
				Connection conn = null;
				List<TaskBean>  data=new ArrayList<TaskBean>();
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=TASKSql.getViewReq(viewID);
					List result=SqlServerUtil.executeQuery(sql, conn);
					if(result!=null&&result.size()>0){
						for(int w=0;w<result.size();w++){
							Map<String,String> dataLine=(Map)result.get(w);
							TaskBean bean=new TaskBean();
							bean.setId(dataLine.get("ID"));
							bean.setTname(dataLine.get("NAME"));
							bean.setReqID(dataLine.get("REQ_ID"));
							bean.setStatus(dataLine.get("STATUS"));
							bean.setArrangeDate(dataLine.get("DEAD_DATE"));
							bean.setOverDate(dataLine.get("DONE_DATE"));
							bean.setOwner(dataLine.get("OWNER"));
							bean.setOwnerName(dataLine.get("OWNERNAME"));
							bean.setScheID(dataLine.get("SCHE_ID"));
							bean.setCrtUser(dataLine.get("CRT_USER"));
							bean.setCrtUserName(dataLine.get("CRT_USERNAME"));
							bean.setCrtTime(dataLine.get("CRT_TIME"));
							bean.setRstApprise(dataLine.get("RST_APPRISE"));
							bean.setCodeApprise(dataLine.get("CODE_APPRISE"));
							///////////////////////////////////////////////////
							bean.setApp(dataLine.get("APP"));
							bean.setRevDesc(dataLine.get("REVDESC"));
							bean.setScopeDesc(dataLine.get("SCOPEDESC"));
							bean.setSubmitJson(dataLine.get("SUBMITJSON"));
							bean.setTestDesc(dataLine.get("TESTDESC"));
							bean.setSelfTestJson(dataLine.get("SELFTESTJSON"));
							bean.setCaseJson(dataLine.get("CASEJSON"));
							bean.setVersionDesc(dataLine.get("VER_DESC"));
							bean.setCurrentUser(dataLine.get("CUR_USERID"));
							bean.setCurrentUserName(dataLine.get("CUR_USERNAME"));
							bean.setCurrentVersion(dataLine.get("CUR_VERSION"));
							bean.setReleaseFlag(dataLine.get("UPT_FLAG"));
							///////////////////////////////////////////
							bean.setFileList(dataLine.get("FILELIST"));
							bean.setCmpFileList(dataLine.get("CMPFILELIST"));
							data.add(bean);
						}
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return data;
			}
			
			//获取任务的视图
			public static String getViewByTask(String taskno){
				Connection conn = null;
				String version="";
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=TASKSql.getViewOfTask(taskno);
					List result=SqlServerUtil.executeQuery(sql, conn);
					if(result!=null&&result.size()>0){
						Map<String,String> line=(Map)result.get(0);
						version=line.get("VIEW_ID");
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return version;
			}
			
			public static List<TaskBean> getAllTasks(){
				Connection conn = null;
				List<TaskBean>  data=new ArrayList<TaskBean>();
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=TASKSql.getAllTask();
					List result=SqlServerUtil.executeQuery(sql, conn);
					if(result!=null&&result.size()>0){
						for(int w=0;w<result.size();w++){
							Map<String,String> dataLine=(Map)result.get(w);
							TaskBean bean=new TaskBean();
							bean.setId(dataLine.get("ID"));
							bean.setTname(dataLine.get("NAME"));
							bean.setReqID(dataLine.get("REQ_ID"));
							bean.setStatus(dataLine.get("STATUS"));
							bean.setArrangeDate(dataLine.get("DEAD_DATE"));
							bean.setOverDate(dataLine.get("DONE_DATE"));
							bean.setOwner(dataLine.get("OWNER"));
							bean.setOwnerName(dataLine.get("OWNERNAME"));
							bean.setScheID(dataLine.get("SCHE_ID"));
							bean.setCrtUser(dataLine.get("CRT_USER"));
							bean.setCrtUserName(dataLine.get("CRT_USERNAME"));
							bean.setCrtTime(dataLine.get("CRT_TIME"));
							bean.setRstApprise(dataLine.get("RST_APPRISE"));
							bean.setCodeApprise(dataLine.get("CODE_APPRISE"));
							///////////////////////////////////////////////////
							bean.setApp(dataLine.get("APP"));
							bean.setRevDesc(dataLine.get("REVDESC"));
							bean.setScopeDesc(dataLine.get("SCOPEDESC"));
							bean.setSubmitJson(dataLine.get("SUBMITJSON"));
							bean.setTestDesc(dataLine.get("TESTDESC"));
							bean.setSelfTestJson(dataLine.get("SELFTESTJSON"));
							bean.setCaseJson(dataLine.get("CASEJSON"));
							bean.setVersionDesc(dataLine.get("VER_DESC"));
							bean.setCurrentUser(dataLine.get("CUR_USERID"));
							bean.setCurrentUserName(dataLine.get("CUR_USERNAME"));
							bean.setCurrentVersion(dataLine.get("CUR_VERSION"));
							bean.setReleaseFlag(dataLine.get("UPT_FLAG"));
							///////////////////////////////////////////
							bean.setFileList(dataLine.get("FILELIST"));
							bean.setCmpFileList(dataLine.get("CMPFILELIST"));
							data.add(bean);
						}
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return data;
			}
			
			//对开发任务的延期修改
			public static int DelayDate(String id,String rdate,String reqID){
				Connection conn = null;
				int count=0;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=TASKSql.getDelay(id, rdate);
					count=SqlServerUtil.executeUpdate(sql, conn);
					//需求对应发生延期
					BACKLOG.delay(reqID);
				}catch(Exception e){
				
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return count;
			}
			
			//开发任务需求对应问题数的统计
			public static  int questionAdd(String reqID,String userID){
				Connection conn = null;
				int count=0;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=TASKSql.getQuestionAdd(reqID, userID);
					count=SqlServerUtil.executeUpdate(sql, conn);
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return count;
			}
			
			//开发任务统计
			public static int releaseAdd(String taskID){
				Connection conn = null;
				int count=0;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=TASKSql.getReleaseAdd(taskID);
					count=SqlServerUtil.executeUpdate(sql, conn);
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return count;
			}
			
	
}
