package sql;

public class TASKSql {

	public static String getSubmit(String id,String revdesc,String fileList, String cmpFileList, String scopeDesc,String submitJson,String testDesc,String selfTestJson,String caseJson,String mdfUser,String version,String verDesc,String codeApprise,String rstApprise){
		String sql="update TASK_DEF set OVERDATE=@OVERDATE, RST_APPRISE='@RST_APPRISE', CODE_APPRISE='@CODE_APPRISE', REVDESC='@REVDESC',SCOPEDESC='@SCOPEDESC',SUBMITJSON='@SUBMITJSON',"+
				 "TESTDESC='@TESTDESC',SELFTESTJSON='@SELFTESTJSON',CASEJSON='@CASEJSON',MDF_USER='@MDF_USER',"+
				 "MDF_TIME=@MDF_TIME,CUR_VERSION='@CUR_VERSION',VER_DESC='@VER_DESC' where ID='@ID'";
				sql=sql.replace("@ID", id);
				sql=sql.replace("@RST_APPRISE", rstApprise);
				sql=sql.replace("@CODE_APPRISE", codeApprise);
				sql=sql.replace("@REVDESC", revdesc);
				sql=sql.replace("@VER_DESC", verDesc);
				sql=sql.replace("@SCOPEDESC",  scopeDesc);
				sql=sql.replace("@SUBMITJSON",  submitJson);
				sql=sql.replace("@TESTDESC",  testDesc);
				sql=sql.replace("@SELFTESTJSON",  selfTestJson);
				sql=sql.replace("@CASEJSON",  caseJson);
				sql=sql.replace("@CUR_VERSION",  version);
				sql=sql.replace("@MDF_USER",  mdfUser);
				sql=sql.replace("@MDF_TIME",   "CONVERT(varchar(100),GETDATE(),120)");
				sql=sql.replace("@OVERDATE",   "CONVERT(varchar(10),GETDATE(),120)");
				return sql;
	}
	
	public static String getUptFlag(String id,String status){
		String sql="update TASK_DEF set UPT_FLAG='@UPT_FLAG' where ID='@ID'";
		sql=sql.replace("@ID", id);
		sql=sql.replace("@UPT_FLAG", status);
		return sql;
	}
	
    public static String getDelay(String id,String rdate){
    	String sql="update TASK_DEF set ARRANGE_DATE='@ARRANGE_DATE',IS_DELAY='1',DELAY_CNT=DELAY_CNT+1, MDF_TIME=@MDF_TIME where ID='@ID'";
    	sql=sql.replace("@ID", id);
		sql=sql.replace("@ARRANGE_DATE", rdate);
		sql=sql.replace("@MDF_TIME",   "CONVERT(varchar(10),GETDATE(),120)");
		return sql;
    }
	
	public static String getAddAttachs(String sno,String fileID,String fileName,String fileTime,String md5,String mdfUser,String version,String fileType){
		String sql="insert into  TASK_FILES(SNO,FILE_ID,FILE_NAME,FILE_TIME,MD5,MDF_USER, MDF_TIME,VERSION,RFILE_TYPE) "+
						 "values('@SNO','@FILE_ID','@FILE_NAME','@FILE_TIME','@MD5','@MDF_USER',@MDF_TIME,'@VERSION','@RFILE_TYPE')";
		sql=sql.replace("@SNO", sno);
		sql=sql.replace("@FILE_ID", fileID);
		sql=sql.replace("@FILE_NAME", fileName);
		sql=sql.replace("@FILE_TIME", fileTime);
		sql=sql.replace("@MD5", md5);
		sql=sql.replace("@MDF_USER", mdfUser);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@VERSION", version);
		sql=sql.replace("@RFILE_TYPE", fileType);
		return sql;
	}
	
	public static String getAddAttachHD(String md5,String location,String mdfUser){
		String sql="insert into  FILES(MD5,LOCATION,CRT_USER, CRT_TIME) "+
						 "values('@MD5','@LOCATION','@CRT_USER',@CRT_TIME)";
		sql=sql.replace("@LOCATION", location);
		sql=sql.replace("@MD5", md5);
		sql=sql.replace("@CRT_USER", mdfUser);
		sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	
	public static String getAttach(String sno,String version,String type){
		String sql="select TASK_FILES.FILE_ID FILE_ID,TASK_FILES.FILE_NAME FILE_NAME, FILES.MD5 MD5,FILES.LOCATION LOCATION,TASK_FILES.FILE_TIME FILE_TIME,TASK_FILES.RFILE_TYPE RFILE_TYPE  "+
					"from TASK_FILES,FILES WHERE TASK_FILES.SNO='@SNO' and  TASK_FILES.RFILE_TYPE='@RFILE_TYPE' and  TASK_FILES.VERSION='@VERSION' and  TASK_FILES.MD5=FILES.MD5  ";
		sql=sql.replace("@SNO", sno);
		sql=sql.replace("@VERSION", version);
		sql=sql.replace("@RFILE_TYPE", type);
		return sql;
	}
	
	public static String getMyReqs(String userID){
		String sql="select REQUIREMENT.ID ID,"
								+"REQUIREMENT.TNAME NAME,"
								+"REQUIREMENT.REQ_ID REQ_ID,"
								+"REQUIREMENT.STATUS STATUS,"
								+"REQUIREMENT.ARRANGE_DATE DEAD_DATE,"
								+"REQUIREMENT.OVERDATE DONE_DATE,"
								+"REQUIREMENT.RDATE RDATE,"
								+"REQUIREMENT.OWNER OWNER,"
								+"USERS.USER_NAME OWNERNAME,"
								+"REQUIREMENT.SCHE_ID SCHE_ID,"
								+"REQUIREMENT.CRT_USER CRT_USER,"
								+"CCUSERS.USER_NAME CRT_USERNAME,"
								+"REQUIREMENT.CRT_TIME CRT_TIME,"
								+"REQUIREMENT.RST_APPRISE RST_APPRISE,"
								+"REQUIREMENT.CODE_APPRISE CODE_APPRISE,"
								+"REQUIREMENT.APP APP,"
								+"REQUIREMENT.REVDESC REVDESC,"
								+"REQUIREMENT.FILELIST FILELIST,"
								+"REQUIREMENT.CMPFILELIST CMPFILELIST,"
								+"REQUIREMENT.SCOPEDESC  SCOPEDESC,"
								+"REQUIREMENT.SUBMITJSON SUBMITJSON,"
								+"REQUIREMENT.TESTDESC TESTDESC,"
								+"REQUIREMENT.SELFTESTJSON SELFTESTJSON,"
								+"REQUIREMENT.CASEJSON CASEJSON,"
								+"REQUIREMENT.MDF_USER MDF_USER,"
								+"REQUIREMENT.REQDESC REQDESC,"
								+"REQUIREMENT.CUR_USERID CUR_USERID,"
								+"CUSERS.USER_NAME CUR_USERNAME,"
								+"REQUIREMENT.CUR_VERSION CUR_VERSION,"
								+"REQUIREMENT.UPT_FLAG UPT_FLAG,"
								+"REQUIREMENT.VER_DESC  VER_DESC "
								+ " from (select * FROM TASK_DEF left join REQUIREMENT_VERSION " +
								              "on (TASK_DEF.ID=REQUIREMENT_VERSION.NO and TASK_DEF.CUR_VERSION=REQUIREMENT_VERSION.VERSION) "
								              +"where  TASK_DEF.OWNER='@USER_ID' or TASK_DEF.CUR_USERID='@USER_ID')REQUIREMENT,USERS,USERS CUSERS,USERS CCUSERS "+       
						"where  REQUIREMENT.OWNER=USERS.USER_ID "+
							   "and REQUIREMENT.CUR_USERID=CUSERS.USER_ID "+
							   "and REQUIREMENT.CRT_USER=CCUSERS.USER_ID "+
							   "order by REQUIREMENT.CRT_TIME DESC";
			sql=sql.replace("@USER_ID", userID);
			return sql;
	}
	
	
	
	public static String getTaskSearch(String keyword){
			String sql="select REQUIREMENT.ID ID,"
					+"REQUIREMENT.TNAME NAME,"
					+"REQUIREMENT.REQ_ID REQ_ID,"
					+"REQUIREMENT.STATUS STATUS,"
					+"REQUIREMENT.ARRANGE_DATE DEAD_DATE,"
					+"REQUIREMENT.OVERDATE DONE_DATE,"
					+"REQUIREMENT.RDATE RDATE,"
					+"REQUIREMENT.OWNER OWNER,"
					+"REQUIREMENT.USER_NAME OWNERNAME,"
					+"REQUIREMENT.SCHE_ID SCHE_ID,"
					+"REQUIREMENT.CRT_USER CRT_USER,"
					+"CCUSERS.USER_NAME CRT_USERNAME,"
					+"REQUIREMENT.CRT_TIME CRT_TIME,"
					+"REQUIREMENT.RST_APPRISE RST_APPRISE,"
					+"REQUIREMENT.CODE_APPRISE CODE_APPRISE,"
					+"REQUIREMENT.APP APP,"
					+"REQUIREMENT.REVDESC REVDESC,"
					+"REQUIREMENT.FILELIST FILELIST,"
					+"REQUIREMENT.CMPFILELIST CMPFILELIST,"
					+"REQUIREMENT.SCOPEDESC  SCOPEDESC,"
					+"REQUIREMENT.SUBMITJSON SUBMITJSON,"
					+"REQUIREMENT.TESTDESC TESTDESC,"
					+"REQUIREMENT.SELFTESTJSON SELFTESTJSON,"
					+"REQUIREMENT.CASEJSON CASEJSON,"
					+"REQUIREMENT.MDF_USER MDF_USER,"
					+"REQUIREMENT.REQDESC REQDESC,"
					+"REQUIREMENT.CUR_USERID CUR_USERID,"
					+"CUSERS.USER_NAME CUR_USERNAME,"
					+"REQUIREMENT.CUR_VERSION CUR_VERSION,"
					+"REQUIREMENT.UPT_FLAG UPT_FLAG,"
					+"REQUIREMENT.VER_DESC  VER_DESC "
					+ " from (select TASK_DEF.*, FILELIST,CMPFILELIST,REQDESC, USER_NAME  FROM TASK_DEF left join REQUIREMENT_VERSION " +
					              "on (TASK_DEF.ID=REQUIREMENT_VERSION.NO and TASK_DEF.CUR_VERSION=REQUIREMENT_VERSION.VERSION) "+
					              "left join USERS on TASK_DEF.OWNER=USERS.USER_ID "
					              +"where  TASK_DEF.ID  LIKE '%@KEY%' or USERS.USER_NAME LIKE '%@KEY%'   or TASK_DEF.OWNER LIKE '%@KEY%' or TASK_DEF.TNAME LIKE '%@KEY%')REQUIREMENT,USERS CUSERS,USERS CCUSERS "+       
			"where    REQUIREMENT.CUR_USERID=CUSERS.USER_ID "+
				   "and REQUIREMENT.CRT_USER=CCUSERS.USER_ID "+
				   "order by REQUIREMENT.CRT_TIME DESC";
			sql=sql.replace("@KEY", keyword);
			return sql;
	  }
	
	
	public static String getTaskById(String id){
		String sql="select REQUIREMENT.ID ID,"
				+"REQUIREMENT.TNAME NAME,"
				+"REQUIREMENT.REQ_ID REQ_ID,"
				+"REQUIREMENT.STATUS STATUS,"
				+"REQUIREMENT.ARRANGE_DATE DEAD_DATE,"
				+"REQUIREMENT.OVERDATE DONE_DATE,"
				+"REQUIREMENT.RDATE RDATE,"
				+"REQUIREMENT.OWNER OWNER,"
				+"USERS.USER_NAME OWNERNAME,"
				+"REQUIREMENT.SCHE_ID SCHE_ID,"
				+"REQUIREMENT.CRT_USER CRT_USER,"
				+"CCUSERS.USER_NAME CRT_USERNAME,"
				+"REQUIREMENT.CRT_TIME CRT_TIME,"
				+"REQUIREMENT.RST_APPRISE RST_APPRISE,"
				+"REQUIREMENT.CODE_APPRISE CODE_APPRISE,"
				+"REQUIREMENT.APP APP,"
				+"REQUIREMENT.REVDESC REVDESC,"
				+"REQUIREMENT.FILELIST FILELIST,"
				+"REQUIREMENT.CMPFILELIST CMPFILELIST,"
				+"REQUIREMENT.SCOPEDESC  SCOPEDESC,"
				+"REQUIREMENT.SUBMITJSON SUBMITJSON,"
				+"REQUIREMENT.TESTDESC TESTDESC,"
				+"REQUIREMENT.SELFTESTJSON SELFTESTJSON,"
				+"REQUIREMENT.CASEJSON CASEJSON,"
				+"REQUIREMENT.MDF_USER MDF_USER,"
				+"REQUIREMENT.REQDESC REQDESC,"
				+"REQUIREMENT.CUR_USERID CUR_USERID,"
				+"CUSERS.USER_NAME CUR_USERNAME,"
				+"REQUIREMENT.CUR_VERSION CUR_VERSION,"
				+"REQUIREMENT.UPT_FLAG UPT_FLAG,"
				+"REQUIREMENT.VER_DESC  VER_DESC "
				+ " from (select * FROM TASK_DEF left join REQUIREMENT_VERSION " +
				              "on (TASK_DEF.ID=REQUIREMENT_VERSION.NO and TASK_DEF.CUR_VERSION=REQUIREMENT_VERSION.VERSION) "
				              +"where  TASK_DEF.ID  ='@ID')REQUIREMENT,USERS,USERS CUSERS,USERS CCUSERS "+       
		"where  REQUIREMENT.OWNER=USERS.USER_ID "+
			   "and REQUIREMENT.CUR_USERID=CUSERS.USER_ID "+
			   "and REQUIREMENT.CRT_USER=CCUSERS.USER_ID "+
			   "order by REQUIREMENT.CRT_TIME DESC";
		sql=sql.replace("@ID", id);
		return sql;
  }
	
	
	
	//获取任务总数
	public static String getCount(){
		String sql="select count(*) AS COUNT from TASK_DEF";
		return sql;
	}
	
	public static String getTopsReqs(String start,String end ){
		String sql="select REQUIREMENT.ID ID,"
				+"REQUIREMENT.TNAME NAME,"
				+"REQUIREMENT.REQ_ID REQ_ID,"
				+"REQUIREMENT.STATUS STATUS,"
				+"REQUIREMENT.ARRANGE_DATE DEAD_DATE,"
				+"REQUIREMENT.OVERDATE DONE_DATE,"
				+"REQUIREMENT.RDATE RDATE,"
				+"REQUIREMENT.OWNER OWNER,"
				+"USERS.USER_NAME OWNERNAME,"
				+"REQUIREMENT.SCHE_ID SCHE_ID,"
				+"REQUIREMENT.CRT_USER CRT_USER,"
				+"CCUSERS.USER_NAME CRT_USERNAME,"
				+"REQUIREMENT.CRT_TIME CRT_TIME,"
				+"REQUIREMENT.RST_APPRISE RST_APPRISE,"
				+"REQUIREMENT.CODE_APPRISE CODE_APPRISE,"
				+"REQUIREMENT.APP APP,"
				+"REQUIREMENT.REVDESC REVDESC,"
				+"REQUIREMENT.FILELIST FILELIST,"
				+"REQUIREMENT.CMPFILELIST CMPFILELIST,"
				+"REQUIREMENT.SCOPEDESC  SCOPEDESC,"
				+"REQUIREMENT.SUBMITJSON SUBMITJSON,"
				+"REQUIREMENT.TESTDESC TESTDESC,"
				+"REQUIREMENT.SELFTESTJSON SELFTESTJSON,"
				+"REQUIREMENT.CASEJSON CASEJSON,"
				+"REQUIREMENT.MDF_USER MDF_USER,"
				+"REQUIREMENT.REQDESC REQDESC,"
				+"REQUIREMENT.CUR_USERID CUR_USERID,"
				+"CUSERS.USER_NAME CUR_USERNAME,"
				+"REQUIREMENT.CUR_VERSION CUR_VERSION,"
				+"REQUIREMENT.UPT_FLAG UPT_FLAG,"
				+"REQUIREMENT.VER_DESC  VER_DESC "
	+" from (select top @end row_number() OVER(ORDER BY CRT_TIME DESC) rn,* FROM TASK_DEF left join REQUIREMENT_VERSION "+
				"on (TASK_DEF.ID=REQUIREMENT_VERSION.NO and TASK_DEF.CUR_VERSION=REQUIREMENT_VERSION.VERSION))REQUIREMENT,USERS,USERS CUSERS,USERS CCUSERS "+
							"where   REQUIREMENT.rn>@start "+
								"and REQUIREMENT.OWNER=USERS.USER_ID "+
							   "and REQUIREMENT.CUR_USERID=CUSERS.USER_ID "+
							   "and REQUIREMENT.CRT_USER=CCUSERS.USER_ID "+
							  "order by REQUIREMENT.CRT_TIME DESC";
			sql=sql.replace("@start", start);
			sql=sql.replace("@end", end);
			return sql;
	}
	
	
	public static String getLog(String reqID,String userID,String stepID,String comment,String seq){
		String sql="insert into REQUIREMENT_LOG(ID,USER_ID,STEP_ID,COMMENT,MDF_TIME,SEQ) "+
						  "values('@ID','@USER_ID','@STEP_ID','@COMMENT',@MDF_TIME,'@SEQ')";
		sql=sql.replace("@ID", reqID);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@STEP_ID", stepID);
		sql=sql.replace("@COMMENT", comment);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@SEQ", seq);
		return sql;
	}
	
	public static String getQuestionAdd(String reqID,String owner){
		String sql="update TASK_DEF set RST_CNT=RST_CNT+1,MDF_TIME=@MDF_TIME where REQ_ID='@REQ_ID' and OWNER='@OWNER'";
		sql=sql.replace("@REQ_ID", reqID);
		sql=sql.replace("@OWNER", owner);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
  public static String getReleaseAdd(String taskID){
		String sql="update TASK_DEF set RLS_CNT=RLS_CNT+1,MDF_TIME=@MDF_TIME where ID='@ID'";
		sql=sql.replace("@ID", taskID);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
  }
	
	
	public static String getProgress(String reqID,String status,String userID,String mdfUser){
		String sql="update  TASK_DEF set STATUS='@STATUS',CUR_USERID='@CUR_USERID',MDF_USER='@MDF_USER',MDF_TIME=@MDF_TIME  "+
				  "where ID='@ID'";
				sql=sql.replace("@ID", reqID);
				sql=sql.replace("@STATUS", status);
				sql=sql.replace("@CUR_USERID", userID);
				sql=sql.replace("@MDF_USER", mdfUser);
				sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
				return sql;
	}
	
	
	public static String getViewReq(String viewID){
				String sql="select REQUIREMENT.ID ID,"
						+"REQUIREMENT.TNAME NAME,"
						+"REQUIREMENT.REQ_ID REQ_ID,"
						+"REQUIREMENT.STATUS STATUS,"
						+"REQUIREMENT.ARRANGE_DATE DEAD_DATE,"
						+"REQUIREMENT.OVERDATE DONE_DATE,"
						+"REQUIREMENT.RDATE RDATE,"
						+"REQUIREMENT.OWNER OWNER,"
						+"USERS.USER_NAME OWNERNAME,"
						+"REQUIREMENT.SCHE_ID SCHE_ID,"
						+"REQUIREMENT.CRT_USER CRT_USER,"
						+"CCUSERS.USER_NAME CRT_USERNAME,"
						+"REQUIREMENT.CRT_TIME CRT_TIME,"
						+"REQUIREMENT.RST_APPRISE RST_APPRISE,"
						+"REQUIREMENT.CODE_APPRISE CODE_APPRISE,"
						+"REQUIREMENT.APP APP,"
						+"REQUIREMENT.REVDESC REVDESC,"
						+"REQUIREMENT.FILELIST FILELIST,"
						+"REQUIREMENT.CMPFILELIST CMPFILELIST,"
						+"REQUIREMENT.SCOPEDESC  SCOPEDESC,"
						+"REQUIREMENT.SUBMITJSON SUBMITJSON,"
						+"REQUIREMENT.TESTDESC TESTDESC,"
						+"REQUIREMENT.SELFTESTJSON SELFTESTJSON,"
						+"REQUIREMENT.CASEJSON CASEJSON,"
						+"REQUIREMENT.MDF_USER MDF_USER,"
						+"REQUIREMENT.REQDESC REQDESC,"
						+"REQUIREMENT.CUR_USERID CUR_USERID,"
						+"CUSERS.USER_NAME CUR_USERNAME,"
						+"REQUIREMENT.CUR_VERSION CUR_VERSION,"
						+"REQUIREMENT.UPT_FLAG UPT_FLAG,"
						+"REQUIREMENT.VER_DESC  VER_DESC "
			+" from (select * FROM TASK_DEF left join REQUIREMENT_VERSION on (TASK_DEF.ID=REQUIREMENT_VERSION.NO and TASK_DEF.CUR_VERSION=REQUIREMENT_VERSION.VERSION) "
					+"	where exists(select 1 from VIEW_REQ WHERE VIEW_ID='@VIEW_ID' and SNO=TASK_DEF.ID))REQUIREMENT,USERS,USERS CUSERS,USERS CCUSERS "
					  + " where    REQUIREMENT.OWNER=USERS.USER_ID "+
							   "and REQUIREMENT.CUR_USERID=CUSERS.USER_ID "+
							   "and REQUIREMENT.CRT_USER=CCUSERS.USER_ID "+
							  "order by REQUIREMENT.CRT_TIME DESC";
			sql=sql.replace("@VIEW_ID", viewID);
			return sql;
	}
	
	
	public static String getNewVersion(String sno){
			String sql="SELECT  ISNULL(max(VERSION),0)+1  ID FROM REQUIREMENT_VERSION WHERE NO='@SNO'";
			sql=sql.replace("@SNO", sno);
		return sql;
	}
	
	public static String getViewOfTask(String taskNo){
		String sql="SELECT  VIEW_ID   FROM VIEW_REQ WHERE SNO='@SNO'";
		sql=sql.replace("@SNO", taskNo);
		return sql;
	}
	
		public static String getAllTask(){
			String sql="select REQUIREMENT.ID ID,"
									+"REQUIREMENT.TNAME NAME,"
									+"REQUIREMENT.REQ_ID REQ_ID,"
									+"REQUIREMENT.STATUS STATUS,"
									+"REQUIREMENT.ARRANGE_DATE DEAD_DATE,"
									+"REQUIREMENT.OVERDATE DONE_DATE,"
									+"REQUIREMENT.RDATE RDATE,"
									+"REQUIREMENT.OWNER OWNER,"
									+"USERS.USER_NAME OWNERNAME,"
									+"REQUIREMENT.SCHE_ID SCHE_ID,"
									+"REQUIREMENT.CRT_USER CRT_USER,"
									+"CCUSERS.USER_NAME CRT_USERNAME,"
									+"REQUIREMENT.CRT_TIME CRT_TIME,"
									+"REQUIREMENT.RST_APPRISE RST_APPRISE,"
									+"REQUIREMENT.CODE_APPRISE CODE_APPRISE,"
									+"REQUIREMENT.APP APP,"
									+"REQUIREMENT.REVDESC REVDESC,"
									+"REQUIREMENT.FILELIST FILELIST,"
									+"REQUIREMENT.CMPFILELIST CMPFILELIST,"
									+"REQUIREMENT.SCOPEDESC  SCOPEDESC,"
									+"REQUIREMENT.SUBMITJSON SUBMITJSON,"
									+"REQUIREMENT.TESTDESC TESTDESC,"
									+"REQUIREMENT.SELFTESTJSON SELFTESTJSON,"
									+"REQUIREMENT.CASEJSON CASEJSON,"
									+"REQUIREMENT.MDF_USER MDF_USER,"
									+"REQUIREMENT.REQDESC REQDESC,"
									+"REQUIREMENT.CUR_USERID CUR_USERID,"
									+"CUSERS.USER_NAME CUR_USERNAME,"
									+"REQUIREMENT.CUR_VERSION CUR_VERSION,"
									+"REQUIREMENT.UPT_FLAG UPT_FLAG,"
									+"REQUIREMENT.VER_DESC  VER_DESC "
						+" from (select * FROM TASK_DEF left join REQUIREMENT_VERSION on (TASK_DEF.ID=REQUIREMENT_VERSION.NO and TASK_DEF.CUR_VERSION=REQUIREMENT_VERSION.VERSION) )REQUIREMENT,USERS,USERS CUSERS,USERS CCUSERS "
								  + " where   REQUIREMENT.OWNER=USERS.USER_ID "+
										   "and REQUIREMENT.CUR_USERID=CUSERS.USER_ID "+
										   "and REQUIREMENT.CRT_USER=CCUSERS.USER_ID "+
										  "order by REQUIREMENT.CRT_TIME DESC";
		return sql;
	}
	
}
