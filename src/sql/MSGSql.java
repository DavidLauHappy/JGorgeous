package sql;

public class MSGSql {
	
	
	public static  String getAdd(String userID,String msgID,String text,String mdfUser){
		String sql="insert into MSGS(USER_ID,MSG_ID,MSG,CRT_USER,CRT_TIME,STATUS) "+
						 "values('@USER_ID','@MSG_ID','@MSG','@CRT_USER',@CRT_TIME,'@STATUS')";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@MSG_ID", msgID);
		sql=sql.replace("@MSG", text);
		sql=sql.replace("@CRT_USER", mdfUser);
		sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@STATUS", "0");
		return sql;
	}
}
