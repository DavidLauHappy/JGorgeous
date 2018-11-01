package bean;
/**
 * @author 作者 E-mail:
 * @date 2016-8-10
 * @version 1.0
 * 类说明
 */
public class Triple {
	private String name="";
	private String value="";
	private String type="";
	private String desc="";
	private String max="";
	private String min="";
	private String tip="";
	
	public Triple(){}
	public Triple(String name, String value, String type, String desc,String max,String min) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
		this.desc = desc;
		this.max=max;
		this.min=min;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public String getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	
	public String getMax() {
		return max;
	}
	public String getMin() {
		return min;
	}

	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}

	public enum ControlType{Check,Radio,Text,Scale,Combox,Special;}

	public void setName(String name) {
		this.name = name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
