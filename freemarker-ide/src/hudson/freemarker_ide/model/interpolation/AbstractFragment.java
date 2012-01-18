package hudson.freemarker_ide.model.interpolation;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public abstract class AbstractFragment implements Fragment {

	private int offset;
	private String content;

	public AbstractFragment (int offset, String content) {
		this.content = content;
		this.offset = offset;
	}

	public int getLength() {
		return content.length();
	}

	public int getOffset() {
		return offset;
	}

	public String getContent() {
		return content;
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

	protected boolean instanceOf (Class test, Class base) {
		if (null == test || null == base) return false;
		while (null != test) {
			for (int i=0; i<test.getInterfaces().length; i++) {
				if (test.getInterfaces()[i].getClass().getName().equals(base.getName())) return true;
			}
			if (test.getName().equals(base.getName())) return true;
			test = test.getSuperclass();
		}
		return false;
	}

	public class CompletionProposalComparator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			return ((ICompletionProposal) arg0).getDisplayString().compareTo(((ICompletionProposal) arg1).getDisplayString());
		}
	}

	public Class getSingularReturnClass(Class parentClass, List fragments, Map context, IResource resource, IProject project) {
		return Object.class;
	}
}