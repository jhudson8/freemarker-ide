package hudson.freemarker_ide.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class IfDirective extends AbstractDirective {

	private IfEndDirective endDirective;
	private IfElseDirective elseDirective;
	private List elseIfDirectives = new ArrayList(1);

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isStartItem() {
		return true;
	}

	public void relateItem(Item directive) {
		if (directive instanceof IfElseDirective)
			elseDirective = (IfElseDirective) directive;
		else if (directive instanceof IfEndDirective)
			endDirective = (IfEndDirective) directive;
		else if (directive instanceof ElseIfDirective)
			elseIfDirectives.add((ElseIfDirective) directive);
	}

	public boolean relatesToItem(Item directive) {
		return (directive instanceof IfDirective
				|| directive instanceof IfElseDirective
				|| directive instanceof ElseIfDirective
				|| directive instanceof IfEndDirective);
	}

	public boolean isNestable() {
		return true;
	}

	public IfElseDirective getElseDirective() {
		return elseDirective;
	}

	public IfEndDirective getEndDirective() {
		return endDirective;
	}

	public List getElseIfDirectives () {
		return elseIfDirectives;
	}

	public Item[] getRelatedItems() {
		if (null == relatedItems) {
			ArrayList l = new ArrayList();
			l.add(this);
			if (null != getElseDirective())
				l.add(getElseDirective());
			if (null != getEndDirective())
				l.add(getEndDirective());
			l.addAll(getElseIfDirectives());
			relatedItems = (Item[]) l.toArray(new Item[l.size()]);
		}
		return relatedItems;
	}
	private Item[] relatedItems;

	public String getTreeImage() {
		return "if.png";
	}

	public Item getEndItem() {
		return endDirective;
	}

//	public ICompletionProposal[] getCompletionProposals(int offset, Map context) {
//		ICompletionProposal[] proposals = super.getCompletionProposals(offset, context);
//		if (null == proposals) {
//			ContentWithOffset contentWithOffset = splitContents(offset);
//			if (contentWithOffset.getIndex() > 0 || contentWithOffset.wasLastCharSpace()) {
//				int index = contentWithOffset.getIndex();
//				int offsetInIndex = contentWithOffset.getOffsetInIndex();
//				int indexOffset = contentWithOffset.getIndexOffset();
//				boolean wasLastCharSpace = contentWithOffset.wasLastCharSpace();
//				String contents = contentWithOffset.getContents()[index];
//				if (wasLastCharSpace || contentWithOffset.getContents()[index-1].equals("=") || offsetInIndex == 0) {
//					// first character
//					if (wasLastCharSpace) {
//						proposals = new CompletionInterpolation(
//								"${", offset-2, getItemSet(), getResource())
//								.getCompletionProposals(offset, context);
//					}
//					else {
//						proposals = new CompletionInterpolation(
//								"${" + contents, indexOffset-2, getItemSet(), getResource())
//								.getCompletionProposals(offset, context);
//					}
//				}
//				else {
//					proposals = new CompletionInterpolation(
//							"${" + contents, indexOffset-2, getItemSet(), getResource())
//							.getCompletionProposals(offset, context);
//				}
//			}
//		}
//		return proposals;
//	}
}