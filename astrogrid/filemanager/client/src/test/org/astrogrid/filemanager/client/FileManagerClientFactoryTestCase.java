package org.astrogrid.filemanager.client;

import junit.framework.TestCase;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.store.Ivorn;

/**
 * JUnit tests for FileManagerClientFactory.
 *
 * @author Guy Rixon
 */
public class FileManagerClientFactoryTestCase extends TestCase {
  
  public void setUp() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.registry.query.endpoint",
                                            "http://foo/");
  }
  
  public void testLoginWithNoArguments() throws Exception {
    FileManagerClientFactory sut = new FileManagerClientFactory();
    FileManagerClient client = sut.login();
    System.out.println(client);
    assertTrue(client instanceof FileManagerClientImpl);
    FileManagerClientImpl clienti = (FileManagerClientImpl) client;
    assertNull(clienti.getHomeIvorn());
  }
  
  public void testLoginWithToken() throws Exception {
    SecurityToken token = new SecurityToken("ivo://frog@pond/community", null);
    FileManagerClientFactory sut = new FileManagerClientFactory();
    FileManagerClient client = sut.login(token);
    System.out.println(client);
    assertTrue(client instanceof FileManagerClientImpl);
    FileManagerClientImpl clienti = (FileManagerClientImpl) client;
    assertEquals("ivo://pond/frog#", clienti.getHomeIvorn().toString());
  }
  
  public void testLoginWithPassword() throws Exception {
    Ivorn account = new Ivorn("ivo://frog@pond/community");
    FileManagerClientFactory sut = new FileManagerClientFactory();
    FileManagerClient client = sut.login(account, "croakcroak");
    System.out.println(client);
    assertTrue(client instanceof FileManagerClientImpl);
    FileManagerClientImpl clienti = (FileManagerClientImpl) client;
    assertEquals("ivo://pond/frog#", clienti.getHomeIvorn().toString());
  }
}
