package hudson.freemarker_ide.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class ListEndDirective extends AbstractDirective {

	private ListDirective listDirective;

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isEndItem() {
		return true;
	}

	public void relateItem(Item directive) {
		if (directive instanceof ListDirective)
			listDirective = (ListDirective) directive;
	}

	public boolean relatesToItem(Item directive) {
		return (directive instanceof IfElseDirective || directive instanceof ListDirective);
	}

	public ListDirective getListDirective() {
		return listDirective;
	}

	public Item getRelatedItem() {
		return getListDirective();
	}

	public Item getStartItem () {
		return getListDirective();
	}
}