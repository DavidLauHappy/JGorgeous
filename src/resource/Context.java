package resource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;


import views.AppView;

public class Context {
		 ////////////////公共参数/////////////////
         public static String LocalIp="";
         public static String Language="";
         public static boolean EnableDebug=false;
         public static String CurrentVersionID="";//当前版本
         public static String EncryptKey="Gorgeous";
         
        
         public static String workDir="";
         public static String appName="Gorgeous";
         public static String appScope="";
         public static String licenceID="";
         public static String expiredate="";
         public static boolean alertEnable=false;
         //////////////部署工具的应用参数////////////////////////
         public static int	SFtpPortNo=22;
         public static int	ServerPort=10088;
         public static String Input_Incluede="0";
         public static String startTime="";
         public static String endTime="";
     	 public static  int   MAX_CONNECTION=20;//最大客户端连接数
     	 public static boolean autoBackupDatabase=false;//安装脚本前自动备份数据库
     	 public static boolean autoBackupDirectory=false;//安装目录前自动备份目录
     	 public static boolean allNodeUploadPkg=false;
     	 public static String uploadType="";
     	 public static int MaxOrder=0;
     	 public static String pkgApp;
     	 public static String cmdbUrl;
     	 public static String cmdbAcessKey;
     	 public static String cmdbSecretKey;
     	 public static int RefreshFrequency;
     	 public static Map<String,String> SysParameters=new HashMap<String,String>();
     	 ///////////////////////////////////////////////////////
     	 public static int inetPort=4096;
     	 
     	 //////////////////////////////////////////////////////////////////////
     	 public static List<File> Files=null;
         
         private static Font fontConsole=null;
         public static Font getConsoleFont(){
        	 if(fontConsole==null)
        		 fontConsole=new Font(AppView.getInstance().getDisplay(),"宋体",12,SWT.NORMAL);
        	 return fontConsole;
         }
         
         //////////////////////数据库服务器在本地init.xml文件里面配置
         public static String SqlServerIp="";
         public static String ServerSqlPort="1433";
         public static String ServerDbName="";
         public static String ServerDbUser="sa";
         public static String ServerDbPasswd="";
         ///////////////////////////////////////////
         public static int ServerDbMinConns=2;
         ////////////////////文档服务器
         public static String FTPServerIp="";
         public static String FTPServerPort="";
         public static String ServerFTPUser="";
         public static String ServerFTPasswd="";
         /////////////////版本服务器
         public static String VersionServerIp="";//版本服务器的ip地址
         public static String  VersionPort="";//版本同步的端口
         ////////////////////////应用信息
         public static String appID="Client.exe";//app的标识
         public static String NewVersion="";//系统最新版本号
         public static String NewVersionDesc="";//系统最新版本描述
         //////////////////////////////////////////////
         public static String  CurrentVersion="";//当前版本号
         ////////////////////////////
         public static boolean NodeEditable=false;
         //开发任务逾期提醒提前天数
         public static String TaskAlertAheadDays="0";
         //禅道数据库信息
         //客户端信息
         //public static String roleType=""; 
         //public static String DomainUser="";
         public static String Apps="";
         //public static String UserName="";
         public static String UserPasswd="";
         public static boolean RemeberPasswd=false;
         //会话信息
         public static Session session=new Session();
         public static String ServiceScheduler="SystemScheduler";//第一层调度
         public static String ServiceSchedulerNode="NodeScheduler";//第二层调度
         public static String ServiceSchedulerQuick="QuickScheduler";//调度服务线程
         public static String ServicePeekerQuick="QuickPeeker";//界面更新线程
         public static String ServiceNetwork="Networker";//网络服务线程
         public static String ServiceNetworkManager="NetworkerManager";//网络管理服务线程
         public static String ServicePeeker="ViewRefresher";//界面更新线程
         public static String ServiceFileChannel="FileNetChannel";//内部网络文件通道服务线程
         public static String ServiceSftp="Sftp";//sftp文件传输服务
         public static String ServiceLinuxShell="LinuxShell";//Linux远程shell执行服务
         public static String ServiceWinShell="WinShell";//Linux远程shell执行服务
         public static String ServiceDbPool="DatabasePool";//数据库连接池服务
}
