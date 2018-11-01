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
 *����ļ�����ֻ֧����Linux�ϵ��ļ���װ�Ȳ���
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
	
	/*����Ŀ���ļ���Ŀ¼�;����ļ��Ĳ����ֿ�(��ָ֤�����)
	 * 1��Ŀ��Ŀ¼�����ɹ����ܣ������ļ�
	 * 2�������ļ��ͻ�ȡ�ļ�md5��ʹ�ùܵ�����֤��׼ȷ�Ļ�ȡ����Ӧmd5
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
					 String source=parameters.get("SRC_PATH");//���Ŀ¼Ӧ���Ǿ���Ŀ¼��(�汾�ϴ�Ŀ¼�����ļ������Ŀ¼��
					 source=FileUtils.formatPath(source);
					 source=source.replace(File.separatorChar, '/');
					 String fileName=source.substring(source.lastIndexOf("/"));
					 //ֱ��ͨ�����޺�׺���ж����ļ�����Ŀ¼��������ģ�Linux�µĿ�ִ���ļ�û�к�׺
					 boolean isFile=true;
					 if(fileName.indexOf(".")==-1){
						 if(StringUtil.isNullOrEmpty(this.fileID)){
							 isFile=false;
						 }
					 }
					 String target=parameters.get("TARGET_PATH");
					 target=FileUtils.formatPath(target);
					 target=target.replace(File.separatorChar, '/');
					 String detail="��["+this.node.getIp()+"]��ִ���ļ���װ[���ļ�:"+source+"��װ��:"+target+"]";
					 tansLoger.setDetail(detail);
					 tansLoger.inroll();
					 //����targetĿ¼��һ������ ��Ҫ����
					 String  cmdCtdir="mkdir -p"+" @dst";
					 cmdCtdir=cmdCtdir.replace("@dst", target);
					 String cmdStr="cp -afpr "+"@src"+" "+"@dst";
					 cmdStr=cmdStr.replace("@src", source);
					 cmdStr=cmdStr.replace("@dst", target);
					 List<String> cmdLinesDir=new ArrayList<String>();
					 if(Dirs.get(node.getId())==null||!Dirs.get(node.getId()).contains(target)){
						 cmdLinesDir.add(cmdCtdir);//����Ŀ¼��ָ��
					 }
					 List<String> cmdLines=new ArrayList<String>();
					 cmdLines.add(cmdStr);//������װ��ָ��
					if(isFile){
						String md5cmd="md5sum "+"@filepath"+" |cut -d ' ' -f1 ";
						md5cmd=md5cmd.replace("@filepath", target+"/"+fileName);
						cmdLines.add(md5cmd);//��ȡMD5��ָ��
					}
					//��ָ����ˮת��Ϊ�ܵ�����ʡ�Ựִ��ʱ��(ִ�н���޷���ȷ��ȡ)
					 String cmdLine="";
					 for(String cmd:cmdLines){
						 cmdLine+=cmd+";";
					 }
					 cmdLine=StringUtil.rtrim(cmdLine, ";");
					 List<String> cmds=new ArrayList<String>();
					 cmds.add(cmdLine);
					 //����Ŀ��Ŀ¼
					 boolean dirResult=true;
					 LnxShell shell= LnxShell.getShell(this.node);
					 if(cmdLinesDir!=null&&cmdLinesDir.size()>0){
						 result=shell.executeCommand(cmdLinesDir);
						 String pushStr=shell.getResponse();
						 if(!result){
							 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
							 String  error=pushStr;
					    	 this.resultInfo="�ļ�["+fileName+"]��װ��["+target+"]ʧ�ܡ�ʧ��ԭ��"+error;
					    	 Logger.getInstance().error(error);
					    	 dirResult=false;
						 }
					 }
					 if(dirResult){
						 result=shell.executeCommand(cmds);
						 String info=shell.getResponse();
						 if(result){
								this.result=LocalAction.Status.ReturnOK.ordinal()+"";
								this.resultInfo="�ļ�["+fileName+"]�ɹ���װ��["+target+"]";
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
					    	 this.resultInfo="�ļ�["+fileName+"]��װ��["+target+"]ʧ�ܡ�ʧ��ԭ��"+error;
					    	 Logger.getInstance().error(error);
						 }
					 }
			   }else{
				   WinShell winShell=WinShell.getShell(node.getIp());
				     Map<String,String> parameters=cmd.getParameters();
				     String fileID=parameters.get("FILE_ID");
					 this.fileID=fileID;
					 //fileID���ڿգ�˵���Ǹ���Ŀ¼
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
									this.resultInfo="�ļ�Զ�̿�����װ�ɹ���"+outMsg;
									status=COMMANDBean.Status.ReturnOK;
									result=true;
					   		 	}else{
						   		 	 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
							    	 this.resultInfo="�ļ�Զ�̿�����װʧ�ܡ�ʧ��ԭ��"+outMsg;
							    	 status=COMMANDBean.Status.ReturnFailed;
							    	 Logger.getInstance().error(outMsg);
							    	 result=false;
					   		 	}
				    	}else{
				    		 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
				    		 this.resultInfo="Ŀ¼Զ�̿���ʧ�ܡ�ʧ��ԭ��"+winShell.getResponse();
				    		 status=COMMANDBean.Status.ReturnFailed;
				    		 result=false;
				    	}
				    }else{
				    	 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
				    	 this.resultInfo="�������["+node.getIp()+"]��VService�����Ƿ������������������";
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
