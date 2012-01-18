package hudson.freemarker_ide.model;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class AssignmentDirective extends AbstractDirective {

	private AssignmentEndDirective endDirective;
	private String type;
	
	public AssignmentDirective (String type) {
		this.type = type;
	}

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isStartItem() {
		return super.isNestable();
	}

	public void relateItem(Item directive) {
		if (directive instanceof AssignmentEndDirective)
			endDirective = (AssignmentEndDirective) directive;
	}

	public boolean relatesToItem(Item directive) {
		return (directive instanceof AssignmentEndDirective);
	}

	public boolean isNestable() {
		return (null != getContents() && !getContents().endsWith("/"));
	}

	public AssignmentEndDirective getEndDirective() {
		return endDirective;
	}

	public Item[] getRelatedItems() {
		if (null == relatedItems) {
			ArrayList l = new ArrayList();
			if (null != endDirective)
				l.add(endDirective);
			relatedItems = (Item[]) l.toArray(new Item[l.size()]);
		}
		return relatedItems;
	}
	private Item[] relatedItems;

	public String getTreeImage() {
		return "assign.png";
	}

	Map contextValues;
	public void addToContext(Map context) {
		if (null == contextValues) {
			String[] values = splitContents();
			String key = null;
			String value = null;
			if (values.length >= 2) key = values[1];
			if (values.length >= 4) value = values[3];
			Class valueClass = null;
			if (null != value && value.length() > 0) {
				if (value.charAt(0) == '\"') valueClass = String.class;
				else if (Character.isDigit(value.charAt(0))) valueClass = Number.class;
				else {
					CompletionInterpolation completionInterpolation =
						new CompletionInterpolation("${" + value, 2, getItemSet(), getResource());
					valueClass = completionInterpolation.getReturnClass(context);
				}
			}
			if (null != key) {
				context.put(key, valueClass);
			}
		}
		super.addToContext(context);
	}

	public Item getEndItem() {
		return endDirective;
	}
}