package hudson.freemarker_ide.model.interpolation;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class ParameterSet {

	private Class returnClass;
	private String[] parameters;

	public ParameterSet (Class returnClass, String[] parameters) {
		this.returnClass = returnClass;
		this.parameters = parameters;
	}

	public Class getReturnClass() {
		return returnClass;
	}

	public String[] getParameters() {
		return parameters;
	}
}
