package hudson.freemarker_ide.editor;

import hudson.freemarker_ide.Plugin;
import hudson.freemarker_ide.preferences.IPreferenceConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;


/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class ColorManager implements IPreferenceConstants {

	protected Map fColorTable = new HashMap(10);

	public void dispose() {
		Iterator e= fColorTable.values().iterator();
		while (e.hasNext())
			((Color) e.next()).dispose();
	}
	
	
	public Color getColor(RGB rgb) {
		Color color= (Color) fColorTable.get(rgb);
		if (color == null) {
			color= new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
	
	public Color getColor(String rgbString) {
		int red=0, green=0, blue=0;
		if(null!=rgbString) {
			StringTokenizer tok = new StringTokenizer(rgbString, ",");
			if(tok.countTokens() == 3) {		
				red = Integer.parseInt(tok.nextToken());
				green = Integer.parseInt(tok.nextToken());
				blue = Integer.parseInt(tok.nextToken());
			}
		}
		return getColor(new RGB(red, green, blue));	
	} 

	public Color getColorFromPreference(String preferenceID) {
		return getColor(Plugin.getInstance().getPreferenceStore().getString(preferenceID));
	}
}