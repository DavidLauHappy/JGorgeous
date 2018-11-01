package resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


import resource.Context;
import resource.Logger;
import resource.SecurityCenter;
import utils.DateUtil;
import utils.FileUtils;
import utils.StringUtil;

/**
 * @author David
 *
 */
public class FtpFileService {
	
	private static FtpFileService unique_instance;
	public static FtpFileService getService(){
		if(unique_instance==null)
			unique_instance=new FtpFileService();
		return unique_instance;
	}
	
	private FtpFileService(){}
	public  FTPClient ftpUploader = new FTPClient(); 
	
	public boolean upLoad(String source,String target){
		int port=Integer.parseInt(Context.FTPServerPort);
		String realPasswd=SecurityCenter.getInstance().decrypt(Context.ServerFTPasswd, Context.EncryptKey);
		boolean result=this.connect(Context.FTPServerIp,port,Context.ServerFTPUser,realPasswd,target);
		if(result){
			File srcFile=new File(source);
			if(srcFile.exists()&&srcFile.isFile()){
			    return	this.uploadFile(srcFile,target);
			}
		}
		return false;
	}
	
	public boolean dowload(String remoteFileName,String remotePath,String localDir,String lastMdfTime){
		int port=Integer.parseInt(Context.FTPServerPort);
		String realPasswd=SecurityCenter.getInstance().decrypt(Context.ServerFTPasswd, Context.EncryptKey);
		boolean result=this.connect(Context.FTPServerIp,port,Context.ServerFTPUser,realPasswd,remotePath);
		if(result){
			try{
			 String unicodeInputFilename=new String(remoteFileName.getBytes("GBK"),"iso-8859-1");
			 String localPath=FileUtils.formatPath(localDir)+File.separator+remoteFileName;
			 return this.downloadFile(localPath, unicodeInputFilename,lastMdfTime);
			}
			catch(Exception e){
				Logger.getInstance().error("FtpFileService.dowload()Զ���ļ���ת�������쳣��"+e.toString());
				return false;
			}
		}
		return false;
	}
	

	
	private  boolean downloadFile(String localPath,String fileName,String lastModifyTime){
		boolean result=false;
		OutputStream outputStream = null; 
		try{
			 File locaFile= new File(localPath); 
			 outputStream = new FileOutputStream(localPath); 
			 result=ftpUploader.retrieveFile(fileName, outputStream);
			 outputStream.flush(); 
			 outputStream.close(); 
			 if(lastModifyTime!=null){
				 long time=DateUtil.getTimeLongFromString(lastModifyTime);
				 locaFile.setLastModified(time);
			 }
		}catch(Exception e){
			result=false;
			Logger.getInstance().error("FtpFileService.downloadFile()��ȡԶ���ļ���"+fileName+"���쳣��"+e.toString());
		}
		finally{
			try { 
				 if (outputStream != null){ 
					 outputStream.close(); 
				 	}
				} 
			catch (IOException exp) { 
				result=false;
				Logger.getInstance().error("FtpFileService.downloadFile()��ȡԶ���ļ���"+fileName+"����ļ����쳣"+exp.toString()); 
			 } 
		}
		return result;
	}
	//�ж�Ŀ¼�Ƿ���ڣ���������ڣ��ʹ���Ŀ¼��������ڣ���Ҫ�ѵ�ǰ����Ŀ¼�л�����Ŀ¼
	private   boolean makeDirs(String path){
		boolean result=false;
		try{
				String[] dirs=path.split("\\\\");
				String currentDir="";
				List<String> Dirs=new ArrayList<String>();
				for(int w=0;w<dirs.length;w++){
					if(StringUtil.isNullOrEmpty(currentDir)){
						currentDir=dirs[w];
					}else{
						currentDir=currentDir+File.separator+dirs[w];
					}
					Dirs.add(currentDir);
				}
				for(String col:Dirs){
					ftpUploader.makeDirectory(col);//System.out.println(col);
				}
				result=ftpUploader.changeWorkingDirectory(path);
			return result;
		}catch(Exception e){
			 Logger.getInstance().error("FtpUpload.makeDirs()�����л�Ŀ¼��"+path+"���쳣��"+e.toString());
		}
		return false;
	}
	
/* public static void main(String[] args){   	
		String path="06252\\2016-10-12\\66\\1";
		String localPath="C:\\temp";
		String remoteFileName="AlterTable(run).sql";
		boolean result=FtpFileService.getService().connect("172.24.178.241", 21, "ftpuser", "Gorgeous1125",path);   
		if(result){
			 String localpath=FileUtils.formatPath(localPath)+File.separator+remoteFileName;
			boolean ret= FtpFileService.getService().downloadFile(localpath, remoteFileName);
		}
	}*/


	
	
	
	public boolean connect(String hostname,int port,String username,String password,String remotePath){   
		boolean result=false;	
		try{
				ftpUploader.connect(hostname, port);     
		        if(FTPReply.isPositiveCompletion(ftpUploader.getReplyCode())){   
		            if(ftpUploader.login(username, password)){   
		            	ftpUploader.enterLocalPassiveMode();   
		            	ftpUploader.setFileType(FTPClient.BINARY_FILE_TYPE);   
		            	//ftpUploader.setControlEncoding("GBK");  
		            	result=makeDirs(remotePath);//����Ŀ¼���л�Ŀ¼
		            return result;
		            }   
		        }else{
		        	disconnect();   
		        }
			}
			catch(Exception e){
		    	   Logger.getInstance().error("FtpUpload.connect()�쳣��"+e.toString());
			}
	        return false;   
	}
	
	public void disconnect() throws IOException{   
        if(ftpUploader.isConnected()){   
        	ftpUploader.disconnect();   
        }   
    }  
	
	/*ftp�ϴ��ļ����޷��ı��ļ�������޸�����*/
	private boolean uploadFile(File inputFile,String path){
		  try{ 
			  String unicodeInputFilename=new String(inputFile.getName().getBytes("GBK"),"iso-8859-1");    
			  FileInputStream inputStream=new FileInputStream(inputFile);
			
			  boolean uptret=ftpUploader.storeFile(unicodeInputFilename, inputStream);
			  inputStream.close();
			 
			 /* long fileTime=inputFile.lastModified();
			  String time=DateUtil.getTime4Long(fileTime);
			  boolean timeResult= ftpUploader.setModificationTime(unicodeInputFilename, time);
			  int ret=ftpUploader.mfmt(unicodeInputFilename, time);*/

			  /*String filePath=path+"/"+unicodeInputFilename;
			  filePath=filePath.replace(File.separatorChar, '/');
			 FTPFile file = ftpUploader.mlistFile(filePath);
			  if(file==null)
				  file=ftpUploader.mlistFile(unicodeInputFilename);
			  if(file!=null){
				  Calendar calendar = Calendar.getInstance();
				  calendar.setTimeInMillis(inputFile.lastModified());
				  file.setTimestamp(calendar);
			  }*/
			  if(!uptret){
		    	   Logger.getInstance().error("FtpFileService.uploadFile()�ϴ��ļ�["+unicodeInputFilename+"]δ�ɹ�");
		       }
			 
			  return true;
	    }
	  catch(Exception exp){
		  Logger.getInstance().error("FtpFileService.uploadFile()�ϴ��ļ�["+inputFile.getPath()+"]�쳣"+exp.toString());
	  	}
		return false;
	}
	
	
}
