/*
 * $Id: DummyQuerierTest.java,v 1.5 2003/11/28 16:10:30 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.query.QueryStatus;
import org.w3c.dom.Document;

/**
 * Tests the dummy querier and resultset.
 * also uses the dummy querier to test behaviours common to all queriers.
 * @author M Hill
 */

public class DummyQuerierTest extends ServerTestCase
{
    public DummyQuerierTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception{
        super.setUp();
        querier = DummyQuerierSPI.createDummyQuerier();
        listener = new MockListener();
        querier.registerListener(listener);
    }
    protected void tearDown() throws Exception {
        super.tearDown();
        querier.close();
    }

    protected Querier querier;
    protected MockListener listener;


    public void testStatusBehaviour() {
        // just been created.
        assertEquals(QueryStatus.UNKNOWN,querier.getStatus());
        // check we can change to next step.
        querier.setStatus(QueryStatus.CONSTRUCTED);
        assertEquals(QueryStatus.CONSTRUCTED,querier.getStatus());
        listener.reset();
        // check we can jump some steps..
        querier.setStatus(QueryStatus.QUERY_COMPLETE);
        assertEquals(QueryStatus.QUERY_COMPLETE,querier.getStatus());
        listener.reset();
        //check that we can;t go back a step however
        try {
            querier.setStatus(QueryStatus.RUNNING_QUERY);
            fail("expected to barf");
        } catch (IllegalStateException e) {
            //expected
        }
        assertFalse(listener.heard);
    }    
    
    public void testErrorBehaviour() {
        // barfs on empty exception
        try {
            querier.getError();
            fail("expected to barf");
        } catch (IllegalStateException e) {
            //expected
        }
        assertEquals(QueryStatus.UNKNOWN,querier.getStatus());
        // now set an error
        Exception e = new Exception("blerghh");
        querier.setErrorStatus(e);
        assertEquals(QueryStatus.ERROR,querier.getStatus());
        listener.reset();
        assertEquals(e,querier.getError());
        // now check we can't alter status any further
        try {
            querier.setStatus(QueryStatus.FINISHED);
            fail("expected to barf");
        } catch (IllegalStateException ignored) {
            //expected
        } 
        assertFalse(listener.heard);
               
    }

   /**
    * Tests the dummy queriers and results
    */
   public void testDummyQuery() throws Exception
   {
      QueryResults results = null;

      //dummy one should accept a null
      results = querier.doQuery();
       assertEquals(QueryStatus.QUERY_COMPLETE,querier.getStatus());
       
       System.out.println(listener.statusList); // test on something here?       
       
      //and results should produce a valid xml document: this will parse it in
      //into a DOM tree
      Document doc = results.toVotable();
      assertNotNull("results.toVotable() returns null",doc);
      assertIsVotable(doc);

      //does a quick check for certain elements
      assertTrue(doc.getElementsByTagName("TR").getLength() > 5);

      //does a quick check for certain elements
       assertTrue("there are less than 5 'FIELD' tags in the VOtable, there should be more",doc.getElementsByTagName("FIELD").getLength() > 5);

   }


    class MockListener implements QuerierListener {
        List statusList = new ArrayList();
        public void queryStatusChanged(Querier querier) {            
            heard = true;
            statusList.add(querier.getStatus());
        }
        boolean heard ;
        public void reset() {
            assertTrue("didn;t receive notification",heard);
            heard = false;
        }
    }


    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(DummyQuerierTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }

}

