package sql;

public class NODEFILESTTSql {
	
	public static  String getDelete(String pkgID,String systemID){
		String sql="delete  from NODE_FILE_STT  where PKG_ID='@PKG_ID' and SYSTEM_ID='@SYSTEM_ID'";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@SYSTEM_ID", systemID);
    	return sql;
	}
	
	public static  String getDelete(String pkgID,String nodeID,String dirName){
		String sql="delete  from NODE_FILE_STT  where PKG_ID='@PKG_ID' and NODE_ID='@NODE_ID' and FILE_DIR='@FILE_DIR'";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@FILE_DIR", dirName);
    	return sql;
	}
	
	public static  String getInsert(String pkgID,String systemID,String nodeID,String dirName,String count,String mdfUser){
		String sql="insert into   NODE_FILE_STT(PKG_ID,SYSTEM_ID,NODE_ID,FILE_DIR,FILE_CNT,MDF_USER,MDF_TIME) values" +
						"('@PKG_ID','@SYSTEM_ID','@NODE_ID','@FILE_DIR','@FILE_CNT','@MDF_USER',@MDF_TIME) ";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@FILE_DIR", dirName);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@FILE_CNT", count);
		sql=sql.replace("@MDF_USER", mdfUser);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getBySystem(String pkgID,String systemID,String flag){
		String sql="select  NODE_FILE_STT.PKG_ID PKG_ID,NODE_FILE_STT.SYSTEM_ID SYSTEM_ID,NODE_FILE_STT.NODE_ID NODE_ID,NODE_FILE_STT.FILE_DIR FILE_DIR,NODE_FILE_STT.FILE_CNT FILE_CNT,NODE_FILE_STT.MDF_USER MDF_USER,NODE_FILE_STT.MDF_TIME MDF_TIME,NODE.NAME NODE_NAME,SYSTEM.NAME SYSTEM_NAME "+
						 "from NODE_FILE_STT,NODE,SYSTEM  where NODE_FILE_STT.SYSTEM_ID=SYSTEM.BUSSID and NODE_FILE_STT.NODE_ID=NODE.ID and SYSTEM.FLAG='@FLAG' and NODE.FLAG in('@FLAG','C') and  NODE_FILE_STT.PKG_ID='@PKG_ID' and NODE_FILE_STT.SYSTEM_ID='@SYSTEM_ID' ";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
}
