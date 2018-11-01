package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
	
	public static boolean IsPassword(String str) {
		String regex = "[A-Za-z]+[0-9]+";
		return match(regex, str);
		}

		/**
		* 验证输入密码长度 (6-18位)
		* 
		* @param 待验证的字符串
		* @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
		*/
		public static boolean IsPasswLength(String str) {
		String regex = "^\\w{6,18}$";
		return match(regex, str);
		}
		
		private static boolean match(String regex, String str) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			return matcher.matches();
			}
}
