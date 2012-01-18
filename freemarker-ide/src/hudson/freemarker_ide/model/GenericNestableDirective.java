package hudson.freemarker_ide.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class GenericNestableDirective extends AbstractDirective {

	private String image;
	private String name;
	private GenericNestableEndDirective endDirective;
	
	public GenericNestableDirective (String name, String image) {
		this.name = name;
		this.image = image;
	}

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public String getTreeImage() {
		return image;
	}

	public Item getStartItem() {
		return this;
	}

	public boolean isNestable() {
		return !getContents().trim().endsWith("/");
	}

	public boolean isStartItem() {
		return true;
	}

	public boolean relatesToItem(Item directive) {
		if (directive instanceof GenericNestableEndDirective) {
			return ((GenericNestableEndDirective) directive).getName().equals(name);
		}
		else return false;
	}

	public void relateItem(Item directive) {
		if (directive instanceof GenericNestableEndDirective) {
			endDirective = (GenericNestableEndDirective) directive;
		}
	}

	public String getName() {
		return name;
	}

	public Item getEndItem() {
		return endDirective;
	}

	private Item[] relatedItems;
	public Item[] getRelatedItems() {
		if (null == relatedItems) {
			if (null != endDirective)
				relatedItems = new Item[]{endDirective};
			else
				relatedItems = new Item[0];
		}
		return relatedItems;
	}
}