package sql;

public class PKGSql {

	public static String addData(String pkgID,String app,String relApps,String desc,String status,String cfgfile,String enable,String mdfUser,String mdftime){
     	String sql="INSERT INTO PKG (ID,APP,RELAPPS,[DESC],STATUS,CFGFILE,ENABLE,MDF_USER,MDF_TIME) "+
     					"VALUES('@ID','@APP','@RELAPPS','@DESC','@STATUS','@CFGFILE','@ENABLE','@MDF_USER','@MDF_TIME') ";
     	sql=sql.replace("@ID", pkgID);
     	sql=sql.replace("@APP", app);
     	sql=sql.replace("@RELAPPS", relApps);
     	sql=sql.replace("@DESC", desc);
     	sql=sql.replace("@STATUS", status);
     	sql=sql.replace("@CFGFILE", cfgfile);
     	sql=sql.replace("@ENABLE", enable);
     	sql=sql.replace("@MDF_USER", mdfUser);
     	sql=sql.replace("@MDF_TIME", mdftime);
     	return sql;
     }
	
	public static String getDeleteArchive(String pkgID,String tableName){
		 String sql="";
		 if("HIS_PKG".equals(tableName)){
			 sql="delete from @TABLENAME WHERE ID='@PKG_ID' ";
		 }else{
		     sql="delete from @TABLENAME WHERE PKG_ID='@PKG_ID' ";
		  }
		 sql=sql.replace("@TABLENAME", tableName);
		 sql=sql.replace("@PKG_ID", pkgID);
	     return sql;
	}
	
	public static String getDelete(String pkgID,String tableName){
		 String sql="";
		 if("PKG".equals(tableName)){
			 sql="delete from @TABLENAME WHERE ID='@PKG_ID' ";
		 }else{
		     sql="delete from @TABLENAME WHERE PKG_ID='@PKG_ID' ";
		  }
		 sql=sql.replace("@TABLENAME", tableName);
		 sql=sql.replace("@PKG_ID", pkgID);
	     return sql;
	}
	
	public static String getArchive(String pkgID,String tableName,String srcTableName){
		 String sql="";
		 if("HIS_PKG".equals(tableName)){
			 sql="insert  into @TABLENAME select * from @SRCTABLENAME WHERE ID='@PKG_ID' ";
		 }else{
		     sql="insert  into @TABLENAME select * from @SRCTABLENAME WHERE PKG_ID='@PKG_ID' ";
		  }
		 sql=sql.replace("@SRCTABLENAME", srcTableName);
		 sql=sql.replace("@TABLENAME", tableName);
		 sql=sql.replace("@PKG_ID", pkgID);
	     return sql;
	}
	
	public static String exist(String pkgID){
		String sql="SELECT COUNT(*) AS COUNT FROM  PKG WHERE ID='@ID' ";
		sql=sql.replace("@ID", pkgID);
		return sql;
	}
	
  public static String updateInroll(String status,String time,String cfgfile,String pkgID,String userID){
		String sql="update   PKG  set STATUS='@STATUS', CFGFILE='@CFGFILE',MDF_TIME='@MDF_TIME',MDF_USER='@MDF_USER' WHERE ID='@ID' ";
		sql=sql.replace("@ID", pkgID);
		sql=sql.replace("@STATUS", status);
     	sql=sql.replace("@CFGFILE", cfgfile);
     	sql=sql.replace("@MDF_TIME", time);
     	sql=sql.replace("@MDF_USER", userID);
		return sql;
  }
  
  public static String disablePkg(String pkgID,String enable,String time){
	  String sql="update   PKG  set ENABLE='@ENABLE',MDF_TIME='@MDF_TIME' WHERE ID='@ID' ";
		sql=sql.replace("@ID", pkgID);
		sql=sql.replace("@ENABLE", enable);
     	sql=sql.replace("@MDF_TIME", time);
     	return sql;
  }
  
  public static String deletePkg(String pkgID){
	  String sql="delete from PKG WHERE ID='@ID' ";
		sql=sql.replace("@ID", pkgID);
     	return sql;
  }
  
  public static String deletePkgViews(String pkgID){
	  String sql="delete from PKG_VIEWS where PKG_ID='@PKG_ID'";
	  sql=sql.replace("@PKG_ID", pkgID);
	  return sql;
  }
  
  public static String getAddPkgView(String pkgID,String viewID,String userID){
	  String sql="insert into  PKG_VIEWS(PKG_ID,VIEW_ID,CRT_USER,CRT_TIME) values('@PKG_ID','@VIEW_ID','@CRT_USER',@CRT_TIME)";
	  sql=sql.replace("@PKG_ID", pkgID);
	  sql=sql.replace("@VIEW_ID", viewID);
	  sql=sql.replace("@CRT_USER", userID);
	  sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
	  return sql;
  }
  
 public static String getMyPkgs(String userID,String enable){ 
	 String sql="SELECT ID,APP,RELAPPS,[DESC],STATUS,CFGFILE,ENABLE,MDF_USER,MDF_TIME FROM PKG WHERE MDF_USER='@MDF_USER' AND ENABLE='@ENABLE' ORDER BY ID";
	 sql=sql.replace("@MDF_USER", userID);
	 sql=sql.replace("@ENABLE", enable);
	 return sql;
 }
 
 
 public static String getPkgByID(String pkgID){ 
	 String sql="SELECT ID,APP,RELAPPS,[DESC],STATUS,CFGFILE,ENABLE,MDF_USER,MDF_TIME FROM PKG WHERE ID='@ID'";
	 sql=sql.replace("@ID", pkgID);
	 return sql;
 }
 
 public static String setStatus(String pkgID,String status,String userID){
	  String sql="update   PKG  set STATUS='@STATUS',MDF_TIME=@MDF_TIME,MDF_USER='@MDF_USER' WHERE ID='@ID' ";
		sql=sql.replace("@ID", pkgID);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@MDF_USER", userID);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
 }
 
 
}
