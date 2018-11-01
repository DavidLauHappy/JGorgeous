package views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import resource.Context;
import utils.LayoutUtils;

/**
 * @author DavidLau
 * @date 2016-8-11
 * @version 1.0
 * 类说明
 */
public class NewFileView {

	public NewFileView(Composite com,String pageContext,String linetext,String filepath)
	{
		self=new ScrolledComposite(com,SWT.V_SCROLL|SWT.H_SCROLL|SWT.BORDER);
		self.addControlListener(new ScrollCompositeResizeAction());
	    self.setAlwaysShowScrollBars(true);
		self.setExpandHorizontal(true);
		self.setExpandVertical(true);
		
     	content=new Composite(self,SWT.BORDER);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(101, 1));
	
		
     	line=new Text(content,SWT.MULTI);
		line.setFont(Context.getConsoleFont());
		line.setText(linetext);
		line.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.FILL, false, true, 1, 1, 40, 0));
		line.setEditable(false);
		
		page=new StyledText(content,SWT.MULTI);
		page.setFont(Context.getConsoleFont());
		page.setText(pageContext);
		page.setData(filepath);
		page.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 100, 1, 0, 0));
		page.addVerifyListener(new ShowLineNoAction());
		
		content.pack();
		self.layout(true);
		self.setContent(content);
		self.pack();
		

	}
	
	public class ShowLineNoAction implements VerifyListener{
		public void verifyText(VerifyEvent e){
			String text=page.getText();
			int length=text.split("\r").length;
			String last=text.split("\r")[length-1];//最后一行内容
			if(last.length()==1)
			{
				length=length-1;
			}
			if(e.keyCode==SWT.CR)
			{
				String linetext=line.getText()+length+"\r\n";
				line.setEditable(true);
				line.setText(linetext);
				line.setEditable(false);	
				line.pack();
			}
			if(e.keyCode==SWT.BS)
			{
				
				String linetext="";
				for(int w=0;w<length;w++)
				{
					linetext+=w+1+"\r\n";
				}
				line.setEditable(true);
				line.setText(linetext);
				line.setEditable(false);
				line.pack();
			}
			
			
		}
	}
	//这个解决ScrollComposite中的子Composite不随ScrollComposite同步放大的问题，可能使效率低
		//如何解决首次就显示滚动条的问题
		public class ScrollCompositeResizeAction extends ControlAdapter
		{
			public void controlResized(ControlEvent e) {
			content.setSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			self.setMinSize(content.getSize());
				
			}
		}
		
	public StyledText page=null;
	public Text line=null;
	public ScrolledComposite self=null;
	public Composite content=null;
}
