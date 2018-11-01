package business.tester.core;

import business.tester.view.TesterToolBarView;
import resource.Context;
import resource.Logger;
import utils.FileFinder;
import views.AppView;

public class TFileLoader  extends Thread{
	public static TFileLoader getFileLoader(){
		if(unique_instance==null)
			unique_instance=new TFileLoader();
		return unique_instance;
	}
	
	public void run(){
		try{
				this.isFinished=false;
				Context.Files=FileFinder.loadFile();
				this.isFinished=true;
				AppView.getInstance().getDisplay().asyncExec(new Runnable() {
					public void run() {
						//TesterToolBarView.unique_instance.updateLoadStatus();
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
	private static TFileLoader unique_instance;
	private TFileLoader(){}
	private boolean isFinished=false;
	
}
