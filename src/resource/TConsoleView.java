package resource;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import utils.LayoutUtils;
import views.AppView;

import bean.LOCALNODEBean;

public class TConsoleView {
	 private LOCALNODEBean data=null;
	 private  ScrolledComposite content=null;
	 
	  public Composite getContent(){
		  return content;
	  }
	  
	  private Text text=null;
	  
	  public  TConsoleView(Composite com,LOCALNODEBean data){
  	    this.data=data;
  		content=new ScrolledComposite(com,SWT.V_SCROLL|SWT.H_SCROLL);
		content.setAlwaysShowScrollBars(true);
		content.setExpandHorizontal(true);
		content.setExpandVertical(true);
		content.setMinSize(Constants.sizeCanvasWidth,Constants.sizeCanvasHeigth);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
		text=new Text(content,SWT.MULTI);
		text.setBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_BLACK));
		text.setForeground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		text.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		text.setFont(Context.getConsoleFont());//设置字体
		text.setEditable(false);
		content.layout(true);
		content.setContent(text);
		content.pack();
	  }
	  
	  public void setConsoleText(String text){
    	  this.text.setEditable(true);
    	  String oldText=this.text.getText();
    	  String content=text+""+"\r\n"+oldText;//文本要使用追加的方式，而且是追加在前面
    	  this.text.setText(content);
    	  this.text.setEditable(false);
      }
}
