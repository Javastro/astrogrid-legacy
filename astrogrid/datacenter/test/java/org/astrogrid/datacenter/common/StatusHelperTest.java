package org.astrogrid.datacenter.common;

import junit.framework.*;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * JUnit test case for StatusHelperTest
 */

public class StatusHelperTest extends TestCase {
   //declare reusable objects to be used across multiple tests
   public StatusHelperTest(String name) {
      super(name);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(StatusHelperTest.class);
   }
   public static Test suite() {
      return new TestSuite(StatusHelperTest.class);
   }
   protected void setUp() {
   //define reusable objects to be used across multiple tests
   }
   protected void tearDown() {
   //clean up after testing (if necessary)
   }
   public void testMakeStatusTag()  throws Exception{

      String result = StatusHelper.makeStatusTag("foo", ServiceStatus.STARTING);
        Document doc = DocHelper.wrap(result); // just test its correct XML.
        assertNotNull(doc);


   }
   public void testMakeJobNotificationTag() throws Exception {

      String result  = StatusHelper.makeJobNotificationTag("foo","bar");
        Document doc = DocHelper.wrap(result);
        assertNotNull(doc);
   }

    /** @todo - as far as I can see this test is correct - the implementation of GetServiceStatus needs fixing */
   public void testGetServiceStatus() throws Exception {

      String src = StatusHelper.makeStatusTag("foo",ServiceStatus.RUNNING_QUERY);
        System.out.println(src);
        Document doc = DocHelper.wrap("<bar>"+src+"</bar>");
        assertNotNull(doc);
      String result = StatusHelper.getServiceStatus("foo", doc.getDocumentElement());
        assertNotNull(result);
        assertEquals("working",result);

   }
}
