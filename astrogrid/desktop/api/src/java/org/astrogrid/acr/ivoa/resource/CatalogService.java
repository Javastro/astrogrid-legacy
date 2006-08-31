/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**
 * Resource object for a vs:CatalogService.
 * @todo find schema documentation
 * @author Noel Winstanley
 * @since Aug 5, 20069:37:58 PM
 */
public interface CatalogService extends Service {

	/** access the coverage information for this service */
	public Coverage getCoverage();
}
