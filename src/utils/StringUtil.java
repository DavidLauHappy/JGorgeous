package utils;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import resource.Logger;

public class StringUtil {
	
      public static boolean isNullOrEmpty(String str){
    	  if( str!=null&&!"".equals(str.trim()))
    		  return false;
    	  return true;
      }
      
      public static boolean isNullOrEmpty(String str,int limit){
    	  if( str!=null&&!"".equals(str.trim()))
    		  return false;
    	  if(str.length()>limit)
    		  return false;
    	  return true;
      }
      private static Pattern pattern_globe=null;
      private static Matcher matcher_globe=null;
      private static String ipRexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";  
      public static boolean isIp(String str){
    	  pattern_globe=Pattern.compile(ipRexp,Pattern.CASE_INSENSITIVE);
     	 matcher_globe=pattern_globe.matcher(str);
 		  if(matcher_globe.find())	{ 
 			  return true;
 		  }
     	return false;
      }
      
      //0-65535
      private static String portRexp = "^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])$";  
      public static boolean isPort(String str){
    	  pattern_globe=Pattern.compile(portRexp,Pattern.CASE_INSENSITIVE);
      	 matcher_globe=pattern_globe.matcher(str);
  		  if(matcher_globe.find())	{ 
  			  return true;
  		  }
      	return false;
      }
      
     public static boolean checkRegex(String text,String regex){
    	 pattern_globe=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
      	 matcher_globe=pattern_globe.matcher(text);
  		  if(matcher_globe.find())	{ 
  			  return true;
  		  }
      	return false;
     }
     
     public static boolean numMid(String start,String end,String input){
    	 int intS=Integer.parseInt(start);
    	 int intE=Integer.parseInt(end);
    	 int curr=Integer.parseInt(input);
    	 if((curr-intS)*(curr-intE)<0){
    		 return true;
    	 }else{
    		 return false;
    	 } 
     }
     
     //从形如：刘伟(06252)中提取用户编号
     public static String getUserIdFromName(String fullName){
    	 int index=fullName.indexOf("(");
    	 if(index!=-1){
    		 String result=fullName.substring(index);
    		 result=result.replace("(", "");
    		 result=result.replace(")", "");
    		 result=result.trim();
    		 return result;
    	 }else{
    		 return fullName;
    	 }
     }
 /*     private static String timeSpanRegx="^(20|21|22|23|[0-1]\\d):[0-5]\\d-(20|21|22|23|[0-1]\\d):[0-5]\\d$";
      public  static void main(String[] args){
    	  String port1="16:00-08:00";
    	  System.out.println(port1+":"+checkRegex(port1,timeSpanRegx));
    	  String port2="2a:00:00-38:00";
    	  System.out.println(port2+":"+checkRegex(port2,timeSpanRegx));
    	  String ver="CTS_v201708021017";
    	  System.out.println(ver);
    	  String app=ver.substring(0, ver.indexOf("_"));
    	  System.out.println(app);
    	  String time=DateUtil.getCurrentDate("HHmm");
    	  System.out.println(time);
      }*/
      
      public static String rightPad(String source,int len){
    	  String result="";
    	  if(source==null)
    		  return "";
    	  int length=source.length();
    	  if(length>=len){
    		  result=source.substring(0, len); 
    	  }
    	  else{
    		  result=source;
    		  for(int w=0;w<len-length;w++){
    			  result=result+"0";
    		  }
    	  }
    	  return result;
      }
      
      public static String leftpad(String source,int len,String padding){		
			if(source==null||source.length()>=len||padding==null||padding.length()==0)
				return source;
			String target=source;
			for(int w=len-source.length();w>0;w=w-padding.length()){
				target=padding+target;
			}
			return target;
		}
      
      public static String rtrim(String source,String chr){
    	  if(source!=null&&chr!=null){
    		  String result=source;
    		  while(result.endsWith(chr)){
    			  result=result.substring(0,result.lastIndexOf(chr));
    		  }
    		  return result;
    	  }else{
    		  return source;
    	  }
      }
      
      public static String ltrim(String source,String chr){
    	  if(source!=null&&chr!=null){
    		  String result=source;
    		  while(result.startsWith(chr)){
    			  result=result.substring(result.indexOf(chr)+1);
    		  }
    		  return result;
    	  }else{
    		  return source;
    	  }
      }
      
      public static String rightPad(String source,int len,String padding){
    	  String result="";
    	  if(source==null)
    		  return "";
    	  int length=source.length();
    	  if(length>=len){
    		  result=source.substring(0, len); 
    	  }
    	  else{
    		  result=source;
    		  for(int w=0;w<len-length;w=w+padding.length()){
    			  result=result+padding;
    		  }
    	  }
    	  return result;
      }
      
      public static String rightPadBytes(String source,int len,String padding){
    	  String result="";
    	  if(source==null)
    		  return "";
    	  int length=source.getBytes().length;
    	  if(length>=len){
    		  result=source.substring(0, len); 
    	  }
    	  else{
    		  result=source;
    		  for(int w=0;w<len-length;w=w+padding.length()){
    			  result=result+padding;
    		  }
    	  }
    	  return result;
      }
      
      public static  Map<String,String> parseXML(String xmlStr){
    	   StringReader read=new StringReader(xmlStr);
			InputSource source=new InputSource(read);
			SAXBuilder builder = new SAXBuilder();  
			Document doc;
			 Map<String,String> result=null;
			 try {
				 doc= builder.build(read); 
					 if(doc!=null){
						 Element root=doc.getRootElement();
						 Element cmd=root.getChild("PACKET");
						 List<Element> children=cmd.getChildren();
						 if(children!=null){
							 result=new HashMap<String,String>();
							 for (Element childElement : children) {  
								 result.put(childElement.getName(), childElement.getText());  
							 }
						 }else{
							 return null;
						 }
					 }
					return result; 
			 }
			 catch(Exception e){
				 Logger.getInstance().error("解析xml["+xmlStr+"]异常："+e.toString());
				 return null;
			 }
      }
      
 
      public static boolean startFullWith(String longer,String shorter){
    	   String tail=longer.replace(shorter, "");
    	   if(StringUtil.isNullOrEmpty(tail)||tail.startsWith(File.separator))
 			  return true;
 		return false;
      }
}
