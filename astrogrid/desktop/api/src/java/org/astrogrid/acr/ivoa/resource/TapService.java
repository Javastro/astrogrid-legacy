/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** A service that can perform a TAP query
 * @author Noel.Winstanley@manchester.ac.uk
 * @since 1.2.1
 */
public interface TapService extends Service {
    
    /** access the capability that describes how to performa a TAP query 
     *
     *@return one of the items within {@link #getCapabilities()}
     */
    public TapCapability findTapCapability();

}
