package bean;

public class Cluster {

	
	private String ComponentID;
	private String name;
	private String componentType;
	
	public Cluster(String componentID, String name) {
		super();
		ComponentID = componentID;
		this.name = name;
	}

	public String getComponentID() {
		return ComponentID;
	}

	public String getName() {
		return name;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	
	
	


}
