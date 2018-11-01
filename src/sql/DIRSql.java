package sql;

public class DIRSql {
        
	public static String getMyActionDirs(String userID,String app){
    	String sql="select ID,NAME,TYPE,PARENTID,APP,MDF_USER,MDF_TIME FROM DIR where MDF_USER='@userID'  and APP='@app' and TYPE<>'0' order by TYPE";
    	sql=sql.replace("@userID", userID);
    	sql=sql.replace("@app", app);
    	return sql;
    }
	   
        public static String getMyChildrenDirs(String userID,String parentID,String app){
        	String sql="select ID,NAME,TYPE,PARENTID,APP,MDF_USER,MDF_TIME FROM DIR where MDF_USER='@userID' and PARENTID='@parentID' and APP='@app' order by ID";
        	sql=sql.replace("@userID", userID);
        	sql=sql.replace("@parentID", parentID);
        	sql=sql.replace("@app", app);
        	return sql;
        }
        
        public static String getID(String userID){
        	String sql="select ISNULL(max(CAST(ID AS INT)),0)+1 as ID FROM DIR where MDF_USER='@userID'";
        	sql=sql.replace("@userID", userID);
        	return sql;
        }
        
        public static String addData(String id,String name,String type,String parentID,String app,String mdfUser,String mdftime){
        	String sql="INSERT INTO DIR (ID,NAME,TYPE,PARENTID,APP,MDF_USER,MDF_TIME) "+
        					"VALUES('@id','@name','@type','@parentID','@app','@userID','@time') ";
        	sql=sql.replace("@id", id);
        	sql=sql.replace("@name", name);
        	sql=sql.replace("@type", type);
        	sql=sql.replace("@parentID", parentID);
        	sql=sql.replace("@app", app);
        	sql=sql.replace("@userID", mdfUser);
        	sql=sql.replace("@time", mdftime);
        	return sql;
        }
        
        public static String nameExist(String app,String parentID,String name,String userID){
        	String sql="select COUNT(*) COUNT FROM DIR where APP='@APP' and MDF_USER='@userID' and PARENTID='@parentID' and NAME='@name'";
        	sql=sql.replace("@userID", userID);
        	sql=sql.replace("@APP", app);
        	sql=sql.replace("@parentID", parentID);
        	sql=sql.replace("@name", name);
        	return sql;
        }
        
        public static String updateName(String id,String userID,String name,String time){
        	String sql="update DIR SET NAME='@name',MDF_TIME='@time' where MDF_USER='@userID' and ID='@id'";
        	sql=sql.replace("@userID", userID);
        	sql=sql.replace("@id", id);
        	sql=sql.replace("@name", name);
        	sql=sql.replace("@time", time);
        	return sql;
        }
        
        public static String delete(String id,String userID){
        	String sql="delete from DIR where MDF_USER='@userID' and ID='@id'";
        	sql=sql.replace("@userID", userID);
        	sql=sql.replace("@id", id);
        	return sql;
        }
        
        public static String updateType(String id,String userID,String type,String time){
        	String sql="update DIR SET TYPE='@type',MDF_TIME='@time' where MDF_USER='@userID' and ID='@id'";
        	sql=sql.replace("@userID", userID);
        	sql=sql.replace("@id", id);
        	sql=sql.replace("@type", type);
        	sql=sql.replace("@time", time);
        	return sql;
        }
        
        public static String getDirFullPath(String userID,String id,String app){
        	String sql="select ID,NAME,TYPE,PARENTID,APP,MDF_USER,MDF_TIME FROM DIR where MDF_USER='@userID' and ID='@id' and APP='@app'";
        	sql=sql.replace("@userID", userID);
        	sql=sql.replace("@id", id);
        	sql=sql.replace("@app", app);
        	return sql;
        }
       
}
