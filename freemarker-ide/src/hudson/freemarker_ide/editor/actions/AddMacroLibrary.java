package hudson.freemarker_ide.editor.actions;

import hudson.freemarker_ide.Plugin;
import hudson.freemarker_ide.configuration.ConfigurationManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.preferences.ProjectSelectionDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class AddMacroLibrary implements IObjectActionDelegate {

    private IWorkbenchPart part;

    public void run(IAction action) {
        ISelectionProvider provider = part.getSite().getSelectionProvider();
        if (null != provider) {
            if (provider.getSelection() instanceof IStructuredSelection) {
                try {
                    IStructuredSelection selection = (IStructuredSelection) provider.getSelection();
                    Object[] obj = selection.toArray();
                    List documents = new ArrayList();
                    for (int i=0; i<obj.length; i++) {
                        if (obj[i] instanceof IFile) {
                            IFile file = (IFile) obj[i];
                            documents.add(file);
                        }
                        else if (obj[i] instanceof JarEntryFile) {
                        	JarEntryFile jef = (JarEntryFile) obj[i];
                        	documents.add(jef);
                        	System.out.println(jef.getFullPath().makeAbsolute());
                        	System.out.println(jef.getFullPath().makeRelative());
                        	IPath path = jef.getFullPath();
                        	System.out.println(path);
                        	System.out.println(jef.getName());
                        	IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(jef.getFullPath());
                        	System.out.println(resource);
                        }
                        else if (obj[i] instanceof IStorage) {
                        	
                        }
                    }
                    IProject project = null;
                    if (documents.size() > 0) {
                    	// what project?
                    	HashSet projects = new HashSet();
                    	IProject[] p = ResourcesPlugin.getWorkspace().getRoot().getProjects();
                    	for (int i=0; i<p.length; i++) {
                    		projects.add(p[i]);
                    	}
                    	ProjectSelectionDialog dialog = new ProjectSelectionDialog(Display.getCurrent().getActiveShell(), projects);
                    	dialog.setTitle("Please choose the associated Java project");
                    	dialog.setMessage("Please choose the Java project that will use the FreeMarker libraries");
                    	int rtn = dialog.open();
                    	if (rtn == IDialogConstants.OK_ID) {
                    		if (dialog.getFirstResult() instanceof JavaProject) {
                    			project = ((JavaProject) dialog.getFirstResult()).getProject();
                    		}
                    		else {
                    			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Java Project Required", "You must choose a Java project");
                    		}
                    	}
                    }
                    if (null != project) {
                    	ConfigurationManager.getInstance(project).associateMappingLibraries(
                    			documents, Display.getCurrent().getActiveShell());
                    }
                }
                catch (Exception e) {
                    Plugin.error(e);
                }
            }
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.part = targetPart;
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
   
    protected boolean shouldForce () {
        return false;
    }
}
