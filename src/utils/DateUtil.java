package utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	
	public static String getCurrentTime()
    {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String staticsTime= format.format(calendar.getTime());
		return staticsTime;
	}
	
	public static String getTimeFormLong(long inputLong){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(inputLong);
		String time=sdf.format(cal.getTime());
		return time;
	}
	
	public static String getTime4Long(long inputLong){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(inputLong);
		String time=sdf.format(cal.getTime());
		return time;
	}
	
	public static String getCurrentDate()
    {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String staticsDate= format.format(calendar.getTime());
		return staticsDate;
	}
	
	//yyyyMMddHHmmssSSS
	public static String getCurrentDate(String form)
    {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(form);
		String staticsDate= format.format(calendar.getTime());
		return staticsDate;
	}
	
	//在当前时间上增加2年的时间，每次激活时使用
	public static String getExpiredDate(){
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		//cal.set(Calendar.DAY_OF_YEAR, 1);
		cal.add(Calendar.YEAR, 2);
		String result=format.format(cal.getTime());
	   return result;
	}
	public static String formatDate(String date)
	{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date t=null;
		String ret=date;
		try {
			t = format.parse(date);
			ret=format.format(t);
		} catch (ParseException e) {
			ret=date;
			e.printStackTrace();
		}
		return ret;
	}
     
	public static String formateTime(String time) 
	{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date t=null;
		String ret=time;
		try {
			t = format.parse(time);
			ret=format.format(t);
		} catch (ParseException e) {
			ret=time;
			e.printStackTrace();
		}
		return ret;
	}
	
	public static long getTimeLong(String time){
		String str=time;
		str=str.replace("-", "");//
		str=str.replace(" ", "");
		str=str.replace(":", "");
		Long result=Long.parseLong(str.trim());
		return result;
	}
	
	public static long getTimeLongFromString(String timeString){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
		Date date=null;
		long result=0;
		try{
			date = sdf.parse(timeString);  
			  result=date.getTime();
		}catch (ParseException e) {
			e.printStackTrace();
		}
		 return result;
	}
}
