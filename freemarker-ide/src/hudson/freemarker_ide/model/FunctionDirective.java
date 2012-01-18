package hudson.freemarker_ide.model;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;


public class FunctionDirective extends AbstractDirective {

	private FunctionEndDirective endDirective;

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isStartItem() {
		return super.isNestable();
	}

	public void relateItem(Item directive) {
		if (directive instanceof FunctionEndDirective)
			endDirective = (FunctionEndDirective) directive;
	}

	public boolean relatesToItem(Item directive) {
		return (directive instanceof FunctionEndDirective);
	}

	public FunctionEndDirective getEndDirective() {
		return endDirective;
	}

	public Item getRelatedItem() {
		return getEndDirective();
	}

	public String getTreeImage() {
		return "element.png";
	}

	public void addToContext(Map context) {
		String[] contents = splitContents();
		if (contents.length >= 2) {
			String key = contents[1];
			context.put(key, Object.class);
		}
	}

	public Item getEndItem() {
		return endDirective;
	}
}