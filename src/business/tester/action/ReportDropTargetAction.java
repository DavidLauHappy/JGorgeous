package business.tester.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import resource.Constants;
import resource.Context;
import resource.Icons;

import utils.DateUtil;
import utils.FileUtils;
import utils.StringUtil;
import views.AppView;
import bean.ViewFileBean;

public class ReportDropTargetAction implements DropTargetListener{
	
	public Table table;
	public  List<ViewFileBean> ReportsFiles=null;;
	public ReportDropTargetAction(Table fileTable,List<ViewFileBean> rfiles){
		this.table=fileTable;
		this.ReportsFiles=rfiles;
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
				 List<TableItem>  selectItems=new ArrayList<TableItem>();
				for(String filePath:filePaths){
					if(this.checkExist(filePath)){
						String msg="文件"+filePath+"已存在！不允许上传，只能上传测试报告";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
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
							int order=table.getItems().length+1;
							bean.setOrignalOrder(order);
							TableItem tableItem=new TableItem(table,SWT.BORDER);
							tableItem.setText(new String[]{bean.getFileName(),bean.getCrtTime(),bean.getFileTime(), bean.getMd5()});
							tableItem.setImage(icon);
							tableItem.setData(bean);
							ReportsFiles.add(bean);
							selectItems.add(tableItem);
						}
					}
				}
				if(selectItems.size()>0){
		   			TableItem[] selections=new TableItem[selectItems.size()];
		   			table.setSelection(selectItems.toArray(selections));
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
	
}
