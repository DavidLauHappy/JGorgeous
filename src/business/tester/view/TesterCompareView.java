package business.tester.view;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bean.ViewFileBean;
import bean.ViewVersionBean;


import resource.Constants;
import resource.Icons;
import resource.Paths;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;


public class TesterCompareView {
	public Composite self=null;
	public Composite parent=null;
	public SashForm content=null;
	private Text textLeftName=null;
	private Text textRightName=null;
	private Text textLeftDesc=null;
	private Text textRightDesc=null;
	private  Combo comboLeftVersion=null;
	private  Combo comboRightVersion=null;
	private Text textLeftTime=null;
	private Text textRightTime=null;
	private Text textLeftUser=null;
	private Text textRightUser=null;
	private Table leftFileTable=null;
	private Table rightFileTable=null;
	private  String[] header=null;
	private ViewVersionBean dataLeft;
	private Map<String,ViewVersionBean> Versions;
	private Map<String,ViewFileBean> FilesMap;
	
	
	public TesterCompareView(Composite com){
		this.parent=com;
		 self=new Composite(com,SWT.BORDER);
		 self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 self.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 content=new SashForm(self,SWT.HORIZONTAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 this.createLeftView();
		 this.createRightView();
		 content.setWeights(new int[]{50,50});
		 self.pack();
	}
	
	private void createLeftView(){
		Composite pannel=new Composite(content,SWT.BORDER|SWT.V_SCROLL);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
		Composite topPannel=new Composite(pannel,SWT.BORDER|SWT.V_SCROLL);
		topPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 200));
		topPannel.setLayout(LayoutUtils.getComGridLayout(4, 1));
		
		Label labVersion=new Label(topPannel,SWT.NONE);
		labVersion.setText(Constants.getStringVaule("label_version"));
		labVersion.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		comboLeftVersion=new Combo(topPannel,SWT.DROP_DOWN);
		comboLeftVersion.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		
		Label labName=new Label(topPannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_name"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textLeftName=new Text(topPannel,SWT.BORDER);
		textLeftName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		Label labDesc=new Label(topPannel,SWT.NONE);
		
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textLeftDesc=new Text(topPannel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textLeftDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
		
		
		Label labTime=new Label(topPannel,SWT.NONE);
		labTime.setText(Constants.getStringVaule("label_mdftime"));
		labTime.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textLeftTime=new Text(topPannel,SWT.BORDER);
		textLeftTime.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		Label labUser=new Label(topPannel,SWT.NONE);
		labUser.setText(Constants.getStringVaule("label_mdfuser"));
		labUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textLeftUser=new Text(topPannel,SWT.BORDER);
		textLeftUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		topPannel.pack();
		
		leftFileTable=new Table(pannel,SWT.BORDER|SWT.FULL_SELECTION|SWT.SINGLE);
		leftFileTable.setHeaderVisible(true);
		leftFileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		leftFileTable.setLinesVisible(true);
		leftFileTable.addMouseListener(new FileItemLeftCmpAction());
		leftFileTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		    header=new String[]{ StringUtil.rightPad(Constants.getStringVaule("header_filename"), 60, " "),
		    								 StringUtil.rightPad(Constants.getStringVaule("header_mdfdate"),25," ") ,
		    								 StringUtil.rightPad(Constants.getStringVaule("header_crtdate"),25," ") };
		    for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(leftFileTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
			
			for(int j=0;j<header.length;j++){		
				leftFileTable.getColumn(j).pack();
			}	
		pannel.pack();
	}
	
	private void createRightView(){
		Composite pannel=new Composite(content,SWT.BORDER|SWT.V_SCROLL);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
		Composite topPannel=new Composite(pannel,SWT.BORDER|SWT.V_SCROLL);
		topPannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 200));
		topPannel.setLayout(LayoutUtils.getComGridLayout(4, 1));
		
		Label labVersion=new Label(topPannel,SWT.NONE);
		labVersion.setText(Constants.getStringVaule("label_version"));
		labVersion.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		comboRightVersion=new Combo(topPannel,SWT.DROP_DOWN);
		comboRightVersion.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		comboRightVersion.addSelectionListener(new SelectVersionAction());
		
		Label labName=new Label(topPannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_name"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textRightName=new Text(topPannel,SWT.BORDER);
		textRightName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		Label labDesc=new Label(topPannel,SWT.NONE);
		labDesc.setText(Constants.getStringVaule("label_desc"));
		labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		textRightDesc=new Text(topPannel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
		textRightDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
		
		
		Label labTime=new Label(topPannel,SWT.NONE);
		labTime.setText(Constants.getStringVaule("label_mdftime"));
		labTime.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textRightTime=new Text(topPannel,SWT.BORDER);
		textRightTime.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		Label labUser=new Label(topPannel,SWT.NONE);
		labUser.setText(Constants.getStringVaule("label_mdfuser"));
		labUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textRightUser=new Text(topPannel,SWT.BORDER);
		textRightUser.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 3, 1, 0, 0));
		topPannel.pack();
		
		rightFileTable=new Table(pannel,SWT.BORDER|SWT.FULL_SELECTION|SWT.SINGLE);
		rightFileTable.setHeaderVisible(true);
		rightFileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		rightFileTable.setLinesVisible(true);
		rightFileTable.addMouseListener(new FileItemRightCmpAction());
		rightFileTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		    header=new String[]{ StringUtil.rightPad(Constants.getStringVaule("header_filename"), 60, " "),
		    								 StringUtil.rightPad(Constants.getStringVaule("header_mdfdate"),25," ") ,
		    								 StringUtil.rightPad(Constants.getStringVaule("header_crtdate"),25," ") };
		    for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(rightFileTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
			
			for(int j=0;j<header.length;j++){		
				rightFileTable.getColumn(j).pack();
			}	
		pannel.pack();
	}
	


	
	 public class SelectVersionAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				String version=comboRightVersion.getText();
				if(!StringUtil.isNullOrEmpty(version)){
					ViewVersionBean data=Versions.get(version);
					if(data!=null){
						setControl(true);
						rightFileTable.removeAll();
						textRightName.setText(dataLeft.getViewName());
						textRightDesc.setText(data.getViewDesc());
						textRightTime.setText(data.getUptTime());
						textRightUser.setText(data.getUptUser());
						setControl(false);
						List<ViewFileBean> Files= VIEW.getVersionFiles(data.getViewID(),data.getVersion());	
						Map<String,ViewFileBean> NewFilesMap=new HashMap<String, ViewFileBean>();
						  if(Files!=null&&Files.size()>0){
							   for(ViewFileBean file:Files){
								   NewFilesMap.put(file.getFileName(), file);
							   }
						  }
						  TableItem[] items=leftFileTable.getItems();
						  for(int w=0;w<items.length;w++){
							  ViewFileBean file=(ViewFileBean)items[w].getData();
							  if(NewFilesMap.containsKey(file.getFileName())){
								  ViewFileBean newFile=NewFilesMap.get(file.getFileName());
								   String name=newFile.getFileName();
							    	Image icon=Icons.getFileImage(name);
							    	if(file.getMd5().equals(newFile.getMd5())){
										TableItem tableItem=new TableItem(rightFileTable,SWT.BORDER);
										tableItem.setText(new String[]{name,newFile.getFileTime(),newFile.getCrtTime()});
										tableItem.setImage(icon);
										tableItem.setData(newFile);
							    	}else{
							    		TableItem tableItem=new TableItem(rightFileTable,SWT.BORDER);
										tableItem.setText(new String[]{"*"+name,newFile.getFileTime(),newFile.getCrtTime()});
										newFile.setShowFileName("*"+name);
										tableItem.setImage(icon);
										tableItem.setData(newFile);
										tableItem.setBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_GREEN));
							    	}
							  }else{
								  TableItem tableItem=new TableItem(rightFileTable,SWT.BORDER);
								  tableItem.setText(new String[]{"*","",""});
								  tableItem.setBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_RED));
							  }
						  }
						  for(String key:NewFilesMap.keySet()){
							  if(!FilesMap.containsKey(key)){
								  ViewFileBean newFile=NewFilesMap.get(key);
								  String name=newFile.getFileName();
						    	Image icon=Icons.getFileImage(name);
								TableItem tableItem=new TableItem(rightFileTable,SWT.BORDER);
								tableItem.setText(new String[]{name,newFile.getFileTime(),newFile.getCrtTime()});
								tableItem.setImage(icon);
								tableItem.setData(newFile);
								tableItem.setBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_GREEN));
							  }
						  }
					}
				}
			}
	 }
	 
	 private void setControl(boolean able){
		 textRightName.setEditable(able);
		 textRightDesc.setEditable(able);
		 textRightTime.setEditable(able);
		 textRightUser.setEditable(able);
	 }
	 public ViewVersionBean getDataLeft() {
		return dataLeft;
	}

	public void setDataLeft(ViewVersionBean dataLeft) {
		this.dataLeft = dataLeft;
		textLeftName.setEditable(true);
		textLeftDesc.setEditable(true);
		textLeftTime.setEditable(true);
		textLeftUser.setEditable(true);
		comboLeftVersion.setEnabled(true);
		textLeftName.setText(this.dataLeft.getViewName());
		textLeftDesc.setText(this.dataLeft.getViewDesc());
		textLeftTime.setText(this.dataLeft.getUptTime());
		textLeftUser.setText(this.dataLeft.getUptUser());
		 leftFileTable.removeAll();
		 rightFileTable.removeAll();
		List<ViewVersionBean> versions=VIEW.getVersions(this.dataLeft.getViewID());
		if(versions!=null&&versions.size()>0){
			Versions=new HashMap<String, ViewVersionBean>();
	    	String[] items=new String[versions.size()];
	    	int i=0;
	    	int index=0;
	    	for(ViewVersionBean version:versions){
	    		items[i]=version.getShowVersion();
	    		if(this.dataLeft.getShowVersion().equals(version.getShowVersion())){
	    			index=i;
	    		}
	    		i++;
	    		Versions.put(version.getShowVersion(), version);
	    	}
	    	comboLeftVersion.setItems(items);
	    	comboLeftVersion.select(index);
	    	comboRightVersion.setItems(items);
	    }
		textLeftName.setEditable(false);
		textLeftDesc.setEditable(false);
		textLeftTime.setEditable(false);
		textLeftUser.setEditable(false);
		comboLeftVersion.setEnabled(false);
		   List<ViewFileBean> Files=VIEW.getVersionFiles(this.dataLeft.getViewID(),this.dataLeft.getVersion());
		   if(Files!=null&&Files.size()>0){
			   FilesMap=new HashMap<String, ViewFileBean>();
			   for(ViewFileBean file:Files){
			    	String name=file.getFileName();
			    	Image icon=Icons.getFileImage(name);
			    	TableItem tableItem=new TableItem(leftFileTable,SWT.BORDER);
					tableItem.setText(new String[]{name,file.getFileTime(),file.getCrtTime()});
					tableItem.setImage(icon);
					tableItem.setData(file);
					FilesMap.put(file.getFileName(), file);
			    }
			    for(int j=0;j<header.length;j++){		
			    	leftFileTable.getColumn(j).pack();
				}	
		   }
		    
	}

	 public ViewFileBean getCompareFile(String fileName){
		 TableItem[] items=rightFileTable.getItems();
		  for(int w=0;w<items.length;w++){
			  ViewFileBean file=(ViewFileBean)items[w].getData();
			  if(file.getShowFileName().indexOf(fileName)!=-1){
				  if(file.getShowFileName().equals(fileName)){
					  return null;
				  }else{
					  return file;
				  }
			  }
		  }
		  return null;
	 }
	 
	 public ViewFileBean getCompareLeftFile(String fileName){
		 TableItem[] items=leftFileTable.getItems();
		  for(int w=0;w<items.length;w++){
			  ViewFileBean file=(ViewFileBean)items[w].getData();
			  if(fileName.indexOf(file.getShowFileName())!=-1){
				  if(file.getShowFileName().equals(fileName)){
					  return null;
				  }else{
					  return file;
				  }
			  }
		  }
		  return null;
	 }

	 public class FileItemLeftCmpAction extends MouseAdapter{
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem tableItem=leftFileTable.getItem(new  Point(e.x,e.y));
			 if(tableItem!=null){
				 ViewFileBean file=(ViewFileBean)tableItem.getData();
				 ViewFileBean tgtFile=getCompareFile(file.getFileName());
				  if(tgtFile==null){
					    String remotePath=file.getRemotePath();
			   			String path=Paths.getInstance().getWorkDir();
			            String localDir=FileUtils.formatPath(path)+File.separator+file.getStreamID()+File.separator+file.getViewID()+File.separator+file.getVersionID();
			   			boolean downResult=file.download();
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
				  }else{//显示文本比较视图					  
					  boolean downFromRet= file.download();
					  boolean downToRet= tgtFile.download();
					  if(downFromRet&&downToRet){
						  String lab=file.getFileName()+":"+file.getVersionID()+" vs "+tgtFile.getVersionID();
							if(!TesterEditView.getInstance(null).getTabState(lab)){
								TesterTextCmpView compareView=new TesterTextCmpView(TesterEditView.getInstance(null).getTabFloder(),file,tgtFile);
								TesterEditView.getInstance(null).setTabItems(compareView.self, lab);
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
	 }
	 
	 public class FileItemRightCmpAction extends MouseAdapter{
		 public void mouseDoubleClick(MouseEvent e) {
			 TableItem tableItem=rightFileTable.getItem(new  Point(e.x,e.y));
			 if(tableItem!=null){
				 ViewFileBean file=(ViewFileBean)tableItem.getData();
				 ViewFileBean tgtFile=getCompareLeftFile(file.getShowFileName());
				  if(tgtFile==null){
					    String remotePath=file.getRemotePath();
			   			String path=Paths.getInstance().getWorkDir();
			            String localDir=FileUtils.formatPath(path)+File.separator+file.getStreamID()+File.separator+file.getViewID()+File.separator+file.getVersionID();
			   			boolean downResult=file.download();
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
				  }else{//显示文本比较视图					  
					  boolean downFromRet= file.download();
					  boolean downToRet= tgtFile.download();
					  if(downFromRet&&downToRet){
						  String lab=file.getFileName()+":"+file.getVersionID()+" vs "+tgtFile.getVersionID();
							if(!TesterEditView.getInstance(null).getTabState(lab)){
								TesterTextCmpView compareView=new TesterTextCmpView(TesterEditView.getInstance(null).getTabFloder(),file,tgtFile);
								TesterEditView.getInstance(null).setTabItems(compareView.self, lab);
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
	 }

}
