package org.astrogrid.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A SecurityGuard specialized for a web service using IVOA
 * TLS-with-client-certificate security. I.e., a guard that knows how to get 
 * information out of the HTTPS context.
 *
 * @author Guy Rixon
 */
public class HttpsServiceSecurityGuard extends SecurityGuard {

  static Log log = LogFactory.getLog(HttpsServiceSecurityGuard.class);

  /**
   * Constructs an HttpServiceSecurityGuard with no credentials or principals.
   */
  public HttpsServiceSecurityGuard() {
    super();
  }

  /**
   * Constructs an HttpsServiceSecurityGuard with a given credentials 
   * and principals.
   *
   * @param subject The credentials and principals.
   */
  public HttpsServiceSecurityGuard(Subject subject) {
    super(subject);
  }

  /**
   * Loads credentials and principals authenticated in an HTTPS request.
   *
   * @param request The HTTPS request defining the context.
   */
  public void loadHttpsAuthentication(HttpServletRequest request) 
      throws CertificateException {
    X509Certificate[] chain = 
        (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
    if (chain != null) {
      setCertificateChain(chain);
    }
    if (request.isSecure()) {
      setX500PrincipalFromCertificateChain();
    }
  }

  /**
   * Tests whether the current call to the web service is
   * anonymous. The call is considered anonymous if there
   * are no JAAS principals in the JAAS Subject.
   *
   * @return true if the call is anonymous
   */
  public boolean isAnonymous () {
    return this.getSubject().getPrincipals().size() == 0;
  }

}

