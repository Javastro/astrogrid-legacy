package org.astrogrid.datacenter.snippet;
import org.astrogrid.datacenter.service.v041.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;

/**
 * JUnit test case for QueryIdHelperTest
 */

public class QueryIdHelperTest extends TestCase {
   //declare reusable objects to be used across multiple tests
   public QueryIdHelperTest(String name) {
      super(name);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(QueryIdHelperTest.class);
   }
   public static Test suite() {
      return new TestSuite(QueryIdHelperTest.class);
   }
   protected void setUp() {
   //define reusable objects to be used across multiple tests
   }
   protected void tearDown() {
   //clean up after testing (if necessary)
   }
   public void testMakeQueryIdTag() throws Exception {

      String result = QueryIdHelper.makeQueryIdTag("noel");
        Document doc = DocHelper.wrap(result);
        assertNotNull(doc);
   }

   public void testMakeTagWithQueryIdAttr() throws Exception {

      String result = QueryIdHelper.makeTagWithQueryIdAttr("foo","bar") + "</foo>";
        Document doc = DocHelper.wrap(result);
        assertNotNull(doc);

   }
    /** Tests that service id un/marshalling works */
    public void testGetQueryId() throws Exception {

        String src = QueryIdHelper.makeQueryIdTag("noel");
        Document doc = DocHelper.wrap("<foo>"+src+"</foo>");
        assertNotNull(doc);
        String result = QueryIdHelper.getQueryId(doc.getDocumentElement());
        assertNotNull(result);
        assertEquals("noel",result);
    }
    /** tests that query id is returned when just query id element is passed in.
     * (this fails at present, but behaviour is required by current delegate
     * <br>Really don't like this xml-mangling way of programming - unsurpsingly its very tricky & buggy.
     * @throws Exception
     */
    public void testGetQueryIdFromRootElement() throws Exception {
        String src = QueryIdHelper.makeQueryIdTag("noel");
        Document doc = DocHelper.wrap(src);
        assertNotNull(doc);
        String result = QueryIdHelper.getQueryId(doc.getDocumentElement());
        assertNotNull(result);
        assertEquals("noel",result);
    }
}
