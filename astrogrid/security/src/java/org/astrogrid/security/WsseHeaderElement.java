package org.astrogrid.security;

import java.util.Iterator;
import javax.xml.namespace.QName;
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
   * The URI for a SOAP actor that does authentication checks.
   */
  private static final String authenticatingActor
      = "urn:astrogrid:soap-actors:authenticator";

  /**
   * The URI for the namespace of the WS-Security standard.
   */
  private static final String wsseNamespace
      = "http://schemas.xmlsoap.org/ws/2002/07/secext";

  /**
   * The URI for the namespace for WSSE utility elements.
   */
  private static final String wssuNamespace
      = "http://schemas.xmlsoap.org/ws/2002/07/utility";


  /**
   * A formatter for using ISO8601 timestamps.
   */
  private static Iso8601DateFormat iso8601 = new Iso8601DateFormat();

  /**
   * Returns the qualified name for the header element.
   */
  public static QName getName() {
    return new QName(WsseHeaderElement.wsseNamespace, "Security");
  }


  /**
   * Adds a WSSE header to the SOAP message.
   */
  public static void write (SecurityGuard sg,
                            SOAPMessage sm) throws SOAPException {
    SOAPPart sp = sm.getSOAPPart();
    assert (sp != null);
    SOAPEnvelope envelope = sp.getEnvelope();
    assert (envelope != null);
    SOAPHeader header = envelope.getHeader();
    assert (header != null);

    // Create the outer element.
    Name securityName = envelope.createName("Security",
                                            "wsse",
                                            WsseHeaderElement.wsseNamespace);
    SOAPHeaderElement security = header.addHeaderElement(securityName);
    security.setActor(WsseHeaderElement.authenticatingActor);

    // Add the UsernameToken.
    SOAPElement usernameToken
        = WsseHeaderElement.addChildElement(envelope,
                                            security,
                                            "UsernameToken",
                                            "wsse",
                                            WsseHeaderElement.wsseNamespace);

    // Add the actual username to the token.
    if (sg.getUsername() != null) {
      SOAPElement username
        = WsseHeaderElement.addChildElement(envelope,
                                            usernameToken,
                                            "Username",
                                            "wsse",
                                            WsseHeaderElement.wsseNamespace);
      username.addTextNode(sg.getUsername());
    }

    // If the password is going as a hash, add the hash, the nonce
    // and the timestamp. All these are generated inside the Password object.
    if (sg.getPassword() != null) {
      if (sg.isPasswordHashing()) {
        PasswordEncoding pe = new PasswordEncoding(sg.getPassword());
        SOAPElement password
            = WsseHeaderElement.addChildElement(envelope,
                                                usernameToken,
                                                "Password",
                                                "wsse",
                                                WsseHeaderElement.wsseNamespace);
        password.addTextNode(pe.getEncodedPassword());
        SOAPElement nonce
            = WsseHeaderElement.addChildElement(envelope,
                                                usernameToken,
                                                "Nonce",
                                                "wsse",
                                                WsseHeaderElement.wsseNamespace);
        nonce.addTextNode(pe.getNonce());
        SOAPElement created
            = WsseHeaderElement.addChildElement(envelope,
                                                usernameToken,
                                                "Created",
                                                "wssu",
                                                WsseHeaderElement.wssuNamespace);
        created.addTextNode(pe.getTimestamp());
        WsseHeaderElement.addAttribute(envelope,
                                       password,
                                       "Type",
                                       "wsse",
                                       WsseHeaderElement.wsseNamespace,
                                       "wsse:PasswordDigest");
      }

      // If the password is going as plain text, add it directly.
      else {
        SOAPElement password
           = WsseHeaderElement.addChildElement(envelope,
                                               usernameToken,
                                               "Password",
                                               "wsse",
                                               WsseHeaderElement.wsseNamespace);
        password.addTextNode(sg.getPassword());
      }
    }

  }


  /**
   * Parses credentials from the SOAP message.
   */
  public static void parse (SOAPMessage   sm,
                            SecurityGuard sg) throws SOAPException {

    // Find the header unit.
    SOAPPart sp = sm.getSOAPPart();
    assert(sp != null);
    SOAPEnvelope se = sp.getEnvelope();
    assert(se != null);
    SOAPHeader sh = se.getHeader();
    assert(sh != null);

    // @TODO: Find the header elements at the first level of the header.
    Iterator i
        = sh.examineHeaderElements(WsseHeaderElement.authenticatingActor);
    SOAPHeaderElement he = null;
    while (i.hasNext() && he == null) {
      Object o = i.next();
      assert(o instanceof SOAPHeaderElement);
      he = (SOAPHeaderElement) o;
      QName q = new QName(he.getElementName().getURI(),
                          he.getElementName().getLocalName());
      if (!WsseHeaderElement.getName().equals(q)) {
        he = null;
      }
      assert(he != null);
    }

    // At this point, we have a header element of the right type.
    System.out.println("Found /Security");

    SOAPElement usernameToken
        = WsseHeaderElement.getChildElement(se,
                                            he,
                                            "UsernameToken",
                                            "wsse",
                                            WsseHeaderElement.wsseNamespace);

    if (usernameToken != null) {
      System.out.println("Found /Security/UsernameToken");
      SOAPElement username
          = WsseHeaderElement.getChildElement(se,
                                              usernameToken,
                                              "Username",
                                              "wsse",
                                              WsseHeaderElement.wsseNamespace);
      sg.setUsername(username.getValue());
      SOAPElement password
          = WsseHeaderElement.getChildElement(se,
                                              usernameToken,
                                              "Password",
                                              "wsse",
                                              WsseHeaderElement.wsseNamespace);
      sg.setPassword(password.getValue());
      if (password != null) {
        String passwordType
           =  WsseHeaderElement.getAttribute(se,
                                             password,
                                             "Type",
                                             "wsse",
                                             WsseHeaderElement.wsseNamespace);
        if (passwordType != null && passwordType.equals("wsse:PasswordDigest")) {
          sg.setPasswordHashing(true);
        }
      }
    }

  }


  /**
   * Locates a child SOAPElement within the current SOAPElement.
   */
  private static SOAPElement getChildElement (SOAPEnvelope envelope,
                                              SOAPElement  parent,
                                              String       child,
                                              String       prefix,
                                              String       uri) throws SOAPException {
    Name name = envelope.createName(child, prefix, uri);
    Iterator i = parent.getChildElements(name);
    while (i.hasNext()) {
      SOAPElement se = (SOAPElement) i.next();
      return se;
    }
    return null;
  }


  /**
   * Gets an attribute off an existing element.
   * The SOAP envelope is used to derive the Name of the attribute.
   *
   * @param envelope SOAP envelope which generates the QNames
   * @param parent existing element to bear the attribute
   * @param name local name of the attribute
   * @param prefix namespace prefix for the attribute
   * @param uri namespace URI for the attribute
   * @param value the value of the attribute
   *
   * @throws SOAPException if there was an error in constructing the
   * QName for the child or in constructing the child element
   */
  private static String getAttribute (SOAPEnvelope envelope,
                                      SOAPElement  parent,
                                      String       name,
                                      String       prefix,
                                      String       uri)
      throws SOAPException {
    assert(envelope != null);
    assert(parent != null);
    assert(name != null);
    assert(prefix != null);
    assert(uri != null);
    Name qName = envelope.createName(name, prefix, uri);
    return parent.getAttributeValue(qName);
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
  private static SOAPElement addChildElement (SOAPEnvelope envelope,
                                              SOAPElement  parent,
                                              String       child,
                                              String       prefix,
                                              String       uri)
      throws SOAPException {
    assert(envelope != null);
    assert(parent != null);
    assert(child != null);
    assert(prefix != null);
    assert(uri != null);
    Name name = envelope.createName(child, prefix, uri);
    return parent.addChildElement(name);
  }


  /**
   * Adds an attribute to an existing element.
   * The SOAP element is used to derive the Name of the attribute.
   *
   * @param envelope SOAP envelope which generates the QNames
   * @param parent existing element to bear the attribute
   * @param name local name of the attribute
   * @param prefix namespace prefix for the attribute
   * @param uri namespace URI for the attribute
   * @param value the value of the attribute
   *
   * @throws SOAPException if there was an error in constructing the
   * QName for the child or in constructing the child element
   */
  private static void addAttribute (SOAPEnvelope envelope,
                                    SOAPElement  parent,
                                    String       name,
                                    String       prefix,
                                    String       uri,
                                    String       value)
      throws SOAPException {
    assert(envelope != null);
    assert(parent != null);
    assert(name != null);
    assert(prefix != null);
    assert(uri != null);
    assert(value != null);
    Name qName = envelope.createName(name, prefix, uri);
    parent.addAttribute(qName, value);
  }

}