package org.astrogrid.security;

import javax.security.auth.Subject;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.SOAPMessage;
import org.astrogrid.security.jaas.SimpleLoginConfiguration;

/**
 * JAX-RPC handler for WSSE headers on the server side.
 *
 * This handler parses wsse:Security elements in incoming
 * messages and uses the credentials therein to identify
 * and authenticate the client.
 *
 * In the current implementation, authentication is by
 * single-use passwords which are checked by reference to
 * an external security-service. If credentials matching this
 * pattern are not present then authentication fails; no
 * attempt is made to use other credentials that may be present.
 *
 * @author Guy Rixon.
 */
public class ServiceCredentialHandler extends CredentialHandler {

  /**
   * Recover credentials from a message to a service.
   *
   * @param mc the context containing the message to which the
   *          SOAP header-block is added.
   *
   * @return true, to allow further processing of the message.
   *         This handler never returns false to stop processing.
   *
   * @throws JAXRPCException if the message context is inappropriate
   *         JAXRPCException if the parsing of the header fails
   */
  public boolean handleRequest (MessageContext mc) throws JAXRPCException {
    System.out.println("Entering ServiceCredentialHandler.handleRequest()");
    SOAPMessage sm = this.getMessage(mc);
    Subject s = new Subject();
    try {
      WsseHeaderElement.parse(sm, s);
    }
    catch (Exception e1) {
      throw new JAXRPCException("Failed to parse a WS-Security header", e1);
    }
    try {
      Configuration.setConfiguration(new SimpleLoginConfiguration());
      LoginContext l = new LoginContext("", s);
      l.login();
      System.out.println("Authentication OK, setting Subject property...");
      mc.setProperty("Subject", s);
    }
    catch (Exception e2) {
      throw new JAXRPCException("Authentication failed", e2);
    }

    return true;
  }
}