package org.astrogrid.security;

import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.JAXRPCException;
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
public class ClientCredentialHandler extends CredentialHandler {

  /**
   * Adds security headers to an outgoing message.
   *
   * @param mc the context containing the message to which the
   *          SOAP header-block is added.
   *
   * @return true, to allow further processing of the message.
   *         This handler never returns false to stop processing.
   *
   * @throws JAXRPCException if the message context is inappropriate
   *         JAXRPCException if the construction of the header fails
   */
  public boolean handleRequest (MessageContext mc) throws JAXRPCException {
    System.out.println("Entering ClientCredentialHandler.handleRequest()");
    SOAPMessage sm = this.getMessage(mc);
    assert (this.guard != null);

    // Write a WS-Security header.
    try {
      WsseHeaderElement.write(this.guard, sm);
      System.out.println(sm.getSOAPPart().getEnvelope());
    }
    catch (Exception e1) {
      throw new JAXRPCException("Failed to write a WS-Security header", e1);
    }

    return true;
  }

}
