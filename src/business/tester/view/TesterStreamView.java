package business.tester.view;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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


public class TesterStreamView extends Composite{
	  private static TesterStreamView unique_instance;
	  private Composite content=null;
	  private Tree tree;
	  public List<ViewFileBean> selectedFile;
	  public static TesterStreamView getInstance(Composite parent){
		  if(unique_instance==null)
			  unique_instance=new TesterStreamView(parent);
		  return unique_instance;
	  }
	  
	  private TesterStreamView(Composite parent){
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
			Button btnRefrsh= new Button(toolPanel,SWT.PUSH);
		    btnRefrsh.setLayoutData(new RowData(18,18));
		    btnRefrsh.setImage(Icons.getRefreshIcon());
		    btnRefrsh.addSelectionListener(new RefreshStreamAction());
		    btnRefrsh.setToolTipText(Constants.getStringVaule("btn_tips_refreshstream"));
		  tree=new Tree(content,SWT.NONE|SWT.SINGLE);//
		  tree.addSelectionListener(new ShowStreamAction());
		  tree.addSelectionListener(new ExpandStreamAction());
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
		  List<StreamBean>  streams=STREAM.getStreams();
		  for(StreamBean bean:streams){
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getStreamName());
	        	 treeRoot.setImage(Icons.getFloderIcon());
	        	 treeRoot.setData(bean);
	        	 treeRoot.setData("$Type", "Stream");
		  }
	  }
	
	  public class RefreshStreamAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			refreshTree();
	   		}
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
		   			if(!TesterEditView.getInstance(null).getTabState(item)){
		   				TesterStreamInfoView releaseView=new TesterStreamInfoView(TesterEditView.getInstance(null).getTabFloder(),bean);
		   				TesterEditView.getInstance(null).setTabItems(releaseView.self, item);
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
	   								//文件下载后，才能拖拽,怕用户shift多选文件 没有实际下载
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

}
