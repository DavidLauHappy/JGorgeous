package resource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.db.DBConnectionManager;

import utils.DataUtil;
import utils.LocalDBUtil;
import utils.SqlServerUtil;
import utils.StringUtil;

public class Dictionary {
	private static Map<String,Map<String,String>> DictionaryMap=new HashMap<String,Map<String,String>>();
	private static Map<String,List<Item>>  DictionaryList=new HashMap<String,List<Item>>();
	
	public static List<Item> getDictionaryList(String type){
		if(!DictionaryList.containsKey(type))
			laodDictionaryData();
		if(DictionaryList.containsKey(type))
			return DictionaryList.get(type);
		else 
			return null;
	}
	
	public static Map<String,String> getDictionaryMap(String type){
		if(!DictionaryMap.containsKey(type))
			laodDictionaryData();
		if(DictionaryMap.containsKey(type))
			return DictionaryMap.get(type);
		else 
			return null;
	}
	
	
	private static void laodDictionaryData(){
		String sql= "SELECT ID,ITEM,VALUE  FROM DICT ORDER BY ID,ITEM";
		 Logger.getInstance().debug("query sql:"+sql);
		 Connection conn = null;
		 try{
			 conn=DBConnectionManager.getInstance().getConnection();
				List sqlResult=SqlServerUtil.executeQuery(sql, conn);
				if(sqlResult!=null&&sqlResult.size()>0){
			    	 String lastKey="";
			    	 List<Item> dataList=null;
			    	 Map<String,String> dataMap=null;
					 for(int w=0;w<sqlResult.size();w++){
						 Map line=(Map)sqlResult.get(w);
						 String key=(String)line.get("ID");
						 if(!key.equals(lastKey)){
							 if(!StringUtil.isNullOrEmpty(lastKey)&&!DictionaryList.containsKey(lastKey)){
								 DictionaryList.put(lastKey, dataList);
							 }
							if(!StringUtil.isNullOrEmpty(lastKey)&&!DictionaryMap.containsKey(key)){
								 DictionaryMap.put(lastKey, dataMap);
							 }
							 dataList=new ArrayList<Item>();
							 dataMap=new HashMap<String, String>();
							 lastKey=key;
						 }
						 Item bean=new Item();
						 bean.setType((String)line.get("ID"));
						 bean.setKey((String)line.get("ITEM"));
						 bean.setValue((String)line.get("VALUE"));
						 dataList.add(bean);
						 dataMap.put(bean.getKey(), bean.getValue());
					 }
					 DictionaryList.put(lastKey, dataList);
					 DictionaryMap.put(lastKey, dataMap);
				}
		 }catch(Exception e){
			 Logger.getInstance().error("加载字典数据异常："+e.toString());
		 }finally{
			 DBConnectionManager.getInstance().freeConnction(conn);
		 }
	}
	
	public static void addDictionaryItem(String type,String key,String value){
		String queryExist="SELECT COUNT(*) FROM DICTIONARY WHERE TYPE='@TYPE' AND KEY='@KEY'";
		queryExist=queryExist.replace("@TYPE", type);
		queryExist=queryExist.replace("@KEY", key);
		String[][] sqlResult=DataUtil.convertList2Array(LocalDBUtil.query(queryExist));
		if(sqlResult!=null){
			int count=Integer.parseInt(sqlResult[0][0]);
			if(count<=0){
				String inserter="INSERT INTO DICTIONARY(TYPE,TYPENAME,KEY,VALUE) VALUES('@TYPE','@TYPENAME','@KEY','@VALUE')";
				inserter=inserter.replace("@TYPENAME", "自定义");
				inserter=inserter.replace("@TYPE", type);
				inserter=inserter.replace("@KEY", key);
				inserter=inserter.replace("@VALUE", value);
				LocalDBUtil.update(inserter);
				DictionaryMap.clear();
				DictionaryList.clear();
			}
		}	
	}
	
	public static void removeDictionarItem(String type,String key){
		String deleter="DELETE FROM DICTIONARY WHERE TYPE='@TYPE' AND KEY='@KEY'";
		deleter=deleter.replace("@TYPE", type);
		deleter=deleter.replace("@KEY", key);
		LocalDBUtil.update(deleter);
		DictionaryMap.clear();
		DictionaryList.clear();
	}
}
