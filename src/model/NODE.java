package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import bean.COMMANDBean;
import bean.NODEBean;
import bean.NODEDIRBean;
import bean.NODESERVICEBean;

import sql.COMMANDSql;
import sql.NODESql;
import sql.USERNODESql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class NODE {
	
	public static int addData(NODEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getInsert(bean.getId(),bean.getIp(),bean.getName(),bean.getOs(),bean.getCluster(),bean.getComponentID(),bean.getSystemID(),bean.getMdfUser(),bean.getFlag());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int deleteByFlag(String flag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getDeleteByFlag(flag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static List<NODEBean> getComponentNodes(String componentID,String flag){
		Connection conn = null;
		List<NODEBean>  data=new ArrayList<NODEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getQueryByComponent(componentID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					NODEBean bean=new NODEBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbname(dataLine.get("DBNAME"));
					bean.setBackDBname(dataLine.get("BACKDBNAME"));
					bean.setDbport(dataLine.get("DBPORT"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setType(dataLine.get("TYPE"));
					bean.setSchFlag(dataLine.get("SCH_FLAG"));
					bean.setOs(dataLine.get("OS"));
					bean.setCluster(dataLine.get("CLUSTER"));
					bean.setComponentID(dataLine.get("COMPONET_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFlag(dataLine.get("FLAG"));
					//bean.setDbType(dataLine.get("DBTYPE"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static List<NODEBean> getSystemNodes(String userID,String systemID,String flag){
		Connection conn = null;
		List<NODEBean>  data=new ArrayList<NODEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getSystemNode(userID, systemID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					NODEBean bean=new NODEBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbname(dataLine.get("DBNAME"));
					bean.setBackDBname(dataLine.get("BACKDBNAME"));
					bean.setDbport(dataLine.get("DBPORT"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setType(dataLine.get("TYPE"));
					bean.setSchFlag(dataLine.get("SCH_FLAG"));
					bean.setOs(dataLine.get("OS"));
					bean.setCluster(dataLine.get("CLUSTER"));
					bean.setComponentID(dataLine.get("COMPONET_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setDbType(dataLine.get("DBTYPE"));
					bean.setsPort(dataLine.get("SPORT"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	
	public static List<NODEBean> getMyClusterNodes(String userID,String componentID,String cluster,String flag){
		Connection conn = null;
		List<NODEBean>  data=new ArrayList<NODEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getMyClusterNode(userID,componentID,cluster, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					NODEBean bean=new NODEBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbname(dataLine.get("DBNAME"));
					bean.setBackDBname(dataLine.get("BACKDBNAME"));
					bean.setDbport(dataLine.get("DBPORT"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setType(dataLine.get("TYPE"));
					bean.setSchFlag(dataLine.get("SCH_FLAG"));
					bean.setOs(dataLine.get("OS"));
					bean.setCluster(dataLine.get("CLUSTER"));
					bean.setComponentID(dataLine.get("COMPONET_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setDbType(dataLine.get("DBTYPE"));
					bean.setsPort(dataLine.get("SPORT"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static String getNewNodeID(){
		String result=UUID.randomUUID().toString().replace("-", "");  
		result="Node"+result;
		return result;
	}
	
	public static  int  setNodeTag(String id,String ip,String name,String flag,String userID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getUptTag(id, ip, name, flag, userID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static  boolean  nodeSeqExistSystem(String sytemID,String seq,String flag,String userID){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.nodeSeqExists(sytemID, seq, userID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return true;
				}
			}
		}catch(Exception e){
			return false;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
	
	public static boolean nodeIpExist(String userID,String ip,String flag){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.nodeIpExists( ip, userID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return true;
				}
			}
		}catch(Exception e){
			return false;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
	
	public static NODEBean getNodeByID(String userID,String nodeID,String flag){
		Connection conn = null;
		NODEBean  bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getNodeByID(userID, nodeID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					bean=new NODEBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbname(dataLine.get("DBNAME"));
					bean.setBackDBname(dataLine.get("BACKDBNAME"));
					bean.setDbport(dataLine.get("DBPORT"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setType(dataLine.get("TYPE"));
					bean.setSchFlag(dataLine.get("SCH_FLAG"));
					bean.setOs(dataLine.get("OS"));
					bean.setCluster(dataLine.get("CLUSTER"));
					bean.setComponentID(dataLine.get("COMPONET_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setDbType(dataLine.get("DBTYPE"));
					bean.setsPort(dataLine.get("SPORT"));
					String dirSql=NODESql.getDirs(nodeID);
					List ret=SqlServerUtil.executeQuery(dirSql, conn);
					Map<String,NODEDIRBean> dirs=new HashMap<String, NODEDIRBean>();
					if(ret!=null&&ret.size()>0){
						for(int w=0;w<ret.size();w++){
							Map<String,String> dataline=(Map)ret.get(w);
							NODEDIRBean dir=new NODEDIRBean();
								dir.setNodeID(dataline.get("NODE_ID"));
								dir.setDirName(dataline.get("DIR_NAME"));
								dir.setDirValue(dataline.get("DIR_VALUE"));
								dir.setDirFilter(dataline.get("DIR_FILTER"));
								dir.setMdfUser(dataline.get("MDF_USER"));
								dir.setMdfTime(dataline.get("MDF_TIME"));
								dirs.put(dataline.get("DIR_NAME"), dir);
						}
					}
					bean.setDirs(dirs);
					String svcSql=NODESql.getServices(nodeID);
					List retSvc=SqlServerUtil.executeQuery(svcSql, conn);
					Map<String,NODESERVICEBean> services=new HashMap<String, NODESERVICEBean>();
					if(retSvc!=null&&retSvc.size()>0){
						for(int w=0;w<retSvc.size();w++){
							Map<String,String> dataline=(Map)retSvc.get(w);
							NODESERVICEBean svc=new NODESERVICEBean();
								svc.setNodeID(dataline.get("NODE_ID"));
								svc.setType(dataline.get("TYPE"));
								svc.setStart(dataline.get("START"));
								svc.setStop(dataline.get("STOP"));
								svc.setMdfUser(dataline.get("MDF_USER"));
								svc.setMdfTime(dataline.get("MDF_TIME"));
								services.put(dataline.get("TYPE"), svc);
						}
					}
					bean.setServices(services);
					
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	
	public static Map<String,NODEDIRBean> getDirs(String nodeID){
		Connection conn = null;
		Map<String,NODEDIRBean> dirs=new HashMap<String, NODEDIRBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String dirSql=NODESql.getDirs(nodeID);
			List ret=SqlServerUtil.executeQuery(dirSql, conn);
			
			if(ret!=null&&ret.size()>0){
				for(int w=0;w<ret.size();w++){
					Map<String,String> dataline=(Map)ret.get(w);
					NODEDIRBean dir=new NODEDIRBean();
						dir.setNodeID(dataline.get("NODE_ID"));
						dir.setDirName(dataline.get("DIR_NAME"));
						dir.setDirValue(dataline.get("DIR_VALUE"));
						dir.setDirFilter(dataline.get("DIR_FILTER"));
						dir.setMdfUser(dataline.get("MDF_USER"));
						dir.setMdfTime(dataline.get("MDF_TIME"));
						dirs.put(dataline.get("DIR_NAME"), dir);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return dirs;
	}
	
	public static  int  removeNodeDirs(String nodeID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getDirDelete(nodeID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static  int  removeNodeServices(String nodeID){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getServiceDelete(nodeID);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static  int  setNodeService(NODESERVICEBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getInsertService(bean.getNodeID(), bean.getType(), bean.getStart(), bean.getStop(), bean.getMdfUser());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static Map<String,NODESERVICEBean> getServices(String nodeID){
		Connection conn = null;
		Map<String,NODESERVICEBean> services=new HashMap<String, NODESERVICEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String dirSql=NODESql.getServices(nodeID);
			List ret=SqlServerUtil.executeQuery(dirSql, conn);
			if(ret!=null&&ret.size()>0){
				for(int w=0;w<ret.size();w++){
					Map<String,String> dataline=(Map)ret.get(w);
					NODESERVICEBean svc=new NODESERVICEBean();
						svc.setNodeID(dataline.get("NODE_ID"));
						svc.setType(dataline.get("TYPE"));
						svc.setStart(dataline.get("START"));
						svc.setStop(dataline.get("STOP"));
						svc.setMdfUser(dataline.get("MDF_USER"));
						svc.setMdfTime(dataline.get("MDF_TIME"));
						services.put(dataline.get("TYPE"), svc);
					}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return services;
	}
	
	public static  int  setNodeStatus(String id,String userID,String status){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERNODESql.getUptNodeStatus(userID, id, status);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static  int  setNodeDir(NODEDIRBean bean){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getInsertDir(bean.getNodeID(), bean.getDirName(), bean.getDirValue(), bean.getDirFilter(), bean.getMdfUser());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	
	public static  int  setNodeScheduleStatus(String id,String userID,String status){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=USERNODESql.getUptNodeSchStatus(userID, id, status);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static  boolean  hasCommand(String pkgID,String nodeID,String status,String flag){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.hasCommand(pkgID, nodeID, status, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return true;
				}
			}
		}catch(Exception e){
			return false;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
	}
	
	public static  boolean  hasNoCommand(String pkgID,String nodeID,String status,String flag){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.hasNoCommand(pkgID, nodeID, status, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map dataLine=(Map)result.get(0);
				String count=dataLine.get("COUNT")+"";
				if(Integer.parseInt(count)>0){
					return false;
				}
			}
		}catch(Exception e){
			return false;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return true;
	}
	
	
	public static int setCommandStatus(String pkgID,String nodeID,String status){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getUptStatus(pkgID, nodeID, status);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int resetCommandStatus(String pkgID,String nodeID,String status){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getReUptStatus(pkgID, nodeID, status);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static List<COMMANDBean> getCommandLists(String pkgID,String nodeID){
		Connection conn = null;
		List<COMMANDBean>  data=new ArrayList<COMMANDBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getScheListByNode(pkgID,nodeID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					COMMANDBean bean=new COMMANDBean();
					bean.setPkgID(dataLine.get("PKG_ID"));
					bean.setStepID(dataLine.get("STEP_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setParameter(dataLine.get("PARAMETER"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setRemote(dataLine.get("REMOTE"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setLogInfo(dataLine.get("LOGINFO"));
					bean.setRetTime(dataLine.get("RET_TIME"));
					bean.setRemind(dataLine.get("REMIND"));
					bean.setApprID(dataLine.get("APPRID"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static List<COMMANDBean> getNodeCommand(String pkgID,String nodeID){
		Connection conn = null;
		List<COMMANDBean>  data=new ArrayList<COMMANDBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=COMMANDSql.getByNode(pkgID,nodeID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					COMMANDBean bean=new COMMANDBean();
					bean.setPkgID(dataLine.get("PKG_ID"));
					bean.setStepID(dataLine.get("STEP_ID"));
					bean.setFileID(dataLine.get("FILE_ID"));
					bean.setNodeID(dataLine.get("NODE_ID"));
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setParameter(dataLine.get("PARAMETER"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setRemote(dataLine.get("REMOTE"));
					bean.setCrtTime(dataLine.get("CRT_TIME"));
					bean.setLogInfo(dataLine.get("LOGINFO"));
					bean.setRetTime(dataLine.get("RET_TIME"));
					bean.setRemind(dataLine.get("REMIND"));
					bean.setApprID(dataLine.get("APPRID"));
					bean.setUserID(dataLine.get("USER_ID"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	
	
	public static List<NODEBean> getUserNodes(String userID,String flag){
		Connection conn = null;
		List<NODEBean>  data=new ArrayList<NODEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getNodeByUserID(userID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					NODEBean bean=new NODEBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbname(dataLine.get("DBNAME"));
					bean.setBackDBname(dataLine.get("BACKDBNAME"));
					bean.setDbport(dataLine.get("DBPORT"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setType(dataLine.get("TYPE"));
					bean.setSchFlag(dataLine.get("SCH_FLAG"));
					bean.setOs(dataLine.get("OS"));
					bean.setCluster(dataLine.get("CLUSTER"));
					bean.setComponentID(dataLine.get("COMPONET_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setDbType(dataLine.get("DBTYPE"));
					bean.setsPort(dataLine.get("SPORT"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static List<NODEBean> getUserIpNodes(String userID,String systemID,String ip,String flag){
		Connection conn = null;
		List<NODEBean>  data=new ArrayList<NODEBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getUserIpNode(userID, systemID, ip, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					NODEBean bean=new NODEBean();
					bean.setId(dataLine.get("ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setIp(dataLine.get("IP"));
					bean.setStatus(dataLine.get("STATUS"));
					bean.setDbUser(dataLine.get("DBUSER"));
					bean.setDbPasswd(dataLine.get("DBPASSWD"));
					bean.setDbname(dataLine.get("DBNAME"));
					bean.setBackDBname(dataLine.get("BACKDBNAME"));
					bean.setDbport(dataLine.get("DBPORT"));
					bean.setSftpUser(dataLine.get("SFTPUSER"));
					bean.setSftpPasswd(dataLine.get("SFTPPASSWD"));
					bean.setSftpDir(dataLine.get("SFTPDIR"));
					bean.setSftpPort(dataLine.get("SFTPPORT"));
					bean.setSeq(dataLine.get("SEQ"));
					bean.setType(dataLine.get("TYPE"));
					bean.setSchFlag(dataLine.get("SCH_FLAG"));
					bean.setOs(dataLine.get("OS"));
					bean.setCluster(dataLine.get("CLUSTER"));
					bean.setComponentID(dataLine.get("COMPONET_ID"));
					bean.setSystemID(dataLine.get("SYSTEM_ID"));
					bean.setMdfUser(dataLine.get("MDF_USER"));
					bean.setMdfTime(dataLine.get("MDF_TIME"));
					bean.setFlag(dataLine.get("FLAG"));
					bean.setDbType(dataLine.get("DBTYPE"));
					bean.setsPort(dataLine.get("SPORT"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static String getInstanceStatus(String nodeID,String userID,String flag){
		Connection conn = null;
		String ret="";
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=NODESql.getNodeStatus(userID, nodeID, flag);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				Map<String,String> dataLine=(Map)result.get(0);
				ret=dataLine.get("STATUS");
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return ret;
	}
	
}
