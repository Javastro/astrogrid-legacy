package org.astrogrid.community.resolver.security.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import org.astrogrid.registry.client.query.v1_0.RegistryService;

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
public class SecurityServiceResolver extends CommunityEndpointResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityServiceResolver.class);

    /**
     * Public constructor, using the default Registry service.
     *
     */
    public SecurityServiceResolver() {
      super();
    }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public SecurityServiceResolver(URL registry) {
      super(registry);
    }
    
  /**
   * Constructs a resolver using a given registry-delegate.
   *
   * @param registry The registry delegate.
   */
  public SecurityServiceResolver(RegistryService registry) {
   super(registry);
  }

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceResolver.resolve()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceResolver.resolve()") ;
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
            return new SecurityServiceMockDelegate() ;
            }
        //
        // If the ident is real.
        else {
            log.debug("Ivorn is real.") ;
            log.debug("Resolving endpoint URL.") ;
            //
            // Lookup the endpoint in the registry.
            URL endpoint = 
                this.resolve(parser.getIvorn(), 
                             "ivo://org.astrogrid/std/Community/v1.0#SecurityService");
            log.debug("PASS : Got endpoint url") ;
            log.debug("  URL : " + endpoint) ;
            log.debug("Creating SOAP delegate.") ;
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
