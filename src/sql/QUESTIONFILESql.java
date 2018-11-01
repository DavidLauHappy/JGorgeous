package sql;

public class QUESTIONFILESql {
	
	public static String getAdd(String qid,String fileid,String filename,String fileTime,String md5,String crtUser,String fileType){
		 String sql="insert into QUESTION_FILE(QID,FILE_ID,FILE_NAME,FILE_TIME,MD5,CRT_USER,CRT_TIME,QFILE_TYPE) values"+
				"('@QID','@FILE_ID','@FILE_NAME','@FILE_TIME','@MD5','@CRT_USER',@CRT_TIME,'@QFILE_TYPE')";
				sql=sql.replace("@QID", qid);
				sql=sql.replace("@FILE_ID", fileid);
				sql=sql.replace("@FILE_NAME", filename);
				sql=sql.replace("@FILE_TIME", fileTime);
				sql=sql.replace("@MD5", md5);
				sql=sql.replace("@CRT_USER", crtUser);
				sql=sql.replace("@CRT_TIME","CONVERT(varchar(100),GETDATE(),120)");
				sql=sql.replace("@QFILE_TYPE", fileType);
				return sql;
		}
	
	
	
	
	public static String getDelete(String qid,String fileID){
		String sql="delete from QUESTION_FILE where FILE_ID='@FILE_ID' and QID='@QID'";
		sql=sql.replace("@QID", qid);
		sql=sql.replace("@FILE_ID", fileID);
		return sql;
	}
	
	public static String getInsertFile(String md5,String path,String crtUser){
		String sql="insert into FILES(MD5,LOCATION,CRT_USER,CRT_TIME )values('@MD5','@LOCATION','@CRT_USER',@CRT_TIME)";
		sql=sql.replace("@MD5", md5);
		sql=sql.replace("@LOCATION", path);
		sql=sql.replace("@CRT_USER", crtUser);
		sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getFiles(String qID){
		String sql="select QID,FILE_ID,FILE_NAME,FILE_TIME,FILES.MD5 MD5,QUESTION_FILE.CRT_USER CRT_USER,QUESTION_FILE.CRT_TIME CRT_TIME,QFILE_TYPE,FILES.LOCATION LOCATION "+
								"from FILES,QUESTION_FILE "+
							"WHERE QUESTION_FILE.QID='@QID' "+
							    "and  QUESTION_FILE.MD5=FILES.MD5";
		sql=sql.replace("@QID", qID);
		return sql;
	}
}
