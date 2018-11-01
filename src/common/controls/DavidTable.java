package common.controls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import utils.LayoutUtils;


public class DavidTable extends Composite{
	Composite parent,content;
	private boolean editable=false;
    private Table table=null;
	public int defaultRows=0;
	public DavidTable(Composite parent,boolean editable,int defaultRows){
		  super(parent,SWT.BORDER);
		  this.parent=parent;
		  this.content=this;
		  this.editable=editable;
		  this.defaultRows=defaultRows;
	}
	
	private String[] header=null;
	private String[] columns=null;
	public void setHeader(String[] header,String[] columns){
		this.header=header;
		this.columns=columns;
	}
	
	public void setHeader(String[] header,String json){
		this.header=header;
		 List<JSONObject>  jsonObjects=JSONArray.fromObject(json);
		 if(jsonObjects!=null){
			 data=new ArrayList<LinkedHashMap<String,String>>();
			 for(int w=0;w<jsonObjects.size();w++){
				 JSONObject  jsonObject=jsonObjects.get(w);
				 if(this.columns==null){
						 this.columns=new String[jsonObject.keySet().size()];
					 Iterator it=jsonObject.keys();
					 int index=0;
					 while(it.hasNext()){
						 columns[index]=it.next().toString();
						 index++;
					 }
				 }
				 LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
				 for(String name:this.columns){
					 String value=jsonObject.getString(name);
					  map.put(name, value);
				 }
				 data.add(map);
			 }
		 }
	}
	
	//LinkedHashMap
	private List<LinkedHashMap<String, String>> data=null;
	
	public void show(){
		this.createAndShow();
		this.content.pack();
	}
	
	private void createAndShow(){
		content.setLayout(LayoutUtils.getComGridLayout(1, 5)); 
		table=new Table(content,SWT.SINGLE);
		table.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		//table.addSelectionListener(new SetSelectionAction());
		table.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		if(this.header!=null&&this.header.length>0){
			  for(int i=0;i<header.length;i++){
					TableColumn tablecolumn=new TableColumn(table,SWT.BORDER);
					tablecolumn.setText(header[i]);
				}
			  if(this.data!=null&&this.data.size()>0){
				  int line=0;
				  for(LinkedHashMap<String, String> map:this.data){
					  String[] vals=new String[map.size()];
					  int index=0;
					  for(String column:this.columns){
						  vals[index]=map.get(column);
						  index++;
					  }
					TableItem tableItem=new TableItem(table,SWT.BORDER);
					tableItem.setText(vals);
					  if(this.editable){
						  for(int w=0;w<columns.length;w++){
							  final TableEditor editor=new TableEditor(table);
							  final Text text=new Text(table,SWT.NONE);
							  text.setText(vals[w]);
							  editor.grabHorizontal=true;
							  editor.setEditor(text, tableItem, w);
							  text.addModifyListener(new MyModifyListener(editor,text, w,line,columns[w]));
						  }
					  }
					  line++;
				  }
			  }else{
				  for(int n=0;n<this.defaultRows;n++){
					  TableItem tableItem=new TableItem(table,SWT.BORDER);
					  if(this.editable){
						  for(int w=0;w<columns.length;w++){
							  final TableEditor editor=new TableEditor(table);
							  final Text text=new Text(table,SWT.NONE);
							  editor.grabHorizontal=true;
							  editor.setEditor(text, tableItem, w);
							  text.addModifyListener(new MyModifyListener(editor,text, w,n,columns[w]));
						  }
					  }
				  }
			  }
			  
			  for(int j=0;j<header.length;j++){		
				  table.getColumn(j).pack();
				}	
			  table.pack();
		}
	}
	
	public class MyModifyListener implements ModifyListener{
		public int index;
		public TableEditor tableEditor;
		public Text text;
		public int lineNo;
		public String column;
		public MyModifyListener(TableEditor tableEditor,Text text,int index,int lineNo,String column){
			this.tableEditor=tableEditor;
			this.index=index;
			this.text=text;
			this.lineNo=lineNo;
			this.column=column;
		}
		public void modifyText(ModifyEvent arg) {
			tableEditor.getItem().setText(index,text.getText());
			//更新值
			updateData(this.lineNo,column,text.getText());
			//如果表的行记录数超过默认值，要自动新家
			if(this.lineNo>=defaultRows-1){
				defaultRows++;
				 String[] vals=new String[columns.length];
				TableItem tableItem=new TableItem(table,SWT.BORDER);
				tableItem.setText(vals);
				  for(int w=0;w<columns.length;w++){
					  final TableEditor editor=new TableEditor(table);
					  final Text text=new Text(table,SWT.NONE);
					  editor.grabHorizontal=true;
					  editor.setEditor(text, tableItem, w);
					  text.addModifyListener(new MyModifyListener(editor,text, w,lineNo+1,columns[w]));
				  }
			}
			
		}
	}
	
	//
	public void updateData(int line,String key,String value){
		LinkedHashMap<String,String> map=null;
		if(this.data!=null&&this.data.size()>line){
			 map=this.data.get(line);
			 if(map==null){
				 map=new LinkedHashMap<String,String>();
			 } 
			 map.put(key, value);
			 this.data.set(line, map);
		}else{
			if(this.data==null)
				this.data=new ArrayList<LinkedHashMap<String, String>>();
			map=new LinkedHashMap<String,String>();
			map.put(key, value);
			 this.data.add(line, map);
		}
	}
	
	public String getData(){
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		if(this.data!=null&&this.data.size()>0){
			for(int w=0;w<this.data.size();w++){
				JSONObject json=new JSONObject();
				LinkedHashMap<String,String> map=this.data.get(w);
				for(String key:map.keySet()){
					json.put(key, map.get(key));
				}
				jsonObjects.add(json);
			}
		}
		return jsonObjects.toString();
	}
}
