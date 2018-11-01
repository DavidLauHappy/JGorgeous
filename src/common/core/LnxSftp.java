package common.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import resource.Context;
import resource.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class LnxSftp {
	private Session session=null;
	private boolean loginResult=false;
	private boolean firstHiarchy=true;
	public String ip;
	public String user;
	private String passwd;
	private List<String> extraPkgDirs;//记录版本保重整体上传时过滤的组件版本文件，节约开销
	private String outMsg;
	
	public LnxSftp(String ip,String user,String passwd){
		this.ip=ip;
		this.user=user;
		this.passwd=passwd;
		this.login(ip, user, passwd, 0);
		extraPkgDirs=new ArrayList<String>();
	}
	public LnxSftp(String ip,String user,String passwd,int port){
		this.ip=ip;
		this.user=user;
		this.passwd=passwd;
		this.login(ip, user, passwd, port);
		extraPkgDirs=new ArrayList<String>();
	}

    public void setExtraPkgDirs(){
    	this.extraPkgDirs.clear();
    	this.firstDirs.clear();
    	firstHiarchy=true;
    }
    
    public void addComDir(String dir){
    	this.extraPkgDirs.add(dir);
    }
   
    public String getOutMsg(){
    	return this.outMsg;
    }
    
    public  boolean upLoadFile(String srcPath,String dstPath){
    	if(!this.loginResult)
    		return false;
    	Channel channel=null;
    	try{
    		firstHiarchy=true;
    		channel=(Channel)session.openChannel("sftp");
    		channel.connect();
    		ChannelSftp sftp = (ChannelSftp) channel;
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"sftp文件推送服务：服务对象["+this.ip+":"+this.user+":"+dstPath+"]成功启动……");
    		//为了支持中文文件名，需要强制修改服务端文件编码方式
    		Class clazz=ChannelSftp.class;
    		Field field=clazz.getDeclaredField("server_version");
    		field.setAccessible(true);  
    		field.set(sftp, 2);
    		sftp.setFilenameEncoding("GBK");
    		try{
    			sftp.cd(dstPath);
    		}catch(SftpException e){
    			sftp.mkdir(dstPath);
    			sftp.cd(dstPath);
    		}
    		File file=new File(srcPath);
    		this.copyFile(sftp, file,sftp.pwd());
    	}catch(Exception e){
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.upLoadFile 文件拷贝"+srcPath+"->"+dstPath+"发生异常："+e.toString());
    		Logger.getInstance().error("LnxSftp.upLoadFile 文件拷贝"+srcPath+"->"+dstPath+"发生异常："+e.toString());
    		outMsg=e.toString();
    		return false;
    	}
    	finally{
    		session.disconnect();
    		channel.disconnect();
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"sftp文件推送服务：服务对象["+this.ip+":"+this.user+":"+srcPath+"->"+dstPath+"]服务完成并退出……");
    	}
    	return true;
    }
    
    public static boolean testSsh(String ip,String user,String passwd,int port,String dstPath){
    	Session session=null;
    	Channel channel=null;
    	try{
    		JSch jsch=new JSch();
	    	if(port<=0){
	    		session=jsch.getSession(user, ip);
	    	}else{
	    		session=jsch.getSession(user, ip, port);
	    	}
	    	session.setPassword(passwd);
	    	session.setConfig("StrictHostKeyChecking", "no");
	    	session.connect();
	    	channel=(Channel)session.openChannel("sftp");
    		channel.connect();
    		ChannelSftp sftp = (ChannelSftp) channel;
    		//为了支持中文文件名，需要强制修改服务端文件编码方式
    		Class clazz=ChannelSftp.class;
    		Field field=clazz.getDeclaredField("server_version");
    		field.setAccessible(true);  
    		field.set(sftp, 2);
    		sftp.setFilenameEncoding("GBK");
    		sftp.cd(dstPath);
    		return true;
    	}catch(Exception e){
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.testSsh发生异常："+e.toString());
    		Logger.getInstance().error("LnxSftp.testSsh发生异常："+e.toString());
    	}finally{
    		try{
	    		session.disconnect();
	    		channel.disconnect();
    		}catch(Exception e){
    			
    		}
    	}
    	return false;
    }
    
    private  boolean login(String ip,String user,String passwd,int port){
    	try{
	    	JSch jsch=new JSch();
	    	if(port<=0){
	    		session=jsch.getSession(user, ip);
	    	}else{
	    		session=jsch.getSession(user, ip, port);
	    	}
	    	session.setPassword(passwd);
	    	session.setConfig("StrictHostKeyChecking", "no");
	    	session.connect();
	    	this.loginResult=true;
	    	return false;
    	}catch(Exception e){
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"SFtp 登录连接到"+ip+":"+port+"发生异常："+e.toString());
    		Logger.getInstance().error("SFtp 登录连接到"+ip+":"+port+"发生异常："+e.toString());
    	}
    	return true;
    }
    
    public List<String> firstDirs=new ArrayList<String>();
    private void copyFile(ChannelSftp sftp, File file,String pwd) throws Exception{
    	if(file.isDirectory()){
    		File[] fileList=file.listFiles();
    		if(firstHiarchy){
    			for(File curfile:fileList){
    				firstDirs.add(curfile.getName());
    			}
    			firstHiarchy=false;
    		}
    		String  dirName=new String(file.getName().getBytes("GBK"),"iso-8859-1");
    		try{
    			sftp.cd(pwd);
    			sftp.mkdir(dirName);
    		}catch(Exception e){
    			Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFile远程创建目录["+file.getName()+"]异常:"+e.toString());
    			Logger.getInstance().error("LnxSftp.copyFile远程创建目录["+file.getName()+"]异常:"+e.toString());
    		}
    		pwd=pwd+"/"+dirName;//这里的这个分隔符与操作系统有关系 只关心Linux
    		try{
    			sftp.cd(dirName);
    		}
    		catch(Exception e){
    			Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFile远程切换目录["+file.getName()+"]异常:"+e.toString());
    			Logger.getInstance().error("LnxSftp.copyFile远程切换目录["+file.getName()+"]异常:"+e.toString());
    			throw e;
    		}
    		if(fileList!=null&&fileList.length>0){
				for(File curfile:fileList){
					if(firstDirs.contains(curfile.getName())){//考虑大版本很多组件不用整体上传文件
						   if(this.extraPkgDirs.contains(curfile.getName())){
								this.copyFile(sftp, curfile, pwd);
							}
					}
					else{
						this.copyFile(sftp, curfile, pwd);
					}
				}
    		}
    	}else{
    		try{
    			sftp.cd(pwd);
    		}
    		catch(SftpException e){
    			Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFile远程切换目录["+pwd+"]异常:"+e.toString());
    			Logger.getInstance().error("LnxSftp.copyFile远程切换目录["+pwd+"]异常:"+e.toString());
    			throw e;
    		}
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"Sftp开始给机器["+this.ip+"]传输文件["+file.getPath()+"]");
    		InputStream instream=null;
    		OutputStream outstream = null;
    		String fileName="";
    		int permissions=700;
    		if(file.canRead()){
    			permissions+=44;
    		}
    		if(file.canExecute()){
    			permissions+=11;
    		}
    		int time=0;
    		 try {
    			  long lastime=file.lastModified();
                  time=(int)(lastime/1000);
    			 fileName=file.getName();//new String(file.getName().getBytes("GBK"),"iso-8859-1");
                 outstream = sftp.put(fileName);
                 instream = new FileInputStream(file);
                 byte b[] = new byte[1024];
                 int n;
                 try {
                     while ((n = instream.read(b)) != -1) {
                         outstream.write(b, 0, n);
                     }
                 } catch (IOException e) {
                	 Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFile读取文件["+file.getName()+"]异常:"+e.toString());
                	Logger.getInstance().error("LnxSftp.copyFile读取文件["+file.getName()+"]异常:"+e.toString());
         			throw e;
                 }
    		 }catch(SftpException e){
    			 Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFile远程创建文件["+file.getName()+"]异常:"+e.toString());
    			 Logger.getInstance().error("LnxSftp.copyFile远程创建文件["+file.getName()+"]异常:"+e.toString());
      			throw e;
     		}catch(IOException e){
     			Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFile远程写入文件["+file.getName()+"]异常:"+e.toString());
     			Logger.getInstance().error("LnxSftp.copyFile远程写入文件["+file.getName()+"]异常:"+e.toString());
     			throw e;
     		}
    		 finally {
	                 try {
	                     outstream.flush();
	                     outstream.close();
	                     instream.close();
	                     sftp.setMtime(fileName,time );//关闭文件流的时候恢复文件时间才能生效
	                     sftp.chmod(Integer.parseInt(permissions+"", 8), fileName);//保持文件的权限Linux权限是8进制
	                 } catch (Exception e){
	                	 Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFile远程写入文件["+file.getName()+"]回收资源时异常:"+e.toString());
	                	  Logger.getInstance().error("LnxSftp.copyFile远程写入文件["+file.getName()+"]回收资源时异常:"+e.toString());
	          			throw e;
	                 }
    		 }
    	}
    }
    
}
