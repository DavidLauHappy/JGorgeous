package resource;

public class UserGroup {
	private String groupID;
	private String groupName;
	private String type;
	public String getGroupID() {
		return groupID;
	}
	public String getGroupName() {
		return groupName;
	}
	public String getType() {
		return type;
	}
	public UserGroup(String groupID, String groupName, String type) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.type = type;
	}
	
	
	
}
