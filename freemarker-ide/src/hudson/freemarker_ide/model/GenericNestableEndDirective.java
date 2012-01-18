package hudson.freemarker_ide.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class GenericNestableEndDirective extends AbstractDirective {

	private String name;
	private GenericNestableDirective startDirective;
	
	public GenericNestableEndDirective (String name) {
		this.name = name;
	}

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public Item getStartItem() {
		if (getRelatedItems() != null && getRelatedItems().length > 0)
			return getRelatedItems()[0];
		else return null;
	}

	public boolean isEndItem() {
		return true;
	}

	public boolean relatesToItem(Item directive) {
		if (directive instanceof GenericNestableDirective) {
			return ((GenericNestableDirective) directive).getName().equals(name);
		}
		else return false;
	}

	public void relateItem(Item directive) {
		if (directive instanceof GenericNestableDirective) {
			startDirective = (GenericNestableDirective) directive;
		}
	}

	private Item[] relatedItems;
	public Item[] getRelatedItems() {
		if (null == relatedItems) {
			if (null != startDirective)
				relatedItems = new Item[]{startDirective};
			else
				relatedItems = new Item[0];
		}
		return relatedItems;
	}

	public String getName() {
		return name;
	}
}