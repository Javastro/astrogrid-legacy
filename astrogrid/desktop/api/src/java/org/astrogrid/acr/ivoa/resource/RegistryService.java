/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**
 * @author Noel Winstanley
 * @since Jul 31, 20065:55:54 PM
 */
public interface RegistryService extends Service{
	public boolean isFull();
	public String[] getManagedAuthorities();
	
	public HarvestCapability findHarvestCapability();
	public SearchCapability findSearchCapability();
}
