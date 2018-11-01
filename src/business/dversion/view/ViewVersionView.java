package business.dversion.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.STREAM;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.program.Program;
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
import org.eclipse.swt.widgets.TreeItem;

import resource.Constants;
import resource.Context;
import resource.FtpFileService;
import resource.Icons;
import resource.Paths;

import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;


import bean.StreamBean;
import bean.ViewFileBean;
import bean.ViewVersionBean;


public class ViewVersionView {
	public Composite self=null;
	public SashForm content=null;
	public Composite parent=null;
	private Text textName,textDesc,textInput,textStream,textVersion,textTime;
	public Table fileTable=null;
	private  String[] header=null;
	private  ViewVersionBean Data=null;
	
	public ViewVersionView(Composite com,ViewVersionBean bean){
		this.parent=com;
		this.Data=bean;
		
		 self=new Composite(com,SWT.NONE);
		 self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 self.setLayout(LayoutUtils.getComGridLayout(1, 1));;
		 
		 content=new SashForm(self,SWT.VERTICAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 this.createPropertyView();
		 this.createListView();
		 content.setWeights(new int[]{30,70});
		 self.pack();
	}
	
	private void createPropertyView(){
		Composite toolPanel=new Composite(content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(8, 5));
		
		Label labName=new Label(toolPanel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_name"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textName=new Text(toolPanel,SWT.BORDER);
		textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		textName.setText(Data.getViewName());
		textName.setEditable(false);
		
		Label labStreamName=new Label(toolPanel,SWT.NONE);
		labStreamName.setText(Constants.getStringVaule("label_streamname"));
		labStreamName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textStream=new Text(toolPanel,SWT.BORDER);
		textStream.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		StreamBean stream=STREAM.getStreamByID(Data.getStreamID());
		String streamName=stream.getStreamName();
		textStream.setText(streamName);
		textStream.setEditable(false);
		
		
		Label labVersion=new Label(toolPanel,SWT.NONE);
		labVersion.setText(Constants.getStringVaule("label_version"));
		labVersion.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textVersion=new Text(toolPanel,SWT.BORDER);
		textVersion.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		textVersion.setText(Data.getShowVersion());
		textVersion.setEditable(false);
		
		Label labTime=new Label(toolPanel,SWT.NONE);
		labTime.setText(Constants.getStringVaule("label_mdftime"));
		labTime.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textTime=new Text(toolPanel,SWT.BORDER);
		textTime.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		textTime.setText(Data.getUptTime());
		textTime.setEditable(false);
		
		Label labDesc=new Label(toolPanel,SWT.NONE);
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textDesc=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 7, 1, 0, 0));
		textDesc.setText(Data.getViewDesc());
		textDesc.setEditable(false);
		
		textInput=new Text(toolPanel,SWT.BORDER);
		textInput.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 6, 1, 0, 0));
		Button btnSearch=new  Button(toolPanel,SWT.PUSH);
		btnSearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
		btnSearch.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnSearch.addSelectionListener(new SearchAction());
		
		Button btnDown=new  Button(toolPanel,SWT.PUSH);
		btnDown.setText("   "+Constants.getStringVaule("btn_download")+"   ");
		btnDown.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnDown.addSelectionListener(new DownloadAction());
		
		toolPanel.pack();
	}
	
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
	    
	    header=new String[]{ StringUtil.rightPad(Constants.getStringVaule("header_filename"), 60, " "),
	    								 StringUtil.rightPad(Constants.getStringVaule("header_mdfdate"),25," ") ,
	    								 StringUtil.rightPad(Constants.getStringVaule("header_crtdate"),25," ") ,
	    								 StringUtil.rightPad(Constants.getStringVaule("header_md5"),50," "),
	    								 StringUtil.rightPad("",8," ")};
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
	    
	    List<ViewFileBean> Files=VIEW.getVersionFiles(Data.getViewID(),Data.getVersion());
	    for(ViewFileBean file:Files){
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
	
	public boolean orderOrignal=true;
	public class OrderFilesNameAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
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
	   		 for(ViewFileBean file:datas){
		    	String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
				tableItem.setImage(icon);
				tableItem.setData(file);
	   		 }
  			
  			
   		}
	}
	
	public class OrderFilesCTimeAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
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
 	   		 for(ViewFileBean file:datas){
 		    	String name=file.getFileName();
 		    	Image icon=Icons.getFileImage(name);
 		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
 				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
 				tableItem.setImage(icon);
 				tableItem.setData(file);
 	   		 }
   		}
	}
	
	public class OrderFilesMTimeAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			TableItem[] items=fileTable.getItems();
			 List<ViewFileBean> datas=new ArrayList<ViewFileBean>();
			for(int w=0;w<items.length;w++){
				ViewFileBean data=(ViewFileBean)items[w].getData();
					datas.add(data);
			}
			if(orderOrignal){
	   			  Collections.sort(datas, new Comparator<ViewFileBean>() {
	   	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
	   	                  return arg0.getFileTime().compareTo(arg1.getFileTime());
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
	   		 for(ViewFileBean file:datas){
		    	String name=file.getFileName();
		    	Image icon=Icons.getFileImage(name);
		    	TableItem tableItem=new TableItem(fileTable,SWT.BORDER);
				tableItem.setText(new String[]{name,file.getCrtTime(),file.getFileTime(),file.getMd5()});
				tableItem.setImage(icon);
				tableItem.setData(file);
	   		 }
   		}
	}
	
	public class FileItemAction extends MouseAdapter{
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem currentItem=fileTable.getItem(new  Point(e.x,e.y));
			 if(currentItem!=null){
				 ViewFileBean file=(ViewFileBean)currentItem.getData();
		   			String remotePath=file.getRemotePath();
		   			String path=Paths.getInstance().getWorkDir();
		            String localDir=FileUtils.formatPath(path)+File.separator+file.getStreamID()+File.separator+file.getViewID()+File.separator+file.getVersionID();
		            File dir=new File(localDir);
		            if(!dir.exists()){
		            	dir.mkdirs();
		            }
		   			boolean downResult=FtpFileService.getService().dowload(file.getFileName(), remotePath, localDir,file.getFileTime());
		   			if(downResult){//下载成功的文件，尝试调用本地程序打开
		   				String fileName=localDir+File.separator+file.getFileName();
		   			  	int dot = fileName.lastIndexOf('.');
		   			 	   if (dot != -1) {
		   			 	     String extension = fileName.substring(dot);
		   			 	     Program program = Program.findProgram(extension);
		   			 	     if (program != null){
		   			 	    	 program.launch(fileName);
		   			 	      }else{
		   			 	    	String msg="无法打开文件【"+file.getFileName()+"】请在工作目录【"+localDir+"】查看！";
		   						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
		   						box.setText(Constants.getStringVaule("messagebox_alert"));
		   						box.setMessage(msg);
		   						box.open();
		   			 	      }
		   			 	   }
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
		   			String remotePath=file.getRemotePath();
		   			String path=Paths.getInstance().getWorkDir();
		             localDir=FileUtils.formatPath(path)+File.separator+file.getStreamID()+File.separator+file.getViewID()+File.separator+file.getVersionID();
		            File dir=new File(localDir);
		            if(!dir.exists()){
		            	dir.mkdirs();
		            }
		            boolean downResult=FtpFileService.getService().dowload(file.getFileName(), remotePath, localDir,file.getFileTime());
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
}
