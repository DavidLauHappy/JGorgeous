package resource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;


import views.AppView;

public class Context {
		 ////////////////��������/////////////////
         public static String LocalIp="";
         public static String Language="";
         public static boolean EnableDebug=false;
         public static String CurrentVersionID="";//��ǰ�汾
         public static String EncryptKey="Gorgeous";
         
        
         public static String workDir="";
         public static String appName="Gorgeous";
         public static String appScope="";
         public static String licenceID="";
         public static String expiredate="";
         public static boolean alertEnable=false;
         //////////////���𹤾ߵ�Ӧ�ò���////////////////////////
         public static int	SFtpPortNo=22;
         public static int	ServerPort=10088;
         public static String Input_Incluede="0";
         public static String startTime="";
         public static String endTime="";
     	 public static  int   MAX_CONNECTION=20;//���ͻ���������
     	 public static boolean autoBackupDatabase=false;//��װ�ű�ǰ�Զ��������ݿ�
     	 public static boolean autoBackupDirectory=false;//��װĿ¼ǰ�Զ�����Ŀ¼
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
        		 fontConsole=new Font(AppView.getInstance().getDisplay(),"����",12,SWT.NORMAL);
        	 return fontConsole;
         }
         
         //////////////////////���ݿ�������ڱ���init.xml�ļ���������
         public static String SqlServerIp="";
         public static String ServerSqlPort="1433";
         public static String ServerDbName="";
         public static String ServerDbUser="sa";
         public static String ServerDbPasswd="";
         ///////////////////////////////////////////
         public static int ServerDbMinConns=2;
         ////////////////////�ĵ�������
         public static String FTPServerIp="";
         public static String FTPServerPort="";
         public static String ServerFTPUser="";
         public static String ServerFTPasswd="";
         /////////////////�汾������
         public static String VersionServerIp="";//�汾��������ip��ַ
         public static String  VersionPort="";//�汾ͬ���Ķ˿�
         ////////////////////////Ӧ����Ϣ
         public static String appID="Client.exe";//app�ı�ʶ
         public static String NewVersion="";//ϵͳ���°汾��
         public static String NewVersionDesc="";//ϵͳ���°汾����
         //////////////////////////////////////////////
         public static String  CurrentVersion="";//��ǰ�汾��
         ////////////////////////////
         public static boolean NodeEditable=false;
         //������������������ǰ����
         public static String TaskAlertAheadDays="0";
         //�������ݿ���Ϣ
         //�ͻ�����Ϣ
         //public static String roleType=""; 
         //public static String DomainUser="";
         public static String Apps="";
         //public static String UserName="";
         public static String UserPasswd="";
         public static boolean RemeberPasswd=false;
         //�Ự��Ϣ
         public static Session session=new Session();
         public static String ServiceScheduler="SystemScheduler";//��һ�����
         public static String ServiceSchedulerNode="NodeScheduler";//�ڶ������
         public static String ServiceSchedulerQuick="QuickScheduler";//���ȷ����߳�
         public static String ServicePeekerQuick="QuickPeeker";//��������߳�
         public static String ServiceNetwork="Networker";//��������߳�
         public static String ServiceNetworkManager="NetworkerManager";//�����������߳�
         public static String ServicePeeker="ViewRefresher";//��������߳�
         public static String ServiceFileChannel="FileNetChannel";//�ڲ������ļ�ͨ�������߳�
         public static String ServiceSftp="Sftp";//sftp�ļ��������
         public static String ServiceLinuxShell="LinuxShell";//LinuxԶ��shellִ�з���
         public static String ServiceWinShell="WinShell";//LinuxԶ��shellִ�з���
         public static String ServiceDbPool="DatabasePool";//���ݿ����ӳط���
}
