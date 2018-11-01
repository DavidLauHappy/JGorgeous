package business.tversion.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.db.DBConnectionManager;
import business.tversion.bean.TDir;
import utils.SqlServerUtil;
/**
 * @author David
 *
 */
public class DataHelper {
   
      
      public static List<TDir> getDirTrees(String appName){
    	  List<TDir> result=new ArrayList<TDir>();
    	  Connection conn = null;
    	  try{
	  		 String query="SELECT APPNAME,ID,NAME,TYPE,PARENTID FROM DIR WHERE APPNAME='@APPNAME' AND  TYPE<>'0' ORDER BY TYPE";
	  		 query=query.replace("@APPNAME", appName);
	  		 conn=DBConnectionManager.getInstance().getConnection();
	 		  List sqlResult=SqlServerUtil.executeQuery(query, conn);
	    		if(sqlResult!=null&&sqlResult.size()>0){
   			    for(int w=0;w<sqlResult.size();w++){
   				      Map<String,String> dataLine=(Map)sqlResult.get(w);
	      			  TDir bean=new TDir(dataLine.get("APPNAME"),dataLine.get("ID"),dataLine.get("NAME"),dataLine.get("TYPE"),dataLine.get("PARENTID"));
	      			  bean.setFullPath(bean.getFullPath(bean.getName(), bean.getParentID()));
	      			  result.add(bean);
	      		  }
	      	  }
    	  }catch(Exception e){
    			
    		}finally{
    			DBConnectionManager.getInstance().freeConnction(conn);
    		}
  		return result;
      }
}
