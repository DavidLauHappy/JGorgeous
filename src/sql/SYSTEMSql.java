package sql;

public class SYSTEMSql {

	public static String getInsert(String id,String name,String abbr,String app,String cate,String bussID,String status,String mdfUser,String flag){
		String sql="insert into SYSTEM(ID,NAME,ABBR,APP,CATE,BUSSID,STATUS,MDF_USER,MDF_TIME,FLAG)  values('@ID','@NAME','@ABBR','@APP','@CATE','@BUSSID','@STATUS','@MDF_USER',@MDF_TIME,'@FLAG')";
		sql=sql.replace("@ID", id);
		sql=sql.replace("@NAME", name);
		sql=sql.replace("@ABBR", abbr);
		sql=sql.replace("@APP", app);
		sql=sql.replace("@CATE", cate);
		sql=sql.replace("@BUSSID",bussID);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@MDF_USER", mdfUser);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getDeleteByFlag(String flag){
		String sql="delete  SYSTEM where FLAG='@FLAG'";
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	public static String getQueryByApp(String appID,String flag){
		String sql="select ID,NAME,ABBR,APP,CATE,BUSSID,STATUS,MDF_USER,MDF_TIME,FLAG from  SYSTEM where APP='@APP' and FLAG='@FLAG' ORDER BY NAME";
		sql=sql.replace("@APP", appID);
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	public static String getQueryByApp(String appID){
		String sql="select SYS,SYSNAME from APP_SYSTEM where APP='@APP' and IS_ENABLE='1'";
		sql=sql.replace("@APP", appID);
    	return sql;
	}
	
	public static String getQueryByID(String bussID,String flag){
		String sql="select ID,NAME,ABBR,APP,CATE,BUSSID,STATUS,MDF_USER,MDF_TIME,FLAG from  SYSTEM where BUSSID='@BUSSID' and FLAG='@FLAG' ";
		sql=sql.replace("@BUSSID", bussID);
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	public static String getSetNodesFlag(String userID,String bussID,String flag,String nodeFlag){
		String sql=" update USER_NODE SET SCH_FLAG='@SCH_FLAG',MDF_TIME=@MDF_TIME WHERE USER_ID='@USER_ID' and SCH_FLAG<>'2' and "+
						" exists(select 1 from NODE where NODE.ID=USER_NODE.NODE_ID and NODE.SYSTEM_ID='@SYSTEM_ID'  and NODE.FLAG in('@FLAG','C'))";
				sql=sql.replace("@USER_ID", userID);
				sql=sql.replace("@SCH_FLAG", nodeFlag);
				sql=sql.replace("@SYSTEM_ID", bussID);
				sql=sql.replace("@FLAG", flag);
				sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getQueryByPkg(String userID,String pkgID,String flag){
		String sql="select SYSTEM.ID ID,SYSTEM.NAME NAME,SYSTEM.ABBR ABBR,SYSTEM.APP APP,SYSTEM.CATE CATE,SYSTEM.BUSSID BUSSID,SYSTEM.STATUS STATUS,SYSTEM.MDF_USER MDF_USER,SYSTEM.MDF_TIME MDF_TIME,SYSTEM.FLAG FLAG "+
						"from  SYSTEM,PKG_SYSTEM where PKG_SYSTEM.PKG_ID='@PKG_ID' and PKG_SYSTEM.MDF_USER='@USER_ID' and SYSTEM.BUSSID=PKG_SYSTEM.SYSTEM_ID and SYSTEM.FLAG='@FLAG' ";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
}
