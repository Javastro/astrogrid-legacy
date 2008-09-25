/**
 * 
 */
package org.astrogrid.acr.ivoa;


/**
 * @exclude 
 * not very interesting
 *  data cache - registry resources, and other stuff.
 * @service ivoa.cache 
 * @author Noel Winstanley
 */
public interface CacheFactory {
	
	
	/** flush all cached data - for example registry entries */
	public void flush();
	
	

}
