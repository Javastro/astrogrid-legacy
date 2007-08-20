package org.astrogrid.security.authorization;

import java.util.Map;
import org.astrogrid.security.SecurityGuard;

/**
 * An AccessPolicy that accepts all requests.
 * 
 * @author Guy Rixon
 */
public class OpenAccessPolicy implements AccessPolicy {
  
  /**
   * Accepts all requests.
   */
  public Map decide(SecurityGuard guard, Map request) {
    return null;
  }
  
}
