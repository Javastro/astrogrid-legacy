package org.astrogrid.security;

import java.util.Iterator;
import javax.security.auth.Subject;
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
 * properties of a JAAS Subject that is set as a
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
   * @throws JAXRPCException if the construction of the header fails
   */
  public final boolean handleRequest (final MessageContext mc) {
    System.out.println("Entering ClientCredentialHandler.handleRequest()");
    SOAPMessage sm = this.getMessage(mc);
    assert (sm != null);

    // Write a WS-Security header.
    try {
      assert(this.subject != null);
      WsseHeaderElement.write(this.getSubject(), sm);
    }
    catch (Exception e1) {
      System.out.println(e1);
      throw new JAXRPCException("Failed to write a WS-Security header", e1);
    }

    return true;
  }


  /**
   * Extracts a JAAS subject containing the credentials.
   * This method takes care of the use of NonceTokens, which need to
   * be "split" before use in a message; the returned subject is a
   * derivative of the Subject stored in the object, not a reference to
   * that stored Subject.
   */
  private Subject getSubject () {
    System.out.println("Entering ClientCredentialHandler.getSubject()");
    try {

      // Clone the existing, shared Subject.
      Subject subject = new Subject(false,
                                    this.subject.getPrincipals(),
                                    this.subject.getPublicCredentials(),
                                    this.subject.getPrivateCredentials());

      // Find any NonceTokens in the subjects.
      Iterator credentials
          = subject.getPrivateCredentials(NonceToken.class).iterator();
      while (credentials.hasNext()) {
        NonceToken n1 = (NonceToken) credentials.next();
        NonceToken n2 = n1.split();
        subject.getPrivateCredentials().remove(n1);
        subject.getPrivateCredentials().add(n2);
      }

      return subject;
    } catch (Exception e) {
      String message = "Failed to prepare credentials "
                     + "(failed to split a NonceToken)";
      System.out.println(message);
      System.out.println("Cause:");
      System.out.println(e.toString());
      throw new JAXRPCException(message, e);
    }
  }

}
