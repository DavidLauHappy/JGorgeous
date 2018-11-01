package business.tversion.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import resource.Logger;
import views.AppView;
import views.StatusBarView;
/**
 * @author Administrator
 *�Ա����ļ��Ĵ���ͽ��
 *���ڰ��Ƚϴ�ʱ�䳤������Ҫ�Խ�����н��ȸ���״̬�������������û�
 */
public class PackageProcessor extends Thread{
	private CountDownLatch latch;
	private String pkgFile;
	private String pkgDir;
	
	public PackageProcessor(CountDownLatch latch,String pkgFile,String pkgDir){
		this.latch=latch;
	    this.pkgFile=pkgFile;
		this.pkgDir=pkgDir;
	}
	public void run(){
		try{
			result=this.zipFile(pkgFile, pkgDir);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			/* if(!AppView.getInstance().isInterrupt()){
	 				AppView.getInstance().getDisplay().asyncExec(new Runnable() {
	 					public void run() {
	 						StatusBarView.getInstance(null).getSelfContent().setVisible(false);
	 					}
	 				});
			 }*/
			latch.countDown();
		}
	}
	
	private boolean result=false;
	private String resultInfo="";
	
	
	public boolean isResult() {
		return result;
	}
	public String getResultInfo() {
		return resultInfo;
	}
	//��汾��
	 private boolean zipFile(String zipFileName,String sourceDir){
	   	  File file = new File(sourceDir);  
	   	  if (!file.exists()) {
	   		  Logger.getInstance().error("�ļ�ѹ��ԴĿ¼�����ڣ�"+sourceDir);
	   	  }
	   	/* //�����ǽ��水ť�ַŵ����̣߳������̸߳���UIΪʲôһֱ������?δ��λ��ԭ��
	    if(!AppView.getInstance().isInterrupt()){
				AppView.getInstance().getDisplay().asyncExec(new Runnable() {
					public void run() {
						StatusBarView.getInstance(null).getSelfContent().setVisible(true);
						StatusBarView.getInstance(null).getStatus().setText("���ɰ汾��["+pkgFile+"]����");
					}
				});
	   	  }*/
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
					return true;
				} catch (Exception e) {
					resultInfo=sourceDir+"�ļ�["+zipFileName+"]ѹ������"+e.getMessage();
					Logger.getInstance().error(sourceDir+"�ļ�["+zipFileName+"]ѹ������"+e.getMessage());
				}
	   	    return false;
	     } 
	 
	 private  void compressPackage(File srcfile, ZipOutputStream out,String baseDir)  throws Exception{
    	 if(srcfile.isDirectory()){
    		 this.compressDirectory(srcfile, out, baseDir);
    	 }else{
    		this.compressFile(srcfile, out, baseDir);
    		 //���½���
    /*	  	 if(!AppView.getInstance().isInterrupt()){
 				AppView.getInstance().getDisplay().asyncExec(new Runnable() {
 					public void run() {
 						int cur=StatusBarView.getInstance(null).getProgressBar().getSelection()+1;
 						if(cur>=100){
 							cur=1;
 						}
 						StatusBarView.getInstance(null).getProgressBar().setSelection(cur);
 					}
 				});
    	  	 }*/
    	 }
  	}
      
      private void compressDirectory(File srcDir, ZipOutputStream out, String baseDir)  throws Exception{
    	  if (!srcDir.exists()) {   
    		  return;
    	  }
    	  File[] files = srcDir.listFiles();   
    	  for (int i = 0; i < files.length; i++) {   
    		  this.compressPackage(files[i], out, baseDir + srcDir.getName() + File.separator);
    	  }
      }
      
      private  void compressFile(File srcfile, ZipOutputStream out,String baseDir) throws Exception {
    	  if (!srcfile.exists()) {   
    		  return;
    	  }
    	  FileInputStream fis;
    		try {
    			fis = new FileInputStream(srcfile);
    			ZipEntry entry = new ZipEntry(baseDir+srcfile.getName());
    			entry.setTime(srcfile.lastModified());//save the modify time
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
    			throw new Exception("�ļ�"+srcfile.getAbsolutePath()+" ѹ������"+e.getMessage());
    		}
      }

}
