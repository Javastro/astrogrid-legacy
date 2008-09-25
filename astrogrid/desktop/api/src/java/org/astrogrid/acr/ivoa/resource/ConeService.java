/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.ivoa.Cone;

/** A service that can perform a cone search
 * @see Cone
 * @author Noel Winstanley
 */
public interface ConeService extends Service {
	/** Access the capability that describes how to perform a cone search.
	 * 
	 * @return one of the items within {@link #getCapabilities()}
	 */
	public ConeCapability findConeCapability();
}
