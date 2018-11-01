package sql;

public class GROUPSql {
	public static String getGroups(){
		 String sql="select GROUP_ID,GROUP_NAME from GROUPS ORDER BY GROUP_ID";
		return sql;
	}
	
	public static String getAdd(String groupID,String groupName,String type,String mdfUser){
		 String sql="insert into GROUPS(GROUP_ID,GROUP_NAME,TYPE,UPT_USER, UPT_TIME) values('@GROUP_ID','@GROUP_NAME','@TYPE','@UPT_USER', @UPT_TIME)";
		sql=sql.replace("@GROUP_ID", groupID);
		sql=sql.replace("@GROUP_NAME", groupName);
		sql=sql.replace("@UPT_USER", mdfUser);
		sql=sql.replace("@TYPE", type);
		sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		 return sql;
	}

	public static String getDelete(String groupID){
		 String sql="delete from  GROUPS where GROUP_ID='@GROUP_ID'";
		 sql=sql.replace("@GROUP_ID", groupID);
		return sql;
	}
	
	 public static String getMaxID(){
		  String sql="select ISNULL(max(cast(GROUP_ID as int)),0)+1 num  from GROUPS";
		   return sql;
	 }
	 
	 public static String getGroupExist(String name){
		 String sql="select COUNT(*) AS COUNT from  GROUPS where GROUP_NAME='@GROUP_NAME'";
		 sql=sql.replace("@GROUP_NAME", name);
		return sql;
	}
}
