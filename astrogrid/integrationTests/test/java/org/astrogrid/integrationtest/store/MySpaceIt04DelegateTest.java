/*$Id: MySpaceIt04DelegateTest.java,v 1.1 2004/03/22 20:02:18 mch Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.integrationtest.store;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.myspace.MySpaceIt04Delegate;

/**
 * This tests the myspace It4 store client against a real myspcae
 *
 * @author M Hill
 *
 */
public class MySpaceIt04DelegateTest extends StoreClientTestHelper {

   
   private static final String TESTSTORE = "myspace:http://vm05.astrogrid.org:8080/astrogrid-mySpace/services/MySpaceManager";

   private static final String TESTSTORE2 ="myspace:http://grendel12.roe.ac.uk:8080/astrogrid-mySpace/services/MySpaceManager";

   
   public void testStoreAccess() throws IOException
   {
      assertStoreAccess(new MySpaceIt04Delegate(User.ANONYMOUS, TESTSTORE),
                        new MySpaceIt04Delegate(User.ANONYMOUS, TESTSTORE),
                        new MySpaceIt04Delegate(User.ANONYMOUS, TESTSTORE2));
   }

   /** Tests getFile etc - if these are failing the rest of the tests might
    * not make much sense.  This is a bit of a combined one anyway... */
   public void testGetFile() throws IOException
   {
      assertGetFileWorks(new MySpaceIt04Delegate(User.ANONYMOUS, (TESTSTORE)));
   }

   /** Tests making folders and paths and stuff.  A bit
    */
   public void testFolders() throws IOException {
      assertFoldersWork(new MySpaceIt04Delegate(User.ANONYMOUS, (TESTSTORE)));
   }
   
   
   public void testMove() throws IOException
   {
      //test move on same server
      assertMove(new MySpaceIt04Delegate(User.ANONYMOUS, (TESTSTORE)), new Agsl(TESTSTORE, MOVE_TEST));
      
      //test move between servers
      assertMove(new MySpaceIt04Delegate(User.ANONYMOUS, (TESTSTORE)), new Agsl(TESTSTORE2, MOVE_TEST));
   }

   public void testCopy() throws IOException
   {
      //test move on same server
      assertCopy(new MySpaceIt04Delegate(User.ANONYMOUS, (TESTSTORE)), new Agsl(TESTSTORE, COPY_TEST));
      
      //test move between servers
      assertCopy(new MySpaceIt04Delegate(User.ANONYMOUS, (TESTSTORE)), new Agsl(TESTSTORE2, COPY_TEST));
   }
   
   
   public void testDelete() throws IOException {
      assertDelete(new MySpaceIt04Delegate(User.ANONYMOUS, (TESTSTORE)));
   }
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(MySpaceIt04DelegateTest.class);
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
$Log: MySpaceIt04DelegateTest.java,v $
Revision 1.1  2004/03/22 20:02:18  mch
Moved to right package

Revision 1.5  2004/03/14 13:23:14  mch
Renamed StoreClientTestHelper so that it doesn't get used directly for testing

Revision 1.4  2004/03/08 13:46:25  mch
Fixed imports

Revision 1.3  2004/03/02 01:25:39  mch
Minor fixes

Revision 1.2  2004/03/01 23:44:10  mch
Factored out common myspace tests

Revision 1.1  2004/03/01 22:35:09  mch
Tests for StoreClient

Revision 1.4  2004/03/01 16:51:10  mch
Removed test for equality between local filespaces

Revision 1.3  2004/01/23 15:22:27  jdt
added logging

Revision 1.2  2003/12/03 17:39:25  mch
New factory/interface based store delegates

Revision 1.1  2003/09/22 17:36:09  mch
tests for storeDumnmyDelegate

*/

