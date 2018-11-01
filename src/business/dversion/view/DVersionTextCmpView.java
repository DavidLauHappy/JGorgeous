package business.dversion.view;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bean.ViewFileBean;

import resource.CompareMo;
import resource.Icons;



import utils.FileUtils;
import utils.LayoutUtils;
import utils.StringUtil;

public class DVersionTextCmpView {
	public Composite self=null;
	public Composite parent=null;
	public SashForm content=null;
	private Table leftFileTable=null;
	private Table rightFileTable=null;
	public List<CompareMo> textLines;
	public DVersionTextCmpView(Composite com,ViewFileBean fileFrom,ViewFileBean fileTo){
		 this.parent=com;
		 self=new Composite(com,SWT.NONE);
		 self.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 self.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 content=new SashForm(self,SWT.HORIZONTAL);
		 content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		 content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		 textLines=FileUtils.compareAll(fileFrom.getLocation(), fileTo.getLocation());
		 this.createLeftView(fileFrom);
		 this.createRightView(fileTo);
		 content.setWeights(new int[]{50,50});
		 self.pack();
	}
	
	private void createLeftView(ViewFileBean fileFrom){
		Composite pannel=new Composite(content,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
		Text labVersion=new Text(pannel,SWT.NONE);
		String label="v"+StringUtil.leftpad(fileFrom.getVersionID(), 4, "0")+"/"+fileFrom.getFileName();
		labVersion.setText(label);
		labVersion.setToolTipText(label);
		labVersion.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
		labVersion.setEditable(false);
		
		leftFileTable=new Table(pannel,SWT.FULL_SELECTION|SWT.MULTI);
		leftFileTable.setHeaderVisible(true);
		leftFileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		leftFileTable.setLinesVisible(true);
		leftFileTable.setCapture(true);
		leftFileTable.getVerticalBar().addSelectionListener(new LeftWhleeVScrollAction());
		leftFileTable.addSelectionListener(new SyncRightPositionAction());
		leftFileTable.addListener(SWT.MeasureItem, new Listener(){
			public void handleEvent(Event e){
				e.height=20;
			}
		});
		String[] header=new String[]{fileFrom.getLocation()};
		for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(leftFileTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setToolTipText(header[i]);
				tablecolumn.setMoveable(true);
		}
		if(textLines!=null&&textLines.size()>0){
			for(CompareMo line:textLines){
				TableItem tableItem=new TableItem(leftFileTable,SWT.BORDER);
				String text=line.getOldText();
				text=StringUtil.ltrim(text, "<");
				tableItem.setText(new String[]{text});
				if(!line.getTag().equals(CompareMo.Result_EQUAL)){
					  tableItem.setBackground(Icons.getDiffColor());
				}
				 tableItem.setData(line);
			}
		}
		for(int j=0;j<header.length;j++){		
		   leftFileTable.getColumn(j).pack();
		}	
		pannel.pack();
	}
	
	private void createRightView(ViewFileBean fileTo){
		Composite pannel=new Composite(content,SWT.NONE);
		pannel.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		pannel.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
		Text labVersion=new Text(pannel,SWT.NONE);
		String label="v"+StringUtil.leftpad(fileTo.getVersionID(), 4, "0")+"/"+fileTo.getFileName();
		labVersion.setText(label);
		labVersion.setToolTipText(label);
		labVersion.setLayoutData(LayoutUtils.getComGridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1, 0, 0));
		labVersion.setEditable(false);
		
		rightFileTable=new Table(pannel,SWT.FULL_SELECTION|SWT.MULTI);
		rightFileTable.setHeaderVisible(true);
		rightFileTable.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		rightFileTable.setLinesVisible(true);
		rightFileTable.setCapture(true);
		rightFileTable.getVerticalBar().addSelectionListener(new RightWhleeVScrollAction());
		rightFileTable.addSelectionListener(new SyncLeftPositionAction());
		rightFileTable.addListener(SWT.MeasureItem, new Listener(){
			public void handleEvent(Event e){
				e.height=20;
			}
		});
		String[] header=new String[]{fileTo.getLocation()};
		for(int i=0;i<header.length;i++){
				TableColumn tablecolumn=new TableColumn(rightFileTable,SWT.BORDER);
				tablecolumn.setText(header[i]);
				tablecolumn.setToolTipText(header[i]);
				tablecolumn.setMoveable(true);
		}
		if(textLines!=null&&textLines.size()>0){
			for(CompareMo line:textLines){
				TableItem tableItem=new TableItem(rightFileTable,SWT.BORDER);
				String text=line.getNewText();
				text=StringUtil.rtrim(text, ">");
				tableItem.setText(new String[]{text});
				if(!line.getTag().equals(CompareMo.Result_EQUAL)){
					  tableItem.setBackground(Icons.getDiffColor());
				}
				 tableItem.setData(line);
			}
		}
		for(int j=0;j<header.length;j++){		
			rightFileTable.getColumn(j).pack();
		}	
		pannel.pack();
	}
	
	public class SyncRightPositionAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			TableItem currentItem=(TableItem)e.item;
   			if(currentItem!=null){
   				CompareMo line=(CompareMo)currentItem.getData();
   				int lineNo=line.getId()-1;
   				rightFileTable.setSelection(lineNo);
   				int index=leftFileTable.getTopIndex();
   				rightFileTable.setTopIndex(index);
   			}
   		}
	}
	
	public class SyncLeftPositionAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			TableItem currentItem=(TableItem)e.item;
   			if(currentItem!=null){
   				CompareMo line=(CompareMo)currentItem.getData();
   				int lineNo=line.getId()-1;
   				leftFileTable.setSelection(lineNo);
   				int index=rightFileTable.getTopIndex();
   				leftFileTable.setTopIndex(index);
   			}
   		}
	}
   		
	public class LeftWhleeVScrollAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			int index=leftFileTable.getTopIndex();
			rightFileTable.setTopIndex(index);
   		}
	}
	
	public class RightWhleeVScrollAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			int index=rightFileTable.getTopIndex();
   			leftFileTable.setTopIndex(index);
   		}
	}
	
	public class LeftWhleeHScrollAction extends SelectionAdapter{
   		public void widgetSelected(SelectionEvent e){
   			int index=leftFileTable.getHorizontalBar().getSelection();
   			rightFileTable.getHorizontalBar().setIncrement(index);
   		}
	}
	
	
	
}
