/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/security/service/SecurityServiceResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceResolver.java,v $
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.28.2  2004/06/17 15:17:30  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.3.28.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.security.service ;

import java.net.URL ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.security.service.SecurityService ;

import org.astrogrid.community.client.security.service.SecurityServiceDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceSoapDelegate ;

import org.astrogrid.community.resolver.CommunityEndpointResolver ;

import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

import org.astrogrid.registry.RegistryException;

/**
 * A toolkit to resolve an Ivorn identifier into a SecurityServiceResolver delegate.
 *
 */
public class SecurityServiceResolver
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
    public SecurityServiceResolver()
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
    public SecurityServiceResolver(URL registry)
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
     * Resolve an Ivorn identifier into a SecurityServiceDelegate.
     * If the Ivorn matches the MOCK_IVORN, this will return a SecurityServiceMockDelegate.
     * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
     * @param ident The service identifier.
     * @return A new SecurityServiceDelegate.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public SecurityServiceDelegate resolve(Ivorn ivorn)
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceResolver.resolve()") ;
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
     * Resolve data from a CommunityIvornParser into a SecurityServiceDelegate.
     * If the Ivorn matches the MOCK_IVORN, this will return a SecurityServiceMockDelegate.
     * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
     * @param parser The service identifier.
     * @return A new SecurityServiceDelegate.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public SecurityServiceDelegate resolve(CommunityIvornParser parser)
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceResolver.resolve()") ;
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
            return new SecurityServiceMockDelegate() ;
            }
        //
        // If the ident is real.
        else {
            if (DEBUG_FLAG) System.out.println("Ivorn is real.") ;
            if (DEBUG_FLAG) System.out.println("Resolving endpoint URL.") ;
            //
            // Lookup the endpoint in the registry.
            URL endpoint = resolver.resolve(parser, SecurityService.class) ;
            if (DEBUG_FLAG) System.out.println("PASS : Got endpoint url") ;
            if (DEBUG_FLAG) System.out.println("  URL : " + endpoint) ;
            if (DEBUG_FLAG) System.out.println("Creating SOAP delegate.") ;
            //
            // Return a new delegate
            return this.resolve(endpoint) ;
            }
        }

    /**
     * Resolve a WebService endpoint into a SecurityServiceDelegate.
     * @param url The SecurityService endpoint URL.
     * @return A new SecurityServiceDelegate.
     *
     */
    protected SecurityServiceDelegate resolve(URL url)
        {
        return new SecurityServiceSoapDelegate(url) ;
        }
    }
