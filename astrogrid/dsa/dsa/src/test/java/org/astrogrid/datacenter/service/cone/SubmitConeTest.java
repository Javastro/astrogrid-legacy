package astrogrid.datacenter.service.cone;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

/**
 * JUnit-4 tests for {@link org.astrogrid.dataservice.service.cone.SubmitCone}.
 *
 * @author Guy Rixon
 */
public final class SubmitConeTest {

  private ServletTester tester = null;
  
  @Before
  public void start() throws Exception {
    tester = new ServletTester();
    tester.setContextPath("/context");
    tester.addServlet("org.astrogrid.dataservice.service.cone.SubmitCone", "/cone");
    tester.start();
  }

  @After
  public void end() throws Exception {
    if (tester != null) {
      tester.stop();
    }
  }

  @Test
  public void testAll() throws Exception {

    // Form the request. The query parameters are set in the URI header.
    HttpTester request = new HttpTester();
    request.setMethod("GET");
    request.setHeader("Host","tester");
    request.setURI("http://tester/context/cone?RA=0&DEC=0&SR=1");
    request.setVersion("HTTP/1.1");

    // Send the query and parse the HTTP response
    // (the VOTable is not parsed at this stage).
    HttpTester response = new HttpTester();
    String raw = tester.getResponses(request.generate());
    System.out.println(raw);
    response.parse(raw);

    // Validate the HTTP response.
    assertEquals( 200, response.getStatus());
    assertTrue(response.getContentType().equals("text/xml") ||
               response.getContentType().startsWith("text/xml;"));
    String content = response.getContent();
    assertNotNull(content);
    assertTrue(content.length() > 0);
  }

}
