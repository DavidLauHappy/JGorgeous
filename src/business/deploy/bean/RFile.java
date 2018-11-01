package business.deploy.bean;

import utils.FileUtils;
import utils.StringUtil;

/**
 * @author Administrator
 * 内部网络文件传输对象
 */
public class RFile {
	private String localPath;//文件本地目录
	private String remotePath;//文件远程目录
	private String name;//文件名称
	private long size;//文件大小
	private long mdftime;//文件最后修改时间
	private String md5;//文件的md5值
	
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
