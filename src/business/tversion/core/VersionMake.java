package business.tversion.core;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import bean.DIRBean;
import business.tversion.view.MakeView;
import resource.ComOrder;
import resource.Logger;
import resource.Paths;
import utils.DateUtil;
import utils.FileUtils;
import utils.StringUtil;

/**
 * @author DavidLau
 * @date 2016-8-12
 * @version 1.0
 * 类说明
 */
public class VersionMake {
	
	public MakeView view;
	private String pkgPath="";
	public String getPKGPath(){
		return this.pkgPath;
	}
   private String tempPkgPath;
   public String getTmpPkgPath(){
	   return tempPkgPath;
   }
	
   public String currentVersionID;
	public VersionMake(){}
	
	public boolean makeVersion(String appName,String desc,String relations,Map<String, TreeItem> data,String oldversionID){
		String path="";
		try{
		String versionID=oldversionID;
		if(StringUtil.isNullOrEmpty(versionID)){
			versionID=appName+"_"+"v"+DateUtil.getCurrentDate("yyyyMMddHHmm");
		}
		currentVersionID=versionID;
		String dirPath=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+versionID;
		this.tempPkgPath=dirPath;
		File versionDir=new File(dirPath);
		if(!versionDir.exists()){
			versionDir.mkdirs();
		}
	     path=dirPath+File.separator+versionID+"_play.xml";
		File file = new File(path);// 创建一个xml文件  
		if(file.exists()){
			file.delete();
		}
		SAXBuilder sb = new SAXBuilder();// 构建一个JDOM文档输入流  
        	  Element  rootPlay=new Element("play");
			  rootPlay.setAttribute("id", versionID);
			  rootPlay.setAttribute("appName", appName);
			  rootPlay.setAttribute("relationApp", relations);
			  rootPlay.setAttribute("desc", desc);
			  Map<String,TreeItem> dirs=data;
				if(dirs!=null&&dirs.size()>0){
					for(String key:dirs.keySet()){
						 TreeItem item=dirs.get(key);
						 DIRBean dir=(DIRBean)item.getData();
						 TreeItem[] fileItems=item.getItems();
						 ComOrder Order=new ComOrder();
						 if(fileItems!=null&&fileItems.length>0){
							 Element Dir = new Element("dir");
							 Dir.setAttribute("id",dir.getId());
							 Dir.setAttribute("name",dir.getName());
							 Dir.setAttribute("fullpath",dir.getFullPath());
							 Dir.setAttribute("action",dir.getType());
							 Dir.setAttribute("parentid",dir.getParentID());
							 //实体文件拷贝和目录创建
							 String curDir=dirPath+dir.getFullPath();
							 File dirAbPath=new File(curDir);
							if(!dirAbPath.exists()){
								dirAbPath.mkdirs();
							}
							 for(int w=0;w<fileItems.length;w++){
								 iterationFloderTree(fileItems[w],curDir,Dir,dir,Order);
							 }
							 rootPlay.addContent(Dir);
						 }
					}
				}
        	Document doc = new Document();// 创建文本对象  
            doc.addContent(rootPlay);// 添加树倒文本中  
            FileOutputStream  file_out=new FileOutputStream(path,true);
			XMLOutputter XMLOut = new XMLOutputter(Format.getPrettyFormat());
			XMLOut.output(doc, file_out);
			file_out.close();
			String zipFileName=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+versionID+".zip";
			this.pkgPath=zipFileName;
			Logger.getInstance().log("开始生成版本包["+zipFileName+"]……");
			CountDownLatch latch=new CountDownLatch(1);
			PackageProcessor processor=new PackageProcessor(latch,zipFileName, dirPath);
			processor.start();
			latch.await();
			boolean result=processor.isResult();
			return result;
        }
        catch(Exception e){
        	Logger.getInstance().error("VersionMake.makeVersion()生产版本配置文件["+path+"]异常："+e.toString());
        	e.printStackTrace();
        	return false;
        }
	}
	
	private void  iterationFloderTree(TreeItem root,String path,Element Dir,DIRBean component,ComOrder order){
		boolean isDir=false;
		TreeItem[] items=root.getItems();
		File vfile=(File)root.getData();
		if(items!=null&&items.length>0){
			isDir=true;
			path=path+File.separator+vfile.getName();
			 File dir=new File(path);
			if(!dir.exists()){
				dir.mkdirs();
			}
			this.makeXmlNode(root, vfile, Dir, component, order);
			for(TreeItem item:items){
				iterationFloderTree(item,path,Dir,component,order);
			}
		}else{//就只有文件或者空目录
			this.makeXmlNode(root, vfile, Dir, component, order);
		}
		if(!isDir){
			FileUtils.moveOrCopy(vfile.getAbsolutePath(), path, FileUtils.FileOperatorType.Copy);
		}
	}
	
	private void makeXmlNode(TreeItem root,File vfile,Element Dir,DIRBean component,ComOrder order){
		   String fileDir=vfile.getAbsolutePath();
		   fileDir=fileDir.replace(vfile.getName(), "");
		   String match=component.getFullPath();
		   match=match.replace('/', File.separatorChar);
		   //根据组件名称和打包文件目录获取相对父目录地址
		   int index=fileDir.indexOf(match);
		   if(index!=-1){
			   fileDir=fileDir.substring(index);
			   fileDir=fileDir.replace(match, "");
		   }else{
			   fileDir="";
		   }
		   fileDir=fileDir.replace( File.separatorChar, '/');
		   fileDir=StringUtil.rtrim(fileDir, "/");
			int flag=0;
				if(root.getData("Boot")!=null){
					flag=(Integer)root.getData("Boot");
				}
				String user="";
				String dbType="";
				String objName="";
				String dbOwner="";
				String action=component.getType();
				if((DIRBean.Type.ExecuteDir.ordinal()+"").equals(action)){
					dbOwner=component.getName();
					if("DB".equals(dbOwner)){
						dbOwner="run";//sqlServer是这样默认的
					}
				    String fullName=vfile.getName();
				    String[] cols=fullName.split("\\.");
				    if(cols!=null&&cols.length>=4){
				    	dbType=cols[0];
				    	user=cols[1];
				    	objName=cols[2];
				    }
				    else {
				    		objName=fullName;
				    		objName=objName.replace(".sql", "");
				    		user="anonymous";
				    		dbType="PROC";
				    }
				}
				Element filed = new Element("file");
				filed.setAttribute("name", vfile.getName());
				filed.setAttribute("dir", fileDir);
				filed.setAttribute("bootfalg", flag+"");
				filed.setAttribute("seq", order.getOrder());
				filed.setAttribute("dbOwner", dbOwner);
				filed.setAttribute("user", user);
				filed.setAttribute("dbType", dbType);
				filed.setAttribute("objName", objName);
				Dir.addContent(filed);
				order.inc();
	}
	
	
}
