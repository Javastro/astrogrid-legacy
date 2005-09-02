package org.astrogrid.security;

import java.security.Principal;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.Subject;
import org.apache.axis.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.handler.WSHandlerResult;


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
 * This class communications with the security handlers in
 * WSS4J. It is entirely dependent on WSS4J and on Axis 1.2
 * or later.
 *
 * @author Guy Rixon
 */
public class AxisServiceSecurityGuard extends SecurityGuard {

  static Log log = LogFactory.getLog(AxisServiceSecurityGuard.class.getName());

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

    log.debug("AxisServiceSecurityGuard.getInstanceFromContext(): enter.");

    // Get the authentication results from the current message context.
    MessageContext msgContext = MessageContext.getCurrentContext();
    Vector results = (msgContext == null)? null :
        (Vector) msgContext.getProperty(WSHandlerConstants.RECV_RESULTS);

    // Inspect and log the results.
    // Construct the Subject as part of the inspection: this avoids fetching the results twice.
    Subject s = new Subject();
    if (results != null) {
      log.debug("AxisServiceSecurityGuard.getInstanceFromContext(): Number of results: " + results.size());
      for (int i = 0; i < results.size(); i++) {
        WSHandlerResult hResult = (WSHandlerResult)results.get(i);
        Vector hResults = hResult.getResults();
        for (int j = 0; j < hResults.size(); j++) {
          WSSecurityEngineResult eResult = (WSSecurityEngineResult) hResults.get(j);
          Principal p = eResult.getPrincipal();
          log.info("ServiceSecurityGuard.getInstanceFromContext(): Authenticated Principal: " +
                   p.getName());
          s.getPrincipals().add(p);
        }
      }
    }

    // Return a SecurityGuard carrying the derived Subject.
    return new AxisServiceSecurityGuard(s);
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

