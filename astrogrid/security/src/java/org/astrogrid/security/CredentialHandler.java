package org.astrogrid.security;

import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.SOAPMessage;


/**
 * A JAX-RPC handler for security credentials.
 *
 * This is a generic class that defines things common
 * to client-side and server-side handers.
 *
 * @author Guy Rixon
 */
public abstract class CredentialHandler extends GenericHandler {

  /**
   * The object through which the security parameters can be got.
   */
  protected SecurityGuard guard;


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
    return new QName[] {WsseHeaderElement.getName()};
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
  protected SOAPMessage getMessage (MessageContext mc) throws JAXRPCException {
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
