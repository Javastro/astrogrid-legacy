package org.astrogrid.security;

import javax.security.auth.Subject;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.SOAPMessage;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.astrogrid.security.jaas.SimpleLoginConfiguration;

/**
 * Axis handler for WSSE headers on the server side.
 *
 * The Axis developers have declined to implement the
 * JAX-RPC standard for handlers; their handler format is
 * different. This class wraps a proper, JAX-RPC handler
 * in an Axis wrapper.
 *
 * Since the underlying handler handles only request messages,
 * this one ignores response messages.
 *
 * @author Guy Rixon.
 *
 * @see {@link ServiceCredentialHandler}
 */
public class ServiceCredentialAxisHandler extends BasicHandler {

  /**
   * A JAX-RPC-compliant handler that does the real work.
   */
  private ServiceCredentialHandler realHandler;


  /**
   * Constructs a handler that composes a JAX-RPC handler.
   */
  public ServiceCredentialAxisHandler () {
    super();
    this.realHandler = new ServiceCredentialHandler();
  }


  /**
   * Handles a message, delegating it to the JAX-RPC handler.
   * Only request messages are handled; resonse messages are
   * ignored.
   *
   * @param mc the message context
   * @throws AxisFault if the JAX-RPC handler throws an exception
   */
  public void invoke (MessageContext mc) throws AxisFault {
    System.out.println("Entering ServiceCredentialAxisHandler.invoke()");
    try {

      // Don't handle response messages.
      if (mc.getPastPivot()) {
      }

      // Call the JAX-RPC-compliant handler.
      else {
        this.realHandler.handleRequest(mc);
      }
    }
    catch (Exception e) {
      System.out.println("ServiceCredentialAxisHandler.invoke() caught an exception");
      throw this.makeFault("Failed to parse credentials in a request message",
                           e);
    }
  }


  /**
   * Makes an AxisFault that retains the ful ldetail of the cause.
   * Axis itself retains only the first of a chain of exceptions
   * given as the cause.  This method compacts all the messages in
   * the chain into the message of the exception at its head.
   *
   * @param contextMessage the primary text of the AxisFault
   * @e0 the exception at the head of the chain of causes
   * @return the created fault
   */
  private AxisFault makeFault (String contextMessage, Exception e0) {
    System.out.println(e0.toString());
    StringBuffer causeMessage = new StringBuffer(e0.getMessage());
    Throwable eLast = e0;
    Throwable eNext = e0.getCause();
    while (eNext != null) {
      causeMessage.append("\nCaused by:\n");
      causeMessage.append(eNext.getMessage());
      eLast = eNext;
      eNext = eLast.getCause();
    }
    Throwable e1 = new Throwable(causeMessage.toString());
    return new AxisFault(contextMessage, e1);
  }

}