/*
 * $Id TestWorkspace.java $
 *
 */

package org.astrogrid.datacenter.service;


/**
 * Unit tests for the Server
 *
 * @author M Hill
 */

import java.io.File;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestServer extends TestCase
{

   /**
    * Tests the query service by itself
    *
   public void testQueryService()
   {
   }
    */

   /**
    * Tests the data server as if this was an axis server
    *
   public void testDataServer()
   {
   }
    */

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(TestServer.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }


}

/*
$Log: TestServer.java,v $
Revision 1.1  2003/08/28 13:21:20  mch
Added service test


*/
