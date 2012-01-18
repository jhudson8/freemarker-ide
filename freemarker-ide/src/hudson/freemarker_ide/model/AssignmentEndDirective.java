package hudson.freemarker_ide.model;

import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class AssignmentEndDirective extends AbstractDirective {

	private AssignmentDirective assignmentDirective;
	private String type;
	
	public AssignmentEndDirective (String type) {
		this.type = type;
	}

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isEndItem() {
		return true;
	}

	public void relateItem(Item directive) {
		if (directive instanceof AssignmentDirective)
			assignmentDirective = (AssignmentDirective) directive;
	}

	public boolean relatesToItem(Item directive) {
		return (directive instanceof AssignmentDirective);
	}

	public AssignmentDirective getAssignmentDirective() {
		return assignmentDirective;
	}

	public Item[] getRelatedItems() {
		if (null == relatedItems) {
			ArrayList l = new ArrayList();
			if (null != assignmentDirective) {
				l.add(assignmentDirective);
			}
			relatedItems = (Item[]) l.toArray(new Item[l.size()]);
		}
		return relatedItems;
	}
	private Item[] relatedItems;

	public Item getStartItem () {
		return getAssignmentDirective();
	}
}