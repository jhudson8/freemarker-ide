 package hudson.freemarker_ide.model;

import hudson.freemarker_ide.model.interpolation.BuiltInFragment;
import hudson.freemarker_ide.model.interpolation.Fragment;
import hudson.freemarker_ide.model.interpolation.NameFragment;
import hudson.freemarker_ide.model.interpolation.NullFragment;
import hudson.freemarker_ide.model.interpolation.ParametersFragment;
import hudson.freemarker_ide.model.interpolation.StringFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;


public class Interpolation extends AbstractDirective {

	private List fragments;

	protected void init(ITypedRegion region, ISourceViewer viewer, IResource resource) throws Exception {
	}

	public String getTreeImage() {
		return "interpolation.png";
	}

	public synchronized ICompletionProposal[] getCompletionProposals(int offset, Map context) {
		Item parent = getParentItem();
		List parents = new ArrayList();
		Item tempItem = getParentItem();
		while (null != parent) {
			parents.add(0, parent);
			parent = parent.getParentItem();
			if (null != parent) tempItem = parent;
		}
		if (null == tempItem) tempItem = this;
		Item[] items = getItemSet().getRootItems();
		for (int i=0; i<items.length; i++) {
			Item item = items[i];
			if (tempItem.equals(item)) break;
			if (item.isStartItem()) {
				item.addToContext(context);
				if (null != item.getEndItem() && item.getEndItem().getRegion().getOffset() < (offset-1))
					item.removeFromContext(context);
			}
			else item.addToContext(context);
		}
		for (Iterator i=parents.iterator(); i.hasNext(); ) {
			Item item = (Item) i.next();
			for (Iterator i2=item.getChildItems().iterator(); i2.hasNext(); ) {
				Item item2 = (Item) i2.next();
				if (parents.contains(item2)) break;
				item2.addToContext(context);
			}
			item.addToContext(context);
		}

		initFragments();
		// find the fragment matching the offset
		int subOffset = offset - getOffset() - 2;
		if (subOffset < 0) return null;
		Fragment fragment = null;
		for (Iterator i = fragments.iterator(); i.hasNext(); ) {
			Fragment fragmentSub = (Fragment) i.next();
			if (fragmentSub.getOffset() <= subOffset) fragment = fragmentSub;
			else break;
		}
		if (null != fragment) {
			// find the parent class
			Class parentClass = null;
			for (Iterator i = fragments.iterator(); i.hasNext(); ) {
				Fragment fragmentSub = (Fragment) i.next();
				if (fragmentSub.equals(fragment)) break;
				else parentClass = fragmentSub.getReturnClass(parentClass, fragments, context, getResource(), getResource().getProject());
			}
			return fragment.getCompletionProposals(
				subOffset - fragment.getOffset(), offset, parentClass, fragments, getViewer(), context, getResource(), getResource().getProject());
		}
		else if (getContents().length() == 0 && subOffset == 0) {
			return new NullFragment().getCompletionProposals(
					subOffset, offset, null, fragments, getViewer(), context, getResource(), getResource().getProject());
		}
		else return null;
	}

	private synchronized void initFragments () {
		if (null != fragments) return;
		fragments = new ArrayList();
		StringBuffer sb = new StringBuffer();
		String contents = getFullContents();
		contents = contents.substring(2, contents.length()-1);
		Stack stack = new Stack();
		int offsetStart = 0;
		boolean inString = false;
		boolean inBuiltIn = false;
		boolean inNameFragment = false;
		boolean inParameters = false;
		boolean escape = false;
		boolean doEscape = false;
		int offset = getOffset();
		for (int i=0; i<contents.length(); i++) {
			doEscape = false;
			char c = contents.charAt(i);
			if (Character.isLetterOrDigit(c) && !inString && !inBuiltIn && !inNameFragment && !inParameters)
				inNameFragment = true;
			if (inNameFragment) {
				if (c == '?') {
					fragments.add(new NameFragment(offsetStart, sb.toString()));
					offsetStart = i;
					sb.delete(0, sb.length());
					inNameFragment = false;
					inBuiltIn = true;
				}
				else if (c == '(') {
					fragments.add(new NameFragment(offsetStart, sb.toString()));
					offsetStart = i;
					sb.delete(0, sb.length());
					inNameFragment = false;
					inParameters = true;
				}
				else if (c == '.') {
					fragments.add(new NameFragment(offsetStart, sb.toString()));
					offsetStart = i;
					sb.delete(0, sb.length());
					inNameFragment = true;
					sb.append(c);
				}
				else if (c == ')') {
					// for now, forget about the stack
					fragments.add(new NameFragment(offsetStart, sb.toString()));
					offsetStart = i+1;
					sb.delete(0, sb.length());
					inNameFragment = false;
				}
				else sb.append(c);
			}
			else if (inBuiltIn) {
				if (c == '?') {
					fragments.add(new BuiltInFragment(offsetStart, sb.toString()));
					offsetStart = i;
					sb.delete(0, sb.length());
				}
				else if (c == ')') {
					fragments.add(new BuiltInFragment(offsetStart, sb.toString()));
					offsetStart = i+1;
					sb.delete(0, sb.length());
					inBuiltIn = false;
				}
				else if (c == '.') {
					fragments.add(new BuiltInFragment(offsetStart, sb.toString()));
					inNameFragment = true;
					offsetStart = i;
					sb.delete(0, sb.length());
					inBuiltIn = false;
					sb.append(c);
				}
				else sb.append(c);
			}
			else if (inParameters) {
				if (inString) if (!escape && c == '\"') inString = false;
				if (!inString && c == ')') {
					fragments.add(new ParametersFragment(offsetStart, sb.toString()));
					offsetStart = i+1;
					sb.delete(0, sb.length());
				}
				else sb.append(c);
			}
			else if (inString) {
				if (escape) sb.append(c);
				else if (c == '\"') {
					fragments.add(new StringFragment(offsetStart, sb.toString()));
					offsetStart = i+1;
					sb.delete(0, sb.length());
				}
				else if (c == '\\') {
					doEscape = true;
					sb.append(c);
				}
				else sb.append(c);
			}
			else if (c == '.') {
				if (sb.length() > 0) {
					if (inBuiltIn) fragments.add(new BuiltInFragment(offsetStart, sb.toString()));
					else fragments.add(new NameFragment(offsetStart, sb.toString()));
				}
				inNameFragment = true;
				offsetStart = i;
				sb.delete(0, sb.length());
			}
			else if (c == '?') {
				if (inBuiltIn) fragments.add(new BuiltInFragment(offsetStart, sb.toString()));
				else fragments.add(new NameFragment(offsetStart, sb.toString()));
				inBuiltIn = true;
				offsetStart = i;
				sb.delete(0, sb.length());
			}
			else if (c == '(') {
				if (fragments.size() == 0) {
					// for now, forget about the stack
				}
				else {
					if (sb.length() > 0 && !inBuiltIn)
						fragments.add(new NameFragment(offsetStart, sb.toString()));
					inParameters = true;
					offsetStart = i;
					sb.delete(0, sb.length());
				}
			}
			else if (c == '"') {
				if (sb.length() > 0) fragments.add(new NameFragment(offsetStart, sb.toString()));
				inString = true;
				offsetStart = i;
				sb.delete(0, sb.length());
			}
			else sb.append(c);
			offset++;
			escape = doEscape;
		}
		if (sb.length() > 0 || inBuiltIn) {
			if (inBuiltIn) fragments.add(new BuiltInFragment(offsetStart, sb.toString()));
			else fragments.add(new NameFragment(offsetStart, sb.toString()));
		}
	}

	private ClassLoader getClassLoader () {
		return Thread.currentThread().getContextClassLoader();
	}

	public boolean isNestable() {
		return false;
	}

	public Class getReturnClass (Map context) {
		initFragments();
		Class returnClass = null;
		for (Iterator i=fragments.iterator(); i.hasNext(); ) {
			Fragment fragment = (Fragment) i.next();
			returnClass = fragment.getReturnClass(returnClass, fragments, context, getResource(), getResource().getProject());
		}
		return returnClass;
	}

	public Class getSingularReturnClass (Map context) {
		initFragments();
		Class returnClass = null;
		for (Iterator i=fragments.iterator(); i.hasNext(); ) {
			Fragment fragment = (Fragment) i.next();
			if (i.hasNext())
				returnClass = fragment.getReturnClass(returnClass, fragments, context, getResource(), getResource().getProject());
			else
				returnClass = fragment.getSingularReturnClass(returnClass, fragments, context, getResource(), getResource().getProject());
		}
		return returnClass;
	}
}