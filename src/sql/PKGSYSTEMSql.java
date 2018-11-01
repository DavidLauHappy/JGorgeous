package sql;

public class PKGSYSTEMSql {
	
            public static String getByStatus(String userID,String status){
            	String sql="SELECT PKG_ID,SYSTEM_ID,STATUS,MDF_USER,MDF_TIME FROM  PKG_SYSTEM WHERE MDF_USER='@MDF_USER' AND STATUS='@STATUS' ORDER BY PKG_ID";
        		sql=sql.replace("@MDF_USER", userID);
        		sql=sql.replace("@STATUS", status);
        		return sql;
            }
            
            public static String getByUser(String userID,String flag){
            	String sql="SELECT PKG_SYSTEM.PKG_ID PKG_ID,PKG_SYSTEM.SYSTEM_ID SYSTEM_ID,PKG_SYSTEM.STATUS STATUS,PKG_SYSTEM.MDF_TIME MDF_TIME,SYSTEM.NAME SYSTEM_NAME FROM  PKG_SYSTEM,SYSTEM WHERE   PKG_SYSTEM.SYSTEM_ID=SYSTEM.BUSSID and SYSTEM.FLAG='@FLAG' and  PKG_SYSTEM.MDF_USER='@MDF_USER'  ORDER BY PKG_SYSTEM.MDF_TIME";
        		sql=sql.replace("@MDF_USER", userID);
        		sql=sql.replace("@FLAG", flag);
        		return sql;
            }
            
            public static String getByID(String userID,String flag,String pkgID,String systemID){
            	String sql="SELECT PKG_SYSTEM.PKG_ID PKG_ID,PKG_SYSTEM.SYSTEM_ID SYSTEM_ID,PKG_SYSTEM.STATUS STATUS,PKG_SYSTEM.MDF_TIME MDF_TIME,SYSTEM.NAME SYSTEM_NAME "+
            					 " FROM  PKG_SYSTEM,SYSTEM "+
            					 "WHERE  PKG_SYSTEM.SYSTEM_ID=SYSTEM.BUSSID "+
            					 	  "and SYSTEM.FLAG='@FLAG' "+
            					       "and PKG_SYSTEM.SYSTEM_ID='@SYSTEM_ID' "+
            					       "and PKG_SYSTEM.PKG_ID='@PKG_ID' "+
            					 	   "and PKG_SYSTEM.MDF_USER='@MDF_USER'  "+
            					 	   "ORDER BY PKG_SYSTEM.MDF_TIME";
        		sql=sql.replace("@SYSTEM_ID", systemID);
        		sql=sql.replace("@PKG_ID", pkgID);
        		sql=sql.replace("@MDF_USER", userID);
        		sql=sql.replace("@FLAG", flag);
        		return sql;
            }
            
            public static String getMyPkgSys(String userID,String pkgID){
            	String sql="SELECT PKG_ID,SYSTEM_ID,STATUS,MDF_USER,MDF_TIME FROM  PKG_SYSTEM WHERE MDF_USER='@MDF_USER' AND PKG_ID='@PKG_ID' and STATUS IN('1','3')  ORDER BY PKG_ID";
        		sql=sql.replace("@MDF_USER", userID);
        		sql=sql.replace("@PKG_ID", pkgID);
        		return sql;
            }
           
            
            public static String getUptStatus(String userID,String pkgID,String systemID,String status){
            	String sql="update  PKG_SYSTEM set STATUS='@STATUS', MDF_TIME=@MDF_TIME WHERE MDF_USER='@MDF_USER' AND PKG_ID='@PKG_ID' and SYSTEM_ID='@SYSTEM_ID'";
        		sql=sql.replace("@MDF_USER", userID);
        		sql=sql.replace("@PKG_ID", pkgID);
        		sql=sql.replace("@SYSTEM_ID", systemID);
        		sql=sql.replace("@STATUS", status);
        		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
        		return sql;
            }
            
            public static String getPkgSysExists(String pkgID,String systemID,String userID){
            	String sql="select COUNT(*) COUNT from  PKG_SYSTEM where PKG_ID='@PKG_ID' and SYSTEM_ID='@SYSTEM_ID' and MDF_USER='@MDF_USER'";
        		sql=sql.replace("@MDF_USER", userID);
        		sql=sql.replace("@PKG_ID", pkgID);
        		sql=sql.replace("@SYSTEM_ID", systemID);
        		return sql;
            }
            
            public static String getDeletePkgSys(String pkgID,String systemID,String userID){
            	String sql="delete from  PKG_SYSTEM where PKG_ID='@PKG_ID' and SYSTEM_ID='@SYSTEM_ID' and MDF_USER='@MDF_USER'";
        		sql=sql.replace("@MDF_USER", userID);
        		sql=sql.replace("@PKG_ID", pkgID);
        		sql=sql.replace("@SYSTEM_ID", systemID);
        		return sql;
            }
            
            public static String hasCommand(String userID,String pkgID,String systemID,String status,String nodeFlag){
            	String sql="select COUNT(*)  COUNT from COMMAND,NODE "+
            				"where  NODE.SYSTEM_ID='@SYSTEM_ID'  and NODE.FLAG in('@FLAG','C') and NODE.ID=COMMAND.NODE_ID "+
            					"and COMMAND.PKG_ID='@PKG_ID'  and COMMAND.USER_ID='@USER_ID' and COMMAND.STATUS='@STATUS' ";
        		sql=sql.replace("@USER_ID", userID);
        		sql=sql.replace("@PKG_ID", pkgID);
        		sql=sql.replace("@SYSTEM_ID", systemID);
        		sql=sql.replace("@STATUS", status);
        		return sql;
            }
            
            
            public static String  getScheduleAbleNode(String pkgID,String systemID,String flag,String userID){
            	String sql="select   top 1  USER_NODE.NODE_ID NODE_ID  "+
            					  "from NODE,USER_NODE  where USER_NODE.NODE_ID=NODE.ID  and  NODE.SYSTEM_ID='@SYSTEM_ID'  and NODE.FLAG in('@FLAG','C') and USER_NODE.USER_ID='@USER_ID' and USER_NODE.SCH_FLAG='1'  and "+
            					  "exists (select 1 from COMMAND where COMMAND.NODE_ID=NODE.ID and COMMAND.PKG_ID='@PKG_ID' and COMMAND.FLAG='1' and COMMAND.STATUS IN('0','1','7')) "+
            					  "order by cast(USER_NODE.SEQ as int) ";
        		sql=sql.replace("@SYSTEM_ID", systemID);
        		sql=sql.replace("@FLAG", flag);
        		sql=sql.replace("@USER_ID", userID);
        		sql=sql.replace("@PKG_ID", pkgID);
        		return sql;
            }
            
            public static String getScheduledNode(String pkgID,String systemID,String flag,String userID){
            	String sql="select  USER_NODE.NODE_ID NODE_ID "+
  					  "from NODE,USER_NODE  where USER_NODE.NODE_ID=NODE.ID  and  NODE.SYSTEM_ID='@SYSTEM_ID'  and NODE.FLAG in('@FLAG','C') and USER_NODE.USER_ID='@USER_ID' and USER_NODE.SCH_FLAG='2'  and "+
  					  "exists (select 1 from COMMAND where COMMAND.NODE_ID=NODE.ID and COMMAND.PKG_ID='@PKG_ID' and COMMAND.FLAG='1' and COMMAND.STATUS IN('0','1','4','7','8')) ";
						sql=sql.replace("@SYSTEM_ID", systemID);
						sql=sql.replace("@FLAG", flag);
						sql=sql.replace("@USER_ID", userID);
						sql=sql.replace("@PKG_ID", pkgID);
						return sql;
            }
            
            public static String getNode(String pkgID,String systemID,String flag,String userID){
            	String sql="select  USER_NODE.NODE_ID NODE_ID "+
            					 "from NODE,USER_NODE  where USER_NODE.NODE_ID=NODE.ID  and  NODE.SYSTEM_ID='@SYSTEM_ID'  and NODE.FLAG in('@FLAG','C') and USER_NODE.USER_ID='@USER_ID'  "+
            					 "and exists (select 1 from COMMAND  where COMMAND.NODE_ID=NODE.ID and COMMAND.PKG_ID='@PKG_ID' and COMMAND.FLAG='1' and COMMAND.STATUS IN('0','1','4','7','8')) "+
            					 "ORDER BY CAST(USER_NODE.SEQ AS INT)";
  						sql=sql.replace("@SYSTEM_ID", systemID);
  						sql=sql.replace("@FLAG", flag);
  						sql=sql.replace("@USER_ID", userID);
  						sql=sql.replace("@PKG_ID", pkgID);
  						return sql;
            }
            
            public static String getAdd(String pkgID,String systemID,String status,String mdfUser){
            	String sql="insert into PKG_SYSTEM(PKG_ID,SYSTEM_ID,STATUS,MDF_USER,MDF_TIME) values('@PKG_ID','@SYSTEM_ID','@STATUS','@MDF_USER',@MDF_TIME) ";
        		sql=sql.replace("@MDF_USER", mdfUser);
        		sql=sql.replace("@PKG_ID", pkgID);
        		sql=sql.replace("@SYSTEM_ID", systemID);
        		sql=sql.replace("@STATUS", status);
        		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
        		return sql;
            }
}
