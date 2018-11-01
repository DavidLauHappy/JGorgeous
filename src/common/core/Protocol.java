package common.core;

public class Protocol {
	  public static int	DefaultPort=10088;
	  public static final String UploadPKg="A100";//�ϴ��汾Ŀ¼
	   public static final String FileCopy="A001";//�ļ�(Ŀ¼)����
	   public static final String DeleteFile="A002";//�ļ�(Ŀ¼)ɾ��
	   public static final String ScriptExecute="A003";//�ű��ļ�ִ��
	   public static final String SqlserverBackup="A004";//���ݿⱸ��
	   public static final String ServiceControl="A005";//��������/ֹͣ
	   public static final String Start="B000";//��ʼָ��,Э��ĵ�һ��ָ��
	   public static final String KeepAlive="B001";//��������
	   public static final String  Barrier="B002";//�ϰ�ָ�ÿ�ΰ汾������һ������ָ��ϰ�ָ����߿ͻ�������ҵ��ָ���´�����ˣ����Կ�ʼ�ɻ���
	   public static final String  Resend="B003";//���ĳ��ҵ��ָ����ط�����ָ����˹��ӹܲ��������
	   public static final String  End="B004";//����ȫ������������˿��Խ���ȴ��ͳ�ʱ��˯��
	   public static final String  Quit="B005";//��Ҫ�ͻ����˳����˳�����Ҫ�ֹ�����
	   public static final String Abrupt="B006";//������쳣�жϻ��˳�
	   public static final String Return="B007";//ҵ��ָ��ִ�н��
		public static final String MsgCheckSum="10000000000000000000";//(20λ)Э��У����
		public static final String FileCheckSum="0000000000";//(10λ)Э��У����
		public static final int     FileChunkSize=1024;//�ļ��������ݿ��С
		public static final int   MsgHeaderLen=10;//Э��ͷ������
		
		//�ļ��ϴ������ָ����Կ��ǲ�ʹ��
		public static String PKG_UPLOAD_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																		"<COMMAND>"+
																			"<PACKET>"+
																				"<IP>@IP</IP>"+
																				"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																				"<NODE_ID>@NODE_ID</NODE_ID>"+
																				"<STEP_ID>@STEP_ID</STEP_ID>"+
																				"<MSGID>@MSGID</MSGID>"+
																				"<CRT_TIME>@CRT_TIME</CRT_TIME>"+
																				"<SEQ>@SEQ</SEQ>"+
																				"<SRC_PATH>@SRC_PATH</SRC_PATH>"+
																				"<TARGET_PATH>@TARGET_PATH</TARGET_PATH>"+
																			"</PACKET>"+
																		"</COMMAND>";
		//�ļ�����
		 public static String FILE_COPY_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																	"<COMMAND>"+
																		"<PACKET>"+
																			"<IP>@IP</IP>"+
																			"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																			"<NODE_ID>@NODE_ID</NODE_ID>"+
																			"<STEP_ID>@STEP_ID</STEP_ID>"+
																			"<MSGID>@MSGID</MSGID>"+
																			"<CRT_TIME>@CRT_TIME</CRT_TIME>"+
																			"<SEQ>@SEQ</SEQ>"+
																			"<FILE_ID>@FILE_ID</FILE_ID>"+	
																			"<SRC_PATH><![CDATA[@SRC_PATH]]></SRC_PATH>"+
																			"<TARGET_PATH><![CDATA[@TARGET_PATH]]></TARGET_PATH>"+
																		 "</PACKET>"+
																		 "</COMMAND>";
		//�汾�ļ�����(������Ϊ���ݵİ汾���ͷſռ�)
		 public static String FILE_DEL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																	"<COMMAND>"+
																		"<PACKET>"+
																			"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																			"<BACK_PATH>@BACK_PATH</BACK_PATH>"+
																		 "</PACKET>"+
																		 "</COMMAND>";
		 
     //�ű�ִ��
		public static String SCRIPT_RUN_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																		"<COMMAND>"+
																			"<PACKET>"+
																				"<IP>@IP</IP>"+
																				"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																				"<NODE_ID>@NODE_ID</NODE_ID>"+
																				"<STEP_ID>@STEP_ID</STEP_ID>"+
																				"<MSGID>@MSGID</MSGID>"+
																				"<CRT_TIME>@CRT_TIME</CRT_TIME>"+
																				"<SEQ>@SEQ</SEQ>"+
																				"<FILE_ID>@FILE_ID</FILE_ID>"+	
																				"<FILE_NAME><![CDATA[@FILE_NAME]]></FILE_NAME>"+
																				"<DB_NAME>@DB_NAME</DB_NAME>"+	
																				"<DB_USER>@DB_USER</DB_USER>"+
																				"<DB_PASSWD>@DB_PASSWD</DB_PASSWD>"+
																				"<RETURN_MODE>@RETURN_MODE</RETURN_MODE>"+
																			"</PACKET>"+
																		"</COMMAND>";
		//���ݿⱸ��
		public static String DB_BACKUP_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																			"<COMMAND>"+
																				"<PACKET>"+
																					"<IP>@IP</IP>"+
																					"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																					"<NODE_ID>@NODE_ID</NODE_ID>"+
																					"<STEP_ID>@STEP_ID</STEP_ID>"+
																					"<MSGID>@MSGID</MSGID>"+
																					"<CRT_TIME>@CRT_TIME</CRT_TIME>"+
																					"<SEQ>@SEQ</SEQ>"+
																					"<DB_NAME>@DB_NAME</DB_NAME>"+	
																					"<DB_USER>@DB_USER</DB_USER>"+
																					"<DB_PASSWD>@DB_PASSWD</DB_PASSWD>"+
																					"<BACK_DB>@BACK_DB</BACK_DB>"+
																			 "</PACKET>"+
																			"</COMMAND>";

		//�汾����
		public static String FILE_BACKUP_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<IP>@IP</IP>"+
																						"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																						"<NODE_ID>@NODE_ID</NODE_ID>"+
																						"<STEP_ID>@STEP_ID</STEP_ID>"+
																						"<MSGID>@MSGID</MSGID>"+
																						"<CRT_TIME>@CRT_TIME</CRT_TIME>"+
																						"<SEQ>@SEQ</SEQ>"+
																						"<FILTER><![CDATA[@FILTER]]></FILTER>"+
																						"<SRC_PATH><![CDATA[@SRC_PATH]]></SRC_PATH>"+
																						"<TARGET_PATH><![CDATA[@TARGET_PATH]]></TARGET_PATH>"+
																					"</PACKET>"+
																				"</COMMAND>";
		
		//������ͣ
		 public static String SERVICE_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<ROUTINE_NAME>@ROUTINE_NAME</ROUTINE_NAME>"+
																						"<MODE>@MODE</MODE>"+
																						"<PATH>@PATH</PATH>"+
																						"<RUN_PATH>@RUN_PATH</RUN_PATH>"+
																						"<CRT_TIME>@TIME</CRT_TIME>"+
																						"<SEQ>@SEQ</SEQ>"+
																						"<IP>@IP</IP>"+
																						"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																						"<NODE_ID>@NODE_ID</NODE_ID>"+
																						"<STEP_ID>@STEP_ID</STEP_ID>"+
																						"<MSGID>@MSGID</MSGID>"+
																					 "</PACKET>"+
																				 "</COMMAND>";

		//��ʼ����ָ��
		 public static String START_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																						"<NODE_ID>@NODE_ID</NODE_ID>"+
																						"<CMD_SUM>@CMD_SUM</CMD_SUM>"+
																						"<TIME>@TIME</TIME>"+
																					 "</PACKET>"+
																				 "</COMMAND>";
		//����ָ��
		 public static String HEART_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<TIME>@TIME</TIME>"+
																					 "</PACKET>"+
																				 "</COMMAND>";
		//�ϰ�ָ��
		 public static String BARRIER_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																						"<NODE_ID>@NODE_ID</NODE_ID>"+
																						"<CMD_SUM>@CMD_SUM</CMD_SUM>"+
																						"<TIME>@TIME</TIME>"+
																					 "</PACKET>"+
																				 "</COMMAND>"; 
		//�ظ�ָ��
		 public static String RESEND_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<MSGID>@MSGID</MSGID>"+
																						"<NODE_ID>@NODE_ID</NODE_ID>"+
																						"<MODE>@MODE</MODE>"+
																					 "</PACKET>"+
																				 "</COMMAND>"; 
		//���ָ��
		 public static String END_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																			"<COMMAND>"+
																				"<PACKET>"+
																					"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																					"<NODE_ID>@NODE_ID</NODE_ID>"+
																					"<TIME>@TIME</TIME>"+
																				 "</PACKET>"+
																			 "</COMMAND>"; 
			//�˳�ָ��
		 public static String QUIT_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																			"<COMMAND>"+
																				"<PACKET>"+
																					"<TIME>@TIME</TIME>"+
																				 "</PACKET>"+
																			 "</COMMAND>"; 
			//�ж�ָ��
		 public static String ABRUPT_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																			"<COMMAND>"+
																				"<PACKET>"+
																					"<VERSION_ID>@VERSION_ID</ VERSION_ID>"+
																					"<TIME>@TIME</TIME>"+
																				 "</PACKET>"+
																			 "</COMMAND>"; 
		 
		 public static String RETURN_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<MSGID>@MSGID</MSGID>"+
																						"<OUT_CODE>@OUT_CODE</OUT_CODE>"+
																						"<OUT_MSG><![CDATA[@OUT_MSG]]></OUT_MSG>"+
																						"<TIME>@TIME</TIME>"+
																						"<FILE_ID>@FILE_ID</FILE_ID>"+	
																						"<INSTALLEDMD5>@INSTALLEDMD5</INSTALLEDMD5>"+	
																					 "</PACKET>"+
																				 "</COMMAND>"; 
		 public enum ResendMode{Redo,Skip;}
}
