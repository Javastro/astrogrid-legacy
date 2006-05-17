package org.astrogrid.desktop.modules.ag;

import java.net.URISyntaxException;
import java.security.Principal;
import org.astrogrid.store.Ivorn;

/**
 * The IVO Resource name (IVORN) or IVO Resource Identifier (IVOID) for the
 * root VOSpace of  a user registered at an IVO community. This is a trivial 
 * extension of the base Ivorn class that indicates the usage of the IVORN. 
 * It is useful when IVORNs are stored inside JAAS Subjects.
 *@todo move to separate subproject
 * @author Guy Rixon
 */
public class HomeIvorn extends Ivorn implements Principal {

  public HomeIvorn(String ivorn) throws URISyntaxException {
    super(ivorn);
  }
  
  public HomeIvorn(String path, String fragment) {
    super(path, fragment);
  }
  
  public HomeIvorn(String authority, String resourceKey, String fragment) {
    super(authority, resourceKey, fragment);
  }
  
  public String getName() {
    return super.toString();
  }
}