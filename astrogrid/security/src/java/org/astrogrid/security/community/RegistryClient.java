package org.astrogrid.security.community ;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.v1_0.RegistryService;

/**
 * A wrapper for the registry delegate to resolve IVORNs endpoints.
 */
public class RegistryClient extends Mockery {
  
  /**
   * Our Registry delegate.
   */
  private RegistryService registry;

  /**
   * Constructs a resolver using the configured registry-service. 
   * The configuration may impose a mockery of the registry for testing.
   */
  public RegistryClient() {
    super();
    
    // This consults the AstroGrid configuration to determine the endpoint
    // of the registry.
    if (isMock()) {
      this.registry = new MockRegistry();
    }
    else {
      this.registry = new RegistryDelegateFactory().createQueryv1_0();
    }
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

