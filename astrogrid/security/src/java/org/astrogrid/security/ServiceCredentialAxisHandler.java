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
      throw new AxisFault("Failed to parse credentials in a request message",
                          e);
    }
  }

}