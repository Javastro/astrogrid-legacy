package org.astrogrid.security.delegation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import javax.security.auth.x500.X500Principal;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.rfc3820.ProxyCertificateFactory;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;

/**
 * An HTTP client for the IVOA delegation-protocol.
 *
 * @author Guy Rixon
 */
public class DelegationClient {
  
  private URI endpoint;
  
  /**
   * The holder for the caller's credentials.
   */
  private SecurityGuard guard;
  
  /**
   * Constructs a DelegationClient.
   */
  public DelegationClient(URI endpoint, SecurityGuard guard) {
    this.endpoint = endpoint;
    this.guard = guard;
  }
  
  /**
   * Delegate a given identity.
   *
   * @throws MalformedURLException If the service endpoint is not a valid URL.
   */
  public void delegate() 
      throws MalformedURLException, IOException, GeneralSecurityException, URISyntaxException {
    
    X509Certificate x509 = guard.getIdentityCertificate();
    if (x509 == null) {
      throw new AccessControlException("No identity certificate was provided.");
    }
    String subject 
        = x509.getSubjectX500Principal().getName(X500Principal.CANONICAL);
    
    WebResource root = new WebResource(this.endpoint, this.guard);
    
    // Connect to the delegations list for posting a new delegation.
    HashMap params = new HashMap();
    params.put("DN", subject);
    root.post(params);
    if (root.getResponseCode() != 201) {
      throw new IOException("Server returned HTTP code " + 
                            root.getResponseCode() + 
                            " when asked to register a new identity." );
    }
    
    // Get the URI for the created resource.
    WebResource identity = root.getRedirectionWebResource();
    System.out.println(identity.getUri());
    
    // Get the CSR.
    WebResource csr = identity.getSubordinateWebResource("CSR");
    csr.get();
    if (csr.getResponseCode() != 200) {
      throw new IOException("Server returned HTTP code " + 
                            csr.getResponseCode() + 
                            " when asked for the CSR." );
    }
    
    // Make a RFC3820 proxy from the CSR.
    ProxyCertificateFactory f = new ProxyCertificateFactory();
    PEMReader p = new PEMReader(new InputStreamReader(csr.getInputStream()));
    PKCS10CertificationRequest c = (PKCS10CertificationRequest) p.readObject();
    X509Certificate proxy = f.createProxyCertificate(this.guard.getCertificateChain()[0],
                                                     this.guard.getPrivateKey(),
                                                     c.getPublicKey(),
                                                     36000,
                                                     false);
    
    // Put the proxy into the service.
    WebResource certificate = identity.getSubordinateWebResource("certificate");
    PEMWriter pem = new PEMWriter(new OutputStreamWriter(certificate.put()));
    pem.writeObject(proxy);
    pem.close();
    if (certificate.getResponseCode() != 200) {
      throw new IOException("Server returned HTTP code " + 
                            certificate.getResponseCode() + 
                            " when given the certificate." );
    }
  }
  
  
}
