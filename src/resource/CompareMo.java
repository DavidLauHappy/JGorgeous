package resource;

public class CompareMo {
	public enum Type{EQUAL,INSERT,DELETE,CHANGE;}
	private int lineNo;
	private String oldLine;
	private String newLine;
	private String tag;
	public static final String Result_EQUAL = "EQUAL";  
	public static final String Result_INSERT = "INSERT";  
	public static final String Result_DELETE = "DELETE";  
	public static final String Result_CHANGE = "CHANGE";

	public CompareMo(int lineNo, String oldLine, String newLine, String type) {
		this.lineNo = lineNo;
		this.oldLine =lineNo+"     "+oldLine;
		this.newLine =lineNo+"     "+ newLine;
		this.tag = type;
	}


	public int getId() {
		return lineNo;
	}

	public String getOldText() {
		return oldLine;
	}

	public String getNewText() {
		return newLine;
	}

	public String getTag() {
		return tag;
	}	
}
