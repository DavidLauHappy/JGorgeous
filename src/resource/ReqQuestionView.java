package resource;

import java.util.List;

import model.QUESTIONS;
import model.TASK;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import utils.LayoutUtils;
import utils.StringUtil;
import views.AppView;

import bean.QUESTIONSBean;
import bean.TaskBean;


/**
 * @author Administrator
 * 需求对应的问题列表(版本提交中)
 */
public class ReqQuestionView {
	private  Shell shell=null;
	private TaskBean req=null;
	private List<String> result=null;
	
	public  ReqQuestionView(TaskBean req,List<String> src){
		this.req=req;
		this.result=src;
	}
	
	public Combo cboReq=null;
	public Table quesTable=null;
	public List<TaskBean> reqs=null;
	public void show(){
		shell=new Shell(AppView.getInstance().getShell(),SWT.CLOSE|SWT.SYSTEM_MODAL);
		shell.setText(Constants.getStringVaule("window_myquestions"));
		shell.setLocation(AppView.getInstance().getCentreScreenPoint());
		shell.setLayout(LayoutUtils.getComGridLayout(1, 0));
		
		Composite Panel=new Composite(shell,SWT.BORDER);
		Panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		Panel.setLayout(LayoutUtils.getComGridLayout(4, 5)); 
		
		 Label labReq=new Label(Panel,SWT.NONE);
		 labReq.setText(Constants.getStringVaule("label_qreq"));
		 labReq.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1, 0, 0));
		 cboReq=new Combo(Panel,SWT.DROP_DOWN);
		 cboReq.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		 cboReq.addSelectionListener(new LoadReqAction());
		  reqs=TASK.getMyTasks(Context.session.userID);
		 if(reqs!=null&&reqs.size()>0){
			 String[] items=new String[reqs.size()];
			 for(int w=0;w<reqs.size();w++){
				 items[w]=reqs.get(w).getTname();
				 if(items[w].equals(this.req.getTname()))
					 cboReq.select(w);
			 }
			 cboReq.setItems(items);
		 }
		 Button btnOk=new   Button(Panel,SWT.PUSH);
		 btnOk.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.BEGINNING, false, false, 1, 1, 0, 0));
		 btnOk.setImage(Icons.getOkIcon());
		 btnOk.setToolTipText(Constants.getStringVaule("btn_ok"));
		 btnOk.addSelectionListener(new SaveListAction());
		 quesTable=new Table(Panel,SWT.CHECK |SWT.FULL_SELECTION);
		 quesTable.setHeaderVisible(true);
		 quesTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true,4, 1, 0, 100));
		 quesTable.setLinesVisible(true);
		 quesTable.addListener(SWT.MeasureItem, new Listener(){
				public void handleEvent(Event e){
					e.height=20;
				}
			});
		 String[] header=new String[]{StringUtil.rightPad(StringUtil.leftpad("问题编号",8," "),16," "),
				 									 StringUtil.rightPad(StringUtil.leftpad("问题描述",50," "),100," ")};
		 for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(quesTable,SWT.BORDER);
				String lenheader=StringUtil.rightPad(StringUtil.leftpad(header[i], 30, " "),60," ");
				tablecolumn.setText(lenheader);
			}
		 List<QUESTIONSBean> questions=QUESTIONS.getQuestionByReq(this.req.getId());
		 if(questions!=null&&questions.size()>0){
			 for(QUESTIONSBean ques:questions){
				 TableItem tableItem=new TableItem(quesTable,SWT.BORDER);
				 tableItem.setText(new String[]{ques.getId(),ques.getQdesc()});
				 tableItem.setData(ques);
				 if("6".equals(ques.getQstatus()))
					 tableItem.setChecked(true);
			 }
		 }
		 
		 for(int j=0;j<header.length;j++){		
			 quesTable.getColumn(j).pack();
			}	
		 quesTable.pack();
		Panel.pack();
		shell.pack();
		shell.open();
	}
	
	public class ShellCloseAction extends ShellAdapter{
		public void shellClosed(ShellEvent e){	
			TableItem[] items=quesTable.getItems();
			if(items!=null&&items.length>0){
				for(TableItem item:items){
					if(item.getChecked()){
						QUESTIONSBean data=(QUESTIONSBean)item.getData();
						data.reSetStatus("6");
					}
				}
			}
			shell.dispose();
		}
	}
	
	public class SaveListAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			TableItem[] items=quesTable.getItems();
			if(items!=null&&items.length>0){
				for(TableItem item:items){
					if(item.getChecked()){
						QUESTIONSBean data=(QUESTIONSBean)item.getData();
						data.reSetStatus("6");
					}
				}
				String msg="问题关联发版成功！";
				MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				box.setText(Constants.getStringVaule("alert_successoperate"));
				box.setMessage(msg);
				int result=box.open();
				if(SWT.OK==result){
					shell.dispose();
				}
			}
		}
	}
	
	public class LoadReqAction  extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			quesTable.removeAll();
		    String reqName=cboReq.getText();
			 if(reqs!=null&&reqs.size()>0){
				 for(TaskBean req:reqs){
					 if(req.getTname().equals(reqName)){
						 List<QUESTIONSBean> questions=QUESTIONS.getQuestionByReq(req.getId());
						 if(questions!=null&&questions.size()>0){
							 for(QUESTIONSBean ques:questions){
								 TableItem tableItem=new TableItem(quesTable,SWT.BORDER);
								 tableItem.setText(new String[]{ques.getId(),ques.getQdesc()});
								 tableItem.setData(ques);
								 if("6".equals(ques.getQstatus()))
									 tableItem.setChecked(true);
							 }
						 }
						 break;
					 }
				 }
			 } 
		}
	}
}
