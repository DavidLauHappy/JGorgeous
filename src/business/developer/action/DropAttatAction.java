package business.developer.action;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import bean.RFile;


import resource.Constants;
import resource.Icons;

import utils.FileUtils;
import utils.LayoutUtils;
import views.AppView;

public class DropAttatAction implements DropTargetListener{
public Table table;
	
	public DropAttatAction(Table fileTable){
		this.table=fileTable;
	}
	
	public void dropAccept(DropTargetEvent e) {
		if(e.detail==DND.DROP_DEFAULT)
			  e.detail=DND.DROP_COPY;
	}
	
	public void drop(DropTargetEvent event) {
		if(FileTransfer.getInstance().isSupportedType(event.currentDataType)){
			String[] filePaths=(String[])event.data;
			//判断文件是否已经存在,存在的文件，自动覆盖，要提示
			if(filePaths!=null&&filePaths.length>0){
				for(String filePath:filePaths){
					if(this.checkExist(filePath)){
						String msg="文件"+filePath+"已存在！确认要覆盖吗？";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.YES|SWT.NO);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						int choose=box.open();
						if(choose==SWT.YES){
							File file=new File(filePath);
							if(file.exists()&&file.isFile()){
								RFile rfile=new RFile();
								rfile.setFileName(file.getName());
								rfile.setLocalPath(file.getAbsolutePath());
								replaceFileItem(rfile);
							}
						}
					}else{
						File file=new File(filePath);
						if(file.exists()&&file.isFile()){
							String md5=FileUtils.getMd5ByFile(file);
							Image icon=Icons.getFileImage(file);
							RFile rfile=new RFile();
							rfile.setFileName(file.getName());
							rfile.setLocalPath(file.getAbsolutePath());
							rfile.setMd5(md5);
							
							TableItem tableItem=new TableItem(table,SWT.BORDER);
							tableItem.setText(new String[]{file.getName()});
							tableItem.setImage(icon);
							final TableEditor editorDel=new TableEditor(table);
							Button btnDel=new Button(table,SWT.PUSH);
							btnDel.setImage(Icons.getDeleteIcon());
							btnDel.addSelectionListener(new SelectionAdapter(){
								public void widgetSelected(SelectionEvent e){
									  RFile bean=(RFile)editorDel.getItem().getData();
									  deleteFileItem(bean);
									  ((Button)e.getSource()).dispose();
									  table.pack();
								}
							});
							editorDel.grabHorizontal=true;
							editorDel.setEditor(btnDel, tableItem, 1);
							tableItem.setData(rfile);
						}
					}	
				}
			}
		}
	}
	
	private boolean checkExist(String filePath){
		TableItem[] items=	this.table.getItems();
		String fileName=FileUtils.getFileName(filePath);
		if(items!=null&&items.length>0){
			for(int i=0;i<items.length;i++){
				RFile file=(RFile)items[i].getData();
				if(filePath.equals(file.getLocalPath()))
					return true;
			}
		}
		return false;
	}
	
	public void deleteFileItem(RFile bean){
		TableItem[] items=	this.table.getItems();
		if(items!=null&&items.length>0){
			for(int i=0;i<items.length;i++){
				RFile data=(RFile)items[i].getData();
				if(bean.getFileName().equals(data.getFileName())){
					this.table.remove(i);
					return;
				}
			}
		}
	}
	
	public void replaceFileItem(RFile bean){
		TableItem[] items=	this.table.getItems();
		if(items!=null&&items.length>0){
			for(int i=0;i<items.length;i++){
				RFile data=(RFile)items[i].getData();
				if(bean.getFileName().equals(data.getFileName())){
					items[i].setData(bean);
					return;
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
}
