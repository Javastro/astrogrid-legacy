package org.astrogrid.ogsa.echo;

import javax.xml.rpc.Stub;
import org.globus.axis.gsi.GSIConstants;
import org.globus.ogsa.impl.security.authentication.Constants;
import org.globus.ogsa.impl.security.authorization.NoAuthorization;

/**
 * A collection of static methods that configure a port object
 * (i.e. an object built on a client stub for a grid service)
 * to use GT3 message security.
 */
public class SecurityHelper {
  
  /**
   * Configures a port stub to use GSI message security
   * with XML signatures.
   */
  static public void configureSecureCall (Object port) {
    Stub stub = (Stub)port;
    stub._setProperty(Constants.MSG_SEC_TYPE,
                      Constants.SIGNATURE);
    stub._setProperty(Constants.AUTHORIZATION,
                      NoAuthorization.getInstance());
  }


  /**
   * Configures a port stub to use "delegation by impersonation"
   * in its limited form. Ports configured with this method must
   * also be configured with {@link #configureSecureCall}.
   */
  static public void configureDelegation (Object port) {
    Stub stub = (Stub)port;
    stub._setProperty(GSIConstants.GSI_MODE,
                      GSIConstants.GSI_MODE_LIMITED_DELEG);
  }

}
