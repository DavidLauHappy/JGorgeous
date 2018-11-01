package sql;

public class COMMANDSql {
	
	public static String getAdd(String pkgID,String stepID,String fileID,String nodeID,String id,String name,String parameter,String seq,String status,String flag,String remote,String remind,String apprID,String userID){
		String sql="insert into COMMAND(PKG_ID,STEP_ID,FILE_ID,NODE_ID,ID,NAME,PARAMETER,SEQ,STATUS,FLAG,REMOTE,CRT_TIME,REMIND,APPRID,USER_ID,MDF_TIME) values"+
						"('@PKG_ID','@STEP_ID','@FILE_ID','@NODE_ID','@ID','@NAME','@PARAMETER','@SEQ','@STATUS','@FLAG','@REMOTE',@CRT_TIME,'@REMIND','@APPRID','@USER_ID',@MDF_TIME)";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@STEP_ID", stepID);
		sql=sql.replace("@FILE_ID", fileID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@ID", id);
		sql=sql.replace("@NAME", name);
		sql=sql.replace("@PARAMETER", parameter);
		sql=sql.replace("@SEQ", seq);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@REMOTE", remote);
		sql=sql.replace("@REMIND", remind);
		sql=sql.replace("@APPRID", apprID);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String hasCommand(String pkg,String node,String status,String flag){
		String sql="SELECT COUNT(*) AS COUNT FROM COMMAND WHERE PKG_ID='@PKG_ID' and NODE_ID='@NODE_ID' and STATUS='@STATUS' and FLAG='@FLAG' ";
		sql=sql.replace("@PKG_ID", pkg);
		sql=sql.replace("@NODE_ID", node);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	public static String hasNoCommand(String pkg,String node,String status,String flag){
		String sql="SELECT COUNT(*) AS COUNT FROM COMMAND WHERE PKG_ID='@PKG_ID' and NODE_ID='@NODE_ID' and STATUS<>'@STATUS' and FLAG='@FLAG' ";
		sql=sql.replace("@PKG_ID", pkg);
		sql=sql.replace("@NODE_ID", node);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@FLAG", flag);
    	return sql;
	}
	
	public static String getUptStatus(String pkgID,String nodeID,String status){
		String sql="update COMMAND set STATUS='@STATUS',MDF_TIME=@MDF_TIME where PKG_ID='@PKG_ID' and NODE_ID='@NODE_ID' and REMIND='0'";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getUptStatusByID(String cmdID,String status){
		String sql="update COMMAND set STATUS='@STATUS',MDF_TIME=@MDF_TIME where ID='@ID'";
		sql=sql.replace("@ID", cmdID);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getUptRemindByID(String cmdID,String remind){
		String sql="update COMMAND set REMIND='@REMIND',MDF_TIME=@MDF_TIME where ID='@ID'";
		sql=sql.replace("@ID", cmdID);
		sql=sql.replace("@REMIND", remind);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getReUptStatus(String pkgID,String nodeID,String status){
		String sql="update COMMAND set STATUS='@STATUS',MDF_TIME=@MDF_TIME where PKG_ID='@PKG_ID' and NODE_ID='@NODE_ID' and REMIND='0' and  STATUS='@RESCHEED_STATUS'";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@RESCHEED_STATUS", "8");
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
	public static String getScheListByNode(String pkgID,String nodeID){
		String sql="SELECT  PKG_ID,STEP_ID,FILE_ID,NODE_ID,ID,NAME,PARAMETER,SEQ,STATUS,FLAG,REMOTE,CRT_TIME,LOGINFO,RET_TIME,REMIND,APPRID,USER_ID,MDF_TIME "+
						 "FROM COMMAND WHERE PKG_ID='@PKG_ID' and NODE_ID='@NODE_ID' and STATUS in('0','1','2','7')  and FLAG='1' and SEQ<>'0' ORDER BY CAST(SEQ as int) ASC";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", nodeID);
    	return sql;
	}
	
	public static String getByNode(String pkgID,String nodeID){
		String sql="SELECT  PKG_ID,STEP_ID,FILE_ID,NODE_ID,ID,NAME,PARAMETER,SEQ,STATUS,FLAG,REMOTE,CRT_TIME,LOGINFO,RET_TIME,REMIND,APPRID,USER_ID,MDF_TIME "+
						 "FROM COMMAND WHERE PKG_ID='@PKG_ID' and NODE_ID='@NODE_ID'  and FLAG='1' and SEQ<>'0' ORDER BY CAST(SEQ as int) ASC";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", nodeID);
    	return sql;
	}
	
	public static String getCmdByID(String id){
		String sql="SELECT  PKG_ID,STEP_ID,FILE_ID,NODE_ID,ID,NAME,PARAMETER,SEQ,STATUS,FLAG,REMOTE,CRT_TIME,LOGINFO,RET_TIME,REMIND,APPRID,USER_ID,MDF_TIME "+
				 "FROM COMMAND WHERE ID='@ID'";
			sql=sql.replace("@ID", id);
			return sql;
	}
	public static String getUptExecuteInfoByID(String cmdID,String status,String logInfo,String remind){
		String sql="update COMMAND set STATUS='@STATUS',LOGINFO='@LOGINFO',REMIND='@REMIND',RET_TIME=@RET_TIME,MDF_TIME=@MDF_TIME where ID='@ID'";
		sql=sql.replace("@ID", cmdID);
		sql=sql.replace("@STATUS", status);
		sql=sql.replace("@LOGINFO", logInfo);
		sql=sql.replace("@REMIND", remind);
		sql=sql.replace("@RET_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
	}
	
    public static String getCount(String pkgID,String nodeID,String status){
    	String sql="SELECT COUNT(*) COUNT FROM COMMAND WHERE PKG_ID='@PKG_ID' and  NODE_ID='@NODE_ID' and STATUS='@STATUS' and REMOTE='1' and FLAG='1'";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@STATUS", status);
		return sql;
    }
	
    
    public static String getDelete(String pkgID,String node){
    	String sql="delete COMMAND  where PKG_ID='@PKG_ID' and NODE_ID='@NODE_ID'";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", node);
    	return sql;
    }
    
    public static String getUptFlag(String pkgID,String systemID,String flag,String dataFlag){
    	String sql="update  COMMAND  SET FLAG='@FLAG',MDF_TIME=@MDF_TIME where PKG_ID='@PKG_ID' and "+
    					"exists(select 1 from NODE where NODE.ID=COMMAND.NODE_ID and NODE.FLAG='@DATAFLAG' and NODE.SYSTEM_ID='@SYSTEM_ID')";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@DATAFLAG", dataFlag);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
    }
    
    public static String getFlagSet(String pkgID,String nodeID,String stepID,String flag){
    	String sql="update  COMMAND  SET FLAG='@FLAG',MDF_TIME=@MDF_TIME where PKG_ID='@PKG_ID' and "+
				" STEP_ID='@STEP_ID' and NODE_ID='@NODE_ID'";
				sql=sql.replace("@PKG_ID", pkgID);
				sql=sql.replace("@NODE_ID", nodeID);
				sql=sql.replace("@FLAG", flag);
				sql=sql.replace("@STEP_ID", stepID);
				sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
				return sql;
    }
    
    public static String getUptFlag(String apprID,String userID,String flag){
    	String sql="update  COMMAND  SET FLAG='@FLAG',MDF_TIME=@MDF_TIME where APPRID='@APPRID' and USER_ID='@USER_ID'";
		sql=sql.replace("@APPRID", apprID);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@FLAG", flag);
		sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
    	return sql;
    }
    
    public static String getReport(String pkgID,String systemID,String nodeID,String flag){
    	String sql="select COMMAND.SEQ SEQ,COMMAND.PKG_ID PKG_ID, SYSTEM.NAME SYSTEM_NAME,NODE.NAME NODE_NAME,NODE.IP IP,COMMAND.NAME NAME,COMMAND.STATUS STATUS,COMMAND.LOGINFO LOGINFO,COMMAND.RET_TIME RET_TIME "+
    					" from COMMAND,NODE,SYSTEM "+
    					"where COMMAND.PKG_ID='@PKG_ID' and COMMAND.NODE_ID=NODE.ID "+
    					"and ('@NODE_ID'='' OR NODE.ID='@NODE_ID') "+
    					"and ('@SYSTEM_ID'='' OR SYSTEM.BUSSID='@SYSTEM_ID') "+
    					"and NODE.FLAG in('@FLAG','C') and SYSTEM.FLAG='@FLAG' "+
    					"ORDER BY NODE.ID,CAST(COMMAND.SEQ AS INT)";
		sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@NODE_ID", nodeID);
		sql=sql.replace("@SYSTEM_ID", systemID);
		sql=sql.replace("@FLAG", flag);
		return sql;
    }
    
    public static String getProgress(String pkgID,String systemID,String flag,String userID){
    	String sql="SELECT COUNT(*) TOTAL, ISNULL(SUM((CASE COMMAND.STATUS WHEN '5' THEN 1  ELSE 0 END)),0) AS DONE "+
    			"FROM COMMAND,NODE "+
    			"WHERE COMMAND.PKG_ID='@PKG_ID' "+
    				"AND COMMAND.USER_ID='@USER_ID' "+
					"AND COMMAND.NODE_ID=NODE.ID "+
					"AND NODE.FLAG in('@FLAG','C') "+
					"AND NODE.SYSTEM_ID='@SYSTEM_ID' "+
					"AND COMMAND.SEQ<>'0' "+
					"AND COMMAND.FLAG='1'";
	    	sql=sql.replace("@PKG_ID", pkgID);
	    	sql=sql.replace("@USER_ID", userID);
			sql=sql.replace("@SYSTEM_ID", systemID);
			sql=sql.replace("@FLAG", flag);
			return sql;
    }
    
    public static String getReminds(String pkgID,String userID,int fetchRows){
    	String sql="select top @num PKG_ID,STEP_ID,FILE_ID,NODE_ID,ID,NAME,PARAMETER,SEQ,STATUS,FLAG,REMOTE,CRT_TIME,LOGINFO,RET_TIME,REMIND,APPRID,USER_ID,MDF_TIME "+
    						"from COMMAND "+
    			"where PKG_ID='@PKG_ID' and USER_ID='@USER_ID' and REMIND='1' and SEQ<>'0' and FLAG='1'  ORDER BY CAST(SEQ AS INT) ";
    	sql=sql.replace("@PKG_ID", pkgID);
		sql=sql.replace("@USER_ID", userID);
		sql=sql.replace("@num", fetchRows+"");
		return sql;
    }
    
    public static String getMyCmdOver(String pkgID,String userID){
    	String sql="SELECT NODE_ID,COUNT(*) TOTAL, ISNULL(SUM((CASE COMMAND.STATUS WHEN '5' THEN 1  ELSE 0 END)),0) AS DONE "+
    			"FROM COMMAND "+
    			"WHERE PKG_ID='@PKG_ID' "+
    			"AND USER_ID='@USER_ID' "+
					"AND SEQ<>'0' "+
					"AND FLAG='1' "+
					"GROUP BY NODE_ID";
	    	sql=sql.replace("@PKG_ID", pkgID);
			sql=sql.replace("@USER_ID", userID);
			return sql;
    }
    
    public static String getMyStaticsticFile(String pkgID,String systemID,String nodeID){
    	String sql="SELECT R.NAME NAME,COUNT(R.ID) NUM FROM "+
				  "(SELECT DISTINCT STEP.NAME NAME,PFILE.ID ID "+
							"FROM COMMAND,STEP,NODE_STEP,PFILE,NODE "+
					"WHERE COMMAND.PKG_ID='@PKG_ID' "+
						"AND COMMAND.NODE_ID='@NODE_ID' "+
						"AND COMMAND.STATUS='5' "+
						"AND COMMAND.NODE_ID=NODE.ID "+	
						"AND NODE.SYSTEM_ID='@SYSTEM_ID' "+
						"AND COMMAND.PKG_ID=NODE_STEP.PKG_ID "+
						"AND COMMAND.NODE_ID=NODE_STEP.NODE_ID "+
						"AND COMMAND.STEP_ID=NODE_STEP.STEP_ID "+
						"AND NODE.SYSTEM_ID=NODE_STEP.SYSTEM_ID "+
						"AND NODE_STEP.STEP_ID=STEP.ID "+
						"AND STEP.ID=PFILE.STEP_ID "+
						"AND COMMAND.PKG_ID=STEP.PKG_ID "+
						"AND STEP.PKG_ID=PFILE.PKG_ID) R "+
						"GROUP BY NAME";
	    	sql=sql.replace("@PKG_ID", pkgID);
			sql=sql.replace("@SYSTEM_ID", systemID);
			sql=sql.replace("@NODE_ID", nodeID);
			return sql;
    }
    
    public static String getMyDeployTask(String userID){
    	String sql="select distinct COMMAND.APPRID APPR_ID,FLOW_DETAIL.REC_STATUS STATUS,FLOW_DETAIL.SUBMIT_TIME SUBMIT_TIME "+
    			"from FLOW_DETAIL,COMMAND "+
    			"WHERE COMMAND.USER_ID='@USER_ID' "+
    			"and COMMAND.APPRID=FLOW_DETAIL.ID "+
    			"and COMMAND.FLAG='0'";
	    	sql=sql.replace("@USER_ID", userID);
			return sql;
    }
    
    public static String getPkgID(String apprID,String userID){
    	String sql="select distinct PKG_ID from COMMAND "+
    			"WHERE APPRID='@APPRID' "+
    			 "and USER_ID='@USER_ID' "+
    			"and FLAG='1'";
	    	sql=sql.replace("@APPRID", apprID);
	    	sql=sql.replace("@USER_ID", userID);
			return sql;
    }
    
    public static String getNodeID(String apprID,String userID){
    	String sql="select top 1 NODE_ID from COMMAND "+
    			"WHERE APPRID='@APPRID' "+
    			 "and USER_ID='@USER_ID' "+
    			"and FLAG='1'";
	    	sql=sql.replace("@APPRID", apprID);
	    	sql=sql.replace("@USER_ID", userID);
			return sql;
    }
}
