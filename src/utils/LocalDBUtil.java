package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import resource.Logger;
import resource.Paths;



public class LocalDBUtil {
	private  static Connection conn;
	public static Connection getLocalConnection()
	{
	   if(conn==null)
	   {
		   conn=createConnect();
	   }
	   return conn;
	}
	
	private  static Connection createConnect()
	{
		  // JDBC驱动
		  String classname = "org.sqlite.JDBC";
		  String path=Paths.getInstance().getLocalDatabasePath()+"data.db";
		  // 数据库地址
		  String URL = "jdbc:sqlite:"+path;
				try {
					// 加载JDBC驱动
					Class.forName(classname);
					// 连接数据库
					conn = DriverManager.getConnection(URL);
				} 
				catch (Exception e) 
				{
					Logger.getInstance().error("establish local database connection error:"+e.toString());
					e.printStackTrace();
				}
		  return conn;
	 }
	
	//系统推出或者长时间占用数据库操作完成后记得释放
	public static void freeConnection(Statement pst,ResultSet rs)
	{
		try {
			rs.close();
			rs=null;
			pst.close();
			pst=null;
			conn.close();
			conn=null;
		} catch (SQLException e) {	
			Logger.getInstance().error("free local database connection error:"+e.toString());
			e.printStackTrace();
		}
	}
	
		//系统退出或者长时间占用数据库操作完成后记得释放
		public static void freeConnection()
		{
			try {
				conn.close();
				conn=null;
			} catch (SQLException e) {	
				Logger.getInstance().error("free local database connection error:"+e.toString());
				e.printStackTrace();
			}
		}
	
	//读取数据库数据时，多条记录才有列表的方式，
	//每条记录的不同字段之间使用(char)29[不可见字符]号隔开，
	public static List query(String sql)
	{
		Statement stmt=null;
		ResultSet rs=null;
		List resultList=new ArrayList();		
		try
		{
			if(conn==null){
				conn=	createConnect();
			}
			 stmt=conn.createStatement();
			 rs=stmt.executeQuery(sql);
			 ResultSetMetaData rsmd=rs.getMetaData();
			 int columns=rsmd.getColumnCount();
			 while(rs.next())
			 {
				 String curValue="";
	              for(int k=1;k<=columns;k++)
	              {
					try {	   
						    String value=rs.getString(k);//String value=new String(rs.getBytes(k),"UTF-8");
							if(StringUtil.isNullOrEmpty(value)){
								  value="N/A";
							}else{
								value=value.trim();
							}
							curValue+=value+(char)29;//curValue+=value+"|";
					} catch (Exception e) {
						e.printStackTrace();
					}	 
	              }
				curValue=curValue.substring(0,curValue.length()-1);
				Logger.getInstance().debug("data line from database :"+curValue);
				resultList.add(curValue);
		     }
			
		}
		 catch(Exception e)
		   {
			 Logger.getInstance().error("query local database data  error:"+"["+sql+"]"+e.toString());
			  e.printStackTrace();
		   }
		   finally { }
		return resultList;
	}
	
	//根据表中数据获取最大记录，实现伪自增的序列
	//注意对同一个表多次取，而没有马上发生更新或者回写操作做，序列将不能自增，引起异常
	public synchronized  static String getIncrementSeqNo(String tableName,String columnName){
		String sql="SELECT MAX(CAST(@COLUMN AS INT)) FROM '@TABLENAME'";
		sql=sql.replaceAll("@COLUMN", columnName);
		sql=sql.replaceAll("@TABLENAME", tableName);
		Logger.getInstance().debug("execute sequece sql:="+sql);
		
		Statement stmt=null;
		ResultSet rs=null;
		String result="";
		try{
			if(conn==null){
				conn=	createConnect();
			}
			 stmt=conn.createStatement();
			 rs=stmt.executeQuery(sql);
			 while(rs.next()){		
				 try {
				       result=rs.getString(1);
					} catch (Exception e) {
						result="";
						e.printStackTrace();
					}	               
		     }
			 if(result!=null) {
				 int x=Integer.parseInt(result);
				 x=x+1;
				 result=String.valueOf(x); 
			 } else {
				 result="1";
			 }
		}
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }
	   finally { }
		  return result;
	}
	
	public synchronized  static boolean update(Connection con,String sql)
	{
		int ret =0;
		try {
			 Statement stmt=con.createStatement();
			 ret=stmt.executeUpdate(sql);
			 //con.commit();
			 if(ret==1 )
			 return true;
		} catch (SQLException e) {
			Logger.getInstance().error("update local database  error:"+e.toString());
			e.printStackTrace();
		}		
        return false;
	}
	
	public synchronized static boolean update(String sql){
		Logger.getInstance().debug("execute sequece sql:="+sql);
		int ret =0;
		try {
			if(conn==null){
				conn=	createConnect();
			}
			 Statement stmt=conn.createStatement();
			 ret=stmt.executeUpdate(sql);
			 //conn.commit();
			 if(ret==1 )
			 return true;
		} catch (SQLException e) {
			Logger.getInstance().error("update local database  error:"+e.toString());
			e.printStackTrace();
		}		
        return false;
	}
	
	

}
