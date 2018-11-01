package business.dversion.view;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.STREAM;
import model.VIEW;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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
import resource.Context;
import resource.Icons;

import utils.LayoutUtils;
import views.AppView;
/**
 * @author David
 *
 */
public class StreamView  extends Composite{
	  private static StreamView unique_instance;
	  private Composite content=null;
	  private Tree tree;
	  public static StreamView getInstance(Composite parent){
		  if(unique_instance==null)
			  unique_instance=new StreamView(parent);
		  return unique_instance;
	  }
	  
	  private StreamView(Composite parent){
		  super(parent,SWT.NONE);
		  content=this;
		  this.createTree();
	  }
	
	  private void createTree(){
		  tree=new Tree(content,SWT.SINGLE);//
		  tree.addSelectionListener(new ShowStreamAction());
		  tree.addSelectionListener(new ExpandStreamAction());
		  tree.addMouseListener(new PopMenuAction());
		  tree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		  //取所有的发布
		  List<StreamBean>  streams=STREAM.getStream("doing");
		  for(StreamBean bean:streams){
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getStreamName());
	        	 treeRoot.setImage(Icons.getFloderIcon());
	        	 treeRoot.setData(bean);
	        	 treeRoot.setData("$Type", "Stream");
		  }
		  tree.pack();
	  }
	  
	  public void refreshTree(){
		  tree.removeAll();
		  List<StreamBean>  streams=STREAM.getStream("doing");
		  for(StreamBean bean:streams){
			    TreeItem  treeRoot=new TreeItem(tree,SWT.SINGLE);
	        	 treeRoot.setText(bean.getStreamName());
	        	 treeRoot.setImage(Icons.getFloderIcon());
	        	 treeRoot.setData(bean);
	        	 treeRoot.setData("$Type", "Stream");
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
		   			if(!DVersionEditView.getInstance(null).getTabState(item)){
		   				StreamInfoView releaseView=new StreamInfoView(DVersionEditView.getInstance(null).getTabFloder(),bean);
		   				DVersionEditView.getInstance(null).setTabItems(releaseView.self, item);
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
	   						TreeItem  treeItem=new TreeItem(currentItem,SWT.MULTI);
	   						treeItem.setText(view.getViewName()+"("+view.getViewID()+")");
	   						treeItem.setImage(Icons.getViewIcon());
	   						treeItem.setData(view);
	   						treeItem.setData("$Type", "Release");
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
	   		}
	  }
	  
	  public class PopMenuAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){
	 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null&&e.button==3){
	 				StreamBean data=(StreamBean)currentItem.getData();
	 				tree.setMenu(StreamView.getInstance(null).getStreamMenu(data));
	 			}else{
	 				tree.setMenu(null);
	 			}
	 		}
	 		 public void mouseDoubleClick(MouseEvent e){
		 			TreeItem currentItem=tree.getItem(new Point(e.x,e.y));
		 			if(currentItem!=null){
		 				StreamBean data=(StreamBean)currentItem.getData();
				 		String itemName=data.getStreamName();
			   			//先不管权限
			   			if(!DVersionEditView.getInstance(null).getTabState(itemName)){
			   				StreamInfoView releaseView=new StreamInfoView(DVersionEditView.getInstance(null).getTabFloder(),data);
			   				DVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
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
			 		String itemName=data.getStreamName();
		   			//先不管权限
		   			if(!DVersionEditView.getInstance(null).getTabState(itemName)){
		   				StreamInfoView releaseView=new StreamInfoView(DVersionEditView.getInstance(null).getTabFloder(),data);
		   				DVersionEditView.getInstance(null).setTabItems(releaseView.self, itemName);
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
    			 	data.setUpdUser(Context.session.userID);
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
    			 	data.setUpdUser(Context.session.userID);
    			 	data.SetStatus("closed");
    			 	MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
					 box.setText(Constants.getStringVaule("alert_successoperate"));
					 box.setMessage(Constants.getStringVaule("alert_successoperate"));
					 box.open();
    		 }
    	 });
		  return versionMenu;
	  }
}
