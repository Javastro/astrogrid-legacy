/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.astrogrid.Stap;

/** A Time Access Service (STAP).
 * @see Stap
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface StapService extends Service{
    /** access the capability that describes this stap service
     * 
    * @return one of the items within {@link #getCapabilities()}
     */
    public StapCapability findStapCapability();
}
