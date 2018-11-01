package business.deploy.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.StringUtil;

public class TableInfo {
   private String tableName;
   private String pkName;
   private String dbType;
   private List<ColumnInfo> columns;
   private Map<String,Boolean> primaryKeys;
   
   public TableInfo(){
	   columns=new ArrayList<ColumnInfo>();
	   primaryKeys=new HashMap<String, Boolean>();
   }
   
   public void setTableName(String tableName){
	   this.tableName=tableName;
   }
   
   public void setColInfo(String columnName, ColumnInfo col){
	   columns.add(col);
   }
   
   public void setPrimaryKey(String columnName,boolean isPrimaryKey){
	   primaryKeys.put(columnName, isPrimaryKey);
   }
   
   
   public String getPkName() {
	return pkName;
}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	

public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

public String toString(){
	   StringBuffer sb=new StringBuffer();
	   if(this.dbType.equals("0")){
		   if(this.columns.size()>0||this.primaryKeys.size()>0){
			   String header="CREATE TABLE ["+tableName+"]("+"\r\n";
			   sb.append(header);
			   for(ColumnInfo column:this.columns){
				   sb.append("	"+column.toString(this.dbType));
				   sb.append(","+"\r\n");
			   }
			   String tail=");"+"\r\n";
			   sb.append(tail);
			   if(!StringUtil.isNullOrEmpty(pkName)){
				   String pk="ALTER TABLE "+tableName+" ADD CONSTRAINT "+pkName+" PRIMARY KEY (";
				   for(String col:primaryKeys.keySet()){
					   if(primaryKeys.get(col)){
						   pk+=col+",";
					   }
				   }
				   pk=StringUtil.rtrim(pk, ",");
				   pk+=")";
				   sb.append(pk);
			   }
			   sb.append("\r\n"+"go");
		   }
	   }else{
		   if(this.columns.size()>0||this.primaryKeys.size()>0){
			   String header="CREATE TABLE "+tableName+"("+"\r\n";
			   sb.append(header);
			   String clnStrs="";
			   for(ColumnInfo column:this.columns){
				   clnStrs+=" "+column.toString(this.dbType);
				   clnStrs+=",";
			   }
			   clnStrs=StringUtil.rtrim(clnStrs, ",");
			   clnStrs=clnStrs.replace(",", ",\r\n")+"\r\n";
			   sb.append(clnStrs);
			   String tail=");"+"\r\n";
			   sb.append(tail);
			   if(!StringUtil.isNullOrEmpty(pkName)){
				   String pk="ALTER TABLE "+tableName+" ADD CONSTRAINT "+pkName+" PRIMARY KEY (";
				   for(String col:primaryKeys.keySet()){
					   if(primaryKeys.get(col)){
						   pk+=col+",";
					   }
				   }
				   pk=StringUtil.rtrim(pk, ",");
				   pk+=")";
				   sb.append(pk);
			   }
			   sb.append("\r\n");
		   }
	   }
	   return sb.toString();
   }
   
}
