package hudson.freemarker_ide.editor.rules;

import java.util.Stack;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class XmlRule extends MultiLineRule {

	public XmlRule(IToken token) {
		super("<", ">", token);
	}
	protected boolean sequenceDetected(
		ICharacterScanner scanner,
		char[] sequence,
		boolean eofAllowed) {
		int c = scanner.read();
		if (sequence[0] == '<') {
			if (c == '?') {
				// processing instruction - abort
				scanner.unread();
				return false;
			}
			if (c == '!') {
				scanner.unread();
				// comment - abort
				return false;
			}
			if (c == '#') {
				scanner.unread();
				// directive - abort
				return false;
			}
		} else if (sequence[0] == '>') {
			scanner.unread();
		}
		return super.sequenceDetected(scanner, sequence, eofAllowed);
	}

	private int LT = '<';
	private int LB = '[';
	private int GT = '>';
	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		int c;
		char[][] delimiters= scanner.getLegalLineDelimiters();
		boolean previousWasEscapeCharacter = false;	
		Stack stack = new Stack();
		while ((c= scanner.read()) != ICharacterScanner.EOF) {
			if (c == fEscapeCharacter) {
				// Skip the escaped character.
				scanner.read();
			} else if (fEndSequence.length > 0 && c == fEndSequence[0]) {
				// Check if the specified end sequence has been found.
				if (sequenceDetected(scanner, fEndSequence, true))
					return true;
			} else if (fBreaksOnEOL) {
				// Check for end of line since it can be used to terminate the pattern.
				for (int i= 0; i < delimiters.length; i++) {
					if (c == delimiters[i][0] && sequenceDetected(scanner, delimiters[i], true)) {
						if (!fEscapeContinuesLine || !previousWasEscapeCharacter)
							return true;
					}
				}
			}
			else if (c == '\"') {
				if (stack.size() > 0 && stack.peek().equals("\""))
					stack.pop();
			}
			else if (c == LT || c == LB) {
				break;
			}
			else if (c == '$') {
				int cNext = scanner.read();
				if (cNext == ICharacterScanner.EOF) break;
				else if (cNext == '{') {
					stack.push(new String(new char[]{(char) c}));
					scanner.unread();
				}
				if (stack.size() == 0) break;
			}
			else if (c == '}') {
				if (stack.size() > 0 && stack.peek().equals("{"))
					stack.pop();
			}
			previousWasEscapeCharacter = (c == fEscapeCharacter);
		}
		if (fBreaksOnEOF) return true;
		scanner.unread();
		return false;
	}
}
