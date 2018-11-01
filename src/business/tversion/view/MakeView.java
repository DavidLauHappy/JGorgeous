package business.tversion.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DIR;
import model.PKG;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import bean.DIRBean;
import bean.ViewFileBean;
import business.tester.view.TesterStreamView;
import business.tversion.action.DataHelper;
import business.tversion.action.DropDragAction;
import business.tversion.core.VersionMake;
import business.tversion.view.TVersionDirMapView.LoadAppDirs;




import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.FileItem;
import resource.Icons;
import resource.Item;
import resource.Logger;
import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;
import views.NewFileView.ScrollCompositeResizeAction;

/**
 * @author 作者 E-mail:
 * @date 2016-8-12
 * @version 1.0
 * 类说明
 */
public class MakeView {
	
	private  Combo comboApp;
	 public  Composite selfContent=null;
	 private  SashForm content=null;
	// public  Group group=null;
	 private  Text describe=null;
	 private Button bthMake=null;
	 private Composite parent;
	 private String versionID;
	 private String xmlPath;
	 public DateTime dateOnLile;
	 public MakeView(Composite com){
		 parent=com;
		 selfContent=new Composite(parent,SWT.NONE);
		 selfContent.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 selfContent.setLayout(LayoutUtils.getComGridLayout(1, 10));

		 content=new SashForm(selfContent,SWT.VERTICAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 DirMaps=new HashMap<String, Map<String,File>>();
		 Dirs=new HashMap<String, TreeItem>();
		 this.createToolView();
		 this.createTreesPanel();
		 content.setWeights(new int[]{20,80});
		 selfContent.pack();
		 
	 }
	 
	 public MakeView(Composite com,String versionID,String xmlPath){
		 parent=com;
		 this.versionID=versionID;
		 this.xmlPath=xmlPath;
		 this.loadVersion();
		 selfContent=new Composite(parent,SWT.NONE);
		 selfContent.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 selfContent.setLayout(LayoutUtils.getComGridLayout(1, 10));
		 
		 content=new SashForm(selfContent,SWT.VERTICAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 DirMaps=new HashMap<String, Map<String,File>>();
		 Dirs=new HashMap<String, TreeItem>();
		 this.createToolView();
		 this.createTreesPanel();
		 content.setWeights(new int[]{30,70});
		 selfContent.pack();
		 
	 }
	 private void createToolView(){
		    Composite toolPanel=new Composite(content,SWT.NONE);
			toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			toolPanel.setLayout(LayoutUtils.getComGridLayout(10, 1));
			
			 Composite Panel=new Composite(toolPanel,SWT.NONE);
			 Panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 10, 1, 0, 0));
			 Panel.setLayout(LayoutUtils.getComGridLayout(10, 1));
				
			Label labName=new Label(Panel,SWT.NONE);
			labName.setText(Constants.getStringVaule("label_appname"));
			labName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
			comboApp=new Combo(Panel,SWT.DROP_DOWN);
			comboApp.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1, 0, 0));
			comboApp.addSelectionListener(new LoadAppDirs());
			List<Item> Apps=Dictionary.getDictionaryList("APP");
			if(Apps!=null&&Apps.size()>0){
		    	String[] items=new String[Apps.size()];
		    	int i=0;
		    	int index=0;
		    	for(Item bean:Apps){
		    		items[i]=bean.getKey()+" "+bean.getValue();
		    		if(!StringUtil.isNullOrEmpty(this.appName)
		    		&&this.appName.equals(bean.getKey())){
		    			index=i;
		    		}
		    		i++;
		    	}
		    	comboApp.setItems(items);
		    	comboApp.select(index);
			}
		
			//测试的预计上线时间
			Label labDeployDate=new Label(Panel,SWT.NONE);
			labDeployDate.setText(Constants.getStringVaule("label_reqrdate"));
			labDeployDate.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
			dateOnLile=new DateTime(Panel, SWT.DATE );
			dateOnLile.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, false, false, 2, 1, 0, 0));
					
					
			bthMake=new Button(Panel,SWT.PUSH);
			bthMake.setText(Constants.getStringVaule("btn_make"));
			bthMake.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1, 0, 0));
			bthMake.addSelectionListener(new MakeVersionAction());
			Panel.pack();
			
			/* group=new Group(toolPanel,SWT.NONE|SWT.V_SCROLL);
			 group.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 3, 1, 0, 0));
			 group.setLayout(LayoutUtils.getComGridLayout(1, 10));
			 group.setText("关联应用");
			 for(Item bean:Apps){
				    Button btn=new Button(group,SWT.CHECK|SWT.LEFT);
					btn.setData(bean);
					btn.setText(bean.getValue());
					btn.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
					if(!StringUtil.isNullOrEmpty(this.relationApp)){
						if(this.relationApp.indexOf(bean.getKey())!=-1){
							btn.setSelection(true);
						}
					}
		    	}
			 group.pack();*/
			 
			Label labDesribe=new Label(toolPanel,SWT.NONE);
			labDesribe.setText(Constants.getStringVaule("label_versionDesc"));
			labDesribe.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, true, 1, 1, 0, 0));
			
			describe=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
			describe.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 9, 1, 0, 0));
			if(!StringUtil.isNullOrEmpty(this.desc)){
				describe.setText(this.desc);
			}
			toolPanel.pack();
	 }
	 
	 public void disableApp(){
				comboApp.setEnabled(false);
	 }
	 
	  public class MakeVersionAction extends SelectionAdapter{
 		public void widgetSelected(SelectionEvent e){
 			//判断是否存在版本描述
 			Context.session.inroll(e.getClass().toString(), "F-101-00004");
 			String  versionInfo=describe.getText();
 			if(StringUtil.isNullOrEmpty(versionInfo)){
 				MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
				messagebox.setText(Constants.getStringVaule("messagebox_import"));
				messagebox.setMessage(Constants.getStringVaule("alert_necessaryinput"));
				messagebox.open();
	 			}else{
	 				 String rawAppName=comboApp.getText();
	 				 if(StringUtil.isNullOrEmpty(rawAppName)){
	 					MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
	 					messagebox.setText(Constants.getStringVaule("messagebox_import"));
	 					messagebox.setMessage("必须选择应用");
	 					messagebox.open();
	 				 }else{
	 					bthMake.setEnabled(false);
	 					List<String> releaseIDs=new ArrayList<String>();
	 					if(TVersionStreamView.getInstance(null)!=null){
	 						List<ViewFileBean> vfiles=TVersionStreamView.getInstance(null).selectedFile;
	 						if(vfiles!=null&&vfiles.size()>0){
	 							for(ViewFileBean vfile:vfiles){
	 								String releaseID=vfile.getViewID();
	 								if(!releaseIDs.contains(releaseID))
	 									releaseIDs.add(releaseID);
	 							}
	 						}
	 					}else if(TesterStreamView.getInstance(null)!=null){
	 						List<ViewFileBean> vfiles=TesterStreamView.getInstance(null).selectedFile;
	 						if(vfiles!=null&&vfiles.size()>0){
	 							for(ViewFileBean vfile:vfiles){
	 								String releaseID=vfile.getViewID();
	 								if(!releaseIDs.contains(releaseID))
	 									releaseIDs.add(releaseID);
	 							}
	 						}
	 					}
	 	 				 String relations=" ";
	 	 				
	 	 				 	String appName=rawAppName.substring(0, rawAppName.indexOf(" "));
	 	 				 	VersionMake action=new VersionMake();
	 						boolean result= action.makeVersion(appName, versionInfo,relations,Dirs,versionID);
	 						 if(result){
	 							 //
	 							String monStr=(dateOnLile.getMonth()+1)+"";
	 							monStr=StringUtil.leftpad(monStr, 2, "0");
	 							String dayStr=dateOnLile.getDay()+"";
	 							dayStr=StringUtil.leftpad(dayStr, 2, "0");
	 							String newDate=dateOnLile.getYear()+"-"+monStr+"-"+dayStr;
	 							
	 							 //记录版本包与发布的关系
	 							    String version=action.currentVersionID;
	 							    if(StringUtil.isNullOrEmpty(versionID)){
	 							    	PKG.deletePkgViews(version);
	 							    	if(releaseIDs!=null&&releaseIDs.size()>0){
	 							    		for(String  releaseID:releaseIDs){
	 							    			PKG.addPkgView(version, releaseID, Context.session.userID);
	 							    			//发布对应需求的上线时间设置
	 							    			VIEW.setReqDateByView(releaseID, newDate);
	 							    		}
	 							    	}
	 							    }else{
	 							     	if(releaseIDs!=null&&releaseIDs.size()>0){
	 							    		for(String  releaseID:releaseIDs){
	 							    			PKG.addPkgView(version, releaseID, Context.session.userID);
	 							    			//发布对应需求的上线时间设置
	 							    			VIEW.setReqDateByView(releaseID, newDate);
	 							    		}
	 							    	}
	 							    }
		 						    FileUtils.deleteFile(action.getTmpPkgPath());
			 						MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
									messagebox.setText(Constants.getStringVaule("messagebox_alert"));
									messagebox.setMessage(Constants.getStringVaule("alert_successed")+action.getPKGPath());
									messagebox.open();
									Logger.getInstance().logServer("打包版本成功："+versionID);
			 					 }
			 					 else{
			 						MessageBox messagebox=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_ERROR|SWT.OK);
									messagebox.setText(Constants.getStringVaule("messagebox_alert"));
									messagebox.setMessage(Constants.getStringVaule("alert_failed"));
									messagebox.open();
									Logger.getInstance().logServer("打包版本失败："+versionID);
			 					 }
	 					
	 						bthMake.setEnabled(true);
	 				 }
	 			}
	 		}
	  }
	 
	  public ScrolledComposite pannel;
	  public Composite targetPanel;
	  
	  private void createTreesPanel(){
		  pannel=new ScrolledComposite(content,SWT.V_SCROLL|SWT.H_SCROLL);
		  pannel.setAlwaysShowScrollBars(true);
		  pannel.setExpandHorizontal(true);
		  pannel.setExpandVertical(true);
		  pannel.setMinSize(800,1000);
		  pannel.addControlListener(new ScrollCompositeResizeAction());
		  pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		  pannel.setLayout(LayoutUtils.getComGridLayout(1, 1));
		    targetPanel=new Composite(pannel,SWT.NONE);
		    targetPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    targetPanel.setLayout(LayoutUtils.getComGridLayout(2, 1));
		    this.makeTree(targetPanel);
	    pannel.layout(true);
	    pannel.setContent(targetPanel);
	    pannel.pack();
	  }
	  
		public class ScrollCompositeResizeAction extends ControlAdapter{
			public void controlResized(ControlEvent e) {
				targetPanel.setSize(targetPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				pannel.setMinSize(targetPanel.getSize());
			}
		}
		
	  private void makeTree(Composite com){
			  Control[] children=com.getChildren();
				if(children!=null&&children.length>0){
					for(Control control:children){
						control.dispose();
					}
				}
				 String rawAppName=comboApp.getText();
				 String appName=rawAppName.substring(0, rawAppName.indexOf(" "));
				 List<DIRBean> dirTrees=DIR.getMyActionDirs( Context.session.userID, appName);
				if(dirTrees!=null&&dirTrees.size()>0){
					for(DIRBean dir:dirTrees){
						String fullpath=DIR.getDirFullPath(dir.getMdfUser(), dir.getApp(), dir.getParentID(), dir.getName());
						dir.setFullPath(fullpath);
						Composite treePanel=new Composite(com,SWT.BORDER);
						treePanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0,300));
						treePanel.setLayout(LayoutUtils.getComGridLayout(1, 1));
						
						Composite toolPanel=new Composite(treePanel,SWT.NONE);
						toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
						toolPanel.setLayout(LayoutUtils.getComGridLayout(7, 1));
						
						Tree dirTree=new Tree(treePanel,SWT.CHECK|SWT.MULTI|SWT.V_SCROLL);//
						dirTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
						TreeItem rootItem=new TreeItem(dirTree,SWT.CHECK);
						rootItem.setText(dir.getFullPath());
						rootItem.setImage(Icons.getFloderIcon());
						rootItem.setData(dir);
						rootItem.setData("$Type", "root");
						dirTree.setData(dir);
					    DropTarget drawableTarget=new DropTarget(dirTree,DND.DROP_COPY|DND.DROP_DEFAULT|DND.DROP_MOVE);
					    drawableTarget.setTransfer(new Transfer[]{FileTransfer.getInstance()});
					    drawableTarget.addDropListener(new DropDragAction(rootItem,this));
					    if(DirData!=null&&DirData.size()>0){
					    	List<FileItem> filelist=DirData.get(dir.getFullPath());
					    	if(filelist!=null){
					    		for(FileItem fileItem:filelist){
					    			 Image icon=null;
					    			 File file=new File(fileItem.getPath());
					    			 TreeItem  treeItem=new TreeItem(rootItem,SWT.NONE);
					    			 treeItem.setText(fileItem.getName());
					    			 treeItem.setData("Boot", fileItem.getBootflag());
					    			 if(fileItem.getBootflag()==0)
					    				 icon=Icons.getFileImage(fileItem.getPath());
					    			 else
					    				 icon=Icons.getDeleteIcon();
					    			 treeItem.setImage(icon);
					    			 treeItem.setData(file);
					    			 treeItem.setData("$Type", "notRoot");
					    		}
					    	}
					    }
					    rootItem.setExpanded(true);
					    dirTree.pack();
					    
					    Button btnDel=new Button(toolPanel,SWT.PUSH);
						btnDel.setText(Constants.getStringVaule("btn_delete"));
						btnDel.setImage(Icons.getDeleteIcon());
						btnDel.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
						btnDel.addSelectionListener(new DeleteDirFilesAction(rootItem));
						
					    if(dir.getType().equals(DIRBean.Type.ExecuteDir.ordinal()+"")){
						    Button btnUp=new Button(toolPanel,SWT.PUSH);
							btnUp.setText(Constants.getStringVaule("btn_up"));
							btnUp.setImage(Icons.getUpIcon());
							btnUp.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
							btnUp.addSelectionListener(new UpFileAction(rootItem));
							
							Button btnDown=new Button(toolPanel,SWT.PUSH);
							btnDown.setText(Constants.getStringVaule("btn_down"));
							btnDown.setImage(Icons.getDownIcon());
							btnDown.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
							btnDown.addSelectionListener(new DownFileAction(rootItem));
							
							Button btnCfg=new Button(toolPanel,SWT.PUSH);
							btnCfg.setText(Constants.getStringVaule("btn_setboot"));
							btnCfg.setImage(Icons.getDaemonIcon());
							btnCfg.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1, 0, 0));
							btnCfg.addSelectionListener(new SetBootAction(rootItem));
							
							
							Button btnUnCfg=new Button(toolPanel,SWT.PUSH);
							btnUnCfg.setText(Constants.getStringVaule("btn_unsetboot"));
							btnUnCfg.setImage(Icons.getDaemonIcon());
							btnUnCfg.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1, 0, 0));
							btnUnCfg.addSelectionListener(new SetUnBootAction(rootItem));
					    }
					    toolPanel.pack();
					    
					    Map<String,File> FileMaps=new HashMap<String, File>();
					    DirMaps.put(dir.getFullPath(), FileMaps);
					    Dirs.put(dir.getFullPath(), rootItem);
						treePanel.pack();
					}
				}
				
	  }
	  
	  public class DeleteDirFilesAction extends SelectionAdapter{
		    public TreeItem rootItem;
		    public DeleteDirFilesAction(TreeItem root){
		    	rootItem=root;
		    }
	 		public void widgetSelected(SelectionEvent e){
	 			DIRBean	dir=(DIRBean)rootItem.getData();
	 			Map<String,File> FileMaps=DirMaps.get(dir.getFullPath());
	 			removeTreeItem(rootItem,FileMaps,true);
	 			DirMaps.put(dir.getFullPath(), FileMaps);	
	 		}
	  }
	  
	  public void removeTreeItem(TreeItem root,Map<String,File> fileMap,boolean needChecked){
		 
		  TreeItem[] items=root.getItems();
		  if(items!=null&&items.length>0){
			  String type=(String)root.getData("$Type");
			  if("root".equals(type)&&root.getChecked()){
				  for(TreeItem item:items){
					  removeTreeItem(item,fileMap,false);
				  }
			  }else{
				  for(TreeItem item:items){
					  removeTreeItem(item,fileMap,true);
				  }
			  }
		  }
		  if(!needChecked||root.getChecked()){
			  String type=(String)root.getData("$Type");
			  if(!"root".equals(type)){
				    File file=(File)root.getData();
					  if(fileMap!=null){
						  fileMap.remove(file.getAbsolutePath());
					  }
					  root.dispose();
			  }
		  }
	  }
	  
	  
	  public class UpFileAction extends SelectionAdapter{
		    public TreeItem rootItem;
		    public UpFileAction(TreeItem root){
		    	rootItem=root;
		    }
	 		public void widgetSelected(SelectionEvent e){
	 			DIRBean	dir=(DIRBean)rootItem.getData();
	 			TreeItem[] items=rootItem.getItems();
	 			File[] fileItems=new File[items.length];
	 			Map<File,Integer> bootFlag=new HashMap<File, Integer>();
	 			//改变顺序并向上滚动
	 			List<Integer> orderItems=new ArrayList<Integer>();
	 			List<Integer> checkedSeq=new ArrayList<Integer>();
	 			if(items!=null&&items.length>0){
	 				for(int w=0;w<items.length;w++){
	 					if(items[w].getChecked()){
	 						orderItems.add(w);
	 					}
	 					int flag=0;
	 					if(items[w].getData("Boot")!=null){
	 						flag=(Integer)items[w].getData("Boot");
	 					}
	 					bootFlag.put((File)items[w].getData(), flag);
	 					fileItems[w]=(File)items[w].getData();
	 				}
	 			}
	 			boolean reentrant=true;
	 			if(orderItems!=null&&orderItems.size()>0){
		 			for(int index:orderItems){
		 				int switchIndex=index-1;
		 				if(switchIndex>=0&&reentrant){
		 					File swichiObj=fileItems[switchIndex];
		 					fileItems[switchIndex]=fileItems[index];
		 					fileItems[index]=swichiObj;
		 					checkedSeq.add(switchIndex);
		 				}else{
		 					checkedSeq.add(index);
		 					reentrant=false;
		 				}
		 			}
		 			rootItem.removeAll();
		 			for(int i=0;i<fileItems.length;i++){
		 				File file=fileItems[i];
		 				 Image icon=Icons.getFileImage(file.getName());
						 TreeItem  newItem=new TreeItem(rootItem,SWT.NONE);
						 newItem.setText(file.getName());
						 if(bootFlag.get(file)==1){
							 newItem.setImage(Icons.getDaemonIcon());
							 newItem.setData("Boot", 1);
						 }else{
						 newItem.setImage(icon);
						 newItem.setData("Boot", 0);
						 }
						 newItem.setData(file);
						 if(checkedSeq.contains(i)){
							 newItem.setChecked(true);
						 }
		 			}
		 			rootItem.setExpanded(true);
	 			}
	 		}
	  }
	  
	  
	  public class DownFileAction extends SelectionAdapter{
		    public TreeItem rootItem;
		    public DownFileAction(TreeItem root){
		    	rootItem=root;
		    }
	 		public void widgetSelected(SelectionEvent e){
	 			DIRBean	dir=(DIRBean)rootItem.getData();
	 			TreeItem[] items=rootItem.getItems();
	 			File[] fileItems=new File[items.length];
	 			Map<File,Integer> bootFlag=new HashMap<File, Integer>();
	 			//改变顺序并向下滚动
	 			List<Integer> orderItems=new ArrayList<Integer>();
	 			List<Integer> checkedSeq=new ArrayList<Integer>();
	 			if(items!=null&&items.length>0){
	 				for(int w=0;w<items.length;w++){
	 					if(items[w].getChecked()){
	 						orderItems.add(w);
	 					}
	 					int flag=0;
	 					if(items[w].getData("Boot")!=null){
	 						flag=(Integer)items[w].getData("Boot");
	 					}
	 					bootFlag.put((File)items[w].getData(), flag);
	 					fileItems[w]=(File)items[w].getData();
	 				}
	 			}
	 			boolean reentrant=true;
	 			if(orderItems!=null&&orderItems.size()>0){
		 				for(int w=orderItems.size()-1;w>=0;w--){
		 				int index=orderItems.get(w);
		 				int switchIndex=index+1;
		 				if(switchIndex<items.length&&reentrant){
		 					File swichiObj=fileItems[switchIndex];
		 					fileItems[switchIndex]=fileItems[index];
		 					fileItems[index]=swichiObj;
		 					checkedSeq.add(switchIndex);
		 				}else{
		 					checkedSeq.add(index);
		 					reentrant=false;
		 				}
		 			}
		 			rootItem.removeAll();
		 			for(int i=0;i<fileItems.length;i++){
		 				File file=fileItems[i];
		 				 Image icon=Icons.getFileImage(file.getName());
						 TreeItem  newItem=new TreeItem(rootItem,SWT.NONE);
						 newItem.setText(file.getName());
						 if(bootFlag.get(file)==1){
							 newItem.setImage(Icons.getDaemonIcon());
							 newItem.setData("Boot", 1);
						 }else{
							 newItem.setImage(icon);
							 newItem.setData("Boot", 0);
						 }
						 newItem.setData(file);
						 if(checkedSeq.contains(i)){
							 newItem.setChecked(true);
						 }
		 			}
		 			rootItem.setExpanded(true);
	 			}
	 		}
	  }
	  
	  public class SetBootAction extends SelectionAdapter{
		    public TreeItem rootItem;
		    public SetBootAction(TreeItem root){
		    	rootItem=root;
		    }
	 		public void widgetSelected(SelectionEvent e){
	 			Button btn=(Button)e.getSource();
	 			DIRBean	dir=(DIRBean)rootItem.getData();
	 			TreeItem[] items=rootItem.getItems();
	 			if(items!=null&&items.length>0){
	 				for(int w=0;w<items.length;w++){
	 					if(items[w].getChecked()){
	 						items[w].setImage(Icons.getDaemonIcon());
	 						items[w].setData("Boot", 1);
	 					}
	 				}
	 			}
	 		}
	  }
	  
	  public class SetUnBootAction extends SelectionAdapter{
		    public TreeItem rootItem;
		    public SetUnBootAction(TreeItem root){
		    	rootItem=root;
		    }
	 		public void widgetSelected(SelectionEvent e){
	 			Button btn=(Button)e.getSource();
	 			DIRBean	dir=(DIRBean)rootItem.getData();
	 			TreeItem[] items=rootItem.getItems();
	 			if(items!=null&&items.length>0){
	 				for(int w=0;w<items.length;w++){
	 					if(items[w].getChecked()){
	 						File file=(File)items[w].getData();
	 						 Image icon=Icons.getFileImage(file.getName());
	 						items[w].setImage(icon);
	 						items[w].setData("Boot", 0);
	 					}
	 				}
	 			}
	 		}
	  }
	  
	 public class LoadAppDirs extends SelectionAdapter{
		 		public void widgetSelected(SelectionEvent e){
		 			DirMaps.clear();
		 			Dirs.clear();
		 			makeTree(targetPanel);
		 			pannel.layout(true);
		 		    pannel.setContent(targetPanel);
		 		    pannel.pack();
		 		}
			}

	 private Map<String,Map<String, File>> DirMaps=null;
	 public Map<String, Map<String, File>> getDirMaps() {
			return DirMaps;
		}	
     private Map<String,TreeItem> Dirs=null;
	public Map<String, TreeItem> getDirs() {
		return Dirs;
	}

	public String getVersionID() {
		return versionID;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	private Map<String,List<FileItem>> DirData=null;
	public String appName;
	public String relationApp;
	public String desc;
	public void loadVersion(){
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		try{
			File f=new File(this.xmlPath);
		    if(f.exists()){
		    	DirData=new HashMap<String, List<FileItem>>();
				doc = builder.build(f.getAbsolutePath());
				Element playNode=doc.getRootElement();
				appName=playNode.getAttributeValue("appName");
				relationApp=playNode.getAttributeValue("relationApp");
				desc=playNode.getAttributeValue("desc");
				List<Element> Dirs=playNode.getChildren();
				if(Dirs!=null&&Dirs.size()>0){
					for(Element dirNode:Dirs){
						String id=dirNode.getAttributeValue("id");
						String name=dirNode.getAttributeValue("name");
						String fullpath=dirNode.getAttributeValue("fullpath");
						String action=dirNode.getAttributeValue("action");
						String parentid=dirNode.getAttributeValue("parentid");
						DIRBean tdir=new DIRBean(id,name,action,parentid,appName, Context.session.userID,"");
						List<Element> Files=dirNode.getChildren();
						if(Files!=null&&Files.size()>0){
							List<FileItem> fileList=new ArrayList<FileItem>();
							for(Element fileNode:Files){
								String filename=fileNode.getAttributeValue("name");
								int bootfalg=Integer.parseInt(fileNode.getAttributeValue("bootfalg"));
								int seq=Integer.parseInt(fileNode.getAttributeValue("seq"));
								String path=FileUtils.formatPath(f.getParentFile().getAbsolutePath())+File.separator+fullpath+File.separator+filename;
								FileItem fileItem=new FileItem(filename,fullpath,path,bootfalg,seq);
								fileList.add(fileItem);
							}
							DirData.put(fullpath, fileList);
						}
					}
				}
		    }
		}
		catch(Exception e){
			Logger.getInstance().error("MakeView.loadVersion()加载信息异常："+e.toString());
		}
	}
}
