package business.aduitor.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import resource.Context;
import resource.Dictionary;

import utils.SqlServerUtil;

import common.db.DBConnectionManager;

import business.aduitor.bean.TransLogBean;

public class DataHelper {
	
      public static List<TransLogBean> getTransLog(String appID,String systemID,String userID,String ip,String date){
    	  Connection conn = null;
    	  List<TransLogBean> result=new ArrayList<TransLogBean>();
    	  Map<String,String> apps=Dictionary.getDictionaryMap("APP");
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql="select  DEPLOY_TRANS_LOG.ID ID,"+
										    "DEPLOY_TRANS_LOG.APP APP,"+
											 "SYSTEM.NAME SYSTEM_NAME,"+
											 "NODE.IP  IP,"+
											 "NODE.NAME+'['+NODE.IP+']'  NODEDESC,"+
											 "DEPLOY_TRANS_LOG.OPER_ID USER_ID,"+
											 "USERS.USER_NAME+'('+USERS.USER_ID+')' USERDESC,"+
											 "DEPLOY_TRANS_LOG.OPER_ID+':['+DEPLOY_TRANS_LOG.OPER_MAC+']'  TERMINAL,"+
											 "FUNC.ID   FUNCID,"+
											 "FUNC.NAME  FUNCNAME,"+
											 "DEPLOY_TRANS_LOG.DETAIL  OPERATION,"+
											 "DEPLOY_TRANS_LOG.OPER_TIME+'~'+DEPLOY_TRANS_LOG.END_TIME TIME "+
									"from DEPLOY_TRANS_LOG,FUNC,USERS,SYSTEM,NODE "+
							"where DEPLOY_TRANS_LOG.APP='@APP' "+
							  "and  DEPLOY_TRANS_LOG.SYSTEM='@SYSTEM' "+
							  "and  ('@USER'='' OR DEPLOY_TRANS_LOG.OPER_ID='@USER') "+
							  "and  ('@IP'='' OR NODE.IP='@IP') "+
							  "and  ('@DATE'='' OR DEPLOY_TRANS_LOG.DATA_DATE='@DATE') "+
							  "and DEPLOY_TRANS_LOG.SYSTEM=SYSTEM.BUSSID "+
							  "and  DEPLOY_TRANS_LOG.OPER_ID=USERS.USER_ID "+
							  "and DEPLOY_TRANS_LOG.FUNC_ID=FUNC.ID "+
							  "and DEPLOY_TRANS_LOG.NODE_ID=NODE.ID "+
							  "and NODE.FLAG=SYSTEM.FLAG "+
							  "and NODE.FLAG in('@FLAG','C')";
				
				sql=sql.replace("@APP", appID);
				sql=sql.replace("@SYSTEM", systemID);
				sql=sql.replace("@USER", userID);
				sql=sql.replace("@IP", ip);
				sql=sql.replace("@DATE", date);
				sql=sql.replace("@FLAG", Context.session.currentFlag);
				List sqlResult=SqlServerUtil.executeQuery(sql, conn);
				if(sqlResult!=null&&sqlResult.size()>0){
					for(int w=0;w<sqlResult.size();w++){
						Map<String,String> dataLine=(Map)sqlResult.get(w);
						TransLogBean bean=new TransLogBean();
						bean.setId(dataLine.get("ID"));
						String app=dataLine.get("APP");
						String appName=apps.get(app);
						bean.setApp(app);
						bean.setAppName(appName);
						bean.setSystemName(dataLine.get("SYSTEM_NAME"));
						bean.setIp(dataLine.get("IP"));
						bean.setTargetNodeDesc(dataLine.get("NODEDESC"));
						bean.setOperID(dataLine.get("USER_ID"));
						bean.setOperDesc(dataLine.get("USERDESC"));
						bean.setUserTerminal(dataLine.get("TERMINAL"));
						bean.setFuncID(dataLine.get("FUNCID"));
						bean.setFuncName(dataLine.get("FUNCNAME"));
						bean.setDetail(dataLine.get("OPERATION"));
						bean.setDuration(dataLine.get("TIME"));
						result.add(bean);
					}
				}
			}
			catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return result;
      }
}
