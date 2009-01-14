package org.astrogrid.community.server.backup;

import java.io.File;
import junit.framework.TestCase;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.sso.PondLifeDb;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

/**
 * JUnit tests for BackUpServlet.
 *
 * @author Guy Rixon
 */
public class BackUpServletTest extends TestCase {
  
  private ServletTester tester;

  @Override
  protected void setUp() throws Exception {
    PondLifeDb pond = new PondLifeDb();
    pond.initialize();
    
    File here = new File(".");
    new CommunityConfiguration().setCredentialDirectory(here);
    
    // Start the embedded Jetty server with the SUT in its context.
    // The servlet is bound to the path /community/accounts/*.
    tester = new ServletTester();
    tester.setContextPath("/community");
    tester.addServlet(BackUpServlet.class, "/admin/backup");;
    tester.start();
  }
  
  public void testGet() throws Exception {
    HttpTester request = new HttpTester();
    HttpTester response = new HttpTester();
    request.setMethod("GET");
    request.setHeader("Host", "tester");
    request.setVersion("HTTP/1.0");
    request.setURI("/community/admin/backup");
    request.addHeader("Content-Type", "application/x-www-form-urlencoded");
    response.parse(tester.getResponses(request.generate()));
    assertEquals(200, response.getStatus());
  }
  
  
}
