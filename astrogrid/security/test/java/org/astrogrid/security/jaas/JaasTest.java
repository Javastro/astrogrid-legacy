package org.astrogrid.security.jaas;

import java.util.Set;
import junit.framework.TestCase;
import javax.security.auth.Subject;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate;
import org.astrogrid.security.AccountName;
import org.astrogrid.security.NonceToken;


public class JaasTest extends TestCase {

  /**
   * Tests that JAAS will accept the configuration for
   * the OneTimePasswordCheck module. The test is
   * passed if JAAS throws no exceptions.
   */
  public void testConfiguration () throws Exception {
    Configuration.setConfiguration(new SimpleLoginConfiguration());
  }

  public void testLogin () throws Exception {

    Configuration.setConfiguration(new SimpleLoginConfiguration());

    // Set up the credentials for the test. Assume that the mock
    // community delegate underlying the JAAS implementation will
    // authenticate any SecurityToken that is syntactically correct.
    SecurityServiceMockDelegate ssd = new SecurityServiceMockDelegate();
    NonceToken token = new NonceToken(ssd.checkPassword("Fred", "secret"));
    Subject subject = new Subject();
    subject.getPrivateCredentials().add(token);

    // Test logging in. This test is passed if no exception
    // is thrown. The "qqq" is the name of the service requiring
    // authentication; this is arbitrary when SimpleLoginConfiguration
    // is used.
    LoginContext lc = new LoginContext("qqq", subject);
    lc.login();

    // Test that the account name has been extracted from the token.
    Set principals = subject.getPrincipals(AccountName.class);
    assertEquals("Exactly one account name", 1, principals.size());
    String name = ((AccountName) principals.iterator().next()).toString();
    assertEquals("Account name matches", "Fred", name);
  }
}