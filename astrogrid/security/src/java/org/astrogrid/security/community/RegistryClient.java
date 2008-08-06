package org.astrogrid.security.community ;

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
    this.registry = new RegistryDelegateFactory().createQueryv1_0();
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

}

