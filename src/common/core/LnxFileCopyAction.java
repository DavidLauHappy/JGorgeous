package common.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.LOCALNODE;

import utils.FileUtils;
import utils.StringUtil;

import bean.COMMANDBean;
import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;
import business.deploy.action.LocalAction;

public class LnxFileCopyAction extends  Action{
	private LOCALCOMMANDBean cmd;
	public LnxFileCopyAction(LOCALCOMMANDBean cmd){
		this.cmd=cmd;
	}
	
	private String resultInfo="";
	public  String getResultInfo(){
		return resultInfo;
	}

	public String installedMD5=" ";
	public String getInstallFileMd5(){
		return this.installedMD5;
	}
	
	public boolean execute(){
		  try{
			   	 Map<String,String> parameters=cmd.getParameters();
				 String versionID=parameters.get("VERSION_ID");
				 String nodeID=parameters.get("NODE_ID");
				 String fileID=parameters.get("FILE_ID");
				 String source=parameters.get("SRC_PATH");//这个目录应该是绝对目录了(版本上传目录加上文件的相对目录）
				 source=FileUtils.formatPath(source);
				 source=source.replace(File.separatorChar, '/');
				 String fileName=source.substring(source.lastIndexOf("/"));
				 boolean isFile=true;//对付Linux的无后缀文件
				 if(fileName.indexOf(".")==-1){
					 if(StringUtil.isNullOrEmpty(fileID)){
						 isFile=false;
					 }
				 }
				 String target=parameters.get("TARGET_PATH");
				 target=FileUtils.formatPath(target);
				 target=target.replace(File.separatorChar, '/');
				 String  cmdCtdir="mkdir -p"+" @dst";
				 cmdCtdir=cmdCtdir.replace("@dst", target);
				 String cmdStr="cp -afpr "+"@src"+" "+"@dst";
				 cmdStr=cmdStr.replace("@src", source);
				 cmdStr=cmdStr.replace("@dst", target);
				 LOCALNODEBean node=LOCALNODE.getNode(nodeID);
				 List<String> cmdLinesDir=new ArrayList<String>();
				 if(node.cmdDirs!=null&&!node.cmdDirs.contains(target)){
					 cmdLinesDir.add(cmdCtdir);//创建目录的指令
				 } 
				 List<String> cmdLines=new ArrayList<String>();
				 cmdLines.add(cmdStr);//拷贝安装的指令
				if(isFile){
					String md5cmd="md5sum "+"@filepath"+" |cut -d ' ' -f1 ";
					md5cmd=md5cmd.replace("@filepath", target+"/"+fileName);
					cmdLines.add(md5cmd);//获取MD5的指令
				}
				//将指令流水转化为管道，节省会话执行时间(执行结果无法精确获取)
				 String cmdLine="";
				 for(String cmd:cmdLines){
					 cmdLine+=cmd+";";
				 }
				 List<String> realCmds=new ArrayList<String>();
				 realCmds.add(cmdLine);
				 LnxShell shell=LnxShell.getShell(node);
				 if(shell!=null){
					 //创建目标目录
					 boolean dirResult=true;
					 if(cmdLinesDir!=null&&cmdLinesDir.size()>0){
						boolean result=shell.executeCommand(cmdLinesDir);
						 String pushStr=shell.getResponse();
						 if(!result){
							 this.resultInfo="文件["+fileName+"]安装到["+target+"]前创建目标目录失败。失败原因："+pushStr;
							 dirResult=false;
						 }
					 }
					 if(dirResult){
						 //为了不重复创建目录节省时间，缓存创建过的目录
						 if(node.cmdDirs==null){
							 node.cmdDirs=new ArrayList<String>();
						 }
						 node.cmdDirs.add(target);
						 boolean result=shell.executeCommand(realCmds);
						 String info=shell.getResponse();
						 if(!result){
							 this.resultInfo="文件["+fileName+"]安装到["+target+"]前创建目标目录失败。失败原因："+info;
							 return false;
						 }else{
							 String md5Info= this.getMD5FromRespone(info);
							this.installedMD5=md5Info;
							 this.resultInfo="文件["+fileName+"]安装到["+target+"]成功";
							return true;
						 }
					 }
				 } 
		  }catch(Exception e){
			   this.resultInfo=e.toString();
			   return  false;
		   }
		   return true;
	}

	public String getMD5FromRespone(String str){
		String[] lines=str.split("\r\n");
		for(String line:lines){
			if(!StringUtil.isNullOrEmpty(line)&&line.indexOf(" ")==-1)
				return line;
		}
		return " ";
	}
}
