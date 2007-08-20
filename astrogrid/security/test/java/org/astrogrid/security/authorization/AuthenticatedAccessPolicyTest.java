package org.astrogrid.security.authorization;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;
import org.astrogrid.security.SecurityGuard;

/**
 * JUnit test for AuthenticatedAccessPolicyTest.
 * 
 * @author Guy Rixon
 */
public class AuthenticatedAccessPolicyTest extends TestCase {
  
  public void testNoCredentials() throws Exception {
    AccessPolicy sut = new OpenAccessPolicy();
    SecurityGuard g = new SecurityGuard();
    try {
      sut.decide(g, new HashMap());
    }
    catch (GeneralSecurityException gse) {
      // expected.
    }
  }
  
  public void testGoodCredentials() throws Exception {
    AccessPolicy sut = new OpenAccessPolicy();
    SecurityGuard g = new SecurityGuard();
    X500Principal p = new X500Principal("O=foo,OU=bar,CN=baz");
    g.setX500Principal(p);
    sut.decide(g, new HashMap());
  }
  
}
