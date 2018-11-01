package sql;

public class STREAMSql {
	
	 public static String getStreamByStatus(String status){
		 String sql="SELECT STREAM_ID,STREAM_NAME,STREAM_DESC,STATUS,UPT_USER,UPT_TIME,DATE_START,DATE_END,STREAM_CODE FROM STREAM WHERE STATUS='@STATUS' ORDER BY UPT_TIME";
		 			sql=sql.replace("@STATUS", status);
		 return sql;
	 }
	 
	 public static String getStream(String id){
		String sql="select STREAM_ID,STREAM_NAME,STREAM_DESC,STATUS,UPT_USER,UPT_TIME,DATE_START,DATE_END,STREAM_CODE from STREAM where STREAM_ID='@STREAM_ID'";
		 sql=sql.replace("@STREAM_ID", id);
		 return sql;
	 }
	 
	 
	 public static String getStreams(){
			String sql="select STREAM_ID,STREAM_NAME,STREAM_DESC,STATUS,UPT_USER,UPT_TIME,DATE_START,DATE_END,STREAM_CODE from STREAM";
			 return sql;
		 }
	 
	 public static String getNameExist(String name){
		 String sql="select  count(*) as COUNT from STREAM where STREAM_NAME='@STREAM_NAME'";
		 sql=sql.replace("@STREAM_NAME", name);
		 return sql;
	 }
	 
	 public static String getNewID(){
		 String sql="select  NEXT VALUE FOR SEQ_STREAM_ID as ID";
		 return sql;
	 }
	 
	 public static String getAdd(String id,String name,String desc,String status,String userID){
		 String sql="insert into STREAM(STREAM_ID,STREAM_NAME,STREAM_DESC,STATUS,UPT_USER,UPT_TIME) "+
				  		 "values('@STREAM_ID','@STREAM_NAME','@STREAM_DESC','@STATUS','@UPT_USER',@UPT_TIME)";
		 sql=sql.replace("@STREAM_ID", id);
		 sql=sql.replace("@STREAM_NAME", name);
		 sql=sql.replace("@STREAM_DESC", desc);
		 sql=sql.replace("@STATUS", status);
		 sql=sql.replace("@UPT_USER", userID);
		 sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		 return sql;
	 }
	 
	 public static String getStatusSet(String id,String status,String mdfUser){
		 String sql="update STREAM set STATUS='@STATUS',UPT_USER='@UPT_USER',UPT_TIME=@UPT_TIME where STREAM_ID='@STREAM_ID'";
		 sql=sql.replace("@STREAM_ID", id);
		 sql=sql.replace("@STATUS", status);
		 sql=sql.replace("@UPT_USER", mdfUser);
		 sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		 return sql;
	 
	 }
	 
	 public static String getViews(String streamID){
			String sql="select  VIEW_ID,VIEW_NAME,VIEW_DESC,STATUS,CRT_USER,CRT_TIME,UPT_USER,UPT_TIME,STREAM_ID,"+
							"APP,RDATE,DEAD_DATE,OWNER,PROGRESS,CUR_USERID "+
							"from VIEWS where STREAM_ID='@STREAM_ID' and STATUS<>'@STATUS' order by UPT_TIME";
			sql=sql.replace("@STATUS", "2");
			sql=sql.replace("@STREAM_ID", streamID);
			return sql;
		}
	 
}
