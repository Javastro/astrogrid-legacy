package org.astrogrid.security;

import javax.security.auth.x500.X500Principal;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.Init;
import org.w3c.dom.Document;
import org.astrogrid.security.wsse.WsseSignature;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPHeaderElement;
import org.globus.gsi.TrustedCertificates;

/**
 * An Axis handler to authenticate callers of a service.
 * <p>
 * The handler checks digital signatures on the SOAP
 * body of the request according to the IVOA SSO profile. If a SOAP
 * header in the WS-Security v1.0 namespace is present, then the handler
 * looks for and validates a digital signature. If the signature is
 * missing or invalid (including those cases where some kind of
 * WS-Security mark-up other than digital signature is present), then the
 * request is rejected: a fault is thrown. If no WS-Security header is
 * present, then the request is accepted; it is up to the implementation of
 * the specific web-service to accept or reject anonymous requests.
 * <p>
 * If the handler runs to completion without throwing a fault, it writes
 * a JAAS Subject to the property org.astrogrid.security.authenticated
 * of the message context. For anonymous requests, this Subject is empty.
 * For requests with a valid signature, the Subject is loaded with an
 * X500Principal giving the authenticated identity and, in the 
 * public-credentials set, the X509Certificate for that identity.
 *
 * @author Guy Rixon
 */
public class AxisServiceCredentialHandler extends BasicHandler {
  
  private static Log log = LogFactory.getLog(AxisServiceCredentialHandler.class);
  
  private static TrustedCertificates trustAnchors;
  
  /**
   * The URI for the XML namespace for WS-Security v1.0.
   */
  protected final String WSSE_1_0_NAMESPACE
      = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
  
  /**
   * Initializes the handler.
   */
  public void init() {
    // Initialize the XML-security library
    Init.init();
    
    // Load the trust anchors used for checking signatures.
    this.loadTrustAnchors();
  }
  
  /**
   * Checks a digital signature and records the results.
   *
   * @param msgContext - message context.
   * @throws AxisFault - if anything goes wrong.
   */
  public void invoke(MessageContext msgContext) {
    AxisServiceSecurityGuard guard = new AxisServiceSecurityGuard();
    try {
      SOAPEnvelope envelope = msgContext.getRequestMessage().getSOAPEnvelope();
      SOAPHeaderElement header = envelope.getHeaderByName(WSSE_1_0_NAMESPACE,
                                                          "Security",
                                                          true);
      if (header != null) {
        Document document = envelope.getAsDocument();
        WsseSignature signature = new WsseSignature(document, this.trustAnchors);
        signature.verify();
        header.setProcessed(true);
        guard = signature.getServiceGuard();
        X500Principal p = guard.getX500Principal();
        log.info("Caller is authenticated as " + p + " by digital signature.");
      }
      else {
        log.info("Caller is anonymous.");
      }
    }
    catch (Exception e) {
      log.info("The digital-signature-checking handler failed: " + e);
    }
    finally {
      msgContext.setProperty("org.astrogrid.security.guard", guard);
    }
    
  }
  
  /**
   * Loads trust anchors from disc.
   * Trust anchors are expressed as a set of trusted certificates, and
   * conventionally stored as a directory full of .pem files. This method
   * loads all the appropriate files in such a directory. The directory is
   * set as an option (e.g. via a parameter element in a WSDD file) on the
   * handler. If the trust anchors cannot be loaded, this method logs the
   * error but does not throw an exception.
   */
  protected void loadTrustAnchors() {
    String trustAnchorDirectory 
        = System.getProperty("X509_CERT_DIR");
    if (trustAnchorDirectory == null) {
      log.info("No directory was specified from which to load trusted certificates. " +
                "/etc/grid-security/certificates is the default.");
      trustAnchorDirectory = "/etc/grid-security/certificates";
    }
    try {
      this.trustAnchors = TrustedCertificates.load(trustAnchorDirectory);
    }
    catch (Exception e) {
      log.error(e);
    }   
  }
}
