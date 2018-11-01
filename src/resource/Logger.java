package resource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import common.db.DBConnectionManager;

import utils.DateUtil;
import utils.FileUtils;
import utils.SqlServerUtil;
import utils.StringUtil;

public class Logger {

	private static Logger unique_instance;
	public static Logger getInstance()
	{
		if(unique_instance==null)
		{
			unique_instance=new Logger();
		}
		return unique_instance;	
	}
	private Logger(){}
	
	public synchronized void log(String info){
		try {
			String logDir=Paths.getInstance().getLoggingPath()+DateUtil.getCurrentDate("yyyyMMdd");
		    File dir=new File(logDir);
		    if(!dir.exists())
		    	dir.mkdirs();
			   String logpath=logDir+File.separator+DateUtil.getCurrentDate()+".log";
			   String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
			   File file=new File(logpath);
			   FileWriter fw = new FileWriter(file,true);
			   BufferedWriter bw = new BufferedWriter(fw);
			   bw.write(currentTime);
			   bw.write(":");
			   bw.write(info);
			   bw.write("\r\n");
			   bw.close();
			   fw.close();
	           }
		catch (Exception e1) {
		e1.printStackTrace();
        }
	}
	
	
	/*日志按服务/按线程/按日期分开
	 * 同步日志，保证日志一定能留下，再考虑优化效率
	 * */
	public synchronized void serviceLog(String serviceNo,String info){
		try{
			String logDir=Paths.getInstance().getLoggingPath()+DateUtil.getCurrentDate("yyyyMMdd");
			//String logDir="C:\\Users\\David\\workspace\\Gorgeous\\log\\"+DateUtil.getCurrentDate("yyyyMMdd");
			File dir=new File(logDir);
		    if(!dir.exists())
		    	dir.mkdirs();
		    String logPath=logDir+File.separator+ serviceNo+".log";
			   String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
			   File file=new File(logPath);
			   FileWriter fw = new FileWriter(file,true);
			   BufferedWriter bw = new BufferedWriter(fw);
			   bw.write(currentTime);
			   bw.write(":");
			   bw.write(info);
			   bw.write("\r\n");
			   bw.close();
			   fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized void logDBInstall(String filePath,String filename,String log){
		try{
		  String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
		   File file=new File(filePath);
		   FileWriter fw = new FileWriter(file,true);
		   BufferedWriter bw = new BufferedWriter(fw);
		   bw.write(currentTime);
		   bw.write(":");
		   bw.write(filename);
		   bw.write("\r\n");
		   bw.write(currentTime);
		   bw.write(":");
		   log=log.replaceAll("&", "\r\n");
		   bw.write(log);
		   bw.write("\r\n");
		   bw.close();
		   fw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
        }
	}
	public synchronized void debug(String info){
		if(Context.EnableDebug){
				try {
					String logDir=Paths.getInstance().getLoggingPath()+DateUtil.getCurrentDate("yyyyMMdd");
				    File dir=new File(logDir);
				    if(!dir.exists())
				    	dir.mkdirs();
					   String logpath=logDir+File.separator+DateUtil.getCurrentDate()+".debug";
					   String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
					   File file=new File(logpath);
					   FileWriter fw = new FileWriter(file,true);
					   BufferedWriter bw = new BufferedWriter(fw);
					   bw.write(currentTime);
					   bw.write(":");
					   bw.write(info);
					   bw.write("\r\n");
					   bw.close();
					   fw.close();
			           }
				catch (Exception e1) {
				e1.printStackTrace();
		     }
		}
	}
	
	public synchronized void error(String info){
		try {
				String logDir=Paths.getInstance().getLoggingPath()+DateUtil.getCurrentDate("yyyyMMdd");
			    File dir=new File(logDir);
			    if(!dir.exists())
			    	dir.mkdirs();
			   String logpath=logDir+File.separator+DateUtil.getCurrentDate()+".err";
			   String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
			   File file=new File(logpath);
			   FileWriter fw = new FileWriter(file,true);
			   BufferedWriter bw = new BufferedWriter(fw);
			   bw.write(currentTime);
			   bw.write(":");
			   bw.write(info);
			   bw.write("\r\n");
			   bw.close();
			   fw.close();
	           }
		catch (Exception e1) {
		e1.printStackTrace();
     }
	}
	
	public static void startInfo(String info){
		try {
			  
			   String dirPath= FileUtils.formatPath(System.getProperty("user.dir"))+File.separator+"log";
			   String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
			   File dir=new File(dirPath);
				   dir.mkdirs();
			   String logpath=dir.getAbsolutePath()+File.separator+"run.log";
			   File file=new File(logpath);
			   FileWriter fw = new FileWriter(file,true);
			   BufferedWriter bw = new BufferedWriter(fw);
			   bw.write(currentTime);
			   bw.write(":");
			   bw.write(info);
			   bw.write("\r\n");
			   bw.close();
			   fw.close();
	           }
		catch (Exception e1) {
		e1.printStackTrace();
		}
	}
	
	//日志记录到数据库服务器上
	public synchronized void logServer(String detail){
		Context.session.detail=detail;
		Connection conn=null;
		try{
			String sqlID="SELECT NEXT VALUE FOR SEQ_LOG_ID as ID";
			conn=DBConnectionManager.getInstance().getConnection();
			String seqID="";
			List sqlResult=SqlServerUtil.executeQuery(sqlID, conn);
			if(sqlResult!=null&&sqlResult.size()>0){
				Map dataLine=(Map)sqlResult.get(0);
				seqID=dataLine.get("ID")+"";
			}
			if(!StringUtil.isNullOrEmpty(seqID)){
				seqID=StringUtil.leftpad(seqID, 5, "0");
				seqID=DateUtil.getCurrentDate("yyyyMMddHHmmss")+seqID;
				String insertSql="insert into LOGS(ID,USER_ID,FUNC_ID,LOG_INFO,CHECK_USER,LOG_TIME) "+
										   "values('@ID','@USER_ID','@FUNC_ID','@LOG_INFO','@CHECK_USER',@LOG_TIME)";
				insertSql=insertSql.replace("@ID", seqID);
				insertSql=insertSql.replace("@USER_ID", Context.session.userID);
				insertSql=insertSql.replace("@FUNC_ID", Context.session.currentFunctionID);
				insertSql=insertSql.replace("@LOG_INFO", Context.session.detail);
				insertSql=insertSql.replace("@CHECK_USER", Context.session.cuurentChecker);
				insertSql=insertSql.replace("@LOG_TIME", "CONVERT(varchar(100),GETDATE(),120)");
				int count=SqlServerUtil.executeUpdate(insertSql, conn);
			}
		}catch(Exception e){
			this.error("执行远程日志记录异常:"+e.toString());
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
	}
}
