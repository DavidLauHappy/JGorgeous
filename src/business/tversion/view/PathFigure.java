package business.tversion.view;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.swt.SWT;

public class PathFigure extends PolylineConnection {
    public PathFigure() {
    	setTargetDecoration(new PolylineDecoration());
	    setConnectionRouter(new BendpointConnectionRouter());
		setTargetDecoration(new PolygonDecoration());
		//setLineStyle(SWT.LINE_SOLID);
		setLineStyle(SWT.LINE_SOLID);
		setLineWidth(2);	
                 
    }

}
