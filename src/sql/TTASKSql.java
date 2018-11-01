package sql;

public class TTASKSql {
		public static String getAdd(String viewID,String taskID,String userID,String status,String crtUser){
			 String sql="insert into TTASK_DEF(VIEW_ID,ID,USER_ID,STATUS,CRT_USER,CRT_TIME,UPT_TIME)"+
					 		"values('@VIEW_ID','@ID','@USER_ID','@STATUS','@CRT_USER',@CRT_TIME,@UPT_TIME)";
			 sql=sql.replace("@VIEW_ID", viewID);
			 sql=sql.replace("@ID", taskID);
			 sql=sql.replace("@USER_ID", userID);
			 sql=sql.replace("@STATUS", status);
			 sql=sql.replace("@CRT_USER", crtUser);
			 sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			 sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			 return sql;
		}
		
		public static String getTasks(String viewID){
			String sql="select VIEW_ID,ID,USER_ID,STATUS,CRT_USER,CRT_TIME,UPT_TIME from TTASK_DEF where VIEW_ID='@VIEW_ID' ";
			 sql=sql.replace("@VIEW_ID", viewID);
			return sql;
		}
		
		public static String getMyTasks(String userID){
			String sql="select TTASK_DEF.VIEW_ID VIEW_ID,TTASK_DEF.ID ID,TTASK_DEF.USER_ID USER_ID,TTASK_DEF.STATUS STATUS,TTASK_DEF.CRT_USER CRT_USER,TTASK_DEF.CRT_TIME CRT_TIME,TTASK_DEF.UPT_TIME,VIEWS.VIEW_NAME VIEW_NAME from TTASK_DEF,VIEWS where USER_ID='@USER_ID' and TTASK_DEF.VIEW_ID=VIEWS.VIEW_ID";
			 sql=sql.replace("@USER_ID", userID);
			return sql;
		}
		
		public static String getStatusSet(String taskID,String status,String userID){
			String sql="update  TTASK_DEF   set STATUS='@STATUS', USER_ID='@USER_ID' where ID='@ID' ";
			 sql=sql.replace("@ID", taskID);
			 sql=sql.replace("@STATUS", status);
			 sql=sql.replace("@USER_ID", userID);
			return sql;
		}
}
