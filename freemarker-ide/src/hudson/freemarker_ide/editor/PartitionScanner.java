package hudson.freemarker_ide.editor;

import hudson.freemarker_ide.editor.rules.DirectiveRule;
import hudson.freemarker_ide.editor.rules.DirectiveRuleEnd;
import hudson.freemarker_ide.editor.rules.GenericDirectiveRule;
import hudson.freemarker_ide.editor.rules.GenericDirectiveRuleEnd;
import hudson.freemarker_ide.editor.rules.InterpolationRule;
import hudson.freemarker_ide.editor.rules.MacroInstanceRule;
import hudson.freemarker_ide.editor.rules.MacroInstanceRuleEnd;
import hudson.freemarker_ide.editor.rules.XmlRule;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class PartitionScanner extends RuleBasedPartitionScanner {

	public final static String FTL_COMMENT = "__ftl_comment";
	public final static String FTL_INCLUDE = "__ftl_include";
	public final static String FTL_IMPORT = "__ftl_import";
	public final static String FTL_ASSIGN = "__ftl_assign";
	public final static String FTL_ASSIGN_END = "__ftl_assign_end";
	public final static String FTL_LOCAL = "__ftl_local";
	public final static String FTL_LOCAL_END = "__ftl_local_end";
	public final static String FTL_GLOBAL = "__ftl_global";
	public final static String FTL_GLOBAL_END = "__ftl_global_end";
	public final static String FTL_BREAK = "__ftl_break";
	public final static String FTL_NESTED = "__ftl_nested";
	public final static String FTL_RETURN = "__ftl_return";
	public final static String FTL_STOP = "__ftl_stop";
	public final static String FTL_FTL_DIRECTIVE = "__ftl_ftl_directive";
	public final static String FTL_FUNCTION_DIRECTIVE_START = "__ftl_function_directive_start";
	public final static String FTL_FUNCTION_DIRECTIVE_END = "__ftl_function_directive_end";
	public final static String FTL_LIST_DIRECTIVE_START = "__ftl_list_directive_start";
	public final static String FTL_LIST_DIRECTIVE_END = "__ftl_list_directive_end";
	public final static String FTL_MACRO_DIRECTIVE_START = "__ftl_macro_directive_start";
	public final static String FTL_MACRO_DIRECTIVE_END = "__ftl_macro_directive_end";
	public final static String FTL_MACRO_INSTANCE_START = "__ftl_macro_instance_start";
	public final static String FTL_MACRO_INSTANCE_END = "__ftl_macro_instance_end";
	public final static String FTL_SWITCH_DIRECTIVE_START = "__ftl_switch_directive_start";
	public final static String FTL_SWITCH_DIRECTIVE_END = "__ftl_switch_directive_end";
	public final static String FTL_CASE_DIRECTIVE_START = "__ftl_case_directive_start";
	public final static String FTL_CASE_DEFAULT_START = "__ftl_case_default_start";
	public final static String FTL_IF_DIRECTIVE_START = "__ftl_if_directive_start";
	public final static String FTL_IF_DIRECTIVE_END = "__ftl_if_directive_end";
	public final static String FTL_IF_ELSE_DIRECTIVE = "__ftl_if_else_directive";
	public final static String FTL_ELSE_IF_DIRECTIVE = "__ftl_else_if_directive";
	public final static String FTL_INTERPOLATION = "__ftl_interpolation";
	public final static String FTL_DIRECTIVE = "__ftl_directive";
	public final static String FTL_DIRECTIVE_END = "__ftl_directive_end";
    public final static String XML_TAG = "__xml_tag";
    public final static String XML_COMMENT = "__xml_comment";
    public final static String STRING = "__string";
   
    public final static String[] DIRECTIVES = {
    	FTL_INCLUDE, FTL_IMPORT, FTL_ASSIGN, FTL_ASSIGN_END, FTL_LOCAL, FTL_LOCAL_END, FTL_GLOBAL,
    	FTL_GLOBAL_END, FTL_BREAK, FTL_NESTED, FTL_RETURN, FTL_STOP, FTL_LIST_DIRECTIVE_START,
    	FTL_LIST_DIRECTIVE_END, FTL_IF_DIRECTIVE_START, FTL_ELSE_IF_DIRECTIVE, FTL_IF_ELSE_DIRECTIVE,
    	FTL_IF_DIRECTIVE_END, FTL_SWITCH_DIRECTIVE_START, FTL_SWITCH_DIRECTIVE_END, FTL_CASE_DIRECTIVE_START,
    	FTL_CASE_DEFAULT_START, FTL_MACRO_DIRECTIVE_START, FTL_MACRO_DIRECTIVE_END, FTL_MACRO_INSTANCE_START,
    	FTL_MACRO_INSTANCE_END, FTL_FTL_DIRECTIVE, FTL_FUNCTION_DIRECTIVE_START, FTL_FUNCTION_DIRECTIVE_END};

    /**
     * The array of partitions used.
     */
    public static String[] PARTITIONS = {
        IDocument.DEFAULT_CONTENT_TYPE,
        FTL_COMMENT,
        XML_TAG,
        XML_COMMENT,
        FTL_INTERPOLATION,
        STRING,
        FTL_DIRECTIVE,
        FTL_DIRECTIVE_END,
    };
   
    static {
    	String[] pSub = new String[PARTITIONS.length + DIRECTIVES.length];
    	int i=0;
    	for (int j=0; j<DIRECTIVES.length; j++) {
    		pSub[i++] = DIRECTIVES[j];
    	}
    	for (int j=0; j<PARTITIONS.length; j++) {
    		pSub[i++] = PARTITIONS[j];
    	}
    	PARTITIONS = pSub;
    }

    /**
     * Creates a new partition scanner.
     */
	public PartitionScanner() {
		List rules = new ArrayList();

		IToken ftlComment = new Token(FTL_COMMENT);

        rules.add(new MultiLineRule("<!--", "-->", new Token(XML_COMMENT)));
        rules.add(new MultiLineRule("<#--", "-->", ftlComment));
        rules.add(new MultiLineRule("[#--", "--]", ftlComment));

        rules.add(new DirectiveRule("ftl", new Token(FTL_FTL_DIRECTIVE)));
        rules.add(new DirectiveRule("if", new Token(FTL_IF_DIRECTIVE_START)));
        rules.add(new DirectiveRule("elseif", new Token(FTL_ELSE_IF_DIRECTIVE)));
        rules.add(new DirectiveRule("else", new Token(FTL_IF_ELSE_DIRECTIVE), true));
        rules.add(new DirectiveRuleEnd("if", new Token(FTL_IF_DIRECTIVE_END)));

        rules.add(new DirectiveRule("function", new Token(FTL_FUNCTION_DIRECTIVE_START)));
        rules.add(new DirectiveRuleEnd("function", new Token(FTL_FUNCTION_DIRECTIVE_END)));

        rules.add(new DirectiveRule("list", new Token(FTL_LIST_DIRECTIVE_START)));
        rules.add(new DirectiveRuleEnd("list", new Token(FTL_LIST_DIRECTIVE_END)));

        rules.add(new DirectiveRule("macro", new Token(FTL_MACRO_DIRECTIVE_START)));
        rules.add(new DirectiveRuleEnd("macro", new Token(FTL_MACRO_DIRECTIVE_END)));
        rules.add(new MacroInstanceRule(new Token(FTL_MACRO_INSTANCE_START)));
        rules.add(new MacroInstanceRuleEnd(new Token(FTL_MACRO_INSTANCE_END)));

        rules.add(new DirectiveRule("switch", new Token(FTL_SWITCH_DIRECTIVE_START)));
        rules.add(new DirectiveRuleEnd("switch", new Token(FTL_SWITCH_DIRECTIVE_END)));
        rules.add(new DirectiveRule("case", new Token(FTL_CASE_DIRECTIVE_START)));
        rules.add(new DirectiveRule("default", new Token(FTL_CASE_DEFAULT_START)));
        
        rules.add(new DirectiveRule("assign", new Token(FTL_ASSIGN)));
        rules.add(new DirectiveRuleEnd("assign", new Token(FTL_ASSIGN_END)));
        rules.add(new DirectiveRule("local", new Token(FTL_LOCAL)));
        rules.add(new DirectiveRuleEnd("local", new Token(FTL_LOCAL_END)));
        rules.add(new DirectiveRule("global", new Token(FTL_GLOBAL)));
        rules.add(new DirectiveRuleEnd("global", new Token(FTL_GLOBAL_END)));

        rules.add(new DirectiveRule("include", new Token(FTL_INCLUDE)));
        rules.add(new DirectiveRule("import", new Token(FTL_IMPORT)));
        rules.add(new DirectiveRule("break", new Token(FTL_BREAK)));
        rules.add(new DirectiveRule("stop", new Token(FTL_STOP)));
        rules.add(new DirectiveRule("nested", new Token(FTL_NESTED)));
        rules.add(new DirectiveRule("return", new Token(FTL_RETURN)));
        
        rules.add(new GenericDirectiveRule(new Token(FTL_DIRECTIVE)));
        rules.add(new GenericDirectiveRuleEnd(new Token(FTL_DIRECTIVE_END)));

        rules.add(new InterpolationRule('$', new Token(FTL_INTERPOLATION)));
        rules.add(new InterpolationRule('#', new Token(FTL_INTERPOLATION)));

        rules.add(new XmlRule(new Token(XML_TAG)));
        
		IPredicateRule[] result= new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}