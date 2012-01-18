package hudson.freemarker_ide.editor.rules;

import org.eclipse.jface.text.rules.IToken;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class MacroInstanceRuleEnd extends DirectiveRuleEnd {

	public MacroInstanceRuleEnd(IToken token) {
		super("", token, false);
	}

	protected char getIdentifierChar() {
		return '@';
	}
}