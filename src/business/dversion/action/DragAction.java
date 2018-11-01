package business.dversion.action;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import views.AppView;

import business.dversion.view.ReleaseVersionView;
import business.dversion.view.VersionFigure;
import business.dversion.view.VersionPropertyView;

public class DragAction extends MouseMotionListener.Stub implements MouseListener {
	public ReleaseVersionView View;
    public DragAction(IFigure figure,ReleaseVersionView view) {
        figure.addMouseMotionListener(this);
        figure.addMouseListener(this);
        this.View=view;
    }

    Point start;
    
    public void mouseReleased(MouseEvent e) {
    }
 
    public void mouseClicked(MouseEvent e) {
    	
    }
 
    public void mouseDoubleClicked(MouseEvent e) {
    	VersionFigure figure=(VersionFigure)e.getSource();
    	org.eclipse.swt.graphics.Point position=AppView.getInstance().getDisplay().getCursorLocation();
    	VersionPropertyView propertyview=figure.getPropertyView();
    	propertyview.setPosition(position);
    	propertyview.showPropertyView();
    }
 
    public void mousePressed(MouseEvent e) {
        start = e.getLocation();
        VersionFigure figure=(VersionFigure)e.getSource();
    	if(e.button==3&&figure!=null){
    		View.setCurrentFigure(figure);
    		View.getCanvas().setMenu(View.getFigurePopMenu());
    	}else{
    		View.getCanvas().setMenu(null);
		}
    }
 
    public void mouseDragged(MouseEvent e) {
        Point p = e.getLocation();
        Dimension d = p.getDifference(start);
        start = p;
        Figure f = ((Figure) e.getSource());
        f.setBounds(f.getBounds().getTranslated(d.width, d.height));
    }
}
