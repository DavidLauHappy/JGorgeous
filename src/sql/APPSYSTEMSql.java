package sql;

public class APPSYSTEMSql {
	
	 public static String getSelect(){
		 String sql="select   APP,SYS,APP_SYSTEM.SYSNAME SYSNAME,IS_ENABLE,APP_SYSTEM.TIMESPAN TIMESAPN,USERS.USER_NAME USER_NAME, APP_SYSTEM.USER_ID USER_ID,MDF_TIME from APP_SYSTEM,USERS WHERE APP_SYSTEM.USER_ID=USERS.USER_ID";
		 return sql;
	 }
	 
	 public static String getUpt(String app,String system,String timespan,String flag,String userID){
		 String sql="update APP_SYSTEM set IS_ENABLE='@IS_ENABLE' ,TIMESPAN='@TIMESPAN',USER_ID='@USER_ID',MDF_TIME=@MDF_TIME where APP='@APP' and SYS='@SYS'";
		 sql=sql.replace("@APP", app);
		 sql=sql.replace("@SYS", system);
		 sql=sql.replace("@IS_ENABLE", flag);
		 sql=sql.replace("@TIMESPAN", timespan);
		 sql=sql.replace("@USER_ID", userID);
		 sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		 return sql;
	 }
	 
	 public static String getTimeSpan(String app,String systemID){
		 String sql=" select a.TIMESPAN  TIMESPAN from APP_SYSTEM a,SYSTEM b where a.APP='@APP'and a.APP=b.APP and a.SYS=b.ABBR and b.BUSSID='@SYSTEMID'";
		 sql=sql.replace("@APP", app);
		 sql=sql.replace("@SYSTEMID", systemID);
		 return sql;
	 }
}
