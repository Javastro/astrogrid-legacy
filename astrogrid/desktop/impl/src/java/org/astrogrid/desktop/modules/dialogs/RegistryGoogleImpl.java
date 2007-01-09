/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ivoa.CacheFactory;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.votech.VoMon;

/**
 * @author Noel Winstanley
 * @since Aug 4, 20061:28:18 AM
 */
public class RegistryGoogleImpl implements RegistryGoogle {
	
	public RegistryGoogleImpl( Configuration conf, HelpServerInternal help, UIInternal ui,RegistryInternal reg,BrowserControl browser, RegistryBrowser regBrowser, CacheFactory cache,VoMon vomon) {
        super();
        dialog = new RegistryGoogleDialog(conf,help,ui,reg,browser, regBrowser,cache,vomon);
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
