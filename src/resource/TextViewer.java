package resource;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

/**
 * @author Administrator
 *
 */
public class TextViewer {
	 private ScrolledComposite self=null;
	 private Composite content=null;
	 private Text sourceText;
	 private Shell shell;
	 public StyledText text;
	 private String srcText;
	 private boolean editable=false;
	 public Point point;
	 
	 public TextViewer(Text sourceText,Point point){
		 this.sourceText=sourceText;
		 if(this.sourceText!=null)
			 this.srcText= this.sourceText.getText();
		    this.editable=this.sourceText.getEditable();
		    this.point=point;
	 }
	 
	 public void show(){
		 	shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL|SWT.RESIZE);
			shell.setText(Constants.getStringVaule("window_detail"));
			//shell.setLocation(AppView.getInstance().getCentreScreenPoint());
			shell.setLocation(this.point);
			
			shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
			
			Composite pannel=new  Composite(shell,SWT.BORDER);
			pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
			pannel.setLayout(LayoutUtils.getComGridLayout(4, 1));
		
			Button btnReset=new Button(pannel,SWT.PUSH);
			btnReset.setText("   "+Constants.getStringVaule("btn_reset")+"   ");
			btnReset.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
			btnReset.addSelectionListener(new ResetTextAction());
			
			Button btnSave=new Button(pannel,SWT.PUSH);
			btnSave.setText("   "+Constants.getStringVaule("btn_apply")+"   ");
			btnSave.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
			btnSave.addSelectionListener(new SaveTextAction());
			pannel.pack();
			
			self=new ScrolledComposite(shell,SWT.V_SCROLL|SWT.H_SCROLL|SWT.BORDER);
			self.addControlListener(new ScrollCompositeResizeAction());
			self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    self.setAlwaysShowScrollBars(true);
			self.setExpandHorizontal(true);
			self.setExpandVertical(true);
			
			content=new Composite(self,SWT.BORDER);
			content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			content.setLayout(LayoutUtils.getComGridLayout(1, 1));
			
			text=new StyledText(content,SWT.MULTI);
			text.setFont(Context.getConsoleFont());
			text.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
			text.setText( this.srcText);
			if(!this.editable){
				btnReset.setVisible(false);
				btnSave.setVisible(false);
				text.setEditable(false);
			}else{
				btnReset.setVisible(true);
				btnSave.setVisible(true);
				text.setEditable(true);
			}
			content.pack();
			self.layout(true);
			self.setContent(content);
			self.pack();
			
		 	shell.pack();
			shell.open();
			shell.addShellListener(new ShellCloseAction());
	 }
	 
	 public class SaveTextAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			 String newText=text.getText();
	   			 if(StringUtil.isNullOrEmpty(newText)){
	   				String msg="内容为空，确认清空内容？";
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK|SWT.CANCEL);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					int result=box.open();
					if(SWT.OK==result){
						sourceText.setText("");
		   				shell.dispose();
					}else{
						return;
					}
	   			 }else{
	   				sourceText.setText(newText);
	   				shell.dispose();
	   			 }
	   		}
	 }
	 
	 public class ResetTextAction extends SelectionAdapter{
	   		public void widgetSelected(SelectionEvent e){
	   			text.setText("");
	   		}
	 }
	 
	 public class ShellCloseAction extends ShellAdapter{
			public void shellClosed(ShellEvent e){	
				if(editable){
					sourceText.setText(text.getText());
				}else{
					shell.dispose();
				}
			}
	 }
	 
	 
		public class ScrollCompositeResizeAction extends ControlAdapter{
			public void controlResized(ControlEvent e) {
			content.setSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			self.setMinSize(content.getSize());	
			}
		}
}
