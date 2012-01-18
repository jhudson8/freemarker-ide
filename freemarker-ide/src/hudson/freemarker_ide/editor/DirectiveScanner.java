package hudson.freemarker_ide.editor;

import java.util.Vector;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class DirectiveScanner extends RuleBasedScanner {

	public DirectiveScanner(ColorManager manager) {
		IToken string =
			new Token(
				new TextAttribute(
						manager.getColorFromPreference(ColorManager.COLOR_STRING)));

		Vector rules = new Vector();
		// Add rule for double quotes
		rules.add(new SingleLineRule("\"", "\"", string,'\\'));
	    // Add rule for single quotes
	    rules.add(new SingleLineRule("'", "'", string,'\\'));
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new WhitespaceDetector()));
	
		IRule[] result = new IRule[rules.size()];
		rules.copyInto(result);
		setRules(result);
	}
}
