package hudson.freemarker_ide.editor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class WhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
