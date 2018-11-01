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
	private List<String> extraPkgDirs;//��¼�汾���������ϴ�ʱ���˵�����汾�ļ�����Լ����
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
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"sftp�ļ����ͷ��񣺷������["+this.ip+":"+this.user+":"+dstPath+"]�ɹ���������");
    		//Ϊ��֧�������ļ�������Ҫǿ���޸ķ�����ļ����뷽ʽ
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
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.upLoadFile �ļ�����"+srcPath+"->"+dstPath+"�����쳣��"+e.toString());
    		Logger.getInstance().error("LnxSftp.upLoadFile �ļ�����"+srcPath+"->"+dstPath+"�����쳣��"+e.toString());
    		outMsg=e.toString();
    		return false;
    	}
    	finally{
    		session.disconnect();
    		channel.disconnect();
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"sftp�ļ����ͷ��񣺷������["+this.ip+":"+this.user+":"+srcPath+"->"+dstPath+"]������ɲ��˳�����");
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
    		//Ϊ��֧�������ļ�������Ҫǿ���޸ķ�����ļ����뷽ʽ
    		Class clazz=ChannelSftp.class;
    		Field field=clazz.getDeclaredField("server_version");
    		field.setAccessible(true);  
    		field.set(sftp, 2);
    		sftp.setFilenameEncoding("GBK");
    		sftp.cd(dstPath);
    		return true;
    	}catch(Exception e){
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.testSsh�����쳣��"+e.toString());
    		Logger.getInstance().error("LnxSftp.testSsh�����쳣��"+e.toString());
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
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"SFtp ��¼���ӵ�"+ip+":"+port+"�����쳣��"+e.toString());
    		Logger.getInstance().error("SFtp ��¼���ӵ�"+ip+":"+port+"�����쳣��"+e.toString());
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
    			Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFileԶ�̴���Ŀ¼["+file.getName()+"]�쳣:"+e.toString());
    			Logger.getInstance().error("LnxSftp.copyFileԶ�̴���Ŀ¼["+file.getName()+"]�쳣:"+e.toString());
    		}
    		pwd=pwd+"/"+dirName;//���������ָ��������ϵͳ�й�ϵ ֻ����Linux
    		try{
    			sftp.cd(dirName);
    		}
    		catch(Exception e){
    			Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFileԶ���л�Ŀ¼["+file.getName()+"]�쳣:"+e.toString());
    			Logger.getInstance().error("LnxSftp.copyFileԶ���л�Ŀ¼["+file.getName()+"]�쳣:"+e.toString());
    			throw e;
    		}
    		if(fileList!=null&&fileList.length>0){
				for(File curfile:fileList){
					if(firstDirs.contains(curfile.getName())){//���Ǵ�汾�ܶ�������������ϴ��ļ�
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
    			Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFileԶ���л�Ŀ¼["+pwd+"]�쳣:"+e.toString());
    			Logger.getInstance().error("LnxSftp.copyFileԶ���л�Ŀ¼["+pwd+"]�쳣:"+e.toString());
    			throw e;
    		}
    		Logger.getInstance().serviceLog(Context.ServiceSftp,"Sftp��ʼ������["+this.ip+"]�����ļ�["+file.getPath()+"]");
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
                	 Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFile��ȡ�ļ�["+file.getName()+"]�쳣:"+e.toString());
                	Logger.getInstance().error("LnxSftp.copyFile��ȡ�ļ�["+file.getName()+"]�쳣:"+e.toString());
         			throw e;
                 }
    		 }catch(SftpException e){
    			 Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFileԶ�̴����ļ�["+file.getName()+"]�쳣:"+e.toString());
    			 Logger.getInstance().error("LnxSftp.copyFileԶ�̴����ļ�["+file.getName()+"]�쳣:"+e.toString());
      			throw e;
     		}catch(IOException e){
     			Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFileԶ��д���ļ�["+file.getName()+"]�쳣:"+e.toString());
     			Logger.getInstance().error("LnxSftp.copyFileԶ��д���ļ�["+file.getName()+"]�쳣:"+e.toString());
     			throw e;
     		}
    		 finally {
	                 try {
	                     outstream.flush();
	                     outstream.close();
	                     instream.close();
	                     sftp.setMtime(fileName,time );//�ر��ļ�����ʱ��ָ��ļ�ʱ�������Ч
	                     sftp.chmod(Integer.parseInt(permissions+"", 8), fileName);//�����ļ���Ȩ��LinuxȨ����8����
	                 } catch (Exception e){
	                	 Logger.getInstance().serviceLog(Context.ServiceSftp,"LnxSftp.copyFileԶ��д���ļ�["+file.getName()+"]������Դʱ�쳣:"+e.toString());
	                	  Logger.getInstance().error("LnxSftp.copyFileԶ��д���ļ�["+file.getName()+"]������Դʱ�쳣:"+e.toString());
	          			throw e;
	                 }
    		 }
    	}
    }
    
}
