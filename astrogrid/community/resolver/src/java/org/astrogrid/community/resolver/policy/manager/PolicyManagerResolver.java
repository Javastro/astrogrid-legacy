/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/policy/manager/PolicyManagerResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerResolver.java,v $
 *   Revision 1.3  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.2.2.2  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 *   Revision 1.2.2.2  2004/03/13 16:08:08  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
 *
 *   Revision 1.2.2.1  2004/03/12 17:42:09  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.2  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/12 13:44:43  dave
 *   Moved MockIvornFactory to MockIvorn
 *
 *   Revision 1.1.2.2  2004/03/12 00:46:25  dave
 *   Added CommunityIvornFactory and CommunityIvornParser.
 *   Added MockIvorn (to be moved to common project).
 *
 *   Revision 1.1.2.1  2004/03/10 17:29:21  dave
 *   Added initial resolver classes.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.policy.manager ;

import java.net.URL ;
import java.net.URISyntaxException ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.exception.CommunityException ;
import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;

import org.astrogrid.community.resolver.CommunityEndpointResolver ;

import org.astrogrid.registry.RegistryException;

/**
 * A toolkit to resolve Ivorn identifiers into PolicyManagerResolver delegates.
 *
 */
public class PolicyManagerResolver
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, using default registry delegate.
     *
     */
    public PolicyManagerResolver()
        {
		//
		// Initialise a default resolver.
		resolver = new CommunityEndpointResolver() ;
        }

    /**
     * Public constructor, with a specific registry delegate.
     * @param registry The endpoint address for our registry delegate.
     *
     */
    public PolicyManagerResolver(URL registry)
        {
		//
		// Initialise a resolver with the url.
		resolver = new CommunityEndpointResolver(registry) ;
        }

	/**
	 * Our endpoint resolver.
	 *
	 */
	private CommunityEndpointResolver resolver ;

    /**
     * Resolve an Ivorn identifier into a PolicyManagerDelegate.
     * If the Ivorn is null, this will return null.
     * If the Ivorn matches the MOCK_IVORN, this will return a PolicyManagerMockDelegate.
     * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
     * This may involve a WebService call to a remote Resistry service.
     * @param ident The IvoIdentifier.
     * @return A reference to a PolicyManagerDelegate, or null if the service cannot be located.
     * @see Ivorn
     *
     */
    public PolicyManagerDelegate resolve(Ivorn ivorn)
        throws CommunityException, RegistryException, MalformedURLException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerResolverImpl.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != ivorn) ? ivorn : null)) ;
        //
        // Check for null ivorn.
        if (null == ivorn) { throw new IllegalArgumentException("Null ivorn") ; }
        //
        // Parse the ivorn and resolve it.
        return this.resolve(
            new CommunityIvornParser(ivorn)
            ) ;
        }

    /**
     * Resolve data from a CommunityIvornParser into a PolicyManagerDelegate.
     * If the Ivorn is null, this will return null.
     * If the Ivorn matches the MOCK_IVORN, this will return a PolicyManagerMockDelegate.
     * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
     * This may involve a WebService call to a remote Resistry service.
     * @param ident The IvoIdentifier.
     * @return A reference to a PolicyManagerDelegate, or null if the service cannot be located.
     * @see Ivorn
     *
     */
    public PolicyManagerDelegate resolve(CommunityIvornParser parser)
        throws CommunityException, RegistryException, MalformedURLException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerResolverImpl.resolve()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
        //
        // Check for null parser.
        if (null == parser) { throw new IllegalArgumentException("Null parser") ; }
        //
        // Check for a mock ivorn.
        if (parser.isMock())
            {
	        if (DEBUG_FLAG) System.out.println("Ivorn is mock.") ;
	        if (DEBUG_FLAG) System.out.println("Creating mock delegate.") ;
            //
            // Return a mock delegate.
            return new PolicyManagerMockDelegate(
            	parser.getIvorn()
            	) ;
            }
        //
        // If the ident is real.
        else {
	        if (DEBUG_FLAG) System.out.println("Ivorn is not mock.") ;
            //
            // Lookup the endpoint in the registry.
			URL endpoint = resolver.resolve(parser, "PolicyManager") ;
			//
			// If we got a valid URL.
			if (null != endpoint)
				{
				if (DEBUG_FLAG) System.out.println("PASS : Got url") ;
				//
				// Return a new delegate
				return this.resolve(endpoint) ;
				}
			//
			// If we didn't get a valid URL.
			else {
				if (DEBUG_FLAG) System.out.println("FAIL : Null url") ;
				return null ;
				}
			}
		}

    /**
     * Resolve a WebService endpoint into a PolicyManagerDelegate.
     * @param url A vaild endpoint URL.
     * @return A reference to a PolicyManagerDelegate
     *
     */
    protected PolicyManagerDelegate resolve(URL url)
        throws CommunityException
        {
        //
        // Check for null ident.
        if (null == url) { throw new IllegalArgumentException("Null url") ; }
        return new PolicyManagerSoapDelegate(url) ;
        }
    }
