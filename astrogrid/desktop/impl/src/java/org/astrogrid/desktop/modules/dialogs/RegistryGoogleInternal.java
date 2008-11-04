/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Component;
import java.net.URI;

import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.Resource;

/** Internal interface to the RegistryGoogle component. 
 * <p/>
 * Extensions allow a parent component to be provided to the registry google dialogue.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 21, 200812:05:38 PM
 */
public interface RegistryGoogleInternal extends RegistryGoogle {
    
    
    Resource[] selectResourceXQueryFilterWithParent(String prompt, boolean multiple, String xzuqery, Component comp);
    
    Resource[] selectResourcesFromListWithParent(String prompt, boolean multiple, URI[] identifiers, Component comp);
    

}
