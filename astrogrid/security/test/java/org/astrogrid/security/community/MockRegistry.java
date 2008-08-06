package org.astrogrid.security.community;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.store.Ivorn;
import org.w3c.dom.Document;

/**
 * A mockery of the registry delegate. This simulates the registry's
 * response for a hypothetical community. It implements RegistryService,
 * but patchily. Most of the methods do nothing and return null. Only those
 * known to be used by the community resolvers have a proper implementation.
 *
 * The simulated resgistry has two registered communities for which the
 * service IVORNs are ivo://org.astrogrid.new-registry/community and
 * ivo://org.astrogrid.new-registry/other-community. Two are needed, since 
 * we need to test the resolvers' ability to detect specific resource keys.
 *
 * @author Guy Rixon
 */
public class MockRegistry implements RegistryService {
  
  final static public int ACCOUNTS_PORT = 6666;
  
  /**
   * Constructs a MockRegistry.
   */
  public MockRegistry() {
  }

  public Document getRegistries() throws RegistryException {
    return null;
  }

  public Document search(String string) throws RegistryException {
    return null;
  }

  public Document search(Document document) throws RegistryException {
    return null;
  }

  public Document searchFromSADQL(String string) throws RegistryException {
    return null;
  }

  public Document keywordSearch(String string, boolean b) throws RegistryException {
    return null;
  }

  public Document keywordSearch(String string) throws RegistryException {
    return null;
  }

  public Document xquerySearch(String string) throws RegistryException {
    return null;
  }

  public Document loadRegistry() throws RegistryException {
    return null;
  }

  public Document getIdentity() throws RegistryException {
    return null;
  }

  public HashMap managedAuthorities() throws RegistryException {
    return null;
  }

  public Document getResourceByIdentifier(Ivorn ivorn) throws RegistryException {
    return null;
  }

  public Document getResourceByIdentifier(String string) throws RegistryException {
    return null;
  }

  public ResourceData getResourceDataByIdentifier(Ivorn ivorn) throws RegistryException {
    return null;
  }

  public ResourceData[] getResourceDataByRelationship(Ivorn ivorn) throws RegistryException {
    return null;
  }

  public ResourceData[] getResourceDataByRelationship(String string) throws RegistryException {
    return null;
  }

  /*
   * This implements the old getEndpointByIdentifier() as if the registry was
   * the old, VOResource-0.10 kind. For a VOResource-1.0 registry this call 
   * wouldn't give consistent results.
   *
  public String getEndPointByIdentifier(Ivorn ivorn) throws RegistryException {
    if (ivorn.toUri().getAuthority().equals("org.astrogrid.local")) {
      return "http://org.astrogrid.local/community-service";
    }
    else {
      throw new RegistryException("'" + ivorn.toString() + "' was not found in the registry.");
    }
  }
  */
  
  // This implementation always fails. That way, we find out if any of the
  // resolver tests are still using it.
  public String getEndPointByIdentifier(Ivorn ivorn) throws RegistryException {
    throw new RegistryException("'" + ivorn.toString() + "' was not found in the registry.");
  }

  public String getEndPointByIdentifier(String string) throws RegistryException {
    return null;
  }

  public String[] getEndpointsByIdentifier(String string, String string0) throws RegistryException {
    return null;
  }

  public String[] getEndpointsByIdentifier(String string, String string0, String string1) throws RegistryException {
    return null;
  }

  public String getEndpointByIdentifier(String ivornString, String standardId) throws RegistryException {
    System.out.println("Simulating look-up of " + ivornString);
    URI ivorn = null;
    try {
      ivorn = new URI(ivornString);
    } catch (URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
    
    if (ivorn.getAuthority().equals("org.astrogrid.new-registry")|| 
        ivorn.getAuthority().equals("pond")) {
      String resourceKey = ivorn.getPath();
      if (resourceKey.equals("/community") || resourceKey.equals("/other-community")) {
        if (standardId.equals("ivo://org.astrogrid/std/Community/accounts")) {
          return "http://localhost:" + ACCOUNTS_PORT + resourceKey + "/accounts";
        }
        else if (standardId.equals("ivo://org.astrogrid/std/Community/v1.0#MyProxy")) {
          return "myproxy://org.astrogrid:7512";
        }
        else {
          return null;
        }
      }
      else {
        return null;
        //throw new RegistryException("'" + ivorn + "' was not found in the registry.");
      }
    }
    else {
      return null;
      //throw new RegistryException("'" + ivorn + "' was not found in the registry.");
    }
  }

  public String getEndpointByIdentifier(String string, String string0, String string1) throws RegistryException {
    return null;
  }

  public String[] getEndpoints(String string) throws RegistryException {
    return null;
  }
  
  public Document keywordSearch(String s, boolean b1, int i, boolean b2) {
    return null;
  }
  
  public Document keywordSearch(String s, boolean b1, int i1, int i2, boolean b2) {
    return null;
  }
  
  public Document keywordSearch(String s, int i2, boolean b2) {
    return null;
  }
  
  public Document keywordSearch(String s, int i1, int i2, boolean b2) {
    return null;
  }
  
  public Document searchFromSADQL(String s, int i1, boolean b2) {
    return null;
  }
  
  public Document searchFromSADQL(String s, int i1, int i2, boolean b2) {
    return null;
  } 

  public Document search(String string, int i, boolean b) throws RegistryException {
    return null;
  }

  public Document search(String string, int i, int i0, boolean b) throws RegistryException {
    return null;
  }

  public Document search(Document document, int i, boolean b) throws RegistryException {
    return null;
  }

  public Document search(Document document, int i, int i0, boolean b) throws RegistryException {
    return null;
  }
  
}
