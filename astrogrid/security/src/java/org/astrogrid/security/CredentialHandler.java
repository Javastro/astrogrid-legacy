package org.astrogrid.security;

import java.util.Map;
import javax.security.auth.Subject;
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
  protected Subject subject;


  /**
   * Recovers the JAAS Subject resource from the configuration.
   *
   * @param i configuration data for the handler, including the Subject.
   * @throws JAXRPCException if the configuration is invalid
   */
  public final void init (final HandlerInfo i) {
    Map m = i.getHandlerConfig();
    Object s = m.get("Subject");
    assert(s != null);
    assert(Subject.class.isInstance(s));
    this.subject = (Subject)s;
  }

  /**
   * Lists the header blocks processed by this handler.
   * Only one type is handled: the identity header.
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
  protected final SOAPMessage getMessage (final MessageContext mc) {
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
                                "the handler was invoked when there was " +
                                "no message to handle.");
    }
    return sm;
  }

}
