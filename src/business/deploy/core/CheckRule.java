package business.deploy.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckRule {
     private static String ipRules="([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
     private static Pattern pattern_globe=null;
     private static Matcher matcher_globe=null;
     
     
     public static boolean checkIp(String input){
    	 pattern_globe=Pattern.compile(ipRules,Pattern.CASE_INSENSITIVE);
    	 matcher_globe=pattern_globe.matcher(input);
		  if(matcher_globe.find())		 
			  return true;
		return false;
     }
}
