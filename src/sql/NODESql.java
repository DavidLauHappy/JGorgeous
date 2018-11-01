package sql;

public class NODESql {
	
	public static String getInsert(String id,String ip,String name,String os,String cluster,String componentID,String systemID,String mdfUser,String flag){
		String sql="insert into NODE(ID,IP,NAME,OS,CLUSTER,COMPONET_ID,SYSTEM_ID,MDF_USER,MDF_TIME,FLAG)  values('@ID','@IP','@NAME','@OS','@CLUSTER','@COMPONET_ID','@SYSTEM_ID','@MDF_USER',@MDF_TIME,'@FLAG')";
		sql=sql.replace("@ID", id);
		sql=sql.replace("@IP", ip);
		sql=sql.replace("@NAME", name);
		sql=sql.replace("@OS", os);
		sql=sql.replace("@CLUSTER", cluster);
		sql=sql.replace("@SYSTEM_ID",systemID);
		sql=sql.replace("@COMPONET_ID", componentID);
		sql=sql.replace("@MDF_USER", mdfUser);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getDeleteByFlag(String flag){
		String sql="delete  NODE where FLAG='@FLAG'";
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	public static String getQueryByComponent(String componentID,String flag){
		String sql="select ID,IP,NAME,[STATUS],DBUSER,DBPASSWD,DBNAME,BACKDBNAME,DBPORT,SFTPUSER,SFTPPASSWD,SFTPDIR,SFTPPORT,SEQ,[TYPE],SCH_FLAG,OS,CLUSTER,COMPONET_ID,SYSTEM_ID,MDF_USER,MDF_TIME,FLAG "+
						"from  NODE where COMPONET_ID='@COMPONET_ID' and FLAG in('@FLAG','C') ORDER BY NAME";
		sql=sql.replace("@COMPONET_ID", componentID);
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	public static String getMyClusterNode(String userID,String componentID,String cluster,String flag){
		String sql="select NODE.ID ID,NODE.IP IP,NODE.NAME NAME,"+
									"USER_NODE.STATUS STATUS,USER_NODE.DBUSER DBUSER,USER_NODE.DBPASSWD DBPASSWD,USER_NODE.DBNAME DBNAME,USER_NODE.BACKDBNAME BACKDBNAME,USER_NODE.DBPORT DBPORT,"+
									"USER_NODE.SFTPUSER SFTPUSER,USER_NODE.SFTPPASSWD SFTPPASSWD,USER_NODE.SFTPDIR SFTPDIR,USER_NODE.SFTPPORT SFTPPORT,"+
									"USER_NODE.SEQ SEQ,USER_NODE.TYPE TYPE,USER_NODE.SCH_FLAG SCH_FLAG,USER_NODE.OS OS,USER_NODE.SPORT SPORT,"+
									"NODE.CLUSTER CLUSTER,NODE.COMPONET_ID COMPONET_ID,NODE.SYSTEM_ID SYSTEM_ID,NODE.MDF_USER MDF_USER,NODE.MDF_TIME MDF_TIME,NODE.FLAG FLAG,USER_NODE.DBTYPE  DBTYPE "+
									"from NODE,USER_NODE where NODE.COMPONET_ID='@COMPONET_ID'  and NODE.FLAG in('@FLAG','C') and NODE.CLUSTER='@CLUSTER' and USER_NODE.USER_ID='@USER_ID' and NODE.ID=USER_NODE.NODE_ID  ORDER BY cast(USER_NODE.SEQ as int)";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@COMPONET_ID", componentID);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@CLUSTER", cluster);
		return sql;
	}
	
	public static String getSystemNode(String userID,String systemID,String flag){
		String sql="select NODE.ID ID,NODE.IP IP,NODE.NAME NAME,"+
									"USER_NODE.STATUS STATUS,USER_NODE.DBUSER DBUSER,USER_NODE.DBPASSWD DBPASSWD,USER_NODE.DBNAME DBNAME,USER_NODE.BACKDBNAME BACKDBNAME,USER_NODE.DBPORT DBPORT,"+
									"USER_NODE.SFTPUSER SFTPUSER,USER_NODE.SFTPPASSWD SFTPPASSWD,USER_NODE.SFTPDIR SFTPDIR,USER_NODE.SFTPPORT SFTPPORT,"+
									"USER_NODE.SEQ SEQ,USER_NODE.TYPE TYPE,USER_NODE.SCH_FLAG SCH_FLAG,USER_NODE.OS OS,USER_NODE.SPORT SPORT,"+
									"NODE.CLUSTER CLUSTER,NODE.COMPONET_ID COMPONET_ID,NODE.SYSTEM_ID SYSTEM_ID,NODE.MDF_USER MDF_USER,NODE.MDF_TIME MDF_TIME,NODE.FLAG FLAG,USER_NODE.DBTYPE  DBTYPE "+
									"from NODE,USER_NODE where NODE.SYSTEM_ID='@SYSTEM_ID'  and NODE.FLAG in('@FLAG','C')  and USER_NODE.USER_ID='@USER_ID' and NODE.ID=USER_NODE.NODE_ID  ORDER BY cast(USER_NODE.SEQ as int)";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@FLAG", flag);
		return sql;
	}
	
	public static  String getUptTag(String id,String ip,String name,String flag,String user){
		String sql="update  NODE  SET IP='@IP',NAME='@NAME',MDF_USER='@MDF_USER', MDF_TIME=@MDF_TIME where FLAG='@FLAG' and ID='@ID'";
		sql=sql.replace("@ID", id);
		sql=sql.replace("@IP", ip);
		sql=sql.replace("@NAME", name);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@MDF_USER", user);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String nodeSeqExists(String systemID,String seq,String userID,String flag){
		String sql="select COUNT(*) COUNT from NODE,USER_NODE where NODE.ID=USER_NODE.NODE_ID and  USER_NODE.USER_ID='@USER_ID'  and USER_NODE.SEQ='@SEQ' and NODE.SYSTEM_ID='@SYSTEM_ID' and NODE.FLAG in('@FLAG','C') ";
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@SEQ", seq);
		return sql;
	}
	
	public static String nodeIpExists(String ip,String userID,String flag){
		String sql="select COUNT(*) COUNT  from NODE,USER_NODE where NODE.ID=USER_NODE.NODE_ID and  USER_NODE.USER_ID='@USER_ID'  and NODE.IP='@IP'  and NODE.FLAG in('@FLAG','C') ";
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@IP", ip);
		return sql;
	}
	
	public static String getNodeByID(String userID,String id,String flag){
		String sql="select  NODE.ID ID,NODE.IP IP,NODE.NAME NAME,"+
						 "USER_NODE.STATUS STATUS,USER_NODE.DBUSER DBUSER,USER_NODE.DBPASSWD DBPASSWD,USER_NODE.DBNAME DBNAME,USER_NODE.BACKDBNAME BACKDBNAME,USER_NODE.DBPORT DBPORT,USER_NODE.SPORT SPORT,"+
						 "USER_NODE.SFTPUSER SFTPUSER,USER_NODE.SFTPPASSWD SFTPPASSWD,USER_NODE.SFTPDIR SFTPDIR,USER_NODE.SFTPPORT SFTPPORT,USER_NODE.SEQ SEQ,USER_NODE.TYPE TYPE,USER_NODE.SCH_FLAG SCH_FLAG,USER_NODE.OS OS,"+
				          "NODE.CLUSTER CLUSTER,NODE.COMPONET_ID COMPONET_ID,NODE.SYSTEM_ID SYSTEM_ID,NODE.MDF_USER MDF_USER,USER_NODE.MDF_TIME MDF_TIME,NODE.FLAG FLAG,USER_NODE.DBTYPE  DBTYPE "+
						 "from NODE,USER_NODE where NODE.ID=USER_NODE.NODE_ID and USER_NODE.USER_ID='@USER_ID'  and NODE.ID='@ID' and NODE.FLAG in('@FLAG','C')";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@ID", id);
		return sql;
	}
	
	public static String getNodeStatus(String userID,String id,String flag){
		String sql="select USER_NODE.STATUS STATUS "+
						 "from NODE,USER_NODE where NODE.ID=USER_NODE.NODE_ID and USER_NODE.USER_ID='@USER_ID'  and NODE.ID='@ID' and NODE.FLAG in('@FLAG','C')";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@ID", id);
		return sql;
	}
	
	
	public static String getNodeByUserID(String userID,String flag){
		String sql="select  NODE.ID ID,NODE.IP IP,NODE.NAME NAME,"+
						 "USER_NODE.STATUS STATUS,USER_NODE.DBUSER DBUSER,USER_NODE.DBPASSWD DBPASSWD,USER_NODE.DBNAME DBNAME,USER_NODE.BACKDBNAME BACKDBNAME,USER_NODE.DBPORT DBPORT,USER_NODE.SPORT SPORT,"+
						 "USER_NODE.SFTPUSER SFTPUSER,USER_NODE.SFTPPASSWD SFTPPASSWD,USER_NODE.SFTPDIR SFTPDIR,USER_NODE.SFTPPORT SFTPPORT,USER_NODE.SEQ SEQ,USER_NODE.TYPE TYPE,USER_NODE.SCH_FLAG SCH_FLAG,USER_NODE.OS OS,"+
				          "NODE.CLUSTER CLUSTER,NODE.COMPONET_ID COMPONET_ID,NODE.SYSTEM_ID SYSTEM_ID,NODE.MDF_USER MDF_USER,USER_NODE.MDF_TIME MDF_TIME,NODE.FLAG FLAG,USER_NODE.DBTYPE  DBTYPE "+
						 "from NODE,USER_NODE where NODE.ID=USER_NODE.NODE_ID and USER_NODE.USER_ID='@USER_ID' and NODE.FLAG in('@FLAG','C')  ORDER BY cast(USER_NODE.SEQ as int)";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@FLAG", flag);
		return sql;
	}
	
	
	//这里不要筛选系统，保证切换系统的版本能使用已经建立的连接
	public static String getUserIpNode(String userID,String systemID,String ip,String flag){
		String sql="select  NODE.ID ID,NODE.IP IP,NODE.NAME NAME,"+
						 "USER_NODE.STATUS STATUS,USER_NODE.DBUSER DBUSER,USER_NODE.DBPASSWD DBPASSWD,USER_NODE.DBNAME DBNAME,USER_NODE.BACKDBNAME BACKDBNAME,USER_NODE.DBPORT DBPORT,USER_NODE.SPORT SPORT,"+
						 "USER_NODE.SFTPUSER SFTPUSER,USER_NODE.SFTPPASSWD SFTPPASSWD,USER_NODE.SFTPDIR SFTPDIR,USER_NODE.SFTPPORT SFTPPORT,USER_NODE.SEQ SEQ,USER_NODE.TYPE TYPE,USER_NODE.SCH_FLAG SCH_FLAG,USER_NODE.OS OS,"+
				          "NODE.CLUSTER CLUSTER,NODE.COMPONET_ID COMPONET_ID,NODE.SYSTEM_ID SYSTEM_ID,NODE.MDF_USER MDF_USER,USER_NODE.MDF_TIME MDF_TIME,NODE.FLAG FLAG,USER_NODE.DBTYPE  DBTYPE "+
						 "from NODE,USER_NODE where NODE.ID=USER_NODE.NODE_ID and NODE.IP='@IP'  and USER_NODE.USER_ID='@USER_ID' and NODE.FLAG in('@FLAG','C')  ORDER BY cast(USER_NODE.SEQ as int)";//and NODE.SYSTEM_ID='@SYSTEM_ID'
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@IP", ip);
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@FLAG", flag);
		return sql;
	}
	
	public static String getDirs(String nodeID){
		   String sql="select NODE_ID,DIR_NAME,DIR_VALUE,DIR_FILTER,MDF_USER,MDF_TIME  from NODE_DIR where  NODE_ID='@NODE_ID'  ORDER BY DIR_NAME";
			sql=sql.replace("@NODE_ID", nodeID);
			return sql;
	}
	
	public static String getInsertDir(String nodeID,String dirName,String dirVal,String filter,String userID){
		String sql="insert into NODE_DIR(NODE_ID,DIR_NAME,DIR_VALUE,DIR_FILTER,MDF_USER,MDF_TIME) "+
						"values('@NODE_ID','@DIR_NAME','@DIR_VALUE','@DIR_FILTER','@MDF_USER',@MDF_TIME )";
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@DIR_NAME", dirName);
		sql=sql.replace("@DIR_VALUE", dirVal);
		sql=sql.replace("@DIR_FILTER", filter);
		sql=sql.replace("@MDF_USER", userID);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getDirDelete(String nodeID){
		 String sql="delete  from NODE_DIR where  NODE_ID='@NODE_ID'";
			sql=sql.replace("@NODE_ID", nodeID);
			return sql;
	}
	
	public static String getServiceDelete(String nodeID){
		 String sql="delete  from NODE_SERVICE where  NODE_ID='@NODE_ID'";
			sql=sql.replace("@NODE_ID", nodeID);
			return sql;
	}
	
	public static String getInsertService(String nodeID,String svcName,String start,String stop,String userID){
		String sql="insert into NODE_SERVICE(NODE_ID,TYPE,START,STOP,MDF_USER,MDF_TIME) "+
						"values('@NODE_ID','@TYPE','@START','@STOP','@MDF_USER',@MDF_TIME )";
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@TYPE", svcName);
		sql=sql.replace("@START", start);
		sql=sql.replace("@STOP", stop);
		sql=sql.replace("@MDF_USER", userID);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getServices(String nodeID){
		  String sql="select NODE_ID,TYPE,START,STOP,MDF_USER,MDF_TIME  from NODE_SERVICE where  NODE_ID='@NODE_ID'  ORDER BY TYPE";
			sql=sql.replace("@NODE_ID", nodeID);
			return sql;
	}
}
