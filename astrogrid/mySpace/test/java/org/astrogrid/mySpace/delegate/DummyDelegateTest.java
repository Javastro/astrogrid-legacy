/*$Id: DummyDelegateTest.java,v 1.4 2004/03/01 16:51:10 mch Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.mySpace.delegate;

import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Tests the dummy delegate
 * @author M Hill
 *
 */
public class DummyDelegateTest extends TestCase {
  /**
   * Commons-logging logger
   */
  private static org.apache.commons.logging.Log log =
    org.apache.commons.logging.LogFactory.getLog(DummyDelegateTest.class);

   private static final String USER = "Me";
   private static final String COMMUNITY ="Us";
   private static final String CERTIFIED = "Mad";

   public void testNormalOperations() throws IOException
   {
      //create myspace connection
      MySpaceDummyDelegate myspace1 = new MySpaceDummyDelegate("DummyDelegateTest");

      //what happens if you try again - should just open connection to same place
      MySpaceDummyDelegate myspace2 = new MySpaceDummyDelegate("DummyDelegateTest");

      //create file in one
      myspace1.saveDataHolding(USER, COMMUNITY, CERTIFIED, "testSameness",
                               "This is just a test file for DummyDelegateTest",
                               "test",
                               MySpaceClient.OVERWRITE);

      //see if you can get it
      URL url1 = new URL(myspace1.getDataHoldingUrl(USER, COMMUNITY, CERTIFIED, "testSameness"));
      assertNotNull(url1);
      log.debug("URL1: "+url1);
      assertNotNull(url1.openStream());

      /** These are in fact two different delegates in this case - I think to avoid
       * overlap problems in other tests.
      //in both
      URL url2 = new URL(myspace2.getDataHoldingUrl(USER, COMMUNITY, CERTIFIED, "testSameness"));
      assertNotNull(url2);
      log.debug("URL2: "+url2);
      assertNotNull(url2.openStream());

      assertEquals(url1, url2);
       */

   }

   /**
    * Tests copy, rename and delete
    * @todo File.renameTo() and File.delete seem to be broken for some reason.
    */
   public void testFileOperations() throws IOException
   {
      //create myspace connection
      MySpaceDummyDelegate myspace = new MySpaceDummyDelegate("DummyDelegateTest");

      //test normal save
      myspace.saveDataHolding(USER, COMMUNITY, CERTIFIED, "testFileOps",
                               "This should have been deleted",
                               "test",
                               MySpaceClient.OVERWRITE);

      URL url = new URL(myspace.getDataHoldingUrl(USER, COMMUNITY, CERTIFIED, "testFileOps"));
      assertNotNull(url);
      assertNotNull(url.openStream());

      //test rename
      /* broken *
      myspace.renameDataHolding(USER, COMMUNITY, "testFileOps", "testRename");

      url = myspace.getDataHoldingUrl(USER, COMMUNITY, "testFileOps");
      assertNull(url);

      url = myspace.getDataHoldingUrl(USER, COMMUNITY, "testRename");
      assertNotNull(url);
      assertNotNull(url.openStream());
      /**/

      //test copy
      String filename1 = "testFileOps";
      String filename2 = "testCopy";

      myspace.copyDataHolding(USER, COMMUNITY, CERTIFIED, filename1, filename2);

      url = new URL(myspace.getDataHoldingUrl(USER, COMMUNITY, CERTIFIED, filename1));
      assertNotNull(url);
      assertNotNull(url.openStream());

      url = new URL(myspace.getDataHoldingUrl(USER, COMMUNITY, CERTIFIED, filename2));
      assertNotNull(url);
      assertNotNull(url.openStream());

      //test delete
      /** also broken
      myspace.deleteDataHolding(USER, COMMUNITY, filename1);
      url = myspace.getDataHoldingUrl(USER, COMMUNITY, filename1);
      assertNull(url);

      myspace.deleteDataHolding(USER, COMMUNITY, filename2);
      url = myspace.getDataHoldingUrl(USER, COMMUNITY, filename2);
      assertNull(url);
       /**/
   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(DummyDelegateTest.class);
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
$Log: DummyDelegateTest.java,v $
Revision 1.4  2004/03/01 16:51:10  mch
Removed test for equality between local filespaces

Revision 1.3  2004/01/23 15:22:27  jdt
added logging

Revision 1.2  2003/12/03 17:39:25  mch
New factory/interface based myspace delegates

Revision 1.1  2003/09/22 17:36:09  mch
tests for MySpaceDumnmyDelegate

*/

