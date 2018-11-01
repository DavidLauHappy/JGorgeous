package resource;

import java.io.File;
import utils.FileUtils;
import utils.StringUtil;

public class Paths {

	private static Paths unique_instance;
	public static Paths getInstance()
	{
		if(unique_instance==null)
		{
			unique_instance=new Paths();
		}
		return unique_instance;	
	}
	private Paths(){}
	///////////////////////////////////////////
	private String basePath="";
	private String iconPath="";
	private String localdbPath="";	
	private String loggingPath="";
	private String configPath="";
	private String workDir="";
	/////////////////////////////////////////
	public  void init(String basepath)
	{
		basePath=FileUtils.formatPath(basepath)+File.separator;
		/*if(basepath==null||"".equals(basepath)||"".equals(basepath.trim())){
			basePath=(System.getProperty("user.dir")); //linux和Windows估计还有区别
		}else{
			basePath=basepath.substring(1, basepath.indexOf("bin"));
		}*/
		iconPath=basePath+"icon"+File.separator;
		localdbPath=basePath+"data"+File.separator;
		loggingPath=basePath+"log"+File.separator;
		configPath=basePath+"init"+File.separator;
		workDir=basePath+"workDir"+File.separator;
		
	}
	
	public String getIconPath(){
		return iconPath;
	}
	
	public String getLoggingPath(){
		return loggingPath;
	}
	
	public String getConfigPath(){
		return configPath;
	}
	
	public String getBasePath(){
		return basePath;
	}
	
	public String getLocalDatabasePath(){
		return localdbPath;
	}
	
	public String getWorkDir(){
		if(StringUtil.isNullOrEmpty(Context.workDir))
			return workDir;
		else{
			return Context.workDir;
		}
	}
}
