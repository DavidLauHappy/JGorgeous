package sql;

public class LOCALCOMMANDSql {
	public static String getAdd(String pkgID,String fileID,String nodeID,String id,String name,String parameter,String seq,String type,String status,String remind,String userID,String lpath,String md5,String remote){
		String sql="insert into LOCAL_COMMAND(VER_ID,FILE_ID,NODE_ID,CMD_ID,CMD_NAME,CMD_TEXT,SEQ,CMD_TYPE,STATUS,REMIND,TIME,USER_ID,LPATH,MD5,REMOTE) values"+
						"('@VER_ID','@FILE_ID','@NODE_ID','@CMD_ID','@CMD_NAME','@CMD_TEXT','@SEQ','@CMD_TYPE','@STATUS','@REMIND',@TIME,'@USER_ID','@LPATH','@MD5','@REMOTE')";
		sql=sql.replace("@VER_ID", pkgID);
		sql=sql.replace("@FILE_ID", fileID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@CMD_ID", id);
		sql=sql.replace("@CMD_NAME", name);
		sql=sql.replace("@CMD_TEXT", parameter);
		sql=sql.replace("@SEQ", seq);
		sql=sql.replace("@CMD_TYPE", type);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@REMIND", remind);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@LPATH", lpath);
		sql=sql.replace("@MD5", md5);
		sql=sql.replace("@REMOTE", remote);
		sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getCommand(String versionID,String nodeID){
		String sql="select VER_ID,FILE_ID,NODE_ID,CMD_ID,CMD_NAME,CMD_TEXT,SEQ,CMD_TYPE,STATUS,"+
						  "LOGINFO,REMIND,TIME,USER_ID,LPATH,INSTALLED,MD5,REMOTE  "+
				"from LOCAL_COMMAND "+
					"WHERE VER_ID='@VER_ID' "+
						"AND NODE_ID='@NODE_ID' "+
							"order by CAST (SEQ AS INT)";
		sql=sql.replace("@VER_ID", versionID);
		sql=sql.replace("@NODE_ID", nodeID);
		return sql;
	}
	
	public static String getScheduleCmd(String version){
		String sql="select VER_ID,FILE_ID,NODE_ID,CMD_ID,CMD_NAME,CMD_TEXT,SEQ,CMD_TYPE,STATUS,"+
								  "LOGINFO,REMIND,TIME,USER_ID,LPATH,INSTALLED,MD5,REMOTE "+
						  "from LOCAL_COMMAND "+
							 "WHERE VER_ID='@VER_ID' "+
						         "AND STATUS IN('0','1','7') "+
							 "order by CAST (SEQ AS INT)";
			sql=sql.replace("@VER_ID", version);
			return sql;
	}
	
	public static String getCmd(String cmdID){
		String sql="select VER_ID,FILE_ID,NODE_ID,CMD_ID,CMD_NAME,CMD_TEXT,SEQ,CMD_TYPE,STATUS,"+
								  "LOGINFO,REMIND,TIME,USER_ID,LPATH,INSTALLED,MD5,REMOTE "+
						  "from LOCAL_COMMAND "+
							 "WHERE CMD_ID='@CMD_ID' ";
			sql=sql.replace("@CMD_ID", cmdID);
			return sql;
	}
	
	public static String getCmdRemote(String cmdID,String status,String loginfo,String remind){
		String sql="update LOCAL_COMMAND  set STATUS='@STATUS',LOGINFO='@LOGINFO',REMIND='@REMIND',TIME=@TIME where CMD_ID='@CMD_ID'";
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@CMD_ID", cmdID);
		sql=sql.replace("@LOGINFO", loginfo);
		sql=sql.replace("@REMIND", remind);
		sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getStatusSet(String id,String status){
		String sql="update LOCAL_COMMAND  set STATUS='@STATUS',TIME=@TIME where CMD_ID='@CMD_ID'";
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@CMD_ID", id);
		sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getRemindSet(String id,String status){
		String sql="update LOCAL_COMMAND  set REMIND='@STATUS',TIME=@TIME where CMD_ID='@CMD_ID'";
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@CMD_ID", id);
		sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getInstallSet(String id,String status){
		String sql="update LOCAL_COMMAND  set INSTALLED='@STATUS',TIME=@TIME where CMD_ID='@CMD_ID'";
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@CMD_ID", id);
		sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getCmdCount(String versionID,String nodeID,String status){
		String sql="select  COUNT(*) COUNT FROM"+
						  "from LOCAL_COMMAND "+
							 "WHERE VER_ID='@VER_ID' "+
							 	 "and STATUS='@STATUS' "+
							 	 "and NODE_ID='@NODE_ID'";
			sql=sql.replace("@VER_ID", versionID);
			sql=sql.replace("@NODE_ID", nodeID);
			sql=sql.replace("@STATUS", status);
			return sql;
	}
	
	public static String getStatistic(String versionID,String nodeID){
		String sql=" SELECT COUNT(*) TOTAL, ISNULL(SUM((CASE STATUS WHEN '5' THEN 1  ELSE 0 END)),0) AS DONE "+
						  "FROM LOCAL_COMMAND "+
							 "where VER_ID='@VER_ID'"+
								"and NODE_ID='@NODE_ID'";
			sql=sql.replace("@VER_ID", versionID);
			sql=sql.replace("@NODE_ID", nodeID);
		return sql;
	}
	
	public static String getReminds(String versionID,String node,int fetchRows){
    	String sql="select top @num  VER_ID,FILE_ID,NODE_ID,CMD_ID,CMD_NAME,CMD_TEXT,SEQ,CMD_TYPE,STATUS,"+
				  					"LOGINFO,REMIND,TIME,USER_ID,LPATH,INSTALLED,MD5,REMOTE "+
				  			"from LOCAL_COMMAND "+
				  	"WHERE VER_ID='@VER_ID' "+
				  		"and NODE_ID='@NODE_ID'"+
				  		"and REMIND='1'"+
				  	"ORDER BY CAST(SEQ AS INT) ";
    	sql=sql.replace("@VER_ID", versionID);
    	sql=sql.replace("@NODE_ID", node);
    	sql=sql.replace("@num", fetchRows+"");
		return sql;
    }
	
	public  static String getDelete(String versionID,String nodeID){
		String sql="delete from LOCAL_COMMAND where VER_ID='@VER_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@VER_ID", versionID);
    	sql=sql.replace("@NODE_ID", nodeID);
    	return sql;
	}
}
