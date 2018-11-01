package sql;

public class VIEWSql {

	public static String getNewID(){
		String sql="SELECT NEXT VALUE FOR SEQ_VIEW_ID as ID ";
    	return sql;
	}
	
	public static String getNameExist(String name){
		String sql="SELECT COUNT(*) AS COUNT FROM VIEWS where VIEW_NAME='@VIEW_NAME'";
		sql=sql.replace("@VIEW_NAME", name);
		return sql;
	}
	
	public static String getAdd(String viewID,String name,String desc,String status,String crtUser,String uptUser,String streamID,String app,String rdate,String deadDate,String owner,String verDesc,String uptFlag ){
		String sql="INSERT INTO VIEWS(VIEW_ID,VIEW_NAME,VIEW_DESC,STATUS,CRT_USER,CRT_TIME,UPT_USER,UPT_TIME,STREAM_ID,APP,RDATE,DEAD_DATE,OWNER,VER_DESC,UPT_FLAG) "+
				 "VALUES('@VIEW_ID','@VIEW_NAME','@VIEW_DESC','@STATUS','@CRT_USER',@CRT_TIME,'@UPT_USER',@UPT_TIME,'@STREAM_ID','@APP','@RDATE','@DEAD_DATE','@OWNER','@VER_DESC','@UPT_FLAG')";
		sql=sql.replace("@VIEW_ID", viewID);
		sql=sql.replace("@VIEW_NAME", name);
		sql=sql.replace("@VIEW_DESC", desc);
		sql=sql.replace("@VER_DESC", verDesc);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@CRT_USER", crtUser);
		sql=sql.replace("@UPT_USER", uptUser);
		sql=sql.replace("@STREAM_ID", streamID);
		sql=sql.replace("@APP", app);
		sql=sql.replace("@RDATE", rdate);
		sql=sql.replace("@DEAD_DATE", deadDate);
		sql=sql.replace("@OWNER", owner);
		sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@UPT_FLAG", uptFlag);
		return sql;
	}
	
	public static String getViewVersion(String viewID){
		String sql="SELECT    ISNULL(max(cast(VERSION as int)),0)  ID FROM VIEW_VERSION WHERE VIEW_ID='@VIEW_ID'";
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	}
	
	public static String getViewNextVersion(String viewID){
		String sql="SELECT    ISNULL(max(cast(VERSION as int)),0)+1  ID FROM VIEW_VERSION WHERE VIEW_ID='@VIEW_ID'";
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	}
	
	public static String getAddVersion(String viewID,String desc,String version,String userID){
		String sql="INSERT INTO VIEW_VERSION(VIEW_ID,VIEW_DESC,VERSION,UPT_USER,UPT_TIME) "+
						" VALUES('@VIEW_ID','@VIEW_DESC','@VERSION','@UPT_USER',@UPT_TIME)";
		sql=sql.replace("@VIEW_ID", viewID);
		sql=sql.replace("@VIEW_DESC", desc);
		sql=sql.replace("@VERSION", version);
		sql=sql.replace("@UPT_USER", userID);
		sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getReqRemove(String viewID){
		String sql="delete from VIEW_REQ where VIEW_ID='@VIEW_ID'";
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	}
	
	public static String getAddReq(String viewID,String sno,String userID){
		String sql="insert into VIEW_REQ(VIEW_ID,SNO,MDF_USER,MDF_TIME) "+
					 	"values('@VIEW_ID','@SNO','@MDF_USER',@MDF_TIME)";
		sql=sql.replace("@VIEW_ID", viewID);
		sql=sql.replace("@SNO", sno);
		sql=sql.replace("@MDF_USER", userID);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getReqDateSet(String viewID,String date){
		String sql="update BACKLOG SET ONLINE_DATE='@ONLINE_DATE' , MDF_TIME=@MDF_TIME WHERE ID "+
						"IN(SELECT REQ_ID FROM VIEW_REQ,TASK_DEF WHERE VIEW_ID='@VIEW_ID' AND VIEW_REQ.SNO=TASK_DEF.ID )";
		sql=sql.replace("@ONLINE_DATE", date);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	
	}
	
	public static String getSetStream(String viewID,String streamID){
		String sql="update  VIEWS  set STREAM_ID='@STREAM_ID', UPT_TIME=@UPT_TIME where VIEW_ID='@VIEW_ID'";
		sql=sql.replace("@STREAM_ID", streamID);
		sql=sql.replace("@UPT_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	}
	
	public static String getSetFlag(String viewID,String falg){
		String sql="update  VIEWS  set UPT_FLAG='@UPT_FLAG', UPT_TIME=@UPT_TIME where VIEW_ID='@VIEW_ID'";
		sql=sql.replace("@UPT_FLAG", falg);
		sql=sql.replace("@UPT_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	}
	
	public static String getSetTowner(String viewID,String towner){
		String sql="update  VIEWS  set TOWNER='@TOWNER', UPT_TIME=@UPT_TIME where VIEW_ID='@VIEW_ID'";
		sql=sql.replace("@TOWNER", towner);
		sql=sql.replace("@UPT_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	}
	
	public static String getNextProgress(String viewID,String progress,String userID,String mdfUser,String lastProgress){
		String sql="update  VIEWS  set PROGRESS='@PROGRESS',LST_PROGRESS='@LST_PROGRESS',CUR_USERID='@CUR_USERID',  UPT_TIME=@UPT_TIME, UPT_USER='@UPT_USER' where VIEW_ID='@VIEW_ID'";
		sql=sql.replace("@PROGRESS", progress);
		sql=sql.replace("@CUR_USERID", userID);
		sql=sql.replace("@LST_PROGRESS", lastProgress);
		sql=sql.replace("@UPT_USER", mdfUser);
		sql=sql.replace("@UPT_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	}
	
	public static String getAllViews(){
		String sql="select  VIEW_ID,VIEW_NAME,VIEW_DESC,STATUS,CRT_USER,CRT_TIME,UPT_USER,UPT_TIME,STREAM_ID,"+
						"APP,RDATE,DEAD_DATE,OWNER,TOWNER, PROGRESS,CUR_USERID,LST_PROGRESS,UPT_FLAG,VER_DESC "+
						"from VIEWS where STATUS<>'@STATUS' order by UPT_TIME";
		sql=sql.replace("@STATUS", "2");
		return sql;
	}
	
	public static String getViews(){
		String sql="select  VIEW_ID,VIEW_NAME,VIEW_DESC,STATUS,CRT_USER,CRT_TIME,UPT_USER,UPT_TIME,STREAM_ID,"+
						"APP,RDATE,DEAD_DATE,OWNER,TOWNER,PROGRESS,CUR_USERID,LST_PROGRESS,UPT_FLAG,VER_DESC  "+
						"from VIEWS  order by UPT_TIME";
		return sql;
	}
	
	public static String getViewByID(String viewID){
		String sql="select  VIEW_ID,VIEW_NAME,VIEW_DESC,STATUS,CRT_USER,CRT_TIME,UPT_USER,UPT_TIME,STREAM_ID,"+
				"APP,RDATE,DEAD_DATE,OWNER,TOWNER,PROGRESS,CUR_USERID,LST_PROGRESS,UPT_FLAG,VER_DESC  "+
				"from VIEWS  where VIEW_ID='@VIEW_ID'";
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	}
	
	public static String getStatusSet(String id,String status,String mdfUser){
		String sql="update  VIEWS  set STATUS='@STATUS',  UPT_TIME=@UPT_TIME, UPT_USER='@UPT_USER' where VIEW_ID='@VIEW_ID'";
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@UPT_USER", mdfUser);
		sql=sql.replace("@UPT_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@VIEW_ID", id);
		return sql;
	}
	
	public static String getVersionFiles(String viewID,String version){
		String sql="select VIEW_FILE.FILE_NAME FILE_NAME,FILES.LOCATION LOCATION,FILES.MD5 MD5,VIEW_FILE.MDF_TIME MDF_TIME,"+
									"VIEW_FILE.MDF_USER MDF_USER,VIEW_FILE.CRT_TIME CRT_TIME,VIEW_FILE.CRT_USER CRT_USER,"+
									"VIEW_FILE.VIEW_ID VIEW_ID,VIEWS.VIEW_NAME VIEW_NAME,VIEW_FILE.STREAM_ID STREAM_ID,"+
									"STREAM.STREAM_NAME STREAM_NAME,FILE_ID FILE_ID,VIEW_FILE.FILE_TIME  FILE_TIME  "+
						"from VIEW_FILE,FILES,VIEWS, STREAM "+
						"where VIEW_FILE.VIEW_ID='@VIEW_ID' "+
						 "and VIEW_FILE.VIEW_ID=VIEWS.VIEW_ID "+
						 "and VIEW_FILE.STREAM_ID=STREAM.STREAM_ID "+
						"and	VIEW_FILE.VERSION='@VERSION' "+
						"and VIEW_FILE.MD5=FILES.MD5 "+
						"and VIEW_FILE.FILE_TYPE='0'";
		sql=sql.replace("@VIEW_ID", viewID);
		sql=sql.replace("@VERSION", version);
		return sql;
	}
	
	public static String getVersionAttachs(String viewID,String version){
		String sql="select VIEW_FILE.FILE_NAME FILE_NAME,FILES.LOCATION LOCATION,FILES.MD5 MD5,VIEW_FILE.MDF_TIME MDF_TIME,"+
									"VIEW_FILE.MDF_USER MDF_USER,VIEW_FILE.CRT_TIME CRT_TIME,VIEW_FILE.CRT_USER CRT_USER,"+
									"VIEW_FILE.VIEW_ID VIEW_ID,VIEWS.VIEW_NAME VIEW_NAME,VIEW_FILE.STREAM_ID STREAM_ID,"+
									"STREAM.STREAM_NAME STREAM_NAME,FILE_ID FILE_ID,VIEW_FILE.FILE_TIME  FILE_TIME  "+
						"from VIEW_FILE,FILES,VIEWS, STREAM "+
						"where VIEW_FILE.VIEW_ID='@VIEW_ID' "+
						 "and VIEW_FILE.VIEW_ID=VIEWS.VIEW_ID "+
						 "and VIEW_FILE.STREAM_ID=STREAM.STREAM_ID "+
						"and	VIEW_FILE.VERSION='@VERSION' "+
						"and VIEW_FILE.MD5=FILES.MD5 "+
						"and VIEW_FILE.FILE_TYPE='1'";
		sql=sql.replace("@VIEW_ID", viewID);
		sql=sql.replace("@VERSION", version);
		return sql;
	}
	
	public static String getVersions(String viewID){
		String sql="select VIEW_VERSION.VIEW_ID VIEW_ID,VIEWS.VIEW_NAME VIEW_NAME,VIEW_VERSION.VIEW_DESC VIEW_DESC,"+
						"VIEW_VERSION.VERSION VERSION,VIEWS.STREAM_ID STREAM_ID,VIEW_VERSION.UPT_USER UPT_USER, "+
						"VIEW_VERSION.UPT_TIME UPT_TIME from VIEW_VERSION,VIEWS "+
						"where VIEW_VERSION.VIEW_ID='@VIEW_ID'  "+
						  	"and VIEW_VERSION.VIEW_ID=VIEWS.VIEW_ID"+
						  	" order by cast(VIEW_VERSION.VERSION as int) desc ";
		sql=sql.replace("@VIEW_ID", viewID);
		return sql;
	}
	
	public static String getFlagSet(String reqSno,String flag,String desc){
		String sql="update VIEWS  SET UPT_FLAG='@FLAG',VER_DESC='@VER_DESC' WHERE exists(SELECT 1 FROM VIEW_REQ WHERE VIEW_REQ.VIEW_ID=VIEWS.VIEW_ID AND SNO='@REQ')";
				 sql=sql.replace("@REQ", reqSno);
		 		sql=sql.replace("@FLAG", flag);
		 		sql=sql.replace("@VER_DESC", desc);
		return sql;
	}

}
