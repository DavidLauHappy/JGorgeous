package business.tversion.core;

import business.tversion.view.TVersionToolBarView;
import resource.Context;
import resource.Logger;
import utils.FileFinder;
import views.AppView;

public class FileLoader extends Thread{

	public static FileLoader getFileLoader(){
		if(unique_instance==null)
			unique_instance=new FileLoader();
		return unique_instance;
	}
	
	public void run(){
		try{
				this.isFinished=false;
				Context.Files=FileFinder.loadFile();
				this.isFinished=true;
				AppView.getInstance().getDisplay().asyncExec(new Runnable() {
					public void run() {
						//TVersionToolBarView.unique_instance.updateLoadStatus();
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
	private static FileLoader unique_instance;
	private FileLoader(){}
	
	
	private boolean isFinished=false;

}
