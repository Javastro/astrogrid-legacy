package org.astrogrid.security;

import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;


/**
 * A JAX-RPC handler for security credentials.
 *
 * This is a client side handler that works on request messages only.
 * Response messages are passed through unchanged.
 *
 * The credentials to be set in the SOAP messages are derived from
 * properties of a {@link SecurityGuard} that is set as a
 * resource in the initialization of this handler.
 *
 * @author Guy Rixon
 */
public class ClientCredentialHandler extends GenericHandler {

  /**
   * The object through which the security parameters can be got.
   */
  private SecurityGuard guard;


  /**
   * Recovers the ClientSecurityGuard resource from the configuration.
   */
  public void init (HandlerInfo i) throws JAXRPCException {
    Map m = i.getHandlerConfig();
    Object g = m.get("SecurityGuard");
    assert(g != null);
    assert(SecurityGuard.class.isInstance(g));
    this.guard = (SecurityGuard)g;
  }

  /**
   * Lists the header blocks processed by this handler.
   * Only one type is handled: the identity header.
   * @TODO find the right QName.
   *
   * @return the list of names of header blocks.
   */
  public QName[] getHeaders () {
    QName[] h = {new QName("http://astrogrid.org","ID")};
    return h;
  }


  /**
   * Adds security headers to an outgoing message.
   *
   * @param c the context containing the message to which the
   *          SOAP header-block is added.
   *
   * @return true, to allow further processing of the message.
   *         This handler never returns false to stop processing.
   *
   * @throws JAXRPCException if the message context is inappropriate
   *         JAXRPCException if the construction of the header fails
   */
  public boolean handleRequest (MessageContext mc) throws JAXRPCException {
    SOAPMessage sm = this.getMessage(mc);
    assert (this.guard != null);

    // Write a WS-Security header.
    try {
      WsseHeaderElement wsse = new WsseHeaderElement(this.guard, sm);
      wsse.write();
    }
    catch (Exception e1) {
      throw new JAXRPCException("Failed to write a WS-Security header", e1);
    }

    return true;
  }


  /**
   * Extracts the SOAP message from the JAX-RPC context.
   * This might fail if this class is used for a service
   * other than a SOAP service.
   *
   * @param mc the message context holding the message
   * @return the message
   * @throws JAXRPC Exception if no SOAP message is available
   */
  private SOAPMessage getMessage (MessageContext mc) throws JAXRPCException {
    SOAPMessage sm = null;
    try {
      SOAPMessageContext smc = (SOAPMessageContext) mc;
      sm = smc.getMessage();
    }
    catch (Exception e) {
      // Downcast failed; i.e. not a SOAP message.
      throw new JAXRPCException("Invalid use of JAX-RPC: " +
                                "the handler was invoked for a MessageContext " +
                                "that is not a SOAPMessage context.");
    }
    if (sm == null) {
      // SOAP context but no message present.
      throw new JAXRPCException("Invalid use of JAX-RPC: " +
                                "the handler was invoked when there was" +
                                "no message to handle.");
    }
    return sm;
  }

}
