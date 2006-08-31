/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** Resource describing a Registry Service 
 * @author Noel Winstanley
 * @since Jul 31, 20065:55:54 PM
 */
public interface RegistryService extends Service{
	/** if true, this is a full registry. False probably implies a publishing registry */
	public boolean isFull();
	/** lists the authority IDs this registry manages. */
	public String[] getManagedAuthorities();
	/** returns the capability that describes the harvesting feature of this registry */
	public HarvestCapability findHarvestCapability();
	/** returns the capability that describes the search feature of this registry */
	public SearchCapability findSearchCapability();
}
