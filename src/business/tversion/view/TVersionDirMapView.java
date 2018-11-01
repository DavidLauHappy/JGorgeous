package business.tversion.view;

import java.util.List;

import model.DIR;
import bean.DIRBean;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


import resource.Constants;
import resource.Context;
import resource.Dictionary;
import resource.Icons;
import resource.Item;
import resource.Logger;

import utils.DateUtil;
import utils.LayoutUtils;
import views.AppView;

/**
 * @author David
 * 按应用设置目录
 */
public class TVersionDirMapView {

	private Composite parent;
	public Composite content;
	private Group group;
	private  Combo comboApp;
	public Tree dirTree;
	public TreeItem  treeRoot;
	public String appName;
	public  TreeEditor editor;
	public TreeItem currentItem;
	public TVersionDirMapView(Composite com){
		this.parent=com;
		this.createAndShow();
	}
	
	private void createAndShow(){
		content=new Composite(this.parent,SWT.NONE);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(3, 10));
		
		Composite pannel=new Composite(content,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, false, 3, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(9, 10));
		
		Label labName=new Label(pannel,SWT.NONE);
		labName.setText(Constants.getStringVaule("label_appname"));
		labName.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1, 0, 0));
		comboApp=new Combo(pannel,SWT.DROP_DOWN);
		comboApp.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1, 0, 0));
		comboApp.addSelectionListener(new LoadAppDirs());
		List<Item> Apps=Dictionary.getDictionaryList("APP");
		if(Apps!=null&&Apps.size()>0){
	    	String[] items=new String[Apps.size()];
	    	int i=0;
	    	for(Item bean:Apps){
	    		items[i]=bean.getKey()+" "+bean.getValue();
	    		i++;
	    	}
	    	comboApp.setItems(items);
	    	comboApp.select(0);
		}
		pannel.pack();
		
		dirTree=new Tree(content,SWT.BORDER|SWT.SINGLE);
		dirTree.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 2, 1, 0, 0));
		dirTree.addSelectionListener(new ShowPropertyAction());
		dirTree.addMouseListener(new PopMenuAction());
	    treeRoot=new TreeItem(dirTree,SWT.NONE);
   	 	treeRoot.setText("/");
   	 	treeRoot.setImage(Icons.getFloderIcon());
   	 	dirTree.pack();
   	    loadDirTree();
   	    editor=new TreeEditor(dirTree);
		editor.horizontalAlignment=SWT.LEFT;
		editor.grabHorizontal=true;
		editor.minimumWidth=30;
   	    
	   	 group=new Group(content,SWT.NONE|SWT.V_SCROLL);
		 group.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 group.setLayout(LayoutUtils.getComGridLayout(1, 10));
		 group.setText("目录安装属性");
		content.pack();
	}
	
	
	public class LoadAppDirs extends SelectionAdapter{
 		public void widgetSelected(SelectionEvent e){
 			loadDirTree();
 		}
	}
	
	private void loadDirTree(){
		treeRoot.removeAll();
		String rawAppName=comboApp.getText();
		appName=rawAppName.substring(0, rawAppName.indexOf(" "));
		List<DIRBean> chilren=DIR.getMyChildrenDirs(Context.session.userID, "0", appName);
		if(chilren!=null&&chilren.size()>0){
			for(DIRBean data:chilren){
				TreeItem  treeItem=new TreeItem(treeRoot,SWT.NONE);
				treeItem.setText(data.getName());
				treeItem.setImage(Icons.getFloderIcon());
				treeItem.setData(data);
				recursionLoadTree(treeItem);
				treeItem.setExpanded(true);
			}
		}
		treeRoot.setExpanded(true);
	}
	
	private void recursionLoadTree(TreeItem treeItem){
		DIRBean dir=(DIRBean)treeItem.getData();
		if(dir!=null){
			List<DIRBean> chilren=DIR.getMyChildrenDirs(Context.session.userID, dir.getId(), appName);
			if(chilren!=null&&chilren.size()>0){
				for(DIRBean data:chilren){
					TreeItem  sonItem=new TreeItem(treeItem,SWT.NONE);
					sonItem.setText(data.getName());
					sonItem.setImage(Icons.getFloderIcon());
					sonItem.setData(data);
					recursionLoadTree(sonItem);
					sonItem.setExpanded(true);
				}
			}
		}
	}
	
	  public class PopMenuAction extends MouseAdapter{
	 		public void mouseDown(MouseEvent e){
	 			TreeItem currentItem=dirTree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null&&e.button==3){
	 				DIRBean data=(DIRBean)currentItem.getData();
	 				 if(data==null){
	 					data=new DIRBean();
	 					data.setApp(appName);
	 					data.setName("/");
	 					data.setId("0");
	 					data.setParentID("0");
	 					data.setType(DIRBean.Type.Hierarchy.ordinal()+"");
	 				 }
	 				     Menu actionMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
		 		    	 MenuItem itemAdd=new MenuItem(actionMenu,SWT.PUSH);
		 		    	 itemAdd.setText("新增子目录");
		 		    	 itemAdd.setData(data);
		 		    	 itemAdd.setData("TreeItem", currentItem);
		 		    	 itemAdd.setImage(Icons.getAddIcon());
		 		    	 itemAdd.addSelectionListener(new SelectionAdapter(){ 	
		 	        		 public void widgetSelected(SelectionEvent e){
		 	        			Context.session.inroll(e.getClass().toString(), "F-101-00001");
		 	        			MenuItem item=(MenuItem)e.getSource();
		 	        			DIRBean data=(DIRBean)item.getData();
		 	        			TreeItem parentItem=(TreeItem)item.getData("TreeItem");
		 	        			TreeItem newItem=new TreeItem(parentItem,SWT.NONE);
		 	        			newItem.setText(data.getName());
		 	        			newItem.setImage(Icons.getFloderIcon());
		 	        			parentItem.setExpanded(true);
		 	        			//数据保存
		 	        			String id=DIR.getID(Context.session.userID);
		 	        			DIRBean newData=new DIRBean();
		 	        			newData.setApp(appName);
		 	        			newData.setId(id);
		 	        			newData.setName("新建文件夹");
		 	        			newData.setType(DIRBean.Type.Hierarchy.ordinal()+"");
		 	        			newData.setMdfUser(Context.session.userID);
		 	        			newData.setParentID(data.getId());
		 	        			newData.setMdfTime(DateUtil.getCurrentTime());
		 	        			DIR.addData(newData);
		 	        			newItem.setData(newData);
		 	        			Logger.getInstance().logServer("新增目录：新建文件夹");
		 	        			
		 	        			Text newEditor=new Text(dirTree,SWT.NONE);
		 	        			newEditor.setText("新建文件夹");
		 	        			newEditor.addModifyListener(new ModifyListener(){
									public void modifyText(ModifyEvent e) {
										Text text=(Text)editor.getEditor();
										String newName=text.getText();
										if(newName.equals("新建文件夹")){
											editor.getItem().setText("");
											text.setText("");
										}else{
										   editor.getItem().setText(newName);
										}
									}
		 	        			});
		 	        			newEditor.setFocus();
		 	        			newEditor.addFocusListener(new FocusListener() {
									public void focusLost(FocusEvent e) {
										Context.session.inroll(e.getClass().toString(), "F-101-00002");
										Text text=(Text)e.getSource();
										String name=text.getText();
										DIRBean data=((DIRBean)editor.getItem().getData());
										boolean result=DIR.updateName(data.getApp(),data.getId(),data.getParentID(), Context.session.userID,name);
										if(result){
											Control oldControl=editor.getEditor();
						 					if(oldControl!=null){
						 						oldControl.dispose();
						 					}
						 					data.setName(name);
						 					editor.getItem().setData(data);
						 					Logger.getInstance().logServer("修改目录："+data.getId()+":"+data.getName());
										}else{
											String msg="目录["+text.getText()+"]已存在！请重新命名！";
											MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
											box.setText(Constants.getStringVaule("messagebox_alert"));
											box.setMessage(msg);
											box.open();
										}
									}
									public void focusGained(FocusEvent e) {
										
									}
								});
		 	        			editor.setEditor(newEditor, newItem);
		 	        		 }
		 		    	});
		 		    	 
		 		    	 MenuItem itemDelete=new MenuItem(actionMenu,SWT.PUSH);
		 		    	 itemDelete.setText("删除目录");
		 		    	 itemDelete.setData(data);
		 		    	 itemDelete.setImage(Icons.getDeleteIcon());
		 		    	 itemDelete.addSelectionListener(new SelectionAdapter(){ 	
		 	        		 public void widgetSelected(SelectionEvent e){
		 	        			Context.session.inroll(e.getClass().toString(), "F-101-00003");
		 	        			MenuItem item=(MenuItem)e.getSource();
		 	        			DIRBean data=(DIRBean)item.getData();
		 	        			DIR.delete(data.getId(), data.getMdfUser());
		 	        			Logger.getInstance().logServer("删除目录："+data.getId()+":"+data.getName());
		 	        			loadDirTree();
		 	        			Text text=(Text)editor.getEditor();
		 	        			if(text!=null){
		 	        				text.dispose();
		 	        			}
		 	        		 }
		 		    	});
	 		    	dirTree.setMenu(actionMenu);
	 		    	 
	 			}else{
	 				dirTree.setMenu(null);
	 			}
	 		}
	 		
	 
	 		public void mouseDoubleClick(MouseEvent e){
	 			TreeItem currentItem=dirTree.getItem(new Point(e.x,e.y));
	 			if(currentItem!=null){
	 				DIRBean data=(DIRBean)currentItem.getData();
	 				 if(data!=null){
	 					Control oldControl=editor.getEditor();
	 					if(oldControl!=null){
	 						oldControl.dispose();
	 					}
					Text newEditor=new Text(dirTree,SWT.NONE);
					newEditor.setText(currentItem.getText());
        			newEditor.addModifyListener(new ModifyListener(){
						public void modifyText(ModifyEvent e) {
							Text text=(Text)editor.getEditor();
							String newName=text.getText();
							editor.getItem().setText(newName);
							//TDir data=((TDir)editor.getItem().getData());
							//data.updateName(data.getId(), newName);
						}
        			});
        			newEditor.addFocusListener(new FocusListener() {
						public void focusLost(FocusEvent e) {
							Text text=(Text)e.getSource();
							String name=text.getText();
							DIRBean data=((DIRBean)editor.getItem().getData());
							boolean result=DIR.updateName(data.getApp(),data.getId(),data.getParentID(), Context.session.userID,name);
							if(result){
								Control oldControl=editor.getEditor();
			 					if(oldControl!=null){
			 						oldControl.dispose();
			 					}
			 					data.setName(name);
			 					editor.getItem().setData(data);
							}else{
								String msg="目录["+text.getText()+"]已存在！请重新命名！";
								MessageBox box=new MessageBox(AppView.getInstance().getShell(),SWT.ICON_WARNING|SWT.OK);
								box.setText(Constants.getStringVaule("messagebox_alert"));
								box.setMessage(msg);
								box.open();
							}
						}
						public void focusGained(FocusEvent e) {}
					});
        			
        			newEditor.setFocus();
        			editor.setEditor(newEditor, currentItem);
	 				}
	 			}
	 		}
	  }
	
	 public class ShowPropertyAction extends SelectionAdapter{
	 		public void widgetSelected(SelectionEvent e){
	 			 currentItem=(TreeItem)e.item;
	 			if(currentItem!=null){
	 				DIRBean data=(DIRBean)currentItem.getData();
	 				Control[] children=group.getChildren();
 					if(children!=null&&children.length>0){
 						for(Control control:children){
 							Button btn=(Button)control;
 							Item type=(Item)btn.getData();
 							if(data!=null){
	 							if(data.getType().equals(type.getKey())){
	 								btn.setSelection(true);
	 							}else{
	 								btn.setSelection(false);
	 							}
 							}else{
 								btn.setSelection(false);
 							}
 						}
 					}else{
 						if(data!=null){
 		 					List<Item> dirTypes=Dictionary.getDictionaryList("DIR.TYPE");
 		 					if(dirTypes!=null&&dirTypes.size()>0){
 		 						for(Item type:dirTypes){
 		 							Button btn=new Button(group,SWT.CHECK|SWT.LEFT);
 		 							btn.setData(type);
 		 							btn.setText(type.getValue());
 		 							if(data.getType().equals(type.getKey())){
 		 								btn.setSelection(true);
 		 							}
 		 							btn.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
 		 							btn.addSelectionListener(new SetDirTypeAction());
 		 						}
 		 						group.pack();
 		 					}
 		 				}
 					}
	 			}
	 		}
	 }
	 
	 public class SetDirTypeAction extends SelectionAdapter{
		 public void widgetSelected(SelectionEvent e){
			    DIRBean dir=(DIRBean)currentItem.getData();
			 	Button btnSource=(Button)e.getSource();
			 	Item data=(Item)btnSource.getData();
				if(btnSource.getSelection()){
					Control[] children=group.getChildren();
					for(Control control:children){
							Button btn=(Button)control;
							if(btnSource==btn){
								
							}else{
								btn.setSelection(false);
							}
					}
					dir.setType(data.getKey());
					DIR.updateType(dir.getId(),dir.getMdfUser(),data.getKey());
					currentItem.setData(dir);
				}
		 }
	 }
}
