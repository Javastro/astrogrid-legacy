package org.astrogrid.security;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import junit.framework.TestCase;
import org.apache.ws.security.WSPasswordCallback;


/**
 * JUnit tests for {@link org.astrogrid.security.PasswordCallback}.
 *
 * @author Guy Rixon
 */
public class PasswordCallbackTest extends TestCase {

  /**
   * Tests that the callback handler returns the given password.
   */
  public void testGivenPassword() throws Exception {
    PasswordCallback p = new PasswordCallback("secret");
    WSPasswordCallback c = new WSPasswordCallback("q", WSPasswordCallback.SIGNATURE);
    Callback[] a = {c};
    p.handle(a);
    assertEquals("match given password", c.getPassword(), "secret");
  }

  /**
   * Tests that the callback handler returns the default password
   * if no passwords is set.
   */
  public void testDefaultPassword() throws Exception {
    PasswordCallback p = new PasswordCallback();
    WSPasswordCallback c = new WSPasswordCallback("q", WSPasswordCallback.SIGNATURE);
    Callback[] a = {c};
    p.handle(a);
    assertEquals("match default password", c.getPassword(), "unknown");
  }

  /**
   * Tests that the handler rejects improper callbacks.
   */
  public void testImproperCallback() throws Exception {
    PasswordCallback p = new PasswordCallback();
    NameCallback c = new NameCallback("qqq");
    Callback[] a = {c};
    try {
      p.handle(a);
      fail("The SUT accepted an improper callback.");
    }
    catch(UnsupportedCallbackException e1) {
      // This is expected.
    }
    catch(Exception e) {
      fail("Unexpected type of exception.");
    }
  }

}