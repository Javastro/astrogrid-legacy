package org.astrogrid.security;

import org.apache.axis.ConfigurationException;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Stub;

/**
 * A SecurityGuard specialized to client operations with Axis and
 * WSS4J. Axis delegates should use this class.
 *
 * An object of this class is typically derived from a given, generic
 * SecurityGuard via the constructor AxisClientSecurityGuard(SecurityGuard).
 * This constructor copies the principals and credentials from the given guard.
 * Subsequent changes to the given guard that add, remove or replace 
 * credentials or principals do not affect the newly-constructed guard.
 * However, any changes to actual objects representing the credentials
 * or principals do affect the new guard.
 *
 * To enable digital signatures on an Axis stub, call the 
 * {@link configureStub} method. This passes references to the credentials
 * to the stub and sets it to sign each subsequent message to its service.
 * In order for the signature to work, the grid subject of the guard must
 * contain the following credentials: a java.security.PrivateKey and a 
 * java.security.cert.CertPath.
 *
 * @author Guy Rixon
 */
public class AxisClientSecurityGuard extends SecurityGuard {
  
  /**
   * Creates a ClientSecurityGuard with no credentials.
   */
  public AxisClientSecurityGuard () {
    super();
  }

  /**
   * Creates a ClientSecurityGuard with credentials.
   * The credentials are copied from a given SecurityGuard.
   *
   * @param sg The source of the credentials.
   */
   public AxisClientSecurityGuard (SecurityGuard sg) {
     super(sg);
  }

  /**
   * Sets a guard on an Axis Stub.
   * This passes to the signature handler in the stub the
   * settings and credential needed to sign outgoing messages.
   *
   * @param stub The stub on which messages are to be signed.
   */
  public void configureStub(Stub stub) throws Exception {
    stub._setProperty("org.astrogrid.security.authenticate", Boolean.TRUE);
    stub._setProperty("org.astrogrid.security.guard", this);
  }

  /**
   * Makes a configuration for an AxisEngine that applies digital signature to
   * outgoing messages. This configuration should be used when creating stubs.
   * in the client.
   *
   * @return The configuration.
   */
  public static EngineConfiguration getEngineConfiguration() 
      throws ConfigurationException {
    return new SecuredClientConfig();
  }

}