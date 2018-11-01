package sql;

public class FUNCSql {
	
	public static String getByID(String id){
		String sql="select ID,NAME,IS_LOG,IS_CHECK FROM FUNC where ID='@ID'";
    	sql=sql.replace("@ID", id);
    	return sql;
	}
}
