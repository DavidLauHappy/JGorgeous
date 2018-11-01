package sql;

public class REQUIREMENTLOGSql {
	
	public static String getAllLog(String id){
		String sql="select REQUIREMENT_LOG.ID  ID,"
								+"REQUIREMENT_LOG.USER_ID  USER_ID,"
								+"REQUIREMENT_LOG.STEP_ID   STEP_ID,"
								+"REQUIREMENT_LOG.COMMENT COMMENT,"
								+"REQUIREMENT_LOG.MDF_TIME MDF_TIME,"
								+"REQUIREMENT_LOG.SEQ	SEQ,"
								+"USERS.USER_NAME USER_NAME "
					+ "from REQUIREMENT_LOG,USERS "
				   +"where REQUIREMENT_LOG.ID='@ID' "
				     +"and REQUIREMENT_LOG.USER_ID=USERS.USER_ID "
				    +"order by REQUIREMENT_LOG.MDF_TIME";
		sql=sql.replace("@ID", id);
		return sql;
	}
	
	public static String getLog(String req,String step){
		String sql="select top 1  REQUIREMENT_LOG.ID  ID,"
										  +"REQUIREMENT_LOG.USER_ID  USER_ID,"
										  +"REQUIREMENT_LOG.STEP_ID   STEP_ID,"
										  +"REQUIREMENT_LOG.COMMENT COMMENT,"
										  +"REQUIREMENT_LOG.MDF_TIME MDF_TIME,"
										  +"REQUIREMENT_LOG.SEQ	SEQ,"
										  +"USERS.USER_NAME USER_NAME "
							+ "from REQUIREMENT_LOG,USERS "
						+"where REQUIREMENT_LOG.ID='@ID' "
						  +"and  REQUIREMENT_LOG.STEP_ID='@STEP_ID' "
						  +"and REQUIREMENT_LOG.USER_ID=USERS.USER_ID";
		sql=sql.replace("@ID", req);
		sql=sql.replace("@STEP_ID", step);
		return sql;
	}
}
