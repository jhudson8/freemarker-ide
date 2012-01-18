package hudson.freemarker_ide.editor.rules;

import java.util.Stack;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class InterpolationRule extends SingleLineRule {

    public InterpolationRule(char startChar, IToken token) {
        super(startChar + "{", "}", token);
    }

	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		int c;
		char[][] delimiters= scanner.getLegalLineDelimiters();
		boolean previousWasEscapeCharacter = false;	
		Stack keyStack = new Stack();
		int charsRead = 0;
		while ((c= scanner.read()) != ICharacterScanner.EOF) {
			charsRead ++;
			char cCheck = (char) c;
			if (c == '{') {
				if (keyStack.size() == 0) {
					break;
				}
			}
			else if (c == '\"') {
				if (keyStack.size() > 0 && keyStack.peek().equals("\"")) {
					keyStack.pop();
				}
				else {
					keyStack.push("\"");
				}
			}
			else if (c == '(') {
				if (keyStack.size() > 0 && keyStack.peek().equals("\"")) {
					// string... don't add to stack
				}
				else {
					keyStack.push("(");
				}
			}
			else if (c == ')') {
				if (keyStack.size() > 0 && keyStack.peek().equals("\"")) {
					// string... don't add to stack
				}
				else if (keyStack.size() > 0 && keyStack.peek().equals("(")) {
					keyStack.pop();
				}
			}
			else if (c == fEscapeCharacter) {
				// Skip the escaped character.
				scanner.read();
				charsRead ++;
			}
			else if (c == '}') {
				if (keyStack.size() == 0) {
					return true;
				}
			}
			else if (c == '\n') {
				break;
			}
			previousWasEscapeCharacter = (c == fEscapeCharacter);
		}
		if (fBreaksOnEOF) return true;
		for (int i=0; i<charsRead; i++)
			scanner.unread();
		return false;
	}
}