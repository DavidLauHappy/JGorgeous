package resource;

import java.util.List;
import java.util.Map;

import model.QUESTIONS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bean.QUESTIONSBean;


import utils.LayoutUtils;
import utils.StringUtil;
import views.EditView;
import views.EditViewFactory;

public class QuestionManageView {
	private Composite parent;
	public Composite content;
	public Table resultTable=null;
	public String[] header=new String[]{StringUtil.rightPad(StringUtil.leftpad(Constants.getStringVaule("label_qid"),10," "),20," "),
															StringUtil.rightPad(StringUtil.leftpad(Constants.getStringVaule("label_suser"),8," "),16," "),
															StringUtil.rightPad(StringUtil.leftpad(Constants.getStringVaule("label_stime"),13," "),30," "),
															StringUtil.rightPad(StringUtil.leftpad(Constants.getStringVaule("label_qown"),8," "),16," "),
															StringUtil.rightPad(StringUtil.leftpad(Constants.getStringVaule("label_qstatus"),10," "),20," "),
															StringUtil.rightPad(StringUtil.leftpad(Constants.getStringVaule("label_qreq"),20," "),40," "),
															StringUtil.rightPad(StringUtil.leftpad(Constants.getStringVaule("label_qdesc"),30," "),60," ")
			};
	
	public QuestionManageView(Composite com){
		this.parent=com;
		content=new  Composite(com,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		this.createAndShow();
		content.pack();
	}
	
	public Button btnAdd,btnMore=null;
	public Text textProcess,textQID,textIndex=null;
	private void createAndShow(){
		Composite toolPanel=new Composite(content,SWT.NONE);
		toolPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
		toolPanel.setLayout(LayoutUtils.getComGridLayout(9, 5)); 
		Label labCurrentUser=new Label(toolPanel,SWT.NONE);
		labCurrentUser.setText(Constants.getStringVaule("label_processor"));
		labCurrentUser.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textProcess=new Text(toolPanel,SWT.BORDER);
		textProcess.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		String  user=Context.session.userName+" "+Context.session.userID;
		textProcess.setText(user);
		Button btnChoose=new  Button(toolPanel,SWT.PUSH);
		btnChoose.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnChoose.setText("   "+Constants.getStringVaule("btn_choose")+"   ");
		btnChoose.addSelectionListener(new ChooseUserAction());
		
		Label labQId=new Label(toolPanel,SWT.NONE);
		labQId.setText(Constants.getStringVaule("label_qid"));
		labQId.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1, 0, 0));
		textQID=new Text(toolPanel,SWT.BORDER);
		textQID.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.CENTER, true, false, 2, 1, 0, 0));
		
		Button btnRearch=new  Button(toolPanel,SWT.PUSH);
		btnRearch.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1, 0, 0));
		btnRearch.setText("   "+Constants.getStringVaule("btn_search")+"   ");
		btnRearch.addSelectionListener(new RearchQuestionAction());
		toolPanel.pack();
		
		Composite dataPanel=new Composite(content,SWT.NONE);
		dataPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		dataPanel.setLayout(LayoutUtils.getComGridLayout(1, 5)); 
		 if((Constants.RoleType.Tester.ordinal()+"").equals(Context.session.roleID)){
			 Composite actionPanel=new Composite(dataPanel,SWT.NONE);
			  actionPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 28));
			  actionPanel.setLayout(LayoutUtils.getComGridLayout(10, 0));
			  btnAdd=new  Button(actionPanel,SWT.PUSH);
			  btnAdd.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.BEGINNING, false, false, 1, 1, 0, 0));
			  btnAdd.setImage(Icons.getAddIcon());
			  btnAdd.addSelectionListener(new AddQuestionAction());
			  Label labAdd=new Label(actionPanel,SWT.NONE);
			  labAdd.setText(Constants.getStringVaule("btn_add"));
			  labAdd.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
			  actionPanel.pack();
		}
		 
		 
		resultTable=new Table(dataPanel,SWT.BORDER|SWT.FULL_SELECTION);
		resultTable.setHeaderVisible(true);
		resultTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true,1, 1, 0, 0));
		resultTable.setLinesVisible(true);
		resultTable.addMouseListener(new ShowQuestDetailAction());
		
		resultTable.addListener(SWT.MeasureItem, new Listener(){
			public void handleEvent(Event e){
				e.height=20;
			}
		});
		for(int i=0;i<header.length;i++){
			TableColumn tablecolumn=new TableColumn(resultTable,SWT.BORDER);
			tablecolumn.setText(header[i]);
			tablecolumn.setMoveable(true);
		}
		for(int j=0;j<header.length;j++){		
			resultTable.getColumn(j).pack();
		}	
		resultTable.pack();
		dataPanel.pack();
		Composite buttomPanel=new Composite(content,SWT.NONE);
		buttomPanel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 28));
		buttomPanel.setLayout(LayoutUtils.getComGridLayout(3, 5)); 
		Label labTip=new Label(buttomPanel,SWT.NONE);
		labTip.setText("¼ÇÂ¼ÐÅÏ¢:");
		labTip.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		textIndex=new Text(buttomPanel,SWT.NONE);
		textIndex.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.TOP, false, false, 1, 1, 0, 0));
		textIndex.setText("0/0");
		btnMore=new  Button(buttomPanel,SWT.PUSH);
		btnMore.setLayoutData(LayoutUtils.getComGridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1, 0, 0));
		btnMore.setImage(Icons.getMoreIcon());
		btnMore.setVisible(false);
		btnMore.setToolTipText(Constants.getStringVaule("btn_more"));
		btnMore.addSelectionListener(new LoadMoreDataAction());
		buttomPanel.pack();
	}
	
	public class ChooseUserAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			UserChoose control=new UserChoose(textProcess);
   		}
	}
	
	public static int PageSize=30;
	public class LoadMoreDataAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			String userCnd="";
  			 String qID=textQID.getText();
  			 String userID=textProcess.getText();
  			 if(!StringUtil.isNullOrEmpty(userID)){
  				userID=userID.substring(userID.indexOf(" "));
  				userID=userID.trim();
	   			 if((Constants.RoleType.Tester.ordinal()+"").equals(Context.session.roleID)){
	   				userCnd=" QUESTIONS.CRT_USER='@userid'";
	   			  }else if((Constants.RoleType.Tester.ordinal()+"").equals(Context.session.roleID)){
	   				userCnd=" QUESTIONS.CUR_USER='@userid'";
	   			  }else{
	   				userCnd="'@userid'='@userid'";
	   			  }
	   			userCnd=userCnd.replace("@userid", userID);
  			 }else{
  				userCnd="'@userid'=''";
  				userCnd=userCnd.replace("@userid", "");
  			 }
  			List<QUESTIONSBean> questions=QUESTIONS.getQuestionSearch(qID, userCnd, currentIndex, currentIndex+PageSize);
    		   if(questions!=null&&questions.size()>0){
    			   Map<String,String> QStatus=Dictionary.getDictionaryMap("QUESTIONS.QSTATUS");
    			   for(QUESTIONSBean que:questions){
    				     String status=QStatus.get(que.getQstatus());
    				     TableItem tableItem=new TableItem(resultTable,SWT.BORDER);
    				     tableItem.setText(new String[]{que.getId(),que.getCrtUserFull(),que.getCrtTime(),que.getCurUserFull(),status,que.getReqFullName(),que.getQdesc()});
    				     tableItem.setData(que);
    			   }
    			   currentIndex+=questions.size();
    			textIndex.setText((currentIndex+"")+"/"+allCount+"");
    			
    		   }
   		}
	}
	public int currentIndex=0;
	public int allCount=0;
	public class RearchQuestionAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			currentIndex=0;
   			allCount=0;
   			resultTable.removeAll();
   			 String userCnd="";
   			 String qID=textQID.getText();
   			 String userID=textProcess.getText();
   			 if(!StringUtil.isNullOrEmpty(userID)){
   				userID=userID.substring(userID.indexOf(" "));
   				userID=userID.trim();
	   			 if((Constants.RoleType.Tester.ordinal()+"").equals(Context.session.roleID)){
	   				userCnd=" QUESTIONS.CRT_USER='@userid'";
	   			  }else if((Constants.RoleType.Tester.ordinal()+"").equals(Context.session.roleID)){
	   				userCnd=" QUESTIONS.CUR_USER='@userid'";
	   			  }else{
	   				userCnd="'@userid'='@userid'";
	   			  }
	   			userCnd=userCnd.replace("@userid", userID);
   			 }else{
   				userCnd="'@userid'=''";
   				userCnd=userCnd.replace("@userid", "");
   			 }
   			allCount=QUESTIONS.getQuestionSearchCount(qID, userCnd);
   			if(allCount>PageSize){
   				btnMore.setVisible(true);
   			}
   			List<QUESTIONSBean> questions=QUESTIONS.getQuestionSearch(qID, userCnd, currentIndex, currentIndex+PageSize);
   		   if(questions!=null&&questions.size()>0){
   			   Map<String,String> QStatus=Dictionary.getDictionaryMap("QUESTIONS.QSTATUS");
   			   for(QUESTIONSBean que:questions){
   				     String status=QStatus.get(que.getQstatus());
   				     TableItem tableItem=new TableItem(resultTable,SWT.BORDER);
   				     tableItem.setText(new String[]{que.getId(),que.getCrtUserFull(),que.getCrtTime(),que.getCurUserFull(),status,que.getReqFullName(),que.getQdesc()});
   				     tableItem.setData(que);
   			   }
   			currentIndex+=questions.size();
   			textIndex.setText((currentIndex+"")+"/"+allCount+"");
   		   }
   		}
	}
	
	public class ShowQuestDetailAction extends MouseAdapter{
 		public void mouseDown(MouseEvent e){}
		 public void mouseDoubleClick(MouseEvent e){
   			TableItem currentItem=resultTable.getItem(new Point(e.x,e.y));
			 if(currentItem!=null){
				 	QUESTIONSBean data=(QUESTIONSBean)currentItem.getData();
				 	EditView editView=EditViewFactory.getEditView(null, Context.session.roleID);
					if(!editView.getTabState(data.getId())){
						QuestionDetailView qdetailTest=new QuestionDetailView(data,"1",editView.getTabFloder());
						editView.setTabItems(qdetailTest.content, data.getId());
					}
			 }
   		}
	}
	
	public class AddQuestionAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			  QUESTIONSBean bean=new QUESTIONSBean();
   			  EditView editView=EditViewFactory.getEditView(null, Context.session.roleID);
	   			if(!editView.getTabState(bean.getId())){
	   				QuestionDetailView qdetailTest=new QuestionDetailView(bean,"0",editView.getTabFloder());
	   				editView.setTabItems(qdetailTest.content, bean.getId());
	   			}
   		}
	}
}
