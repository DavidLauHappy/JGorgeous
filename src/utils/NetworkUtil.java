package utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import resource.Logger;

public class NetworkUtil {
          public static void getLocalAddress(){
        	  try {
                  Enumeration<NetworkInterface> netInterfaces = NetworkInterface .getNetworkInterfaces();
                  while (netInterfaces.hasMoreElements()) {
                      NetworkInterface nif = netInterfaces.nextElement();
                      if(!nif.isPointToPoint()&&   !nif.isLoopback()&&!nif.isVirtual()&&nif.getDisplayName().indexOf("VPN")==-1){
                      Enumeration<InetAddress> iparray = nif.getInetAddresses();
                          while (iparray.hasMoreElements()) {
                        	  InetAddress addr=iparray.nextElement();
                        	  if(!addr.isLinkLocalAddress()){
                        	  //Logger.getInstance().debug("local ip address:="+iparray.nextElement().getHostAddress());
                        		  	System.out.println(addr.getHostAddress()+"#"+addr.getCanonicalHostName()+"#"+addr.getHostName().toString());
                        	  }
                          }
                      }
                      }
              } catch (Exception e) {
            	  Logger.getInstance().error("get all interface ip address error:"+e.toString());
              }
          }
          
          public static String getLocalMac(){
          	String result="";
          	try{
          		InetAddress ia=InetAddress.getLocalHost();
          		byte[] mac=NetworkInterface.getByInetAddress(ia).getHardwareAddress();
          		StringBuffer sb=new StringBuffer();
          		for(int i=0;i<mac.length;i++){
          			if(i!=0){
          				sb.append("-");
          			}
          			int temp=mac[i]&0xff;
          			String str=Integer.toHexString(temp);
          			if(str.length()==1)
          				str="0"+str;
          			str=str.toUpperCase();
          			sb.append(str);
          		}
          		result=sb.toString();
          	}
          	catch(Exception e){
          		
          	}
          	return result;
          }
          public static String getLocalIp(){
        	  String result="";
        	  try{
        	    InetAddress addr = InetAddress.getLocalHost(); 
  	            result=addr.getHostAddress();
  	            Logger.getInstance().debug("local ip address:="+result);
        	    }
        	  catch (Exception e) {
                  Logger.getInstance().error("get local ip address error:"+e.toString());
              }
        	  return result;
          }

        /*  多重网络下，获得局域网ip地址*/
          public static String getInetAddress(){
        	  String result="";
        	  try {
                  Enumeration<NetworkInterface> netInterfaces = NetworkInterface .getNetworkInterfaces();
                  while (netInterfaces.hasMoreElements()) {
                      NetworkInterface nif = netInterfaces.nextElement();
                      if(!nif.isPointToPoint()&&   !nif.isLoopback()&&!nif.isVirtual()&&nif.getDisplayName().indexOf("VPN")==-1){
	                      Enumeration<InetAddress> iparray = nif.getInetAddresses();
	                          while (iparray.hasMoreElements()) {
	                        	  InetAddress addr=iparray.nextElement();
	                        	  if(!addr.isLinkLocalAddress()){
	                        		  result=addr.getHostAddress();
	                        		  return result;
	                        	  }
	                          }
	                      }
                      }
              } catch (Exception e) {
            	  Logger.getInstance().error("get all interface ip address error:"+e.toString());
              }
        	  return result;
          }
}
