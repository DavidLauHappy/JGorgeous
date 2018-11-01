package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import resource.Context;
import resource.Logger;

public class SqlServerUtil {

	public static List executeQuery(String sql,Connection conn){
		Logger.getInstance().serviceLog(Context.ServiceDbPool,"执行sql:"+sql);
		List result=new ArrayList();
		Statement stmt=null;
		ResultSet rs=null;
		try{
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql);
			ResultSetMetaData rsmd=rs.getMetaData();
			while(rs.next()){
				Map map=new HashMap();
				for(int i=1;i<=rsmd.getColumnCount();i++){
					map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				result.add(map);
			}	
		}catch(Exception e){
			Logger.getInstance().serviceLog(Context.ServiceDbPool,"执行sql:"+sql+"异常:"+e.toString());
			Logger.getInstance().error("执行sql:"+sql+"异常:"+e.toString());
			throw new RuntimeException("执行["+sql+"]异常："+e.toString());
		}finally{
			if(rs!=null){
				try{
					if (rs != null) rs.close();
					if (stmt != null) stmt.close();
				}catch(Exception e){
					Logger.getInstance().serviceLog(Context.ServiceDbPool,"执行sql:"+sql+"回收资源异常常:"+e.toString());
					Logger.getInstance().error("执行sql:"+sql+"回收资源异常常:"+e.toString());
					throw new RuntimeException("执行["+sql+"]回收资源异常："+e.toString());
				}
			}
		}
		return result;
	}
	
	public static int executeUpdate(String sql,Connection conn){
		Logger.getInstance().serviceLog(Context.ServiceDbPool,"执行sql:"+sql);
		int result=0;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			stmt=conn.createStatement();
			result=stmt.executeUpdate(sql);
		}catch(Exception e){
			Logger.getInstance().serviceLog(Context.ServiceDbPool,"执行sql:"+sql+"异常:"+e.toString());
			Logger.getInstance().error("执行sql:"+sql+"异常:"+e.toString());
			throw new RuntimeException("执行["+sql+"]异常："+e.toString());
		}finally{
			if(rs!=null){
				try{
					if (rs != null) rs.close();
					if (stmt != null) stmt.close();
				}catch(Exception e){
					Logger.getInstance().serviceLog(Context.ServiceDbPool,"执行sql:"+sql+"回收资源异常常:"+e.toString());
					Logger.getInstance().error("执行sql:"+sql+"回收资源异常常:"+e.toString());
					throw new RuntimeException("执行["+sql+"]回收资源异常："+e.toString());
				}
			}
		}
		return result;
	}
	
	/*
	public static String[][] executeProc(String procname, Vector input, Vector output,Connection con){
		String[][] rsdatas = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try{
				StringBuffer params=new StringBuffer();
				int in = input.size();
				int out = output.size();
				int pos = 0;
				if (in + out > 1) {
					for (int k = 0; k < in + out; k++) { // 拼凑存储过程使用的参数个数，包括入参和出参
						if (k == (in + out - 1))
							params.append("?)");
						else if (k == 0)
							params.append("(?, ");
						else
							params.append("?, ");
					}
				} else { // 处理只有一个参数的情况
					params.append("(?)");
				}
				cs = con.prepareCall("{ call " + procname + params.toString() + " }");
				int i = 1;
				for (; i <= in; i++) {
					cs.setString(i, (String) input.get(i - 1));
				}
				for (; i <= (in + out); i++) {
					String typename = (String) output.get(i - in - 1);
					if ("CURSOR".equals(typename)) {
						cs.registerOutParameter(i, Types.OTHER);
						pos = i;
					} else if ("VARCHAR".equals(typename)) {
						cs.registerOutParameter(i, Types.VARCHAR);
						pos = 0;
					}
				}
				cs.execute();	
				//检查存储过程是否成功执行
				String retcode = "";
				String retmsg = "";
				if(pos==0) { //无游标
					rsdatas = new String[1][out];
					for (int j = in; j < in+out; j++) {
						rsdatas[0][j-in] = cs.getString(j+1);
					}
					retcode = rsdatas[0][0];
					try {retmsg = rsdatas[0][1];} catch(Exception e) {}
					if(!"0".equals(retcode)) {
						if("1".equals(retcode)){
							rsdatas = null;
							rsdatas = new String[][]{{"info", retmsg}};											
						}else {
							rsdatas = null;
							rsdatas = new String[][]{{"error", "执行存储过程["+procname+"]发生错误：["+retcode+"], ["+retmsg+"]"}};												
						}
						return rsdatas;
					}
				}else {//返回游标处理
					retcode = cs.getString(i - out);				
					try {retmsg = cs.getString(i-out+1);} catch(Exception e) {}
					if(!"0".equals(retcode)) {
						if("1".equals(retcode)){
							rsdatas = null;
							rsdatas = new String[][]{{"info", retmsg}};											
						}else {
							rsdatas = null;
							rsdatas = new String[][]{{"error", "执行存储过程["+procname+"]发生错误：["+retcode+"], ["+retmsg+"]"}};												
						}
						return rsdatas;
					}				
					int count = cs.getInt(i - 2);
					int idx = 0;
					if(count==0) 
						{
						rsdatas= new String[][]{{"nodata", "游标无数据"}};  
						return rsdatas; // 游标没有数据
						}
					rsdatas = new String[count][];
					if (pos != 0) {
						rs = (ResultSet) cs.getObject(pos);
						ResultSetMetaData rsm = rs.getMetaData();
						int cols = rsm.getColumnCount();
						while (rs.next()) {
							rsdatas[idx] = new String[cols];
							for (int z = 1; z <= cols; z++) {
								rsdatas[idx][z-1] = rs.getString(z);
							}
							idx++;
						}
					}
				}
		}catch(Exception e){
			rsdatas = null;
			rsdatas = new String[][]{{"error", "系统未知错误：["+e.getMessage()+"]"}};
			throw new RuntimeException("SqlServer.query()数据库执行【"+"】异常："+e.toString());
		}finally {
			try {
				if (rs != null) rs.close();
				if (cs != null) cs.close();
			} catch (Exception e) {
				rsdatas = null;
				rsdatas = new String[][]{{"error", "释放资源时发生错误：["+e.getMessage()+"]"}};
			}
		}	
		return rsdatas;
	}*/
	
}
