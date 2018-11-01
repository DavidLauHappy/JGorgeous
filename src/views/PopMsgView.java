package views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



import resource.IMessage;
import resource.Icons;

import utils.LayoutUtils;

public class PopMsgView {

	private Point Location;
	private  Shell shell=null;
	private List<IMessage> data;
	private static final int msgWidth=400;
	private static final int msgHeight=40;
	private Map<String,IMessage> Msg;
	public boolean visiable=false;
	public static PopMsgView getPopView(){
		if(unique_instance==null){
			unique_instance=new PopMsgView();
		}
		return unique_instance;
	}
	
	private static  PopMsgView unique_instance;
	
	private PopMsgView(){}
	
	public void show(List<IMessage> data){
		if(!visiable){
			this.data=data;
			Msg=new HashMap<String, IMessage>();
			
			int startX=-20+AppView.getInstance().getShell().getLocation().x+AppView.getInstance().getShell().getBounds().width-msgWidth;
			int startY=-20+AppView.getInstance().getShell().getLocation().y+AppView.getInstance().getShell().getBounds().height-this.data.size()*msgHeight;
			Location=new Point(startX,startY);
			shell=new Shell(AppView.getInstance().getShell(),SWT.BORDER);
			shell.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, false, true, 1, 1, 0, 0));
			shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
			shell.setLocation(Location);
			
			if(this.data!=null&&this.data.size()>0){
				for(IMessage message:this.data){
					Composite pannel=new Composite(shell,SWT.BORDER);
					pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, false, false, 1, 1, 400, 40));
					pannel.setLayout(LayoutUtils.getComGridLayout(10, 0));
					
					Text textMsg=new Text(pannel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.WRAP);
					textMsg.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 9, 1, 0, 0));
					textMsg.setText(message.getMsgText());
					textMsg.setEditable(false);
					
					Button btnClose=new  Button(pannel,SWT.PUSH);
					btnClose.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1, 0, 0));
					btnClose.setImage(Icons.getDeleteIcon());
					btnClose.setData(message);
					btnClose.addSelectionListener(new RemoveMsgAction());
					
					pannel.pack();
					Msg.put(message.getMsgID(), message);
				}
			}
			visiable=true;
			shell.pack();
			shell.open();
		}else{
			this.data=data;
			if(this.data!=null&&this.data.size()>0){
				for(IMessage message:this.data){
					Msg.put(message.getMsgID(), message);	
				}
			}
			shell.dispose();
			shell=new Shell(AppView.getInstance().getShell(),SWT.BORDER);
			shell.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, false, true, 1, 1, 0, 0));
			shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
			int startX=-20+AppView.getInstance().getShell().getLocation().x+AppView.getInstance().getShell().getBounds().width-msgWidth;
			int startY=-20+AppView.getInstance().getShell().getLocation().y+AppView.getInstance().getShell().getBounds().height-Msg.size()*msgHeight;
			Location=new Point(startX,startY);
			shell.setLocation(Location);
			for(String key:Msg.keySet()){
				IMessage message=Msg.get(key);
				Composite pannel=new Composite(shell,SWT.BORDER);
				pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, false, false, 1, 1, 400, 40));
				pannel.setLayout(LayoutUtils.getComGridLayout(10, 0));
				
				Text textMsg=new Text(pannel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.WRAP);
				textMsg.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 9, 1, 0, 0));
				textMsg.setText(message.getMsgText());
				textMsg.setEditable(false);
				
				Button btnClose=new  Button(pannel,SWT.PUSH);
				btnClose.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1, 0, 0));
				btnClose.setImage(Icons.getDeleteIcon());
				btnClose.setData(message);
				btnClose.addSelectionListener(new RemoveMsgAction());
				
				pannel.pack();
			}
			visiable=true;
			shell.pack();
			shell.open();
		}
	}
	
	public class RemoveMsgAction extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			Button btn=(Button)e.getSource();
			IMessage data=(IMessage)btn.getData();
			if(data!=null){
				data.closeMsg();
				Msg.remove(data.getMsgID());
			}
			if(Msg.size()>0){
				shell.dispose();
				shell=new Shell(AppView.getInstance().getShell(),SWT.BORDER);
				shell.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, false, true, 1, 1, 0, 0));
				shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
				int startX=-20+AppView.getInstance().getShell().getLocation().x+AppView.getInstance().getShell().getBounds().width-msgWidth;
				int startY=-20+AppView.getInstance().getShell().getLocation().y+AppView.getInstance().getShell().getBounds().height-Msg.size()*msgHeight;
				Location=new Point(startX,startY);
				shell.setLocation(Location);
				for(String key:Msg.keySet()){
					IMessage message=Msg.get(key);
					Composite pannel=new Composite(shell,SWT.BORDER);
					pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, false, false, 1, 1, 400, 40));
					pannel.setLayout(LayoutUtils.getComGridLayout(10, 0));
					
					Text textMsg=new Text(pannel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.WRAP);
					textMsg.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 9, 1, 0, 0));
					textMsg.setText(message.getMsgText());
					textMsg.setEditable(false);
					
					Button btnClose=new  Button(pannel,SWT.PUSH);
					btnClose.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1, 0, 0));
					btnClose.setImage(Icons.getDeleteIcon());
					btnClose.setData(message);
					btnClose.addSelectionListener(new RemoveMsgAction());
					
					pannel.pack();
				}
				visiable=true;
				shell.pack();
				shell.open();
			}else{
				visiable=false;
				shell.dispose();
			}
		}
	}
}
