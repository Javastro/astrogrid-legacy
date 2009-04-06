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
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunityPolicyException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

/**
 * Memoizes expensive calls to the community resolver.
 * <p/>
 * Wraps the expensive original resolver with memoization functionality - 
 * so it remembers previous answers, and only consults the services once.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 5, 200712:33:28 AM
 */
public class MemoizingCommunityAccountSpaceResolver extends
		CommunityAccountSpaceResolver {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(MemoizingCommunityAccountSpaceResolver.class);
	
	public MemoizingCommunityAccountSpaceResolver() {
		super();
	}

	public MemoizingCommunityAccountSpaceResolver(final URL arg0) {
		super(arg0);
	}

	@Override
    public Ivorn resolve(final Ivorn arg0) throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException, CommunityResolverException, RegistryException {
		final String key = arg0.toString(); // equals isn't implemented on Ivorn - another black mark.
		final Object o = results.get(key);
		if (o != null) {
		    if (logger.isDebugEnabled()) {
		        logger.debug("cache hit! " + arg0 + " -> " + o);
		    }
			// good greif - not one base type. how sad.
			if (o instanceof CommunityServiceException) {
				throw (CommunityServiceException)o;
			} else if (o instanceof CommunityIdentifierException) {
				throw (CommunityIdentifierException)o;
			} else if (o instanceof CommunityPolicyException) {
				throw (CommunityPolicyException)o;
			} else if (o instanceof CommunityResolverException) {
				throw (CommunityResolverException)o;
			} else if (o instanceof RegistryException) {
				throw (RegistryException)o;				
			} else if (o instanceof RuntimeException) {
				throw (RuntimeException)o;
			} else if (o instanceof Error) {
				throw (Error)o;
			} else {
				return (Ivorn)o;
			}
		}
		if (logger.isDebugEnabled()) {
		    logger.debug("cache miss " + arg0);
		    MapUtils.debugPrint(System.out,"memoized communities",results);
		}
		// not got a memoized result - need to compute it.
		try {
			final Ivorn result = super.resolve(arg0);
			if (result != null) { // cachemap doesn't permit null values
				results.put(key,result);
			}
			return result;
		}  catch (final CommunityServiceException e) {
			results.put(key,e);
			throw e;
		} catch (final CommunityIdentifierException e) {
			results.put(key,e);
			throw e;
		}  catch (final CommunityPolicyException e) {
			results.put(key,e);
			throw e;
		}  catch (final CommunityResolverException e) {
			results.put(key,e);
			throw e;
		} catch (final RegistryException e) {
			results.put(key,e);
			throw e;
		} catch (final RuntimeException e) {
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
