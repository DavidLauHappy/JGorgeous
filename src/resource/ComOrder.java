package resource;

public class ComOrder {
   
	private int order;
	public ComOrder(){
		order=1;
	}
	
	public ComOrder(int order){
		this.order=order;
	}
	
	public void inc(){
		this.order++;
	}
	
	public String getOrder(){
		return this.order+"";
	}
	
	public int getOrderInt(){
		return this.order;
	}
}
