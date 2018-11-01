package sql;

public class QUESTIONSql {
	
	public static String getAdd(String id,String qdesc,String type,String status,String app,String reqID,String crtUser,String curUser,String owner){
		 String sql="insert into QUESTIONS(ID,QDESC,QTYPE,QSTATUS,APP,REQID,CRT_USER,CRT_TIME,CUR_USER,MDF_TIME,DEVELOPER) values"+
				"('@ID','@QDESC','@QTYPE','@QSTATUS','@APP','@REQID','@CRT_USER',@CRT_TIME,'@CUR_USER',@MDF_TIME,'@DEVELOPER')";
				sql=sql.replace("@ID", id);
				sql=sql.replace("@QDESC", qdesc);
				sql=sql.replace("@QTYPE", type);
				sql=sql.replace("@QSTATUS", status);
				sql=sql.replace("@APP", app);
				sql=sql.replace("@REQID", reqID);
				sql=sql.replace("@CRT_USER", crtUser);
				sql=sql.replace("@CUR_USER", curUser);
				sql=sql.replace("@DEVELOPER", owner);
				sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
				sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
				return sql;
		}
	
	  public static String getDEdit(String id,String mdesc,String status){
		  String sql="update QUESTIONS set MDESC='@MDESC',QSTATUS='@QSTATUS',MDF_TIME=@MDF_TIME where ID='@ID'";
			sql=sql.replace("@ID", id);
			sql=sql.replace("@MDESC", mdesc);
			sql=sql.replace("@QSTATUS", status);
			sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
	  }
	  
	  public static String getUpdate(String id,String qdesc,String type,String status,String app,String reqID,String curUser,String developer){
			 String sql="update QUESTIONS set QDESC='@QDESC', REQID='@REQID',QTYPE='@QTYPE', APP='@APP',CUR_USER='@CUR_USER',QSTATUS='@QSTATUS', DEVELOPER='@DEVELOPER', MDF_TIME=@MDF_TIME where ID='@ID'";
				sql=sql.replace("@ID", id);
				sql=sql.replace("@QDESC", qdesc);
				sql=sql.replace("@QTYPE", type);
				sql=sql.replace("@QSTATUS", status);
				sql=sql.replace("@@APP", app);
				sql=sql.replace("@REQID", reqID);
				sql=sql.replace("@CUR_USER", curUser);
				sql=sql.replace("@DEVELOPER", developer);
				sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
				return sql;
	}
	  
	public static String getStatusSet(String id,String qstatus){  
		 String sql="update QUESTIONS set QSTATUS='@QSTATUS',MDF_TIME=@MDF_TIME where ID='@ID'";
			sql=sql.replace("@ID", id);
			sql=sql.replace("@QSTATUS", qstatus);
			sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
	}
	  
	  public static String getTEdit(String id,String status,String curUser,String qdesc){
		  String sql="update QUESTIONS set CUR_USER='@CUR_USER',QSTATUS='@QSTATUS',QDESC='@QDESC',MDF_TIME=@MDF_TIME where ID='@ID'";
					sql=sql.replace("@ID", id);
					sql=sql.replace("@CUR_USER", curUser);
					sql=sql.replace("@QSTATUS", status);
					sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
					return sql;
	  }
	  
	  
	
	  public static String getNewID(){
		String sql="select NEXT VALUE  FOR SEQ_QUST_ID as ID";
		return sql;
	  }
	  
	  public static String getSearchCount(String id,String userID){
		  String sql="select COUNT(*) COUNT from QUESTIONS,REQUIREMENT B,USERS C,USERS D "+
				  			"WHERE QUESTIONS.REQID=B.ID "+
				  			  "AND QUESTIONS.CRT_USER=C.USER_ID "+
				  			  "AND QUESTIONS.CUR_USER=D.USER_ID "+
				  			  "AND ('@qid'='' or QUESTIONS.ID='@qid') "+
				  			  "AND (@userCnd)";
			sql=sql.replace("@qid", id);
			sql=sql.replace("@userCnd", userID);
			return sql;
	  }
	  
	  public static String getSearch(String id,String userID,String startIndex,String endIndex){
		  String sql=" select ID,QDESC,MDESC,QTYPE,QSTATUS,APP,REQID,REQ_ID, REQ_NAME,CRT_USER,CRT_USERNAME,CRT_TIME,CUR_USER,CUR_USERNAME,DEVELOPER  from("+
				  			"SELECT TOP @endIndex row_number() over(ORDER BY QUESTIONS.CRT_TIME DESC) RN, QUESTIONS.ID ID,QUESTIONS.QDESC QDESC,"+
				  						"QUESTIONS.MDESC MDESC,QUESTIONS.QTYPE QTYPE,QUESTIONS.QSTATUS QSTATUS,QUESTIONS.APP APP,QUESTIONS.REQID REQID,"+
				  						 "B.ID REQ_ID,B.TNAME REQ_NAME,QUESTIONS.CRT_USER CRT_USER,C.USER_NAME CRT_USERNAME,QUESTIONS.CRT_TIME CRT_TIME,"+
				  						  "QUESTIONS.CUR_USER CUR_USER,D.USER_NAME CUR_USERNAME,QUESTIONS.DEVELOPER  DEVELOPER FROM QUESTIONS,TASK_DEF B,USERS C,USERS D "+
								  "WHERE QUESTIONS.REQID=B.ID "+
								    "AND QUESTIONS.CRT_USER=C.USER_ID "+
								    "AND QUESTIONS.CUR_USER=D.USER_ID "+
								    "AND ('@qid'='' or QUESTIONS.ID='@qid') "+
								    "AND @userCnd )QUESTION "+
								 "WHERE QUESTION.RN>@startIndex";
			sql=sql.replace("@qid", id);
			sql=sql.replace("@userCnd", userID);
			sql=sql.replace("@startIndex", startIndex);
			sql=sql.replace("@endIndex", endIndex);
			return sql;
	  }
	  
	  public static String getReqQustions(String reqID){
		  String  sql="select ID,QDESC,QSTATUS from QUESTIONS where REQID='@REQID' and QSTATUS in('2','6')";
		  sql=sql.replace("@REQID", reqID);
		  return sql;
	  }
	  
	  public static String getAllReqQustions(String reqID,String qid){
		  String  sql="select ID,QDESC,QSTATUS from QUESTIONS where REQID='@REQID'  and ID<>'@ID'";
		  sql=sql.replace("@REQID", reqID);
		  sql=sql.replace("@ID", qid);
		  return sql;
	  }
	  
}
