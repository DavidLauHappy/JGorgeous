package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.FlowStepBean;

import sql.FLOWSTEPSql;
import utils.SqlServerUtil;

import common.db.DBConnectionManager;

public class FLOWSTEP {

	public static List<FlowStepBean> getSteps(String flowID){
		Connection conn = null;
		List<FlowStepBean>  data=new ArrayList<FlowStepBean>();
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=FLOWSTEPSql.getSteps(flowID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					FlowStepBean bean=new FlowStepBean();
					bean.setFlowID(dataLine.get("FLOW_ID"));
					bean.setStepID(dataLine.get("STEP_ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setAppUserID(dataLine.get("APPRER_ID"));
					bean.setAppUserType(dataLine.get("APPRER_TYPE"));
					bean.setNextID(dataLine.get("NEXT_ID"));
					data.add(bean);
				}
			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return data;
	}
	
	public static FlowStepBean getStep(String flowID,String stepID){
		Connection conn = null;
		FlowStepBean  bean=null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=FLOWSTEPSql.getStep(flowID, stepID);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
					Map<String,String> dataLine=(Map)result.get(0);
					bean=new FlowStepBean();
					bean.setFlowID(dataLine.get("FLOW_ID"));
					bean.setStepID(dataLine.get("STEP_ID"));
					bean.setName(dataLine.get("NAME"));
					bean.setAppUserID(dataLine.get("APPRER_ID"));
					bean.setAppUserType(dataLine.get("APPRER_TYPE"));
					bean.setNextID(dataLine.get("NEXT_ID"));

			}
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return bean;
	}
	
	
}
