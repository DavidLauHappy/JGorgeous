package sql;

public class PFILESql {
	public static String addData(String id, String name, String path, String bootfalg,
			String seq, String pkgID, String stepID, String md5, String type,
			String dbOwner, String user, String dbType, String objName,
			String dir, String isDir, String mdfUser, String mdfTime){
     	String sql="INSERT INTO PFILE (ID,NAME,[PATH],BOOTFLAG,SEQ,PKG_ID,STEP_ID,MD5,[TYPE],[OWNER],[USER],DBTYPE,OBJNAME,DIR,ISDIR,MDF_USER,MDF_TIME) "+
     					"VALUES('@ID','@NAME','@PATH','@BOOTFLAG','@SEQ','@PKG_ID','@STEP_ID','@MD5','@TYPE','@OWNER','@USER','@DBTYPE','@OBJNAME','@DIR','@ISDIR','@MDF_USER',@MDF_TIME) ";
     	sql=sql.replace("@ID", id);
     	sql=sql.replace("@NAME", name);
     	sql=sql.replace("@PATH", path);
     	sql=sql.replace("@BOOTFLAG", bootfalg);
     	sql=sql.replace("@SEQ", seq);
     	sql=sql.replace("@PKG_ID", pkgID);
     	sql=sql.replace("@STEP_ID", stepID);
     	sql=sql.replace("@MD5", md5);
     	sql=sql.replace("@TYPE", type);
     	sql=sql.replace("@OWNER", dbOwner);
     	sql=sql.replace("@USER", user);
     	sql=sql.replace("@DBTYPE", dbType);
     	sql=sql.replace("@OBJNAME", objName);
     	sql=sql.replace("@DIR", dir);
     	sql=sql.replace("@ISDIR", isDir);
     	sql=sql.replace("@MDF_USER", mdfUser);
     	sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
     	return sql;
     }
	
	public static String deleteByPkg(String pkgID){
		  String sql="delete from PFILE WHERE PKG_ID='@PKG_ID' ";
			sql=sql.replace("@PKG_ID", pkgID);
	     	return sql;
	}
	
	public static String getStepFiles(String pkgID,String stepID){
		 String sql="select  ID,NAME,[PATH],BOOTFLAG,SEQ,PKG_ID,STEP_ID,MD5,[TYPE],[OWNER],[USER],DBTYPE,OBJNAME,DIR,ISDIR,MDF_USER,MDF_TIME from PFILE WHERE PKG_ID='@PKG_ID' and STEP_ID='@STEP_ID' ORDER BY CAST(SEQ AS INT) ASC";
			sql=sql.replace("@PKG_ID", pkgID);
			sql=sql.replace("@STEP_ID", stepID);
	     	return sql;
	}
	
	public static String getFileByID(String pkgID,String stepID,String id){
		 String sql="select  ID,NAME,[PATH],BOOTFLAG,SEQ,PKG_ID,STEP_ID,MD5,[TYPE],[OWNER],[USER],DBTYPE,OBJNAME,DIR,ISDIR,MDF_USER,MDF_TIME from PFILE WHERE PKG_ID='@PKG_ID' and STEP_ID='@STEP_ID' and ID='@ID'";
			sql=sql.replace("@PKG_ID", pkgID);
			sql=sql.replace("@STEP_ID", stepID);
			sql=sql.replace("@ID", id);
	     	return sql;
	}
	
	public static String getFilesByPkg(String pkgID,String owner){
		 String sql="select  ID,NAME,[PATH],BOOTFLAG,SEQ,PKG_ID,STEP_ID,MD5,[TYPE],[OWNER],[USER],DBTYPE,OBJNAME,DIR,ISDIR,MDF_USER,MDF_TIME from PFILE WHERE PKG_ID='@PKG_ID' and OWNER='@OWNER' ORDER BY CAST(SEQ AS int) ASC";
			sql=sql.replace("@PKG_ID", pkgID);
			sql=sql.replace("@OWNER", owner);
	     	return sql;
	}
	
	public static String getStepByAction(String pkgID,String actionType){
		 String sql="select PKG_ID,ID,NAME,[DESC],[ACTION],PARENT_ID,SEQ,BACKUP_FLAG,MDF_USER,MDF_TIME from STEP WHERE PKG_ID='@PKG_ID' and ACTION='@ACTION' ORDER BY CAST(SEQ AS INT) ASC";
			sql=sql.replace("@PKG_ID", pkgID);
			sql=sql.replace("@ACTION", actionType);
	     	return sql;
	}
	
	
}
