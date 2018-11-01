package sql;

public class FLOWAPPROVERSql {
	public static String getAdd(String apprID,String userID,String flowID,String stepID,String mdfUser){
		 String sql="insert into FLOW_APPROVER(APPR_ID,USER_ID,FLOW_ID,STEP_ID,MDF_TIME,MDF_INFO) "+
				 		  "values('@APPR_ID','@USER_ID','@FLOW_ID','@STEP_ID',@MDF_TIME,'@MDF_INFO')";
		 sql=sql.replace("@APPR_ID", apprID);
		 sql=sql.replace("@USER_ID", userID);
		 sql=sql.replace("@FLOW_ID", flowID);
		 sql=sql.replace("@STEP_ID", stepID);
		 sql=sql.replace("@MDF_INFO", mdfUser);
		 sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		 return sql;
	}
	
	
}
