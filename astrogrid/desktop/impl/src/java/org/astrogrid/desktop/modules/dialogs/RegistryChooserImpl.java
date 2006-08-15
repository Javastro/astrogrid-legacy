/*$Id: RegistryChooserImpl.java,v 1.6 2006/08/15 10:19:53 nw Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

/** Impleentaitn of the obsolete registry chooser component.
 * It's an inefficent stop-gap in terms of RegistryGoogle, and then selected
 * resources are looked up in obsolete registry interface.
 * @deprecated
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 *
 */
public class RegistryChooserImpl implements RegistryChooser {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(RegistryChooserImpl.class);

    /** Construct a new RegistryChooserImpl
     * 
     */
    public RegistryChooserImpl( RegistryGoogle g,Registry reg) {
        super();
        this.dialog = g;
        this.reg = reg;
    }
    private final RegistryGoogle dialog;
    private final Registry reg;
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
    	Resource[] arr = dialog.selectResourcesAdqlFilter(arg0,false,arg1);
        return arr == null || arr.length < 1 ? null : cvt(arr[0]); 
    }
    
    private ResourceInformation cvt(Resource r) {
    	try {
			return reg.getResourceInformation(r.getId());
		} catch (Exception x) {
			logger.error("Didn't expect to get null here - the resouce was here a moment ago..",x);
			return null;
			
		} 
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
    	Resource[] arr = dialog.selectResourcesAdqlFilter(arg0,false,arg1);
    	if (arr == null) {
    		return new ResourceInformation[0];
    	}
    	ResourceInformation[] result = new ResourceInformation[arr.length];
    	for (int i = 0; i < arr.length; i++) {
    		result[i] = cvt(arr[i]);
    	}
    	return result;
    }

}


/* 
$Log: RegistryChooserImpl.java,v $
Revision 1.6  2006/08/15 10:19:53  nw
implemented new registry google dialog.

Revision 1.5  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.4.30.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.14.1  2005/11/23 04:47:36  nw
attempted to improve dialogue behaviour

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.6.1  2005/10/12 09:21:38  nw
added java help system

Revision 1.2  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/