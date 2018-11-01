package business.deploy.action;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import common.db.DataHelper;

import model.COMPONENT;
import model.PFILE;
import model.STEP;
import model.SYSTEM;


import resource.Context;
import resource.Logger;
import resource.Paths;
import utils.FileUtils;
import utils.SqlServer;
import utils.StringUtil;
import bean.COMMANDBean;
import bean.COMPONENTBean;
import bean.NODEBean;
import bean.PFILEBean;
import bean.STEPBean;
import bean.SYSTEMBean;
import business.deploy.bean.TransData;



/**
 * @author Administrator
 * ���ݱ���Ҫ�������Ӷ��� 
 * ���屸�ݺ����ݷֿ���
 * ���ݱ����ڱ��ݿ⣬���Կ��ٱ��ݴ�����屸�ݳɲ������ı��ļ�
 *
 */
public class DBBackupAction extends LocalAction {

	private String resultInfo="";
	private String result="";
	private String backPath="";
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
	public String odlMD5="";
	public String getInstallFileMd5(){
		return this.odlMD5;
	}


	private COMMANDBean cmd;
	private NODEBean node;
	public DBBackupAction(COMMANDBean data,NODEBean bean){
		this.cmd=data;
		this.node=bean;
		backPath=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+  "backup"+File.separator+this.node.getId()+File.separator+ this.cmd.getPkgID()+File.separator;
		File dir=new File(this.backPath);
		if(!dir.exists())
			dir.mkdirs();
	}
	
	public boolean execute(){
		COMMANDBean.Status status=null;
		TransData tansLoger=new TransData();
		SYSTEMBean system= SYSTEM.getSystemsByID(this.node.getSystemID(), Context.session.currentFlag);
		tansLoger.setId(this.cmd.getId());
		tansLoger.setApp(system.getApp());
		tansLoger.setSystemID(system.getBussID());
		tansLoger.setNodeID(this.node.getId());
		tansLoger.setFuncID("F-999-00001");
		boolean ret=false;
		String fileOperation="";
		        try{
					 Map<String,String> parameters=this.cmd.getParameters();
					 String dbuser=parameters.get("DB_USER");
					 String dbpasswd=parameters.get("DB_PASSWD");
					 String dbnameData=parameters.get("DB_NAME");
					 String backDbName=parameters.get("BACK_DB");
					 String[] dbnames=dbnameData.split(",");
					 Connection bkConection=null;
					 String detail="��["+this.node.getIp()+"]���ݿ��������ִ�б���[���û�:"+dbnameData+"���󱸷ݵ�:"+backDbName+"]";
					 tansLoger.setDetail(detail);
					 tansLoger.inroll();
					 try{
						 
						 bkConection=SqlServer.getInstance().getConnection(this.node.getIp(), this.node.getDbport(),dbuser, dbpasswd,backDbName,this.node.getDbType());
						 //һ���ڵ����ݿ�1��ʵ��������⣬ѭ�������б��ݣ������û����õ�˳��
						 for(String dbname:dbnames){
							 List<PFILEBean> Objects=PFILE.gePkgFiles(this.cmd.getPkgID(), dbname);
							 Connection con=null;
							 try{
								  con =SqlServer.getInstance().getConnection(this.node.getIp(), this.node.getDbport(), dbuser, dbpasswd,dbname,this.node.getDbType());
								  for(PFILEBean pfile:Objects){
									  String ddl=this.backUPDbObject(con, pfile.getDbType(), pfile.getObjName(),this.node.getDbType());
									  if(!StringUtil.isNullOrEmpty(ddl)){
										  this.ddl2LoaclFile(pfile.getStepID(), pfile.getObjName(), ddl);
									  }
									  if(!PFILEBean.DbType.PROC.name().equals(pfile.getDbType())){
										  this.backUPDbObjectData(bkConection, pfile.getDbOwner(), pfile.getObjName(),this.node.getDbType());
									  }
								  }
							 }
							 catch(Exception e){
								 Logger.getInstance().error("DBBackupAction.execute()�������ݿ⡾"+dbname+"�����������쳣��"+e.toString());
								 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
								 this.resultInfo=e.toString();
								 status=COMMANDBean.Status.ReturnFailed;
								 ret=false;
							 }finally{
								 if(con!=null){
									 con.close();
								 }
							 }
						 }
					 }catch(Exception e){
						 Logger.getInstance().error("DBBackupAction.execute()�������ݿ⡾"+backDbName+"�������쳣��"+e.toString());
						 this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
						 this.resultInfo=e.toString();
						 status=COMMANDBean.Status.ReturnFailed;
						 ret=false;
					 }finally{
						 if(bkConection!=null){
							 bkConection.close();
						 }
						 tansLoger.setEndTime();
					 }
					 
					this.result=LocalAction.Status.ReturnOK.ordinal()+"";
					this.resultInfo="�ɹ��������ݿ⡣���ж��屸�ݵ���"+this.backPath+",���ݱ��ݵ���"+this.node.getIp()+"��"+backDbName+"��";
					status=COMMANDBean.Status.ReturnOK;
					ret=true;
			}
			catch(Exception exp){
				this.result=LocalAction.Status.ReturnFailed.ordinal()+"";
				this.resultInfo=exp.toString();
				status=COMMANDBean.Status.ReturnFailed;
				ret=false;
			}
		      //��¼����������־
			String path=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+this.cmd.getPkgID()+File.separator+system.getName()+"_installdb.log";
			Logger.getInstance().logDBInstall(path,fileOperation,this.resultInfo);    
			 this.cmd.setExecuteInfo(this.result, this.resultInfo, COMMANDBean.Remind.No.ordinal()+"");
		    return ret;
	}
	
	private String backUPDbObject(Connection con,String objType,String objName,String dbType){
		String objText="";
		if(PFILEBean.DbType.PROC.name().equals(objType)){
			 objText=DataHelper.getDbProcText(con, objName,dbType);
		 }
		else{
			objText=DataHelper.getDbTableText(con, objName,dbType);
		}
		return objText;
	}
	
	private void ddl2LoaclFile(String stepID,String objName,String content){
		STEPBean step=STEP.getPkgSteps(this.cmd.getPkgID(), stepID);
		String dir=step.getName();
		dir=StringUtil.ltrim(dir, "/");
		dir=dir.replaceAll("/", File.separator);
		String fileName=objName+".sql";
		String dirPath=this.backPath+dir;
		File dirObj=new File(dirPath);
		if(!dirObj.exists()){
			dirObj.mkdirs();
		}
		FileUtils.writeFile(dirObj.getAbsolutePath()+File.separator+fileName, content);
		
	}
	private boolean  backUPDbObjectData(Connection con,String dbOwner,String objName,String dbtype){
		String backTableName=this.cmd.getPkgID()+"_"+objName;
		String sql="";//
		if((NODEBean.DB.SqlServer.ordinal()+"").equals(dbtype)){
			sql="select * into @backTableName from @dbOwner.dbo.@objName";
		}else{
			sql="create table  @backTableName as  select *  from @dbOwner.@objName";
		}
		sql=sql.replace("@backTableName", backTableName);
		sql=sql.replace("@objName", objName);
		sql=sql.replace("@dbOwner", dbOwner);
		return DataHelper.backDbTableData(con, sql);
	}
	
/*	public static void main(String[] args){
		try{
			String dbname="run";
			 String backupDbname=dbname+"_"+DateUtil.getCurrentDate("yyyyMMdd");
			 String backupPath="D:\\Test"+File.separator+backupDbname+".bak";
			 String bakSQL = "backup database "+dbname+" to disk=? with init";// SQL���  
			 PreparedStatement ps=SqlServerUtils.getInstance().createConnect("172.24.183.111","1433","sa", "1", "run").prepareStatement(bakSQL);
			 ps.setString(1, backupPath);// path�����Ǿ���·��  
			 ps.execute(); // �������ݿ�  
			 ps.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
}
