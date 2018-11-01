package resource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import common.db.DBConnectionManager;

import utils.DataUtil;
import utils.DateUtil;
import utils.LocalDBUtil;
import utils.SqlServerUtil;

public class IMessage {
  public enum Status{Unread,Read;}
   private String userID;
   private String msgID;
   private String msgText;
   private String crtUser;
   private String crtTime;
   private String status;
   
   public IMessage(){};
   
   public List<IMessage> getMessage(String userID){
	   List<IMessage> result=new ArrayList<IMessage>();
		Connection conn = null;
		try{
		   String query="SELECT  top 5 USER_ID,MSG_ID,MSG,CRT_USER,CRT_TIME,STATUS FROM MSGS WHERE USER_ID='@USER_ID' AND STATUS='@STATUS'  ORDER BY CRT_TIME DESC ";
		   query=query.replace("@USER_ID", userID);
		   query=query.replace("@STATUS", Status.Unread.ordinal()+"");
		   conn=DBConnectionManager.getInstance().getConnection();
		   List sqlResult=SqlServerUtil.executeQuery(query, conn);
		   if(sqlResult!=null&&sqlResult.size()>0){
				  for(int w=0;w<sqlResult.size();w++){
					  Map<String,String> dataLine=(Map)sqlResult.get(w);
					  IMessage data=new IMessage(dataLine.get("USER_ID"),dataLine.get("MSG_ID"),dataLine.get("MSG"),dataLine.get("CRT_USER"),dataLine.get("CRT_TIME"),dataLine.get("STATUS"));
					  result.add(data);
				  }
		   }
	   }catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
	   return result;
   }
   
   public  IMessage(String userID,String msgText){
	   this.userID=userID;
	   this.msgText=msgText;
	   this.msgID=UUID.randomUUID().toString();
	   this.status=Status.Unread.ordinal()+"";
   }
   
   public boolean addMsg(){
	   Connection conn = null;
		try{
		   String inserter="INSERT INTO MSGS(USER_ID,MSG_ID,MSG,CRT_USER,CRT_TIME,STATUS) VALUES"+
				   					"('@USER_ID','@MSG_ID','@MSG','@CRT_USER','@CRT_TIME','@STATUS')";
		   inserter=inserter.replace("@USER_ID", this.userID);
		   inserter=inserter.replace("@MSG_ID", this.msgID);
		   inserter=inserter.replace("@MSG", this.msgText);
		   inserter=inserter.replace("@CRT_USER", Context.session.userID);
		   inserter=inserter.replace("@CRT_TIME", DateUtil.getCurrentTime());
		   inserter=inserter.replace("@STATUS", this.status);
		   conn=DBConnectionManager.getInstance().getConnection();
		   int count=SqlServerUtil.executeUpdate(inserter, conn);
	       if(count>0)
	    	   return true;
		}catch(Exception e){
			
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return false;
   }
   
   public boolean closeMsg(){
	      Connection conn = null;
	 		try{
		   	String updater="UPDATE MSGS SET STATUS='@STATUS' WHERE USER_ID='@USER_ID' AND MSG_ID='@MSG_ID'";
		   	updater=updater.replace("@USER_ID", this.userID);
		   	updater=updater.replace("@MSG_ID", this.msgID);
		   	updater=updater.replace("@STATUS", Status.Read.ordinal()+"");
		    conn=DBConnectionManager.getInstance().getConnection();
		   	 int count=SqlServerUtil.executeUpdate(updater, conn);
		     if(count>0)
		    	 return true;
	 		}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return false;
   }

public IMessage(String userID, String msgID, String msgText, String crtUser,
		String crtTime, String status) {
	super();
	this.userID = userID;
	this.msgID = msgID;
	this.msgText = msgText;
	this.crtUser = crtUser;
	this.crtTime = crtTime;
	this.status = status;
}

public String getUserID() {
	return userID;
}

public String getMsgID() {
	return msgID;
}

public String getMsgText() {
	return msgText;
}

public String getCrtUser() {
	return crtUser;
}

public String getCrtTime() {
	return crtTime;
}

public String getStatus() {
	return status;
}
   
//生成开发任务逾期提醒
public  static  void makeAlert(String userID,String aheadDays){
	Connection conn = null;
	try{
		 conn=DBConnectionManager.getInstance().getConnection();
		  String sql="select ID,TNAME,ARRANGE_DATE from TASK_DEF WHERE OWNER='@USER_ID' and STATUS IN('1','2') AND ARRANGE_DATE<=CONVERT (varchar(10), GETDATE()-@DAYS, 120) ";
		  sql=sql.replace("@USER_ID", userID);
		  sql=sql.replace("@DAYS", aheadDays);
		   List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					String taskID=dataLine.get("ID");
					String taskName=dataLine.get("TNAME");
					String expiredDate=dataLine.get("ARRANGE_DATE");
					//////////////////////////////
					String text="开发任务:"+taskName+"("+taskID+")即将逾期。完成截止日期为："+expiredDate+",请及时关注。";
					IMessage msg=new IMessage(userID,text);
					msg.addMsg();
				}
			}
	}catch(Exception e){
		
	}finally{
		DBConnectionManager.getInstance().freeConnction(conn);
	}
	}
   
}
