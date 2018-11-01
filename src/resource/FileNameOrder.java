package resource;

import java.util.HashMap;
import java.util.Map;

public class FileNameOrder {
	private static Map<String,Integer> Order=new HashMap<String,Integer>();
	private static int order=0;
    public static int getOrder(String fileName){
    	String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
    	if(!Order.containsKey(prefix)){
    		order++;
    		Order.put(prefix, order);
    	}
    	return Order.get(prefix);
    }
}
