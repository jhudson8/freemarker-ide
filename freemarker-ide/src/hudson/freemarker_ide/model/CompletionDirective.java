package hudson.freemarker_ide.model;

import hudson.freemarker_ide.Plugin;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TypedRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;

public class CompletionDirective extends AbstractDirective {

	private String contents;
	private int offset;
	private int length;
	private AbstractDirective directive = this;

	public CompletionDirective (int offset, int length, ItemSet itemSet, ISourceViewer viewer, IResource resource) throws BadLocationException {
		this.contents = viewer.getDocument().get(offset, length);
		ITypedRegion region = new TypedRegion(offset, this.contents.length(), "default");
		this.offset = offset;
		this.length = length;
		String[] arr = splitContents();
		if (null != arr || arr.length > 0) {
			String s = arr[0];
			try {
				if (s.equals("list")) {
					directive = new ListDirective();
					directive.load(new TypedRegion(offset, this.contents.length(), "list"), viewer, resource);
					directive.setItemSet(itemSet);
				}
				else if (s.equals("if")) {
					directive = new IfDirective();
					directive.load(new TypedRegion(offset, this.contents.length(), "if"), viewer, resource);
					directive.setItemSet(itemSet);
				}
			}
			catch (Exception e) {
				Plugin.log(e);
				directive = this;
			}
		}
		try {
			load(region, viewer, resource);
		}
		catch (Exception e) {
			Plugin.log(e);
		}
	}

	public int getOffset() {
		return offset;
	}

	public String getFullContents() {
		return contents;
	}

	public int getLength() {
		return length;
	}

	public ICompletionProposal[] getCompletionProposals(int offset, Map context) {
		if (directive.equals(this)) return super.getCompletionProposals(offset, context);
		else return directive.getCompletionProposals(offset, context);
	}

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}
}