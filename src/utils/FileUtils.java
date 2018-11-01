package utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.MessageBox;

import difflib.DiffRow;
import difflib.DiffRowGenerator;
import resource.CompareMo;
import resource.Constants;
import resource.Logger;
import views.AppView;

public class FileUtils {
	   
	 public enum FileOperatorType {Move,Copy;}
	  public static String formatPath(String path)
		{
		  if (path != null && !"".equals(path)) {
				path = path.replace('/', File.separatorChar);
				path = path.replace('\\', File.separatorChar);
				while (path.endsWith(String.valueOf(File.separatorChar))) {
					path = path.substring(0, path.length() - 1);
				}
			/*	if(path.indexOf(File.separatorChar)==-1){
					path=path+File.separator;
				}*/
			} else {
				return "";
			}
			return path;
		}
	  
	  public static String getFileName(String path){
		  File file=new File(path);
		  if(file.exists()){
			  return file.getName();
		  }else{
			  return "";
		  }
	  }
	  
	  public static String  getFileNameNoSuffix(String fileName){
		  String result=fileName;
		  result=result.substring(0, result.lastIndexOf("."));
		  return result;
	  }
	  
	 
	  public static String getFileSuffix(String fileName){
		  String result=fileName;
		  result=result.substring(result.lastIndexOf(".")+1);
		  return result;
	  }
	  
	  public static void deleteFile(String path){
		  File file=new File(path);
		  if(file.isFile()){
			  file.delete();
		  }else{
			  File[] files=file.listFiles();
			  if(files!=null&&files.length>0){
				 for(File curFile:files){ 
					 deleteFile(curFile.getAbsolutePath());
				 }
			  }
			 file.delete();
		  }
	  }
	  /*�ݹ��ȡĳ��Ŀ¼�µ������ļ����嵥��֧�ֵݹ�*/
	  public static void getFileList(List<File> result,String startPath){
		  if(result!=null){
			  String path=formatPath(startPath);
			  File dir = new File(path);
			  if (!dir.isDirectory()){
				  result.add(dir);
			  } 
			  else{
				  File[] files=dir.listFiles();
				  for(int i=0; i<files.length; i++){
					  File currentFile=files[i];
					  if(currentFile.isDirectory()){
						  getFileList( result, currentFile.getAbsolutePath());
					  }else{
					  result.add(currentFile);
					  }
				  }
			  }
		  }
	  }
	  
	  public static void getFileListWithDir(List<File> result,String startPath){
		  if(result!=null){
			  String path=formatPath(startPath);
			  File dir = new File(path);
			  result.add(dir);
			  if (dir.isDirectory()){
				  File[] files=dir.listFiles();
				  for(int i=0; i<files.length; i++){
					  File currentFile=files[i];
					  if(currentFile.isDirectory()){
						  getFileList( result, currentFile.getAbsolutePath());
					  }else{
						  result.add(currentFile);
					  }
				  }
			  }
		  }
	  }
	  
		/**
		 * ���������в����ƶ��ļ���ָ��Ŀ¼��
		 * ��windowsϵͳ�µ���mv��cp�����ƶ��򿽱��ļ���ָ��Ŀ¼��
		 * windowsϵͳ��Ҫ����move��copy�����ƶ��򿽱��ļ���ָ��Ŀ¼��
		 * optype Ϊ1���ƶ��ļ��� �����Ǹ����ļ�
		 */
	  public static boolean moveOrCopy(String filePath,String targetPath,FileOperatorType opType){
		  if(filePath==null){
			  filePath = "";
			}
			if(targetPath==null){
				targetPath= "";
			}
			String cmdline = "";
			String OsName = System.getProperty("os.name").toLowerCase();
			if (OsName.indexOf("win") != -1) {
				if(opType.equals(FileOperatorType.Move)){
					cmdline = "cmd.exe /c move /Y \"" + filePath + "\" \"" + targetPath + "\"";
				}else{
					cmdline = "cmd.exe /c copy /Y \"" + filePath + "\" \"" + targetPath + "\"";
				}
			}else{
				if(opType.equals(FileOperatorType.Move)){
					cmdline = "mv " + filePath + " " + targetPath;
				}else{
					cmdline = "cp " + filePath + " " + targetPath;
				}
			}
			Logger.getInstance().debug("ִ��shell���"+cmdline);
			return ShellUtil.command(cmdline);
	  }
	  
	  //���ڴ��ļ�������JVM�����ƣ����ļ��޷����棬�Ѿ��쳣
	 /* public static String getMd5ByFile(File file){
		String value = null;
		FileInputStream in=null; 
		try {
			in= new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	  }*/
	  
	  public static String getMd5ByPath(String filePath){
		  FileInputStream fis = null;
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				File file=new File(filePath);
					if(file!=null&&file.isFile()){
						fis = new FileInputStream(file);
						FileChannel fChannel = fis.getChannel();
						ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
						int length = -1;
						while ((length = fChannel.read(buffer)) != -1) {
							buffer.flip();
							md.update(buffer);
							buffer.compact();
						}
						byte[] b = md.digest();
						return byteToHexString(b);
				}else{
					return "";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	  }
	  
	  public static String getMd5ByFile(File file) {
			FileInputStream fis = null;
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				fis = new FileInputStream(file);
				FileChannel fChannel = fis.getChannel();
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
				int length = -1;
				while ((length = fChannel.read(buffer)) != -1) {
					buffer.flip();
					md.update(buffer);
					buffer.compact();
				}
				byte[] b = md.digest();
				return byteToHexString(b);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	  private static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'a', 'b', 'c', 'd', 'e', 'f' };
	  private static String byteToHexString(byte[] tmp) {
			String s;// ���ֽڱ�ʾ���� 16 ���ֽ�
			char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
			// ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ� ת���� 16 �����ַ���ת��
				byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
				str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��, >>> Ϊ�߼����ƣ�������λһ������
				str[k++] = hexdigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
			}
			s = new String(str); // ����Ľ��ת��Ϊ�ַ���
			return s;
		}
	  
	   public synchronized static String getFileContent(String path){
			  String content = "";
			  try {
					File file=new File(path);
					String filename=file.getName();
						FileReader fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);
						String line = "";
						while ((line = br.readLine()) != null) {
							content += line + "\r\n";
						}
						fr.close();
						br.close();
					} catch (Exception e){		
					   Logger.getInstance().equals("��ȡ�ļ�"+path+"�쳣:"+e.toString());
					}
			return content;
		  }
	    
	    public synchronized static List<String> getFileLineList(String path){
			  List<String> result=new ArrayList<String>();
			  try {
					File file=new File(path);
						FileReader fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);
						String line = "";
						while ((line = br.readLine()) != null) {
							result.add(line);
						}
						fr.close();
						br.close();
					} catch (Exception e){		
					   Logger.getInstance().error("��ȡ�ļ�"+path+"�쳣:"+e.toString());
					}
			return result;
		  }
	    
	    public static List<CompareMo> compareAll(String fromFileName,String toFileName){
	    	List<String> original = getFileLineList(fromFileName); 
	    	List<String> revised = getFileLineList(toFileName); 
	    	final DiffRowGenerator.Builder builder = new DiffRowGenerator.Builder(); 
	    	final DiffRowGenerator dfg = builder.build();  
	    	final List<DiffRow> rows = dfg.generateDiffRows(original, revised);  
	    	 List<CompareMo> listCompareMo = new ArrayList<CompareMo>(); 
	    	 int i=1;  
	    	 int oldSize = original.size();  
	    	 int newSize = revised.size();  
	    	 int insertSize = 0;  
	    	 int deleteSize = 0;  
	    	 for (final DiffRow diffRow : rows) {  
    	             String tag = diffRow.getTag().toString();  
    	             String oldLine = diffRow.getOldLine();  
    	             String newLine = diffRow.getNewLine();  
    	             if(CompareMo.Result_CHANGE.equals(tag)){  
				    	 boolean isInset = false;  
				    	 if ((i-insertSize) <= oldSize) {  
				    		 if(oldLine!=null&& oldLine.trim().length()==0){  
				    			 if(!original.get(i-1-insertSize).equals(oldLine)){  
				    	                             tag = CompareMo.Result_INSERT;  
				    	                             isInset = true;  
				    	                             insertSize ++;  
				    	                   }  
				    	           }  
				    	    }  
				    	 if (!isInset) {  
				    	    if ((i-deleteSize) <= newSize) {  
				    	    	if(newLine!=null&& newLine.trim().length()==0){  
				    	    		if(!revised.get(i-1-deleteSize).equals(oldLine)){  
				    	                                 tag = CompareMo.Result_DELETE;  
				    	                                 isInset = true;  
				    	                                 deleteSize ++;  
				    	                             }  
				    	               }  
				    	        }  
				    	    }  
	    	             }  
	    	   listCompareMo.add(new CompareMo(  i, oldLine,   newLine,   tag   ));  
	    	     i++;  
	    	   }  
	    	 return listCompareMo;
	    }
	    
	    public synchronized static void writeFile(String path,String content){
	    	try {
				File file = new File(path);
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();
				fw.close();
			} catch (Exception e) {
				 Logger.getInstance().equals("�����ļ�"+path+"�쳣:"+e.toString());
			}
	    }
	    
	    public synchronized static void writeFile(String path,List<String> lines){
	    	try {
				File file = new File(path);
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				if(lines!=null&&lines.size()>0){
					for(String line:lines){
						bw.write(line);
					}
				}
				bw.close();
				fw.close();
			} catch (Exception e) {
				 Logger.getInstance().equals("�����ļ�"+path+"�쳣:"+e.toString());
			}
	    }
	    
	    //�ݹ�Ѱ��Ŀ¼��ĳ���ļ�
	    public synchronized static File findFileByEnd(String startDir,String filename){
	    	File dir = new File(startDir);
	    	if(dir.isFile()&&dir.getName().endsWith(filename))
	    		return dir;
	    	else{
	    		File[] files=dir.listFiles();
	    		for(File file:files){
	    			if(file.isFile()&&file.getName().endsWith(filename)){
	    				return file;
	    			}else{
	    				return findFileByEnd(file.getAbsolutePath(),filename);
	    			}
	    		}
	    	}
	    	return null;
	    }

	   public static void openFileByLocal(String filePath){ 
			int dot = filePath.lastIndexOf('.');
			 if (dot != -1) {
			 	     String extension = filePath.substring(dot);
			 	     Program program = Program.findProgram(extension);
			 	     if (program != null){
			 	    	 program.launch(filePath);
			 	      }else{
			 	    	String msg="�޷����ļ���"+filePath+"����֧�ֵ��ļ����ͣ�";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
			 	      }
			 	 }
			 else{
				   String msg="�޷�ʶ����ļ���"+filePath+"��";
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					box.open();
			 }
	   }
}
