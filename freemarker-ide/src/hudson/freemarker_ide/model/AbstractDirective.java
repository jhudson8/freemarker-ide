package hudson.freemarker_ide.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;


public abstract class AbstractDirective extends AbstractItem {

	String contents;

	public String getContents() {
		if (null == contents) {
			contents = super.getContents();
			if (null != contents) {
				try {
					contents = contents.substring(2, contents.length()-1);
				}
				catch (StringIndexOutOfBoundsException e) {
				}
			}
		}
		return contents;
	}

	public static String[] directives = new String[] {
		"if", "else", "elseif", "switch", "case", "default", "break", "list",
		"break", "include", "import", "noparse", "compress", "escape", "noescape",
		"assign", "global", "local", "setting", "macro", "nested", "return", "flush", "function",
		"stop", "ftl", "t", "lt", "rt", "nt", "attempt", "recover", "visit", "recurse",
		"fallback"
	};
	public ICompletionProposal[] getCompletionProposals(int offset, Map context) {
		if (offset < 2) return null;
		ContentWithOffset contentWithOffset = splitContents(offset);
		int index = contentWithOffset.getIndex();
		if (index == 0) {
			int subOffset = contentWithOffset.getOffsetInIndex();
			int directiveOffset = contentWithOffset.getOffset();
			String[] contents = contentWithOffset.getContents();
			// name
			if (contentWithOffset.wasLastCharSpace()) {
				if (contents.length == 1) {
					// first param
					CompletionInterpolation completionInterpolation = completionInterpolation = new CompletionInterpolation(
							"${" , offset - contentWithOffset.getOffsetInIndex() - 2, getItemSet(), getResource());
					return completionInterpolation.getCompletionProposals(offset, context);
				}
				else {
					return null;
				}
			}
			String prefix = contents[index].substring(0, subOffset);
			List l = new ArrayList();
			for (int i=0; i<directives.length; i++) {
				String name = directives[i];
				if (name.startsWith(prefix)) {
					l.add(getCompletionProposal(offset, subOffset,
							name, contents[0]));
				}
			}
			return completionProposals(l);
		}
		else if (index == 1 && !contentWithOffset.wasLastCharSpace()) {
			String value = "";
			try {
				value = contentWithOffset.getContents()[index].substring(0, contentWithOffset.getOffsetInIndex());
			}
			catch (Exception e) {}
			CompletionInterpolation completionInterpolation = completionInterpolation = new CompletionInterpolation(
					"${" + value , offset - contentWithOffset.getOffsetInIndex() - 2, getItemSet(), getResource());
			return completionInterpolation.getCompletionProposals(offset, context);
		}
		return null;
	}

	public ICompletionProposal[] completionProposals (List l) {
		Collections.sort(l, new CompletionProposalComparator());
		return (ICompletionProposal[]) l.toArray(new ICompletionProposal[l.size()]);
	}

	public ICompletionProposal getCompletionProposal (int offset, int subOffset,
			String replacementString, String replacingString) {
		return new CompletionProposal (
				replacementString, offset-subOffset,
				replacingString.length(), replacementString.length());
	}

	public class CompletionProposalComparator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			return ((ICompletionProposal) arg0).getDisplayString().compareTo(((ICompletionProposal) arg1).getDisplayString());
		}
	}
}