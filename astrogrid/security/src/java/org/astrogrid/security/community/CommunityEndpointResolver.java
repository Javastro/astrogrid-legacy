package org.astrogrid.security.community ;

import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.store.Ivorn;


/**
 * A wrapper for the registry delegate to resolve IVORNs endpoints.
 *
 * Two kinds of IVORN may be resolved: those that identify services directly
 * and those that identify accounts hosted on community services. In the latter
 * case, endpoints may only be obtained for services of that community. Both
 * old (ivo://authority/user) and new (ivo://user@authority/...) forms are
 * supported for account IVORNs.
 *
 * This class works only with v1.0 of the registry interface and assumes
 * service registrations based on VOResource 1.0; i.e. it resolves specific,
 * named capabilities of registered services. E.g., a community registration
 * consists in one service resource with (at least) capabilities for
 * PolicyManager, SecurityService and MyProxy. The formal names of capabilities
 * are themselves IVORNs.
 */
public class CommunityEndpointResolver extends Mockery {

  private static Log log = LogFactory.getLog(CommunityEndpointResolver.class);
  
  /**
   * Our Registry delegate.
   */
  private RegistryService registry;
  
  /**
   * The endpoint for the SSO/accounts service.
   */
  private URI accountsEndpoint;
  
  /**
   * The endpoint for the MyProxy service.
   * This is stored as a URI with the scheme myproxy.
   * This kind of URI is only recognized withing the AstroGrid
   * security-facade.
   */
  private URI myProxyEndpoint;

  /**
   * Constructs a resolver using the configured registry-service. 
   * The configuration may impose a mockery of the registry for testing.
   *
   * @param community The formal name (IVORN) of the community service.
   */
  public CommunityEndpointResolver(String community) throws Exception {
    super();
    
    // This consults the AstroGrid configuration to determine the endpoint
    // of the registry.
    if (isMock()) {
      this.registry = new MockRegistry();
    }
    else {
      this.registry = new RegistryDelegateFactory().createQueryv1_0();
    }
    this.accountsEndpoint = resolve(community, "ivo://org.astrogrid/std/Community/accounts");
    this.myProxyEndpoint  = resolve(community, "ivo://org.astrogrid/std/Community/v1.0#MyProxy");
  }
  
  /**
   * Reveals the endpoint for the SSO/Accounts service.
   *
   * @return The endpoint URI (null if not known).
   */
  public URI getAccounts() {
    return this.accountsEndpoint;
  }
  
  /**
   * Reveals the endpoint for the MyProxy service.
   *
   * @return The endpoint URI (null if not known).
   */
  public URI getMyProxy() {
    return this.myProxyEndpoint;
  }
    
  /**
   * Resolves an IVORN to an endpoint URI.
   */
  private URI resolve(String account, String standardId) throws Exception {
    assert account != null;
    assert standardId != null;
    
    // Translate the account IVORN to an IVORN for the service hosting
    // the account.
    Ivorn community = new CommunityIvornParser(account).getCommunityIvorn();
    
    // Look up the service in the registry and get its endpoint.
    String endpoint = 
        registry.getEndpointByIdentifier(community.toString(), standardId);
    if (endpoint == null) {
      log.debug("No endpoint is registered for " + standardId + " in " + community);
      return null;
    }
    else {
      return new URI(endpoint);
    }
  }

}

