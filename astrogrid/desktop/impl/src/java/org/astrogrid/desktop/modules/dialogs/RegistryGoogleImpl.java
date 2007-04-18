/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since Aug 4, 20061:28:18 AM
 */
public class RegistryGoogleImpl implements RegistryGoogle {
	
	public RegistryGoogleImpl( UIContext context,RegistryGooglePanel regPanel) {
        super();
        dialog = new RegistryGoogleDialog(context,regPanel);
        dialog.pack();
        // set size..
    }
    private final RegistryGoogleDialog dialog;

	
	public Resource[] selectResources(String arg0, boolean arg1) {
		return selectResourcesXQueryFilter(arg0,arg1,null);
	}

	//@todo unsure whether I'm going to be able to implement both adql and xquery variants.
	public Resource[] selectResourcesAdqlFilter(String arg0, boolean arg1,
			String arg2) {
		return selectResourcesXQueryFilter(arg0,arg1,arg2);
	}

	public Resource[] selectResourcesXQueryFilter(String arg0, boolean arg1,
			String arg2) {
        dialog.setPrompt(arg0);
        dialog.setMultipleResources(arg1);
        dialog.setFilter(arg2);
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();        
        return dialog.getSelectedResources();
	}

}
