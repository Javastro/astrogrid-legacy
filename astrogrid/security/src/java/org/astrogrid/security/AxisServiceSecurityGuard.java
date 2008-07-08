package org.astrogrid.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;
import org.apache.axis.MessageContext;
import org.apache.axis.server.AxisServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.security.delegation.Delegations;


/**
 * A SecurityGuard specialized for a web service. Objects of
 * this class are used in the implementation methods of the
 * service to get access to the credentials acquired and tested
 * by the handler chain.
 *
 * The public, no-argument constructor creates a
 * ServiceSecurityGuard with no credentials (i.e. with an
 * empty JAAS subject). This is not very useful. A service
 * should normal acquire a ServiceSecurityGuard by calling the
 * static method {@link #getInstanceFromContext()} which
 * initializes the guard from the JAX-RPC message context.
 *
 * @author Guy Rixon
 */
public class AxisServiceSecurityGuard extends SecurityGuard {

  static Log log = LogFactory.getLog(AxisServiceSecurityGuard.class);
  
  /**
   * A message context used for testing only.
   */
  static MessageContext testContext = null;
  
  /**
   * The name of the Axis property under which the guard is cached in the
   * message context.
   */
  static protected final String AXIS_PROPERTY = "org.astrogrid.security.guard";

  /**
   * Constructs a ServiceSecurityGuard with an empty JAAS subject.
   */
  public AxisServiceSecurityGuard () {
    super();
  }

  /**
   * Constructs a AxisServiceSecurityGuard with a given JAAS subject.
   */
  public AxisServiceSecurityGuard(Subject s) {
    super(s);
  }
  
  /**
   * Constructs an AxisServiceSecurityGuard based on a previous authentication.
   * The results of authentication must be attached to the Axis
   * message-context; see {@link AxisServiceCredentialHandler}. If authentication
   * did not succeed then this method completes silently and constructs a
   * guard containing no credentials or principals.
   *
   * @param context The message context holding credentials and principals.
   */
  public AxisServiceSecurityGuard(MessageContext context) {
    super();
    SecurityGuard guard = 
        (context == null)? null : (SecurityGuard) context.getProperty(AXIS_PROPERTY);
    if (guard == null) {
      log.debug("Axis message-context did not contain a security-guard object " +
                "so the constructed object has no principals.");
    }
    else {
      this.subject = this.cloneSubject(guard.getSubject());
    }
  }
  
  
  /**
   * Constructs a ServiceSecurityGuard and sets its credentials
   * from the message context of the call to the web service.
   */
  public static AxisServiceSecurityGuard getInstanceFromContext () {
    MessageContext context = 
        (testContext == null)? MessageContext.getCurrentContext() : testContext;
    return new AxisServiceSecurityGuard(context);
  }
  
  /**
   * Loads credentials and principals delegated by a client.
   * For this method to work, the guard must already have an authenticated
   * X500Principal.
   */
  public void loadDelegation() throws CertificateException {
    X500Principal principal = getX500Principal();
    if (principal != null) {
      Delegations delegations = Delegations.getInstance();
      String hashKey = Integer.toString(principal.hashCode());
      if (delegations.hasCertificate(hashKey)) {
        X509Certificate x509 = delegations.getCertificate(hashKey);
        X509Certificate[] chain1 = getCertificateChain();
        X509Certificate[] chain2 = new X509Certificate[chain1.length+1];
        chain2[0] = x509;
        for (int i = 1; i < chain2.length; i++) {
          chain2[i] = chain1[i-1];
        }
        setCertificateChain(chain2);
        setPrivateKey(delegations.getPrivateKey(hashKey));
      }
    }
  }
  
  /**
   * Loads a principal for a fake user, as if that user had authenticated to
   * the service. Does not load the user's credentials. The user information
   * is attached to a fake message-context such that the factory method
   * {@link #getInstanceFromContext()} see an apparent authentication.
   * This method is only for testing.
   */
  public static void loadFakeUserFred() {
    AxisServiceSecurityGuard.testContext = new MessageContext(new AxisServer());
    AxisServiceSecurityGuard g = new AxisServiceSecurityGuard();
    g.setX500Principal(new X500Principal("o=ioa,cn=fred hoyle"));
    AxisServiceSecurityGuard.testContext.setProperty(AXIS_PROPERTY, g);
  }


  /**
   * Tests whether the current call to the web service is
   * anonymous. The call is considered anonymous if there
   * are no JAAS principals in the JAAS Subject.
   *
   * @return true if the call is anonymous
   */
  public boolean isAnonymous () {
    return this.getSubject().getPrincipals().size() == 0;
  }

}

