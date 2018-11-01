package sql;

import resource.Context;

public class FLOWDETAILSql {
	
	 public static String getSeq(){
		 String sql="select NEXT VALUE  FOR SEQ_APPR_ID as ID";
		 return sql;
	  }
	
	 public static String getAdd(String id,String flowID,String stepID,String status,String locked,String apprData,String mdfUser,String applyUser,String bussObj){
		 String sql="insert into FLOW_DETAIL(ID, FLOW_ID,CURR_STEP,REC_STATUS,LOCKED,APPR_DATA,MDF_USER,MDF_TIME,SUBMIT_USER,SUBMIT_TIME,BUSS_OBJ) "+
				 		  "values('@ID', '@FLOW_ID','@CURR_STEP','@REC_STATUS','@LOCKED','@APPR_DATA','@MDF_USER',@MDF_TIME,'@SUBMIT_USER',@SUBMIT_TIME,'@BUSS_OBJ')";
		 sql=sql.replace("@ID", id);
		 sql=sql.replace("@FLOW_ID", flowID);
		 sql=sql.replace("@CURR_STEP", stepID);
		 sql=sql.replace("@REC_STATUS", status);
		 sql=sql.replace("@LOCKED", locked);
		 sql=sql.replace("@APPR_DATA", apprData);
		 sql=sql.replace("@MDF_USER", mdfUser);
		 sql=sql.replace("@SUBMIT_USER", applyUser);
		 sql=sql.replace("@BUSS_OBJ", bussObj);
		 sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		 sql=sql.replace("@SUBMIT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		 return sql;
	 }
	 
	   public static String getDetail(String userID){
		   String sql="select FLOW_DETAIL.ID APPR_ID,FLOW_DETAIL.FLOW_ID FLOW_ID,FLOW_DEF.NAME FLOW_NAME,FLOW_DETAIL.SUBMIT_USER SUBMIT_USER,"+
				   						"FLOW_DETAIL.SUBMIT_TIME SUBMIT_TIME,FLOW_DETAIL.REC_STATUS STATUS,FLOW_DETAIL.CURR_STEP STEP_ID, FLOW_STEP.NAME STEP_NAME,"+
				   						"FLOW_APPROVER.USER_ID USER_ID,FLOW_DETAIL.APPR_DATA APPR_DATA "+
				   			"from FLOW_DETAIL,FLOW_DEF,FLOW_STEP,FLOW_APPROVER "+
				   		"where FLOW_DETAIL.SUBMIT_USER='@USER_ID' "+
				   			"and   FLOW_DETAIL.FLOW_ID=FLOW_DEF.ID "+
				   			"and   FLOW_DETAIL.FLOW_ID=FLOW_STEP.FLOW_ID "+
				   			"and   FLOW_DETAIL.CURR_STEP=FLOW_STEP.ID "+
				   			"and   FLOW_DETAIL.ID=FLOW_APPROVER.APPR_ID "+
				   			"and   FLOW_DETAIL.CURR_STEP=FLOW_APPROVER.STEP_ID "+
				   			"and   FLOW_DETAIL.FLOW_ID=FLOW_APPROVER.FLOW_ID";
	    	sql=sql.replace("@USER_ID", userID);
	    	return sql;
	   }
	   
	   public static String getTask(String userID){
		   String sql="select  FLOW_DETAIL.ID  ID,"+
										"FLOW_DETAIL.SUBMIT_USER  SUBMIT_USER,"+
										"USERS.USER_NAME  APPLY_USERNAME,"+
										"FLOW_DETAIL.SUBMIT_TIME SUBMIT_TIME,"+
										"FLOW_DEF.ID	    FLOW_ID,"+
										"FLOW_DEF.NAME	    FLOW_NAME,"+
										"FLOW_STEP.ID			STEP_ID,"+
										"FLOW_STEP.NAME	STEPNAME,"+
										"FLOW_DETAIL.APPR_DATA APPR_DATA,"+
										"FLOW_DETAIL.BUSS_OBJ	BUSS_OBJ,"+
										"FLOW_STEP.PAGE_URL	 PAGE_URL, "+
										"FLOW_STEP.URL	URL,"+
										"FLOW_STEP.ACTNAME 	ACTNAME,"+
										"FLOW_STEP.NOT_URL	NOT_URL,"+
										"FLOW_STEP.NOT_ACTNAME	NOT_ACTNAME "+
							   "from FLOW_DETAIL,FLOW_DEF,FLOW_STEP,FLOW_APPROVER,USERS "+
					"where FLOW_DETAIL.ID=FLOW_APPROVER.APPR_ID "+
						"and FLOW_DETAIL.LOCKED='0' "+
						"and FLOW_DETAIL.REC_STATUS <>'2' "+
						"and FLOW_APPROVER.USER_ID='@USER_ID' "+
						"and FLOW_DETAIL.SUBMIT_USER=USERS.USER_ID "+
						"and FLOW_DETAIL.FLOW_ID=FLOW_DEF.ID "+
						"and FLOW_DETAIL.FLOW_ID=FLOW_STEP.FLOW_ID "+
						"and FLOW_DETAIL.CURR_STEP=FLOW_STEP.ID "+
						"and FLOW_APPROVER.FLOW_ID=FLOW_DETAIL.FLOW_ID "+
						"and FLOW_APPROVER.STEP_ID=FLOW_STEP.ID";
		   sql=sql.replace("@USER_ID", userID);
	    	return sql;
	   }
	   
	   public static String getDone(String userID){
		   String    sql  ="select  FLOW_DETAIL.ID  ID, "+
											"FLOW_DETAIL.SUBMIT_USER  SUBMIT_USER, "+
											"USERS.USER_NAME  APPLY_USERNAME,  "+
											"FLOW_DETAIL.SUBMIT_TIME SUBMIT_TIME, "+
											"FLOW_DETAIL.REC_STATUS  REC_STATUS, "+
											"FLOW_DEF.ID	    FLOW_ID, "+
											"FLOW_DEF.NAME	    FLOW_NAME, "+
											"FLOW_STEP.ID			STEP_ID, "+
											"FLOW_STEP.NAME	STEPNAME, "+
											"FLOW_DETAIL.APPR_DATA APPR_DATA, "+
											"FLOW_DETAIL.BUSS_OBJ	BUSS_OBJ, "+
											"FLOW_STEP.PAGE_URL  PAGE_URL, "+
											"FLOW_APPROVE.APPR_INFO  APPR_INFO, "+
											"CONVERT(varchar(100),FLOW_APPROVE.MDF_TIME,120)    MDF_TIME "+
									"from FLOW_DETAIL,FLOW_DEF,FLOW_STEP,FLOW_APPROVE,USERS  "+
							"where FLOW_DETAIL.ID=FLOW_APPROVE.APPR_ID  "+
							"and FLOW_APPROVE.USER_ID='@USER_ID'  "+
							"and FLOW_DETAIL.SUBMIT_USER=USERS.USER_ID  "+
							"and FLOW_DETAIL.FLOW_ID= FLOW_DEF.ID "+
							"and FLOW_DETAIL.FLOW_ID=FLOW_APPROVE.FLOW_ID  "+
							"and FLOW_APPROVE.FLOW_ID=FLOW_STEP.FLOW_ID  "+
							"and FLOW_APPROVE.STEP_ID=FLOW_STEP.ID "+
							"order by FLOW_APPROVE.MDF_TIME DESC";
		   			sql=sql.replace("@USER_ID", userID);
		   			return sql;
	   }
	   
	   
	   public static String getFlowProcess(String bussObj){
		   String sql="SELECT FLOW_DETAIL.ID ID,FLOW_DETAIL.FLOW_ID  FLOW_ID,FLOW_APPROVE.STEP_ID STEP_ID,FLOW_STEP.NAME NAME," +
		   								"FLOW_APPROVE.USER_ID USER_ID,USERS.USER_NAME USER_NAME,FLOW_APPROVE.APPR_INFO APPR_INFO,CONVERT(varchar(100),FLOW_APPROVE.MDF_TIME,120) MDF_TIME "+
		   							"FROM FLOW_DETAIL,FLOW_APPROVE,FLOW_STEP,USERS " +
								"WHERE FLOW_DETAIL.BUSS_OBJ='@BUSS_OBJ' " +
								"and FLOW_DETAIL.ID=FLOW_APPROVE.APPR_ID " +
								"and FLOW_DETAIL.FLOW_ID=FLOW_APPROVE.FLOW_ID " +
								"and FLOW_DETAIL.FLOW_ID=FLOW_STEP.FLOW_ID " +
								"and FLOW_APPROVE.STEP_ID=FLOW_STEP.ID " +
								"and FLOW_APPROVE.USER_ID=USERS.USER_ID " +
								"ORDER BY FLOW_APPROVE.MDF_TIME";
		   sql=sql.replace("@BUSS_OBJ", bussObj);
		   return sql;
	   }
	   
	   public static String getLocked(String id){
		   String sql="select LOCKED from FLOW_DETAIL where ID='@ID'";
		   sql=sql.replace("@ID", id);
	       return sql;
	   }
	   
	   public static String getStatus(String id){
		   String sql="select REC_STATUS from FLOW_DETAIL where ID='@ID'";
		   sql=sql.replace("@ID", id);
	       return sql;
	   }
	   
	   public static String getSetLocked(String userID,String id,String locked){
		   String sql="update FLOW_DETAIL set LOCKED='@LOCKED',MDF_USER='@MDF_USER',MDF_TIME=@MDF_TIME   where ID='@ID'";
		   sql=sql.replace("@ID", id);
		   sql=sql.replace("@LOCKED", locked);
		   sql=sql.replace("@MDF_USER", userID);
		   sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
	       return sql;
	   }
	   
	   public static String getApprNotStep(String apprID,String userID,String flowID,String stepID,String status,String info,String seq){
		   String sql="insert into  FLOW_APPROVE(APPR_ID,USER_ID,FLOW_ID,STEP_ID,APPR_STATUS,APPR_INFO,MDF_TIME,SEQ_ID) "+
				   			"values('@APPR_ID','@USER_ID','@FLOW_ID','@STEP_ID','@APPR_STATUS','@APPR_INFO',@MDF_TIME,'@SEQ_ID')";
		   sql=sql.replace("@APPR_ID", apprID);
		   sql=sql.replace("@USER_ID", userID);
		   sql=sql.replace("@FLOW_ID", flowID);
		   sql=sql.replace("@STEP_ID", stepID);
		   sql=sql.replace("@APPR_STATUS", status);
		   sql=sql.replace("@APPR_INFO", info);
		   sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		   sql=sql.replace("@SEQ_ID", seq);
	       return sql;
	   }
	   
	  public static String getApprOverDetail(String apprID,String userID,String status){
		  String sql="update FLOW_DETAIL set LOCKED='@LOCKED',REC_STATUS='@REC_STATUS',MDF_USER='@MDF_USER',MDF_TIME=@MDF_TIME   where ID='@ID'";
		   sql=sql.replace("@ID", apprID);
		   sql=sql.replace("@LOCKED", "0");//解锁
		   sql=sql.replace("@REC_STATUS", status);//流程结束
		   sql=sql.replace("@MDF_USER", userID);
		   sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
	       return sql;
	  }
	  
	  public static String getNextStep(String flowID,String stepID){
		  String sql="select  NEXT_ID from  FLOW_STEP where FLOW_ID='@FLOW_ID' and ID='@ID'";
		   sql=sql.replace("@FLOW_ID", flowID);
		   sql=sql.replace("@ID", stepID);
	       return sql;
	  }
	  
	  public static String getApprDetailJump(String apprID,String stepID){
		  String sql="update FLOW_DETAIL set CURR_STEP='@CURR_STEP',LOCKED='@LOCKED',REC_STATUS='@REC_STATUS',MDF_USER='@MDF_USER',MDF_TIME=@MDF_TIME   where ID='@ID'";
		   sql=sql.replace("@ID", apprID);
		   sql=sql.replace("@LOCKED", "0");//解锁
		   sql=sql.replace("@REC_STATUS", "1");//流程继续进行中
		   sql=sql.replace("@CURR_STEP", stepID);
		   sql=sql.replace("@MDF_USER", Context.session.userID);
		   sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
	       return sql;
	  }
	  
	  public static String getNextProcess(String bussObj){
		  String sql="select FLOW_DETAIL.ID  ID, FLOW_DETAIL.FLOW_ID FLOW_ID,FLOW_DETAIL.CURR_STEP CURR_STEP,A.NAME CURR_STEPNAME,A.NEXT_ID NEXT_STEPID,B.NAME NEXT_STEPNAME,A.APPRER_TYPE APPRER_TYPE,A.APPRER_ID APPRER_ID,"+
				  					 "FLOW_DETAIL.REC_STATUS REC_STATUS,FLOW_DETAIL.LOCKED  LOCKED,FLOW_DETAIL.SUBMIT_USER SUBMIT_USER, FLOW_DETAIL.SUBMIT_TIME  SUBMIT_TIME,A.URL URL,A.ACTNAME ACTNAME,A.NOT_URL  NOT_URL,A.NOT_ACTNAME NOT_ACTNAME "+
				  "from FLOW_DETAIL,FLOW_STEP A,FLOW_STEP B "+
				"WHERE FLOW_DETAIL.BUSS_OBJ='@BUSSID' "+
					"and FLOW_DETAIL.FLOW_ID=A.FLOW_ID "+
					"and A.FLOW_ID=B.FLOW_ID "+
					"and FLOW_DETAIL.CURR_STEP=A.ID "+
					"and A.NEXT_ID=B.ID";
		  sql=sql.replace("@BUSSID", bussObj);
		   return sql;
	  }
	  
	  public static String getApprDetail(String bussObj){
		  String sql="select ID,FLOW_ID,CURR_STEP,REC_STATUS,LOCKED,APPR_DATA,MDF_USER,MDF_TIME,SUBMIT_USER,SUBMIT_TIME,BUSS_OBJ from FLOW_DETAIL where BUSS_OBJ='@BUSS_OBJ'";
		  sql=sql.replace("@BUSS_OBJ", bussObj);
		  return sql;
	  }
	  
	  public static String getCurrApprover(String apprID,String flowID,String stepID){
		  String sql="select USER_ID from FLOW_APPROVER where APPR_ID='@APPR_ID' and FLOW_ID='@FLOW_ID' and STEP_ID='@STEP_ID'";
		  sql=sql.replace("@APPR_ID", apprID);
		  sql=sql.replace("@FLOW_ID", flowID);
		  sql=sql.replace("@STEP_ID", stepID);
		  return sql;
	  }
}
