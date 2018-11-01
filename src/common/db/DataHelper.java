package common.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.NODE;
import model.NODEFILESTT;
import resource.Context;
import resource.Logger;
import sql.COMMANDSql;
import sql.NODEFILESql;
import sql.NODESTEPSql;
import sql.PKGSYSTEMSql;
import utils.SqlServerUtil;
import bean.NODEBean;
import bean.NODEFILESTTBean;
import business.deploy.bean.ApproveData;
import business.deploy.bean.ColumnInfo;
import business.deploy.bean.CompareData;
import business.deploy.bean.InstallData;
import business.deploy.bean.ReviewData;
import business.deploy.bean.StrategyData;
import business.deploy.bean.TableInfo;


public class DataHelper {


	  /////////需要保留的接口///涉及数据库连接的数据接口
	  public static String getDbProcText(Connection con,String procName,String dbType){
		   StringBuilder sb=new   StringBuilder();
		   Statement stmt=null;
		   try{
		     stmt=con.createStatement();
		     String sql="";
		     if("0".equals(dbType)){
		    	 sql="sp_helptext @PROC";
		     }else{
		    	 sql="SELECT TEXT FROM USER_SOURCE WHERE UPPER(NAME)=UPPER('@PROC')";
		     }
	         sql=sql.replace("@PROC", procName);
	         Logger.getInstance().debug(sql);
	         boolean hasResult = stmt.execute(sql);
	         if(hasResult){
	        	 ResultSet rs = stmt.getResultSet();
	        	 while (rs.next()) {
	            	 String value=rs.getString(1);
	            	 sb.append(value);
	             }
	         }
		    }catch(Exception e){
		    	Logger.getInstance().error("DataHelper.getProcText()获取数据库对象【"+procName+"】内容异常："+e.toString());
		    }finally{
				if(stmt!=null){
					try{
						stmt.close();
					}catch(Exception e){
						Logger.getInstance().error("DataHelper.getProcText()获取数据库对象【"+procName+"】内容回收资源时异常："+e.toString());
					}
				}
			}
		   return sb.toString();
	  }
	  
	  /////////////////需要保留的接口///////////////////////////////////
	  public static String getDbTableText(Connection con,String tableName,String dbType){
		  Statement stmt=null;
		  TableInfo result = new TableInfo();
		   try{
		     stmt=con.createStatement();
		    
		     /**设置表名*/
		     result.setTableName(tableName);
		     result.setDbType(dbType);
		     DatabaseMetaData dbmd = con.getMetaData();
		     ResultSet rs = dbmd.getColumns(null, null, tableName, null);
		     /**判断字段是否自增*/
		     String sql = "select * from " + tableName + " where 1=2";
		     ResultSet rst = con.prepareStatement(sql).executeQuery();
		     ResultSetMetaData rsmd = rst.getMetaData();
		     int i=1;
		     while(rs.next()){
		    	   //列名称
		    	   String columnName = rs.getString("COLUMN_NAME");//列名
		    	   //数据类型
		    	   int dataType = rs.getInt("DATA_TYPE");//类型  
		    	   //数据类型名称
		    	   String dataTypeName = rs.getString("TYPE_NAME");   
		    	   //精度,列的大小
		    	   int precision = rs.getInt("COLUMN_SIZE");//精度   
		    	   //小数位数
		    	   int scale = rs.getInt("DECIMAL_DIGITS");// 小数的位数   
		    	   //是否为空
		    	   int isNull = rs.getInt("NULLABLE");//是否为空   
		    	   //字段默认值
		    	   String defaultValue = "";//
		    	   boolean isAutoIncrement=false; 
		    	   if("0".equals(dbType)){//Oracle不适用
		    		   defaultValue=rs.getString("COLUMN_DEF");
		    		   isAutoIncrement=rsmd.isAutoIncrement(i); 
		    	   }
		    	   ColumnInfo col = new ColumnInfo();                     
		    	   col.setName(columnName);
		    	   col.setDataType(dataType);
		    	   col.setDataTypeName(dataTypeName);
		    	   col.setPrecision(precision);
		    	   col.setScale(scale);
		    	   col.setIsNull(isNull); 
		    	   col.setDefaultValue(defaultValue);
		    	   col.setAutoIncrement(isAutoIncrement);
		    	   
		    	     result.setColInfo(columnName, col);
		    	   i++;
		    	  }
		    	  rs.close();
		    	  /**设置主键*/
		    	  rs = dbmd.getPrimaryKeys(null, null, tableName);
		    	  while(rs.next()){
		    		  result.setPkName(rs.getString("PK_NAME"));
		    	      result.setPrimaryKey(rs.getString("COLUMN_NAME"), true);
		    	  }
		    	  rs.close();
		    	  
		   }catch(Exception e){
		    	Logger.getInstance().error("DataHelper.getDbTableText()获取数据库对象【"+tableName+"】内容异常："+e.toString());
		    }finally{
				if(stmt!=null){
					try{
						stmt.close();
					}catch(Exception e){
						Logger.getInstance().error("DataHelper.getDbTableText()获取数据库对象【"+tableName+"】内容回收资源时异常："+e.toString());
					}
				}
			}
		   return result.toString();
	  }
      //////////////////////////////////////保留的方法////////////////////////// 
	  public static boolean backDbTableData(Connection con ,String sql){
		  Logger.getInstance().debug(sql);
		  boolean ret=true;
		  Statement stmt=null;
		   try{
		     stmt=con.createStatement();
	        boolean result = stmt.execute(sql);
		    }catch(Exception e){
		    	ret=false;
		    	Logger.getInstance().error("DataHelper.backDbTableData()备份数据库对象【"+sql+"】内容异常："+e.toString());
		    }finally{
				if(stmt!=null){
					try{
						stmt.close();
					}catch(Exception e){
						ret=false;
						Logger.getInstance().error("DataHelper.backDbTableData()备份数据库对象【"+sql+"】内容回收资源时异常："+e.toString());
					}
				}
			}
		   return ret;
	  }
	  
	  //////////////////////////////////////保留的方法//////////////////////////
	  public static String getDbObjectUptInfo(Connection con,String objName,String dbtype){
		  String result="无法获取对象["+objName+"]的时间信息";
		  Statement stmt=null;
		  String sql="";
		   try{
		     stmt=con.createStatement();
		     if("0".equals(dbtype)){
		    	 sql="select CONVERT(varchar(100),create_date,120),CONVERT(varchar(100),modify_date,120) from sys.objects where name='@PROC'";
		     }else{
		    	 sql="select CREATED,LAST_DDL_TIME from USER_OBJECTS where OBJECT_NAME='@PROC' and rownum=1";
		     }
		     sql=sql.replace("@PROC", objName);
		 	Logger.getInstance().debug(sql);
	         boolean hasResult = stmt.execute(sql);
	         if(hasResult){
	        	 ResultSet rs = stmt.getResultSet();
	        	 while (rs.next()) {
	            	 String cdate=rs.getString(1);
	            	 String mdate=rs.getString(2);
	            	 result="创建:"+cdate+"更新:"+mdate;
	             }
	         }  
		   }catch(Exception e){
		    	Logger.getInstance().error("DataHelper.getDbObjectUptInfo()获取数据库对象【"+objName+"】内容异常："+e.toString());
		    }finally{
				if(stmt!=null){
					try{
						stmt.close();
					}catch(Exception e){
						Logger.getInstance().error("DataHelper.getDbObjectUptInfo()获取数据库对象【"+objName+"】内容回收资源时异常："+e.toString());
					}
				}
			}
		   return result;
	  }
	  ////////////////涉及数据库连接的数据接口
	 
	  /////////////////////////////保留的方法//////////////////////获取某个群组的部署安装策略复核数据
	  public static List<StrategyData> getStrategeCmdData(String pkgID,String systemID,String flag){
		  List<StrategyData> data=new ArrayList<StrategyData>();
			Connection conn = null;
			int  num=0;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=NODESTEPSql.getQuery(pkgID, systemID, Context.session.currentFlag, flag,Context.session.userID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					for(int w=0;w<result.size();w++){
						Map<String,String> dataLine=(Map)result.get(w);
						String versionID=dataLine.get("PKG_ID");
						String stepID=dataLine.get("STEP_ID");
						String stepName=dataLine.get("DESC");
						String nodeID=dataLine.get("NODE_ID");
						String nodeName=dataLine.get("NODENAME");
						String nodeIp=dataLine.get("IP");
						String groupID=dataLine.get("SYSTEM_ID");
						String Flag=dataLine.get("FLAG");	
						String stepComp=dataLine.get("STEPNAME");
						StrategyData bean=new StrategyData(versionID,stepID,stepName,nodeID,nodeName,nodeIp,groupID,Flag,stepComp);
						data.add(bean);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return data;
	  }
	  /////////////////////////////保留的方法(获取版本的安装数据)//////////////////////
	  public static List<InstallData> getMyInstallData(String userID,String flag){
		   List<InstallData> data=new ArrayList<InstallData>();
			Connection conn = null;
			int  num=0;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=PKGSYSTEMSql.getByUser(userID, flag);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					for(int w=0;w<result.size();w++){
						Map<String,String> dataLine=(Map)result.get(w);
						InstallData bean=new InstallData();
						bean.setMdfTime(dataLine.get("MDF_TIME"));
						bean.setSystemName(dataLine.get("SYSTEM_NAME"));
						bean.setPkgID(dataLine.get("PKG_ID"));
						bean.setSystemID(dataLine.get("SYSTEM_ID"));
						bean.setStatus(dataLine.get("STATUS"));
						data.add(bean);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return data;
	  }
	  
	  /////////////////////////////保留的方法(获取版本的安装数据)//////////////////////
	  public static InstallData getMyInstallData(String userID,String flag,String pkgID,String systemID){
		     InstallData bean=null;
			Connection conn = null;
			int  num=0;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=PKGSYSTEMSql.getByID(userID, flag, pkgID, systemID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
						Map<String,String> dataLine=(Map)result.get(0);
						 bean=new InstallData();
						bean.setMdfTime(dataLine.get("MDF_TIME"));
						bean.setSystemName(dataLine.get("SYSTEM_NAME"));
						bean.setPkgID(dataLine.get("PKG_ID"));
						bean.setSystemID(dataLine.get("SYSTEM_ID"));
						bean.setStatus(dataLine.get("STATUS"));
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return bean;
	  }
     
	  /////////////////////////////保留的方法(获取版本文件比对数据)//////////////////////
	  public static List<CompareData> getCompareData(String userID,String flag,String pkgID,String systemID,String nodeID){
		   List<CompareData> data=new ArrayList<CompareData>();
			Connection conn = null;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=NODEFILESql.getCompare(userID, flag, pkgID, systemID, nodeID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					for(int w=0;w<result.size();w++){
						Map<String,String> dataLine=(Map)result.get(w);
						CompareData bean=new CompareData(dataLine.get("PKG_ID"),dataLine.get("SYSTEM_NAME"),dataLine.get("NODE_NAME"),dataLine.get("IP"),dataLine.get("FILE_NAME"),dataLine.get("MD5"),dataLine.get("INSTALL_MD5"));
						data.add(bean);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return data;
	  }
	  
	  /////////////////////////////保留的方法(获取版本指令安装数据)//////////////////////
	  public static List<ReviewData> getReportCmdData(String version,String groupid,String nodeid,String flag){
		  List<ReviewData> data=new ArrayList<ReviewData>();
		  Connection conn = null;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql=COMMANDSql.getReport(version, groupid, nodeid, flag);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					for(int w=0;w<result.size();w++){
						Map<String,String> dataLine=(Map)result.get(w);
						ReviewData bean=new ReviewData(dataLine.get("SEQ"),dataLine.get("PKG_ID"),dataLine.get("NAME"),dataLine.get("SYSTEM_NAME"),dataLine.get("NODE_NAME"),dataLine.get("IP"),dataLine.get("STATUS"),dataLine.get("LOGINFO"),dataLine.get("RET_TIME"));
						data.add(bean);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return data;
	  }
	  
	  /////////////////////////////保留的方法(获取版本指令安装进度)//////////////////////
	  public static String getInstallProgress(String pkgID,String systemID,String flag,String userID){
		   Connection conn = null;
			String progress="";
			try{
				conn=DBConnectionManager.getInstance().getConnection();
			    String sql=COMMANDSql.getProgress(pkgID, systemID, flag,userID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
						Map dataLine=(Map)result.get(0);
						String total=dataLine.get("TOTAL")+"";
						String done=dataLine.get("DONE")+"";
						progress=total+"|"+done;
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return progress;
	  }
	  
	  	/////////////////////////////保留的方法(获取版本指令节点安装的情况)//////////////////////
	  public static List<String> checkCmdOver(String pkgID,String userID){
		  Connection conn = null;
		  List<String> result=new ArrayList<String>();
			try{
				conn=DBConnectionManager.getInstance().getConnection();
			    String sql=COMMANDSql.getMyCmdOver(pkgID, userID);
				List sqlResult=SqlServerUtil.executeQuery(sql, conn);
				if(sqlResult!=null&&sqlResult.size()>0){
					for(int w=0;w<sqlResult.size();w++){
						Map dataLine=(Map)sqlResult.get(w);
						String node=(String)dataLine.get("NODE_ID");
						String total=dataLine.get("TOTAL")+"";
						String done=dataLine.get("DONE")+"";
						String line=node+"|"+total+"|"+done;
						result.add(line);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return result;
	  }
	/////////////////////////////保留的方法(统计节点安装情况)//////////////////////  
	 public static void statisticMyPkgFiles(String pkgID,String systemID,String userID){ 
		 Connection conn = null;
		 NODEFILESTT.deleteSystemData(pkgID, systemID);
		 List<NODEBean> nodes=NODE.getSystemNodes(userID, systemID, Context.session.currentFlag);
		 try{
				conn=DBConnectionManager.getInstance().getConnection();
				for(NODEBean node:nodes){
				 	String sql=COMMANDSql.getMyStaticsticFile(pkgID, systemID, node.getId());
				 	List sqlResult=SqlServerUtil.executeQuery(sql, conn);
					if(sqlResult!=null&&sqlResult.size()>0){
						for(int w=0;w<sqlResult.size();w++){
							Map dataLine=(Map)sqlResult.get(w);
							String name=(String)dataLine.get("NAME");
							String num=dataLine.get("NUM")+"";
							NODEFILESTTBean bean=new NODEFILESTTBean(pkgID,systemID,node.getId(),name,num,userID);
						    bean.inroll();
						}
					}
				 }
		 }
		 catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
	 }
	 
		/////////////////////////////保留的方法(审核通过后可执行的部署)//////////////////////  
	 public static List<ApproveData> getMyDeployTask(String userID){
		 Connection conn = null;
		  List<ApproveData> result=new ArrayList<ApproveData>();
			try{
				conn=DBConnectionManager.getInstance().getConnection();
			    String sql=COMMANDSql.getMyDeployTask(userID);
				List sqlResult=SqlServerUtil.executeQuery(sql, conn);
				if(sqlResult!=null&&sqlResult.size()>0){
					for(int w=0;w<sqlResult.size();w++){
						Map dataLine=(Map)sqlResult.get(w);
						String apprID=(String)dataLine.get("APPR_ID");
						String status=(String)dataLine.get("STATUS");
						String time=(String)dataLine.get("SUBMIT_TIME");
						ApproveData data=new ApproveData(apprID,status,time);
						result.add(data);
					}
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return result;
	 }
	 /////////////////////////////保留的方法//////////////////////  
	 public static String getPkgByApprID(String apprID,String userID){
		   Connection conn = null;
			String pkgID="";
			try{
				conn=DBConnectionManager.getInstance().getConnection();
			    String sql=COMMANDSql.getPkgID(apprID, userID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
						Map dataLine=(Map)result.get(0);
						 pkgID=(String)dataLine.get("PKG_ID");
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return pkgID;
	  }
	 
	 /////////////////////////////保留的方法//////////////////////  
	 public static String getSystemByApprID(String apprID,String userID){
		   Connection conn = null;
			String systemID="";
			try{
				conn=DBConnectionManager.getInstance().getConnection();
			    String sql=COMMANDSql.getNodeID(apprID, userID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
						Map dataLine=(Map)result.get(0);
						String nodeID =(String)dataLine.get("NODE_ID");
						NODEBean node=NODE.getNodeByID(userID, nodeID, Context.session.currentFlag);
						if(node!=null)
							systemID=node.getSystemID();
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return systemID;
	  }
	 
	 /////////////////////////////保留的方法（更新审计记录的回写时间)//////////////////////  
	 public synchronized static void  setTransLogEnd(String cmdID){
		 String logID=cmdID.replace("-", "");
		 Connection conn = null;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql="update DEPLOY_TRANS_LOG set END_TIME=@END_TIME,UPT_TIME=@UPT_TIME where ID='@ID'";
				sql=sql.replace("@ID", logID);
				sql=sql.replace("@END_TIME", "CONVERT(varchar(100),GETDATE(),120)");
				sql=sql.replace("@UPT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
				SqlServerUtil.executeUpdate(sql, conn);
			}
			catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
	 }
}



