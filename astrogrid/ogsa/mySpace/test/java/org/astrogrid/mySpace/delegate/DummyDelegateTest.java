/*$Id: DummyDelegateTest.java,v 1.1.1.1 2003/09/28 17:19:55 gtr Exp $
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
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceDummyDelegate;

/** Tests the dummy delegate
 * @author M Hill
 *
 */
public class DummyDelegateTest extends TestCase {

   private static final String USER = "Me";
   private static final String COMMUNITY ="Us";

   public void testNormalOperations() throws IOException
   {
      //create myspace connection
      MySpaceDummyDelegate myspace1 = new MySpaceDummyDelegate("DummyDelegateTest");

      //what happens if you try again - should just open connection to same place
      MySpaceDummyDelegate myspace2 = new MySpaceDummyDelegate("DummyDelegateTest");

      //create file in one
      myspace1.saveDataHolding(USER, COMMUNITY, "testSameness",
                               "This is just a test file for DummyDelegateTest",
                               "test",
                               myspace1.OVERWRITE);

      //see if you can get it
      URL url1 = myspace1.getDataHoldingUrl(USER, COMMUNITY, "testSameness");
      assertNotNull(url1);
      assertNotNull(url1.openStream());

      //in both
      URL url2 = myspace2.getDataHoldingUrl(USER, COMMUNITY, "testSameness");
      assertNotNull(url2);
      assertNotNull(url2.openStream());

      assertEquals(url1, url2);


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
      myspace.saveDataHolding(USER, COMMUNITY, "testFileOps",
                               "This should have been deleted",
                               "test",
                               myspace.OVERWRITE);

      URL url = myspace.getDataHoldingUrl(USER, COMMUNITY, "testFileOps");
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

      myspace.copyDataHolding(USER, COMMUNITY, filename1, filename2);

      url = myspace.getDataHoldingUrl(USER, COMMUNITY, filename1);
      assertNotNull(url);
      assertNotNull(url.openStream());

      url = myspace.getDataHoldingUrl(USER, COMMUNITY, filename2);
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
Revision 1.1.1.1  2003/09/28 17:19:55  gtr
A copy of the source from the mySpace component taken two days from the end
of iteration 3.  This copy was made in order to experiment with GridFTP
independently of the mainstream development.

Revision 1.1  2003/09/22 17:36:09  mch
tests for MySpaceDumnmyDelegate

*/

