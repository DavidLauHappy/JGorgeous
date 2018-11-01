package business.deploy.action;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

public class NodeDragAction implements DragSourceListener{
	public String sourceType;
	
	public NodeDragAction(String type){
		this.sourceType=type;
	}
	
	public void dragStart(DragSourceEvent e)
	{
	}
	
	public void dragSetData(DragSourceEvent e)
	{
		if(TextTransfer.getInstance().isSupportedType(e.dataType))
			e.data=sourceType;
	}
	public void dragFinished(DragSourceEvent e)
	{		
		
	}
}
