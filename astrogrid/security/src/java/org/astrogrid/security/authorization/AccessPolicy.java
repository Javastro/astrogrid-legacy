package org.astrogrid.security.authorization;

import java.security.GeneralSecurityException;
import java.util.Map;
import org.astrogrid.security.SecurityGuard;

/**
 * The interface for a policy object that accepts or refuses service
 * requests.
 *
 * @author Guy Rixon
 */
public interface AccessPolicy {
  
  /**
   * Accepts or refuses a request taking into consideration the identity of the
   * caller and a collection of attributes describing the request. Returns the
   * request attributes with whatever modifications, typically restrictions,
   * the policy requires.
   * 
   * @param guard The credentials and authenticated principals of the caller.
   * @param request Request attributes.
   * @return Request attributes, possibly modified.
   * @throws SecurityException If service is refused.
   * @throws GeneralSecurityException If service is refused.
   * @throws Exception If no decision can be reached.
   */
  public Map decide(SecurityGuard guard, Map request) 
      throws SecurityException, GeneralSecurityException, Exception;
  
}
