package sql;

public class USERNODESql {

	public static String getExistByID(String nodeId,String userID){
		String sql="SELECT COUNT(*) AS COUNT FROM USER_NODE WHERE USER_ID='@USER_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@NODE_ID", nodeId);
    	return sql;
	}
	
	public static String getAddData(String userID,String nodeID,String status,String seq,String type,String schFlag,String os){
		String sql="INSERT INTO USER_NODE(USER_ID,NODE_ID,STATUS,SEQ,TYPE,SCH_FLAG,OS,MDF_TIME) "+
	                     "VALUES('@USER_ID','@NODE_ID','@STATUS','@SEQ','@TYPE','@SCH_FLAG','@OS',@MDF_TIME)";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@SEQ", seq);
		sql=sql.replace("@TYPE", type);
		sql=sql.replace("@SCH_FLAG", schFlag);
		sql=sql.replace("@OS", os);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getDelete(String userID,String nodeID){
		String sql="delete from  USER_NODE   WHERE USER_ID='@USER_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@NODE_ID", nodeID);
    	return sql;
	}
	
	public static String getUptBasicInfo(String userID,String nodeID,String seq,String os,String sPort){
		String sql="update USER_NODE SET SEQ='@SEQ',OS='@OS',SPORT='@SPORT',MDF_TIME=@MDF_TIME WHERE USER_ID='@USER_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@SEQ", seq);
		sql=sql.replace("@OS", os);
		sql=sql.replace("@SPORT", sPort);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	
	public static String getUptDBInfo(String userID,String nodeID,String dbType,String dbuser,String dbpasswd,String dbname,String backdbname,String port){
		String sql="update USER_NODE SET DBTYPE='@DBTYPE',DBUSER='@DBUSER',DBPASSWD='@DBPASSWD',DBNAME='@DBNAME',BACKDBNAME='@BACKDBNAME', DBPORT='@DBPORT',MDF_TIME=@MDF_TIME WHERE USER_ID='@USER_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@DBTYPE", dbType);
		sql=sql.replace("@DBUSER", dbuser);
		sql=sql.replace("@DBPASSWD", dbpasswd);
		sql=sql.replace("@DBNAME", dbname);
		sql=sql.replace("@BACKDBNAME", backdbname);
		sql=sql.replace("@DBPORT", port);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getUptSftpInfo(String userID,String nodeID,String sftpuser,String sftppasswd,String port,String dir){
		String sql="update USER_NODE SET SFTPUSER='@SFTPUSER',SFTPPASSWD='@SFTPPASSWD',SFTPDIR='@SFTPDIR',SFTPPORT='@SFTPPORT',MDF_TIME=@MDF_TIME WHERE USER_ID='@USER_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@SFTPUSER", sftpuser);
		sql=sql.replace("@SFTPPASSWD", sftppasswd);
		sql=sql.replace("@SFTPDIR", dir);
		sql=sql.replace("@SFTPPORT", port);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getUptNodeStatus(String userID,String nodeID,String status){
		String sql="update USER_NODE SET STATUS='@STATUS',MDF_TIME=@MDF_TIME WHERE USER_ID='@USER_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getUptNodeSchStatus(String userID,String nodeID,String status){
		String sql="update USER_NODE SET SCH_FLAG='@SCH_FLAG',MDF_TIME=@MDF_TIME WHERE USER_ID='@USER_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@SCH_FLAG", status);
		sql=sql.replace("@MDF_TIME",  "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	
}
