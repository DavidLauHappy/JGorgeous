package business.tversion.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.VIEW;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import bean.ViewBean;
import bean.ViewVersionBean;
import business.tversion.view.VersionFigure;
import business.tversion.action.DataHelper;
import business.tversion.action.TVersionDragAction;

import resource.Constants;
import resource.Icons;
import utils.LayoutUtils;
import views.AppView;


public class TReleaseVersionView {
	public  Composite content=null;
	private ScrolledComposite scollCanvas=null;
	private  FigureCanvas canvas=null;
	private  LightweightSystem lws=null;
	private  FreeformLayeredPane drawBoard=null;
	private  ViewBean data;
	private Map<String,VersionFigure> Nodes;
	private VersionFigure currentFigure;
	public TReleaseVersionView(Composite com,ViewBean viewData){
		this.data=viewData;
		Nodes=new HashMap<String,VersionFigure>();
		content=new Composite(com,SWT.BORDER);
		content.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		content.setLayout(LayoutUtils.getComGridLayout(1, 1));
		createDrawView();
		content.pack();
	}
	
	private void createDrawView(){
		scollCanvas=new ScrolledComposite(content,SWT.V_SCROLL|SWT.H_SCROLL|SWT.BORDER);
		scollCanvas.setAlwaysShowScrollBars(true);
		scollCanvas.setExpandHorizontal(true);
		scollCanvas.setExpandVertical(true);
		scollCanvas.setMinSize(Constants.sizeCanvasWidth,Constants.sizeCanvasHeigth);
		scollCanvas.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		scollCanvas.setLayout(LayoutUtils.getComGridLayout(1, 1));
		
		canvas=new FigureCanvas(scollCanvas,SWT.NONE);
		canvas.setLayoutData(LayoutUtils.getComGridData(SWT.FILL, SWT.FILL, true, true, 1, 1, 0, 0));
		canvas.setBackground(AppView.getInstance().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		canvas.pack();
		
		scollCanvas.layout(true);
		scollCanvas.setContent(canvas);
		scollCanvas.pack();
		
	    //draw2d部分
		lws= new LightweightSystem(canvas);
		drawBoard=new FreeformLayeredPane();
		drawBoard.setLayoutManager(new FreeformLayout());
		drawBoard.setBorder(new MarginBorder(5));
		drawBoard.setBackgroundColor(ColorConstants.white);
		drawBoard.setOpaque(true);
		lws.setContents(drawBoard);
		
		List<ViewVersionBean> versions=VIEW.getVersions(data.getViewID());
		int x=200;
		int y=50;
		int width=100; 
		int height=50;
		for(ViewVersionBean version:versions){
			VersionFigure figure=new VersionFigure();
			figure.setName(version.getShowVersion());
			figure.setToolTip(new Button(version.getViewName()));  
			Rectangle rect=new Rectangle(x,y,width,height);
			figure.setBounds(rect);
			figure.setData(version);
			y=y+100;
			TVersionPropertyView propertyView=new TVersionPropertyView(version);
			figure.setPropertyView(propertyView);
			new  TVersionDragAction(figure,this);
			this.drawBoard.add(figure);
			Nodes.put(version.getVersion(), figure);
		}
		//线条
		int versionCount=versions.size();
		int nextVersion=0;
		if(versionCount>=2){
			for(int w=versionCount-1;w>0;w--){
				PathFigure Path=new PathFigure();
				VersionFigure startFigure=Nodes.get(Integer.toString(w));
				nextVersion=w-1;
				VersionFigure endFigure=Nodes.get(Integer.toString(nextVersion));
				Path.setSourceAnchor(startFigure.outAnchor);
				Path.setTargetAnchor(endFigure.inAnchor);
				 this.drawBoard.add(Path);
			}
		}
	  //draw2d部分
	}

	public FigureCanvas getCanvas() {
		return canvas;
	}
	
	private Menu uniqueMenu;
	public Menu getFigurePopMenu(){
		if(uniqueMenu==null){
				uniqueMenu=new Menu(AppView.getInstance().getShell(),SWT.POP_UP);
				MenuItem itemVersion=new MenuItem(uniqueMenu,SWT.PUSH);
				itemVersion.setText(Constants.getStringVaule("menuitem_version"));
				itemVersion.setImage(Icons.getDetailIcon());
				itemVersion.addSelectionListener(new SelectionAdapter() { 
					public void widgetSelected(SelectionEvent e){
						if(currentFigure!=null){
							ViewVersionBean data=currentFigure.getData();
							String itemName=data.getViewName()+"_"+data.getShowVersion();
	        	   			if(!TVersionEditView.getInstance(null).getTabState(itemName)){
	        	   				TViewVersionView versionView=new TViewVersionView(TVersionEditView.getInstance(null).getTabFloder(),data);
	        	   				TVersionEditView.getInstance(null).setTabItems(versionView.self, itemName);
	        	   			}
						}
				 	}
				});
				MenuItem itemSeperator=new MenuItem(uniqueMenu,SWT.SEPARATOR);
				MenuItem itemCmp=new MenuItem(uniqueMenu,SWT.PUSH);
				itemCmp.setText(Constants.getStringVaule("menuitem_cmpversion"));
				itemCmp.setImage(Icons.getCmpVerIcon());
				itemCmp.addSelectionListener(new SelectionAdapter() { 
					public void widgetSelected(SelectionEvent e){
						ViewVersionBean data=currentFigure.getData();
						String itemName=data.getViewName()+":"+Constants.getStringVaule("menuitem_cmpversion");
						if(!TVersionEditView.getInstance(null).getTabState(itemName)){
							TVersionCompareView compareView=new TVersionCompareView(TVersionEditView.getInstance(null).getTabFloder());
							 compareView.setDataLeft(data);
							 TVersionEditView.getInstance(null).setTabItems(compareView.self, itemName);
						}
				 	}
				});
		}
		return uniqueMenu;
	}

	public void setCurrentFigure(VersionFigure currentFigure) {
		this.currentFigure = currentFigure;
	}
	
}
