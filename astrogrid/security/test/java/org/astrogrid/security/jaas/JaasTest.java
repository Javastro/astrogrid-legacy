package org.astrogrid.security.jaas;

import java.util.Set;
import junit.framework.TestCase;
import javax.security.auth.Subject;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.resolver.security.service.SecurityServiceResolver;
import org.astrogrid.security.AccountName;
import org.astrogrid.security.NonceToken;
import org.astrogrid.store.Ivorn;


public class JaasTest extends TestCase {

  /**
   * Tests that JAAS will accept the configuration for
   * the OneTimePasswordCheck module. The test is
   * passed if JAAS throws no exceptions.
   */
  public void testConfiguration () throws Exception {
    Configuration.setConfiguration(new SimpleLoginConfiguration());
  }

  public void testLoginWithValidCredentials () throws Exception {

    Configuration.setConfiguration(new SimpleLoginConfiguration());

    // Set up the credentials for the test. Get the initial token from
    // the community delegate to ensure that the token is valid.
    String account = "ivo://org.astrogrid.mock/Fred";
    Ivorn ivorn = new Ivorn(account);
    SecurityServiceResolver ssr = new SecurityServiceResolver();
    SecurityServiceDelegate ssd = ssr.resolve(ivorn);
    NonceToken token = new NonceToken(ssd.checkPassword(account, "secret"));
    System.out.println(token.isValid());
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
    assertEquals("Account name matches", account, name);
  }


  public void testLoginWithInvalidCredentials () throws Exception {

    Configuration.setConfiguration(new SimpleLoginConfiguration());

    // Set up the credentials for the test.
    NonceToken token = new NonceToken("bogus", "nbg");
    Subject subject = new Subject();
    subject.getPrivateCredentials().add(token);

    // Test logging in. This test is passed if no exception
    // is thrown. The "qqq" is the name of the service requiring
    // authentication; this is arbitrary when SimpleLoginConfiguration
    // is used.
    try {
      LoginContext lc = new LoginContext("qqq", subject);
      lc.login();
      fail("SUT failed to throw an exception for invalid credentials.");
    }
    catch (LoginException e1) {
      System.out.println("LoginException, as expected: " + e1);
    }
    catch (Exception e2) {
      fail("SUT threw the wrong kind of exception: " + e2);
    }
  }

}