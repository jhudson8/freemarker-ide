package hudson.freemarker_ide.outline;

import hudson.freemarker_ide.ImageManager;
import hudson.freemarker_ide.model.Item;
import hudson.freemarker_ide.preferences.IPreferenceConstants;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class OutlineLabelProvider
	extends LabelProvider
	implements IPreferenceConstants {

	public OutlineLabelProvider() {
		super();
	}

	public Image getImage(Object anElement) {
		if (null == anElement)
			return null;
		if (anElement instanceof Item) {
			return ImageManager.getImage(((Item) anElement).getTreeImage());
		}
		else {
			return null;
		}
	}

	public String getText(Object anElement) {
		if (anElement instanceof Item)
			return ((Item) anElement).getTreeDisplay();
		else
			return null;
	}

	public void dispose() {
	}	
}
