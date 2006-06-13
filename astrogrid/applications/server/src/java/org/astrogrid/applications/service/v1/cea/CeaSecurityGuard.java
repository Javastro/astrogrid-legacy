package org.astrogrid.applications.service.v1.cea;

import org.astrogrid.security.SecurityGuard;

/**
 * A specialization of SecurityGuard for use in CEA services.
 *
 * It is sometimes necessary to get at the authenticated principals
 * and credentials from the method that implements an CEA application. 
 * That method typically runs in a separate thread from the web-service
 * invocation that launched it; therefore, the working method cannot call
 * AxisClientSecurityGuard.getInstanceFromContext() to get the guard object.
 * This class offers a form of getInstanceFromContext() that works between
 * threads, such that a guard may be cached in the web-service invocation
 * and retrieved in the worker thread.
 *
 * The mechanism uses java.lang.InheritableThreadLocal to cache one
 * instance of this class per thread. Assuming that each invocation of the
 * web service runs in a separate thread, this is then well-behaved for
 * concurrent invocations of the web-service. Any worker thread created
 * by a web-service thread inherits a copy of the guard cached by the
 * latter. If worker threads are not used - i.e. if the application code
 * is executed directly from the web-service thread - then the correct guard
 * is still retrieved from the cache.
 *
 * @author Guy Rixon
 */
public class CeaSecurityGuard extends SecurityGuard {
  
  /**
   * The cached guard-object. When accessed, the cache for a given thread
   * always contains an object that was explicitly put in, or, failing that,
   * one created on the fly using the anonymous, inner class below. In the 
   * latter case, the guard has no principals or credentials and expresses 
   * a lack of authentication.
   */
  private static InheritableThreadLocal store
      = new InheritableThreadLocal() {
          protected synchronized Object initialValue() {
            return new CeaSecurityGuard();
          }
        };
  
  /** Creates a new instance of CeaSecurityGuard */
  public CeaSecurityGuard() {
    super();
  }
  
  public CeaSecurityGuard(SecurityGuard g) {
    super(g);
  }
  
  public static void setInstanceInContext(SecurityGuard g1) {
    CeaSecurityGuard g2 = new CeaSecurityGuard(g1);
    CeaSecurityGuard.store.set(g2);
  }
  
  /**
   * Fetch the guard from the cache. If nothing is in the cache,
   * creates a new guard with no credentials or principals.
   *
   * @return - the guard (never null).
   */
  public static CeaSecurityGuard getInstanceFromContext() {
    return (CeaSecurityGuard)(CeaSecurityGuard.store.get());
  }
  
  /**
   * Tests whether the current call to the web service has been
   * authenticated. Assumes that an X500Principal will have been
   * recorded in any successful authentication.
   *
   * @return true if the call is anonymous
   */
  public boolean isAuthenticated() {
    return (this.getX500Principal() != null);
  }

}
