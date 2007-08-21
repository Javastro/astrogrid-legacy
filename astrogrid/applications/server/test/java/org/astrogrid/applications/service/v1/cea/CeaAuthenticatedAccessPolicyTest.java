package org.astrogrid.applications.service.v1.cea;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;
import org.astrogrid.security.SecurityGuard;

/**
 * JUnit tests for CeaAuthenticatedAccessPolicy.
 *
 * @author Guy Rixon
 */
public class CeaAuthenticatedAccessPolicyTest extends TestCase {
  
  public void testInitAuthenticated() throws Exception {
    CeaAuthenticatedAccessPolicy sut = new CeaAuthenticatedAccessPolicy();
    SecurityGuard sg = new SecurityGuard();
    sg.setX500Principal(new X500Principal("CN=foo"));
    sut.decide(sg, new HashMap());
  }
  
  public void testInitAnonymous() throws Exception {
    CeaAuthenticatedAccessPolicy sut = new CeaAuthenticatedAccessPolicy();
    SecurityGuard sg = new SecurityGuard();
    try {
      sut.decide(sg, new HashMap());
      fail("Should not accept anonymous requests.");
    }
    catch (GeneralSecurityException gse) {
      // Expected.
    }
  }
  
  public void testExecuteAuthenticated() throws Exception {
    CeaAuthenticatedAccessPolicy sut = new CeaAuthenticatedAccessPolicy();
    SecurityGuard sg = new SecurityGuard();
    sg.setX500Principal(new X500Principal("CN=foo"));
    HashMap h = new HashMap();
    h.put("cea.job.owner", new X500Principal("CN=foo"));
    sut.decide(sg, h);
  }
  
  public void testExecuteAnonymous() throws Exception {
    CeaAuthenticatedAccessPolicy sut = new CeaAuthenticatedAccessPolicy();
    SecurityGuard sg = new SecurityGuard();
    HashMap h = new HashMap();
    h.put("cea.job.owner", new X500Principal("CN=foo"));
    try {
      sut.decide(sg, h);
      fail("Should not accept anonymous requests.");
    } catch (GeneralSecurityException ex) {
      // Expected.
    }
  }
  
    public void testExecuteWrongUser() throws Exception {
    CeaAuthenticatedAccessPolicy sut = new CeaAuthenticatedAccessPolicy();
    SecurityGuard sg = new SecurityGuard();
    sg.setX500Principal(new X500Principal("CN=bar"));
    HashMap h = new HashMap();
    h.put("cea.job.owner", new X500Principal("CN=foo"));
    try {
      sut.decide(sg, h);
      fail("Should not accept requests other than from the job owner.");
    } catch (GeneralSecurityException ex) {
      // Expected.
    }
  }
  
}
