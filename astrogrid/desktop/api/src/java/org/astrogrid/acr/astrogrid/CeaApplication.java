/**
 * 
 */
package org.astrogrid.acr.astrogrid;


import org.astrogrid.acr.ivoa.resource.Resource;


/** registry entry for a cea application
 * @todo implement new details.
 * @author Noel Winstanley
 * @since Jul 31, 20066:03:15 PM
 */
public interface CeaApplication extends Resource {
    /** The names of the interfaces provided by this application.
     * @return an array of interface names
     * @xmlrpc key will be <tt>interfaces</tt>, type will be array.
     */	
    public InterfaceBean[] getInterfaces();
    /** The Parameters used in this application.
   */    
    public ParameterBean[] getParameters();
}
