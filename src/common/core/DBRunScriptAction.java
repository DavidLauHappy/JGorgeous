package common.core;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.db.DataHelper;
import model.LOCALNODE;

import resource.Context;
import resource.Paths;
import resource.SecurityCenter;
import utils.FileUtils;
import utils.SqlServer;
import utils.StringUtil;

import bean.LOCALCOMMANDBean;
import bean.LOCALNODEBean;

/**
 * @author Administrator
 * ִ�����ݿ�ű�
 */
public class DBRunScriptAction extends  Action{
	
	private LOCALCOMMANDBean cmd;
	public DBRunScriptAction(LOCALCOMMANDBean cmd){
		this.cmd=cmd;
	}
	
	private String resultInfo="";
	public  String getResultInfo(){
		return resultInfo;
	}

	private String fileID;
	public  String getFileID(){
		return fileID;
	}
	public String installedMD5=" ";
	public String getInstallFileMd5(){
		return this.installedMD5;
	}
	
	public boolean execute(){
		  try{
			   	 Map<String,String> parameters=cmd.getParameters();
			   	 String nodeID=parameters.get("NODE_ID");
			   	 String versionID=parameters.get("VERSION_ID");
				 String ip=parameters.get("IP");
				 String msgID=parameters.get("MSGID");
				 String dbUser=parameters.get("DB_USER");
				 String dbpasswdRaw=parameters.get("DB_PASSWD");
				 String dbname=parameters.get("DB_NAME");
				 String dbpasswd=SecurityCenter.getInstance().decrypt(dbpasswdRaw, Context.EncryptKey);
				 String fileName=parameters.get("FILE_NAME");
				 String objName=this.getObjName(fileName);
				 LOCALNODEBean node=LOCALNODE.getNode(nodeID);
				 String detail="��["+node.getIp()+"]���ݿ��������ִ�нű�[�û�:"+dbUser+"����:"+dbname+"�ű��ļ���"+fileName+"]";
				 String path=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+versionID+File.separator+"runlog"+File.separator;
				 File runLog=new File(path);
				 runLog.mkdirs();
				 String outfile=FileUtils.formatPath(runLog.getPath())+File.separator+msgID+".log";
				 String cmdLine="";
				 if("0".equals(node.getDbType())){
					  cmdLine="sqlcmd -S "+"@ip"+" -U "+"@user"+" -P "+"@passwd"+" -i "+"@file"+" -b "+" -o "+"@outfile";
				 }else{
					 cmdLine="sqlplus "+"@user/@passwd"+"@"+"@ip:@port/@sid"+" @"+"@file"+" > "+"@outfile";
					 cmdLine=cmdLine.replace("@sid", dbname);
					 cmdLine=cmdLine.replace("@port", node.getDbPort());
				 }
				 cmdLine=cmdLine.replace("@ip", ip);
				 cmdLine=cmdLine.replace("@user", dbUser);
				 cmdLine=cmdLine.replace("@passwd", dbpasswd);
				 cmdLine=cmdLine.replace("@file", fileName);
				 cmdLine=cmdLine.replace("@outfile", outfile);
				 WinConsole console=new WinConsole();
				 boolean result=console.callCmd(cmdLine);
				 while(!console.isDone()){
					 Thread.sleep(100L);
				 }
				 String outMsg=console.getOutMsg();
				 if(StringUtil.isNullOrEmpty(outMsg)){
					 	List<String> lines=FileUtils.getFileLineList(outfile);
					 	if(lines!=null&&lines.size()>0){
					 		for(String line:lines){
					 			outMsg=outMsg+"&"+line;//���еı�����Ϣ�޷��������ݿ��XML�ṹ����&�������
					 		}
					 	}
					 	 if(!StringUtil.isNullOrEmpty(outMsg)&&outMsg.length()>4000){
							 outMsg=outMsg.substring(0, 3999);
						 	}
						 	if(!StringUtil.isNullOrEmpty(outMsg)){
						 		outMsg=outMsg.replaceAll("'", "\"");
						 	}
						 	if(this.checkLineResult(lines, node.getDbType())){
						 			this.resultInfo=outMsg;
						 			//ȡ���ݶ���ĸ���ʱ��,���Ӷ���ÿ�δ�����رգ�����ʹ��
								 	Connection con=null;
								 	try{
									 	con=SqlServer.getInstance().getConnection(node.getIp(),node.getDbPort(), dbUser, dbpasswdRaw, node.getDbName(),node.getDbType());
									 	String times=DataHelper.getDbObjectUptInfo(con, objName,node.getDbType());
									 	if(!StringUtil.isNullOrEmpty(times)){
									 		this.installedMD5=times;
									 	}
								 	}catch(Exception e){
								 		this.resultInfo=this.resultInfo+"\r\n"+"��ȡ���ݿ����"+objName+"������ʱ����Ϣ�쳣��"+e.toString();
								 	}finally{
								 		if(con!=null)
								 			con.close();
								 	}
								 	return true;
						 	}else{
						 		this.resultInfo=outMsg;
						 		return false;
						 	}
				   }else{
					   this.resultInfo=outMsg;
				 		return false;
				   }
		  }
		 catch(Exception e){
			   this.resultInfo=e.toString();
			   return  false;
		   }
	}
	
	private boolean checkLineResult(List<String> inputs,String dbType){
		if("0".equals(dbType)){
			 return this.sqlServerCheck(inputs);
		}else{
			 return this.oracleCheck(inputs);
		}
	}
	
	private boolean sqlServerCheck(List<String> inputs){
		if(inputs!=null&&inputs.size()>0){
			for(String line:inputs){
				pattern_globe=Pattern.compile(errorRule,Pattern.CASE_INSENSITIVE);
		    	 matcher_globe=pattern_globe.matcher(line);
				  if(matcher_globe.find())		 
					  return false;
			}
		}else{
			return false;
		}
		return true;		
	}
	
	private boolean oracleCheck(List<String> inputs){
		if(inputs!=null&&inputs.size()>0){
			for(String line:inputs){
				pattern_globe=Pattern.compile(oracleErrorRule,Pattern.CASE_INSENSITIVE);
		    	 matcher_globe=pattern_globe.matcher(line);
				  if(matcher_globe.find())		 
					  return false;
			}
		}else{
			return false;
		}
		return true;		
	}
	
	//���ݽű����ƻ�ȡ���ݿ��������
	//�����ݿ⿪��δ�淶֮ǰ������һ��һ����Ը�Ĵ���
	private String getObjName(String scriptName){
		String fileName=FileUtils.getFileNameNoSuffix(scriptName);
		return FileUtils.getFileSuffix(fileName);
	}
	private static Pattern pattern_globe=null;
    private static Matcher matcher_globe=null;
    private String errorRule="^.*����.+״̬.+$";
    private String oracleErrorRule="^.*ORA-\\d{5}\\:.+$";
}
