/*
 * $Id: TestDummies.java,v 1.2 2003/08/27 16:34:15 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Document;
import org.apache.axis.utils.XMLUtils;

/**
 * Tests the dummy querier and resultset.
 *
 * @author M Hill
 */

public class TestDummies extends TestCase
{

   /**
    * Tests the dummy queriers and results
    */
   public void testDummies() throws IOException, DatabaseAccessException
   {
      System.out.println("Testing Dummy queriers...");

      DatabaseQuerier querier = new DummyQuerier();
      QueryResults results = null;

      //dummy one should accept a null
      results = querier.queryDatabase(null);

      //and results should produce a valid xml document: this will parse it in
      //into a DOM tree
      Document doc = results.toVotable();
      if ( doc == null)
      {
         throw new RuntimeException("results.toVotable() returns null");
      }

      //this gives a visual test but isn't much use for automated testers
//      System.out.println("...results:");
//      XMLUtils.DocumentToStream(doc, System.out);

      //does a quick check for certain elements
      if (doc.getElementsByTagName("TR").getLength()<5)
      {
         throw new RuntimeException("there are less than 5 rows in the VOtable, there should be more");
      }

      //does a quick check for certain elements
      if (doc.getElementsByTagName("FIELD").getLength()<5)
      {
         throw new RuntimeException("there are less than 5 'FIELD' tags in the VOtable, there should be more");
      }

      System.out.println("...dummy querier test finished");

   }


    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(TestDummies.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }

}

