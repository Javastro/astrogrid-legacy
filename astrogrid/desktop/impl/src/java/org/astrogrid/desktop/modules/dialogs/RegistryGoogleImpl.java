/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Component;
import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.folders.ResourceTreeModel;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.HeadClauseSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLParser;

import ca.odell.glazedlists.matchers.Matcher;

/** Implementation of the RegistryGoogle component.
 * @author Noel Winstanley
 * @since Aug 4, 20061:28:18 AM
 */ 
public class RegistryGoogleImpl implements RegistryGoogleInternal {
	
	public RegistryGoogleImpl( final UIContext context, final TypesafeObjectBuilder builder, final ResourceTreeModel model) {
        super();
        dialog = new RegistryGoogleDialog(context,builder, model);
    }
    private final RegistryGoogleDialog dialog;

	/** not implemented */
	public Resource[] selectResources(final String arg0, final boolean arg1, final String ssrql)
	    throws InvalidArgumentException {
	    final SRQL srql = new SRQLParser(ssrql).parse();
	    final String xq = new HeadClauseSRQLVisitor().build(srql,null);
	    return selectResourcesXQueryFilter(arg0,arg1,xq);
	}

	/** not implemented */
	public Resource[] selectResourcesAdqlFilter(final String arg0, final boolean arg1,
			final String arg2) {
		return new Resource[]{};
	}

	public Resource[] selectResourcesXQueryFilter(final String arg0, final boolean arg1,
			final String arg2) {
        dialog.setPrompt(arg0);
        dialog.setMultipleResources(arg1);
        dialog.setShowPlaylists(false);
        dialog.populateFromXQuery(arg2);
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();        
        return dialog.getSelectedResources();
	}

    public Resource[] selectResourcesFromList(final String arg0, final boolean arg1,
            final URI[] arg2) {
        dialog.setPrompt(arg0);
        dialog.setMultipleResources(arg1);
        dialog.setShowPlaylists(false);
        dialog.populateWithIds(arg2);       
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();        
        return dialog.getSelectedResources();
    }

    // internal interface.
    public Resource[] selectResourcesFromListWithParent(final String prompt,
            final boolean multiple, final URI[] identifiers, final Component comp) {
        dialog.setLocationRelativeTo(comp);
        return selectResourcesFromList(prompt,multiple,identifiers);
    }

    public Resource[] selectResourceXQueryFilterWithParent(final String prompt,
            final boolean multiple, final String query, final Component comp) {
        dialog.setLocationRelativeTo(comp);
        return selectResourcesXQueryFilter(prompt,multiple,query);
    }

    public Resource[] selectResourcesFullUI(final String prompt, final boolean multiple,
            final Matcher<Resource> accept, final Component comp) {
        dialog.setPrompt(prompt);
        dialog.setMultipleResources(multiple);
        dialog.setShowPlaylists(true);
        dialog.installMatcher(accept);
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();        
        return dialog.getSelectedResources();
    }

    public Resource[] selectResourcesWithParent(final String prompt,
            final boolean multiple, final String srql, final Component comp) throws InvalidArgumentException {
        dialog.setLocationRelativeTo(comp);
        return selectResources(prompt,multiple,srql);
    }


}
