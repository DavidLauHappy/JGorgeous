package business.deploy.core;
/**
 * @authorDavid
 * @date 2016-7-29
 * @version 1.0
 * 类说明
 */

public class CmdType {
   public static final String KeepAlive="0000";//心跳命令
   public static final String FileCopy="0001";//文件(目录)拷贝
   public static final String DeleteFile="0002";//文件(目录)删除
   public static final String ScriptExecute="0003";//文件执行
   public static final String SqlserverBackup="0004";//数据库备份
   public static final String Idel="0005";//服务端退出后，agent后台线程睡眠
   //不同类型命令对应的属性名称
   
}
