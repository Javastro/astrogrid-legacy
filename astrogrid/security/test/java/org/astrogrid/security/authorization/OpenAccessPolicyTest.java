package org.astrogrid.security.authorization;

import java.util.HashMap;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;
import org.astrogrid.security.SecurityGuard;

/**
 * JUnit test for OpenAccessPolicy.
 *
 * @author Guy Rixon
 */
public class OpenAccessPolicyTest extends TestCase {
  
  public void testNoCredentials() throws Exception {
    AccessPolicy sut = new OpenAccessPolicy();
    SecurityGuard g = new SecurityGuard();
    sut.decide(g, new HashMap());
  }
  
  public void testGoodCredentials() throws Exception {
    AccessPolicy sut = new OpenAccessPolicy();
    SecurityGuard g = new SecurityGuard();
    X500Principal p = new X500Principal("O=foo,OU=bar,CN=baz");
    g.setX500Principal(p);
    sut.decide(g, new HashMap());
  }
  
}
