package org.astrogrid.integrationtest.security;

import java.net.URL;
import javax.xml.namespace.QName;
import junit.framework.TestCase;
import org.apache.axis.client.Stub;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.ClientSecurityGuard;
import org.astrogrid.security.Password;
import org.astrogrid.security.sample.SamplePortType;
import org.astrogrid.security.sample.SampleImplServiceLocator;


/**
 * JUnit Integration tests for the security facade, the community
 * component and the registry component.
 *
 * The test uses the security-sample service to exercise the
 * authentication system.  The latter works with the registry and
 * community services. The security-sample service, defined
 * in the astrogrid/security/sample Maven-project, must be
 * previously deployed for this test to work.
 *
 * The security-sample web-service provides one operation,
 * "whoAmI()", which returns the callers identity as determined
 * from the credentials in the request message. It supports
 * AstroGrid's authentication system using nonce tokens and
 * the community service as authenticator.
 *
 * In the test, the account is first signed on and then the
 * whoAmI operation is invoked using the credentials received
 * from signing on.
 *
 * These tests require the configuration file
 * config/security.properties to be in the directory where
 * the tests are run. The following properties must be set in the
 * file:
 *
 * org.astrogrid.registry.query.endpoint: the URL for the
 * registry service (used by the community resolver which is
 * called during this test).
 *
 * org.astrogrid.security.sso.username: the name of the (community)
 * account used to sign on to the grid.
 *
 * org.astrogrid.security.sso.password: the password used to sign on
 * to the grid.
 *
 * org.astrogrid.security.sample.endpoint: the URL for the
 * service to be used in the test.
 *
 * @author Guy Rixon
 */
public class WhoAmITest extends TestCase {

  /**
   * Loads the configuration from file.
   */
  public void setUp () throws Exception {

    // Load properties from file.
    // Assume that the the file is in a subdirectory called config
    // of the current directory.  This holds when the tests are run
    // from a Maven project.
    SimpleConfig.load("config/security.properties");
  }


  public void testAuthenticationWithValidCredentials () throws Exception {
    Config config = SimpleConfig.getSingleton();

    String account  = config.getString("org.astrogrid.security.sso.username");
    String password = config.getString("org.astrogrid.security.sso.password");
    String address  = config.getString("org.astrogrid.security.sample.endpoint");

    // Submit the SSO credentials and get grid credentials.
    // The security guard keeps the grid credentials; this
    // application doesn't need to handle them explicitly.
    ClientSecurityGuard sg = new ClientSecurityGuard();
    sg.setUsername(account);
    sg.setPassword(new Password(password, false));
    sg.signOn();

    // Set up the proxy for the security-sample service.
    // Have the security guard add the grid credentials to
    // outgoing messages.
    QName portName = new QName("urn:astrogrid:port-types", "security-sample");
    SampleImplServiceLocator l = new SampleImplServiceLocator();
    sg.mountGuard(l, portName);
    SamplePortType p = l.getSecuritySample(new URL(address));
    Stub s = (Stub)p;
    s.setPortName(portName);

    // Invoke the security-sample service.
    String result = p.whoAmI();
    assertNotNull(result);
    System.out.println("Result of whoAmI() in client: " + result);
    assertEquals(result, account);
  }

}