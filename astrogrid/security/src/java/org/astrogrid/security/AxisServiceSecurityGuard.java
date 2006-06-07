package org.astrogrid.security;

import javax.security.auth.Subject;
import org.apache.axis.MessageContext;
import org.apache.log4j.Logger;


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
 * static method getInstanceFromContext() which
 * initializes the guard from the JAX-RPC message context.
 *
 * @author Guy Rixon
 */
public class AxisServiceSecurityGuard extends SecurityGuard {

  static Logger log = Logger.getLogger("org.astrogrid.security.AxisServiceSecurityGuard");

  /**
   * Constructs a ServiceSecurityGuard with an empty JAAS subject.
   */
  public AxisServiceSecurityGuard () {
    super();
  }

  /**
   * Constructs a ServiceSecurityGuard with a given JAAS subject.
   */
  public AxisServiceSecurityGuard (Subject s) {
    super(s);
  }

  /**
   * Constructs a ServiceSecurityGuard and sets its credentials
   * from the message context of the call to the web service.
   */
  public static AxisServiceSecurityGuard getInstanceFromContext () {
    AxisServiceSecurityGuard guard = null;

    // Get the authentication results from the current message context.
    // There may be no result: the might not be a message context, or
    // authentication might not have occured.
    MessageContext msgContext = MessageContext.getCurrentContext();
    if (msgContext == null) {
      log.debug("There is no Axis message context, so principals and credentials cannot be retrieved.");
    }
    if (msgContext != null) {
      guard = (AxisServiceSecurityGuard)(msgContext.getProperty("org.astrogrid.security.guard"));
    }
    if (guard == null) {
      guard = new AxisServiceSecurityGuard();
    }
    
    return guard;
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

