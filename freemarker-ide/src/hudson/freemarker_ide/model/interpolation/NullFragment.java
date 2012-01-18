package hudson.freemarker_ide.model.interpolation;


/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class NullFragment extends NameFragment {

	public NullFragment() {
		super(0, "");
	}

	public boolean isStartFragment() {
		return true;
	}
}