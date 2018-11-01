package business.tester.view;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.STREAM;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bean.StreamBean;
import bean.ViewFileBean;


import resource.Constants;
import resource.Icons;
import resource.Paths;
import resource.TQuickDeployView;
import utils.DateUtil;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;


public class TesterStreamInfoView {
	public Composite self=null;
	public SashForm content=null;
	public Composite parent=null;
	private Text textName,textDesc,textInput;
	public Table fileTable=null;
	private  String[] header=null;
	private StreamBean Data;
	Map<String,ViewFileBean> StreamFiles;
	public TesterStreamInfoView(Composite com,StreamBean data){
		 this.parent=com;
		 this.Data=data;
		 self=new Composite(com,SWT.NONE);
		 self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 self.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 content=new SashForm(self,SWT.VERTICAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 this.createToolsView();
		 this.createListView();
		 content.setWeights(new int[]{40,60});
		 self.pack();
	}
	
	private void createToolsView(){
	  	Composite toolPanel=new Composite(content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(8, 5));
		
		Label labName=new Label(toolPanel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_name"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textName=new Text(toolPanel,SWT.BORDER);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 7, 1, 0, 0));
		textName.setText(Data.getStreamName());
		textName.setEditable(false);
		
		Label labDesc=new Label(toolPanel,SWT.NONE);
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textDesc=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 7, 1, 0, 0));
		textDesc.setText(Data.getStreamDesc());
		textDesc.setEditable(false);
		
		Label labStart=new Label(toolPanel,SWT.NONE);
		labStart.setText(Constants.getStringVaule("label_streamStart"));
		labStart.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
		Text textStart=new Text(toolPanel,SWT.BORDER);
		textStart.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		textStart.setText(Data.getStartTime());
		textStart.setEditable(false);
		
		Label labEnd=new Label(toolPanel,SWT.NONE);
		labEnd.setText(Constants.getStringVaule("label_streamEnd"));
		labEnd.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		Text textEnd=new Text(toolPanel,SWT.BORDER);
		textEnd.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		textEnd.setText(Data.getEndTime());
		textEnd.setEditable(false);
		
		textInput=new Text(toolPanel,SWT.BORDER);
		textInput.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 4, 1, 0, 0));
		Button btnSearch=new  Button(toolPanel,SWT.PUSH);
		btnSearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
		btnSearch.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnSearch.addSelectionListener(new SearchAction());
		
		Button btnDeploy=new  Button(toolPanel,SWT.PUSH);
		btnDeploy.setText("   "+Constants.getStringVaule("btn_deploy")+"   ");
		btnDeploy.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnDeploy.addSelectionListener(new QuickDeployAction());
		
		Button btnDown=new  Button(toolPanel,SWT.PUSH);
		btnDown.setText("   "+Constants.getStringVaule("btn_download")+"   ");
		btnDown.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnDown.addSelectionListener(new DownloadAction());
		

		Button btnLately=new  Button(toolPanel,SWT.PUSH);
		btnLately.setText(Constants.getStringVaule("btn_localcmp"));
		btnLately.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnLately.addSelectionListener(new CompareLocalAction());
		toolPanel.pack();
	}
	
	
	public static  Color green = AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_GREEN);  
	public static Color gray = AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_GRAY);  
	private void createListView(){
		    Composite pannelData=new Composite(content,SWT.NONE);
		    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		    fileTable=new Table(pannelData,SWT.FULL_SELECTION|SWT.MULTI);
		    fileTable.setHeaderVisible(true);
		    fileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    fileTable.setLinesVisible(true);
		    fileTable.addMouseListener(new FileItemAction());
		    fileTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		    fileTable.addListener(SWT.EraseItem, new Listener(){
				public void handleEvent(Event event){
					event.detail &= ~SWT.HOT;  
		            if ((event.detail & SWT.SELECTED) == 0) return; /* item not selected */  
		            int clientWidth = fileTable.getClientArea().width;  
		            GC gc = event.gc;  
		            Color oldForeground = gc.getForeground();  
		            Color oldBackground = gc.getBackground();  
		            gc.setForeground(green);  
		            gc.setBackground(gray);  
		            gc.fillGradientRectangle(0, event.y, clientWidth, event.height, false);  
		            gc.setForeground(oldForeground);  
		            gc.setBackground(oldBackground);  
		            event.detail &= ~SWT.SELECTED;  
				}
			});
		    
		    header=new String[]{ StringUtil.rightPad(Constants.getStringVaule("header_filename"), 80, " "),
		    								 StringUtil.rightPad(Constants.getStringVaule("header_mdfdate"),50," ") ,
		    								 StringUtil.rightPad(Constants.getStringVaule("header_crtdate"),50," ") ,
		    								 StringUtil.rightPad(Constants.getStringVaule("header_md5"),50," ") };
		    for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(fileTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
				if(i==0){
					tablecolumn.addSelectionListener(new OrderFilesNameAction());
				}
				if(i==1){
					tablecolumn.addSelectionListener(new OrderFilesMTimeAction());
				}
				if(i==2){
					tablecolumn.addSelectionListener(new OrderFilesCTimeAction());
				}
			}
			StreamFiles=new HashMap<String,ViewFileBean>();
		    List<ViewFileBean> Files=STREAM.getFiles(this.Data.getStreamID());
		    for(ViewFileBean file:Files){
		    	String fileName=file.getFileName();
		    	if(StreamFiles.containsKey(fileName)){
		    		ViewFileBean existFile=StreamFiles.get(fileName);
		    		long existTime=DateUtil.getTimeLong(existFile.getFileTime());
		    		long time=DateUtil.getTimeLong(file.getFileTime());
		    		if(time>existTime){
		    			StreamFiles.put(fileName, file);
		    		}
		    	}else{
		    		StreamFiles.put(fileName, file);
		    	}
		    }
		    for(String key:StreamFiles.keySet()){
		    	ViewFileBean file=StreamFiles.get(key);
		    	String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
				tableItem.setImage(icon);
				tableItem.setData(file);
		    }
			for(int j=0;j<header.length;j++){		
				fileTable.getColumn(j).pack();
			}	
		    pannelData.pack();
	}
	
	public List<String> getSelected(){
		List<String> result=new ArrayList<String>();
		TableItem[] items=fileTable.getSelection();
		if(items!=null&&items.length>0){
			for(TableItem item:items){
				ViewFileBean data=(ViewFileBean)item.getData();
				result.add(data.getFileName());
			}
		}
		return result;
	}
	
	public boolean orderOrignal=true;
	public class OrderFilesNameAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			List<String> selectes=getSelected();
   			 TableItem[] items=fileTable.getItems();
   			 List<ViewFileBean> datas=new ArrayList<ViewFileBean>();
   			for(int w=0;w<items.length;w++){
   				ViewFileBean data=(ViewFileBean)items[w].getData();
					datas.add(data);
   			}
   			if(orderOrignal){
	   			  Collections.sort(datas, new Comparator<ViewFileBean>() {
	   	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
	   	                  return arg0.getNameOrder().compareTo(arg1.getNameOrder());
	   	              }
	   			   });
	   			orderOrignal=false;
   			}else{
   			 Collections.sort(datas, new Comparator<ViewFileBean>() {
  	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
  	                  return arg0.getOrignalOrder().compareTo(arg1.getOrignalOrder());
  	              }
  			   });
   			orderOrignal=true;
   			}
   		  fileTable.removeAll();
   		  List<TableItem>  selectItems=new ArrayList<TableItem>();
	   		 for(ViewFileBean file:datas){
	   			String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
				tableItem.setImage(icon);
				tableItem.setData(file);
				if(selectes.contains(name))
					selectItems.add(tableItem);
	   		 }
	   		if(selectItems.size()>0){
	   			TableItem[] selections=new TableItem[selectItems.size()];
	   			fileTable.setSelection(selectItems.toArray(selections));
	   		 } 
   		}
	}
	
	public class OrderFilesMTimeAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			List<String> selectes=getSelected();
   			 TableItem[] items=fileTable.getItems();
   			 List<ViewFileBean> datas=new ArrayList<ViewFileBean>();
   			for(int w=0;w<items.length;w++){
   				ViewFileBean data=(ViewFileBean)items[w].getData();
					datas.add(data);
   			}
   			if(orderOrignal){
	   			  Collections.sort(datas, new Comparator<ViewFileBean>() {
	   	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
	   	                  return arg0.getMdfTime().compareTo(arg1.getMdfTime());
	   	              }
	   			   });
	   			orderOrignal=false;
   			}else{
   			 Collections.sort(datas, new Comparator<ViewFileBean>() {
  	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
  	                  return arg0.getOrignalOrder().compareTo(arg1.getOrignalOrder());
  	              }
  			   });
   			orderOrignal=true;
   			}
   		  fileTable.removeAll();
   		 List<TableItem> selectItems=new ArrayList<TableItem>();
	   		 for(ViewFileBean file:datas){
	   			String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
				tableItem.setImage(icon);
				tableItem.setData(file);
				if(selectes.contains(name))
					selectItems.add(tableItem);
	   		 }
	   		 if(selectItems.size()>0){
	   			TableItem[] selections=new TableItem[selectItems.size()];
	   			fileTable.setSelection(selectItems.toArray(selections));
	   		 }
   		}
	}
	
	public class OrderFilesCTimeAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			  List<String> selectes=getSelected();
   			 TableItem[] items=fileTable.getItems();
   			 List<ViewFileBean> datas=new ArrayList<ViewFileBean>();
   			for(int w=0;w<items.length;w++){
   				ViewFileBean data=(ViewFileBean)items[w].getData();
					datas.add(data);
   			}
   			if(orderOrignal){
	   			  Collections.sort(datas, new Comparator<ViewFileBean>() {
	   	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
	   	                  return arg0.getCrtTime().compareTo(arg1.getCrtTime());
	   	              }
	   			   });
	   			orderOrignal=false;
   			}else{
   			 Collections.sort(datas, new Comparator<ViewFileBean>() {
  	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
  	                  return arg0.getOrignalOrder().compareTo(arg1.getOrignalOrder());
  	              }
  			   });
   			orderOrignal=true;
   			}
   		  fileTable.removeAll();
   		  List<TableItem> sItems=new ArrayList<TableItem>();
	   		 for(ViewFileBean file:datas){
	   			String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
				tableItem.setImage(icon);
				tableItem.setData(file);
				if(selectes.contains(name))
					sItems.add(tableItem);
	   		 }
	   		 if(sItems.size()>0){
	   			TableItem[] selecteds=new TableItem[sItems.size()];
	   			fileTable.setSelection(sItems.toArray(selecteds));
	   		 }
   		}
	}
	
	public class SearchAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			String keyword=textInput.getText();
   			if(!StringUtil.isNullOrEmpty(keyword)){
   				TableItem[] items=fileTable.getItems();
   				for(int w=0;w<items.length;w++){
   					ViewFileBean data=(ViewFileBean)items[w].getData();
   					if(data.getFileName().startsWith(keyword)){
   						fileTable.setSelection(items[w]);
   					}
   				}
   			}
   		}
	}
	
	public class FileItemAction  extends MouseAdapter{
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem tableItem=fileTable.getItem(new  Point(e.x,e.y));
			 if(tableItem!=null){
				   ViewFileBean file=(ViewFileBean)tableItem.getData();
		            boolean downResult= file.downloadStream(); 
		            if(downResult){//下载成功的文件，尝试调用本地程序打开
		            	FileUtils.openFileByLocal(file.getLocation());
		   			}else{
		   				String msg="文件【"+file.getFileName()+"】下载失败。请稍后重试！";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
		   			}
			 }
		 }
	}
	
	
	
	public class QuickDeployAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			TableItem[] selections= fileTable.getSelection();
   			if(selections!=null&&selections.length>0){
   				List<ViewFileBean> tabList=new ArrayList<ViewFileBean>();
				List<ViewFileBean> initList=new ArrayList<ViewFileBean>();
				List<ViewFileBean> otherList=new ArrayList<ViewFileBean>();
   				boolean hasDbFiles=false;
   				boolean hasOFiles=false;
   				for(TableItem item:selections){
   					ViewFileBean data=(ViewFileBean)item.getData();
   					String suffix=FileUtils.getFileSuffix(data.getFileName());
   					if("SQL".equalsIgnoreCase(suffix)){
   						hasDbFiles=true;
   					}
   					if(!"SQL".equalsIgnoreCase(suffix)){
   						hasOFiles=true;
   					}
   					if(((data.getFileName()).toLowerCase()).indexOf("table")!=-1){
   						tabList.add(data);
   					}else if(((data.getFileName()).toLowerCase()).indexOf("init")!=-1){
   						initList.add(data);
   					}else{
   						otherList.add(data);
   					}
   				}
   				if(hasDbFiles&&hasOFiles){
	   				 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					 box.setText(Constants.getStringVaule("messagebox_alert"));
					 box.setMessage("快速部署不能同时选择多种类型的文件(建议一次选择一种类型的文件进行快速部署)");
					 box.open();
   				}else{
   					List<ViewFileBean> files=new ArrayList<ViewFileBean>();
   					//脚本临时分类
   					if(tabList.size()>0){
   						for(ViewFileBean data:tabList)
   							files.add(data);
   					}
   					if(initList.size()>0){
   						for(ViewFileBean data:initList)
   							files.add(data);
   					}
   					if(otherList.size()>0){
   						for(ViewFileBean data:otherList)
   							files.add(data);
   					}
   					TQuickDeployView.getInstance().show(files);
   				}
   			}else{
	   			 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 box.setText(Constants.getStringVaule("messagebox_alert"));
				 box.setMessage("请选择需要部署的文件(一次只能部署一个组件)");
				 box.open();
   			}
   		}
	}
	
	
	public class DownloadAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			TableItem[] items=fileTable.getSelection();
   			if(items==null||items.length<=0){
   					items=fileTable.getItems();
   			}
   			String localDir="";
   			 List<String> errorList=new ArrayList<String>();
				for(int w=0;w<items.length;w++){
					ViewFileBean file=(ViewFileBean)items[w].getData();
					if(StringUtil.isNullOrEmpty(localDir)){
						localDir=FileUtils.formatPath(Paths.getInstance().getWorkDir())+File.separator+file.getStreamName();
					}
		            boolean downResult=file.downloadStream(); 
				   if(!downResult){
					   errorList.add(file.getFileName());
				   }
				}
				if(errorList!=null&&errorList.size()>0){
					   String fileName="";
					    for(String line:errorList){
					    	fileName=fileName+line+"\r\n";
					    }
					    String msg="文件【"+fileName+"】下载失败。请稍后重试！";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
				}else{
					    String msg="文件下载完成。可以点击【确定】在目录【"+localDir+"】查看。";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						int choice=box.open();
						if(SWT.OK==choice){
							FileDialog dialog=new FileDialog(AppView.getInstance().getShell(),SWT.OPEN);
							dialog.setFilterPath(localDir);
							dialog.setFilterNames(new String[]{"All Files(*.*)"});
							dialog.open();
						}
				}
   		}
	}


	public class CompareLocalAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			String path=Paths.getInstance().getWorkDir();
            String localDir=FileUtils.formatPath(path)+File.separator+Data.getStreamID();
            File dir=new File(localDir);
            if(dir.exists()&&dir.isDirectory()){
            	 File[] files=dir.listFiles(new FileFilter() {
								public boolean accept(File file){
									  if(file.isFile()){
										  return true;
									  }
									  return false;
								  }
							});
            	 Map<String,String> LocalFiles=new HashMap<String, String>();
            	 if(files!=null&&files.length>0){
            		 for(File file:files){
            			 String md5=FileUtils.getMd5ByFile(file);
            			 LocalFiles.put(file.getName(), md5);
            		 }
            	 }
	            TableItem[] items=fileTable.getItems();
	            List<TableItem> diffs=new ArrayList<TableItem>();
	            if(items!=null&&items.length>0){
	            	for(TableItem item:items){
	            		ViewFileBean file=(ViewFileBean)item.getData();
	            		if(LocalFiles.containsKey(file.getFileName())){
	            			String localMd5=LocalFiles.get(file.getFileName());
	            			if(!localMd5.equals(file.getMd5())){
	            				diffs.add(item);//updated file
	            			}
	            		}else{
	            			diffs.add(item);//added file
	            		}
	            	}
	            }
	            if(diffs.size()>0){
	            	TableItem[] seletes=new TableItem[diffs.size()];
	            	fileTable.setSelection(diffs.toArray(seletes));
	            }
            }
		}
	}
	
}
