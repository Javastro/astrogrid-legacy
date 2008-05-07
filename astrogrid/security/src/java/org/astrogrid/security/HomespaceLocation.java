package org.astrogrid.security;

import java.security.Principal;

/**
 * A principal that records a users home space in VOSpace.
 * The name of the principal is the string form of the URI locating that
 * space, which may be in the ivo or vos schemes.
 *
 * @author Guy Rixon
 */
public class HomespaceLocation implements Principal {

  /**
   * The URI for the space.
   */
  private String uri;
  
  /**
   * Constructs a HomespaceLocation.
   */
  public HomespaceLocation(String uri) {
    this.uri = uri;
  }
  
  /**
   * Reveals the URI for the homespace. This is normally an abstract URI in the
   * vos scheme (for IVOA VOSpace) or the ivo scheme (for AstroGrid MySpace).
   *
   * @return The URI.
   */
  public String getName() {
    return this.uri;
  }
  
}
