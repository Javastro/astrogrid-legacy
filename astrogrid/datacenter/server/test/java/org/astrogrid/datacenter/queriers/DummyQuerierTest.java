/*
 * $Id: DummyQuerierTest.java,v 1.4 2003/11/27 17:28:09 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.axisdataserver.types.QueryHelper;
import org.w3c.dom.Document;

/**
 * Tests the dummy querier and resultset.
 *
 * @author M Hill
 */

public class DummyQuerierTest extends ServerTestCase
{
    public DummyQuerierTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception{
        super.setUp();
    }
    protected void tearDown() throws Exception {
        super.tearDown();
    }

   /**
    * Tests the dummy queriers and results
    */
   public void testDummies() throws Exception
   {
      Querier querier = DummyQuerierSPI.createDummyQuerier();
      QueryResults results = null;

      //dummy one should accept a null
      results = querier.doQuery();

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

