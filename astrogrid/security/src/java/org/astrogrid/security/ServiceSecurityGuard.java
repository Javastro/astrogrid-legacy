package org.astrogrid.security;

import java.util.Set;
import javax.security.auth.Subject;
import org.apache.axis.MessageContext;


/**
 * A SecurityGuard specialized for a web service. Objects of
 * this class are used in the implementation methods of the
 * service to get access to the credentials acquired and tested
 * by the handler chain (q.v. {@link ServiceCredentialHandler}.
 *
 * The public, no-argument constructor creates a
 * ServiceSecurityGuard with no credentials (i.e. with an
 * empty JAAS subject). This is not very useful. A service
 * should normal acquire a ServiceSecurityGuard by calling the
 * static method {@link getInstanceFromContext} which
 * initializes the guard from the JAX-RPC message context.
 *
 * If a client invokes a web-service method with no credentials,
 * then the handler chain for authentication is not triggered.
 * In this case the ServiceSecurityGuard has no credentials
 * in its JAAS Subject. The convenience method
 * {@link isAnonymous} on the ServiceSecurityGuard checks for
 * this condition.
 *
 * If a client calls a service with invalid credentials, then
 * authentication fails in the handler chain. In this case,
 * the service implementation is never called and so the
 * ServiceSecurityGuard does not have methods to deal with
 * the condition.
 *
 * This class uses the Axis implementation of MessageContext
 * to get the current context. It seems to be impossible to get
 * the context of an operation inside a service - i.e. in the
 * implementation of the operation rather than in the handler chain -
 * using only the standard JAX-RPC interfaces.
 *
 * @author Guy Rixon
 */
public class ServiceSecurityGuard extends SecurityGuard {

  /**
   * Constructs a ServiceSecurityGuard with an empty JAAS subject.
   */
  public ServiceSecurityGuard () {
    super();
  }

  /**
   * Constructs a ServiceSecurityGuard with a given JAAS subject.
   */
  public ServiceSecurityGuard (Subject s) {
    super(s);
  }

  /**
   * Constructs a ServiceSecurityGuard and sets its credentials
   * from the message context of the call to the web service.
   */
  public static ServiceSecurityGuard getInstanceFromContext () {
    Subject s
        = (Subject) MessageContext.getCurrentContext().getProperty("Subject");
    if (s == null) {
	  System.out.println("ServiceSecurityGuard: no JAAS subject; access is anonymous");
      return new ServiceSecurityGuard();
    }
    else {
      return new ServiceSecurityGuard(s);
    }
  }


  /**
   * Tests whether the current call to the web service is
   * anonymous. The call is considered anonymous if there
   * are no JAAS principals in the JAAS Subject.
   *
   * @return true if the call is anonymous
   */
  public boolean isAnonymous () {
    Set principals = this.getGridSubject().getPrincipals();
    return (principals.size() == 0);
  }

}
