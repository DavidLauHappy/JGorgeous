package business.admin.core;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import model.COMPONENT;
import model.DATAFLAG;
import model.NODE;
import model.SYSTEM;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;


import resource.Context;
import resource.Constants;
import resource.Dictionary;
import resource.Item;
import utils.StringUtil;
import views.AppView;
import bean.COMPONENTBean;
import bean.DATAFLAGBean;
import bean.NODEBean;
import bean.SYSTEMBean;
import bean.Triple;

public class HttpLoader implements Runnable{
	
/*	public static void main(String[] args){
		String Obj = "BUSINESS";
		Map<String, String> data = new TreeMap<String, String>();  
		data.put("page", "1");
		data.put("pageSize", "10");
		EasyOpsAPI eoa = new EasyOpsAPI("500487939150874db6876d8b", "f4ae7b7a200605c5efe2a91512b8a2d2c62278107fd1d691ba5aaec8b3e57a40", "http://172.24.143.143");
		String res = eoa.doApi("/cmdb/object/instance/list/" + Obj, data);
		JSONObject jsonObject = JSONObject.fromObject(res);
        String code=jsonObject.getString("code");
        if("0".equals(code)){
        	int total=jsonObject.getJSONObject("data").getInt("total");
        	if(total>0){
        		Map<String, String> param = new TreeMap<String, String>();  
        		param.put("page", "1");
        		param.put("pageSize", total+"");
        		String result = eoa.doApi("/cmdb/object/instance/list/" + Obj, param);
        		//System.out.println(result);
        		JSONObject jsonData = JSONObject.fromObject(result);
        		JSONArray dataList=  jsonData.getJSONObject("data").getJSONArray("list");
	       		 for(int w=0;w<dataList.size();w++){
	       	        	JSONObject   jsonItem = dataList.getJSONObject(w);
	       	        	String id= jsonItem.getJSONObject("_id").getString("$id");
	       	        	String name=jsonItem.getString("name");
	       	        	String abbreviation=jsonItem.getString("abbreviation");
	       	        	System.err.println("insert into APP_SYSTEM(APP,SYS,SYSNAME,IS_ENABLE) values('"+abbreviation+"','"+abbreviation+"','"+name+"','0');");
	       		 }
        	}
        }
	}*/
	
	public  void run(){
		try{
			AppView.getInstance().getDisplay().asyncExec(new Runnable() {
				public void run() {
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("从CMDB加载存量应用节点信息时间略长，请耐心等待");
					box.open();
				}
			});
			DATAFLAGBean dataflag=DATAFLAG.getByID(Constants.dataFlagCmdb);
		 	String flag="";
		 	if(dataflag==null){
		 		flag="A";
		 		DATAFLAG.addDataFlag(Constants.dataFlagCmdb, flag);
		 	}else{
		 		flag=dataflag.getFlag();
		 		if("A".equals(flag))
		 			flag="B";
		 		else
		 			flag="A";
		 	}
		 	//清理AB表数据
		 	SYSTEM.deleteByFlag(flag);
		 	COMPONENT.deleteByFlag(flag);
		 	NODE.deleteByFlag(flag);
			List<Item> Apps=Dictionary.getDictionaryList("APP");
			for(Item app:Apps){
				List<String> sys=SYSTEM.getAppSystem(app.getKey());
				getAppDeploy(app.getKey(),sys,flag);
			}
			DATAFLAG.setFlag(Constants.dataFlagCmdb, flag);
			AppView.getInstance().getDisplay().asyncExec(new Runnable() {
				public void run() {
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage("CMDB最新的节点信息加载完成，可根据应用进行下拉选择");
					box.open();
				}
			});
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

    private static void getAppDeploy(String appID,List<String> allSys,String flag){
	    	String Obj = "BUSINESS";
			Map<String, String> data = new TreeMap<String, String>();  
			data.put("page", "1");
			data.put("pageSize", "10");
			EasyOpsAPI eoa = new EasyOpsAPI(Context.cmdbAcessKey, Context.cmdbSecretKey, Context.cmdbUrl);
			String res = eoa.doApi("/cmdb/object/instance/list/" + Obj, data);
			JSONObject jsonObject = JSONObject.fromObject(res);
	        String code=jsonObject.getString("code");
	        if("0".equals(code)){
	        	int total=jsonObject.getJSONObject("data").getInt("total");
	        	if(total>0){
	        		Map<String, String> param = new TreeMap<String, String>();  
	        		param.put("page", "1");
	        		param.put("pageSize", total+"");
	        		String result = eoa.doApi("/cmdb/object/instance/list/" + Obj, param);
	        		JSONObject jsonData = JSONObject.fromObject(result);
	        		JSONArray dataList=  jsonData.getJSONObject("data").getJSONArray("list");
	        		 for(int w=0;w<dataList.size();w++){
	        	        	JSONObject   jsonItem = dataList.getJSONObject(w);
	        	        	String id= jsonItem.getJSONObject("_id").getString("$id");
	        	        	String abbreviation=jsonItem.getString("abbreviation");
	        	        	if(allSys.contains(abbreviation)){
	        	        		String name=jsonItem.getString("name");
		        	        	String businessId=jsonItem.getString("businessId");
		        	        	String instanceId=jsonItem.getString("instanceId");
		        	        	String businessClafy=" ";
		        	        	if(jsonItem.containsKey("businessClafy")){
		        	        		 businessClafy=jsonItem.getJSONArray("businessClafy").getJSONObject(0).getString("name");
		        	        	}
		        	        	String status=jsonItem.getString("status");
		        	        	SYSTEMBean bean=new SYSTEMBean(id,name,abbreviation,appID,businessClafy,businessId,status, Context.session.userID,flag);
		        	        	SYSTEM.addDataFlag(bean);
		        	        	getComponet(businessId,flag);
	        	        }
	        	    }
	        	}
	        }
	      
	    }
	    
	    private static void getComponet(String bussID,String flag){
	    	String Obj = "APP";  
	    	EasyOpsAPI eoa = new EasyOpsAPI(Context.cmdbAcessKey, Context.cmdbSecretKey, Context.cmdbUrl);
	    	Map<String, String> data = new TreeMap<String, String>();  
			data.put("page", "1");
			data.put("pageSize", "10");
			data.put("businesses:businessId$eq", Base64.encodeBase64String(bussID.getBytes()));   
			String res = eoa.doApi("/cmdb/object/instance/list/" + Obj, data);
			JSONObject jsonObject = JSONObject.fromObject(res);
	        String code=jsonObject.getString("code");
	        if("0".equals(code)){
	        	int total=jsonObject.getJSONObject("data").getInt("total");
	        	if(total>0){
	        		Map<String, String> param = new TreeMap<String, String>();  
	        		param.put("page", "1");
	        		param.put("pageSize", total+"");
	        		param.put("businesses:businessId$eq", Base64.encodeBase64String(bussID.getBytes()));   
	        		String result = eoa.doApi("/cmdb/object/instance/list/" + Obj, param);
	        		/*if(bussID.equals("c5417e2a09fa15e5a1b1dbd825158fd1"))
	        			System.err.println(result);*/
	        		jsonObject=JSONObject.fromObject(result);
	       		    code=jsonObject.getString("code");
		  	       if("0".equals(code)){
		  	    		 JSONArray dataList=  jsonObject.getJSONObject("data").getJSONArray("list");
		  	    		 for(int w=0;w<dataList.size();w++){
		       	           JSONObject   jsonItem = dataList.getJSONObject(w);
		       	       	   String id= jsonItem.getJSONObject("_id").getString("$id");
		       	       	   String name=jsonItem.getString("name");
		       	       	   String abbreviation=jsonItem.getString("abbreviation");
		       	       	   String status="";//jsonItem.getString("status");
		       	       	   String type="";
		       	       	   if(jsonItem.containsKey("com_type")){
		       	       		   type=jsonItem.getString("com_type");
		       	       	   }
		       	       	COMPONENTBean bean=new COMPONENTBean(id, name, abbreviation, type, bussID, status, Context.session.userID,flag);
		       	       	COMPONENT.addDataFlag(bean);
		       	       	    JSONArray clusterList=jsonItem.getJSONArray("clusters");
		       	       	    for(int n=0;n<clusterList.size();n++){
		       	       	    	JSONObject  jsonCluster=clusterList.getJSONObject(n);
		       	       	    	String cluster=jsonCluster.getString("name");
		       	       	        if( jsonCluster.containsKey("deviceList")){
		       	       	        	try{
				       	       	    		JSONArray deviceList=jsonCluster.getJSONArray("deviceList");
						    	      	     for(int i=0;i<deviceList.size();i++){
						    	      	    	 JSONObject jsonDevice= deviceList.getJSONObject(i);
						    	      	    	 String hostname=jsonDevice.getString("hostname");
						    	      	    	 String ip=jsonDevice.getString("ip");
						    	      	    	 String instanceId=jsonDevice.getString("instanceId");
						    	      	    	 //instanceId
						    	      	    	 String nodeStatus=jsonDevice.getString("status");
						    	      	    	 String deviceId=jsonDevice.getString("deviceId");
						    	      	    	 String osVersion=getNodePlatform(deviceId);
						    	      	    	 if(!StringUtil.isNullOrEmpty(osVersion)){
						    	      	    		 if(osVersion.toUpperCase().contains("WINDOWS")){
						    	      	    			osVersion="2";
						    	      	    		 }
						    	      	    		 else if(osVersion.toUpperCase().contains("LINUX")){
						    	      	    			osVersion="1";
						    	      	    		 }else{
						    	      	    			osVersion="0";
						    	      	    		 }
						    	      	    	 }else{
						    	      	    		osVersion="0";
						    	      	    	 }
						    	      	    	 String nodeID=bean.getId()+"_"+deviceId;
						    	      	    	NODEBean node=new NODEBean(nodeID,ip,hostname,osVersion,cluster,bean.getId(),bussID, Context.session.userID,flag);
						    	      	    	NODE.addData(node);
						    	      	     }
			       	       	          }catch(Exception e){
			       	       	        	  
			       	       	          }
			       	       	       }
		       	       	    }		       	        
		  	    		}
		  	       }
	        	}
	        }
	    }
	    
	    private  static String  getNodePlatform(String deviceId){
	    	String osVersion="";
	    	if(StringUtil.isNullOrEmpty(deviceId))
	    		return osVersion="";
	    	String Obj = "HOST";
	    	//clear data before call
			Map<String, String> data = new TreeMap<String, String>();  
			data.put("page", "1");
			data.put("pageSize", "10");
			
			data.put("deviceId$eq", Base64.encodeBase64String(deviceId.getBytes()));   
			EasyOpsAPI eoa = new EasyOpsAPI(Context.cmdbAcessKey, Context.cmdbSecretKey, Context.cmdbUrl);
			String res = eoa.doApi("/cmdb/object/instance/list/" + Obj, data);
			JSONObject jsonObject = JSONObject.fromObject(res);
	        String code=jsonObject.getString("code");
	        if("0".equals(code)){
	        	     JSONArray dataList=  jsonObject.getJSONObject("data").getJSONArray("list");
	        	     if(dataList!=null&&dataList.size()>0){
	        	    	 JSONObject   jsonItem = dataList.getJSONObject(0);
	        	    	    if(jsonItem.containsKey("osVersion")){
	        	    	    	osVersion=jsonItem.getString("osVersion");
	        	    	    }
	        	     }
	        }
	        return osVersion;
	    }
}
