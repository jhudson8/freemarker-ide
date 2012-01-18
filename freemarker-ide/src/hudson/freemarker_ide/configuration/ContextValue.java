package hudson.freemarker_ide.configuration;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class ContextValue {

    public String name;
    public Class objClass;
    public Class singularClass;

    public ContextValue (String name, Class objClass, Class singularClass) {
        this.name = name;
        this.objClass = objClass;
        this.singularClass = singularClass;
    }
}
