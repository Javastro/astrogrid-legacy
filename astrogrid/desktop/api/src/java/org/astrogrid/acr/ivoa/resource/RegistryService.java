/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.Registry;

/** An IVOA Registry Service
 * @see Registry
 * @see ExternalRegistry 
 * @author Noel Winstanley
 */
public interface RegistryService extends Service{
	/** if true, this is a full registry. False probably implies a publishing registry */
	public boolean isFull();
	/** lists the authority IDs that this registry manages. */
	public String[] getManagedAuthorities();
	/** The capability that describes the harvesting feature of this registry 
	 * 
	 * @return one of the items within {@link #getCapabilities()}, if this registry is capable of being harvested from.
	 */
	public HarvestCapability findHarvestCapability();
	/** The capability that describes the search feature of this registry 
	* @return one of the items within {@link #getCapabilities()} if this registry is capable of being searched.
	* */
	public SearchCapability findSearchCapability();
}
