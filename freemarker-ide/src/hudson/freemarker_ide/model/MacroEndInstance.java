package hudson.freemarker_ide.model;

import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;

public class MacroEndInstance extends AbstractDirective {

	private MacroInstance macroInstance;
	private String name;

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
		name = getSplitValue(0);
	}
		

	public boolean isEndItem() {
		return true;
	}

	public void relateItem(Item directive) {
		if (directive instanceof MacroInstance)
			macroInstance = (MacroInstance) directive;
	}

	public boolean relatesToItem(Item directive) {
		if (directive instanceof MacroInstance) {
			MacroInstance macroInstance = (MacroInstance) directive;
			return macroInstance.relatesToItem(this);
		}
		else return false;
	}

	public MacroInstance getMacroDirective() {
		return macroInstance;
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

	public String getName() {
		return name;
	}

	private String contents;
	public String getContents() {
		if (null == contents) {
			try {
				contents = getViewer().getDocument().get(
						getRegion().getOffset(), getRegion().getLength());
			}
			catch (BadLocationException e) {}
			if (null != contents) {
				contents = contents.trim();
				contents = contents.substring(3, contents.length()-1);
			}
		}
		return contents;
	}
}