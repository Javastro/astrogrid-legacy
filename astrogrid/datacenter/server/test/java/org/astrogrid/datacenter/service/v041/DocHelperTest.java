package org.astrogrid.datacenter.service.v041;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.service.v041.DocHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * JUnit test case for DocHelperTest2
 */

public class DocHelperTest extends TestCase {
   //declare reusable objects to be used across multiple tests
   public DocHelperTest(String name) {
      super(name);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(DocHelperTest.class);
   }
   public static Test suite() {
      return new TestSuite(DocHelperTest.class);
   }
   protected void setUp() {
   //define reusable objects to be used across multiple tests
   }
   protected void tearDown() {
   //clean up after testing (if necessary)
   }

   /**
    * Tests bad wrap - should throw SAXException
    */
   public void testBadWrap()
   {
    try {
         DocHelper.wrap("<Buggle><Wuggle></Muggle>");

         fail("Should have failed with an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        // expected.
    }
   }

   /**
    * Test good wrap
    */
   public void testWrap() throws SAXException
   {
      String xmlSnippet = "<dummy />";
      Document wrapper = DocHelper.wrap(xmlSnippet);
      assertNotNull(wrapper);
      assertEquals("dummy",wrapper.getDocumentElement().getLocalName());
   }
}
