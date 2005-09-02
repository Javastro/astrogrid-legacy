package org.astrogrid.security;

import java.util.Set;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Stub;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.handler.WSHandlerConstants;

/**
 * A SecurityGuard specialized to client operations with Axis and
 * WSS4J. Axis delegates should use this class.
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
   * @param sg The souurce of the credentials.
   */
   public AxisClientSecurityGuard (SecurityGuard sg) {
     super();
     this.gridSubject = sg.getGridSubject();
     this.ssoSubject  = sg.getSsoSubject();
  }

  /**
   * Sets a guard on an Axis Stub.
   * This passes to the signature handler in the stub the
   * settings and credential needed to sign outgoing messages.
   *
   * @param stub The stub on which messages are to be signed.
   */
  public void configureStub(Stub stub) {

    // Make the security handler do digital signature as opposed to
    // encyption or passwords.
    stub._setProperty(WSHandlerConstants.ACTION, "Signature");

    // Specify that the caller's certificate should be included in the
    // request as part of the security header. If this were not set,
    // the default in WSS4J is to require the service to have a copy of
    // that certificate.
    stub._setProperty(WSHandlerConstants.SIG_KEY_ID, "DirectReference");

    // Send the Crypto object, which encapsulates the certificate chain
    // and private key, to the security handler.
    Crypto c;
    Set credentials = this.gridSubject.getPrivateCredentials(Crypto.class);
    if (credentials.size() == 0) {
      c = null;
    }
    else {
      c = (Crypto)(credentials.iterator().next());
    }
    stub._setProperty("org.astrogrid.security.signature.crypto", c);

    // Tell the signature handler the alias and password that it needs to use the Crypto.
    stub._setProperty(WSHandlerConstants.USER, this.getSsoUsername());
    PasswordCallback pwcb = new PasswordCallback(this.getSsoPassword());
    stub._setProperty(WSHandlerConstants.PW_CALLBACK_REF, pwcb);
  }

  /**
   * Makes a configuration for an AxisEngine that applies digital signature to
   * outgoing messages. This configuration should be used when creating stubs.
   * in the client.
   *
   * @return The configuration.
   */
  public static EngineConfiguration getEngineConfiguration() {
    return new SecuredClientConfig();
  }

}
