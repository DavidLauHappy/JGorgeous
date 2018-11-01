package business.dversion.core;

import business.dversion.view.DVersionToolBarView;
import resource.Context;
import resource.Logger;
import utils.FileFinder;
import views.AppView;

public class DVFileLoader extends Thread{

	public static DVFileLoader getFileLoader(){
		if(unique_instance==null)
			unique_instance=new DVFileLoader();
		return unique_instance;
	}
	
	public void run(){
		try{
				this.isFinished=false;
				Context.Files=FileFinder.loadFile();
				this.isFinished=true;
				AppView.getInstance().getDisplay().asyncExec(new Runnable() {
					public void run() {
						//DVersionToolBarView.unique_instance.updateLoadStatus();
					}
				});
		}
		catch(Exception e){
			Logger.getInstance().error("FileLoader.run()线程执行异常："+e.toString());
		}
	}
	
	public boolean getFinished(){
		return this.isFinished;
	}
	
	  public void exit(){
	    	try{
	    		FileFinder.runable=false;
	    		this.interrupt();
	    	}
	    	catch(Exception e){
	    		Logger.getInstance().error("FileLoader.exit()线程退出异常："+e.toString());
	    	}
	    }
	
	public void refresh(){
		this.run();
	}
	private static DVFileLoader unique_instance;
	private DVFileLoader(){}
	
	
	private boolean isFinished=false;

}
