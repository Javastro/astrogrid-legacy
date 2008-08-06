package org.astrogrid.security.community ;

import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.registry.RegistryException;

/**
 * JUnit tests for the CommunityEndpointResolver.
 */
public class RegistryClientTest extends TestCase {
  
  /**
   * Specifies a mockery of the registry for testing.
   */
  public void setUp() {
    SimpleConfig.getSingleton().setProperty(
      "org.astrogrid.security.community.RegistryClient.mock",
      "true"
    );
  }

  /**
   * Test that we can resolve a service in the community.
   *
   */
  public void testResolveKnown() throws Exception {
    RegistryClient sut = new RegistryClient(new MockRegistry());
    String accounts = sut.getEndpointByIdentifier("ivo://org.astrogrid.new-registry/community",
                                                  "ivo://org.astrogrid/std/Community/accounts");
    assertNotNull(accounts);
    String myProxy = sut.getEndpointByIdentifier("ivo://org.astrogrid.new-registry/community",
                                                 StandardIds.MY_PROXY_2);
    assertNotNull(myProxy);
  }

  /**
   * Tests the resolution of an unregistered resource.
   * This is particularly important because the registry delegate
   * returns a null endpoint which must be trapped - see BZ2521.
   */
  public void testResolveUnknown() throws Exception {
    RegistryClient sut = new RegistryClient(new MockRegistry());
    try {
      sut.getEndpointByIdentifier("ivo://bogus/not-there",
                                  "ivo://org.astrogrid/std/Community/accounts");
      fail("Should have thrown a RegistryException.");
    }
    catch (RegistryException e) {
      // Expected.
    }
    try {
      sut.getEndpointByIdentifier("ivo://bogus/not-there",
                                  StandardIds.MY_PROXY_2);
      fail("Should have thrown a RegistryException.");
    }
    catch (RegistryException e) {
      // Expected.
    }
  }
  
}
