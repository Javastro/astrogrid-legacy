package org.astrogrid.dataservice.service.vosi;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

/**
 * JUnit-4 tests for {@link org.astrogrid.dataservice.service.vosi.CapabilityServlet}.
 * <p>
 * Each test starts a new {@code ServletTester}.
 * 
 * @author Guy Rixon
 */
public class CapabilityServletTest {
  
  private ServletTester tester = null;

  /**
   * Starts the sample-stars plug-in. This defines the available catalogue
   * and tables.
   *
   * @throws Exception If the pug-in cannot operate.
   */
  @Before
  public void startPlugin() throws Exception {
    SampleStarsPlugin.initialise();
  }

  /**
   * Shuts down the servlet after a test.
   */
  @After
  public void endServlet() throws Exception {
    if (tester != null) {
      tester.stop();
    }
  }


  @Test
  public void testUnsecured() throws Exception {
    SimpleConfig.setProperty("cone.search.access.policy", "org.astrogrid.security.authorization.OpenAccessPolicy");
    SimpleConfig.setProperty("datacenter.url",  "http://some.where/dsa");
    SimpleConfig.setProperty("datacenter.url.secure",  "https://some.where/dsa");
    SimpleConfig.setProperty("cone.search.secure", "false");
    startServlet();
    
    XmlTester response = getCapabilities();
    response.validateWithW3cSchema(this.getClass().getResource("Capabilities.xsd"));

    XTruth x1 = new XTruth("//capability[@standardID='ivo://ivoa.net/std/TAP']/interface/accessURL",
                           response.getXmlContent(),
                           new Namespaces());
    assertTrue(x1.exists());
    assertEquals(1, x1.count());
    assertEquals("http://some.where/dsa/TAP", x1.getValue());

    XTruth x3 = new XTruth("//capability[@standardID='ivo://ivoa.net/std/TAP']/interface/securityMethod",
                           response.getXmlContent(),
                           new Namespaces());
    assertFalse(x3.exists());

    XTruth x2 = new XTruth("//capability[@standardID='ivo://ivoa.net/std/ConeSearch']/interface/accessURL",
                           response.getXmlContent(),
                           new Namespaces());
    assertTrue(x2.exists());
    assertEquals(2, x2.count());
    assertEquals("http://some.where/dsa/SubmitCone?DSACAT=CatName_SampleStarsCat&DSATAB=TabName_SampleStars&", x2.getValue(0));
    assertEquals("http://some.where/dsa/SubmitCone?DSACAT=CatName_SampleStarsCat&DSATAB=TabName_SampleStars2&", x2.getValue(1));

    XTruth x4 = new XTruth("//capability[@standardID='ivo://ivoa.net/std/ConeSearch']/interface/securityMethod",
                           response.getXmlContent(),
                           new Namespaces());
    assertFalse(x4.exists());

    XTruth x5 = new XTruth("//capability[@standardID='ivo://org.astrogrid/std/CEA/v1.0']/interface/accessURL",
                           response.getXmlContent(),
                           new Namespaces());
    assertTrue(x5.exists());
    assertEquals(1, x5.count());
    assertEquals("http://some.where/dsa/services/CommonExecutionConnectorService", x5.getValue());

    XTruth x6 = new XTruth("//capability[@standardID='ivo://ivoa.net/std/TAP']/interface/securityMethod",
                           response.getXmlContent(),
                           new Namespaces());
    assertFalse(x6.exists());
  }

  @Test
  public void testAllSecured() throws Exception {
    SimpleConfig.setProperty("cone.search.access.policy", "org.astrogrid.security.authorization.AuthenticatedAccessPolicy");
    SimpleConfig.setProperty("cea.access.policy", "org.astrogrid.security.authorization.AuthenticatedAccessPolicy");
    SimpleConfig.setProperty("tap.access.policy", "org.astrogrid.security.authorization.AuthenticatedAccessPolicy");
    SimpleConfig.setProperty("datacenter.url",  "http://some.where/dsa");
    SimpleConfig.setProperty("datacenter.url.secure",  "https://some.where/dsa");
    SimpleConfig.setProperty("cone.search.secure", "true");
    SimpleConfig.setProperty("tap.secure", "true");
    SimpleConfig.setProperty("cea.secure", "true");
    startServlet();

    XmlTester response = getCapabilities();
    response.validateWithW3cSchema(this.getClass().getResource("Capabilities.xsd"));

    XTruth x1 = new XTruth("//capability[@standardID='ivo://ivoa.net/std/TAP']/interface/accessURL",
                           response.getXmlContent(),
                           new Namespaces());
    assertTrue(x1.exists());
    assertEquals(1, x1.count());
    assertEquals("https://some.where/dsa/TAP", x1.getValue());

    XTruth x3 = new XTruth("//capability[@standardID='ivo://ivoa.net/std/TAP']/interface/securityMethod/@standardID",
                           response.getXmlContent(),
                           new Namespaces());
    assertTrue(x3.exists());
    assertEquals(1, x3.count());
    assertEquals("ivo://ivoa.net/sso#tls-with-client-certificate", x3.getValue());
    
    XTruth x2 = new XTruth("//capability[@standardID='ivo://ivoa.net/std/ConeSearch']/interface/accessURL",
                           response.getXmlContent(),
                           new Namespaces());
    assertTrue(x2.exists());
    assertEquals(2, x2.count());
    assertEquals("https://some.where/dsa/SubmitCone?DSACAT=CatName_SampleStarsCat&DSATAB=TabName_SampleStars&", x2.getValue(0));
    assertEquals("https://some.where/dsa/SubmitCone?DSACAT=CatName_SampleStarsCat&DSATAB=TabName_SampleStars2&", x2.getValue(1));

    XTruth x4 = new XTruth("//capability[@standardID='ivo://ivoa.net/std/ConeSearch']/interface/securityMethod/@standardID",
                           response.getXmlContent(),
                           new Namespaces());
    assertTrue(x4.exists());
    assertEquals(2, x4.count());
    assertEquals("ivo://ivoa.net/sso#tls-with-client-certificate", x4.getValue(0));
    assertEquals("ivo://ivoa.net/sso#tls-with-client-certificate", x4.getValue(1));

    XTruth x5 = new XTruth("//capability[@standardID='ivo://org.astrogrid/std/CEA/v1.0']/interface/accessURL",
                           response.getXmlContent(),
                           new Namespaces());
    assertTrue(x5.exists());
    assertEquals(1, x5.count());
    assertEquals("http://some.where/dsa/services/CommonExecutionConnectorService", x5.getValue());

    XTruth x6 = new XTruth("//capability[@standardID='ivo://org.astrogrid/std/CEA/v1.0']/interface/securityMethod/@standardID",
                           response.getXmlContent(),
                           new Namespaces());
    assertEquals(1, x6.count());
    assertEquals("ivo://ivoa.net/sso#soap-digital-signature", x6.getValue());
  }




  /**
   * Requests the capabilities from the servlet. Validate the HTTP details of
   * the response using JUnit assertions, then returns the response
   * for further validation.
   *
   * @return The response to the HTTP request.
   */
  private XmlTester getCapabilities() throws Exception {
    // Form the request. The query parameters are set in the URI header.
    HttpTester request = new HttpTester();
    request.setMethod("GET");
    request.setHeader("Host", "tester");
    request.setURI("http://tester/vosi/capabilities");
    request.setVersion("HTTP/1.1");

    // Send the query and parse the HTTP response
    // (the VOTable is not parsed at this stage).
    XmlTester response = new XmlTester();
    String raw = tester.getResponses(request.generate());
    System.out.println(raw);
    response.parse(raw);

    // Validate the HTTP response.
    assertEquals(200, response.getStatus());
    assertTrue(response.getContentType().equals("text/xml") ||
               response.getContentType().startsWith("text/xml;"));
    String content = response.getContent();
    assertNotNull(content);
    assertTrue(content.length() > 0);

    return response;
  }


  /**
   * Creates and starts a new {@code ServletTester}. Leaves a reference to
   * the tester in the instance variable {@link #tester}.
   *
   * @throws Exception If the servlet fails to start.
   */
  private void startServlet() throws Exception {
    tester = new ServletTester();
    tester.setContextPath("/vosi");
    tester.addServlet("org.astrogrid.dataservice.service.vosi.CapabilityServlet", "/capabilities");
    tester.start();
  }



}
