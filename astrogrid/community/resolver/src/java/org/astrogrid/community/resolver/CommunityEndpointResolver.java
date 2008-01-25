package org.astrogrid.community.resolver ;

import java.net.URI;
import java.net.URL ;
import java.net.MalformedURLException ;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityServiceIvornFactory ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.v1_0.RegistryService ;
import org.astrogrid.registry.client.query.ResourceData ;
import org.astrogrid.store.Ivorn ;


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
public class CommunityEndpointResolver {

  private static Log log = LogFactory.getLog(CommunityEndpointResolver.class);
  
  /**
   * Our Registry delegate.
   */
  private RegistryService registry;

  /**
   * Constructs a resolver using the default Registry service.
   *
   */
  public CommunityEndpointResolver() {
    this.registry = new RegistryDelegateFactory().createQueryv1_0();
  }
  
  /**
   * Constructs a resolver using a given registry endpoint.
   * @param registry The endpoint address for our RegistryDelegate.
   *
   */
  public CommunityEndpointResolver(URL registry) {
    this.registry = new RegistryDelegateFactory().createQueryv1_0(registry);
  }
 
  /**
   * Constructs a resolver using a given registry-delegate.
   *
   * @param registry The registry delegate.
   */
  public CommunityEndpointResolver(RegistryService registry) {
    this.registry = registry;
  }
  
  /**
   * Changes the registry delegate.
   * Normally, a registry delegate is set when the object is constructed.
   * This method changes the delegate later. It allows a mock delegate to
   * be injected for testing.
   *
   * @param registry The registry delegate.
   */
  public void setRegistry(RegistryService registry) {
    this.registry = registry;
  }
    
  /**
   * Resolves an IVORN to an endpoint URI.
   */
  public URI resolveToUri(Ivorn account, String standardId)
      throws CommunityIdentifierException, CommunityResolverException {
    if (account == null) {
      throw new CommunityIdentifierException("Can't resolve a null IVORN.");
    }
    
    // Translate the account IVORN to an IVORN for the service hosting
    // the account.
    Ivorn community = new CommunityIvornParser(account).getCommunityIvorn();
    
    // Look up the service in the registry and get its endpoint.
    try {
      
      String endpoint = 
          registry.getEndpointByIdentifier(community.toString(), standardId);
      if (endpoint == null) {
        throw new CommunityResolverException(
            "No endpoint was found for " +
            standardId +
            " in " +
            community +
            " in the registry."
        );
      }
      else {
        return new URI(endpoint);
      }
    } 
    catch (RegistryException ex) {
      throw new CommunityResolverException("Failed to resolve " + account, ex);
    }
    catch (java.net.URISyntaxException ex) {
      throw new CommunityResolverException("Registry gave a malformed endpoint " +
                                           "for service " + 
                                           community + 
                                           ", capability " + 
                                           standardId);
    }
  }
  
  /**
   * Resolves an IVORN to an endpoint URL.
   */
  public URL resolve(Ivorn ivorn, String standardId)
      throws CommunityIdentifierException, CommunityResolverException {
    URI endpoint = resolveToUri(ivorn, standardId);
    try {
      return endpoint.toURL();
    } catch (MalformedURLException ex) {
      throw new CommunityResolverException(endpoint +
                                           " is not a valid java.net.URL");
    }
  }
  
  /**
   * Resolves a service IVORN to a URI.
   * This method will NOT work with an account URI.
   * If a null IVORN is passed, an NPE will be thrown.
   */
  private URI resolveServiceToUri(Ivorn ivorn, String standardId)
      throws CommunityResolverException {
    try {  
      String endpoint = 
          registry.getEndpointByIdentifier(ivorn.toString(), standardId);
      if (endpoint == null) {
        throw new CommunityResolverException(
            "No endpoint was found for " +
            standardId +
            " in " +
            ivorn +
            " in the registry."
        );
      }
      else {
        return new URI(endpoint);
      }
    } 
    catch (RegistryException ex) {
      throw new CommunityResolverException("Failed to resolve " + ivorn, ex);
    }
    catch (java.net.URISyntaxException ex) {
      throw new CommunityResolverException("Registry gave a malformed endpoint " +
                                           "for service " + 
                                           ivorn + 
                                           ", capability " + 
                                           standardId);
    }
  }

}

