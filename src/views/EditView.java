package views;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

public abstract class EditView {
	public abstract void showEditView();
	public abstract boolean getTabState(String name);
	public abstract CTabFolder getTabFloder();
	public  abstract void  setTabItems(Composite com,String tabName);
}
