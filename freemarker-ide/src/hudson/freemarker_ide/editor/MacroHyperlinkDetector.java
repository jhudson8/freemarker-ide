package hudson.freemarker_ide.editor;

import java.util.Iterator;
import java.util.List;

import hudson.freemarker_ide.configuration.ConfigurationManager;
import hudson.freemarker_ide.configuration.MacroLibrary;
import hudson.freemarker_ide.model.Item;
import hudson.freemarker_ide.model.MacroDirective;
import hudson.freemarker_ide.model.MacroInstance;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;

public class MacroHyperlinkDetector implements IHyperlinkDetector {

	private Editor editor;
	public MacroHyperlinkDetector(ITextViewer textViewer, Editor editor) {
		this.editor = editor;
	}

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		Item item = editor.getItemSet().getItem(region.getOffset());
		if (null != item && item instanceof MacroInstance) {
			MacroInstance instance = (MacroInstance) item;
			int index = instance.getName().indexOf('.');
			if (index > 0) {
				// it is from a macro library
				String namespace = instance.getName().substring(0, index);
				MacroLibrary macroLibrary = ConfigurationManager.getInstance(editor.getProject()).getMacroLibrary(namespace);
				if (null != macroLibrary) {
					for (int i=0; i<macroLibrary.getMacros().length; i++) {
						if (macroLibrary.getMacros()[i].getName().equals(instance.getName())) {
							// we have a match
							return new IHyperlink[]{new MacroHyperlink(
									instance, macroLibrary.getFile(),
									macroLibrary.getMacros()[i].getOffset(), macroLibrary.getMacros()[i].getLength())};
						}
					}
				}
				if (null != macroLibrary)
					return new IHyperlink[]{new MacroHyperlink(instance, macroLibrary.getFile(), -1, -1)};
			}
			else {
				List macroDefinitions = instance.getItemSet().getMacroDefinitions();
				for (Iterator i=macroDefinitions.iterator(); i.hasNext(); ) {
					MacroDirective macroDefinition = (MacroDirective) i.next();
					if (macroDefinition.getName().equals(instance.getName())) {
						return new IHyperlink[]{new MacroHyperlink(
								instance, editor.getFile(),
								macroDefinition.getOffset(), macroDefinition.getLength())};
					}
				}
			}
		}
		return null;
	}
}
