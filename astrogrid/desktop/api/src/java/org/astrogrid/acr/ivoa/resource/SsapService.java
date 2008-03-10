/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** Registry description of a spectral-access service
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 24, 20082:39:53 PM
 */
public interface SsapService extends Service{
    /** access the capability that describes this ssap service
     * 
     * @return one of the items within <tt>Service.getCapabilities()</tt>
     */
    public SsapCapability findSsapCapability();

}
