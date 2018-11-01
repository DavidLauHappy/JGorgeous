package resource;

import java.util.List;

import bean.RoleBean;


/**
 * @author Administrator
 *  客户端简单会话对象，登录时生成
 */
public class Session {
	public String userID;
	public String roleID;
	public String userName;
	public String currentFunctionID="";
	public String cuurentChecker="";//当前审核人
	public String detail="";//当前操作的描述
	public String clientIp;//客户端地址信息
	public String currentClass;
	public String currentFlag;//AB表中的用户，部署角色登录后固定不变
	public String clientMac;
	public  List<RoleBean> Roles;
	public Session(){	}
	
	public void inroll(String className,String funcID){
		this.currentClass=className;
		this.currentFunctionID=funcID;
	}
}
