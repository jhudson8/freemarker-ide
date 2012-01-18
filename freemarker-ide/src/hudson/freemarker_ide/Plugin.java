package hudson.freemarker_ide;

import hudson.freemarker_ide.preferences.IPreferenceConstants;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class Plugin extends AbstractUIPlugin implements IPreferenceConstants {

	//The shared instance.
	private static Plugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;

	public Plugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("hudson.freemarker_ide.resources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * The constructor.
	 */
	public Plugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static Plugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public static Plugin getInstance() {
		return plugin;
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	/**
	 * Initializes the plugin preferences with default preference values for
	 * this plug-in.
	 */
	protected void initializeDefaultPluginPreferences() {
		Preferences prefs = getPluginPreferences();
		prefs.setDefault(HIGHLIGHT_RELATED_ITEMS, true);
		prefs.setDefault(COLOR_COMMENT, "170,0,0");
		prefs.setDefault(COLOR_TEXT, "0,0,0");
		prefs.setDefault(COLOR_INTERPOLATION, "255,0,128");
		prefs.setDefault(COLOR_DIRECTIVE, "0,0,255");
		prefs.setDefault(COLOR_STRING, "0,128,128");
		prefs.setDefault(COLOR_XML_COMMENT, "128,128,128");
		prefs.setDefault(COLOR_XML_TAG, "0,0,128");
		prefs.setDefault(COLOR_RELATED_ITEM, "255,255,128");
	}

	public static void error (Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		MessageDialog.openError(Display.getCurrent().getActiveShell(), t.getMessage(), sw.toString());
		log(t);
	}

	public static void log (Throwable t) {
		t.printStackTrace();
        FileOutputStream fos = null;
        try {
            String logFileName = getInstance().getStateLocation() + "error.log";
            fos = new FileOutputStream(logFileName, true);
            fos.write(new Date().toString().getBytes());
            fos.write('\t');
            fos.write(t.getMessage().getBytes());
            fos.write("\n".getBytes());
    		StringWriter sw = new StringWriter();
    		t.printStackTrace(new PrintWriter(sw));
    		fos.write(sw.toString().getBytes());
    		fos.write("\n-----\n".getBytes());
        } catch (Exception e) {
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
	}
}