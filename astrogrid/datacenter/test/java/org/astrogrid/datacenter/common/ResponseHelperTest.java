package org.astrogrid.datacenter.common;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.queriers.DummyQuerier;
import org.astrogrid.datacenter.queriers.DummyQueryResults;

/**
 * JUnit test case for DocHelperTest2
 */

public class ResponseHelperTest extends TestCase
{
   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
   public static Test suite() {
      return new TestSuite(ResponseHelperTest.class);
   }

   /**
    * Make normal documents
    * @todo test that created documents are valid (coverage only at the moment)
    */
   public void testNormalResponses() throws Throwable
   {
      DummyQuerier querier = new DummyQuerier();

      ResponseHelper.makeStartQueryResponse(querier);

      ResponseHelper.makeStatusResponse(querier);

      ResponseHelper.makeUnknownIdResponse("UnknownId");

      DummyQueryResults results = new DummyQueryResults("ResponseHelperTest");
      ResponseHelper.makeResultsResponse(querier, results.toVotable().getDocumentElement());
   }

   /**
    * Tests that errors are thrown as exceptions
    */
   public void testErrorResponses() throws Throwable
   {
      DummyQuerier querier = new DummyQuerier();

      querier.setErrorStatus(new IOException("Test error"));

      try {
         ResponseHelper.makeStartQueryResponse(querier);
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
         fail("Didn't throw error exception");
      }
      catch (IOException e) {}

   }
}
