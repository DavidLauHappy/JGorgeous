package business.deploy.bean;

import utils.StringUtil;

public class ColumnInfo {
	 /** 主键标识 */
	 private boolean isKey;
	 /** 列名称 */
	 private String name;
	 /** 数据类型 */
	 private int dataType;
	 /** 数据类型名称 */
	 private String dataTypeName;
	 /** 自增标识 */
	 private boolean isAutoIncrement;
	 /** 精度 */
	 private int precision;
	 /** 是否为空*/
	 private int isNull;
	 /**小数位数 */
	 private int scale;
	 /**默认值 */
	 private String defaultValue;
	 public boolean isKey() {
	  return isKey;
	 }
	 public void setKey(boolean isKey) {
	  this.isKey = isKey;
	 } 
	 public String getDefaultValue() { 
	  return defaultValue;
	 }
	 public void setDefaultValue(String defaultValue) {
	  if(StringUtil.isNullOrEmpty(defaultValue)){
	   
	  }else{
	   this.defaultValue = "'"+defaultValue+"'";
	  }
	 }
	 public String getName() {
	  return name;
	 }
	 public void setName(String name) {
	  this.name = name;
	 } 
	 public int getDataType() {
	  return dataType;
	 }
	 public void setDataType(int dataType) {
	  this.dataType = dataType;
	 }
	 public boolean isAutoIncrement() {
	  return isAutoIncrement;
	 }
	 public void setAutoIncrement(boolean isAutoIncrement) {
	  this.isAutoIncrement = isAutoIncrement;
	 }
	 public String getDataTypeName() {
	  return dataTypeName;
	 }
	 public void setDataTypeName(String dataTypeName) {
	  this.dataTypeName = dataTypeName;
	 }
	 
	 public String toString(String dbType){
	  StringBuffer buf = new StringBuffer();
	 // buf.append("-------------\n");
	//  buf.append("字段名称：" + getName() + "\n");
	  buf.append( getName() + " ");
	 // buf.append("数据类型：" + getDataType() + "\n");
	 // buf.append(getDataType() + " ");
	 //buf.append("类型名称：" + getDataTypeName() + "\n");
	  buf.append(getDataTypeName() + "");
	  buf.append("("+precision+") ");
	  //buf.append("主键：" + isKey() + "\n");
	  //buf.append("自增：" + isAutoIncrement + "\n");
	  String nullStr="";
	  if(isNull==0){
		  nullStr="not null";
	  }else{
		  if("1".equals(dbType)){
			  nullStr="";  
		  }else{
			  nullStr="null";
		  }
	  }
	  buf.append(nullStr);
	  String defaultVal="";
	  if(!StringUtil.isNullOrEmpty(defaultValue)){
		  String dfv=defaultValue.replace("'", "");
		  defaultVal=" DEFAULT "+dfv;
		  buf.append(defaultVal );
	  }
	  //buf.append("小数位数：" + scale + "\n");
	  //buf.append("精度:"+precision+"\n");
	  //buf.append("初始值:"+defaultValue+"\n");
	  return buf.toString();
	 }
	 public int getPrecision() {
	  return precision;
	 }
	 public void setPrecision(int precision) {
	  this.precision = precision;
	 }
	 public int getIsNull() {
	  return isNull;
	 }
	 public void setIsNull(int isNull) {
	  this.isNull = isNull;
	 }
	 public int getScale() {
	  return scale;
	 }
	 public void setScale(int scale) {
	  this.scale = scale;
	 }
}
