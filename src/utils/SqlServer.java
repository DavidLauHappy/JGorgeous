package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.DbObj;

import resource.Context;
import resource.Logger;
import resource.SecurityCenter;

/**
 * @author David
 * SQLServer 数据库底层操作封装
 * 查询、更新、修改，以语句为单位
 */
/**
 * @author Administrator
 *
 */
public class SqlServer {
	
	private static SqlServer unique_instance;
	private  Connection con;
	public static SqlServer getInstance(){
		if(unique_instance==null)
			unique_instance=new SqlServer();
		return unique_instance;
	}
	
	private SqlServer(){}
	
	public Connection getConnection(String nodeServer,String serverPort,String dbuser,String passwd,String dbname,String dbType){	
		Connection con=null;
		String url="";
		String DriverName="";
		try{
			 if("0".equals(dbType)){
				 DriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
				  url="jdbc:sqlserver://"+nodeServer+":"+serverPort+";DatabaseName="+dbname;
			 }else{
				 DriverName = "oracle.jdbc.driver.OracleDriver"; 
				  url="jdbc:oracle:thin:@"+nodeServer+":"+serverPort+":"+dbname;
			 }
			Class.forName(DriverName);  
			String passWord=SecurityCenter.getInstance().decrypt(passwd, Context.EncryptKey);
			con = DriverManager.getConnection(url, dbuser, passWord);  
		}catch(Exception e){
			Logger.getInstance().error("SqlServer.getConnection()获取数据库对象【"+url+"】内容异常："+e.toString());
		}
		return con;
	}
	
	public boolean testConnection(String dbType,String nodeServer,String serverPort,String dbuser,String passwd,String dbname){
		Connection con=null;
		String url="";
		String DriverName="";
		try{
			 if("0".equals(dbType)){
				 DriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
				  url="jdbc:sqlserver://"+nodeServer+":"+serverPort+";DatabaseName="+dbname;
			 }else{
				 DriverName = "oracle.jdbc.driver.OracleDriver"; 
				  url="jdbc:oracle:thin:@"+nodeServer+":"+serverPort+":"+dbname;
			 }
			Class.forName(DriverName);  
			String passWord=SecurityCenter.getInstance().decrypt(passwd, Context.EncryptKey);
			con = DriverManager.getConnection(url, dbuser, passWord);  
			return true;
		}catch(Exception e){
			Logger.getInstance().error("连接到数据库异常："+e.toString());
		}finally{
			try{
				if(con!=null)
					con.close();
			}catch(Exception e){
				
			}
		}
		return false;
	}
	
	public List<DbObj> getDbObjects(String dbType,String nodeServer,String serverPort,String dbuser,String passwd,String dbname){
		Connection con=null;
		String url="";
		String sql="";
		String DriverName="";
		 Statement stmt=null;
		 List<DbObj> result=new ArrayList<DbObj>();
		try{
			 if("0".equals(dbType)){
				 DriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
				  url="jdbc:sqlserver://"+nodeServer+":"+serverPort+";DatabaseName="+dbname;
				  sql="select name,type  from sys.objects where type in('U','P') order by type,name";
			 }else{
				 DriverName = "oracle.jdbc.driver.OracleDriver"; 
				  url="jdbc:oracle:thin:@"+nodeServer+":"+serverPort+":"+dbname;
				  sql="select object_name name, OBJECT_TYPE type from USER_OBJECTS where OBJECT_TYPE IN('TABLE','PROCEDURE','PACKAGE BODY')";
			 }
			Class.forName(DriverName);  
			String passWord=SecurityCenter.getInstance().decrypt(passwd, Context.EncryptKey);
			con = DriverManager.getConnection(url, dbuser, passWord);  
			 stmt=con.createStatement();
			 boolean hasResult = stmt.execute(sql);
	         if(hasResult){
	        	 ResultSet rs = stmt.getResultSet();
	        	 String type="";
	        	 while (rs.next()) {
	            	 String NAME=rs.getString(1).trim();
	            	 String TYPE=rs.getString(2).trim();
	            	 if("U".equalsIgnoreCase(TYPE)||"TABLE".equalsIgnoreCase(TYPE)){
	            		 type=DbObj.Type.Table.ordinal()+"";
	            	 }else{
	            		 type=DbObj.Type.Proc.ordinal()+"";
	            	 }
	            	 DbObj obj=new DbObj(NAME,type);
	            	 result.add(obj);
	        	 }
	         }
			  
		}catch(Exception e){
			Logger.getInstance().error("连接到数据库异常："+e.toString());
		}finally{
			try{
				if(con!=null)
					con.close();
			}catch(Exception e){
				
			}
		}
		return result;
	}
	
/*	private  Connection   createTestConnect(){
		Connection con=null;
		try{
			String DriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  
			String url="jdbc:sqlserver://172.24.178.241:1433"+";DatabaseName=Gorgeous";
			//String DriverName = "oracle.jdbc.driver.OracleDriver";  
			//String url="jdbc:oracle:thin:@172.24.183.105:1521:YGZ";
			
			Class.forName(DriverName);  
			//con = DriverManager.getConnection(url, "DAVID", "david");  
			con = DriverManager.getConnection(url, "sa", "Gorgeous1125");  
		}
		catch(Exception exp){
			Logger.getInstance().error("连接到SqlServer数据库异常："+exp.toString());
		}
		return con;
	}*/
	


/*public static void main(String[] args){
		try{
			Connection con=SqlServer.getInstance().createTestConnect();
			String table=DataHelper.getDbTableText(con, "NODE","0");
			System.out.println(table);
			if(con!=null)
				con.close();
		}
		catch(Exception exp){
			
		}
	}*/
}
