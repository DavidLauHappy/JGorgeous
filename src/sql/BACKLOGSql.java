package sql;

public class BACKLOGSql {
		 public static String getReqById(String id){
				String sql="select ID,"
		                                +"SUSER,"
		                                +"DEPT,"
		                                +"SDATE,"
		                                +"IUSER,"
		                                +"NAME,"
		                                +"BACKGROUND,"
		                                +"RDESC,"
		                                +"RDATE,"
		                                +"REASON,"
		                                +"CUSER,"
		                                +"AUSER,"
		                                +"COMMENT,"
		                                +"RSRC,"
		                                +"RTYPE,"
		                                +"RCLASS,"
		                                +"STATUS,"
		                                +"LINK,"
		                                +"CUR_USER,"
		                                +"MDF_TIME,"
		                                +"SYNC_FLAG "
		                       +"from BACKLOG "
		                         +"WHERE ID='@ID'  ";
			sql=sql.replace("@ID", id);
			return sql;
		 }
		 
		 
		 public static String getReqUpt(String id,String status,String currentUserID,String syncflag){
			 String sql="update  BACKLOG set SYNC_FLAG='@SYNC_FLAG',MDF_TIME=@MDF_TIME,CUR_USER='@CUR_USER',STATUS='@STATUS' where  ID='@ID'";
			 sql=sql.replace("@SYNC_FLAG", syncflag);
			 sql=sql.replace("@CUR_USER", currentUserID);
			 sql=sql.replace("@STATUS", status);
			 sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			 sql=sql.replace("@ID", id);
			 return sql;
		 }
		 
		 public static String getDelay(String id){
			 String sql="update  BACKLOG set  SYNC_FLAG='2',MDF_TIME=@MDF_TIME,IS_DELAY='1' where  ID='@ID'";
			 sql=sql.replace("@ID", id);
			 sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			 return sql;
		 }
		 
}
