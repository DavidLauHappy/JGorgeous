package business.dversion.view;


import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import bean.ViewVersionBean;



public class VersionFigure extends ActivityFigure {
	 public FixedAnchor inAnchor, outAnchor;
		
	 public VersionFigure(){
		  	inAnchor = new FixedAnchor(this);
			inAnchor.place = new Point(1, 0);
			targetAnchors.put("in", inAnchor);
			outAnchor = new FixedAnchor(this);
			outAnchor.place = new Point(1, 2);
			sourceAnchors.put("out", outAnchor);    
			

	 }
	 
	 public void paintFigure(Graphics g) {
	        Rectangle r = bounds;
	        g.drawText(message, r.x + r.width / 4, r.y + r.height / 4);
	        g.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
	    }
	 
	 private VersionPropertyView propertyView;

	public VersionPropertyView getPropertyView() {
		return propertyView;
	}

	public void setPropertyView(VersionPropertyView propertyView) {
		this.propertyView = propertyView;
	}
	
	private ViewVersionBean Data;

	public ViewVersionBean getData() {
		return Data;
	}

	public void setData(ViewVersionBean data) {
		Data = data;
	}
	
	 
}
