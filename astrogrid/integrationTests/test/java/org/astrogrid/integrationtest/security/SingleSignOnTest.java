package org.astrogrid.integrationtest.security;

import junit.framework.TestCase;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.ClientSecurityGuard;
import org.astrogrid.security.NonceToken;
import org.astrogrid.security.Password;


/**
 * JUnit tests for the integration of the security facade
 * (org.astrogrid.security) with the Community services.
 *
 * These tests need the configuration file
 * config/security.properties in the directory where the
 * test are run.  The file must set the following properties.
 *
 * org.astrogrid.security.sso.username: the account name used to
 * sign on to the grid.
 *
 * org.astrogrid.security.sso.password: the password used to
 * sign on to the grid.
 *
 * org.astrogrid.registry.query.endpoint: the URL for the
 * registry service (used by the community resolver which is
 * called during this test).
 *
 * @author Guy Rixon
 */
public class SingleSignOnTest extends TestCase {

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


  /**
   * Tests signing on to the grid and getting an initial
   * security token.
   */
  public void testWithMockCredentials () throws Exception {

    // These credentials will address a mock delegate.  They
    // won't actually address a commmunity service.
    String username = "ivo://org.astrogrid.mock/Fred";
    String password = "secret";
    String account = this.signOn(username, password);
    assertNotNull("Account name", account);
    assertEquals("Names match", username, account);
  }


  /**
   * Tests signing on to the grid and getting an initial
   * security token.
   */
  public void testWithValidCredentials () throws Exception {

    // These credentials will address a mock delegate.  They
    // won't actually address a commmunity service.
    Config config = SimpleConfig.getSingleton();
    String username = config.getString("org.astrogrid.security.sso.username");
    String password = config.getString("org.astrogrid.security.sso.password");
    String account = this.signOn(username, password);
    assertNotNull("Account name", account);
    assertEquals("Names match", username, account);
  }


  /**
   * Tests signing on to the grid and getting an initial
   * security token.
   */
  public void testWithInvalidCredentials () throws Exception {

    // These credentials will address a real delegate but
    // won't be accepted.
    String username = "ivo://bogus.site/bogus/user";
    String password = "secret";
    try {
      String account = this.signOn(username, password);
      fail("Sign-on succeeded for invalid credentials.");
    }
    catch (Exception e) {
      // Exception is expected; ignore it.
    }
  }


  /**
   * Exercises the sign-on function with given SSO credentials.
   *
   * @param username account name, in IVORN-string format
   * @param password clear-text password known to Community
   * @return the account name from the token sent back by the community
   */
  public String signOn (String username, String password) throws Exception {

    // Record the SSO credentials.
    ClientSecurityGuard sg = new ClientSecurityGuard();
    sg.setUsername(username);
    sg.setPassword(new Password(password, false));

    // Use the SSO credentials to get a security token that can be
    // sent to a service.
    sg.signOn();

    // Check that a token came back.
    NonceToken n = sg.getNonceToken();
    return n.getAccount();
  }

}
