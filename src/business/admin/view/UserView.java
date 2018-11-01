package business.admin.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.USERS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import resource.Constants;
import resource.Context;
import resource.Icons;

import bean.UserBean;

import utils.LayoutUtils;
import utils.StringUtil;


/**
 * @author Administrator
 *
 */
public class UserView extends Composite {

	 public static UserView getUserView(Composite parent){
		 if(unique_instance==null){
	   		 if(parent!=null){
	   			 unique_instance=new UserView(parent);
	   		 }
	 	}
	 	return unique_instance;
	 }

	private UserView(Composite parent){
		super(parent,SWT.NONE);
		 content=this;
		 this.createSearchPanel();
		 this.createTree();
	}
	
	 private void createSearchPanel(){
	    	Composite panel=new Composite(content,SWT.NONE);
	    	panel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 1, 1, 0, 0));
	    	panel.setLayout(LayoutUtils.getComGridLayout(6, 2));
	    	textSearch=new Text(panel,SWT.BORDER);
	    	textSearch.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 5, 1, 0, 0));
	    	textSearch.addKeyListener(new SearchUserAction());
	        Button btnAdd=new Button(panel,SWT.PUSH);	
	        btnAdd.setToolTipText(Constants.getStringVaule("btn_tips_adduser"));
	        btnAdd.setImage(Icons.getAddIcon());
	        btnAdd.setLayoutData(LayoutUtils.getComGridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1, 0, 0));
	        btnAdd.addSelectionListener(new AddUserAction());
	    	panel.pack();
	    }
	 
	 private void createTree(){
	    	
		 userTree=new Tree(content,SWT.MULTI);//
		 userTree.addMouseListener(new ShowUserAction());
		 userTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 DragSource dbNodeSource=new DragSource(userTree,DND.DROP_COPY|DND.DROP_MOVE);
	  		dbNodeSource.addDragListener(new DragSourceListener() {
				public void dragStart(DragSourceEvent event) {
					if(userTree.getSelectionCount()==0){
						event.doit=false;
					}
				}	
				public void dragSetData(DragSourceEvent event) {
					if(FileTransfer.getInstance().isSupportedType(event.dataType)){
						TreeItem[] dragItems=userTree.getSelection();
						String[] items=new String[dragItems.length];
						for(int w=0;w<dragItems.length;w++){
							UserBean user=(UserBean)dragItems[w].getData();
							items[w]=user.getUserID();
						}
						event.data=items;
					}
				}
				public void dragFinished(DragSourceEvent event) {}
			});
	  		dbNodeSource.setTransfer(new Transfer[]{FileTransfer.getInstance()});
	  		//加载用户数据
	  		Users=USERS.getAllUser( Context.session.userID);
	  		if(Users!=null&&Users.size()>0){
	  			for(UserBean user:Users){
	  				 TreeItem  treeUser=new TreeItem(userTree,SWT.SINGLE);
	  				treeUser.setText(user.getShowName());
	  				treeUser.setImage(Icons.getUserIcon());
	  				treeUser.setData(user);
	  			}
	  		}
	  		userTree.pack();
	  		
	 }
	 
	 public class ShowUserAction extends MouseAdapter{
			public void mouseDown(MouseEvent e){
			}
			public void mouseDoubleClick(MouseEvent e){
	 			TreeItem currentItem=userTree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null){
		 			UserBean data=(UserBean)currentItem.getData();
		 			UserPropertyView view=new UserPropertyView();
		 			view.setData(data);
		 			view.setNewAble(false);
		 			view.show();
	 			}
			}
	 }
	 
		public class AddUserAction  extends SelectionAdapter{
			public void widgetSelected(SelectionEvent e){
				UserPropertyView view=new UserPropertyView();
				view.setNewAble(true);
	 			view.show();
			}
	 }
	 
	 public class SearchUserAction implements KeyListener{
			public void keyPressed(KeyEvent e){}
			public void keyReleased(KeyEvent e){
				boolean allUserFlag=false;
				String keyword=textSearch.getText();
				List<UserBean> result=new ArrayList<UserBean>();
				if(!StringUtil.isNullOrEmpty(keyword)){
					int w=0;
					for(UserBean user:Users){
						String userID=user.getUserID().trim();
						String userName=user.getUserName();
						if(userID.startsWith(keyword)||userName.startsWith(keyword)){
						    if(w<=Limit){
						    	result.add(user);
						    	w++;
						    }else{
						    	break;
						    }
						}
		  			}
				}else{
					result.addAll(Users);
					allUserFlag=true;
				}
				userTree.removeAll();
				for(UserBean user:result){
	  				 TreeItem  treeUser=new TreeItem(userTree,SWT.SINGLE);
	  				treeUser.setText(user.getShowName());
	  				treeUser.setImage(Icons.getUserIcon());
	  				treeUser.setData(user);
	  				if(!allUserFlag){
	  					userTree.select(treeUser);
	  				}
	  				//userTree.setFocus();
	  			}
				textSearch.setFocus();
			}
	 }
	 
	 public void refreshUserTree(){
		     userTree.removeAll();
			Users=USERS.getAllUser( Context.session.userID);
			for(UserBean user:Users){
				 TreeItem  treeUser=new TreeItem(userTree,SWT.SINGLE);
				treeUser.setText(user.getShowName());
				treeUser.setImage(Icons.getUserIcon());
				treeUser.setData(user);
			}
	 }
	 
	 
	 private static UserView unique_instance=null;
	 private Composite content=null;
	 private Text textSearch;
	 private Tree userTree;
	 public List<UserBean> Users;
	 public static final int Limit=20;
}
