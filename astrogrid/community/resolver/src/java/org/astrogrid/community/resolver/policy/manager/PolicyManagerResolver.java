/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/policy/manager/PolicyManagerResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerResolver.java,v $
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.2.2  2004/03/23 14:51:26  dave
 *   Added SecurityManagerResolver and SecurityServiceResolver.
 *
 *   Revision 1.4.2.1  2004/03/23 11:11:53  dave
 *   No comment ....
 *
 *   Revision 1.4  2004/03/19 14:43:15  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.2.5  2004/03/19 04:51:46  dave
 *   Updated resolver with new Exceptions
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.policy.manager ;

import java.net.URL ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;

import org.astrogrid.community.resolver.CommunityEndpointResolver ;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

import org.astrogrid.registry.RegistryException;

/**
 * A toolkit to resolve an Ivorn identifier into a PolicyManagerResolver delegate.
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
     * Public constructor, using the default Registry service.
     *
     */
    public PolicyManagerResolver()
        {
        //
        // Initialise a default resolver.
        resolver = new CommunityEndpointResolver() ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
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
     * If the Ivorn matches the MOCK_IVORN, this will return a PolicyManagerMockDelegate.
     * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
     * @param ident The service identifier.
     * @return A new PolicyManagerDelegate.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public PolicyManagerDelegate resolve(Ivorn ivorn)
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerResolverImpl.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != ivorn) ? ivorn : null)) ;
        //
        // Check for null ivorn.
        if (null == ivorn)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Parse the ivorn and resolve it.
        return this.resolve(
            new CommunityIvornParser(ivorn)
            ) ;
        }

    /**
     * Resolve data from a CommunityIvornParser into a PolicyManagerDelegate.
     * If the Ivorn matches the MOCK_IVORN, this will return a PolicyManagerMockDelegate.
     * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
     * @param parser The service identifier.
     * @return A new PolicyManagerDelegate.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public PolicyManagerDelegate resolve(CommunityIvornParser parser)
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerResolverImpl.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
        //
        // Check for null parser.
        if (null == parser)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check for a mock ivorn.
        if (parser.isMock())
            {
            if (DEBUG_FLAG) System.out.println("Ivorn is mock.") ;
            if (DEBUG_FLAG) System.out.println("Creating mock delegate.") ;
            //
            // Return a mock delegate.
            return new PolicyManagerMockDelegate() ;
            }
        //
        // If the ident is real.
        else {
            if (DEBUG_FLAG) System.out.println("Ivorn is real.") ;
            if (DEBUG_FLAG) System.out.println("Resolving endpoint URL.") ;
            //
            // Lookup the endpoint in the registry.
            URL endpoint = resolver.resolve(parser, PolicyManager.class) ;
            if (DEBUG_FLAG) System.out.println("PASS : Got endpoint url") ;
            if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
            if (DEBUG_FLAG) System.out.println("Creating SOAP delegate.") ;
            //
            // Return a new delegate
            return this.resolve(endpoint) ;
            }
        }

    /**
     * Resolve a WebService endpoint into a PolicyManagerDelegate.
     * @param url The PolicyManager endpoint URL.
     * @return A new PolicyManagerDelegate.
     *
     */
    protected PolicyManagerDelegate resolve(URL url)
        {
        return new PolicyManagerSoapDelegate(url) ;
        }
    }
