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
	  /*递归获取某个目录下的所有文件的清单，支持递归*/
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
		 * 调用命令行参数移动文件到指定目录下
		 * 非windows系统下调用mv和cp命令移动或拷贝文件到指定目录下
		 * windows系统下要调用move和copy命令移动或拷贝文件到指定目录下
		 * optype 为1是移动文件， 其他是复制文件
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
			Logger.getInstance().debug("执行shell命令："+cmdline);
			return ShellUtil.command(cmdline);
	  }
	  
	  //对于大文件，由于JVM的限制，大文件无法生存，已经异常
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
			String s;// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
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
					   Logger.getInstance().equals("读取文件"+path+"异常:"+e.toString());
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
					   Logger.getInstance().error("读取文件"+path+"异常:"+e.toString());
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
				 Logger.getInstance().equals("生成文件"+path+"异常:"+e.toString());
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
				 Logger.getInstance().equals("生成文件"+path+"异常:"+e.toString());
			}
	    }
	    
	    //递归寻找目录下某个文件
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
			 	    	String msg="无法打开文件【"+filePath+"】不支持的文件类型！";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
			 	      }
			 	 }
			 else{
				   String msg="无法识别的文件【"+filePath+"】";
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					box.open();
			 }
	   }
}
