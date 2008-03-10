/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** Registry description of a time-access service
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 24, 20083:29:18 PM
 */
public interface StapService extends Service{
    /** access the capability that describes this stap service
     * 
     * @return one of the items within <tt>Service.getCapabilities()</tt>
     */
    public StapCapability findStapCapability();
}
