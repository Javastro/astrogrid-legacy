/*$Id: RegistryChooserImpl.java,v 1.2 2005/09/12 15:21:16 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.desktop.modules.system.UIInternal;

/** Impleentaito of the registry chooser component.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 *
 */
public class RegistryChooserImpl implements RegistryChooser {

    /** Construct a new RegistryChooserImpl
     * 
     */
    public RegistryChooserImpl( Configuration conf, HelpServer help, UIInternal ui,Registry reg) {
        super();
        dialog = new RegistryChooserDialog(conf,help,ui,reg);
        dialog.pack();
        // set size..
    }
    private final RegistryChooserDialog dialog;

    /**
     * @see org.astrogrid.acr.dialogs.RegistryChooser#chooseResource(java.lang.String)
     */
    public ResourceInformation chooseResource(String arg0) {
        return chooseResourceWithFilter(arg0,null);
        
    }

    /**
     * @see org.astrogrid.acr.dialogs.RegistryChooser#chooseResourceWithFilter(java.lang.String, java.lang.String)
     */
    public ResourceInformation chooseResourceWithFilter(String arg0, String arg1) {
        dialog.setPrompt(arg0);
        dialog.setFilter(arg1);
        dialog.setMultipleResources(false);
        dialog.setVisible(true);
        ResourceInformation[] arr = dialog.getSelectedResources();
        return arr == null || arr.length < 1 ? null : arr[0]; 
    }

    /**
     * @see org.astrogrid.acr.dialogs.RegistryChooser#chooseResources(java.lang.String)
     */
    public ResourceInformation[] chooseResources(String arg0) {
        return chooseResourcesWithFilter(arg0,null);
    }

    /**
     * @see org.astrogrid.acr.dialogs.RegistryChooser#chooseResourcesWithFilter(java.lang.String, java.lang.String)
     */
    public ResourceInformation[] chooseResourcesWithFilter(String arg0, String arg1) {
        dialog.setPrompt(arg0);
        dialog.setFilter(arg1);
        dialog.setMultipleResources(true);
        dialog.setVisible(true);
        return dialog.getSelectedResources();
    }

}


/* 
$Log: RegistryChooserImpl.java,v $
Revision 1.2  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/