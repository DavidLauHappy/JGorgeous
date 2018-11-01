package business.tversion.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.STREAM;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import bean.StreamBean;
import bean.VStep;
import bean.ViewBean;
import bean.ViewFileBean;


import resource.Constants;
import resource.Icons;
import utils.LayoutUtils;
import views.AppView;


public class TVersionStreamView extends Composite{
	  private static TVersionStreamView unique_instance;
	  private Composite content=null;
	  private Tree tree;
	  public Button btnShow;
	  public List<ViewFileBean> selectedFile;
	  public static TVersionStreamView getInstance(Composite parent){
		  if(unique_instance==null)
			  unique_instance=new TVersionStreamView(parent);
		  return unique_instance;
	  }
	  
	  private TVersionStreamView(Composite parent){
		  super(parent,SWT.NONE);
		  content=this;
		  selectedFile=new ArrayList<ViewFileBean>();
		  this.createTree();
	  }
	
	  private void createTree(){
		    Composite toolPanel=new Composite(content,SWT.NONE);
			toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 20));
			RowLayout layout=new RowLayout();
			layout.type=SWT.HORIZONTAL;
			layout.marginLeft=1;
			layout.marginTop=1;
			layout.marginRight=1;
			layout.marginBottom=1;
			layout.spacing=5;
			toolPanel.setLayout(layout);
			toolPanel.setBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		    btnShow=new Button(toolPanel,SWT.PUSH);
			btnShow.setImage(Icons.getVisableIcon());
			btnShow.setLayoutData(new RowData(18,18));
			btnShow.setToolTipText(Constants.getStringVaule("btn_tips_showClosed"));
			btnShow.addSelectionListener(new FilterShowAction());
			Button btnRefrsh= new Button(toolPanel,SWT.PUSH);
		    btnRefrsh.setLayoutData(new RowData(18,18));
		    btnRefrsh.setImage(Icons.getRefreshIcon());
		    btnRefrsh.setToolTipText(Constants.getStringVaule("btn_tips_refreshstream"));
		    btnRefrsh.addSelectionListener(new RefreshStreamAcition());
			toolPanel.pack();
		  tree=new Tree(content,SWT.MULTI);//
		  tree.addSelectionListener(new ShowStreamAction());
		  tree.addSelectionListener(new ExpandStreamAction());
		  tree.addMouseListener(new PopMenuAction());
		  tree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		  
	    	DragSource viewFilesSource=new DragSource(tree,DND.DROP_COPY|DND.DROP_MOVE);
	    	viewFilesSource.addDragListener(new DragSourceListener() {
				public void dragStart(DragSourceEvent event) {
					if(tree.getSelectionCount()==0){
						event.doit=false;
					}
					TreeItem[] dragItems=tree.getSelection();
					for(int w=0;w<dragItems.length;w++){
						String type=(String)dragItems[w].getData("$Type");
						if( !"VFile".equals(type)){
							event.doit=false;
						}
					}
				}
				public void dragSetData(DragSourceEvent event) {
					if(FileTransfer.getInstance().isSupportedType(event.dataType)){
						TreeItem[] dragItems=tree.getSelection();
						List<TreeItem> fileItems=new ArrayList<TreeItem>();
						for(int w=0;w<dragItems.length;w++){
							String type=(String)dragItems[w].getData("$Type");
							if( "VFile".equals(type)){
								fileItems.add(dragItems[w]);
							}
						}
						if(fileItems.size()>0){
							String[] items=new String[fileItems.size()];
							int w=0;
							for(TreeItem item:fileItems){
								ViewFileBean data=(ViewFileBean)item.getData();
								String filePath=data.getLocation();
								items[w]=filePath;
								w++;
								if(!selectedFile.contains(data))
									selectedFile.add(data);
							}
							event.data=items;
						}
					}
				}
				public void dragFinished(DragSourceEvent event) {}
			});
	    	viewFilesSource.setTransfer(new FileTransfer[]{FileTransfer.getInstance()});
		  //取所有的发布
		  List<StreamBean>  streams=STREAM.getStreams();
		  for(StreamBean bean:streams){
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getStreamName());
	        	 if(bean.getStatus().equals("closed")){
	        		 treeRoot.setImage(Icons.getFloderDelIcon());
	        	 }else{
	        		 treeRoot.setImage(Icons.getFloderIcon());
	        	 }
	          treeRoot.setData(bean);
	          treeRoot.setData("$Type", "Stream");
		  }
		  tree.pack();
	  }
	  
	  public void refreshTree(){
		  tree.removeAll();
		  List<StreamBean>  streams;
		  if(!showClosed){
			  streams=STREAM.getStream("doing");
		  }else{
			  streams=STREAM.getStreams();
		  }

		  for(StreamBean bean:streams){
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getStreamName());
	        	 if(bean.getStatus().equals("closed")){
	        		 treeRoot.setImage(Icons.getFloderDelIcon());
	        	 }else{
	        		 treeRoot.setImage(Icons.getFloderIcon());
	        	 }
	        	 treeRoot.setData(bean);
	        	 treeRoot.setData("$Type", "Stream");
		  }
	  }
	  
	  public boolean showClosed=false;
	  public class FilterShowAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			List<StreamBean>  streams=null;
	   			if(!showClosed){
	   				btnShow.setToolTipText(Constants.getStringVaule("btn_tips_hideClosed"));
	   				btnShow.setImage(Icons.getInvisableIcon());
	   				streams=STREAM.getStreams();
	   				showClosed=true;
	   			}else{
	   				btnShow.setToolTipText(Constants.getStringVaule("btn_tips_showClosed"));
	   				btnShow.setImage(Icons.getVisableIcon());
	   				streams=STREAM.getStream("doing");
	   				showClosed=false;
	   			}
	   		 tree.removeAll();
			  for(StreamBean bean:streams){
				    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
		        	 treeRoot.setText(bean.getStreamName());
		        	 if(bean.getStatus().equals("closed")){
		        		 treeRoot.setImage(Icons.getFloderDelIcon());
		        	 }else{
		        		 treeRoot.setImage(Icons.getFloderIcon());
		        	 }
		        	 treeRoot.setData(bean);
		        	 treeRoot.setData("$Type", "Stream");
			  }
	   		}
	  }
	  
	  public  GridData getToolsLayoutData(){
			GridData griddata_row=new GridData();
			griddata_row.horizontalAlignment=SWT.RIGHT;
			griddata_row.verticalAlignment=SWT.FILL;
			griddata_row.horizontalSpan=1;//水平占据单元格数目
			griddata_row.verticalSpan=1;//垂直占据单元格数目
			griddata_row.widthHint=16;
			griddata_row.heightHint=16;
			//垂直方向上布局的误差
			griddata_row.verticalIndent=-5;
			griddata_row.grabExcessHorizontalSpace=true;//水平方向是否放大
			griddata_row.grabExcessVerticalSpace=false;//垂直方向是否放大
			return griddata_row;
		}
	  
	  public class ShowStreamAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			//根据权限，来编辑或查看发布的详情
	   			TreeItem currentItem=(TreeItem)e.item;
	   			String type=(String)currentItem.getData("$Type");
	   			if("Stream".equals(type)){
		   			StreamBean bean=(StreamBean)currentItem.getData();
		   			String item=bean.getStreamName();
		   			//先不管权限
		   			if(!TVersionEditView.getInstance(null).getTabState(item)){
		   				TVersionStreamInfoView releaseView=new TVersionStreamInfoView(TVersionEditView.getInstance(null).getTabFloder(),bean);
		   				TVersionEditView.getInstance(null).setTabItems(releaseView.self, item);
		   			}
	   			}
	   		}
	  }
	  
	  public class ExpandStreamAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			TreeItem currentItem=(TreeItem)e.item;
	   			String type=(String)currentItem.getData("$Type");
	   			if("Stream".equals(type)){
	   				StreamBean bean=(StreamBean)currentItem.getData();
	   				currentItem.removeAll();
	   				List<ViewBean> views=STREAM.getViews(bean.getStreamID());
	   				if(views!=null&&views.size()>0){
	   					for(ViewBean view:views){
	   						if(VStep.VersionPackage.equals(view.getProgress())){
		   						TreeItem  treeItem=new TreeItem(currentItem,SWT.MULTI);
		   						treeItem.setText(view.getViewName()+"("+view.getViewID()+")");
		   						treeItem.setImage(Icons.getViewIcon());
		   						treeItem.setData(view);
		   						treeItem.setData("$Type", "Release");
	   						}
	   					}
	   					currentItem.setExpanded(true);
	   				}
	   			}
	   			else if( "Release".equals(type)){
	   					ViewBean view=(ViewBean)currentItem.getData();
	   					currentItem.removeAll();
	   					List<ViewFileBean> vfiles=VIEW.getVersionFiles(view.getViewID(), view.getVersion());
	   					if(vfiles!=null&&vfiles.size()>0){
	   							//按文件名称排序
			   					 Collections.sort(vfiles, new Comparator<ViewFileBean>() {
			   	   	   			  public int compare(ViewFileBean arg0, ViewFileBean arg1) {
			   	   	                  return arg0.getNameOrder().compareTo(arg1.getNameOrder());
			   	   	              }
			   	   			   });
	   							for(ViewFileBean vfile:vfiles){
	   								vfile.download();
	   								TreeItem  treeItem=new TreeItem(currentItem,SWT.MULTI);
	   								treeItem.setText(vfile.getFileName());
	   								String name=vfile.getFileName();
	   						    	Image icon=Icons.getFileImage(name);
	   						    	treeItem.setImage(icon);
	   						    	treeItem.setData(vfile);
			   						treeItem.setData("$Type", "VFile");
			   						
	   							}
	   						 currentItem.setExpanded(true);
	   					}
	   			}
	   			else if( "VFile".equals(type)){
	   				 	//文件下载后，才能拖拽
	   				ViewFileBean vfile=(ViewFileBean)currentItem.getData();
	   				vfile.download();
	   				
	   			}
	   		}
	  }
	  
	  public class PopMenuAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){
	 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null&&e.button==3){
	 				StreamBean data=(StreamBean)currentItem.getData();
	 				tree.setMenu(TVersionStreamView.getInstance(null).getStreamMenu(data));
	 			}else{
	 				tree.setMenu(null);
	 			}
	 		}
	 		
	 		 public void mouseDoubleClick(MouseEvent e){
		 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
		 			if(currentItem!=null){
		 				StreamBean data=(StreamBean)currentItem.getData();
		  			 	String status=data.getStatus();
		  			 	if(status.equals("closed")){
		  			 		 String msg="流【"+data.getStreamName()+"】已被关闭！无法使用！";
								 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
								 box.setText(Constants.getStringVaule("messagebox_alert"));
								 box.setMessage(msg);
								 box.open();
		  			 	}else{
		  			 		String itemName=data.getStreamName();
		  		   			//先不管权限
		  		   			if(!TVersionEditView.getInstance(null).getTabState(itemName)){
		  		   				TVersionStreamInfoView releaseView=new TVersionStreamInfoView(TVersionEditView.getInstance(null).getTabFloder(),data);
		  		   				TVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
		  		   			}
		  			 	}
		 			}
	 		 }
	  }
	  
	  public  Menu getStreamMenu(StreamBean obj){
		  Menu versionMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
		  MenuItem itemView=new MenuItem(versionMenu,SWT.PUSH);
	  	 itemView.setText("详情");
	  	 itemView.setData(obj);
	  	 itemView.setImage(Icons.getDetailIcon());
	  	 itemView.addSelectionListener(new SelectionAdapter(){ 	
  		 public void widgetSelected(SelectionEvent e){
  			 	MenuItem item=(MenuItem)e.getSource();
  			 	StreamBean data=(StreamBean)item.getData();
  			 	String status=data.getStatus();
  			 	if(status.equals("closed")){
  			 		 String msg="流【"+data.getStreamName()+"】已被关闭！无法使用！";
						 MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						 box.setText(Constants.getStringVaule("messagebox_alert"));
						 box.setMessage(msg);
						 box.open();
  			 	}else{
  			 		String itemName=data.getStreamName();
  		   			//先不管权限
  		   			if(!TVersionEditView.getInstance(null).getTabState(itemName)){
  		   				TVersionStreamInfoView releaseView=new TVersionStreamInfoView(TVersionEditView.getInstance(null).getTabFloder(),data);
  		   				TVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
  		   			}
  			 	}
  		 }
  	 });
  	 
		  	 MenuItem itemViewUnlock=new MenuItem(versionMenu,SWT.PUSH);
		  	 itemViewUnlock.setText("解锁");
		  	 itemViewUnlock.setData(obj);
		  	 itemViewUnlock.setImage(Icons.getActiveIcon());
		  	 itemViewUnlock.addSelectionListener(new SelectionAdapter(){ 	
		  		 public void widgetSelected(SelectionEvent e){
		  			 	MenuItem item=(MenuItem)e.getSource();
		  			 	StreamBean data=(StreamBean)item.getData();
		  			 	data.SetStatus("doing");
		  			 	MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						 box.setText(Constants.getStringVaule("alert_successoperate"));
						 box.setMessage(Constants.getStringVaule("alert_successoperate"));
						 box.open();
		  		 }
		  	 });
		  	 
		  	 MenuItem itemViewLock=new MenuItem(versionMenu,SWT.PUSH);
		  	 itemViewLock.setText("锁定");
		  	 itemViewLock.setData(obj);
		  	 itemViewLock.setImage(Icons.getLockIcon());
		  	 itemViewLock.addSelectionListener(new SelectionAdapter(){ 	
		  		 public void widgetSelected(SelectionEvent e){
		  			 	MenuItem item=(MenuItem)e.getSource();
		  			 	StreamBean data=(StreamBean)item.getData();
		  			 	data.SetStatus("closed");
		  			 	MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						 box.setText(Constants.getStringVaule("alert_successoperate"));
						 box.setMessage(Constants.getStringVaule("alert_successoperate"));
						 box.open();
		  		 }
		  	 });
		  	 
		  	 MenuItem itemViewClose=new MenuItem(versionMenu,SWT.PUSH);
		  	 itemViewClose.setText("关闭流");
		  	 itemViewClose.setData(obj);
		   	 itemViewClose.setImage(Icons.getDeleteIcon());
		  	 itemViewClose.addSelectionListener(new SelectionAdapter(){ 	
		  		 public void widgetSelected(SelectionEvent e){
		  			 	MenuItem item=(MenuItem)e.getSource();
		  			 	StreamBean data=(StreamBean)item.getData();
		  			 	data.SetStatus("closed");
		  			 	refreshTree();
		  			 	MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
						 box.setText(Constants.getStringVaule("alert_successoperate"));
						 box.setMessage(Constants.getStringVaule("alert_successoperate"));
						 box.open();
		  		 }
			 });
		  return versionMenu;
	  }

	  public class RefreshStreamAcition extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				refreshTree();
			}
	  }
	  
}
