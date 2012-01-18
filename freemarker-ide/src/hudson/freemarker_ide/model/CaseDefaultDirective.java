package hudson.freemarker_ide.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class CaseDefaultDirective extends AbstractDirective {

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isStartAndEndItem() {
		return true;
	}

	public boolean relatesToItem(Item directive) {
		return (directive instanceof CaseDirective
				|| directive instanceof CaseDefaultDirective);
	}

	public boolean isNestable() {
		return true;
	}

	public String getTreeImage() {
		return "default.png";
	}

	public void relateItem(Item directive) {
		if (null == relatedItemsArr) {
			List dRelatedItems = null;
			if (directive instanceof CaseDirective)
				dRelatedItems = ((CaseDirective) directive).getRelatedItemsArray();
			else if (directive instanceof CaseDirective)
				dRelatedItems = ((CaseDefaultDirective) directive).getRelatedItemsArray();
			relatedItemsArr = dRelatedItems;
		}
		if (null == relatedItemsArr)
			relatedItemsArr = new ArrayList();
		if (!relatedItemsArr.contains(directive))
			relatedItemsArr.add(directive);
	}

	public void relateItemNoRecurse (Item directive) {
		if (null == relatedItemsArr)
			relatedItemsArr = new ArrayList();
		if (!relatedItemsArr.contains(directive))
			relatedItemsArr.add(directive);
	}

	public List getRelatedItemsArray () {
		return relatedItemsArr;
	}
}