package resource;

import java.util.List;

import bean.RoleBean;


/**
 * @author Administrator
 *  �ͻ��˼򵥻Ự���󣬵�¼ʱ����
 */
public class Session {
	public String userID;
	public String roleID;
	public String userName;
	public String currentFunctionID="";
	public String cuurentChecker="";//��ǰ�����
	public String detail="";//��ǰ����������
	public String clientIp;//�ͻ��˵�ַ��Ϣ
	public String currentClass;
	public String currentFlag;//AB���е��û��������ɫ��¼��̶�����
	public String clientMac;
	public  List<RoleBean> Roles;
	public Session(){	}
	
	public void inroll(String className,String funcID){
		this.currentClass=className;
		this.currentFunctionID=funcID;
	}
}
