package business.tester.action;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import business.tester.view.TesterPropertyView;
import business.tester.view.TesterReleaseVersionView;
import business.tester.view.VersionFigure;

import views.AppView;


public class TesterDragAction extends MouseMotionListener.Stub implements MouseListener {
	public TesterReleaseVersionView View;
    public TesterDragAction(IFigure figure,TesterReleaseVersionView view) {
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
    	TesterPropertyView propertyview=figure.getPropertyView();
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
