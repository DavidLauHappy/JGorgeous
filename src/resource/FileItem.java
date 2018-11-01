package resource;

public class FileItem {
	 private String name;
	 private String fullpath;
	 private String path;
	 private int bootflag;
	 private int seq;
	 
	public FileItem(String name, String fullpath, String path, int bootflag,
			int seq) {
		super();
		this.name = name;
		this.fullpath = fullpath;
		this.path = path;
		this.bootflag = bootflag;
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public String getFullpath() {
		return fullpath;
	}

	public String getPath() {
		return path;
	}

	public int getBootflag() {
		return bootflag;
	}

	public int getSeq() {
		return seq;
	}
	
	
	 
}
