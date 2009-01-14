package org.astrogrid.community.server;

import java.io.File;
import java.net.URI;
import java.net.URL;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;

/**
 * JUnit-3 tests for CommunityConfiguration.
 *
 * @author Guy Rixon
 */
public class CommunityConfigurationTest extends TestCase {

  public void testGetPublishingAuthorityPlainWithoutPath() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.ident",
                                            "foo.bar");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals("foo.bar", sut.getPublishingAuthority());
  }

  public void testGetPublishingAuthorityPlainWithPath() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.ident",
                                            "foo.bar/baz");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals("foo.bar", sut.getPublishingAuthority());
  }

  public void testGetPublishingAuthorityIvornWithPath() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.ident",
                                            "ivo://foo.bar/baz");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals("foo.bar", sut.getPublishingAuthority());
  }

  public void testGetPublishingAuthorityIvornWithoutPath() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.ident",
                                            "ivo://foo.bar");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals("foo.bar", sut.getPublishingAuthority());
  }

  public void testGetVosiBaseUrlHttp() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.vosi.baseurl",
                                            "http://foo.bar/baz");
    SimpleConfig.getSingleton().setProperty("org.astrogrid.vosi.baseurlsecure",
                                            "https://foo.bar/baz");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new URL("http://foo.bar/baz"), sut.getBaseUrlHttp());
  }

  public void testGetVosiBaseUrlHttps() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.vosi.baseurl",
                                            "http://foo.bar/baz");
    SimpleConfig.getSingleton().setProperty("org.astrogrid.vosi.baseurlsecure",
                                            "https://foo.bar/baz");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new URL("https://foo.bar/baz"), sut.getBaseUrlHttps());
  }

  public void testGetVoSpaceIvorn() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.default.vospace",
                                            "ivo://foo.bar/vospace");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new URI("ivo://foo.bar/vospace"), sut.getVoSpaceIvorn());
  }

  public void testGetRegistryQueryUrl() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.registry.query.endpoint",
                                            "http://foo.bar/registry/services/RegistryQuery");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new URL("http://foo.bar/registry/services/RegistryQuery"), sut.getRegistryQueryUrl());
  }

  public void testGetRegistryQueryAlternateUrl() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.registry.query.altendpoint",
                                            "http://foo.bar/registry/services/RegistryQuery");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new URL("http://foo.bar/registry/services/RegistryQuery"), sut.getRegistryQueryAlternateUrl());
  }

  public void testGetDatabaseConfigurationUrl() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.dbconfigurl",
                                            "http://foo.bar/community/db/config.blah");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new URL("http://foo.bar/community/db/config.blah"), sut.getDatabaseConfigurationUrl());
  }

  public void testGetCredentialDirectory() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.myproxy",
                                            "/astrogrid/community/foo/bar");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new File("/astrogrid/community/foo/bar"), sut.getCredentialDirectory());
  }

  public void testGetCaCertificateFile() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.cacert",
                                            "/astrogrid/community/ca/cert.pem");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new File("/astrogrid/community/ca/cert.pem"), sut.getCaCertificateFile());
  }

  public void testGetCaKeyFile() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.cakey",
                                            "/astrogrid/community/ca/key.pem");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new File("/astrogrid/community/ca/key.pem"), sut.getCaKeyFile());
  }

  public void testGetCaSerialNumberFile() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.caserial",
                                            "/astrogrid/community/ca/serial.txt");
    CommunityConfiguration sut = new CommunityConfiguration();
    assertEquals(new File("/astrogrid/community/ca/serial.txt"), sut.getCaSerialNumberFile());
  }

}
