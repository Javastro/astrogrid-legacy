package org.astrogrid.datacenter.common;

import junit.framework.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * JUnit test case for ServiceIdHelperTest
 */

public class ServiceIdHelperTest extends TestCase {
   //declare reusable objects to be used across multiple tests
   public ServiceIdHelperTest(String name) {
      super(name);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(ServiceIdHelperTest.class);
   }
   public static Test suite() {
      return new TestSuite(ServiceIdHelperTest.class);
   }
   protected void setUp() {
   //define reusable objects to be used across multiple tests
   }
   protected void tearDown() {
   //clean up after testing (if necessary)
   }
   public void testMakeServiceIdTag() throws Exception {

      String result = ServiceIdHelper.makeServiceIdTag("noel");
        Document doc = DocHelper.wrap(result);
        assertNotNull(doc);
   }

   public void testMakeTagWithServiceIdAttr() throws Exception {

      String result = ServiceIdHelper.makeTagWithServiceIdAttr("foo","bar") + "</foo>";
        Document doc = DocHelper.wrap(result);
        assertNotNull(doc);

   }
    /** Tests that service id un/marshalling works */
    public void testGetServiceId() throws Exception {

        String src = ServiceIdHelper.makeServiceIdTag("noel");
        Document doc = DocHelper.wrap("<foo>"+src+"</foo>");
        assertNotNull(doc);
        String result = ServiceIdHelper.getServiceId(doc.getDocumentElement());
        assertNotNull(result);
        assertEquals("noel",result);
    }
}
