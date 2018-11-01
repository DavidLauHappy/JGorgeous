package sql;

public class CONFIGSql {
	public static String getConfig(String app,String filename,String userID){
		String sql="select APP,ENV_TYPE,CFG_FILE,PARAM_NAME,PARAM_VAL,CRT_USER,MDF_USER,MDF_TIME "+
				"from CONFIG "+
	"where APP='@APP' and CRT_USER='@CRT_USER' and CFG_FILE='@CFG_FILE'  ORDER BY PARAM_NAME";
		sql=sql.replace("@APP", app);
		sql=sql.replace("@CRT_USER", userID);
		sql=sql.replace("@CFG_FILE", filename);
		return sql;
	}
	
	public static String getConfig(String app,String userID){
		String sql="select APP,ENV_TYPE,CFG_FILE,PARAM_NAME,PARAM_VAL,CRT_USER,MDF_USER,MDF_TIME "+
				"from CONFIG "+
	"where APP='@APP' and CRT_USER='@CRT_USER'   ORDER BY CFG_FILE";
		sql=sql.replace("@APP", app);
		sql=sql.replace("@CRT_USER", userID);
		return sql;
	}
	
	public static String getInsert(String app,String envType,String filename,String paramName,String value,String userID){
		String sql="insert into CONFIG(APP,ENV_TYPE,CFG_FILE,PARAM_NAME,PARAM_VAL,CRT_USER,MDF_USER,MDF_TIME) "+
						"values('@APP','@ENV_TYPE','@CFG_FILE','@PARAM_NAME','@PARAM_VAL','@CRT_USER','@MDF_USER',@MDF_TIME)";
		sql=sql.replace("@APP", app);
		sql=sql.replace("@ENV_TYPE", envType);
		sql=sql.replace("@CFG_FILE", filename);
		sql=sql.replace("@PARAM_NAME", paramName);
		sql=sql.replace("@PARAM_VAL", value);
		sql=sql.replace("@CRT_USER", userID);
		sql=sql.replace("@MDF_USER", userID);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		return sql;
	}
	
	public static String getUpdate(String app,String envType,String filename,String paramName,String value,String userID){
		String sql="update  CONFIG set  PARAM_VAL='@PARAM_VAL',ENV_TYPE='@ENV_TYPE',MDF_TIME=@MDF_TIME "+
				" where  APP='@APP' and CFG_FILE='@CFG_FILE'  and PARAM_NAME='@PARAM_NAME' and CRT_USER='@CRT_USER'";
			sql=sql.replace("@APP", app);
			sql=sql.replace("@ENV_TYPE", envType);
			sql=sql.replace("@CFG_FILE", filename);
			sql=sql.replace("@PARAM_NAME", paramName);
			sql=sql.replace("@PARAM_VAL", value);
			sql=sql.replace("@CRT_USER", userID);
			sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
	}
}
