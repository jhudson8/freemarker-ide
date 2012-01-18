package hudson.freemarker_ide.editor.rules;

import org.eclipse.jface.text.rules.IToken;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class MacroInstanceRule extends DirectiveRule {

	public MacroInstanceRule(IToken token) {
		super("", token, false);
	}

	protected char getIdentifierChar() {
		return '@';
	}
}