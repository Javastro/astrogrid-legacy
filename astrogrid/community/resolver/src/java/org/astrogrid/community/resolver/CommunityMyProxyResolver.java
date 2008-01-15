package org.astrogrid.community.resolver;

import java.net.URI;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.store.Ivorn;
import org.globus.myproxy.MyProxy;

/**
 * A resolver for the MyProxy server associated with a community.
 *
 * An AstroGrid Community installation is assumed to have exactly
 * one co-located MyProxy server. That server is assumed to be
 * registered under the same authority ID as the community
 * web-services, and with the resource key 
 *   org.astrogrid.community.MyProxy
 *
 * The resolver returns a java.net.URI for the MyProxy server.
 * The scheme of the URI is "myproxy"; the authority is the
 * server address and the port is the actual port on which
 * the server runs; all this is read from the AccessURL element
 * in the registration. It's not returned as a java.net.URL as 
 * there is no Java connection-handler for the myproxy
 * URI scheme. 
 *
 * @author Guy Rixon
 */
public class CommunityMyProxyResolver extends CommunityEndpointResolver {
  
  private static Log log 
      = LogFactory.getLog(CommunityMyProxyResolver.class);
  
  /** Creates a new instance of CommunityMyProxyResolver */
  public CommunityMyProxyResolver() {
  }
  
  /**
   * Constructs a resolver using a given registry-endpoint.
   *
   * @param registry The registry endpoint.
   */
  public CommunityMyProxyResolver(URL registry) {
    super(registry);
  }
  
  /**
   * Constructs a resolver using a given registry-delegate.
   *
   * @param registry The registry delegate.
   */
  public CommunityMyProxyResolver(RegistryService registry) {
    super(registry);
  }
  
  /**
   * Resolves an IVOID into a delegate for the associated MyProxy server.
   *
   * @return The delegate.
   */
  MyProxy resolve(Ivorn ivoid) 
      throws RegistryException, 
             CommunityIdentifierException, 
             CommunityResolverException {
    
    log.debug("Resolving IVOID "  + 
              ((null != ivoid) ? ivoid : null) +
              " to an org.globus.myproxy.MyProxy.");
        
    // Reject null IVOIDs here for clarity; we can make a better 
    // error-mesage here than we could in the next layer down.
    if (null == ivoid) {
      throw new CommunityIdentifierException(
          "Can't resolve the given identifier (IVOID) " +
          "to an org.globus.myproxy.MyProxy: identifier is null.");
      }
        
    // Resolve the IVOID. The MyProxy class-name is used to infer
    // the resource key for the MyProxy service in the registry.
    URI endpoint = 
        resolveToUri(new CommunityIvornParser(ivoid),
                     "ivo:/org.astrogrid/std/Community/v1.0#MyProxy");
    
    // Extract the service address and port and use them to build the
    // MyProxy delegate.
    String myProxyAddress = endpoint.getHost();
    if (myProxyAddress == null) {
      throw new CommunityResolverException(
          "The MyProxy server associated with " +
          ivoid +
          " has been resolved to the URI " +
          endpoint +
          " which seems to have no host address.");
    }
    int myProxyPort = endpoint.getPort();
    if (myProxyPort == -1) {
      myProxyPort = 7512;
    }
    return new MyProxy(myProxyAddress, myProxyPort);
  }
  
}
