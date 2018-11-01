package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import resource.Context;
import resource.Logger;



//����api��������ϵͳ��Ϣ
public class ZenDaoApi {
	/*
	 *  �޸������������״̬
	 */
	 public  int  setReqStatus(String reqID,String stauts){
		 Connection con=null;
		 String sql="";
		 try{
			 con=this.getMySqlConnection();
			 if(con!=null){
				  sql="update zentao.zt_story set stage='@status',lastEditedBy='@mdfUser', lastEditedDate='@mdfTime' where id='@reqID'";
				  sql=sql.replace("@status",stauts);
				  sql=sql.replace("@mdfUser", "admin");//������Ҫͬ����������
				  sql=sql.replace("@mdfTime", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				  sql=sql.replace("@reqID", reqID);
				  return this.executeUpdate(sql, con);
			 }
			 else{
				 return -1;
			 }
		 }
		 catch(Exception e){
			 Logger.getInstance().error("���������ӿڸ�������״̬["+sql+"]�쳣��"+e.toString());
			 return -1;
		 }finally{
				if(con!=null)
					try {
						con.close();
					} catch (SQLException exp) {
						 Logger.getInstance().error("���������ӿڸ�������״̬["+sql+"]�ͷ���Դ�쳣��"+exp.toString());
					}
		 }
	 }
	 
	 /*
	  * ������������
	  */
	 public  int setReqPlan(String reqID,String plan){
		 Connection con=null;
		 String sql="";
		 try{
			 con=this.getMySqlConnection();
			 if(con!=null){
				  sql="update zentao.zt_projectstory set project='@project' where story='@reqID'";
				  sql=sql.replace("@project",plan);
				  sql=sql.replace("@reqID", reqID);
				  return this.executeUpdate(sql, con);
			 }
			 else{
				 return -1;
			 }
		 }
		 catch(Exception e){
			 Logger.getInstance().error("���������ӿڸ�����������["+sql+"]�쳣��"+e.toString());
			 return -1;
		 }finally{
				if(con!=null)
					try {
						con.close();
					} catch (SQLException exp) {
						 Logger.getInstance().error("���������ӿڸ�����������["+sql+"]�ͷ���Դ�쳣��"+exp.toString());
					}
		 }
	 }
	 
	 /*
	  * ������������
	  */
	 public  int setTaskPlan(String taskID,String plan){
		 Connection con=null;
		 String sql="";
		 try{
			 con=this.getMySqlConnection();
			 if(con!=null){
				  sql="update zentao.zt_task set project='@project',lastEditedBy='admin' where id='@id'";
				  sql=sql.replace("@project",plan);
				  sql=sql.replace("@id", taskID);
				  return this.executeUpdate(sql, con);
			 }
			 else{
				 return -1;
			 }
		 }
		 catch(Exception e){
			 Logger.getInstance().error("���������ӿڸ�����������["+sql+"]�쳣��"+e.toString());
			 return -1;
		 }finally{
				if(con!=null)
					try {
						con.close();
					} catch (SQLException exp) {
						 Logger.getInstance().error("���������ӿڸ�����������["+sql+"]�ͷ���Դ�쳣��"+exp.toString());
					}
		 }
	 }
		 
		 private ZenDaoApi(){};
		 private static class ZenDaoApiInstance{
				private static final ZenDaoApi unique_instance=new ZenDaoApi();
			}
		 public static ZenDaoApi getInstance(){
				return ZenDaoApiInstance.unique_instance;
			}
		 
		 private  int executeUpdate(String sql,Connection conn){
				int result=0;
				Statement stmt=null;
				ResultSet rs=null;
				Logger.getInstance().debug("ִ��sql:"+sql);
				try{
					stmt=conn.createStatement();
					result=stmt.executeUpdate(sql);
				}catch(Exception e){
					Logger.getInstance().error("ִ��["+sql+"]�쳣��"+e.toString());
				}finally{
					if(rs!=null){
						try{
							if (rs != null) rs.close();
							if (stmt != null) stmt.close();
						}catch(Exception e){
							Logger.getInstance().error("ִ��["+sql+"]������Դ�쳣��"+e.toString());
						}
					}
				}
				return result;
			}
		 
		 
		 private Connection getMySqlConnection(){
				try{
					String DriverName = "com.mysql.jdbc.Driver";  
					String ip=Context.SysParameters.get("zentaoDbServer");
					String portNo=Context.SysParameters.get("zentaoDbPortNo");
					String dbName=Context.SysParameters.get("zentaoDbName");
					String dbUser=Context.SysParameters.get("zentaoDbUser");
					String dbPasswd=Context.SysParameters.get("zentaoDbPasswd");
					String url="jdbc:mysql://"+ip+":"+portNo+"/"+dbName;
					Class.forName(DriverName);  
					Connection con = DriverManager.getConnection(url, dbUser, dbPasswd); 
					return con;
				}catch (ClassNotFoundException e) {
					throw new RuntimeException("�޷��ҵ�ָ����:"+e.toString());
				}
				catch(SQLException e){
					throw new RuntimeException("�����ݿ�����ʧ��:"+e.toString());
				}
			}
}
