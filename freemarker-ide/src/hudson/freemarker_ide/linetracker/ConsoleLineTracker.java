package hudson.freemarker_ide.linetracker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.ui.console.FileLink;
import org.eclipse.debug.ui.console.IConsole;
import org.eclipse.debug.ui.console.IConsoleLineTracker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;

/**
 * @version $Id: ConsoleLineTracker.java,v 1.1 2006/04/28 17:49:25 jhudson8 Exp $
 * @author <a href="mailto:stephan&#64;chaquotay.net">Stephan Mueller </a>
 * @author <a href="mailto:joe&#64;binamics.net">Joe Hudson </a>
 */
public class ConsoleLineTracker implements IConsoleLineTracker {

	private IConsole console;
	private static final Pattern pattern1 = Pattern.compile("^.*\\[on line (\\d+), column (\\d+) in (\\S+)\\]$");
	private static final Pattern pattern2 = Pattern.compile("^.*\\[on line\\: (\\d+), column\\: (\\d+) in (\\S+)\\]$");

	private static final String CHECK = "freemarker";
	private static final String CHECK_LINE = "line:";
	private static final String CHECK_TEMPLATE = "template:";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.console.IConsoleLineTracker#init(org.eclipse.debug.ui.console.IConsole)
	 */
	public void init(IConsole console) {
		this.console = console;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.console.IConsoleLineTracker#lineAppended(org.eclipse.jface.text.IRegion)
	 */
	public void lineAppended(IRegion line) {
		try {
			String text = console.getDocument().get(line.getOffset(), line.getLength());
			if (text.indexOf(CHECK) >= 0) {
				// it might relate to us
				int i1 = text.indexOf(CHECK_TEMPLATE);
				if (i1 > 0) {
					// this is most likely an error message
					int linkOffset = i1 + 10;
					int linkLength = text.length() - linkOffset;
					String fileName = text.substring(linkOffset, text.length()).trim();
					
					int lineNumber = -1;
					try {
						int i2 = text.indexOf(CHECK_LINE);
						if (12 > 0) {
							i2 += 5;
							int i3 = text.indexOf(",", i2);
							if (i3 > 0) {
								lineNumber = Integer.parseInt(text.substring(i2, i3).trim());
							}
						}
					}
					catch (Exception e) {}
					
					IPath path = new Path(fileName);
	
					IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
					
					List files = new ArrayList();
					for (int i = 0; i < projects.length; i++) {
						IProject project = projects[i];
						IJavaProject jproject = JavaCore.create(project);
						
						IFile[] foundFiles = retrieveFiles(project, fileName);
	
						for (int j = 0; j < foundFiles.length; j++) {
							IFile file = foundFiles[j];
							
							boolean isInsideOutputLocation = false;
							
							try {
							    // check whether the file is located inside a Java project's output location
	                            isInsideOutputLocation = jproject.getOutputLocation().isPrefixOf(file.getFullPath());
	                        } catch (JavaModelException e1) {
	                            // noop; isInsideOutputLocation = false
	                        }
	                        
							if(!isInsideOutputLocation) {
							    // FTL files inside a project's output location are probably copies
							    files.add(file);
							}
						}
					}
	
					if (files.size() != 0) {
						List filteredFiles = new ArrayList();
	
						nextfile : for (int j = 0; j < files.size(); j++) {
							IFile aFile = (IFile) files.get(j);
							IContainer parent = aFile.getParent();
							while (parent != null) {
								parent = parent.getParent();
							}
							filteredFiles.add(aFile);
						}
	
						if (filteredFiles.size() != 0) {
							IFile file = (IFile) filteredFiles.get(0);
							if (file != null && file.exists()) {
								FileLink link = new FileLink(file, null, -1, -1, lineNumber);
								console.addLink(link, linkOffset, linkLength);
							}
						}
					}
				}
			}
		} catch (BadLocationException e) {
		}
	}

	public IFile[] retrieveFiles(IContainer container, String filename) {
		String[] seqs = filename.split("/|\\\\");

		List l = new LinkedList();
		l.add(container);
		List files = new ArrayList();
		while (!l.isEmpty()) {
			IContainer c = (IContainer) l.get(0);
			l.remove(0);
			try {
				IResource[] res = c.members();
				for (int i = 0; i < res.length; i++) {
					IResource resource = res[i];
					if (resource instanceof IContainer) {
						l.add(resource);
					} else if (resource instanceof IFile) {
						if (isCorrectFile((IFile) resource, seqs)) {
							IPath path = ((IFile) resource).getProjectRelativePath();
							files.add(resource);
						}
					}
				}
			} catch (CoreException ce) {
			}
		}
		IFile[] f = new IFile[files.size()];
		files.toArray(f);
		return f;
	}

	private boolean isCorrectFile(IFile file, String[] filenameSeqs) {
		IResource temp = file;
		for (int i = filenameSeqs.length - 1; i >= 0; i--) {
			String seq = filenameSeqs[i];
			if (!seq.equals(temp.getName())) {
				return false;
			}
			temp = temp.getParent();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.console.IConsoleLineTracker#dispose()
	 */
	public void dispose() {
	    // do nothing here
	}

}