/*
 * $Id: QuerierTest.java,v 1.8 2003/09/24 21:10:24 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;

/**
 * Tests the dummy querier and resultset.
 *
 * @author M Hill
 */

public class QuerierTest extends TestCase
{

   /**
    * Tests the dummy queriers and results
    */
   public void testDummies() throws IOException, DatabaseAccessException
   {
      DatabaseQuerier querier = new DummyQuerier();
      QueryResults results = null;

      //dummy one should accept a null
      results = querier.queryDatabase(null);

      //and results should produce a valid xml document: this will parse it in
      //into a DOM tree
      Document doc = results.toVotable();
      assertNotNull("results.toVotable() returns null",doc);

      //this gives a visual test but isn't much use for automated testers
//      System.out.println("...results:");
//      XMLUtils.DocumentToStream(doc, System.out);

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
        return new TestSuite(QuerierTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }

}

