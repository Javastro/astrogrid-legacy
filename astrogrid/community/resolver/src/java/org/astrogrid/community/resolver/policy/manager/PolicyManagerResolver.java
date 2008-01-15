package org.astrogrid.community.resolver.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import org.astrogrid.registry.client.query.v1_0.RegistryService;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;
import org.astrogrid.registry.client.query.ResourceData ;

import org.astrogrid.community.resolver.CommunityEndpointResolver ;

import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

import org.astrogrid.registry.RegistryException;

/**
 * A toolkit to resolve an Ivorn identifier into a PolicyManagerResolver delegate.
 *
 */
public class PolicyManagerResolver extends CommunityEndpointResolver {
    
    /**
     * A log, connected through commons-logging.
     */
    private static Log log = LogFactory.getLog(PolicyManagerResolver.class);

    /**
     * Constructs a PolicyManagerResolver using the default Registry service.
     */
    public PolicyManagerResolver() {
      super();
    }

    /**
     * Constructs a PolicyManagerResolver  a specific Registry service.
     *
     * @param registry The endpoint address for our RegistryDelegate.
     */
    public PolicyManagerResolver(URL registry) {
      super(registry);
    }

  /**
   * Constructs a resolver using a given registry-delegate.
   *
   * @param registry The registry delegate.
   */
  public PolicyManagerResolver(RegistryService registry) {
   super(registry);
  }
  
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
        return this.getIvornForService(ivorn,PolicyManager.class);
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
            URL endpoint = this.resolve(parser, "ivo://org.astrogrid/std/Community/v1.0#PolicyManager") ;
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
