package resource;

public class Item {
	private String type="";
	private String key="";
	private String value="";
	public Item(){}
	public Item(String type, String key, String value) {
		super();
		this.type = type;
		this.key = key;
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
