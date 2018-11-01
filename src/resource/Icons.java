package resource;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.program.Program;

import bean.COMMANDBean;
import bean.NODEBean;

import views.AppView;

public class Icons {

	public static Color getDiffColor(){
		Color color = new Color(AppView.getInstance().getDisplay(), 247, 204, 219);
		return color;
	}
	private static Image  appIcon;
	public static Image getAppIcon()
	{
		if(appIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_manager.png";
			appIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return appIcon;
	}
	
	private static Image  settingIcon;
	public static Image getSettingIcon(){
		if(settingIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_setting.png";
			settingIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return settingIcon;
	}
	
	private static Image  ftpIcon;
	public static Image getFtpIcon(){
		if(ftpIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_ftp.png";
			ftpIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return ftpIcon;
	}
	
	private static Image  sambaIcon;
	public static Image getSambaIcon(){
		if(sambaIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_samba.png";
			sambaIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return sambaIcon;
	}
	
	private static Image operatorIcon;
	public static Image getOperatorIcon(){
		if(operatorIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_user64.png";
			operatorIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return operatorIcon;
	}
	
	private static Image  streamIcon;
	public static Image getStreamIcon(){
		if(streamIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_stream.png";
			streamIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return streamIcon;
	}
	
	private static Image  makeIcon;
	public static Image getMakeIcon(){
		if(makeIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_make.png";
			makeIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return makeIcon;
	}
	
	private static Image  addStreamIcon;
	public static Image getAddStreamIcon(){
		if(addStreamIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_add_stream.png";
			addStreamIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return addStreamIcon;
	}
	
	private static Image  releaseIcon;
	public static Image getReleaseIcon(){
		if(releaseIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_release.png";
			releaseIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return releaseIcon;
	}
	
	private static Image  dirMapIcon;
	public static Image getDirMapIcon(){
		if(dirMapIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_dir.png";
			dirMapIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return dirMapIcon;
	}
	
	private static Image  addReleaseIcon;
	public static Image getAddReleaseIcon(){
		if(addReleaseIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_add_release.png";
			addReleaseIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return addReleaseIcon;
	}
	
	private static Image  contactIcon;
	public static Image getContactIcon(){
		if(contactIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_contact.png";
			contactIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return contactIcon;
	}
	
	private static Image  setIcon;
	public static Image getSetIcon(){
		if(setIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_set.png";
			setIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return setIcon;
	}
	
	private static Image  helpIcon;
	public static Image getHelpIcon(){
		if(helpIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_help.png";
			helpIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return helpIcon;
	}
	
	private static Image  aboutIcon;
	public static Image getAboutIcon(){
		if(aboutIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_about.png";
			aboutIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return aboutIcon;
	}
	
	private static Image browserIcon;
	public static Image getBrowserIcon(){
		if(browserIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_browser.png";
			browserIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
		}
		return browserIcon;
	}
	
	private static Image pkgOKIcon;
	public static Image getPkgOKIcon(){
		if(pkgOKIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_pkgOK.png";
			pkgOKIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return pkgOKIcon;
	}
	
	private static Image retrunIcon;
	public static Image getReturnIcon(){
		if(retrunIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_return.png";
			retrunIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return retrunIcon;
	}
	
	
	private static Image cmpverIcon;
	public static Image getCmpVerIcon(){
		if(cmpverIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_cmpversion.gif";
			cmpverIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return cmpverIcon;
	}
	
	private static Image detailIcon;
	public static Image getDetailIcon(){
		if(detailIcon==null){
			 String iconPath=Paths.getInstance() .getIconPath()+"icon_details.gif";
			 detailIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return detailIcon;
	}
	
	private static Image refreshIcon;
	public static Image getRefreshIcon(){
		if(refreshIcon==null){
			 String iconPath=Paths.getInstance() .getIconPath()+"icon_refresh.png";
			 refreshIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return refreshIcon;
	}
	
	private static Image fliterIcon;
	public static Image getFliterIcon(){
		if(fliterIcon==null){
			 String iconPath=Paths.getInstance() .getIconPath()+"icon_fliter.png";
			 fliterIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return fliterIcon;
	}
	
	
	private static Image historyIcon;
	public static Image getHistoryIcon(){
		if(historyIcon==null){
			 String iconPath=Paths.getInstance() .getIconPath()+"icon_history.gif";
			 historyIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return historyIcon;
	}
	
	private static Image activeIcon;
	public static Image getActiveIcon(){
		if(activeIcon==null){
			 String iconPath=Paths.getInstance() .getIconPath()+"icon_ative.gif";
			 activeIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return activeIcon;
	}
	
	private static Image lockIcon;
	public static Image getLockIcon(){
		if(lockIcon==null){
			 String iconPath=Paths.getInstance() .getIconPath()+"icon_lock.gif";
			 lockIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return lockIcon;
	}
	
	
	private static Image  meIcon;
	public static Image getMeIcon(){
		String iconPath="";
		if(Context.session.roleID.equals(Constants.RoleType.Deploy.ordinal()+"")){
			 iconPath=Paths.getInstance() .getIconPath()+"icon_role_developer.png";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.Tester.ordinal()+"")){
			 iconPath=Paths.getInstance() .getIconPath()+"icon_role_test.png";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.DeveloperVersion.ordinal()+"")){
			 iconPath=Paths.getInstance() .getIconPath()+"icon_role_version.png";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.TesterVersion.ordinal()+"")){
			 iconPath=Paths.getInstance() .getIconPath()+"icon_role_version.png";
		}
		else if(Context.session.roleID.equals(Constants.RoleType.DeveloperManager.ordinal()+"")){
			 iconPath=Paths.getInstance() .getIconPath()+"icon_role_manger.png";
		}else{
			 iconPath=Paths.getInstance() .getIconPath()+"icon_user64.png";
		}
		meIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
		return meIcon;
	}
	private static Image  mapIcon;
	public static Image getMapIcon()
	{
		if(mapIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_map.png";
			mapIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return mapIcon;
	}
	
	private static Image  loadingIcon;
	public static Image getLoadingIcon()
	{
		if(loadingIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_loading.png";
			loadingIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return loadingIcon;
	}
	
	private static Image  dbbackIcon;
	public static Image getDbbackupIcon(){
		if(dbbackIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_dbbackup.png";
			dbbackIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return dbbackIcon;
	}
	
	private static Image  loadedIcon;
	public static Image getLoadedIcon()
	{
		if(loadedIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_action.png";
			loadedIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return loadedIcon;
	}
	
	private static Image  statisticIcon;
	public static Image getStatisticIcon()
	{
		if(statisticIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_statistic.png";
			statisticIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return statisticIcon;
	}
	
	private static Image  actionIcon;
	public static Image getActionIcon()
	{
		if(actionIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_action.png";
			actionIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return actionIcon;
	}
	
	private static Image  importIcon;
	public static Image getImportIcon(){
		if(importIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_import.png";
			importIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return importIcon;
	}
	
	private static Image  importIcon16;
	public static Image getImportIcon16(){
		if(importIcon16==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_import16.png";
			importIcon16=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return importIcon16;
	}
	
	
	private static Image  addIcon;
	public static Image getAddIcon()
	{
		if(addIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_add.png";
			addIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return addIcon;
	}
	
	private static Image moreIcon;
	public static Image getMoreIcon(){
		if(moreIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_more.gif";
			moreIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
		}
		return moreIcon;
	}
	
	private static Image addDbIcon;
	public static Image getAddDbIcon(){
		if(addDbIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_adddb.png";
			addDbIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
		}
		return addDbIcon;
	}
	
	private static Image addOpenIcon;
	public static Image getOpenDbIcon(){
		if(addOpenIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_dbopen.png";
			addOpenIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
		}
		return addOpenIcon;
	}
	
	
	private static Image  deleteIcon;
	public static Image getDeleteIcon(){
		if(deleteIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_delete.png";
			deleteIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return deleteIcon;
	}
	
	private static Image  okIcon;
	public static Image getOkIcon(){
		if(okIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_ok.png";
			okIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return okIcon;
	}
	
	private static Image  upIcon;
	public static Image getUpIcon(){
		if(upIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_up.png";
			upIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return upIcon;
	}
	
	private static Image  backupIcon;
	public static Image getBackupIcon(){
		if(backupIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_backup.png";
			backupIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return backupIcon;
	}
	
	private static Image  serviceIcon;
	public static Image getServiceIcon(){
		if(serviceIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_service.png";
			serviceIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return serviceIcon;
	}
	
	private static Image  downIcon;
	public static Image getDownIcon(){
		if(downIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_down.png";
			downIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return downIcon;
	}
	
	private static Image  daemonIcon;
	public static Image getDaemonIcon(){
		if(daemonIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_daemons.png";
			daemonIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return daemonIcon;
	}
	
	
	private static Image  connectIcon;
	public static Image getConnectIcon()
	{
		if(connectIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_connect.png";
			connectIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return connectIcon;
	}
	
	private static Image serverIcon;
	public static Image getServerIcon()
	{
		if(serverIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_server.png";
			serverIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return serverIcon;
	}
	

	
	private static Image dbNodeIconX;
	public static Image getDbNodeIcon(String status){
		   String iconPath=Paths.getInstance() .getIconPath();
		    if(status.equals(NODEBean.Status.Connected.ordinal()+""))
			         iconPath=iconPath+"icon_database_connected.png";
		    else if(status.equals(NODEBean.Status.Init.ordinal()+""))
		    	 iconPath=iconPath+"icon_database_init.png";
		    else if(status.equals(NODEBean.Status.Error.ordinal()+""))
		    	 iconPath=iconPath+"icon_database_error.png";
		    else if(status.equals(NODEBean.Status.Running.ordinal()+""))
		    	 iconPath=iconPath+"icon_database_running.png";
		    else if(status.equals(NODEBean.Status.Done.ordinal()+""))
		    	iconPath=iconPath+"icon_database_done.png";
		    else 
		    	iconPath=iconPath+"icon_database.png";
			dbNodeIconX=new Image(AppView.getInstance().getDisplay(),iconPath);
		return dbNodeIconX;
	}
	
	private static Image dbNodeIcon24;
	public static Image getDbNodeIcon24()
	{
		if(dbNodeIcon24==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_database24.png";
			dbNodeIcon24=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return dbNodeIcon24;
	}
	
	
	private static Image kcbpNodeIconX;
	public static Image getNodeIcon(String status){
		 String iconPath=Paths.getInstance() .getIconPath();
		    if((NODEBean.Status.Connected.ordinal()+"").equals(status))
			         iconPath=iconPath+"icon_node_connect.png";
		    else if((NODEBean.Status.Init.ordinal()+"").equals(status))
		    	 iconPath=iconPath+"icon_node_init.png";
		    else if((NODEBean.Status.Error.ordinal()+"").equals(status))
		    	 iconPath=iconPath+"icon_node_error.png";
		    else if((NODEBean.Status.Running.ordinal()+"").equals(status))
		    	 iconPath=iconPath+"icon_node_run.png";
		    else if((NODEBean.Status.Done.ordinal()+"").equals(status))
		    	iconPath=iconPath+"icon_node_done.png";
		    else
		    	iconPath=iconPath+"icon_midnode.png";
		    kcbpNodeIconX=new Image(AppView.getInstance().getDisplay(),iconPath);
		return kcbpNodeIconX;
	}
	
	private static Image kcbpNodeIcon24;
	public static Image getKcbpNodeIcon24()
	{
		if(kcbpNodeIcon24==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_kcbp24.png";
			kcbpNodeIcon24=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return kcbpNodeIcon24;
	}
	
	 public static Image getFileImage(String fileName){
	    	Image iconImage=null;
	    	int dot = fileName.lastIndexOf('.');
	 	   if (dot != -1) {
	 	     String extension = fileName.substring(dot);
	 	     Program program = Program.findProgram(extension);
	 	     if (program != null){
	 	         String typeString = program.getName();
	 	         ImageData imageData = program.getImageData();
	 	         if (imageData != null){
	 	        	 iconImage = new Image(null, imageData, imageData.getTransparencyMask());
	 	         }
	 	      }
	 	   }
	 	   return iconImage;
	    }
	 
	 public static Image getFileImage(File file){
		   String fileName=file.getName();
	    	Image iconImage=null;
	    	int dot = fileName.lastIndexOf('.');
	 	   if (dot != -1) {
	 	     String extension = fileName.substring(dot);
	 	     Program program = Program.findProgram(extension);
	 	     if (program != null){
	 	         String typeString = program.getName();
	 	         ImageData imageData = program.getImageData();
	 	         if (imageData != null){
	 	        	 iconImage = new Image(null, imageData, imageData.getTransparencyMask());
	 	         }else{
	 	        	    ImageIcon systemIcon=(ImageIcon)FileSystemView.getFileSystemView().getSystemIcon(file);
	 	        	    java.awt.Image image=systemIcon.getImage();
	 	        	    int width = image.getWidth(null);
	 	        	    int height = image.getHeight(null);
	 	        	    BufferedImage bufferedImage = (BufferedImage) systemIcon.getImage();
	 	        	    int[] data = ((DataBufferInt) bufferedImage.getData().getDataBuffer()).getData();
		 	        	  for (int i = 0; i< data.length; i++){
		 	        	    if (data[i] == 0)
		 	        	        data[i] = 0xFFFFFF;
		 	        	    }
		 	        	   imageData = new ImageData(width, height, 24, new PaletteData(0xFF0000, 0x00FF00, 0x0000FF));
		 	               imageData.setPixels(0, 0, data.length, data, 0);
		 	               iconImage = new Image(AppView.getInstance().getDisplay(),imageData);
	 	         }
	 	      }else{
	 	    	    ImageIcon systemIcon=(ImageIcon)FileSystemView.getFileSystemView().getSystemIcon(file);
	        	    java.awt.Image image=systemIcon.getImage();
	        	    int width = image.getWidth(null);
	        	    int height = image.getHeight(null);
	        	    BufferedImage bufferedImage = (BufferedImage) systemIcon.getImage();
	        	    int[] data = ((DataBufferInt) bufferedImage.getData().getDataBuffer()).getData();
	 	        	  for (int i = 0; i< data.length; i++){
	 	        	    if (data[i] == 0)
	 	        	        data[i] = 0xFFFFFF;
	 	        	    }
	 	        	 ImageData imageData = new ImageData(width, height, 24, new PaletteData(0xFF0000, 0x00FF00, 0x0000FF));
	 	              imageData.setPixels(0, 0, data.length, data, 0);
	 	             iconImage = new Image(AppView.getInstance().getDisplay(),imageData);  
	 	      }
	 	   }
	 	   return iconImage;
	    }
	 
	private static Image floderIcon;
	public static Image getFloderIcon(){
		if(floderIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_floder.png";
			floderIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return floderIcon;
	}
	
	private static Image tagFloderIcon;
	public static Image getTagFloderIcon(){
		if(tagFloderIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_class.png";
			tagFloderIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return tagFloderIcon;
	}
	
	private static Image floderDelIcon;
	public static Image getFloderDelIcon(){
		if(floderDelIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_del_floder.png";
			floderDelIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return floderDelIcon;
	}
	
	private static Image myViewIcon;
	public static Image getMyViewIcon(){
		if(myViewIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_myview.png";
			myViewIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return myViewIcon;
	}
	
	private static Image viewIcon;
	public static Image getViewIcon(){
		if(viewIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_view.png";
			viewIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return viewIcon;
	}
	
	private static Image userIcon;
	public static Image getUserIcon()
	{
		if(userIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_user.png";
			userIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return userIcon;
	}
	
	
	public static Image stepIcon;
	public static Image getStepIcon(String status)
	{
		 String iconPath=Paths.getInstance() .getIconPath();
		    if(status.equals(COMMANDBean.Status.Initial.ordinal()+""))
			         iconPath=iconPath+"icon_step_initial.png";
		    else if(status.equals(COMMANDBean.Status.Runnable.ordinal()+""))
		    	 iconPath=iconPath+"icon_step_runnable.png";
		    else if(status.equals(COMMANDBean.Status.Scheduling.ordinal()+""))
		    	 iconPath=iconPath+"icon_step_schedule.png";
		    else if(status.equals(COMMANDBean.Status.Running.ordinal()+""))
		    	 iconPath=iconPath+"icon_step_running.png";
		    else if(status.equals(COMMANDBean.Status.TimeOut.ordinal()+""))
		    	iconPath=iconPath+"icon_step_failed.png";
		    else if(status.equals(COMMANDBean.Status.ReturnOK.ordinal()+""))
		    	iconPath=iconPath+"icon_step_ok.png";
		    else if(status.equals(COMMANDBean.Status.ReturnFailed.ordinal()+""))
		    	iconPath=iconPath+"icon_step_failed.png";
		    else if(status.equals(COMMANDBean.Status.ReturnNull.ordinal()+""))
		    	iconPath=iconPath+"icon_step_failed.png";
		    else if(status.equals(COMMANDBean.Status.Scheduled.ordinal()+""))
		    	iconPath=iconPath+"icon_step_schedule.png";
		    else
		    	iconPath=iconPath+"icon_step.png";
		    stepIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
		return stepIcon;
	}
	private static Image fileIcon;
	public static Image getFileIcon()
	{
		if(fileIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_file.png";
			fileIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return fileIcon;
	}
	
	private static Image hideVerIcon;
	public static Image getHideVersionIcon()
	{
		if(hideVerIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_hide.png";
			hideVerIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return hideVerIcon;
	}
	
	private static Image visiableIcon;
	public static Image getVisableIcon(){
		if(visiableIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_visiable.png";
			visiableIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return visiableIcon;
	}
	
	private static Image invisiableIcon;
	public static Image getInvisableIcon(){
		if(invisiableIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_invisiable.png";
			invisiableIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return invisiableIcon;
	}
	
	private static Image cfgNodeIcon;
	public static Image getCfgNodeIcon()
	{
		if(cfgNodeIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_cfgnode.png";
			cfgNodeIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return cfgNodeIcon;
	}
	
	private static Image compareIcon;
	public static Image getCompareIcon()
	{
		if(compareIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_cmp.png";
			compareIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return compareIcon;
	}
	
	private static Image passwdIcon;
	public static Image getPasswdIcon(){
		if(passwdIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_passwd.png";
			passwdIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return passwdIcon;
	}
	
	private static Image approveIcon;
	public static Image getApproveIcon(){
		if(approveIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_appr.png";
			approveIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return approveIcon;
	}
	
	private static Image addReqIcon;
	public static Image getReqIcon(){
		if(addReqIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_req.png";
			addReqIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return addReqIcon;
	}
	
	private static Image findIcon;
	public static Image getFindIcon(){
		if(findIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_find.png";
			findIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return findIcon;
	}
	
	private static Image questionIcon;
	public static Image getQustionIcon(){
		if(questionIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_bug.png";
			questionIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return questionIcon;
	}
	
	
	private static Image lNodeIcon;
	public static Image getLocalNodeIcon(){
		if(lNodeIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_localnode.png";
			lNodeIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return lNodeIcon;
	}
	
	private static Image taskIcon;
	public static Image getTaskIcon(){
		if(taskIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_task.png";
			taskIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return taskIcon;
	}
	
	private static Image cfgIcon;
	public static Image getCfgIcon(){
		if(cfgIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_config.png";
			cfgIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return cfgIcon;
	}
	
	private static Image nodeGroupIcon;
	public static Image getNodeGroupIcon()
	{
		if(nodeGroupIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_group.png";
			nodeGroupIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return nodeGroupIcon;
	}
	
	private static Image nodeGroupManageIcon;
	public static Image getNodeGroupManageIcon(){
		if(nodeGroupManageIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_group_delete.png";
			nodeGroupManageIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return nodeGroupManageIcon;
	}
	
	private static Image nodeTagManageIcon;
	public static Image getTagIcon(){
		if(nodeTagManageIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_tag.png";
			nodeTagManageIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return nodeTagManageIcon;
	}
	
	private static Image userManageIcon;
	public static Image getManageUserIcon(){
		if(userManageIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_usermanage.png";
			userManageIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return userManageIcon;
	}
	
	private static Image auditIcon;
	public static Image getAuditIcon(){
		if(auditIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_audit.png";
			auditIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return auditIcon;
	}
	
	private static Image selectIcon;
	public static Image getSelectIcon(){
		if(selectIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_select.png";
			selectIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return selectIcon;
	}
	
	private static Image userGroupIcon;
	public static Image getGroupUserIcon(){
		if(userGroupIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_usergroup.png";
			userGroupIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return userGroupIcon;
	}
	
	private static Image groupIcon;
	public static Image getGroupIcon(){
		if(groupIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_groupmng.png";
			groupIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return groupIcon;
	}
	
	private static Image flowIcon;
	public static Image getFlowIcon(){
		if(flowIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_approve.png";
			flowIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return flowIcon;
	}
	private static Image reportIcon;
	public static Image getReportIcon()
	{
		if(reportIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_report.png";
			reportIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return reportIcon;
	}
	
	private static Image dbNodeIcon;
	public static Image getDbNodeIcon()
	{
		if(dbNodeIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_dbnode.png";
			dbNodeIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return dbNodeIcon;
	}
	
	private static Image midNodeIcon;
	public static Image getMidNodeIcon()
	{
		if(midNodeIcon==null)
			{
			String iconPath=Paths.getInstance() .getIconPath()+"icon_midnode.png";
			midNodeIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return midNodeIcon;
	}
	
	private static Image searchIcon;
	public static Image  getSearchIcon(){
		if(searchIcon==null){
			String iconPath=Paths.getInstance() .getIconPath()+"icon_search.png";
			searchIcon=new Image(AppView.getInstance().getDisplay(),iconPath);
			}
		return searchIcon;
	}
}
