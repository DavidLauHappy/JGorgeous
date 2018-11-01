package common.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.oro.text.regex.MalformedPatternException;

import resource.Context;
import resource.Logger;
import resource.SecurityCenter;
import utils.StringUtil;


import bean.LOCALNODEBean;
import bean.LocalUserInfo;
import bean.NODEBean;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.EofMatch;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;
import expect4j.matches.TimeoutMatch;

public class LnxShell {
	  private Session session;
	    private ChannelShell channel;
	    private  Expect4j expect=null;
	    public static final String BACKSLASH_R = "\r";
	    public static final String BACKSLASH_N = "\n";
	    public static final String COLON_CHAR = ":";
	    public static String ENTER_CHARACTER = BACKSLASH_R;
	    public static final int SSH_PORT = 22;
	  //正则匹配，用于处理服务器返回的结果
	    public static String[] linuxPromptRegEx = new String[] { "\\>","\\$","~]#", "~#", "#",":~#", "/$", ">"};
	    public static String[] errorMsg=new String[]{"could not acquire the config lock "};
	    private String ip;
	    private int port;
	    private String user;
	    private String passwd;
	    private StringBuffer buffer=null;
	    private String response="";
	    private static final long defaultTimeOut = 1000;
	    private static ConcurrentMap<String,LnxShell> ShellMap=new ConcurrentHashMap<String, LnxShell>();
	    
	    public static  void  clearShell(LOCALNODEBean node){
	    	ShellMap.remove(node.getId());
	    	//FileCopyAction.Dirs.remove(node.getId());
	    }
	    
	    public static  void  clearShell(NODEBean node){
	    	ShellMap.remove(node.getId());
	    	//FileCopyAction.Dirs.remove(node.getId());
	    }
	    
	    public static  LnxShell getShell(LOCALNODEBean node){
	    	 if(ShellMap.containsKey(node.getId()))
	    		 return ShellMap.get(node.getId());
	    	 int port=0;
	    	 if(!StringUtil.isNullOrEmpty(node.getSftpPort()))
	    		 port=Integer.parseInt(node.getSftpPort());
	    	 else 
	    		 port=22;
	    	 String passwd=SecurityCenter.getInstance().decrypt(node.getSftpPasswd(), Context.EncryptKey);
	    	 LnxShell shell=new LnxShell(node.getIp(),port,node.getSftpUser(),passwd);
	    	 ShellMap.put(node.getId(), shell);
	    	 return shell;
	    }
	    
	    public static  LnxShell getShell(NODEBean node){
	    	 if(ShellMap.containsKey(node.getId()))
	    		 return ShellMap.get(node.getId());
	    	 int port=0;
	    	 if(!StringUtil.isNullOrEmpty(node.getSftpPort()))
	    		 port=Integer.parseInt(node.getSftpPort());
	    	 else 
	    		 port=22;
	    	 String passwd=SecurityCenter.getInstance().decrypt(node.getSftpPasswd(), Context.EncryptKey);
	    	 LnxShell shell=new LnxShell(node.getIp(),port,node.getSftpUser(),passwd);
	    	 ShellMap.put(node.getId(), shell);
	    	 return shell;
	    }
	    
	    private LnxShell(String ip,int port,String user,String password) {
	        this.ip=ip;
	        this.port=port;
	        this.user=user;
	        this.passwd=password;
	        expect = getExpect();
	        buffer=new StringBuffer();
	        response="";
	    }
	    
	    private Expect4j getExpect() {
	    	try {
	            JSch jsch = new JSch();
	            session = jsch.getSession(user, ip, port);
	            session.setPassword(passwd);
	            Hashtable<String, String> config = new Hashtable<String, String>();
	            config.put("StrictHostKeyChecking", "no");
	            session.setConfig(config);
	            LocalUserInfo ui = new LocalUserInfo();
	            session.setUserInfo(ui);
	            session.connect();
	            channel = (ChannelShell) session.openChannel("shell");
	            expect = new Expect4j(channel.getInputStream(), channel .getOutputStream());
	            channel.connect();
	            return expect;
	        } catch (Exception ex) {
	        	Logger.getInstance().serviceLog(Context.ServiceLinuxShell,"连接到Linux的["+ip+"]建立shell发生异常："+ex.toString());
	        	Logger.getInstance().error("连接到Linux的["+ip+"]建立shell发生异常："+ex.toString());
	        }
	        return null;
	    }
	    
	    public boolean executeCommand(List<String> cmds){
	    	try{
		    		if(expect==null){
		    			Logger.getInstance().serviceLog(Context.ServiceLinuxShell,"连接到Linux的["+ip+"]建立shell的会话被自行中断后仍接收执行指令");
		    			return false;
		    	    }
		    	 Closure closure = new Closure() {
		    		 public void run(ExpectState expectState) throws Exception{
		    			  buffer.append(expectState.getBuffer());
		    			  expectState.exp_continue();
		    		 }
		    	};
	    	List<Match> lstPattern = new ArrayList<Match>();
	    	 String[] regEx = linuxPromptRegEx;  
	    	for(String regexElement:regEx){
		    		try{
		    			RegExpMatch mat=new RegExpMatch(regexElement,closure);
		    			lstPattern.add(mat);
		    		}
		    		catch (MalformedPatternException e) {  
		                return false;  
		            } catch (Exception e) {  
		                return false;  
		            } 
	    		}
		    	
	    	lstPattern.add(new EofMatch(new Closure() {  
		                public void run(ExpectState state) {  
		               
		                }  
		            }));  
	    	
		     lstPattern.add(new TimeoutMatch(defaultTimeOut, new Closure() {  
		                public void run(ExpectState state) {  
		               
		                }  
		            })); 
	    		
	           for (String regex : regEx) {  
		                RegExpMatch mat;  
		                try {  
		                    mat = new RegExpMatch(regex, closure);  
		                    lstPattern.add(mat);  
		                } catch (MalformedPatternException e) {  
		                    //e.printStackTrace();  
		                }  
	                }  
	            
	            	boolean isSuccess=true;
	            	for(String cmd:cmds){
	            		 Logger.getInstance().serviceLog(Context.ServiceLinuxShell,"连接到Linux的["+ip+"]建立LinuxShell执行ssh command:"+cmd);
	            		isSuccess=isSuccess(lstPattern, cmd);
	            		if(!isSuccess){
	            			isSuccess=isSuccess(lstPattern, cmd);
	            		}
	            	}
	            		//单挑指令执行且无输出的情况下异常应该是正常（未定义的模式）
	        		isSuccess = !checkResult(expect.expect(lstPattern));  
	        		String response=buffer.toString().toLowerCase();  

	                for (String msg : errorMsg) {  
	                    if (response.indexOf(msg) > -1) {  
	                        return false;  
	                    }  
	                }  
	                return isSuccess;
	            }catch(Exception e){
	            	return false;
	            }finally{
	            	this.response=buffer.toString();
	            	if(!StringUtil.isNullOrEmpty(this.response)&&this.response.length()>1024)
	            		this.response=this.response.substring(0, 1024);
	            	Logger.getInstance().serviceLog(Context.ServiceLinuxShell,"连接到Linux的["+ip+"]建立LinuxShell当前执行结果:\r\n"+this.response);
	            	buffer.delete(0, buffer.length());
	            	//closeConnection();
	            }
	    }
	    
	    private boolean isSuccess(List<Match> objPattern, String strCommandPattern){
	    	try {  
	            boolean isFailed = checkResult(expect.expect(objPattern));  
	            if (!isFailed) {  
	                expect.send(strCommandPattern);  
	                //expect.send("\n");  
	                expect.send("\r");  
	                return true;  
	            }  
	            return false;  
	        } catch (MalformedPatternException ex) {  
	            return false;  
	        } catch (Exception ex) {  
	            return false;  
	        }  
	    }
	    
	    private  boolean checkResult(int intRetVal) {  
		        if (intRetVal == -2) {  
		            return true;  
		        }  
		        return false;  
	    }  
	    
	   
	    public  String getResponse() {  
	        return this.response;  
	    }  
	    
	    public void closeConnection(String nodeID){
	    	try{
	    		Logger.getInstance().serviceLog(Context.ServiceLinuxShell,"连接到Linux的["+ip+"]LinuxShell.closeConnection()退出服务");
		    	if(this.expect!=null){
		    		 this.expect.close();
		    		 this.expect=null;
		    	}
		    	//虽然会有日志打印，但不会有独立的线程造成效率问题
		    	if(this.channel!=null)
		    		this.channel.disconnect();
		    	if(this.session!=null)
		    		this.session.disconnect();
		    	ShellMap.remove(nodeID);
	    	}catch(Exception e){
	    		Logger.getInstance().serviceLog(Context.ServiceLinuxShell,"连接到Linux的["+ip+"]LinuxShell.closeConnection()发生异常:"+e.toString());
	    		Logger.getInstance().error("LinuxShell.closeConnection()发生异常:"+e.toString());
	    	}
	    }
	    
	    public void exit(){
	    	try{
	    		if(this.channel!=null)
		    		this.channel.disconnect();
		    	if(this.session!=null)
		    		this.session.disconnect();
	    	}
	    	catch(Exception e){
	    		Logger.getInstance().serviceLog(Context.ServiceLinuxShell,"连接到Linux的["+ip+"]LinuxShell.exit()发生异常:"+e.toString());
	    		Logger.getInstance().error("LinuxShell.exit()发生异常:"+e.toString());
	    	}
	    }
	    
	    public static void release(){
	    	for(String id:ShellMap.keySet()){
	    		ShellMap.get(id).closeConnection(id);
	    	}
	    }
	    
	   public static String getMD5FromRespone(String str){
			String[] lines=str.split("\r\n");
			for(String line:lines){
				if(!StringUtil.isNullOrEmpty(line)&&line.indexOf(" ")==-1)
					return line;
			}
			return "";
		}
}
