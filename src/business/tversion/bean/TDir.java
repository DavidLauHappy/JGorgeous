package business.tversion.bean;

import java.util.ArrayList;
import java.util.List;

import utils.DataUtil;
import utils.LocalDBUtil;
import utils.StringUtil;

public class TDir {
	
    private String appName;
    private String id;
    private String name;
    private String type;
    private String parentID;
    private String fullPath;
	public TDir(String appName, String id, String name, String type,String parentID) {
		super();
		this.appName = appName;
		this.id = id;
		this.name = name;
		this.type = type;
		this.parentID = parentID;
	}
	
	public TDir(String appName) {
		super();
		this.appName = appName;
	}
	
	public String getAppName() {
		return appName;
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getParentID() {
		return parentID;
	}
	
	
    
	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String newDirID(){
		String id="";
		String query="SELECT  IFNULL(max(CAST(ID AS INT)),0)+1 FROM DIR";
		 String[][] sqlResult=DataUtil.convertList2Array(LocalDBUtil.query(query));	  
		 if(sqlResult!=null){
			  id=sqlResult[0][0];
		 }
		 return id;
	}
	
	public List<TDir> getChildren(String paretID){
		 List<TDir> result=new ArrayList<TDir>();
		String query="SELECT APPNAME,ID,NAME,TYPE,PARENTID FROM DIR WHERE APPNAME='@APPNAME' AND  PARENTID='@PARENTID'";
		query=query.replace("@APPNAME", this.appName);
		if(StringUtil.isNullOrEmpty(paretID)){
			query=query.replace("@PARENTID", "0");
		}else{
			query=query.replace("@PARENTID", paretID);
		}
		  String[][] sqlResult=DataUtil.convertList2Array(LocalDBUtil.query(query));
    	  if(sqlResult!=null){
    		  for(int w=0;w<sqlResult.length;w++){
    			  TDir bean=new TDir(sqlResult[w][0],sqlResult[w][1],sqlResult[w][2],sqlResult[w][3],sqlResult[w][4]);
    			  result.add(bean);
    		  }
    	  }
		return result;
	}
	
	public String getFullPath(String path,String nextID){
		String query="SELECT NAME,PARENTID FROM DIR WHERE APPNAME='@APPNAME' AND  ID='@NEXTID'";
		query=query.replace("@APPNAME", this.appName);
		query=query.replace("@NEXTID", nextID);
		String[][] sqlResult=DataUtil.convertList2Array(LocalDBUtil.query(query));
  	    if(sqlResult!=null){
  		    String next=sqlResult[0][1];
  		    String fullpath=sqlResult[0][0]+"/"+path;
  		    return this.getFullPath(fullpath, next);
  	   }else{
  		   return "/"+path;
  	   }
		
	}
	public boolean hibernateNew(String appName,String id,String name,String type,String parentID){
		String updater="INSERT INTO DIR( APPNAME,ID,NAME,TYPE,PARENTID) "+
				 "VALUES('@APPNAME','@ID','@NAME','@TYPE','@PARENTID')";
				updater=updater.replace("@APPNAME", appName);
				updater=updater.replace("@ID",id);
				updater=updater.replace("@NAME", name);
				updater=updater.replace("@TYPE",type);
				updater=updater.replace("@PARENTID", parentID);
				boolean result=LocalDBUtil.update(updater);	
			return result;
	}
	
	public boolean updateName(String appName,String parentID,String id,String name){
		String query="SELECT COUNT(*) FROM DIR WHERE APPNAME='@APPNAME' AND PARENTID='@PARENTID' AND NAME='@NAME'";
		query=query.replace("@PARENTID", parentID);
		query=query.replace("@NAME", name);
		query=query.replace("@APPNAME", appName);
		 String[][] sqlResult=DataUtil.convertList2Array(LocalDBUtil.query(query));
	   	  if(sqlResult!=null){
	   		    int count= Integer.parseInt(sqlResult[0][0]);
		   		 if(count>0){
		   			 return false;
		   		 }else{
		   			String updater="UPDATE  DIR SET NAME='@NAME' WHERE  ID='@ID'";
					updater=updater.replace("@NAME", name);
					updater=updater.replace("@ID",id);
					boolean result=LocalDBUtil.update(updater);	
					return result;
		   		 }
	   	  }else{
	   		String updater="UPDATE  DIR SET NAME='@NAME' WHERE  ID='@ID'";
			updater=updater.replace("@NAME", name);
			updater=updater.replace("@ID",id);
			boolean result=LocalDBUtil.update(updater);	
			return result;
	   	  }
		
	}
	
	public boolean updateType(String type){
		String updater="UPDATE  DIR SET TYPE='@TYPE' WHERE  ID='@ID'";
				updater=updater.replace("@TYPE", type);
				updater=updater.replace("@ID",this.id);
				boolean result=LocalDBUtil.update(updater);	
	   return result;
	}
	
	public void delete(){
		String updater="DELETE FROM  DIR  WHERE  ID='@ID'";
		updater=updater.replace("@ID",this.getId());
		LocalDBUtil.update(updater);	
		updater="DELETE FROM  DIR  WHERE  PARENTID='@PARENTID'";
		updater=updater.replace("@PARENTID",this.getId());
		LocalDBUtil.update(updater);	
	}
}
