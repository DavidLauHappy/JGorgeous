package common.core;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import resource.Context;
import resource.Logger;
import utils.DateUtil;
import utils.FileUtils;
import utils.StringUtil;
/**
 * @author DavidLau
 * Windows 到Windows机器测穿越
 */
public class WinShell {
	
		 public static WinShell getShell(String ip){
			 if(Shells.containsKey(ip)){
				   WinShell shell=Shells.get(ip);
				    shell.setExtraPkgDirs();
				 return shell;
			 }
			 WinShell shell=null;
				try {
					shell = new WinShell(ip);
				} catch (Exception e) {
					
				}
				if(shell!=null){
					Shells.put(ip, shell);
				}
		    return shell;
		 }
		 

		 public boolean executeCommand(String cmd,boolean needRetrun){
			  boolean result=false;
			  this.protocalStatus=MsgStatus.Init;
			  this.response="";
			  try{
				  this.protocalStatus=MsgStatus.Sending;
				  this.sentCommandBytes(this.getSendMsgBytes(cmd));
				  //发送指令成功
				  this.protocalStatus=MsgStatus.Sended;
				  if(MsgStatus.Sended.equals( this.protocalStatus)&&!needRetrun){
					  result=true;
					  return result;
				  }else{
					  long timeStart=System.currentTimeMillis();
					  while(MsgStatus.Sended.equals( this.protocalStatus)&&(this.session.getInputStream())!=null){
							  if(System.currentTimeMillis()-timeStart>TimeRecvOut*1000){
								  result=false;
								  this.protocalStatus=MsgStatus.TimeOut;
				    			  break;
							  }else{
								    if(this.session.getInputStream().available()>0){
								    	 this.getRecvMsgBytes(); 
								    }
							  }
					  }
				  }
			  }catch(Exception e){
				   this.response=e.toString();
				   e.printStackTrace();
			  }finally{
				  if(this.protocalStatus.equals(MsgStatus.Received))
					  result=true;
			  }
			  return result;
		  }
		  
		 public List<String> firstDirs=new ArrayList<String>();
		 public boolean sendDir(String dir,String remotePath){
			boolean result=true;
			 try{
					File file=new File(dir);
					if(file.exists()){
						if(file.isFile()){
							 this.sendFile(file.getAbsolutePath(), remotePath);
						}else {
							File[] fileList=file.listFiles();
							if(firstHiarchy){
				    			for(File curFile:fileList){
				    				firstDirs.add(curFile.getName());
				    			}
				    			firstHiarchy=false;
				    		}
							File[] files=file.listFiles();
							if(files!=null&&files.length>0){
								for(File curfile:files){
									if(firstDirs.contains(curfile.getName())){
										 if(this.extraPkgDirs.contains(curfile.getName())){
											 this.sendDir(curfile.getAbsolutePath(), remotePath+File.separator+file.getName());
										 }
									}else{
										this.sendDir(curfile.getAbsolutePath(), remotePath+File.separator+file.getName());
									}
								}
							}
						}
					}
				}catch(Exception e){
					result=false;
			    }
			 
			return result;
		 }
		 
		 public boolean sendFile(String filePath,String remotePath){
			  boolean result=true;
			  this.protocalStatus=MsgStatus.Init;
			  this.response="";
			 FileInputStream fis=null;
			   try{
				     File file=new File(filePath);
				     String md5=FileUtils.getMd5ByFile(file);
				     this.protocalStatus=MsgStatus.Sending;
					 this.sentCommandBytes(this.getSendFileBytes());
				     fis =new FileInputStream(file);
				     dos.writeUTF(file.getName());//文件名
		             dos.flush();
		             dos.writeUTF(remotePath);//文件在接收端的相对路径
		             dos.flush();
		             dos.writeUTF(md5);//文件在发送端计算得到的md5码
		             dos.flush();
		             dos.writeLong(file.lastModified());//文件最后修改时间
		             dos.flush();
		             dos.writeLong(file.length());//文件数据大小
		             dos.flush();
		             byte[] sendBytes =new byte[Protocol.FileChunkSize];
	                int length =0;
	                long timeStartSend=System.currentTimeMillis();
	                Logger.getInstance().serviceLog(Context.ServiceWinShell,"开始发送文件到["+ip+"]VService。文件名："+filePath);
	                while((length = fis.read(sendBytes,0, sendBytes.length)) >0&&
	                		(System.currentTimeMillis()-timeStartSend<FileSendTimeOut*1000)){
	                    dos.write(sendBytes,0, length);
	                    dos.flush();
	                    if(System.currentTimeMillis()-timeStartSend>FileSendTimeOut*1000){
	                    	 this.protocalStatus=MsgStatus.TimeOut;
	                    	break;
	                    }
	                }
	                if(fis!=null)
	           			fis.close();
		            //文件发送完成，等待接收方的响应
	                if(MsgStatus.Sending.equals(this.protocalStatus)){
	                	 Logger.getInstance().serviceLog(Context.ServiceWinShell,"文件发送到["+ip+"]VService成功。文件名："+filePath);
	                	this.protocalStatus=MsgStatus.Sended;
	                }
				  long timeStart=System.currentTimeMillis();
				  while(MsgStatus.Sended.equals( this.protocalStatus)&&(this.session.getInputStream())!=null){
						  if(System.currentTimeMillis()-timeStart>FileSendTimeOut*1000){
							  result=false;
							  this.protocalStatus=MsgStatus.TimeOut;
			    			  break;
						  }else{
							    if(this.session.getInputStream().available()>0){
							    	 this.getRecvMsgBytes(); 
							    }
						  }
				  }
			   }catch(Exception e){
				   this.response=e.toString();
			   }finally{
				   try{
				   if(fis!=null)
	           			fis.close();
				   }catch(Exception e){
					   e.printStackTrace();
				   }
			   }
			   return result;
		 }
		 
		 public String getResponse(){
			 String result=this.response;
			 if(StringUtil.isNullOrEmpty(result)){
				  switch(this.protocalStatus){
				  		case Init:	result="会话对象未初始化";break;
				  		case Sending:	result="指令发送过程中发生错误";break;
				  		case Sended:	result="会话过程中流对象异常";break;
				  		case StartReceive:	result="指令有响应，处理响应异常";break;
				  		case Receiving:	result="通过指令头部校验，指令正文处理异常";break;
				  		case SkipHeader:	result="指令头部长度校验不通过";break;
				  		case SkipBody:	result="指令正文长度校验不通过";break;
				  		case CheckError:	result="指令内容交易不通过";break;
				  		case TimeOut:	result="响应指令超时";break;
				  		 default:result="未知错误";break;
				  }
			 }
			 return result;
		 }
		 
		 
		 public static void release(){
			 for(String ip:Shells.keySet()){
				  Shells.get(ip).exit();
			 }
		 }
		 public void exit(){
			 try{
				 this.response="";
				 //发一个退出指令通知服务释放资源
				 String msg=Protocol.QUIT_CONTROL_CMD;
				 msg=msg.replace("@TIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				 msg=Protocol.Quit+"|"+msg;
				 this.executeCommand(msg, false);
				 Thread.sleep(1*1000);
				 if(this.channel!=null)
					 this.channel.close();
				 if(this.session!=null)
					 this.session.close();
				 this.Shells.remove(this.ip);
				 Thread.sleep(1*1000);
			 }catch(Exception e){
				 e.printStackTrace();
			 }
		 }
		 
		 public void setExtraPkgDirs(){
		    	this.extraPkgDirs.clear();
		    	this.firstDirs.clear();
		    	firstHiarchy=true;
		    }
		    
		    public void addComDir(String dir){
		    	this.extraPkgDirs.add(dir);
		    }
		 
		    
		 private void sentCommandBytes(byte[] bytes){   
			 String msg=new String(bytes);
			 try{
				 this.session.getOutputStream().write(bytes);
				 this.session.getOutputStream().flush();
				 Logger.getInstance().serviceLog(Context.ServiceWinShell,"成功发送消息到["+ip+"]VService。消息内容："+msg);
			 }catch(Exception e){
				 Logger.getInstance().serviceLog(Context.ServiceWinShell,"发送消息到["+ip+"]VService。消息内容："+msg+"异常："+e.toString());
			 }
		 }
		 private WinShell(String ip) throws Exception {
				try{
					this.ip=ip;
					extraPkgDirs=new ArrayList<String>();
					InetAddress localhost = InetAddress.getLocalHost(); 
					InetAddress serverAddress=InetAddress.getByName(this.ip);
					this.session=new Socket(serverAddress,Context.ServerPort,localhost, 0);
					this.session.setSoTimeout(TimeOut*1000);
					this.channel=new Socket(serverAddress,Context.ServerPort,localhost, 0);
					this.channel.setSoTimeout(TimeOut*1000);
					 dos=new DataOutputStream(this.channel.getOutputStream());
					 Logger.getInstance().serviceLog(Context.ServiceWinShell,"成功连接到["+ip+"]VService");
				} catch(Exception e){
					Logger.getInstance().serviceLog(Context.ServiceWinShell,"连接到["+ip+"]VService的发生异常："+e.toString());
					throw e;
				}
			}
		 
		 private byte[] getSendMsgBytes(String cmd){
				   //Logger.getInstance().log("开始发送报文["+text+"]");
				   byte[] xmlsequence = cmd.getBytes();
					//组装IP包头 头部需要包含内容的长度，并且格式化成标准的东西
					int len = xmlsequence.length+Protocol.MsgCheckSum.length();
					DecimalFormat format = new DecimalFormat("0000");
					String packLen = format.format(len);
					String packetHeader=StringUtil.leftpad(packLen, Protocol.MsgHeaderLen, "0");
					byte[] msgHead = packetHeader.getBytes();
					//组包（内容）
					String packetChkSum = Protocol.MsgCheckSum;//20位校验码，是通讯包的固定组成部分
					byte[] seq = packetChkSum.getBytes();
					byte[] msgBody = new byte[seq.length + xmlsequence.length];
					System.arraycopy(seq, 0, msgBody, 0, seq.length);
					System.arraycopy(xmlsequence, 0, msgBody, seq.length,xmlsequence.length);
					//最终包
					byte[] msgSequence = new byte[msgHead.length + msgBody.length];
					System.arraycopy(msgHead, 0, msgSequence, 0, msgHead.length);
					System.arraycopy(msgBody, 0, msgSequence, msgHead.length,msgBody.length);
					return msgSequence;
		  }
		 
		 private byte[] getSendFileBytes(){
				int len = Protocol.FileCheckSum.length();
				DecimalFormat format = new DecimalFormat("0000");
				String packLen = format.format(len);
				String packetHeader=StringUtil.leftpad(packLen, Protocol.MsgHeaderLen, "0");
				byte[] msgHead = packetHeader.getBytes();
				return msgHead;
		 }
		 
		 private void getRecvMsgBytes() throws Exception{
	    	 this.protocalStatus=MsgStatus.StartReceive;
		    	byte[] header=new byte[Protocol.MsgHeaderLen];
		    	byte[] body;
		    	int   bodyLen=0,realBodyLen=0,realHeaderLen=0;
		    	 String content,checkCode="";
		    	 realHeaderLen=this.session.getInputStream().read(header);
		    	 if(realHeaderLen==Protocol.MsgHeaderLen){
		    		 this.protocalStatus=MsgStatus.Receiving;
		    		 bodyLen=Integer.parseInt(new String(header));//按照包头中存储的长度读取对应的内容
	    			  body=new byte[bodyLen];
	    			  realBodyLen=this.session.getInputStream().read(body);
	    			  if(bodyLen==realBodyLen){
	    				  content=new String(body);
			    		  checkCode=content.substring(0, Protocol.MsgCheckSum.length());
			    		  if(Protocol.MsgCheckSum.equals(checkCode)){
			    			  this.response=content.substring(Protocol.MsgCheckSum.length()); 
			    			  this.protocalStatus=MsgStatus.Received;
			    			  Logger.getInstance().serviceLog(Context.ServiceWinShell,"成功接收到到["+ip+"]VService消息："+this.response);
			    		  }else{
			    			  this.protocalStatus=MsgStatus.CheckError;
			    		  }
	    			  }else{
	    				  this.protocalStatus=MsgStatus.SkipBody;
	    			  }
		    	 }else{
		    		 this.protocalStatus=MsgStatus.SkipHeader;
		    	 }
		 }
		 
		 /*public static void main(String[] args){
			 String testIp="172.24.143.30";
			 WinShell shell=WinShell.getShell(testIp);
			 if(shell!=null){
				 shell.extraPkgDirs.add("KCBP");
				 shell.extraPkgDirs.add("DOC");
				 shell.extraPkgDirs.add("DB");
				 String dir="C:\\CTS_v201708021017";
				 shell.sendDir(dir, "$WORKDIR");
				 shell.exit();
			 }
			 String textFile1="C:\\share\\data.db";
			 shell.sendFile(textFile1, "C:\\share\\Test");
			 String textFile2="C:\\share\\Client.jar";
			 shell.sendFile(textFile2, "C:\\share\\Test");
			 shell.exit();

			 shell=WinShell.getShell(testIp);
			 String textFile3="C:\\share\\MEM_v201709120958_play.xml";
			 shell.sendFile(textFile3, "C:\\share\\Test");
			 String msg=Protocol.FILE_COPY_CMD;
			 msg=msg.replace("@VERSION_ID", "MEM_v201709120958");
			 msg=msg.replace("@SRC_PATH", "C:\\share\\Test\\data.db");
			 msg=msg.replace("@TARGET_PATH", "C:\\share\\TestB");
			 msg=Protocol.FileCopy+"|"+msg;
			 shell.executeCommand(msg, true);
			 shell.exit();
		 }*/
		 
		    private String ip;
			private Socket session;
			private Socket channel;
			private DataOutputStream dos=null;
			private int port=0;
			private String response="";
			private  MsgStatus protocalStatus;
			private enum MsgStatus{Init,
													Sending,
												   Sended,
												   StartReceive,
												   Receiving,
												   Received,
												   SkipHeader,
												   SkipBody,
												   CheckError,
												   TimeOut;}
			private static int TimeOut=10;//连接建立超时时间(s)
			private static int TimeRecvOut=300;//发送指令后接收结果的等待时间(s)
			private static int FileSendTimeOut=30;//单个文件的发送时间
			private static ConcurrentMap<String,WinShell> Shells=new ConcurrentHashMap<String, WinShell>();
			private List<String> extraPkgDirs;//记录版本保重整体上传时过滤的组件版本文件，节约开销
			private boolean firstHiarchy=true;
			public static final String DEFAULT_PATH="$WORKDIR";
}
