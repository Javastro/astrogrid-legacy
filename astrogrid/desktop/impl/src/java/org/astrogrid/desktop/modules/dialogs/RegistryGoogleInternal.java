/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Component;
import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.Resource;

import ca.odell.glazedlists.matchers.Matcher;

/** Internal interface to the RegistryGoogle component. 
 * <p/>
 * Extensions allow a parent component to be provided to the registry google dialogue.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 21, 200812:05:38 PM
 */
public interface RegistryGoogleInternal extends RegistryGoogle {
    
    
    Resource[] selectResourceXQueryFilterWithParent(String prompt, boolean multiple, String xzuqery, Component comp);
    
    Resource[] selectResourcesFromListWithParent(String prompt, boolean multiple, URI[] identifiers, Component comp);
    
    /** srql query, with parent 
     * @throws InvalidArgumentException */
    Resource[] selectResourcesWithParent(String prompt, boolean multiple, String srql, Component comp) throws InvalidArgumentException;
    
    /**display left-hand resource tree too. 
     * @param prompt prompt to display at top of window
     * @param multiple whether multiple selection is allowed
     * @param accept a matcher that should match only acceptable resources to select
     * @param comp parent component.
     * @return */
    Resource[] selectResourcesFullUI(String prompt, boolean multiple, Matcher<Resource> accept, Component comp);


    
}
