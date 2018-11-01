package business.tversion.view;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;


import resource.Constants;
import utils.LayoutUtils;
import views.AppView;

public class FileSearchView {
private static FileSearchView unique_instance;
private  Shell Result=null;
private org.eclipse.swt.widgets.List listControl=null;
	public static FileSearchView getSearchView(){
		if(unique_instance==null)
			unique_instance=new FileSearchView();
		return unique_instance;
	}
	
	private FileSearchView(){
		Result=new Shell(AppView.getInstance().getShell(),SWT.CLOSE);
		Result.setText(Constants.getStringVaule("window_searchret"));
		Point point = AppView.getInstance().getDisplay().getCurrent().getCursorLocation();
		Result.setLocation(new Point(point.x,point.y+25));
		Result.setLayout(LayoutUtils.getComGridLayout(1, 0));
		Composite pannel=new Composite(Result,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 400, 260));
		pannel.setLayout(LayoutUtils.getComGridLayout(1, 0));
		listControl=new org.eclipse.swt.widgets.List(pannel,SWT.SINGLE|SWT.V_SCROLL);
		listControl.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		listControl.addMouseListener(new ChooseAndTrickAction());
		pannel.pack();
		Result.pack();
		Result.open();
		Result.addShellListener(new ShellCloseAction());
	}
	
	public void show(String[] files){
		listControl.removeAll();
		if(files!=null&&files.length>0){
			listControl.setItems(files);
			}
   }

	public void exit(){
		Result.dispose();
		unique_instance=null;
	}
	
public class ShellCloseAction extends ShellAdapter{
	public void shellClosed(ShellEvent e){	
		Result.dispose();
		unique_instance=null;
	}	
}
	
public class ChooseAndTrickAction implements MouseListener{
		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
			if(listControl.getSelectionCount()>0){
				String text=listControl.getSelection()[0];
				String[] columns=text.split(""+(char)29+"");
				if(columns!=null&&columns.length==2){
					String filePath=columns[1];
			    	//FileView.getInstance(null).setSelection(filePath);
			    	//FileSearchView.getSearchView().exit();
				}
		    	
			}
			
		}
		public void mouseDown(MouseEvent e) {}
		public void mouseUp(MouseEvent arg0) {}
}
	
}
