package hudson.freemarker_ide.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class StringSubRule extends SingleLineRule {

	private int unReadAmount;
	public StringSubRule(String startSequence, String endSequence, int unReadAmount, IToken token) {
		super(startSequence, endSequence, token);
	}

	protected boolean sequenceDetected(ICharacterScanner scanner,
			char[] sequence, boolean eofAllowed) {
		return true;
	}

	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		for (int i=0; i<unReadAmount; i++)
			scanner.unread();
		return true;
	}
}