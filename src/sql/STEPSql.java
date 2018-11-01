package sql;

public class STEPSql {
	 public static String addData(String pkgID,String id,String name,String desc,String action,String parentID,String seq,String backflag,String mdfUser){
     	String sql="INSERT INTO STEP (PKG_ID,ID,NAME,[DESC],[ACTION],PARENT_ID,SEQ,BACKUP_FLAG,MDF_USER,MDF_TIME) "+
     					"VALUES('@PKG_ID','@ID','@NAME','@DESC','@ACTION','@PARENT_ID','@SEQ','@BACKUP_FLAG','@MDF_USER',@MDF_TIME) ";
     	sql=sql.replace("@PKG_ID", pkgID);
     	sql=sql.replace("@ID", id);
     	sql=sql.replace("@NAME", name);
     	sql=sql.replace("@DESC", desc);
     	sql=sql.replace("@ACTION", action);
     	sql=sql.replace("@PARENT_ID", parentID);
     	sql=sql.replace("@SEQ", seq);
     	sql=sql.replace("@BACKUP_FLAG", backflag);
     	sql=sql.replace("@MDF_USER", mdfUser);
     	 sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
     	return sql;
     }
	 
	 public static String deleteByPkg(String pkgID){
		  String sql="delete from STEP WHERE PKG_ID='@PKG_ID' ";
		   sql=sql.replace("@PKG_ID", pkgID);
	     return sql;
	 }
	 
	 public static String deleteByID(String pkgID,String id){
		  String sql="delete from STEP WHERE PKG_ID='@PKG_ID' and ID='@ID'";
		   sql=sql.replace("@PKG_ID", pkgID);
		   sql=sql.replace("@ID", id);
	     return sql;
	 }
	 
	 
	 public static String setStepOrder(String pkgID,String id,String order){
		 String sql="update  STEP set SEQ='@SEQ',MDF_TIME=@MDF_TIME WHERE PKG_ID='@PKG_ID' AND ID='@ID'";
		   sql=sql.replace("@SEQ", order);
		   sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		   sql=sql.replace("@PKG_ID", pkgID);
		   sql=sql.replace("@ID", id);
	     return sql;
		 
	 }
	 
	 public static String setBackFlag(String pkgID,String id,String backFlag){
		 String sql="update  STEP set BACKUP_FLAG='@BACKUP_FLAG',MDF_TIME=@MDF_TIME WHERE PKG_ID='@PKG_ID' AND ID='@ID'";
		   sql=sql.replace("@BACKUP_FLAG", backFlag);
		   sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		   sql=sql.replace("@PKG_ID", pkgID);
		   sql=sql.replace("@ID", id);
	     return sql;
	 }
	 
	 public static String setParent(String pkgID,String id,String parentID){
		 String sql="update  STEP set PARENT_ID='@PARENT_ID',MDF_TIME=@MDF_TIME WHERE PKG_ID='@PKG_ID' AND ID='@ID'";
		   sql=sql.replace("@PARENT_ID", parentID);
		   sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		   sql=sql.replace("@PKG_ID", pkgID);
		   sql=sql.replace("@ID", id);
	     return sql;
		 
	 }
	 
	 public static String getMaxID(String pkgID){
		  String sql="select ISNULL(max(cast(ID as int)),0)+1 num  from STEP WHERE PKG_ID='@PKG_ID' ";
		   sql=sql.replace("@PKG_ID", pkgID);
		   return sql;
	 }
	 
	 public static String getStepExist(String pkgID,String stepName){
		  String sql="select ID   from STEP WHERE PKG_ID='@PKG_ID'  and [DESC]='@DESC'";
		   sql=sql.replace("@PKG_ID", pkgID);
		   sql=sql.replace("@DESC", stepName);
		   return sql;
	 }
	 
	 public static String getPkgSteps(String pkgID){
		  String sql="select  PKG_ID,ID,NAME,[DESC],[ACTION],PARENT_ID,SEQ,BACKUP_FLAG,MDF_USER,MDF_TIME from STEP WHERE PKG_ID='@PKG_ID' ORDER BY CAST(SEQ AS INT) ";
		   sql=sql.replace("@PKG_ID", pkgID);
	     return sql;
	 }
	 
	 public static String getPkgSteps(String pkgID,String stepID){
		  String sql="select  PKG_ID,ID,NAME,[DESC],[ACTION],PARENT_ID,SEQ,BACKUP_FLAG,MDF_USER,MDF_TIME from STEP WHERE PKG_ID='@PKG_ID' and ID='@ID' ORDER BY CAST(SEQ AS INT) ";
		   sql=sql.replace("@PKG_ID", pkgID);
		   sql=sql.replace("@ID", stepID);
	     return sql;
	 }
	 
	 public static String getStepByName(String pkgID,String svcName,String userID){
		  String sql="select  PKG_ID,ID,NAME,[DESC],[ACTION],PARENT_ID,SEQ,BACKUP_FLAG,MDF_USER,MDF_TIME from STEP WHERE PKG_ID='@PKG_ID' and NAME='@NAME' and MDF_USER='@MDF_USER'  ORDER BY CAST(SEQ AS INT) ";
		   sql=sql.replace("@PKG_ID", pkgID);
			sql=sql.replace("@NAME", svcName);
			sql=sql.replace("@MDF_USER", userID);
	     	return sql;
	}
}
