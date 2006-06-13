package org.astrogrid.applications.service.v1.cea;

import java.security.Principal;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.handlers.BasicHandler;
import org.apache.log4j.Logger;
import org.astrogrid.security.AxisServiceSecurityGuard;

/**
 * A handler to check authorization of requests to CEA services.
 * 
 * The checks are minimal: only the init and execute operations are
 * checked (no authorization is required for the others); and any caller
 * who authenticates is considered authorized. Unauthorized requests are
 * rejected by throwing AxisFaults.
 *
 * This handler must be deployed in the request chain to function properly and
 * AxisServiceCredentialHandler must precede it in that chain.
 *
 * @author Guy Rixon
 */
public class AuthorizationHandler extends BasicHandler {
  
  private static Logger log = Logger.getLogger(AuthorizationHandler.class);
  
  /** Creates a new instance of AuthorizationHandler */
  public AuthorizationHandler() {
    super();
  }
  
  /**
   * Handles a request message.
   * Gets name of the web-service operation and calls the
   * authorization check.
   */
  public void invoke(MessageContext msgContext) throws AxisFault {
    
    // Find out which web-service operation has been called.
    // This allows the authorization to discrimination between operations.
    OperationDesc operation = msgContext.getOperation();
    if (operation == null) {
      log.info("Axis cannot tell the web-service operation.");
    }
    else {
      this.checkAuthorization(operation);
    }
  }
  
  /**
   * Checks authorization for an operation based on a signed request.
   * The authenticated identity of the caller is available from the security
   * facade (assuming that this code is used in the intended environment -
   * see the Javadoc comments for the class). Throws an Axis fault if 
   * authorization is denied.
   *
   * The 'init' and 'execute' operations are authorized for any authenticated
   * caller. The other operations are authorized for any caller, whether or
   * not authenticated.
   */
  protected void checkAuthorization(OperationDesc operation) throws AxisFault {
    String name = operation.getName();
    if ("init".equals(name) || "execute".equals(name)) {
      AxisServiceSecurityGuard guard 
          = AxisServiceSecurityGuard.getInstanceFromContext();
      Principal x500 = guard.getX500Principal();
      if (x500 != null) {
        log.info(x500.getName() + " is authorized to use operation '" + name + "'.");
      }
      else {
        String message = "A request for operation '" + name +
                         "' was rejected because the caller was not authenticated.";
        log.info(message);
        throw new AxisFault(message);
      }
    }
    else {
      log.info("Authorization is not required for operation '" + name + "'.");
    }
  }
  
}
