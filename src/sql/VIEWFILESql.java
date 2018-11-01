package sql;

public class VIEWFILESql {

	   public static String getAdd(String fileID,String name,String fileTime,String md5,String viewID,String streamID,String crtUser,String uptUser,String version,String fileType){
		   String sql="insert into VIEW_FILE(FILE_ID,FILE_NAME,FILE_TIME,MD5,VIEW_ID,STREAM_ID,CRT_USER,CRT_TIME,MDF_USER,MDF_TIME,VERSION,FILE_TYPE) " +
		   		"values('@FILE_ID','@FILE_NAME','@FILE_TIME','@MD5','@VIEW_ID','@STREAM_ID','@CRT_USER',@CRT_TIME,'@UPT_USER',@UPT_TIME,'@VERSION','@FILE_TYPE')";
		   sql=sql.replace("@FILE_ID", fileID);
		   sql=sql.replace("@FILE_NAME", name);
		   sql=sql.replace("@FILE_TIME", fileTime);
		   sql=sql.replace("@MD5", md5);
		   sql=sql.replace("@VIEW_ID", viewID);
		   sql=sql.replace("@STREAM_ID", streamID);
		   sql=sql.replace("@CRT_USER", crtUser);
		   sql=sql.replace("@UPT_USER", uptUser);
		   sql=sql.replace("@VERSION", version);
		   sql=sql.replace("@FILE_TYPE", fileType);
		   sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		   sql=sql.replace("@CRT_TIME","CONVERT(varchar(100),GETDATE(),120)");
		   return sql;
	   }
	   
	   public static String getAddFileObj(String md5,String loaction,String userID){
		   String sql="INSERT INTO FILES(MD5,LOCATION,CRT_USER,CRT_TIME) "+
				   			"VALUES('@MD5','@LOCATION','@CRT_USER',@CRT_TIME)";
		   sql=sql.replace("@MD5", md5);
		   sql=sql.replace("@LOCATION", loaction);
		   sql=sql.replace("@CRT_USER", userID);
		   sql=sql.replace("@CRT_TIME","CONVERT(varchar(100),GETDATE(),120)");
		   return sql;
	   }
	   
	   public static String checkExist(String md5){
		   String sql="select COUNT(*) AS COUNT from FILES where MD5='@MD5'";
		   sql=sql.replace("@MD5", md5);
		   return sql;
	   }
	   
	   public static String getRemove(String viewID,String version,String fileID){
		   String sql="delete from VIEW_FILE where FILE_ID='@FILE_ID' and VIEW_ID='@VIEW_ID' and  VERSION='@VERSION'";
		   sql=sql.replace("@FILE_ID", fileID);
		   sql=sql.replace("@VIEW_ID", viewID);
		   sql=sql.replace("@VERSION", version);
		   return sql;
	   }
}
