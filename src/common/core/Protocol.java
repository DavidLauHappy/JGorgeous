package common.core;

public class Protocol {
	  public static int	DefaultPort=10088;
	  public static final String UploadPKg="A100";//上传版本目录
	   public static final String FileCopy="A001";//文件(目录)拷贝
	   public static final String DeleteFile="A002";//文件(目录)删除
	   public static final String ScriptExecute="A003";//脚本文件执行
	   public static final String SqlserverBackup="A004";//数据库备份
	   public static final String ServiceControl="A005";//服务启动/停止
	   public static final String Start="B000";//开始指令,协议的第一条指令
	   public static final String KeepAlive="B001";//心跳命令
	   public static final String  Barrier="B002";//障碍指令，每次版本部署有一个该种指令，障碍指令告诉客户端所有业务指令下达完成了，可以开始干活了
	   public static final String  Resend="B003";//针对某个业务指令的重发控制指令，由人工接管操作后产生
	   public static final String  End="B004";//任务全部结束，服务端可以进入等待和长时间睡眠
	   public static final String  Quit="B005";//需要客户端退出，退出后需要手工启动
	   public static final String Abrupt="B006";//服务端异常中断或退出
	   public static final String Return="B007";//业务指令执行结果
		public static final String MsgCheckSum="10000000000000000000";//(20位)协议校验码
		public static final String FileCheckSum="0000000000";//(10位)协议校验码
		public static final int     FileChunkSize=1024;//文件传输数据库大小
		public static final int   MsgHeaderLen=10;//协议头部长度
		
		//文件上传：这个指令可以考虑不使用
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
		//文件拷贝
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
		//版本文件清理(清理以为备份的版本，释放空间)
		 public static String FILE_DEL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																	"<COMMAND>"+
																		"<PACKET>"+
																			"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																			"<BACK_PATH>@BACK_PATH</BACK_PATH>"+
																		 "</PACKET>"+
																		 "</COMMAND>";
		 
     //脚本执行
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
		//数据库备份
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

		//版本备份
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
		
		//服务启停
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

		//开始启动指令
		 public static String START_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																						"<NODE_ID>@NODE_ID</NODE_ID>"+
																						"<CMD_SUM>@CMD_SUM</CMD_SUM>"+
																						"<TIME>@TIME</TIME>"+
																					 "</PACKET>"+
																				 "</COMMAND>";
		//心跳指令
		 public static String HEART_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<TIME>@TIME</TIME>"+
																					 "</PACKET>"+
																				 "</COMMAND>";
		//障碍指令
		 public static String BARRIER_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																						"<NODE_ID>@NODE_ID</NODE_ID>"+
																						"<CMD_SUM>@CMD_SUM</CMD_SUM>"+
																						"<TIME>@TIME</TIME>"+
																					 "</PACKET>"+
																				 "</COMMAND>"; 
		//重复指令
		 public static String RESEND_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																				"<COMMAND>"+
																					"<PACKET>"+
																						"<MSGID>@MSGID</MSGID>"+
																						"<NODE_ID>@NODE_ID</NODE_ID>"+
																						"<MODE>@MODE</MODE>"+
																					 "</PACKET>"+
																				 "</COMMAND>"; 
		//完结指令
		 public static String END_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																			"<COMMAND>"+
																				"<PACKET>"+
																					"<VERSION_ID>@VERSION_ID</VERSION_ID>"+
																					"<NODE_ID>@NODE_ID</NODE_ID>"+
																					"<TIME>@TIME</TIME>"+
																				 "</PACKET>"+
																			 "</COMMAND>"; 
			//退出指令
		 public static String QUIT_CONTROL_CMD="<?xml version=\"1.0\" encoding=\"gb2312\" ?>" +
																			"<COMMAND>"+
																				"<PACKET>"+
																					"<TIME>@TIME</TIME>"+
																				 "</PACKET>"+
																			 "</COMMAND>"; 
			//中断指令
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
