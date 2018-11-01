package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import resource.Context;
import utils.FileUtils;
import utils.StringUtil;

public class FileFinder {
	public static boolean runable=true;
	
	//只能把工作目录的文件做缓存，否则造成空间问题
	public static List<File> loadFile(){
		List<File> result=new ArrayList<File>();
		  File[] roots=File.listRoots();
		   if(!StringUtil.isNullOrEmpty(Context.workDir)){
			   File startFile=new File(FileUtils.formatPath(Context.workDir));
			   searchaAllFile(result, startFile);
		   }
		   return result;
	}
	

   
   private  static void searchaAllFile(List<File> result,File startFile){
	   if(runable){
			  if(result!=null){
				  	result.add(startFile);
				 if (startFile.isDirectory()){
					  File[] files=startFile.listFiles();
					  if(files!=null){
						  for(int i=0; i<files.length; i++){
							  File currentFile=files[i];
							  if(currentFile.isDirectory()){
								  searchaAllFile( result, currentFile);
							  }else{
									  result.add(currentFile);
							  }
						  }
					  }
				  }
			  }
   		}
   }
   
  
   public static String[] searchFileFromRoot(String keyword,int limited){
	   String[] items=null;
	   List<File> result=new ArrayList();
	   for(int w=0;w<Context.Files.size();w++){
		   if(Context.Files.get(w).getName().toUpperCase().startsWith(keyword.toUpperCase())){
				   if(result.size()<limited){
				       result.add(Context.Files.get(w));
				   }else{
					   break;  
				   }
		   }
	   }
	   if(result.size()>0){
			   items=new String[result.size()];
			   int j=0;
		   for(int i=0;i<result.size();i++){
			     items[i]=result.get(i).getName()+"  "+(char)29+result.get(i).getAbsolutePath();
		   }
	  }
	   return items;
   }
   
}
