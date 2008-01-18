package org.astrogrid.community.resolver;

import junit.framework.TestCase;
import org.astrogrid.store.Ivorn;
import org.globus.myproxy.MyProxy;

/**
 * JUnit tests for CommunityMyProxyResolver.
 *
 * @author Guy Rixon
 */
public class CommunityMyProxyResolverTestCase extends TestCase {
  
  public void testGoodIvorn() throws Exception {
    CommunityMyProxyResolver sut = 
        new CommunityMyProxyResolver(new MockRegistry());
    
    Ivorn ivorn = new Ivorn("ivo://org.astrogrid.new-registry/other-community");
    MyProxy delegate = sut.resolve(ivorn);
    assertNotNull(delegate);
    assertEquals("org.astrogrid", delegate.getHost());
    assertEquals(7512, delegate.getPort());
  }

  
}
