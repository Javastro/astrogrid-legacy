/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.net.URL;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.filemanager.resolver.FileManagerEndpointResolver;
import org.astrogrid.filemanager.resolver.FileManagerEndpointResolverImpl;
import org.astrogrid.filemanager.resolver.FileManagerResolverException;
import org.astrogrid.store.Ivorn;

/** 
 * Memoizes expensive calls to a FileManagerEndpointResolver.
 * <p/>
 * Wraps the expensive original resolver with memoization functionality - 
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

	@Override
    public URL resolve(final Ivorn arg0) throws FileManagerResolverException {
		final String key = arg0.toString(); // equals isn't implemented on Ivorn - another black mark.
		final Object o = results.get(key);
		if (o != null) {
		    if (logger.isDebugEnabled()) {
		        logger.debug("cache hit! " + arg0 + " -> " + o);
		    }
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
        if (logger.isDebugEnabled()) {
            logger.debug("cache miss " + arg0);
            MapUtils.debugPrint(System.out,"memoized endpoints",results);
        }
		// not got a memoized result - need to compute it.
		try {
			final URL result = super.resolve(arg0);
			if (result != null) { // cachemap doesn't permit null values
				results.put(key,result);
			}			
			return result;
		} catch (final FileManagerResolverException e) {
			results.put(key,e);
			throw e;
		}  catch (final RuntimeException e) {
			results.put(key,e);
			throw e;
		} catch (final Error e) {
			results.put(key,e);
			throw e;
		}
	}
	
	
	// a memory-sensitive cache.
	private final Map<String, Object> results = new ReferenceMap(ReferenceMap.HARD,ReferenceMap.SOFT);

}
