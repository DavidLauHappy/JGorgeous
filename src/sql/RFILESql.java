package sql;

public class RFILESql {
	
	public static String getFileExist(String md5){
		String sql="select COUNT(*) AS COUNT from FILES where MD5='@MD5'";
		sql=sql.replace("@MD5", md5);
		return sql;
	}
	
}
