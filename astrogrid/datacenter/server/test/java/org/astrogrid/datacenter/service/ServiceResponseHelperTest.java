package org.astrogrid.datacenter.service;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.queriers.DummyQuerier;
import org.astrogrid.datacenter.queriers.DummyQueryResults;


/**
 * JUnit test case for DocHelperTest2
 */

public class ServiceResponseHelperTest extends TestCase
{
   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
   public static Test suite() {
      return new TestSuite(ServiceResponseHelperTest.class);
   }

   /**
    * Make normal documents
    * @todo test that created documents are valid (coverage only at the moment)
    */
   public void testNormalResponses() throws Throwable
   {
      DummyQuerier querier = new DummyQuerier();

      ServiceResponseHelper.makeQueryCreatedResponse(querier);

      ServiceResponseHelper.makeQueryStartedResponse(querier);

      ServiceResponseHelper.makeStatusResponse(querier);

      ServiceResponseHelper.makeUnknownIdResponse("UnknownId");

      DummyQueryResults results = new DummyQueryResults("ResponseHelperTest");
      ServiceResponseHelper.makeResultsResponse(querier, results.toVotable().getDocumentElement());
   }

   /**
    * Tests that errors are thrown as exceptions
    */
   public void testErrorResponses() throws Throwable
   {
      DummyQuerier querier = new DummyQuerier();

      querier.setErrorStatus(new IOException("Test error"));

      try {
         ServiceResponseHelper.makeQueryCreatedResponse(querier);
         fail("Didn't throw error exception");
      }
      catch (IOException e) {}

      try {
         ServiceResponseHelper.makeQueryStartedResponse(querier);
         fail("Didn't throw error exception");
      }
      catch (IOException e) {}

      try {
         ServiceResponseHelper.makeStatusResponse(querier);
         fail("Didn't throw error exception");
      }
      catch (IOException e) {}

      try {
         DummyQueryResults results = new DummyQueryResults("ResponseHelperTest");
         ServiceResponseHelper.makeResultsResponse(querier, results.toVotable().getDocumentElement());
//isn't supposed to any more...         fail("Didn't throw error exception");
      }
      catch (IOException e) {}

   }
}
