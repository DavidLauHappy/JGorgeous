package sql;

public class NODEFILESql {
	
	public static String getAdd(String pkgID,String nodeID,String fileID,String md5,String userID){
		String sql="insert into NODE_FILE(PKG_ID,NODE_ID,FILE_ID,MD5,MDF_USER,MDF_TIME) values('@PKG_ID','@NODE_ID','@FILE_ID','@MD5','@MDF_USER',@MDF_TIME)";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@FILE_ID", fileID);
		sql=sql.replace("@MD5", md5);
		sql=sql.replace("@MDF_USER", userID);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getDelete(String pkgID,String nodeID,String fileID){
		String sql="delete from NODE_FILE where PKG_ID='@PKG_ID' and NODE_ID='@NODE_ID' and FILE_ID='@FILE_ID'";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@FILE_ID", fileID);
		return sql;
	}
	
	public static String getCompare(String userID,String flag,String pkgID,String systemID,String nodeID){
		String sql="SELECT PFILE.PKG_ID PKG_ID,SYSTEM.NAME SYSTEM_NAME,NODE.NAME NODE_NAME,NODE.IP IP,PFILE.NAME FILE_NAME,PFILE.MD5 MD5,NODE_FILE.MD5 INSTALL_MD5   "+
						"FROM PFILE,NODE_FILE,NODE,SYSTEM "+
						"WHERE  NODE_FILE.FILE_ID=PFILE.ID "+
						"AND  NODE_FILE.PKG_ID=PFILE.PKG_ID "+
						"AND NODE_FILE.NODE_ID=NODE.ID "+
						"AND NODE.SYSTEM_ID=SYSTEM.BUSSID "+
						"AND NODE.FLAG in('@FLAG','C') "+
						"AND NODE_FILE.MDF_USER='@USER_ID' "+
						"AND ('@SYSTEM_ID'='' OR NODE.SYSTEM_ID='@SYSTEM_ID') "+
						"AND ('@PKG_ID'=''  OR  NODE_FILE.PKG_ID='@PKG_ID') "+
						"AND ('@NODE_ID'=''  OR NODE_FILE.NODE_ID='@NODE_ID')";
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@NODE_ID", nodeID);
		return sql;
		
	}
}
