package org.astrogrid.slinger.sourcetargets;

import java.net.URI;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.SecurityGuard;

/**
 * JUnit-3 tests for {@link VOSpaceSourceTarget}.
 *
 * @author Guy Rixon
 */
public class VOSpaceSourceTargetTest extends TestCase {

  public void testAnonymous() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.registry.query.endpoint",
                                            "http://bogus/fake");
    URI location = new URI("vos://bogus!fake/missing");
    VOSpaceSourceTarget sut =
        new VOSpaceSourceTarget(location, new SecurityGuard());
  }

}
