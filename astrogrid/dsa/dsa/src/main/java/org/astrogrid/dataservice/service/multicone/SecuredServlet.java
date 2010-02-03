package org.astrogrid.dataservice.service.multicone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.HttpsServiceSecurityGuard;
import org.astrogrid.security.authorization.AccessPolicy;

/**
 * A servlet that supports IVOA access-control.
 * <p>
 * The access policy is loaded on initialization of the servlet. The policy
 * class is determined by the configuration property cone.search.access.policy.
 *
 * @author Guy Rixon
 */
public class SecuredServlet extends HttpServlet {

  private static final Log log = LogFactory.getLog(SecuredServlet.class);

  /**
   * The access policy for cone searches. Principals resulting from
   * authentication are compared against this policy to test authorization.
   */
  AccessPolicy policy;


  /**
   * Initializes the servlet. Loads the configured access-policy for
   * cone searches.
   *
   * @throws ServletException If the superclass {@code init()} throws.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    policy = getConeSearchAccessPolicy();
  }


  /**
   * Instantiates the access policy. The policy object is re-used for all
   * queries by this servlet.
   */
  protected AccessPolicy getConeSearchAccessPolicy() {
    String policyClassName = SimpleConfig.getProperty("cone.search.access.policy");
    try {
      return (AccessPolicy) Class.forName(policyClassName).newInstance();
    }
    catch (Exception ex) {
      log.fatal(ex);
      throw new RuntimeException("Can't load the access policy for cone search", ex);
    }
  }

  
  /**
   * Obtains the results of authentication and check them against the
   * pre-loaded access-policy. Returns silently if access is allowed.
   *
   * @param request The HTTP request, including the results of authentication.
   * @throws Exception If access is denied.
   */
  protected void checkAuthorization(HttpServletRequest request) throws Exception {
    HttpsServiceSecurityGuard guard = new HttpsServiceSecurityGuard();
    guard.loadHttpsAuthentication(request);
    guard.setAccessPolicy(policy);
    guard.decide(null);
  }

}
