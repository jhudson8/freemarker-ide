package hudson.freemarker_ide.model;

import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class MacroEndDirective extends AbstractDirective {

	private MacroDirective macroDirective;

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isEndItem() {
		return true;
	}

	public void relateItem(Item directive) {
		if (directive instanceof MacroDirective)
			macroDirective = (MacroDirective) directive;
	}

	public boolean relatesToItem(Item directive) {
		return (directive instanceof MacroDirective);
	}

	public MacroDirective getMacroDirective() {
		return macroDirective;
	}

	public Item[] getRelatedItems() {
		if (null == relatedItems) {
			ArrayList l = new ArrayList();
			if (null != getMacroDirective()) {
				l.add(getMacroDirective());
			}
			relatedItems = (Item[]) l.toArray(new Item[l.size()]);
		}
		return relatedItems;
	}
	private Item[] relatedItems;

	public Item getStartItem () {
		return getMacroDirective();
	}
}