/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/policy/manager/PolicyManagerResolver.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/07 14:14:25 $</cvs:date>
 * <cvs:version>$Revision: 1.11 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerResolver.java,v $
 *   Revision 1.11  2005/01/07 14:14:25  jdt
 *   merged from Reg_KMB_787
 *
 *   Revision 1.10.12.1  2004/12/11 11:53:16  KevinBenson
 *   modifications to the jsps, also merged in several pieces of the validtion stuff
 *
 *   Revision 1.10  2004/11/04 18:00:02  jdt
 *   Restored following fixes to auto-integration
 *   Merged in Reg_KMB_546 and Reg_KMB_603 and Comm_KMB_583
 *
 *   Revision 1.8  2004/11/02 21:47:39  jdt
 *   Merge of Comm_KMB_583
 *
 *   Revision 1.7.20.1  2004/10/26 06:10:39  KevinBenson
 *   sprucing up admin interface and getting it where it grabs communities and accounts from external communities
 *
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.36.2  2004/06/17 15:17:30  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.5.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;
import org.astrogrid.registry.client.query.ServiceData ;

import org.astrogrid.community.resolver.CommunityEndpointResolver ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyManagerResolver.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyManagerResolverImpl.resolve()") ;
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
    
    public Ivorn getIvornForService(Ivorn ivorn) throws CommunityIdentifierException {
        return resolver.getIvornForService(ivorn,PolicyManager.class);
    }
    
    /*
    public URL[] resolve()
       throws RegistryException, CommunityIdentifierException, CommunityResolverException {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyManagerResolverImpl.resolve()") ;
        
        log.debug("Resolving endpoint URLs.") ;
        //
        // Lookup the endpoint in the registry.
        URL endpoints[] = resolver.resolve() ;
        PolicyManagerD
        elegate []pmd = new PolicyManagerDelegate[endpoints.length];
        
        log.debug("PASS : Got endpoint urls number = " + endpoints.length) ;
        for(int i = 0;i < endpoints.length; i++) {
            log.debug("  URL : " + endpoints[i]) ;
            log.debug("Creating SOAP delegate.") ;
            pmd[i] = this.resolve(endpoints[i]);            
        }
        //
        // Return the delegates
        return pmd;
    }
    */
    
    
    public ServiceData[] resolve()
    throws RegistryException, CommunityIdentifierException, CommunityResolverException {
     log.debug("") ;
     log.debug("----\"----") ;
     log.debug("PolicyManagerResolverImpl.resolve()") ;
     //
     // Lookup the endpoint in the registry.
     return resolver.resolve() ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyManagerResolverImpl.resolve(parser)") ;
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
            return new PolicyManagerMockDelegate() ;
            }
        //
        // If the ident is real.
        else {
            log.debug("Ivorn is real.") ;
            log.debug("Resolving endpoint URL.") ;
            //
            // Lookup the endpoint in the registry.
            URL endpoint = resolver.resolve(parser, PolicyManager.class) ;
            log.debug("PASS : Got endpoint url") ;
            log.debug("  URL : " + endpoint) ;
            log.debug("Creating SOAP delegate.") ;
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
