package business.deploy.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CONFIG;
import model.PFILE;
import model.PKG;
import model.STEP;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import bean.ConfigBean;
import bean.DIRBean;
import bean.PFILEBean;
import bean.PKGBean;
import bean.STEPBean;
import business.deploy.figures.StepView;

import resource.Context;
import resource.Logger;
import resource.Paths;
import utils.DateUtil;
import utils.FileUtils;
import utils.StringUtil;
import utils.Zipper;
import views.AppView;

public class PKGOperator extends Thread{
   private String pkgPath;
   private String pkgName;
   public String app;
   public boolean isDone=false;
   public PKGOperator(String path,String name){
	   this.pkgPath=path;
	   this.pkgName=FileUtils.getFileNameNoSuffix(name);
   }
   
	public void run(){
		try{
			isDone=false;
			this.unPackage();
			isDone=true;
		}catch(Exception e){
			Logger.getInstance().error("PKGOperator.run()线程执行异常："+e.toString());
		}
   }
   private void unPackage(){
	   String workDir=Paths.getInstance().getWorkDir();
	   String tgtDir=FileUtils.formatPath(workDir)+File.separator+this.pkgName;
	   File PkgFile=new File(tgtDir);
	   if(PkgFile.exists())
		   PkgFile.delete();
	   //对应版本的数据也需要清理
	   Zipper.getInstance().unPackage(this.pkgPath, workDir);
	   //取对应的配置文件
	   String cfgFile=null;
	   PkgFile=new File(tgtDir);
	   if(PkgFile.exists()){
		   File[] files=PkgFile.listFiles();
		   for(File file:files){
			   if(file.isFile()&&file.getName().endsWith("_play.xml")){
				   cfgFile=file.getAbsolutePath();
				   break;
			   }
		   }
	   }
	   if(!StringUtil.isNullOrEmpty(cfgFile)){
		   this.resolveCfg(cfgFile);
	   }else{
		   //无法处理的版本包
	   }
	   //进行配置文件的变量替换
	   List<File> files=new ArrayList<File>();
	   FileUtils.getFileList(files, tgtDir);
	   if(files!=null&&files.size()>0){
		   //对配置项目按文件名进行分组
		   List<ConfigBean> configs=CONFIG.getConfigs(app, Context.session.userID);
		   Map<String,List<ConfigBean>> cfgMap=new HashMap<String, List<ConfigBean>>();
		   if(configs!=null&&configs.size()>0){
			   for(ConfigBean cfg:configs){
				   String file=cfg.getFileName();
				   if(cfgMap.containsKey(file)){
					   List<ConfigBean> cfgs=cfgMap.get(file);
					   cfgs.add(cfg);
					   cfgMap.put(file, cfgs);
				   }else{
					   List<ConfigBean> cfgs=new ArrayList<ConfigBean>();
					   cfgs.add(cfg);
					   cfgMap.put(file, cfgs);
				   }
			   }
		   }
		   //以文件名为单位进行实际替换
		   for(File file:files){
			   if(cfgMap.containsKey(file.getName())){
				   List<String> fileLines=FileUtils.getFileLineList(file.getAbsolutePath());
				   List<String> newFileLines=new ArrayList();
				   boolean meet=false;
				   if(fileLines!=null&&fileLines.size()>0){
					   for(String line:fileLines){
						   List<ConfigBean> cfgs=cfgMap.get(file.getName());
						   for(ConfigBean cfg:cfgs){
							   if(line.indexOf(cfg.getParamName())!=-1){
								   meet=true;
								   line=line.replace(cfg.getParamName(), cfg.getParamVal());
							   }
						   }
						   newFileLines.add(line); 
					   }
				   }
				   if(meet){
					   String dirName=file.getAbsolutePath().replace(file.getName(), "");
					   String bakFilePath=dirName+"bak_"+file.getName();
					   String newFilePath=file.getAbsolutePath();
					   File bakFile=new File(bakFilePath);
					   file.renameTo(bakFile);
					   FileUtils.writeFile(newFilePath, newFileLines);
				   }
			   }
		   }
	   }
   }
   
   
   private void  resolveCfg(String xmlFile){
	   SAXBuilder builder = new SAXBuilder();
		org.jdom.Document doc;
		try {
			    File f=new File(xmlFile);
			    String dirPath=f.getParentFile().getAbsolutePath();
			    if(f.exists()){
			    	this.cleanPkg(this.pkgName);
			    	//自动步骤
			    	STEPBean upLoadStep=new STEPBean(this.pkgName,"-1","$UPLOAD","上传版本包",STEPBean.ActionType.UploadPkg.ordinal()+"","0","0","0",Context.session.userID,DateUtil.getCurrentTime());
			    	STEP.addData(upLoadStep);
			    	doc = builder.build(xmlFile);
					Element nodePlay=doc.getRootElement();
					String id=nodePlay.getAttributeValue("id");
					String appName=nodePlay.getAttributeValue("appName");
					app=appName;
					String relationApp=nodePlay.getAttributeValue("relationApp");
					String desc=nodePlay.getAttributeValue("desc");
					PKGBean pkg=new PKGBean(id,appName,relationApp,desc,PKGBean.Status.Initial.ordinal()+"",xmlFile,"1",Context.session.userID,DateUtil.getCurrentTime());
					PKG.inrollData(pkg);
					int i=1;
					List<Element> nodeDirs=nodePlay.getChildren();
					if(nodeDirs!=null&&nodeDirs.size()>0){
						for(Element ele:nodeDirs){
							String dirId=ele.getAttributeValue("id");
							String dirname=ele.getAttributeValue("name");
							String dirfullpath=ele.getAttributeValue("fullpath");
							String diraction=ele.getAttributeValue("action");
							String dirparentid=ele.getAttributeValue("parentid");
							//只有特定目录类型的文件才能安装
							if(diraction.equals(DIRBean.Type.ExecuteDir.ordinal()+"")||
							   diraction.equals(DIRBean.Type.InstallDir.ordinal()+"")){
								String dirdesc="安装["+dirfullpath+"]";
								String backupFlag="0";
								String fileType="";
								if(diraction.equals(STEPBean.ActionType.FileCopy.ordinal()+"")){
									if(Context.autoBackupDirectory){
										backupFlag="1";
									}else{
										backupFlag="0";
									}
									fileType=PFILEBean.Type.Binary.ordinal()+"";
								}
								if(diraction.equals(STEPBean.ActionType.ScriptInstall.ordinal()+"")){
									if(Context.autoBackupDatabase){
										backupFlag="1";
									}else{
										backupFlag="0";
									}
									fileType=PFILEBean.Type.Text.ordinal()+"";
								}
								STEPBean step=new STEPBean(this.pkgName,dirId,dirfullpath,dirdesc,diraction,dirparentid,"0",backupFlag,Context.session.userID,DateUtil.getCurrentTime());
								STEP.addData(step);
								List<Element> nodeFiles=ele.getChildren();
								if(nodeFiles!=null&&nodeFiles.size()>0){
									for(Element element:nodeFiles){
										String filename=element.getAttributeValue("name");
										String dir=element.getAttributeValue("dir");
										String bootfalg=element.getAttributeValue("bootfalg");
										String seq=element.getAttributeValue("seq");
										String dbOwner=element.getAttributeValue("dbOwner");
										String user=element.getAttributeValue("user");
										String dbType=element.getAttributeValue("dbType");
										String objName=element.getAttributeValue("objName");
										String relDir="";
										if(!StringUtil.isNullOrEmpty(dir)){
											relDir=dir;
											relDir=relDir.replace('/', File.separatorChar);
										}
										//这里判断是文件还是目录的方法有问题
										
										String isDir="1";
										if(filename.indexOf(".")!=-1){
											isDir="0";
										}
										dirfullpath=dirfullpath.replace('/', File.separatorChar);
										String path=FileUtils.formatPath(dirPath)+dirfullpath+relDir+File.separator+filename;
										//Linux 下有些文件没有后缀仍然要按文件处理
										File testFile=new File(path);
										 if(testFile.exists()&&testFile.isFile()){
											 isDir="0";
										 }
										String md5="";
										//目录不放进去，不安装，
										if("0".equals(isDir)){
											File file=new File(path);
											if(file.exists()){
												md5=FileUtils.getMd5ByFile(file);
											}
											
											PFILEBean pfile=new PFILEBean((i+""),filename,path,bootfalg,seq,this.pkgName,dirId,md5,fileType,dbOwner,user,dbType,objName,dir,isDir,Context.session.userID,DateUtil.getCurrentTime());
											PFILE.addData(pfile);
											i++;
										}
									}
								}
						    }
						}
					}
					AppView.getInstance().getDisplay().asyncExec(new Runnable() {
						public void run() {
							StepView.getInstance(null).refreshVersionTree(pkgName);
						}
					});
			    }
		}
		 catch (Exception e) {
			 Logger.getInstance().error("PKGOperator.resolveCfg()处理文件["+xmlFile+"]异常："+e.toString());
		 }
   }
   
   private void cleanPkg(String versionID){
	    PKG.deletePkg(versionID);
	    STEP.deleteByPkg(versionID);
	    PFILE.deleteByPkg(versionID);
		
	    /*updater="DELETE FROM NODE_STEP WHERE PKG_ID='@PKG_ID'";
		updater=updater.replace("@PKG_ID",versionID);
		LocalDBUtil.update(updater);	
		updater="DELETE FROM COMMAND WHERE VERSIONID='@PKG_ID'";
		updater=updater.replace("@PKG_ID",versionID);
		LocalDBUtil.update(updater);	*/
   }
   
}
