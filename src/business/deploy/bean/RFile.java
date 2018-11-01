package business.deploy.bean;

import utils.FileUtils;
import utils.StringUtil;

/**
 * @author Administrator
 * �ڲ������ļ��������
 */
public class RFile {
	private String localPath;//�ļ�����Ŀ¼
	private String remotePath;//�ļ�Զ��Ŀ¼
	private String name;//�ļ�����
	private long size;//�ļ���С
	private long mdftime;//�ļ�����޸�ʱ��
	private String md5;//�ļ���md5ֵ
	
	public RFile(String localPath, String remotePath, String name, long size,
			long mdftime) {
		super();
		this.localPath = localPath;
		this.remotePath = remotePath;
		this.name = name;
		this.size = size;
		this.mdftime = mdftime;
		this.md5=FileUtils.getMd5ByPath(this.localPath);
	}

	public String getLocalPath() {
		return localPath;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

	public long getMdftime() {
		return mdftime;
	}
	
	public String getMd5() {
		return md5;
	}

}
