package hudson.freemarker_ide.editor.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class GenericDirectiveRuleEnd extends GenericDirectiveRule {

	public GenericDirectiveRuleEnd(IToken token) {
		super(token);
	}

	/**
	 * Evaluates this rules without considering any column constraints. Resumes
	 * detection, i.e. look sonly for the end sequence required by this rule if the
	 * <code>resume</code> flag is set.
	 *
	 * @param scanner the character scanner to be used
	 * @param resume <code>true</code> if detection should be resumed, <code>false</code> otherwise
	 * @return the token resulting from this evaluation
	 * @since 2.0
	 */
	protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
		if (resume) {
			if (endSequenceDetected(scanner))
				return fToken;
		} else {
			int c= scanner.read();
			char cCheck = (char) c;
			if (c == START_SEQUENCES[0] || c == START_SEQUENCES[1]) {
				int c2 = scanner.read();
				if (c2 == '/') {
					// check for the sequence identifier
					c2 = scanner.read();
					if (c2 == getIdentifierChar()) {
						if (sequenceDetected(scanner, c, false)) {
							if (endSequenceDetected(scanner, c))
								return fToken;
						}
					}
					scanner.unread();
				}
				scanner.unread();
			}
		}
		scanner.unread();
		return Token.UNDEFINED;
	}
}