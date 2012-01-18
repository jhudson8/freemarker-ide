package hudson.freemarker_ide.model.interpolation;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public interface Fragment {

	public int getOffset();
	public int getLength();
	public Class getReturnClass (Class parentClass, List fragments, Map context, IResource resource, IProject project);
	public Class getSingularReturnClass (Class parentClass, List fragments, Map context, IResource resource, IProject project);
	public ICompletionProposal[] getCompletionProposals (int subOffset, int offset, Class parentClass,
			List fragments, ISourceViewer sourceViewer, Map context, IResource file, IProject project);
}
