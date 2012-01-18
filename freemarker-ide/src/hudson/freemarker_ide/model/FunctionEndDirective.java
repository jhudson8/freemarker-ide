package hudson.freemarker_ide.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class FunctionEndDirective extends AbstractDirective {

	private FunctionDirective functionDirective;

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public boolean isEndItem() {
		return true;
	}

	public void relateItem(Item directive) {
		if (directive instanceof FunctionDirective)
			functionDirective = (FunctionDirective) directive;
	}

	public boolean relatesToItem(Item directive) {
		return (directive instanceof FunctionDirective);
	}

	public FunctionDirective getFunctionDirective() {
		return functionDirective;
	}

	public Item getRelatedItem() {
		return getFunctionDirective();
	}

	public Item getStartItem () {
		return getFunctionDirective();
	}
}