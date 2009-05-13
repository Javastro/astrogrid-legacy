package org.astrogrid.webapp;

import java.io.IOException;
import javax.security.auth.x500.X500Principal;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.config.SimpleConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.security.HttpsServiceSecurityGuard;
import org.astrogrid.security.authorization.AccessPolicy;

/**
 * A filter to apply an access-control policy to HTTP requests.
 * A go/no-go decision is mad concerning each request. If access is
 * denied, the request receives the "forbidden" error-code.
 * <p>
 * A configurable access-policy class is called to make the decision. Set this
 * class by setting the property <i>cea.access.policy</i> in the AstroGrid
 * configuration (i.e. in the configuration read by SimpleConfig; use 
 * <i>astrogrid.properties</i> or JNDI). The policy class must decide based
 * only on the caller's identity. This filter does not pass other details of
 * the request, so the policy class cannot discriminate according to, e.g.,
 * the table to be searched.
 * <p>
 * The caller's identity is obtained from the HTTP request assuming that
 * the identity has been authenticated by HTTPS and client certificates.
 * If authentication was not attempted (e.g. for a plain HTTP request) then
 * the policy class will not find the user's identity. Any powers delegated
 * to the service by the client are also loaded and are visible to the policy 
 * class when a decision is made.
 *
 * @author Guy Rixon
 */
public class AuthorizationFilter implements Filter {
  
  static private Log log = LogFactory.getLog(AuthorizationFilter.class);
  
  /**
   * The configured access-policy.
   */
  private AccessPolicy policy;
  
  /**
   * Loads the access policy. The policy class is read from the
   * AstroGrid configuration.
   */
  public void init(FilterConfig arg0) throws ServletException {
    try {
      String accessPolicyName =
        SimpleConfig.getSingleton().getString("cea.access.policy");
      this.policy =
         (AccessPolicy) Class.forName(accessPolicyName).newInstance();
      log.info("The access policy " + accessPolicyName + " has been loaded.");
    } catch (Exception e) {
      log.error("Failed to load the access policy; " + e);
      throw new ServletException("Failed to load the access policy" + e);
    }
  }

  /**
   * Checks authorization of HTTP requests. The authorization check will go
   * wrong unless the caller has previously been authenticated by HTTPS
   * with client certificates.
   */
  public void doFilter(ServletRequest  request, 
                       ServletResponse response, 
                       FilterChain chain) throws IOException, 
                                                 ServletException {
    try {
      HttpsServiceSecurityGuard guard = new HttpsServiceSecurityGuard();
      guard.setAccessPolicy(this.policy);
        
      // Load the results of previous authentication and delegation.
      HttpServletRequest httpIn = (HttpServletRequest) request;
      guard.loadHttpsAuthentication(httpIn);
      guard.loadDelegation();
        
      // Make the authorization decision. An exception is thrown
      // if access is denied and is caught below.
      guard.decide(null);
      chain.doFilter(request, response);
    }
    catch (Exception e) {
      log.info("Access denied: " + e.getMessage());
      HttpServletResponse httpOut = (HttpServletResponse) response;
      httpOut.sendError(httpOut.SC_FORBIDDEN, e.getMessage());
    }
  }

  public void destroy() {
    // Nothing to destroy.
  }

}
