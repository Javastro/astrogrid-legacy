package org.astrogrid.datacenter.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;

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

      String result = StatusHelper.makeStatusTag("foo", QueryStatus.STARTING);
        Document doc = DocHelper.wrap(result); // just test its correct XML.
        assertNotNull(doc);


   }
   public void testMakeJobNotificationTag() throws Exception {

      String result  = StatusHelper.makeJobNotificationTag("foo","bar");
        Document doc = DocHelper.wrap(result);
        assertNotNull(doc);
   }

    /** Tests that service status un/marshalling works */
   public void testGetServiceStatus() throws Exception {

      String src = StatusHelper.makeStatusTag("foo",QueryStatus.RUNNING_QUERY);
        Document doc = DocHelper.wrap("<bar>"+src+"</bar>"); //also tests that src is valid XML
        assertNotNull(doc);
      QueryStatus result = StatusHelper.getServiceStatus("foo", doc.getDocumentElement());
        assertNotNull(result);
        assertEquals(result, QueryStatus.RUNNING_QUERY);

   }
   
   /** test the getServiceStatus method works in the case when the required element is the root one */
   public void testGetServiceStatusFromRootElement() throws Exception {
       String src = StatusHelper.makeStatusTag("foo",QueryStatus.RUNNING_QUERY);
       Document doc = DocHelper.wrap(src);
       assertNotNull(doc);
       QueryStatus result = StatusHelper.getServiceStatus("foo",doc.getDocumentElement());
       assertNotNull(result);
       assertEquals(result,QueryStatus.RUNNING_QUERY);
       
   }
}
