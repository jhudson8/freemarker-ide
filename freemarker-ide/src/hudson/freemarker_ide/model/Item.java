package hudson.freemarker_ide.model;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;

public interface Item {

	public void load (ITypedRegion region, ISourceViewer viewer, IResource resource);

	public boolean isNestable ();

	public boolean isStartItem ();

	public boolean isEndItem ();
	
	public boolean isStartAndEndItem ();
	
	public Item getStartItem ();

	public Item getEndItem ();

	public boolean relatesToItem (Item directive);

	public void relateItem (Item directive);

	public ITypedRegion getRegion();

	public List getChildItems();

	public Item getParentItem();
	
	public void setParentItem(Item item);

	public void addSubDirective(Item directive);

	public Item[] getRelatedItems ();
	
	public String getContents();

	public String getTreeImage();
	
	public String getTreeDisplay();

	public ICompletionProposal[] getCompletionProposals(int offset, Map context);

	public void setItemSet (ItemSet itemSet);

	public String getFirstToken ();

	public void addToContext (Map context);

	public void removeFromContext (Map context);

	public String getName();
}
