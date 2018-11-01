package business.deploy.action;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.db.DataHelper;

import model.PFILE;
import model.SYSTEM;

import resource.Context;
import resource.Logger;
import resource.Paths;
import resource.SecurityCenter;
import utils.FileUtils;
import utils.ShellUtil;
import utils.SqlServer;
import utils.StringUtil;
import bean.COMMANDBean;
import bean.NODEBean;
import bean.PFILEBean;
import bean.SYSTEMBean;
import business.deploy.bean.TransData;

public class DBRunAction extends LocalAction {
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
	private boolean doneFlag=false;
	public void setDoneFlag(boolean able){
		this.doneFlag=able;
	}
	private COMMANDBean cmd;
	private NODEBean node;
	public DBRunAction(COMMANDBean data,NODEBean bean){
		this.cmd=data;
		this.node=bean;
	}
	public boolean execute(){
		   boolean ret;
		   String fileOperation="";
		   COMMANDBean.Status status;
		   TransData tansLoger=new TransData();
			SYSTEMBean system= SYSTEM.getSystemsByID(this.node.getSystemID(), Context.session.currentFlag);
			tansLoger.setId(this.cmd.getId());
			tansLoger.setApp(system.getApp());
			tansLoger.setSystemID(system.getBussID());
			tansLoger.setNodeID(this.node.getId());
			tansLoger.setFuncID("F-999-00002");
			try{
					Map<String,String> parameters=cmd.getParameters();
					 String versionID=parameters.get("VERSION_ID");
					 String ip=parameters.get("IP");
					 String stepID=parameters.get("STEP_ID");
					 String fileID=parameters.get("FILE_ID");
					 this.fileID=fileID;
					 PFILEBean pfile=PFILE.geFile(versionID, stepID, fileID);
					 String dbUser=parameters.get("DB_USER");
					 String dbpasswdRaw=parameters.get("DB_PASSWD");
					 String dbname=parameters.get("DB_NAME");
					 String dbpasswd=SecurityCenter.getInstance().decrypt(dbpasswdRaw, Context.EncryptKey);
					 String fileName=parameters.get("FILE_NAME");
					 fileOperation="��["+this.node.getName()+"]��ִ���ļ�["+fileName+"]";
					 String detail="��["+this.node.getIp()+"]���ݿ��������ִ�нű�[�û�:"+dbUser+"����:"+dbname+"�ű��ļ���"+fileName+"]";
					 tansLoger.setDetail(detail);
					 tansLoger.inroll();
					 String path=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+versionID+File.separator;
					 path=path+"runlog"+File.separator;
					 File runLog=new File(path);
					 runLog.mkdirs();
					 String outfile=FileUtils.formatPath(runLog.getPath())+File.separator+this.cmd.getId()+".log";
					//-eѡ�Ҫ��ֱ�Ӵ�ԭʼ�˽��
					 String cmdLine="";
					 if("0".equals(pfile.getBootfalg())){
						 if((NODEBean.DB.SqlServer.ordinal()+"").equals(this.node.getDbType())){
							  cmdLine="sqlcmd -S "+"@ip"+" -U "+"@user"+" -P "+"@passwd"+" -i "+"@file"+" -b "+" -o "+"@outfile";
						 }else{
							 cmdLine="sqlplus "+"@user/@passwd"+"@"+"@ip:@port/@sid"+" @"+"@file"+" > "+"@outfile";
							 cmdLine=cmdLine.replace("@sid", dbname);
							 cmdLine=cmdLine.replace("@port", this.node.getDbport());
						 }
					 }else{
						 cmdLine=fileName;
					 }
					
					 cmdLine=cmdLine.replace("@ip", ip);
					 cmdLine=cmdLine.replace("@user", dbUser);
					 cmdLine=cmdLine.replace("@passwd", dbpasswd);
					 cmdLine=cmdLine.replace("@file", fileName);
					 cmdLine=cmdLine.replace("@outfile", outfile);
					 Logger.getInstance().debug("sqlcmd/sqlplus:"+cmdLine);
					 try{
						 	ShellUtil.execute(cmdLine,this);
						 	//��ȡִ�н����������Ҫʹ��֪ͨģʽ���޷�Ԥ֪�ⲿ����ʲôʱ���ܹ�ִ����ɡ�
						 	while(!this.doneFlag){
						 		Thread.sleep(100);
						 	}
						 	//���ܽű�ִ�������Σ����������ݿ�ȡִ������
						 	String returnInfo="";
						 	List<String> lines=FileUtils.getFileLineList(outfile);
						 	if(lines!=null&&lines.size()>0){
						 		for(String line:lines){
						 			returnInfo=returnInfo+"&"+line;//���еı�����Ϣ�޷��������ݿ��XML�ṹ����&�������
						 		}
						 	}else{
						 		returnInfo=this.resultInfo;
						 	}
						 	if(!StringUtil.isNullOrEmpty(returnInfo)&&returnInfo.length()>4000){
						 		returnInfo=returnInfo.substring(0, 3999);
						 	}
						 	if(!StringUtil.isNullOrEmpty(returnInfo)){
						 		returnInfo=returnInfo.replaceAll("'", "\"");
						 	}
						 	if(this.checkLineResult(lines,this.node.getDbType())){
						 		 this.result=LocalAction.Status.ReturnOK.ordinal()+"";
								 this.resultInfo=returnInfo;
								 ret=true;
								 status= COMMANDBean.Status.ReturnOK;
						 	}else{
						 		 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
						 		 this.resultInfo=returnInfo;
								 ret=false;
								 status= COMMANDBean.Status.ReturnFailed;
						 	}
						 	
						 	//ȡ���ݶ���ĸ���ʱ��,���Ӷ���ÿ�δ�����رգ�����ʹ��
						 	Connection con=null;
						 	try{
							 	con=SqlServer.getInstance().getConnection(this.node.getIp(),this.node.getDbport(), dbUser, dbpasswdRaw, pfile.getDbOwner(),this.node.getDbType());
							 	String times=DataHelper.getDbObjectUptInfo(con, pfile.getObjName(),this.node.getDbType());
							 	if(!StringUtil.isNullOrEmpty(times)){
							 		this.installedMD5=times;
							 	}
						 	}catch(Exception e){
						 		Logger.getInstance().error("DBRunAction.execute()��ȡ���ݿ����"+pfile.getObjName()+"������ʱ����Ϣ�쳣��"+e.toString());
						 	}finally{
						 		if(con!=null)
						 			con.close();
						 	}
						
					 }catch(Exception exp){
						 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
				    	 this.resultInfo="���ýű������쳣��"+exp.toString();
				    	 ret=false;
						 status= COMMANDBean.Status.ReturnFailed;
					 }
					
			}catch(Exception e){
				 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
		    	 this.resultInfo=e.toString();
		    	 status=COMMANDBean.Status.ReturnFailed;
		    	 ret=false;
			}
			 tansLoger.setEndTime();
			//��¼����������־
			String path=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+this.cmd.getPkgID()+File.separator+system.getName()+"_installdb.log";
			Logger.getInstance().logDBInstall(path,fileOperation,this.resultInfo);    
			 this.cmd.setExecuteInfo(this.result, this.resultInfo, COMMANDBean.Remind.No.ordinal()+"");
			return ret;
	}
	
	private boolean checkLineResult(List<String> inputs,String dbType){
		 if((NODEBean.DB.SqlServer.ordinal()+"").equals(dbType)){
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
	
	 public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}
	public void setResult(String result) {
		this.result = result;
	}
	private static Pattern pattern_globe=null;
    private static Matcher matcher_globe=null;
    private String errorRule="^.*����.+״̬.+$";
    private String oracleErrorRule="^.*ORA-\\d{5}\\:.+$";
}
