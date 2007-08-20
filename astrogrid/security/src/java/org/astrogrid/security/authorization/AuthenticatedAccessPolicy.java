package org.astrogrid.security.authorization;

import java.security.GeneralSecurityException;
import java.util.Map;
import org.astrogrid.security.SecurityGuard;

/**
 * An access policy that accepts any request from an authenticated caller.
 *
 * @author Guy Rixon
 */
public class AuthenticatedAccessPolicy implements AccessPolicy {
  
  public Map decide(SecurityGuard guard, 
                    Map request) throws SecurityException, 
                                        GeneralSecurityException, 
                                        Exception {
    if (guard.getX500Principal() == null) {
      throw new GeneralSecurityException(
          "The caller's identity is not authenticated."
      );
    }
    return request;
  }
  
  
}
