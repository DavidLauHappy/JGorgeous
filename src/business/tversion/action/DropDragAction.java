package business.tversion.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

import bean.DIRBean;
import business.tversion.view.MakeView;

import resource.Icons;
import resource.Logger;
import utils.FileUtils;

/**
 * @author DavidLau
 * @date 2016-8-12
 * @version 1.0
 * 类说明
 */
public class DropDragAction implements DropTargetListener{
	
	private TreeItem root;
	private DIRBean dir;
	private MakeView view;
	public DropDragAction(TreeItem treeitem,MakeView view){
		this.root=treeitem;
		dir=(DIRBean)root.getData();
		this.view=view;
	}
	public void dropAccept(DropTargetEvent e) {
		if(e.detail==DND.DROP_DEFAULT)
			  e.detail=DND.DROP_COPY;
	}
	
	//对不同的文件夹支持
	
	public void drop(DropTargetEvent event) {
		//System.err.println(event.detail);
		if(FileTransfer.getInstance().isSupportedType(event.currentDataType)){
			String[] filePaths=(String[])event.data;
			if(filePaths!=null&&filePaths.length>0){
				for(String filePath:filePaths){
					if(!this.chekExist(filePath)){
						File file=new File(filePath);
						//List<File> files=new ArrayList<File>();
						if(file.isDirectory()){
						    Logger.getInstance().debug("拖拽了文件夹:"+filePath);
						     Image icon=Icons.getFloderIcon();
							 TreeItem  newItem=new TreeItem(this.root,SWT.NONE);
							 newItem.setText(file.getName());
							 newItem.setImage(icon);
							 newItem.setData(file);
							 newItem.setData("$root", dir.getFullPath());
							 iterationFloderTree(newItem,file);
						}
						else{
							Logger.getInstance().debug("拖拽了文件:"+filePath);
							 Image icon=Icons.getFileImage(file.getName());
							 TreeItem  newItem=new TreeItem(this.root,SWT.NONE);
							 newItem.setText(file.getName());
							 newItem.setImage(icon);
							 newItem.setData(file);
							 newItem.setData("$root", dir.getFullPath());
						}
					  view.getDirMaps().get(dir.getFullPath()).put(file.getAbsolutePath(), file); 
						/* for(File curfile:files){
							 view.getDirMaps().get(dir.getFullPath()).put(curfile.getAbsolutePath(), curfile); 
							 Image icon=Icons.getFileImage(curfile.getName());
							 TreeItem  newItem=new TreeItem(this.root,SWT.NONE);
							 newItem.setText(file.getName());
							 newItem.setImage(icon);
							 newItem.setData(curfile);
						 }*/
			        	 this.root.setExpanded(true);
					}
				}
			}
		}
	}
	
	public void  iterationFloderTree(TreeItem parent,File dir){
		if(dir.isDirectory()){
			File[] fileList=dir.listFiles();
			if(fileList!=null&&fileList.length>0){
				for(File file:fileList){
					if(file.isDirectory()){
						 Image icon=Icons.getFloderIcon();
						 TreeItem  newItem=new TreeItem(parent,SWT.NONE);
						 newItem.setText(file.getName());
						 newItem.setImage(icon);
						 newItem.setData(file);
						 newItem.setData("$root", this.dir.getFullPath());
						 iterationFloderTree(newItem,file);
					}else{
						 Image icon=Icons.getFileImage(file.getName());
						 TreeItem  newItem=new TreeItem(parent,SWT.NONE);
						 newItem.setText(file.getName());
						 newItem.setImage(icon);
						 newItem.setData(file);
						 newItem.setData("$root", this.dir.getFullPath());
					}
					 view.getDirMaps().get(this.dir.getFullPath()).put(file.getAbsolutePath(), file); 
				}
			}
		}
	}
	
	public void dragOver(DropTargetEvent event) {
		event.feedback=DND.FEEDBACK_EXPAND|DND.FEEDBACK_SELECT;
	}
	public void dragOperationChanged(DropTargetEvent e) {
		if(e.detail==DND.DROP_DEFAULT)
			  e.detail=DND.DROP_COPY;
	}
	public void dragLeave(DropTargetEvent arg0) {
	}
	public void dragEnter(DropTargetEvent arg0) {
	}
	
	private boolean chekExist(String name){
		Map<String, File> fileMap=null;
		if(dir!=null){
			fileMap=view.getDirMaps().get(dir.getFullPath());
			  if(fileMap.containsKey(name)){
				  return  true;
			  }
		}
	  return false;
	}

}
