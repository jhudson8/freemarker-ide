package hudson.freemarker_ide.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class FtlDirective extends AbstractDirective {

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isNestable() {
		return false;
	}

	public Item[] getRelatedItems() {
		return relatedItems;
	}
	private Item[] relatedItems = new Item[0];

	public String getTreeImage() {
		return "element.png";
	}
}