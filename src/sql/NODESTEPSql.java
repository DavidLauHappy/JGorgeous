package sql;

import utils.StringUtil;

public class NODESTEPSql {
	public static String addData(String pkgID,String systemID,String nodeID,String stepID,String flag,String mdfUser){
     	String sql="INSERT INTO NODE_STEP (PKG_ID,SYSTEM_ID,NODE_ID,STEP_ID,FLAG,MDF_USER,MDF_TIME) "+
     					"VALUES('@PKG_ID','@SYSTEM_ID','@NODE_ID','@STEP_ID','@FLAG','@MDF_USER',@MDF_TIME) ";
     	sql=sql.replace("@PKG_ID", pkgID);
     	sql=sql.replace("@SYSTEM_ID", systemID);
     	sql=sql.replace("@NODE_ID", nodeID);
     	sql=sql.replace("@STEP_ID", stepID);
     	sql=sql.replace("@FLAG", flag);
     	sql=sql.replace("@MDF_USER", mdfUser);
     	sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
     	return sql;
     }
	
	public static String getStepNode(String pkgID,String systemID,String stepID){
		String sql="select PKG_ID,SYSTEM_ID,NODE_ID,STEP_ID,FLAG,MDF_USER,MDF_TIME from NODE_STEP where PKG_ID='@PKG_ID' and  SYSTEM_ID='@SYSTEM_ID' and STEP_ID='@STEP_ID'";
		sql=sql.replace("@PKG_ID", pkgID);
     	sql=sql.replace("@SYSTEM_ID", systemID);
     	sql=sql.replace("@STEP_ID", stepID);
     	return sql;
	}
	
	public static String getDelete(String pkgID,String systemID,String nodeID,String stepID){
		String sql="delete from NODE_STEP where PKG_ID='@PKG_ID' and  SYSTEM_ID='@SYSTEM_ID' and STEP_ID='@STEP_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@PKG_ID", pkgID);
     	sql=sql.replace("@SYSTEM_ID", systemID);
     	sql=sql.replace("@STEP_ID", stepID);
    	sql=sql.replace("@NODE_ID", nodeID);
     	return sql;
	}
	
	public static String getDelete(String pkgID,String systemID,String stepID){
		String sql="delete from NODE_STEP where PKG_ID='@PKG_ID' and  SYSTEM_ID='@SYSTEM_ID' and STEP_ID='@STEP_ID'";
		sql=sql.replace("@PKG_ID", pkgID);
	 	sql=sql.replace("@SYSTEM_ID", systemID);
	 	sql=sql.replace("@STEP_ID", stepID);
	 	return sql;
	}
	
	public static String getSetFlag(String pkgID,String systemID,String nodeID,String stepID,String flag){
		String sql="update  NODE_STEP  set FLAG='@FLAG', MDF_TIME=@MDF_TIME where PKG_ID='@PKG_ID' and  SYSTEM_ID='@SYSTEM_ID' and STEP_ID='@STEP_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@PKG_ID", pkgID);
	 	sql=sql.replace("@SYSTEM_ID", systemID);
	 	sql=sql.replace("@STEP_ID", stepID);
	 	sql=sql.replace("@NODE_ID", nodeID);
	 	sql=sql.replace("@FLAG", flag);
	 	sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
	 	return sql;
	}
	
	public static String getQuery(String pkgID,String systemID,String dataFlag,String flag,String userID){
		String sql="";
		if(StringUtil.isNullOrEmpty(flag)){
			sql="SELECT NODE_STEP.PKG_ID PKG_ID,NODE_STEP.STEP_ID STEP_ID,STEP.[DESC] [DESC],NODE_STEP.NODE_ID NODE_ID,NODE.NAME NODENAME,NODE.IP IP,NODE_STEP.SYSTEM_ID SYSTEM_ID,NODE_STEP.FLAG FLAG,STEP.NAME  STEPNAME "+
				"FROM NODE_STEP,STEP,NODE,USER_NODE "+
  					"WHERE NODE_STEP.PKG_ID='@PKG_ID' "+
						 "and NODE_STEP.SYSTEM_ID='@SYSTEM_ID'  "+
						 "and STEP.PKG_ID=NODE_STEP.PKG_ID  "+
						 "and STEP.ID=NODE_STEP.STEP_ID "+
						 "and NODE_STEP.NODE_ID=NODE.ID "+
						 "and NODE.ID=USER_NODE.NODE_ID "+
						 "and USER_NODE.USER_ID='@USER_ID' "+
						 "and NODE.FLAG in('@DATAFLAG','C') "+
						 "ORDER BY CAST(USER_NODE.SEQ AS INT) ASC, CAST(STEP.SEQ AS INT) ASC";
		}else{
			sql="SELECT NODE_STEP.PKG_ID PKG_ID,NODE_STEP.STEP_ID STEP_ID,STEP.[DESC] [DESC],NODE_STEP.NODE_ID NODE_ID,NODE.NAME NODENAME,NODE.IP IP,NODE_STEP.SYSTEM_ID SYSTEM_ID,NODE_STEP.FLAG FLAG,STEP.NAME  STEPNAME "+
				"FROM NODE_STEP,STEP,NODE,USER_NODE "+
  					"WHERE NODE_STEP.PKG_ID='@PKG_ID' "+
						 "and NODE_STEP.SYSTEM_ID='@SYSTEM_ID'  "+
						 "and STEP.PKG_ID=NODE_STEP.PKG_ID  "+
						 "and STEP.ID=NODE_STEP.STEP_ID "+
						 "and NODE_STEP.NODE_ID=NODE.ID "+
						 "and NODE.ID=USER_NODE.NODE_ID "+
						 "and USER_NODE.USER_ID='@USER_ID' "+
						 "and NODE.FLAG in('@DATAFLAG','C') "+
						 "and NODE_STEP.FLAG='@FLAG' "+
						 "ORDER BY  CAST(USER_NODE.SEQ AS INT)  ASC, CAST(STEP.SEQ AS INT) ASC";
			sql=sql.replace("@FLAG", flag);
		}
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@USER_ID", userID);
	 	sql=sql.replace("@SYSTEM_ID", systemID);
	 	sql=sql.replace("@DATAFLAG", dataFlag);
		return sql;
	}
}
