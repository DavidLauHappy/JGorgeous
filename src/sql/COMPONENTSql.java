package sql;

public class COMPONENTSql {
	public static String getInsert(String id,String name,String abbr,String type,String systemID,String status,String mdfUser,String flag){
		String sql="insert into COMPONENT(ID,NAME,ABBR,TYPE,SYSTEM_ID,STATUS,MDF_USER,MDF_TIME,FLAG)  values('@ID','@NAME','@ABBR','@TYPE','@SYSTEM_ID','@STATUS','@MDF_USER',@MDF_TIME,'@FLAG')";
		sql=sql.replace("@ID", id);
		sql=sql.replace("@NAME", name);
		sql=sql.replace("@ABBR", abbr);
		sql=sql.replace("@TYPE", type);
		sql=sql.replace("@SYSTEM_ID",systemID);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@MDF_USER", mdfUser);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getDeleteByFlag(String flag){
		String sql="delete  COMPONENT where FLAG='@FLAG'";
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	public static String getComponentByID(String id,String flag){
		String sql="select ID,NAME,ABBR,[TYPE],SYSTEM_ID,[STATUS],MDF_USER,MDF_TIME,FLAG from  COMPONENT where ID='@ID' and FLAG='@FLAG'";
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@ID", id);
    	return sql;
	}
	
	public static String getQueryBySystem(String systemID,String flag){
		String sql="select ID,NAME,ABBR,[TYPE],SYSTEM_ID,[STATUS],MDF_USER,MDF_TIME,FLAG from  COMPONENT where SYSTEM_ID='@SYSTEM_ID' and FLAG='@FLAG' ORDER BY NAME";
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	public static String getQueryByUserSystem(String userID,String systemID,String flag){
		String sql="select ID,NAME,ABBR,[TYPE],SYSTEM_ID,[STATUS],MDF_USER,MDF_TIME,FLAG from  COMPONENT where SYSTEM_ID='@SYSTEM_ID' and FLAG='@FLAG' AND  EXISTS (SELECT 1  FROM USER_NODE,NODE WHERE USER_NODE.USER_ID='@USER_ID' AND USER_NODE.NODE_ID=NODE.ID AND NODE.COMPONET_ID=COMPONENT.ID) ORDER BY NAME";
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@USER_ID", userID);
    	return sql;
	}
	
	public static String geCluster(String componentID,String flag){
		String sql="select  distinct CLUSTER,COMPONET_ID FROM NODE WHERE COMPONET_ID='@COMPONET_ID' and FLAG='@FLAG'";
		sql=sql.replace("@COMPONET_ID", componentID);
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	
	
	
}
