package org.astrogrid.slinger.sourcetargets;

import org.astrogrid.security.SecurityGuard;
import org.astrogrid.vospace.v11.client.security.SecurityGuardResolver;

/**
 * A resolver for security guards. An instance of this class supplies
 * SecurityGuard objects to the VOSpace code in a thread-safe manner.
 *
 * To connect the resolver with the VOSpace system-delegate, instantiate
 * this class and pass the instance to the constructor of the delegate.
 * The latter will then call {@link #current()} on the resolver whenever
 * it needs credentials. To pass the credentials of the right user,
 * observing that the delegate may be used from many threads, obtain a
 * lock on the resolver (how? WTF?), then call {#setCurrent(SecurityGuard)}
 * on the resolver, then call the desired function on the delegate,
 * then release the lock.
 *
 * @author Guy Rixon
 */
public class CredentialResolver implements SecurityGuardResolver {

  /**
   * The credentials of the current user.
   */
  private SecurityGuard guard = null;

  /**
   * Constructs a resolver with a given guard.
   *
   * @param guard The guard.
   */
  public CredentialResolver(SecurityGuard g) {
    guard = g;
  }

  /**
   * Supplies the credentials for the current user.
   * If no current user has been set, supplies a guard empty
   * of credentials.
   * 
   * @return The credentials (never null).
   */
  public SecurityGuard current() {
    return (guard == null)? new SecurityGuard() : guard;
  }

  /**
   * Not used.
   */
  public boolean login() {
    throw new UnsupportedOperationException("Not appropriate for this application.");
  }
}
