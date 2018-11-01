package sql;

public class REQUIREMENTVERSIONSql {
	
	public static String getInser(String sno,String fileList,String cmpFileList,String desc,String version){
		String sql="insert into REQUIREMENT_VERSION(NO,FILELIST,CMPFILELIST,REQDESC,VERSION) "+
						 "values('@SNO','@FILELIST','@CMPFILELIST','@REQDESC','@VERSION')";
		sql=sql.replace("@SNO",  sno);
		sql=sql.replace("@FILELIST",  fileList);
		sql=sql.replace("@CMPFILELIST",  cmpFileList);
		sql=sql.replace("@REQDESC",  desc);
		sql=sql.replace("@VERSION",  version);
		return sql;
	}
}
