package business.deploy.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.core.LnxShell;
import common.core.WinShell;

import model.SYSTEM;

import resource.Context;
import resource.Logger;
import utils.FileUtils;
import utils.StringUtil;

import bean.COMMANDBean;
import bean.NODEBean;
import bean.SYSTEMBean;
import business.deploy.bean.TransData;

/**
 * @author DavidLau 
 *这个文件推送只支持往Linux上的文件安装等操作
 */
public class FileCopyAction extends LocalAction {
	private String resultInfo="";
	private String result="";
	public  String getResultInfo(){
		return resultInfo;
	}
	public  String getResult(){
		return result;
	}
	private String fileID;
	public  String getFileID(){
		return fileID;
	}
	public String installedMD5="";
	public String getInstallFileMd5(){
		return this.installedMD5;
	}
	

	private COMMANDBean cmd;
	private NODEBean node;
	public static Map<String,ArrayList<String>> Dirs=new HashMap<String, ArrayList<String>>();
	public FileCopyAction(COMMANDBean data,NODEBean bean){
		this.cmd=data;
		this.node=bean;
	}
	
	/*创建目标文件的目录和具体文件的操作分开(保证指令不会乱)
	 * 1）目标目录创建成功才能，拷贝文件
	 * 2）拷贝文件和获取文件md5码使用管道，保证能准确的获取到对应md5
	 * */
	public boolean execute(){
		   boolean result=false;
		   COMMANDBean.Status status;
		   TransData tansLoger=new TransData();
			SYSTEMBean system= SYSTEM.getSystemsByID(this.node.getSystemID(), Context.session.currentFlag);
			tansLoger.setId(this.cmd.getId());
			tansLoger.setApp(system.getApp());
			tansLoger.setSystemID(system.getBussID());
			tansLoger.setNodeID(this.node.getId());
			tansLoger.setFuncID("F-999-00003");
		   try{
			   if((NODEBean.OS.Linux.ordinal()+"").equals(node.getOs())){
				   	 Map<String,String> parameters=cmd.getParameters();
					 String versionID=parameters.get("VERSION_ID");
					 String fileID=parameters.get("FILE_ID");
					 this.fileID=fileID;
					 String source=parameters.get("SRC_PATH");//这个目录应该是绝对目录了(版本上传目录加上文件的相对目录）
					 source=FileUtils.formatPath(source);
					 source=source.replace(File.separatorChar, '/');
					 String fileName=source.substring(source.lastIndexOf("/"));
					 //直接通过有无后缀来判断是文件还是目录是有问题的，Linux下的可执行文件没有后缀
					 boolean isFile=true;
					 if(fileName.indexOf(".")==-1){
						 if(StringUtil.isNullOrEmpty(this.fileID)){
							 isFile=false;
						 }
					 }
					 String target=parameters.get("TARGET_PATH");
					 target=FileUtils.formatPath(target);
					 target=target.replace(File.separatorChar, '/');
					 String detail="在["+this.node.getIp()+"]上执行文件安装[将文件:"+source+"安装到:"+target+"]";
					 tansLoger.setDetail(detail);
					 tansLoger.inroll();
					 //这里target目录不一定存在 需要创建
					 String  cmdCtdir="mkdir -p"+" @dst";
					 cmdCtdir=cmdCtdir.replace("@dst", target);
					 String cmdStr="cp -afpr "+"@src"+" "+"@dst";
					 cmdStr=cmdStr.replace("@src", source);
					 cmdStr=cmdStr.replace("@dst", target);
					 List<String> cmdLinesDir=new ArrayList<String>();
					 if(Dirs.get(node.getId())==null||!Dirs.get(node.getId()).contains(target)){
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
					 cmdLine=StringUtil.rtrim(cmdLine, ";");
					 List<String> cmds=new ArrayList<String>();
					 cmds.add(cmdLine);
					 //创建目标目录
					 boolean dirResult=true;
					 LnxShell shell= LnxShell.getShell(this.node);
					 if(cmdLinesDir!=null&&cmdLinesDir.size()>0){
						 result=shell.executeCommand(cmdLinesDir);
						 String pushStr=shell.getResponse();
						 if(!result){
							 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
							 String  error=pushStr;
					    	 this.resultInfo="文件["+fileName+"]安装到["+target+"]失败。失败原因："+error;
					    	 Logger.getInstance().error(error);
					    	 dirResult=false;
						 }
					 }
					 if(dirResult){
						 result=shell.executeCommand(cmds);
						 String info=shell.getResponse();
						 if(result){
								this.result=LocalAction.Status.ReturnOK.ordinal()+"";
								this.resultInfo="文件["+fileName+"]成功安装到["+target+"]";
								status=COMMANDBean.Status.ReturnOK;
							    ArrayList<String> nodeDirs=Dirs.get(node);
							    if(nodeDirs!=null){
								    if(!nodeDirs.contains(target)){
								    	nodeDirs.add(target);
								    }
							    }else{
							    	nodeDirs=new ArrayList<String>();
							    	nodeDirs.add(target);
							    }
							 	Dirs.put(node.getId(), nodeDirs);
							    String md5Info= this.getMD5FromRespone(info);
								this.installedMD5=md5Info;
								result=true;
						 }
						 else{
							 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
							 String  error=info;
					    	 this.resultInfo="文件["+fileName+"]安装到["+target+"]失败。失败原因："+error;
					    	 Logger.getInstance().error(error);
						 }
					 }
			   }else{
				   WinShell winShell=WinShell.getShell(node.getIp());
				     Map<String,String> parameters=cmd.getParameters();
				     String fileID=parameters.get("FILE_ID");
					 this.fileID=fileID;
					 //fileID等于空，说明是复制目录
				    if(winShell!=null){
				    	boolean ret=winShell.executeCommand(cmd.getParameter(),true);
				    	if(ret){
				    		String response=winShell.getResponse();
				    		int index=response.indexOf("|");
				   		 	String xmlStr=response.substring(index+1);
				   		 	Map<String,String> retMap=StringUtil.parseXML(xmlStr);
				   		 	String outMsg=retMap.get("OUT_MSG");
				   		 	String outCode=retMap.get("OUT_CODE");
				   			this.installedMD5=retMap.get("MD5");
					   		 if("0".equals(outCode)){
					   			   this.result=LocalAction.Status.ReturnOK.ordinal()+"";
									this.resultInfo="文件远程拷贝安装成功："+outMsg;
									status=COMMANDBean.Status.ReturnOK;
									result=true;
					   		 	}else{
						   		 	 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
							    	 this.resultInfo="文件远程拷贝安装失败。失败原因："+outMsg;
							    	 status=COMMANDBean.Status.ReturnFailed;
							    	 Logger.getInstance().error(outMsg);
							    	 result=false;
					   		 	}
				    	}else{
				    		 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
				    		 this.resultInfo="目录远程拷贝失败。失败原因："+winShell.getResponse();
				    		 status=COMMANDBean.Status.ReturnFailed;
				    		 result=false;
				    	}
				    }else{
				    	 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
				    	 this.resultInfo="请检查机器["+node.getIp()+"]上VService服务是否正常部署或正常启动";
				    	 status=COMMANDBean.Status.ReturnFailed;
				    	 result=false;
				    }
			   }
		   }catch(Exception e){
			   this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
			   this.resultInfo=e.toString();
			   status=COMMANDBean.Status.ReturnFailed;
			   result=false;
		   }finally{
			   tansLoger.setEndTime();
			   this.cmd.setExecuteInfo(this.result, this.resultInfo, COMMANDBean.Remind.No.ordinal()+"");
		   }
		   return result;
	}
	
	public String getMD5FromRespone(String str){
		String[] lines=str.split("\r\n");
		for(String line:lines){
			if(!StringUtil.isNullOrEmpty(line)&&line.indexOf(" ")==-1)
				return line;
		}
		return "";
	}
}
