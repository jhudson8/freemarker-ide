package hudson.freemarker_ide.editor;

import java.util.ArrayList;
import java.util.List;

import hudson.freemarker_ide.editor.rules.InterpolationRule;
import hudson.freemarker_ide.editor.rules.StringSubRule;

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
public class XMLTagScanner extends RuleBasedScanner {

	private IToken lastToken;
	
	public IToken nextToken() {
		lastToken = super.nextToken();
		return lastToken;
	}

	public IToken getLastToken () {
		return lastToken;
	}

	public XMLTagScanner(ColorManager manager) {
		IToken string =
			new Token(
				new TextAttribute(
						manager.getColorFromPreference(ColorManager.COLOR_STRING)));
		IToken interpolation =
			new Token(
				new TextAttribute(
						manager.getColorFromPreference(ColorManager.COLOR_INTERPOLATION)));

		List l = new ArrayList();

		l.add(new StringSubRule("\"", "${", 2, string));
		l.add(new InterpolationRule('$', interpolation));
		l.add(new InterpolationRule('#', interpolation));

		l.add(new SingleLineRule("\"", "\"", string, '\\'));
		l.add(new SingleLineRule("'", "'", string, '\\'));
		l.add(new WhitespaceRule(new WhitespaceDetector()));
		
		setRules((IRule[]) l.toArray(new IRule[l.size()]));
	}
}
