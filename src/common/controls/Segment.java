package common.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import utils.LayoutUtils;
import utils.StringUtil;

public class Segment extends Composite{
	Composite parent,content;
	private boolean editable=false;
	public Segment(Composite parent,boolean editable){
		  super(parent,SWT.BORDER);
		  this.parent=parent;
		  this.editable=editable;
		  content=this;
	}
	
	private int left=1;
	private int right=2;
	public Segment(Composite parent,boolean editable ,int left ,int rigth){
		  super(parent,SWT.BORDER);
		  this.parent=parent;
		  this.editable=editable;
		  this.left=left;
			this.right=right;
		  content=this;

	}

	public void show(){
		this.createView();
		this.pack();
	}
	public Map<String,String> labelList=new LinkedHashMap<String, String>();
	 public Map<String,Object> textMap=new LinkedHashMap<String, Object>();
	 private void createView(){
		 this.setLayout(LayoutUtils.getComGridLayout(this.left+this.right, 0));
		 if(this.labels!=null&&this.labels.length>0){
			 for(String label:labels){
				 Label labelControl =new Label(content,SWT.NONE);
				 labelControl.setText(label);
				 labelControl.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, this.left, 1, 0, 0));
				 String key=this.labelList.get(label);
				 Text text=new Text(content,SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);//SWT.V_SCROLL|
				 text.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, this.right, 1, 0, 0));
				 String value=(String)this.textMap.get(key);
				 text.setText(value);
				 if(this.editable){
					   textMap.put(key, text);
				 }else{
					 text.setEditable(false);
				 }
			 }
		 }
	 }
	 


	 public String[] labels=null;
	 public void setDatas(String[] labels,String jsonData){
		 this.labels=labels;
		 if(!StringUtil.isNullOrEmpty(jsonData)){
			 List<JSONObject> jsonObjects=JSONArray.fromObject(jsonData);
			 if(jsonObjects!=null&&jsonObjects.size()>0){
				 for(int w=0;w<jsonObjects.size();w++){
					 JSONObject   jsonItem = jsonObjects.get(w);
					 String name=jsonItem.getString("name");
					 String value=jsonItem.getString("value");
					 textMap.put(name, value);
					 labelList.put(this.labels[w], name);
				 }
			 }
		 }
	 }
	 
	 public String getData(){
		 List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		 if(this.textMap.size()>0){
			 for(String name:textMap.keySet()){
				 JSONObject jsonObj=new JSONObject();
				 jsonObj.put("name", name);
				 String val="";
				 if(textMap.get(name)!=null){
					 try{
						 Text text=(Text)textMap.get(name);
						 val=text.getText();
					 }catch(Exception e){
						 val=(String)textMap.get(name);
					 }
				 }
				 jsonObj.put("value", val);
				 jsonObjects.add(jsonObj);
			 }
		 }
		return jsonObjects.toString();
	 }
}
