package sql;

public class ROLESql {
	
	public static String getRoles(){
		 String sql="select  ID,NAME from ROLES  ORDER BY ID";
		return sql;
	}
	
	
	
}
