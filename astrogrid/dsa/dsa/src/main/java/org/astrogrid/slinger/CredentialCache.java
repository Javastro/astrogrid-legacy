package org.astrogrid.slinger;

import java.security.Principal;
import java.util.Hashtable;
import java.util.Map;
import org.astrogrid.security.SecurityGuard;

/**
 * A cache for the credentials of authenticated users.
 * <p>
 * Some requests to a DSA service authenticate the user's identity and
 * supply delegated credentials. Some activivities, notably writing to
 * VOSpace need to use those delegated credentials. For historical reasons, 
 * the credentials are acquired and used in different classes called from 
 * different threads, and does not pass the credentials between them. It does
 * however pass a Principal.
 * <p>
 * This class stores credentials (packed as SecurityGuard objects) in a map
 * indexed by principals. The class and map are static, so all requests
 * see the same cache. This correct behaviour: requests under the same identity
 * have the same set of credentials and principals.
 * 
 *
 * @author Guy Rixon
 */
public class CredentialCache {
  
  /**
   * The storage for the credentials.
   */
  private static Map<Principal,SecurityGuard> guards = 
      new Hashtable<Principal,SecurityGuard>();
  
  /**
   * Stores credentials. Any previous credentials under the given principal
   * are overwritten.
   *
   * @param p The identity for which credentials are to be stored.
   * @param sg The object holding the credentials to be stored.
   */
  public static void put(Principal p, SecurityGuard sg) {
    guards.put(p, sg);
  }
  
  /**
   * Returns credentials for the given identity. If no credentials are
   * stored for that identity, returns a SecurityGuard empty of credentials.
   *
   * @param p The identity for which credentials are stored.
   * @return The object holding the credentials (never null).
   */
  public static SecurityGuard get(Principal p) {
    SecurityGuard sg = guards.get(p);
    return (sg == null)? new SecurityGuard() : sg;
  }
  
}
