package hudson.freemarker_ide.model;

import org.eclipse.core.resources.IResource;

public class CompletionMacroInstance extends MacroInstance {

	private String contents;
	private int offset;

	public CompletionMacroInstance (String contents, int offset, ItemSet itemSet, IResource resource) {
		this.contents = contents;
		this.offset = offset;
		setItemSet(itemSet);
		setResource(resource);
	}

	public int getOffset() {
		return offset;
	}

	public String getFullContents() {
		return contents;
	}

	public int getLength() {
		return contents.length();
	}
}