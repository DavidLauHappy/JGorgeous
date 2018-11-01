package sql;

public class SYSPARAMETERSql {
	public static String getAllParameter(){
		String sql="select NAME,VALUE,[DESC],MDF_USER,MDF_TIME from  SYS_PARAMETER order by MDF_TIME";
    	return sql;
	}
	
	public static String getUpdate(String name,String value,String mdfUser){
		String sql="update SYS_PARAMETER set VALUE='@VALUE',MDF_TIME=@MDF_TIME, MDF_USER='@MDF_USER' where NAME='@NAME'";
		sql=sql.replace("@NAME", name);
		sql=sql.replace("@VALUE", value);
		sql=sql.replace("@MDF_USER", mdfUser);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	
}
