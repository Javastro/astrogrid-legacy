/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.ivoa.Ssap;

/** A Spectral Access Service (SSAP).
 * @see Ssap
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface SsapService extends Service{
    /** Access the capability that describes this ssap service
     * 
    * @return one of the items within {@link #getCapabilities()}
     */
    public SsapCapability findSsapCapability();

}
