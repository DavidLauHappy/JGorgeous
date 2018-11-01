package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
	
	public static boolean IsPassword(String str) {
		String regex = "[A-Za-z]+[0-9]+";
		return match(regex, str);
		}

		/**
		* ��֤�������볤�� (6-18λ)
		* 
		* @param ����֤���ַ���
		* @return ����Ƿ��ϸ�ʽ���ַ���,���� <b>true </b>,����Ϊ <b>false </b>
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
