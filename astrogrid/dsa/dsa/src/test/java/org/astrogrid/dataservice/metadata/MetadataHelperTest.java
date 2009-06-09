package org.astrogrid.dataservice.metadata;

import junit.framework.TestCase;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.PropertyNotFoundException;

/**
 * JUnit-3 tests for MetdataHelper.
 *
 * @author Guy Rixon
 */
public class MetadataHelperTest extends TestCase {

  public void testInstallationSecureBaseUrl() throws Exception {

    // Try first when the property isn't set.
    try {
      String s = MetadataHelper.getInstallationSecureBaseURL();
      fail("Should throw an exception when secure-base-URL property is not set.");
    }
    catch (MetadataException e) {
      // Expected.
    }
    catch (PropertyNotFoundException e) {
      // Expected.
    }

    ConfigFactory.getCommonConfig().setProperty("datacenter.url.secure", "https://foo.bar:8443/dsa/");
    assertEquals("https://foo.bar:8443/dsa/", MetadataHelper.getInstallationSecureBaseURL());

    ConfigFactory.getCommonConfig().setProperty("datacenter.url.secure", "https://foo.bar:8443/dsa");
    assertEquals("https://foo.bar:8443/dsa/", MetadataHelper.getInstallationSecureBaseURL());
  }

  public void testIsConeSearchSecure() throws Exception {
    assertFalse(MetadataHelper.isConeSearchSecure());

    ConfigFactory.getCommonConfig().setProperty("cone.search.secure", "true");
    assertTrue(MetadataHelper.isConeSearchSecure());
  }

}
