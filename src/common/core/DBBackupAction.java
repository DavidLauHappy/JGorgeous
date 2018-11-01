package common.core;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

import resource.Context;
import resource.SecurityCenter;

import utils.DateUtil;
import utils.SqlServer;

import model.LOCALNODE;

import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;

/**
 * @author Administrator
 *  数据库实例备份,全库备份 
 */
public class DBBackupAction extends  Action{
	
	public LOCALCOMMANDBean cmd;
	public DBBackupAction(LOCALCOMMANDBean cmd){
		this.cmd=cmd;
	}
	
	private String resultInfo="";
	public  String getResultInfo(){
		return resultInfo;
	}

	public String installedMD5="";
	public String getInstallFileMd5(){
		return this.installedMD5;
	}
	
	public boolean execute(){
		boolean result=true;
		try{
			Map<String,String> parameters=cmd.getParameters();
			 String versionID=parameters.get("VERSION_ID");
			 String ip=parameters.get("IP");
			 String nodeID=parameters.get("NODE_ID");
			 String dbuser=parameters.get("DB_USER");
			 String dbname=parameters.get("DB_NAME");
			 String dbpasswdRaw=parameters.get("DB_PASSWD");
			 String dbpasswd=SecurityCenter.getInstance().decrypt(dbpasswdRaw, Context.EncryptKey);
			 LOCALNODEBean node=LOCALNODE.getNode(nodeID);
			 String backPath="";
			 String sql="";
			 String backDbname="";
			 if("1".equals(node.getOs())){
				 backPath="/home";
				  if("0".equals(node.getDbType())){
					  backDbname=dbname+"_"+DateUtil.getCurrentDate("yyyyMMddHHmmss")+".bak";
					  sql= "backup database "+dbname+" to disk=? with init";// SQL语句  
					  backPath=backPath+"/"+backDbname;
				  }else{
					  backDbname=dbname+"_"+DateUtil.getCurrentDate("yyyyMMddHHmmss")+".dmp";  
					  sql= "exp @dbuser/@passwd@#dbname "+" file= ? "+"full=y";// SQL语句  
					  backPath=backPath+"/"+backDbname;
				  }
			 }else{
				 backPath="C:\\share";
				 if("0".equals(node.getDbType())){
					 backDbname=dbname+"_"+DateUtil.getCurrentDate("yyyyMMddHHmmss")+".bak";
					 sql= "backup database "+dbname+" to disk=? with init";// SQL语句  
					 backPath=backPath+File.separator+backDbname;
				  }else{
					  backDbname=dbname+"_"+DateUtil.getCurrentDate("yyyyMMddHHmmss")+".dmp";  
					  sql= "exp @dbuser/@passwd@#dbname "+" file= ? "+"full=y";// SQL语句  
					  backPath=backPath+File.separator+backDbname;
				  }
			 }
			 sql=sql.replace("@dbuser", dbuser);
			 sql=sql.replace("@passwd", dbpasswd);
			 sql=sql.replace("#dbname", dbname);
			 String operation="在节点["+ip+"]上备份数据库"+dbname+"到文件"+backPath;
			 Connection con=null;
			 	try{
				 	con=SqlServer.getInstance().getConnection(node.getIp(),node.getDbPort(), dbuser, dbpasswdRaw, node.getDbName(),node.getDbType());
				 	 PreparedStatement ps=con.prepareStatement(sql);
				 	 ps.setString(1, backPath);// path必须是绝对路径  
					 ps.execute(); // 备份数据库  
					 ps.close();
					 this.resultInfo=	operation+"成功完成";
			 	}catch(Exception e){
			 		result=false;
			 		this.resultInfo=operation+"异常："+e.toString();
			 	}finally{
			 		if(con!=null)
			 			con.close();
			 	}
		}catch(Exception e){
			result=false;
	 		this.resultInfo="备份数据库异常："+e.toString();
		}
		return result;
	}

}
