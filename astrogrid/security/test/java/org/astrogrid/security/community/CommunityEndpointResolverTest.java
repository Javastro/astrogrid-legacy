package org.astrogrid.security.community ;

import java.net.URI;
import java.net.URL;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;

/**
 * JUnit tests for the CommunityEndpointResolver.
 *
 */
public class CommunityEndpointResolverTest extends TestCase {
  
  /**
   * Specifies a mockery of the registry for testing.
   */
  public void setUp() {
    SimpleConfig.getSingleton().setProperty(
      "org.astrogrid.security.community.CommunityEndpointResolver.mock",
      "true"
    );
  }

  /**
   * Test that we can resolve a service in the community.
   *
   */
  public void testResolveKnown() throws Exception {
    CommunityEndpointResolver resolver = 
        new CommunityEndpointResolver("ivo://org.astrogrid.new-registry/community");
    assertNotNull(resolver.getMyProxy());
    assertNotNull(resolver.getAccounts());
  }

  /**
   * Tests the resolution of an unregistered resource.
   * This is particularly important because the registry delegate
   * returns a null endpoint which must be trapped - see BZ2521.
   */
  public void testResolveUnknown() throws Exception {
    CommunityEndpointResolver resolver =
        new CommunityEndpointResolver("ivo://nix.nada.not-there/bogus");
    assertNull(resolver.getMyProxy());
    assertNull(resolver.getAccounts());
  }
  
}
