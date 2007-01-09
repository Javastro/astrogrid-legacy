/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import net.sf.ehcache.CacheManager;

/** interface to a data cache.
 * @author Noel Winstanley
 * @since Aug 8, 20061:16:54 AM
 */
public interface CacheFactory {
	
	public CacheManager getManager() ;
	
	public void flush();
	
	public final static String RESOURCES_CACHE = "resources";
	public final static String PW_CACHE = "pw";
	public final static String DOCUMENTS_CACHE = "documents";
	public final static String BULK_CACHE = "bulk";	
	public final static String APPLICATION_RESOURCES_CACHE="applicationResources";
	public final static String VOMON_CACHE = "vomon";

}
