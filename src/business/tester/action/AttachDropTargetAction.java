package business.tester.action;

import java.io.File;
import java.util.List;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import resource.Context;
import resource.Icons;

import utils.DateUtil;
import utils.FileUtils;
import bean.ViewFileBean;

public class AttachDropTargetAction implements DropTargetListener{
		
		public Table table;
		private List<ViewFileBean> DeleteFiles;
		public AttachDropTargetAction(Table fileTable,List<ViewFileBean> deletes){
			this.table=fileTable;
			this.DeleteFiles=deletes;
		}
		
		public void dropAccept(DropTargetEvent e) {
			if(e.detail==DND.DROP_DEFAULT)
				  e.detail=DND.DROP_COPY;
		}
		
		public void drop(DropTargetEvent event) {
			if(FileTransfer.getInstance().isSupportedType(event.currentDataType)){
				String[] filePaths=(String[])event.data;
				//判断文件是否已经存在,存在的文件，自动覆盖
				if(filePaths!=null&&filePaths.length>0){
					for(String filePath:filePaths){
						if(this.checkExist(filePath)){
							File file=new File(filePath);
							String fileName=FileUtils.getFileName(filePath);
							if(file.exists()&&file.isFile()){
								TableItem[] items=	this.table.getItems();
								for(int i=0;i<items.length;i++){
									ViewFileBean data=(ViewFileBean)items[i].getData();
									if(data.getFileName().equals(fileName)){
										long date=file.lastModified();
										String lastTime=DateUtil.getTimeFormLong(date);
										String md5=FileUtils.getMd5ByFile(file);
										Image icon=Icons.getFileImage(fileName);
										data.setFileName(fileName);
										data.setLocation(filePath);
										data.setMdfTime(DateUtil.getCurrentTime());
										data.setCrtTime(lastTime);
										data.setFileTime(lastTime);
										data.setMd5(md5);
										data.setCrtUser( Context.session.userID);
										data.setCrtTime(DateUtil.getCurrentTime());
										data.setFileMode(ViewFileBean.Mode.Local.ordinal()+"");
										data.setType("1");
										int order=table.getItems().length+1;
										data.setOrignalOrder(order);
										items[i].setText(new String[]{data.getFileName()});
										items[i].setImage(icon);
										items[i].setData(data);
										break;
									}
								}
							}
						}else{
							File file=new File(filePath);
							if(file.exists()&&file.isFile()){
								String name=file.getName();
								long date=file.lastModified();
								String lastTime=DateUtil.getTimeFormLong(date);
								String md5=FileUtils.getMd5ByFile(file);
								Image icon=Icons.getFileImage(name);
								ViewFileBean bean=new ViewFileBean();
								bean.setFileName(name);
								bean.setLocation(filePath);
								bean.setMdfTime(DateUtil.getCurrentTime());
								bean.setCrtTime(lastTime);
								bean.setFileTime(lastTime);
								bean.setMd5(md5);
								bean.setCrtUser( Context.session.userID);
								bean.setCrtTime(DateUtil.getCurrentTime());
								bean.setFileMode(ViewFileBean.Mode.Local.ordinal()+"");
								bean.setType("1");
								int order=table.getItems().length+1;
								bean.setOrignalOrder(order);
								TableItem tableItem=new TableItem(table,SWT.BORDER);
								tableItem.setText(new String[]{bean.getFileName()});
								tableItem.setImage(icon);
								final TableEditor editorDel=new TableEditor(table);
								Button btnDel=new Button(table,SWT.PUSH);
								btnDel.setImage(Icons.getDeleteIcon());
								btnDel.addSelectionListener(new SelectionAdapter(){
									public void widgetSelected(SelectionEvent e){
										ViewFileBean bean=(ViewFileBean)editorDel.getItem().getData();
										deleteCmtFileItem(bean);
										  ((Button)e.getSource()).dispose();
										  table.pack();
									}
								});
								editorDel.grabHorizontal=true;
								editorDel.setEditor(btnDel, tableItem, 1);
								tableItem.setData(bean);
							}
						}
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
		private boolean checkExist(String filePath){
			TableItem[] items=	this.table.getItems();
			String fileName=FileUtils.getFileName(filePath);
			if(items!=null&&items.length>0){
				for(int i=0;i<items.length;i++){
					ViewFileBean data=(ViewFileBean)items[i].getData();
					if(data.getFileName().equals(fileName)){
						return true;
					}
				}
			}
			return false;
		}
		
		public void deleteCmtFileItem(ViewFileBean bean){
			TableItem[] items=	this.table.getItems();
			if(items!=null&&items.length>0){
				for(int i=0;i<items.length;i++){
					ViewFileBean data=(ViewFileBean)items[i].getData();
					if(bean.getFileName().equals(data.getFileName())){
						this.table.remove(i);
						DeleteFiles.add(data);
						return;
					}
				}
			}
		}
}
