package org.astrogrid.datacenter.snippet;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.common.QueryHelper;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.DummyQuerierSPI;
import org.astrogrid.datacenter.queriers.DummyQueryResults;
import org.w3c.dom.Document;


/**
 * JUnit test case for DocHelperTest2
 */

public class ResponseHelperTest extends ServerTestCase
{
    public ResponseHelperTest(String name) {
        super(name);
    }
    
   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
   public static Test suite() {
      return new TestSuite(ResponseHelperTest.class);
   }

    protected Querier querier;

   /**
    * Make normal documents 
    */
   public void testQCResponse() throws Throwable {
      Document d = ResponseHelper.makeQueryCreatedResponse(querier);
      assertIsQueryCreatedResponse(d);
    }

    public void testQSReponse() throws Throwable {
      Document d = ResponseHelper.makeQueryStartedResponse(querier);
      assertIsQueryStartedResponse(d);
    }
    
    public void testSResponse() throws Throwable {
      Document d = ResponseHelper.makeStatusResponse(querier);
      assertIsStatusResponse(d);
    }
    public void testUIResponse() throws Exception {
      Document d = ResponseHelper.makeUnknownIdResponse("UnknownId");
      assertIsUnknownIdResponse(d);
    }
    public void testRResponse() throws Exception {
      
      DummyQueryResults results = new DummyQueryResults("ResponseHelperTest");
      Document d = ResponseHelper.makeResultsResponse(querier, results.toVotable().getDocumentElement());
      assertIsResultsResponse(d);      
   }

   /**
    * Tests that errors are thrown as exceptions
    */
   public void testErrorResponses() throws Throwable
   {
      querier.setError(new IOException("Test error"));

      try {
         ResponseHelper.makeQueryCreatedResponse(querier);
         fail("Didn't throw error exception");
      }
      catch (IOException e) {}

      try {
         ResponseHelper.makeQueryStartedResponse(querier);
         fail("Didn't throw error exception");
      }
      catch (IOException e) {}

      try {
         ResponseHelper.makeStatusResponse(querier);
         fail("Didn't throw error exception");
      }
      catch (IOException e) {}

      try {
         DummyQueryResults results = new DummyQueryResults("ResponseHelperTest");
         ResponseHelper.makeResultsResponse(querier, results.toVotable().getDocumentElement());
//isn't supposed to any more...         fail("Didn't throw error exception");
      }
      catch (IOException e) {}

   }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        querier = DummyQuerierSPI.createDummyQuerier();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
