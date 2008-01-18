/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/policy/service/PolicyServiceResolver.java,v $</cvs:source>
 * <cvs:author>$Author: gtr $</cvs:author>
 * <cvs:date>$Date: 2008/01/18 16:36:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceResolver.java,v $
 *   Revision 1.5  2008/01/18 16:36:08  gtr
 *   Branch community-gtr-2502 is merged.
 *
 *   Revision 1.4.170.2  2008/01/18 15:50:04  gtr
 *   I avoid premature and unreliable conversion of account IVORNs.
 *
 *   Revision 1.4.170.1  2008/01/17 10:57:26  gtr
 *   Resolution based on Class is obsolete (it wasn't working anyway in the last alpha-release).
 *
 *   Revision 1.4  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.3.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.3  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.36.2  2004/06/17 15:17:30  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.2.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.policy.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.service.PolicyService ;

import org.astrogrid.community.client.policy.service.PolicyServiceDelegate ;
import org.astrogrid.community.client.policy.service.PolicyServiceMockDelegate ;
import org.astrogrid.community.client.policy.service.PolicyServiceSoapDelegate ;

import org.astrogrid.community.resolver.CommunityEndpointResolver ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyServiceResolver.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceResolverImpl.resolve()") ;
        log.debug("  Ivorn : " + ((null != ivorn) ? ivorn : null)) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceResolverImpl.resolve()") ;
        log.debug("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
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
            log.debug("Ivorn is mock.") ;
            log.debug("Creating mock delegate.") ;
            //
            // Return a mock delegate.
            return new PolicyServiceMockDelegate() ;
            }
        //
        // If the ident is real.
        else {
            log.debug("Ivorn is real.") ;
            log.debug("Resolving endpoint URL.") ;
            //
            // Lookup the endpoint in the registry.
            URL endpoint = 
                resolver.resolve(parser.getIvorn(), 
                                 "ivo://org.astrogrid/std/Community/v1.0#PolicyService");
            log.debug("PASS : Got endpoint url") ;
            log.debug("  URL : " + endpoint) ;
            log.debug("Creating SOAP delegate.") ;
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
