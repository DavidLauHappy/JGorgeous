package views;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import resource.Constants;
import resource.Icons;

import utils.LayoutUtils;
import utils.StringUtil;

public class FileSourceView extends Composite{

	private static FileSourceView unique_instance;
	private Composite content=null;
	private Tree fileTree;
	private Text textSearch;
	public static FileSourceView getInstance(Composite parent){
		if(unique_instance==null)
			unique_instance=new FileSourceView(parent);
		return unique_instance;
	}
	
	private FileSourceView(Composite com){
		super(com,SWT.NONE);
		 content=this;
		 this.createSearchPanel();
		 this.createTree();
	}
	
	 private void createSearchPanel(){
	    	Composite panel=new Composite(content,SWT.NONE);
	    	panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
	    	panel.setLayout(LayoutUtils.getComGridLayout(6, 2));
	    	textSearch=new Text(panel,SWT.BORDER);
	    	textSearch.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 5, 1, 0, 0));
	    	Button btnFind=new Button(panel,SWT.PUSH);
			btnFind.setToolTipText(Constants.getStringVaule("btn_tips_findByPath"));
			btnFind.setImage(Icons.getSearchIcon());
			btnFind.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.FILL, true, false, 1, 1, 0, 0));
			btnFind.addSelectionListener(new FindByPathAction());
	    	panel.pack();
	 }
	 
	 public static  Color green = AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_GREEN);  
	public static Color gray = AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_GRAY);  
	  private void createTree(){
	    	fileTree=new Tree(content,SWT.MULTI);
	    	fileTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
	    	fileTree.addSelectionListener(new ExpandFileAction());
	    	fileTree.addListener(SWT.EraseItem, new Listener(){
				public void handleEvent(Event event){
					event.detail &= ~SWT.HOT;  
		            if ((event.detail & SWT.SELECTED) == 0) return; /* item not selected */  
		            int clientWidth = fileTree.getClientArea().width;  
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
	    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    	DragSource dbNodeSource=new DragSource(fileTree,DND.DROP_COPY|DND.DROP_MOVE);
	  		dbNodeSource.addDragListener(new DragSourceListener() {
				public void dragStart(DragSourceEvent event) {
					if(fileTree.getSelectionCount()==0){
						event.doit=false;
					}
				}
				public void dragSetData(DragSourceEvent event) {
					if(FileTransfer.getInstance().isSupportedType(event.dataType)){
						//多个对象的拖拽判断
						TreeItem[] selectedItems=fileTree.getSelection();
						String[] filePaths=new String[selectedItems.length];
						for(int w=0;w<selectedItems.length;w++){
							String filePath=((File)selectedItems[w].getData()).getAbsolutePath();
							filePaths[w]=filePath;
						}
						event.data=filePaths;
					}
				}
				public void dragFinished(DragSourceEvent event) {}
			});
	  		dbNodeSource.setTransfer(new Transfer[]{FileTransfer.getInstance()});
	  		//////////////////////////////////////////////////////////////////////////////////////////////
	  		FileSystemView sys = FileSystemView.getFileSystemView();
	  		 File[] roots=File.listRoots();
	    	 for(File file : roots){
	    		 String label=sys.getSystemTypeDescription(file);
	    		 TreeItem  treeRoot=new TreeItem(fileTree,SWT.MULTI);
	        	 treeRoot.setText(label+" "+file.getAbsolutePath());
	        	 treeRoot.setImage(Icons.getFloderIcon());
	        	 treeRoot.setData(file);
	    	 }
	    	fileTree.pack();
	  }
	  
	   public class ExpandFileAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			TreeItem currentItem=(TreeItem)e.item;
	   			 File file=(File)currentItem.getData();
	   				currentItem.removeAll();
	   				File[] children=file.listFiles();
	   				if(children!=null){
		   				 for(int w=0;w<children.length;w++){
		  		    		  TreeItem  step=new TreeItem(currentItem,SWT.CHECK|SWT.MULTI);
		  		    		  step.setText(children[w].getName());
		  		    		  Image icon=null;
		  		    		  if(children[w].isDirectory()){
		  		    			icon=Icons.getFloderIcon();
		  		    		  }
		  		    		  else{
		  		    			icon =Icons.getFileImage(children[w]);
		  		    		  }
		  		    		  step.setImage(icon);
		  		    		  step.setData(children[w]);
		  				 }
		   				currentItem.setExpanded(true);
	   				}
	   		}
	   }
	  
	   
	   public class FindByPathAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){ 
	   				String keyword=textSearch.getText();
	   				if(!StringUtil.isNullOrEmpty(keyword)){
	   					TreeItem[] items=fileTree.getItems();
	   					TreeItem start=null;
	   					for(TreeItem like:items){
	   			    		String filepath=((File)like.getData()).getAbsolutePath();
	   			    		filepath=filepath.replace(File.separator, "");
	   			    		if(startFullWith(keyword,filepath)){
	   			    			start=like;
	   			    		   backgroundMakeRree(start,true);
	   			    		   like.setExpanded(true);
	   			    		   break;
	   			    		}
	   					}
	   					if(start!=null){
		   					TreeItem meetItem=findItem(start, keyword);
		   			    	if(meetItem!=null){
		   				    	fileTree.select(meetItem);
		   				    	fileTree.setFocus();
		   			    	}
	   					}
	   				}
	   		}
	   }
	   
	   private static TreeItem findItem(TreeItem start,String filepath){
		   TreeItem[] items=start.getItems();
			   		if(items!=null&&items.length>0){
						    	for(TreeItem item:items){
						    		    String path=((File)item.getData()).getAbsolutePath();
						    		    if(StringUtil.startFullWith(filepath,path)){
						    		    		backgroundMakeRree(item,true);
							    		       if(filepath.equals(path)){
							    		    	   return item;
							    		       }
							    		       else{
							    		    	   return findItem(item, filepath);
							    		       }
						    		    }
						    	}
			   	}
			  return null;
	   }
	   
	   private static void backgroundMakeRree(TreeItem item,boolean expanded){
				   if(item.getItemCount()<=0){
			    	    File file=(File)item.getData();
			    	    item.removeAll();
						File[] children=file.listFiles();
						if(children!=null){
							 for(int w=0;w<children.length;w++){
					    		  TreeItem  step=new TreeItem(item,SWT.CHECK|SWT.MULTI);
					    		  step.setText(children[w].getName());
					    		  Image icon=null;
					    		  if(children[w].isDirectory()){
					    			icon=Icons.getFloderIcon();
					    		  }
					    		  else{
					    			icon =Icons.getFileImage(children[w].getName());
					    		  }
					    		  step.setImage(icon);
					    		  step.setData(children[w]);
							 }
							 item.setExpanded(expanded);
						}
		   	}
	   }
	   
	   public static boolean startFullWith(String longer,String shorter){
    	   String tail=longer.replace(shorter, "");
    	   if(StringUtil.isNullOrEmpty(tail)||tail.startsWith(File.separator))
 			  return true;
 		return false;
      }
	 
}
