/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Component;
import java.net.URI;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;

/**
 * @author Noel Winstanley
 * @since Aug 4, 20061:28:18 AM
 */ 
public class RegistryGoogleImpl implements RegistryGoogleInternal {
	
	public RegistryGoogleImpl( final UIContext context, final TypesafeObjectBuilder builder) {
        super();
        dialog = new RegistryGoogleDialog(context,builder);
    }
    private final RegistryGoogleDialog dialog;

	/** not implemented */
	public Resource[] selectResources(final String arg0, final boolean arg1) {
	       return new Resource[]{};
//        dialog.setPrompt(arg0);
//        dialog.setMultipleResources(arg1);
//        dialog.setShowPlaylists(true);        
//        dialog.setVisible(true);
//        dialog.toFront();
//        dialog.requestFocus();        
//        return dialog.getSelectedResources();
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


}
