package org.astrogrid.community.resolver;

import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.resolver.CommunityEndpointResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
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
public class CommunityMyProxyResolver {
  
  private static Log log 
      = LogFactory.getLog(CommunityMyProxyResolver.class);
  
  /** Creates a new instance of CommunityMyProxyResolver */
  public CommunityMyProxyResolver() {
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
    CommunityEndpointResolver resolver = new CommunityEndpointResolver();
    URI endpoint = resolver.resolveToUri(new CommunityIvornParser(ivoid),
                                         MyProxy.class);
    // @TODO: consider caching the resolver object.
    
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
