/**
 * 
 */
package org.astrogrid.acr.ivoa;


/** data cache.
 * @service ivoa.cache 
 * @author Noel Winstanley
 * @since 2007.1
 */
public interface CacheFactory {
	
	
	/** flush all cached data - for example registry entries */
	public void flush();
	
	

}
