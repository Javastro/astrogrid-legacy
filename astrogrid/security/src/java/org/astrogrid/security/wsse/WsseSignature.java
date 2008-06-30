package org.astrogrid.security.wsse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.SOAPConstants;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.message.EnvelopeIdResolver;
import org.apache.ws.security.message.token.BinarySecurity;
import org.apache.ws.security.message.token.PKIPathSecurity;
import org.apache.ws.security.message.token.Reference;
import org.apache.ws.security.message.token.SecurityTokenReference;
import org.apache.ws.security.message.token.X509Security;
import org.apache.ws.security.util.WSSecurityUtil;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.algorithms.SignatureAlgorithm;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.AxisServiceSecurityGuard;
import org.astrogrid.security.rfc3820.CertificateChainValidator;
import org.globus.gsi.TrustedCertificates;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import org.apache.ws.security.WSDocInfo;
import org.apache.ws.security.WSDocInfoStore;


/**
 * A WS-Security header containing a digital signature on the body of a 
 * SOAP message. Objects of this class are created by reading a DOM tree of
 * a complete, signed SOAP message, as might be done in a web service that
 * receives the message. The public methods allow the signature to be verified
 * and an authenticated Principal to be extracted.
 *
 * @author Guy Rixon
 */
public class WsseSignature {
  
  /**
   * Creates a new instance of WsseSignature.
   *
   * @param document - the signed SOAP message.
   * @param trustAnchors - the trusted certificates (may be null if the object is only used for signing)
   */
  public WsseSignature(Document document, TrustedCertificates trustAnchors) {
    
    // Remember the SOAP envelope.
    this.message = document;
    
    // Remember the trust anchors.
    this.trustAnchors = trustAnchors;
    
    // Make somewhere to put the authenticated identity and credentials.
    // This will be empty until successful authentication.
    this.authenticated = new AxisServiceSecurityGuard();
    
    // Use default settings for WSS4J
    // @TODO get rid of this.
    this.wssConfig = WSSConfig.getDefaultWSConfig();
    
    // Make a QName for the BinarySecurityTokn in the WSSE namespace of choice.
    // @TODO get rid of this.
    this.binaryToken = new QName(wssConfig.getWsseNS(), WSConstants.BINARY_TOKEN_LN);
    
    // Find and remember the security SOAP header. If there is no header, then
    // this will return null.
    // @TODO refactor away the SOAP constants.
    SOAPConstants sc = WSSecurityUtil.getSOAPConstants(this.message.getDocumentElement());
    this.header = WSSecurityUtil.getSecurityHeader(this.wssConfig, this.message, null, sc);
  }
  
  /**
   * Signs the SOAP message and returns the signed copy.
   *
   * @return - the signed message as a DOM document.
   */
  public Document sign(SecurityGuard guard) throws Exception {
    
    // Extract the certificate chain. This array contains all the
    // certificates that will be sent with the signature but not
    // the associated trust-anchors.
    X509Certificate[] chain = guard.getCertificateChain();
    if (chain.length == 0) {
      throw new Exception("The given credentials do not contain a certificate chain.");
    }
    
    // Extract the secret key from the given Subject.
    PrivateKey privateKey = guard.getPrivateKey();
    if (privateKey == null) {
      throw new Exception("There is no private key in the given credentials.");
    }
    
    // Create the signature structure.
    XMLSignature sig = this.createSigatureElement(this.getSignatureAlgorithm(chain[0]));    
    
    // Create the binary security-token and associate it with the signature.
    // The token carries the data from the certificate chain given in 
    // the user credentials.
    Reference ref = new Reference(wssConfig, this.message);
    String certUri = "CertId-" + chain[0].hashCode();
    ref.setURI("#" + certUri);
    PKIPathSecurity bstToken
        = new PKIPathSecurity(wssConfig, this.message);
    bstToken.setX509Certificates(chain, false, new Uther());
    bstToken.setID(certUri);
    ref.setValueType(bstToken.getValueType());
    SecurityTokenReference secRef = new SecurityTokenReference(wssConfig, this.message);
    String strUri = "STRId-" + secRef.hashCode();
    secRef.setID(strUri);
    secRef.setReference(ref);
    KeyInfo info = sig.getKeyInfo();
    String keyInfoUri = "KeyId-" + info.hashCode();
    info.setId(keyInfoUri);
    info.addUnknownElement(secRef.getElement());

    // Sign the message. This call retains the cryptographic value of the
    // signature inside the signature object.
    sig.sign(privateKey);
    
    // Assemble the security header in the document.
    Element securityHeader = insertSecurityHeader(this.message);
    WSSecurityUtil.prependChildElement(this.message,
                                       securityHeader,
                                       sig.getElement(),
                                       false);
    WSSecurityUtil.prependChildElement(this.message,
                                       securityHeader,
                                       bstToken.getElement(),
                                       false);

    log.debug("Signing complete.");
    return (this.message);
  }
  
  /**
   * Verifies the signature and remembers the authenticated identity.
   * An exception is thrown if the verification fails.
   */
  public void verify() throws Exception {
    if (this.trustAnchors == null) {
      throw new Exception("Signatures cannot be checked because " +
                          "no trust-anchor certificates are loaded.");
    }
    
    // If there's no security header - i.e. if this request message is
    // anonymous - do nothing.
    if (this.header == null) {
      return;
    }

    // Process the security SOAP-header: verify the signature cryptographically
    // and return the certificate chain so that trust in the signing 
    // certificate can later be verified. This code will throw an exception
    // if the signature is invalid.
    log.debug("Checking the signature...");
    X509Certificate[] chain = this.processSecurityHeader(header);
    log.debug("Signature is valid.");
            
    // Validate the chain of trust from the certificate that signed the message 
    // to the trust anchor. This code throws an exception if trust 
    // is not established.
    // @TODO create the validator and TrustedCertificates at time of construction
    // @TODO generalize the trust anchors.
    // @TODO refactor the validator to hold the chain internally.
    log.debug("Checking the certificate chain...");
    CertificateChainValidator validator = 
        new CertificateChainValidator(this.trustAnchors.getCertificates());
    validator.validate(chain);
    log.debug("Certificate chain is valid.");

    // Remember the authenticated identity and its certificates.
    this.authenticated.setCertificateChain(chain);
    this.authenticated.setX500PrincipalFromCertificateChain();
    
  }
  
  /**
   * Retrives the credentials and identities extracted from the message.
   * The important datum is an X500Principal: this is the
   * authenticated identity of the sender of the message. There may be
   * additional data. The returned subject is never null, but it does not
   * contain the X500Principal unless the signature has been successfully
   * verified.
   *
   * @return - the JAAS subject holding the authenticated identity
   */
  public AxisServiceSecurityGuard getServiceGuard() {
    return this.authenticated;
  }
  
  /**
   * Creates a digital-signature element in respect of the SOAP body.
   */
  protected XMLSignature createSigatureElement(SignatureAlgorithm signatureAlgorithm) throws Exception {
    Element canonElem 
        = XMLUtils.createElementInSignatureSpace(this.message,
                                                 Constants._TAG_CANONICALIZATIONMETHOD);
    canonElem.setAttributeNS(null, Constants._ATT_ALGORITHM, canonAlgo);
    XMLSignature sig = new XMLSignature(this.message, null, signatureAlgorithm.getElement(), canonElem);
    sig.addResourceResolver(EnvelopeIdResolver.getInstance(wssConfig));

    // Make a reference to the body of the SOAP message, which is the
    // part to be signed.
    SOAPConstants soapConstants = WSSecurityUtil.getSOAPConstants(this.message.getDocumentElement());
    Element envelope = this.message.getDocumentElement();
    Element body = (Element) WSSecurityUtil.findElement(envelope,
                                                        soapConstants.getBodyQName().getLocalPart(),
                                                        soapConstants.getEnvelopeURI());
    if (body == null) {
      throw new Exception("The message cannot be signed " +
                          "because it has no SOAP body.");
    }
    Transforms transforms = new Transforms(this.message);
    transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
    sig.addDocument("#" + setWsuId(body), transforms);
    
    return sig;
  }
  
  /**
   * Determines the algorithm used for the signature.
   * The algorithm is chosen to match that used to sign the user's
   * certificate. If that algorithm is not supported and exception is
   * thrown.
   */
  protected SignatureAlgorithm getSignatureAlgorithm(X509Certificate certificate) throws Exception {
    // Choose the signature algorithm to match the algorithm used in signing
    // the user's certificate.
    String pubKeyAlgo = certificate.getPublicKey().getAlgorithm();
    String sigAlgo;
    log.debug("automatic sig algo detection: " + pubKeyAlgo);
    if (pubKeyAlgo.equalsIgnoreCase("DSA")) {
      sigAlgo = XMLSignature.ALGO_ID_SIGNATURE_DSA;
    }
    else if (pubKeyAlgo.equalsIgnoreCase("RSA")) {
      sigAlgo = XMLSignature.ALGO_ID_SIGNATURE_RSA;
    }
    else {
      throw new Exception("The signature algorithm in the presented certificate  - " +
                          pubKeyAlgo +
                          " - is not supported.");
    }
    return new SignatureAlgorithm(this.message, sigAlgo);
  }
  
  /**
   * Process only the signature part of the header.
   */
  protected X509Certificate[] processSecurityHeader(Element securityHeader) throws Exception {
    X509Certificate[] chain = null;

    // Find and process the first signature element. 
    // Ignore following signature elements and all other elements.
    NodeList list = securityHeader.getChildNodes();
    int len = list.getLength();
    for (int i = 0; i < len; i++) {
      Node node = list.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element)node;
        QName qn = new QName(element.getNamespaceURI(), element.getLocalName());
        if (qn.equals(SIGNATURE)) {
          chain = this.processSignatureElement(element);
        }
      }
    }
      
    return chain;
  }
            
  protected X509Certificate[] processSignatureElement(Element element) throws Exception {
    log.debug("Found signature element");
      
    // Creating this object validates some of the internal structure of the signature element.
    // Invalid structures cause exceptions to be thrown here.
    XMLSignature signature = new XMLSignature(element, null);
    signature.addResourceResolver(new EnvelopeIdResolver(this.wssConfig, this.message));

    // The signature element must include a ds:keyInfo child that specifies
    // where in the security header lies the public key for verifying the
    // signature. If it's missing then this code can't verify the signature.
    // (There are special modes where the receiver can infer the correct
    // key from a pre-configured set, but they aren't supported here.)
    KeyInfo info = signature.getKeyInfo();
    if (info == null) {
      throw new Exception("The signature in the message does not include " +
                          "a reference to the key for checking the " +
                          "signature; this usage is not supported.");
    }
    
    // Parse the KeyInfo and recover the X.509 certificate-chain in the message.
    X509Certificate[] chain = this.getCredentialsFromMessage(info, element);

    // Check the signature using the certificate at the
    // head of the chain recovered from the security token.
    // This does not validate the certificate itself.
	  if (!signature.checkSignatureValue(chain[0])) {
      throw new Exception("The signature is cryptographically invalid.");
    }
    
    return chain;
  }
  
  
  protected X509Certificate[] getCredentialsFromMessage(KeyInfo info, Element elem) throws Exception {
    X509Certificate[] certs;
    
    Crypto crypto = new Uther();
    
            // Look up the security-token reference and get, from the token,
        // the X.509 certificates.
        Node node = WSSecurityUtil.getDirectChild(info.getElement(),
						SecurityTokenReference.SECURITY_TOKEN_REFERENCE,
						wssConfig.getWsseNS());
			if (node == null) {
				throw new WSSecurityException(
						WSSecurityException.INVALID_SECURITY,
						"unsupportedKeyInfo");
			}
			SecurityTokenReference secRef = new SecurityTokenReference(
					wssConfig, (Element) node);

			int docHash = elem.getOwnerDocument().hashCode();
			/*
			 * Her we get some information about the document that is being
			 * processed, in partucular the crypto implementation, and already
			 * detected BST that may be used later during dereferencing.
			 */
			WSDocInfo wsDocInfo = WSDocInfoStore.lookup(docHash);

			if (secRef.containsReference()) {
				Element token = secRef.getTokenElement(elem.getOwnerDocument(), wsDocInfo);
				if (token.getLocalName().equals(binaryToken.getLocalPart())) {
					certs = getCertificatesTokenReference((Element) token, crypto);
				} 
        else {
					throw new Exception("The signature refers to a token of type " +
                              "{" +
                              token.getNamespaceURI() +
                              "}" +
                              token.getLocalName() +
                              " which is not supported.");
				}	
      } else if (secRef.containsX509IssuerSerial()) {
				certs = secRef.getX509IssuerSerial(crypto);
			} else if (secRef.containsKeyIdentifier()) {
				certs = secRef.getKeyIdentifier(crypto);
			} else {
				throw new WSSecurityException(
						WSSecurityException.INVALID_SECURITY,
						"unsupportedKeyInfo", new Object[] { node.toString() });
			}

    if (certs == null || certs.length == 0 || certs[0] == null) {
      throw new Exception("No certificates were found in the message header.");
    }
    return certs;
  }

  /**
	 * Extracts the certificate(s) from the Binary Security token reference.
	 * <p/>
	 *
	 * @param elem
	 *            The element containing the binary security token. This is
	 *            either X509 certificate(s) or a PKIPath.
	 * @return an array of X509 certificates
	 * @throws WSSecurityException
	 */
    protected X509Certificate[] getCertificatesTokenReference(Element elem,
                                                           Crypto crypto)
            throws WSSecurityException {
        BinarySecurity token = createSecurityToken(elem);
        if (token instanceof PKIPathSecurity) {
            return ((PKIPathSecurity) token).getX509Certificates(false, crypto);
        } else if (token instanceof X509Security) {
            X509Certificate cert = ((X509Security) token).getX509Certificate(crypto);
            X509Certificate[] certs = new X509Certificate[1];
            certs[0] = cert;
            return certs;
        } else {
            throw new WSSecurityException(WSSecurityException.UNSUPPORTED_SECURITY_TOKEN,
                    "unhandledToken", new Object[]{token.getClass().getName()});
        }
    }

    /**
     * Checks the <code>element</code> and creates appropriate binary security object.
     *
     * @param element The XML element that contains either a <code>BinarySecurityToken
     *                </code> or a <code>PKIPath</code> element. Other element types a not
     *                supported
     * @return the BinarySecurity object, either a <code>X509Security</code> or a
     *         <code>PKIPathSecurity</code> object.
     * @throws WSSecurityException
     */
    private BinarySecurity createSecurityToken(Element element) throws WSSecurityException {
        BinarySecurity token = new BinarySecurity(wssConfig, element);
        String type = token.getValueType();
        Class clazz = null;
        if (wssConfig.getProcessNonCompliantMessages() ||
                wssConfig.isBSTValuesPrefixed()) {
            if (type.endsWith(X509Security.X509_V3)) {
                clazz = X509Security.class;
            } else if (type.endsWith(PKIPathSecurity.X509PKI_PATH)) {
                clazz = PKIPathSecurity.class;
            }
        } else {
            if (type.equals(X509Security.getType(wssConfig))) {
                clazz = X509Security.class;
            } else if (type.equals(PKIPathSecurity.getType(wssConfig))) {
                clazz = PKIPathSecurity.class;
            }
        }
        if (clazz == null) {
            throw new WSSecurityException(WSSecurityException.UNSUPPORTED_SECURITY_TOKEN,
                    "unsupportedBinaryTokenType", new Object[]{type});
        }
        try {
            Constructor constructor = clazz.getConstructor(constructorType);
            if (constructor == null) {
                throw new WSSecurityException(WSSecurityException.FAILURE,
                        "invalidConstructor", new Object[]{clazz});
            }
            return (BinarySecurity) constructor.newInstance(new Object[]{wssConfig, element});
        } catch (InvocationTargetException e) {
            Throwable ee = e.getTargetException();
            if (ee instanceof WSSecurityException) {
                throw (WSSecurityException) ee;
            } else {
                throw new WSSecurityException(WSSecurityException.FAILURE, null, null, e);
            }
        } catch (NoSuchMethodException e) {
            throw new WSSecurityException(WSSecurityException.FAILURE, null, null, e);
        } catch (InstantiationException e) {
            throw new WSSecurityException(WSSecurityException.FAILURE, null, null, e);
        } catch (IllegalAccessException e) {
            throw new WSSecurityException(WSSecurityException.FAILURE, null, null, e);
        }
    }
    
   /**
     * Creates a security header and inserts it as child into the SOAP Envelope.
     * <p/>
     * Check if a WS Security header block for an actor is already available
     * in the document. If a header block is found return it, otherwise a new
     * wsse:Security header block is created and the attributes set
     *
     * @param doc A SOAP envelope as <code>Document</code>
     * @return A <code>wsse:Security</code> element
     */
    protected Element insertSecurityHeader(Document doc) {
        SOAPConstants soapConstants =
                WSSecurityUtil.getSOAPConstants(doc.getDocumentElement());
        // lookup a security header block that matches actor
        Element securityHeader =
                WSSecurityUtil.getSecurityHeader(wssConfig, doc, null, soapConstants);
        if (securityHeader == null) { // create if nothing found
            securityHeader =
                    WSSecurityUtil.findWsseSecurityHeaderBlock(wssConfig,
                            doc,
                            doc.getDocumentElement(),
                            null,
                            true);
        }
        return securityHeader;
    }
    
    protected String setWsuId(Element bodyElement) {
        String id = null;
        // try to get a differently qualified Id in case it was created with
        // an older spec namespace
        if (wssConfig.getProcessNonCompliantMessages()) {
            id = WSSecurityUtil.getAttributeValueWSU(bodyElement, "Id", null);
        }
        if (wssConfig.getProcessNonCompliantMessages() ||
                !wssConfig.isTargetIdQualified()) {
            if ((id == null) || (id.length() == 0)) {
                id = bodyElement.getAttribute("Id");
            }
        } else {
            id = bodyElement.getAttributeNS(wssConfig.getWsuNS(), "Id");
        }
        if ((id == null) || (id.length() == 0)) {
            id = "id-" + Integer.toString(bodyElement.hashCode());
            if (wssConfig.isTargetIdQualified()) {
                String prefix =
                        WSSecurityUtil.setNamespace(bodyElement,
                                wssConfig.getWsuNS(),
                                WSConstants.WSU_PREFIX);
                bodyElement.setAttributeNS(wssConfig.getWsuNS(), prefix + ":Id", id);
            } else {
                bodyElement.setAttributeNS(null, "Id", id);
            }
        }
        return id;
    }
  
  /**
   * The DOM tree holding the signed message.
   */
  private Document message;
  
  /**
   * The set of trusted certificates used to check signatures.
   */
  private TrustedCertificates trustAnchors;
  
  /**
   * The WS-Security SOAP header as a DOM fragment.
   */
  private Element header;
  
  /**
   * The credentials extracted from the message.
   */
  private AxisServiceSecurityGuard authenticated;
  
  
  
      /**
     * <code>wsse:BinarySecurityToken</code> as defined by WS Security specification.
     */
    protected QName binaryToken;
    
    /**
     * <code>ds:Signature</code> as defined by XML-signature specification,
     * enhanced by WS Security specification.
     */
    protected static final QName SIGNATURE = new QName(WSConstants.SIG_NS, WSConstants.SIG_LN);
    
    protected String canonAlgo = Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS;
    
    protected WSSConfig wssConfig;
    
    private static final Class[] constructorType = {WSSConfig.class, org.w3c.dom.Element.class};
    
    private static Log log = LogFactory.getLog(WsseSignature.class.getName());
}