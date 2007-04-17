/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import java.net.URL;
import java.util.Map;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.filemanager.resolver.FileManagerEndpointResolver;
import org.astrogrid.filemanager.resolver.FileManagerEndpointResolverImpl;
import org.astrogrid.filemanager.resolver.FileManagerResolverException;
import org.astrogrid.store.Ivorn;

/** Wraps the expensive original resolver with memoization functionality - 
 * so it remembers previous answers, and only consults the services once.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 4, 200711:18:11 AM
 */
public class MemoizingEndpointResolver extends FileManagerEndpointResolverImpl
		implements FileManagerEndpointResolver {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(MemoizingEndpointResolver.class);

	public URL resolve(Ivorn arg0) throws FileManagerResolverException {
		String key = arg0.toString(); // equals isn't implemented on Ivorn - another black mark.
		Object o = results.get(key);
		if (o != null) {
			logger.debug("cache hit! " + arg0);
			if (o instanceof FileManagerResolverException) {
				throw (FileManagerResolverException)o;
			} else if (o instanceof RuntimeException) {
				throw (RuntimeException)o;
			} else if (o instanceof Error) {
				throw (Error)o;				
			} else {
				return (URL)o;
			}
		}
		logger.debug("cahce miss " + arg0);
		// not got a memoized result - need to compute it.
		try {
			URL result = super.resolve(arg0);
			if (result != null) { // cachemap doesn't permit null values
				results.put(key,result);
			}			
			return result;
		} catch (FileManagerResolverException e) {
			results.put(key,e);
			throw e;
		}  catch (RuntimeException e) {
			results.put(key,e);
			throw e;
		} catch (Error e) {
			results.put(key,e);
			throw e;
		}
	}
	
	
	// a memory-sensitive cache.
	private final Map results = new ReferenceMap(ReferenceMap.HARD,ReferenceMap.SOFT);

}
