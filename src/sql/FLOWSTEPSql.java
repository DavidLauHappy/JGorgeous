package sql;

public class FLOWSTEPSql {
	
	  public static String getStep(String flowID,String stepID){
		  String sql="select  FLOW_ID,ID,NAME,APPRER_ID,APPRER_TYPE,NEXT_ID from  FLOW_STEP where FLOW_ID='@FLOW_ID' and ID='@ID'";
		   sql=sql.replace("@FLOW_ID", flowID);
		   sql=sql.replace("@ID", stepID);
	       return sql;
	  }
	  
	  public static String getSteps(String flowID){
		  String sql="select  FLOW_ID,ID,NAME,APPRER_ID,APPRER_TYPE,NEXT_ID from  FLOW_STEP where FLOW_ID='@FLOW_ID' ORDER BY ID";
		   sql=sql.replace("@FLOW_ID", flowID);
	       return sql;
	  }
}
