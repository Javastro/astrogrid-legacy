package org.astrogrid.datacenter.service.v041;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.datacenter.service.v041.DocHelper;
import org.astrogrid.datacenter.service.v041.StatusHelper;
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

      String result = StatusHelper.makeStatusTag("foo", QueryState.STARTING);
        Document doc = DocHelper.wrap(result); // just test its correct XML.
        assertNotNull(doc);


   }
   
   /** @todo  can't run this at the mo as we need to create a proper querier
   */
   public void testMakeJobNotificationTag() throws Exception {
    /*
      String result  = StatusHelper.makeJobNotificationTag(DatabaseQuerier.createQuerier(null));
        Document doc = DocHelper.wrap(result);
        assertNotNull(doc);
        */
   }
    
   
    /** Tests that service status un/marshalling works */
   public void testGetServiceStatus() throws Exception {

      String src = StatusHelper.makeStatusTag("foo",QueryState.RUNNING_QUERY);
        Document doc = DocHelper.wrap("<bar>"+src+"</bar>"); //also tests that src is valid XML
        assertNotNull(doc);
      QueryState result = StatusHelper.getServiceStatus("foo", doc.getDocumentElement());
        assertNotNull(result);
        assertEquals(result, QueryState.RUNNING_QUERY);

   }
   
   /** test the getServiceStatus method works in the case when the required element is the root one */
   public void testGetServiceStatusFromRootElement() throws Exception {
       String src = StatusHelper.makeStatusTag("foo",QueryState.RUNNING_QUERY);
       Document doc = DocHelper.wrap(src);
       assertNotNull(doc);
       QueryState result = StatusHelper.getServiceStatus("foo",doc.getDocumentElement());
       assertNotNull(result);
       assertEquals(result,QueryState.RUNNING_QUERY);
       
   }
}
