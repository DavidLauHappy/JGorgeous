package sql;

public class FLOWDEFSql {
	
	 public static String getByFunc(String funcID){
		 String sql="select ID,FUNC_ID,NAME,IS_ENABLE, MDF_USER,MDF_TIME from  FLOW_DEF where FUNC_ID='@FUNC_ID'";
		 sql=sql.replace("@FUNC_ID", funcID);
		 return sql;
	 }
	 
	 public static String getByID(String id){
		 String sql="select ID,FUNC_ID,NAME,IS_ENABLE, MDF_USER,MDF_TIME from  FLOW_DEF where ID='@ID'";
		 sql=sql.replace("@ID", id);
		 return sql;
	 }
	 
	 public static String getFlows(){
		 String sql="select ID,FUNC_ID,NAME,IS_ENABLE, MDF_USER,MDF_USER from  FLOW_DEF order by  ID";
		 return sql;
	 }
	 
	 public static String getFlowEnable(String flowID,String enable,String mdfUser){
		 String sql="update  FLOW_DEF  set  IS_ENABLE='@IS_ENABLE', MDF_USER='@MDF_USER',MDF_TIME=@MDF_TIME where ID='@ID'";
		 sql=sql.replace("@IS_ENABLE", enable);
		 sql=sql.replace("@ID", flowID);
		 sql=sql.replace("@MDF_USER", mdfUser);
		 sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		 return sql;
	 }
	 
	 public static String getFuncEnable(String funcID,String isCheck){
		 String sql="update  FUNC  set  IS_CHECK='@IS_CHECK'  where ID='@ID'";
		 sql=sql.replace("@IS_CHECK", isCheck);
		 sql=sql.replace("@ID", funcID);
		 return sql;
	 }
}
