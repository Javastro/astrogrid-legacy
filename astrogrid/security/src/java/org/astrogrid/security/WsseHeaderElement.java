package org.astrogrid.security;

import java.util.Date;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * A SOAP-header element matching the WS-Security specification of OASIS.
 *
 * An object of this class associates credentials, carried in a
 * {@link SecurityGuard} object, with a single SOAP message. The
 * guard and message are set during construction. This class can
 * write headers to the message or parse credentials from the message.
 *
 * This class uses SAAJ to access the message header but does not
 * use JAX-RPC.
 *
 * @author Guy Rixon
 */
public class WsseHeaderElement {

  /**
   * A facade object connecting with the calling application.
   * This is a way to get credentials in and out.
   */
  private SecurityGuard guard;

  /**
   * The SOAP message to which this header applies.
   */
  private SOAPMessage message;


  /**
   * The URI for the namespace of the WS-Security standard.
   */
  private String WsseNamespace
      = "http://schemas.xmlsoap.org/ws/2002/04/secext";

  /**
   * A formatter for using ISO8601 timestamps.
   */
  private Iso8601DateFormat iso8601 = new Iso8601DateFormat();


  /**
   * Adds a WSSE header to a SOAP message.
   *
   * @param sg the facade object that interfaces to the application
   * @param sm the SAAJ form of the message
   */
  public WsseHeaderElement (SecurityGuard sg, SOAPMessage sm) {
    this.guard = sg;
    this.message = sm;
  }


  /**
   * Adds a WSSE header to the SOAP message.
   */
  public void write () throws SOAPException {
    SOAPPart     sp = this.message.getSOAPPart();
    assert (sp != null);
    SOAPEnvelope se = sp.getEnvelope();
    assert (se != null);
    SOAPHeader   sh = se.getHeader();

    // Create the outer element.
    Name securityName = se.createName("Security", "wsse", this.WsseNamespace);
    SOAPHeaderElement security = sh.addHeaderElement(securityName);

    // Add the UsernameToken.
    SOAPElement usernameToken = this.addChildElement(se,
                                                     security,
                                                     "UsernameToken",
                                                     "wsse",
                                                     this.WsseNamespace);

    // Add a Nonce, generated randomly here.
    String nonceValue = "123456789"; // @TODO: generate a proper nonce.
    SOAPElement nonce = this.addChildElement(se,
                                             usernameToken,
                                             "Nonce",
                                             "wsse",
                                             this.WsseNamespace);
    nonce.addTextNode(nonceValue);

    // Add a creation timestamp.
    String createdValue = iso8601.format(new Date());
    SOAPElement created = this.addChildElement(se,
                                               usernameToken,
                                               "Created",
                                               "wsse",
                                               this.WsseNamespace);
    created.addTextNode(createdValue);

    // Add the actual username to the token.
    String usernameValue = this.guard.getUsername();
    if (usernameValue != null) {
      SOAPElement username = this.addChildElement(se,
                                                  usernameToken,
                                                  "Username",
                                                  "wsse",
                                                  this.WsseNamespace);
      username.addTextNode(usernameValue);
    }

    // Add the password to the token.  If password hashing is turned on,
    // concatenate the password with the nonce and timestamp and hash the
    // result.
    String passwordValue = this.guard.getPassword();
    if (passwordValue != null) {
     SOAPElement password = this.addChildElement(se,
                                                  usernameToken,
                                                  "Password",
                                                  "wsse",
                                                  this.WsseNamespace);
     if (this.guard.isPasswordHashing()) {
       // @TODO: add hashing algorithm.
       // @TODO: add type attribute to password element.
     }
     password.addTextNode(passwordValue);
    }

  }


  /**
   * Parses credentials from the SOAP message.
   */
  public void parse () {
    // @TODO write a parser.
  }


  /**
   * Adds a child element inside a SOAP header.
   * This cannot be used to add children at the top level of
   * a header as it returns SOAPElement instead of SOAPHeaderElement.
   *
   * @param envelope SOAP envelope which generates the QNames
   * @param parent existing element to bear the child
   * @param child local name of element to be added
   * @param prefix namespace prefix for the child element
   * @param uri namespace URI for the child element
   *
   * @return the child element
   *
   * @throws SOAPException if there was an error in constructing the
   * QName for the child or in constructing the child element
   */
  private SOAPElement addChildElement (SOAPEnvelope envelope,
                                       SOAPElement  parent,
                                       String       child,
                                       String       prefix,
                                       String       uri) throws SOAPException {
    assert(envelope != null);
    assert(parent != null);
    assert(child != null);
    assert(prefix != null);
    assert(uri != null);
    Name name = envelope.createName(child, prefix, uri);
    return parent.addChildElement(name);
  }

}