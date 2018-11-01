package sql;

public class GROUPUSERSql {
	
		public static String  getGroupUser(String groupID){
			 String sql="SELECT GROUP_USER.GROUP_ID, "
					 			         +"GROUP_USER.USER_ID,"
					 			         +"USERS.USER_NAME  "
								+"FROM GROUP_USER,USERS "
							+"WHERE GROUP_USER.GROUP_ID='@GROUPID'"
								+"AND GROUP_USER.USER_ID=USERS.USER_ID";
				sql=sql.replace("@GROUPID", groupID);
			 return sql;
		}
}
