package org.astrogrid.community.server.policy.manager ;

import junit.framework.TestCase;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.security.SecurityGuard;

/**
 * JUnit-3 tests for {@link VOSpace}.
 *
 * @author Guy Rixon
 */
public class VOSpaceTest extends TestCase {

  public void testAllocateVoSpace() throws Exception {
    new CommunityConfiguration().setVoSpaceIvorn("vos://pond!vospace/");
    VOSpace sut = new VOSpace(new MockNodeDelegate());
    SecurityGuard sg = new SecurityGuard();
    String space = sut.allocateSpace("ivo://frog@pond/community", "frog", sg);
    assertNotNull(space);
  }

  public void testAllocateMySpace() throws Exception {
    new CommunityConfiguration().setVoSpaceIvorn("ivo://pond/vospace");
    VOSpace sut = new VOSpace(new MockVoSpaceSystemDelegate(),
                              new CredentialResolver());
    String space = sut.allocateSpace("ivo://frog@pond/community", "frog", new SecurityGuard());
    assertNotNull(space);
  }
    

}

