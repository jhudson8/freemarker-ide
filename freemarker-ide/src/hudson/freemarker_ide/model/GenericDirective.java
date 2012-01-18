package hudson.freemarker_ide.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class GenericDirective extends AbstractDirective {

	private String image;

	public GenericDirective (String image) {
		this.image = image;
	}

	protected void init(ITypedRegion region, ISourceViewer viewer)
			throws Exception {
	}

	public String getTreeImage() {
		return image;
	}

	public boolean isNestable() {
		return false;
	}

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}
}