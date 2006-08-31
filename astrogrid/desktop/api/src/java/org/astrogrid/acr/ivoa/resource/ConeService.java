/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** Registry description of a cone-search service
 * @author Noel Winstanley
 * @since Aug 5, 200610:17:29 PM
 */
public interface ConeService extends Service {
	/** access the capability that describes this cone search service
	 * 
	 * @return one of the items within <tt>Service.getCapabilities()</tt>
	 */
	public ConeCapability findConeCapability();
}
