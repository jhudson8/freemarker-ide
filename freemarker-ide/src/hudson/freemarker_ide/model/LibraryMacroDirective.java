package hudson.freemarker_ide.model;

import hudson.freemarker_ide.configuration.MacroLibrary;


public class LibraryMacroDirective extends MacroDirective {

	private String contents;
	private String namespace;
	private int offset;
	private int length;

	public static void main (String[] args) {
		try {
			String content = "#macro entries startIndex=1\r\n" +
			"data=\"data\" headerUrls=[] sortIndex=-1";
			
			LibraryMacroDirective lmd = new LibraryMacroDirective("lib", content, 0, content.length());
			String[] attributes = lmd.getAttributes();
			for (int i=0; i<attributes.length; i++) {
				System.out.println(attributes[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public LibraryMacroDirective (String namespace, String contents, int offset, int length) {
		this.contents = contents;
		this.namespace = namespace;
		this.offset = offset;
		this.length = length;
	}

	private String name;
	public String getName() {
		if (null == name)
			name = namespace + "." + super.getName();
		return name;
	}

	public String getContents() {
		return contents;
	}

	public String getFullContents() {
		return contents;
	}

	protected int getCursorPosition(int offset) {
		return 1;
	}

	public int getLength() {
		return length;
	}

	public int getOffset() {
		return offset;
	}
}