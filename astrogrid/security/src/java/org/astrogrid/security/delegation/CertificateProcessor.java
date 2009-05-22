package org.astrogrid.security.delegation;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Guy Rixon
 */
public class CertificateProcessor extends ResourceProcessor {
  
  private static Log log = LogFactory.getLog(CertificateProcessor.class);
  
  /**
   * Responds to HTTP requests.
   */
  @Override
  public void service(HttpServletRequest  request,
                      DelegationUri       path,
                      HttpServletResponse response) throws IOException {
    if (request.getMethod().equals("GET")) {
      sendCertificate(path.getUser(), response);
    }
    else if (request.getMethod().equals("PUT")) {
      receiveCertificate(request, path.getUser(), response);
    }
    else {
      response.setHeader("Accept", "GET");
      response.sendError(response.SC_METHOD_NOT_ALLOWED);
    }
  }

  /**
   * Writes to the client the X.509 certificate for an identity.
   */
  private void sendCertificate(String              hashKey, 
                               HttpServletResponse response) throws IOException {
    if (Delegations.getInstance().hasCertificate(hashKey)) {
      response.setContentType("text/plain");
      Delegations.getInstance().writeCertificate(hashKey, response.getWriter());
    }
    else {
      response.sendError(response.SC_NOT_FOUND);
    }
  }
  
  /**
   * Receives an uploaded identity certificate. The certificate becomes the
   * content for they identity's /certificate resource.
   */
  private void receiveCertificate(HttpServletRequest  request,
                                  String              hashKey, 
                                  HttpServletResponse response) throws IOException {
    if (Delegations.getInstance().isKnown(hashKey)) {
      try {
        CertificateFactory factory = CertificateFactory.getInstance("X509");
        X509Certificate certificate = 
            (X509Certificate)factory.generateCertificate(request.getInputStream());
        try {
          Delegations.getInstance().setCertificate(hashKey, certificate);
        } catch (InvalidKeyException ex) {
          throw new RuntimeException(ex);
        }
        log.info("Received a certificate for " + 
                 certificate.getSubjectX500Principal() +
                 " (" + hashKey +").");
      } 
      catch (CertificateException ex) {
        System.out.println(ex);
        response.sendError(response.SC_BAD_REQUEST,
                           "Failed to parse the certificate: " + ex);
      }
    }
    else {
      response.sendError(response.SC_NOT_FOUND);
    }
  }
  
}
