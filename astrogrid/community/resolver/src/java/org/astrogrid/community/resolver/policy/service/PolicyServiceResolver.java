/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/policy/service/PolicyServiceResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceResolver.java,v $
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/23 14:51:26  dave
 *   Added SecurityManagerResolver and SecurityServiceResolver.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.policy.service ;

import java.net.URL ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.service.PolicyService ;

import org.astrogrid.community.client.policy.service.PolicyServiceDelegate ;
import org.astrogrid.community.client.policy.service.PolicyServiceMockDelegate ;
import org.astrogrid.community.client.policy.service.PolicyServiceSoapDelegate ;

import org.astrogrid.community.resolver.CommunityEndpointResolver ;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

import org.astrogrid.registry.RegistryException;

/**
 * A toolkit to resolve an Ivorn identifier into a PolicyServiceResolver delegate.
 *
 */
public class PolicyServiceResolver
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
    public PolicyServiceResolver()
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
    public PolicyServiceResolver(URL registry)
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
     * Resolve an Ivorn identifier into a PolicyServiceDelegate.
     * If the Ivorn matches the MOCK_IVORN, this will return a PolicyServiceMockDelegate.
     * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
     * @param ident The service identifier.
     * @return A new PolicyServiceDelegate.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public PolicyServiceDelegate resolve(Ivorn ivorn)
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceResolverImpl.resolve()") ;
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
     * Resolve data from a CommunityIvornParser into a PolicyServiceDelegate.
     * If the Ivorn matches the MOCK_IVORN, this will return a PolicyServiceMockDelegate.
     * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
     * @param parser The service identifier.
     * @return A new PolicyServiceDelegate.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public PolicyServiceDelegate resolve(CommunityIvornParser parser)
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceResolverImpl.resolve()") ;
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
            return new PolicyServiceMockDelegate() ;
            }
        //
        // If the ident is real.
        else {
            if (DEBUG_FLAG) System.out.println("Ivorn is real.") ;
            if (DEBUG_FLAG) System.out.println("Resolving endpoint URL.") ;
            //
            // Lookup the endpoint in the registry.
            URL endpoint = resolver.resolve(parser, PolicyService.class) ;
            if (DEBUG_FLAG) System.out.println("PASS : Got endpoint url") ;
            if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
            if (DEBUG_FLAG) System.out.println("Creating SOAP delegate.") ;
            //
            // Return a new delegate
            return this.resolve(endpoint) ;
            }
        }

    /**
     * Resolve a WebService endpoint into a PolicyServiceDelegate.
     * @param url The PolicyService endpoint URL.
     * @return A new PolicyServiceDelegate.
     *
     */
    protected PolicyServiceDelegate resolve(URL url)
        {
        return new PolicyServiceSoapDelegate(url) ;
        }
    }
