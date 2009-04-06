/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** A service that provides VOSpace access.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 6, 20091:34:17 PM
 * @see <a href='http://www.ivoa.net/Documents/latest/VOSpace.html'>VOSpace Specification</a>
 */
public interface VospaceService extends Service {

    /** access the capability that describes how to access VOSpace
    *
    *@return one of the items within {@link #getCapabilities()}
    */ 
   public VospaceCapability findVospaceCapability();

}
