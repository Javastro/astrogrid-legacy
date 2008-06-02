package org.astrogrid.security.delegation;

import java.io.IOException;
import java.security.Security;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.security.delegation.*;

/**
 * A servlet to handle the tree of web resources representing delegated
 * identities and credentials. All the real work is done by external objects
 * that are from sub-classes of ResourceProcessor. This servlet just works out
 * which processor to use.
 *
 * @author Guy Rixon
 */
public class DelegationServlet extends HttpServlet {
  
  public void init() {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }
  
  /** 
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void service(HttpServletRequest  request, 
                         HttpServletResponse response) throws IOException {
    DelegationUri path = new DelegationUri(request.getPathInfo());
    getProcessor(path).service(request, path, response);
  }
  
  /**
   * Dispenses the correct kind of processor for the requested resource.
   */
  private ResourceProcessor getProcessor(DelegationUri path) {
    switch (path.getResourceCode()) {
      case DelegationUri.LIST:        return new DelegationListProcessor();
      case DelegationUri.IDENTITY:    return new IdentityProcessor();
      case DelegationUri.CSR:         return new CsrProcessor();
      case DelegationUri.CERTIFICATE: return new CertificateProcessor();
      default:                        return new ResourceProcessor();
    }
  }
  
}
