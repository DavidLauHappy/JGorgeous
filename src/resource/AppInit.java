package resource;


import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import common.db.DBConnectionManager;
import common.localdb.LocalDataHelper;


import utils.DateUtil;
import utils.FileUtils;
import utils.NetworkUtil;
import utils.SqlServerUtil;
import utils.StringUtil;


public class AppInit {
	private static AppInit unique_instance;
	public static AppInit getStartInstance(){
		if(unique_instance==null)
		{
			unique_instance=new AppInit();
		}
		return unique_instance;	
	}
	
	private AppInit(){}
	
	public  boolean startUp()	  
	{
        boolean result=this.loadConfig();
        result=result&&this.loadParameterFromDb();
        result=result&&this.loadConfigFromLocalDb();
        this.loadAppInfo();
        result=result&&this.initiateResource();
		return result;
	}
	
	public void refreshParameters(){
		this.loadConfigFromLocalDb();
	}

	private static final int defaultMaxCount=20;
	private static final int defaultMaxOrder=16;
	
	private boolean loadConfig(){
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		String initPath="";
		try {
			 initPath=Paths.getInstance().getLocalDatabasePath()+"init.xml";
			doc = builder.build(initPath);
			Element sysParams=doc.getRootElement();
			String SqlServerIp=sysParams.getChildText("DBSERVER_IP");
			 if(!StringUtil.isNullOrEmpty(SqlServerIp)){
				   Context.SqlServerIp=SqlServerIp;
			  } 
			 String ServerSqlPort=sysParams.getChildText("PORT");
			   if(!StringUtil.isNullOrEmpty(ServerSqlPort)){
				   Context.ServerSqlPort=ServerSqlPort;
				} 
			   String ServerDbName=sysParams.getChildText("DB_NAME");
			   if(!StringUtil.isNullOrEmpty(ServerDbName)){
				   Context.ServerDbName=ServerDbName;
				} 
			   String ServerDbUser=sysParams.getChildText("DB_USER");
			   if(!StringUtil.isNullOrEmpty(ServerDbUser)){
				   Context.ServerDbUser=ServerDbUser;
				} 
			   String ServerDbPasswd=sysParams.getChildText("DB_PASSWD");
			   if(!StringUtil.isNullOrEmpty(ServerDbPasswd)){
				   Context.ServerDbPasswd=ServerDbPasswd;
				} 
			   return true;
		}
		catch(Exception e){
			Logger.startInfo("AppInit.loadConfig() 加载解析初始化配置文件["+initPath+"]发生异常:"+e.toString());
		}
		return false;
	}
	
	
	private boolean loadParameterFromDb(){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String query="select  NAME,VALUE from SYS_PARAMETER";
			List result=SqlServerUtil.executeQuery(query, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> parameters=(Map)result.get(w);
					String name=parameters.get("NAME");
					String value=parameters.get("VALUE");
					value=value.trim();
					name=name.trim();
					Context.SysParameters.put(name, value);
					  if(("FTPServerIp").equals(name)){
						   String FTPServerIp=value;
						   Context.FTPServerIp=FTPServerIp;
						} 
					   if(("FTPServerPort").equals(name)){
						   String FTPServerPort=value;
						   Context.FTPServerPort=FTPServerPort;
						} 
					   if(("ServerFTPUser").equals(name)){
						   String ServerFTPUser=value;
						   Context.ServerFTPUser=ServerFTPUser;
						}   
					   if(("ServerFTPasswd").equals(name)){
							   String ServerFTPasswd=value;
							   Context.ServerFTPasswd=ServerFTPasswd;
						} 
					   if(("CMDP_URL").equals(name)){
						   String CMDP_URL=value;
						   Context.cmdbUrl=CMDP_URL;
						} 
					   if(("ACCESS_KEY").equals(name)){
						   String ACCESS_KEY=value;
						   Context.cmdbAcessKey=ACCESS_KEY;
						} 
					   if(("SECRET_KEY").equals(name)){
						   String SECRET_KEY=value;
						   Context.cmdbSecretKey=SECRET_KEY;
						} 
					   if(("NetPortNo").equals(name)){		 
							 String NetPortNo=value;
								int netPortNo=Integer.parseInt(NetPortNo);
								Context.ServerPort=netPortNo;
						 }
					   if(("FilePortNo").equals(name)){		 
							 String FilePortNo=value;
								int filePortNo=Integer.parseInt(FilePortNo);
								Context.inetPort=filePortNo;
						}
					   if(("TaskAlertDays").equals(name)){		 
							 String TaskAlertAheadDays=value;
								Context.TaskAlertAheadDays=TaskAlertAheadDays;
						}
					   if(("NodeEditable").equals(name)){		 
							 String editable=value;
							if("YES".equals(editable.toUpperCase())||"Y".equals(editable.toUpperCase())){
								Context.NodeEditable=true;
							}
						}
					   //版本服务器信息
					  if(("VersionServerIp").equals(name)){
						  	String VersionServerIp=value;
						  	Context.VersionServerIp=VersionServerIp;
					  }
					  if(("VersionPort").equals(name)){
						  String VersionPort=value;
						  Context.VersionPort=VersionPort;
					  }
				}
			}
		}catch(Exception e){
			 return false;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return true;
	}
	
	//加载应用最新版本信息
	private void loadAppInfo(){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String query="select  APPID,NVERSION,VDESC from APP_VERSION where APPID='@APPID'";
			query=query.replace("@APPID", Context.appID);
			List result=SqlServerUtil.executeQuery(query, conn);
			if(result!=null&&result.size()>0){
				Map<String,String> map=(Map)result.get(0);
				String newVer=map.get("NVERSION");
				Context.NewVersion=newVer;
				String verDesc=map.get("VDESC");
				Context.NewVersionDesc=verDesc;
			}
		}catch(Exception e){
			 
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
	}

	private boolean loadConfigFromLocalDb(){
		Map<String, String> parameters=LocalDataHelper.getSystemParameters();
		////公共配置参数区域///////////////////////////////////////////////////////
			   if(parameters.containsKey("DEBUG")){
				   String enableDebug=parameters.get("DEBUG");
				   if("1".equals(enableDebug))
					   Context.EnableDebug=true;
				   else
					   Context.EnableDebug=false;
			   }
		   if(parameters.containsKey("LANGUAGE")){
			   String language=parameters.get("LANGUAGE");
			   if("CHINESE".equals(language.toUpperCase())
						||("ZH_CN").equals(language.toUpperCase())){
							Context.Language="zh";
						}
				else if("ENGLISH".equals(language.toUpperCase())
						||("EN").equals(language.toUpperCase())){
					Context.Language="en";
				}
		   }
		   if(parameters.containsKey("KEY")){
			   String key=parameters.get("KEY");
				Context.EncryptKey=StringUtil.rightPad(key, 8);
				}
		   if(parameters.containsKey("WORKDIR")){
			   String path=parameters.get("WORKDIR");
				Context.workDir=FileUtils.formatPath(path);
				}
		   if(parameters.containsKey("MSGALERT")){
			   String path=parameters.get("MSGALERT");
			   if("1".equalsIgnoreCase(path))
				   Context.alertEnable=true;
				}
		    if(parameters.containsKey("LICENSE")){
				   String licence=parameters.get("LICENSE");
					Context.licenceID=licence;
					} 
		    if(parameters.containsKey("Expired_Date")){
				   String expired=parameters.get("Expired_Date");
					Context.expiredate=expired;
					} 
		    	if(parameters.containsKey("DefaultRole")){
				   String defaultRole=parameters.get("DefaultRole");
					Context.session.roleID=defaultRole;
					}
		    	//软件当前版本
		    	if(parameters.containsKey("CurrentVersion")){
					   String CurrentVersion=parameters.get("CurrentVersion");
						Context.CurrentVersion=CurrentVersion;
				}
		    	//当前软件编号
		    	if(parameters.containsKey("appID")){
					   String appID=parameters.get("appID");
						Context.appID=appID;
				}
		////--公共配置参数区域///////////////////////////////////////////////////////
		    
	  ///////版本部署工具专有参数///////////////////////////////////////////////////////	    
			
			try{
				 if(parameters.containsKey("Max_Connection")){		 
					 String maxConnect=parameters.get("Max_Connection");
						int macCount=Integer.parseInt(maxConnect);
						Context.MAX_CONNECTION=macCount;
				 }
			}
			catch(Exception exp){
				Context.MAX_CONNECTION=defaultMaxCount;
			}
		   if(parameters.containsKey("Input_Incluede")){
			   String input=parameters.get("Input_Incluede");
				Context.Input_Incluede=input;
				}
		 
		   if (parameters.containsKey("Time_Span")){
				   String expired=parameters.get("Time_Span");
				   if(expired.indexOf("-")!=-1){
					   String[] times=expired.split("-");
					   Context.startTime=times[0];
					   Context.endTime=times[1];
				     }
				} 
		   if(parameters.containsKey("AllNode_upload")){
			   String allUpload=parameters.get("AllNode_upload");
				   if("1".equals(allUpload)){
					   Context.allNodeUploadPkg=true;
				   }else{
					   Context.allNodeUploadPkg=false;
				   }
				} 
		   if(parameters.containsKey("Auto_BackupDb")){
			   String autoBackDb=parameters.get("Auto_BackupDb");
				   if("1".equals(autoBackDb)){
					   Context.autoBackupDatabase=true;
				   }else{
					   Context.autoBackupDatabase=false;
				   }
				} 
		   if(parameters.containsKey("Auto_BackupDir")){
			   String autoBackDir=parameters.get("Auto_BackupDir");
				   if("1".equals(autoBackDir)){
					   Context.autoBackupDirectory=true;
				   }else{
					   Context.autoBackupDirectory=false;
				   }
				}
		   if(parameters.containsKey("Upload_Type")){
			   String uploadType=parameters.get("Upload_Type");
			   Context.uploadType=uploadType;
				} 
		   if(parameters.containsKey("Max_Order")){
				   try{
					   String maxOrder=parameters.get("Max_Order");
					   int order=Integer.parseInt(maxOrder);
					   Context.MaxOrder=order;
				   }catch(Exception e){
					   Context.MaxOrder=defaultMaxOrder;
				   }
				} 
	    if(parameters.containsKey("DefaultPkgApp")){
		   	String DefaultPkgApp=parameters.get("DefaultPkgApp");
		   	Context.pkgApp=DefaultPkgApp;
			} 
		   
		   
		   ///////--版本部署工具专有参数///////////////////////////////////////////////////////	    
		   if(parameters.containsKey("User_Passwd")){
				   String passwd=parameters.get("User_Passwd");
				   Context.UserPasswd=passwd;
				} 
		   if(parameters.containsKey("User_Id")){
			   String userID=parameters.get("User_Id");
			   Context.session.userID=userID;
			} 
		   if(parameters.containsKey("Remember_Flag")){
			   String remFlag=parameters.get("Remember_Flag");
			   if("1".equals(remFlag)){
				   Context.RemeberPasswd=true;
			   }else{
				   Context.RemeberPasswd=false;
			   }
			} 
		  
		   if(parameters.containsKey("RefreshFrequency")){
			   String RefreshFrequency=parameters.get("RefreshFrequency");
			   Context.RefreshFrequency=Integer.parseInt(RefreshFrequency)*1000;
			}
		   if(parameters.containsKey("AppScope")){
			   String AppScope=parameters.get("AppScope");
			   Context.appScope=AppScope;
			}
		 
		   
		   
	    return true;
	}
	//必要的资源初始化，包括网络资源，数据库加载
	private boolean initiateResource(){
		String localIp=NetworkUtil.getLocalIp();
		String mac=NetworkUtil.getLocalMac();
		Context.LocalIp=localIp;
		Context.session.clientIp=localIp;
		Context.session.clientMac=mac;
		return true;
	}
	
	
	public void loadSysInfo(){
		/*Map<String, String> parameters=DataHelper.getSystemParameters();
		   if(parameters.containsKey("ROLE")){
			   String roleView=parameters.get("ROLE");
			   Context.session.roleID=roleView;
		   }
		   if(parameters.containsKey("AppName")){
			   String appname=parameters.get("AppName");
			   Context.appName=appname;
		   }*/
	}
	public boolean checkLicence(){
		if(StringUtil.isNullOrEmpty(Context.licenceID)){
			String licenseID=NetworkUtil.getInetAddress()+"@"+this.getSystemUser();
			LocalDataHelper.updateParameters("LICENSE", licenseID);
			String expired=DateUtil.getExpiredDate();
			LocalDataHelper.updateParameters("Expired_Date", expired);
			return true;
		}
		else{
			String licenseID=NetworkUtil.getInetAddress()+"@"+this.getSystemUser();
			long current=Long.parseLong(DateUtil.getCurrentDate("yyyyMMddHHmmss"));
			long expried=Long.parseLong(Context.expiredate);
			if(licenseID.equals(Context.licenceID)&&expried>=current){
				return true;
			}
		}
		//return false;
		return true;
	}
	
	private String getSystemUser(){
		String localUser="";
		try{
			Properties prop = System.getProperties();
			localUser=prop.getProperty("user.name");
			//Context.session.userID=localUser;
		}catch(Exception e){
			Logger.getInstance().error("AppInit.getSystemUser()获取本地操作系统域用户发生异常:"+e.toString());
		}
		return localUser;
	}
	
	public String getAppName(){
		String result="Gorgeous";
		if(Context.session.roleID.equals(Constants.RoleType.Deploy.ordinal()+"")){
			result="部署员";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.TesterVersion.ordinal()+"")){
			result="测试版本管理员";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.DeveloperVersion.ordinal()+"")){
			result="开发版本管理员";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.Developer.ordinal()+"")){
			result="开发员";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.DeveloperManager.ordinal()+"")){
			result="开发管理员";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.Tester.ordinal()+"")){
			result="测试员";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.Admin.ordinal()+"")){
			result="系统管理员";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.Auditor.ordinal()+"")){
			result="部署审计员";
		}
		return result;
	}
}
