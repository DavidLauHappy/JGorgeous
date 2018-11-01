package sql;

public class DELAYDETAILSql {
	public static String getInsert(String reqID,String taskID,String logID,String applyUser,String apprUser,String status,String reason,String pdate,String ddate){
		String sql="insert into DELAY_DETAIL(REQ_ID,TASK_ID,LOG_ID,APPLY_USER,APPLY_TIME,APPR_USER,DSTATUS,REASON,DTIME,PDATE,DDATE,MTIME)  "+
						"values('@REQ_ID','@TASK_ID','@LOG_ID','@APPLY_USER',@APPLY_TIME,'@APPR_USER','@DSTATUS','@REASON',@DTIME,'@PDATE','@DDATE',@MTIME)";
		sql=sql.replace("@REQ_ID", reqID);
		sql=sql.replace("@TASK_ID", taskID);
		sql=sql.replace("@LOG_ID", logID);
		sql=sql.replace("@APPLY_USER", applyUser);
		sql=sql.replace("@APPR_USER", apprUser);
		sql=sql.replace("@DSTATUS", status);
		sql=sql.replace("@REASON", reason);
		sql=sql.replace("@PDATE", pdate);
		sql=sql.replace("@DDATE", ddate);
		sql=sql.replace("@APPLY_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@DTIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@MTIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getApproveSet(String logID,String status,String comment){
		String sql="update DELAY_DETAIL set DSTATUS='@DSTATUS',DCOMMENT='@DCOMMENT',MTIME=@MTIME where LOG_ID='@LOG_ID'";
		sql=sql.replace("@DSTATUS", status);
		sql=sql.replace("@DCOMMENT", comment);
		sql=sql.replace("@LOG_ID", logID);
		sql=sql.replace("@MTIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getApprs(String applyUserID,String userID,String status,String keyword){
		String sql="select REQ_ID,BACKLOG.NAME NAME,TASK_ID,LOG_ID,APPLY_USER,APPLY_TIME,APPR_USER,DSTATUS,DELAY_DETAIL.REASON REASON,DCOMMENT,DTIME,PDATE,DDATE,MTIME "+
					"from DELAY_DETAIL,BACKLOG "+
				"WHERE DELAY_DETAIL.REQ_ID=BACKLOG.ID"+
				 " and ('@APPLY_USER' ='' OR DELAY_DETAIL.APPLY_USER='@APPLY_USER')"+
				 " and ('@KEYWORD'='' OR BACKLOG.NAME LIKE '%@KEYWORD%')"+
				 " and DELAY_DETAIL.APPR_USER='@APPR_USER'"+
				 " and DELAY_DETAIL.DSTATUS='@STATUS'";
		sql=sql.replace("@APPLY_USER", applyUserID);
		sql=sql.replace("@KEYWORD", keyword);
		sql=sql.replace("@APPR_USER", userID);
		sql=sql.replace("@STATUS", status);
		return sql;
	}
	
	
	public static String getApproves(String applyUserID,String userID,String status,String keyword){
		String sql="select REQ_ID,BACKLOG.NAME NAME,TASK_ID,LOG_ID,APPLY_USER,APPLY_TIME,APPR_USER,DSTATUS,DELAY_DETAIL.REASON REASON,DCOMMENT,DTIME,PDATE,DDATE,MTIME "+
					"from DELAY_DETAIL,BACKLOG "+
				"WHERE DELAY_DETAIL.REQ_ID=BACKLOG.ID"+
				 " and ('@APPLY_USER' ='' OR DELAY_DETAIL.APPLY_USER='@APPLY_USER')"+
				 " and ('@KEYWORD'='' OR BACKLOG.NAME LIKE '%@KEYWORD%')"+
				 " and DELAY_DETAIL.APPR_USER='@APPR_USER'"+
				 " and DELAY_DETAIL.DSTATUS<>'@STATUS'";
		sql=sql.replace("@APPLY_USER", applyUserID);
		sql=sql.replace("@KEYWORD", keyword);
		sql=sql.replace("@APPR_USER", userID);
		sql=sql.replace("@STATUS", status);
		return sql;
	}
	
	public static String getApplys(String applyUserID,String keyword){
		String sql="select REQ_ID,BACKLOG.NAME NAME,TASK_ID,LOG_ID,APPLY_USER,APPLY_TIME,APPR_USER,DSTATUS,DELAY_DETAIL.REASON REASON,DCOMMENT,DTIME,PDATE,DDATE,MTIME "+
					"from DELAY_DETAIL,BACKLOG "+
				"WHERE DELAY_DETAIL.REQ_ID=BACKLOG.ID"+
				 " and DELAY_DETAIL.APPLY_USER='@APPLY_USER' "+
				 " and ('@KEYWORD'='' OR BACKLOG.NAME LIKE '%@KEYWORD%')";
		sql=sql.replace("@APPLY_USER", applyUserID);
		sql=sql.replace("@KEYWORD", keyword);
		return sql;
	}
	
}
