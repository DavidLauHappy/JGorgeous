package common.controls;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import utils.LayoutUtils;

/**
 * @author Administrator
 *  支持多选的下拉选择框
 */
public class DavidCombo extends Composite{

	private Text _text=null;
	private List _list=null;
	private Shell _shell=null;
	private String[] _items=null;
	private int[] _selection=null;
	private boolean clicked=false;
	
	public DavidCombo(Composite parent,int style){
		super(parent,style);
		init();
	}

	private void init(){
		this.setLayout(LayoutUtils.getComGridLayout(1, 1));
		_text=new Text(this,SWT.BORDER|SWT.READ_ONLY);
		_text.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		_text.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent event){
				clicked=true;
				super.mouseDown(event);
				initShell();
			}
		});
	}
	
	private void initShell(){
		 Point p = _text.getParent().toDisplay(_text.getLocation()); 
		 Point size = _text.getSize(); 
		 Rectangle shellRect = new Rectangle(p.x, p.y + size.y, size.x, 0); 
		 _shell = new Shell(DavidCombo.this.getShell(), SWT.NO_TRIM); 
		 GridLayout gl = new GridLayout(); 
		  gl.marginBottom = 2; 
		  gl.marginTop = 2; 
		  gl.marginRight = 2; 
		  gl.marginLeft = 2; 
		  gl.marginWidth = 0; 
		  gl.marginHeight = 0; 
		  _shell.setLayout(gl); 
		  _list = new List(_shell, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL); 
		  for (String value : _items) { 
				 _list.add(value); 
			 } 
		  if(_selection!=null)
			  _list.setSelection(_selection); 
		  GridData gd = new GridData(GridData.FILL_BOTH); 
		  _list.setLayoutData(gd); 
		  _shell.setSize(shellRect.width, 100); 
		  _shell.setLocation(shellRect.x, shellRect.y); 
		  _list.addMouseListener(new MouseAdapter() { 
				 public void mouseUp(MouseEvent event) { 
					 super.mouseUp(event); 
					 _selection = _list.getSelectionIndices(); 
					 if ((event.stateMask & SWT.CTRL) == 0) { 
						 _shell.dispose(); 
						 displayText(); 
					 } 
				 } 
			 }); 
		  _shell.addShellListener(new ShellAdapter() {
				 public void shellDeactivated(ShellEvent e) { 
					 if (_shell != null && !_shell.isDisposed()) { 
						 _selection = _list.getSelectionIndices(); 
						 displayText(); 
						 _shell.dispose(); 
					 } 
				 } 
			 }); 
		  _shell.open(); 
	}
	
	 private void displayText(){ 
		 if(_selection!=null && _selection.length>0){ 
			 StringBuffer sb = new StringBuffer(); 
			 for(int i=0;i<_selection.length;i++){ 
				 if(i>0)sb.append(","); 
				 sb.append(_items[_selection[i]]); 
			 } 
			 _text.setText(sb.toString()); 
			 _text.setToolTipText(sb.toString());
		 }else{ 
			 _text.setText(""); 
		 } 
	 }
	
	public void setItems(String[] _items) {
		this._items = _items;
	}

	public void setSelection(int[] _selection) {
		this._selection = _selection;
	}
	
	public void setSelectionItems(String[] selections){
		java.util.List<Integer> indices=new ArrayList<Integer>();
		for(int w=0;w<this._items.length;w++){
			for(String item:selections){
				if(item.equals(_items[w])){
					indices.add(w);
				}
			}
		}
		int[] selection=new int[indices.size()];
		for(int n=0;n<indices.size();n++){
			selection[n]=indices.get(n);
		}
	  this._selection=selection;
	  if(!this.clicked)
		  displayText();
	}
	
	/*public int[] getSelection() {
		return _selection;
	}

	public String[] getItems() {
		return _items;
	}*/
	
	public String getText(){
		return _text.getText();
	}
}
