package utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;
import resource.Logger;

public class Zipper {
	
          private static Zipper unique_instance;
          public static Zipper getInstance(){
        	  if(unique_instance==null)
        		  unique_instance=new Zipper();
        	  return unique_instance;
          }
          
          private Zipper(){};
          
          private static final int BufferSize=1024;
          
       /*   ��ѹ�汾��
          packageName ѹ����·��
          outputDir       ��Ҫ�ͷŵ�Ŀ��Ŀ¼
          */
          public boolean unPackage(String packageName,String outputDir){
        	  boolean result=true;
        	  String type = packageName.substring(packageName.lastIndexOf(".") + 1);
        	  if (type.equals("zip")) {
        		  result= this.unZip(packageName, outputDir);
        		  } else if (type.equals("rar")) {
        			  result=this.unRar(packageName, outputDir);
        		  } else {
        		   Logger.getInstance().error("ֻ֧��zip��rar��ʽ��ѹ������" );
        		   result=false;
        		  }
        	  return result;
          }
          
         private boolean unZip(String zipFilePath,String outputDir){
        	  Logger.getInstance().debug("zipFilePath:="+zipFilePath+"; outputDir:="+outputDir);
        	  boolean result=true;
        	  ZipFile  zipFile=null;
        	  try{
        		  File outDir=new File(outputDir);
        		  if(!outDir.exists()){
        			  outDir.mkdirs();//��������Ŀ¼����������Ӳ�̿ռ䲻�������
        		  }
        		  zipFile=new ZipFile(zipFilePath,"gbk");//����ļ���������������
        		  Enumeration en=zipFile.getEntries();
        		  ZipArchiveEntry zipEntry=null;
        		  while(en.hasMoreElements()){
        			  zipEntry=(ZipArchiveEntry)en.nextElement();
        			  Logger.getInstance().debug(" zipzipEntry="+zipEntry.getName());
        			  if(zipEntry.isDirectory()){
        				  String dirName=zipEntry.getName();
        				  dirName=dirName.substring(0, dirName.length()-1);
        				  File dir=new File(outDir.getPath()+File.separator+dirName);
        				  dir.mkdirs();
        				  dir.setLastModified(zipEntry.getTime());
        			  }else{
        				  String filePath=outDir.getPath()+File.separator+zipEntry.getName();
        				  File file=new File(filePath);
        				  if(!file.exists()){
        					  String splitor="";
        					  String OsName = System.getProperty("os.name").toLowerCase();		
        						if (OsName.indexOf("win") != -1){
        							splitor="/";
        						}else{
        							splitor="\\";
        						}
        					  String[] floderNames=zipEntry.getName().split(splitor);
        					  String realFloder="";
        					  for(int w=0;w<floderNames.length-1;w++){
        						  realFloder+=floderNames[w]+File.separator;
        					  }
        					  realFloder=outDir.getPath()+File.separator+realFloder;
        					  File tempDir=new File(realFloder);
        					  tempDir.mkdirs();
        					  tempDir.setLastModified(zipEntry.getTime());
        					  Logger.getInstance().debug(" tempDir="+realFloder);
        				  }
        				  file.createNewFile();        				 
        				  InputStream in=zipFile.getInputStream(zipEntry);
        				  FileOutputStream out=new FileOutputStream(file);
        				  try{
        					  int c;
        					  byte[] by=new byte[BufferSize];
        					  while((c=in.read(by))!=-1){
        						  out.write(by, 0, c);
        					  }
        					  out.flush();
        			
        					
        				  }
        				  catch(Exception exp){
        					  result=false;
        					  Logger.getInstance().error("exception when readding package file :"+exp.toString() );
        					  exp.printStackTrace();
        				  }
        				  finally{
        					  out.close();
        					  in.close();  
        					  file.setLastModified(zipEntry.getTime());
        				  }
        			  }
        		  }
        		  zipFile.close();
        	  }
        	  catch(Exception e){
        		  result=false;
        		  Logger.getInstance().error("exception when unZip package :"+e.toString() );
        		  e.printStackTrace();
        	  }finally{ }
        	  return result;
          }
         
       /*  ������Ҫʹ��rar�����汾ѹ����
         1 rar�е�ĳЩdll exe�����м����㷨�����ļ�У�� �����޷���ѹ
         2 rar�е�ĳЩ�ļ����󣬵��»��治�����˳�������
         3 �������ļ�������Ŀ¼�޷��ܺõ�֧��
         */
          private  boolean unRar(String fileName, String unZipDir) {  
	        	  boolean result=true;
	        	   Archive archive = null;
		           FileOutputStream fos = null;
        	         try {   
        	            File f = new File(unZipDir);    // ���ж�Ŀ���ļ����Ƿ���ڣ�������������½��������Ŀ¼������Ҳ�½�   
        	             if (!f.exists()) {   
        	                 f.mkdirs();   
        	            }   
        	             archive = new Archive(new File(fileName));
        	             FileHeader fh = archive.nextFileHeader();        	            
        	             while (fh != null) {
        	            	 if (!fh.isDirectory()) {
        	            	     String compressFileName = fh.getFileNameString().trim();
        	            	     String destFileName = "";
        	            	     String destDirName = "";
        	            	     // ��windowsϵͳ
        	            	     if (File.separator.equals("/")) {
        	            	      destFileName = unZipDir +File.separator+ compressFileName.replaceAll("\\\\", "/");
        	            	      destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
        	            	     } else {
        	            	      destFileName = unZipDir +File.separator+ compressFileName.replaceAll("/", "\\\\");
        	            	      destDirName = destFileName.substring(0,destFileName.lastIndexOf("\\"));
        	            	     }
        	            	     File dir = new File(destDirName);
        	            	     if (!dir.exists() || !dir.isDirectory()) {
        	            	      dir.mkdirs();
        	            	     }
        	            	     Logger.getInstance().debug("unRar file:="+fh.getFileNameString()+" ");
        	            	     fos = new FileOutputStream(new File(destFileName));
        	            	     try{
		        	            	     archive.extractFile(fh, fos);
		        	            	     fos.flush();
		        	            	     fos.close();
        	            	     }catch(Exception exp){
        	            	    	 Logger.getInstance().error("can not extractFile:"+fh.getFileNameString()+" for reason "+exp.getMessage());
        	            	    	  result=false;
        	            	     }
        	            	     finally{
        	            	    	 fos = null;
        	            	    	 }
        	            	    }
        	            	    fh = archive.nextFileHeader();
        	            	   }
        	             archive.close();
        	             archive = null; 
        	         } catch (Exception e) {   
        	        	  result=false;
    					  Logger.getInstance().error("exception when unRar package file :"+fileName+" to "+unZipDir+";"+e.toString() );
        	              e.printStackTrace();   
        	          }  
        	         finally {
        	        	   if (fos != null) {
        	        	    try {
	        	        	     fos.close();
	        	        	     fos = null;
        	        	    	} catch (Exception e) {
        	        	    	e.printStackTrace();
        	        	    	}
        	        	   }
        	        	   if (archive != null) {
        	        	    try {
        	        	    	archive.close();
        	        	    	archive = null;
        	        	    } catch (Exception e) {
        	        	     e.printStackTrace();
        	        	     }
        	        	  }
        	         }
        	         return result;
        	     }   
 
          
          public void zipFile(String zipFileName,String sourceDir){
        	  File file = new File(sourceDir);  
        	  if (!file.exists()) {
        		  Logger.getInstance().error("�ļ�ѹ��ԴĿ¼�����ڣ�"+sourceDir);
        	  }
        	  File zipfile=new File(zipFileName);
        		//��������Apache�Ŀ�Դѹ�����������޷�֧������·���������ļ����Ƶĸ���
        	  ZipOutputStream out=null;
        	  FileOutputStream fos;
        	  try {
    				fos = new FileOutputStream(zipfile);
    				CheckedOutputStream cos=new CheckedOutputStream(fos,new CRC32());
    				out=new ZipOutputStream(cos);
    				String baseDir="";
    				this.compressPackage(file, out, baseDir);
    				if(out!=null)
    					out.close();
    			} catch (Exception e) {
    				Logger.getInstance().error("�ļ�ѹ������"+e.getMessage());
    			}
        	 
          }
        
          private  void compressPackage(File srcfile, ZipOutputStream out,String baseDir)  {
        	 if(srcfile.isDirectory()){
        		 Logger.getInstance().debug(" compress directory="+srcfile.getAbsolutePath());
        		 this.compressDirectory(srcfile, out, baseDir);
        	 }else{
        		 Logger.getInstance().debug(" compress file="+srcfile.getAbsolutePath());
        		 this.compressFile(srcfile, out, baseDir);
        	 }
      	}
          
          private void compressDirectory(File srcDir, ZipOutputStream out, String baseDir) {
        	  if (!srcDir.exists()) {   
        		  return;
        	  }
        	  File[] files = srcDir.listFiles();   
        	  for (int i = 0; i < files.length; i++) {   
        		  this.compressPackage(files[i], out, baseDir + srcDir.getName() + File.separator);
        	  }
          }
          
          private  void compressFile(File srcfile, ZipOutputStream out,String baseDir) {
        	  if (!srcfile.exists()) {   
        		  return;
        	  }
        	  FileInputStream fis;
        		try {
        			fis = new FileInputStream(srcfile);
        			ZipEntry entry = new ZipEntry(baseDir+srcfile.getName());
        			entry.setTime(srcfile.lastModified());
        			out.putNextEntry(entry);
        			int count = 0;
        			byte data[] = new byte[2046];
        			while ((count = fis.read(data)) != -1) {
        				out.write(data, 0, count);
        			}
        			out.flush();
        			fis.close();
        		} catch (Exception e) {
        			Logger.getInstance().error("�ļ�"+srcfile.getAbsolutePath()+" ѹ������"+e.getMessage());
        			e.printStackTrace();
        		}
          }
}
