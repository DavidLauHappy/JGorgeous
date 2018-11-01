package business.deploy.bean;

import utils.StringUtil;

public class ColumnInfo {
	 /** ������ʶ */
	 private boolean isKey;
	 /** ������ */
	 private String name;
	 /** �������� */
	 private int dataType;
	 /** ������������ */
	 private String dataTypeName;
	 /** ������ʶ */
	 private boolean isAutoIncrement;
	 /** ���� */
	 private int precision;
	 /** �Ƿ�Ϊ��*/
	 private int isNull;
	 /**С��λ�� */
	 private int scale;
	 /**Ĭ��ֵ */
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
	//  buf.append("�ֶ����ƣ�" + getName() + "\n");
	  buf.append( getName() + " ");
	 // buf.append("�������ͣ�" + getDataType() + "\n");
	 // buf.append(getDataType() + " ");
	 //buf.append("�������ƣ�" + getDataTypeName() + "\n");
	  buf.append(getDataTypeName() + "");
	  buf.append("("+precision+") ");
	  //buf.append("������" + isKey() + "\n");
	  //buf.append("������" + isAutoIncrement + "\n");
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
	  //buf.append("С��λ����" + scale + "\n");
	  //buf.append("����:"+precision+"\n");
	  //buf.append("��ʼֵ:"+defaultValue+"\n");
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
