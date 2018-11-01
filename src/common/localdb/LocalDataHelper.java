package common.localdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import resource.Logger;
import utils.DataUtil;
import utils.DateUtil;
import utils.LocalDBUtil;
import bean.Triple;

public class LocalDataHelper {
	  public static  Map  getSystemParameters(){
		  String selector="SELECT NAME,VALUE FROM PARAMETERS";
		  String[][] sqlResult=DataUtil.convertList2Array(LocalDBUtil.query(selector));	  
		  Map<String, String>  result=new HashMap<String, String>();
		   if(sqlResult!=null) {
				 for(int w=0;w<sqlResult.length;w++){
					 result.put(sqlResult[w][0], sqlResult[w][1]);
				 }
		   }
		   return result;
	  }
	  
	
	  public static  List<Triple>  getParameters(){
		  String selector="SELECT NAME,VALUE,TYPE,DESC,MAX,MIN,TIP FROM PARAMETERS WHERE SHOW='1' ORDER BY SEQ";
		  String[][] sqlResult=DataUtil.convertList2Array(LocalDBUtil.query(selector));	  
		  List<Triple>  result=new ArrayList<Triple>();
		   if(sqlResult!=null) {
				 for(int w=0;w<sqlResult.length;w++){
					 Triple bean=new Triple(sqlResult[w][0],sqlResult[w][1],sqlResult[w][2],sqlResult[w][3],sqlResult[w][4],sqlResult[w][5]);
					 bean.setTip(sqlResult[w][6]);
					 result.add(bean);
				 }
		   }
		   return result;
	  }
	  
	  public static void updateParameters(String id,String value){
			 String updater=" UPDATE PARAMETERS SET VALUE='@VALUE',TIME='@TIME' WHERE NAME='@NAME'";
			 updater=updater.replace("@VALUE", value);
			 updater=updater.replaceAll("@TIME",DateUtil.getCurrentTime());
			 updater=updater.replaceAll("@NAME", id);
			 Logger.getInstance().debug("execute sql:"+updater);
			 LocalDBUtil.update(updater);
		  }
	  
	  public static String getParameters(String key){
		  String result="";
		  String selector="SELECT NAME,VALUE,TYPE,DESC,MAX,MIN,TIP FROM PARAMETERS WHERE NAME='@NAME'";
		  selector=selector.replace("@NAME", key);
		  String[][] sqlResult=DataUtil.convertList2Array(LocalDBUtil.query(selector));	  
		  if(sqlResult!=null) {
			  result=sqlResult[0][1];
		  }
		  return result;
	  }
}
