package sql;

public class LOCALNODESql {
		public static String getAdd(String userID,String app,String id,String name,String ip,String os){
			String sql="insert into LOCAL_NODE(USER_ID,APP,ID,NAME,IP,OS,TIME) values"+
					"('@USER_ID','@APP','@ID','@NAME','@IP','@OS',@TIME)";
			sql=sql.replace("@USER_ID", userID);
			sql=sql.replace("@APP", app);
			sql=sql.replace("@ID", id);
			sql=sql.replace("@NAME", name);
			sql=sql.replace("@IP", ip);
			sql=sql.replace("@OS", os);
			sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
		}
		
		public static String getDirSet(String id,String dir1,String dir2,String dir3,String dir4,String dir5,String autoBackup,String start,String stop,String autSotart){
			 String sql="update LOCAL_NODE SET DIR1='@DIR1', DIR2='@DIR2',DIR3='@DIR3' ,DIR4='@DIR4',DIR5='@DIR5',AUTOBACKUP='@AUTOBACKUP',"+
					 		  "START='@START', STOP='@STOP',AUTOSTART='@AUTOSTART', TIME=@TIME where ID='@ID'";
				sql=sql.replace("@DIR1", dir1);
				sql=sql.replace("@DIR2", dir2);
				sql=sql.replace("@DIR3", dir3);
				sql=sql.replace("@DIR4", dir4);
				sql=sql.replace("@DIR5", dir5);
				sql=sql.replace("@AUTOBACKUP", autoBackup);
				sql=sql.replace("@START", start);
				sql=sql.replace("@STOP", stop);
				sql=sql.replace("@AUTOSTART", autSotart);
				sql=sql.replace("@ID", id);
				sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
				return sql;
		
		}

		public static String  getDbSet(String id,String dbtype,String dbuser,String dbpasswd,String dbname,String  backdbname,String dbport,String dbautobackup){
			 String sql="update LOCAL_NODE SET DBTYPE='@DBTYPE', DBUSER='@DBUSER',DBPASSWD='@DBPASSWD',"+
			 		  "DBNAME='@DBNAME', DBPORT='@DBPORT',DBAUTOBACKUP='@DBAUTOBACKUP', BACKDBNAME='@BACKDBNAME',TIME=@TIME where ID='@ID'";
					sql=sql.replace("@DBTYPE", dbtype);
					sql=sql.replace("@DBUSER", dbuser);
					sql=sql.replace("@DBPASSWD", dbpasswd);
					sql=sql.replace("@DBNAME", dbname);
					sql=sql.replace("@DBPORT", dbport);
					sql=sql.replace("@DBAUTOBACKUP", dbautobackup);
					sql=sql.replace("@BACKDBNAME", backdbname);
					sql=sql.replace("@ID", id);
					sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
		}
		
		public static String getSftpSet(String id,String sftpUser,String sftpPasswd,String sftpPort,String sftpDir){
			 String sql="update LOCAL_NODE SET SFTPUSER='@SFTPUSER', SFTPPASSWD='@SFTPPASSWD',SFTPPORT='@SFTPPORT',"+
					 		  "SFTPDIR='@SFTPDIR', TIME=@TIME where ID='@ID'";
					sql=sql.replace("@SFTPUSER", sftpUser);
					sql=sql.replace("@SFTPPASSWD", sftpPasswd);
					sql=sql.replace("@SFTPPORT", sftpPort);
					sql=sql.replace("@SFTPDIR", sftpDir);
					sql=sql.replace("@ID", id);
					sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
		}
		
		
		public static String getNodes(String userID,String app){
			   String sql="select USER_ID,APP,ID,NAME,IP,OS,DIR1,DIR2,DIR3,DIR4,DIR5,AUTOBACKUP,START,STOP,AUTOSTART,"+
					   				    "DBTYPE,DBUSER,DBPASSWD,DBNAME,DBPORT,DBAUTOBACKUP,"+
					   				    "SFTPUSER,SFTPPASSWD,SFTPPORT,SFTPDIR,STATUS,TIME,BACKDBNAME from LOCAL_NODE "	+
					   		 "where USER_ID='@USER_ID' and APP='@APP'";
			   	sql=sql.replace("@USER_ID", userID);
				sql=sql.replace("@APP", app);
				return sql;
		}
		
		public static String getNode(String id){
			   String sql="select USER_ID,APP,ID,NAME,IP,OS,DIR1,DIR2,DIR3,DIR4,DIR5,AUTOBACKUP,START,STOP,AUTOSTART,"+
					   				    "DBTYPE,DBUSER,DBPASSWD,DBNAME,DBPORT,DBAUTOBACKUP,"+
					   				    "SFTPUSER,SFTPPASSWD,SFTPPORT,SFTPDIR,STATUS,TIME,BACKDBNAME from LOCAL_NODE "	+
					   		 "where ID='@ID'";
			   	sql=sql.replace("@ID", id);
				return sql;
		}
		
		
		public static String getVersionNodes(String versionID){
			String sql="select USER_ID,APP,ID,NAME,IP,OS,DIR1,DIR2,DIR3,DIR4,DIR5,AUTOBACKUP,START,STOP,AUTOSTART,"+
					   				    "DBTYPE,DBUSER,DBPASSWD,DBNAME,DBPORT,DBAUTOBACKUP,"+
					   				    "SFTPUSER,SFTPPASSWD,SFTPPORT,SFTPDIR,STATUS,TIME,BACKDBNAME from LOCAL_NODE "+
					   			"where exists(select 1 from LOCAL_COMMAND WHERE VER_ID='@VER_ID' AND NODE_ID=LOCAL_NODE.ID)";
			sql=sql.replace("@VER_ID", versionID);
			return sql;
		}
		
		public static String getNodesByIp(String versionID,String ip){
			String sql="select USER_ID,APP,ID,NAME,IP,OS,DIR1,DIR2,DIR3,DIR4,DIR5,AUTOBACKUP,START,STOP,AUTOSTART,"+
					   				    "DBTYPE,DBUSER,DBPASSWD,DBNAME,DBPORT,DBAUTOBACKUP,"+
					   				    "SFTPUSER,SFTPPASSWD,SFTPPORT,SFTPDIR,STATUS,TIME,BACKDBNAME from LOCAL_NODE "+
					   			"where  IP='@IP'";
			sql=sql.replace("@IP", ip);
			return sql;
		}
		
		public static String getDbNodes(String userID){
			String sql="select USER_ID,APP,ID,NAME,IP,OS,DIR1,DIR2,DIR3,DIR4,DIR5,AUTOBACKUP,START,STOP,AUTOSTART,"+
					   				    "DBTYPE,DBUSER,DBPASSWD,DBNAME,DBPORT,DBAUTOBACKUP,"+
					   				    "SFTPUSER,SFTPPASSWD,SFTPPORT,SFTPDIR,STATUS,TIME,BACKDBNAME from LOCAL_NODE "+
					   			"where  USER_ID='@USER_ID' and DBTYPE is not NULL";
			sql=sql.replace("@USER_ID", userID);
			return sql;
		}
		
		
		public static String getNodeExists(String userID,String ip){
			String sql="select COUNT(*) COUNT from LOCAL_NODE where USER_ID='@USER_ID'  and IP='@IP'";
			sql=sql.replace("@USER_ID", userID);
			sql=sql.replace("@IP", ip);
			return sql;
		}

		public static String getNodeDelete(String id){
			String sql="delete from LOCAL_NODE where ID='@ID'";
			sql=sql.replace("@ID", id);
			return sql;
		}
		
		public static String getStatusSet(String id,String status){
			String sql="update  LOCAL_NODE  set  STATUS='@STATUS',TIME=@TIME   where ID='@ID'";
			sql=sql.replace("@ID", id);
			sql=sql.replace("@STATUS", status);
			sql=sql.replace("@TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
		}
		
}
