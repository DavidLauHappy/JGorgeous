package sql;

public class DATAFLAGSql {

	public static String getByID(String id){
		String sql="SELECT ID,FLAG,MDF_TIME FROM DATA_FLAG WHERE ID='@ID'";
		sql=sql.replace("@ID", id);
    	return sql;
	}
	
	public static String getUpdateSql(String id,String flag){
		String sql="update  DATA_FLAG set FLAG='@FLAG',MDF_TIME=@MDF_TIME WHERE ID='@ID'";
		sql=sql.replace("@ID", id);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getInsertSql(String id,String flag){
		String sql="insert into DATA_FLAG(ID,FLAG,MDF_TIME )  values('@ID','@FLAG',@MDF_TIME)";
		sql=sql.replace("@ID", id);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
}
