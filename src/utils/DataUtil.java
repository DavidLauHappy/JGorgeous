package utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataUtil {
	
	public static String[][] convertList2Array(List list)
	{
		if(list.size()>0)
		{
			int idx=(((String)list.get(0)).split(""+(char)29+"")).length;
			int idy=list.size();
			String[][] ret=new String[idy][idx];
			for(int k=0;k<idy;k++)
			{
				String cur=(String)list.get(k);
				String[] columns=cur.split(""+(char)29+"");
				for(int w=0;w<columns.length;w++)
				{		
					  if("N/A".equals(columns[w])){
						  ret[k][w]="";
					  }else{
					   ret[k][w]=columns[w];
					  }
				}
			}
		   return ret;
		}
		else
			return null;
	}
	
	public static String[] convertListRet2Array(List list)
	{	
		int idy=list.size();
		String[] ret=new String[idy];
		for(int k=0;k<idy;k++)
		{
			String cur=(String)list.get(k);
			ret[k]=cur;
		}
		return ret;
	}
	
	public static Object getMapFirstKey(Map map,Object value){
		Set set=map.entrySet();
		  Iterator it=set.iterator();
		  while(it.hasNext()) {
		   Map.Entry entry=(Map.Entry)it.next();
			   if(entry.getValue().equals(value)) {
				   return entry.getKey();
			   }
		  }
		  return null;
	}
	
	public static String[] getSeqArray(int maxNo){
		String[] ret=new String[maxNo];
		for(int w=0;w<maxNo;w++){
			ret[w]=Integer.toString(w+1);
		}
		return ret;
	}
	
	 public static <T> void swap(List<T> list,int oldPos,int newPos){
		 T tempElement = list.get(oldPos);
		  if(oldPos < newPos){
		        for(int i = oldPos; i < newPos; i++){
		            list.set(i, list.get(i + 1));
		        }
		        list.set(newPos, tempElement);
		    }
		  if(oldPos > newPos){
		        for(int i = oldPos; i > newPos; i--){
		            list.set(i, list.get(i - 1));
		        }
		        list.set(newPos, tempElement);
		    }
	 }
}
