package org.astrogrid.security.community ;

import java.net.URI;
import java.net.URISyntaxException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.v1_0.RegistryService;

/**
 * A wrapper for the registry delegate to resolve IVORNs endpoints.
 */
public class RegistryClient {
  
  /**
   * Our Registry delegate.
   */
  private RegistryService registry;

  /**
   * Constructs a resolver using the configured registry-service. 
   * The configuration may impose a mockery of the registry for testing.
   */
  public RegistryClient() {
    this.registry = RegistryDelegateFactory.createQueryv1_0();
  }
  
  /**
   * Constructs a resolver using a given stub for the registry.
   * This constructor is for unit testing: pass a MockRegistry here.
   *
   * @param registry The stub for the registry service (may be a mock object).
   */
  public RegistryClient(RegistryService registry) {
    this.registry = registry;
  }
  
  /**
   * Reveals the endpoint for a given resource and capability.
   */
  public String getEndpointByIdentifier(String service,
                                        String capability) throws RegistryException {
    String endpoint = registry.getEndpointByIdentifier(service, capability);
    if (endpoint == null) {
      throw new RegistryException(
          "No endpoint is registered for " + capability + " in " + service
      );
    }
    return endpoint;
  }

  /**
   * Supplies the endpoint for an community-accounts service.
   *
   * @param ivorn The IVORN for the service registration.
   * @return The endpoint (null if service or capability not registered).
   * @throws RegistryException If the registry does not respond.
   */
  public URI getAccountsEndpoint(URI ivorn) throws RegistryException {
   return getEndpoint(ivorn, "ivo://org.astrogrid/std/Community/accounts");
  }

  /**
   * Supplies the endpoint for an MyProxy service.
   *
   * @param ivorn The IVORN for the service registration.
   * @return The endpoint (null if service or capability not registered).
   * @throws RegistryException If the registry does not respond.
   */
  public URI getMyProxyEndpoint(URI ivorn) throws RegistryException {
    return getEndpoint(ivorn, "ivo://ivoa.net/std/MyProxy");
  }

  /**
   * Supplies the endpoint for a given registration and capability.
   *
   * @param ivorn The IVORN for the service registration.
   * @param capability The URI identifying the capability.
   * @return The endpoint (null if service or capability not registered).
   * @throws RegistryException If the registry does not respond.
   */
  private URI getEndpoint(URI ivorn, String capability) throws RegistryException {
    if (ivorn.getScheme().equals("ivo")) {
      try {
        return new URI(registry.getEndpointByIdentifier(ivorn.toString(), capability));
      }
      catch (URISyntaxException ex) {
        throw new RegistryException("Registered endpoint is invalid", ex);
      }
    }
    else {
      return null;
    }
  }
}

