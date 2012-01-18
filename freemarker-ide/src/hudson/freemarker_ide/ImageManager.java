package hudson.freemarker_ide;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class ImageManager {

	public static final String IMG_MACRO = "icons/userdefined_directive_call.gif";
	public static final String IMG_IMPORT = "icons/import.gif";
	public static final String IMG_IMPORT_COLLECTION = "icons/import_collection.gif";
	public static final String IMG_FUNCTION = "icons/function.gif";
	

	public static Image getImage(String filename) {
		if (null == filename) return null;
		ImageDescriptor temp = getImageDescriptor(filename);
		if(null!=temp) {
			return temp.createImage();
		} else {
			return null;
		}
	}
	
	public static ImageDescriptor getImageDescriptor(String filename) {
		if (null == filename) return null;
		try {
		URL url = new URL(Plugin.getInstance().getDescriptor().getInstallURL(),
                  "icons/" + filename);
                  return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException mue) {
			
		}
		return null;
	}

}
