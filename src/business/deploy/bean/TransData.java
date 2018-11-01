package business.deploy.bean;

import java.sql.Connection;
import java.util.UUID;

import resource.Context;

import utils.SqlServerUtil;

import common.db.DBConnectionManager;

/**
 * @author Administrator
 *  审计记录信息
 */
public class TransData {

	private String id;
	private String app;
	private String systemID;
	private String operID;
	private String funcID;
	private String nodeID;
	private String detail;
	private String operIp;
	private String operMac;
	private String operTime;
	private String endTime;
	private String uptTime;
	private String date;
	
	public TransData(){
		this.operID=Context.session.userID;
		this.operIp=Context.session.clientIp;
		this.operMac=Context.session.clientMac;
	}
	
	public void inroll(){
		String sql="insert into DEPLOY_TRANS_LOG(ID,APP,SYSTEM,OPER_ID,FUNC_ID,NODE_ID,DETAIL,OPER_IP,OPER_MAC,OPER_TIME,UPT_TIME,DATA_DATE) "+
							"values('@ID','@APP','@SYSTEM','@OPER_ID','@FUNC_ID','@NODE_ID','@DETAIL','@OPER_IP','@OPER_MAC',@OPER_TIME,@UPT_TIME,@DATA_DATE)";
		sql=sql.replace("@ID", this.id);
		sql=sql.replace("@APP", this.app);
		sql=sql.replace("@SYSTEM", this.systemID);
		sql=sql.replace("@OPER_ID", this.operID);
		sql=sql.replace("@FUNC_ID", this.funcID);
		sql=sql.replace("@NODE_ID", this.nodeID);
		sql=sql.replace("@DETAIL", this.detail);
		sql=sql.replace("@OPER_IP", this.operIp);
		sql=sql.replace("@OPER_MAC", this.operMac);
		sql=sql.replace("@OPER_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@DATA_DATE", "CONVERT(varchar(100),GETDATE(),23)");
		
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
	}

	public void setEndTime() {
		String sql="update DEPLOY_TRANS_LOG set END_TIME=@END_TIME,UPT_TIME=@UPT_TIME where ID='@ID'";
		sql=sql.replace("@ID", this.id);
		sql=sql.replace("@END_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
	}
	
	public void setId(String id) {
		String newID=id.replace("-", "");
		this.id = newID;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}

	public void setOperID(String operID) {
		this.operID = operID;
	}

	public void setFuncID(String funcID) {
		this.funcID = funcID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setOperIp(String operIp) {
		this.operIp = operIp;
	}

	public void setOperMac(String operMac) {
		this.operMac = operMac;
	}

	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}

	public void setUptTime(String uptTime) {
		this.uptTime = uptTime;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
}
