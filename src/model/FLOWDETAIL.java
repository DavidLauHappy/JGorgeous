package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import bean.FLOWDEFBean;
import bean.FlowDetailBean;
import bean.FlowProcessBean;
import bean.FlowStepBean;
import bean.UserBean;


import common.db.DBConnectionManager;

import resource.Context;
import resource.Logger;
import sql.FLOWDETAILSql;
import utils.SqlServerUtil;
import utils.StringUtil;

public class FLOWDETAIL {

	       //提交审批，去掉存储过程的处理
	       public static String approveSubmit(String functionID,String nextUserID,String apprData,String obj){
	    	   String seqNo="";
	    		Connection conn = null;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					 FLOWDEFBean flow=FLOWDEF.getByFunc(functionID);
					 seqNo=FLOWDETAIL.getSeq();
					 if(!StringUtil.isNullOrEmpty(seqNo)){
						 String sql=FLOWDETAILSql.getAdd(seqNo, flow.getId(), "1", FlowDetailBean.Status.Init.ordinal()+"", FlowDetailBean.Locked.No.ordinal()+"", apprData, Context.session.userID,  Context.session.userID,obj);
					     int count=SqlServerUtil.executeUpdate(sql, conn);
					     if(count>0){
					    	  String  msg="用户["+Context.session.userName+"("+Context.session.userID+")向您提交了:"+flow.getName()+"["+seqNo+"]请及时处理。";
					    	 FLOWAPPROVER.add(seqNo, nextUserID, flow.getId(), "1",  Context.session.userID, msg);
					     }
					 }
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return seqNo;
	       }
			
	       //提交给流程的下一个环节
	       public static FlowStepBean getNextProcess(String bussObj){
	    	   Connection conn = null;
	    	   FlowStepBean data=null;
	    	   try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getNextProcess(bussObj);
					List sqlResult=SqlServerUtil.executeQuery(sql, conn);
					if(sqlResult!=null&&sqlResult.size()>0){
							Map<String,String> dataLine=(Map)sqlResult.get(0);
							data=new FlowStepBean();
							data.setApprID(dataLine.get("ID"));
							data.setStepID(dataLine.get("CURR_STEP"));
							data.setName(dataLine.get("CURR_STEPNAME"));
							data.setNextID(dataLine.get("NEXT_STEPID"));
							data.setNextStepName(dataLine.get("NEXT_STEPNAME"));
							data.setAppUserType(dataLine.get("APPRER_TYPE"));
							data.setAppUserID(dataLine.get("APPRER_ID"));
							data.setApprStatus(dataLine.get("REC_STATUS"));
							data.setLocked(dataLine.get("LOCKED"));
							data.setSubmitUser(dataLine.get("SUBMIT_USER"));
							data.setSubmitTime(dataLine.get("SUBMIT_TIME"));
							data.setFlowID(dataLine.get("FLOW_ID"));
							data.setBussObj(bussObj);
							data.setUrl(dataLine.get("URL"));
							data.setNotUrl(dataLine.get("NOT_URL"));
							data.setActionName(dataLine.get("ACTNAME"));
							data.setNotActionName(dataLine.get("NOT_ACTNAME"));
							
					}
	    	   }
	    	   catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
	    	   return data;
	       }
	       
	       public static String getSeq(){
	    	   Connection conn = null;
				String result="";
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getSeq();
					List sqlResult=SqlServerUtil.executeQuery(sql, conn);
					if(sqlResult!=null&&sqlResult.size()>0){
							Map dataLine=(Map)sqlResult.get(0);
							result=dataLine.get("ID")+"";
							if(!StringUtil.isNullOrEmpty(result)){
								result=StringUtil.leftpad(result, 9, "0");
							}
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return result;
	       }
	       //审批流程记录到数据库服务器
	       /*
			public static synchronized String submitArrprove(String functionID,String nextUserID,String apprData){
				Connection conn = null;
				String apprID="";
				Vector input = new Vector();
				input.add(Context.session.userID);
				input.add(functionID);
				input.add(nextUserID);
				input.add(apprData);
				Vector output = new Vector();
				output.add("VARCHAR");
				output.add("VARCHAR");
				output.add("VARCHAR");
				try{
						conn=DBConnectionManager.getInstance().getConnection();	
						String  result[][]=SqlServerUtil.executeProc("SUBMIT_APPROVE", input, output, conn);
						if (result[0][0].equals("error")) {
							Logger.getInstance().error("执行远程日志记录[PROC_LOG]异常");
						}
						else{
							apprID=result[0][2];
						}
				  }
				catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
			  return apprID;
			}*/
			
			public static List<FlowDetailBean> getMyApply(String userID){
				Connection conn = null;
				List<FlowDetailBean>  data=new ArrayList<FlowDetailBean>();
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getDetail(userID);
					List result=SqlServerUtil.executeQuery(sql, conn);
					if(result!=null&&result.size()>0){
						for(int w=0;w<result.size();w++){
							Map<String,String> dataLine=(Map)result.get(w);
							FlowDetailBean bean=new FlowDetailBean();
							bean.setId(dataLine.get("APPR_ID"));
							bean.setFlowID(dataLine.get("FLOW_ID"));
							bean.setFlowName(dataLine.get("FLOW_NAME"));
							bean.setApplyUserID(dataLine.get("SUBMIT_USER"));
							bean.setApplyTime(dataLine.get("SUBMIT_TIME"));
							bean.setStatus(dataLine.get("STATUS"));
							bean.setStepID(dataLine.get("STEP_ID"));
							bean.setStepName(dataLine.get("STEP_NAME"));
							bean.setCurUserID(dataLine.get("USER_ID"));
							bean.setApprData(dataLine.get("APPR_DATA"));
							data.add(bean);
						}
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return data;
			}
			
			//查询待我处理
			public static List<FlowDetailBean> getMyTaskList(String userID){
				Connection conn = null;
				List<FlowDetailBean>  data=new ArrayList<FlowDetailBean>();
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getTask(userID);
					List result=SqlServerUtil.executeQuery(sql, conn);
					if(result!=null&&result.size()>0){
						for(int w=0;w<result.size();w++){
							Map<String,String> dataLine=(Map)result.get(w);
							FlowDetailBean bean=new FlowDetailBean();
							bean.setId(dataLine.get("ID"));
							bean.setFlowID(dataLine.get("FLOW_ID"));
							bean.setFlowName(dataLine.get("FLOW_NAME"));
							bean.setApplyUserID(dataLine.get("SUBMIT_USER"));
							bean.setApplyTime(dataLine.get("SUBMIT_TIME"));
							bean.setApplyUserName(dataLine.get("APPLY_USERNAME"));
							bean.setStepID(dataLine.get("STEP_ID"));
							bean.setStepName(dataLine.get("STEPNAME"));
							bean.setApprData(dataLine.get("APPR_DATA"));
							bean.setBussiObj(dataLine.get("BUSS_OBJ"));
							bean.setUrl(dataLine.get("URL"));
							bean.setActionName(dataLine.get("ACTNAME"));
							bean.setNotUrl(dataLine.get("NOT_URL"));
							bean.setNotActionName(dataLine.get("NOT_ACTNAME"));
							bean.setPageUrl(dataLine.get("PAGE_URL"));
							data.add(bean);
						}
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return data;
			}
			
			//我处理过的审批
			public static List<FlowDetailBean> getMyDonesList(String userID){
				Connection conn = null;
				List<FlowDetailBean>  data=new ArrayList<FlowDetailBean>();
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getDone(userID);
					List result=SqlServerUtil.executeQuery(sql, conn);
					if(result!=null&&result.size()>0){
						for(int w=0;w<result.size();w++){
							Map<String,String> dataLine=(Map)result.get(w);
							FlowDetailBean bean=new FlowDetailBean();
							bean.setId(dataLine.get("ID"));
							bean.setApplyUserID(dataLine.get("SUBMIT_USER"));
							bean.setApplyUserName(dataLine.get("APPLY_USERNAME"));
							bean.setApplyTime(dataLine.get("SUBMIT_TIME"));
							bean.setStatus(dataLine.get("REC_STATUS"));
							bean.setFlowID(dataLine.get("FLOW_ID"));
							bean.setFlowName(dataLine.get("FLOW_NAME"));
							bean.setStepID(dataLine.get("STEP_ID"));
							bean.setStepName(dataLine.get("STEPNAME"));
							bean.setApprData(dataLine.get("APPR_DATA"));
							bean.setBussiObj(dataLine.get("BUSS_OBJ"));
							bean.setAppComment(dataLine.get("APPR_INFO"));
							bean.setProcessTime(dataLine.get("MDF_TIME"));
							bean.setPageUrl(dataLine.get("PAGE_URL"));
							data.add(bean);
						}
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return data;
			}
			
			public static String getApprLocked(String id){
				Connection conn = null;
				String result="";
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getLocked(id);
					List sqlResult=SqlServerUtil.executeQuery(sql, conn);
					if(sqlResult!=null&&sqlResult.size()>0){
							Map<String,String> dataLine=(Map)sqlResult.get(0);
							result=dataLine.get("LOCKED");
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return result;
			}
			
			public static String getApprStatus(String id){
				Connection conn = null;
				String result="";
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getStatus(id);
					List sqlResult=SqlServerUtil.executeQuery(sql, conn);
					if(sqlResult!=null&&sqlResult.size()>0){
							Map<String,String> dataLine=(Map)sqlResult.get(0);
							result=dataLine.get("REC_STATUS");
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return result;
			}
			
			public static int  setLocked(String userID,String id,String locked){
				Connection conn = null;
				int num=0;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getSetLocked(userID, id, locked);
					num=SqlServerUtil.executeUpdate(sql, conn);
				}catch(Exception e){

				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return num;
			}
			
			//审批不通过 流程结束模式
			public static int  apprReject(String apprID,String userID,String flowID,String stepID,String status,String info){
				Connection conn = null;
				int num=0;
				String seq=UUID.randomUUID().toString();
				try{
					seq=seq.replace("-", "");
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getApprNotStep(apprID, userID, flowID, stepID, status, info,seq);
					String detailStatus="2";
					if(status.equals("0"))
						detailStatus="3";
					num=SqlServerUtil.executeUpdate(sql, conn);
					if(num>0){
						sql=FLOWDETAILSql.getApprOverDetail(apprID, userID,detailStatus);
						num=SqlServerUtil.executeUpdate(sql, conn);
					}
				}catch(Exception e){

				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return num;
			}
			
			//审批不通过，流程退回模式
			public static int apprRejectBack(String apprID,String userID,String flowID,String stepID,String status,String info ){
				Connection conn = null;
				int num=0;
				String seq=UUID.randomUUID().toString();
				try{
					seq=seq.replace("-", "");
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getApprNotStep(apprID, userID, flowID, stepID, status, info,seq);
					num=SqlServerUtil.executeUpdate(sql, conn);
					//流程明细当前步骤更新为1(发起人处理的步骤)
					String nextStep="1";
					if(num>0){
						sql=FLOWDETAILSql.getApprDetailJump(apprID, nextStep);
						num=SqlServerUtil.executeUpdate(sql, conn);
					}
				}catch(Exception e){

				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return num;
			}
			
			//审批通过
			public static int  apprApproved(String apprID,String userID,String flowID,String stepID,String status,String info){
				Connection conn = null;
				int num=0;
				String seq=UUID.randomUUID().toString();
				try{
					seq=seq.replace("-", "");
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getApprNotStep(apprID, userID, flowID, stepID, status, info,seq);
					String detailStatus="2";
					if(status.equals("0"))
						detailStatus="3";
					num=SqlServerUtil.executeUpdate(sql, conn);
					if(num>0){
						sql=FLOWDETAILSql.getApprOverDetail(apprID, userID,detailStatus);
						num=SqlServerUtil.executeUpdate(sql, conn);
					}
				}catch(Exception e){

				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return num;
			}
			
			//审批通过
			public static int  apprJump(String apprID,String userID,String flowID,String stepID,String status,String info,String nextStep){
				Connection conn = null;
				int num=0;
				String seq=UUID.randomUUID().toString();
				try{
					seq=seq.replace("-", "");
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getApprNotStep(apprID, userID, flowID, stepID, status, info,seq);
					num=SqlServerUtil.executeUpdate(sql, conn);
					if(num>0){
						sql=FLOWDETAILSql.getApprDetailJump(apprID, nextStep);
						num=SqlServerUtil.executeUpdate(sql, conn);
					}
				}catch(Exception e){

				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return num;
			}
			
			//生成审批明细的处理人
			public static  void  makeGroupApprover(String apprID,String groupID,String flowID,String stepID,String mdfUser,String msg){
				try{
					 List<UserBean> users=USERS.getGroupUsers(groupID);
					 for(UserBean user:users){
						FLOWAPPROVER.add(apprID, user.getUserID(), flowID, stepID, mdfUser,msg);
					 }
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
			public static  void  makeRoleApprover(String apprID,String roleID,String flowID,String stepID,String mdfUser,String msg){
				try{
					 List<UserBean> users=USERS.getRoleUsers(roleID);
					 for(UserBean user:users){
						FLOWAPPROVER.add(apprID, user.getUserID(), flowID, stepID, mdfUser,msg);
					 }
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}


			public static String getNextStep(String flowID,String stepID){
				Connection conn = null;
				String result="";
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getNextStep(flowID, stepID);
					List sqlResult=SqlServerUtil.executeQuery(sql, conn);
					if(sqlResult!=null&&sqlResult.size()>0){
							Map<String,String> dataLine=(Map)sqlResult.get(0);
							result=dataLine.get("NEXT_ID");
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return result;
			}
			
			public static FlowDetailBean getDetailByBuss(String bussID){
				Connection conn = null;
				FlowDetailBean data=null;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getApprDetail(bussID);
					List sqlResult=SqlServerUtil.executeQuery(sql, conn);
					if(sqlResult!=null&&sqlResult.size()>0){
							Map<String,String> dataLine=(Map)sqlResult.get(0);
							data=new FlowDetailBean();
							data.setId(dataLine.get("ID"));
							data.setFlowID(dataLine.get("FLOW_ID"));
							data.setStepID(dataLine.get("CURR_STEP"));
							data.setLocked(dataLine.get("LOCKED"));
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return data;
			}
			
			public static String getCurrentApprover(String apprID,String flowID,String stepID){
				Connection conn = null;
				String result=null;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getCurrApprover(apprID,flowID,stepID);
					List sqlResult=SqlServerUtil.executeQuery(sql, conn);
					if(sqlResult!=null&&sqlResult.size()>0){
							Map<String,String> dataLine=(Map)sqlResult.get(0);
							result=dataLine.get("USER_ID");
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return result;
			}
			
			public static List<FlowProcessBean> getFlowProcessSteps(String bussID){
				Connection conn = null;
				List<FlowProcessBean>  data=new ArrayList<FlowProcessBean>();
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql=FLOWDETAILSql.getFlowProcess(bussID);
					List result=SqlServerUtil.executeQuery(sql, conn);
					if(result!=null&&result.size()>0){
						for(int w=0;w<result.size();w++){
							Map<String,String> dataLine=(Map)result.get(w);
							FlowProcessBean bean=new FlowProcessBean();
					        bean.setApprID(dataLine.get("ID"));
					        bean.setFlowID(dataLine.get("FLOW_ID"));
					        bean.setStepID(dataLine.get("STEP_ID"));
					        bean.setStepName(dataLine.get("NAME"));
					        bean.setUserID(dataLine.get("USER_ID"));
					        bean.setUserName(dataLine.get("USER_NAME"));
					        bean.setApprInfo(dataLine.get("APPR_INFO"));
					        bean.setApprTime(dataLine.get("MDF_TIME"));
							data.add(bean);
						}
					}
				}catch(Exception e){
					
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				return data;
			}
			
}
