package business.dversion.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import bean.StreamBean;
import resource.Constants;
import resource.Context;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

/**
 * @author David
 * 新建流
 */
public class NewStreamView{

	public Composite self=null;
	public SashForm content=null;
	public Composite parent=null;
	private Text textName,textDesc,textInput;
	public Table fileTable=null;
	private  String[] header=null;
	
	public NewStreamView(Composite com){
		 this.parent=com;
		 self=new Composite(com,SWT.NONE);
		 self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 self.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 
		 content=new SashForm(self,SWT.VERTICAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 this.createToolsView();
		 this.createListView();
		 content.setWeights(new int[]{30,70});
		 self.pack();
	}
	
	private void createToolsView(){
		  	Composite toolPanel=new Composite(content,SWT.NONE);
			toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
			toolPanel.setLayout(LayoutUtils.getComGridLayout(10, 5));
			
			Label labName=new Label(toolPanel,SWT.NONE);
			labName.setText(Constants.getStringVaule("label_name"));
			labName.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1, 0, 0));
			textName=new Text(toolPanel,SWT.BORDER);
			textName.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 6, 1, 0, 0));
			Button btnSumbit=new  Button(toolPanel,SWT.PUSH);
			btnSumbit.setText("   "+Constants.getStringVaule("btn_add")+"   ");
			btnSumbit.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
			btnSumbit.addSelectionListener(new AddStreamAction());
			
			Label labDesc=new Label(toolPanel,SWT.NONE);
			labDesc.setText(Constants.getStringVaule("label_desc"));
			labDesc.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1, 0, 0));
			textDesc=new Text(toolPanel,SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.LEFT|SWT.WRAP);
			textDesc.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 8, 1, 0, 0));
			
			textInput=new Text(toolPanel,SWT.BORDER);
			textInput.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 8, 1, 0, 0));
			Button btnSearch=new  Button(toolPanel,SWT.PUSH);
			btnSearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
			btnSearch.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1, 0, 0));
			
			toolPanel.pack();
	}
	
	private void createListView(){
		    Composite pannelData=new Composite(content,SWT.NONE);
		    pannelData.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    pannelData.setLayout(LayoutUtils.getComGridLayout(1, 0));
		    fileTable=new Table(pannelData,SWT.NONE);
		    fileTable.setHeaderVisible(true);
		    fileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		    fileTable.setLinesVisible(true);
		    fileTable.addListener(SWT.MeasureItem, new Listener(){
					public void handleEvent(Event e){
						e.height=20;
					}
				});
		    header=new String[]{ StringUtil.rightPad(Constants.getStringVaule("header_filename"), 80, " "),
		    								 StringUtil.rightPad(Constants.getStringVaule("header_mdfdate"),50," ") ,
		    								 StringUtil.rightPad(Constants.getStringVaule("header_crtdate"),50," ") ,
		    								 StringUtil.rightPad(Constants.getStringVaule("header_md5"),50," ") };
		    for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(fileTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setMoveable(true);
			}
			
			for(int j=0;j<header.length;j++){		
				fileTable.getColumn(j).pack();
			}	
	    pannelData.pack();
	}
	
	  public class AddStreamAction extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				String name=textName.getText();
				if(StringUtil.isNullOrEmpty(name)){
					String msg="流名称不能为空！";
					MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
					box.setText(Constants.getStringVaule("messagebox_alert"));
					box.setMessage(msg);
					box.open();
					return;
				}else{
					int len=name.length();
					if(len>60){
						String msg="流名称超长，不能超过60个字符！";
						MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
						box.setText(Constants.getStringVaule("messagebox_alert"));
						box.setMessage(msg);
						box.open();
						return;
					}else{
						String desc=textDesc.getText();
						StreamBean stream=new StreamBean();
						boolean ret=stream.nameExist(name);
						if(ret){
							String msg="流名称【"+name+"】已存在，不能重复新增！";
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(msg);
							box.open();
							return;
						}
						else{
							stream.setStreamName(name);
							stream.setStreamDesc(desc);
							stream.setUpdUser(Context.session.userID);
							stream.create();
							String msg="新建流成功！";
							//刷新流管理器
							StreamView.getInstance(null).refreshTree();
							MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
							box.setText(Constants.getStringVaule("messagebox_alert"));
							box.setMessage(msg);
							box.open();
						}
					}
				}
			}
	  }
}
