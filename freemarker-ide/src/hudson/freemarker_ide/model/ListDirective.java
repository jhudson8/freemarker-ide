package hudson.freemarker_ide.model;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;


public class ListDirective extends AbstractDirective {

	private ListEndDirective endDirective;

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isStartItem() {
		return super.isNestable();
	}

	public void relateItem(Item directive) {
		if (directive instanceof ListEndDirective)
			endDirective = (ListEndDirective) directive;
	}

	public boolean relatesToItem(Item directive) {
		return (directive instanceof ListEndDirective);
	}

	public boolean isNestable() {
		return super.isNestable();
	}

	public ListEndDirective getEndDirective() {
		return endDirective;
	}

	public Item getRelatedItem() {
		return getEndDirective();
	}

	public String getTreeImage() {
		return "list.png";
	}

	public ICompletionProposal[] getCompletionProposals(int offset, Map context) {
		ICompletionProposal[] proposals = super.getCompletionProposals(offset, context);
		if (null == proposals) {
			ContentWithOffset contentWithOffset = splitContents(offset);
			int index = contentWithOffset.getIndex();
			if (index == 1 || (index == 0 && contentWithOffset.wasLastCharSpace())) {
				String value = "";
				if (contentWithOffset.getContents().length >= 2)
					value = contentWithOffset.getContents()[1];
				CompletionInterpolation completionInterpolation = completionInterpolation = new CompletionInterpolation(
							"${" + value, offset - contentWithOffset.getOffsetInIndex() - 2, getItemSet(), getResource());
				proposals = completionInterpolation.getCompletionProposals(offset, context);
			}
		}
		return proposals;
	}

	public void addToContext(Map context) {
		String[] contents = splitContents();
		if (contents.length == 4) {
			String key = contents[3];
			CompletionInterpolation completionInterpolation = new CompletionInterpolation("${" + contents[1], 0, getItemSet(), getResource());
			context.put(key, completionInterpolation.getSingularReturnClass(context));
		}
	}

	public void removeFromContext(Map context) {
		String[] contents = splitContents();
		if (contents.length == 4) {
			String key = contents[3];
			context.remove(key);
		}
	}

	public Item getEndItem() {
		return endDirective;
	}
}