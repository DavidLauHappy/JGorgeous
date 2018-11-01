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
	
	//�����ύ
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
	
	 //�������ȥ�ķ���֪ͨ����
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
	
	//���񸽼����
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
	
	//������ȡ����ĸ�����Ϣ
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
	
	//��ȡ���˵������б�
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
					bean.setRdate(dataLine.get("RDATE")); //Ԥ������ʱ��
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
	
	//������������֧�� ��������/�û����/�������ƵĹؼ����Ѳ�
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
	
	//����������ȡ����
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
	
	//����������ȡ������
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
	
	//��ҳ��ѯ�����б�
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
	
	//��¼�����ÿ����������Ϣ
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
	
	//���ݲ�ͬ��ɫ�����������ת
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
	
	//ȡ������̰汾���°汾
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
	
		//���ÿ������Ĺ�����¼
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
			
			//��ȡ����ĳ������Ĵ�����Ϣ(ͬһ����������ܴ����Σ�
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
			
			//��ȡ��ͼ�������б�Ĭ����һ����ͼ��Ӧһ������
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
			
			//��ȡ�������ͼ
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
			
			//�Կ�������������޸�
			public static int DelayDate(String id,String rdate,String reqID){
				Connection conn = null;
				int count=0;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=TASKSql.getDelay(id, rdate);
					count=SqlServerUtil.executeUpdate(sql, conn);
					//�����Ӧ��������
					BACKLOG.delay(reqID);
				}catch(Exception e){
				
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return count;
			}
			
			//�������������Ӧ��������ͳ��
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
			
			//��������ͳ��
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
