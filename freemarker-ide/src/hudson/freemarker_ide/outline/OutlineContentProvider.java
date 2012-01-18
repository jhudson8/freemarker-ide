package hudson.freemarker_ide.outline;

import hudson.freemarker_ide.editor.Editor;
import hudson.freemarker_ide.model.Item;
import hudson.freemarker_ide.model.ItemSet;
import hudson.freemarker_ide.model.MacroDirective;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class OutlineContentProvider implements ITreeContentProvider {
	private Editor fEditor;

	public OutlineContentProvider(Editor anEditor) {
		fEditor = anEditor;
	}

	public void inputChanged(
		Viewer aViewer,
		Object anOldInput,
		Object aNewInput) {
	}

	public boolean isDeleted(Object anElement) {
		return false;
	}

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		ItemSet itemSet = null;
		if (inputElement instanceof ItemSet)
			itemSet = (ItemSet) inputElement;
		else
			itemSet = fEditor.getItemSet();
		List rootItems = new ArrayList();
		
		rootItems.addAll(fEditor.getItemSet().getMacroDefinitions());
		Item[] items = fEditor.getItemSet().getRootItems();
		for (int i=0; i<items.length; i++) {
			if (!(items[i] instanceof MacroDirective)) 
				rootItems.add(items[i]);
		}
		return rootItems.toArray();
	}

	public Object[] getChildren(Object anElement) {
		if (anElement instanceof Item) {
			if (anElement instanceof MacroDirective) return null;
			Object[] items = ((Item) anElement).getChildItems().toArray(
					new Item[((Item) anElement).getChildItems().size()]);
			List l = new ArrayList(items.length);
			for (int i=0; i<items.length; i++) {
				if (!(items[i] instanceof MacroDirective))
					l.add(items[i]);
			}
			return l.toArray();
		}
		else
			return null;
	}

	public Object getParent(Object anElement) {
		if (anElement instanceof Item)
			return ((Item) anElement).getParentItem();
		else
			return null;
	}

	public boolean hasChildren(Object anElement) {
		if (anElement instanceof Item)
			if (anElement instanceof MacroDirective) return false;
			else return ((Item) anElement).getChildItems().size() > 0;
		else
			return false;
	}
}